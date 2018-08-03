@procedure
Feature: Procedure Tests (version 0.11) .N-4-13


    Scenario Outline: <reference> Procedure Test
        Given FHIR STU3 Server
        When I Procedure Get <query>
        Then the method response code should be <response>
        And have <resultCount> Procedure's returned
        And Procedure Identifiers = <procedureIds>
        And resource is valid

        Examples:
            | reference | query                                                         | response | resultCount | procedureIds      |
            | 4.13.dev1 | patient=1                                                     | 200      | 1           |                   |
            | 4.13.dev2 | subject=Patient/1                                             | 200      | 1           |                   |
            | 4.13.dev3 | subject:Patient=1                                             | 200      | 1           |                   |
            | 4.13.dev4 | date=lt2010-01-01                                             | 200      | >=0         |                   |
            | 4.13.dev5 | date=gt2010-01-01&patient=1010                                | 200      | 2           |  100002 100003    |


