package kw.test.pacmen.actor;

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

public class GhostActorAi {
    private GhostActor ghostActor;
    public GhostActorAi(GhostActor ghostActor){
        this.ghostActor = ghostActor;
    }

    public void update(){
        if (ghostActor.nextNode == null || ghostActor.timer > 0.2f) {
            ghostActor.nextNode = MyGameManager.getinstance().pathfinder.findNextNode(ghostActor.getPosition(), MyGameManager.getinstance().ghostSpawnPos);
            ghostActor.timer = 0;
            System.out.println(ghostActor.nextNode+"----------------------");
        }

        if (ghostActor.currentState == MyGhostComponent.MOVE_UP){
            moveUp();
        }else if (ghostActor.currentState == MyGhostComponent.MOVE_DOWN){
            moveDown();
        }else if (ghostActor.currentState == MyGhostComponent.MOVE_LEFT){
            moveLeft();
        }else if (ghostActor.currentState == MyGhostComponent.MOVE_RIGHT){
            moveRight();
        }else if (ghostActor.currentState == MyGhostComponent.PURSUE){
            PURSUE();
        }else if (ghostActor.currentState == MyGhostComponent.ESCAPE){
            ESCAPE();
        }else if (ghostActor.currentState == MyGhostComponent.DIE){
            DIE();
        }else if (ghostActor.currentState == MyGhostComponent.RESPAWN){
            RESPAWN();
        }
    }

