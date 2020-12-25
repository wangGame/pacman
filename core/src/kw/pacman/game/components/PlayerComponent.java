package kw.pacman.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class PlayerComponent implements Component {
    public Body body;
    public PlayerComponent(Body body) {
        this.body = body;
    }
}
