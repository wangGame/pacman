package kw.test.pacmen.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;

import box2dLight.RayHandler;
import kw.test.pacmen.PacmanGame;
import kw.test.pacmen.listener.MyWorldContactListener;
import kw.test.pacmen.manger.MyGameManager;
import kw.test.pacmen.system.MyAniamationSystem;
import kw.test.pacmen.system.MyGhostSystem;
import kw.test.pacmen.system.MyMovementSystem;
import kw.test.pacmen.system.MyPillSystem;
import kw.test.pacmen.system.MyPlayerSystem;
import kw.test.pacmen.system.MyRenderSystem;
import kw.test.pacmen.system.MyStateSystem;
import kw.test.pacmen.worldbuilder.MyWorldBuilder;

/**
 * 游戏引擎  一个引擎由系统组成，  系统中由组件组成
 */
public class GameScreen extends ScreenAdapter {
    //地图的大小  我们会进行等比缩放
    private float WORLDWIDTH = 19.0F;
    private float WORLDHEIGHT = 23.0F;
    private PacmanGame pacmanGame;
    private SpriteBatch spriteBatch;
    private OrthographicCamera worldCamera;
    private FillViewport worldFitport;

    //system
    private MyPlayerSystem playerSystem;
    private MyGhostSystem ghostSystem;
    private MyMovementSystem movementSystem;
    private MyPillSystem pillSystem;
    private MyAniamationSystem aniamationSystem;
    private MyRenderSystem renderSystem;
    private MyStateSystem stateSystem;

    //engine
    private Engine engine;

    //box
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private boolean showBox2DDebugRenderer = false;
    private RayHandler rayHandler;
    private float ambientLight = 0.5f;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private boolean changeScreen;
    public GameScreen(PacmanGame pacmanGame) {
        this.pacmanGame = pacmanGame;
        this.spriteBatch = pacmanGame.batch;
    }

    @Override
    public void show() {
        worldCamera = new OrthographicCamera();
        worldFitport = new FillViewport(WORLDWIDTH,WORLDHEIGHT,worldCamera);
        worldCamera.translate(WORLDWIDTH/2,WORLDHEIGHT/2);
        worldCamera.update();
        engine = new Engine();
        //box
        world = new World(Vector2.Zero,true);
        world.setContactListener(new MyWorldContactListener());
        debugRenderer = new Box2DDebugRenderer();
        showBox2DDebugRenderer = false;


        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(ambientLight);

        //map
        tiledMap = new TmxMapLoader().load("map/map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/ MyGameManager.PPM,spriteBatch);
        new MyWorldBuilder(tiledMap,engine,world,rayHandler).buildMap();

        playerSystem = new MyPlayerSystem();
        ghostSystem = new MyGhostSystem();
        movementSystem = new MyMovementSystem();
        pillSystem = new MyPillSystem();
        aniamationSystem = new MyAniamationSystem();
        renderSystem = new MyRenderSystem(spriteBatch);
        stateSystem = new MyStateSystem();
        engine.addSystem(playerSystem);
        engine.addSystem(ghostSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(pillSystem);
        engine.addSystem(aniamationSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(stateSystem);
        changeScreen = false;
    }

    private void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)){

        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        handleInput();
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(worldCamera);
        tiledMapRenderer.render();
//
        spriteBatch.setProjectionMatrix(worldCamera.combined);
        if (changeScreen){
            playerSystem.setProcessing(false);
            ghostSystem.setProcessing(false);
            movementSystem.setProcessing(false);
        }else {
            world.step(1/60F,8,3);
        }
        engine.update(delta);
        debugRenderer .render(world, worldCamera.combined);
//
        rayHandler.setCombinedMatrix(worldCamera);
        rayHandler.updateAndRender();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        worldFitport.update(width, height);
    }
}
