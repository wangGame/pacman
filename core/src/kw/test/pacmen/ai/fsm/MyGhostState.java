package kw.test.pacmen.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kw.test.pacmen.ai.astar.MyAStarMap;
import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.manger.MyGameManager;

public enum  MyGhostState implements State<MyGhostAgent> {
    MOVE_UP(){
        /**
         * This is the state's normal update function
         *
         * @param entity the entity lasting the state
         */
        @Override
        public void update(MyGhostAgent entity) {
            entity.ghostComponent.currentState = MyGhostComponent.MOVE_UP;
            Body body = entity.ghostComponent.getBody();
            body.applyLinearImpulse(tempV1.set(0,entity.speed).scl(body.getMass()),body.getWorldCenter(),true);

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed){
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (checkHitWall(entity,MyGhostComponent.MOVE_UP)){
                changeState(entity,getRandomDirectionChoice(getDirectionChoices(entity,MyGhostComponent.MOVE_DOWN)));
                return;
            }
            
            if (entity.timer > 0.5F && inPosition(entity,0.05F)){
                entity.timer = 0;
                int  newState = getRandomDirectionChoice(getDirectionChoices(entity,MyGhostComponent.MOVE_DOWN));
                if (newState != entity.ghostComponent.currentState){
                    changeState(entity,newState);
                    return;
                }
            }
            
            if (entity.ghostComponent.warken){
                entity.ghostComponent.currentState = MyGhostComponent.ESCAPE;
                if (entity.ghostComponent.hp <= 0 && inPosition(entity,0.1F)){
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }
        }
    },
    MOVE_DOWN(){
        @Override
        public void update(MyGhostAgent entity) {
            entity.ghostComponent.currentState = MyGhostComponent.MOVE_DOWN;
            Body body = entity.ghostComponent.getBody();
            body.applyLinearImpulse(tempV1.set(0,-entity.speed).scl(body.getMass()),body.getWorldCenter(),true);

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (checkHitWall(entity, MyGhostComponent.MOVE_DOWN)) {
                changeState(entity, getRandomDirectionChoice(getDirectionChoices(entity, MyGhostComponent.MOVE_UP)));
                return;
            }

            if (entity.timer > 0.5f && inPosition(entity, 0.05f)) {
                entity.timer = 0;
                int newState = getRandomDirectionChoice(getDirectionChoices(entity, MyGhostComponent.MOVE_UP));
                if (newState != entity.ghostComponent.currentState) {
                    changeState(entity, newState);
                    return;
                }
            }

            if (entity.ghostComponent.warken) {
                entity.ghostComponent.currentState = MyGhostComponent.ESCAPE;
                if (entity.ghostComponent.hp <= 0 && inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }

            if (nearPlayer(entity, PURSUE_RADIUS) && (MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible) && inPosition(entity, 0.1f)) {
                if (entity.ghostComponent.warken) {
                    entity.stateMachine.changeState(ESACPE);
                } else {
                    entity.stateMachine.changeState(PURSUE);
                }
            }
        }
    },
    MOVE_LEFT(){
        @Override
        public void update(MyGhostAgent entity) {
            entity.ghostComponent.currentState = MyGhostComponent.MOVE_LEFT;

            Body body = entity.ghostComponent.getBody();
            body.applyLinearImpulse(tempV1.set(-entity.speed, 0).scl(body.getMass()), body.getWorldCenter(), true);

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (checkHitWall(entity, MyGhostComponent.MOVE_LEFT)) {
                changeState(entity, getRandomDirectionChoice(getDirectionChoices(entity, MyGhostComponent.MOVE_RIGHT)));
                return;
            }

            if (entity.timer > 0.5f && inPosition(entity, 0.05f)) {
                entity.timer = 0;
                int newState = getRandomDirectionChoice(getDirectionChoices(entity, MyGhostComponent.MOVE_RIGHT));
                if (newState != entity.ghostComponent.currentState) {
                    changeState(entity, newState);
                    return;
                }
            }

            if (entity.ghostComponent.warken) {
                entity.ghostComponent.currentState = MyGhostComponent.ESCAPE;

                if (entity.ghostComponent.hp <= 0 && inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }

            if (nearPlayer(entity, PURSUE_RADIUS) && (MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible) && inPosition(entity, 0.1f)) {
                if (entity.ghostComponent.warken) {
                    entity.stateMachine.changeState(ESACPE);
                } else {
                    entity.stateMachine.changeState(PURSUE);
                }
            }
        }
    },
    MOVE_RIGHT(){
        @Override
        public void update(MyGhostAgent entity) {
            entity.ghostComponent.currentState = MyGhostComponent.MOVE_RIGHT;

            Body body = entity.ghostComponent.getBody();
            body.applyLinearImpulse(tempV1.set(entity.speed, 0).scl(body.getMass()), body.getWorldCenter(), true);

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (checkHitWall(entity, MyGhostComponent.MOVE_RIGHT)) {
                changeState(entity, getRandomDirectionChoice(getDirectionChoices(entity, MyGhostComponent.MOVE_LEFT)));
                return;
            }

            if (entity.timer > 0.5f && inPosition(entity, 0.05f)) {
                entity.timer = 0;
                int newState = getRandomDirectionChoice(getDirectionChoices(entity, MyGhostComponent.MOVE_LEFT));
                if (newState != entity.ghostComponent.currentState) {
                    changeState(entity, newState);
                    return;
                }
            }

            if (entity.ghostComponent.warken) {
                entity.ghostComponent.currentState = MyGhostComponent.ESCAPE;

                if (entity.ghostComponent.hp <= 0 && inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(DIE);
                    return;
                }
            }

            if (nearPlayer(entity, PURSUE_RADIUS) && (MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible) && inPosition(entity, 0.1f)) {
                if (entity.ghostComponent.warken) {
                    entity.stateMachine.changeState(ESACPE);
                } else {
                    entity.stateMachine.changeState(PURSUE);
                }
            }
        }
    },
    PURSUE(){
        @Override
        public void update(MyGhostAgent entity) {
            // run after the player
            if (MyGameManager.getinstance().playerLocation == null || !(MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible)) {
                changeState(entity, MathUtils.random(0, 3));
                return;
            }

            // do path finding every 0.1 second
            if (entity.nextNode == null || entity.timer > 0.1f) {
                entity.nextNode = MyGameManager.getinstance().pathfinder.findNextNode(entity.getPosition(), MyGameManager.getinstance().playerLocation.getPosition());
                entity.timer = 0;
            }
            if (entity.nextNode == null) {
                // no path found or player is dead
                changeState(entity, MathUtils.random(0, 3));
                return;
            }

            float x = (entity.nextNode.x - MathUtils.floor(entity.getPosition().x)) * entity.speed;
            float y = (entity.nextNode.y - MathUtils.floor(entity.getPosition().y)) * entity.speed;

            Body body = entity.ghostComponent.getBody();

            if (body.getLinearVelocity().isZero(0.1f) || inPosition(entity, 0.2f)) {
                body.applyLinearImpulse(tempV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
            }

            if (x > 0) {
                entity.ghostComponent.currentState = MyGhostComponent.MOVE_RIGHT;
            } else if (x < 0) {
                entity.ghostComponent.currentState = MyGhostComponent.MOVE_LEFT;
            } else if (y > 0) {
                entity.ghostComponent.currentState = MyGhostComponent.MOVE_UP;
            } else if (y < 0) {
                entity.ghostComponent.currentState = MyGhostComponent.MOVE_DOWN;
            }

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (!nearPlayer(entity, PURSUE_RADIUS) && inPosition(entity, 0.1f)) {
                changeState(entity, entity.ghostComponent.currentState);
                return;
            }

            if (entity.ghostComponent.warken) {
                entity.ghostComponent.currentState = MyGhostComponent.ESCAPE;
                if (inPosition(entity, 0.1f)) {
                    entity.stateMachine.changeState(ESACPE);
                }
            }
        }
    },
    ESACPE(){
        @Override
        public void update(MyGhostAgent entity) {
            // get away from the player
            entity.ghostComponent.currentState = MyGhostComponent.ESCAPE;

            // update path every 0.2f
            if (entity.nextNode == null || entity.timer > 0.2f) {
                MyAStarMap map = MyGameManager.getinstance().pathfinder.myAStarMap;

                float x = (MyGameManager.getinstance().playerLocation.getPosition().x + map.getWidth() / 2);
                float y = (MyGameManager.getinstance().playerLocation.getPosition().y + map.getHeight() / 2);

                do {
                    x += 1;
                    y += 1;
                    x = x > map.getWidth() ? x - map.getWidth() : x;
                    y = y > map.getHeight() ? y - map.getHeight() : y;
                } while (map.getNodeAt(MathUtils.floor(x), MathUtils.floor(y)).isWall);

                tempV1.set(x, y);
                entity.nextNode = MyGameManager.getinstance().pathfinder.findNextNode(entity.getPosition(), tempV1);
                entity.timer = 0;
            }

            if (entity.nextNode == null || !nearPlayer(entity, PURSUE_RADIUS + 1)) {
                // no path found or away from the player
                changeState(entity, MathUtils.random(0, 3));
                return;
            }

            float x = (entity.nextNode.x - MathUtils.floor(entity.getPosition().x)) * entity.speed;
            float y = (entity.nextNode.y - MathUtils.floor(entity.getPosition().y)) * entity.speed;

            Body body = entity.ghostComponent.getBody();

            if (body.getLinearVelocity().isZero(0.1f) || inPosition(entity, 0.1f)) {
                body.applyLinearImpulse(tempV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
            }

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed / body.getLinearVelocity().len()));
            }

            if (!entity.ghostComponent.warken && inPosition(entity, 0.1f)) {
                entity.stateMachine.changeState(PURSUE);
                return;
            }

            if (entity.ghostComponent.hp <= 0 && inPosition(entity, 0.1f)) {
                entity.stateMachine.changeState(DIE);
            }
        }
    },
    DIE(){
        @Override
        public void update(MyGhostAgent entity) {
            entity.ghostComponent.currentState = MyGhostComponent.DIE;
            // respawn when getting back to the respawning postion
            // update path every 0.2f
            if (entity.nextNode == null || entity.timer > 0.2f) {
                entity.nextNode = MyGameManager.getinstance().pathfinder.findNextNode(entity.getPosition(), MyGameManager.getinstance().ghostSpawnPos);
                entity.timer = 0;
            }

            if (entity.nextNode == null || entity.getPosition().dst2(MyGameManager.getinstance().ghostSpawnPos) < 0.04f) {
                // no path found or reach target
                entity.ghostComponent.getBody().setTransform(MyGameManager.getinstance().ghostSpawnPos, 0);
                entity.stateMachine.changeState(RESPAWN);
                return;
            }

            float x = (entity.nextNode.x - MathUtils.floor(entity.getPosition().x)) * entity.speed;
            float y = (entity.nextNode.y - MathUtils.floor(entity.getPosition().y)) * entity.speed;

            Body body = entity.ghostComponent.getBody();

            if (body.getLinearVelocity().isZero(0.1f) || inPosition(entity, 0.2f)) {
                body.applyLinearImpulse(tempV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
            }

            if (body.getLinearVelocity().len2() > entity.speed * entity.speed * 4) {
                body.setLinearVelocity(body.getLinearVelocity().scl(entity.speed * 2 / body.getLinearVelocity().len()));
            }

        }
    },
    RESPAWN(){
        @Override
        public void update(MyGhostAgent entity) {
            entity.ghostComponent.respawn();
            entity.stateMachine.changeState(MOVE_UP);
        }
    };

    protected boolean nearPlayer(MyGhostAgent entity,float distance){
        if (MyGameManager.getinstance().playerLocation == null){
            return false;
        }
        Vector2 pos = entity.getPosition();
        Vector2 playerPos = MyGameManager.getinstance().playerLocation.getPosition();
        return pos.dst2(playerPos)< distance*distance;
    }

    protected RayCastCallback rayCastCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if (fixture.getFilterData().categoryBits == MyGameManager.getinstance().WALL_BIT){
                hitWall = true;
                return 0;
            }
            return 0;
        }
    };

    /**
     * This method will execute when the state is entered.
     *
     * @param entity the entity entering the state
     */
    @Override
    public void enter(MyGhostAgent entity) {
        entity.ghostComponent.getBody().setLinearVelocity(0,0);
        if (!inPosition(entity,0.1F)){
            entity.ghostComponent.getBody().setTransform(tempV1.set(MathUtils.floor(entity.getPosition().x)+0.5F,MathUtils.floor(entity.getPosition().y)+0.5F),0);
        }
        entity.timer = 0;
    }

    protected boolean inPosition(MyGhostAgent entity,float radius){
        float x = entity.getPosition().x;
        float y = entity.getPosition().y;

        float xLow = MathUtils.floor(x) + 0.5F - radius;
        float xHight = MathUtils.floor(x) + 0.5F + radius;

        float yLow = MathUtils.floor(y) + 0.5F - radius;
        float yHight = MathUtils.floor(y) + 0.5F + radius;

        return xLow < x && x < xHight && yLow < y && y < yHight;
    }
    /**
     * This method will execute when the state is exited.
     *
     * @param entity the entity exiting the state
     */
    @Override
    public void exit(MyGhostAgent entity) {
        entity.nextNode = null;
    }

    /**
     * This method executes if the {@code entity} receives a {@code telegram} from the message dispatcher while it is in this
     * state.
     *
     * @param entity   the entity that received the message
     * @param telegram the message sent to the entity
     * @return true if the message has been successfully handled; false otherwise.
     */
    @Override
    public boolean onMessage(MyGhostAgent entity, Telegram telegram) {
        return false;
    }

    protected static final Vector2 tempV1 = new Vector2();
    protected static final Vector2 tempV2 = new Vector2();
    protected static final List<Integer> choicesList = new ArrayList<>();
    protected static boolean hitWall = false;
    protected static final float RADIUS = 0.55F;
    protected static final float PURSUE_RADIUS = 5F;

    protected boolean checkHitWall(MyGhostAgent entity,int state){
        Body body = entity.ghostComponent.getBody();
        World world = body.getWorld();
        hitWall = false;
        tempV1.set(body.getWorldCenter());
        switch (state){
            case MyGhostComponent.MOVE_UP:
                tempV2.set(tempV1).add(0,RADIUS);
                break;
            case MyGhostComponent.MOVE_DOWN:
                tempV2.set(tempV1).add(0,-RADIUS);
                break;
            case MyGhostComponent.MOVE_LEFT:
                tempV2.set(tempV1).add(-RADIUS,0);
                break;
            case MyGhostComponent.MOVE_RIGHT:
                tempV2.set(tempV1).add(RADIUS,0);
                break;
            default:
                tempV2.setZero();
                break;
        }
        world.rayCast(rayCastCallback,tempV1,tempV2);
        return hitWall;
    }

    protected void changeState(MyGhostAgent entity,int state){
        switch (state) {
            case MyGhostComponent.MOVE_UP: // UP
                entity.stateMachine.changeState(MOVE_UP);
                break;
            case MyGhostComponent.MOVE_DOWN: // DOWN
                entity.stateMachine.changeState(MOVE_DOWN);
                break;
            case MyGhostComponent.MOVE_LEFT: // LEFT
                entity.stateMachine.changeState(MOVE_LEFT);
                break;
            case MyGhostComponent.MOVE_RIGHT: // RIGHT
                entity.stateMachine.changeState(MOVE_RIGHT);
                break;
            case MyGhostComponent.ESCAPE: // ESCAPE
                entity.stateMachine.changeState(ESACPE);
                break;
            case MyGhostComponent.DIE: // DIE
                entity.stateMachine.changeState(DIE);
                break;
            default:
                break;
        }
    }

    protected int getRandomDirectionChoice(Integer[] choices){
        if (choices.length == 0){
            return 0;
        }
        int length = choices.length;
        return choices[MathUtils.random(length - 1)];
    }

    protected Integer[] getDirectionChoices(MyGhostAgent entity,int state){
        Body body = entity.ghostComponent.getBody();
        World world = body.getWorld();

        /**
         * 四个方向
         */
        choicesList.clear();
        for (int i = 0; i < 4; i++) {
            choicesList.add(i);
        }
        //不选当前
        choicesList.remove(state);

        tempV1.set(body.getWorldCenter());
        Iterator<Integer> itor = choicesList.iterator();
        while (itor.hasNext()){
            Integer integer = itor.next();
            hitWall = false;
            switch (integer){
                case MyGhostComponent.MOVE_UP: // UP
                    tempV2.set(tempV1).add(0, RADIUS);
                    break;
                case MyGhostComponent.MOVE_DOWN: // DOWN
                    tempV2.set(tempV1).add(0, -RADIUS);
                    break;
                case MyGhostComponent.MOVE_LEFT: // LEFT
                    tempV2.set(tempV1).add(-RADIUS, 0);
                    break;
                case MyGhostComponent.MOVE_RIGHT: // RIGHT
                    tempV2.set(tempV1).add(RADIUS, 0);
                    break;
                default:
                    tempV2.setZero();
                    break;
            }
            world.rayCast(rayCastCallback,tempV1,tempV2);
            if (hitWall){
                itor.remove();
            }
        }
        Integer[] result = choicesList.toArray(new Integer[choicesList.size()]);
        return result;
    }
}
