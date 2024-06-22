Feature: Integration between Gym and Reporter microservices
  Scenario: Send training data from Gym to Reporter and verify it's processed
    Given a training data with the username "john_doe"
    When the Gym microservice sends the data to the Reporter microservice
    Then the Reporter microservice should save the training summary with the username "john_doe"

  Scenario: Request training summary from Gym and verify Reporter response
    Given a training summary exists in the Reporter microservice for username "john_doe"
    When the Gym microservice requests the training summary for username "john_doe"
    Then the Gym microservice should receive the training summary with the username "john_doe"

  Scenario: Send malformed training data from Gym to Reporter
    Given a malformed training data with the username "john_doe"
    When the Gym microservice sends the data to the Reporter microservice
    Then an error should be returned indicating the data is invalid
