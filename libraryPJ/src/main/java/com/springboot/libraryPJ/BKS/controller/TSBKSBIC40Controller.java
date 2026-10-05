package com.springboot.libraryPJ.BKS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIC20DTO;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIC20outDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBIC20Service;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 資料情報更新完了画面
 */
@Controller
@RequestMapping("/TSBKSBIC40/")
public class TSBKSBIC40Controller {

//	Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSBKSBIC20Service tsbksbic20Service;

//	繋げる用
	@GetMapping("init")
	public String initTSCOMLOG40(@ModelAttribute("bookId") String bookId, Model model) {
		// LOG出力
		logger.debug("TSBKSBIC40");
		logger.debug("updated target bookId:" + bookId);

		// TODO:データ取得処理
		TSBKSBIC20outDto outDto = tsbksbic20Service.searchBookId(bookId);

		// 業務エラー処理
		if (outDto.getResultCd() != 0) {
			// DEBUG用LOG出力
			logger.debug("tsbksbic20Service.searchIsbn data empty");
			String message = MessageConstants.getMessage(MessageConstants.MSGCOM012E, new String[] { "資料情報" });

			ObjectError error = new ObjectError("error", message);

			model.addAttribute("error", error);

			return "book/book_edit_input";
		}

		// サービスからの取得結果をmodel、sessionに格納
		TSBKSBIC20DTO tsbksbic20dto = new TSBKSBIC20DTO();
		tsbksbic20dto.setBookId(outDto.getBookId());
		tsbksbic20dto.setTitle(outDto.getTitle());
		tsbksbic20dto.setCategory(outDto.getCategory());
		tsbksbic20dto.setAuthor(outDto.getAuthor());
		tsbksbic20dto.setPublisher(outDto.getPublisher());
		tsbksbic20dto.setReleaseDate(outDto.getReleaseDate());
		tsbksbic20dto.setIsbn(outDto.getIsbn());
		tsbksbic20dto.setArrival(outDto.getArrival());
		tsbksbic20dto.setIsbnOrigin(outDto.getIsbn());
		tsbksbic20dto.setBookExclusiveKey(outDto.getBookExclusiveKey());
		tsbksbic20dto.setLibraryExclusiveKey(outDto.getLibraryExclusiveKey());

		model.addAttribute("tsbksbic20dto", tsbksbic20dto);

		return "book/book_edit_complete";
	}
}
