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

import com.springboot.libraryPJ.BKS.dto.TSBKSBIS20Dto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIS30inDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIS30outDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBIS30Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 資料入庫確認画面
 */
@Controller
@RequestMapping("/TSBKSBIS30/")
public class TSBKSBIS30Controller {
	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSBKSBIS30Service tsbksbis30Service;

	@PostMapping("init")
	public String initTSBKSBIC30(@ModelAttribute TSBKSBIS20Dto tsbksbis20Dto, BindingResult result, Model model) {

		logger.debug("addBookCount Error" + tsbksbis20Dto);

		model.addAttribute("tsbksbis20dto", tsbksbis20Dto);
		
		// 入力パラメータの単項目チェック
		if (StringUtils.isEmpty(tsbksbis20Dto.getArrivalDate())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

			tsbksbis20Dto.setArrivalDate(sdf.format(new Date(System.currentTimeMillis())));
		}

		isVaild(tsbksbis20Dto, result, model);
		if (result.hasErrors()) {

			// 画面遷移
			// 資料入庫画面へ移動
			return "book/book_stock_input";
		}
		model.addAttribute("tsbksbis20dto", tsbksbis20Dto);

		return "book/book_stock_confirm";
	}

	@PostMapping("confirm")
	public String confirm(RedirectAttributes redirectAttributes, @ModelAttribute TSBKSBIS20Dto tsbksbis20Dto,
			BindingResult result, HttpSession session, Model model) {

		model.addAttribute("tsbksbis20Dto", tsbksbis20Dto);
		logger.debug("" +tsbksbis20Dto);
		// log出力
		logger.debug("TSBKSBIC30 confirm ISBN: " + tsbksbis20Dto.getIsbn() + " addBookCount: "
				+ tsbksbis20Dto.getAddBookCount());

		TSBKSBIS30inDto inDto = new TSBKSBIS30inDto();
		
		inDto.setIsbn(tsbksbis20Dto.getIsbn());
		inDto.setArrivalDate(tsbksbis20Dto.getArrivalDate());
		inDto.setAddBookCount(tsbksbis20Dto.getAddBookCount());
		
		// DB登録
		TSBKSBIS30outDto outDto = tsbksbis30Service.insertLibraryInfo(inDto, session);

		// 値渡し（bookIdのみ)
		redirectAttributes.addFlashAttribute("isbn", outDto.getIsbn());
		redirectAttributes.addFlashAttribute("bookIdList", outDto.getBookIdList());

		// 画面遷移
		return "redirect:/TSBKSBIS40/init";
	}

	private void isVaild(@ModelAttribute TSBKSBIS20Dto dto, BindingResult result, Model model) {

		String addBookCountErrorMessage = null;
		String arrivalDateErrorMessage = null;

		if (dto.getAddBookCount() == 0) {
			addBookCountErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { "追加本数" });
		} else if (!(1 <= dto.getAddBookCount()) && 10 >= dto.getAddBookCount()) {
			addBookCountErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM003E,
					new String[] { "追加本数" });
		}

		if (!StringUtils.isEmpty(addBookCountErrorMessage)) {
			// LOG出力
			logger.debug("addBookCount Error");
			model.addAttribute("addBookCountErrorMessage", addBookCountErrorMessage);
			result.addError(new ObjectError("error", null));
		}

		try {
			// 入荷年月日のエラーチェック
			if (!CheckerUtil.stringLengthCheck(dto.getArrivalDate(), 0, 10)) {
				arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E,
						new String[] { "入荷年月日", "10" });
			} else if (!CheckerUtil.dateFormatCheck(dto.getArrivalDate())) {
				arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM005E,
						new String[] { "入荷年月日", "yyyy/MM/dd" });
			} else if (CheckerUtil.compareToDate(dto.getArrivalDate()) == CommonConstants.CHECK_RESULT_AFTER_DATE) {
				arrivalDateErrorMessage = MessageConstants.getMessage(MessageConstants.MSGBKS001E,
						new String[] { "入荷年月日" });
			} else {
				if (checkArrivalToRelease(dto.getReleaseDate(), dto.getArrivalDate())) {
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
