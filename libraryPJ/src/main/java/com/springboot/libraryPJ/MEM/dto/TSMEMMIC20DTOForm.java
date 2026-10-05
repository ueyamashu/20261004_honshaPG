package com.springboot.libraryPJ.MEM.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSMEMMIC20DTOForm {

	// 会員ID
	private String memberId;

	// 会員区分
	private String memberKbn;

	// 名前
	private String memberName;

	// 生年月日
	private String birthDate;

	// 郵便番号
	private String postNo;

	// 郵便番号１
	private String postNo1;

	// 郵便番号２
	private String postNo2;

	// 住所
	private String address;

	// 電話番号1
	private String phoneNo1;

	// 電話番号2
	private String phoneNo2;

	// 電話番号3
	private String phoneNo3;

	// 電話番号
	private String phoneNo;

	// メールアドレス（変更前）
	private String mailAddressMoto;

	// メールアドレス
	private String mailAddress;

	// 退会年月日
	private String withdrawalDate;
}
