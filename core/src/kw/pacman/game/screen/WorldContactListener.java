package kw.pacman.game.screen;

import com.badlogic.ashley.core.Entity;
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
import kw.pacman.game.components.GhostComponent;
import kw.pacman.game.components.PillComponent;
import kw.pacman.game.components.PlayerComponent;
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
                Entity pill = (Entity)fixtureB.getUserData();
                PillComponent pillComponent = pill.getComponent(PillComponent.class);
                pillComponent.eaten = true;
                Constant.bigPill = pillComponent.isBig;
            } else if (fixtureB.getFilterData().categoryBits == Constant.PLAYER_BIT) {
                Entity pill = (Entity)fixtureA.getUserData();
                PillComponent pillComponent = pill.getComponent(PillComponent.class);
                pillComponent.eaten = true;
                Constant.bigPill = pillComponent.isBig;
            }
        } else if (fixtureA.getFilterData().categoryBits == Constant.GHOST_BIT ||
                fixtureB.getFilterData().categoryBits == Constant.GHOST_BIT) {
            // ghost
            if (fixtureA.getFilterData().categoryBits == Constant.PLAYER_BIT) {
//                PlayerComponent player = (PlayerComponent) fixtureA.getBody().getUserData();
//                GhostComponent ghost = (GhostComponent) fixtureB.getBody().getUserData();
                PlayerComponent player = ((Entity)fixtureA.getBody().getUserData()).getComponent(PlayerComponent.class);
                GhostComponent ghost = ((Entity)fixtureB.getBody().getUserData()).getComponent(GhostComponent.class);
                if (ghost.currentState == GhostComponent.DIE) {
                    return;
                }
                if (ghost.weaken) {
                    // kill ghost
                    ghost.hp--;  //这个时候杀死 鬼
//                    GameManager.instance.addScore(800);
//                    GameManager.instance.assetManager.get("sounds/ghost_die.ogg", Sound.class).play();
                } else // kill player if player is not invincible
                {
                    if (!Constant.playerIsInvincible) {
                        player.hp--;
                        if (Constant.playerIsAlive) {
//                            GameManager.instance.assetManager.get("sounds/pacman_die.ogg", Sound.class).play();
                        }
                    }
                }
            } else if (fixtureB.getFilterData().categoryBits == Constant.PLAYER_BIT) {
                PlayerComponent player = ((Entity)fixtureB.getBody().getUserData()).getComponent(PlayerComponent.class);
                GhostComponent ghost = ((Entity)fixtureA.getBody().getUserData()).getComponent(GhostComponent.class);
                if (ghost.currentState == GhostComponent.DIE) {
                    return;
                }
                if (ghost.weaken) {
                    // kill ghost
                    ghost.hp--;
//                    GameManager.instance.addScore(800);
//                    GameManager.instance.assetManager.get("sounds/ghost_die.ogg", Sound.class).play();
                } else {// kill player if player is not invincible
                    if (!Constant.playerIsInvincible) {
                        player.hp--;
                        if (Constant.playerIsAlive) {
//                            Constant.assetManager.get("sounds/pacman_die.ogg", Sound.class).play();
                        }
                    }
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
