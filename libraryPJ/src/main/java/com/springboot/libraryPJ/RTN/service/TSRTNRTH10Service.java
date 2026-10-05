package com.springboot.libraryPJ.RTN.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.libraryPJ.RTN.dto.TSRTNRTHinDto;
import com.springboot.libraryPJ.RTN.dto.TSRTNRTHoutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;

@Service
public class TSRTNRTH10Service {
	@Autowired
	RentalRepository rentalRepository;

	// 貸出返却一覧の初期表示用データを取得する
	public List<TSRTNRTHoutDto> getReturnHistoryList() {
		List<RentalEntity> returnList = rentalRepository.findAllOrdered();

		// 取得した情報をDTOに格納する
		List<TSRTNRTHoutDto> outDto = returnList.stream().map(o -> new TSRTNRTHoutDto(o)).collect(Collectors.toList());
		return outDto;
	}

	// 貸出返却一覧の検索結果を取得する
	public List<TSRTNRTHoutDto> searchReturnHistoryList(TSRTNRTHinDto inDto) {
		List<RentalEntity> returnList = null;

		if (StringUtils.isEmpty(inDto.getMemberId()) && StringUtils.isEmpty(inDto.getBookId())) {
			// 検索条件を指定しない場合、全件検索
			returnList = rentalRepository.findAllOrdered();

		} else if (!StringUtils.isEmpty(inDto.getMemberId()) && StringUtils.isEmpty(inDto.getBookId())) {
			// 会員IDのみ入力した場合
			returnList = rentalRepository.findByMemberId(inDto.getMemberId());

		} else if (StringUtils.isEmpty(inDto.getMemberId()) && !StringUtils.isEmpty(inDto.getBookId())) {
			// 資料IDのみ入力した場合
			returnList = rentalRepository.findByBookId(inDto.getBookId());

		} else if (!StringUtils.isEmpty(inDto.getMemberId()) && !StringUtils.isEmpty(inDto.getBookId())) {
			// 会員IDと資料IDを入力した場合
			returnList = rentalRepository.findByMemberIdAndBookId(inDto.getMemberId(), inDto.getBookId());
		}

		// 取得した情報をDTOに格納する
		List<TSRTNRTHoutDto> outDto = returnList.stream().map(o -> new TSRTNRTHoutDto(o)).collect(Collectors.toList());
		return outDto;
	}
}
