package com.example.demo.business.repository;

import com.example.demo.business.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {

    String roomSql = "select r from Room r where r.isDel=0";
    @Query(value = roomSql)
    List<Room> findCurrentRoom(Pageable pageable);
    @Query(value = "select count(r) from Room r where r.isDel=0")
    Integer countFindCurrentRoom();
    @Query(value = "select r from Room r where r.isDel=0")
    List<Room> findAllRoom();

}
