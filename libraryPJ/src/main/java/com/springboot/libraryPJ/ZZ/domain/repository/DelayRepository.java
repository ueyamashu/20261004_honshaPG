package com.springboot.libraryPJ.ZZ.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.DelayEntity;

/**
 * 延滞情報一覧
 */
@Transactional(rollbackFor = Exception.class)
public interface DelayRepository extends JpaRepository<DelayEntity, Long> {
	@Query(value = "SELECT * FROM delay WHERE REMIND_FLAG = :remidFlag ORDER BY RENTAL_ID ", nativeQuery = true)
	List<DelayEntity> findByRemidFlag(@Param("remidFlag") String remidFlag);

	List<DelayEntity> findByRentalId(long rentalId);

	@Query(value = "SELECT * FROM delay WHERE OVER_DUE_DAYS >= '10' ORDER BY RENTAL_ID ", nativeQuery = true)
	List<DelayEntity> findByDelay();
}