package com.springboot.libraryPJ.MEM.service;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20OutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;

/*
 * 会員パスワード更新完了
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSMEMMPC40Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberRepository memberRepository;

	public TSMEMMPC20OutDto searchChangedMember(String memberId, String password) {

		logger.debug("memberId:" + memberId);
		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// SHA256対応
		String pwSHA256 = DigestUtils.sha256Hex(password);
		logger.debug("password SHA-256:" + pwSHA256);

		// 会員情報取得
		List<MemberEntity> memberEntityList = memberRepository.findByMemberIdAndPasswordAndDeleteFlag(
				Integer.parseInt(memberId), pwSHA256, CommonConstants.NOT_DELETE);

		MemberEntity memberEntity = memberEntityList.get(0);

		TSMEMMPC20OutDto outDto = new TSMEMMPC20OutDto();

		outDto.setPassword(memberEntity.getPassword());
		outDto.setRecordCnt(memberEntityList.size());
		outDto.setMemberId(String.valueOf(memberEntity.getMemberId()));
		outDto.setName(memberEntity.getName());
		outDto.setExclusiveKey(memberEntity.getExclusiveKey());

		return outDto;
	}

}
