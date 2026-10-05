package com.springboot.libraryPJ.ZZ.service;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.dto.TSCOMLOGDto;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;

/**
 * ログイン画面
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSCOMLOG00Service {

	@Autowired
	MemberRepository memberRepository;

	/**
	 * 会員情報取得
	 * 
	 * @param 画面入力値
	 * @return 会員情報リスト
	 */
	public List<MemberEntity> searchLoginMember(TSCOMLOGDto tscomLogDto) {

		// SHA256対応
		String pwSHA256 = DigestUtils.sha256Hex(tscomLogDto.getPassword());

		List<MemberEntity> member = memberRepository.findByMemberIdAndPasswordAndDeleteFlag(
				Integer.parseInt(tscomLogDto.getMemberId()), pwSHA256, CommonConstants.NOT_DELETE);

		return member;
	}

}
