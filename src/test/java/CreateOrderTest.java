import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.BaseApi;
import main.OrderApi;
import main.OrderAssert;
import main.UserApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateOrderTest extends BaseApi {
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    OrderAssert orderAssert = new OrderAssert();

    @Before
    public void setUp() {
        openUri();
        userApi.registration(userApi.getCreateUser());
        orderApi.getIngredients();
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя")
    public void createOrderAuthorization() {
        Response status = orderApi.createOrder(orderApi.getIngredientsValid(), userApi.getToken());
        orderAssert.statusOk(status);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя без ингредиентов")
    public void createOrderWithAuthorization() {
        Response status = orderApi.createOrder(orderApi.getNullIngredients(), userApi.getToken());
        orderAssert.statusBadRequest(status);
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя с невалидным хэшем")
    public void createOrderWithBadHashAndAuthorization() {
        Response status = orderApi.createOrder(orderApi.getInvalIdIngredients(), userApi.getToken());
        orderAssert.statusInternalServerError(status);
    }

    @Test
    @DisplayName("Создание заказа неавторизованного пользователя")
    public void createOrderNonAuthorization() {
        Response status = orderApi.createOrder(orderApi.getIngredientsValid(), "");
        orderAssert.statusOk(status);
    }

    @Test
    @DisplayName("Создание заказа неавторизованного пользователя без ингредиентов")
    public void createOrderWithNonAuthorization() {
        Response status = orderApi.createOrder(orderApi.getNullIngredients(), "");
        orderAssert.statusBadRequest(status);
    }

    @Test
    @DisplayName("Создание заказа неавторизованного пользователя с невалидным хэшем")
    public void createOrderWithBadHashAndNonAuthorization() {
        Response status = orderApi.createOrder(orderApi.getInvalIdIngredients(), "");
        orderAssert.statusInternalServerError(status);
    }

    @After
    public void deleteUser() {
        userApi.deleteUser();
    }
}
