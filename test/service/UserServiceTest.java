package service;

import model.portfolio.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService("test/resources/users_test.csv");
    }

    @Test
    void findById_shouldReturnCorrectUser() {
        User user = userService.findById(1);
        assertEquals("Test User", user.getFullName());
    }



    @Test
    void findById_shouldReturnNullForUnknownId() {
        assertNull(userService.findById(999));
    }



}