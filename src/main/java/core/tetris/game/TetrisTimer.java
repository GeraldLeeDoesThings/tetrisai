package core.tetris.game;

public class TetrisTimer {

    public static final long gravityDelay = 500L;
    public static final long softDropDelay = 50L;
    public static final long fuseDelay = 500L;

    private Timer gravityDropTimer, softDropTimer, fuseTimer;

    public TetrisTimer() {
        gravityDropTimer = new Timer(gravityDelay);
        softDropTimer = new Timer(softDropDelay);
        fuseTimer = new Timer(fuseDelay);
    }

    public void resetAll() {
        gravityDropTimer.reset();
        softDropTimer.reset();
        fuseTimer.reset();
    }

    public void resetDropTimer() {
        softDropTimer.reset();
    }

    public void resetGravityTimer() {
        gravityDropTimer.reset();
    }

    public void resetFuseTimer() {
        fuseTimer.reset();
    }

    public boolean checkFuseTimer() {
        return fuseTimer.check();
    }

    public boolean checkGravityTimer() {
        return gravityDropTimer.check();
    }

    public boolean checkDropTimer() {
        return softDropTimer.check();
    }

}
