Feature: Sim card activation

  Scenario: Successful SIM card activation
    Given a sim card with iccid "1255789453849037777" and customer email "success@example.com"
    When I submit an activation request
    And I query activation with id 1
    Then the activation should be successful

  Scenario: Failed SIM card activation
    Given a sim card with iccid "8944500102198304826" and customer email "failure@example.com"
    When I submit an activation request
    And I query activation with id 2
    Then the activation should be unsuccessful
