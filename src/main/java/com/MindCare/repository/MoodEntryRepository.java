package com.MindCare.repository;

import com.MindCare.entity.MoodEntry;
import com.MindCare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {
    List<MoodEntry> findByUser(User user);
    List<MoodEntry> findByUserId(Long userId);
    @Query("SELECT m.factors FROM MoodEntry m WHERE m.user.id = :userId")
    List<String> findFactorsByUserId(Long userId);
}
