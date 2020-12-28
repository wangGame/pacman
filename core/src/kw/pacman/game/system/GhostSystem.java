package kw.pacman.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import kw.pacman.game.components.GhostComponent;
import kw.pacman.game.constant.Constant;

public class GhostSystem extends IteratingSystem {
    private final ComponentMapper<GhostComponent> ghostM = ComponentMapper.getFor(GhostComponent.class);

//    private
    public GhostSystem() {
        super(Family.all(GhostComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GhostComponent ghost = ghostM.get(entity);
        ghost.agent.update(deltaTime);
        //吃大的
        if (Constant.bigPill) {
            ghost.waken_time = 0;
        }
        if (ghost.weaken) {
            if (Constant.bigPill) {
                ghost.waken_time += deltaTime;
                if (ghost.waken_time > GhostComponent.WEAK_TIME) {
                    ghost.waken_time = 0;
                    ghost.weaken = false;
                }
            }
        }

        if (Constant.bigPill){
            ghost.weaken = true;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);   //update
        Constant.bigPill = true;

    }
}
