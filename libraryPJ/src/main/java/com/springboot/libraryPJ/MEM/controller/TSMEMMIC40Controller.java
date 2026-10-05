package com.springboot.libraryPJ.MEM.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.MEM.dto.TSMEMMIC20DTOForm;
import com.springboot.libraryPJ.MEM.dto.TSMEMMIC20outDTO;
import com.springboot.libraryPJ.MEM.service.TSMEMMIC20Service;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 会員情報更新完了
 */
@Controller
@RequestMapping("/TSMEMMIC40/")
public class TSMEMMIC40Controller {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMIC20Service tsmemmic20Service;

	/**
	 * @param memberId
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSMEMMIC40(@ModelAttribute("memberId") String memberId, Model model) {
		// log出力
		logger.debug("TSMEMMIC40 init memberId" + memberId);

		TSMEMMIC20outDTO outDto = tsmemmic20Service.searchLoginMember(memberId);

		if (outDto.getRecordCnt() != 1) {
			// DEBUG用LOG出力
			logger.debug("tsmemmic20Service.searchLoginMember data empty");

			String message = MessageConstants.getMessage(MessageConstants.MSGCOM012E, new String[] { "会員情報" });

			ObjectError error = new ObjectError("error", message);

			model.addAttribute("error", error);

			return "redirect:/TSMEMMTB10/init";
		}

		TSMEMMIC20DTOForm tsmemmic20dto = new TSMEMMIC20DTOForm();

		tsmemmic20dto.setMemberId(outDto.getMemberId());
		tsmemmic20dto.setAddress(outDto.getAddress());
		tsmemmic20dto.setBirthDate(outDto.getBirthDate());
		tsmemmic20dto.setMailAddress(outDto.getMailAddress());
		tsmemmic20dto.setMemberKbn(outDto.getMemberKbn());
		tsmemmic20dto.setMemberName(outDto.getMemberName());
		tsmemmic20dto.setPhoneNo1(outDto.getPhoneNo().substring(0, 3));
		tsmemmic20dto.setPhoneNo2(outDto.getPhoneNo().substring(4, 8));
		tsmemmic20dto.setPhoneNo3(outDto.getPhoneNo().substring(9, 13));
		tsmemmic20dto.setPhoneNo(outDto.getPhoneNo());
		tsmemmic20dto.setPostNo(outDto.getPostNo());
		tsmemmic20dto.setPostNo1(outDto.getPostNo().substring(0, 3));
		tsmemmic20dto.setPostNo2(outDto.getPostNo().substring(4, 8));

		model.addAttribute("tsmemmic20dto", tsmemmic20dto);

		// 画面遷移
		return "member/member_edit_complete";
	}
}