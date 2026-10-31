package com.demo.eligibility_service.service;

import com.demo.eligibility_service.dto.EligibilityRequest;
import com.demo.eligibility_service.dto.EligibilityResponse;
import com.demo.eligibility_service.model.Participant;
import com.demo.eligibility_service.repository.InMemoryParticipantRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class EligibilityEvaluator {

	private final InMemoryParticipantRepository participantRepository;

	public EligibilityEvaluator(InMemoryParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

	public EligibilityResponse evaluate(EligibilityRequest request) {
		EligibilityResponse evaluationResult = null;
		String participantIdentifier = null;
		Participant participant = null;
		String plan = null;
		String employment = null;
		String requestedType = null;
		BigDecimal requestedAmount = null;
		BigDecimal roundedAmount = null;
		BigDecimal vestedBalance = null;
		BigDecimal partialMaximum = null;
		BigDecimal remainingBalance = null;
		LocalDate lastWithdrawal = null;
		boolean validPartialType = false;
		boolean validFullType = false;
		boolean fullWithdrawal = false;
		int annualLimit = 0;

		if (request == null) {
			evaluationResult = rejected("Participant ID is required.");
		} else {
			participantIdentifier = request.participantId();
			if (participantIdentifier == null || participantIdentifier.isBlank()) {
				evaluationResult = rejected("Participant ID is required.");
			} else {
				participant = participantRepository.findById(participantIdentifier).orElse(null);
				if (participant == null) {
					evaluationResult = rejected("Participant was not found.");
				} else {
					if (participant.age() < 18) {
						evaluationResult = rejected("Participant must be at least 18 years old.");
					} else {
						if (!"ACTIVE".equalsIgnoreCase(participant.accountStatus())) {
							evaluationResult = rejected("Participant account is not active.");
						} else {
							plan = participant.planType();
							if (plan == null
									|| !(plan.equalsIgnoreCase("401K")
									|| plan.equalsIgnoreCase("403B")
									|| plan.equalsIgnoreCase("IRA"))) {
								evaluationResult = rejected("Plan type must be 401K, 403B, or IRA.");
							} else {
								employment = participant.employmentStatus();
								requestedType = request.withdrawalType();
								fullWithdrawal = requestedType != null
										&& requestedType.trim().equalsIgnoreCase("FULL");
								if ("ACTIVE".equalsIgnoreCase(employment)) {
									if (fullWithdrawal) {
										evaluationResult = rejected("Active employees cannot take a FULL withdrawal.");
									}
								} else {
									if (fullWithdrawal) {
										if ("TERMINATED".equalsIgnoreCase(employment)
												|| "RETIRED".equalsIgnoreCase(employment)) {
											if (participant.outstandingLoanBalance() != null
													&& participant.outstandingLoanBalance().compareTo(BigDecimal.ZERO) > 0) {
												evaluationResult = rejected(
														"A FULL withdrawal is not allowed with an outstanding loan balance.");
											}
										}
									}
								}

								if (evaluationResult == null) {
									if (participant.vestingPercent() < 50) {
										evaluationResult = rejected("Participant must be at least 50 percent vested.");
									} else {
										validPartialType = requestedType != null
												&& requestedType.trim().equalsIgnoreCase("PARTIAL");
										validFullType = fullWithdrawal;
										if (!validPartialType && !validFullType) {
											evaluationResult = rejected("Withdrawal type must be PARTIAL or FULL.");
										} else {
											requestedAmount = request.amount();
											if (requestedAmount == null
													|| requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
												evaluationResult = rejected("Withdrawal amount must be greater than zero.");
											} else {
												lastWithdrawal = participant.lastWithdrawalDate();
												if (lastWithdrawal != null
														&& !lastWithdrawal.isBefore(LocalDate.now().minusDays(30))) {
													evaluationResult = rejected("A withdrawal was made within the last 30 days.");
												} else {
													roundedAmount = requestedAmount.setScale(0, RoundingMode.DOWN);
													vestedBalance = participant.balance()
															.multiply(BigDecimal.valueOf(participant.vestingPercent()))
															.divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
													if (validPartialType) {
														if (roundedAmount.compareTo(BigDecimal.valueOf(500)) < 0) {
															evaluationResult = rejected("PARTIAL withdrawals must be at least 500.");
														} else {
															partialMaximum = vestedBalance.divide(BigDecimal.valueOf(2), 2, RoundingMode.DOWN);
															if (roundedAmount.compareTo(partialMaximum) > 0) {
																evaluationResult = rejected(
																		"PARTIAL withdrawal cannot exceed 50 percent of the vested balance.");
															}
														}
													}

													if (evaluationResult == null) {
														if (roundedAmount.compareTo(vestedBalance) > 0) {
															evaluationResult = rejected("Withdrawal amount cannot exceed the vested balance.");
														} else {
															if (validPartialType) {
																remainingBalance = participant.balance().subtract(roundedAmount);
																if (remainingBalance.compareTo(BigDecimal.valueOf(1000)) < 0) {
																	evaluationResult = rejected(
																			"At least 1000 must remain after a PARTIAL withdrawal.");
																}
															}

															if (evaluationResult == null) {
																if (plan.equalsIgnoreCase("IRA")) {
																	annualLimit = 4;
																} else {
																	annualLimit = 2;
																}
																if (participant.withdrawalsThisYear() >= annualLimit) {
																	evaluationResult = rejected("Annual withdrawal limit has been reached.");
																} else {
																	evaluationResult = new EligibilityResponse(true, "Eligible for withdrawal.");
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return evaluationResult;
	}

	private EligibilityResponse rejected(String reason) {
		return new EligibilityResponse(false, reason);
	}
}