package utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListeners implements ITestListener {

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("Before test start");

	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("On test success");

	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub

		System.out.println("Before test failure");

	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("On test skip");
	}


	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		System.out.println("Before  start");

	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		System.out.println("On  finish");

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

}
