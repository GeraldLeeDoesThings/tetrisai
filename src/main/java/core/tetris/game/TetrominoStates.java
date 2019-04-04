package core.tetris.game;

public interface TetrominoStates {

    int[][][] I_STATES = {
            {{0,-1}, {1,-1}, {2,-1}, {3,-1}},
            {{2,0}, {2,-1}, {2,-2}, {2,-3}},
            {{0,-2}, {1,-2}, {2,-2}, {3,-2}},
            {{1,0}, {1,-1}, {1,-2}, {1,-3}}
            };

    int[][][] J_STATES = {
            {{0,0}, {0,-1}, {1,-1}, {2,-1}},
            {{1,0}, {2,0}, {1,-1}, {1,-2}},
            {{0,-1}, {1,-1}, {2,-1}, {2,-2}},
            {{0,-2}, {1,-2}, {1,-1}, {1,-0}}
    };

    int[][][] L_STATES = {
            {{2,0}, {0,-1}, {1,-1}, {2,-1}},
            {{1,0}, {2,-2}, {1,-1}, {1,-2}},
            {{0,-1}, {1,-1}, {2,-1}, {0,-2}},
            {{0,0}, {1,-2}, {1,-1}, {1,0}}
    };

    int[][][] O_STATES = {
            {{1,0}, {2,0}, {1,-1}, {2,-1}}
    };

    int[][][] S_STATES = {
            {{0,-1}, {1,-1}, {1,0}, {2,0}},
            {{1,0}, {1,-1}, {2,-1}, {2,-2}},
            {{0,-2}, {1,-2}, {1,-1}, {2,-1}},
            {{0,0}, {0,-1}, {1,-1}, {1,-2}}
    };

    int[][][] T_STATES = {
            {{0,-1}, {1,0}, {2,-1}, {1,-1}},
            {{1,0}, {1,-2}, {2,-1}, {1,-1}},
            {{0,-1}, {2,-1}, {1,-2}, {1,-1}},
            {{0,-1}, {1,0}, {1,-2}, {1,-1}}
    };

    int[][][] Z_STATES = {
            {{0,0}, {1,0}, {1,-1}, {2,-1}},
            {{1,-2}, {1,-1}, {2,-1}, {2,0}},
            {{0,-1}, {1,-1}, {1,-2}, {2,-2}},
            {{0,-2}, {0,-1}, {1,-1}, {1,0}}
    };

}
