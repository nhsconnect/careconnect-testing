package uk.nhs.careconnect.ri.integrationTest;

import ca.uhn.fhir.context.FhirContext;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

public class conformanceFeatureSteps {

    private static HttpTestClient client=null;

    @Before
    public static void beforeClass() throws Exception {

        if (client == null) {
            client = new HttpTestClient(FhirContext.forDstu3());
        }
    }


    @Given("^FHIR STU(\\d+) Server$")
    public void fhir_STU_Server(int arg1) throws Throwable {

    }

    @When("^I retrieve the ConformanceStatement with no format")
    public void i_retrieve_the_ConformanceStatement_without_Format() throws Throwable {
        client.doGet("metadata");
    }
    @When("^I retrieve the ConformanceStatement format=application/(\\w+)$")
    public void i_retrieve_the_ConformanceStatement(String format) throws Throwable {
        client.doGet("metadata?_format=application/"+format);
    }

    @When("^I retrieve the ConformanceStatement using OPTIONS format=application/(\\w+)$")
    public void i_retrieve_the_ConformanceStatement_using_options(String format) throws Throwable {
        client.doOptions("?_format=application/"+format);
    }

    @When("^I retrieve the ConformanceStatement using OPTIONS without format")
    public void i_retrieve_the_ConformanceStatement_using_options_without_format () throws Throwable {
        client.doOptions("");
    }

    @Then("^the response code should be (\\d+)$")
    public void the_response_code_should_be(int responseCode) throws Throwable {
        Assert.assertEquals(responseCode,client.getResponseCode());
    }

    @Then("^the Header:([A-z-]+)=([A-z//+]+)$")
    public void the_Header_ContentType_application_json_fhir(String header, String response) throws Throwable {
        //System.out.println("Header = "+header+" Response Value = "+response);
        Assert.assertThat(client.getHeader(header), CoreMatchers.containsString(response));

    }

    @When("^I retrieve the ConformanceStatement$")
    public void i_retrieve_the_ConformanceStatement() throws Throwable {
        client.doOptions("");
    }

}
