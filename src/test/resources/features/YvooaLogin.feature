Feature: User Login
  description: This feature will test the login functionality of the application.
  @login @visualAI
  Scenario: Successful Login with valid credentials
    Scenario Outline: Outline: Successful Login with valid credentials
      Given open browser
      When User launch Login page
      Then User enters "<username>" and "<password>" valid credentials
      And User is redirected to the Dashboard
      And User closes the browser

      Examples:
        | username | password|
        | gogowad347@erapk.com | Sreenu80@ |
        | jereji7472@doishy.com | Sreenu80@ |
        | mimoyac655@doishy.com | Sreenu80@ |
        | gogowad347@erapk.com | Sreenu80@ |

    @login @visualAI
      Scenario: invalid login
        Given open browser
        When User launch Login page
        Then User enters invalid credentials
        And User closes the browser

        @login @visualAI
        Scenario: empty login
          Given open browser
          When User launch Login page
          Then User enters empty credentials
          And User closes the browser


