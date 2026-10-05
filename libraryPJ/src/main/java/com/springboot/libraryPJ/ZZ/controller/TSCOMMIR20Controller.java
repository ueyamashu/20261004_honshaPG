package com.springboot.libraryPJ.ZZ.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.ZZ.dto.TSCOMMIRDto;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 会員登録
 */
@Controller
@RequestMapping("/TSCOMMIR20/")
public class TSCOMMIR20Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 画面初期表示
	 *
	 * @param tscommirDto
	 * @param model
	 * @param memberId
	 * @return
	 */
	@GetMapping("init")
	public String displayMemberRegister(@ModelAttribute TSCOMMIRDto tscommirDto, Model model, HttpSession session) {

		// 教育用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// 会員ID取得
		String memberId = (String) session.getAttribute("memberId");

		// LOG出力
		logger.debug("memberId: " + memberId);

		// 会員区分判定
		if (!StringUtils.isEmpty(memberId)) {
			// 一般を設定
			tscommirDto.setMemberClass(CommonConstants.MEMBERSHIP_PUBLIC);
		} else {
			// 職員を設定
			tscommirDto.setMemberClass(CommonConstants.MEMBERSHIP_STAFF);
		}

		// 会員ID設定
		tscommirDto.setMemberId(memberId);

		// 画面出力設定
		model.addAttribute("tscommirDto", tscommirDto);

		return "common/member_register_input";
	}

}
