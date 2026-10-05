package com.springboot.libraryPJ.RTN.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.RTN.dto.TSRTNRTBinDto;
import com.springboot.libraryPJ.RTN.dto.TSRTNRTBoutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class TSRTNRTB10Service {
	@Autowired
	RentalRepository rentalRepository;

	/**
	 * 資料返却一覧取得
	 *
	 * @param deleteFlag
	 * @return
	 */
	public List<TSRTNRTBoutDto> getReturnBookList(String deleteFlag) {
		List<RentalEntity> rentalList = rentalRepository.findByDeleteFlag(deleteFlag);

		// 取得した情報をDTOに格納する
		List<TSRTNRTBoutDto> outDto = rentalList.stream().map(o -> new TSRTNRTBoutDto(o)).collect(Collectors.toList());
		return outDto;
	}

	/**
	 * 資料返却一覧検索
	 *
	 * @param inDto
	 * @return
	 */
	public List<TSRTNRTBoutDto> searchRtnBookList(TSRTNRTBinDto inDto) {
		List<RentalEntity> rentalList = null;

		if (StringUtils.isEmpty(inDto.getMemberId()) && StringUtils.isEmpty(inDto.getBookId())) {
			// 検索条件を指定しない場合、全件検索
			rentalList = rentalRepository.findByDeleteFlag(inDto.getDeleteFlag());

		} else if (!StringUtils.isEmpty(inDto.getMemberId()) && StringUtils.isEmpty(inDto.getBookId())) {
			// 会員IDのみ入力した場合
			rentalList = rentalRepository.findByMemberIdAndDeleteFlag(inDto.getMemberId(), inDto.getDeleteFlag());

		} else if (StringUtils.isEmpty(inDto.getMemberId()) && !StringUtils.isEmpty(inDto.getBookId())) {
			// 資料IDのみ入力した場合
			rentalList = rentalRepository.findByBookIdAndDeleteFlag(inDto.getBookId(), inDto.getDeleteFlag());

		} else if (!StringUtils.isEmpty(inDto.getMemberId()) && !StringUtils.isEmpty(inDto.getBookId())) {
			// 会員IDと資料IDを入力した場合
			rentalList = rentalRepository.findByMemberIdAndBookIdAndDeleteFlag(inDto.getMemberId(), inDto.getBookId(),
					inDto.getDeleteFlag());
		}

		// 取得した情報をDTOに格納する
		List<TSRTNRTBoutDto> outDto = rentalList.stream().map(o -> new TSRTNRTBoutDto(o)).collect(Collectors.toList());
		return outDto;
	}
}
