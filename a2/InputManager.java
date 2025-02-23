package a2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class InputManager implements KeyListener{
    private final boolean[] keys = new boolean[256];

    public InputManager(JFrame frame) {
        frame.addKeyListener(this);
        frame.setFocusable(true);
    }

    public boolean isKeyPressed(int keyCode) {
        return keys[keyCode];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >=0 && keyCode < keys.length) {
            keys[keyCode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode >=0 && keyCode < keys.length) {
            keys[keyCode] = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
}
