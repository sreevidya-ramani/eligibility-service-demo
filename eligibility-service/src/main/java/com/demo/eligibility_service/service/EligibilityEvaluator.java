package com.demo.eligibility_service.service;

import com.demo.eligibility_service.dto.EligibilityRequest;
import com.demo.eligibility_service.dto.EligibilityResponse;
import org.springframework.stereotype.Service;

@Service
public class EligibilityEvaluator {

	public EligibilityResponse evaluate(EligibilityRequest request) {
		return new EligibilityResponse(true, "Eligible");
	}
}