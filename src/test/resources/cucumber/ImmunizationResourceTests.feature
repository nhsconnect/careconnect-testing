@immunization
Feature: Immunization Tests (version 0.11) .N-4-10


    Scenario Outline: <reference> Immunization Test
        Given FHIR STU3 Server
        When I Immunization Get <query>
        Then the method response code should be <response>
        And have <resultCount> Immunization's returned
        And Immunization Identifiers = <immunizationIds>
        And resource is valid

        Examples:
            | reference | query                                                         | response | resultCount | immunizationIds |
            | 4.10.dev1 | patient=1                                                     | 200      | 1           |                 |
            | 4.10.dev2 | status=completed&patient=1004                                 | 200      | 5           |  268435458      |
            | 4.10.dev3 | date=ge2010-01-01                                             | 200      | >=9         |                 |


