package kw.pacman.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import kw.pacman.game.constant.Constant;
import kw.pacman.game.screen.MainScreen;

public class Pacman extends Game {
    private OrthographicCamera camera;
    @Override
    public void create() {
        //正交
        camera  = Constant.camera = new OrthographicCamera();
//        camera.translate(19.0F/2,23.0F/2);
//        camera.update();
        camera.translate(19,23);
        camera.update(true);
        //转换
        camera.setToOrtho(false,19,23);
        Constant.batch = new SpriteBatch();
        Constant.batch.setProjectionMatrix(camera.combined);
        Constant.game = this;
        Constant.assetManager = new AssetManager();
        Constant.world = new World(Vector2.Zero, true);
        Constant.box2DDebugRenderer = new Box2DDebugRenderer();
        Constant.viewport = new ExtendViewport(720,1280);
        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        setScreen(new MainScreen());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Constant.viewport.update(width,height);
        Constant.viewport.apply();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
