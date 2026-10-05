package com.springboot.libraryPJ.MEM.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.MEM.dto.TSMEMMIC20outDTO;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

/**
 * 会員情報更新
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSMEMMIC20Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberRepository memberRepository;

	/**
	 * @param memberId
	 * @return
	 */
	public TSMEMMIC20outDTO searchLoginMember(String memberId) {

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// 会員情報取得
		List<MemberEntity> memberEntityList = memberRepository.findByMemberIdAndDeleteFlag(Integer.parseInt(memberId),
				CommonConstants.NOT_DELETE);
		MemberEntity memberEntity = memberEntityList.get(0);

		TSMEMMIC20outDTO outDto = new TSMEMMIC20outDTO();
		outDto.setRecordCnt(memberEntityList.size());
		outDto.setMemberId(String.valueOf(memberEntity.getMemberId()));
		outDto.setAddress(memberEntity.getAddress());
		outDto.setBirthDate(CommonUtil.dateToString(memberEntity.getBirthday()));
		outDto.setMailAddress(memberEntity.getEmail());
		outDto.setMemberKbn(memberEntity.getMemberClass());
		outDto.setMemberName(memberEntity.getName());
		outDto.setPhoneNo(memberEntity.getTelNumber());
		outDto.setPostNo(memberEntity.getPostalCode());

		return outDto;
	}

}
