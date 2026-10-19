package com.example.addroomshotelmanagement.services;

import com.example.addroomshotelmanagement.exceptions.UnAuthorizedAccess;
import com.example.addroomshotelmanagement.exceptions.UserNotFoundException;
import com.example.addroomshotelmanagement.models.Room;
import com.example.addroomshotelmanagement.models.RoomType;
import com.example.addroomshotelmanagement.models.User;
import com.example.addroomshotelmanagement.models.UserType;
import com.example.addroomshotelmanagement.repositories.RoomRepository;
import com.example.addroomshotelmanagement.repositories.UserRepository;

public class RoomServiceImp implements RoomService {
    private RoomRepository roomRepository;
    private UserRepository userRepository;

    public RoomServiceImp(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Room addRoom(long userId, String roomName, double price, String roomType, String description) throws UserNotFoundException, UnAuthorizedAccess {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User doesn't exist. Please try registering the user first."));

        if(user.getUserType() != UserType.ADMIN)
            throw new UnAuthorizedAccess("Admin permision is required");

        Room room = new Room();
        room.setName(roomName);
        room.setPrice(price);
        room.setRoomType(RoomType.valueOf(roomType.trim().toUpperCase()));
        room.setDescription(description);

        roomRepository.add(room);

        return room;
    }
}
