package com.example.waitingroom.repository;

import com.example.waitingroom.domain.WaitingRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WaitingRoomRepository extends JpaRepository<WaitingRooms,Long> {
    WaitingRooms findByGameRoomCode(String gameRoomCode);

    @Query("SELECT wr FROM WaitingRooms wr WHERE wr.participants < wr.maxParticipants " +
            "AND wr.gameType = 'Basic' AND wr.isPublic = true AND wr.isStarted = false")
    WaitingRooms findExistWaitingRooms();
    @Query("SELECT wr FROM WaitingRooms wr WHERE wr.id = :id")
    WaitingRooms findWaitingRoomsById(@Param("id") Long id);

//    void deleteById(Long id);
}
