package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.dao.mood.tag.facade.TagEntityDaoFacade;
import com.niksob.database_service.mapper.entity.mood.tag.MoodTagEntityMapper;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.layer_connector.annotation.LayerConnector;

import java.util.Set;

@LayerConnector(source = TagEntityDaoFacade.class, mapper = MoodTagEntityMapper.class)
public interface MoodTagDao {
    Set<MoodTag> loadByUserId(UserId userId);

    MoodTag save(MoodTag moodTag);

    void update(MoodTag moodTag);

    Set<MoodTag> mergeAll(Set<MoodTag> moodTags);

    void deleteById(MoodTag moodTag);
}
