package kw.test.pacmen.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

import box2dLight.RayHandler;
import kw.test.pacmen.PacmanGame;
import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.components.MyPlayerComponent;
import kw.test.pacmen.constant.Constant;
import kw.test.pacmen.listener.MyWorldContactListener;
import kw.test.pacmen.manger.MyGameManager;
import kw.test.pacmen.worldbuilder.MyWorldBuilder;

/**
 * 游戏引擎  一个引擎由系统组成，  系统中由组件组成
 */
public class GameScreen extends ScreenAdapter {
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private TiledMap tiledMap;
    private PacmanGame pacmanGame;
    private TiledMapRenderer tiledMapRenderer;
    private Stage stage;
    private Group gameView;
    private MyWorldBuilder myWorldBuilder;
    private RayHandler rayHandler;
    private float ambientLight = 0.5f;

    public GameScreen(PacmanGame pacmanGame) {
        this.pacmanGame = pacmanGame;
        this.stage = new Stage(pacmanGame.getGameView(),pacmanGame.batch);
    }


    @Override
    public void show() {
        world = new World(Vector2.Zero,true);
        world.setContactListener(new MyWorldContactListener());
        debugRenderer = new Box2DDebugRenderer();
        tiledMap = new TmxMapLoader().load("map/map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/16.0F,pacmanGame.getBatch());
        gameView = new Group();
        gameView.setSize(Constant.GAMEWIDTH,Constant.GAMEHEIGHT);
        stage.addActor(gameView);
        gameView.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHEIGHT/2, Align.center);
        gameView.setDebug(true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.5F);
        myWorldBuilder = new MyWorldBuilder(tiledMap, world, rayHandler, gameView);
        myWorldBuilder.buildMap();

//        rayHandler.setAmbientLight(MathUtils.clamp(0.6F, 0f, 1f));
//        rayHandler.removeAll();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        myWorldBuilder.update();
        world.step(1/60F,8,3);
        debugRenderer .render(world, pacmanGame.getWorldView().getCamera().combined);
        tiledMapRenderer.setView((OrthographicCamera) pacmanGame.getWorldView().getCamera());
        tiledMapRenderer.render();
        stage.act();
        stage.draw();
        rayHandler.setCombinedMatrix(((OrthographicCamera) pacmanGame.getWorldView().getCamera()));
        rayHandler.updateAndRender();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
