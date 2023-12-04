package CreateCalendarEvent;
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

import Helper.Helper;

public class DataDriven {
    public static void createEvent(WebDriver driver, String stt, String title, String day, String show, String location,
                                   String duration, String repeatTime, String expected) throws InterruptedException {
        String baseUrl = "https://sandbox.moodledemo.net/calendar/view.php?view=month";

//      launch and direct it to the Base URL
        driver.get(baseUrl);
        Thread.sleep(5000);

//      Setup: Open event-modal
        WebElement newEvent = driver.findElement(By.cssSelector("[data-action='new-event-button']"));
        newEvent.click();
        Thread.sleep(5000);

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
            Thread.sleep(5000);

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
        Helper.printTestResult(stt, got, expected, result);
    }

    public static void testCreateNewCalendarEvent(WebDriver driver) throws IOException {
        String path = "src/dataset/datasetEvent.csv";

        File file = new File(path);

        Integer index = 0;
        String line = "";
        Helper.printTableHeader();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] fields = line.split(",");
                createEvent(driver, fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7]);
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

        testCreateNewCalendarEvent(driver);
    }
}
