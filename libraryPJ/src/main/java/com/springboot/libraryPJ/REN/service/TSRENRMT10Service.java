package com.springboot.libraryPJ.REN.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.REN.dto.TSRENRMTinDto;
import com.springboot.libraryPJ.REN.dto.TSRENRMToutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 貸出会員一覧
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSRENRMT10Service {

	@Autowired
	MemberRepository memberRepository;

	/**
	 * 貸出の会員情報リストを取得する。
	 *
	 * @param indto
	 * @return
	 */
	public TSRENRMToutDto findMemberList(TSRENRMTinDto indto) {
		// 出力DTOを設定する。
		TSRENRMToutDto outDto = new TSRENRMToutDto();

		// 削除フラグを条件に会員情報を取得する。
		List<MemberEntity> memberList = memberRepository.findByDeleteFlag(indto.getDeleteFlag());

		// 取得結果が0件の場合はエラーメッセージを出力する。
		if (memberList.size() == 0 || memberList == null) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM017E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO }));
		}

		// 取得した会員情報をセットする。
		outDto.setMemberList(memberList);

		// 出力DTOを返す。
		return outDto;
	}

	/**
	 * 貸出の会員情報を検索する。
	 *
	 * @param indto
	 * @return
	 */
	public TSRENRMToutDto searchMemberList(TSRENRMTinDto indto) {
		// 出力DTOを設定する。
		TSRENRMToutDto outDto = new TSRENRMToutDto();

		// 会員情報リストを定義する。
		List<MemberEntity> memberList = new ArrayList<MemberEntity>();

		// 会員IDのみ存在する場合は会員IDと削除フラグを条件に会員情報を取得する。
		if (!StringUtils.isEmpty(indto.getMemberId()) && StringUtils.isEmpty(indto.getName())) {
			memberList = memberRepository.findByMemberIdAndDeleteFlag(Integer.parseInt(indto.getMemberId()),
					indto.getDeleteFlag());
		}
		// 名前のみ存在する場合は名前と削除フラグを条件に会員情報を取得する。
		else if (StringUtils.isEmpty(indto.getMemberId()) && !StringUtils.isEmpty(indto.getName())) {
			memberList = memberRepository.findByNameAndDeleteFlag(indto.getName(), indto.getDeleteFlag());
		}
		// 会員IDと名前が存在する場合は会員IDと名前、削除フラグを条件に会員情報を取得する。
		else if (!StringUtils.isEmpty(indto.getMemberId()) && !StringUtils.isEmpty(indto.getName())) {
			memberList = memberRepository.findByMemberIdAndNameAndDeleteFlag(Integer.parseInt(indto.getMemberId()),
					indto.getName(), indto.getDeleteFlag());
		}
		// その他の場合は削除フラグを条件に会員情報を取得する。
		else {
			memberList = memberRepository.findByDeleteFlag(indto.getDeleteFlag());
		}

		// 取得結果が0件の場合はエラーメッセージを出力する。
		if (memberList.size() == 0 || memberList == null) {
			outDto.setResultCd(-1);
			outDto.setErrmsg(MessageConstants.getMessage(MessageConstants.MSGCOM013E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO }));
		}

		// 取得した会員情報をセットする。
		outDto.setMemberList(memberList);

		// 出力DTOを返す。
		return outDto;
	}

}
