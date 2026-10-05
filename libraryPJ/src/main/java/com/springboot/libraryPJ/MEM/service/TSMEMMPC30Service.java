package com.springboot.libraryPJ.MEM.service;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20Dto;
import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20OutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

/*
 * 会員パスワード更新確認
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSMEMMPC30Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberRepository memberRepository;

	/**
	 * 会員情報取得
	 * 
	 * @param 画面入力値
	 * @return
	 */
	public List<MemberEntity> searchLoginMember(TSMEMMPC20Dto tsmemmpc20Dto) {

		// SHA256対応 -暗号的ハッシュ関数 256bitの暗号化したパスワード
		String pwSHA256 = DigestUtils.sha256Hex(tsmemmpc20Dto.getMemberPassword());

		List<MemberEntity> member = memberRepository.findByMemberIdAndPasswordAndDeleteFlag(
				Integer.parseInt(tsmemmpc20Dto.getMemberId()), pwSHA256, CommonConstants.NOT_DELETE);

		return member;
	}

	/**
	 * パスワードの更新
	 * 
	 * @param tsmemmpc20Dto
	 * @param exclusiveKey
	 * @return
	 * @throws Exception
	 */
	public void updatePassword(TSMEMMPC20Dto tsmemmpc20Dto, HttpSession session) throws Exception {

		// log出力
		logger.debug("memberId:" + tsmemmpc20Dto.getMemberId());
		logger.debug("Entity確認用　memberId:" + tsmemmpc20Dto.getMemberId());
		logger.debug("deleteFlag:" + CommonConstants.NOT_DELETE);
		logger.debug("MemberPasswordNew:" + tsmemmpc20Dto.getMemberPasswordNew());
		logger.debug("exclusiveKey:" + tsmemmpc20Dto.getExclusiveKey());
		logger.debug("MemberPassword:" + tsmemmpc20Dto.getMemberPassword());

		TSMEMMPC20OutDto outDto = new TSMEMMPC20OutDto();

		// SHA256対応
		String pwSHA256Old = DigestUtils.sha256Hex(tsmemmpc20Dto.getMemberPassword());
		logger.debug("MemberPassword SHA-256:" + pwSHA256Old);

		List<MemberEntity> memberList = memberRepository.findMemberByIdPasswordKeyAndFlag(
				Integer.parseInt(tsmemmpc20Dto.getMemberId()), pwSHA256Old, tsmemmpc20Dto.getExclusiveKey(),
				CommonConstants.NOT_DELETE);

		if (memberList == null || memberList.size() == 0) {
			outDto.setRecordCnt(-1);
		}

		MemberEntity entity = memberList.get(0);

		// SHA256対応
		String pwSHA256 = DigestUtils.sha256Hex(tsmemmpc20Dto.getMemberPasswordNew());
		logger.debug("暗号化更新パスワード: " + pwSHA256);

		// パスワードの更新
		entity.setPassword(pwSHA256);

		entity = (MemberEntity) CommonUtil.setWhoInfoUpdate(entity, session);

		// 更新項目の設定
		memberRepository.save(entity);

	}

}
