package cn.hurrican.dtl;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResMessage {

    private static ResMessage response = new ResMessage();

    public static ResMessage creator() {
        return new ResMessage();
    }

    public ResMessage retCodeEqual(Integer retCode) {
        this.retCode = retCode;
        return this;
    }

    public ResMessage logIs(String log) {
        this.log = log;
        return this;
    }

    public ResMessage msg(String msg) {
        this.message = msg;
        return this;
    }

    public ResMessage model(Map<String, Object> model) {
        this.model = model;
        return this;
    }

    public ResMessage put(String key, Object value) {
        this.model.put(key, value);
        return this;
    }

    public ResMessage putAll(Map<String, Object> model) {
        this.model.putAll(model);
        return this;
    }

    /**
     * 状态码
     */
    private int retCode = 0;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 日志
     */
    private String log;

    /**
     * 要返回的对象信息
     */
    private Map<String, Object> model = new HashMap<>();

    public int getRetCode() {
        return retCode;
    }

    public String getMessage() {
        return message;
    }

    public String getLog() {
        return log;
    }

    public Map<String, Object> getModel() {
        return model;
    }


    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return JSONObject.fromObject(this).toString();
    }
}
