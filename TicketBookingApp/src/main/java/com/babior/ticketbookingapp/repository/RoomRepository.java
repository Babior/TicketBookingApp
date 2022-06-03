package com.babior.ticketbookingapp.repository;

import com.babior.ticketbookingapp.business.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
