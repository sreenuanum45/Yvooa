Feature: User maintenance
  description: This feature will test maintenance functionality of the application.

  @maintenance @visualAI
  Scenario: Successful maintenance with valid credentials

  Scenario Outline:Successful maintenance with valid credentials With DataDriven
    Given open browser
    When User launch Login page
    Then User enters "<username>" and "<password>" valid credentials
    And User clicks on the maintenance button
    And User is redirected to the Maintenance page
    And Fill the Create Maintenance Ticket "<Select Portfolio>"
    And Fill the Add Maintenance Request "<Request Type>" And "<problemDescription>" And "<Location of Problem>" And "<Notes>" And "<MediaSelectFile>"
    And Verify the A Maintenance Work Order has been successfully Created! message
    And User closes the browser
    Examples:
      | username              | password  | Select Portfolio | Request Type                 | problemDescription | Location of Problem | Notes | MediaSelectFile                                         |
      | gogowad347@erapk.com  | Sreenu80@ | Test Portfolio_3 | Maintenance Request-Property | NA                 | NA                  | NA    | C:\Users\sreenu\OneDrive\Pictures\Screenshots\block.png |
      | deyes34838@eligou.com | Sreenu80@ | Test Portfolio_3 | Maintenance Request-Property | NA                 | NA                  | NA    | C:\Users\sreenu\OneDrive\Pictures\Screenshots\block.png |