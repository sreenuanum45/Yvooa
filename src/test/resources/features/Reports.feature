Feature: User Reports
  description: This feature will test the Reports functionality of the application.

  Scenario: Successful Login with valid credentials and generate reports
    Scenario Outline: Successful Login with valid credentials and generate reports
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User is redirected to the Dashboard
    When User clicks on the Reports button
    And User Select ReportCategory "<propertyType>"
    And User Select Report "<Select Report>"
    And Select the Report Builder dropdowns
    And User Click on the Apply button
    And User closes the browser

    Examples:
      | username             | password  | propertyType | Select Report        |
      |gogowad347@erapk.com  |Sreenu80@  |      Property|  Rental Ledger Report|