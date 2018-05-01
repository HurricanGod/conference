package cn.hurrican.exception;

import java.util.HashMap;
import java.util.Map;

public class NecessaryParameterNullException extends RuntimeException {

    private Object retVal;
    private Map<String,Object> model = new HashMap<>(8);

    public NecessaryParameterNullException retValQEqual(Object value){
        this.retVal = value;
        return this;
    }

    public NecessaryParameterNullException put(String key, Object value){
        this.model.put(key, value);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public Object getRetVal() {
        return retVal;
    }

    public void setRetVal(Object retVal) {
        this.retVal = retVal;
    }

    public NecessaryParameterNullException() {
    }

    public NecessaryParameterNullException(String message) {
        super(message);
    }

    public NecessaryParameterNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public NecessaryParameterNullException(Throwable cause) {
        super(cause);
    }

    public NecessaryParameterNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
