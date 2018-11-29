package com.alpersemerci.tictactoe;

import com.alpersemerci.tictactoe.exception.GameAlreadyStartedException;
import com.alpersemerci.tictactoe.exception.InvalidBoardSizeException;
import com.alpersemerci.tictactoe.exception.InvalidMoveException;
import com.alpersemerci.tictactoe.exception.NoPlayerInGameException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RunWith(MockitoJUnitRunner.class)
public class TicTacToeAppTest {

    private InputStream original;

    @Before
    public void setUp() throws Exception {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String testInputPath = rootPath + "test.input";

        original = System.in;
        FileInputStream fileInputStream = new FileInputStream(new File(testInputPath));
        System.setIn(fileInputStream);
    }

    @After
    public void tearDown() throws Exception {
        System.setIn(original);
    }


    @Test
    public void test_command_line_game() throws NoPlayerInGameException, GameAlreadyStartedException, InvalidMoveException, InterruptedException, InvalidBoardSizeException, FileNotFoundException {
        String[] args = new String[0];
        TicTacToeApp.main(args);
    }

}