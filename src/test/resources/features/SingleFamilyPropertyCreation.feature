Feature: Single Family Property Creation

  Scenario: Single Family Property Creation

  Scenario Outline:Portfolio Creation
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User is redirected to the Dashboard And User clicks on the Portfolio button
    Then User is redirected to the Portfolio page
    And Click on the "<Portfolio Name>" button
    Examples:
      | username             | password  | Portfolio Name   |
      | gogowad347@erapk.com | Sreenu80@ | Test Portfolio_3 |
