/**
 * Created by NewObject on 2017/11/2.
 */
/**
 *  获取当前的日期时间 格式“yyyy-MM-dd”
 * @returns {string}
 */
function getTodayString() {
    var date = new Date();
    var seperator = "-";
    var month = date.getMonth() + 1;

    var day = date.getDay();

    return date.getFullYear() + seperator + month + seperator + day;
}

/**
 * 使用带有 contentType 的ajax请求在SpringMVC的控制器方法里参数需要使用@RequestBody注解
 * 如：  控制器有个方法 public Object query(ObjectType args),
 *      如果ajax请求指定了 contentType 的类型为 "application/json"
 *      那么就需要在方法参数前加上 @RequestBody注解，如果不加此注解将得不到前端发送的请求参数
 *      public Object query(@RequestBody ObjectType args),
 *
 * @param url
 * @param obj
 * @param successCallback
 */
function sendAjaxRequest(url, obj, successCallback){

    if (obj == null) {
        $.ajax({
            type :"post",
            url : url,
            dataType : "json",
            contentType : "application/json;charset=utf-8", // 指定这个协议很重要
            success: successCallback
        });
    } else {
        $.ajax({
            type :"post",
            url : url,
            dataType : "json",
            contentType : "application/json;charset=utf-8", // 指定这个协议很重要
            data : JSON.stringify(obj),
            success: successCallback
        });
    }

}

/**
 *  请求SpringMVC后台没有@RequestBody注解的方法
 * @param url
 * @param obj
 * @param requestMethod 请求方法(get,post)
 * @param successCallback 成功后的回调函数
 */
function sendSimpleAjaxRequest(url, requestMethod, obj, successCallback) {
    if (obj == null) {
        $.ajax({
            type :requestMethod,
            url : url,
            dataType : "json",
            success: successCallback
        });
    } else {
        $.ajax({
            type :requestMethod,
            url : url,
            dataType : "json",
            data : obj,
            success: successCallback
        });
    }
}

function sendAjaxRequestUnassignProtocol(url, obj, successCallback){

    if (obj == null) {
        $.ajax({
            type :"post",
            url : url,
            dataType : "json",
            success: successCallback
        });
    } else {
        $.ajax({
            type :"post",
            url : url,
            dataType : "json",
            data : JSON.stringify(obj),
            success: successCallback
        });
    }

}


function successCallBack(data){
    if (typeof(data.isSuccess) != "undefined" && data.isSuccess != null) {
        console.log("data.isSuccess = " + data.isSuccess);
    }
}