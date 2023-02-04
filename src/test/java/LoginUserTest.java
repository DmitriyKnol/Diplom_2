import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.BaseApi;
import main.UserApi;
import main.UserAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static main.UserGenerator.*;

public class LoginUserTest extends BaseApi {
    UserApi userApi = new UserApi();
    UserAssert userAssert = new UserAssert();

    @Before
    public void setUp() {
        openUri();
        userApi.registration(getCreateUser());
    }

    @Test
    @DisplayName("Проверка возможности успешного логина")
    public void successLoginUser() {
        Response status = userApi.loginUser(getUser());
        userAssert.statusOk(status);
    }

    @Test
    @DisplayName("Проверка возможности логина c неправильным Email")
    public void loginUserWithBadEmail() {
        Response status = userApi.loginUser(getUserBadEmail());
        userAssert.statusUnauthorized(status);
    }

    @Test
    @DisplayName("Проверка возможности логина c неправильным паролем")
    public void loginUserWithBadPassword() {
        Response status = userApi.loginUser(getUserBadPassword());
        userAssert.statusUnauthorized(status);
    }
    @After
    public void deleteUser() {
        userApi.deleteUser();
    }
}
