package kw.test.pacmen.worldbuilder;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;


import box2dLight.RayHandler;
import kw.test.pacmen.AnimationActor;
import kw.test.pacmen.BodyImage;
import kw.test.pacmen.GhostActor;
import kw.test.pacmen.PlayerActor;
import kw.test.pacmen.ai.astar.MyAStarMap;
import kw.test.pacmen.ai.astar.MyAstartPathFinding;
import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.components.MyPlayerComponent;
import kw.test.pacmen.manger.MyGameManager;

public class MyWorldBuilder {
    private final TiledMap tiledMap;
    private final World world;
    private final AssetManager assetManager;
    private final TextureAtlas atlas;
    private boolean wall;
    private Group gameView;

    public MyWorldBuilder(TiledMap tiledMap, World world, RayHandler rayHandler, Group gameView){
        this.gameView = gameView;
        this.tiledMap = tiledMap;
        this.world = world;
        assetManager = MyGameManager.getinstance().assetManager;
        atlas = assetManager.get("images/actors.pack",TextureAtlas.class);
    }

    public void buildMap(){
        MapLayers mapLayers = tiledMap.getLayers();
        int mapWidth = ((TiledMapTileLayer)mapLayers.get(0)).getWidth();
        int mapHeight = ((TiledMapTileLayer)mapLayers.get(0)).getHeight();
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
            fixtureDef.filter.categoryBits = MyGameManager.WALL_BIT;
            fixtureDef.filter.maskBits = MyGameManager.PLAYER_BIT |MyGameManager.GHOST_BIT |  MyGameManager.LIGHT_BIT ;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
        // create map for A* path finding
        MyAStarMap aStarMap = new MyAStarMap(mapWidth, mapHeight);
        QueryCallback queryCallback = new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                //撞到墙 就 返回false
                wall = fixture.getFilterData().categoryBits == MyGameManager.WALL_BIT;
                return false; // stop finding other fixtures in the query area
            }
        };

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                wall = false;
                world.QueryAABB(queryCallback, x + 0.2f,
                        y + 0.2f, x + 0.8f, y + 0.8f);
                if (wall) {
                    aStarMap.getNodeAt(x, y).isWall = true;
                }
            }
        }
        MyGameManager.getinstance().pathfinder = new MyAstartPathFinding(aStarMap);

        //Gate
        MapLayer gateLayer = mapLayers.get("Gate");
        for (MapObject object : gateLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            correctRectangle(rectangle);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);

            Body body = world.createBody(bodyDef);
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(rectangle.width / 2, rectangle.height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.filter.categoryBits = MyGameManager.GATE_BIT;
            fixtureDef.filter.maskBits = MyGameManager.PLAYER_BIT | MyGameManager.GHOST_BIT;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }

        // pills
        MapLayer pillLayer = mapLayers.get("Pill"); // pill layer
        for (MapObject mapObject : pillLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
            float radius = 0.1F;
            TextureRegion textureRegion;
            BodyImage bodyImage;
            boolean isBig = true;
            if (mapObject.getProperties().containsKey("big")){
                radius = 0.2F;
                textureRegion = new TextureRegion(atlas.findRegion("Pill"), 16, 0, 16, 16);
            }else {
                isBig = false;
                textureRegion = new TextureRegion(atlas.findRegion("Pill"), 0, 0, 16, 16);
            }
            bodyImage = new BodyImage(textureRegion);
            bodyImage.setBig(isBig);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(rectangle.x+rectangle.width/2,rectangle.y+rectangle.height/2);
            Body body = world.createBody(bodyDef);
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(radius);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.filter.categoryBits = MyGameManager.PILL_BIT;
            fixtureDef.filter.maskBits = MyGameManager.PLAYER_BIT;
            fixtureDef.isSensor = true;
            body.createFixture(fixtureDef);
            circleShape.dispose();

            //创建一个实体
//            Entity entity = new Entity();
//            entity.add(new MyPillComponent(isBig));
//            entity.add(new MyTransformComponent(rectangle.x + rectangle.width/2,
//                    rectangle.y + rectangle.height/2,5));
////            entity.add(new MyTexureComponent(textureRegion));
//            entity.add(new MyMovementComponent(body));
            bodyImage.setBody(body);
            gameView.addActor(bodyImage);

//            engine.addEntity(entity);
            MyGameManager.getinstance().totalPills++;
        }

        MapLayer ghostLayer = mapLayers.get("Ghost");
        for (MapObject mapObject : ghostLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            correctRectangle(rectangle);
            MyGameManager.getinstance().ghostSpawnPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);

            // create four ghosts
            for (int i = 0; i < 4; i++) {
                createGhost(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2, i);
            }
        }

        MapLayer mapLayer = mapLayers.get("Player");
        for (MapObject object : mapLayer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            correctRectangle(rectangle);
            MyGameManager.getinstance().playerSpawnPos.set(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
            createPlayer(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        }
    }

    private Body playerBody;

    public Body getPlayerBody() {
        return playerBody;
    }

    private void createPlayer(float x, float y) {
        PlayerActor animationActor = new PlayerActor();
        gameView.addActor(animationActor);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 16f;
        playerBody = world.createBody(bodyDef);
        animationActor.setBody(playerBody);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.45f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = MyGameManager.PLAYER_BIT;
        fixtureDef.filter.maskBits = MyGameManager.WALL_BIT | MyGameManager.GATE_BIT |
                MyGameManager.GHOST_BIT | MyGameManager.PILL_BIT;
        playerBody.createFixture(fixtureDef);
        playerBody.setUserData(animationActor);
        circleShape.dispose();

        // box2d light
//        PointLight pointLight = new PointLight(rayHandler, 50, new Color(0.5f, 0.5f, 0.5f, 1.0f), 12f, 0, 0);
//        pointLight.setContactFilter(MyGameManager.LIGHT_BIT, MyGameManager.NOTHING_BIT, MyGameManager.WALL_BIT);
//        pointLight.setSoft(true);
//        pointLight.setSoftnessLength(2.0f);
//        pointLight.attachToBody(body);

        TextureRegion textureRegion = new TextureRegion(atlas.findRegion("Pacman"), 0, 0, 16, 16);

        MyPlayerComponent player = new MyPlayerComponent(playerBody);
        MyGameManager.getinstance().playerLocation = player.ai;

//        Entity entity = new Entity();
//        entity.add(player);
//        entity.add(new MyTransformComponent(x, y, 1));
//        entity.add(new MyMovementComponent(body));
//        entity.add(new MyStateComponent(MyPlayerComponent.IDLE_RIGHT));
//        entity.add(new MyTexureComponent(textureRegion));

//        MyAnimationComponent animationComponent = new MyAnimationComponent();
        Animation animation;
        Array<TextureRegion> keyFrames = new Array<>();

        // idle
        keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), 16 * 1, 0, 16, 16));
        animation = new Animation(0.2f, keyFrames);
        animationActor.addAnimation(MyPlayerComponent.IDLE_RIGHT, animation);

        keyFrames.clear();

        keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), 16 * 3, 0, 16, 16));
        animation = new Animation(0.2f, keyFrames);
        animationActor.addAnimation(MyPlayerComponent.IDLE_LEFT, animation);

        keyFrames.clear();

        keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), 16 * 5, 0, 16, 16));
        animation = new Animation(0.2f, keyFrames);
        animationActor.addAnimation(MyPlayerComponent.IDLE_UP, animation);

        keyFrames.clear();

        keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), 16 * 7, 0, 16, 16));
        animation = new Animation(0.2f, keyFrames);
        animationActor.addAnimation(MyPlayerComponent.IDLE_DOWN, animation);

        keyFrames.clear();

        // move
        for (int i = 1; i < 3; i++) {
            keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), i * 16, 0, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyPlayerComponent.MOVE_RIGHT, animation);

        keyFrames.clear();

        for (int i = 3; i < 5; i++) {
            keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), i * 16, 0, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyPlayerComponent.MOVE_LEFT, animation);

        keyFrames.clear();

        for (int i = 5; i < 7; i++) {
            keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), i * 16, 0, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyPlayerComponent.MOVE_UP, animation);

        keyFrames.clear();

        for (int i = 7; i < 9; i++) {
            keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), i * 16, 0, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyPlayerComponent.MOVE_DOWN, animation);

        keyFrames.clear();

        for (int i = 0; i < 10; i++) {
            keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), i * 16, 16, 16, 16));
        }
        keyFrames.add(new TextureRegion(atlas.findRegion("Pacman"), 9 * 16, 0, 16, 16)); // invisible
        animation = new Animation(0.1f, keyFrames, Animation.PlayMode.NORMAL);
        animationActor.addAnimation(MyPlayerComponent.DIE, animation);

        keyFrames.clear();
        animationActor.setAniType(MyPlayerComponent.MOVE_LEFT);

