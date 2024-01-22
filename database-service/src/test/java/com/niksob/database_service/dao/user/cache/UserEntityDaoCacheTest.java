package com.niksob.database_service.dao.user.cache;

import com.niksob.database_service.MainContextTest;
import com.niksob.database_service.config.user.UserEntityTestConfig;
import com.niksob.database_service.dao.user.UserEntityDao;
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
public class UserEntityDaoCacheTest extends MainContextTest {
    @Autowired
    private UserEntityDao userEntityDao;
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
        userEntityDao.clearCache();
        Mockito.reset(userRepository);
    }

    @Test
    public void testCachingUserInfoAfterLoading() {
        userEntityDao.load(userEntity.getUsername());
        userEntityDao.load(userEntity.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).getByUsername(userEntity.getUsername());
    }

    @Test
    public void testCachingUserInfoAfterSaving() {
        userEntityDao.save(userEntity);
        userEntityDao.load(userEntity.getUsername());
        Mockito.verify(userRepository, Mockito.times(0)).getByUsername(userEntity.getUsername());
    }

    @Test
    public void testCachingUserInfoAfterUpdating() {
        userEntityDao.update(userEntity);
        userEntityDao.load(userEntity.getUsername());
        Mockito.verify(userRepository, Mockito.times(0)).getByUsername(userEntity.getUsername());
    }

    @Test
    public void testCachingUserInfoAfterDeleting() {
        userEntityDao.load(userEntity.getUsername()); // userEntity is caching
        userEntityDao.load(userEntity.getUsername()); // Checks that userEntity is cached. If not, the userRepository.getByUsername() method will be called 2 times, not 1
        Mockito.verify(userRepository, Mockito.times(1)).getByUsername(userEntity.getUsername());

        userEntityDao.delete(userEntity.getUsername());
        userEntityDao.load(userEntity.getUsername()); // Checks that userEntity not cached. If cached, the userRepository.getByUsername() method will be called 3 times, not 2
        Mockito.verify(userRepository, Mockito.times(2)).getByUsername(userEntity.getUsername());
    }
}
