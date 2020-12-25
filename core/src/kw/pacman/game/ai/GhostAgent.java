package kw.pacman.game.ai;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;

import kw.pacman.game.components.GhostComponent;

public class GhostAgent implements Telegraph {

    public GhostComponent component;
    public StateMachine<GhostAgent,GhostState> stateMachine;
    public float speed = 2.4F;
    public float timer;

    public GhostAgent(GhostComponent ghostComponent){
        this.component = ghostComponent;
        stateMachine = new DefaultStateMachine<>(this);
        timer = 0;
    }

    public Vector2 getPosition(){
        return component.getBody().getPosition();
    }

    public void update(float deltaTime){
        timer += deltaTime;
        stateMachine.update();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }
}
