package cn.hurrican.service;

import cn.hurrican.dtl.ConferenceTag;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * Created by NewObject on 2017/10/29.
 */

@Component(value = "updateTagTask")
public class TopTagsUpdateTimedTask {



   @Resource(name = "conferenceInfoService")
    private ConferenceInfoService mysqlService;


    public void updateTopTags(){
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();

        ServletContext context = webApplicationContext.getServletContext();
        if (context != null) {
            System.out.println("context != null");

            List<ConferenceTag> tagList = mysqlService.queryLatestConferenceHotTagTopN();
            context.setAttribute("tags", tagList);

        } else {
            System.out.println("context == null");
        }
    }
}
