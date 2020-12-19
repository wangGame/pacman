package kw.pacman.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import java.awt.AWTKeyStroke;

public class Box2DActor extends Image {
    private Listener listener;
    private Body body;
    public Box2DActor(TextureRegion region) {
        super(region);
    }

    public Box2DActor(Texture texture) {
        super(texture);
    }

    public void setBody(Body body) {
        this.body = body;
        setPosition(body.getPosition().x,body.getPosition().y,Align.center);//        System.out.println(body.getPosition().x+"=============="+body.getPosition().y);
        setOrigin(Align.center);
        setScale(0.0625F);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        if (listener != null){
            listener.touchEvent();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        public void touchEvent();
    }
    private final Vector2 tmpV1 = new Vector2();
    private final Vector2 tmpV2 = new Vector2();
    public void keyEvent(int keyCode){
        System.out.println(keyCode+",,,,,,,,,");
        if ((Gdx.input.isKeyPressed(Input.Keys.G))) {
            System.out.println("=======----------");
        }
        if (keyCode == Input.Keys.A){
            System.out.println("=======>>>>>");
            body.applyLinearImpulse(tmpV1.set(-3.6F, 0).scl(body.getMass()), body.getWorldCenter(), true);
        }else if (keyCode == Input.Keys.D){
            System.out.println("=======>>>>>D");
            body.applyLinearImpulse(tmpV1.set(3.6F, 0).scl(body.getMass()), body.getWorldCenter(), true);
        }else if (keyCode == Input.Keys.W){
            System.out.println("=======>>>>>W");
            body.applyLinearImpulse(tmpV1.set(0,3.6F).scl(body.getMass()), body.getWorldCenter(), true);
        }else if (keyCode == Input.Keys.S){
            body.applyLinearImpulse(tmpV1.set(0,-3.6F).scl(body.getMass()), body.getWorldCenter(), true);
            System.out.println("=======>>>>>S");
        }
        setPosition(body.getPosition().x,body.getPosition().y,Align.center);
    }
}
