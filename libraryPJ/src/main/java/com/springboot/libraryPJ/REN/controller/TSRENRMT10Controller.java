package com.springboot.libraryPJ.REN.controller;

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

import com.springboot.libraryPJ.REN.dto.TSRENRMTinDto;
import com.springboot.libraryPJ.REN.dto.TSRENRMToutDto;
import com.springboot.libraryPJ.REN.service.TSRENRMT10Service;
import com.springboot.libraryPJ.ZZ.dto.TSRENRMTDto;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 貸出会員一覧
 */
@Controller
@RequestMapping("/TSRENRMT10/")
public class TSRENRMT10Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRENRMT10Service tsrenrmt10Service;

	/***
	 * 貸出会員一覧初期表示
	 *
	 * @param page
	 * @param size
	 * @param pageable
	 * @param tsrenrmtDto
	 * @param session
	 * @param result
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSRENRMT10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRENRMTDto tsrenrmtDto, HttpSession session, BindingResult result, Model model) {

		// 入力内容のmodel格納
		model.addAttribute("tsrenrmtDto", tsrenrmtDto);

		// 退会していない会員情報を取得する。
		TSRENRMTinDto indto = new TSRENRMTinDto();
		indto.setDeleteFlag(CommonConstants.NOT_DELETE);

		TSRENRMToutDto outDto = tsrenrmt10Service.findMemberList(indto);

		// ページネーション用
		List<Object> pageList = new ArrayList<>();
		String baseUrl = "/TSRENRMT10/init?";

		// 取得結果がエラーの場合はエラーメッセージを出力する。
		if (outDto.getResultCd() != 0) {
			logger.debug("data empty");
			model.addAttribute("error", outDto.getErrmsg());
			CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);
			return "book/rental_member_list";
		}

		// サービスからの取得結果をmodel、sessionに格納
		for (Object item : outDto.getMemberList()) {
			pageList.add(item);
		}
		CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);
		session.setAttribute("memberList", outDto.getMemberList());

		// 貸出会員一覧画面に遷移
		return "book/rental_member_list";
	}

	/**
	 * 貸出会員検索
	 *
	 * @param page
	 * @param size
	 * @param pageable
	 * @param tsrenrmtDto
	 * @param session
	 * @param result
	 * @param model
	 * @return
	 */
	@GetMapping("member/search")
	public String searchRentalMmemberList(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRENRMTDto tsrenrmtDto, HttpSession session, BindingResult result, Model model) {

		// 入力内容のmodel格納
		model.addAttribute("tsrenrmtDto", tsrenrmtDto);

		// ログを出力する。
		logger.debug("memberId: " + tsrenrmtDto.getMemberId());
		logger.debug("name: " + tsrenrmtDto.getName());

		// 入力パラメータの単項目チェックする。
		validationCheck(tsrenrmtDto, result, model);

		// ページネーション用
		List<Object> pageList = new ArrayList<>();
		String baseUrl = "/TSRENRMT10/member/search" + "?memberId=" + tsrenrmtDto.getMemberId() + "&name="
				+ tsrenrmtDto.getName() + "&";

		// 入力パラメータの単項目チェックエラー発生した場合貸出会員一覧画面を表示する。
		if (result.hasErrors()) {
			CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);
			return "book/rental_member_list";
		}

		// 入力DTOを設定する。
		TSRENRMTinDto indto = new TSRENRMTinDto();
		indto.setMemberId(tsrenrmtDto.getMemberId());
		indto.setName(tsrenrmtDto.getName());
		indto.setDeleteFlag(CommonConstants.NOT_DELETE);

		// 検索条件に一致する退会していない会員情報を取得する。
		TSRENRMToutDto outDto = tsrenrmt10Service.searchMemberList(indto);

		// 取得結果がエラーの場合はエラーメッセージを出力する。
		if (outDto.getResultCd() != 0) {
			logger.debug("data empty");
			model.addAttribute("error", outDto.getErrmsg());
			CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);
			return "book/rental_member_list";
		}

		// サービスからの取得結果をmodel、sessionに格納
		for (Object item : outDto.getMemberList()) {
			pageList.add(item);
		}
		CommonUtil.pageModule(page, size, model, pageList, "memberList", baseUrl);
		session.setAttribute("memberList", outDto.getMemberList());

		// 貸出会員一覧画面に遷移
		return "book/rental_member_list";
	}

	/**
	 * メインメニューに戻る
	 *
	 * @return
	 */
	@GetMapping("main")
	public String displayMainMenu() {
		return "common/main";
	}

	/**
	 * バリデーション
	 *
	 * @param tsrenrmtDto
	 * @param result
	 * @param model
	 */
	public void validationCheck(@ModelAttribute TSRENRMTDto tsrenrmtDto, BindingResult result, Model model) {
		String memberIdErrorMessage = null;
		String nameErrorMessage = null;

		// 会員IDの値が存在する場合はバリデーションする。
		if (!StringUtils.isEmpty(tsrenrmtDto.getMemberId())) {
			// 半角数字
			if (!CheckerUtil.dataTypeCheck(tsrenrmtDto.getMemberId(), CheckerUtil.CHECK_TYPE_HALF_NUM)) {
				memberIdErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM014E,
						new String[] { CommonConstants.STRING_MEMBERID });
			}
			// 最大長
			else if (!CheckerUtil.stringLengthCheck(tsrenrmtDto.getMemberId(), CommonConstants.MIN_SIZE,
					CommonConstants.MEMBER_TABLE_MEMBERID_MAX_SIZE)) {
				memberIdErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
						CommonConstants.STRING_MEMBERID, CommonConstants.MEMBER_TABLE_MEMBER_ID_MAX_SIZE_STRING });
			}
		}

		// 名前の値が存在する場合はバリデーションする。
		if (!StringUtils.isEmpty(tsrenrmtDto.getName())) {
			// 全角漢字・全角カタカナ
			if (!CheckerUtil.dataTypeCheck(tsrenrmtDto.getName(), CommonConstants.REGEX_TEXT_FULL_KATAKANA_KANJI)) {
				nameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM015E,
						new String[] { CommonConstants.STRING_NAME });
			}
			// 最大長
			else if (!CheckerUtil.stringLengthCheck(tsrenrmtDto.getName(), CommonConstants.MIN_SIZE,
					CommonConstants.MEMBER_TABLE_NAME_MAX_SIZE)) {
				nameErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
						CommonConstants.STRING_NAME, CommonConstants.MEMBER_TABLE_NAME_MAX_SIZE_STRING });
			}
		}

		// 会員IDエラーメッセージが存在する場合はログを出力する。
		if (!StringUtils.isEmpty(memberIdErrorMessage)) {
			logger.debug("memberId error");
			model.addAttribute("validationMemberIdError", memberIdErrorMessage);
			ObjectError validationError = new ObjectError("validationMemberIdError", memberIdErrorMessage);
			result.addError(validationError);
		}

		// 名前エラーメッセージが存在する場合はログを出力する。
		if (!StringUtils.isEmpty(nameErrorMessage)) {
			logger.debug("name error");
			model.addAttribute("validationNameError", nameErrorMessage);
			ObjectError validationError = new ObjectError("validationNameError", nameErrorMessage);
			result.addError(validationError);
		}
	}

}
