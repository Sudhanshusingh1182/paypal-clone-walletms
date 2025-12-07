package com.paypal.walletms.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	private Long id;
	private Long walletId;
	private String type; //CREDIT, DEBIT, HOLD, RELEASE, CAPTURE
	private Double amount;
	private LocalDateTime createdDate;
	private String status; //SUCCESS, FAILED, EXPIRED
}
