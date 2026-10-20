package com.example.getrooms.repositories;

import com.example.getrooms.models.Room;
import com.example.getrooms.models.RoomType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRoomRepositoryImpl implements RoomRepository {
    private Map<Long, Room> roomsMap;
    private static long idCounter = 0;

    public InMemoryRoomRepositoryImpl() {
        //ToDo
    }

    @Override
    public Room add(Room room) {
        //ToDo
        return null;
    }

    @Override
    public List<Room> getRooms() {
        //ToDo
        return null;
    }

    @Override
    public List<Room> getRoomsByRoomType(RoomType roomType) {
        //ToDo
        return null;
    }

    @Override
    public Room save(Room room) {
        //ToDo
        return null;
    }
}

