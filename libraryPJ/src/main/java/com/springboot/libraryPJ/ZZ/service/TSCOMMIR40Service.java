package com.springboot.libraryPJ.ZZ.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.dto.TSCOMMIROutDto;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

/**
 * 会員登録完了画面
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSCOMMIR40Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberRepository memberRepository;

	/**
	 *
	 * 会員登録内容取得
	 *
	 * @param memberId
	 * @return
	 */
	public TSCOMMIROutDto selectRegistMember(String memberId) {

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// 会員登録内容取得
		List<MemberEntity> memberEntityList = memberRepository.findByMemberIdAndDeleteFlag(Integer.parseInt(memberId),
				CommonConstants.NOT_DELETE);
		MemberEntity memberEntity = memberEntityList.get(0);

		TSCOMMIROutDto outDto = new TSCOMMIROutDto();
		outDto.setRecordCnt(memberEntityList.size());
		outDto.setTodayDate(CommonUtil.dateToString(memberEntity.getRegisterDate()));
		outDto.setBirthday(CommonUtil.dateToString(memberEntity.getBirthday()));
		outDto.setMemberId(String.valueOf(memberEntity.getMemberId()));

		outDto.setPassword("password");
		outDto.setMemberClass(memberEntity.getMemberClass());
		outDto.setName(memberEntity.getName());
		outDto.setPostalCode(memberEntity.getPostalCode());
		outDto.setAddress(memberEntity.getAddress());
		outDto.setTelNumber(memberEntity.getTelNumber());
		outDto.setEmail(memberEntity.getEmail());
		outDto.setRegistId(Long.toString(memberEntity.getRegisterId()));
		return outDto;
	}

}
