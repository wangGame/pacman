package kw.test.pacmen.ai.fsm;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;

import kw.test.pacmen.components.MyPlayerComponent;

/**
 * 消息系统
 */
public class MyPlayerAgent implements Telegraph {
    public final MyPlayerComponent playerComponent;
    public final StateMachine<MyPlayerAgent,MyPlayerState> stateStateMachine;
    public float timer;

    public MyPlayerAgent(MyPlayerComponent component){
        this.playerComponent = component;
        stateStateMachine = new DefaultStateMachine<>(this);
        timer = 0;
    }

    public void  update(float deltaTime){
        timer += deltaTime;
        stateStateMachine.update();
    }
    /**
     * Handles the telegram just received.
     *
     * @param msg The telegram
     * @return {@code true} if the telegram has been successfully handled; {@code false} otherwise.
     */
    @Override
    public boolean handleMessage(Telegram msg) {
        return stateStateMachine.handleMessage(msg);
    }
}
