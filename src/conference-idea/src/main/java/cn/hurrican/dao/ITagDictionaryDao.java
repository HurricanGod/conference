package cn.hurrican.dao;

import cn.hurrican.beans.ResearchTag;

import java.util.List;

/**
 * Created by NewObject on 2017/10/28.
 */
public interface ITagDictionaryDao {

    List<ResearchTag> selectKeywordToTagMapper();
}
