package cn.hurrican.dtl;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResMessage {

    private static ResMessage response = new ResMessage();

    public static ResMessage creator() {
        return response;
    }

    public ResMessage retCodeEqual(Integer retCode) {
        response.retCode = retCode;
        return response;
    }

    public ResMessage logIs(String log) {
        response.log = log;
        return response;
    }

    public ResMessage msg(String msg) {
        response.message = msg;
        return response;
    }

    public ResMessage model(Map<String, Object> model) {
        response.model = model;
        return response;
    }

    public ResMessage put(String key, Object value) {
        response.model.put(key, value);
        return response;
    }

    public ResMessage putAll(Map<String, Object> model) {
        response.model.putAll(model);
        return response;
    }

    /**
     * 状态码
     */
    private int retCode;

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

    @Override
    public String toString() {
        return JSONObject.fromObject(this).toString();
    }
}
