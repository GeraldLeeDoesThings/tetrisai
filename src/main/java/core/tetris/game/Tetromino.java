package core.tetris.game;

import core.tetris.TetrisConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
//4 + hold
public class Tetromino implements TetrominoStates, TetrisConstants {

    public enum Type {
        I(Color.CYAN), J(Color.BLUE), L(Color.ORANGE), O(Color.YELLOW), S(Color.GREEN), T(new Color(102, 0, 153)), Z(Color.RED);

        private final Color COLOR;

        public Color getColor() {return COLOR;}

        Type(Color c) {
            this.COLOR = c;
        }

    }

    public enum Rotate {
        LEFT,
        RIGHT;
    }

    public enum State {
        SPAWN(0),
        RIGHT(1),
        TWO(2),
        LEFT(3);

        private final int VALUE;

        State(int value) {
            VALUE = value;
        }

        public int getValue() {
            return VALUE;
        }

    }

    private final Type type;
    private State state;
    private final Point origin;

    public Tetromino(@NotNull Type type) {
        this.type = type;
        state = State.SPAWN;
        origin = new Point(3, (type.equals(Type.I) ? 21 : 22));
    }

    @Contract(pure = true)
    public int[][] getPositions() {
        return getPositions(state.VALUE);
    }

    @Contract(pure = true)
    public int[][] getPositions(int rotationState) {
        int[][] data;
        switch (type) {
            case O:
                data = O_STATES[0];
                break;
            case I:
                data = I_STATES[rotationState];
                break;
            case J:
                data = J_STATES[rotationState];
                break;
            case L:
                data = L_STATES[rotationState];
                break;
            case S:
                data = S_STATES[rotationState];
                break;
            case T:
                data = T_STATES[rotationState];
                break;
            case Z:
                data = Z_STATES[rotationState];
                break;
            default:
                data = null;
        }
        return shiftAllPoints(data, origin.x, origin.y);
    }

    @Contract(pure = true)
    protected static int[][] getTestShifts(@NotNull Tetromino tetromino, @NotNull Rotate rotate) {
        switch (tetromino.getType()) {
            case J:
            case L:
            case S:
            case T:
            case Z:
                return getTestShifts(tetromino.getState(), rotate, false);
            case I:
                return getTestShifts(tetromino.getState(), rotate, true);
            case O:
                    return new int[][]{{0,0}};
        }
        return null;
    }

    @Contract(pure = true)
    protected static State getResultantState(@NotNull State initial, @NotNull Rotate rotation) {
        switch (initial) {
            case SPAWN:
                return (rotation.equals(Rotate.RIGHT)) ? State.RIGHT : State.LEFT;
            case LEFT:
                return (rotation.equals(Rotate.RIGHT)) ? State.SPAWN : State.TWO;
            case RIGHT:
                return (rotation.equals(Rotate.RIGHT)) ? State.TWO : State.SPAWN;
            case TWO:
                return  (rotation.equals(Rotate.RIGHT)) ? State.LEFT : State.RIGHT;
        }
        return State.TWO;
    }

    @Contract(pure = true)
    protected static int getStateShiftCode(@NotNull State initial, @NotNull State end) {
        switch (initial) {
            case SPAWN:
                switch (end) {
                    case LEFT:
                        return 7;
                    case RIGHT:
                        return 0;
                }
            case LEFT:
                switch (end) {
                    case TWO:
                        return 5;
                    case SPAWN:
                        return 6;
                }
            case RIGHT:
                switch (end) {
                    case TWO:
                        return 2;
                    case SPAWN:
                        return 1;
                }
            default:
                switch (end) {
                    case LEFT:
                        return 4;
                    case RIGHT:
                        return 3;
                }
                return -1;
        }
    }

