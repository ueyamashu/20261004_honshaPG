package com.springboot.libraryPJ.BKS.service;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.BKS.dto.TSBKSBIDinDto;
import com.springboot.libraryPJ.ZZ.domain.entity.LibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.LibraryRepository;

/**
 * 資料情報廃棄完了
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBID40Service {

	@Autowired
	LibraryRepository libraryRepository;

	/**
	 * 廃棄した資料情報を取得する
	 *
	 * @param bookId
	 */
	public TSBKSBIDinDto getDisposalLibrary(String bookId) {
		LibraryEntity entity = libraryRepository.findByBookId(Integer.parseInt(bookId));

		TSBKSBIDinDto dto = new TSBKSBIDinDto();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		dto.setBookId(bookId);
		dto.setDisposalDate(sdf.format(entity.getDisposalDate()));
		dto.setDisposalNote(entity.getDisposalNote());
		return dto;
	}
}
