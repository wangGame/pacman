package kw.pacman.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
import com.badlogic.gdx.scenes.scene2d.Group;

import kw.pacman.game.actor.Box2DActor;
import kw.pacman.game.components.GhostComponent;
import kw.pacman.game.components.MoveComponent;
import kw.pacman.game.components.PillComponent;
import kw.pacman.game.components.PlayerComponent;
import kw.pacman.game.components.TextureComponent;
import kw.pacman.game.components.TransformComponent;
import kw.pacman.game.constant.Constant;

public class MainWorldView extends Group {
    private final TextureAtlas actorAtlas;
    private AssetManager assetManager;
    private World world;
    private boolean wall;
    private Box2DActor player;
    private Engine engine;

    public MainWorldView(Engine engine) {
        this.engine = engine;
        assetManager = Constant.assetManager;
        assetManager.load("images/actors.pack", TextureAtlas.class);
        assetManager.finishLoading();
        actorAtlas = assetManager.get("images/actors.pack", TextureAtlas.class);
    }

    public void mapWorldView(TiledMap tiledMap, World world) {
        this.world = world;
        MapLayers mapLayers = tiledMap.getLayers();
        int mapWidth = ((TiledMapTileLayer) mapLayers.get(0)).getWidth();
        int mapHeight = ((TiledMapTileLayer) mapLayers.get(0)).getHeight();
        MapLayer wallLayer = mapLayers.get("Wall");
        for (MapObject mapObject : wallLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            Body body = this.world.createBody(bodyDef);
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rectangle.width / 2, rectangle.height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = Constant.WALL_BIT;
            fixtureDef.filter.maskBits = Constant.PLAYER_BIT | Constant.GHOST_BIT | Constant.LIGHT_BIT;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }

        // Gate
        MapLayer gateLayer = mapLayers.get("Gate"); // gate layer
        for (MapObject mapObject : gateLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            Body body = this.world.createBody(bodyDef);
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
            TextureRegion textureRegion;

            if (mapObject.getProperties().containsKey("big")) {
                isBig = true;
                radius = 0.2f;
                textureRegion = new TextureRegion(actorAtlas.findRegion("Pill"), 16, 0, 16, 16);
            } else {
                textureRegion = new TextureRegion(actorAtlas.findRegion("Pill"), 0, 0, 16, 16);
            }

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            Body body = this.world.createBody(bodyDef);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(radius);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.filter.categoryBits = Constant.PILL_BIT;
            fixtureDef.filter.maskBits = Constant.PLAYER_BIT;
            fixtureDef.isSensor = true;
            Fixture fixture = body.createFixture(fixtureDef);
            circleShape.dispose();

//            Box2DActor actor = new Box2DActor(textureRegion);
//            actor.setBody(body);
//            addActor(actor);

            /**
             * 豆子
             *  1.状态
             *  2.转换（大小，位置， 缩放 ，层级）
             *  3.绘制需要看到
             *  4.移动
             */
            Entity entity = new Entity();
            entity.add(new PillComponent(isBig));
//            z的值是根据显示的上下位置定的  起到1 2 3 4
            entity.add(new TransformComponent(rectangle.x+rectangle.width/2,
                    rectangle.y+rectangle.height/2,
                    5));
            entity.add(new TextureComponent(textureRegion));
            entity.add(new MoveComponent(body));
            engine.addEntity(entity);
            fixture.setUserData(entity);  // 信息的携带，使用的时候直接强转使用
            Constant.pillNum++;
        }

        // ghosts
        MapLayer ghostLayer = mapLayers.get("Ghost");
        for (MapObject mapObject : ghostLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
//            Constant.instance.ghostSpawnPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            // create four ghosts
            TextureRegion pacman = new TextureRegion(actorAtlas.findRegion("Pacman"), 0, 0, 16, 16);
            for (int i = 0; i < 4; i++) {
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);

                Body body = this.world.createBody(bodyDef);
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
                TextureRegion textureRegion = actorAtlas.findRegion("Ghost");
                Entity entity = new Entity();
                entity.add(new GhostComponent(body));
                entity.add(new TransformComponent(rectangle.x+rectangle.width/2,rectangle.y+rectangle.height/2,3));
                entity.add(new MoveComponent(body));
                entity.add(new StateComponent());
                entity.add(new TextureComponent(new TextureRegion(textureRegion,0, 0, 16, 16)));
                engine.addEntity(entity);
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
            Body body = this.world.createBody(bodyDef);
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
            TextureRegion pacman = new TextureRegion(actorAtlas.findRegion("Pacman"),0,0,16,16);
            PlayerComponent player = new PlayerComponent(body);
            Entity entity = new Entity();
            entity.add(player);
            entity.add(new TransformComponent(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2, 5));
            entity.add(new MoveComponent(body));
            entity.add(new TextureComponent(pacman));
            engine.addEntity(entity);
            body.setUserData(entity);
        }
    }


    private void correctRectangle(Rectangle rectangle) {
        rectangle.x = rectangle.x / Constant.PPM;
        rectangle.y = rectangle.y / Constant.PPM;
        rectangle.width = rectangle.width / Constant.PPM;
        rectangle.height = rectangle.height / Constant.PPM;
    }

}
