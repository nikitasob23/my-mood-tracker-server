package com.niksob.database_service.config.service.mood.tag.loader;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.database_service.service.mood.tag.loader.MoodTagLoaderImpl;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoodTagLoaderConfig {
    @Bean("moodTagLoader")
    public MoodTagLoaderImpl getMoodTagLoader(UserExistenceService userExistenceService, MoodTagDao moodTagDao) {
        return new MoodTagLoaderImpl(userExistenceService, moodTagDao);
    }
}
