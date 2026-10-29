package com.demo.eligibility_service.dto;

import java.math.BigDecimal;

public record EligibilityRequest(String participantId, String withdrawalType, BigDecimal amount) {
}