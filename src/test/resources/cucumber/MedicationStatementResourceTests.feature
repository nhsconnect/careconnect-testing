@medicationstatement
Feature: MedicationStatement Tests (version 0.11) .N-4-12


    Scenario Outline: <reference> MedicationStatement Test
        Given FHIR STU3 Server
        When I MedicationStatement Get <query>
        Then the method response code should be <response>
        And have <resultCount> MedicationStatement's returned
        And MedicationStatement Identifiers = <medicationStatementIds>
        And resource is valid

        Examples:
            | reference | query                                                         | response | resultCount | medicationStatementIds |
            | 4.12.1     | patient=2                                                    | 200      | 0           |                        |
            | 4.12.2     | effective=gt2017-07-31                                       | 200      | >=4         |  4294967300             |
            | 4.12.3     | effective=ge2017-07-31                                       | 200      | >=5         |  4294967297             |
            | 4.12.4     | effective=ge2017-07-31&status=completed                      | 200      | >=5         |  4294967297             |


