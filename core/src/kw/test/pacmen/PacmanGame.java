package kw.test.pacmen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import kw.test.pacmen.manger.MyGameManager;
import kw.test.pacmen.screen.GameScreen;

public class PacmanGame extends Game {
//    创建画笔
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        MyGameManager.getinstance().dispose();
    }
}
