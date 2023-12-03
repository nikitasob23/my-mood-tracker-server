package com.niksob.database_service.dao.user.cache;

import com.niksob.database_service.MainContextTest;
import com.niksob.database_service.config.user.UserEntityTestConfig;
import com.niksob.database_service.config.user.UserTestConfig;
import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.model.user.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {UserTestConfig.class, UserEntityTestConfig.class})
public class UserDaoCacheTest extends MainContextTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserEntity userEntity;
    @Autowired
    private UserInfo userInfo;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        Mockito.when(userRepository.getByUsername(userEntity.getUsername())).thenReturn(userEntity);
    }

    @AfterEach
    public void reset() {
        userDao.clearCache();
        Mockito.reset(userRepository);
    }

    @Test
    public void testCachingUserInfoAfterLoading() {
        userDao.load(userInfo.getUsername());
        userDao.load(userInfo.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getByUsername(userEntity.getUsername());
    }

    @Test
    public void testCachingUserInfoAfterSaving() {
        userDao.save(userInfo);
        userDao.load(userInfo.getUsername());
        Mockito.verify(userRepository, Mockito.times(0)).getByUsername(userEntity.getUsername());
    }
}
