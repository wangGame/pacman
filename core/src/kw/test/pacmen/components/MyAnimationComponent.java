package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

public class MyAnimationComponent implements Component {
    public IntMap<Animation> animations = new IntMap<>();
}
