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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.libraryPJ.BKS.dto.TSBKSBOToutDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBOT10Service;
import com.springboot.libraryPJ.ZZ.domain.entity.DelayEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;
import com.springboot.libraryPJ.ZZ.util.CommonUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 延滞資料一覧画面Controller
 */

@Controller
@RequestMapping("/TSBKSBOT10/")
@RequiredArgsConstructor
public class TSBKSBOT10Controller {

	// Logger定義
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TSBKSBOT10Service tsbksbot10Service;
	RentalRepository rentalRepository;
	TSBKSBOToutDto tsbksbotOutDto;

	/**
	 * 延滞資料一覧画面初期表示
	 * 
	 * @param tsbksBotDto
	 * @param result
	 * @param model
	 * @return ２次開発 ページネーション機能
	 */

	// 初期化
	@GetMapping("init")
	public String initTSBKSBOT10(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSBKSBOToutDto tsbksBotDto, BindingResult result, Model model) {

		// DebugLog
		logger.debug("initTSBKSBOT10");

		TSBKSBOToutDto outDto = tsbksbot10Service.getDelayBookList(CommonConstants.NOT_DELETE);

		// ページネーションを取得するリスト
		List<Object> pageList = new ArrayList<>();

		if (outDto.getDelayList().size() > 0) {
			DelayEntity entity = outDto.getDelayList().get(0);
			logger.debug("initTSBKSBOT10 getRemidFlag:" + entity.getRemidFlag());
		}
		// 延滞情報一覧の初期表示
		model.addAttribute("delayList", outDto.getDelayList());
		model.addAttribute("delayClearFlg", outDto.getDelayClearFlg());

		// ページネーション（正しい処理）
		for (Object item : outDto.getDelayList()) {
			pageList.add(item);
		}

		CommonUtil.pageModule(page, size, model, pageList, "delayList", "/TSBKSBOT10/init?");

		return "book/arrears_list";
	}

	// 連絡のボタン用
	@GetMapping("contact/{id}")
	public String contact(@ModelAttribute TSBKSBOToutDto tsbksBotDto, BindingResult result, Model model,
			HttpSession session, @PathVariable String id) {
		// DebugLog
		logger.debug("contact rentalId : " + Long.parseLong(id));

		// ページネーションを取得するリスト
		tsbksbot10Service.notiFied(Long.parseLong(id), session);

		TSBKSBOToutDto outDto = tsbksbot10Service.getDelayBookList(CommonConstants.NOT_DELETE);
		// 延滞情報一覧の初期表示
		model.addAttribute("delayList", outDto.getDelayList());
		model.addAttribute("delayClearFlg", outDto.getDelayClearFlg());

		int page = 1;

		for (int i = 0; i < outDto.getDelayList().size(); i++) {

			Long rentalId = outDto.getDelayList().get(i).getRentalId();
			if (id.equals(String.valueOf(rentalId))) {
				page = (i / 5) + 1;
				break;
			}

		}
		return "redirect:/TSBKSBOT10/init?page=" + page;
	}

	// リセットのボタン用
	@GetMapping("contactClear")
	public String contactClear(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size, Pageable pageable,
			@ModelAttribute TSBKSBOToutDto tsbksBotDto, BindingResult result, Model model, HttpSession session) {

		// DebugLog
		logger.debug("contactClear");

		// ページネーションを取得するリスト
		List<Object> pageList = new ArrayList<>();

		tsbksbot10Service.contactClear(session);

		TSBKSBOToutDto outDto = tsbksbot10Service.getDelayBookList(CommonConstants.NOT_DELETE);
		// 延滞情報一覧の初期表示
		model.addAttribute("delayList", outDto.getDelayList());
		model.addAttribute("delayClearFlg", outDto.getDelayClearFlg());

		// ページネーション（正しい処理）
		for (Object item : outDto.getDelayList()) {
			pageList.add(item);
		}

		CommonUtil.pageModule(page, size, model, pageList, "delayList", "/TSBKSBOT10/init?");

		return "redirect:/TSBKSBOT10/init?page=" + page;
	}

}