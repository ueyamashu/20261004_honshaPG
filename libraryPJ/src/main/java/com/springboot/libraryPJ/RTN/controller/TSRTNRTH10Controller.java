
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

import com.springboot.libraryPJ.RTN.dto.TSRTNRTHinDto;
import com.springboot.libraryPJ.RTN.dto.TSRTNRTHoutDto;
import com.springboot.libraryPJ.RTN.service.TSRTNRTH10Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/*
 * 資料返却履歴一覧
 */

@Controller
@RequestMapping("/TSRTNRTH10")
public class TSRTNRTH10Controller {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRTNRTH10Service tsrtnrth10Service;

	/**
	 * 貸出返却履歴一覧の初期表示
	 *
	 * @param tscomLogDto
	 * @param model
	 * @return 貸出返却履歴一覧パス
	 */
	@GetMapping("init")
	public String initTSRTNRTH10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRTNRTHinDto inDto, Model model) {
		// サービス呼び出し
		List<TSRTNRTHoutDto> outDto = tsrtnrth10Service.getReturnHistoryList();

		// サービスからの取得結果をmodel、sessionに格納
		model.addAttribute("outDto", outDto);

		List<Object> pageList = new ArrayList<>();
		for (Object item : outDto) {
			pageList.add(item);
		}

		CommonUtil.pageModule(page, size, model, pageList, "outDto", "/TSRTNRTH10/init?");

		// 画面遷移
		return "book/return_history_list";
	}

	/**
	 * 貸出返却履歴一覧の検索
	 *
	 * @param inDto
	 * @param result
	 * @param session
	 * @param model
	 * @return 貸出返却履歴一覧パス
	 */
	@GetMapping("search")
	public String searchTSRTNRTH10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRTNRTHinDto inDto, BindingResult result, Model model) {
		// ログ出力
		logger.debug("memberId: " + inDto.getMemberId());
		logger.debug("bookId: " + inDto.getBookId());

		// 入力パラメータの単項目チェック（最後に作業してください）
		validationCheck(inDto, result, model);

		List<Object> pageList = new ArrayList<>();
		String baseUrl = "/TSRTNRTH10/search" + "?memberId=" + inDto.getMemberId() + "&bookId=" + inDto.getBookId()
				+ "&";

		// チェックでエラーが発生した場合、資料返却一覧画面に遷移する
		if (result.hasErrors()) {
			CommonUtil.pageModule(page, size, model, pageList, "outDto", baseUrl);
			return "book/return_history_list";
		}

		// サービス呼び出し
		List<TSRTNRTHoutDto> outDto = tsrtnrth10Service.searchReturnHistoryList(inDto);

		// 業務エラー処理：検索結果が0件の場合
		if (outDto.size() == 0) {
			// DEBUG用LOG出力
			logger.debug("tsrtnrth10Service.searchReturnHistoryList data empty");

			// エラーメッセージを設定する
			String message = MessageConstants.getMessage(MessageConstants.MSGCOM012E, new String[] { "資料返却履歴" });
			model.addAttribute("error", message);
			ObjectError error = new ObjectError("error", message);
			result.addError(error);
			CommonUtil.pageModule(page, size, model, pageList, "outDto", baseUrl);
			return "book/return_history_list";
		}

		// サービスからの取得結果をmodelに格納
		model.addAttribute("outDto", outDto);

		for (Object item : outDto) {
			pageList.add(item);
		}

		CommonUtil.pageModule(page, size, model, pageList, "outDto", baseUrl);

		// 画面遷移
		return "book/return_history_list";
	}

	/**
	 * 検索条件の入力チェック
	 *
	 * @param inDto
	 * @param result
	 * @param model
	 */
	private void validationCheck(@ModelAttribute TSRTNRTHinDto inDto, BindingResult result, Model model) {
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
