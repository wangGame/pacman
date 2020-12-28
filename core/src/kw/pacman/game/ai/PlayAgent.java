package kw.pacman.game.ai;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;

import kw.pacman.game.components.PlayerComponent;

public class PlayAgent implements Telegraph {

    public final PlayerComponent playerComponent;
    public final StateMachine<PlayAgent, PlayState> stateMachine;

    public float timer;

    public PlayAgent(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
        stateMachine = new DefaultStateMachine<>(this);
        timer = 0;
    }

    public void update(float deltaTime) {
        timer += deltaTime;
        stateMachine.update();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
