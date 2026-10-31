package com.demo.eligibility_service.repository;

import com.demo.eligibility_service.model.Participant;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryParticipantRepository {

		private final List<Participant> participants = List.of(
			new Participant("P1001", "Avery Morgan", 34, "ACTIVE", "401K", "TERMINATED", 100,
					new BigDecimal("20000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(90), 0),
			new Participant("P1002", "Jordan Lee", 42, "ACTIVE", "403B", "ACTIVE", 100,
					new BigDecimal("35000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(120), 0),
			new Participant("P1003", "Casey Patel", 17, "ACTIVE", "IRA", "TERMINATED", 100,
					new BigDecimal("12000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(150), 0),
			new Participant("P1004", "Riley Chen", 51, "SUSPENDED", "401K", "RETIRED", 100,
					new BigDecimal("50000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(180), 0),
			new Participant("P1005", "Taylor Brooks", 38, "CLOSED", "IRA", "TERMINATED", 75,
					new BigDecimal("18000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(200), 1),
			new Participant("P1006", "Morgan Rivera", 46, "UNDER_REVIEW", "403B", "RETIRED", 80,
					new BigDecimal("42000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(220), 0),
			new Participant("P1007", "Quinn Parker", 39, "ACTIVE", "403B", "TERMINATED", 100,
					new BigDecimal("24000.00"), new BigDecimal("750.00"), LocalDate.now().minusDays(90), 0),
			new Participant("P1008", "Rowan Ellis", 58, "ACTIVE", "ROTH", "RETIRED", 100,
					new BigDecimal("20000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(90), 0),
			new Participant("P1009", "Harper Diaz", 45, "ACTIVE", "401K", "TERMINATED", 40,
					new BigDecimal("30000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(90), 0),
			new Participant("P1010", "Elliot Shaw", 52, "ACTIVE", "401K", "TERMINATED", 100,
					new BigDecimal("28000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(10), 0),
			new Participant("P1011", "Sasha Nair", 41, "ACTIVE", "401K", "TERMINATED", 100,
					new BigDecimal("1800.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(90), 0),
			new Participant("P1012", "Devon Kaur", 49, "ACTIVE", "401K", "TERMINATED", 100,
					new BigDecimal("26000.00"), new BigDecimal("0.00"), LocalDate.now().minusDays(90), 2)
	);

	public Optional<Participant> findById(String id) {
		return participants.stream()
				.filter(participant -> participant.id().equals(id))
				.findFirst();
	}
}