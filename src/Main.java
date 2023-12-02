import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.Duration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.openqa.selenium.interactions.

public class Main {
    public static void printTableHeader() {
        System.out.println("|--------------------------------|-----------------|-----------------|-----------------|");
        System.out.println("| Testcase                       | Got             | Expected        | Result          |");
        System.out.println("|--------------------------------|-----------------|-----------------|-----------------|");
    }

    public static void printTestResult(String testcase, String got, String expected, String result) {
        System.out.printf("| %-30s | %-15s | %-15s | %-15s |\n", testcase, got, expected, result);
        System.out.println("|--------------------------------|-----------------|-----------------|-----------------|");
    }
    public static void login(WebDriver driver, String username, String password) throws InterruptedException {
        String baseUrl = "https://sandbox.moodledemo.net/login/index.php?lang=en";

//      launch and direct it to the Base URL
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement userElement = driver.findElement(By.name("username"));
        WebElement passwordElement = driver.findElement(By.name("password"));
        WebElement submitElement = driver.findElement(By.id("loginbtn"));

//      Input username
//        System.out.println("Enter username");
        userElement.sendKeys(username);

//      Input password
//        System.out.println("Enter password");
        passwordElement.sendKeys(password);

//      Click login
//        System.out.println("Click login");
        submitElement.click();
        Thread.sleep(3000);

//        System.out.println("Login successfully");
    }

    public static void editUserProfile(WebDriver driver, String firstName, String lastName, String email, String expected) {
        System.out.println(email);
        String baseUrl = "https://sandbox.moodledemo.net/user/edit.php?id=4&returnto=profile";
        driver.get(baseUrl);
        String result = "";

        WebElement firstNameInput = driver.findElement(By.name("firstname"));
        System.out.println("Enter first name");
        firstNameInput.clear();
//        firstNameInput.sendKeys(firstName);

        WebElement lastNameInput = driver.findElement(By.name("lastname"));
        System.out.println("Enter last name");
        lastNameInput.clear();
//        lastNameInput.sendKeys(lastName);

        WebElement emailInput = driver.findElement(By.id("id_email"));
        System.out.println("Enter email");
        emailInput.clear();
//        emailInput.sendKeys(email);

        WebElement page = driver.findElement(By.id("page"));

        WebElement updateButton = driver.findElement(By.name("submitbutton"));
        System.out.println("Click Update");
//        updateButton.click();

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

//        Error
        try {
            WebElement errorFirstName = driver.findElement(By.id("id_error_firstname"));
            System.out.println(errorFirstName.getText());
            result = "failure";
        } catch(Exception e) {
            result = "success";
        };

//        System.out.println("");
        if (result.equals(expected)) {
            System.out.println("Test passed: Got: " + result + ", Expected: " + expected);
        }
        else {
            System.out.println("Test failed: Got: " + result + ", Expected: " + expected);
        }
        System.out.println("Update successfully");
    }

