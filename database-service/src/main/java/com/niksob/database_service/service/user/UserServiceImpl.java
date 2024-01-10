package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserEntityDao;
import com.niksob.database_service.mapper.dao.user.UserEntityMapper;
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
    private final UserEntityDao userEntityDao;

    private final UserEntityMapper userEntityMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserInfo load(Username username) {
        return Stream.of(username)
                .map(userEntityMapper::toEntityUsername)
                .map(userEntityDao::load)
                .map(userEntityMapper::fromEntity)
                .peek(userInfo -> log.debug("Get user info from user DAO", userInfo))
                .findFirst().get();
    }

    @Override
    public void save(UserInfo userInfo) {
        Stream.of(userInfo)
                .map(userEntityMapper::toEntity)
                .forEach(userEntityDao::save);
        log.debug("Save user info to user DAO", userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        Stream.of(userInfo)
                .map(userEntityMapper::toEntity)
                .forEach(userEntityDao::update);
        log.debug("Update user info to user DAO", userInfo);
    }

    @Override
    public void delete(Username username) {
        Stream.of(username)
                .map(userEntityMapper::toEntityUsername)
                .peek(userEntityDao::delete)
                .forEach(userInfo -> log.debug("Deleted user info from user DAO", userInfo));
    }
}
