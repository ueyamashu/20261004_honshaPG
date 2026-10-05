package com.springboot.libraryPJ.ZZ.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import com.springboot.libraryPJ.ZZ.domain.entity.BookEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;

import jakarta.servlet.http.HttpSession;

public class CommonUtil {

	static public final String DATE_PATTERN = "yyyy/MM/dd";

	/**
	 * 郵便番号編集
	 *
	 * 140-1234のように結合する
	 *
	 * @param postNo1
	 * @param postNo2
	 * @return
	 */
	public static String makePostNo(String postNo1, String postNo2) {

		return String.format("%s-%s", postNo1, postNo2);
	}

	/**
	 * 電話番号編集
	 *
	 * 080-1234-1234のように結合する
	 *
	 * @param phoneNo1
	 * @param phoneNo2
	 * @param phoneNo3
	 * @return
	 */
	public static String makePhoneNo(String phoneNo1, String phoneNo2, String phoneNo3) {

		return String.format("%s-%s-%s", phoneNo1, phoneNo2, phoneNo3);
	}

	/**
	 * sql.DateタイプをStringタイプに変換
	 *
	 * @param sqlDate
	 * @return
	 */
	public static String dateToString(Date sqlDate) {
		String str;
		if (sqlDate == null) {
			str = null;
		} else {
			str = new SimpleDateFormat(DATE_PATTERN).format(sqlDate);
		}
		return str;
	}

	/**
	 * Stringタイプをsql.Dateタイプに変換
	 *
	 * @param stringDate
	 * @return
	 */
	public static Date stringToDate(String stringDate) {
		Date date;
		if (stringDate == null) {
			date = null;
		} else {
			date = Date.valueOf(stringDate.replace("/", "-"));
		}
		return date;
	}

	/**
	 * @param entity
	 * @param session
	 * @return
	 */
	public static Object setWhoInfoInsert(Object entity, HttpSession session) {

		long loginId = 9999;

		if (session.getAttribute("memberId") != null
				&& StringUtils.isNotBlank((String) session.getAttribute("memberId"))) {
			loginId = Long.parseLong((String) session.getAttribute("memberId"));
		}

		long miliseconds = System.currentTimeMillis();
		Date date = new Date(miliseconds);

		if (entity instanceof BookEntity) {
			((BookEntity) entity).setDeleteFlag("0");
			((BookEntity) entity).setRegisterId(loginId);
			((BookEntity) entity).setRegisterDate(date);
			((BookEntity) entity).setUpdateId(loginId);
			((BookEntity) entity).setUpdateDate(date);
			((BookEntity) entity).setExclusiveKey(0);
		}
		if (entity instanceof LibraryEntity) {
			((LibraryEntity) entity).setDeleteFlag("0");
			((LibraryEntity) entity).setRegisterId(loginId);
			((LibraryEntity) entity).setRegisterDate(date);
			((LibraryEntity) entity).setUpdateId(loginId);
			((LibraryEntity) entity).setUpdateDate(date);
			((LibraryEntity) entity).setExclusiveKey(0);
		}

		if (entity instanceof MemberEntity) {
			((MemberEntity) entity).setDeleteFlag("0");
			((MemberEntity) entity).setRegisterId(loginId);
			((MemberEntity) entity).setRegisterDate(date);
			((MemberEntity) entity).setUpdateId(loginId);
			((MemberEntity) entity).setUpdateDate(date);
			((MemberEntity) entity).setExclusiveKey(0);
		}

		if (entity instanceof RentalEntity) {
			((RentalEntity) entity).setDeleteFlag("0");
			((RentalEntity) entity).setRegisterId(loginId);
			((RentalEntity) entity).setRegisterDate(date);
			((RentalEntity) entity).setUpdateId(loginId);
			((RentalEntity) entity).setUpdateDate(date);
			((RentalEntity) entity).setExclusiveKey(0);
		}

		return entity;
	}

	/**
	 * @param entity
	 * @param session
	 * @return
	 */
	public static Object setWhoInfoUpdate(Object entity, HttpSession session) {

		long loginId = 9999;

		if (session.getAttribute("memberId") != null
				&& StringUtils.isNotBlank((String) session.getAttribute("memberId"))) {
			loginId = Long.parseLong((String) session.getAttribute("memberId"));
		}

		long miliseconds = System.currentTimeMillis();
		Date date = new Date(miliseconds);

		if (entity instanceof BookEntity) {
			((BookEntity) entity).setUpdateId(loginId);
			((BookEntity) entity).setUpdateDate(date);
			((BookEntity) entity).setExclusiveKey(((BookEntity) entity).getExclusiveKey() + 1);
		}
		if (entity instanceof LibraryEntity) {
			((LibraryEntity) entity).setUpdateId(loginId);
			((LibraryEntity) entity).setUpdateDate(date);
			((LibraryEntity) entity).setExclusiveKey(((LibraryEntity) entity).getExclusiveKey() + 1);
		}

		if (entity instanceof MemberEntity) {
			((MemberEntity) entity).setUpdateId(loginId);
			((MemberEntity) entity).setUpdateDate(date);
			((MemberEntity) entity).setExclusiveKey(((MemberEntity) entity).getExclusiveKey() + 1);
		}

		if (entity instanceof RentalEntity) {
			((RentalEntity) entity).setUpdateId(loginId);
			((RentalEntity) entity).setUpdateDate(date);
			((RentalEntity) entity).setExclusiveKey(((RentalEntity) entity).getExclusiveKey() + 1);
		}

		return entity;
	}

	/**
	 * ページネーションの情報を編集、モデルへの格納
	 * 
	 * @param page
	 * @param size
	 * @param model
	 * @param dbList
	 * @param address
	 */
	public static void pageModule(int page, int size, Model model, List<Object> dbList, String outDtoListNm,
			String address) {

		// 一覧データリスト＝0件の場合
		if (dbList == null || dbList.size() == 0) {
			model.addAttribute(outDtoListNm, dbList);

			model.addAttribute("currentPage", 1);
			model.addAttribute("totalPages", 1);
			model.addAttribute("address", address);

			model.addAttribute("firstNm", 1);
			model.addAttribute("lastNm", 1);
			return;
		}

		Pageable pageRequest = PageRequest.of(page - 1, size);

		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), dbList.size());

		List<Object> subList = dbList.subList(start, end);

		Page<Object> pageList = new PageImpl<Object>(subList, pageRequest, dbList.size());

		model.addAttribute(outDtoListNm, pageList);

		// 表示されるページ設定
		int firstNm;
		int lastNm;

		if (pageList.getTotalPages() <= 5) {
			firstNm = 1;
			lastNm = pageList.getTotalPages();
		} else {
			firstNm = page - 2 > 1 ? (page > pageList.getTotalPages() - 2 ? pageList.getTotalPages() - 4 : page - 2)
					: 1;
			lastNm = page + 2 > pageList.getTotalPages() ? pageList.getTotalPages() : (page + 2 > 5 ? page + 2 : 5);
		}

		model.addAttribute("firstNm", firstNm);
		model.addAttribute("lastNm", lastNm);

		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", pageList.getTotalPages());
		model.addAttribute("address", address);
	}
}
