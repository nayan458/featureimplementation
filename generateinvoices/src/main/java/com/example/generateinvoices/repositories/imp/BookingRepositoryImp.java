package com.example.generateinvoices.repositories.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.generateinvoices.models.Booking;
import com.example.generateinvoices.repositories.BookingRepository;

public class BookingRepositoryImp implements BookingRepository {

    private static Map<Long, List<Booking>> bookingRepository;
    public BookingRepositoryImp() {
        bookingRepository = new HashMap<>();
    }

    @Override
    public Booking save(Booking booking) {
        Long customerSessionId = booking.getCustomerSession().getId();
        bookingRepository.computeIfAbsent(customerSessionId, k -> new ArrayList<>()).add(booking);
        return booking;
    }

    @Override
    public List<Booking> findBookingByCustomerSession(long customerSessionId) {
        return bookingRepository.getOrDefault(customerSessionId, new ArrayList<>());
    }

    @Override
    public void delete(long customerSessionId) {
        bookingRepository.remove(customerSessionId);
    }
}