    public static void createEvent(WebDriver driver, String stt, String title, String day, String show, String location,
                                   String duration, String repeatTime, String expected) throws InterruptedException {
        String baseUrl = "https://sandbox.moodledemo.net/calendar/view.php?view=month";

//      launch and direct it to the Base URL
        driver.get(baseUrl);
        Thread.sleep(3000);

//      Setup: Open event-modal
        WebElement newEvent = driver.findElement(By.cssSelector("[data-action='new-event-button']"));
        newEvent.click();
        Thread.sleep(3000);

//      Title
        WebElement titleInput = driver.findElement(By.xpath("//*[@id=\"id_name\"]"));
        titleInput.sendKeys(title);

//      Date
        if (!day.equals("0")) {
            Select daySelect = new Select(driver.findElement(By.id("id_timestart_day")));
            daySelect.selectByValue(day);
        }

//      Show more
        if (show.equals("TRUE")) {
//          Click show more
            driver.findElement(By.className("moreless-toggler")).click();
            Thread.sleep(3000);

//          Location
            driver.findElement(By.id("id_location")).sendKeys(location);

//          Duration
            if (!duration.isEmpty()) {
                // Biểu thức chính quy
                String regex = "[0-9]?[0-9]/[0-1]?[0-9]";

                // Tạo đối tượng Pattern từ biểu thức chính quy
                Pattern pattern = Pattern.compile(regex);

                // Tạo đối tượng Matcher để so khớp biến duration với biểu thức chính quy
                Matcher matcher = pattern.matcher(duration);

                // Kiểm tra xem duration có khớp với biểu thức chính quy không
                if (matcher.matches()) {
//                  Until
                    driver.findElement(By.id("id_duration_1")).click();

                    String[] arr = duration.split("/");
                    String durationDayValue = arr[0];
                    String durationMonthValue = arr[1];

                    Select durationDaySelect = new Select(driver.findElement(By.id("id_timedurationuntil_day")));
//                  handle convert day
                    if (durationDayValue.length() == 2) {
                        if (durationDayValue.indexOf('0') ==  0) {
                            durationDayValue = durationDayValue.substring(1);
                        }
                    }
                    durationDaySelect.selectByValue(durationDayValue);

                    Select durationMonthSelect = new Select(driver.findElement(By.id("id_timedurationuntil_month")));
//                  handle convert month
                    if (durationMonthValue.length() == 2) {
                        if (durationMonthValue.indexOf('0') ==  0) {
                            durationMonthValue = durationMonthValue.substring(1);
                        }
                    }
                    durationMonthSelect.selectByValue(durationMonthValue);


                }
                else {
                    driver.findElement(By.id("id_duration_2")).click();

                    WebElement timeDurationMinutes = driver.findElement(By.id("id_timedurationminutes"));
                    timeDurationMinutes.sendKeys(duration);
                }
            }

//          Is repeat clicked?
            if (!repeatTime.equals("0")) {
                driver.findElement(By.id("id_repeat")).click();

                WebElement repeatInput = driver.findElement(By.id("id_repeats"));
                repeatInput.sendKeys(repeatTime);
            }
        }

//      Save
        WebElement submitButton = driver.findElement(By.cssSelector("[data-action='save']"));
        submitButton.click();
        Thread.sleep(5000);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10000));

        String got = "success";

        WebElement errorTitle = driver.findElement(By.id("id_error_name"));
        if (errorTitle.isDisplayed()) got = "failure";

        WebElement errorFGroup = driver.findElement(By.id("fgroup_id_error_durationgroup"));
        if (errorFGroup.isDisplayed()) got = "failure";

        String result = "";
        if (got.equals(expected)) result = "Passed";
        else result = "Failed";
        printTestResult(stt, got, expected, result);
    }

    public static void testCreateNewCalendarEvent(WebDriver driver) throws IOException {
        String path = "src/dataset/datasetEvent.csv";

        File file = new File(path);

        Integer index = 0;
        String line = "";
        printTableHeader();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                createEvent(driver, fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7]);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting");

        String USERNAME = "student";
        String PASSWORD = "sandbox";
        String FIRSTNAME = "Kane2";
        String LASTNAME = "Ly2";
        String EMAIL = "kane.ly@digibank.vn";

        WebDriver driver = new ChromeDriver();

        login(driver, USERNAME, PASSWORD);
//        editUserProfile(driver, FIRSTNAME, LASTNAME, EMAIL, "success");

        String TITLE = "abc";
        String DAY = "2";
        Boolean SHOW = true;
        String LOCATION = "VietNam";
        String DURATION = "24/12";
        Boolean ISREPEAT = true;
        String REPEATTIME = "3";
//        createEvent(driver, TITLE, DAY, SHOW, LOCATION, DURATION, ISREPEAT, REPEATTIME, "success");
        testCreateNewCalendarEvent(driver);


//        driver.close();
    }
}