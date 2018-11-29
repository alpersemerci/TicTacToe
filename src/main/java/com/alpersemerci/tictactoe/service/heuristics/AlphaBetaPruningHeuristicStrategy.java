package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.exception.InvalidMoveException;
import com.alpersemerci.tictactoe.exception.NoPlayerInGameException;
import com.alpersemerci.tictactoe.exception.NoPossibleMovesLeftException;
import com.alpersemerci.tictactoe.model.*;
import com.alpersemerci.tictactoe.service.game.BoardService;
import com.alpersemerci.tictactoe.service.game.GameService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

/**
 * AlphaBetaPruningHeuristicStrategy is an implementation of HeuristicStrategy.
 * Basically an implementation of alpha beta pruning search algorithm for multi player tic-tac-toe game.
 * <p>
 * https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
 */
@Slf4j
@AllArgsConstructor
public class AlphaBetaPruningHeuristicStrategy implements HeuristicStrategy {

    private BoardService boardService;

    private GameService gameService;

    @Override
    public Cell getMove(Game game) throws NoPossibleMovesLeftException {

        Cell move = null;

        try {
            //Start to build game tree
            move = alphaBeta(game, Long.MIN_VALUE, Long.MAX_VALUE, null).getBestMove();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (move != null) {
            return move;
        } else {
            //This is our fallback logic. If our algorithm is unsuccessful on finding
            //an available move despite of empty cells return first available cell.
            //Also it means our algorithm doesn't work properly
            if (!boardService.getAvailableCells(game.getBoard()).isEmpty()) {
                log.warn("Alpha Beta unable to find a move despite of empty cells. Please check implementation.");
                return boardService.getAvailableCells(game.getBoard()).get(0);
            }
        }

        throw new NoPossibleMovesLeftException("Alpha Beta unable to find a move!");
    }

    /**
     * Play min or max game by current player.
     *
     * @param game
     * @param alpha
     * @param beta
     * @param cell
     * @return score and best move
     * @throws NoPlayerInGameException
     * @throws InvalidMoveException
     */
    private AlphaBetaResult alphaBeta(Game game, Long alpha, Long beta, Cell cell) throws NoPlayerInGameException, InvalidMoveException {
        Player currentPlayer = gameService.getCurrentPlayer(game);

        if (gameService.isGameOver(game)) {
            return new AlphaBetaResult(score(game, currentPlayer), cell);
        }

        if (PlayerType.AI.equals(currentPlayer.getType())) {
            return getMax(game, alpha, beta);
        } else {
            return getMin(game, alpha, beta);
        }
    }

    /**
     * Play max game and get results.
     *
     * @param game
     * @param alpha
     * @param beta
     * @return score and best move for max play
     * @throws InvalidMoveException
     * @throws NoPlayerInGameException
     */
    private AlphaBetaResult getMax(Game game, Long alpha, Long beta) throws InvalidMoveException, NoPlayerInGameException {

        AlphaBetaResult result = new AlphaBetaResult();

        for (Cell cell : boardService.getAvailableCells(game.getBoard())) {
            Game newGame = cloneGame(game);
            gameService.play(newGame, cell);

            AlphaBetaResult abResult = alphaBeta(newGame, alpha, beta, cell);
            Long score = abResult.getScore();
            result.setBestMove(abResult.getBestMove() != null ? abResult.getBestMove() : cell);
            result.setScore(score);

            if (score > alpha) {
                alpha = score;
                result.setBestMove(cell);
                result.setScore(alpha);
            }

            if (alpha >= beta) {
                break;
            }

        }

        return result;
    }

    /**
     * Play min game and get results.
     *
     * @param game
     * @param alpha
     * @param beta
     * @return score and best move for max play
     * @throws InvalidMoveException
     * @throws NoPlayerInGameException
     */
    private AlphaBetaResult getMin(Game game, Long alpha, Long beta) throws InvalidMoveException, NoPlayerInGameException {

        AlphaBetaResult result = new AlphaBetaResult();

        for (Cell cell : boardService.getAvailableCells(game.getBoard())) {
            Game newGame = cloneGame(game);
            gameService.play(newGame, cell);

            AlphaBetaResult abResult = alphaBeta(newGame, alpha, beta, cell);
            Long score = abResult.getScore();
            result.setBestMove(abResult.getBestMove() != null ? abResult.getBestMove() : cell);
            result.setScore(score);

            if (score < beta) {
                beta = score;
                result.setBestMove(cell);
                result.setScore(beta);
            }

            if (alpha >= beta) {
                break;
            }

        }

        return result;
    }

    /**
     * Get score from full board evaluation
     *
     * @param game
     * @param player
     * @return
     */
    private Long score(Game game, Player player) {

        Map<Player, Long> scoreMap = evaluateBoard(game.getBoard());

        Optional<Long> score;
        if (!PlayerType.AI.equals(player.getType())) {
            score = scoreMap.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue)).map(Map.Entry::getValue);
        } else {
            score = scoreMap.entrySet().stream().min(Comparator.comparingLong(Map.Entry::getValue)).map(Map.Entry::getValue);
        }

