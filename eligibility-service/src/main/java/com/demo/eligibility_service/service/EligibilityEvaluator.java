package com.demo.eligibility_service.service;

import com.demo.eligibility_service.dto.EligibilityRequest;
import com.demo.eligibility_service.dto.EligibilityResponse;
import com.demo.eligibility_service.model.Participant;
import com.demo.eligibility_service.repository.InMemoryParticipantRepository;
import org.springframework.stereotype.Service;

@Service
public class EligibilityEvaluator {

	private final InMemoryParticipantRepository participantRepository;

	public EligibilityEvaluator(InMemoryParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

	public EligibilityResponse evaluate(EligibilityRequest request) {
		if (request == null || request.participantId() == null || request.participantId().isBlank()) {
			return rejected("Participant ID is required.");
		}

		Participant participant = participantRepository.findById(request.participantId()).orElse(null);
		if (participant == null) {
			return rejected("Participant was not found.");
		}

		if (participant.age() < 18) {
			return rejected("Participant must be at least 18 years old.");
		}

		if (!"ACTIVE".equalsIgnoreCase(participant.accountStatus())) {
			return rejected("Participant account is not active.");
		}

		String withdrawalType = request.withdrawalType();
		if (withdrawalType == null
				|| !(withdrawalType.trim().equalsIgnoreCase("PARTIAL")
				|| withdrawalType.trim().equalsIgnoreCase("FULL"))) {
			return rejected("Withdrawal type must be PARTIAL or FULL.");
		}

		if (request.amount() == null || request.amount().signum() <= 0) {
			return rejected("Withdrawal amount must be greater than zero.");
		}

		return new EligibilityResponse(true, "Eligible for withdrawal.");
	}

	private EligibilityResponse rejected(String reason) {
		return new EligibilityResponse(false, reason);
	}
}