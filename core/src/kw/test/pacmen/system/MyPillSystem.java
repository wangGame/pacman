package kw.test.pacmen.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Body;

import kw.test.pacmen.components.MyMovementComponent;
import kw.test.pacmen.components.MyPillComponent;
import kw.test.pacmen.manger.MyGameManager;

public class MyPillSystem extends IteratingSystem {
    private final ComponentMapper<MyPillComponent> pillM = ComponentMapper.getFor(MyPillComponent.class);
    private final ComponentMapper<MyMovementComponent>  momentM = ComponentMapper.getFor(MyMovementComponent.class);

    public MyPillSystem() {
        super(Family.all(MyPillComponent.class,MyMovementComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MyPillComponent pill = pillM.get(entity);
        MyMovementComponent movement = momentM.get(entity);

        Body body = movement.body;
        if (pill.eaten){
            if (pill.big){
                MyGameManager.getinstance().addScore(500);
                //音效
            }else {
                MyGameManager.getinstance().addScore(100);
//                MyGameManager.getinstance().
//            音效
            }
            body.getWorld().destroyBody(body);
            getEngine().removeEntity(entity);
            MyGameManager.getinstance().totalPills -- ;
        }
    }
}