    public void moveUp() {
        ghostActor.currentState = MyGhostComponent.MOVE_UP;
        Body body = ghostActor.getBody();
        body.applyLinearImpulse(tmpV1.set(0, ghostActor.speed).scl(body.getMass()), body.getWorldCenter(), true);

        if (body.getLinearVelocity().len2() > ghostActor.speed * ghostActor.speed) {
            body.setLinearVelocity(body.getLinearVelocity().scl(ghostActor.speed / body.getLinearVelocity().len()));
        }

        if (checkHitWall(ghostActor, MyGhostComponent.MOVE_UP)) {
            changeState(ghostActor, getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_DOWN)));
            return;
        }

        if (ghostActor.timer > 0.5f && inPosition(ghostActor, 0.05f)) {
            ghostActor.timer = 0;
            int newState = getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_DOWN));
            if (newState != ghostActor.currentState) {
                changeState(ghostActor, newState);
                return;
            }
        }

        if (ghostActor.weaken) {
            ghostActor.currentState = MyGhostComponent.ESCAPE;
            if (ghostActor.hp <= 0 && inPosition(ghostActor, 0.1f)) {
                ghostActor.currentState = MyGhostComponent.DIE;
                return;
            }
        }

        if (nearPlayer(ghostActor, PURSUE_RADIUS) &&
                (MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible)
                && inPosition(ghostActor, 0.1f)) {
            if (ghostActor.weaken) {
                ghostActor.currentState = MyGhostComponent.ESCAPE;
            } else {
                ghostActor.currentState = MyGhostComponent.PURSUE;
            }
        }
    }

    public void moveDown() {
        ghostActor.currentState = MyGhostComponent.MOVE_DOWN;

        Body body = ghostActor.getBody();
        body.applyLinearImpulse(tmpV1.set(0, -ghostActor.speed).scl(body.getMass()), body.getWorldCenter(), true);

        if (body.getLinearVelocity().len2() > ghostActor.speed * ghostActor.speed) {
            body.setLinearVelocity(body.getLinearVelocity().scl(ghostActor.speed / body.getLinearVelocity().len()));
        }

        if (checkHitWall(ghostActor, MyGhostComponent.MOVE_DOWN)) {
            changeState(ghostActor, getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_UP)));
            return;
        }

        if (ghostActor.timer > 0.5f && inPosition(ghostActor, 0.05f)) {
            ghostActor.timer = 0;
            int newState = getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_UP));
            if (newState != ghostActor.currentState) {
                changeState(ghostActor, newState);
                return;
            }
        }

        if (ghostActor.weaken) {
            ghostActor.currentState = MyGhostComponent.ESCAPE;
            if (ghostActor.hp <= 0 && inPosition(ghostActor, 0.1f)) {
                ghostActor.currentState = MyGhostComponent.DIE;
                return;
            }
        }

        if (nearPlayer(ghostActor, PURSUE_RADIUS) && (MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible) && inPosition(ghostActor, 0.1f)) {
            if (ghostActor.weaken) {
                ghostActor.currentState = MyGhostComponent.ESCAPE;
            } else {
                ghostActor.currentState = MyGhostComponent.PURSUE;
            }
        }
    }

    public void moveLeft() {
        ghostActor.currentState = MyGhostComponent.MOVE_LEFT;

        Body body = ghostActor.getBody();
        body.applyLinearImpulse(tmpV1.set(-ghostActor.speed, 0).scl(body.getMass()), body.getWorldCenter(), true);

        if (body.getLinearVelocity().len2() > ghostActor.speed * ghostActor.speed) {
            body.setLinearVelocity(body.getLinearVelocity().scl(ghostActor.speed / body.getLinearVelocity().len()));
        }

        if (checkHitWall(ghostActor, MyGhostComponent.MOVE_LEFT)) {
            changeState(ghostActor, getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_RIGHT)));
            return;
        }

        if (ghostActor.timer > 0.5f && inPosition(ghostActor, 0.05f)) {
            ghostActor.timer = 0;
            int newState = getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_RIGHT));
            if (newState != ghostActor.currentState) {
                changeState(ghostActor, newState);
                return;
            }
        }

        if (ghostActor.weaken) {
            ghostActor.currentState = MyGhostComponent.ESCAPE;

            if (ghostActor.hp <= 0 && inPosition(ghostActor, 0.1f)) {
                ghostActor.currentState = MyGhostComponent.DIE;
                return;
            }
        }

        if (nearPlayer(ghostActor, PURSUE_RADIUS) && (MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible) && inPosition(ghostActor, 0.1f)) {
            if (ghostActor.weaken) {
                ghostActor.currentState = MyGhostComponent.ESCAPE;
            } else {
                ghostActor.currentState = MyGhostComponent.PURSUE;
            }
        }
    }

    public void moveRight() {
        ghostActor.currentState = MyGhostComponent.MOVE_RIGHT;

        Body body = ghostActor.getBody();
        body.applyLinearImpulse(tmpV1.set(ghostActor.speed, 0).scl(body.getMass()), body.getWorldCenter(), true);

        if (body.getLinearVelocity().len2() > ghostActor.speed * ghostActor.speed) {
            body.setLinearVelocity(body.getLinearVelocity().scl(ghostActor.speed / body.getLinearVelocity().len()));
        }

        if (checkHitWall(ghostActor, MyGhostComponent.MOVE_RIGHT)) {
            changeState(ghostActor, getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_LEFT)));
            return;
        }

        if (ghostActor.timer > 0.5f && inPosition(ghostActor, 0.05f)) {
            ghostActor.timer = 0;
            int newState = getRandomDirectionChoice(getDirectionChoices(ghostActor, MyGhostComponent.MOVE_LEFT));
            if (newState != ghostActor.currentState) {
                changeState(ghostActor, newState);
                return;
            }
        }

        if (ghostActor.weaken) {
            ghostActor.currentState = MyGhostComponent.ESCAPE;

            if (ghostActor.hp <= 0 && inPosition(ghostActor, 0.1f)) {
                ghostActor.currentState = MyGhostComponent.DIE;
                return;
            }
        }

        if (nearPlayer(ghostActor, PURSUE_RADIUS) && (MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible) && inPosition(ghostActor, 0.1f)) {
            if (ghostActor.weaken) {
                ghostActor.currentState = MyGhostComponent.ESCAPE;
            } else {
                ghostActor.currentState = MyGhostComponent.PURSUE;
            }
        }
    }

    public void PURSUE() {
     // run after the player
        if (MyGameManager.getinstance().playerLocation == null || !(MyGameManager.getinstance().playerIsAlive && !MyGameManager.getinstance().playerIsInvincible)) {
            changeState(ghostActor, MathUtils.random(0, 3));
            return;
        }

        // do path finding every 0.1 second
        if (ghostActor.nextNode == null || ghostActor.timer > 0.1f) {
            ghostActor.nextNode = MyGameManager.getinstance().pathfinder.findNextNode(ghostActor.getPosition(), MyGameManager.getinstance().playerLocation);
            System.out.println(ghostActor.nextNode+"----------------------");
            ghostActor.timer = 0;
        }
        if (ghostActor.nextNode == null) {
            // no path found or player is dead
            changeState(ghostActor, MathUtils.random(0, 3));
            return;
        }

        float x = (ghostActor.nextNode.x - MathUtils.floor(ghostActor.getPosition().x)) * ghostActor.speed;
        float y = (ghostActor.nextNode.y - MathUtils.floor(ghostActor.getPosition().y)) * ghostActor.speed;

        Body body = ghostActor.getBody();

        if (body.getLinearVelocity().isZero(0.1f) || inPosition(ghostActor, 0.2f)) {
            body.applyLinearImpulse(tmpV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
        }

        if (x > 0) {
            ghostActor.currentState = MyGhostComponent.MOVE_LEFT;
        } else if (x < 0) {
            ghostActor.currentState = MyGhostComponent.MOVE_RIGHT;
        } else if (y > 0) {
            ghostActor.currentState = MyGhostComponent.MOVE_DOWN;
        } else if (y < 0) {
            ghostActor.currentState = MyGhostComponent.MOVE_UP;
        }

        if (body.getLinearVelocity().len2() > ghostActor.speed * ghostActor.speed) {
            body.setLinearVelocity(body.getLinearVelocity().scl(ghostActor.speed / body.getLinearVelocity().len()));
        }

        if (!nearPlayer(ghostActor, PURSUE_RADIUS) && inPosition(ghostActor, 0.1f)) {
            changeState(ghostActor, ghostActor.currentState);
            return;
        }

        if (ghostActor.weaken) {
            ghostActor.currentState = MyGhostComponent.ESCAPE;
            if (inPosition(ghostActor, 0.1f)) {
                ghostActor.currentState = MyGhostComponent.ESCAPE;
            }
        }
    }
    public void ESCAPE() {
        // get away from the player
        ghostActor.currentState = MyGhostComponent.ESCAPE;

        // update path every 0.2f
        if (ghostActor.nextNode == null || ghostActor.timer > 0.2f) {
            MyAStarMap map = MyGameManager.getinstance().pathfinder.myAStarMap;

            float x = (MyGameManager.getinstance().playerLocation.x + map.getWidth() / 2);
            float y = (MyGameManager.getinstance().playerLocation.y + map.getHeight() / 2);

            do {
                x += 1;
                y += 1;
                x = x > map.getWidth() ? x - map.getWidth() : x;
                y = y > map.getHeight() ? y - map.getHeight() : y;
            } while (map.getNodeAt(MathUtils.floor(x), MathUtils.floor(y)).isWall);

            tmpV1.set(x, y);
            ghostActor.nextNode = MyGameManager.getinstance().pathfinder.findNextNode(ghostActor.getPosition(), tmpV1);
            ghostActor.timer = 0;
            System.out.println(ghostActor.nextNode+"----------------------");
        }

        if (ghostActor.nextNode == null || !nearPlayer(ghostActor, PURSUE_RADIUS + 1)) {
            // no path found or away from the player
            changeState(ghostActor, MathUtils.random(0, 3));
            return;
        }

        float x = (ghostActor.nextNode.x - MathUtils.floor(ghostActor.getPosition().x)) * ghostActor.speed;
        float y = (ghostActor.nextNode.y - MathUtils.floor(ghostActor.getPosition().y)) * ghostActor.speed;

        Body body = ghostActor.getBody();

        if (body.getLinearVelocity().isZero(0.1f) || inPosition(ghostActor, 0.1f)) {
            body.applyLinearImpulse(tmpV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
        }

        if (body.getLinearVelocity().len2() > ghostActor.speed * ghostActor.speed) {
            body.setLinearVelocity(body.getLinearVelocity().scl(ghostActor.speed / body.getLinearVelocity().len()));
        }

        if (!ghostActor.weaken && inPosition(ghostActor, 0.1f)) {
            ghostActor.currentState = MyGhostComponent.PURSUE;
            return;
        }

        if (ghostActor.hp <= 0 && inPosition(ghostActor, 0.1f)) {
            ghostActor.currentState = MyGhostComponent.DIE;
        }
    }
    public void DIE() {
        ghostActor.currentState = MyGhostComponent.DIE;
        // respawn when getting back to the respawning postion
        // update path every 0.2f
        if (ghostActor.nextNode == null || ghostActor.timer > 0.2f) {
            ghostActor.nextNode = MyGameManager.getinstance().pathfinder.findNextNode(ghostActor.getPosition(), MyGameManager.getinstance().ghostSpawnPos);
            ghostActor.timer = 0;
            System.out.println(ghostActor.nextNode+"----------------------");
        }

        if (ghostActor.nextNode == null || ghostActor.getPosition().dst2(MyGameManager.getinstance().ghostSpawnPos) < 0.04f) {
            // no path found or reach target
            ghostActor.getBody().setTransform(MyGameManager.getinstance().ghostSpawnPos, 0);
            ghostActor.currentState = MyGhostComponent.RESPAWN;
            return;
        }

        float x = (ghostActor.nextNode.x - MathUtils.floor(ghostActor.getPosition().x)) * ghostActor.speed;
        float y = (ghostActor.nextNode.y - MathUtils.floor(ghostActor.getPosition().y)) * ghostActor.speed;

        Body body = ghostActor.getBody();

        if (body.getLinearVelocity().isZero(0.1f) || inPosition(ghostActor, 0.2f)) {
            body.applyLinearImpulse(tmpV1.set(x, y).scl(body.getMass()), body.getWorldCenter(), true);
        }

        if (body.getLinearVelocity().len2() > ghostActor.speed * ghostActor.speed * 4) {
            body.setLinearVelocity(body.getLinearVelocity().scl(ghostActor.speed * 2 / body.getLinearVelocity().len()));
        }
    }
    public void RESPAWN() {
        ghostActor.respawn();
        ghostActor.currentState = MyGhostComponent.MOVE_UP;
    };

    protected boolean nearPlayer(GhostActor ghostActor, float distance) {
        if (MyGameManager.getinstance().playerLocation == null) {
            return false;
        }
        Vector2 pos = ghostActor.getBody().getPosition();
        Vector2 playerPos = MyGameManager.getinstance().playerLocation;

        return pos.dst2(playerPos) < distance * distance;
    }

    protected boolean inPosition(GhostActor ghostActor, float radius) {
        float x = ghostActor.getPosition().x;
        float y = ghostActor.getPosition().y;

        float xLow = MathUtils.floor(x) + 0.5f - radius;
        float xHight = MathUtils.floor(x) + 0.5f + radius;

        float yLow = MathUtils.floor(y) + 0.5f - radius;
        float yHight = MathUtils.floor(y) + 0.5f + radius;

        return xLow < x && x < xHight && yLow < y && y < yHight;
    }

    protected void changeState(GhostActor ghostActor, int state) {
        switch (state) {
            case MyGhostComponent.MOVE_UP: // UP
//                ghostActor.changeState(MOVE_UP);
                ghostActor.currentState = MyGhostComponent.MOVE_UP;
                break;
            case MyGhostComponent.MOVE_DOWN: // DOWN
                ghostActor.currentState = MyGhostComponent.MOVE_DOWN;
                break;
            case MyGhostComponent.MOVE_LEFT: // LEFT
                ghostActor.currentState = MyGhostComponent.MOVE_LEFT;
                break;
            case MyGhostComponent.MOVE_RIGHT: // RIGHT
                ghostActor.currentState = MyGhostComponent.MOVE_RIGHT;
                break;
            case MyGhostComponent.ESCAPE: // ESCAPE
                ghostActor.currentState = MyGhostComponent.ESCAPE;
                break;
            case MyGhostComponent.DIE: // DIE
                ghostActor.currentState = MyGhostComponent.DIE;
                break;
            default:
                break;
        }
    }

    protected static final Vector2 tmpV1 = new Vector2();
    protected static final Vector2 tmpV2 = new Vector2();
    protected static final List<Integer> choicesList = new ArrayList<>(4);
    protected static boolean hitWall = false;

    protected static final float RADIUS = 0.55f;

    protected static final float PURSUE_RADIUS = 5f;

    protected boolean checkHitWall(GhostActor ghostActor, int state) {
        Body body = ghostActor.getBody();
        World world = body.getWorld();
        hitWall = false;

        tmpV1.set(body.getWorldCenter());

        switch (state) {
            case MyGhostComponent.MOVE_UP:
                tmpV2.set(tmpV1).add(0, RADIUS);
                break;
            case MyGhostComponent.MOVE_DOWN:
                tmpV2.set(tmpV1).add(0, -RADIUS);
                break;
            case MyGhostComponent.MOVE_LEFT:
                tmpV2.set(tmpV1).add(-RADIUS, 0);
                break;
            case MyGhostComponent.MOVE_RIGHT:
                tmpV2.set(tmpV1).add(RADIUS, 0);
                break;
            default:
                tmpV2.setZero();
                break;
        }
        world.rayCast(rayCastCallback, tmpV1, tmpV2);

        return hitWall;
    }

    protected Integer[] getDirectionChoices(GhostActor ghostActor, int state) {
        Body body = ghostActor.getBody();
        World world = body.getWorld();

        choicesList.clear();
        for (int i = 0; i < 4; i++) {
            choicesList.add(i);
        }

        choicesList.remove(state);

        tmpV1.set(body.getWorldCenter());

        Iterator<Integer> itor = choicesList.iterator();
        while (itor.hasNext()) {
            Integer integer = itor.next();

            hitWall = false;
            switch (integer) {
                case MyGhostComponent.MOVE_UP: // UP
                    tmpV2.set(tmpV1).add(0, RADIUS);
                    break;
                case MyGhostComponent.MOVE_DOWN: // DOWN
                    tmpV2.set(tmpV1).add(0, -RADIUS);
                    break;
                case MyGhostComponent.MOVE_LEFT: // LEFT
                    tmpV2.set(tmpV1).add(-RADIUS, 0);
                    break;
                case MyGhostComponent.MOVE_RIGHT: // RIGHT
                    tmpV2.set(tmpV1).add(RADIUS, 0);
                    break;
                default:
                    tmpV2.setZero();
                    break;
            }

            world.rayCast(rayCastCallback, tmpV1, tmpV2);
            if (hitWall) {
                itor.remove();
            }
        }

        Integer[] result = choicesList.toArray(new Integer[choicesList.size()]);

        return result;
    }

    protected RayCastCallback rayCastCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if (fixture.getFilterData().categoryBits == MyGameManager.WALL_BIT) {
                hitWall = true;
                return 0;
            }
            return 0;
        }
    };

    protected int getRandomDirectionChoice(Integer[] choices) {
        if (choices.length == 0) {
            return 0;
        }
        int length = choices.length;
        return choices[MathUtils.random(length - 1)];
    }


    public void enter(GhostActor ghostActor) {
        ghostActor.getBody().setLinearVelocity(0, 0);
        if (!inPosition(ghostActor, 0.1f)) {
            ghostActor.getBody().setTransform(tmpV1.set(MathUtils.floor(ghostActor.getPosition().x) + 0.5f,
                    MathUtils.floor(ghostActor.getPosition().y) + 0.5f), 0);
        }
//        ghostActor.timer = 0;
    }

//    public void exit(GhostAgent ghostActor) {
//        ghostActor.nextNode = null;
//    }

//    @Override
//    public boolean onMessage(GhostAgent ghostActor, Telegram telegram) {
//        return false;
//    }
}
