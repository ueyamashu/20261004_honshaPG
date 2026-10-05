package com.springboot.libraryPJ.MEM.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.MEM.dto.TSMEMMID30Dto;
import com.springboot.libraryPJ.MEM.dto.TSMEMMID30outDTO;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

/**
 * 会員退会確認
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSMEMMID30Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	private RentalRepository rentalRepository;

	// 借りている資料の有無を確認するメソッド
	public boolean hasBorrowedBooks(String memberId) {
		List<RentalEntity> rentals = rentalRepository.findRentalListByMemberId(memberId);
		return !rentals.isEmpty();
	}

	/**
	 *
	 * @param tsmemmid30Dto
	 * @return
	 */
	public TSMEMMID30outDTO deleteMember(TSMEMMID30Dto tsmemmid30Dto, HttpSession session) {
		// log出力
		logger.debug("deleteMember memberId:" + Integer.parseInt(tsmemmid30Dto.getMemberId()));

		TSMEMMID30outDTO outDto = new TSMEMMID30outDTO();

		List<MemberEntity> memberList = memberRepository
				.findByMemberIdAndDeleteFlag(Integer.parseInt(tsmemmid30Dto.getMemberId()), CommonConstants.NOT_DELETE);

		MemberEntity entity = memberList.get(0);

		entity.setMemberId(Integer.parseInt(tsmemmid30Dto.getMemberId()));
		entity.setDeleteFlag("1");
		entity.setWithdrawDate(new Date(System.currentTimeMillis()));
		entity = (MemberEntity) CommonUtil.setWhoInfoUpdate(entity, session);

		memberRepository.save(entity);

		return outDto;

	}

	/**
	 * @param memberId
	 */
	public void markAsWithdrawn(long memberId) {

	}

	/**
	 * @param id
	 * @return
	 */
	public TSMEMMID30outDTO selectMember(String id, String deleteFlg) {

		TSMEMMID30outDTO outDto = new TSMEMMID30outDTO();

		List<MemberEntity> memberList = memberRepository.findByMemberIdAndDeleteFlag(Integer.parseInt(id), deleteFlg);

		MemberEntity entity = memberList.get(0);

		logger.debug("selectMember memberId: " + entity.getMemberId());

		outDto.setMemberId(String.valueOf(entity.getMemberId()));
		outDto.setAddress(entity.getAddress());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		outDto.setBirthday(sdf.format(entity.getBirthday()));
		outDto.setEmail(entity.getEmail());
		outDto.setMembership(entity.getMemberClass());
		outDto.setMemberName(entity.getName());
		outDto.setTelNumber(entity.getTelNumber());
		outDto.setPostalCode(entity.getPostalCode());
		if (entity.getWithdrawDate() != null) {
			outDto.setWithdrawDate(sdf.format(entity.getWithdrawDate()));
		}
		return outDto;
	}

}