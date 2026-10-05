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
 * 会員退会完了
 */

@Controller
@RequestMapping("/TSMEMMID40/")
public class TSMEMMID40Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSMEMMID30Service tSMEMMID30Service;

	// 1. メソッド宣言
	@GetMapping("init/{id}")
	public String initTSMEMMID40(@PathVariable String id, Model model) {

		// 2. 入力内容のmodel格納

		// 3. ログ出力
		logger.debug("TSMEMMIC40 init memberId" + id);

		TSMEMMID30outDTO tsmemmid30outdto = tSMEMMID30Service.selectMember(id, CommonConstants.DELETE);

		TSMEMMIC20DTOForm form = new TSMEMMIC20DTOForm();

		form.setWithdrawalDate(tsmemmid30outdto.getWithdrawDate());
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

		// 4. 入力パラメータの単項目チェック
		// memberKbnが0なら「一般」、1なら「職員」を設定します。
		if ("0".equals(tsmemmid30outdto.getMembership())) {
			form.setMemberKbn("一般");
		} else if ("1".equals(tsmemmid30outdto.getMembership())) {
			form.setMemberKbn("職員");
		} else {
			form.setMemberKbn("不明");
		}

		// ５．サービスの入力パラメータ（入力DTO）設定
		// ６．サービス呼び出し
		// ７．業務エラー処理

		logger.debug("TSMEMMIC40 init form.getMemberId:" + form.getMemberId());

		// 8. サービスからの取得結果をmodel、sessionに格納
		model.addAttribute("TSMEMMIC20DTOForm", form);

		// 9. 画面遷移
		return "member/withdrawl_complete";
	}

	@PostMapping("returnBtn")
	public String withdrawlComplete(RedirectAttributes redirectAttributes,
			@ModelAttribute("TSMEMMIC20DTOForm") TSMEMMID30Dto form, BindingResult result, HttpSession session,
			Model model) {
		// 3. ログ出力
		logger.debug("withdrawlConfirm memberId: " + form.getMemberId());
		try {
			// 値渡し（memberIdのみ）
			redirectAttributes.addAttribute("id", form.getMemberId());

			// log出力
			logger.debug("TSMEMMID30 complete id:" + form.getMemberId());

			// 操作者自身のMemberIdを取得
			logger.debug("操作者自身のMemberIdを取得");
			String currentUserId = (String) session.getAttribute("memberId");

			logger.debug("操作者自身が退会した場合はログアウト画面に遷移する処理入場");
			// 操作者自身が退会した場合はログアウト画面に遷移する処理
			if (currentUserId.equals(form.getMemberId())) {
				logger.debug("ログアウト画面に遷移する処理開始");
				return "redirect:/";
			} else {
				// 画面遷移
				return "redirect:/member/list";
			}
		} catch (Exception e) {
			model.addAttribute("error", "エラーが発生しました。再度お試しください。");
			return "redirect:/member/list";
		}

	}
}
