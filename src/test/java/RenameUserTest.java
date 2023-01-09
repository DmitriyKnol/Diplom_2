import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.BaseApi;
import main.UserApi;
import main.UserAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RenameUserTest extends BaseApi {
    UserApi userApi = new UserApi();
    UserAssert userAssert = new UserAssert();

    @Before
    public void setUp() {
        openUri();
        userApi.registration(userApi.getCreateUser());
    }

    @Test
    @DisplayName("Проверка возможности изменения Email авторизованного пользователя")
    public void renameEmailWithAuthorization() {
        Response status = userApi.rename("email", userApi.getNewEmail(), userApi.getToken());
        userAssert.statusOk(status);
    }

    @Test
    @DisplayName("Проверка возможности изменения имени авторизованного пользователя")
    public void renameNameWithAuthorization() {
        Response status = userApi.rename("name", userApi.getNewName(), userApi.getToken());
        userAssert.statusOk(status);
    }

    @Test
    @DisplayName("Проверка возможности изменения Email неавторизованного пользователя")
    public void renameEmailNonAuthorization() {
        Response status = userApi.rename("email", userApi.getNewEmail(), "");
        userAssert.statusUnauthorized(status);
    }

    @Test
    @DisplayName("Проверка возможности изменения имени неавторизованного пользователя")
    public void renameNameNonAuthorization() {
        Response status = userApi.rename("name", userApi.getNewName(), "");
        userAssert.statusUnauthorized(status);
    }

    @After
    public void deleteUser() {
        userApi.deleteUser();
    }
}
