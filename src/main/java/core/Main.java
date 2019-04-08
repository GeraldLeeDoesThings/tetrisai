package core;

import core.tetris.controllers.HumanKeyboardPlayer;
import core.tetris.game.Tetris;

public class Main {

    public static void main(String[] args) {
        Tetris t = new Tetris();
        new HumanKeyboardPlayer(t);
    }

}
