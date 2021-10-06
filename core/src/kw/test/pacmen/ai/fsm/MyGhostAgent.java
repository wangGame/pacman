package kw.test.pacmen.ai.fsm;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;

import kw.test.pacmen.ai.astar.MyNode;
import kw.test.pacmen.components.MyGhostComponent;

public class MyGhostAgent implements Telegraph {
    public StateMachine<MyGhostAgent,MyGhostState> stateMachine;
    public MyGhostComponent ghostComponent;
    public float speed = 2.4F;
    public float timer;
    public MyNode nextNode;

    public MyGhostAgent(MyGhostComponent ghostComponent){
        this.ghostComponent = ghostComponent;
        stateMachine = new DefaultStateMachine<>(this);
        timer = 0;
    }

    public Vector2 getPosition(){
        return ghostComponent.getBody().getPosition();
    }

    public void update(float deltaTime){
        timer += deltaTime;
        stateMachine.update();
    }
    /**
     * Handles the telegram just received.
     *
     * @param msg The telegram
     * @return {@code true} if the telegram has been successfully handled; {@code false} otherwise.
     */
    @Override
    public boolean handleMessage(Telegram msg) {
        return stateMachine.handleMessage(msg);
    }
}
