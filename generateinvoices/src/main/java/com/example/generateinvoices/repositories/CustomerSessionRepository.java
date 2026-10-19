package com.example.generateinvoices.repositories;

import com.example.generateinvoices.models.CustomerSession;

import java.util.Optional;

public interface CustomerSessionRepository {
    public CustomerSession save(CustomerSession customerSession);

    public void delete(long id);

    public Optional<CustomerSession> findActiveCustomerSessionByUserId(long userId);
}

