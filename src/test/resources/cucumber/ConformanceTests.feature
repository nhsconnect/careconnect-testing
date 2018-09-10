@conformance
Feature: Conformance Http Test .N-1
The conformance statement will be used to validate the structure definition of the Patient resource. Additional content is expected as the conformance resources will include resources that are not expected to be accessed directly over the API (such as OperationDefinition, StructureDefinition and ValueSet).

    Scenario: 1.2 Conformance Retrieval XML using OPTIONS
        Given FHIR STU3 Server
        When I retrieve the ConformanceStatement using OPTIONS format=application/xml
        Then the response code should be 200

    Scenario: 1.3 Conformance Retrieval XML using GET
        Given FHIR STU3 Server
        When I retrieve the ConformanceStatement format=application/xml
        Then the response code should be 200
        Then the Header:Content-Type=application/fhir+xml

    Scenario: 1.4 Conformance Retrieval JSON using OPTIONS
        Given FHIR STU3 Server
        When I retrieve the ConformanceStatement using OPTIONS format=application/json
        Then the response code should be 200

    Scenario: 1.5 Conformance Retrieval JSON using GET
        Given FHIR STU3 Server
        When I retrieve the ConformanceStatement format=application/json
        Then the response code should be 200
        Then the Header:Content-Type=application/fhir+json

    Scenario: 1.6 Conformance Retrieval using OPTIONS
        Given FHIR STU3 Server
        When I retrieve the ConformanceStatement
        Then the response code should be 200



