package core.tetris.game;

import org.jetbrains.annotations.Contract;

public class SynchronizedTetrisAction {

    private TetrisAction action;

    public SynchronizedTetrisAction() {
        action = null;
    }

    public SynchronizedTetrisAction(TetrisAction action) {
        this.action = action;
    }

    public synchronized TetrisAction getAction() {
        TetrisAction clone = action;
        action = null;
        return clone;
    }

    public synchronized void setAction(TetrisAction action) {
        this.action = action;
    }

}
