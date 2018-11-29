package com.alpersemerci.tictactoe.service.menu;

import com.alpersemerci.tictactoe.model.Board;
import com.alpersemerci.tictactoe.service.config.ConfigService;
import com.alpersemerci.tictactoe.service.game.BoardService;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * CommandLineInterfaceGameMenu is an implementation of GameMenu interface for command line user interface experience.
 */
@Slf4j
public class CommandLineInterfaceGameMenu implements GameMenu {

    private ConfigService configService = ConfigService.getInstance();

    private BoardService boardService = new BoardService();

    private Scanner s = new Scanner(System.in);

    /**
     * Displays a message to user via system out interface
     *
     * @param propertyKey
     */
    @Override
    public void showMessage(String propertyKey) {
        String message = configService.getConfigValue(propertyKey);
        System.out.println(message);
    }

    /**
     * Displays a message to user with parameters via system out interface
     *
     * @param propertyKey
     * @param parameters
     */
    @Override
    public void showMessage(String propertyKey, String... parameters) {
        String message = configService.getConfigValue(propertyKey);
        System.out.println(MessageFormat.format(message, parameters));
    }

    /**
     * Gets input from user with a message (with parameters) and converts input into desired type.
     *
     * @param propertyKey
     * @param inputConverter
     * @param parameters
     * @param <T>
     * @return
     */
    @Override
    public <T> T getInput(String propertyKey, Function<String, T> inputConverter, String... parameters) {
        showMessage(propertyKey, parameters);
        String input = s.next();
        return inputConverter.apply(input);
    }

    /**
     * Gets input from user with a message (with parameters) and converts input into desired type and validates converted input value.
     *
     * @param propertyKey
     * @param inputConverter
     * @param inputValidator
     * @param parameters
     * @param <T>
     * @return
     */
    @Override
    public <T> T getInput(String propertyKey, Function<String, T> inputConverter, Predicate<T> inputValidator, String... parameters) {
        while (true) {
            showMessage(propertyKey, parameters);
            String input = s.next();

            T t = null;
            try {
                t = inputConverter.apply(input);
            } catch (Exception e) {
                log.info("[COMMAND_LINE_GAME_MENU] [GET_INPUT] [CAN_NOT_CONVERT_VALUE]", e.getMessage());
            }

            if (t != null && inputValidator.test(t)) {
                return t;
            } else {
                showMessage("invalid.input");
            }
        }
    }

    /**
     * Prints a new board via system out interface.
     *
     * @param board
     */
    @Override
    public void updateBoard(Board board) {
        System.out.println();
        System.out.println(boardService.printBoard(board));
        System.out.println();
    }

}
