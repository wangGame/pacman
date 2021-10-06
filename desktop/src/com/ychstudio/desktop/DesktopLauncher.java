package com.ychstudio.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import kw.test.pacmen.PacmanGame;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 528; // 570
        config.height = 590; // 690
        new LwjglApplication(new PacmanGame(), config);
    }
}
