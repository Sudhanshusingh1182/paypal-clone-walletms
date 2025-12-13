package com.paypal.walletms.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.walletms.dao.WalletHoldRepo;
import com.paypal.walletms.dao.WalletRepo;
import com.paypal.walletms.entity.Wallet;
import com.paypal.walletms.entity.WalletHold;
import com.paypal.walletms.error.ErrorDetail;
import com.paypal.walletms.pojo.CaptureHoldRequest;
import com.paypal.walletms.pojo.CreateWalletRequest;
import com.paypal.walletms.pojo.CreateWalletResponse;
import com.paypal.walletms.pojo.CreditRequest;
import com.paypal.walletms.pojo.CreditResponse;
import com.paypal.walletms.pojo.DebitRequest;
import com.paypal.walletms.pojo.DebitResponse;
import com.paypal.walletms.pojo.WalletAPI;
import com.paypal.walletms.pojo.WalletHoldRequest;
import com.paypal.walletms.pojo.WalletHoldResponse;
import com.paypal.walletms.pojo.WalletResponse;
import com.paypal.walletms.util.StringUtils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WalletServiceImpl {

	@Autowired
	private WalletHoldRepo walletHoldRepo;

	@Autowired
	private WalletRepo walletRepo;
	
	private static final String STATUS_ACTIVE = "ACTIVE";
	private static final String STATUS_RELEASED = "RELEASED";
	private static final String STATUS_CAPTURED = "CAPTURED";

	@Transactional
	public CreateWalletResponse createWallet(CreateWalletRequest createWalletRequest) {
		try {
			log.debug("createWallet:: createWalletRequest: {}", createWalletRequest);

			Wallet wallet = Wallet.builder().userId(createWalletRequest.getUserId())
					.currency(createWalletRequest.getCurrency()).balance(0D).availableBalance(0D)
					.createdDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).build();

			Wallet savedWallet = walletRepo.save(wallet);
			log.debug("createWallet:: Created a wallet and saved in DB: {}", savedWallet);
			WalletAPI walletAPI = WalletAPI.builder().id(savedWallet.getId()).userId(savedWallet.getUserId())
					.currency(savedWallet.getCurrency()).balance(savedWallet.getBalance())
					.availableBalance(savedWallet.getAvailableBalance()).build();

			return CreateWalletResponse.builder().walletAPI(walletAPI).build();
		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return CreateWalletResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR))
					.build();
		}
	}

	@Transactional
	public CreditResponse credit(CreditRequest creditRequest) {
		try {
			log.debug("credit:: creditRequest: {}", creditRequest);

			Optional<Wallet> walletOpt = walletRepo.findByUserIdAndCurrency(creditRequest.getUserId(),
					creditRequest.getCurrency());

			if (walletOpt.isEmpty()) {
				log.warn("credit:: No wallet found for userId: {} and currency: {}", creditRequest.getUserId(),
						creditRequest.getCurrency());
				return CreditResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.WALLET_NOT_FOUND)).build();
			}

			Wallet wallet = walletOpt.get();

			wallet.setBalance(wallet.getBalance() + creditRequest.getAmount());
			wallet.setAvailableBalance(wallet.getAvailableBalance() + creditRequest.getAmount());
			Wallet savedWallet = walletRepo.save(wallet);
			log.debug("credit:: Credit done successfully: {}", wallet);

			// create transaction

			WalletAPI walletAPI = WalletAPI.builder().id(savedWallet.getId()).userId(savedWallet.getUserId())
					.currency(savedWallet.getCurrency()).balance(savedWallet.getBalance())
					.availableBalance(savedWallet.getAvailableBalance()).build();

			return CreditResponse.builder().walletAPI(walletAPI).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return CreditResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}

	@Transactional
	public DebitResponse debit(DebitRequest debitRequest) {
		try {
			log.debug("debit:: debitRequest: {}", debitRequest);

			Optional<Wallet> walletOpt = walletRepo.findByUserIdAndCurrency(debitRequest.getUserId(),
					debitRequest.getCurrency());

			if (walletOpt.isEmpty()) {
				log.warn("credit:: No wallet found for userId: {} and currency: {}", debitRequest.getUserId(),
						debitRequest.getCurrency());
				return DebitResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.WALLET_NOT_FOUND)).build();
			}

			Wallet wallet = walletOpt.get();

			if (wallet.getAvailableBalance() < debitRequest.getAmount()) {
				log.warn("debit:: Not enough balance for debit");
				return DebitResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INSUFFICIENT_FUND)).build();
			}

			wallet.setBalance(wallet.getBalance() - debitRequest.getAmount());
			wallet.setAvailableBalance(wallet.getAvailableBalance() - debitRequest.getAmount());
			Wallet savedWallet = walletRepo.save(wallet);
			log.debug("debit:: Debit done successfully: {}", wallet);

			WalletAPI walletAPI = WalletAPI.builder().id(savedWallet.getId()).userId(savedWallet.getUserId())
					.currency(savedWallet.getCurrency()).balance(savedWallet.getBalance())
					.availableBalance(savedWallet.getAvailableBalance()).build();

			return DebitResponse.builder().walletAPI(walletAPI).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return DebitResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}

	public WalletResponse getWallet(Long userId) {
		try {
			log.debug("getWallet:: userId: {}", userId);

			Optional<Wallet> walletOpt = walletRepo.findByUserId(userId);

			if (walletOpt.isEmpty()) {
				log.warn("getWallet:: No wallet found for the userId: {}", userId);
				return WalletResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.WALLET_NOT_FOUND)).build();
			}

			Wallet wallet = walletOpt.get();

			WalletAPI walletAPI = WalletAPI.builder().id(wallet.getId()).userId(wallet.getUserId())
					.currency(wallet.getCurrency()).balance(wallet.getBalance())
					.availableBalance(wallet.getAvailableBalance()).build();

			return WalletResponse.builder().walletAPI(walletAPI).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return WalletResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}

	@Transactional
	public WalletHoldResponse placeHold(WalletHoldRequest walletHoldRequest) {
		try {
			log.debug("placeHold:: walletHoldRequest: {}", walletHoldRequest);

			Optional<Wallet> walletOpt = walletRepo.findByUserIdAndCurrency(walletHoldRequest.getUserId(),
					walletHoldRequest.getCurrency());

			if (walletOpt.isEmpty()) {
				log.warn("placeHold:: No wallet found for the userId: {}", walletHoldRequest.getUserId());
				return WalletHoldResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.WALLET_NOT_FOUND))
						.build();
			}

			Wallet wallet = walletOpt.get();

			if (wallet.getAvailableBalance() < walletHoldRequest.getAmount()) {
				log.warn("placeHold: Insufficient funds");
				return WalletHoldResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INSUFFICIENT_FUND))
						.build();
			}

			wallet.setAvailableBalance(wallet.getAvailableBalance() - walletHoldRequest.getAmount());

			WalletHold walletHold = WalletHold.builder().wallet(wallet).amount(walletHoldRequest.getAmount())
					.holdReference("HOLD-" + System.currentTimeMillis()).status(STATUS_ACTIVE).build();

			walletRepo.save(wallet);
			walletHoldRepo.save(walletHold);

			return WalletHoldResponse.builder().holdReference(walletHold.getHoldReference())
					.amount(walletHold.getAmount()).status(walletHold.getStatus()).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return WalletHoldResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR))
					.build();
		}
	}

	@Transactional
	public WalletResponse captureHold(CaptureHoldRequest captureHoldRequest) {
		try {
			log.debug("captureHold:: captureHoldRequest: {}", captureHoldRequest);

			Optional<WalletHold> walletHoldOpt = walletHoldRepo
					.findByHoldReference(captureHoldRequest.getHoldReference());
			if (walletHoldOpt.isEmpty()) {
				log.warn("captureHold:: Hold not found");
				return WalletResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.HOLD_NOT_FOUND)).build();
			}

			WalletHold walletHold = walletHoldOpt.get();

			if (!STATUS_ACTIVE.equalsIgnoreCase(walletHold.getStatus())) {
				log.warn("captureHold:: Hold not active");
				return WalletResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.HOLD_NOT_ACTIVE)).build();
			}

			Wallet wallet = walletHold.getWallet();
			wallet.setBalance(wallet.getBalance() - walletHold.getAmount());

			walletHold.setStatus(STATUS_CAPTURED);
			walletRepo.save(wallet);
			walletHoldRepo.save(walletHold);

			WalletAPI walletAPI = WalletAPI.builder().id(wallet.getId()).userId(wallet.getUserId())
					.currency(wallet.getCurrency()).balance(wallet.getBalance())
					.availableBalance(wallet.getAvailableBalance()).build();

			return WalletResponse.builder().walletAPI(walletAPI).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return WalletResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR)).build();
		}
	}

	@Transactional
	public WalletHoldResponse releaseHold(String holdReference) {
		try {
			log.debug("releaseHold:: holdReference: {}", holdReference);

			Optional<WalletHold> walletHoldOpt = walletHoldRepo.findByHoldReference(holdReference);

			if (walletHoldOpt.isEmpty()) {
				log.warn("releaseHold:: Hold not found");
				return WalletHoldResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.HOLD_NOT_FOUND)).build();
			}

			WalletHold walletHold = walletHoldOpt.get();

			if (!STATUS_ACTIVE.equalsIgnoreCase(walletHold.getStatus())) {
				log.warn("captureHold:: Hold not active");
				return WalletHoldResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.HOLD_NOT_ACTIVE)).build();
			}

			Wallet wallet = walletHold.getWallet();

			wallet.setAvailableBalance(wallet.getAvailableBalance() + walletHold.getAmount());

			walletHold.setStatus(STATUS_RELEASED);
			walletRepo.save(wallet);
			walletHoldRepo.save(walletHold);

			return WalletHoldResponse.builder().holdReference(walletHold.getHoldReference())
					.amount(walletHold.getAmount()).status(walletHold.getStatus()).build();

		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
			return WalletHoldResponse.builder().errorDetailList(Arrays.asList(ErrorDetail.INTERNAL_SERVER_ERROR))
					.build();
		}
	}
}