//        entity.add(animationComponent);

//        engine.addEntity(entity);
//        body.setUserData(entity);
    }
    private void createGhost(float x,float y,int index){
        GhostActor animationActor = new GhostActor();
        gameView.addActor(animationActor);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        animationActor.setBody(body);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.4f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = MyGameManager.GHOST_BIT;
        fixtureDef.filter.maskBits = MyGameManager.PLAYER_BIT;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);

        fixtureDef.filter.categoryBits = MyGameManager.GHOST_BIT;
        fixtureDef.filter.maskBits = MyGameManager.WALL_BIT | MyGameManager.GATE_BIT;
        fixtureDef.isSensor = false;
        body.createFixture(fixtureDef);

        // box2d light
//        PointLight pointLight = new PointLight(rayHandler, 50, new Color(0.2f, 0.2f, 0.2f, 1.0f), 12f, 0, 0);
//        pointLight.setContactFilter(MyGameManager.LIGHT_BIT, MyGameManager.NOTHING_BIT, MyGameManager.WALL_BIT);
//        pointLight.setSoft(true);
//        pointLight.setSoftnessLength(5.0f);
//        pointLight.attachToBody(body);
        circleShape.dispose();

        //texture
        TextureRegion textureRegion = atlas.findRegion("Ghost");
