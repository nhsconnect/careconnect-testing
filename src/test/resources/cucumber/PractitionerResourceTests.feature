@practitioner
Feature: Practitioner Tests (version 0.11) .N-4-4


    Scenario Outline: <reference> Practitioner Test
        Given FHIR STU3 Server
        When I Get <query>
        Then the method response code should be <response>
        And have <resultCount> Practitioner's returned
        And Practitioner Identifiers = <practitionerIds>
        And resource is valid

        Examples:
            | reference | query                                   | response | resultCount | practitionerIds       |
            | 4.4.1     | Practitioner?identifier=G6684183        | 200      | 1           | G6684183              |
            | 4.4.2     | Practitioner?identifier=Dummy123        | 200      | 0           |                       |
            | 4.4.3     | Practitioner?address-postalcode=LS15    | 200      | >=50        |                       |
            | 4.4.4     | Practitioner?address-postalcode=LS9 6AU | 200      | >=5         | G8241436 G9351592     |
            | 4.4.5     | Practitioner?address-postalcode=Dum     | 200      | 0           |                       |

