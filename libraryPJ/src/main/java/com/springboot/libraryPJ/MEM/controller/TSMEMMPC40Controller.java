package com.springboot.libraryPJ.MEM.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20Dto;
import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20OutDto;
import com.springboot.libraryPJ.MEM.service.TSMEMMPC40Service;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/*
 * 会員パスワード更新完了
 */

@Controller
@RequestMapping("/TSMEMMPC40/")
public class TSMEMMPC40Controller {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMPC40Service tsmemmic40Service;

	/**
	 * 画面初期表示
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSMEMMPC40(@ModelAttribute("memberId") String memberId,
			@ModelAttribute("password") String password, Model model) {

		try {
			TSMEMMPC20OutDto outDto = tsmemmic40Service.searchChangedMember(memberId, password);

			TSMEMMPC20Dto tsmemmpc20Dto = new TSMEMMPC20Dto();

			// log出力
			logger.debug("MemberId:" + memberId);
			logger.debug("change password:" + password);

			tsmemmpc20Dto.setMemberId(outDto.getMemberId());
			tsmemmpc20Dto.setName(outDto.getName());
			tsmemmpc20Dto.setMemberPasswordNew(password);

			model.addAttribute("tsmemmpc20Dto", tsmemmpc20Dto);

		} catch (Exception e) {
			logger.debug("会員情報取得失敗");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO_SELECT }));
			return "member/member_password_confirm";
		}

		return "member/member_password_complete";
	}
}