        return score.orElse(0L);
    }

    /**
     * Evaluates given board. Simply travels whole board and calculates player scores in diagonally, reverse-diagonally,
     * vertically and horizontally. If cells are occupied by only one player (that means possible victory)
     * player score updated with multiples of 10. For example on 3x3 board if PLayer1 occupies 3 cells diagonally it is
     * score will be updated as 10^3.
     *
     * @param board
     * @return
     */
    private Map<Player, Long> evaluateBoard(Board board) {
        Map<Player, Long> playerScoreMap = new HashMap<>();

        Integer boardSize = board.getSize();

        //vertically and horizontally board evaluation
        IntStream.range(0, boardSize).forEach(i ->
                IntStream.range(0, boardSize).forEach(j -> {

                    //Collect vertical players
                    List<Player> verticalPlayers = new ArrayList<>();
                    IntStream.range(0, boardSize).forEach(column -> {
                        if (board.getCells()[i][column] != null) {
                            verticalPlayers.add(board.getCells()[i][column]);
                        }
                    });

                    //If only one player occupies vertical cells calculate score
                    if (verticalPlayers.stream().distinct().count() == 1) {
                        Player player = verticalPlayers.get(0);
                        playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, verticalPlayers.size()));
                    }

                    //Collect horizontal players
                    List<Player> horizontalPlayers = new ArrayList<>();
                    IntStream.range(0, boardSize).forEach(row -> {
                        if (board.getCells()[row][j] != null) {
                            horizontalPlayers.add(board.getCells()[row][j]);
                        }
                    });

                    //If only one player occupies horizontal cells calculate score
                    if (horizontalPlayers.stream().distinct().count() == 1) {
                        Player player = horizontalPlayers.get(0);
                        playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, horizontalPlayers.size()));
                    }
                })
        );

        //Diagonal board evaluation
        List<Player> diagonalPlayers = new ArrayList<>();

        List<Player> reverseDiagonalPlayers = new ArrayList<>();

        //Collect diagonal and reverse diagonal players
        IntStream.range(0, boardSize).forEach(i -> {
            if (board.getCells()[i][i] != null) {
                diagonalPlayers.add(board.getCells()[i][i]);
            }

            if (board.getCells()[i][boardSize - i - 1] != null) {
                reverseDiagonalPlayers.add(board.getCells()[i][boardSize - i - 1]);
            }

        });

        //If only one player occupies diagonal cells calculate score
        if (diagonalPlayers.stream().distinct().count() == 1) {
            Optional<Player> optionalPlayer = diagonalPlayers.stream().distinct().reduce((player1, player2) -> player1);
            if (optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, diagonalPlayers.size()));
            }
        }

        //If only one player occupies reverse diagonal cells calculate score
        if (reverseDiagonalPlayers.stream().distinct().count() == 1) {
            Optional<Player> optionalPlayer = reverseDiagonalPlayers.stream().distinct().reduce((player1, player2) -> player1);
            if (optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, reverseDiagonalPlayers.size()));
            }
        }

        //Multiply by -1 human scores in order to play min game.
        playerScoreMap.entrySet().forEach(entry -> {
            if (!PlayerType.AI.equals(entry.getKey().getType())) {
                playerScoreMap.put(entry.getKey(), entry.getValue() * -1L);
            }
        });

        return playerScoreMap;
    }

    /**
     * Clones game for building game tree.
     *
     * @param game
     * @return
     */
    private Game cloneGame(Game game) {
        Game cloneGame = new Game(game.getBoard().getSize());
        cloneGame.setCurrentTurn(game.getCurrentTurn());
        cloneGame.setBoard(cloneBoard(game.getBoard()));
        cloneGame.setPlayerList(game.getPlayerList());
        return cloneGame;
    }

    /**
     * Clones board for building game tree.
     * @param board
     * @return
     */
    private Board cloneBoard(Board board) {
        Board cloneBoard = new Board(board.getSize());

        IntStream.range(0, board.getSize()).forEach(i ->
                IntStream.range(0, board.getSize()).forEach(j ->
                        cloneBoard.getCells()[i][j] = board.getCells()[i][j]
                )
        );

        return cloneBoard;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class AlphaBetaResult {

        private Long score;

        private Cell bestMove;
    }
}