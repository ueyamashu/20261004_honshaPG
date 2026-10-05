package com.springboot.libraryPJ.BKS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.RentalEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.RentalRepository;
import com.springboot.libraryPJ.ZZ.util.CommonConstants;

/**
 * 資料情報廃棄
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBID20Service {
	@Autowired
	RentalRepository rentalRepository;

	/**
	 * 貸出中の資料かを確認する
	 *
	 * @param bookId
	 * @return
	 */
	public String checkRenturnedBookByBookId(String bookId) {
		List<RentalEntity> entity = rentalRepository.findByBookIdAndDeleteFlag(bookId, CommonConstants.NOT_DELETE);
		return entity.size() == 0 ? CommonConstants.DELETE : CommonConstants.NOT_DELETE;
	}
}
