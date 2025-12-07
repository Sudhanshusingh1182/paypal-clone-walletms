package com.paypal.walletms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique= true)
	private Long userId;
	
	@Column(nullable = false, length = 3)
	private String currency;
	
	@Column(nullable = false)
	private Long balance;
	
	@Column(nullable = false)
	private Long availableBalance;
	
	@Column(nullable = false)
	private LocalDateTime createdDate;
	
	@Column(nullable = false)
	private LocalDateTime modifiedDate;
	
}
