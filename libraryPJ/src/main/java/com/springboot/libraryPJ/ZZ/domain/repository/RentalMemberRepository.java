package com.springboot.libraryPJ.ZZ.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.RentalMemberEntity;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)
public interface RentalMemberRepository extends JpaRepository<RentalMemberEntity, Integer> {

	/*
	 * 会員テーブル キー：会員ID 取得：名前 蔵書テーブル キー：資料ID 取得：ISBN 資料テーブル キー：ISBN 取得：資料名 貸出テーブル
	 * キー：会員ID、資料ID 取得：貸出ID キー：貸出ID 取得：貸出期限
	 *
	 */
	@Query(value = "SELECT rental.RENTAL_ID, rental.member_id,rental.book_id, member.name, library.isbn, book.title, rental.rental_due_date, trunc(sysdate) - trunc(rental.rental_due_date) AS over_due_days "
			+ "FROM rental " + "LEFT OUTER JOIN " + "member ON member.member_id= rental.member_id " + "LEFT OUTER JOIN "
			+ "library ON library.book_id = rental.book_id " + "LEFT OUTER JOIN " + "book ON library.isbn = book.isbn "
			+ "WHERE  " + "rental.rental_id in (:rentalId)" + "and rental.delete_flag = '0'", nativeQuery = true)
	List<RentalMemberEntity> getReturnBook(@Param("rentalId") List<String> rentalId);

	/*
	 * 会員テーブル キー：会員ID 取得：名前 蔵書テーブル キー：資料ID 取得：ISBN 資料テーブル キー：ISBN 取得：資料名 貸出テーブル
	 * キー：会員ID、資料ID 取得：貸出ID キー：貸出ID 取得：貸出期限
	 */
	@Query(value = "SELECT rental.RENTAL_ID, rental.member_id,rental.book_id, member.name, library.isbn, book.title, rental.rental_due_date, trunc(sysdate) - trunc(rental.rental_due_date) AS over_due_days "
			+ "FROM rental " + "LEFT OUTER JOIN " + "member ON member.member_id　= rental.member_id "
			+ "LEFT OUTER JOIN " + "library ON library.book_id = rental.book_id " + "LEFT OUTER JOIN "
			+ "book ON library.isbn = book.isbn " + "WHERE " + "rental.rental_id in :rentalId "
			+ "and rental.delete_flag = '1' order by rental_due_date desc", nativeQuery = true)
	List<RentalMemberEntity> getReturnedBook(@Param("rentalId") List<String> rentalId);
}
