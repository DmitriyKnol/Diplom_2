import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.BaseApi;
import main.UserApi;
import main.UserAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateUserTest extends BaseApi {
    UserApi userApi = new UserApi();
    UserAssert userAssert = new UserAssert();

    @Before
    public void setUp() {
        openUri();
    }

    @Test
    @DisplayName("Проверка возможности создать нового пользователя")
    public void successfulRegistrationNewUser() {
        Response status = userApi.registration(userApi.getCreateUser());
        userAssert.statusOk(status);
    }

    @Test
    @DisplayName("Попытка создать пользователя с повторяющимся именем")
    public void registrationDuplicateUser() {
        userApi.registration(userApi.getCreateUser());
        Response status = userApi.registrationWhithoutExtractToken(userApi.getCreateUser());
        userAssert.statusForbidden(status);
    }

    @Test
    @DisplayName("Попытка создания пользователя без заполненного email")
    public void registrationUserWithoutEmail() {
        Response status = userApi.registrationWhithoutExtractToken(userApi.getCreateUserWithoutEmail());
        userAssert.statusForbidden(status);
    }

    @Test
    @DisplayName("Попытка создания пользователя без заполненного Password")
    public void registrationUserWithoutPassword() {
        Response status = userApi.registrationWhithoutExtractToken(userApi.getCreateUserWithoutPassword());
        userAssert.statusForbidden(status);
    }

    @Test
    @DisplayName("Попытка создания пользователя без заполненного имени пользователя")
    public void registrationUserWithoutUserName() {
        Response status = userApi.registrationWhithoutExtractToken(userApi.getCreateUserWithoutUserName());
        userAssert.statusForbidden(status);
    }

    @After
    public void deleteUser() {
        userApi.deleteUser();
    }
}