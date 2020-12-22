package test.learn;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;

import sun.dc.pr.PRError;

class PlayScreen implements Screen {
    private final float WIDTH = 19.0F;
    private final float HEIGHT = 23.0F;
    private OrthographicCamera camera;
    private FillViewport fitViewport;
    private SpriteBatch batch;
    private World world;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private LearnGame game;
    public PlayScreen(LearnGame learnGame) {
        this.game = learnGame;
        this.camera = game.getCamera();
    }

    @Override
    public void show() {

        fitViewport = new FillViewport(WIDTH,HEIGHT,camera);
        batch = new SpriteBatch();
        world = new World(Vector2.Zero, true);
//        world.setContactListener(new WorldContactListener());
        tiledMap = new TmxMapLoader().load("map/map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 16f, batch);
    }

    @Override
    public void render(float delta) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

    }

    @Override
    public void resize(int width, int height) {

        fitViewport.update(width, height);
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

    @Override
    public void dispose() {

    }
}
