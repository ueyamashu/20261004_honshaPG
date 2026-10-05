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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIC20DTO;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIC20outDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBIC30Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 資料情報更新確認画面
 */
@Controller
@RequestMapping("/TSBKSBIC30/")
public class TSBKSBIC30Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSBKSBIC30Service tsbksbic30Service;

	@PostMapping("init")
	public String initTSBKSBIC30(@ModelAttribute TSBKSBIC20DTO tsbksbic20Dto, BindingResult result, Model model) {
		model.addAttribute("tsbksbic20dto", tsbksbic20Dto);

		validationCheck(tsbksbic20Dto, result, model);
		if (result.hasErrors()) {

			// 更新画面でのISBNはDISABLE項目のため、値を新たに設定する必要あり
			tsbksbic20Dto.setIsbn(tsbksbic20Dto.getIsbnOrigin());
			// 画面遷移
			// 資料情報登録画面へ移動
			return "book/book_edit_input";
		}
		model.addAttribute("tsbksbic20dto", tsbksbic20Dto);
		return "book/book_edit_confirm";

	}

	@PostMapping("confirm")
	public String confirm(RedirectAttributes redirectAttributes, @ModelAttribute TSBKSBIC20DTO tsbksbic20dto,
			BindingResult result, HttpSession session, Model model) {
		model.addAttribute("tsbksbic20dto", tsbksbic20dto);

		// log出力
		logger.debug("TSBKSBIC30 confirm bookId: " + tsbksbic20dto.getBookId() + " ExcusiveKey: "
				+ tsbksbic20dto.getBookExclusiveKey());
		try {
			// DB登録
			TSBKSBIC20outDto outDto = tsbksbic30Service.updateBookInfo(tsbksbic20dto, session);

			if (outDto.getResultCd() != 0) {
				logger.debug("資料情報更新失敗");

				model.addAttribute("ErrorMessage",
						MessageConstants.getMessage(MessageConstants.MSGCOM016E, new String[] { "資料情報更新" }));

				return "book/book_edit_confirm";
			}

		} catch (Exception e) {
			logger.debug("資料情報更新失敗");

			model.addAttribute("ErrorMessage",
					MessageConstants.getMessage(MessageConstants.MSGCOM016E, new String[] { "資料情報更新" }));

			return "book/book_edit_confirm";
		}

		// 値渡し（bookIdのみ)
		redirectAttributes.addFlashAttribute("bookId", tsbksbic20dto.getBookId());

		// 画面遷移
		return "redirect:/TSBKSBIC40/init";
	}

	/**
	 *
	 * @param dto
	 * @param result
	 * @param model
	 * @throws ParseException
	 */
	public void validationCheck(@ModelAttribute TSBKSBIC20DTO dto, BindingResult result, Model model) {

		// チェック用変数初期化
		String titleErrorMessage = null;
		String categoryErrorMessage = null;
		String authorErrorMessage = null;
		String publisherErrorMessage = null;
		String releaseDateErrorMessage = null;
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
