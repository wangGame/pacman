package kw.test.pacmen.actor;

import com.badlogic.gdx.math.Vector2;

import kw.test.pacmen.ai.astar.MyNode;
import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.manger.MyGameManager;

public class GhostActor extends AnimationActor{

    public int currentState = MyGhostComponent.MOVE_UP;
    public boolean warken;
    public int hp;
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
        System.out.println("---------------"+timer);
        if (MyGameManager.getinstance().bigPillEaten){
            warken = true;
        }

        if (warken) {
            weakenTime += delta;
            if (weakenTime > 1) {
                warken = false;

            }
        }

        ai.update();
    }

    public void respawn() {
        hp = 1;
        warken = false;
    }

    public Vector2 getPosition() {
        return getBody().getPosition();
    }
}
