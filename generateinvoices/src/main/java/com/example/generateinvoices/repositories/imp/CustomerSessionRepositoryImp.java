package com.example.generateinvoices.repositories.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.generateinvoices.models.CustomerSession;
import com.example.generateinvoices.repositories.CustomerSessionRepository;

public class CustomerSessionRepositoryImp implements CustomerSessionRepository{
    private static Map<Long, CustomerSession> customerSessionRepository = new HashMap<>();

    @Override
    public CustomerSession save(CustomerSession customerSession) {
        Long id = customerSession.getUser().getId();
        customerSessionRepository.put(id, customerSession);
        return customerSession;
    }

    @Override
    public Optional<CustomerSession> findActiveCustomerSessionByUserId(long userId) {
        return Optional.ofNullable(customerSessionRepository.get(userId));
    }

    @Override
    public void delete(long userId) {
        customerSessionRepository.remove(userId);
    }
}
