package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import javax.xml.bind.ValidationException;

public class MyTransformComponent implements Component {
    public Vector2 pos = new Vector2();
    public Vector2 scale = new Vector2();

    public int zIndex;
    public float rotation = 0;
    public MyTransformComponent(float x,float y){
        this(x,y,0);
    }

    public MyTransformComponent(float x,float y,int zIndex){
        this(x,y,zIndex,1.0F,1.0F,0F);
    }

    public MyTransformComponent(float x,float y,int zIndex,
                                float sclX,float sclY,float rotation){
        pos.set(x,y);
        this.zIndex = zIndex;
        scale.set(sclX,sclY);
        this.rotation = rotation;
    }

}
