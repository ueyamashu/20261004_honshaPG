package com.springboot.libraryPJ.RTN.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.RTN.dto.TSRTNRTB40outDto;
import com.springboot.libraryPJ.RTN.service.TSRTNRTB40Service;

/*
 * 資料返却完了
 */
@Controller
@RequestMapping("/TSRTNRTB40/")
public class TSRTNRTB40Controller {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSRTNRTB40Service tsrtnrtb40Service;

	/**
	 * 資料返却完了画面の初期表示
	 *
	 * @param inDto
	 * @param rentalIdList
	 * @param model
	 * @return
	 */
	@GetMapping("init")
	public String initTSRTNRTHB40(@ModelAttribute("rentalIdList") List<String> rentalIdList, Model model) {
		model.addAttribute("rentalIdList", rentalIdList);
		// log出力
		logger.debug("TSRTNRTB40 init  rentalIdList = " + rentalIdList);

		// サービス呼び出し
		List<TSRTNRTB40outDto> outDto = tsrtnrtb40Service.getReturnedBook(rentalIdList);

		logger.debug("" + outDto);

		// 返却資料情報をmodelの格納
		model.addAttribute("outDto", outDto);

		// 画面遷移
		return "book/return_complete";

	}
}
