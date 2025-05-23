Feature: Sign up on Yuvoo website

Scenario: User signs up with valid information
    Given User is on the Sign Up page of Yuvoo website
    When User enters valid first name, last name, email, and password
    And User clicks on the Sign Up button
    Then User should be successfully registered on Yuvoo website

Scenario: User signs up with existing email
    Given User is on the Sign Up page of Yuvoo website
    When User enters an email address that is already registered
    And User clicks on the Sign Up button
    Then User should see an error message indicating that the email is already in use

Scenario: User signs up with incomplete information
    Given User is on the Sign Up page of Yuvoo website
    When User enters only first name and email
    And User clicks on the Sign Up button
    Then User should see an error message indicating that all required fields must be filled out