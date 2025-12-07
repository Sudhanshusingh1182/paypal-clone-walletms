package com.paypal.walletms.controller;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.walletms.pojo.CaptureHoldRequest;
import com.paypal.walletms.pojo.CreateWalletRequest;
import com.paypal.walletms.pojo.CreateWalletResponse;
import com.paypal.walletms.pojo.CreditRequest;
import com.paypal.walletms.pojo.CreditResponse;
import com.paypal.walletms.pojo.DebitRequest;
import com.paypal.walletms.pojo.DebitResponse;
import com.paypal.walletms.pojo.WalletHoldRequest;
import com.paypal.walletms.pojo.WalletHoldResponse;
import com.paypal.walletms.pojo.WalletResponse;
import com.paypal.walletms.service.WalletServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class WalletController {

	@Autowired
	private WalletServiceImpl walletServiceImpl;

	@PostMapping("/api/walletms/v1/wallet")
	public CreateWalletResponse createWallet(@RequestBody CreateWalletRequest createWalletRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return walletServiceImpl.createWallet(createWalletRequest);
		} finally {
			log.debug("Time taken createWallet: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@PostMapping("/api/walletms/v1/credit")
	public CreditResponse credit(@RequestBody CreditRequest creditRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return walletServiceImpl.credit(creditRequest);
		} finally {
			log.debug("Time taken credit: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@PostMapping("/api/walletms/v1/debit")
	public DebitResponse debit(@RequestBody DebitRequest debitRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return walletServiceImpl.debit(debitRequest);
		} finally {
			log.debug("Time taken debit: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@GetMapping("/api/walletms/v1/wallet")
	public WalletResponse getWallet(@RequestParam(required = true) Long userId) {
		long currentTime = System.currentTimeMillis();
		try {
			return walletServiceImpl.getWallet(userId);
		} finally {
			log.debug("Time taken getWallet: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@PostMapping("/api/walletms/v1/hold")
	public WalletHoldResponse placeHold(@RequestBody WalletHoldRequest walletHoldRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return walletServiceImpl.placeHold(walletHoldRequest);
		} finally {
			log.debug("Time taken placeHold: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@PostMapping("/api/walletms/v1/capture")
	public WalletResponse captureHold(@RequestBody CaptureHoldRequest captureHoldRequest) {
		long currentTime = System.currentTimeMillis();
		try {
			return walletServiceImpl.captureHold(captureHoldRequest);
		} finally {
			log.debug("Time taken captureHold: {}", (System.currentTimeMillis() - currentTime));
		}
	}

	@PostMapping("/api/walletms/v1/release")
	public WalletHoldResponse releaseHold(@RequestParam(required = true) String holdReference) {
		long currentTime = System.currentTimeMillis();
		try {
			return walletServiceImpl.releaseHold(holdReference);
		} finally {
			log.debug("Time taken releaseHold: {}", (System.currentTimeMillis() - currentTime));
		}
	}

}
