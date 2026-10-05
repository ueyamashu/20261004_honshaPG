package com.springboot.libraryPJ.ZZ.controller;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

/**
 * メインメニュー画面
 */
@Controller
@RequestMapping("/")
public class TSCOMMNU00Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * メインメニューを表示
	 *
	 * @return
	 */
	@GetMapping("init")
	public String displayMainMenu() {
		return "common/main";
	}

	/**
	 * 会員一覧初期表示
	 */
	@GetMapping("member/list")
	public String displayMemberList() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSMEMMTB10/init";
	}

	/**
	 * 資料一覧初期表示
	 */
	@GetMapping("book/list")
	public String displayBookList() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSBKSBTB10/init";
	}

	/**
	 * 貸出会員一覧初期表示（貸出リンク）
	 */
	@GetMapping("book/rental/member/list")
	public String displayBookRentalMemberList() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSRENRMT10/init";
	}

	/**
	 * 資料返却一覧初期表示
	 */
	@GetMapping("book/return/list")
	public String displayBookReturnList() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSRTNRTB10/init";
	}

	/**
	 * 貸出返却履歴一覧初期表示
	 */
	@GetMapping("book/return/history/list")
	public String displayBookReturnHistoryList() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSRTNRTH10/init";
	}

	/**
	 * 貸出返却履歴一覧初期表示
	 */
	@GetMapping("book/arrears/list")
	public String displayArrearsList() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSBKSBOT10/init";
	}

	/**
	 * ログイン画面
	 */
	@GetMapping("logout")
	public String displayLogin(HttpSession session) {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		Enumeration<String> sessionAttribute = session.getAttributeNames();
		while (sessionAttribute.hasMoreElements()) {
			String attributeName = sessionAttribute.nextElement().toString();
			session.removeAttribute(attributeName);
		}
		return "redirect:/";
	}

	/**
	 * パスワード変更画面
	 */
	@GetMapping("member/password")
	public String displayChangePassword() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSMEMMPC20/init";
	}
}
