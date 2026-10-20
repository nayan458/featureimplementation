package com.example.getrooms.services;

import com.example.getrooms.models.Room;
import com.example.getrooms.models.RoomType;
import com.example.getrooms.repositories.RoomRepository;

import java.util.List;

public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getRooms(String roomType) {
        if(roomType == null)
            return roomRepository.getRooms();
        RoomType type = RoomType.valueOf(roomType.toUpperCase());
       return roomRepository.getRoomsByRoomType(type);
    }
}

