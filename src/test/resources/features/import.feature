Feature: import module
  description: This feature will test the import functionality of the application.

  @login
  Scenario: import module with valid credentials

  Scenario Outline: import module with valid credentials
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User is redirected to the Dashboard And User clicks on the import button
    And User select "<Select Category>" And "<Portfolio Name>" And "<Property Name>" And "<Select File>" Fields
    And user click on the uploadFinalFile button
    #And User closes the browser
    Examples:
      | username             | password  | Select Category     | Select File                                    | Portfolio Name   | Property Name |
#      | gogowad347@erapk.com | Sreenu80@ | Portfolio             | C:\Users\sreenu\Downloads\PortfolioCSV.csv             | NA               |               |
#      | gogowad347@erapk.com | Sreenu80@ | Property Single Family | C:\Users\sreenu\Downloads\PropertySingleFamilyCSV.csv  | Test Portfolio_3 |               |
#     | gogowad347@erapk.com | Sreenu80@ | Property Multi Family  | C:\Users\sreenu\Downloads\PropertyMultiFamilyCSV.csv   | Test Portfolio_7 |               |
#      | gogowad347@erapk.com | Sreenu80@ | Unit                   | C:\Users\sreenu\Downloads\UnitCSV.csv                  | Test Portfolio_7 | Cat           |
#      | gogowad347@erapk.com | Sreenu80@ | Single Family Tenant | C:\Users\sreenu\Downloads\ImportTenantTemplate (2).csv | Test Portfolio_3 |               |
#      | gogowad347@erapk.com | Sreenu80@ | Multi-Family Tenant | C:\Users\sreenu\Downloads\ImportTenantTemplate (3).csv | Test Portfolio_7 | apple5        |
      | gogowad347@erapk.com | Sreenu80@ | Utility Charges     | C:\Users\sreenu\Downloads\UtilityBills (1).csv | Test Portfolio_7 | apple5        |
#      | gogowad347@erapk.com | Sreenu80@ | Rental Transactions |                                                |                  |               |