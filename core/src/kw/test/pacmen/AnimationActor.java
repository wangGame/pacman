package kw.test.pacmen;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import kw.test.pacmen.ai.fsm.MyGhostAgent;
import kw.test.pacmen.components.MyAnimationComponent;
import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.manger.MyGameManager;

public class AnimationActor extends Actor {
    private Animation animation;
    private HashMap<Integer,Animation> animationHashMap = new HashMap<>();
    private float time;
    private TextureRegion region;
    private Body body;
    public AnimationActor(){

    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void addAnimation(int name, Animation animation){
//        animation = new Animation(0.2f, keyFrames);
        animationHashMap.put(name,animation);
    }

    public void initAnimation(int type){
        setAniType(type);
    }

    public void setAniType(int type){
        animation = animationHashMap.get(type);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation!=null){
            region = animation.getKeyFrame(time);
        }
        time += delta;
        if (body!=null) {
            setPosition(body.getPosition().x* MyGameManager.PPM,body.getPosition().y*MyGameManager.PPM);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (region!=null) {
            batch.draw(region, getX()-region.getRegionWidth()/2, getY()-region.getRegionHeight()/2);
        }
    }
}
