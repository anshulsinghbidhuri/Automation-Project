@chatNamsateTalk
Feature: This feature is for the chat msa of NamsateTalk application.
# purpose for this feature file is to test the chat msa of NamsateTalk application.

  Scenario: User sings in to NamsateTalk application and proceeds for onboarding registration
    Given User is able to signin in NamsateTalk page
    When User enters Valid Email and Password for Login
    And User Proceeds for Onboarding resgetartion
    When User checks the status for Onboarding
    Then User logs out from the application

    Scenario: User sings in to NamsateTalk application and Delete the User account
     Given User is able to signin in NamsateTalk page
     When User enters Valid Email and Password for Login
     And User Proceeds for Onboarding resgetartion
     When User checks the status for Onboarding
     Then User Deletes the account the application