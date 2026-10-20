package com.example.bookrooms.repositories.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.bookrooms.models.Room;
import com.example.bookrooms.repositories.RoomRepository;

public class RoomRepositoryImp implements RoomRepository {

    private Map<Long, Room> roomRepo;

    public RoomRepositoryImp() {
        roomRepo = new HashMap<>();
    }

    public Room save(Room room) {
        roomRepo.put(room.getId(), room);
        return room;
    }

    public Optional<Room> findById(long roomId) {
        return Optional.ofNullable(roomRepo.get(roomId));
    }
}
