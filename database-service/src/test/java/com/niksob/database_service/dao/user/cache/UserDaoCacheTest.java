package com.niksob.database_service.dao.user.cache;

import com.niksob.database_service.MainContextTest;
import com.niksob.database_service.config.user.UserEntityTestConfig;
import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {UserEntityTestConfig.class})
public class UserDaoCacheTest extends MainContextTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserEntity userEntity;
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
        userDao.load(userEntity.getUsername());
        userDao.load(userEntity.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getByUsername(userEntity.getUsername());
    }

    @Test
    public void testCachingUserInfoAfterSaving() {
        userDao.save(userEntity);
        userDao.load(userEntity.getUsername());
        Mockito.verify(userRepository, Mockito.times(0)).getByUsername(userEntity.getUsername());
    }

    @Test
    public void testCachingUserInfoAfterUpdating() {
        userDao.update(userEntity);
        userDao.load(userEntity.getUsername());
        Mockito.verify(userRepository, Mockito.times(0)).getByUsername(userEntity.getUsername());
    }

    @Test
    public void testCachingUserInfoAfterDeleting() {
        userDao.load(userEntity.getUsername()); // userEntity is caching
        userDao.load(userEntity.getUsername()); // Checks that userEntity is cached. If not, the userRepository.getByUsername() method will be called 2 times, not 1
        Mockito.verify(userRepository, Mockito.times(1)).getByUsername(userEntity.getUsername());

        userDao.delete(userEntity.getUsername());
        userDao.load(userEntity.getUsername()); // Checks that userEntity not cached. If cached, the userRepository.getByUsername() method will be called 3 times, not 2
        Mockito.verify(userRepository, Mockito.times(2)).getByUsername(userEntity.getUsername());
    }
}