    @Contract(pure = true)
    protected static int[][] getTestShifts(@NotNull State state,@NotNull Rotate rotate, boolean isI) {
        switch (getStateShiftCode(state, getResultantState(state, rotate))) {
            case 0:
                return (isI) ? new int[][]{{0,0},{-1,0},{-1,1},{0,-2},{-1,-2}}
                        : new int[][]{{0,0},{-2,0},{1,0},{-2,-1},{1,2}};
            case 1:
                return (isI) ? new int[][]{{0,0},{1,0},{1,-1},{0,2},{1,2}}
                        : new int[][]{{0,0},{2,0},{-1,0},{2,1},{-1,-2}};
            case 2:
                return (isI) ? new int[][]{{0,0},{1,0},{1,-1},{0,2},{1,2}}
                        : new int[][]{{0,0},{-1,0},{2,0},{-1,2},{2,-1}};
            case 3:
                return (isI) ? new int[][]{{0,0},{-1,0},{-1,1},{0,-2},{-1,-2}}
                        : new int[][]{{0,0},{1,0},{-2,0},{1,-2},{-2,1}};
            case 4:
                return (isI) ? new int[][]{{0,0},{1,0},{1,1},{0,-2},{1,-2}}
                        : new int[][]{{0,0},{2,0},{-1,0},{2,1},{-1,-2}};
            case 5:
                return (isI) ? new int[][]{{0,0},{-1,0},{-1,-1},{0,2},{-1,2}}
                        : new int[][]{{0,0},{-2,0},{1,0},{-2,-1},{1,2}};
            case 6:
                return (isI) ? new int[][]{{0,0},{-1,0},{-1,-1},{0,-2},{-1,2}}
                        : new int[][]{{0,0},{1,0},{-2,0},{1,-2},{-2,1}};
            case 7:
                return (isI) ? new int[][]{{0,0},{1,0},{1,1},{0,-2},{1,-2}}
                        : new int[][]{{0,0},{-1,0},{2,0},{-1,2},{2,-1}};
        }
        return null;
    }

    public Type getType() {
        return type;
    }

    public State getState() {
        return state;
    }

    @Contract(pure = true)
    public static int[][] shiftAllPoints(@NotNull int[][] data, int xShift, int yShift) {
        int[][] shifter = new int[data.length][];
        int pointNum = 0;
        for (int[] point : data) {
            shifter[pointNum] = new int[]{point[0] + xShift, point[1] + yShift};
            pointNum++;
        }
        return shifter;
    }

    public static boolean doesCollide(@NotNull boolean[][] board, @NotNull int[][] data, int xShift, int yShift) {
        return doesCollide(board, shiftAllPoints(data, xShift, yShift));
    }

    @Contract(pure = true)
    public static boolean doesCollide(@NotNull boolean[][] board, @NotNull int[][] data) {
        for (int[] point : data) {
            int x = point[0];
            int y = point[1];
            if (x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT + BOARD_BUFFER) {
                if (board[x][y]) {
                    return true;
                }
            }
            else {
                return true;
            }
        }
        return false;
    }

    public void attemptRotation(@NotNull boolean[][] board, @NotNull Rotate direction) {
        boolean didRotate = false;
        int[][] data = getPositions(getResultantState(state, direction).VALUE);
        int[][] testShifts = getTestShifts(state, direction, type.equals(Type.I));
        int[] acceptedShift = null;
        for (int[] pointShift : testShifts) {
            int[][] shiftedPoints = shiftAllPoints(data, pointShift[0], pointShift[1]);
            if (!doesCollide(board, shiftedPoints)) {
                didRotate = true;
            }
            if (didRotate) {
                acceptedShift = pointShift;
                break;
            }
        }
        if (acceptedShift != null) {
            state = getResultantState(state, direction);
            origin.x += acceptedShift[0];
            origin.y += acceptedShift[1];
        }
    }

    public boolean softDrop(@NotNull boolean[][] validPositions) {
        if (doesCollide(validPositions, getPositions(), 0, -1)) {
            return true;
        }
        origin.y--;
        return false;
    }

    public void hardDrop(@NotNull boolean[][] validPositions) {
        while (!softDrop(validPositions));
    }

    public boolean tryLeftShift(@NotNull boolean[][] board) {
        if (!doesCollide(board, getPositions(), -1, 0)) {
            origin.x--;
            return true;
        }
        return false;
    }

    public boolean tryRightShift(@NotNull boolean[][] board) {
        if (!doesCollide(board, getPositions(), 1, 0)) {
            origin.x++;
            return true;
        }
        return false;
    }

    public boolean wouldFuseOnSoftDrop(@NotNull boolean[][] board) {
        return doesCollide(board, getPositions(), 0, -1);
    }

}
