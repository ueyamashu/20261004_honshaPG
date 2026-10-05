package com.springboot.libraryPJ.ZZ.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TSCOMMNU00Service {

}
