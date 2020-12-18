package kw.pacman.game.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import javafx.scene.PointLight;
import kw.pacman.game.actor.Box2DActor;
import kw.pacman.game.constant.Constant;
import kw.pacman.game.screen.base.BaseMapScreen;

public class MainScreen extends BaseMapScreen {
    private World world;
    private boolean wall;
    public MainScreen() {
        super("map/map.tmx");
        world = Constant.world;
    }

    @Override
    protected void initView() {
        //绘制地图里面的墙等元素
        mapWorldView();
        initPanelView();
    }

    private void initPanelView() {
        Image image = new Image(new Texture("tip.png"));
        stage.addActor(image);
        image.setPosition(Constant.width/2,Constant.height, Align.top);
    }

    private void mapWorldView() {
        MapLayers mapLayers = tiledMap.getLayers();
        //get width and hight
        int mapWidth = ((TiledMapTileLayer) mapLayers.get(0)).getWidth();
        int mapHeight = ((TiledMapTileLayer) mapLayers.get(0)).getHeight();
        // walls
        MapLayer wallLayer = mapLayers.get("Wall"); // wall layer
        for (MapObject mapObject : wallLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);

            Body body = world.createBody(bodyDef);
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rectangle.width / 2, rectangle.height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = Constant.WALL_BIT;
            fixtureDef.filter.maskBits = Constant.PLAYER_BIT | Constant.GHOST_BIT | Constant.LIGHT_BIT;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
        /***********************create  find map*******************/
        // create map for A* path finding
//        AStarMap aStarMap = new AStarMap(mapWidth, mapHeight);

        QueryCallback queryCallback = new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                wall = fixture.getFilterData().categoryBits == Constant.WALL_BIT;
                return false; // stop finding other fixtures in the query area
            }
        };

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                wall = false;
                //查找提提供的范围内有没有重叠的部分。
                world.QueryAABB(queryCallback, x + 0.2f, y + 0.2f, x + 0.8f, y + 0.8f);
                if (wall) {
//                    aStarMap.getNodeAt(x, y).isWall = true;
                }
            }
        }
//        System.out.println(aStarMap.toString());

        /*******************create find map**********************/
//        GameManager.instance.pathfinder = new AStartPathFinding(aStarMap);



        // Gate
        MapLayer gateLayer = mapLayers.get("Gate"); // gate layer
        for (MapObject mapObject : gateLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);

            Body body = world.createBody(bodyDef);
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rectangle.width / 2, rectangle.height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = Constant.GATE_BIT;
            fixtureDef.filter.maskBits = Constant.PLAYER_BIT | Constant.GHOST_BIT;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }

        // pills
        MapLayer pillLayer = mapLayers.get("Pill"); // pill layer
        for (MapObject mapObject : pillLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);

            boolean isBig = false;
            float radius = 0.1f;
            Texture texture;

            if (mapObject.getProperties().containsKey("big")) {
                radius = 0.2f;
                texture = new Texture("duzi/actors_03.png");
            } else {
                texture = new Texture("duzi/actors_04.png");
            }

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            Body body = world.createBody(bodyDef);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(radius);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.filter.categoryBits = Constant.PILL_BIT;
            fixtureDef.filter.maskBits = Constant.PLAYER_BIT;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            circleShape.dispose();
            Box2DActor actor = new Box2DActor(texture);
            actor.setBody(body);
            fillStage.addActor(actor);
        }

        // ghosts
        MapLayer ghostLayer = mapLayers.get("Ghost");
        for (MapObject mapObject : ghostLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
//            Constant.instance.ghostSpawnPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            // create four ghosts
            Texture texture = new Texture("play/actors_03.jpg");
            for (int i = 0; i < 4; i++) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);

                Body body = world.createBody(bodyDef);
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(0.4f);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.filter.categoryBits = Constant.GHOST_BIT;
                fixtureDef.filter.maskBits = Constant.PLAYER_BIT;
                fixtureDef.isSensor = true;
                body.createFixture(fixtureDef);

                fixtureDef.filter.categoryBits = Constant.GHOST_BIT;
                fixtureDef.filter.maskBits = Constant.WALL_BIT | Constant.GATE_BIT;
                fixtureDef.isSensor = false;
                body.createFixture(fixtureDef);
                Box2DActor actor = new Box2DActor(texture);
                actor.setBody(body);
                fillStage.addActor(actor);
            }
        }

        // player
        MapLayer playerLayer = mapLayers.get("Player"); // player layer
        for (MapObject mapObject : playerLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            bodyDef.linearDamping = 16f;

            Body body = world.createBody(bodyDef);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(0.45f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.filter.categoryBits = Constant.PLAYER_BIT;
            fixtureDef.filter.maskBits = Constant.WALL_BIT |
                    Constant.GATE_BIT | Constant.GHOST_BIT |
                    Constant.PILL_BIT;
            body.createFixture(fixtureDef);
            circleShape.dispose();
            Box2DActor actor = new Box2DActor(new Texture("play/actors_04.jpg"));
            actor.setBody(body);
            fillStage.addActor(actor);
        }
    }

    private void correctRectangle(Rectangle rectangle) {
        rectangle.x = rectangle.x / Constant.PPM;
        rectangle.y = rectangle.y / Constant.PPM;
        rectangle.width = rectangle.width / Constant.PPM;
        rectangle.height = rectangle.height / Constant.PPM;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
