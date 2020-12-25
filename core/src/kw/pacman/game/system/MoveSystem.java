package kw.pacman.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import kw.pacman.game.components.MoveComponent;
import kw.pacman.game.components.TransformComponent;

public class MoveSystem extends IteratingSystem {
    private ComponentMapper<MoveComponent> moveM = ComponentMapper.getFor(MoveComponent.class);
    private ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    public MoveSystem() {
        super(Family.all(MoveComponent.class, TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MoveComponent move = moveM.get(entity);
        TransformComponent transform = transformM.get(entity);

        if (move.body.getPosition().x <= 0){
            move.body.setTransform(19,move.body.getPosition().y,0);
        }else if (move.body.getPosition().x >= 19F){
            move.body.setTransform(0,move.body.getPosition().y,0);
        }

        transform.position.set(move.body.getPosition());
    }
}
