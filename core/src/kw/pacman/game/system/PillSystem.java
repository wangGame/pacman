package kw.pacman.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;

import kw.pacman.game.components.MoveComponent;
import kw.pacman.game.components.PillComponent;

public class PillSystem extends IteratingSystem {
    private final ComponentMapper<PillComponent> pillM = ComponentMapper.getFor(PillComponent.class);
    private final ComponentMapper<MoveComponent> moveM = ComponentMapper.getFor(MoveComponent.class);

    public PillSystem() {
        super(Family.all(PillComponent.class,MoveComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PillComponent pillComponent = pillM.get(entity);
        MoveComponent moveComponent = moveM.get(entity);
        Body body = moveComponent.body;

        if (pillComponent.eaten){
            if (pillComponent.isBig){

            }else {

            }
            body.getWorld().destroyBody(body);
            getEngine().removeEntity(entity);
        }
    }
}
