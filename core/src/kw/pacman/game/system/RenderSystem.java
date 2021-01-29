package kw.pacman.game.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import kw.pacman.game.components.TextureComponent;
import kw.pacman.game.components.TransformComponent;
import kw.pacman.game.constant.Constant;

public class RenderSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<TextureComponent> textureformM = ComponentMapper.getFor(TextureComponent.class);
    private final Array<Entity> renderArray;
    private Batch batch;
    public RenderSystem(Batch batch) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());
        renderArray = new Array<>();
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderArray.add(entity);
    }

    @Override
    public void update(float delta){
        super.update(delta);
        renderArray.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                TransformComponent transform1 = transformM.get(o1);
                TransformComponent transform2 = transformM.get(o2);
                return transform2.zIndex - transform1.zIndex;
            }
        });
        batch.setProjectionMatrix(Constant.camera.combined);
        batch.begin();
        for (Entity entity : renderArray) {
            TransformComponent transform = transformM.get(entity);
            TextureComponent tex = textureformM.get(entity);
            float width = tex.region.getRegionWidth() / Constant.PPM;
            float height = tex.region.getRegionHeight() / Constant.PPM;
            float originX = width * 0.5f;
            float originY = height * 0.5f;
            batch.draw(tex.region,
                    transform.position.x - originX, transform.position.y - originY,
                    originX, originY,
                    width, height,
                    transform.scale.x, transform.scale.y,
                    transform.rotation * MathUtils.radiansToDegrees);
        }
        batch.end();
        renderArray.clear();
    }
}
