package com.springboot.libraryPJ.ZZ.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {

	//	 2次開発 ソート順修正
	@Query(value = "SELECT * FROM rental order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findAllOrdered();

	@Query(value = "SELECT * FROM rental WHERE delete_flag = :deleteFlag order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findByDeleteFlag(@Param("deleteFlag") String deleteFlag);

	@Query(value = "SELECT * FROM rental WHERE member_id =:memberId and delete_flag = :deleteFlag order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findByMemberIdAndDeleteFlag(@Param("memberId") String memberId,
			@Param("deleteFlag") String deleteFlag);

	@Query(value = "SELECT * FROM rental WHERE book_id =:bookId and delete_flag = :deleteFlag order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findByBookIdAndDeleteFlag(@Param("bookId") String bookId,
			@Param("deleteFlag") String deleteFlag);

	@Query(value = "SELECT * FROM rental WHERE (member_id =:memberId or book_id =:bookId) and delete_flag = :deleteFlag order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findByMemberIdAndBookIdAndDeleteFlag(@Param("memberId") String memberId,
			@Param("bookId") String bookId, @Param("deleteFlag") String deleteFlag);

	@Query(value = "SELECT * FROM rental WHERE member_id =:memberId or book_id =:bookId order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findByMemberIdAndBookId(@Param("memberId") String memberId, @Param("bookId") String bookId);

	@Query(value = "SELECT * FROM rental WHERE member_id =:memberId order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findByMemberId(@Param("memberId") String memberId);

	@Query(value = "SELECT * FROM rental WHERE book_id =:bookId order by RENTAL_DUE_DATE ASC", nativeQuery = true)
	List<RentalEntity> findByBookId(@Param("bookId") String bookId);

	/**
	 * 会員IDを条件に貸出中の資料情報を取得する。
	 */
	@Query(value = "SELECT * FROM rental WHERE member_id =:memberId AND return_date IS NULL AND delete_flag = 0", nativeQuery = true)
	List<RentalEntity> findRentalListByMemberId(@Param("memberId") String memberId);

	RentalEntity findByRentalIdAndDeleteFlag(int rentalId, String deleteFlag);

	List<RentalEntity> findByRemindFlag(String remindFlag);

	/**
	 * 会員IDを条件に延滞中の資料情報を取得する。
	 */
	@Query(value = "SELECT * FROM rental WHERE member_id =:memberId AND rental_due_date < SYSDATE AND return_date IS NULL AND delete_flag = 0", nativeQuery = true)
	List<RentalEntity> findArrearsListByMemberId(@Param("memberId") String memberId);

	/**
	 * 資料IDを条件に貸出中の資料情報を取得する。
	 */
	@Query(value = "SELECT * FROM rental WHERE book_id =:bookId AND return_date IS NULL AND delete_flag = 0", nativeQuery = true)
	List<RentalEntity> findRentalListByBookId(@Param("bookId") String bookId);

}
