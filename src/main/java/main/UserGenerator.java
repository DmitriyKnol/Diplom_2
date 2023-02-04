package main;

import com.github.javafaker.Faker;
import main.pojo.CreateUser;
import main.pojo.User;


public class UserGenerator {
    static Faker faker = new Faker();
    private static final String emailFaker = faker.internet().emailAddress();
    private static final String passwordFaker = faker.internet().password(6, 10);
    private static final String userNameFaker = faker.name().firstName();
    private static final String newEmail = faker.internet().emailAddress();
    private static final String newName = faker.name().firstName();
    static CreateUser createUser = new CreateUser(emailFaker, passwordFaker, userNameFaker);
    static CreateUser createUserWithoutEmail = new CreateUser("", passwordFaker, userNameFaker);
    static CreateUser createUserWithoutPassword = new CreateUser(emailFaker, "", userNameFaker);
    static CreateUser createUserWithoutUserName = new CreateUser(emailFaker, passwordFaker, "");
    static User user = new User(emailFaker, passwordFaker);
    static User userBadEmail = new User(emailFaker.substring(1), passwordFaker);
    static User userBadPassword = new User(emailFaker, passwordFaker.substring(1));

    public static String getNewEmail() {

        return newEmail;
    }

    public static String getNewName() {
        return newName;
    }

    public static CreateUser getCreateUser() {
        return createUser;
    }

    public static CreateUser getCreateUserWithoutEmail() {
        return createUserWithoutEmail;
    }

    public static CreateUser getCreateUserWithoutPassword() {
        return createUserWithoutPassword;
    }

    public static CreateUser getCreateUserWithoutUserName() {
        return createUserWithoutUserName;
    }

    public static User getUser() {
        return user;
    }

    public static User getUserBadEmail() {
        return userBadEmail;
    }

    public static User getUserBadPassword() {
        return userBadPassword;
    }


}
