package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.mapper.entity.mood.tag.MoodTagEntityMapper;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.mood.tag.MoodTagName;
import com.niksob.layer_connector.annotation.LayerConnector;

@LayerConnector(source = MoodTagEntityDao.class, mapper = {MoodTagEntityMapper.class})
public interface MoodTagDao {
    MoodTag load(MoodTagName name);

    void deleteByName(MoodTagName name);
}
