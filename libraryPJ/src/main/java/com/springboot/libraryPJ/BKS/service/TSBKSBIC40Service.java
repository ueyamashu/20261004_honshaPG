package com.springboot.libraryPJ.BKS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.libraryPJ.ZZ.domain.entity.BookLibraryEntity;
import com.springboot.libraryPJ.ZZ.domain.repository.BookLibraryRepository;

/**
 * 資料情報更新完了画面
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TSBKSBIC40Service {

	@Autowired
	BookLibraryRepository booklibraryRepository;

	/**
	 * @return
	 */
	public List<BookLibraryEntity> findAll() {
		return booklibraryRepository.findAll();
	}
}
