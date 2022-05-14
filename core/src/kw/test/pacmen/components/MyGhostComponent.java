package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * 对于鬼  可以运动   它也有自己的状态
 */
public class MyGhostComponent implements Component {
    public static final int MOVE_UP = 0;
    public static final int MOVE_DOWN = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;
    public static final int ESCAPE = 4;
    public static final int DIE = 5;
    public static final int PURSUE = 6;

    public static final int RESPAWN = 7;
}
