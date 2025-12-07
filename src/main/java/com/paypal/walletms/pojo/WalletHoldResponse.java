package com.paypal.walletms.pojo;

import java.util.List;

import com.paypal.walletms.error.ErrorDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletHoldResponse {
	private String holdReference;
	private Long amount;
	private String status;
	private List<ErrorDetail> errorDetailList;
}
