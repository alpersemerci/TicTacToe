package com.alpersemerci.tictactoe.service.menu;

import com.alpersemerci.tictactoe.model.Board;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class CommandLineInterfaceGameMenuTest {

    private GameMenu menu;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
    private final PrintStream originalPrintStream = System.out;
    private final PrintStream originalErrorPrintStream = System.err;

    private InputStream original;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorOutputStream));

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String testInputPath = rootPath + "test.input";

        original = System.in;
        FileInputStream fileInputStream = new FileInputStream(new File(testInputPath));
        System.setIn(fileInputStream);

        menu = new CommandLineInterfaceGameMenu();
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(originalPrintStream);
        System.setErr(originalErrorPrintStream);
        System.setIn(original);
    }

    @Test
    public void test_showMessage_without_parameter() {
        int initialStreamSize = outputStream.size();
        menu.showMessage("banner");
        int finalStreamSize = outputStream.size();
        Assert.assertTrue("Final stream size must be greater than initial stream size", initialStreamSize < finalStreamSize);
    }

    @Test
    public void test_showMessage_with_parameter() {
        String parameter = "Keyser Söze";
        int initialStreamSize = outputStream.size();
        menu.showMessage("player.symbol.input", parameter);
        int finalStreamSize = outputStream.size();
        Assert.assertTrue("Final stream size must be greater than initial stream size", initialStreamSize < finalStreamSize);
        Assert.assertTrue("Final stream must contain parameter", outputStream.toString().contains(parameter));

    }

    @Test
    public void test_getInput_without_parameter_and_without_validator() {
        int initialStreamSize = outputStream.size();
        String s = menu.getInput("banner", String::new);
        int finalStreamSize = outputStream.size();
        Assert.assertTrue("Final stream size must be greater than initial stream size", initialStreamSize < finalStreamSize);
        Assert.assertTrue("Input shouldn't be null", s != null);
    }

    @Test
    public void test_getInput_without_parameter_and_with_validator() {
        int initialStreamSize = outputStream.size();
        String s = menu.getInput("banner", String::new, string -> string != null);
        int finalStreamSize = outputStream.size();
        Assert.assertTrue("Final stream size must be greater than initial stream size", initialStreamSize < finalStreamSize);
        Assert.assertTrue("Input shouldn't be null", s != null);
    }

    @Test
    public void test_getInput_with_parameter_and_with_validator() {
        String parameter = "Keyser Söze";
        int initialStreamSize = outputStream.size();
        String s = menu.getInput("player.symbol.input", String::new, string -> string != null, parameter);
        int finalStreamSize = outputStream.size();
        Assert.assertTrue("Final stream size must be greater than initial stream size", initialStreamSize < finalStreamSize);
        Assert.assertTrue("Input shouldn't be null", s != null);
        Assert.assertTrue("Final stream must contain parameter", outputStream.toString().contains(parameter));
    }

    @Test
    public void updateBoard() {
        int initialStreamSize = outputStream.size();
        Board board = new Board(10);
        menu.updateBoard(board);
        int finalStreamSize = outputStream.size();
        Assert.assertTrue("Final stream size must be greater than initial stream size", initialStreamSize < finalStreamSize);
    }
}