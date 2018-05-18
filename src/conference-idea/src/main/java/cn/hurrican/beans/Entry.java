package cn.hurrican.beans;

/**
 * @Author: Hurrican
 * @Description:
 * @Date 2018/3/24
 * @Modified 10:32
 */
public class Entry<K, V> {

    private K key;
    private V value;

    @Override
    public String toString() {
        return "Entry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public Entry() {
    }

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }



    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }


}
