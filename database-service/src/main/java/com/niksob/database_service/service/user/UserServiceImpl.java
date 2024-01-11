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
    public UserInfo save(UserInfo userInfo) {
        return Stream.of(userInfo)
                .map(userEntityMapper::toEntity)
                .map(userEntityDao::save)
                .map(userEntityMapper::fromEntity)
                .peek(u -> log.debug("Save user info to user DAO", u))
                .findFirst().get();
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        return Stream.of(userInfo)
                .map(userEntityMapper::toEntity)
                .map(userEntityDao::update)
                .map(userEntityMapper::fromEntity)
                .peek(u -> log.debug("Update user info to user DAO", u))
                .findFirst().get();
    }

    @Override
    public UserInfo delete(Username username) {
        final String entityUsername = userEntityMapper.toEntityUsername(username);

        final UserInfo loaded = Stream.of(entityUsername)
                .map(userEntityDao::load)
                .map(userEntityMapper::fromEntity)
                .findFirst().get();

        Stream.of(entityUsername)
                .peek(userEntityDao::delete)
                .forEach(userInfo -> log.debug("Deleted user info from user DAO", userInfo));
        return loaded;
    }
}
