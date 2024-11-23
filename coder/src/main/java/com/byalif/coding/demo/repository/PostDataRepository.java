package com.byalif.coding.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.byalif.coding.demo.model.PostData;

@Repository
public interface PostDataRepository extends JpaRepository<PostData, Long> {

	List<PostData> findTop20ByOrderByIdDesc();

	void deleteByCreatedAtBefore(LocalDateTime cutoffDate);
}
