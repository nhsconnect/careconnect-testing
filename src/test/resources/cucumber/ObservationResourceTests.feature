@observation
Feature: Observation Tests (version 0.11) .N-4-6


    Scenario Outline: <reference> Observation Test
        Given FHIR STU3 Server
        When I Get <query>
        Then the method response code should be <response>
        And have <resultCount> Observation's returned
        And Observation Identifiers = <observationIds>
        And resource is valid

        Examples:
            | reference | query                                                         | response | resultCount | observationIds |
            | 4.6.1     | Observation?category=laboratory                               | 200      | >=10        |                |
            | 4.6.2     | Observation?category=imaging                                  | 200      | 0           |                |
            | 4.6.3     | Observation?code=165581004                                    | 200      | 4           |                |
            | 4.6.4     | Observation?code=Dummy1234                                    | 200      | 0           |                |
            | 4.6.5     | Observation?date=2017-10-16                                   | 200      | >=10        |                |
            | 4.6.6     | Observation?date=16-sep-2017                                  | 400      | 0           |                |
            | 4.6.7     | Observation?date=2012                                         | 200      | >=3         |                |
            | 4.6.8     | Observation?date=2012-11                                      | 200      | >=1         |                |
            | 4.6.9     | Observation?patient=#1004#                                      | 200      | 0           |                |
            | 4.6.10    | Observation?patient=#1050#                                      | 200      | 1           |                |
            | 4.6.11    | Observation?patient=#1051#                                      | 200      | >=10        |                |
            | 4.6.12    | Observation?patient=#1050#&category=observation                 | 200      | 0           |                |
            | 4.6.13    | Observation?patient=#1050#&category=vital-signs                 | 200      | 1           |                |
            | 4.6.14    | Observation?patient=#1051#&category=vital-signs                 | 200      | >=10        |                |
            | 4.6.15    | Observation?patient=#1051#&category=vital-signs&date=2017-02-11 | 200      | 7           |                |
            | 4.6.16    | Observation?patient=#1051#&category=vital-signs&date=2017-02-12 | 200      | 0           |                |
            | 4.6.17    | Observation?patient=#1051#&category=observation&date=2017-02-11 | 200      | 0           |                |
            | 4.6.18    | Observation?patient=#1051#&category=vital-signs&code=50373000   | 200      | 3           |                |
            | 4.6.19    | Observation?patient=#1051#&category=vital-signs&code=50373001   | 200      | 0           |                |
            | 4.6.20    | Observation?patient=#1051#&category=observation&code=50373000   | 200      | 0           |                |
            | 4.6.21    | Observation?patient=#1051#&category=vital-signs&code=50373000&date=2017-02-11 | 200      | 1           |                |
            | 4.6.22    | Observation?patient=#1051#&category=observation&code=50373000&date=2017-02-12 | 200      | 0           |                |
            | 4.6.23    | Observation?date=eq2017-10-16                                 | 200      | >=10        |                |
            | 4.6.24    | Observation?date=ne2017-10-16                                 | 400      | 0           |                |
            | 4.6.25    | Observation?date=gt2016                                       | 200      | *           |                |
            | 4.6.26    | Observation?date=gt2016-09                                    | 200      | >=10        |                |
            | 4.6.27    | Observation?date=gt2017-10-17                                 | 200      | *           |                |
            | 4.6.28    | Observation?date=lt2013                                       | 200      | >=3         |                |
            | 4.6.29    | Observation?date=lt2012-10                                    | 200      | >=2         |                |
            | 4.6.30    | Observation?date=lt2012-08-10                                 | 200      | >=0         |                |
            | 4.6.31    | Observation?date=lt2012-08-11                                 | 200      | >=2         |                |
            | 4.6.32    | Observation?date=gt2017-10-18                                 | 200      | >=10        |                |
            | 4.6.33    | Observation?date=ge2017-10-18                                 | 200      | *           |                |
            | 4.6.34    | Observation?date=le2012-08-10                                 | 200      | >=2         |                |
            | 4.6.35    | Observation?date=sa2017-10-17                                 | 400      | 0           |                |
            | 4.6.36    | Observation?date=ap2017-10-17                                 | 400      | 0           |                |

     @ignore
     Scenario Outline: <reference> Observation Test Ignored
         Given FHIR STU3 Server
         When I Get <query>
         Then the method response code should be <response>
         And have <resultCount> Observation's returned
         And Observation Ids = <observationIds>
         And resource is valid

         Examples:
             | reference | query                                                         | response | resultCount | observationIds |
             | 4.6.6done | Observation?date=16-sep-2017                                  | 400      | 0           |                |