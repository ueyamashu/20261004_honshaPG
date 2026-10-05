package com.springboot.libraryPJ.BKS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料情報登録確認outDto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIR30outDto {
	// 結果コード
	private int resultCd;
	// エラーメッセージ
	private String errmsg;
}
