package com.springboot.libraryPJ.REN.dto;

import java.util.List;

import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出資料出力DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRENRBToutDto {

	private int resultCd;

	private String errmsg;

	private List<BookLibraryEntity> bookLibraryList;

}