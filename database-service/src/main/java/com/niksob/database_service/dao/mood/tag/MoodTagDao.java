package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.mapper.entity.mood.tag.MoodTagEntityMapper;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.layer_connector.annotation.LayerConnector;

import java.util.Set;

@LayerConnector(source = MoodTagEntityDao.class, mapper = {MoodTagEntityMapper.class})
public interface MoodTagDao {
    MoodTag save(MoodTag moodTag);

    Set<MoodTag> loadByUserId(UserId userId);
}
