package api_testcases;

import com.github.javafaker.Faker;
import io.restassured.internal.RestAssuredResponseImpl;
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


public class Test_flow2 extends BasePage {

    static RestClientWrapper restclient;
    Resources res;
    Faker faker = new Faker();
    private static String baseUrl = null;
    private static String key = null;
    private static String token = null;
    private static String listID = null;
    private static String boardsID = null;
    Response response;

    @BeforeClass
    public void setupTest() throws Exception {
        baseUrl = readingPropertiesFile.getProperty("baseurl");
        key = readingPropertiesFile.getProperty("key");
        token = readingPropertiesFile.getProperty("token");
    }

    @BeforeMethod
    public void setupRequest() throws FileNotFoundException {
        restclient = new RestClientWrapper(baseUrl, httpRequest, key, token);
        res = new Resources();
    }

    @Test
    public void createBoard() throws Exception {
        restclient.addAdditionalParameters("name",faker.name().firstName());
        response = restclient.post(boardsEndpoints(), " ");
        boardsID = Utils.getValueFromResponse(response, "id");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }
    @Test(dependsOnMethods = {"createBoard"})
    public void createList() throws Exception {
        restclient.addAdditionalParameters("idBoard",boardsID);
        restclient.addAdditionalParameters("name",faker.name().firstName());
        response  = restclient.post(listEndpoint(), " ");
        listID = Utils.getValueFromResponse(response, "id");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard","createList"})
    public void updateList() throws Exception {
        restclient.addAdditionalParameters("name",faker.name().firstName());
        response  = restclient.put(listEndpoint() + listID, " ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard","createList"})
    public void createCard() throws Exception {
        restclient.addAdditionalParameters("name",faker.name().firstName());
        response  = restclient.post(cardsEndpoints(listID), " ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard","createList"})
    public void archiveList() throws Exception {
        restclient.addAdditionalParameters("value","true");
        response  = restclient.put(archiveList(listID), " ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }



}
