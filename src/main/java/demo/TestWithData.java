package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.hamcrest.core.StringContains.containsString;

public class TestWithData {

	public WebDriver driver;
	public WebDriverWait wait;
	public Actions actions;
	public Alert alert;
	public JavascriptExecutor js;
	public TakesScreenshot screenshot;

	private static String osName;

	public int scriptTimeout = 5;
	public int flexibleWait = 120;
	public int implicitWait = 1;
	public long pollingInterval = 500;

	public String baseURL = "about:blank";

	private final String username = "you%40yourdomain.com";
	private final String authkey = "yourauthkey";
	private Sheet spreadsheet = getSpreadSheet();
	private final String spreadsheetName = "MySheet";

	@BeforeSuite
	public void beforeSuite() {

		System.setProperty("webdriver.chrome.driver",
				(new File("c:/java/selenium/chromedriver.exe")).getAbsolutePath());
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();

		Map<String, Object> chromePrefs = new HashMap<>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		String downloadFilepath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "target"
				+ System.getProperty("file.separator");
		chromePrefs.put("download.default_directory", downloadFilepath);
		chromePrefs.put("enableNetwork", "true");
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("allow-running-insecure-content");
		options.addArguments("allow-insecure-localhost");
		options.addArguments("enable-local-file-accesses");
		options.addArguments("disable-notifications");
		// options.addArguments("start-maximized");
		options.addArguments("browser.download.folderList=2");
		options.addArguments(
				"--browser.helperApps.neverAsk.saveToDisk=image/jpg,text/csv,text/xml,application/xml,application/vnd.ms-excel,application/x-excel,application/x-msexcel,application/excel,application/pdf");
		options.addArguments("browser.download.dir=" + downloadFilepath);
		// options.addArguments("user-data-dir=/path/to/your/custom/profile");
		capabilities.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		driver = new ChromeDriver(capabilities);
		actions = new Actions(driver);

		driver.manage().timeouts().setScriptTimeout(scriptTimeout,
				TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, flexibleWait);
		wait.pollingEvery(pollingInterval, TimeUnit.MILLISECONDS);
		screenshot = ((TakesScreenshot) driver);
		js = ((JavascriptExecutor) driver);
		spreadsheet = getSpreadSheet();
	}

	public Sheet getSpreadSheet() {
		// NOTE: Exception in thread "main" org.testng.TestNGException:
		// Cannot
		// instantiate class demo.TestWithData
		// String resourcePath = Thread.currentThread().getContextClassLoader()
		// .getResource("").getPath();
		// System.err.println("Resource path: " + resourcePath);
		System.err.println("User dir: " + System.getProperty("user.dir"));
		File file = new File(System.getProperty("user.dir") + File.separator
				+ "target\\classes\\Test.xlsx");

		FileInputStream inputStream = null;
		Workbook wb = null;
		try {
			inputStream = new FileInputStream(file);
			wb = WorkbookFactory.create(inputStream);
			System.err.println("Workbook: " + wb.toString());
		} catch (IOException e) {
			System.err.println("Excption (ignored): " + e.getMessage());
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			System.err.println("Invalid File format!");
		}

		Sheet spreadsheet = wb.getSheet(spreadsheetName);

		return spreadsheet;
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		String methodName = method.getName();
		System.err.println("Test Name: " + methodName + "\n");
		driver.get("http://crossbrowsertesting.github.io/login-form.html");
	}

	@AfterMethod
	public void afterMethod() {
		driver.get("about:blank");
	}

	@Test
	public void failedLoginPage() {

		Row row = spreadsheet.getRow(0);
		Cell cell = row.getCell(0);
		String username = cell.toString();
		assertThat(username, notNullValue());
		System.err.println("Entering username: " + username);
		driver.findElement(By.name("username")).sendKeys(username);

		cell = row.getCell(1);
		String password = cell.toString();
		assertThat(password, notNullValue());
		System.err.println("Entering password: " + password);
		driver.findElement(By.name("password")).sendKeys(password);

		System.err.println("Logging in");
		driver.findElement(By.cssSelector("div.form-actions > button")).click();

		System.err.println("Confirm not being able to login");
		wait.until(ExpectedConditions.textToBePresentInElementLocated(
				By.xpath("/html/body/div/div/div/div[1]"),
				"Username or password is incorrect"));
	}

	@Test
	public void loginPage() {

		String username = spreadsheet.getRow(1).getCell(0).toString();
		assertThat(username, notNullValue());
		System.err.println("Entering username: " + username);
		driver.findElement(By.name("username")).sendKeys(username);

		String password = spreadsheet.getRow(1).getCell(1).toString();
		assertThat(password, notNullValue());
		System.err.println("Entering password: " + password);
		driver.findElement(By.name("password")).sendKeys(password);

		System.err.println("Logging in");
		driver.findElement(By.cssSelector("div.form-actions > button")).click();

		System.err.println("Wait for the page load completely");
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@id=\"logged-in-message\"]/h2")));

		String welcomeMessage = driver
				.findElement(By.xpath("//*[@id=\"logged-in-message\"]/h2")).getText();
		assertThat(welcomeMessage,
				containsString("Welcome tester@crossbrowsertesting.com"));
		System.err.println("Confirmed the welcome message");

		System.err.println("Test Finished");
	}

	@AfterSuite
	public void tearDown() {
		driver.quit();
	}
}
