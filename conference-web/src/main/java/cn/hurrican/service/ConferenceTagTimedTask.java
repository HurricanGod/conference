package cn.hurrican.service;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.beans.ResearchTag;
import cn.hurrican.dao.IConferenceInfoDao;
import cn.hurrican.dao.ITagDictionaryDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by NewObject on 2017/10/27.
 */
@Component(value = "tagServiceTask")
public class ConferenceTagTimedTask {

    @Resource(name = "IConferenceInfoDao")
    private IConferenceInfoDao dao;


    @Resource(name = "ITagDictionaryDao")
    private ITagDictionaryDao dictionaryDao;

    private HashMap<String, String> keywords2TagMap = null;


    //    @Scheduled(cron = "0 * * */1 * ?")
    public void updateConferenceTag(){
        System.out.println("执行周期任务:\tTid = " + Thread.currentThread().getId());


        if (keywords2TagMap == null) {
            List<ResearchTag> keyword2TagList = dictionaryDao.selectKeywordToTagMapper();
            keywords2TagMap = new HashMap<>();
            keyword2TagList.forEach(element ->
                    keywords2TagMap.put(element.getName(), element.getDirectionFieldName()));

        }


        List<ConferenceInfo> lists = dao.queryUnClassifyConferenceByTag();

        generateTags(lists);

        lists.forEach(c ->{
            try {
                dao.updateConferenceTag(c);
            } catch (Exception e) {
                System.out.println("异常信息：\n" + e.getMessage());
            }

        });


    }

    private void generateTags(List<ConferenceInfo> lists) {
        for (int i = 0; i < lists.size(); i++) {
            ConferenceInfo info = lists.get(i);
            String name = info.getCnName() != null ? info.getCnName() : info.getEnName();
            // 判断会议信息是否有名字
            if (name != null) {

                // 获取会议所属的 Tag
                String tag = info.getTag();
                StringBuilder builder = new StringBuilder();

                if (tag != null && !tag.equals("")) {
                    builder.append(tag).append(",");
                }

                // 获取关键词集合
                Set<String> keySet = keywords2TagMap.keySet();

                // 判断会议名字是否关键词，包含了关键词则对该会议进行归类
                // 把会议所属的 Tag 暂时保存到 StringBuilder 里
                for (String ele : keySet) {
                    if (name.toLowerCase().contains(ele.toLowerCase())) {

                        String value = keywords2TagMap.get(ele);

                        if (! builder.toString().contains(value)) {
                            builder.append(value).append(",");
                        }
                    }
                }
                String t = builder.toString();
                if (t.endsWith(",")) {

                    info.setTag(t.substring(0, t.length()-1));
                } else {
                    info.setTag(t);
                }

            }
        }
    }
}
