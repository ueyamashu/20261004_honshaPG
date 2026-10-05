package com.springboot.libraryPJ.BKS.dto;

import java.util.List;

import com.springboot.libraryPJ.ZZ.domain.entity.DelayEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 延滞情報一覧画面用DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBOToutDto {
	
	// 遅延ビューの抽出リスト
	private List<DelayEntity> delayList;
	// 連絡ボタンフラグ
	private String delayClearFlg;

}