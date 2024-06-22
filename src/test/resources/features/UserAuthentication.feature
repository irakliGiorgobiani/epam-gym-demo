@Authentication
Feature: User Authentication

  Scenario: Correct Username And Password
    Given username "username" and password "password"
    When checking credentials
    Then authentication is successful

  Scenario: Incorrect Username And Correct Password
    Given username "incorrectUsername" and password "password"
    When checking credentials
    Then authentication is unsuccessful, because of the incorrect username

  Scenario: Correct Username And Incorrect Password
    Given username "username" and password "incorrectPassword"
    When checking credentials
    Then authentication is unsuccessful, because of the incorrect password