package kw.pacman.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class GhostComponent implements Component {
    public static final short MOVE_UP = 0;
    public static final short MOVE_DOWN = 1;
    public static final short MOVE_LEFT = 2;
    public static final short MOVE_RIGHT = 3;

//    public static final float WEAK_TIME = 10F;

    public final Body body;
    public boolean weaken;
    public int hp;

    public float waken_time;
    public static final float WEAK_TIME = 10f;
    /*************************************************/
    public int currentState;

    public GhostComponent(Body body){
        this.body = body;
        weaken = false;
        hp = 1;
    }

    public Body getBody() {
        return body;
    }

    public void resPawn(){
        hp = 1;
        weaken = true;
    }
}
