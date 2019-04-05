package core.tetris.game;

import core.tetris.GameInstance;
import core.tetris.GridDisplay;

public class Tetris extends GameInstance {

    private Thread ticker;
    private GridDisplay display;

    public Tetris() {

        ticker = new Thread(() -> {

        });

    }

}
