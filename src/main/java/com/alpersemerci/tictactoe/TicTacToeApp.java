package com.alpersemerci.tictactoe;

import com.alpersemerci.tictactoe.exception.*;
import com.alpersemerci.tictactoe.model.Cell;
import com.alpersemerci.tictactoe.model.Game;
import com.alpersemerci.tictactoe.model.Player;
import com.alpersemerci.tictactoe.model.PlayerType;
import com.alpersemerci.tictactoe.service.game.BoardService;
import com.alpersemerci.tictactoe.service.game.GameService;
import com.alpersemerci.tictactoe.service.heuristics.HeuristicStrategy;
import com.alpersemerci.tictactoe.service.heuristics.StayCloseToCenterHeuristicStrategy;
import com.alpersemerci.tictactoe.service.menu.CommandLineInterfaceGameMenu;
import com.alpersemerci.tictactoe.service.menu.GameMenu;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TicTacToeApp {

    private static BoardService boardService = new BoardService();

    private static GameService gameService = new GameService(boardService);

    private static GameMenu menu;

    private static HeuristicStrategy strategy;

    private static Integer numberOfPlayers;

    private static Integer boardSize;

    private static Game game;

    public static void main(String[] args) throws InvalidBoardSizeException, GameAlreadyStartedException, NoPlayerInGameException, InvalidMoveException, InterruptedException {

        //Game setup and game play flow
        selectUserInterface();

        //Show game banner
        greeting();

        //Get number of players from user
        getNumberOfPlayers();

        //Get board size from user
        getBoardSize();

        //Initialize game with given user inputs
        createGame();

        //Get player details (like player type and icon) from user
        initializePlayers();

        //Decide ai heuristic algorithm
        decideAiGameStrategy();

        //and action...
        startGame();

    }

    /**
     * Applies selected user interface (Command line User Interface or Graphical User Interface) to the game.
     */
    public static void selectUserInterface() {
        // Unfortunately GraphicalUserInterfaceGameMenu not implemented yet.
        menu = new CommandLineInterfaceGameMenu();
    }

    /**
     * Displays game banner
     */
    public static void greeting() {
        menu.showMessage("banner");
    }

    /**
     * Gets & validates number of players from user input
     */
    public static void getNumberOfPlayers() {
        numberOfPlayers = menu.getInput("player.count.input", Integer::parseInt, i -> i > 1 && i < 4);
    }

    /**
     * Gets & validates board size from user input
     */
    public static void getBoardSize() {
        boardSize = menu.getInput("board.size.input", Integer::parseInt, i -> i > 2 && i < 11);
    }

    /**
     * Initializes game from user inputs
     *
     * @throws InvalidBoardSizeException
     */
    public static void createGame() throws InvalidBoardSizeException {
        game = gameService.createGame(boardSize);
    }

    /**
     * Initializes players from user inputs
     *
     * @throws GameAlreadyStartedException
     */
    public static void initializePlayers() throws GameAlreadyStartedException {
        List<Character> symbols = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            Character symbol = menu.getInput("player.symbol.input", s -> s.charAt(0), c -> c != null && !symbols.contains(c), i + "");
            PlayerType playerType = menu.getInput("player.type.input", s -> "1".equals(s) ? PlayerType.HUMAN : PlayerType.AI);
            Player player = new Player(playerType, symbol);
            symbols.add(symbol);
            gameService.addPlayerToGame(game, player);
            menu.showMessage("player.created.message", player.toString());
        }
    }

    /**
     * Decides AI strategy. Please @see HeuristicStrategy implementations for different AI strategies.
     */
    public static void decideAiGameStrategy() {
        strategy = new StayCloseToCenterHeuristicStrategy(boardService);
    }


    /**
     * Simply starts game with given game setup. If it is human player's turn, gets input from user
     * else gets move from AI strategy service with selected strategy implementation.
     *
     * @throws NoPlayerInGameException
     * @throws InvalidMoveException
     * @throws InterruptedException
     */
    public static void startGame() throws NoPlayerInGameException, InvalidMoveException, InterruptedException {
        while (true) {
            Player player = gameService.getCurrentPlayer(game);

            menu.showMessage("player.turn.message", player.toString());
            menu.updateBoard(game.getBoard());

            switch (player.getType()) {
                case HUMAN:

                    Cell cell = menu.getInput("player.move.input", s -> {
                        String[] coordinates = s.split(",");
                        Integer row = Integer.valueOf(coordinates[0].trim());
                        Integer column = Integer.valueOf(coordinates[1].trim());
                        return new Cell(row, column);
                    }, c -> gameService.isCellAvailableForPlay(game, c));

                    if (gameService.play(game, cell)) {
                        menu.showMessage("player.wins", player.toString());
                        return;
                    }

                    break;
                case AI:
                    menu.showMessage("ai.thinking.message");
                    Thread.sleep(2000);
                    Cell move;

                    try {
                        move = strategy.getMove(game);
                        if (gameService.play(game, move)) {
                            menu.showMessage("player.wins", player.toString());
                            return;
                        }
                    } catch (NoPossibleMovesLeftException noPossibleMovesLeftException) {
                        log.error(noPossibleMovesLeftException.getMessage());
                    }

                    break;
            }

            menu.updateBoard(game.getBoard());

            if (gameService.isGameOver(game)) {
                menu.showMessage("game.over", player.toString());
                return;
            }

        }
    }

    public static void setMenu(GameMenu menu) {
        TicTacToeApp.menu = menu;
    }
}
