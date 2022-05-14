//package kw.test.pacmen.system;
//
//import com.badlogic.ashley.core.ComponentMapper;
//import com.badlogic.ashley.core.Entity;
//import com.badlogic.ashley.core.Family;
//import com.badlogic.ashley.systems.IteratingSystem;
//
//public class MyStateSystem extends IteratingSystem {
//    private final ComponentMapper<MyStateComponent> stateM = ComponentMapper.getFor(MyStateComponent.class);
//    public MyStateSystem() {
//        super(Family.all(MyStateComponent.class).get());
//    }
//
//    @Override
//    protected void processEntity(Entity entity, float deltaTime) {
//        MyStateComponent state = stateM.get(entity);
//        state.increaseStateTime(deltaTime);
//    }
//}
