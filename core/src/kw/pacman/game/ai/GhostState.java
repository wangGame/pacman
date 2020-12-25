package kw.pacman.game.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import kw.pacman.game.components.GhostComponent;
import kw.pacman.game.constant.Constant;

import static kw.pacman.game.components.GhostComponent.MOVE_UP;

public enum  GhostState implements State<GhostAgent> {
    MOVE_UP(){
        @Override
        public void update(GhostAgent entity) {
            entity.component.currentState = GhostComponent.MOVE_UP;

            Body body = entity.component.getBody();
//            在一点上施加冲力。这立即修改了速度。如果应用的点不在质心上，它也会修改角速度。这会唤醒身体。
//            获得质心的世界位置      质心在世界的位置    此向量乘以标量   得到身体的总质量    mv冲量
            body.applyLinearImpulse(tmpV1.set(0,entity.speed).scl(body.getMass()),
                    body.getWorldCenter(),true);
//            获取线速度     线速度大于  实体的速度的时候   x and y的线速度
            if (body.getLinearVelocity().len2() > entity.speed * entity.speed){
                //在x y 上进行减速
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            //向上遇到墙
            if (checkHitWall(entity, GhostComponent.MOVE_UP)){

            }
        }
    }, MOVE_DOWN(){
        public void update(GhostAgent entity) {}
    },MOVE_LEFT(){
        public void update(GhostAgent entity) {}
    },MOVE_RIGHT(){
        public void update(GhostAgent entity) {}
    }
    ;

    //每次尝试0.55F
    protected static final float RADIUS = 0.55f;
    public boolean hitWall = false;
    protected boolean checkHitWall(GhostAgent entity, short state) {
        hitWall = false;
        Body body = entity.component.body;
        tmpV1.set(body.getWorldCenter());
        switch (state){
            case GhostComponent.MOVE_UP:
                tmpV2.set(tmpV1).add(0,RADIUS);
                break;
            case GhostComponent.MOVE_DOWN:
                tmpV2.set(tmpV1).add(0,-RADIUS);
                break;
            case GhostComponent.MOVE_LEFT:
                tmpV2.set(tmpV1).add(-RADIUS,0);
                break;
            case GhostComponent.MOVE_RIGHT:
                tmpV2.set(tmpV1).add(RADIUS,0);
                break;
            default:
                tmpV2.setZero();
                break;
        }
        World world = entity.component.body.getWorld();
        world.rayCast(castCallback,tmpV1,tmpV2);
        return hitWall;
    }

    private RayCastCallback castCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if (fixture.getFilterData().categoryBits == Constant.WALL_BIT) {
                hitWall = true;
            }
            return 0;
        }
    };

    protected static final Vector2 tmpV1 = new Vector2();
    protected static final Vector2 tmpV2 = new Vector2();
    protected static final List<Integer> choicesList = new ArrayList<>(4);


    @Override
    public void enter(GhostAgent entity) {

    }

    @Override
    public boolean onMessage(GhostAgent entity, Telegram telegram) {
        return false;
    }

    @Override
    public void exit(GhostAgent entity) {

    }

    public void changeState(GhostAgent ghostAgent,int state){
        switch (state){
            case GhostComponent.MOVE_UP:
                ghostAgent.stateMachine.changeState(MOVE_UP);
                break;
            case GhostComponent.MOVE_DOWN:
                ghostAgent.stateMachine.changeState(MOVE_DOWN);
                break;
            case GhostComponent.MOVE_LEFT:
                ghostAgent.stateMachine.changeState(MOVE_LEFT);
                break;
            case GhostComponent.MOVE_RIGHT:
                ghostAgent.stateMachine.changeState(MOVE_RIGHT);
                break;
            default:
                break;
        }
    }

}
