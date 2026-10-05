package com.springboot.libraryPJ.ZZ.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class CheckerUtil {

	@Size(min = 1, max = 10, message = "{0}は1～10桁を入力してください")
	@NotEmpty(message = "入力してください")
	private String id;

	// チェックタイプ：半角数値
	public static final String CHECK_TYPE_HALF_NUM = "^[0-9]+$";
	// チェックタイプ：日付
	public static final String CHECK_TYPE_DATE = CommonConstants.REGEX_DATE_FORMAT;
	// チェックタイプ：全角漢字
	public static final String CHECK_TYPE_FULL_STRING = CommonConstants.REGEX_TEXT_KANJI;
	// チェックタイプ：全角カタカナ
	public static final String CHECK_TYPE_FULL_KATAKANA = CommonConstants.REGEX_TEXT_FULL_KATAKANA;
	// チェックタイプ：半角英数字
	public static final String CHECK_TYPE_HALF_ENGNUM = CommonConstants.REGEX_HALF_WIDTH_ENG_NUMBER;

	@AssertTrue(message = "{t001.validation.multiRequired}")

	/**
	 * 入力値が必須項目がnull、空文字、全て半角スペース、全て全角スペース、タブのいずれかではないかチェックする
	 *
	 * @param value
	 * @return
	 */
	public static boolean dataRequiredCheck(String value) {
		if (value == null) {
			return false;
		}

		if (value.isEmpty()) {
			return false;
		}

		if (value.matches("[　]+")) {
			return false;
		}

		if (value.matches("[ ]+")) {
			return false;
		}

		return true;
	}

	/**
	 * 入力値のデータ型をチェックする<br>
	 * パラメータのデータ型により入力値が半角数値、日付、全角漢字、全角カタカナ、半角英数字のいずれかをチェックする
	 *
	 * @param value
	 * @param type  REGEX [CHECK_TYPE_HALF_NUM, CHECK_TYPE_DATE,
	 *              CHECK_TYPE_FULL_STRING, CHECK_TYPE_FULL_KATAKANA,
	 *              CHECK_TYPE_HALF_ENGNUM]
	 * @return
	 */
	public static boolean dataTypeCheck(String value, String type) {

		if (value == null) {
			return false;
		}

		if (value.matches(type)) {
			return true;
		}

		return false;
	}

	/**
	 * 日付のフォーマットが「yyyy/mm/dd」かをチェックする
	 *
	 * @param value チェック対象
	 * @return チェック結果 （false:チェックNG true:チェックOK）
	 */
	public static boolean dateFormatCheck(String value) {

		if (value == null) {
			return false;
		}

		// yyyy/MM/dd
		if (value.matches(CommonConstants.REGEX_DATE_FORMAT)) {
			return true;
		}

		return false;
	}

	/**
	 * 入力値がパラメータのコード値の配列に存在するかをチェックする<br>
	 * パラメータのコード値はCommonConstantsクラスの定数を使用する
	 *
	 * @param value
	 * @param code  [BOOK_TABLE_CATEGORY_CODE_VALUE,
	 *              MEMBER_TABLE_MEMBERSHIP_CODE_VALUE]
	 * @return
	 */
	public static boolean dataExistCheck(String value, String code) {

		if (value == null) {
			return false;
		}

		if (value.matches(code)) {
			return true;
		}

		return false;
	}

	/**
	 * 入力値が最小桁数と最大桁数の以内かをチェックする
	 *
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean stringLengthCheck(String value, int min, int max) {

		int length = 0;

		if (value == null) {
			length = 0;
		}

		length = value.length();

		if (length >= min && length <= max) {
			return true;
		}

		return false;
	}

	/**
	 * 入力値が最小桁数と最大桁数の以内かをチェックする。（最大桁数の省略版）
	 *
	 * @param value
	 * @param min
	 * @return
	 */
	public static boolean stringLengthCheck(String value, int min) {

		int length = 0;

		if (value == null) {
			length = 0;
		}

		length = value.length();

		if (length >= min) {
			return true;
		}

		return false;
	}

	/**
	 * 入力文字列が「yyyy/MM/dd」形式ではない場合、例外発生（ParseException）。
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int compareToDate(String date) throws ParseException {

		// 現在時刻でカレンダーのインスタンスを取得
		Calendar cal = Calendar.getInstance();

		// SimpleDateFormatで書式を指定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		// Calendarの日付をSimpleDateFormatで指定した書式で文字列に変換
		String now = sdf.format(cal.getTime());

		Date inputdate = sdf.parse(date);
		Date sysdate = sdf.parse(now);

		int compareResult = sysdate.compareTo(inputdate);

		if (compareResult == 1) {

			// 過去日
			return CommonConstants.CHECK_RESULT_BEFORE_DATE;

		} else if (compareResult == 0) {

			// 現在日
			return CommonConstants.CHECK_RESULT_TODAY_DATE;

		} else {

			// 未来日
			return CommonConstants.CHECK_RESULT_AFTER_DATE;

		}

	}

	/**
	 * Eメールアドレスのフォーマットが正しいかをチェックする。
	 *
	 * @param email
	 * @return
	 */
	public static boolean emailAddressFormatCheck(String email) {

		if (email == null) {
			return false;
		}

		if (email.matches(CommonConstants.REGEX_EMAILADDRESS_FORMAT)) {
			return true;
		}

		return false;
	}

	/**
	 * 「00-0000-0000」形式又は、「000-0000-0000」形式であるかをチェックする。 図書館管理システムでは携帯電話番号のみ許容しているため、
	 * 「00-0000-0000」形式はNGとして判断する。
	 *
	 * @param telnum
	 * @return
	 */
	public static boolean telValueCheck(String telnum) {

		if (telnum == null) {
			return false;
		}

		if (telnum.matches(CommonConstants.REGEX_TEL_FORMAT_1)) {
			return false;
		}

		if (telnum.matches(CommonConstants.REGEX_TEL_FORMAT_2)) {
			return true;
		}

		return false;

	}

	/**
	 * 「000-0000」形式であるかをチェックする。
	 *
	 * @param postNum
	 * @return
	 */
	public static boolean postalValueCheck(String postNum) {

		if (postNum == null) {
			return false;
		}

		if (postNum.matches(CommonConstants.REGEX_POSTAL_FORMAT)) {
			return true;
		}

		return false;
	}

}
