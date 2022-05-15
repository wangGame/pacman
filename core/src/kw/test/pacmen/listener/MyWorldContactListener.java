package kw.test.pacmen.listener;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import kw.test.pacmen.actor.BodyImage;
import kw.test.pacmen.actor.GhostActor;
import kw.test.pacmen.actor.PlayerActor;
import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.components.MyPillComponent;
import kw.test.pacmen.components.MyPlayerComponent;
import kw.test.pacmen.manger.MyGameManager;
//import kw.test.pacmen.system.MyGhostSystem;

public class MyWorldContactListener implements ContactListener {

    public MyWorldContactListener(){

    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //遇到了奖励
        if (fixtureA.getFilterData().categoryBits == MyGameManager.getinstance().PILL_BIT ||
                fixtureB.getFilterData().categoryBits == MyGameManager.getinstance().PILL_BIT){
            if (fixtureA.getFilterData().categoryBits == MyGameManager.getinstance().PLAYER_BIT){
                Body body = fixtureB.getBody();
                BodyImage animationActor = (BodyImage) body.getUserData();
                animationActor.setDie(true);
                MyGameManager.getinstance().bigPillEaten = animationActor.isBig();
            }else if (fixtureB.getFilterData().categoryBits == MyGameManager.PLAYER_BIT) {
                Body body = fixtureA.getBody();
                BodyImage animationActor = (BodyImage) body.getUserData();
                animationActor.setDie(true);
                MyGameManager.getinstance().bigPillEaten = animationActor.isBig();
            }
        }else if (fixtureA.getFilterData().categoryBits == MyGameManager.GHOST_BIT ||
                fixtureB.getFilterData().categoryBits == MyGameManager.GHOST_BIT) {
            // ghost
            if (fixtureA.getFilterData().categoryBits == MyGameManager.PLAYER_BIT) {
                PlayerActor player = (PlayerActor) fixtureB.getBody().getUserData();
                GhostActor ghost = (GhostActor) fixtureA.getBody().getUserData();
//
                if (ghost.currentState == MyGhostComponent.DIE) {
                    return;
                }
                if (ghost.warken) {
                    // kill ghost
                    ghost.hp--;
                    MyGameManager.getinstance().addScore(800);
               } else // kill player if player is not invincible
                {
                    if (!MyGameManager.getinstance().playerIsInvincible) {
                        player.hp--;
                        MyGameManager.getinstance().playerIsInvincible = true;
                        if (MyGameManager.getinstance().playerIsAlive) {
//                            GameManager.instance.assetManager.get("sounds/pacman_die.ogg", Sound.class).play();
                        }
                    }
                }

            } else if (fixtureB.getFilterData().categoryBits == MyGameManager.PLAYER_BIT) {
                PlayerActor player = (PlayerActor) fixtureB.getBody().getUserData();
                GhostActor ghost = (GhostActor) fixtureA.getBody().getUserData();
                if (ghost.currentState == MyGhostComponent.DIE) {
                    return;
                }
                if (ghost.warken) {
                    // kill ghost
                    ghost.hp--;
                    MyGameManager.getinstance().addScore(800);
                } else // kill player if player is not invincible
                    if (!MyGameManager.getinstance().playerIsInvincible) {
                        player.hp--;
                    }
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
