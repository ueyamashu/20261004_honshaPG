package com.springboot.libraryPJ.MEM.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会員一覧画面DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSMEMMTBDto {

	private String memberId;

	private String name;

	private String email;

}
