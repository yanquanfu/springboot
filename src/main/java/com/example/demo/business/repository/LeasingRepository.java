package com.example.demo.business.repository;

import com.example.demo.business.entity.Leasing;
import com.example.demo.business.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeasingRepository extends CrudRepository<Leasing, Integer> {

//    String roomSql = "select r from Room r where r.isDel=0";
//    @Query(value = roomSql)
//    List<Room> findCurrentRoom(Pageable pageable);
//    @Query(value = "select count(r) from Room r where r.isDel=0")
//    Integer countFindCurrentRoom();
    @Query(value = "SELECT L FROM Leasing L WHERE L.isDel=0 AND L.roomId=:roomId")
    List<Leasing> findCurrentLeasingByRoomId(Integer roomId,Pageable pageable);

    @Query(value = "SELECT count(L) FROM Leasing L WHERE L.isDel=0 AND L.roomId=:roomId")
    Integer countFindCurrentLeasingByRoomId(Integer roomId);

    @Query(value = "SELECT L FROM Leasing L WHERE L.isDel=0 AND L.userId=:userId")
    List<Leasing> findCurrentLeasingByUserId(Integer userId,Pageable pageable);

    @Query(value = "SELECT count(L) FROM Leasing L WHERE L.isDel=0 AND L.userId=:userId")
    Integer countFindCurrentLeasingByUserId(Integer userId);

//    @Query(value = "SELECT l FROM Leasing l WHERE date(datetime(l.scheduledDate / 1000,'unixepoch')) >= date('now') " +
//            "and date(datetime(l.scheduledDate / 1000,'unixepoch'))  <= date('now')",nativeQuery=true)
    @Query(value = "SELECT * FROM leasing WHERE date(datetime(scheduled_date / 1000,'unixepoch')) >= :beginDate " +
            "and date(datetime(scheduled_date / 1000,'unixepoch'))  < :endDate",nativeQuery=true)
//    @Query(value = "SELECT L FROM Leasing L WHERE L.isDel=0 AND L.scheduledDate >='2021-03-07'")
    List<Leasing> queryBackLog(String beginDate,String endDate);
}
