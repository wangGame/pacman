package kw.test.pacmen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import kw.test.pacmen.constant.Constant;
import kw.test.pacmen.manger.MyGameManager;
import kw.test.pacmen.screen.GameScreen;

public class PacmanGame extends Game {
//    创建画笔
    public SpriteBatch batch;
    public Viewport worldView;
    public Viewport gameView;
//    public Batch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        batch = new CpuSpriteBatch();
        worldView = new FitViewport(Constant.WORLDWIDTH,Constant.WORLDHEIGHT);
        gameView = new FitViewport(Constant.GAMEWIDTH,Constant.GAMEHEIGHT);
        setScreen(new GameScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        worldView.update(width,height,true);
        gameView.update(width,height,true);
    }

    public Viewport getGameView() {
        return gameView;
    }

    public Viewport getWorldView() {
        return worldView;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        MyGameManager.getinstance().dispose();
    }
}
