package com.springboot.libraryPJ.MEM.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.libraryPJ.MEM.dto.TSMEMMIC20DTOForm;
import com.springboot.libraryPJ.MEM.dto.TSMEMMID30Dto;
import com.springboot.libraryPJ.MEM.dto.TSMEMMID30outDTO;
import com.springboot.libraryPJ.MEM.service.TSMEMMID30Service;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;

import jakarta.servlet.http.HttpSession;

/*
 * 会員退会確認
 */

@Controller
@RequestMapping("/TSMEMMID30/")
public class TSMEMMID30Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMID30Service tSMEMMID30Service;

	// 1. メソッド宣言
	@GetMapping("init/{id}")
	public String initTSMEMMID30(@PathVariable String id, Model model) {
		logger.debug("initTSMEMMID30 memberId: " + id);

		// 2. 入力内容のmodel格納

		// 3. ログ出力
		logger.debug("memberId: " + id);

		// 4. 入力パラメータの単項目チェック

		// ５．サービスの入力パラメータ（入力DTO）設定

		TSMEMMID30outDTO tsmemmid30outdto = tSMEMMID30Service.selectMember(id, CommonConstants.NOT_DELETE);

		// ６．サービス呼び出し
		// ７．業務エラー処理

		// 本を借りているかどうかのチェック
		if (tSMEMMID30Service.hasBorrowedBooks(id)) {
			model.addAttribute("borrowedBookMessage", "貸出中の資料があるため退会できません");
		}

		// 8. サービスからの取得結果をmodel、sessionに格納
		TSMEMMIC20DTOForm form = new TSMEMMIC20DTOForm();

		form.setMemberId(tsmemmid30outdto.getMemberId());
		form.setMemberKbn(tsmemmid30outdto.getMembership());
		form.setMemberName(tsmemmid30outdto.getMemberName());
		form.setBirthDate(tsmemmid30outdto.getBirthday());
		form.setPostNo(tsmemmid30outdto.getPostalCode());
		form.setPostNo1(tsmemmid30outdto.getPostalCode().substring(0, 3));
		form.setPostNo2(tsmemmid30outdto.getPostalCode().substring(4, 8));
		form.setAddress(tsmemmid30outdto.getAddress());
		form.setPhoneNo(tsmemmid30outdto.getTelNumber());
		form.setPhoneNo1(tsmemmid30outdto.getTelNumber().substring(0, 3));
		form.setPhoneNo2(tsmemmid30outdto.getTelNumber().substring(4, 8));
		form.setPhoneNo3(tsmemmid30outdto.getTelNumber().substring(9, 13));
		form.setMailAddress(tsmemmid30outdto.getEmail());
		form.setMemberKbn(tsmemmid30outdto.getMembership());
		model.addAttribute("TSMEMMIC20DTOForm", form);

		return "member/withdrawl_confirm";
	}


	@PostMapping("complete")
	public String withdrawlConfirm(RedirectAttributes redirectAttributes,
			@ModelAttribute("TSMEMMIC20DTOForm") TSMEMMID30Dto form, BindingResult result, HttpSession session,
			Model model) {

		// 3. ログ出力
		logger.debug("withdrawlConfirm memberId: " + form.getMemberId());

		try {

			model.addAttribute("TSMEMMIC30Dto", form);
			// DB更新
			tSMEMMID30Service.deleteMember(form, session);

			// 値渡し（memberIdのみ）
			redirectAttributes.addAttribute("id", form.getMemberId());

			// log出力
			logger.debug("TSMEMMID30 complete id:" + form.getMemberId());

			// 画面遷移
			return "redirect:/TSMEMMID40/init/{id}";

		} catch (Exception e) {
			model.addAttribute("error", "エラーが発生しました。再度お試しください。");
			return "member/withdrawl_confirm";
		}

	}

}