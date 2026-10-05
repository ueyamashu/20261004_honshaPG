package com.springboot.libraryPJ.BKS.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIR30outDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIRinDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBIR30Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * 資料情報登録確認画面
 */
@Controller
@RequestMapping("/TSBKSBIR30/")
public class TSBKSBIR30Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSBKSBIR30Service tsbksBir30Service;

	/**
	 * 初期表示
	 * 
	 * @param dto
	 * @param model
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("init")
	public String initTSBKSBIR30(@ModelAttribute TSBKSBIRinDto dto, BindingResult result, Model model) {
		// 入力内容のmodel格納
		model.addAttribute("registerData", dto);

		// LOG出力
		logger.debug("TSBKSBIR30");

		// 入力パラメータの単項目チェック
		if (StringUtils.isEmpty(dto.getArrival())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

			dto.setArrival(sdf.format(new Date(System.currentTimeMillis())));
		}

		validationCheck(dto, result, model);

		if (result.hasErrors()) {

			// 画面遷移
			// 資料情報登録画面へ移動
			return "book/book_register_input";
		}

		// サービスの入力パラメータ（入力DTO）設定

		// サービス呼び出し
		TSBKSBIR30outDto outDto = tsbksBir30Service.checkBookInfo(dto);

		// 業務エラー処理
		if (outDto.getResultCd() != 0) {
			logger.debug("Data Insert Error" + outDto.getErrmsg());
			// エラーを表示する
			model.addAttribute("ErrorMessage", outDto.getErrmsg());
		}

		// 画面遷移
		return "book/book_register_confirm";
	}

	/**
	 * 登録処理
	 * 
	 * @param redirectAttributes
	 * @param dto
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("confirm")
	public String confirmTSBKSBIR30(RedirectAttributes redirectAttributes,
			@ModelAttribute("dto") @Validated TSBKSBIRinDto dto, Model model, BindingResult result,
			HttpSession session) {

		// 入力内容のmodel格納
		model.addAttribute("registerData", dto);

		// LOG出力
		logger.debug("TSBKSBIC30");

		// 入力パラメータの単項目チェック
		// 入力項目がないため不要

		// サービスの入力パラメータ（入力DTO）設定

		// サービス呼び出し
		TSBKSBIR30outDto outDto = tsbksBir30Service.insertBookInfo(dto, session);

		// 業務エラー処理
		if (outDto.getResultCd() != 0) {
			// エラーを表示する
			logger.debug("Data Insert Error" + outDto.getErrmsg());
			model.addAttribute("ErrorMessage", outDto.getErrmsg());
			ObjectError error = new ObjectError("ErrorMessage", outDto.getErrmsg());
			result.addError(error);
			return "book/book_register_confirm";
		}

		// サービスからの取得結果をmodel、sessionに格納
		redirectAttributes.addFlashAttribute("registerData", dto);

		// 画面遷移
		return "redirect:/TSBKSBIR40/init";
	}

	/**
	 * 
	 * @param dto
	 * @param result
	 * @param model
	 * @throws ParseException
	 */
	public void validationCheck(@Valid TSBKSBIRinDto dto, BindingResult result, Model model) {

		// チェック用変数初期化
		String titleErrorMessage = null;
		String categoryErrorMessage = null;
		String authorErrorMessage = null;
		String publisherErrorMessage = null;
		String releaseDateErrorMessage = null;
		String isbnErrorMessage = null;
		String arrivalDateErrorMessage = null;

		// 資料名のエラーチェック
		if (!CheckerUtil.dataRequiredCheck(dto.getTitle())) {
			titleErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E, new String[] { "資料名" });
		} else if (!CheckerUtil.stringLengthCheck(dto.getTitle(), 0, 100)) {
			titleErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] { "資料名", "100" });
		}
		if (!StringUtils.isEmpty(titleErrorMessage)) {
			// LOG出力
			logger.debug("titleError");
			model.addAttribute("titleErrorMessage", titleErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// カテゴリーのエラーチェック
		if (!CheckerUtil.dataRequiredCheck(dto.getCategory())) {
			categoryErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E, new String[] { "分類" });
		} else if (!CheckerUtil.dataExistCheck(dto.getCategory(), CommonConstants.BOOK_TABLE_CATEGORY_CODE_VALUE)) {
			categoryErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM003E, new String[] { "分類" });
		}
		if (!StringUtils.isEmpty(categoryErrorMessage)) {
			// LOG出力
			logger.debug("categoryError");
			model.addAttribute("categoryErrorMessage", categoryErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// 著者名のエラーチェック
		if (!CheckerUtil.dataRequiredCheck(dto.getAuthor())) {
			authorErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E, new String[] { "著者名" });
		} else if (!CheckerUtil.stringLengthCheck(dto.getAuthor(), 0, 50)) {
			authorErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] { "著者名", "50" });
		}

		if (!StringUtils.isEmpty(authorErrorMessage)) {
			// LOG出力
			logger.debug("authorError");
			model.addAttribute("authorErrorMessage", authorErrorMessage);
			result.addError(new ObjectError("error", null));
		}
		// 出版社のエラーチェック
		if (!CheckerUtil.dataRequiredCheck(dto.getPublisher())) {
			publisherErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E, new String[] { "出版社" });
		} else if (!CheckerUtil.stringLengthCheck(dto.getPublisher(), 0, 100)) {
			publisherErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
					new String[] { "出版社", "100" });
		}

		if (!StringUtils.isEmpty(publisherErrorMessage)) {
			// LOG出力
			logger.debug("publisherError");
			model.addAttribute("publisherErrorMessage", publisherErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		try {
			// 出版日のエラーチェック
			if (!CheckerUtil.dataRequiredCheck(dto.getReleaseDate())) {
				releaseDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
						new String[] { "出版日" });
			} else if (!CheckerUtil.stringLengthCheck(dto.getReleaseDate(), 0, 10)) {
				releaseDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
						new String[] { "出版日", "10" });
			} else if (!CheckerUtil.dateFormatCheck(dto.getReleaseDate())) {
				releaseDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
						new String[] { "出版日", "yyyy/MM/dd" });
			} else if (CheckerUtil.compareToDate(dto.getReleaseDate()) == CommonConstants.CHECK_RESULT_AFTER_DATE) {
				releaseDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGBKS001E,
						new String[] { "出版日" });
			}

			if (!StringUtils.isEmpty(releaseDateErrorMessage)) {
				// LOG出力
				logger.debug("releaseDateError");
				model.addAttribute("releaseDateErrorMessage", releaseDateErrorMessage);
				result.addError(new ObjectError("error", null));
			}
		} catch (ParseException e) {
			releaseDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM003E, new String[] { "出版日" });

			// LOG出力
			logger.debug("releaseDateError");
			model.addAttribute("releaseDateErrorMessage", releaseDateErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		// ISBN番号のエラーチェック
		if (!CheckerUtil.dataRequiredCheck(dto.getIsbn())) {
			isbnErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E, new String[] { "ISBN番号" });
		} else if (!CheckerUtil.stringLengthCheck(dto.getIsbn(), 13, 13)) {
			isbnErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
					new String[] { "ISBN番号", "13" });
		} else if (!CheckerUtil.dataTypeCheck(dto.getIsbn(), CommonConstants.REGEX_HALF_WIDTH_NUMBER)) {
			isbnErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM014E, new String[] { "ISBN番号" });
		}

		if (!StringUtils.isEmpty(isbnErrorMessage)) {
			// LOG出力
			logger.debug("isbnError");
			model.addAttribute("isbnErrorMessage", isbnErrorMessage);
			ObjectError validationError = new ObjectError("isbnErrorMessage", isbnErrorMessage);
			result.addError(validationError);
		}

		try {
			// 入荷年月日のエラーチェック
			if (!CheckerUtil.stringLengthCheck(dto.getArrival(), 0, 10)) {
				arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
						new String[] { "入荷年月日", "10" });
			} else if (!CheckerUtil.dateFormatCheck(dto.getArrival())) {
				arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
						new String[] { "入荷年月日", "yyyy/MM/dd" });
			} else if (CheckerUtil.compareToDate(dto.getArrival()) == CommonConstants.CHECK_RESULT_AFTER_DATE) {
				arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGBKS001E,
						new String[] { "入荷年月日" });
			}

			if (StringUtils.isEmpty(releaseDateErrorMessage) && StringUtils.isEmpty(arrivalDateErrorMessage)) {
				if (checkArrivalToRelease(dto.getReleaseDate(), dto.getArrival())) {
					arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGBKS002E, new String[] {});
				}
			}

			if (!StringUtils.isEmpty(arrivalDateErrorMessage)) {
				// LOG出力
				logger.debug("arrivalDateError");
				model.addAttribute("arrivalDateErrorMessage", arrivalDateErrorMessage);
				result.addError(new ObjectError("error", null));
			}
		} catch (ParseException e) {
			arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM003E,
					new String[] { "入荷年月日" });

			// LOG出力
			logger.debug("arrivalDateErrorMessage");
			model.addAttribute("arrivalDateErrorMessage", arrivalDateErrorMessage);
			result.addError(new ObjectError("error", null));
		}
	}

	/**
	 * 入力文字列が「yyyy/MM/dd」形式ではない場合、例外発生（ParseException）。
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	private static boolean checkArrivalToRelease(String releaseDate, String arrivalDate) throws ParseException {

		// SimpleDateFormatで書式を指定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		Date release = sdf.parse(releaseDate);
		Date arrival = sdf.parse(arrivalDate);

		int compareResult = release.compareTo(arrival);

		if (compareResult == 1) {

			// 過去日
			return true;
		}

		return false;
	}
}
