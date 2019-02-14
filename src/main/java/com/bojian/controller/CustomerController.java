package com.bojian.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class CustomerController {
	
	
	@RequestMapping("/index")
	public String index(){
		return "index";
	}

}
