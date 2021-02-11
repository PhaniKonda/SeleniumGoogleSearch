package com.google.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Helper {
    public static boolean checkElementExists(WebElement ele){
        if(ele!=null && ele.isDisplayed() && ele.isEnabled())
            return true;
        else
            return false;

    }
}
