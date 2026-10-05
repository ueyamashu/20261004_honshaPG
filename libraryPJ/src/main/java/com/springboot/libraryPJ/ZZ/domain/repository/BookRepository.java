package com.springboot.libraryPJ.ZZ.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.BookEntity;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
	/**
	 * 削除フラグを条件に貸出資料を取得する。
	 */
	@Query(value = "SELECT * FROM book ORDER BY title ASC", nativeQuery = true)
	List<BookEntity> findByDeleteFlag(String deleteFlag);

	/**
	 * 資料名（前方一致）と削除フラグを条件に貸出資料リストを取得
	 */
	@Query(value = "SELECT * FROM book WHERE title LIKE :title% AND delete_flag = :deleteFlag ORDER BY title ASC", nativeQuery = true)
	List<BookEntity> findByTitleAndDeleteFlag(@Param("title") String title, @Param("deleteFlag") String deleteFlag);

	/**
	 * ISBN番号と排他キーと削除フラグを条件に資料リストを取得
	 */
	@Query(value = "SELECT * FROM book WHERE isbn = :isbn AND exclusive_key = :exclusiveKey AND delete_flag = :deleteFlag", nativeQuery = true)
	List<BookEntity> findByIsbnAndExcKeyAndDeleteFlag(@Param("isbn") String isbn,
			@Param("exclusiveKey") int exclusiveKey, @Param("deleteFlag") String deleteFlag);

	/**
	 * ISBN番号と削除フラグを条件に資料情報を取得
	 *
	 * @param isbn
	 * @param deleteFlag
	 * @return
	 */
	BookEntity findByIsbnAndDeleteFlag(String isbn, String deleteFlag);

	/**
	 * 資料IDを条件に資料情報を取得する。
	 *
	 * @param bookId 資料ID
	 * @return 資料情報
	 */
	@Query(value = "SELECT * FROM book WHERE book_id = :bookId", nativeQuery = true)
	List<BookEntity> findByBookId(@Param("bookId") int bookId);

	/**
	 * ISBN番号と排他キーと削除フラグを条件に蔵書リストを取得
	 */

	@Query(value = "SELECT * FROM book WHERE isbn = :isbn AND exclusive_key = :exclusiveKey AND delete_flag = :deleteFlag", nativeQuery = true)
	List<BookEntity> findByBookIdAndIsbnAndExclusiveKeyAndDeleteFlag(@Param("isbn") String isbn,
			@Param("exclusiveKey") int exclusiveKey, @Param("deleteFlag") String deleteFlag);

}
