package com.springboot.libraryPJ.ZZ.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)
public interface BookLibraryRepository extends JpaRepository<BookLibraryEntity, Integer> {

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookList();

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE library.book_id = :book_id ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByBookId(@Param("book_id") int book_id);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.title LIKE :title% ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByTitle(@Param("title") String title);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.author LIKE :author% ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByAuthor(@Param("author") String author);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.category = :category ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByCategory(@Param("category") String category);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.title LIKE :title% AND book.author LIKE :author% ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByTitleAndAuthor(@Param("title") String title, @Param("author") String author);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.title LIKE :title% AND book.category = :category ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByTitleAndCategory(@Param("title") String title,
			@Param("category") String category);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.author LIKE :author% AND book.category = :category ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByAuthorAndCategory(@Param("author") String author,
			@Param("category") String category);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.title LIKE :title% AND book.author LIKE :author% AND book.category = :category ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByTitleAndAuthorAndCategory(@Param("title") String title,
			@Param("author") String author, @Param("category") String category);

	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.isbn =:isbn ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByIsbn(@Param("isbn") String isbn);

	// ISBNだけ検索する方法
	@Query(value = "SELECT library.book_id, library.isbn AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.isbn =:isbn ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findBookListByIsbn1(@Param("isbn") String isbn);

	/**
	 * 貸出可能な資料情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.delete_flag = 0 AND library.delete_flag = 0 AND NOT exists (SELECT 1 FROM rental WHERE delete_flag = 0 AND rental.book_id = library.book_id) ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findRentalBookList();

	/**
	 * 資料ＩＤを条件に貸出可能な資料情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.delete_flag = 0 AND library.delete_flag = 0 AND NOT exists (SELECT 1 FROM rental WHERE delete_flag = 0 AND rental.book_id = library.book_id) AND library.book_id = :bookId", nativeQuery = true)
	List<BookLibraryEntity> findRentalBookListByBookId(@Param("bookId") int bookId);

	/**
	 * 資料ＩＤリストを条件に貸出可能な資料情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.delete_flag = 0 AND library.delete_flag = 0 AND NOT exists (SELECT 1 FROM rental WHERE delete_flag = 0 AND rental.book_id = library.book_id) AND library.book_id In (:bookIdList) order by library.book_id desc", nativeQuery = true)
	List<BookLibraryEntity> findRentalBookListByBookIdList(@Param("bookIdList") List<String> bookIdList);


	/**
	 * 資料名を条件に貸出可能な資料情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.delete_flag = 0 AND library.delete_flag = 0 AND NOT exists (SELECT 1 FROM rental WHERE delete_flag = 0 AND rental.book_id = library.book_id) AND book.title LIKE :title% ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findRentalBookListByTitle(@Param("title") String title);

	/**
	 * 資料IDと資料名を条件に貸出可能な資料情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.delete_flag = 0 AND library.delete_flag = 0 AND NOT exists (SELECT 1 FROM rental WHERE delete_flag = 0 AND rental.book_id = library.book_id) AND library.book_id = :bookId AND book.title LIKE :title% ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findRentalBookListByBookIdAndTitle(@Param("bookId") int bookId,
			@Param("title") String title);

	/**
	 * 資料IDと削除フラグを条件に資料蔵書情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn AND library.delete_flag = :delete_flag WHERE library.book_id = :book_id AND library.delete_flag = :delete_flag ORDER BY library.book_id DESC", nativeQuery = true)
	BookLibraryEntity findByBookIdAndDeleteFlag(@Param("book_id") long book_id,
			@Param("delete_flag") String delete_flag);

	/**
	 * 二次開発 ISBNを条件に資料蔵書情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE book.delete_flag = 0 AND library.delete_flag = 0 AND NOT exists (SELECT 1 FROM rental WHERE delete_flag = 0 AND rental.book_id = library.book_id) AND book.isbn = :isbn ORDER BY library.book_id DESC", nativeQuery = true)
	List<BookLibraryEntity> findByBookIsbn(@Param("isbn") String isbn);

	/**
	 * 二次開発 資料IDを条件に資料蔵書情報を取得する。
	 */
	@Query(value = "SELECT library.book_id, library.isbn, book.category, book.title, book.author, book.publisher, book.release_date, library.arrival_date, library.disposal_date, library.disposal_note, book.exclusive_key AS book_exclusive_key, library.exclusive_key AS library_exclusive_key FROM book INNER JOIN library ON library.isbn = book.isbn WHERE library.book_id = :bookId", nativeQuery = true)
	List<BookLibraryEntity> findBookLibraryByBookId(@Param("bookId") int bookId);

}
