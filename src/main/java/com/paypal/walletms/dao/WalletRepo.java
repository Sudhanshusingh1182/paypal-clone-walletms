package com.paypal.walletms.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.paypal.walletms.entity.Wallet;

import jakarta.persistence.LockModeType;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, Long> {
	
	Optional<Wallet> findByUserId(Long userId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Wallet> findByUserIdAndCurrency(Long userId, String currency);
}
