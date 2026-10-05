package com.springboot.libraryPJ.ZZ.service;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.dto.TSCOMMIRDto;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

/**
 * 会員登録確認画面
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSCOMMIR30Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberRepository memberRepository;

	/**
	 * 会員登録
	 *
	 * @param member
	 */
	public MemberEntity createMember(TSCOMMIRDto tscommirDto, String memberId, HttpSession session) {

		MemberEntity member = new MemberEntity();
		member.setMemberClass(tscommirDto.getMemberClass());
		member.setName(tscommirDto.getName());
		member.setBirthday(CommonUtil.stringToDate(tscommirDto.getBirthday()));
		member.setPostalCode(tscommirDto.getPostalCode());
		member.setAddress(tscommirDto.getAddress());
		member.setTelNumber(tscommirDto.getTelNumber());
		member.setEmail(tscommirDto.getEmail());
		member.setJoinDate(new Date(System.currentTimeMillis()));
		member.setRegisterDate(new Date(System.currentTimeMillis()));

		// 登録者
		if (!StringUtils.isEmpty(memberId)) {
			member.setRegisterId(Integer.parseInt(memberId));
		} else {
			member.setRegisterId(CommonConstants.REGISTER_ID);
		}
		// 削除フラグ
		member.setDeleteFlag(CommonConstants.NOT_DELETE);
		// パスワード（暗号化）
		member.setPassword(tscommirDto.getEncryptPassword());

		member = (MemberEntity) CommonUtil.setWhoInfoInsert(member, session);

		return memberRepository.save(member);
	}

	/**
	 *
	 * メールアドレス存在チェック
	 *
	 * @param email
	 * @return
	 */
	public int selectEmail(String email) {
		// log出力
		logger.debug("email:" + email);

		List<MemberEntity> memberData = memberRepository.findByEmailAndDeleteFlag(email, CommonConstants.NOT_DELETE);
		return memberData.size();
	}
}
