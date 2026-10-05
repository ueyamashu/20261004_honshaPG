package com.springboot.libraryPJ.MEM.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.libraryPJ.MEM.dto.TSMEMMTBDto;
import com.springboot.libraryPJ.MEM.dto.TSMEMMTBOutDto;
import com.springboot.libraryPJ.MEM.service.TSMEMMTB10Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 会員一覧画面
 */
@Controller
@RequestMapping("/TSMEMMTB10/")
public class TSMEMMTB10Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMTB10Service tsmemmTb10Service;

	/**
	 * 初期表示
	 *
	 * @param tsmemmTbDto
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String displayMemberList(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSMEMMTBDto tsmemmTbDto, Model model) {
		model.addAttribute("tsmemmTbDto", tsmemmTbDto);

		try {
			List<TSMEMMTBOutDto> memberList = tsmemmTb10Service.searchMemberList();

			if (memberList.size() == 0 || memberList == null) {
				// DEBUG用LOG出力
				logger.debug("data empty");
				String message = MessageConstants.getMessage(MessageConstants.MSGCOM017E,
						new String[] { CommonConstants.STRING_MEMBER_JOHO });
				model.addAttribute("error", message);
			}

			model.addAttribute("memberList", memberList);

			List<Object> pageList = new ArrayList<>();
			for (Object item : memberList) {
				pageList.add(item);
			}

			CommonUtil.pageModule(page, size, model, pageList, "memberList", "/TSMEMMTB10/init?");

			return "member/member_list";

		} catch (Exception e) {
			logger.debug("会員情報取得失敗");

			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO_SELECT }));
			return "member/member_list";
		}
	}

	/**
	 * 検索ボタン押下
	 *
	 * @param tsmemmTbDto
	 * @param result
	 * @param model
	 * @return
	 */
	@GetMapping("search")
	public String displayMemberListBySearchBtn(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSMEMMTBDto tsmemmTbDto, BindingResult result, Model model) {
		model.addAttribute("tsmemmTbDto", tsmemmTbDto);

		// LOG出力
		logger.debug("name: " + tsmemmTbDto.getName());
		logger.debug("email: " + tsmemmTbDto.getEmail());

		// 入力パラメータの単項目チェック
		validationCheck(tsmemmTbDto, result, model);

		List<Object> pageList = new ArrayList<>();
		String baseUrl = "/TSMEMMTB10/search" + "?name=" + tsmemmTbDto.getName() + "&email=" + tsmemmTbDto.getEmail()
				+ "&";

		// 入力パラメータの単項目チェックエラー発生
		if (result.hasErrors()) {
			CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);
			return "member/member_list";
		}

		try {
			// サービス呼び出し
			List<TSMEMMTBOutDto> memberList = tsmemmTb10Service.searchMemberListBySearchBtn(tsmemmTbDto.getName(),
					tsmemmTbDto.getEmail());

			// 業務エラー処理：検索結果が0件の場合
			if (memberList.size() == 0 || memberList == null) {

				// DEBUG用LOG出力
				logger.debug("data empty");
				String message = MessageConstants.getMessage(MessageConstants.MSGCOM013E,
						new String[] { CommonConstants.STRING_MEMBER_JOHO });
				model.addAttribute("error", message);
				CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);
				return "member/member_list";
			}

			// サービスからの取得結果をmodelに格納
			model.addAttribute("memberList", memberList);

			for (Object item : memberList) {
				pageList.add(item);
			}

			CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);

			return "member/member_list";

		} catch (Exception e) {
			logger.debug("会員情報取得失敗");
			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO_SELECT }));
			return "member/member_list";
		}
	}

	/**
	 * 戻るボタン押下
	 *
	 * @return
	 */
	@GetMapping("/main")
	public String displayMainMenu() {
		return "common/main";
	}

	/**
	 * 入力パラメータの単項目チェック
	 *
	 * @param tsmemmTbDto
	 * @param result
	 * @param model
	 */
	public void validationCheck(@ModelAttribute TSMEMMTBDto tsmemmTbDto, BindingResult result, Model model) {
		String nameErrorMessage = null;
		String emailErrorMessage = null;

		if (!StringUtils.isEmpty(tsmemmTbDto.getName())) {
			if (!CheckerUtil.dataTypeCheck(tsmemmTbDto.getName(), CommonConstants.REGEX_TEXT_FULL_KATAKANA_KANJI)) {
				nameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM015E,
						new String[] { CommonConstants.STRING_NAME });
			} else if (!CheckerUtil.stringLengthCheck(tsmemmTbDto.getName(), CommonConstants.MIN_SIZE,
					CommonConstants.MEMBER_TABLE_NAME_MAX_SIZE)) {
				nameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
						CommonConstants.STRING_NAME, CommonConstants.MEMBER_TABLE_NAME_MAX_SIZE_STRING });
			}
		}

		if (!StringUtils.isEmpty(tsmemmTbDto.getEmail())) {
			if (!CheckerUtil.emailAddressFormatCheck(tsmemmTbDto.getEmail())) {
				emailErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM010E, new String[] { "" });
			} else if (!CheckerUtil.stringLengthCheck(tsmemmTbDto.getEmail(), CommonConstants.MIN_SIZE,
					CommonConstants.MEMBER_TABLE_EMAIL_MAX_SIZE)) {
				emailErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
						CommonConstants.STRING_EMAIL, CommonConstants.MEMBER_TABLE_EMAIL_MAX_SIZE_STRING });
			}
		}

		if (!StringUtils.isEmpty(nameErrorMessage)) {
			// LOG出力
			logger.debug("name error");
			model.addAttribute("validationNameError", nameErrorMessage);
			ObjectError validationError = new ObjectError("validationNameError", nameErrorMessage);
			result.addError(validationError);
		}

		if (!StringUtils.isEmpty(emailErrorMessage)) {
			// LOG出力
			logger.debug("email error");
			model.addAttribute("validationEmailError", emailErrorMessage);
			ObjectError validationError = new ObjectError("validationEmailError", emailErrorMessage);
			result.addError(validationError);
		}
	}
}