//        MyAnimationComponent anim = new MyAnimationComponent();
        Animation animation;
        Array<TextureRegion> keyFrames = new Array<>();

        // move right
        for (int i = 0; i < 2; i++) {
            keyFrames.add(new TextureRegion(textureRegion,
                    i*16,index*16,16,16));
        }
        animation = new Animation(0.2F,keyFrames,Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyGhostComponent.MOVE_RIGHT,animation);

        keyFrames.clear();

        //move left
        for (int i = 2; i < 4; i++) {
            keyFrames.add(new TextureRegion(textureRegion,i*16,index*16,16,16));
        }
        animation = new Animation(0.2F,keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyGhostComponent.MOVE_RIGHT,animation);

        keyFrames.clear();

        // move up
        for (int i = 4; i < 6; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, index * 16, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyGhostComponent.MOVE_UP, animation);

        keyFrames.clear();

        // move down
        for (int i = 6; i < 8; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, index * 16, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyGhostComponent.MOVE_DOWN, animation);

        keyFrames.clear();

        // escape
        for (int i = 0; i < 4; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 16 * 4, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyGhostComponent.ESCAPE, animation);

        keyFrames.clear();

        // die
        for (int i = 4; i < 8; i++) {
            keyFrames.add(new TextureRegion(textureRegion, i * 16, 16 * 4, 16, 16));
        }
        animation = new Animation(0.2f, keyFrames, Animation.PlayMode.LOOP);
        animationActor.addAnimation(MyGhostComponent.DIE, animation);

//        MyGhostComponent ghostComponent = new MyGhostComponent(body);
//        Entity entity = new Entity();
//        entity.add(ghostComponent);
//        entity.add(new MyTransformComponent(x,y,3));
//        entity.add(new MyMovementComponent(body));
//        entity.add(new MyStateComponent());
//        entity.add(new MyTexureComponent(new TextureRegion(textureRegion,
//                0,0,16,16)));
//        entity.add(anim);
//        engine.addEntity(entity);
        body.setUserData(animationActor);
        animationActor.initAnimation(MyGhostComponent.MOVE_UP);
    }
    // make rectangle correct position and dimensions
    private void correctRectangle(Rectangle rectangle) {
        rectangle.x = rectangle.x / MyGameManager.PPM;
        rectangle.y = rectangle.y / MyGameManager.PPM;
        rectangle.width = rectangle.width / MyGameManager.PPM;
        rectangle.height = rectangle.height / MyGameManager.PPM;
    }

    private final Vector2 tmpV1 = new Vector2();

    public void update(){
        AnimationActor animationActor = (AnimationActor) playerBody.getUserData();
        if (MyPlayerComponent.CURRENT_DIR!=MyPlayerComponent.IDLE) {
            animationActor.setAniType(MyPlayerComponent.CURRENT_DIR);
        }
        if (MyPlayerComponent.CURRENT_DIR == MyPlayerComponent.MOVE_LEFT){
            System.out.println("left");
            playerBody.applyLinearImpulse(tmpV1.set(-3.6f, 0).scl(playerBody.getMass()), playerBody.getWorldCenter(), true);
        }else if (MyPlayerComponent.CURRENT_DIR == MyPlayerComponent.MOVE_RIGHT){
            System.out.println("right");
            playerBody.applyLinearImpulse(tmpV1.set(3.6f, 0).scl(playerBody.getMass()), playerBody.getWorldCenter(), true);
        }else if (MyPlayerComponent.CURRENT_DIR == MyPlayerComponent.MOVE_UP){
            playerBody.applyLinearImpulse(tmpV1.set(0, 3.6f).scl(playerBody.getMass()), playerBody.getWorldCenter(), true);
            System.out.println("down");
        }else if (MyPlayerComponent.CURRENT_DIR == MyPlayerComponent.MOVE_DOWN){
            playerBody.applyLinearImpulse(tmpV1.set(0, -3.6f).scl(playerBody.getMass()), playerBody.getWorldCenter(), true);
            System.out.println("up");
        }
        if (playerBody.getLinearVelocity().len2() > 3.6F * 3.6F) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().scl(3.6F / playerBody.getLinearVelocity().len()));
        }
    }
}
