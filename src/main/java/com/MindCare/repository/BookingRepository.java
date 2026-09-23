package com.MindCare.repository;

import com.MindCare.entity.Booking;
import java.util.Optional;
import com.MindCare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserEmail(String email);
    List<Booking> findByUser(User user);
    Optional<Booking> findById(Long id);


}

