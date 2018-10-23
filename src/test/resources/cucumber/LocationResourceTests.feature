@location
Feature: Location Tests (version 0.11) .N-4-2


    Scenario Outline: <reference> Location Test
        Given FHIR STU3 Server
        When I Get <query>
        Then the method response code should be <response>
        And have <resultCount> Location's returned
        And Location Identifiers = <locationIds>
        And resource is valid

        Examples:
            | reference | query                                     | response | resultCount | locationIds      |
            | 4.2.1     | Location?address-postalcode=NG10          | 200      | >=1         |                  |
            | 4.2.2     | Location?address-postalcode=NG10 1RY      | 200      | >=1         | RTG08            |
            | 4.2.3     | Location?address-postalcode=B68 9QP       | 200      | 0           |                  |
            | 4.2.4	    | Location?identifier=RTG08	                | 200      | 1           | RTG08            |
            | 4.2.5	    | Location?identifier=Dummy123	            | 200      | 0           |                  |
