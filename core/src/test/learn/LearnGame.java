package test.learn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;

import kw.pacman.game.constant.Constant;

public class LearnGame extends Game {
    public SpriteBatch batch;
    private OrthographicCamera camera;
    @Override
    public void create() {
        camera  = Constant.camera = new OrthographicCamera();
        camera.translate(19.0F/2,23.0F/2);
        camera.update();
        FillViewport fillViewport = new FillViewport(19,23,camera);
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        GameManager.instance.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
