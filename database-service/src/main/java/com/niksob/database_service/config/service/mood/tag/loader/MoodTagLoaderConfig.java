package com.niksob.database_service.config.service.mood.tag.loader;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.database_service.service.mood.tag.loader.MoodTagLoaderImpl;
import com.niksob.database_service.service.user.loader.UserLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoodTagLoaderConfig {
    @Bean("moodTagLoader")
    public MoodTagLoaderImpl getMoodTagLoader(UserLoader userLoader, MoodTagDao moodTagDao) {
        return new MoodTagLoaderImpl(userLoader, moodTagDao);
    }
}
