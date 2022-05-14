package kw.test.pacmen.actor;

import kw.test.pacmen.manger.MyGameManager;

public class PlayerActor extends AnimationActor {
    public int hp;

    @Override
    public void act(float delta) {
        super.act(delta);
        MyGameManager.getinstance().playerLocation = getBody().getPosition();
    }
}
