package kw.pacman.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import kw.pacman.game.ai.PlayAgent;
import kw.pacman.game.ai.PlayState;
import kw.pacman.game.ai.PlayerAI;
import kw.pacman.game.system.PlaySystem;

public class PlayerComponent implements Component {
    public static final int IDLE = 0;
    public static final int IDLE_UP = 0;
    public static final int IDLE_DOWN = 1;
    public static final int IDLE_LEFT = 2;
    public static final int IDLE_RIGHT = 3;

    public static final int MOVE_UP = 4;
    public static final int MOVE_DOWN = 5;
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_RIGHT = 7;
    public static final int DIE = 8;

    public Body body;
    public Location<Vector2> ai;
    public float invicibleTime = 0;
    public int hp;
    public PlayAgent playAgent;
    public int currentState;
    public PlayerComponent(Body body) {
        this.body = body;
        ai = new PlayerAI(body);
        hp = 1;
        playAgent = new PlayAgent(this);
        playAgent.stateMachine.setInitialState(PlayState.MOVE_LEFT);
        invicibleTime = 0;
        currentState = MOVE_LEFT;
    }

    public Body getBody() {
        return body;
    }
}
