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


public class Test_flow4 extends BasePage {

    static RestClientWrapper restclient;
    Resources res;
    private static String baseUrl = null;
    private static String key = null;
    private static String token = null;
    private static String membersID = null;
    private static String boardsID = null;
    Response response;
    Faker faker = new Faker();
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
    public void getFilteredList() throws Exception {
        response  = restclient.get(getFilteredListEndpoint(boardsID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard"})
    public void inviteMembers() throws Exception {
        restclient.addAdditionalParameters("email","ravit.alaugh@nagarro.com");
        response  = restclient.put(membersEndpoint(boardsID), " ");
        membersID = Utils.getValueFromResponse(response, "members[0].id");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);

    }

    @Test(dependsOnMethods = {"createBoard","inviteMembers"})
    public void addMemberToBoard() throws Exception {
        restclient.addAdditionalParameters("type","normal");
        response  = restclient.put(getMembersEndpoint(boardsID, membersID), " ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

    @Test(dependsOnMethods = {"createBoard","inviteMembers","addMemberToBoard"})
    public void getMember() throws Exception {
        response = restclient.get(getMember(membersID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

    @Test(dependsOnMethods = {"createBoard","inviteMembers","addMemberToBoard"})
    public void updateMember() throws Exception {
        restclient.addAdditionalParameters("bio","RavitAlaugh");
        response = restclient.get(getMember(membersID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

    @Test(dependsOnMethods = {"createBoard","inviteMembers","addMemberToBoard"})
    public void uploadNewBoardBackgroundForaMember() throws Exception {
        response = restclient.post(newBoardBackgroud(membersID),"");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }

    @Test(dependsOnMethods = {"createBoard","inviteMembers","addMemberToBoard"})
    public void createSavedSearchForMember() throws Exception {
        restclient.addAdditionalParameters("name", String.valueOf(faker.name()));
        restclient.addAdditionalParameters("query","@" + faker.name().toString());
        restclient.addAdditionalParameters("pos","32768");
        response = restclient.post(createSavedSearches(membersID)," ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
    }
}
