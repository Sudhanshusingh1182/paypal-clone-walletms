package com.paypal.walletms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "walletholds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletHold {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name= "walletId")
	private Wallet wallet;
	
	@Column(nullable = false)
    private String holdReference;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String status; // ACTIVE, CAPTURED, RELEASED

    private LocalDateTime createdDate;

    private LocalDateTime expiryDate;
}
