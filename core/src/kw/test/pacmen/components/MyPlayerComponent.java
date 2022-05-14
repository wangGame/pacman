package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

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
}
