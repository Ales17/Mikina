package cz.ales17.mikina.data.service.impl;

import cz.ales17.mikina.data.entity.UserEntity;
import cz.ales17.mikina.data.repository.UserRepository;
import cz.ales17.mikina.data.service.UserService;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Getter
    public final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }
    @Override
    public List<UserEntity> findAllUsers() {
        return repository.findAll();
    }
    @Override
    public void delete(UserEntity user) {
        repository.delete(user);
    }
    @Override
    public void saveUser(UserEntity user) {
        repository.save(user);
    }
    @Override
    public void updatePassword(UserEntity user, String password) {
        String hashedPassword = encoder.encode(password);
        user.setHashedPassword(hashedPassword);
        saveUser(user);
    }
}
