package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

import kw.test.pacmen.ai.fsm.MyGhostAgent;
import kw.test.pacmen.ai.fsm.MyGhostState;

/**
 * 对于鬼  可以运动   它也有自己的状态
 */
public class MyGhostComponent implements Component {
    public static final int MOVE_UP = 0;
    public static final int MOVE_DOWN = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;
    public static final int ESCAPE = 4;
    public static final int DIE = 5;

    public static final float WEAK_TIME = 10F;
    public float weak_time;
    public MyGhostAgent ghostAgent;
    private final Body body;
    public int currentState;
    public boolean warken;
    public int hp;

    public MyGhostComponent(Body body){
        this.body = body;
        ghostAgent = new MyGhostAgent(this);
        ghostAgent.stateMachine.setInitialState(MyGhostState.MOVE_UP);
        warken = false;
        hp = 1;
    }

    public Body getBody() {
        return body;
    }

    public void respawn(){
        hp = 1;
        warken = false;
    }
}
