package kw.test.pacmen.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import kw.test.pacmen.constant.Constant;
import kw.test.pacmen.manger.MyGameManager;

public class BodyImage extends Image {
    private Body body;
    private boolean die;
    private boolean isBig;

    public void setDie(boolean die) {
        this.die = die;
        if (die){
            remove();
        }
    }

    public void setBig(boolean big) {
        isBig = big;
    }

    public boolean isBig() {
        return isBig;
    }

    public boolean isDie() {
        return die;
    }

    public BodyImage(TextureRegion textureRegion) {
        super(textureRegion);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setPosition(body.getPosition().x * MyGameManager.PPM
                ,body.getPosition().y * MyGameManager.PPM, Align.center);
    }

    public void setBody(Body body) {
        this.body = body;
        body.setUserData(this);
    }
}
