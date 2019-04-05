package core.tetris.game;

import core.tetris.GameInstance;

public class Simulator extends GameInstance {

    public Simulator(boolean[][] board, Tetromino currentPiece) {
        super(board);
        this.currentPiece = currentPiece;
    }

}
