package com.ychstudio.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import kw.test.pacmen.PacmanGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 0;
        config.y = 0;
        config.width = 570; // 570
        config.height = 690; // 690
        new LwjglApplication(new PacmanGame(), config);
    }
}
