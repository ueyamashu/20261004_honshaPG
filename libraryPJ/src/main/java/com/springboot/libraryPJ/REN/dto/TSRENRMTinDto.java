package com.springboot.libraryPJ.REN.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出会員入力DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRENRMTinDto {

	private String memberId;

	private String name;

	private String deleteFlag;

}
