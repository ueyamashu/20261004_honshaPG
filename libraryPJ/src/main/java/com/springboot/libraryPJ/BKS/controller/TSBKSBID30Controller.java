package com.springboot.libraryPJ.BKS.controller;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIDinDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBID30Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/TSBKSBID30/")
public class TSBKSBID30Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TSBKSBID30Service tsbksbid30service;

	/**
	 * 資料廃棄確認画面の初期表示
	 *
	 * @param inDto
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("init")
	public String initTSBKSBID30(@ModelAttribute TSBKSBIDinDto inDto, BindingResult result, Model model)
			throws ParseException {
		// ログ出力
		logger.debug("TSBKSBID30 init inDto:" + inDto);

		// 入力内容のmodel格納
		model.addAttribute("inDto", inDto);

		// 入力パラメータの単項目チェック
		validationCheck(inDto, result, model);
		// チェックでエラーが発生した場合、資料返却一覧画面に遷移する
		if (result.hasErrors()) {
			return "book/discard_input";
		}

		// 画面遷移
		return "book/discard_confirm";
	}

	/**
	 * 資料廃棄
	 *
	 * @param inDto
	 * @param result
	 * @param model
	 * @return
	 */
	@PostMapping("complete")
	public String completeTSBKSBID30(RedirectAttributes redirectAttributes, @ModelAttribute TSBKSBIDinDto inDto,
			BindingResult result, Model model, HttpSession session) {
		// ログ出力
		logger.debug("TSBKSBID30 comfirm inDto:" + inDto);

		// 入力内容のmodel格納
		model.addAttribute("inDto", inDto);

		// サービス呼び出し
		tsbksbid30service.disposalLibraryRecord(inDto, session);

		// 値渡し（bookIdのみ）
		redirectAttributes.addFlashAttribute("bookId", inDto.getBookId());

		// 画面遷移
		return "redirect:/TSBKSBID40/init";
	}

	/**
	 * 入力値をチェックする
	 *
	 * @param inDto
	 * @param result
	 * @param model
	 * @throws ParseException
	 */
	private void validationCheck(@ModelAttribute TSBKSBIDinDto inDto, BindingResult result, Model model)
			throws ParseException {
		String dispDateErrMsg = null;
		String dispNoteErrMsg = null;

		// 廃棄年月日
		if (!CheckerUtil.dataRequiredCheck(inDto.getDisposalDate())) {
			dispDateErrMsg = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_DISPOSALDATE });

		} else if (!CheckerUtil.stringLengthCheck(inDto.getDisposalDate(), CommonConstants.DETAIL_TABLE_NOTE_MIN_SIZE,
				CommonConstants.LIBRARY_TABLE_DISPOSAL_DATE_MAX_SIZE)) {
			dispDateErrMsg = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
					new String[] { CommonConstants.STRING_DISPOSALDATE,
							String.valueOf(CommonConstants.LIBRARY_TABLE_DISPOSAL_DATE_MAX_SIZE) });

		} else if (!CheckerUtil.dateFormatCheck(inDto.getDisposalDate())) {
			dispDateErrMsg = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
					new String[] { CommonConstants.STRING_DISPOSALDATE, CommonConstants.STRING_BITHDAY_REGEXP });

		} else if (CheckerUtil.compareToDate(inDto.getDisposalDate()) == CommonConstants.CHECK_RESULT_BEFORE_DATE) {
			dispDateErrMsg = MessageConstants.getMessage(MessageConstants.MSGCOM011E, new String[] {});
		}

		// 備考
		if (!CheckerUtil.dataRequiredCheck(inDto.getDisposalNote())) {
			dispNoteErrMsg = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_DISPOSALNOTE });

		} else if (!CheckerUtil.stringLengthCheck(inDto.getDisposalNote(), CommonConstants.DETAIL_TABLE_NOTE_MIN_SIZE,
				CommonConstants.DETAIL_TABLE_NOTE_MAX_SIZE)) {
			dispNoteErrMsg = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_DISPOSALNOTE, String.valueOf(CommonConstants.DETAIL_TABLE_NOTE_MAX_SIZE) });
		}

		// 廃棄年月日
		if (!StringUtils.isEmpty(dispDateErrMsg)) {
			// LOG出力
			logger.debug("disposalDate error");
			model.addAttribute("validationDisposalDateError", dispDateErrMsg);
			ObjectError validationError = new ObjectError("validationDisposalDateError", dispDateErrMsg);
			result.addError(validationError);
		}

		// 備考
		if (!StringUtils.isEmpty(dispNoteErrMsg)) {
			// LOG出力
			logger.debug("disposalNote error");
			model.addAttribute("validationDisposalNoteError", dispNoteErrMsg);
			ObjectError validationError = new ObjectError("validationDisposalNoteError", dispNoteErrMsg);
			result.addError(validationError);
		}
	}
}
