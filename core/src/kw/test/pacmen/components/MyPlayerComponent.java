package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.sun.xml.internal.bind.v2.model.core.ID;

import kw.test.pacmen.ai.MyPlayerAi;
import kw.test.pacmen.ai.fsm.MyPlayerAgent;
import kw.test.pacmen.ai.fsm.MyPlayerState;

public class MyPlayerComponent implements Component {
    //初始状态是那个方向的
    public static final int IDLE = 0;
    public static final int IDLE_UP = 0;
    public static final int IDLE_DOWN = 1;
    public static final int IDLE_LEFT = 2;
    public static final int IDLE_RIGHT = 3;

    public static int CURRENT_DIR = IDLE;


    public static final int MOVE_UP = 4;
    public static final int MOVE_DOWN = 5;
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_RIGHT = 7;
    public static final int DIE = 8;

    public MyPlayerAi ai;
    public MyPlayerAgent playerAgent;

    private final Body body;

    public int currentState;

    public int hp;

    //无敌计数器
    public float invincibleTimer;

    public MyPlayerComponent(Body body){
        this.body = body;
        ai = new MyPlayerAi(body);
        playerAgent = new MyPlayerAgent(this);
        playerAgent.stateStateMachine.setInitialState(MyPlayerState.IDLE_RIGHT);
        currentState = IDLE_RIGHT;
        hp = 1;
        invincibleTimer = 0;
    }

    public Body getBody(){
        return body;
    }
}
