//package kw.test.pacmen.system;
//
//import com.badlogic.ashley.core.ComponentMapper;
//import com.badlogic.ashley.core.Entity;
//import com.badlogic.ashley.core.Family;
//import com.badlogic.ashley.systems.IteratingSystem;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.utils.Array;
//import java.util.Comparator;
//
//import kw.test.pacmen.manger.MyGameManager;
//
//public class MyRenderSystem extends IteratingSystem {
//    private final Array<Entity> renderArray = new Array();
//    private final SpriteBatch batch;
//    private final ComponentMapper<MyTransformComponent> transformM = ComponentMapper.getFor(MyTransformComponent.class);
//    private final ComponentMapper<MyTexureComponent> rendererM = ComponentMapper.getFor(MyTexureComponent.class);
//
//    public MyRenderSystem(SpriteBatch batch) {
//        super(Family.all(MyTransformComponent.class,MyTexureComponent.class).get());
//        this.batch = batch;
//    }
//
//    @Override
//    public void update(float deltaTime) {
//        super.update(deltaTime);
//        renderArray.sort(new Comparator<Entity>() {
//            @Override
//            public int compare(Entity t1, Entity t2) {
//                MyTransformComponent transform1 = transformM.get(t1);
//                MyTransformComponent transform2 = transformM.get(t2);
//                return transform1.zIndex - transform2.zIndex;
//            }
//        });
//        batch.begin();
//        for (Entity entity1 : renderArray) {
//            MyTransformComponent transform = transformM.get(entity1);
//            MyTexureComponent tex = rendererM.get(entity1);
//            float width = tex.region.getRegionWidth() / MyGameManager.PPM;
//            float height = tex.region.getRegionHeight() / MyGameManager.PPM;
//            float originX = width * 0.5f;
//            float originY = height * 0.5f;
//            batch.draw(tex.region,
//                    transform.pos.x - originX, transform.pos.y - originY,
//                    originX, originY,
//                    width, height,
//                    transform.scale.x, transform.scale.y,
//                    transform.rotation * MathUtils.radiansToDegrees);
//        }
//        batch.end();
//        renderArray.clear();
//    }
//
//    @Override
//    protected void processEntity(Entity entity, float deltaTime) {
//        renderArray.add(entity);
//    }
//}
