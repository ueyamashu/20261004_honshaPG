package com.springboot.libraryPJ.ZZ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会員登録確認画面
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSCOMMIRDto {

	// 会員区分
	private String memberClass;

	// 氏名
	private String name;

	// 生年月日
	private String birthday;

	// 郵便番号１
	private String postalCode1;

	// 郵便番号２
	private String postalCode2;

	// 郵便番号（全体）
	private String postalCode;

	// 住所
	private String address;

	// 電話番号１
	private String telNumber1;

	// 電話番号２
	private String telNumber2;

	// 電話番号３
	private String telNumber3;

	// 電話番号（全体）
	private String telNumber;

	// E-Mail
	private String email;

	// パスワード
	private String password;

	// 暗号化パスワード
	private String encryptPassword;

	// 会員ID
	private String memberId;

	/**
	 * 電話番号を返却
	 *
	 * @return
	 */
	public String getFullTelNumber() {
		return String.format("%s-%s-%s", telNumber1, telNumber2, telNumber3);

	}

	/**
	 * 郵便番号を返却
	 *
	 * @return
	 */
	public String getFullPostalCode() {
		return String.format("%s-%s", postalCode1, postalCode2);

	}

}
