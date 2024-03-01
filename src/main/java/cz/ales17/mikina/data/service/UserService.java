package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.entity.UserEntity;
import cz.ales17.mikina.data.repository.UserRepository;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    @Getter
    public final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void saveUser(UserEntity user) {
        repository.save(user);
    }

    public List<UserEntity> findAllUsers() {
        return repository.findAll();
    }

    public Optional<UserEntity> get(Long id) {
        return repository.findById(id);
    }

    public void updatePassword(UserEntity user, String password) {
        String hashedPassword = encoder.encode(password);
        user.setHashedPassword(hashedPassword);
        saveUser(user);
    }

    public void delete(UserEntity user) {
        repository.delete(user);
    }

    public Page<UserEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UserEntity> list(Pageable pageable, Specification<UserEntity> filter) {
        return repository.findAll(filter, pageable);
    }

}
