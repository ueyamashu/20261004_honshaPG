package com.springboot.libraryPJ.BKS.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIS40Dto;
import com.springboot.libraryPJ.BKS.dto.TSBKSBIS40outDto;
import com.springboot.libraryPJ.BKS.service.TSBKSBIS40Service;

@Controller
@RequestMapping("/TSBKSBIS40/")
public class TSBKSBIS40Controller {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TSBKSBIS40Service tsbksbis40Service;

	@GetMapping("init")
	public String initTSCOMLOG40(@ModelAttribute("isbn") String isbn, @ModelAttribute("bookIdList") List<Integer> bookIdList, Model model) {
		// LOG出力
		logger.debug("TSBKSBIC40");
		logger.debug("inserted isbn: " + isbn + " idList = "+ bookIdList);

		TSBKSBIS40outDto outDto = tsbksbis40Service.searchInfo(isbn, bookIdList.get(0));

		// サービスからの取得結果をmodel、sessionに格納
		TSBKSBIS40Dto tsbksbis40dto = new TSBKSBIS40Dto();
		tsbksbis40dto.setIsbn(outDto.getIsbn());
		tsbksbis40dto.setTitle(outDto.getTitle());
		tsbksbis40dto.setCategory(outDto.getCategory());
		tsbksbis40dto.setAuthor(outDto.getAuthor());
		tsbksbis40dto.setPublisher(outDto.getPublisher());
		tsbksbis40dto.setReleaseDate(outDto.getReleaseDate());
		tsbksbis40dto.setBookCount(outDto.getBookCount());
		tsbksbis40dto.setArrivalDate(outDto.getArrivalDate());
		tsbksbis40dto.setBookIds(bookIdList.toString().replaceAll("[\\[\\]]", ""));

		model.addAttribute("tsbksbis40dto", tsbksbis40dto);

		return "book/book_stock_complete";
	}
}
