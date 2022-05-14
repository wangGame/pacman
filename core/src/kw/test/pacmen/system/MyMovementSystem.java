//package kw.test.pacmen.system;
//
//import com.badlogic.ashley.core.ComponentMapper;
//import com.badlogic.ashley.core.Entity;
//import com.badlogic.ashley.core.Family;
//import com.badlogic.ashley.systems.IteratingSystem;
//
//import kw.test.pacmen.components.MyMovementComponent;
//
///**
// * 移动组件     位置组件
// */
//public class MyMovementSystem extends IteratingSystem {
//    private ComponentMapper<MyMovementComponent> movementM
//            = ComponentMapper.getFor(MyMovementComponent.class);
//    private ComponentMapper<MyTransformComponent> transformM
//            = ComponentMapper.getFor(MyTransformComponent.class);
//
//    public MyMovementSystem() {
//        super(Family.all(MyMovementComponent.class,MyTransformComponent.class).get());
//    }
//
//    @Override
//    protected void processEntity(Entity entity, float deltaTime) {
//        MyMovementComponent movement = movementM.get(entity);
//        MyTransformComponent transform = transformM.get(entity);
//
//        if (movement.body.getPosition().x <= 0){
//            movement.body.setTransform(19.0F,movement.body.getPosition().y,0);
//        } else if (movement.body.getPosition().x >= 19f) {
//            movement.body.setTransform(0, movement.body.getPosition().y, 0);
//        }
//        transform.pos.set(movement.body.getPosition());
//    }
//}
