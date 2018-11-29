package com.alpersemerci.tictactoe.service.menu;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphicalUserInterfaceGameMenuTest {

    private GameMenu menu;

    @Before
    public void setUp() throws Exception {
        menu = new GraphicalUserInterfaceGameMenu();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_showMessage() {
        menu.showMessage("test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_showMessage_with_parameter() {
        menu.showMessage("test", "test2");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_getInput_with_parameter() {
        menu.getInput("test", null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_getInput_with_validator_and_parameter() {
        menu.getInput("test", null, null,null);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void updateBoard() {
        menu.updateBoard(null);
    }
}