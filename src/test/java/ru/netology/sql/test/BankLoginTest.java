package ru.netology.sql.test;

import org.junit.jupiter.api.*;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.data.SQLHelper;
import ru.netology.sql.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.sql.data.SQLHelper.cleanAuthCodes;
import static ru.netology.sql.data.SQLHelper.cleanDatabase;


public class BankLoginTest {
    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll () {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open ("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should login with defauld data")
    void shouldLoginCorrectlyTest() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Should get error if unknown user")
    void shouldGetErrorIfRandomUserLoginTest() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка. Такой пользователь не зарегистрирован.");
    }

    @Test
    @DisplayName("Should get error if existing user send random verification code")
    void shouldGetErrorNotificationIfWrongVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка. Вы ввели неверный код.");

    }


}
