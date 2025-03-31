Feature: User Portfolio
  description: This feature will test the Portfolio functionality of the application.

  @login
  Scenario: Portfolio Creation with valid credentials

  @Portfolio
  Scenario Outline:Portfolio Creation
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User is redirected to the Dashboard And User clicks on the Portfolio button
    Then User is redirected to the Portfolio page
    And User Fills the Portfolio Details form with "<portfolioName>", "<portfolioType>", "<EIN No>", "<Portfolio Owner Email ID>", "<Portfoilo_Owner_Contact_Number>"
    And User Click on the Next button
    Then User Fills the Business Address details form with "<Address 1>", "<Address 2>", "<City>", "<State>", "<Zip Code>", "<Country>"
    And User Click on the Next button
    Then user Click on the Confirm button
    And user Display the New Portfolio Created and Click on the OK button
    And User closes the browser
    Examples:
      | username             | password  | portfolioName | portfolioType | EIN No     | Portfolio Owner Email ID | Portfoilo_Owner_Contact_Number | Address 1 | Address 2 | City     | State    | Zip Code | Country       |
      | gogowad347@erapk.com | Sreenu80@ | {random}      | Single Family | 44-4444444 | {random}                 | {random}                       | 12        | Suite 500 | New York | New York | 22222    | United States |
      | gogowad347@erapk.com | Sreenu80@ | {random}      | Multi-Family  | 44-4444444 | {random}                 | {random}                       | 12        | Suite 500 | New York | New York | 22222    | United States |

  @login
  Scenario: Portfolio Creation with valid credentials Help_OF_ExcelSheet

  @Portfolio
  Scenario Outline:Portfolio Creation
    Given open browser
    Given load the data from "<filePath>" and "<sheetName>" excel sheet
    When User launch Login page
    Then User enters valid credentials
    And User is redirected to the Dashboard And User clicks on the Portfolio button
    Then User is redirected to the Portfolio page
    And User Fills the Portfolio Details form
    And User Click on the Next button
    Then user Fills the Business Address details form
    And User Click on the Next button
    And user Click on the Confirm button
    And user Display the New Portfolio Created and Click on the OK button
    And User closes the browser
    Examples:
      | filePath                                           | sheetName          |
      | D:\batch264\Yvooa\src\test\resources\testdata.xlsx | Portfolio Creation |

