package uk.nhs.careconnect.ri.integrationTest;

import ca.uhn.fhir.context.FhirContext;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.dstu3.model.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class HttpTestClient {

    //private static String DEFAULT_SERVER_BASE_URL = "http://127.0.0.1:8183/ccri-fhir/STU3/";

   // private static String DEFAULT_SERVER_BASE_URL = "https://data.developer.nhs.uk/ccri-fhir/STU3/";

   private static String DEFAULT_SERVER_BASE_URL = "https://data.developer-test.nhs.uk/ccri-fhir/STU3/";


    private final FhirContext ctx;

    private HttpResponse response = null;

    public Bundle bundle = null;

    private String serverBaseUrl = null;

    HttpTestClient(FhirContext ctx) {
        this.ctx = ctx;
    }

    private HttpClient getHttpClient(){
        final HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient;
    }

    private String constructFullUrl(String url){
        return getServerBaseUrl() + url;
    }

    private String getServerBaseUrl() {
        if (serverBaseUrl == null ){
            serverBaseUrl = System.getProperty("serverBaseUrl", DEFAULT_SERVER_BASE_URL);
        }
        return serverBaseUrl;
    }

    public String encodeUrl(String url) {
        url = url.replace(" ","%20");

        url = url.replace("|","%7C");
        return url;
    }

    public int getResponseCode() throws IOException {
        return response.getStatusLine().getStatusCode();
    }

    public String getHeader(String header) {
        /*
        Header[] headers = response.getAllHeaders();
        for (Header headeritem : headers) {
            System.out.println("Key : " + headeritem.getName()
                    + " ,Value : " + headeritem.getValue());
        }
        */
        if (response == null ) return null;
        if (response.getFirstHeader(header) == null) return null;
        return response.getFirstHeader(header).getValue();

    }

    public void doGet(String httpUrl) throws Exception {
        final HttpClient client = getHttpClient();
        final String query = encodeUrl(constructFullUrl(httpUrl));
        final HttpGet request = new HttpGet(query);
        response = client.execute(request);
    }

    public void doOptions(String httpUrl) throws Exception {
        final HttpClient client = getHttpClient();
        final String query = encodeUrl(constructFullUrl(httpUrl));
        final HttpOptions request = new HttpOptions(query);
        response = client.execute(request);
    }

    public void doDelete(String httpUrl) throws Exception {
        final HttpClient client = getHttpClient();
        final HttpDelete request = new HttpDelete(constructFullUrl(httpUrl));
        response = client.execute(request);

    }

    public void doHead(String httpUrl) throws Exception {
        final HttpClient client = getHttpClient();
        final HttpHead request = new HttpHead(constructFullUrl(httpUrl));
        response = client.execute(request);

    }

    public void doPatch(String httpUrl, String body) throws Exception {

        final HttpClient client = getHttpClient();
        final HttpPatch request = new HttpPatch(constructFullUrl(httpUrl));
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
        request.setEntity(new StringEntity(body));
       // System.out.println(request.getURI());
        response = client.execute(request);

    }

    public void doPost(String httpUrl, String body) throws Exception {
        final HttpClient client = getHttpClient();
        final HttpPost request = new HttpPost(constructFullUrl(httpUrl));
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
        if (body != null && !body.isEmpty()) {
            request.setEntity(new StringEntity(body));
        }
        //System.out.println(request.getURI());
        response = client.execute(request);

    }

    public int countResources() {
        if (bundle == null) return 0;
        return bundle.getTotal();
    }

    public Patient convertToPatient(){
        Patient patient = null;
        try {

            Reader reader = new InputStreamReader(this.response.getEntity().getContent());
            patient = (Patient) ctx.newJsonParser().parseResource(reader);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return patient;
    }

    public void convertReplytoBundle(){
        bundle = null;
        try {

            Reader reader = new InputStreamReader(this.response.getEntity().getContent());
            bundle = (Bundle) ctx.newJsonParser().parseResource(reader);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Boolean checkResourceType(String resource) {
        if (bundle == null) return null;

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            if (!entry.getResource().getResourceType().toString().equals(resource)) return false;
        }
        return true;

    }

    public String getFirstPatientId() {
        if (bundle == null) return null;

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Patient patient = (Patient) entry.getResource();
            return patient.getIdElement().getIdPart();
        }
        return null;
    }

    public List<String> getPatientIds() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Patient patient = (Patient) entry.getResource();
            ids.add(patient.getIdElement().getIdPart());
        }
        return ids;
    }

    public List<String> getLocationIds() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Location location = (Location) entry.getResource();
            ids.add(location.getIdElement().getIdPart());
        }
        return ids;
    }

    public List<String> getAllergyIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            AllergyIntolerance allergy = (AllergyIntolerance) entry.getResource();
            for (Identifier identifier : allergy.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getConditionIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Condition condition = (Condition) entry.getResource();
            for (Identifier identifier :
                    condition.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getProcedureIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Procedure procedure = (Procedure) entry.getResource();
            for (Identifier identifier :
                    procedure.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getObservationIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Observation observation = (Observation) entry.getResource();
            for (Identifier identifier :
                    observation.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getOrganizationIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Organization organization = (Organization) entry.getResource();
            for (Identifier identifier :
                    organization.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getLocationIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Location location = (Location) entry.getResource();
            for (Identifier identifier :
                    location.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getPractitionerIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Practitioner practitioner = (Practitioner) entry.getResource();
            for (Identifier identifier :
                    practitioner.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getPractitionerRoleIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            PractitionerRole practitionerRole = (PractitionerRole) entry.getResource();
            for (Identifier identifier :
                    practitionerRole.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }


    public List<String> getImmunizationIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Immunization immunization = (Immunization) entry.getResource();
            for (Identifier identifier :
                    immunization.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getMedicationRequestIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            MedicationRequest medicationRequest = (MedicationRequest) entry.getResource();
            for (Identifier identifier :
                    medicationRequest.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getMedicationStatementIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            MedicationStatement medicationStatement = (MedicationStatement) entry.getResource();
            for (Identifier identifier :
                    medicationStatement.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }

    public List<String> getEncounterIdentifiers() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Encounter encounter = (Encounter) entry.getResource();
            for (Identifier identifier :
                    encounter.getIdentifier()) {
                ids.add(identifier.getValue());
            }
        }
        return ids;
    }


    public List<String> getOrganizationIds() {
        if (bundle == null) return null;

        List<String> ids = new ArrayList<>();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Organization organization = (Organization) entry.getResource();
            ids.add(organization.getIdElement().getIdPart());
        }
        return ids;
    }


}
