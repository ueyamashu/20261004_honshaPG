package com.springboot.libraryPJ.REN.dto;

import java.util.List;

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 貸出会員出力DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSRENRMToutDto {

	private int resultCd;

	private String errmsg;

	private List<MemberEntity> memberList;

}
