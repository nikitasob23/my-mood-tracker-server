package com.niksob.database_service.config.service.user.loader;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.service.mood.entry.MoodEntryService;
import com.niksob.database_service.service.mood.tag.loader.MoodTagLoader;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import com.niksob.database_service.service.user.existence.UserExistenceServiceImpl;
import com.niksob.database_service.util.date.DefUserDateRangeUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UserLoaderConfig {
    @Bean("userExistenceService")
    public UserExistenceService getUserExistenceService(
            UserDao userDao,
            MoodEntryService moodEntryService,
            @Lazy MoodTagLoader moodTagLoader,
            DefUserDateRangeUtil defUserDateRangeUtil
    ) {
        return new UserExistenceServiceImpl(userDao, moodEntryService, moodTagLoader, defUserDateRangeUtil);
    }
}
