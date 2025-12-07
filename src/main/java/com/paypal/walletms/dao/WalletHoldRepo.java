package com.paypal.walletms.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypal.walletms.entity.WalletHold;

@Repository
public interface WalletHoldRepo extends JpaRepository<WalletHold, Long> {
	
	Optional<WalletHold> findByHoldReference(String holdReference);
	
	List<WalletHold> findByStatusAndExpiryDateBefore(String active, LocalDateTime now);
}
