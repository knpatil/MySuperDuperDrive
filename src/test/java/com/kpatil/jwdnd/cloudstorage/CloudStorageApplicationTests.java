package com.kpatil.jwdnd.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    private static final String SERVER = "http://localhost:";
    private static final String USERNAME = "test11";
    private static final String PASSWORD = "test1234";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String LOGIN_URL = "/login";
    private static final String SIGN_UP_URL = "/signup";

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
    }

    @AfterAll
    static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }

    @Test
    public void getLoginPage() {
        driver.get(getLoginUrl());
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void getSignUpPage() {
        driver.get(getSignUpUrl());
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void testUnauthorizedHomePageAccess() {
        driver.get(getHomePageUrl());
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUserSignUpLoginAndLogout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        // sign up user
        driver.get(getSignUpUrl());
        SignUpPage singUpPage = new SignUpPage(driver);
        singUpPage.signUp(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);

        // login user
        driver.get(getLoginUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);

        // assertions
        Assertions.assertEquals("Home", driver.getTitle());
        WebElement welcomeTextField = driver.findElement(By.id("welcomeText"));
        assertThat(welcomeTextField.getText()).isEqualTo("Welcome " + FIRST_NAME);

        // logout
        loginPage.logout();

        // wait for logout
        Thread.sleep(500);
        assertThat(driver.getTitle()).isEqualTo("Login");

        // try accessing home page after logout
        driver.get(getHomePageUrl());
        assertThat(driver.getTitle()).isEqualTo("Login"); // redirect to login page
    }

    @Test
    public void testNoteCreateEditDelete() throws InterruptedException {
        // sign up user
        driver.get(getSignUpUrl());
        SignUpPage singUpPage = new SignUpPage(driver);
        singUpPage.signUp(FIRST_NAME, LAST_NAME, USERNAME, PASSWORD);

        // login user
        driver.get(getLoginUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);

        // go to notes tab
        NotePage notePage = new NotePage(driver);
        notePage.goToNotesTab();

        // create a new note
        notePage.addNewNote("Test Note", "Test Description");

        // assert data
        Thread.sleep(1000);
        WebElement noteTitle = driver.findElement(By.id("note1"));
        assertThat(noteTitle.getText()).isEqualTo("Test Note");


        // Thread.sleep(10000);
        notePage.logout();
        Thread.sleep(500);
    }

    private String getHomePageUrl() {
        return SERVER + this.port + "/home";
    }

    private String getLoginUrl() {
        return SERVER + this.port + LOGIN_URL;
    }

    private String getSignUpUrl() {
        return SERVER + this.port + SIGN_UP_URL;
    }
}
