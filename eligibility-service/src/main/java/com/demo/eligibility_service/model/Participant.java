package com.demo.eligibility_service.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Participant(
		String id,
		String name,
		int age,
		String accountStatus,
		String planType,
		String employmentStatus,
		int vestingPercent,
		BigDecimal balance,
		BigDecimal outstandingLoanBalance,
		LocalDate lastWithdrawalDate,
		int withdrawalsThisYear
) {
}