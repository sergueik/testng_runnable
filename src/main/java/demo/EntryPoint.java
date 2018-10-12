package demo;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import demo.DataDrivenTest;

public class EntryPoint {
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void main(String[] args) {
		TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { DataDrivenTest.class });
		testng.addListener(testListenerAdapter);
		testng.run();
	}
}
