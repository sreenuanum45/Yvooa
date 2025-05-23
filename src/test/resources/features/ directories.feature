Feature: Verify the directories feature
  description: This feature will test the directories functionality of the application.

  Scenario: Successful Login with valid credentials and Users Directory

  Scenario Outline: Successful Login with valid credentials and Users Directory
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User is redirected to the Dashboard
    When User clicks on the Directories button
    And User Select Directory "<Select Directory>"
    And User navigates to the selected directory "<Select Directory>"
    And Click on the AddNew button
    And Fill the Personal Information form with the details "<First Name>" "<Last Name>" "<Contact Number>" "<Email ID>"
    And Click on the Confirm button
    And Verify the created successfully message And Click on the OK button
    And User closes the browser
    Examples:
      | username                | password  | Select Directory | First Name | Last Name | Contact Number | Email ID | Choose Profile Type |
      | kaliho8670@evluence.com | Sreenu80@ | Users Directory  | {random}   | doe       | {random}       | {random} | Property Manager    |
      | kaliho8670@evluence.com | Sreenu80@ | Users Directory  | {random}   | doe       | {random}       | {random} | Client Admin        |

  Scenario: Successful Login with valid credentials and Tenant Directorya

  Scenario Outline: Successful Login with valid credentials and Tenant Directory
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User is redirected to the Dashboard
    When User clicks on the Directories button
    And User Select Directory "<Select Directory>"
    And User navigates to the selected directory "<Select Directory>"
    And User closes the browser
    Examples:
      | username                | password  | Select Directory |
#      | gogowad347@erapk.com | Sreenu80@ | Tenant Directory |
#      | deyes34838@eligou.com | Sreenu80@ | Tenant Directory |
      | kaliho8670@evluence.com | Sreenu80@ | Tenant Directory |

  Scenario: Successful Login with valid credentials and Vendor Directory

  Scenario Outline: Successful Login with valid credentials and Vendor Directory
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User is redirected to the Dashboard
    When User clicks on the Directories button
    And User Select Directory "<Select Directory>"
    And User navigates to the selected directory "<Select Directory>"
    And Click on the AddNew button
    And Fill Vendor Details form  And "<Vendor Category>" Click on the Next button
    And Click on the Confirm button
    And Verify the created successfully message And Click on the OK button
    And User closes the browser
    Examples:
      | username                | password  | Select Directory  | Vendor Category |
      | kaliho8670@evluence.com | Sreenu80@ | Vendors Directory | PLUMBING        |

