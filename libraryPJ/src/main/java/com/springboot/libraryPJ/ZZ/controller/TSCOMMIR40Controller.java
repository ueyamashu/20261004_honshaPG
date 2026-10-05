package com.springboot.libraryPJ.ZZ.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.ZZ.dto.TSCOMMIRDto;
import com.springboot.libraryPJ.ZZ.dto.TSCOMMIROutDto;
import com.springboot.libraryPJ.ZZ.service.TSCOMMIR40Service;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 会員登録完了画面
 */
@Controller
@RequestMapping("/TSCOMMIR40/")
public class TSCOMMIR40Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSCOMMIR40Service tscommir40Service;

	/**
	 * 画面初期表示
	 *
	 * @param email
	 * @param password
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String register(@ModelAttribute("memberId") String memberId, @ModelAttribute("password") String password,
			@ModelAttribute("tscommirDto") TSCOMMIRDto tscommirDto, Model model) {

		model.addAttribute("tscommirDto", tscommirDto);

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			// 登録した会員情報を取得
			TSCOMMIROutDto outDto = tscommir40Service.selectRegistMember(memberId);
			outDto.setPassword(password);

			if (outDto.getRecordCnt() != 1) {
				// DEBUG用LOG出力
				logger.debug("会員登録情報なし");

				String errorMsg = MessageConstants.getMessage(MessageConstants.MSGCOM012E,
						new String[] { CommonConstants.STRING_MEMBER_JOHO });

				model.addAttribute("error", errorMsg);

				return "common/member_register_confirm";
			}
			model.addAttribute("outDto", outDto);
			return "common/member_register_complete";
		} catch (Exception e) {
			// DEBUG用LOG出力
			logger.debug("会員登録情報なし");

			String errorMsg = MessageConstants.getMessage(MessageConstants.MSGCOM012E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO });

			model.addAttribute("error", errorMsg);

			return "common/member_register_confirm";
		}
	}

}