package com.springboot.libraryPJ.ZZ.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	List<MemberEntity> findByMemberIdAndPasswordAndDeleteFlag(int memberId, String password, String deleteFlag);

	/**
	 * 削除フラグを条件に会員情報を取得する。
	 */
	@Query(value = "SELECT * FROM member WHERE delete_flag = :deleteFlag ORDER BY name ASC", nativeQuery = true)
	List<MemberEntity> findByDeleteFlag(String deleteFlag);

	/**
	 * 会員ID（完全一致）と削除フラグを条件に会員情報リストを取得する。
	 */
	List<MemberEntity> findByMemberIdAndDeleteFlag(int memberId, String deleteFlag);

	/**
	 * 名前（前方一致）と削除フラグを条件に会員情報リストを取得する。
	 */
	@Query(value = "SELECT * FROM member WHERE name LIKE :name% AND delete_flag = :deleteFlag ORDER BY name ASC", nativeQuery = true)
	List<MemberEntity> findByNameAndDeleteFlag(@Param("name") String name, @Param("deleteFlag") String deleteFlag);

	/**
	 * 会員ID（完全一致）と名前（前方一致）、削除フラグを条件に会員情報リストを取得する。
	 */
	@Query(value = "SELECT * FROM member WHERE member_id = :memberId AND name LIKE :name% AND delete_flag = :deleteFlag", nativeQuery = true)
	List<MemberEntity> findByMemberIdAndNameAndDeleteFlag(@Param("memberId") int memberId, @Param("name") String name,
			@Param("deleteFlag") String deleteFlag);

	/**
	 * メールアドレスと削除フラグを条件に会員情報リストを取得する。
	 */
	@Query(value = "SELECT * FROM member WHERE email = :email AND delete_flag = :deleteFlag ORDER BY name ASC", nativeQuery = true)
	List<MemberEntity> findByEmailAndDeleteFlag(@Param("email") String email, @Param("deleteFlag") String deleteFlag);

	/**
	 * 名前（前方一致）とメールアドレスと削除フラグを条件に会員情報リストを取得する。
	 */
	@Query(value = "SELECT * FROM member WHERE name LIKE :name% AND email = :email AND delete_flag = :deleteFlag ORDER BY name ASC", nativeQuery = true)
	List<MemberEntity> findByNameAndByEmailAndDeleteFlag(@Param("name") String name, @Param("email") String email,
			@Param("deleteFlag") String deleteFlag);

	/**
	 * 会員ID(完全一致)とパスワードと排他キーと削除フラグを条件に会員情報リストを取得する。
	 */
	@Query(value = "SELECT * FROM member WHERE member_id = :memberId AND password = :password "
			+ "AND exclusive_key = :exclusiveKey AND delete_flag = :deleteFlag ORDER BY name ASC", nativeQuery = true)
	List<MemberEntity> findMemberByIdPasswordKeyAndFlag(@Param("memberId") int memberId,
			@Param("password") String password, @Param("exclusiveKey") long exclusiveKey,
			@Param("deleteFlag") String deleteFlag);

}
