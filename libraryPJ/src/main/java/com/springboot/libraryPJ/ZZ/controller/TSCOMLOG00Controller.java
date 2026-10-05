package com.springboot.libraryPJ.ZZ.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import com.springboot.libraryPJ.ZZ.domain.entity.MemberEntity;
import com.springboot.libraryPJ.ZZ.dto.TSCOMLOGDto;
import com.springboot.libraryPJ.ZZ.service.TSCOMLOG00Service;
import com.springboot.libraryPJ.ZZ.util.CheckerUtil;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.MessageConstants;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * ログイン画面
 */
@Controller
@RequestMapping("/")
public class TSCOMLOG00Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSCOMLOG00Service tscomlog00Service;

	/**
	 * 画面初期表示
	 *
	 * @param tscomLogDto
	 * @param model
	 * @return
	 */
	@GetMapping("")
	public String displayLogin(@ModelAttribute TSCOMLOGDto tscomLogDto, Model model) {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		model.addAttribute("tscomLogDto", tscomLogDto);
		return "common/login";
	}

	/**
	 * ログインボタン押下
	 *
	 * @param tscomLogDto
	 * @param result
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("login")
	public String login(@Valid TSCOMLOGDto tscomLogDto, BindingResult result, HttpSession session, Model model) {
		model.addAttribute("tscomLogDto", tscomLogDto);

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		// バリデーションチェック
		validationCheck(tscomLogDto, result, model);

		// バリデーションエラー発生
		if (model.containsAttribute("validationMemberIdError") || model.containsAttribute("validationPasswordError")) {
			return "common/login";
		}

		try {
			// 会員情報確認
			List<MemberEntity> member = tscomlog00Service.searchLoginMember(tscomLogDto);
			// 会員情報確認結果判定
			if (member.size() == 0) {
				// 会員が存在しない場合：ログインIDとパスワードに該当する会員情報が存在しません
				model.addAttribute("error", MessageConstants.MSGCOM999E);

				// ログイン画面に戻る
				return "common/login";
			} else {
				// ログインユーザーチェック
				for (MemberEntity memberData : member) {
					// 会員区分 ： 職員でない場合
					if (!memberData.getMemberClass().equals(CommonConstants.MEMBERSHIP_STAFF)) {
						// 職員ユーザーのみでログインしてください
						model.addAttribute("error", MessageConstants.MSGZZ001E);
						// ログイン画面に戻る
						return "common/login";
					}
				}
			}
			// ログイン情報のセッション格納
			session.setAttribute("member", member);
			session.setAttribute("memberId", tscomLogDto.getMemberId());

			// メインメニュー画面に遷移
			return "common/main";
		} catch (Exception e) {
			logger.debug("会員情報取得失敗");

			model.addAttribute("error", MessageConstants.getMessage(MessageConstants.MSGCOM016E,
					new String[] { CommonConstants.STRING_MEMBER_JOHO_SELECT }));
			// ログイン画面に戻る
			return "common/login";
		}
	}

	/**
	 * 登録ボタン押下
	 *
	 * @return
	 */
	@GetMapping("member/register")
	public String displayMemberReister() {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		return "redirect:/TSCOMMIR20/init";
	}

	/**
	 * バリデーションチェック
	 *
	 * @param tscomLogDto
	 * @param result
	 * @param model
	 */
	public void validationCheck(@Valid TSCOMLOGDto tscomLogDto, BindingResult result, Model model) {

		// Study用ログ出力
		logger.trace(
				this.getClass().toGenericString() + " / " + Thread.currentThread().getStackTrace()[1].getMethodName());

		String memberIdMessage = null;
		String passwordMessage = null;

		// 会員ID
		if (!CheckerUtil.dataRequiredCheck(tscomLogDto.getMemberId())) {
			memberIdMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_MEMBERID });
		} else if (!CheckerUtil.dataTypeCheck(tscomLogDto.getMemberId(), CommonConstants.REGEX_HALF_WIDTH_NUMBER)) {
			memberIdMessage = MessageConstants.getMessage(MessageConstants.MSGCOM004E,
					new String[] { CommonConstants.STRING_MEMBERID, CommonConstants.STRING_MEMBER_ID_FORMAT });
		} else if (!CheckerUtil.stringLengthCheck(tscomLogDto.getMemberId(), CommonConstants.MIN_SIZE,
				CommonConstants.MEMBER_TABLE_MEMBERID_MAX_SIZE)) {
			memberIdMessage = MessageConstants.getMessage(MessageConstants.MSGCOM006E, new String[] {
					CommonConstants.STRING_MEMBERID, CommonConstants.MEMBER_TABLE_MEMBERID_MAX_SIZE_STRING });
		}

		// パスワード
		if (!CheckerUtil.dataRequiredCheck(tscomLogDto.getPassword())) {
			passwordMessage = MessageConstants.getMessage(MessageConstants.MSGCOM001E,
					new String[] { CommonConstants.STRING_PASSWORD });
		} else if (!CheckerUtil.dataTypeCheck(tscomLogDto.getPassword(), CommonConstants.REGEX_HALF_WIDTH_ENG_NUMBER)) {
			passwordMessage = MessageConstants.getMessage(MessageConstants.MSGCOM004E,
					new String[] { CommonConstants.STRING_PASSWORD, CommonConstants.STRING_PASSWORD_FORMAT });
		} else if (!CheckerUtil.stringLengthCheck(tscomLogDto.getPassword(), CommonConstants.MEMBER_TABLE_PW_MIN_SIZE,
				CommonConstants.MEMBER_TABLE_PW_MAX_SIZE)) {
			passwordMessage = MessageConstants.getMessage(MessageConstants.MSGCOM019E, new String[] {
					CommonConstants.STRING_PASSWORD, CommonConstants.MEMBER_TABLE_PASSWORD_MIN_SIZE_STRING, CommonConstants.MEMBER_TABLE_PASSWORD_MAX_SIZE_STRING });
		}

		if (!StringUtils.isEmpty(memberIdMessage)) {
			// LOG出力
			logger.debug("memberId error");
			// 返却値設定
			model.addAttribute("validationMemberIdError", memberIdMessage);
		}

		if (!StringUtils.isEmpty(passwordMessage)) {
			// LOG出力
			logger.debug("password error");
			// 返却値設定
			model.addAttribute("validationPasswordError", passwordMessage);
		}
	}
}
