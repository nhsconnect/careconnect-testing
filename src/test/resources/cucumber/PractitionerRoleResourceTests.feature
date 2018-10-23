@practitionerrole
Feature: PractitionerRole Tests (version 0.11) .N-4-5


    Scenario Outline: <reference> PractitionerRole Test
        Given FHIR STU3 Server
        When I Get <query>
        Then the method response code should be <response>
        And have <resultCount> PractitionerRole's returned
        And PractitionerRole Identifiers = <practitionerRoleIds>
        And resource is valid

        Examples:
            | reference | query                                         | response | resultCount | practitionerRoleIds |
            | 4.5.1     | PractitionerRole?identifier=C2442626          | 200      | >=1         | C2442626            |
            | 4.5.2     | PractitionerRole?practitioner=Dummy123        | 200      | 0           |                     |
            | 4.5.3     | PractitionerRole?organization=1               | 200      | >=2         |                     |
            | 4.5.4     | PractitionerRole?organization=2               | 200      | >=1         |                     |
            | 4.5.5     | PractitionerRole?address-postalcode=Dummy123  | 404      | 0           |                     |
            | 4.5.5dev  | PractitionerRole?identifier=G6684183          | 200      | 1           | G6684183            |

