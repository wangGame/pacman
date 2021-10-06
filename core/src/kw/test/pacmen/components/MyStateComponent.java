package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;

public class MyStateComponent implements Component {
    private float stateTime ;
    private int state;

    public MyStateComponent(){
        this(0);
    }

    public MyStateComponent(int state){
        this.state = state;
        stateTime = 0;
    }

    public void increaseStateTime(float delta){
        stateTime += delta;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void resetStateTime(){
        stateTime = 0;
    }

    public int getState(){
        return state;
    }

    public void setState(int newState) {
        if (this.state == newState) {
            return;
        }
        state = newState;
        stateTime = 0;
    }
}
