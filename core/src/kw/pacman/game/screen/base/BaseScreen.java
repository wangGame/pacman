package kw.pacman.game.screen.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import kw.pacman.game.constant.Constant;

public abstract class BaseScreen implements Screen {
    protected InputMultiplexer inputMultiplexer;
    @Override
    public void show() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        initData();
        initView();
        initListener();
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();


    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    protected void enterScreen(BaseScreen screen){
        Constant.game.setScreen(screen);
    }
    public <T extends Actor> T findActor(Group group, String name){
        return group.findActor(name);
    }
}
