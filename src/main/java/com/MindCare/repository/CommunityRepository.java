package com.MindCare.repository;

import com.MindCare.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    // Suggest communities where the category matches any of the user's mood factors
    List<Community> findByCategoryIn(Set<String> categories);
    @Query("SELECT c FROM Community c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Community> searchCommunities(String keyword);
}
