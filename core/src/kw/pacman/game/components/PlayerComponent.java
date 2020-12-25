package kw.pacman.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import kw.pacman.game.ai.PlayerAI;

public class PlayerComponent implements Component {
    public Body body;
    public Location<Vector2> ai;

    public PlayerComponent(Body body) {
        this.body = body;
        ai = new PlayerAI(body);
    }
}
