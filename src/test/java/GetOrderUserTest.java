import io.qameta.allure.junit4.DisplayName;
import main.BaseApi;
import main.OrderApi;
import main.UserApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static main.UserGenerator.getCreateUser;
import static org.hamcrest.CoreMatchers.is;
import static org.apache.http.HttpStatus.*;

public class GetOrderUserTest extends BaseApi {
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();

    @Before
    public void setUp() {
        openUri();
        userApi.registration(getCreateUser());
    }

    @Test
    @DisplayName("Получение списка заказов пользователя с авторизацией")
    public void getUserOrdersWithAuthorization() {
        orderApi.getOrder(userApi.getToken())
                .then()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Получение списка заказов пользователя без авторизации")
    public void getUserOrdersWithNonAuthorization() {
        orderApi.getOrder("")
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false));
    }

    @After
    public void deleteUser() {
        userApi.deleteUser();
    }
}
