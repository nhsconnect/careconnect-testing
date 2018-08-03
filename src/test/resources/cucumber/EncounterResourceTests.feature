@encounter
Feature: Encounter Tests (version 0.11) .N-4-9


    Scenario Outline: <reference> Encounter Test
        Given FHIR STU3 Server
        When I Encounter Get <query>
        Then the method response code should be <response>
        And have <resultCount> Encounter's returned
        And Encounter Identifiers = <encounterIds>
        And resource is valid


        Examples:
            | reference | query                                                         | response | resultCount | encounterIds |
            | 4.9.dev1  | patient=4                                                     | 200      | 3           |              |
            | 4.9.dev2  | patient=1                                                     | 200      | 0           |              |
            | 4.9.dev4  | date=lt2010-01-01&patient=1007                                | 200      | 2           |  4451 4500   |
            | 4.9.dev5  | date=gt2010-01-01&patient=1007                                | 200      | 2           |  4121 4122   |


