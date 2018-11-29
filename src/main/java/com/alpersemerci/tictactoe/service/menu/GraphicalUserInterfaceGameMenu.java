package com.alpersemerci.tictactoe.service.menu;

import com.alpersemerci.tictactoe.model.Board;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * CommandLineInterfaceGameMenu is an implementation of GameMenu interface for graphical user interface experience.
 */
public class GraphicalUserInterfaceGameMenu implements GameMenu {

    @Override
    public void showMessage(String propertyKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showMessage(String propertyKey, String... parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getInput(String propertyKey, Function<String, T> inputConverter, String...parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getInput(String propertyKey, Function<String, T> inputConverter, Predicate<T> inputValidator, String...parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateBoard(Board board) {
        throw new UnsupportedOperationException();
    }
}
