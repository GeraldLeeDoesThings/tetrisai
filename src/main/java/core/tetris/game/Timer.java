package core.tetris.game;

public class Timer {

    private final long DELAY;
    private long lastCheck;

    public Timer(long delay) {
        DELAY = delay;
        lastCheck = System.currentTimeMillis();
    }

    public synchronized long reset() {
        lastCheck = System.currentTimeMillis();
        return lastCheck;
    }

    public synchronized boolean check() {
        long target = lastCheck + DELAY;
        boolean output = target <= System.currentTimeMillis();
        if (output) {
            reset();
        }
        return (output);
    }

}
