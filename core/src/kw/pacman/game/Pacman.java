package kw.pacman.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.text.View;

import kw.pacman.game.asset.Asset;
import kw.pacman.game.constant.Constant;
import kw.pacman.game.screen.LoadingScreen;

public class Pacman extends Game {
    private Viewport viewport;
    private OrthographicCamera camera;
    @Override
    public void create() {
        Constant.viewport = viewport = new ExtendViewport(720,1280);
        camera  = Constant.camera = new OrthographicCamera();
        camera.translate(10.0F/2,23.0F/2);
        camera.update();
        Constant.fillViewport = new FillViewport(19,23,camera);
        Constant.batch = new SpriteBatch();
        Constant.game = this;
        Constant.assetManager = new AssetManager();
        Constant.world = new World(Vector2.Zero, true);
        Constant.box2DDebugRenderer = new Box2DDebugRenderer();
        setScreen(new LoadingScreen());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width,height);
        Constant.height = viewport.getWorldHeight();
        Constant.width = viewport.getWorldWidth();
        Constant.bgScale = Math.max(Constant.width / 720, Constant.height / 1280);
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
