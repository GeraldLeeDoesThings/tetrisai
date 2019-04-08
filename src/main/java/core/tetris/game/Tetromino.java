package core.tetris.game;

import java.awt.*;
//4 + hold
public class Tetromino implements TetrominoStates {

    public enum Type {
        I(Color.CYAN), J(Color.BLUE), L(Color.ORANGE), O(Color.YELLOW), S(Color.GREEN), T(Color.MAGENTA), Z(Color.RED);

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

    public Tetromino(Type type) {
        this.type = type;
        state = State.SPAWN;
        origin = new Point(3, (type.equals(Type.I) ? 21 : 22));
    }

    public int[][] getPositions() {
        int[][] data;
        switch (type) {
            case O:
                data = O_STATES[0];
            case I:
                data = I_STATES[state.VALUE];
            case J:
                data = J_STATES[state.VALUE];
            case L:
                data = L_STATES[state.VALUE];
            case S:
                data = S_STATES[state.VALUE];
            case T:
                data = T_STATES[state.VALUE];
            case Z:
                data = Z_STATES[state.VALUE];
                default:
                    data = null;
        }
        return shiftAllPoints(data, origin.x, origin.y);
    }

    protected static int[][] getTestShifts(Tetromino tetromino, Rotate rotate) {
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

    protected static State getResultantState(State initial, Rotate rotation) {
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
        return null;
    }

    protected static int getStateShiftCode(State initial, State end) {
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

    protected static int[][] getTestShifts(State state, Rotate rotate, boolean isI) {
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

    public static int[][] shiftAllPoints(int[][] data, int xShift, int yShift) {
        int[][] shifter = new int[data.length][];
        int pointNum = 0;
        for (int[] point : data) {
            shifter[pointNum] = new int[]{point[0] + xShift, point[1] + yShift};
        }
        return shifter;
    }

    public static boolean doesCollide(boolean[][] board, int[][] data, int xShift, int yShift) {
        return doesCollide(board, shiftAllPoints(data, xShift, yShift));
    }

    public static boolean doesCollide(boolean[][] board, int[][] data) {
        for (int[] point : data) {
            int x = point[0];
            int y = point[1];
            if (x >= 0 && x < 10 && y >= 0 && y < 23) {
                if (board[x][y]) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean attemptRotation(boolean[][] board, Rotate direction, boolean canFloorKick) {
        boolean didRotate = false;
        boolean didFloorKick = false;
        int[][] data = getPositions();
        int[][] testShifts = getTestShifts(state, direction, type.equals(Type.I));
        int[] acceptedShift = null;
        for (int[] pointShift : testShifts) {
            int[][] shiftedPoints = shiftAllPoints(data, pointShift[0], pointShift[1]);
            if (!doesCollide(board, shiftedPoints)) {
                if ((pointShift[1] <= 0) || canFloorKick) {
                    if (pointShift[1] > 0) {
                        didFloorKick = true;
                    }
                    didRotate = true;
                }
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
        return didFloorKick;
    }

    public boolean softDrop(boolean[][] validPositions) {
        if (doesCollide(validPositions, getPositions(), 0, -1)) {
            return true;
        }
        origin.y--;
        return false;
    }

    public void hardDrop(boolean[][] validPositions) {
        int drop = 0;
        while (!doesCollide(validPositions, getPositions(), 0, drop - 1)) {
            drop--;
        }
        origin.y += drop;
    }

    public boolean tryLeftShift(boolean[][] board) {
        if (!doesCollide(board, getPositions(), -1, 0)) {
            origin.x--;
            return true;
        }
        return false;
    }

    public boolean tryRightShift(boolean[][] board) {
        if (!doesCollide(board, getPositions(), 1, 0)) {
            origin.x++;
            return true;
        }
        return false;
    }

}
