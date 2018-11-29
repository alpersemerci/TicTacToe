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

@Slf4j
@AllArgsConstructor
public class AlphaBetaPruningHeuristicStrategy implements HeuristicStrategy {

    private BoardService boardService;

    private GameService gameService;

    @Override
    public Cell getMove(Game game) throws NoPossibleMovesLeftException {

        Cell move = null;
        try {
            move = alphaBeta(game, Long.MIN_VALUE, Long.MAX_VALUE, null).getBestMove();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (move != null) {
            return move;
        }

        throw new NoPossibleMovesLeftException(" Alpha Beta unable to find a move!");
    }

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

    private Map<Player, Long> evaluateBoard(Board board) {
        Map<Player, Long> playerScoreMap = new HashMap<>();

        Integer boardSize = board.getSize();

        IntStream.range(0, boardSize).forEach(i ->
                IntStream.range(0, boardSize).forEach(j -> {

                    List<Player> verticalPlayers = new ArrayList<>();
                    IntStream.range(0, boardSize).forEach(column -> {
                        if (board.getCells()[i][column] != null) {
                            verticalPlayers.add(board.getCells()[i][column]);
                        }
                    });

                    if (verticalPlayers.stream().distinct().count() == 1) {
                        Player player = verticalPlayers.get(0);
                        playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, verticalPlayers.size()));
                    }

                    List<Player> horizontalPlayers = new ArrayList<>();
                    IntStream.range(0, boardSize).forEach(row -> {
                        if (board.getCells()[row][j] != null) {
                            horizontalPlayers.add(board.getCells()[row][j]);
                        }
                    });

                    if (horizontalPlayers.stream().distinct().count() == 1) {
                        Player player = horizontalPlayers.get(0);
                        playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, horizontalPlayers.size()));
                    }
                })
        );

        //Diagonal board evaluation
        List<Player> diagonalPlayers = new ArrayList<>();

        List<Player> reverseDiagonalPlayers = new ArrayList<>();

        IntStream.range(0, boardSize).forEach(i -> {
            if (board.getCells()[i][i] != null) {
                diagonalPlayers.add(board.getCells()[i][i]);
            }

            if (board.getCells()[i][boardSize - i - 1] != null) {
                reverseDiagonalPlayers.add(board.getCells()[i][boardSize - i - 1]);
            }

        });

        if (diagonalPlayers.stream().distinct().count() == 1) {
            Optional<Player> optionalPlayer = diagonalPlayers.stream().distinct().reduce((player1, player2) -> player1);
            if (optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, diagonalPlayers.size()));
            }
        }

        if (reverseDiagonalPlayers.stream().distinct().count() == 1) {
            Optional<Player> optionalPlayer = reverseDiagonalPlayers.stream().distinct().reduce((player1, player2) -> player1);
            if (optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                playerScoreMap.put(player, playerScoreMap.getOrDefault(player, 0L) + (long) Math.pow(10, reverseDiagonalPlayers.size()));
            }
        }

        playerScoreMap.entrySet().forEach(entry -> {
            if (!PlayerType.AI.equals(entry.getKey().getType())) {
                playerScoreMap.put(entry.getKey(), entry.getValue() * -1L);
            }
        });

        return playerScoreMap;
    }

    private Game cloneGame(Game game) {
        Game cloneGame = new Game(game.getBoard().getSize());
        cloneGame.setCurrentTurn(game.getCurrentTurn());
        cloneGame.setBoard(cloneBoard(game.getBoard()));
        cloneGame.setPlayerList(game.getPlayerList());
        return cloneGame;
    }

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