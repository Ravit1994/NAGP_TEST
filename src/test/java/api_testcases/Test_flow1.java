package api_testcases;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testbase.BasePage;
import utilities.Resources;
import utilities.RestClientWrapper;
import utilities.Utils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Test_flow1 extends BasePage {

    static RestClientWrapper restclient;
    Resources res;
    Faker faker = new Faker();
    private static String baseUrl = null;
    private static String key = null;
    private static String token = null;
    private static String boardsID = null;
    private static String membersID = null;
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
    public void getBoard() throws Exception {
        response  = restclient.get(getBoardsEndpoints()+boardsID);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }
    @Test(dependsOnMethods = {"createBoard"})
    public void updateBoard() throws Exception {
        restclient.addAdditionalParameters("name",faker.name().firstName());
        response  = restclient.put(getBoardsEndpoints()+boardsID," ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }
    @Test(dependsOnMethods = {"createBoard"})
    public void createList() throws Exception {
        restclient.addAdditionalParameters("idBoard",boardsID);
        restclient.addAdditionalParameters("name",faker.name().firstName());
        response  = restclient.post(createListEndpoint(boardsID), " ");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }
    @Test(dependsOnMethods = {"createBoard"})
    public void getFilteredList() throws Exception {
        response  = restclient.get(getFilteredListEndpoint(boardsID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }

//    @Test(dependsOnMethods = {"createBoard"},dataProvider = "email")
//    public void inviteMembers(String email) throws Exception {
//        restclient.addAdditionalParameters("email",email);
//        response  = restclient.put(membersEndpoint(boardsID), " ");
//        int statusCode = response.getStatusCode();
//        Assert.assertEquals(statusCode,200);
//        membersID = Utils.getValueFromResponse(response, "members[0].id");
//    }

//    @Test(dependsOnMethods = {"createBoard","inviteMembers"})
//    public void addMemberToBoard() throws Exception {
//        restclient.addAdditionalParameters("type","normal");
//        response  = restclient.put(getMembersEndpoint(boardsID, membersID), " ");
//        int statusCode = response.getStatusCode();
//        Assert.assertEquals(statusCode,200);
//    }

    @Test(dependsOnMethods = {"createBoard","inviteMembers","addMemberToBoard"})
    public void removeMemberFromBoard() throws Exception {
        restclient.addAdditionalParameters("type","normal");
        response  = restclient.delete(getMembersEndpoint(boardsID, membersID));
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);
    }
@DataProvider(name="email")
    public Object[][] email()
    {
        Object[][] arrObj = getExcelData(System.getProperty("user.dir")+"/src/test/java/test_data/Book1.xlsx");
        return arrObj;
    }

    private String[][] getExcelData(String fileName) {
        String[][] data = null;

        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sh = wb.getSheet("Sheet1");
            XSSFRow row = sh.getRow(0);
            int noOfRows = sh.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();
            Cell cell;
            data = new String[noOfRows-1][noOfCols];
            for(int i =1; i<noOfRows;i++){
                for(int j=0;j<noOfCols;j++){
                    row = sh.getRow(i);
                    cell= row.getCell(j);
                    data[i-1][j] = cell.getStringCellValue();
                }
            }
        }
        catch (Exception e) {
            System.out.println("The exception is: " +e.getMessage());
        }
        return data;

    }
}
