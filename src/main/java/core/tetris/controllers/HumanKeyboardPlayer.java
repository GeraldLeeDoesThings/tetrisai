package core.tetris.controllers;

import core.tetris.game.Tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HumanKeyboardPlayer implements KeyListener {

    private final Tetris tetris;

    public HumanKeyboardPlayer(Tetris tetris) {
        this.tetris = tetris;
        tetris.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                tetris.hardDropTetromino();
                break;
            case KeyEvent.VK_W:
                tetris.hold();
                break;
            case KeyEvent.VK_A:
                tetris.moveTetrominoLeft();
                break;
            case KeyEvent.VK_D:
                tetris.moveTetrominoRight();
                break;
            case KeyEvent.VK_Q:
                tetris.rotateTetrominoLeft();
                break;
            case KeyEvent.VK_E:
                tetris.rotateTetrominoRight();
                break;
            case KeyEvent.VK_S:
                tetris.startSoftDrop();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            tetris.stopSoftDrop();
        }
    }

}
