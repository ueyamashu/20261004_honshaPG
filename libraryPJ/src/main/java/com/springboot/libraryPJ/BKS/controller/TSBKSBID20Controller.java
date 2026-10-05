package com.springboot.libraryPJ.BKS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIDinDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBID20Service;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

@Controller
@RequestMapping("/TSBKSBID20/")
public class TSBKSBID20Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TSBKSBID20Service tsbksbid20service;

	/**
	 * 資料情報廃棄画面の初期表示
	 *
	 * @param bookId
	 * @return
	 */
	@GetMapping("/init/{id}")
	public String initTSBKSBID20(@PathVariable("id") String bookId, Model model) {
		// ログ出力
		logger.debug("TSBKSBID20 init id:" + bookId);

		// 資料IDが返却済みかを確認する。
		String rentalFlag = tsbksbid20service.checkRenturnedBookByBookId(bookId);

		// 返却済みではない場合、エラーメッセージを表示する
		if (CommonConstants.NOT_DELETE.equals(rentalFlag)) {
			// LOG出力
			logger.debug("initTSBKSBID20 error");
			String errMsg = MessageConstants.getMessage(MessageConstants.MSGBKS004E, new String[] {});
			model.addAttribute("validationBookIdError", errMsg);
		}

		// 資料IDをDTOに格納
		TSBKSBIDinDto inDto = new TSBKSBIDinDto();
		inDto.setBookId(bookId);
		inDto.setRentalFlag(rentalFlag);
		model.addAttribute("inDto", inDto);

		// 画面遷移
		return "book/discard_input";
	}
}
