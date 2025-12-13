package com.paypal.walletms.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletAPI {
	private Long id;
	private Long userId;
	private String currency;
	private Double balance;
	private Double availableBalance;
}
