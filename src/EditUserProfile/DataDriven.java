package EditUserProfile;

import Helper.Helper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataDriven {
    public static void editUserProfileBoundary(WebDriver driver, String stt, String firstName, String lastName,
                                               String email, String expected) throws InterruptedException {
        String baseUrl = "https://sandbox.moodledemo.net/user/edit.php?id=4&returnto=profile";
        driver.get(baseUrl);
        Thread.sleep(3000);

        try {
            WebElement cancelEmail = driver.findElement(By.linkText("Cancel email change"));
            cancelEmail.click();
            Thread.sleep(3000);
        } catch(Exception ignored) {}

        WebElement firstNameInput = driver.findElement(By.name("firstname"));
        firstNameInput.clear();

        WebElement lastNameInput = driver.findElement(By.name("lastname"));
        lastNameInput.clear();

        WebElement emailInput = driver.findElement(By.id("id_email"));
        emailInput.clear();

        WebElement page = driver.findElement(By.id("page"));

        WebElement updateButton = driver.findElement(By.name("submitbutton"));

        Actions action = new Actions(driver);
        action
                .click(lastNameInput)
                .sendKeys(lastName)
                .click(firstNameInput)
                .sendKeys(firstName)
                .click(emailInput)
                .sendKeys(email)
                .click(page)
                .pause(4)
        ;

        action.click(updateButton);
        action.perform();
        action.release();

        Thread.sleep(3000);

        String got = "";
//        Error
        try {
            WebElement errorFirstName = driver.findElement(By.id("id_error_firstname"));
            got = "failure";
        } catch(Exception e) {
            got = "success";
        };

        String result = "";
        if (got.equals(expected)) result = "Passed";
        else result = "Failed";
        Helper.printTestResult(stt, got, expected, result);
    }

    public static void testEditUserProfileBoundary(WebDriver driver) throws IOException {
        String path = "src/dataset/datasetEditBoundary.csv";

        File file = new File(path);

        Integer index = 0;
        String line = "";
        Helper.printTableHeader();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                editUserProfileBoundary(driver, fields[0], fields[1], fields[2], fields[3], fields[4]);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editUserProfileEquivalent(WebDriver driver, String stt, String firstName, String lastName,
                                                 String email, String moodleNetProfile, String city, String expected)
            throws InterruptedException {
        String baseUrl = "https://sandbox.moodledemo.net/user/edit.php?id=4&returnto=profile";
        driver.get(baseUrl);
        Thread.sleep(3000);

        try {
            WebElement cancelEmail = driver.findElement(By.linkText("Cancel email change"));
            cancelEmail.click();
            Thread.sleep(3000);
        } catch(Exception ignored) {}

        WebElement firstNameInput = driver.findElement(By.name("firstname"));
        firstNameInput.clear();

        WebElement lastNameInput = driver.findElement(By.name("lastname"));
        lastNameInput.clear();

        WebElement emailInput = driver.findElement(By.id("id_email"));
        emailInput.clear();

        WebElement moodleNetProfileInput = driver.findElement(By.id("id_moodlenetprofile"));
        moodleNetProfileInput.clear();

        WebElement cityInput = driver.findElement(By.id("id_city"));
        cityInput.clear();

//        id_city

        WebElement page = driver.findElement(By.id("page"));

        WebElement updateButton = driver.findElement(By.name("submitbutton"));

        Actions action = new Actions(driver);
        action
                .click(lastNameInput)
                .sendKeys(lastName)
                .click(firstNameInput)
                .sendKeys(firstName)
                .click(emailInput)
                .sendKeys(email)
                .click(moodleNetProfileInput)
                .sendKeys(moodleNetProfile)
                .click(cityInput)
                .sendKeys(city)
                .pause(2)
                .click(page)
                .pause(2)
        ;

        action.click(updateButton);
        action.perform();
        action.release();

        Thread.sleep(3000);

        String got = "success";
//        Error
        try {
            WebElement errorFirstName = driver.findElement(By.id("id_error_firstname"));
            if (errorFirstName.isDisplayed()) got = "failure";

            WebElement errorLastName = driver.findElement(By.id("id_error_lastname"));
            if (errorLastName.isDisplayed()) got = "failure";

            WebElement errorEmail = driver.findElement(By.id("id_error_email"));
            if (errorEmail.isDisplayed()) got = "failure";
        } catch(Exception e) {
            got = "success";
        };

        String result = "";
        if (got.equals(expected)) result = "Passed";
        else result = "Failed";
        Helper.printTestResult(stt, got, expected, result);
    }

    public static void testEditUserProfileEquivalent(WebDriver driver) throws IOException {
        String path = "src/dataset/datasetEditEquivalent.csv";

        File file = new File(path);

        Integer index = 0;
        String line = "";
        Helper.printTableHeader();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            line = reader.readLine();
            while((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                editUserProfileEquivalent(driver, fields[0], fields[1], fields[2], fields[3], fields[4], fields[5],
                        fields[6]);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        String USERNAME = "student";
        String PASSWORD = "sandbox";

        WebDriver driver = new ChromeDriver();

        Helper.login(driver, USERNAME, PASSWORD);

        testEditUserProfileBoundary(driver);
        testEditUserProfileEquivalent(driver);
    }

}
