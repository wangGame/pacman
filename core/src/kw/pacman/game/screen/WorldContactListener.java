package kw.pacman.game.screen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import javax.swing.Box;

import kw.pacman.game.actor.Box2DActor;
import kw.pacman.game.constant.Constant;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getFilterData().categoryBits == Constant.PILL_BIT ||
                fixtureB.getFilterData().categoryBits == Constant.PILL_BIT) {
            // pill
            if (fixtureA.getFilterData().categoryBits == Constant.PLAYER_BIT) {
                Body body = fixtureB.getBody();
                System.out.println("====>>> doudou1 kill !!!");
                Box2DActor box2DActor = (Box2DActor) body.getUserData();
                box2DActor.remove();
            } else if (fixtureB.getFilterData().categoryBits == Constant.PLAYER_BIT) {
                Body body = fixtureA.getBody();
                Box2DActor box2DActor = (Box2DActor) body.getUserData();
                box2DActor.remove();
                System.out.println("====>>> doudou2 kill !!!");
            }
        } else if (fixtureA.getFilterData().categoryBits == Constant.GHOST_BIT ||
                fixtureB.getFilterData().categoryBits == Constant.GHOST_BIT) {
            // ghost
            if (fixtureA.getFilterData().categoryBits == Constant.PLAYER_BIT) {
                Box2DActor player = (Box2DActor)fixtureA.getBody().getUserData();
                Box2DActor ghost = (Box2DActor)fixtureB.getBody().getUserData();


//                PlayerComponent player = playerM.get((Entity) fixtureA.getBody().getUserData());
//                GhostComponent ghost = ghostM.get((Entity) fixtureB.getBody().getUserData());

//                if (ghost.currentState == GhostComponent.DIE) {
//                    return;
//                }
//
//                if (ghost.weaken) {
//                    // kill ghost
//                    ghost.hp--;
//                    GameManager.instance.addScore(800);
//                    GameManager.instance.assetManager.get("sounds/ghost_die.ogg", Sound.class).play();
//                } else // kill player if player is not invincible
//                {
//                    if (!GameManager.instance.playerIsInvincible) {
//                        player.hp--;
//
//                        if (GameManager.instance.playerIsAlive) {
//                            GameManager.instance.assetManager.get("sounds/pacman_die.ogg", Sound.class).play();
//                        }
//                    }
//                }
//
//            } else if (fixtureB.getFilterData().categoryBits == GameManager.PLAYER_BIT) {
//                PlayerComponent player = playerM.get((Entity) fixtureB.getBody().getUserData());
//                GhostComponent ghost = ghostM.get((Entity) fixtureA.getBody().getUserData());
//
//                if (ghost.currentState == GhostComponent.DIE) {
//                    return;
//                }
//
//                if (ghost.weaken) {
//                    // kill ghost
//                    ghost.hp--;
//                    GameManager.instance.addScore(800);
//                    GameManager.instance.assetManager.get("sounds/ghost_die.ogg", Sound.class).play();
//                } else {// kill player if player is not invincible
//                    if (!GameManager.instance.playerIsInvincible) {
//                        player.hp--;
//                        if (GameManager.instance.playerIsAlive) {
//                            GameManager.instance.assetManager.get("sounds/pacman_die.ogg", Sound.class).play();
//                        }
//                    }
//                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
