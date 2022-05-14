package kw.test.pacmen.actor;

import com.badlogic.gdx.math.Vector2;

import kw.test.pacmen.ai.astar.MyNode;

public class GhostActor extends AnimationActor{

    public int currentState;
    public boolean warken;
    public int hp;
    public float speed = 2.4F;
    public float timer = 0;
    public boolean weaken;
    public MyNode nextNode;
    private GhostActorAi ai;

    public GhostActor(){
        super();
        ai = new GhostActorAi(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timer += delta;
        ai.update();
    }

    public void respawn() {
        hp = 1;
        weaken = false;
    }

    public Vector2 getPosition() {
        return getBody().getPosition();
    }
}
