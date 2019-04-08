package core.tetris.simulator;

import java.util.HashSet;

public class TetrisSimulator {

    private HashSet<SimulatedState> inSearch;
    private HashSet<SimulatedState> resolved;

    public TetrisSimulator() {
        inSearch = new HashSet<>();
        resolved = new HashSet<>();
    }

}
