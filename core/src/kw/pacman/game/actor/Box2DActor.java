package kw.pacman.game.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import java.awt.AWTKeyStroke;

public class Box2DActor extends Image {
    public Box2DActor(TextureRegion region) {
        super(region);
    }

    public Box2DActor(Texture texture) {
        super(texture);
    }

    public void setBody(Body body) {
        setPosition(body.getPosition().x,body.getPosition().y,Align.center);//        System.out.println(body.getPosition().x+"=============="+body.getPosition().y);
        setOrigin(Align.center);
        setScale(0.0625F);
    }

}
