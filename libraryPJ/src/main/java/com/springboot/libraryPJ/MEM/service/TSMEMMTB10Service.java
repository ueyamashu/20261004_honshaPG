package com.springboot.libraryPJ.MEM.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.MEM.dto.TSMEMMTBOutDto;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.MemberRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;

/**
 * 会員一覧画面
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSMEMMTB10Service {

	@Autowired
	MemberRepository memberRepository;

	/**
	 * @return
	 */
	public List<TSMEMMTBOutDto> searchMemberList() {
		List<MemberEntity> memberList = memberRepository.findByDeleteFlag(CommonConstants.NOT_DELETE);
		List<TSMEMMTBOutDto> outDto = memberList.stream().map(o -> new TSMEMMTBOutDto(o)).collect(Collectors.toList());
		return outDto;
	}

	/**
	 * @param name
	 * @param email
	 * @return
	 */
	public List<TSMEMMTBOutDto> searchMemberListBySearchBtn(String name, String email) {
		List<MemberEntity> memberList = null;
		if (StringUtils.isEmpty(name) && StringUtils.isEmpty(email)) {
			memberList = memberRepository.findByDeleteFlag(CommonConstants.NOT_DELETE);
		} else if (StringUtils.isEmpty(name) && !StringUtils.isEmpty(email)) {
			memberList = memberRepository.findByEmailAndDeleteFlag(email, CommonConstants.NOT_DELETE);
		} else if (!StringUtils.isEmpty(name) && StringUtils.isEmpty(email)) {
			memberList = memberRepository.findByNameAndDeleteFlag(name, CommonConstants.NOT_DELETE);
		} else if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(email)) {
			memberList = memberRepository.findByNameAndByEmailAndDeleteFlag(name, email, CommonConstants.NOT_DELETE);
		}
		List<TSMEMMTBOutDto> outDto = memberList.stream().map(o -> new TSMEMMTBOutDto(o)).collect(Collectors.toList());
		return outDto;
	}
}
