package com.springboot.libraryPJ.MEM.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20Dto;
import com.springboot.libraryPJ.MEM.dto.TSMEMMPC20OutDto;
import com.springboot.libraryPJ.MEM.service.TSMEMMPC20Service;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 会員パスワード更新
 */

@Controller
@RequestMapping("/TSMEMMPC20/")
public class TSMEMMPC20Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMPC20Service tsmemmpc20Service;

	/**
	 * 画面初期表示
	 * 
	 * @param tsmemmpc20Dto
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("init")
	public String initTSMEMMPC20(@ModelAttribute TSMEMMPC20Dto tsmemmpc20Dto, Model model, HttpSession session) {
		model.addAttribute("tsmemmpc20Dto", tsmemmpc20Dto);
		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// 会員ID取得
		String memberId = (String) session.getAttribute("memberId");

		// log出力
		logger.debug("memberId:" + memberId);

		// 修正:例外処理追加
		try {

			// outDto設定
			TSMEMMPC20OutDto outDto = tsmemmpc20Service.searchLoginMember(memberId);

			if (outDto.getRecordCnt() != 1) {
				// DEBUG用LOG出力
				logger.debug("tsmemmpc20Service.searchLoginMember data empty");

				String message = MessageConstants.getMessage(MessageConstants.MSGCOM012E, new String[] { "会員情報" });

				model.addAttribute("error", message);

				return "member/member_password_input";
			}

			TSMEMMPC20Dto tsmemmpc20dto = new TSMEMMPC20Dto();

			tsmemmpc20dto.setMemberId(outDto.getMemberId());
			tsmemmpc20dto.setName(outDto.getName());
			tsmemmpc20dto.setPassword(outDto.getPassword());
			tsmemmpc20dto.setExclusiveKey(outDto.getExclusiveKey());

			// 画面出力設定
			model.addAttribute("tsmemmpc20Dto", tsmemmpc20dto);

			return "member/member_password_input";

		} catch (Exception e) {
			logger.debug("会員情報取得失敗");

			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO_SELECT }));
			return "member/member_password_input";
		}

	}

}
