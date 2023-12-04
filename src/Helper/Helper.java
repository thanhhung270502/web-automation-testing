package Helper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Helper {
    public static void login(WebDriver driver, String username, String password) throws InterruptedException {
        String baseUrl = "https://sandbox.moodledemo.net/login/index.php?lang=en";

//      launch and direct it to the Base URL
        driver.get(baseUrl);
        Thread.sleep(3000);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

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

    public static void printTableHeader() {
        System.out.println("|--------------------------------|-----------------|-----------------|-----------------|");
        System.out.println("| Testcase                       | Got             | Expected        | Result          |");
        System.out.println("|--------------------------------|-----------------|-----------------|-----------------|");
    }

    public static void printTestResult(String testcase, String got, String expected, String result) {
        System.out.printf("| %-30s | %-15s | %-15s | %-15s |\n", testcase, got, expected, result);
        System.out.println("|--------------------------------|-----------------|-----------------|-----------------|");
    }
}
