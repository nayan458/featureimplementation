package com.example.bookrooms.repositories.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.bookrooms.models.CustomerSession;
import com.example.bookrooms.repositories.CustomerSessionRepository;

public class CustomerSessionRepositoryImp implements CustomerSessionRepository {
    private Map<Long, CustomerSession> customerSessionRepo;

    public CustomerSessionRepositoryImp() {
        customerSessionRepo = new HashMap<>();
    }

    public CustomerSession save(CustomerSession customerSession) {
        customerSessionRepo.put(customerSession.getUser().getId(), customerSession);
        return customerSession;
    }

    public Optional<CustomerSession> findActiveCustomerSessionByUserId(long userId) {
        return Optional.ofNullable(customerSessionRepo.get(userId));
    }
}
