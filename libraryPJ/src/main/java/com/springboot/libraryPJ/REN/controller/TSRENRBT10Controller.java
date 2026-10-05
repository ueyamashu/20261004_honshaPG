package com.springboot.libraryPJ.REN.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import com.springboot.libraryPJ.REN.dto.TSRENRBTinDto;
import com.springboot.libraryPJ.REN.dto.TSRENRBToutDto;
import com.springboot.libraryPJ.REN.service.TSRENRBT10Service;
import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.dto.TSRENRBTDto;
import com.springboot.libraryPJ.ZZ.dto.TSRENRMTDto;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;

/**
 * 貸出資料一覧
 */
@Controller
@RequestMapping("/TSRENRBT10/")
public class TSRENRBT10Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRENRBT10Service tsrenrbt10Service;

	/***
	 * 貸出資料一覧初期表示
	 *
	 * @param page
	 * @param size
	 * @param pageable
	 * @param tsrenrbtDto
	 * @param session
	 * @param result
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSRENRBT10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRENRBTDto tsrenrbtDto, HttpSession session, BindingResult result, Model model) {

		// 入力内容のmodel格納。
		model.addAttribute("tsrenrbtDto", tsrenrbtDto);

		// ログを出力する。
		logger.debug("memberId: " + tsrenrbtDto.getMemberId());
		logger.debug("tsrenrmtDto: " + tsrenrbtDto);

		// ページネーション用
		List<Object> pageList = new ArrayList<>();
		// 貸出会員一覧URL設定
		String memberListUrl = "/TSRENRMT10/init?";
		// 貸出資料一覧URL設定
		String bookListUrl = "/TSRENRBT10/init" + "?memberId=" + tsrenrbtDto.getMemberId() + "&";

		// 初期チェックする。
		initCheck(tsrenrbtDto, result, model);

		// 初期チェックエラー発生した場合、貸出会員一覧画面を表示する。
		if (result.hasErrors()) {
			// 会員一覧、ページネーションをmodelに設定
			List<MemberEntity> memberList = (List<MemberEntity>) session.getAttribute("memberList");
			for (Object item : memberList) {
				pageList.add(item);
			}
			CommonUtil.pageModule(page, size, model, pageList, "memberList", memberListUrl);

			// DTO設定
			TSRENRMTDto tsrenrmtDto = new TSRENRMTDto();
			model.addAttribute("tsrenrmtDto", tsrenrmtDto);
			return "book/rental_member_list";
		}

		// 貸出可能な資料情報を取得する。
		TSRENRBToutDto outDto = tsrenrbt10Service.findBookLibraryList();

		// 取得結果がエラーの場合はエラーメッセージを出力する。
		if (outDto.getResultCd() != 0) {
			logger.debug("data empty");
			model.addAttribute("error", outDto.getErrmsg());
			CommonUtil.pageModule(page, size, model, pageList, "bookLibraryList", bookListUrl);
			return "book/rental_list";
		}

		// サービスからの取得結果をmodel、sessionに格納
		for (Object item : outDto.getBookLibraryList()) {
			pageList.add(item);
		}
		CommonUtil.pageModule(page, size, model, pageList, "bookLibraryList", bookListUrl);
		session.setAttribute("bookLibraryList", outDto.getBookLibraryList());

		// 貸出資料一覧画面に遷移
		return "book/rental_list";
	}

	/**
	 * 貸出資料検索
	 *
	 * @param page
	 * @param size
	 * @param pageable
	 * @param tsrenrbtDto
	 * @param session
	 * @param result
	 * @param model
	 * @return
	 */
	@GetMapping("search")
	public String searchRentalBookList(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSRENRBTDto tsrenrbtDto, HttpSession session, BindingResult result, Model model) {

		// 入力内容のmodel格納。
		model.addAttribute("tsrenrbtDto", tsrenrbtDto);

		// ログを出力する。
		logger.debug("bookId: " + tsrenrbtDto.getBookId());
		logger.debug("title: " + tsrenrbtDto.getTitle());
		logger.debug("tsrenrbtDto: " + tsrenrbtDto);

		// ページネーション用
		List<Object> pageList = new ArrayList<>();
		String baseUrl = "/TSRENRBT10/search" + "?memberId=" + tsrenrbtDto.getMemberId() + "&bookId="
				+ tsrenrbtDto.getBookId() + "&title=" + tsrenrbtDto.getTitle() + "&";

		// 入力パラメータの単項目チェックする。
		validationCheck(tsrenrbtDto, result, model);

		// 入力パラメータの単項目チェックエラー発生した場合貸出資料一覧画面を表示する。
		if (result.hasErrors()) {
			CommonUtil.pageModule(page, size, model, pageList, "bookLibraryList", baseUrl);
			return "book/rental_list";
		}

		// 入力DTOを設定する。
		TSRENRBTinDto indto = new TSRENRBTinDto();
		indto.setBookId(tsrenrbtDto.getBookId());
		indto.setTitle(tsrenrbtDto.getTitle());

		// 検索条件に一致する貸出可能な資料情報を取得する。
		TSRENRBToutDto outDto = tsrenrbt10Service.searchBookList(indto);

		// 取得結果がエラーの場合はエラーメッセージを出力する。
		if (outDto.getResultCd() != 0) {
			logger.debug("data empty");
			model.addAttribute("error", outDto.getErrmsg());
			CommonUtil.pageModule(page, size, model, pageList, "bookLibraryList", baseUrl);
			return "book/rental_list";
		}

		// サービスからの取得結果をmodel、sessionに格納
		for (Object item : outDto.getBookLibraryList()) {
			pageList.add(item);
		}
		CommonUtil.pageModule(page, size, model, pageList, "bookLibraryList", baseUrl);
		session.setAttribute("bookLibraryList", outDto.getBookLibraryList());

		// 貸出資料一覧画面に遷移
		return "book/rental_list";
	}

	/**
	 * 貸出会員一覧画面に戻る
	 *
	 * @return
	 */
	@GetMapping("member/list")
	public String displayRentalMmemberList() {
		return "redirect:/TSRENRMT10/init";
	}

	/**
	 * 貸出会員一覧画面で選択した会員IDをチェック
	 *
	 * @param tsrenrbtDto
	 * @param result
	 * @param model
	 */
	public void initCheck(@ModelAttribute TSRENRBTDto tsrenrbtDto, BindingResult result, Model model) {
		// 会員IDの存在をチェックする。
		if (!CheckerUtil.dataRequiredCheck(tsrenrbtDto.getMemberId())) {
			logger.debug("param memberId empty");
			model.addAttribute("paramError", MessageConstants.getMessage(MessageConstants.MSGCOM002E,
					new String[] { CommonConstants.STRING_MEMBER }));
			ObjectError paramError = new ObjectError("paramError", "");
			result.addError(paramError);
		}

		// 入力DTOを設定する。
		TSRENRBTinDto indto = new TSRENRBTinDto();
		indto.setMemberId(tsrenrbtDto.getMemberId());

		// 貸出可能会員なのかをチェックする。
		TSRENRBToutDto outDto = tsrenrbt10Service.checkRentalPossibleMember(indto);

		// 取得結果がエラーの場合はエラーメッセージを出力する。
		if (outDto.getResultCd() != 0) {
			logger.debug("rental impossible");
			model.addAttribute("paramError", outDto.getErrmsg());
			ObjectError paramError = new ObjectError("paramError", "");
			result.addError(paramError);
		}
	}

	/**
	 * 検索条件のバリデーション
	 *
	 * @param tsrenrbtDto
	 * @param result
	 * @param model
	 */
	public void validationCheck(@ModelAttribute TSRENRBTDto tsrenrbtDto, BindingResult result, Model model) {
		String bookIdErrorMessage = null;
		String titleErrorMessage = null;

		// 資料IDの値が存在する場合はバリデーションする。
		if (!StringUtils.isEmpty(tsrenrbtDto.getBookId())) {
			// 半角数字
			if (!CheckerUtil.dataTypeCheck(tsrenrbtDto.getBookId(), CheckerUtil.CHECK_TYPE_HALF_NUM)) {
				bookIdErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM014E,
						new String[] { CommonConstants.STRING_BOOKID });
			}
			// 最大長
			else if (!CheckerUtil.stringLengthCheck(tsrenrbtDto.getBookId(), CommonConstants.MIN_SIZE,
					CommonConstants.BOOK_TABLE_ID_MAX_SIZE)) {
				bookIdErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
						CommonConstants.STRING_BOOKID, CommonConstants.RENTAL_TABLE_BOOK_ID_MAX_SIZE_STRING });
			}
		}

		// 資料名の値が存在する場合はバリデーションする。
		if (!StringUtils.isEmpty(tsrenrbtDto.getTitle())) {
			// 最大長
			if (!CheckerUtil.stringLengthCheck(tsrenrbtDto.getTitle(), CommonConstants.MIN_SIZE,
					CommonConstants.BOOK_TABLE_TITLE_MAX_SIZE)) {
				titleErrorMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
						CommonConstants.STRING_TITLE, CommonConstants.RENTAL_TABLE_TITLE_MAX_SIZE_STRING });
			}
		}

		// 資料IDエラーメッセージが存在する場合はログを出力する。
		if (!StringUtils.isEmpty(bookIdErrorMessage)) {
			logger.debug("bookId error");
			model.addAttribute("validationBookIdError", bookIdErrorMessage);
			ObjectError validationError = new ObjectError("validationBookIdError", bookIdErrorMessage);
			result.addError(validationError);
		}

		// 資料名エラーメッセージが存在する場合はログを出力する。
		if (!StringUtils.isEmpty(titleErrorMessage)) {
			logger.debug("title error");
			model.addAttribute("validationTitleError", titleErrorMessage);
			ObjectError validationError = new ObjectError("validationTitleError", titleErrorMessage);
			result.addError(validationError);
		}
	}

}
