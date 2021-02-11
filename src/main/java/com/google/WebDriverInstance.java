package com.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WebDriverInstance {

    @BeforeSuite

    public WebDriver getDriver(String browser){
        if(browser.toLowerCase().equals("chrome")){
            System.setProperty("webdriver.chrome.driver","./drivers/chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.get("http://google.com");
            return driver;
        }
        return null;
    }

}
