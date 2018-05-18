package cn.hurrican.controller;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.dtl.ConferenceMsg;
import cn.hurrican.dtl.ConferenceTag;
import cn.hurrican.dtl.ResMessage;
import cn.hurrican.service.ConferenceInfoService;
import cn.hurrican.utils.LanguageUtils;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by NewObject on 2017/10/9.
 *
 */
@Controller
@RequestMapping(value = "/conference/advanced")
public class SearchController {

    @Autowired
    private JiebaSegmenter segmenter;

    @Autowired
    private ConferenceInfoService conferenceInfoService;


    @RequestMapping(value = "/queryHotConference.do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object findHotConference(@RequestBody List<ConferenceTag> tags){
        /**
         * @decription: 根据用户提供的Tag获取相应Tag的会议
         * @param tags 用户关注的会议Tag
         * @return: java.lang.Object
         */
        if (tags != null) {
            for (int i = 0; i < tags.size(); i++) {
                ConferenceTag tag = tags.get(i);
                System.out.println(tag);
            }
        }
        return null;
    }


    @RequestMapping(value = "/search.do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResMessage searchConference(String keyword, Integer offset, Integer number){
        ResMessage resMessage = new ResMessage();
        List<ConferenceInfo> resultList;
        List<String> words;
        /** 判断是否包含中文 **/
        if(LanguageUtils.isContainChinese(keyword)){
            words = segmenter.process(keyword, JiebaSegmenter.SegMode.SEARCH).stream()
                    .filter(e -> e.word.length() > 1)
                    .map(e -> e.word)
                    .sorted((o1, o2) -> o2.length() - o1.length())
                    .collect(Collectors.toList());
        }else{
            words = Arrays.stream(keyword.split(" +"))
                    .sorted((e1,e2) -> e2.length() - e1.length())
                    .collect(Collectors.toList());
        }
        HashSet<String> wordSet = new HashSet<>(words);
        wordSet.add(keyword);
        resultList = conferenceInfoService.queryConferenceByKeyWords(wordSet, offset, number);
        List<ConferenceMsg> resList = new ArrayList<>(resultList.size());
        resultList.forEach(e -> resList.add(ConferenceMsg.convert(e)));
        return resMessage.put("resultList", resList);
    }

}
