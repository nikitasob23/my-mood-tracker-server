package com.niksob.database_service.config.service.mood.entry;

import com.niksob.database_service.dao.mood.MoodEntryWithTagsDao;
import com.niksob.database_service.service.mood.entry.MoodEntryService;
import com.niksob.database_service.service.mood.entry.MoodEntryServiceImpl;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MoodEntryServiceConfig {
    @Bean
    public MoodEntryService getMoodEntryService(
            MoodEntryWithTagsDao moodEntryWithTagsDao, @Lazy UserExistenceService userExistenceService
    ) {
        return new MoodEntryServiceImpl(moodEntryWithTagsDao, userExistenceService);
    }
}