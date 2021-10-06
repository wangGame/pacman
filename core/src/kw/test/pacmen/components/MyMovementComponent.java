package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class MyMovementComponent implements Component {
    public float speed = 3.6F;
    public Body body;

    public MyMovementComponent(Body body){
        this.body = body;
    }
}
