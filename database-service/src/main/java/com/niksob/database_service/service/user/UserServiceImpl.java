package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public UserInfo load(Username username) {
        return userDao.load(username);
    }

    @Override
    public void save(UserInfo userInfo) {
        userDao.save(userInfo);
    }
}
