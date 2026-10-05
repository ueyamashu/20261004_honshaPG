package com.springboot.libraryPJ.BKS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIS20Dto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIS20outDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBIS20Service;

/**
 * 資料入庫画面
 */
@Controller
@RequestMapping("/TSBKSBIS20/")
public class TSBKSBIS20Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSBKSBIS20Service tsbksbis20Service;

	/**
	 *
	 * @param isbn
	 * @param model
	 * @return
	 */
	@GetMapping("init/{isbn}")
	public String initTSBKSBIS20(@PathVariable("isbn") String isbn, Model model) {
		// 入力内容のmodel格納
		// 初期処理のため不要

		// ログ出力
		logger.debug("isbn: " + isbn);
		logger.debug("TSBKSBIS20");

		// 入力パラメータの単項目チェック
		// 初期処理のため不要.

		// サービスの入力パラメータ（入力DTO）設定
		// isbnをそのまま設定

		// サービス呼び出し
		TSBKSBIS20outDto outDto = tsbksbis20Service.searchInfo(isbn);

		// 業務エラー処理
		// エラー処理なし
		
		// サービスからの取得結果をModelに格納
		TSBKSBIS20Dto tsbksbis20dto = new TSBKSBIS20Dto();
		tsbksbis20dto.setIsbn(outDto.getIsbn());
		tsbksbis20dto.setTitle(outDto.getTitle());
		tsbksbis20dto.setCategory(outDto.getCategory());
		tsbksbis20dto.setAuthor(outDto.getAuthor());
		tsbksbis20dto.setPublisher(outDto.getPublisher());
		tsbksbis20dto.setReleaseDate(outDto.getReleaseDate());
		tsbksbis20dto.setBookCount(outDto.getBookCount());

		model.addAttribute("tsbksbis20dto", tsbksbis20dto);
		
		// 画面遷移
		return "book/book_stock_input";
	}
}
