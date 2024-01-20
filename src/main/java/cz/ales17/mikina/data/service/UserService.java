package cz.ales17.mikina.data.service;

import cz.ales17.mikina.data.entity.User;
import cz.ales17.mikina.data.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public PasswordEncoder getEncoder() {
        return encoder;
    }

    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public void updatePassword(User user, String password) {
        String hashedPassword = encoder.encode(password);
        user.setHashedPassword(hashedPassword);
        update(user);
    }

    public void update(User entity) {
        repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
