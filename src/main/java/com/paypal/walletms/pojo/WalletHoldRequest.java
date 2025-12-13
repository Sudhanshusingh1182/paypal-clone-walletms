package com.paypal.walletms.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletHoldRequest {
	private Long userId;
	private Double amount;
	private String currency;
}
