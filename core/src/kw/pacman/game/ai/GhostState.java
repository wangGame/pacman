package kw.pacman.game.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import kw.pacman.game.ai.astar.AStarMap;
import kw.pacman.game.components.GhostComponent;
import kw.pacman.game.components.MoveComponent;
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
                //向上运动遇到墙，向下（是不对的）  除过相反的方向   删除之后，找出剩下方向没有障碍的
                Integer[] directionChoices = getDirectionChoices(entity, GhostComponent.MOVE_DOWN);
                //随机的选取一个进行
                int randomDirectionChoice = getRandomDirectionChoice(directionChoices);
                for (Integer directionChoice : directionChoices) {
                    System.out.println(directionChoice);
                }

                changeState(entity,randomDirectionChoice);
                return;
            }

            //  不是很明白   一个方向上走 0.5s 后改变方向，否则就会在一条路上走
            if (entity.timer>0.5F && inPosition(entity,0.05F)){
                entity.timer = 0;
                int newState = getRandomDirectionChoice(getDirectionChoices(entity, GhostComponent.MOVE_DOWN));
                if (newState != entity.component.currentState) {
                    changeState(entity, newState);
                    return;
                }
            }

//
//            减弱      ESCAPE
            if (entity.component.weaken) {
                entity.component.currentState = GhostComponent.ESCAPE;
//                实体如果死亡了，就让他重新开始
                if (entity.component.hp <= 0 && inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }

            /**
             * 在追赶范围内     或者    不是无敌状态
             */
            if (nearPlayer(entity, PURSUE_RADIUS) &&
                    (Constant.playerIsAlive && !Constant.playerIsInvincible)
                    && inPosition(entity, 0.1f)) {
                //减弱
                if (entity.component.weaken) {
                    entity.stateMachine.changeState(ESCAPE);
                } else {
                    entity.stateMachine.changeState(PURSUE);
                }
            }
        }
    }, MOVE_DOWN(){
        public void update(GhostAgent entity) {
            entity.component.currentState = GhostComponent.MOVE_DOWN;

            Body body = entity.component.getBody();
            body.applyLinearImpulse(tmpV1.set(0,-entity.speed).scl(body.getMass()),body.getWorldCenter(),true);

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed){
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (checkHitWall(entity, GhostComponent.MOVE_DOWN)) {
                changeState(entity,getRandomDirectionChoice(getDirectionChoices(entity,GhostComponent.MOVE_UP)));
            }
            if (entity.timer > 0.5f && inPosition(entity, 0.05f)) {
                entity.timer = 0;
                int newState = getRandomDirectionChoice(getDirectionChoices(entity, GhostComponent.MOVE_UP));
                if (newState != entity.component.currentState) {
                    changeState(entity, newState);
                    return;
                }
            }

            if (entity.component.weaken) {
                entity.component.currentState = GhostComponent.ESCAPE;
                if (entity.component.hp <= 0 && inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }

            if (nearPlayer(entity, PURSUE_RADIUS) &&
                    (Constant.playerIsAlive && !Constant.playerIsInvincible) &&
                    inPosition(entity, 0.1f)) {
                if (entity.component.weaken) {
                    entity.stateMachine.changeState(ESCAPE);
                } else {
                    entity.stateMachine.changeState(PURSUE);
                }
            }
        }
    },MOVE_LEFT(){
        public void update(GhostAgent entity) {
            entity.component.currentState = GhostComponent.MOVE_LEFT;
            Body body = entity.component.getBody();
            body.applyLinearImpulse(tmpV1.set(-entity.speed, 0).scl(body.getMass()), body.getWorldCenter(), true);

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (checkHitWall(entity, GhostComponent.MOVE_LEFT)) {
                changeState(entity, getRandomDirectionChoice(getDirectionChoices(entity, GhostComponent.MOVE_RIGHT)));
                return;
            }

            if (entity.timer > 1f && inPosition(entity, 0.05f)) {
                entity.timer = 0;
                int newState = getRandomDirectionChoice(getDirectionChoices(entity, GhostComponent.MOVE_RIGHT));
                if (newState != entity.component.currentState) {
                    changeState(entity, newState);
                    return;
                }
            }

            if (entity.component.weaken) {
                entity.component.currentState = GhostComponent.ESCAPE;

                if (entity.component.hp <= 0 && inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }

            if (nearPlayer(entity, PURSUE_RADIUS) &&
                    (Constant.playerIsAlive && !Constant.playerIsInvincible) &&
                    inPosition(entity, 0.1f)) {
                if (entity.component.weaken) {
                    entity.stateMachine.changeState(ESCAPE);
                } else {
                    entity.stateMachine.changeState(PURSUE);
                }
            }
        }
    },MOVE_RIGHT(){
        public void update(GhostAgent entity) {
            entity.component.currentState = GhostComponent.MOVE_RIGHT;

            Body body = entity.component.getBody();
            body.applyLinearImpulse(tmpV1.set(entity.speed, 0).scl(body.getMass()), body.getWorldCenter(), true);

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (checkHitWall(entity, GhostComponent.MOVE_RIGHT)) {
                changeState(entity, getRandomDirectionChoice(getDirectionChoices(entity, GhostComponent.MOVE_LEFT)));
                return;
            }

            if (entity.timer > 1f && inPosition(entity, 0.05f)) {
                entity.timer = 0;
                int newState = getRandomDirectionChoice(getDirectionChoices(entity, GhostComponent.MOVE_LEFT));
                if (newState != entity.component.currentState) {
                    changeState(entity, newState);
                    return;
                }
            }

            if (entity.component.weaken) {
                entity.component.currentState = GhostComponent.ESCAPE;

                if (entity.component.hp <= 0 && inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }

            if (nearPlayer(entity, PURSUE_RADIUS) && (Constant.playerIsAlive &&
                    !Constant.playerIsInvincible) && inPosition(entity, 0.1f)) {
                if (entity.component.weaken) {
                    entity.stateMachine.changeState(ESCAPE);
                } else {
                    entity.stateMachine.changeState(PURSUE);
                }
            }
        }
    },DIE(){
        @Override
        public void update(GhostAgent entity) {
//            entity.component.currentState = GhostComponent.DIE;
//            // respawn when getting back to the respawning postion
//            // update path every 0.2f
//            if (entity.nextNode == null || entity.timer > 0.2f) {
//                entity.nextNode = Constant.pathfinder.findNextNode(entity.getPosition(), Constant.ghostSpawnPos);
//                entity.timer = 0;
//            }
//
//            if (entity.nextNode == null || entity.getPosition().dst2(Constant.ghostSpawnPos) < 0.04f) {
//                // no path found or reach target
//                entity.component.getBody().setTransform(Constant.ghostSpawnPos, 0);
//                entity.stateMachine.changeState(RESPAWN);
//                return;
//            }
//
//            float x = (entity.nextNode.x - MathUtils.floor(entity.getPosition().x)) * entity.speed;
//            float y = (entity.nextNode.y - MathUtils.floor(entity.getPosition().y)) * entity.speed;
//
//            Body body = entity.component.getBody();
//
//            if (body.getLinearVelocity().isZero(0.1f) || inPosition(entity, 0.2f)) {
//                body.applyLinearImpulse(tmpV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
//            }
//
//            if (body.getLinearVelocity().len2() > entity.speed * entity.speed * 4) {
//                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed * 2 / body.getLinearVelocity().len()));
//            }
        }
    },ESCAPE(){
        @Override
        public void update(GhostAgent entity) {
//            // get away from the player
//            entity.component.currentState = GhostComponent.ESCAPE;
//
//            // update path every 0.2f
//            if (entity.nextNode == null || entity.timer > 0.2f) {
//                AStarMap map = Constant.pathfinder.map;
//
//                float x = (Constant.playerLocation.getPosition().x + map.getWidth() / 2);
//
//                float y = (Constant.playerLocation.getPosition().y + map.getHeight() / 2);
//
//                do {
//                    x += 1;
//                    y += 1;
//                    x = x > map.getWidth() ? x - map.getWidth() : x;
//                    y = y > map.getHeight() ? y - map.getHeight() : y;
//                } while (map.getNodeAt(MathUtils.floor(x), MathUtils.floor(y)).isWall);
//
//                tmpV1.set(x, y);
//                entity.nextNode = Constant.pathfinder.findNextNode(entity.getPosition(), tmpV1);
//                entity.timer = 0;
//            }
//
//            if (entity.nextNode == null || !nearPlayer(entity, PURSUE_RADIUS + 1)) {
//                // no path found or away from the player
//                changeState(entity, MathUtils.random(0, 3));
//                return;
//            }
//
//            float x = (entity.nextNode.x - MathUtils.floor(entity.getPosition().x)) * entity.speed;
//            float y = (entity.nextNode.y - MathUtils.floor(entity.getPosition().y)) * entity.speed;
//
//            Body body = entity.component.getBody();
//
//            if (body.getLinearVelocity().isZero(0.1f) || inPosition(entity, 0.1f)) {
//                body.applyLinearImpulse(tmpV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
//            }
//
//            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
//                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
//            }
//
//            if (!entity.component.weaken && inPosition(entity, 0.1f)) {
//                entity.stateMachine.changeState(PURSUE);
//                return;
//            }
//
//            if (entity.component.hp <= 0 && inPosition(entity, 0.1f)) {
//                entity.stateMachine.changeState(DIE);
//            }

        }
    },PURSUE(){
        @Override
        public void update(GhostAgent entity) {
//            if (Constant.playerLocation == null || !(Constant.playerIsAlive && !Constant.playerIsInvincible)) {
//                changeState(entity, MathUtils.random(0, 3));
//                return;
//            }
//
//            // do path finding every 0.1 second
//            if (entity.nextNode == null || entity.timer > 0.1f) {
//                entity.nextNode = Constant.pathfinder.findNextNode(entity.getPosition(), Constant.playerLocation.getPosition());
//                entity.timer = 0;
//            }
//            if (entity.nextNode == null) {
//                // no path found or player is dead
//                changeState(entity, MathUtils.random(0, 3));
//                return;
//            }
//
//            float x = (entity.nextNode.x - MathUtils.floor(entity.getPosition().x)) * entity.speed;
//            float y = (entity.nextNode.y - MathUtils.floor(entity.getPosition().y)) * entity.speed;
//
//            Body body = entity.component.getBody();
//
//            if (body.getLinearVelocity().isZero(0.1f) || inPosition(entity, 0.2f)) {
//                body.applyLinearImpulse(tmpV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
//            }
//
//            if (x > 0) {
//                entity.component.currentState = GhostComponent.MOVE_RIGHT;
//            } else if (x < 0) {
//                entity.component.currentState = GhostComponent.MOVE_LEFT;
//            } else if (y > 0) {
//                entity.component.currentState = GhostComponent.MOVE_UP;
//            } else if (y < 0) {
//                entity.component.currentState = GhostComponent.MOVE_DOWN;
//            }
//
//            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
//                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
//            }
//
//            if (!nearPlayer(entity, PURSUE_RADIUS) && inPosition(entity, 0.1f)) {
//                changeState(entity, entity.component.currentState);
//                return;
//            }
//
//            if (entity.component.weaken) {
//                entity.component.currentState = GhostComponent.ESCAPE;
//                if (inPosition(entity, 0.1f)) {
//                    entity.stateMachine.changeState(ESCAPE);
//                }
//            }
        }

    },RESPAWN(){
        @Override
        public void update(GhostAgent entity) {
            entity.component.respawn();
            entity.stateMachine.changeState(MOVE_UP);
        }
    };

    protected static final float PURSUE_RADIUS = 5f;

    protected boolean inPosition(GhostAgent entity, float radius) {
        float x = entity.getPosition().x;
        float y = entity.getPosition().y;
        float xLow = MathUtils.floor(x) + 0.5f - radius;
        float xHight = MathUtils.floor(x) + 0.5f + radius;

        float yLow = MathUtils.floor(y) + 0.5f - radius;
        float yHight = MathUtils.floor(y) + 0.5f + radius;

        return xLow < x && x < xHight && yLow < y && y < yHight;
    }

    /**
     * 两个物体的距离是不是小于追赶的目标距离
     * @param agent
     * @param distance
     * @return
     */
    protected boolean nearPlayer(GhostAgent agent,float distance){
        //如果玩家死了，就没有了，就直接返回了
        Vector2 pos = agent.getPosition();
        Vector2 playerPos = Constant.playerLocation.getPosition();
        return pos.dst(playerPos) < distance * distance;
    }

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

    protected int getRandomDirectionChoice(Integer[] choices) {
        if (choices.length == 0) {
            return 0;
        }
        int length = choices.length;
        return choices[MathUtils.random(length - 1)];
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

    public Array<Short> choiceArray = new Array<>();
    public Integer[] getDirectionChoices(GhostAgent ghostAgent,short stage){
        Body body = ghostAgent.component.getBody();
        World world = body.getWorld();
        choiceArray.clear();

        for (short i = 0; i < 4; i++) {
            choiceArray.add(i);
        }

        //不要走回头路
        choiceArray.removeValue(stage,false);
        tmpV1.set(body.getWorldCenter());  //质心位置

        Array<Short> arrayTemp = new Array<>(choiceArray);
        //删除会撞墙的
        for (Short aShort : arrayTemp) {
             hitWall = false;
             switch (aShort){
                 case GhostComponent.MOVE_UP:
                     tmpV2.set(tmpV1).add(0, RADIUS);
                     break;
                 case GhostComponent.MOVE_DOWN:
                     tmpV2.set(tmpV1).add(0, -RADIUS);
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
             //是墙  那就删除
            world.rayCast(castCallback, tmpV1, tmpV2);
             if (hitWall){
                 choiceArray.removeValue(aShort,false);
             }
        }
        Integer[] result = choicesList.toArray(new Integer[choiceArray.size]);
        for (int i = 0; i < choiceArray.size; i++) {
            result[i] = Integer.valueOf(Short.valueOf(choiceArray.get(i)));
        }
        return result;
    }

//    public void common(GhostAgent agent,short state,int speed,Vector2 tmpV1){
//        agent.component.currentState = state;
//        Body body = agent.component.getBody();
//        body.applyLinearImpulse(tmpV1.scl(body.getMass()), body.getWorldCenter(), true);
//        if (body.getLinearVelocity().len2() > agent.speed * agent.speed) {
//            body.setLinearVelocity(body.getLinearVelocity().scl(agent.speed / body.getLinearVelocity().len()));
//        }
//        if (checkHitWall(agent,state)) {
//            changeState(agent, getRandomDirectionChoice(getDirectionChoices(agent, state)));
//            return;
//        }
//        if (agent.timer > 0.5f && inPosition(agent, 0.05f)) {
//            agent.timer = 0;
//            int newState = getRandomDirectionChoice(getDirectionChoices(agent, GhostComponent.MOVE_UP));
//            if (newState != agent.component.currentState) {
//                changeState(agent, newState);
//                return;
//            }
//        }
//        if (agent.component.weaken) {
//            agent.component.currentState = GhostComponent.ESCAPE;
//            if (agent.component.hp <= 0 && inPosition(agent, 0.1f)) {
//                agent.stateMachine.changeState(DIE);
//                return;
//            }
//        }
//
//        if (nearPlayer(agent, PURSUE_RADIUS) &&
//                (Constant.playerIsAlive && !Constant.playerIsInvincible) &&
//                inPosition(agent, 0.1f)) {
//            if (agent.component.weaken) {
//                agent.stateMachine.changeState(ESCAPE);
//            } else {
//                assert .stateMachine.changeState(PURSUE);
//            }
//        }
//    }
}
