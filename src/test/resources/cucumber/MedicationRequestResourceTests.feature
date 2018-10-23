@medicationrequest
Feature: MedicationRequest Tests (version 0.11) .N-4-11


    Scenario Outline: <reference> MedicationRequest Test
        Given FHIR STU3 Server
        When I MedicationRequest Get <query>
        Then the method response code should be <response>
        And have <resultCount> MedicationRequest's returned
        And MedicationRequest Identifiers = <medicationRequestIds>
        And resource is valid

        Examples:
            | reference | query                                                         | response | resultCount | medicationRequestIds    |
            | 4.11.1     | patient=1                                                    | 200      | 0         |                         |
            | 4.11.2     | code=1040511000001102                                        | 404      | 0           |                         |
            | 4.11.3     | authoredon=gt2017-07-31                                      | 200      | >=4         |  4294967300             |
            | 4.11.4     | authoredon=ge2017-07-31                                      | 200      | >=5         |  4294967299             |
            | 4.11.5     | authoredon=ge2017-07-31&status=completed                     | 200      | >=5         |  4294967299             |

