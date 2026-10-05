package com.springboot.libraryPJ.ZZ.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)
public interface LibraryRepository extends JpaRepository<LibraryEntity, Integer> {

	List<LibraryEntity> findByDeleteFlag(String deleteFlag);

	LibraryEntity findByBookId(int bookId);

	LibraryEntity findByBookIdAndDeleteFlag(int bookId, String deleteFlag);

	/**
	 * ISBN番号と排他キーと削除フラグを条件に蔵書リストを取得
	 */
	@Query(value = "SELECT * FROM library WHERE isbn = :isbn AND exclusive_key = :exclusiveKey AND delete_flag = :deleteFlag", nativeQuery = true)
	List<LibraryEntity> findByIsbnAndExcKeyAndDeleteFlag(@Param("isbn") String isbn,
			@Param("exclusiveKey") int exclusiveKey, @Param("deleteFlag") String deleteFlag);

	/**
	 * 資料IDと削除フラグを条件に蔵書リストを取得する。
	 */
	@Query(value = "SELECT * FROM library WHERE book_id = :bookId AND delete_flag = :deleteFlag", nativeQuery = true)
	List<LibraryEntity> findByBookIdAndDeleteFlag(@Param("bookId") String bookId,
			@Param("deleteFlag") String deleteFlag);

	/**
	 * 資料ID ISBN番号と排他キーと削除フラグを条件に蔵書リストを取得
	 */
	@Query(value = "SELECT * FROM library WHERE book_id = :bookId AND isbn = :isbn AND exclusive_key = :exclusiveKey AND delete_flag = :deleteFlag", nativeQuery = true)
	List<LibraryEntity> findByBookIdAndIsbnAndExclusiveKeyAndDeleteFlag(@Param("bookId") int bookId,
			@Param("isbn") String isbn, @Param("exclusiveKey") int exclusiveKey,
			@Param("deleteFlag") String deleteFlag);

	/**
	 * 入庫画面
	 */
	@Query(value = "SELECT * FROM library WHERE isbn = :isbn AND delete_flag = :deleteFlag", nativeQuery = true)
	List<LibraryEntity> findByIsbnAndDeleteFlag(@Param("isbn") String isbn, @Param("deleteFlag") String deleteFlag);
}
