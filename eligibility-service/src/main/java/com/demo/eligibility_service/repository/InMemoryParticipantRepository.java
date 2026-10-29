package com.demo.eligibility_service.repository;

import com.demo.eligibility_service.model.Participant;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryParticipantRepository {

	private final List<Participant> participants = List.of(
			new Participant("P1001", "Avery Morgan", 34, "ACTIVE"),
			new Participant("P1002", "Jordan Lee", 42, "ACTIVE"),
			new Participant("P1003", "Casey Patel", 17, "ACTIVE"),
			new Participant("P1004", "Riley Chen", 51, "SUSPENDED"),
			new Participant("P1005", "Taylor Brooks", 38, "CLOSED"),
			new Participant("P1006", "Morgan Rivera", 46, "UNDER_REVIEW")
	);

	public Optional<Participant> findById(String id) {
		return participants.stream()
				.filter(participant -> participant.id().equals(id))
				.findFirst();
	}
}