package com.springboot.libraryPJ.MEM.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.MEM.dto.TSMEMMIC20DTOForm;
import com.springboot.libraryPJ.MEM.dto.TSMEMMIC20outDTO;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

/**
 * 
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSMEMMIC30Service {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MemberRepository memberRepository;

	/**
	 * @param tsmemmic20dto
	 * @return
	 */
	public TSMEMMIC20outDTO updateMember(TSMEMMIC20DTOForm tsmemmic20dto, HttpSession session) {
		// log出力
		logger.debug("memberId:" + tsmemmic20dto.getMemberId());

		TSMEMMIC20outDTO outDto = new TSMEMMIC20outDTO();

		List<MemberEntity> memberList = memberRepository
				.findByMemberIdAndDeleteFlag(Integer.parseInt(tsmemmic20dto.getMemberId()), CommonConstants.NOT_DELETE);

		MemberEntity entity = memberList.get(0);

		entity.setMemberClass(tsmemmic20dto.getMemberKbn());
		entity.setMemberId(Integer.parseInt(tsmemmic20dto.getMemberId()));
		entity.setName(tsmemmic20dto.getMemberName());
		// 生年月日の更新
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			java.util.Date utilDate = sdf.parse(tsmemmic20dto.getBirthDate());
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			entity.setBirthday(sqlDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		entity.setPostalCode(tsmemmic20dto.getPostNo());
		entity.setAddress(tsmemmic20dto.getAddress());
		entity.setTelNumber(tsmemmic20dto.getPhoneNo());
		entity.setEmail(tsmemmic20dto.getMailAddress());

		entity = (MemberEntity) CommonUtil.setWhoInfoUpdate(entity, session);
		// 更新項目の設定

		memberRepository.save(entity);

		return outDto;
	}
}
