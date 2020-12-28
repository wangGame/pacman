package kw.pacman.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

import kw.pacman.game.ai.GhostAgent;
import kw.pacman.game.ai.GhostState;

public class GhostComponent implements Component {
    public static final short MOVE_UP = 0;
    public static final short MOVE_DOWN = 1;
    public static final short MOVE_LEFT = 2;
    public static final short MOVE_RIGHT = 3;
    public static final int ESCAPE = 4;
    public static final int DIE = 5;
//    public static final float WEAK_TIME = 10F;

    public final Body body;
    public boolean weaken;
    public int hp;

    public float waken_time;
    public static final float WEAK_TIME = 10f;
    /*************************************************/
    public int currentState;

    public GhostAgent agent;
    public GhostComponent(Body body){
        this.body = body;
        weaken = false;
        agent = new GhostAgent(this);
        agent.stateMachine.setInitialState(GhostState.MOVE_UP);
        hp = 1;
    }

    public Body getBody() {
        return body;
    }

    public void respawn() {
        hp = 1;
        weaken = false;
    }
}
