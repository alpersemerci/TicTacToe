package com.alpersemerci.tictactoe.service.heuristics;

import com.alpersemerci.tictactoe.model.Game;
import com.alpersemerci.tictactoe.model.Player;
import com.alpersemerci.tictactoe.model.PlayerType;
import com.alpersemerci.tictactoe.service.game.BoardService;
import com.alpersemerci.tictactoe.service.game.GameService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public abstract class AbstractHeuristicStrategyTest {

    protected BoardService boardService;

    protected GameService gameService;

    protected HeuristicStrategy strategy;

    protected Game game;

    @Before
    public void setUp() throws Exception {
        boardService = new BoardService();
        gameService = new GameService(boardService);
        game = gameService.createGame(10);
        gameService.addPlayerToGame(game, new Player(PlayerType.AI, 'X'));
        gameService.addPlayerToGame(game, new Player(PlayerType.AI, 'O'));
        gameService.addPlayerToGame(game, new Player(PlayerType.AI, 'C'));

    }

}
