@condition
Feature: Condition Tests (version 0.11) .N-4-8


    Scenario Outline: <reference> Condition Test
        Given FHIR STU3 Server
        When I Condition Get <query>
        Then the method response code should be <response>
        And have <resultCount> Condition's returned
        And Condition Identifiers = <conditionIds>
        And resource is valid

        Examples:
            | reference | query                                                         | response | resultCount | conditionIds |
            | 4.8.dev1  | patient=1                                                     | 200      | >=1         |              |
            | 4.8.dev2  | clinical-status=active&patient=#1010#                         | 200      | 2           | 10006        |
            | 4.8.dev3  | category=symptom                                              | 200      | 0           |              |
            | 4.8.dev4  | category=encounter-diagnosis&patient=#1047#                   | 200      | 1           | 10024        |
            | 4.8.dev5  | asserted-date=ge2010-01-01                                    | 200      | >=10        |              |
            | 4.8.dev6  | asserted-date=lt2010-01-01                                    | 200      | >=0         |              |

