package api_testcases;

import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

public class CreateUser extends BasePage {

    private static RestClientWrapper restClient;
    private static String baseUrl;
    private static String Authorization;
    private static String user_token;
    private static String user_login;
//    private static String resource = "api/users/2";

    @BeforeClass
    public void setupTest() throws Exception {

        baseUrl = readingPropertiesFile.getProperty("baseurl");
        Authorization = readingPropertiesFile.getProperty("Authorization");
//        baseUrl = readingPropertiesFile.getProperty("baseurl");

    }

    @BeforeMethod
    public void setUpRequest() throws FileNotFoundException {

        // adding headers common to class
//		httpRequest.header("key", "value");

        restClient = new RestClientWrapper(baseUrl, httpRequest, "key", Authorization);

    }




    @Test(dataProvider = "email")
    public void createUser(String email) throws Exception {
        String bodyString = Utils.generateStringFromResource("/src/test/java/test_data/createUser.json");
        bodyString.replace("ravit.alaugh@nagarro.com", email);
        Response serverResponse = restClient.post(Resources.postEndPoint, bodyString );
        serverResponse.prettyPeek();
        user_token = Utils.getValueFromResponse(serverResponse, "User-Token");
        user_login = Utils.getValueFromResponse(serverResponse, "login");
        System.out.println(user_token);
    }

    @Test(dependsOnMethods={"createUser"})
    public void createSession() throws Exception {
        String bodyString = Utils.generateStringFromResource("/src/test/java/test_data/createUser.json");
        Response serverResponse  = restClient.post(createuserSession(),bodyString);
        user_token = Utils.getValueFromResponse(serverResponse, "User-Token");
        user_login = Utils.getValueFromResponse(serverResponse, "login");
        serverResponse.prettyPeek();
        System.out.println(user_token);
        System.out.println(user_login);
    }

    @Test(dependsOnMethods={"createUser","createSession"})
    public void getUser() throws Exception {
        restClient.addAdditionalParameters("User-Token",user_token);
        Response serverResponse  = restClient.get(getUserEndpoint(user_login));
        serverResponse.prettyPeek();
    }

    @Test(dependsOnMethods={"createUser","createSession"})
    public void updateUser() throws Exception {
        Response serverResponse  = restClient.get(getUserEndpoint(user_login));
        serverResponse.prettyPeek();
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
