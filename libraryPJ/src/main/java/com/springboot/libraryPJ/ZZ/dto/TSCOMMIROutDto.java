package com.springboot.libraryPJ.ZZ.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会員登録完了画面OUTDTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSCOMMIROutDto {

	// 会員区分
	private String memberClass;

	// 氏名
	private String name;

	// 生年月日
	private String birthday;

	// 郵便番号
	private String postalCode;

	// 住所
	private String address;

	// 電話番号
	private String telNumber;

	// E-Mail
	private String email;

	// システム日付
	private String todayDate;

	// 会員ID
	private String memberId;

	// レコード件数
	private int recordCnt;

	// パスワード
	private String password;

	// 登録ID
	private String registId;
}
