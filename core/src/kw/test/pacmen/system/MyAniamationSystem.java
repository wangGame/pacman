package kw.test.pacmen.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import kw.test.pacmen.components.MyAnimationComponent;
import kw.test.pacmen.components.MyStateComponent;
import kw.test.pacmen.components.MyTexureComponent;

public class MyAniamationSystem extends IteratingSystem {
    private final ComponentMapper<MyTexureComponent> textureM = ComponentMapper.getFor(MyTexureComponent.class);
    private final ComponentMapper<MyAnimationComponent> animationM = ComponentMapper.getFor(MyAnimationComponent.class);
    private final ComponentMapper<MyStateComponent> stateM = ComponentMapper.getFor(MyStateComponent.class);

    public MyAniamationSystem() {
        super(Family.all(MyTexureComponent.class,MyAnimationComponent.class,MyStateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MyTexureComponent tex = textureM.get(entity);
        MyAnimationComponent anim = animationM.get(entity);
        MyStateComponent state = stateM.get(entity);
        try {
            tex.region.setRegion(anim.animations.get(state.getState()).getKeyFrame(state.getStateTime()));
        }catch (Exception e){
            System.out.println("------------");
        }
    }
}
