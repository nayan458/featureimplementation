package com.example.bookrooms.repositories.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.bookrooms.models.User;
import com.example.bookrooms.repositories.UserRepository;

public class UserRepositoryImp implements UserRepository {
    Map<Long, User> userRepo;

    public UserRepositoryImp() {
        userRepo = new HashMap<>();
    }

    public Optional<User> findById(long id) {
        return Optional.ofNullable(userRepo.get(id));
    }

    public User save(User user) {
        userRepo.put(user.getId(), user);
        return user;
    }
}
