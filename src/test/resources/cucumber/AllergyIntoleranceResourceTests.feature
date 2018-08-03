@allergyintolerance
Feature: AllergyIntolerance Tests (version 0.11) .N-4-7


    Scenario Outline: <reference> AllergyIntolerance Test
        Given FHIR STU3 Server
        When I AllergyIntolerance Get <query>
        Then the method response code should be <response>
        And have <resultCount> AllergyIntolerance's returned
        And AllergyIntolerance Identifiers = <allergyIntoleranceIds>
        And resource is valid

        Examples:
            | reference | query                                                         | response | resultCount | allergyIntoleranceIds |
            | 4.7.dev1  | patient=1                                                     | 200      | 1           |                       |
            | 4.7.dev2  | clinical-status=active&patient=1008                           | 200      | 1           | 100003                |
            | 4.7.dev3  | date=ge2010-01-01                                             | 200      | >=10        |                       |


