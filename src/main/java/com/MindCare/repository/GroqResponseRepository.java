// GroqResponseRepository.java
package com.MindCare.repository;

import com.MindCare.entity.GroqResponse;
import com.MindCare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroqResponseRepository extends JpaRepository<GroqResponse, Long> {
    List<GroqResponse> findByUserId(Long userId);


}
