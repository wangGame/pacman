package kw.pacman.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
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
    private Viewport fillViewport;
    private OrthographicCamera camera;
    @Override
    public void create() {
        //create viewport and as a constant
        Constant.viewport = viewport = new ExtendViewport(720,1280);
        camera  = Constant.camera = new OrthographicCamera();
        camera.translate(19.0F/2,23.0F/2);
        camera.update();
        Constant.fillViewport = fillViewport = new FillViewport(19,23,camera);
        // call to set value Constant.width and Constant.height
        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //create batch
        Constant.batch = new CpuSpriteBatch();
        //loading
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
        Constant.fillViewport.update(width,height);
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
