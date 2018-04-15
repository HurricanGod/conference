package cn.hurrican.controller;

import cn.hurrican.beans.GivenPraiseBean;
import cn.hurrican.dtl.PraiseAndCollectRequestParams;
import cn.hurrican.service.GivenPraiseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by NewObject on 2017/10/30.
 */

@Controller
@RequestMapping(value = "/perference")
public class PraiseConferenceController {

    @Resource(name = "givenPraiseService")
    private GivenPraiseService service;

    @RequestMapping(value = "/admire.do", method = RequestMethod.POST)
    @ResponseBody
    public Object givePraiseToConference(GivenPraiseBean msg){
        /**
         * @decription: 会议点赞功能，保存用户点赞的会议id
         * @param msg
         * @return: java.lang.Object
         */
        boolean isReturn = service.checkIsSaveBePraisedConference(msg);
        if (isReturn) {
            return new Serializable() {public boolean isSuccess = false;};
        }

        service.saveBeAdmiredConference(msg);

        return new Serializable() {public boolean isSuccess = true;};
    }

    @RequestMapping(value = "/unPraise.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object cancelGivePraiseToConference(GivenPraiseBean entity){
        /**
         * @decription: 取消点赞功能，删除用户已经点赞的会议
         * @param entity
         * @return: java.lang.Object
         */
        service.dropBeAdmiredConference(entity);

        return new Serializable() {public boolean isSuccess = true;};
    }

    @RequestMapping(value = "/queryPraisedConference.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryPraisedConferenceByUid(PraiseAndCollectRequestParams requestParameters){
        /**
         * @decription: 查询用户点赞过的所有会议
         * @param requestParameters
         * @return:  List<ConferenceMsg>
         */

        return service.queryBeAdmiredConferenceByUidService(requestParameters);
    }


    @RequestMapping(value = "/queryUserPraisedCount.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryTheTotalOfUserHadAdmiredConference(PraiseAndCollectRequestParams params){

        /**
         * @decription:
         * @param params
         * @return: 匿名对象，该对象只有一个属性："number"
         *           表示用户点过的会议总数
         */
        Integer count = service.queryUserHadAdmiredConferenceCountService(params);

        return new Serializable() {public Integer number = count;};
    }


    @RequestMapping(value = "/queryCount.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryBePraiseConferenceCountByCid(Integer cid){
        /**
         * @decription: 根据会议cid查询该会议被点赞的次数
         * @param cid
         * @return: 匿名对象，该对象只有一个属性："number"，
         *          表示id为cid的会议被点赞的次数
         */
        Integer count = service.queryBeAdmiredConferenceCount(cid);

        return new Serializable() {public Integer number = count;};
    }


    @RequestMapping(value = "/checkIsAdmired.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object checkUserIsAdmiredTheConference(GivenPraiseBean msg){
        /**
         * @decription: 查询uid和cid查询用户有没有对id为cid的会议点赞过
         * @param msg
         * @return: 匿名对象
         */
        boolean isAdmired = service.checkIsSaveBePraisedConference(msg);
        return new Serializable() {public boolean isSuccess = isAdmired;};
    }
}
