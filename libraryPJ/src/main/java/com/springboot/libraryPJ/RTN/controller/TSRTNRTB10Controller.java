package com.springboot.libraryPJ.RTN.controller;

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

import com.springboot.libraryPJ.RTN.dto.TSRTNRTBinDto;
import com.springboot.libraryPJ.RTN.dto.TSRTNRTBoutDto;
import com.springboot.libraryPJ.RTN.service.TSRTNRTB10Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/*
 * 資料返却一覧
 */
@Controller
@RequestMapping("/TSRTNRTB10/")
public class TSRTNRTB10Controller {
	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRTNRTB10Service tsrtnrtb10Service;

	/**
	 * 資料返却一覧の初期表示
	 *
	 * @param page
	 * @param size
	 * @param pageable
	 * @param inDto
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSRTNRTB10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRTNRTBinDto inDto, HttpSession session, Model model) {
		model.addAttribute("inDto", inDto);

		// サービス呼び出し
		List<TSRTNRTBoutDto> outDto = tsrtnrtb10Service.getReturnBookList(CommonConstants.NOT_DELETE);

		List<Object> pageList = new ArrayList<>();
		for (Object item : outDto) {
			pageList.add(item);
		}

		// サービスからの取得結果をmodel、sessionに格納
		CommonUtil.pageModule(page, size, model, pageList, "outDto", "/TSRTNRTB10/init?");

		// 画面遷移
		return "book/return_list";
	}

	/**
	 * 資料返却一覧の検索機能
	 *
	 * @param page
	 * @param size
	 * @param pageable
	 * @param inDto
	 * @param result
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("search")
	public String searchTSRTNRTB10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRTNRTBinDto inDto, BindingResult result, HttpSession session, Model model) {
		model.addAttribute("inDto", inDto);
		// ログ出力
		logger.debug("memberId: " + inDto.getMemberId());
		logger.debug("bookId: " + inDto.getBookId());

		// 入力パラメータの単項目チェック
		validationCheck(inDto, result, model);

		List<Object> pageList = new ArrayList<>();
		String baseUrl = "/TSRTNRTB10/search" + "?memberId=" + inDto.getMemberId() + "&bookId=" + inDto.getBookId()
				+ "&";

		// チェックでエラーが発生した場合、資料返却一覧画面に遷移する
		if (result.hasErrors()) {
			CommonUtil.pageModule(page, size, model, pageList, "outDto", baseUrl);
			return "book/return_list";
		}

		// サービスの入力パラメータ（入力DTO）設定
		inDto.setDeleteFlag(CommonConstants.NOT_DELETE);
		// サービス呼び出し
		List<TSRTNRTBoutDto> outDto = tsrtnrtb10Service.searchRtnBookList(inDto);

		// 業務エラー処理：検索結果が0件の場合
		if (outDto.size() == 0) {
			// DEBUG用LOG出力
			logger.debug("tsrtnrtb10Service.searchRtnBookList data empty");

			// エラーメッセージを設定する
			String message = MessageConstants.getMessage(MessageConstants.MSGCOM012E, new String[] { "資料返却情報" });
			model.addAttribute("error", message);
			ObjectError error = new ObjectError("error", message);
			result.addError(error);
			CommonUtil.pageModule(page, size, model, pageList, "outDto", baseUrl);
			return "book/return_list";
		}

		// サービスからの取得結果をmodelに格納
		model.addAttribute("outDto", outDto);

		for (Object item : outDto) {
			pageList.add(item);
		}

		CommonUtil.pageModule(page, size, model, pageList, "outDto", baseUrl);

		// 画面遷移
		return "book/return_list";
	}

	/**
	 * 検索条件の入力チェック
	 *
	 * @param inDto
	 * @param result
	 * @param model
	 */
	private void validationCheck(@ModelAttribute TSRTNRTBinDto inDto, BindingResult result, Model model) {
		String memberIdErrorMessage = null;
		String bookIdErrorMessage = null;

		// 会員IDを入力した場合、半角数字かをチェックする
		if (!StringUtils.isEmpty(inDto.getMemberId())) {
			if (!CheckerUtil.dataTypeCheck(inDto.getMemberId(), CheckerUtil.CHECK_TYPE_HALF_NUM)) {
				memberIdErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM004E,
						new String[] { "会員ID", "数字" });
			}
		}

		// 資料IDを入力した場合、半角数字かをチェックする
		if (!StringUtils.isEmpty(inDto.getBookId())) {
			if (!CheckerUtil.dataTypeCheck(inDto.getBookId(), CheckerUtil.CHECK_TYPE_HALF_NUM)) {
				bookIdErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM004E,
						new String[] { "資料ID", "数字" });
			}
		}

		if (!StringUtils.isEmpty(memberIdErrorMessage)) {
			// LOG出力
			logger.debug("name error");

			// エラーメッセージを設定する
			model.addAttribute("validationMemberIdError", memberIdErrorMessage);
			ObjectError validationError = new ObjectError("validationMemberIdError", memberIdErrorMessage);
			result.addError(validationError);
		}

		if (!StringUtils.isEmpty(bookIdErrorMessage)) {
			// LOG出力
			logger.debug("email error");

			// エラーメッセージを設定する
			model.addAttribute("validationBookIdError", bookIdErrorMessage);
			ObjectError validationError = new ObjectError("validationBookIdError", bookIdErrorMessage);
			result.addError(validationError);
		}
	}
}
