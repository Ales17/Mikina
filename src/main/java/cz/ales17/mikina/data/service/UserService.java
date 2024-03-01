package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.model.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> findAllUsers();

    void delete(UserEntity user);

    void saveUser(UserEntity user);

    void updatePassword(UserEntity user, String password);
}
