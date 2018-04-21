package cn.hurrican.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;


public class JedisExecutor implements RedisExecutor {

    private static Logger logger = LogManager.getLogger(JedisExecutor.class);

    private JedisPool jedisPool;

    public JedisExecutor() {
    }

    public JedisExecutor(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private Jedis getJedis(JedisPool jedisPool) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
        } catch (JedisException e) {
            logger.error("从连接池中拿Jedis连接时发生异常", e);
            throw e;
        }
        if (resource == null) {
            logger.error("从连接池中拿Jedis连接为空");
            throw new IllegalStateException("从连接池中拿Jedis连接为空");
        }
        return resource;
    }

    private void releaseResource(JedisPool jedisPool, Jedis resource, boolean broken) {
        if (jedisPool == null) {
            return;
        }
        resource.close();
    }

    @Override
    public void doInRedis(JedisRunnable runnable) {
        if (runnable == null) {
            logger.error("runnable is null");
            throw new JedisRuntimeException("runnable must be not null");
        }
        Jedis jedis = getJedis(jedisPool);

        try {
            runnable.run(jedis);
        } catch (JedisException jedisException) {
            logger.error("发生异常：\n{}", jedisException);
            throw new JedisRuntimeException(jedisException);
        } catch (Exception e) {
            logger.error("发生异常：\n{}", e);
            throw new JedisRuntimeException(e);
        } finally {
            releaseResource(jedisPool, jedis, true);
        }
    }

    @Override
    public <T> T doInRedis(JedisCallable<T> callable) {
        if (callable == null) {
            logger.error("callable is null");
            throw new JedisRuntimeException("callable must be not null");
        }
        boolean happen = false;
        Jedis jedis = getJedis(jedisPool);

        try {
            return callable.call(jedis);
        } catch (JedisException jedisException) {
            logger.error("发生异常：\n{}", jedisException);
            happen = true;
            throw new JedisRuntimeException(jedisException);
        } catch (Exception e) {
            happen = true;
            throw new JedisRuntimeException(e);
        } finally {
            releaseResource(jedisPool, jedis, true);
            if (happen) {
                return null;
            }
        }
    }

    @Override
    public void doInPipeline(JedisPipelined pipeline) {
        Jedis jedis = getJedis(jedisPool);
        Pipeline pipelined = jedis.pipelined();

        try {
            pipeline.runInPipeline(pipelined);
        } catch (JedisException jedisException) {
            logger.error("发生异常：\n{}", jedisException);
            throw new JedisRuntimeException(jedisException);
        } catch (Exception e) {
            throw new JedisRuntimeException(e);
        } finally {
            releaseResource(jedisPool, jedis, true);
        }
    }
}
