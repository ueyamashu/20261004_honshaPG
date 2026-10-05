package com.springboot.libraryPJ.BKS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIR40outDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIRinDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBIR40Service;

/**
 * 資料情報登録完了
 */
@Controller
@RequestMapping("/TSBKSBIR40/")
public class TSBKSBIR40Controller {

	@Autowired
	TSBKSBIR40Service tsbksbir40Service;

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 初期表示
	 * 
	 * @param dto
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSBKSBIR40(@ModelAttribute("registerData") TSBKSBIRinDto dto, Model model) {

		// 入力内容のmodel格納
		// 初期表示のため不要

		// LOG出力
		logger.debug("TSBKSBIR40");

		// 入力パラメータの単項目チェック
		// 完了画面のため不要

		// サービスの入力パラメータ（入力DTO）設定
		// isbnを使う

		// サービス呼び出し
		TSBKSBIR40outDto outDto = tsbksbir40Service.findBookInfo(dto.getIsbn());

		// 業務エラー処理
		if (outDto.getResultCd() != 0) {
			return "book/book_register_complete";
		} else {
			dto.setBookId(outDto.getBookId());
			dto.setTitle(outDto.getTitle());
			dto.setCategory(outDto.getCategory());
			dto.setAuthor(outDto.getAuthor());
			dto.setPublisher(outDto.getPublisher());
			dto.setReleaseDate(outDto.getReleaseDate());
			dto.setIsbn(outDto.getIsbn());
			dto.setArrival(outDto.getArrival());
		}

		// サービスからの取得結果をmodel、sessionに格納
		model.addAttribute("registerData", dto);

		return "book/book_register_complete";
	}
}
