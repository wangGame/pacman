package kw.test.pacmen.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import kw.test.pacmen.components.MyPlayerComponent;
import kw.test.pacmen.manger.MyGameManager;

public class PlayerActor extends AnimationActor {
    public int hp;
    public boolean wudi;
    private float time;
    public int current_dir;
    private final Vector2 tmpV1 = new Vector2();
    private int player = 0;

    public PlayerActor(){
        wudi = true;
        time = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        MyGameManager.getinstance().playerLocation = getBody().getPosition();
        if (wudi) {
            time += delta;
            if (time > 3){
                time = 0;
                wudi = false;
            }
        }
        if (player == 0) {
            initKeyInput();
        }else {
            initKeyInput1();
        }
        move();
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void move() {
//        AnimationActor animationActor = (AnimationActor) playerBody.getUserData();
        if (current_dir != MyPlayerComponent.IDLE) {
            setAniType(MyPlayerComponent.CURRENT_DIR);
        }
        if (current_dir == MyPlayerComponent.MOVE_LEFT) {
//            System.out.println("left");
            getBody().applyLinearImpulse(tmpV1.set(-3.6f, 0).scl(getBody().getMass()), getBody().getWorldCenter(), true);
        } else if (current_dir == MyPlayerComponent.MOVE_RIGHT) {
//            System.out.println("right");
            getBody().applyLinearImpulse(tmpV1.set(3.6f, 0).scl(getBody().getMass()), getBody().getWorldCenter(), true);
        } else if (current_dir == MyPlayerComponent.MOVE_UP) {
            getBody().applyLinearImpulse(tmpV1.set(0, 3.6f).scl(getBody().getMass()), getBody().getWorldCenter(), true);
//            System.out.println("down");
        } else if (current_dir == MyPlayerComponent.MOVE_DOWN) {
            getBody().applyLinearImpulse(tmpV1.set(0, -3.6f).scl(getBody().getMass()), getBody().getWorldCenter(), true);
//            System.out.println("up");
        }
        if (getBody().getLinearVelocity().len2() > 3.6F * 3.6F) {
            getBody().setLinearVelocity(getBody().getLinearVelocity().scl(3.6F / getBody().getLinearVelocity().len()));
        }
    }


    public void initKeyInput1(){
        current_dir = MyPlayerComponent.IDLE;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            current_dir = MyPlayerComponent.MOVE_LEFT;
        }else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            current_dir = MyPlayerComponent.MOVE_RIGHT;
        }else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            current_dir = MyPlayerComponent.MOVE_DOWN;
        }else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            current_dir = MyPlayerComponent.MOVE_UP;
        }
    }

    public void initKeyInput(){
        current_dir = MyPlayerComponent.IDLE;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            current_dir = MyPlayerComponent.MOVE_LEFT;
        }else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            current_dir = MyPlayerComponent.MOVE_RIGHT;
        }else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            current_dir = MyPlayerComponent.MOVE_DOWN;
        }else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            current_dir = MyPlayerComponent.MOVE_UP;
        }
    }
}
