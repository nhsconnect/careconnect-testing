package uk.nhs.careconnect.ri.integrationTest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.CoreMatchers;
import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Resource;
import org.junit.Assert;
import uk.org.hl7.fhir.core.Stu3.CareConnectSystem;
import uk.org.hl7.fhir.validation.stu3.CareConnectProfileValidationSupport;
import uk.org.hl7.fhir.validation.stu3.SNOMEDUKMockValidationSupport;

import java.util.List;
import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class methodFeatureSteps {


    public static final String LINESEP = System.getProperty("line.separator");

    private static HttpTestClient client=null;

    private static FhirContext ctx = null;

    private static FhirValidator val = null;

    private static FhirInstanceValidator instanceValidator = null;

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    @Before
    public static void beforeClass() throws Exception {

        if (client == null) {

            ctx = FhirContext.forDstu3();
            client = new HttpTestClient(ctx);
            val = ctx.newValidator();
            instanceValidator = new FhirInstanceValidator();
            val.registerValidatorModule(instanceValidator);
            ValidationSupportChain validationSupport = new ValidationSupportChain(
                    new DefaultProfileValidationSupport()
                    , new CareConnectProfileValidationSupport(ctx)
                    , new SNOMEDUKMockValidationSupport()
            );
            val.setValidateAgainstStandardSchema(true);
            // todo reactivate once this is fixed https://github.com/nhsconnect/careconnect-reference-implementation/issues/36
            //val.setValidateAgainstStandardSchematron(true);
            instanceValidator.setValidationSupport(validationSupport);
        }
    }

    @When("^I Delete ([A-z0-9//]+)$")
    public void Delete_Url(String url) throws Throwable {

        client.doDelete(url);
    }

    @When("^I Patch ([A-z0-9//]+)$")
    public void Patch_Url(String url, DataTable xmlString) throws Throwable {
        List<List<String>> data = xmlString.raw();

        client.doPatch(url,data.get(0).get(0));
    }

    @When("^I Post (\\w+)/(\\w+)$")
    public void Post_Url(String url1, String url2, DataTable xmlString) throws Throwable {
        List<List<String>> data = xmlString.raw();

        client.doPost(url1+"/"+url2,data.get(0).get(0));
    }

    @When("^I Post ([A-z0-9/_=]+)\\?([A-z0-9/\\-_=]+)$")
    public void PostSearch_Url(String url1, String url2) throws Throwable {


        client.doPost(url1+"?"+url2, null);
    }

    @When("^I Put ([A-z0-9/]+)$")
    public void Put_Url(String url, DataTable xmlString) throws Throwable {
        List<List<String>> data = xmlString.raw();

        client.doPatch(url,data.get(0).get(0));
    }

    @When("^I Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:#()|&]+)$")
    public void Get_Url(String url) throws Throwable {
        if (url.contains("#")) {
            url = getActualPatientId(url);
        }
        client.doGet(url);
    }

    @When("^I Head ([A-z0-9/?\\-_=]+)$")
    public void Head_Url(String url) throws Throwable {

        client.doHead(url);
    }

    @Then("^have (\\d+) (\\w+)'s returned$")
    public void have_Patient_s_returned(int count,String resource) throws Throwable {
        client.convertReplytoBundle();

        Assert.assertEquals(count, client.countResources());
        if (count > 0) {
            Assert.assertTrue(client.checkResourceType(resource));
        }
    }

    @Then("^have >=(\\d+) (\\w+)'s returned$")
    public void have_Resources_returned(int count,String resource) throws Throwable {
        client.convertReplytoBundle();
        Assert.assertThat("ResourceCount",client.countResources(),greaterThanOrEqualTo(count) );
        if (count > 0) {
            Assert.assertTrue(client.checkResourceType(resource));
        }
    }

    @Then("^have >(\\d+) Observation's returned$")
    public void have_Observation_s_returned(int count) throws Throwable {
        client.convertReplytoBundle();
        Assert.assertThat("ResourceCount",client.countResources(),greaterThan(count) );
    }

    @Then("^have \\* Observation's returned$")
    public void have_Observation_s_returned() throws Throwable {
        client.convertReplytoBundle();
        Assert.assertThat("ResourceCount",client.countResources(),greaterThan(0) );
    }

    @Then("^the method response code should be (\\d+)$")
    public void the_method_response_code_should_be(int responseCode) throws Throwable {
        Assert.assertEquals(responseCode,client.getResponseCode());
    }

    @Then("^Patient Id = (\\d+)$")
    public void patient_Id(String patientId) throws Throwable {
        Assert.assertEquals(patientId, client.getFirstPatientId());
    }

    @Then("^Location Ids = $")
    public void location_Ids() throws Throwable {

    }

    @Then("^Location Ids = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void location_Ids(String locations) throws Throwable {
        String[] locationList = locations.split(" ");
        List<String> idArray= client.getLocationIds();

        for (String locationId : locationList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(locationId));
        }
    }

    @Then("^Organization Ids = $")
    public void organization_Ids() throws Throwable {

    }

    @Then("^Organization Ids = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void organization_Ids(String organizations) throws Throwable {
        String[] organizationList = organizations.split(" ");
        List<String> idArray= client.getOrganizationIds();

        for (String organizationId : organizationList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(organizationId));
        }
    }


    @Then("^contains Ids$")
    public void contain_Ids(DataTable ids) throws Throwable {
       List<String> idArray= client.getPatientIds();
       List<Map<String, String>> data = ids.asMaps(String.class, String.class);

        for (Map map : data) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(map.get("PatientId").toString()));
        }
    }

    @Then("^Practitioner Ids = $")
    public void practitioner_Ids() throws Throwable {

    }

    @Then("^resource is valid$")
    public void resource_is_valid() throws Throwable {
        if (client.getResponseCode() == 200) {
            Assert.assertThat(client.bundle, CoreMatchers.instanceOf(Bundle.class));
            Assert.assertNull(validateResource(client.bundle));
        }
    }

    @Then("^PractitionerRole Ids = $")
    public void practitionerrole_Ids() throws Throwable {

    }

    @Then("^Observation Ids = $")
    public void observation_Ids() throws Throwable {

    }

    @Then("^the resource is a Bundle$")
    public void the_resource_is_a_Bundle() throws Throwable {
        Assert.assertThat(client.bundle, CoreMatchers.instanceOf(Bundle.class));
    }



    @Then("^Condition Ids = $")
    public void condition_Ids() throws Throwable {

    }


    @Then("^Procedure Ids = $")
    public void procedure_Ids() throws Throwable {

    }

    @When("^I AllergyIntolerance Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void allergy_intolerance_Get_Url(String url) throws Throwable {

        client.doGet("AllergyIntolerance?"+url);
    }

    @Then("^AllergyIntolerance Identifiers = $")
    public void allergyintolerance_Identifiers() throws Throwable {

    }

    @Then("^AllergyIntolerance Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void allergyIntolerance_Ids(String allergies) throws Throwable {
        String[] allergyList = allergies.split(" ");
        List<String> idArray= client.getAllergyIdentifiers();

        for (String allergyId : allergyList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(allergyId));
        }
    }

    @Then("^Condition Identifiers = $")
    public void condition_Identifiers() throws Throwable {

    }
    @Then("^Condition Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void condition_IdentifiersParam(String conditions) throws Throwable {
        String[] conditionList = conditions.split(" ");
        List<String> idArray= client.getConditionIdentifiers();

        for (String conditionId : conditionList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(conditionId));
        }
    }

    @When("^I Procedure Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void procedure_intolerance_Get_Url(String url) throws Throwable {

        client.doGet("Procedure?"+url);
    }

    @When("^I Condition Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()#|&]+)$")
    public void condition_intolerance_Get_Url(String url) throws Throwable {

        if (url.contains("#")) {
            url = getActualPatientId(url);
        }
        client.doGet("Condition?"+url);
    }

    public String getActualPatientId(String url) throws Throwable {
        if (url.contains("#")) {
            String patientId = url.substring(url.indexOf("#")+1,url.indexOf("#",url.indexOf("#")+1));
         //   System.out.println("PATIENT = "+patientId);
            client.doGet("Patient/"+patientId);
            Patient patient = client.convertToPatient();
            url = url.replace("#"+patientId+"#",patient.getId());
         //   System.out.println("New url = "+url);
        }
        return url;
    }

    @When("^I Encounter Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void encounter_intolerance_Get_Url(String url) throws Throwable {

        client.doGet("Encounter?"+url);
    }


    @Then("^Encounter Identifiers = $")
    public void encounter_Ids() throws Throwable {

    }

    @Then("^Encounter Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void encounter_IdentifiersParam(String encounters) throws Throwable {
        String[] encounterList = encounters.split(" ");
        List<String> idArray= client.getEncounterIdentifiers();

        for (String encounterId : encounterList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(encounterId));
        }
    }

    @Then("^Immunization Identifiers = $")
    public void immunization_Identifiers() throws Throwable {

    }

    @Then("^Immunization Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void immunization_IdentifiersParam(String immunizations) throws Throwable {
        String[] immunizationList = immunizations.split(" ");
        List<String> idArray= client.getImmunizationIdentifiers();

        for (String immunizationId : immunizationList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(immunizationId));
        }
    }

    @Then("^MedicationRequest Identifiers = $")
    public void MedicationRequest_Identifiers() throws Throwable {

    }

    @Then("^MedicationRequest Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void MedicationRequest_IdentifiersParam(String medicationRequests) throws Throwable {
        String[] medicationRequestList = medicationRequests.split(" ");
        List<String> idArray= client.getMedicationRequestIdentifiers();

        for (String medicationRequestId : medicationRequestList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(medicationRequestId));
        }
    }

    @Then("^MedicationStatement Identifiers = $")
    public void MedicationStatement_Identifiers() throws Throwable {

    }

    @Then("^MedicationStatement Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void MedicationStatement_IdentifiersParam(String medicationStatements) throws Throwable {
        String[] medicationStatementList = medicationStatements.split(" ");
        List<String> idArray= client.getMedicationStatementIdentifiers();

        for (String medicationStatementId : medicationStatementList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(medicationStatementId));
        }
    }

    @Then("^Observation Identifiers = $")
    public void Observation_Identifiers() throws Throwable {

    }

    @Then("^Observation Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void Observation_IdentifiersParam(String observations) throws Throwable {
        String[] observationList = observations.split(" ");
        List<String> idArray= client.getObservationIdentifiers();

        for (String observationId : observationList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(observationId));
        }
    }

    @Then("^Procedure Identifiers = $")
    public void Procedure_Identifiers() throws Throwable {

    }

    @Then("^Procedure Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void Procedure_IdentifiersParam(String procedures) throws Throwable {
        String[] procedureList = procedures.split(" ");
        List<String> idArray= client.getProcedureIdentifiers();

        for (String procedureId : procedureList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(procedureId));
        }
    }

    @Then("^Practitioner Identifiers = $")
    public void Practitioner_Identifiers() throws Throwable {

    }

    @Then("^Practitioner Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void Practitioner_IdentifiersParam(String practitioners) throws Throwable {
        String[] practitionerList = practitioners.split(" ");
        List<String> idArray= client.getPractitionerIdentifiers();

        for (String practitionerId : practitionerList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(practitionerId));
        }
    }

    @Then("^Organization Identifiers = $")
    public void Organization_Identifiers() throws Throwable {

    }

    @Then("^Organization Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void Organization_IdentifiersParam(String organizations) throws Throwable {
        String[] organizationList = organizations.split(" ");
        List<String> idArray= client.getOrganizationIdentifiers();

        for (String organizationId : organizationList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(organizationId));
        }
    }
    @Then("^Location Identifiers = $")
    public void Location_Identifiers() throws Throwable {

    }

    @Then("^Location Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void Location_IdentifiersParam(String locations) throws Throwable {
        String[] locationList = locations.split(" ");
        List<String> idArray= client.getLocationIdentifiers();

        for (String locationId : locationList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(locationId));
        }
    }

    @Then("^PractitionerRole Identifiers = $")
    public void PractitionerRole_Identifiers() throws Throwable {

    }

    @Then("^PractitionerRole Identifiers = ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)")
    public void PractitionerRole_IdentifiersParam(String roles) throws Throwable {
        String[] roleList = roles.split(" ");
        List<String> idArray= client.getPractitionerRoleIdentifiers();

        for (String roleId :roleList) {
            Assert.assertThat(idArray.toString(), CoreMatchers.containsString(roleId));
        }
    }


    @When("^I Immunization Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void immunization_Get_Url(String url) throws Throwable {

        client.doGet("Immunization?"+url);
    }

    @Then("^Immunization Ids = $")
    public void immunization_Ids() throws Throwable {

    }

    @When("^I MedicationRequest Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void MedicationRequest_Get_Url(String url) throws Throwable {

        client.doGet("MedicationRequest?"+url);
    }


    @Then("^MedicationRequest Ids = $")
    public void medicationorder_Ids() throws Throwable {

    }


    @When("^I MedicationStatement Get ([A-zÀ-ÿ0-9ć/?_\\-=\\s@.:()|&]+)$")
    public void MedicationStatement_intolerance_Get_Url(String url) throws Throwable {

        client.doGet("MedicationStatement?"+url);
    }

    @Then("^MedicationStatement Ids = $")
    public void medicationstatement_Ids() throws Throwable {

    }


    public String validateResource(Bundle bundle) {
        String passed = null;
        if (bundle != null) {
            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                Resource resource = entry.getResource();

                ValidationResult results = val.validateWithResult(resource);

                StringBuilder b = new StringBuilder("Validation results:" + ansi().boldOff());
                int count = 0;
                for (SingleValidationMessage next : results.getMessages()) {
                    // We are not used a UK Terminology server and can't expand resources at present. Hence ignoring these errors/warnings
                    if (next.getMessage().contains("and a code from this value set is required") && next.getMessage().contains(CareConnectSystem.SNOMEDCT)) {
                        System.out.println("match **");
                    } else if (next.getMessage().contains("a code is required from this value set") && next.getMessage().contains(CareConnectSystem.SNOMEDCT)) {
                        System.out.println("match ** ** ");
                    } else if (next.getMessage().contains("and a code is recommended to come from this value set") && next.getMessage().contains(CareConnectSystem.SNOMEDCT)) {
                        System.out.println("match ** ** **");
                    } else if (next.getMessage().contains("and a code is recommended to come from this value set") && next.getMessage().contains("https://fhir.hl7.org.uk/STU3/ValueSet/CareConnect-ConditionCategory-1")) {
                        System.out.println("match ** ** **");
                    } else if (next.getMessage().contains("and a code should come from this value set unless it has no suitable code") && next.getMessage().contains(CareConnectSystem.SNOMEDCT)) {
                        System.out.println("match ** ** ** **");
                    } else if (next.getMessage().contains("and a code should come from this value set unless it has no suitable code") && next.getMessage().contains("https://fhir.hl7.org.uk/STU3/ValueSet/CareConnect-ConditionCategory-1")) {
                        System.out.println("match ** ** ** **");
                    } else if (next.getMessage().contains("path Patient.name (fhirPath = true and (use memberOf")) {
                        System.out.println("** ** ** Code Issue ValueSet expansion not implemented in instanceValidator" );
                    } else if (next.getMessage().contains("Error Multiple filters not handled yet")) {
                        System.out.println("** ** ** multiple filters in ValueSet not implemented" );
                    } else {

                        count++;

                        String message ="HL7 FHIR CareConnect Validation - "+next.getSeverity() + " " + next.getMessage()+ " Resource="+resource.getResourceType().toString()+"/"+resource.getIdElement().getIdPart();
                        System.out.println(message);
                        if (passed == null) {
                            switch (next.getSeverity()) {
                                case ERROR:
                                case FATAL:
                                case WARNING:
                                    passed = message;
                            }
                        }
                    }
                }
            }
        }
        return passed;
    }

}
