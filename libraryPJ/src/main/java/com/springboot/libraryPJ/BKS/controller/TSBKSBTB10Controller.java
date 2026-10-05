package com.springboot.libraryPJ.BKS.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.springboot.libraryPJ.BKS.dto.TSBKSBTBDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBTBinDto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBTBoutDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBTB10Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

/**
 * 資料一覧画面Controller
 */
@Controller
@RequestMapping("/TSBKSBTB10/")
public class TSBKSBTB10Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSBKSBTB10Service tsbksBtb10Service;

	/**
	 * 資料一覧画面初期表示
	 * 
	 * @param tsbksBtbDto
	 * @param result
	 * @param model
	 * @return ２次開発 ページネーション機能
	 */

	@GetMapping("init")
	public String initTSBKSBTB10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSBKSBTBDto tsbksBtbDto, BindingResult result, Model model) {
		// 入力内容のmodel格納
		model.addAttribute("tsbksBtbDto", tsbksBtbDto);

		// LOG出力
		logger.debug("TSBKSBTB10 ： 資料一覧画面_初期表示");

		// 入力パラメータの単項目チェック
		// 初期処理のため不要

		// サービスの入力パラメータ（入力DTO）設定
		// 空の入力DTO使用
		TSBKSBTBinDto inDto = new TSBKSBTBinDto();

		// サービス呼び出し
		// 資料情報一覧リストを取得する
		TSBKSBTBoutDto outDto = tsbksBtb10Service.searchBookInfo(inDto);

		// ページネーションを取得するリスト
		List<Object> pageList = new ArrayList<>();

		// 業務エラー処理
		if (outDto.getResultCd() != 0) {
			// エラーを表示する
			logger.debug("Data Empty");
			model.addAttribute("validationError", outDto.getErrmsg());
			ObjectError error = new ObjectError("validationError", outDto.getErrmsg());
			result.addError(error);

			// 共通
			CommonUtil.pageModule(page, size, model, pageList, "bookList", "/TSBKSBTB10/init");
			return "book/book_list";
		}
		// ページネーション（正しい処理）
		for (Object item : outDto.getBookList()) {
			pageList.add(item);
		}

		CommonUtil.pageModule(page, size, model, pageList, "bookList", "/TSBKSBTB10/init?");

		// 画面遷移
		return "book/book_list";
	}

	/**
	 * 資料一覧検索ボタン
	 * 
	 * @param tsbksBtbDto
	 * @param result
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("search")
	public String search(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSBKSBTBDto tsbksBtbDto, BindingResult result, Model model) {
		// 入力内容のmodel格納
		model.addAttribute("tsbksBtbDto", tsbksBtbDto);

		// LOG出力
		logger.debug("bookTitle : " + tsbksBtbDto.getBookTitle());
		logger.debug("authorName : " + tsbksBtbDto.getAuthorName());
		logger.debug("categoryCode : " + tsbksBtbDto.getCategoryCode());
		// 2次開発 LOG出力
		logger.debug("isbn : " + tsbksBtbDto.getIsbn());

		// 入力パラメータの単項目チェック
		String message = "入力内容を確認してください。";

		String baseUrl = "/TSBKSBTB10/search" + "?bookTitle=" + tsbksBtbDto.getBookTitle() + "&authorName="
				+ tsbksBtbDto.getAuthorName() + "&categoryCode=" + tsbksBtbDto.getCategoryCode() + "&isbn="
				+ tsbksBtbDto.getIsbn() + "&";

		// ページネーションを取得するリスト
		List<Object> pageList = new ArrayList<>();

		// 入力チェック ― 資料名 （桁数 ～１００桁）
		// 入力チェック ― 資料名 （桁数 ～１００桁）
		if (!CheckerUtil.stringLengthCheck(tsbksBtbDto.getBookTitle(), 0, CommonConstants.BOOK_TABLE_TITLE_MAX_SIZE)) {

			logger.debug("bookTitle Length Error");
			model.addAttribute("validationError",
					MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] { "資料名", "100" }));

			ObjectError validationError = new ObjectError("validationError", message);
			result.addError(validationError);

			CommonUtil.pageModule(page, size, model, pageList, "bookList", baseUrl);
			return "book/book_list";
		}

		// 入力チェック ― 著者名 （桁数 ～５０桁）
		if (!CheckerUtil.stringLengthCheck(tsbksBtbDto.getAuthorName(), 0,
				CommonConstants.BOOK_TABLE_AUTHOR_MAX_SIZE)) {

			logger.debug("authorName Length Error");
			model.addAttribute("validationError",
					MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] { "著者名", "50" }));

			ObjectError validationError = new ObjectError("validationError", message);
			result.addError(validationError);

			CommonUtil.pageModule(page, size, model, pageList, "bookList", baseUrl);
			return "book/book_list";
		}

		// 入力チェック ― 分類 （コード値範囲チェック）
		if (CheckerUtil.dataRequiredCheck(tsbksBtbDto.getCategoryCode()) && !CheckerUtil
				.dataExistCheck(tsbksBtbDto.getCategoryCode(), CommonConstants.BOOK_TABLE_CATEGORY_CODE_VALUE)) {

			logger.debug("categoryCode Length Error");
			model.addAttribute("validationError",
					MessageConstants.getMessage(MessageConstants.MSGCOM003E, new String[] { "分類" }));

			ObjectError validationError = new ObjectError("validationError", message);
			result.addError(validationError);

			CommonUtil.pageModule(page, size, model, pageList, "bookList", baseUrl);
			return "book/book_list";
		}

		// ２次開発
		// 入力チェック ― ISBN番号 （桁数 ～13桁）
		if (!StringUtils.isEmpty(tsbksBtbDto.getIsbn())) {
			// ISBN番号が空でない場合、正規表現チェックを行う
			if (!CheckerUtil.dataTypeCheck(tsbksBtbDto.getIsbn(), CommonConstants.REGEX_HALF_WIDTH_NUMBER)) {
				logger.debug("ISBN Format Error");
				model.addAttribute("validationError",
						MessageConstants.getMessage(MessageConstants.MSGCOM014E, new String[] { "ISBN番号" }));

				ObjectError validationError = new ObjectError("validationError", message);
				result.addError(validationError);

				CommonUtil.pageModule(page, size, model, pageList, "bookList", baseUrl);
				return "book/book_list";
			}

			if (!CheckerUtil.stringLengthCheck(tsbksBtbDto.getIsbn(), CommonConstants.BOOK_TABLE_ISBN_MAX_SIZE,
					CommonConstants.BOOK_TABLE_ISBN_MAX_SIZE)) {

				logger.debug("ISBN Length Error");
				model.addAttribute("validationError",
						MessageConstants.getMessage(MessageConstants.MSGCOM004E, new String[] { "ISBN番号", "13桁" }));

				ObjectError validationError = new ObjectError("validationError", message);
				result.addError(validationError);

				CommonUtil.pageModule(page, size, model, pageList, "bookList", baseUrl);
				return "book/book_list";
			}
		}

		// サービスの入力パラメータ（入力DTO）設定
		TSBKSBTBinDto inDto = new TSBKSBTBinDto();

		inDto.setBookTitle(tsbksBtbDto.getBookTitle());
		inDto.setAuthorName(tsbksBtbDto.getAuthorName());
		inDto.setCategoryCode(tsbksBtbDto.getCategoryCode());
		// ２次開発
		inDto.setIsbn(tsbksBtbDto.getIsbn());

		// サービス呼び出し
		TSBKSBTBoutDto outDto = tsbksBtb10Service.searchBookInfo(inDto);

		// 業務エラー処理
		if (outDto.getResultCd() != 0) {
			// エラーを表示する
			logger.debug("Data Empty");
			model.addAttribute("validationError", outDto.getErrmsg());
			ObjectError error = new ObjectError("validationError", outDto.getErrmsg());
			result.addError(error);

			// 共通
			CommonUtil.pageModule(page, size, model, pageList, "bookList", baseUrl);
			return "book/book_list";
		}

		// ページネーション（正しい処理）
		for (Object item : outDto.getBookList()) {
			pageList.add(item);
		}

		// 共通
		CommonUtil.pageModule(page, size, model, pageList, "bookList", baseUrl);

		// 画面遷移
		return "book/book_list";
	}
}
