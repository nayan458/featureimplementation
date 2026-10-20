package com.example.getrooms.repositories;

import com.example.getrooms.models.Room;
import com.example.getrooms.models.RoomType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRoomRepositoryImpl implements RoomRepository {
    private Map<RoomType, List<Room>> roomsMap;
    private List<Room> rooms;
    private static long idCounter = 0;

    public InMemoryRoomRepositoryImpl() {
        roomsMap = new HashMap<>();
        rooms = new ArrayList<>();
    }

    @Override
    public Room add(Room room) {
        idCounter++;
        rooms.add(room);
        roomsMap.computeIfAbsent(room.getRoomType(), k -> new ArrayList<>()).add(room);
        return room;
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public List<Room> getRoomsByRoomType(RoomType roomType) {
        return roomsMap.get(roomType);
    }

    @Override
    public Room save(Room room) {
        return add(room);
    }
}

