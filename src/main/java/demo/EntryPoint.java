package demo;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import demo.DataDrivenTest;

public class EntryPoint {
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void main(String[] args) {
		TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
		TestNG testng = new TestNG();
		XmlSuite suite = new XmlSuite();
		// see also: 
		// https://www.lambdatest.com/automation-testing-advisor/selenium/methods/org.testng.xml.XmlSuite.setFileName
		System.err.println("Running " + args[0]);
		suite.setFileName(args[0]);	
		// suite.addTest(arg0);
		// 	suite.getGroups()
		testng.setCommandLineSuite(suite);
		// Default Suite
		// Total tests run: 0, Failures: 0, Skips: 0
		//
		// testng.setTestClasses(new Class[] { DataDrivenTest.class });
		testng.addListener(testListenerAdapter);
		testng.run();
	}
}
