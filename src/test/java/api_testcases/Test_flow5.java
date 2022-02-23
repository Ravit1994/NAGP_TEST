package api_testcases;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testbase.BasePage;
import utilities.Resources;
import utilities.RestClientWrapper;
import utilities.Utils;

import java.io.FileNotFoundException;


public class Test_flow5 extends BasePage {

    static RestClientWrapper restclient;
    Resources res;
    private static String baseUrl = null;
    private static String key = null;
    private static String token = null;
    private static String List_id;
    Response response;
    private static String organisationId = "";
    private static String memberId = " ";
    Faker faker = new Faker();
    @BeforeClass
    public void setupTest() throws Exception {

        baseUrl = readingPropertiesFile.getProperty("baseurl");
        key = readingPropertiesFile.getProperty("key");
        token = readingPropertiesFile.getProperty("token");
    }

    @BeforeMethod
    public void setupRequest() throws FileNotFoundException {
//        RequestSpecification httpRequest = null;
        restclient = new RestClientWrapper(baseUrl, httpRequest, key, token);
        res = new Resources();
    }


    @Test
    public void createOrganisation() throws Exception {
        restclient.addAdditionalParameters("displayName",faker.name().firstName());
        response  = restclient.post(organizationEndpoint(), " ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
        organisationId = Utils.getValueFromResponse(response, "id");
    }

    @Test(dependsOnMethods = {"createOrganisation"})
    public void updateOrganisation() throws Exception {
        restclient.addAdditionalParameters("displayName",faker.name().firstName());
        response  = restclient.put(OrganisationEndpoints(organisationId), " ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
        organisationId = Utils.getValueFromResponse(response, "id");
    }
    @Test(dependsOnMethods = {"createOrganisation"}, priority = 2)
    public void getOrganisation() throws Exception {
        response  = restclient.get(OrganisationEndpoints(organisationId));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createOrganisation"}, priority = 3)
    public void deleteOrganisation() throws Exception {
        response  = restclient.delete(OrganisationEndpoints(organisationId));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }
}
