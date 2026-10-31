# eligibility-service

Java + Spring Boot microservice that evaluates retirement withdrawal requests against participant and plan rules. Participant data is held in an in-memory repository for this demo.

## API

`POST /api/v1/eligibility/check`

Request fields:

```json
{
	"participantId": "P1001",
	"withdrawalType": "PARTIAL",
	"amount": 500.00
}
```

Response fields:

```json
{
	"eligible": true,
	"reason": "Eligible for withdrawal."
}
```

The service returns `eligible: false` and a rejection reason when a request fails a rule. Checks include participant existence, minimum age, active account status, supported plan and withdrawal type, employment and loan restrictions for full withdrawals, vesting, withdrawal timing, balance and amount limits, and annual withdrawal limits.

Account statuses other than `ACTIVE`, including `UNDER_REVIEW`, currently receive the same rejection reason: `Participant account is not active.`

## Run and test

From this repository's `eligibility-service` directory:

```sh
./mvnw spring-boot:run
./mvnw test
```
