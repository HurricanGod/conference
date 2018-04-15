package cn.hurrican.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by NewObject on 2017/10/4.
 */
public class AbstractBaseMongoTemplete implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        MongoTemplate mongoTemplate =
                applicationContext.getBean("mongoTemplate", MongoTemplate.class);
        this.setMongoTemplate(mongoTemplate);


    }

    @Autowired
    @Qualifier(value = "mongoTemplate")
    protected MongoTemplate mongoTemplate;

    /**
     * 设置mongoTemplate
     * @param mongoTemplate the mongoTemplate to set
     */
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
