package kw.test.pacmen.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.components.MyStateComponent;
import kw.test.pacmen.manger.MyGameManager;

public class MyGhostSystem extends IteratingSystem {
    private final ComponentMapper<MyGhostComponent> ghostM = ComponentMapper.getFor(MyGhostComponent.class);
    private final ComponentMapper<MyStateComponent> stateM = ComponentMapper.getFor(MyStateComponent.class);
    /**
     * Instantiates a system that will iterate over the entities described by the Family.
     *
     * @param family The family of entities iterated over in this System
     */
    public MyGhostSystem() {
        super(Family.all(MyGhostComponent.class,MyStateComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        MyGameManager.getinstance().bigPillEaten = false;
    }

    /**
     * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
     * specific processing.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MyGhostComponent ghost = ghostM.get(entity);
        MyStateComponent state = stateM.get(entity);

        ghost.ghostAgent.update(deltaTime);
        state.setState(ghost.currentState);

        if (MyGameManager.getinstance().bigPillEaten){
            ghost.weak_time = 0;
        }

        if (ghost.warken){
            ghost.weak_time += deltaTime;
            if (ghost.weak_time >= MyGhostComponent.WEAK_TIME){
                ghost.warken = false;
                ghost.weak_time = 0;
            }
        }

        if (MyGameManager.getinstance().bigPillEaten){
            ghost.warken = true;
            state.resetStateTime();
        }
    }
}
