package com.demo.eligibility_service.service;

import com.demo.eligibility_service.dto.EligibilityRequest;
import com.demo.eligibility_service.dto.EligibilityResponse;
import com.demo.eligibility_service.repository.InMemoryParticipantRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Characterization tests: they record what the evaluator does today, not what it should do.
class EligibilityEvaluatorTests {

	private final EligibilityEvaluator evaluator = new EligibilityEvaluator(new InMemoryParticipantRepository());

	
	@Test
    void underReviewAccountGetsTheGenericInactiveReason() {
            assertRejected("P1006", "PARTIAL", "500", "Participant account is not active.");
    }

    @Test
    void closedAccountGetsTheGenericInactiveReason() {
        assertRejected("P1005", "PARTIAL", "500", "Participant account is not active.");
    }

	@Test
	void existingEligibleParticipantCanStillWithdraw() {
		EligibilityResponse response = evaluator.evaluate(
				new EligibilityRequest("P1001", "PARTIAL", new BigDecimal("500.00")));

		assertTrue(response.eligible());
		assertEquals("Eligible for withdrawal.", response.reason());
	}

	@Test
	void nullRequestRequiresParticipantId() {
		assertRejected(evaluator.evaluate(null), "Participant ID is required.");
	}

	@Test
	void blankParticipantIdIsRejected() {
		assertRejected("  ", "PARTIAL", "500", "Participant ID is required.");
	}

	@Test
	void unknownParticipantIsRejected() {
		assertRejected("P9999", "PARTIAL", "500", "Participant was not found.");
	}

	@Test
	void underageParticipantIsRejected() {
		assertRejected("P1003", "PARTIAL", "500", "Participant must be at least 18 years old.");
	}

	@Test
	void inactiveAccountIsRejected() {
		assertRejected("P1004", "PARTIAL", "500", "Participant account is not active.");
	}

	@Test
	void unsupportedPlanIsRejected() {
		assertRejected("P1008", "PARTIAL", "500", "Plan type must be 401K, 403B, or IRA.");
	}

	@Test
	void activeEmployeeCannotMakeFullWithdrawal() {
		assertRejected("P1002", "FULL", "500", "Active employees cannot take a FULL withdrawal.");
	}

	@Test
	void fullWithdrawalWithOutstandingLoanIsRejected() {
		assertRejected("P1007", "FULL", "500",
				"A FULL withdrawal is not allowed with an outstanding loan balance.");
	}

	@Test
	void participantBelowMinimumVestingIsRejected() {
		assertRejected("P1009", "PARTIAL", "500", "Participant must be at least 50 percent vested.");
	}

	@Test
	void unsupportedWithdrawalTypeIsRejected() {
		assertRejected("P1001", "OTHER", "500", "Withdrawal type must be PARTIAL or FULL.");
	}

	@Test
	void missingAmountIsRejected() {
		assertRejected("P1001", "PARTIAL", null, "Withdrawal amount must be greater than zero.");
	}

	@Test
	void nonPositiveAmountIsRejected() {
		assertRejected("P1001", "PARTIAL", "0", "Withdrawal amount must be greater than zero.");
	}

	@Test
	void recentWithdrawalIsRejected() {
		assertRejected("P1010", "PARTIAL", "500", "A withdrawal was made within the last 30 days.");
	}

	@Test
	void partialAmountBelowMinimumAfterRoundingIsRejected() {
		assertRejected("P1001", "PARTIAL", "499.99", "PARTIAL withdrawals must be at least 500.");
	}

	@Test
	void partialAmountAboveHalfOfVestedBalanceIsRejected() {
		assertRejected("P1001", "PARTIAL", "10001",
				"PARTIAL withdrawal cannot exceed 50 percent of the vested balance.");
	}

	@Test
	void partialAmountAtHalfOfVestedBalanceIsAllowed() {
		assertEligible("P1001", "PARTIAL", "10000");
	}

	@Test
	void fullAmountAboveVestedBalanceIsRejected() {
		assertRejected("P1001", "FULL", "20001", "Withdrawal amount cannot exceed the vested balance.");
	}

	@Test
	void fullAmountEqualToVestedBalanceIsAllowed() {
		assertEligible("P1001", "FULL", "20000");
	}

	@Test
	void partialWithdrawalMustLeaveAtLeastOneThousand() {
		assertRejected("P1011", "PARTIAL", "850",
				"At least 1000 must remain after a PARTIAL withdrawal.");
	}

	@Test
	void partialWithdrawalLeavingExactlyOneThousandIsAllowed() {
		assertEligible("P1011", "PARTIAL", "800");
	}

	@Test
	void annualWithdrawalLimitIsEnforced() {
		assertRejected("P1012", "PARTIAL", "500", "Annual withdrawal limit has been reached.");
	}

	@Test
	void eligibleFullWithdrawalRemainsAllowed() {
		EligibilityResponse response = evaluator.evaluate(
				new EligibilityRequest("P1001", "FULL", new BigDecimal("500.00")));

		assertTrue(response.eligible());
		assertEquals("Eligible for withdrawal.", response.reason());
	}

	@Test
	void activeEmployeeCanMakePartialWithdrawal() {
		EligibilityResponse response = evaluator.evaluate(
				new EligibilityRequest("P1002", "PARTIAL", new BigDecimal("500.00")));

		assertTrue(response.eligible());
		assertEquals("Eligible for withdrawal.", response.reason());
	}

	@Test
	void withdrawalTypeIsTrimmedAndCaseInsensitive() {
		EligibilityResponse response = evaluator.evaluate(
				new EligibilityRequest("P1001", " partial ", new BigDecimal("500.00")));

		assertTrue(response.eligible());
		assertEquals("Eligible for withdrawal.", response.reason());
	}

	private void assertRejected(String participantId, String withdrawalType, String amount, String reason) {
		EligibilityResponse response = evaluator.evaluate(new EligibilityRequest(
				participantId,
				withdrawalType,
				amount == null ? null : new BigDecimal(amount)));
		assertRejected(response, reason);
	}

	private void assertEligible(String participantId, String withdrawalType, String amount) {
		EligibilityResponse response = evaluator.evaluate(new EligibilityRequest(
				participantId, withdrawalType, new BigDecimal(amount)));
		assertTrue(response.eligible());
		assertEquals("Eligible for withdrawal.", response.reason());
	}

	private void assertRejected(EligibilityResponse response, String reason) {
		assertFalse(response.eligible());
		assertEquals(reason, response.reason());
	}
}