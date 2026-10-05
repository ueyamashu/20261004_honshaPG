package com.springboot.libraryPJ.REN.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.REN.dto.TSRENRBRoutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalMemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalMemberRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 資料貸出確認
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSRENRBR40Service {

	@Autowired
	RentalMemberRepository rentalMemberRepository;

	@Autowired
	MemberRepository memberRepository;

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 会員IDから会員情報を取得する
	 *
	 * @param memberId
	 * @return
	 */
	public List<MemberEntity> getMemberInfo(String memberId) {

		logger.debug("getBookInfo");

		// データ取得
		List<MemberEntity> memberList = memberRepository.findByMemberIdAndDeleteFlag(Integer.valueOf(memberId),
				CommonConstants.NOT_DELETE);
		return memberList;
	}

	/**
	 * 貸出IDから貸出情報を取得する
	 *
	 * @param rentalIdList
	 * @return
	 */
	public List<TSRENRBRoutDto> getReturnBook(List<Integer> rentalIdList) {

		logger.debug("getBookInfo");

		// リストのデータ型変更
		List<String> rentalIds = rentalIdList.stream().map(i -> i.toString()).collect(Collectors.toList());

		// 貸出情報を取得する
		List<RentalMemberEntity> rentalList = rentalMemberRepository.getReturnBook(rentalIds);

		List<TSRENRBRoutDto> outDtoList = new ArrayList<TSRENRBRoutDto>();
		if (rentalList.size() == 0 || rentalList == null) {
			TSRENRBRoutDto outDto = new TSRENRBRoutDto();
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM017E,
					new String[] { CommonConstants.STRING_BOOK_JOHO }));
			outDtoList.add(outDto);
		}
		outDtoList = rentalList.stream().map(o -> new TSRENRBRoutDto(o)).collect(Collectors.toList());
		outDtoList.sort(Comparator.comparing(TSRENRBRoutDto::getBookId).reversed());

		return outDtoList;
	}
}
