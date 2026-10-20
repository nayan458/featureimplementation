package com.example.bookrooms.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.bookrooms.exceptions.InvalidRoomException;
import com.example.bookrooms.exceptions.UserNotFoundException;
import com.example.bookrooms.models.Booking;
import com.example.bookrooms.models.CustomerSession;
import com.example.bookrooms.models.CustomerSessionStatus;
import com.example.bookrooms.models.Room;
import com.example.bookrooms.models.User;
import com.example.bookrooms.repositories.BookingRepository;
import com.example.bookrooms.repositories.CustomerSessionRepository;
import com.example.bookrooms.repositories.RoomRepository;
import com.example.bookrooms.repositories.UserRepository;

public class BookingServiceImp implements BookingService {
    private UserRepository userRepository;
    private CustomerSessionRepository customerSessionRepository;
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository;

    public BookingServiceImp(UserRepository userRepository, CustomerSessionRepository customerSessionRepository, RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.customerSessionRepository = customerSessionRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking makeBooking(long userId, Map<Long, Integer> roomsToBeBooked)
            throws UserNotFoundException, InvalidRoomException {
        // check customer
        User customer = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        CustomerSession session;
        
        // find/start a session
        try {
            session = customerSessionRepository.findActiveCustomerSessionByUserId(userId).orElseThrow();
        } catch (Exception e) {
            session = new CustomerSession();
            session.setUser(customer);
            session.setCustomerSessionStatus(CustomerSessionStatus.ACTIVE);
            customerSessionRepository.save(session);
        }

        // check room exist
        Map<Room,Integer> bookedRooms = new HashMap<>();
        for(Map.Entry<Long, Integer> entry: roomsToBeBooked.entrySet()) {
            long roomId = entry.getKey();
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new InvalidRoomException("No such Room exist"));
            bookedRooms.put(room, bookedRooms.getOrDefault(room, 0) + entry.getValue());
        }
        
        // add room
        Booking booking = new Booking();
        booking.setCustomerSession(session);
        booking.setBookedRooms(bookedRooms);
        bookingRepository.save(booking);
        
        return booking;
    }
}

/*

Customer Book a room -> check if customer session exist
   -> NO
      -> create customer session -> create a new booking -> add the room to the booking
   -> YES
      -> get the customer session -> add the room to the booking

 */