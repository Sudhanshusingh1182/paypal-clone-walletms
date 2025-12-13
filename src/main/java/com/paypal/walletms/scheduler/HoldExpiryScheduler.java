package com.paypal.walletms.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.paypal.walletms.dao.WalletHoldRepo;
import com.paypal.walletms.entity.WalletHold;
import com.paypal.walletms.service.WalletServiceImpl;
import com.paypal.walletms.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HoldExpiryScheduler {
	
	@Autowired
	private WalletHoldRepo walletHoldRepo;
	
	@Autowired
	private WalletServiceImpl walletServiceImpl;
	
	private static final String ACTIVE = "ACTIVE";
	
	@Scheduled(fixedRate = 60000)
	public void expireOldHolds() {
		try {
			log.debug("expireOldHolds:: Inside the expireOldHolds method");
			
			LocalDateTime now = LocalDateTime.now();
			List<WalletHold> walletHoldList = walletHoldRepo.findByStatusAndExpiryDateBefore(ACTIVE, now);
			
			for(WalletHold walletHold: walletHoldList) {
				String holdRef = walletHold.getHoldReference();
				walletServiceImpl.releaseHold(holdRef);
				log.debug("expireOldHolds:: Expired hold released: {}", holdRef);
			} 
		} catch (Exception e) {
			log.error(StringUtils.ERROR_STR, e.getClass(), e.getLocalizedMessage(), e);
		}
	}
}
