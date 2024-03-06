package kw.test.pacmen.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

import kw.test.pacmen.manger.MyGameManager;

public class AnimationActor extends Actor {
    //动画
    private Animation animation;
    //所有的动画，名称和动画匹配
    private HashMap<Integer,Animation> animationHashMap;
    //动画进行的时间
    private float animationTime;
    //当前需要绘制的动画
    private TextureRegion region;
    //刚体
    private Body body;
    //是不是死亡
    private boolean die;
    public AnimationActor(){
        this.animationHashMap = new HashMap<>();
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
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
        animationTime = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation!=null){
            region = animation.getKeyFrame(animationTime);
        }
        animationTime += delta;
        if (body!=null) {
            setPosition(body.getPosition().x* MyGameManager.PPM,body.getPosition().y*MyGameManager.PPM);
        }
        if (die){
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (region!=null) {
            batch.draw(region, getX()-region.getRegionWidth()/2, getY()-region.getRegionHeight()/2);
        }
    }
}
