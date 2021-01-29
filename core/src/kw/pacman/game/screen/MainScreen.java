package kw.pacman.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import java.awt.event.KeyListener;

import javafx.scene.PointLight;
import kw.pacman.game.actor.Box2DActor;
import kw.pacman.game.components.MoveComponent;
import kw.pacman.game.constant.Constant;
import kw.pacman.game.screen.base.BaseMapScreen;
import kw.pacman.game.system.GhostSystem;
import kw.pacman.game.system.MoveSystem;
import kw.pacman.game.system.PillSystem;
import kw.pacman.game.system.PlaySystem;
import kw.pacman.game.system.RenderSystem;

public class MainScreen extends BaseMapScreen {
    private RenderSystem renderSystem;
    private PlaySystem playSystem;
    private MoveSystem moveSystem;
    private PillSystem pillSystem;
    private GhostSystem ghostSystem;
    public MainScreen() {
        super("map/map.tmx");
    }

    @Override
    protected void initView() {
        //绘制地图里面的墙等元素
        addSystem();
        MainWorldView view = new MainWorldView(engine);
        view.mapWorldView(tiledMap,world);
        initPanelView();
        initWordListener();
    }

    private void addSystem() {
        playSystem = new PlaySystem();
        engine.addSystem(playSystem);
        moveSystem = new MoveSystem();
        engine.addSystem(moveSystem);
        pillSystem = new PillSystem();
        engine.addSystem(pillSystem);
        renderSystem = new RenderSystem(Constant.batch);
        engine.addSystem(renderSystem);
        ghostSystem = new GhostSystem();
        engine.addSystem(ghostSystem);
    }

    private void initWordListener() {
        World world = Constant.world;
        world.setContactListener(new WorldContactListener());
    }

    private void initPanelView() {
        Image image = new Image(new Texture("images/actors.png"));
        stage.addActor(image);
        image.setPosition(100,1000);
    }



    @Override
    protected void keyEvent(int keyCode) {
        super.keyEvent(keyCode);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
