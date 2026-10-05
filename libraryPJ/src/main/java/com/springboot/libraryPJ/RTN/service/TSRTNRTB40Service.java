package com.springboot.libraryPJ.RTN.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.libraryPJ.RTN.dto.TSRTNRTB40outDto;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalMemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalMemberRepository;

@Service
public class TSRTNRTB40Service {

	@Autowired
	RentalMemberRepository rmRep;

	/**
	 * 返却した資料の情報を取得する
	 *
	 * @param ids
	 * @return
	 */
	public List<TSRTNRTB40outDto> getReturnedBook(List<String> ids) {

		List<RentalMemberEntity> returnCompList = rmRep.getReturnedBook(ids);

		List<TSRTNRTB40outDto> outDto = new ArrayList<TSRTNRTB40outDto>();

		// DBから取得した情報をDTOに格納する
		if (returnCompList.size() != 0 && returnCompList != null) {
			outDto = returnCompList.stream().map(o -> new TSRTNRTB40outDto(o)).collect(Collectors.toList());
		}

		return outDto;
	}
}
