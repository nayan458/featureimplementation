package com.example.addroomshotelmanagement.repositories.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.addroomshotelmanagement.models.User;
import com.example.addroomshotelmanagement.repositories.UserRepository;

public class UserRepositoryImp implements UserRepository {
    
    private Map<Long, User> userRepository;

    public UserRepositoryImp() {
        this.userRepository = new HashMap<>();
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(userRepository.getOrDefault(userId,null));
    }

    @Override
    public User save(User user) {
        long userId = user.getId();
        userRepository.put(userId, user);
        return user;
    }
}
