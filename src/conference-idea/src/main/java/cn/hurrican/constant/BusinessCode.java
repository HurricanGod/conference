package cn.hurrican.constant;

public enum BusinessCode {
    Ok(0, "正常返回"),
    CallApiFail(-8, "调用Api返回结果错误"),
    NetworkError(-9, "网络错误"),
    ServerError(-1, "服务器内部错误"),
    ParametersIsNullError(-2, "请求参数为null错误"),
    SendRequestFail(-3, "发送http请求失败")
    ;

    int code;
    String msg;

    BusinessCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
