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


public class Test_flow3 extends BasePage {

    static RestClientWrapper restclient;
    Resources res;
    Faker faker =  new Faker();
    private static String baseUrl = null;
    private static String key = null;
    private static String token = null;
    private static String listID = null;
    private static String cardID = null;
    private static String attachmentID = null;
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
//        httpRequest = request
//                .baseUri(baseUrl)
//                .queryParam("key", key)
//                .queryParam("token", token)
//                .filter(RequestLoggingFilter.logRequestTo(log))
//                .filter(ResponseLoggingFilter.logResponseTo(log))

        restclient = new RestClientWrapper(baseUrl, httpRequest, key, token);
//        restclient.log();
        res = new Resources();
    }

    @Test
    public void createBoard() throws Exception {
        restclient.addAdditionalParameters("name", faker.name().firstName());
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
    public void createCard() throws Exception {
        restclient.addAdditionalParameters("name",faker.name().firstName());
        response  = restclient.post(cardsEndpoints(listID), " ");
        cardID = Utils.getValueFromResponse(response, "id");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard","createList","createCard"},priority = 1)
    public void getCard() throws Exception {
        response  = restclient.get(getCards(cardID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard","createList","createCard"})
    public void createAttachmentOnCard() throws Exception {
        restclient.addAdditionalParameters("name","nameOfAttachment");
        restclient.addAdditionalParameters("url","https://ravit.com");
        response  = restclient.post(createAttachment(cardID)," ");
        attachmentID = Utils.getValueFromResponse(response, "id");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard","createList","createCard","createAttachmentOnCard"}, priority = 2)
    public void deleteAttachmentOnCard() throws Exception {
        response  = restclient.delete(deleteAttachment(cardID, attachmentID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }
    @Test(dependsOnMethods = {"createBoard","createList","createCard"},priority = 3)
    public void deleteCard() throws Exception {
        response  = restclient.delete(getCards(cardID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }
}
