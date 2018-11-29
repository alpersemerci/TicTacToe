package com.alpersemerci.tictactoe.service.menu;

import com.alpersemerci.tictactoe.model.Board;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Game Menu Interface defines menu and game user interface operation requirements.
 * From command line interface to VR glasses these requirements are same. For example;
 *
 * <ul>
 * <li>Display a message to user</li>
 * <li>Display a message to user with parameters</li>
 * <li>Get input from user with a message (with parameters), convert input</li>
 * <li>Get input from user with a message (with parameters), convert input and validate input</li>
 * </ul>
 * <p>
 * Basically GameMenu interface defines contracts for user interaction methods...
 */
public interface GameMenu {

    /**
     * Displays a message to user.
     *
     * @param propertyKey
     */
    void showMessage(String propertyKey);

    /**
     * Displays a message to user with parameters
     *
     * @param propertyKey
     * @param parameters
     */
    void showMessage(String propertyKey, String... parameters);

    /**
     * Gets input from user with a message (with parameters) and converts input into desired type.
     *
     * @param propertyKey
     * @param inputConverter
     * @param parameters
     * @param <T>
     * @return
     */
    <T> T getInput(String propertyKey, Function<String, T> inputConverter, String... parameters);

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
    <T> T getInput(String propertyKey, Function<String, T> inputConverter, Predicate<T> inputValidator, String... parameters);

    /**
     * Updates state of displayed board
     *
     * @param board
     */
    void updateBoard(Board board);
}
