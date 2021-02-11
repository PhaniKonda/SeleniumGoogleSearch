package com.google;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.core.Helper;
import org.apache.tools.ant.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

public class SearchFunctionalityTest {

    private WebDriver driver;
    private ExtentHtmlReporter sparkReport;
    private ExtentReports report;
    private ExtentTest test;
    @BeforeTest
    @Parameters({"browser"})
    void setUp(String browser){
        WebDriverInstance instance = new WebDriverInstance();
        sparkReport = new ExtentHtmlReporter("result.html");
        report = new ExtentReports();
        report.attachReporter(sparkReport);
        driver = instance.getDriver(browser);
    }

    @Test(priority=0)
    void checkLandingPage(){
        test = report.createTest("Google Search Page","Searching and validating results in google engine");
        test.log(Status.INFO,"Starting up the run..");
        driver.switchTo().frame(0);
        WebElement consent = driver.findElement(By.xpath(WebElements.bthAgreeConsent));
        if(Helper.checkElementExists(consent)){
            consent.click();
        }
    }

    @Test(priority=1)
    @Parameters({"searchKey"})
    void searchInput(String strSearchString){
        test.log(Status.INFO,"Entering search");
        test.log(Status.INFO,"Search performed for keyword : '" + strSearchString + "'");
        WebElement searchBox = driver.findElement(By.xpath(WebElements.edtSearchBox));
        if(Helper.checkElementExists(searchBox)){
            searchBox.sendKeys(strSearchString);
            searchBox.sendKeys(Keys.ENTER);
            WebElement resultStats = driver.findElement(By.id(WebElements.eleResultStats));
            Assert.assertTrue(Helper.checkElementExists(resultStats));
        }
    }

    @Test(priority=2,dependsOnMethods = {"searchInput"})
    void validateSearchResult(){
        int recordsCount=0;
        WebElement resultStats = driver.findElement(By.id(WebElements.eleResultStats));
        Vector<String> recordsFound = StringUtils.split(resultStats.getText(),' ');
        test.log(Status.INFO,"Search records found: " + recordsFound.get(1));
        List<WebElement> searchResults = driver.findElements(By.xpath(WebElements.eleSearchResults));
        for(WebElement ele: searchResults){
            if(ele.getText().trim().length()>0){
                test.log(Status.INFO,"SearchResult "+(recordsCount+1)+": "+ele.getText());
                recordsCount++;
            }
        }
        System.out.println(recordsCount);
    }

    @Test(priority=3,dependsOnMethods = {"searchInput"})
    void verifySearchResult(){
        List<WebElement> searchResults = driver.findElements(By.xpath(WebElements.lnkSearchResults));
        for(WebElement ele: searchResults){
            try {
                HttpURLConnection huc = null;
                int respCode=200;
                String url = ele.getAttribute("href");
                if(url.length()>0 && url.toLowerCase().contains("http")){
                    huc = (HttpURLConnection)(new URL(ele.getAttribute("href")).openConnection());
                    huc.setRequestMethod("HEAD");
                    huc.connect();
                    respCode = huc.getResponseCode();
                    if(respCode>=400){
                        test.log(Status.FAIL,url+" : "+respCode+" code received");
                    }else{
                        test.log(Status.PASS,url+" : "+respCode+" code received");
                    }
                }
            } catch (MalformedURLException e) {
                test.log(Status.FAIL,e.getMessage());
            } catch (IOException e){
                test.log(Status.FAIL,e.getMessage());
            }
        }
    }

    @AfterTest
    void tearDown(){
        test.log(Status.INFO,"Completed run..");
        report.setSystemInfo("Browser","Chrome");
        report.flush();
        driver.quit();
    }

}
