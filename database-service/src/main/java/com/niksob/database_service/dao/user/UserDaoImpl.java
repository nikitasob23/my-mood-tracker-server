package com.niksob.database_service.dao.user;

import com.niksob.database_service.mapper.dao.user.UserEntityMapper;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class UserDaoImpl implements UserDao {
    private final UserRepository userRepository;

    private final UserEntityMapper userEntityMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserRepository.class);

    @Override
    public UserInfo load(Username username) {
        log.debug("Loading user info by username", username);
        return Stream.of(username)
                .map(userEntityMapper::toEntityUsername)
                .map(userRepository::getByUsername)
                .map(userEntityMapper::fromEntity)
                .filter(Objects::nonNull)
                .peek(userInfo -> log.debug("User info loaded", userInfo))
                .findFirst().orElseThrow(() -> createEntityNotFoundException(username));
    }

    @Override
    public void save(UserInfo userInfo) {
        log.debug("Saving user info", userInfo);
        Stream.of(userInfo)
                .map(userEntityMapper::toEntity)
                .peek(userRepository::save)
                .forEach(userEntity -> log.debug("User info saved", userEntity));
    }

    private EntityNotFoundException createEntityNotFoundException(Username username) {
        final EntityNotFoundException e = new EntityNotFoundException("Username not found by username");
        log.error("Failed loading user by username from repository", e, username);
        return e;
    }
}