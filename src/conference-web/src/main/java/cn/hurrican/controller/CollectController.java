package cn.hurrican.controller;

import cn.hurrican.beans.ConcernedConference;
import cn.hurrican.dtl.PraiseAndCollectRequestParams;
import cn.hurrican.service.CollectConferenceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by NewObject on 2017/10/30.
 */

@Controller
@RequestMapping(value = "/collect")
public class CollectController {

    @Resource(name = "collectService")
    private CollectConferenceService service;


    @RequestMapping(value = "/collectConference.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object collectConference(ConcernedConference params){
        /**
         * @decription: 功能：收藏会议
         * @param params 该对象包含3个成员： cid、uid、id，
         *               这3个变量的含义分别为：
         *                                      cid:  会议id  (此参数必选)
         *                                      uid: 用户id  (此参数必选)
         *                                      id:  对应表主键id (此参数可选)
         *
         * @return:  如果操作成功将返回一个匿名对象，对象有一个成员 isSuccess 表示操作是否成功
         */
        service.saveConcernedConference(params);

        return new Serializable() {public boolean isSuccess = true;};
    }


    @RequestMapping(value = "/checkIsCollected.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object checkUserIsCollectedConference(ConcernedConference params){

        Integer count = service.queryUserIsCollectedTheConferenceService(params);

        return new Serializable() {public Boolean isCollected = count > 0;};
    }



    @RequestMapping(value = "/cancelCollectConference.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object cancelCollectConference(ConcernedConference params){
        /**
         * @decription:  功能：取消收藏会议
         * @param params 该对象包含3个成员： cid、uid、id，
         *               这3个变量的含义分别为：
         *                                      cid:  会议id  (此参数必选)
         *                                      uid: 用户id  (此参数必选)
         *                                      id:  对应表主键id (此参数可选)
         *
         * @return: java.lang.Object
         */
        service.cancelCollectingConference(params);
        return new Serializable() {public boolean isSuccess = true;};
    }




    @RequestMapping(value = "/queryMyCollections.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryCollectedConference(PraiseAndCollectRequestParams queryParam)
            throws ParseException {
        /**
         * @decription: 查看已经收藏的会议，可以进行给定时间范围查询、分页查询、每页数量设置
         * @param queryParam 前端发送的查询请求参数，该对象有5个成员：
         *                    uid: 用户id(必选)
         *
         *                    startTime: 范围查询中的起始日期(可选)，
         *                               字符串类型，格式为"yyyy-MM-dd"，如未传递默认为系统当前日期
         *
         *                    offset: 整型，相对 startTime 的偏移量(可选)，可正可负，如未传递默认为 7
         *
         *                    page: 整型，分页查询中的页数(可选)，如未传递默认为 1
         *
         *                    perPageNumber: 分页查询页数控制(可选)，如未传递默认为 8
         *
         * @return:  ConferenceMsg列表集合
         */

        return service.queryCollectedConference(queryParam);
    }

    @RequestMapping(value = "/queryMyCollectionCount.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryUserCollectedConferenceCount(Integer uid){
        /**
         * @decription:
         * @param uid
         * @return: java.lang.Object
         */
        Integer count = service.queryTotalOfUserColectedConference(uid);

        return new Serializable() {public Integer number = count;};
    }


    @RequestMapping(value = "/queryCollectedNumber.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryBeCollectedNumber(Integer cid){
        Integer count = service.queryTheNumberOfByCollectedConferenceService(cid);

        return new Serializable() {public Integer number = count;};
    }


}
