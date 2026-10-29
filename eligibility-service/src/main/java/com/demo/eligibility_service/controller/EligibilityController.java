package com.demo.eligibility_service.controller;

import com.demo.eligibility_service.dto.EligibilityRequest;
import com.demo.eligibility_service.dto.EligibilityResponse;
import com.demo.eligibility_service.service.EligibilityEvaluator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/eligibility")
public class EligibilityController {

	private final EligibilityEvaluator eligibilityEvaluator;

	public EligibilityController(EligibilityEvaluator eligibilityEvaluator) {
		this.eligibilityEvaluator = eligibilityEvaluator;
	}

	@PostMapping("/check")
	public EligibilityResponse checkEligibility(@RequestBody EligibilityRequest request) {
		return eligibilityEvaluator.evaluate(request);
	}
}