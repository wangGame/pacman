package kw.test.pacmen.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import kw.test.pacmen.components.MyMovementComponent;
import kw.test.pacmen.components.MyPlayerComponent;
import kw.test.pacmen.components.MyStateComponent;
import kw.test.pacmen.manger.MyGameManager;

/**
 * 1.玩家系统   会有玩家组件
 * 2.玩家会移动   移动组件
 * 3.玩家有各种状态   状态组件
 *
 * 玩家先判断是活着的 ，根据输入进行设置移动方向
 */
public class MyPlayerSystem extends IteratingSystem {
    private final ComponentMapper<MyPlayerComponent> playerM = ComponentMapper.getFor(MyPlayerComponent.class);
    private final ComponentMapper<MyMovementComponent> movementM = ComponentMapper.getFor(MyMovementComponent.class);
    private final ComponentMapper<MyStateComponent> stateM = ComponentMapper.getFor(MyStateComponent.class);

    private final Vector2 tmpV1 = new Vector2();
    private final Vector2 tmpV2 = new Vector2();
    private boolean canMove;

    private enum MoveDir{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    public MyPlayerSystem() {
        super(Family.all(MyPlayerComponent.class,MyMovementComponent.class,MyStateComponent.class).get());
    }

    /**
     * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
     * specific processing.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MyPlayerComponent player = playerM.get(entity);
        MyStateComponent state = stateM.get(entity);
        MyMovementComponent movement = movementM.get(entity);

        Body body = movement.body;
        if (player.hp > 0){
            if ((Gdx.input.isKeyPressed(Input.Keys.D)||
                    Gdx.input.isKeyPressed(Input.Keys.RIGHT))&&
                    checkMovable(body,MoveDir.RIGHT)){
                body.applyLinearImpulse(
                        tmpV1.set(movement.speed,0).scl(body.getMass()),
                        body.getWorldCenter(), true);

            }else if ((Gdx.input.isKeyPressed(Input.Keys.A)||
                    Gdx.input.isKeyPressed(Input.Keys.LEFT))&&
                    checkMovable(body,MoveDir.LEFT)){
                body.applyLinearImpulse(
                        tmpV1.set(-movement.speed,0).scl(body.getMass()),
                        body.getWorldCenter(), true);
            }else if ((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) && checkMovable(body, MoveDir.UP)) {
                body.applyLinearImpulse(tmpV1.set(0, movement.speed).scl(body.getMass()), body.getWorldCenter(), true);
            } else if ((Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))&& checkMovable(body,MoveDir.DOWN)) {
                body.applyLinearImpulse(tmpV1.set(0, -movement.speed).scl(body.getMass()), body.getWorldCenter(), true);
            }

            if (body.getLinearVelocity().len2() > movement.speed * movement.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(movement.speed / body.getLinearVelocity().len()));
            }
        }

        //无敌  出生的时候
        if (MyGameManager.getinstance().playerIsInvincible){
            player.invincibleTimer += deltaTime;
            if (player.invincibleTimer>=3.0F){
                MyGameManager.getinstance().playerIsInvincible = false;
                player.invincibleTimer = 0;
            }
        }

//        player.playerAgent.update(deltaTime);
//        state.setState(player.currentState);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    private boolean checkMovable(Body body, MoveDir dir) {
        canMove = true;
        World world = body.getWorld();

        RayCastCallback rayCastCallback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits == MyGameManager.WALL_BIT){
                    canMove = false;
                    return 0;
                }
                return 0;
            }
        };

        for (int i=0;i<2;i++){
            tmpV1.set(body.getPosition());
            switch (dir){
                case UP:
                    tmpV2.set(body.getPosition().x - (i-0.5F)*0.2F,body.getPosition().y+0.6F);
                    break;
                case DOWN:
                    tmpV2.set(body.getPosition().x - (i-0.5F)*0.2F,body.getPosition().y-0.6F);
                    break;
                case LEFT:
                    tmpV2.set(body.getPosition().x - 0.6f, body.getPosition().y - (i - 0.5f) * 0.2f);
                    break;
                case RIGHT:
                    tmpV2.set(body.getPosition().x + 0.6f, body.getPosition().y - (i - 0.5f) * 0.2f);
                    break;
                default:
                    break;
            }
            world.rayCast(rayCastCallback,tmpV1,tmpV2);
        }
        return canMove;
    }
}
