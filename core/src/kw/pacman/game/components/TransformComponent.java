package kw.pacman.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * 转换
 */
public class TransformComponent implements Component {
    public Vector2 scale = new Vector2(1,1);
    public Vector2 position = new Vector2();
    public int zIndex;
    public float rotation = 0;
    public TransformComponent(float x,float y){
        this(x,y,0);
    }

    public TransformComponent(float x, float y, int zIndex) {
        this(x,y,zIndex,1.0F,1.0F,0);
    }

    public TransformComponent(float x, float y, int zIndex, float sclX, float sclY, int rotation) {
        this.position.set(x,y);
        this.rotation = rotation;
        this.zIndex = zIndex;
        this.scale.set(sclX,sclY);
    }
}
