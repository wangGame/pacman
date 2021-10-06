package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyTexureComponent implements Component {
    public TextureRegion region;
    public MyTexureComponent(TextureRegion textureRegion){
        region = new TextureRegion(textureRegion);
    }
}
