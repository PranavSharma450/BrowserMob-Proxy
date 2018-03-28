package com.maven.browserMob2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class CaptureNetworkTraffic {
	public static WebDriver driver;
	public WebDriverWait wait = null;
	public static BrowserMobProxyServer server;
	Map<String, String> map = null;

	@BeforeClass
	public void setup() throws Exception {

		server = new BrowserMobProxyServer();
		server.start();

		int port = server.getPort();
		Proxy proxy = ClientUtil.createSeleniumProxy(server);

		DesiredCapabilities seleniumCapabilities = new DesiredCapabilities();
		seleniumCapabilities.setCapability(CapabilityType.PROXY, proxy);

		HashSet<CaptureType> enable = new HashSet<CaptureType>();
		// enable.add(CaptureType.REQUEST_HEADERS);
		enable.add(CaptureType.REQUEST_CONTENT);
		// enable.add(CaptureType.RESPONSE_HEADERS);
		enable.add(CaptureType.RESPONSE_CONTENT);
		server.enableHarCaptureTypes(enable);

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\pranav.sharma\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver(seleniumCapabilities);

		map = LoadPlaceHolders.getProperties();
		driver.get(map.get("url"));

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 20);
		driver.manage().window().maximize();

		System.out.println("Port started:" + port);

	}

	@Test
	public void demo_test1() throws InterruptedException, JsonProcessingException, IOException {

		server.newHar("mcd-dev-portal.har");

		ArrayList<String> whiteListURL = new ArrayList<String>();
		whiteListURL.add("https?://.*(mcdportal-dev.digital.diversey.com)+.*");
		server.whitelistRequests(whiteListURL, 200);

		driver.manage().window().maximize();
		driver.findElement(By.id("userName")).sendKeys(map.get("name"));
		driver.findElement(By.id("password")).sendKeys(map.get("password"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,100)");
		driver.findElement(By.className("login-btn")).click();
		xyz("file1345");
		Thread.sleep(8000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href,'temp-monitoring')]")));
		driver.findElement(By.xpath("//a[contains(@href,'temp-monitoring')]")).click();
		xyz("file123");
		Thread.sleep(15000);
	}

	
	public void xyz(String filename) throws JsonProcessingException, IOException {

		// get the HAR data
		Har har = server.getHar();

		// Write HAR Data in a File
		File harFile = new File("C:\\tmp\\"+ filename+".har");
		
		try {
			har.writeTo(harFile);
			
		} catch (IOException ex) {
			System.out.println(ex.toString());
			System.out.println("Could not find file ");

		}

//		ObjectMapper mapper = new ObjectMapper();
//		mapper.enable(SerializationFeature.INDENT_OUTPUT);
//		JsonNode root = mapper.readTree(new File("C:\\tmp\\NewFile10.har"));
//		JsonNode entries = root.path("log").path("entries");
//		for (JsonNode entry : entries) {
//			if (entry.get("request").get("postData") != null && !entry.get("request").get("postData").isEmpty(null)) {
//				{
//					System.out.println("request:\n" + entry.get("request") + "\n");
//
//					String str = entry.get("request").get("postData").get("text").toString();
//					if (str.contains(map.get("name")) && str.contains(map.get("password")))
//						System.out.println(
//								"name verified:" + map.get("name") + "\n password verified:" + map.get("password"));
//				}
//				// if (driver != null) {
//				// server.stop();
//				// driver.quit();
//				// }
//			}
//		}
	}
}
