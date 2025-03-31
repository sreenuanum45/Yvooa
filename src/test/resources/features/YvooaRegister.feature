Feature: Yvooa Register
  description: This feature will test the registration functionality of the application.

  @register

  Scenario: User signs up with valid information
    Given User is on the Sign Up page of Yuvoo website
    When User enters valid registration details
      | First Name | Last Name | Email    | Contact Number | Company Name | Address 1   | Address 2 | City    | Zip Code | Password  | Confirm Password |
      | Sreenu     | Anumandla | [random] | 1234567890     | Yuvoo        | 123 Main St | 123       | newyork | 45878    | Sreenu80@ | Sreenu80@        |
    And select Dropdowns Product Type,state,country
    And User Click on the  Terms & Conditions
    And User clicks on the Sign Up button
    Then User should be successfully registered on Yuvoo website

