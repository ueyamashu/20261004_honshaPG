package com.springboot.libraryPJ.BKS.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資料入庫画面用のinDto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSBKSBIS30outDto {

	// ISBN番号
	private String isbn;
	
	private List<Integer> bookIdList;
}
