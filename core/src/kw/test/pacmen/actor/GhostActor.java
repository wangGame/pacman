package kw.test.pacmen.actor;

import com.badlogic.gdx.math.Vector2;

import kw.test.pacmen.ai.astar.MyNode;
import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.manger.MyGameManager;

public class GhostActor extends AnimationActor{

    public int currentState = MyGhostComponent.MOVE_UP;
    public boolean warken;
    public int hp = 100;
    public float speed = 2.4F;
    public float timer = 0;
//    public boolean weaken;
    public MyNode nextNode;
    private GhostActorAi ai;
    private float weakenTime;

    public GhostActor(){
        super();
        ai = new GhostActorAi(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timer += delta;
        if (MyGameManager.getinstance().bigPillEaten){
            warken = true;
            weakenTime = 0;
            System.out.println(warken);
        }
        if (warken) {
            weakenTime += delta;
            if (weakenTime > 3F) {
                warken = false;
            }
        }
        ai.update();


        if (getBody().getPosition().x <= 0) {
            getBody().setTransform(19.0f, getBody().getPosition().y, 0);
        }

        else if (getBody().getPosition().x >= 19f) {
            getBody().setTransform(0, getBody().getPosition().y, 0);
        }
    }

    public void respawn() {
        hp = 1;
//        warken = false;
    }

    public Vector2 getPosition() {
        return getBody().getPosition();
    }
}
