
package com.springboot.libraryPJ.MEM.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSMEMMID30outDTO {

	// 会員ID
	private String memberId;

	// 会員区分
	private String membership;

	// 名前
	private String memberName;

	// 生年月日
	private String birthday;

	// 郵便番号
	private String postalCode;

	// 住所
	private String address;

	// 電話番号
	private String telNumber;

	// メールアドレス
	private String email;

	// レコード件数
	private int recordCnt;

	// 退会年月日
	private String withdrawDate;

	// 削除フラグ
	private String deleteFlag;
}
