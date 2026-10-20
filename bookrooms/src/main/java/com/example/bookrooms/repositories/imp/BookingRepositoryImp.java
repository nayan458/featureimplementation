package com.example.bookrooms.repositories.imp;

import java.util.ArrayList;
import java.util.List;

import com.example.bookrooms.models.Booking;
import com.example.bookrooms.repositories.BookingRepository;

public class BookingRepositoryImp implements BookingRepository {

    private List<Booking> bookings;

    public BookingRepositoryImp() {
        bookings = new ArrayList<>();
    }

    @Override
    public Booking save(Booking booking) {
        bookings.add(booking);
        return booking;
    }
}
