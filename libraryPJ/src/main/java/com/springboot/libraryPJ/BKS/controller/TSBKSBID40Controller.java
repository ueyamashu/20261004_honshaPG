package com.springboot.libraryPJ.BKS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIDinDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBID40Service;

@Controller
@RequestMapping("/TSBKSBID40/")
public class TSBKSBID40Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TSBKSBID40Service tsbksbid40service;

	/**
	 * 資料返却完了画面
	 *
	 * @param bookId
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSBKSBID40(@ModelAttribute("bookId") String bookId, Model model) {
		// ログ出力
		logger.debug("TSBKSBID40 init id:" + bookId);

		// サービス呼び出し
		TSBKSBIDinDto dto = tsbksbid40service.getDisposalLibrary(bookId);

		// サービスからの取得結果をmodelに格納
		model.addAttribute("tsbksbid40dto", dto);

		// 画面遷移
		return "book/discard_complete";
	}
}
