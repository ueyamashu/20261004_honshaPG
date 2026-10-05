package com.springboot.libraryPJ.BKS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIRinDto;

/**
 * 資料情報登録画面
 */
@Controller
@RequestMapping("/TSBKSBIR20/")
public class TSBKSBIR20Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 初期表示
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSBKSBIR20(@ModelAttribute TSBKSBIRinDto dto, Model model) {
		// 入力内容のmodel格納
		// 初期処理のため不要

		// LOG出力
		logger.debug("TSBKSBIR20");

		// 入力パラメータの単項目チェック
		// サービスの入力パラメータ（入力DTO）設定
		// サービス呼び出し
		// 業務エラー処理
		// 初期処理のため不要

		// サービスからの取得結果をmodel、sessionに格納
		model.addAttribute("registerData", dto);

		// 画面遷移
		return "book/book_register_input";
	}
}
