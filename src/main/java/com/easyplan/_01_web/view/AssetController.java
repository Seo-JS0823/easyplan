package com.easyplan._01_web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/asset")
public class AssetController {
	
	@GetMapping("")
	public String assetView() {
		return "asset/dashboard";
	}
	
	@GetMapping("/ledger/create")
	public String ledgerCreateView() {
		return "asset/ledger/ledger-create";
	}
}
