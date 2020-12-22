package kw.pacman.game.screen.base;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import kw.pacman.game.constant.Constant;

public class BaseMapScreen extends BaseScreen {
    protected static TiledMapRenderer tiledMapRenderer;
    protected static TiledMap tiledMap;
    protected OrthographicCamera camera;
    protected World world;
    protected Stage fillStage;
    public BaseMapScreen(String path){
        tiledMap = new TmxMapLoader().load(path);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/16f, new SpriteBatch());
        camera = Constant.camera;
        world = Constant.world;
        fillStage = new Stage(Constant.fillViewport,Constant.batch);
    }

    @Override
    public void show() {
        super.show();
        inputMultiplexer.addProcessor(keyListener);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void render(float delta) {
        tiledMapRenderer.setView(Constant.camera);
        tiledMapRenderer.render();
        fillStage.act();
        fillStage.draw();
        Constant.world.step(1/60f,8,3);
//        Constant.box2DDebugRenderer.render(Constant.world, camera.combined);
        super.render(delta);
    }

    private InputAdapter keyListener = new InputAdapter(){
        private int keycode;
        @Override
        public boolean keyDown(int keycode) {
            this.keycode = keycode;
            return super.keyDown(keycode);
        }

        @Override
        public boolean keyTyped(char character) {
            keyEvent(keycode);
            return super.keyTyped(character);
        }

        @Override
        public boolean keyUp(int keycode) {
            return super.keyUp(keycode);
        }
    };

    protected void keyEvent(int keyCode){

    }
}
