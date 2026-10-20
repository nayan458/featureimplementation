package com.example.bookrooms.controllers;

import java.util.Map;

import com.example.bookrooms.dtos.MakeBookingRequestDto;
import com.example.bookrooms.dtos.MakeBookingResponseDto;
import com.example.bookrooms.dtos.ResponseStatus;
import com.example.bookrooms.models.Booking;
import com.example.bookrooms.services.BookingService;

public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public MakeBookingResponseDto makeBooking(MakeBookingRequestDto makeBookingRequestDto) {
        MakeBookingResponseDto response = new MakeBookingResponseDto();
        try {
            long userId = makeBookingRequestDto.getUserId();
            Map<Long, Integer> bookedRooms = makeBookingRequestDto.getBookedRooms();
            
            Booking booking = bookingService.makeBooking(userId, bookedRooms);

            response.setBooking(booking);
            response.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setResponseStatus(ResponseStatus.FAILURE);
        }
        return response;
    }
}

