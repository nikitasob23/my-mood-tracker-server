package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserInfo load(Username username) {
        return Stream.of(username)
                .map(userDao::load)
                .peek(userInfo -> log.debug("Get user info from user DAO", userInfo))
                .findFirst().get();
    }

    @Override
    public void save(UserInfo userInfo) {
        userDao.save(userInfo);
        log.debug("Save user info to user DAO", userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userDao.update(userInfo);
        log.debug("Update user info to user DAO", userInfo);
    }
}
