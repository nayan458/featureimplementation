package com.example.addroomshotelmanagement.repositories.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.addroomshotelmanagement.models.Room;
import com.example.addroomshotelmanagement.models.RoomType;
import com.example.addroomshotelmanagement.repositories.RoomRepository;

public class RoomRepositoryImp implements RoomRepository {
    private Map<RoomType, List<Room>> roomRepository;
    private List<Room> rooms;

    public RoomRepositoryImp(){
        this.roomRepository = new HashMap<>();
        this.rooms = new ArrayList<>();
    }

    @Override 
    public Room add(Room room) {
        roomRepository.computeIfAbsent(room.getRoomType(),k -> new ArrayList<>()).add(room);
        return room;
    }

    @Override 
    public List<Room> getRooms() {
        return rooms;
    }

    @Override 
    public List<Room> getRoomsByRoomType(RoomType roomType) {
        return roomRepository.getOrDefault(roomType, new ArrayList<>());
    }

    @Override 
    public Room save(Room room) {
        roomRepository.computeIfAbsent(room.getRoomType(),k -> new ArrayList<>()).add(room);
        return room;
    }
}
