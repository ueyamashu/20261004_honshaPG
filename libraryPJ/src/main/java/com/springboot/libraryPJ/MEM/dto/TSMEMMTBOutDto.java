package com.springboot.libraryPJ.MEM.dto;

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会員一覧画面OUTDTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TSMEMMTBOutDto {
	private int memberId;

	private String name;

	private String email;

	public TSMEMMTBOutDto(MemberEntity member) {
		this.memberId = member.getMemberId();
		this.name = member.getName();
		this.email = member.getEmail();
	}
}
