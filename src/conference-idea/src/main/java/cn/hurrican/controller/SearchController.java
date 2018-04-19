package cn.hurrican.controller;

import cn.hurrican.dtl.ConferenceTag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by NewObject on 2017/10/9.
 *
 */
@Controller
@RequestMapping(value = "/conference/advanced")
public class SearchController {


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

}
