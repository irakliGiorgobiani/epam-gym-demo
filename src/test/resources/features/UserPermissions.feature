@Permissions
Feature: User Permission

  Scenario: Request with valid JWT token should be successful
    Given a request with a valid JWT token
    When making a request to the secured endpoint
    Then the request should be successful

  Scenario: Request with invalid JWT token should be unauthorized
    Given a request with an invalid JWT token
    When making a request to the secured endpoint
    Then the request should be unauthorized

  Scenario: Request with invalid JWT token should throw an unauthorized exception
    Given a request with an invalid JWT token
    Then an unauthorized exception should be thrown