package com.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.TimeUnit;

public class WebDriverInstance {

    @BeforeSuite

    public WebDriver getDriver(String browser){
        WebDriver driver;
        switch(browser.toLowerCase()){
            case "chrome":
            case "google chrome":
                System.setProperty("webdriver.chrome.driver","./drivers/chromedriver.exe");
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                driver.get("http://google.com");
                return driver;
        }
        return null;
    }

}
