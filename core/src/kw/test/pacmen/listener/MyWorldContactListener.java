package kw.test.pacmen.listener;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import kw.test.pacmen.components.MyGhostComponent;
import kw.test.pacmen.components.MyPillComponent;
import kw.test.pacmen.components.MyPlayerComponent;
import kw.test.pacmen.manger.MyGameManager;
import kw.test.pacmen.system.MyGhostSystem;

public class MyWorldContactListener implements ContactListener {
    private final ComponentMapper<MyPillComponent> pillM = ComponentMapper.getFor(MyPillComponent.class);
    private final ComponentMapper<MyGhostComponent> ghostM = ComponentMapper.getFor(MyGhostComponent.class);
    private final ComponentMapper<MyPlayerComponent> playerM = ComponentMapper.getFor(MyPlayerComponent.class);

    public MyWorldContactListener(){

    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getFilterData().categoryBits
                == MyGameManager.getinstance().PILL_BIT ||
        fixtureB.getFilterData().categoryBits
                == MyGameManager.getinstance().PILL_BIT){
            if (fixtureA.getFilterData().categoryBits
                    == MyGameManager.getinstance().PLAYER_BIT){
                Body body = fixtureB.getBody();
                Entity entity = (Entity) body.getUserData();
                MyPillComponent pill = pillM.get(entity);
                pill.eaten = true;
                MyGameManager.getinstance().bigPillEaten = pill.big;
            }else if (fixtureB.getFilterData().categoryBits
                    == MyGameManager.PLAYER_BIT) {
                Body body = fixtureA.getBody();
                Entity entity = (Entity) body.getUserData();
                MyPillComponent pill = pillM.get(entity);
                pill.eaten = true;
                MyGameManager.getinstance().bigPillEaten = pill.big;
            }
        }else if (fixtureA.getFilterData().categoryBits == MyGameManager.GHOST_BIT ||
                fixtureB.getFilterData().categoryBits == MyGameManager.GHOST_BIT) {
            // ghost
            if (fixtureA.getFilterData().categoryBits == MyGameManager.PLAYER_BIT) {
                MyPlayerComponent player = playerM.get((Entity) fixtureA.getBody().getUserData());
                MyGhostComponent ghost = ghostM.get((Entity) fixtureB.getBody().getUserData());

                if (ghost.currentState == MyGhostComponent.DIE) {
                    return;
                }

                if (ghost.warken) {
                    // kill ghost
                    ghost.hp--;
                    MyGameManager.getinstance().addScore(800);
//                    MyGameManager.getinstance().assetManager.get("sounds/ghost_die.ogg", Sound.class).play();
                } else // kill player if player is not invincible
                {
                    if (!MyGameManager.getinstance().playerIsInvincible) {
                        player.hp--;
                        if (MyGameManager.getinstance().playerIsAlive) {
//                            GameManager.instance.assetManager.get("sounds/pacman_die.ogg", Sound.class).play();
                        }
                    }
                }

            } else if (fixtureB.getFilterData().categoryBits == MyGameManager.PLAYER_BIT) {
                MyPlayerComponent player = playerM.get((Entity) fixtureB.getBody().getUserData());
                MyGhostComponent ghost = ghostM.get((Entity) fixtureA.getBody().getUserData());

                if (ghost.currentState == MyGhostComponent.DIE) {
                    return;
                }

                if (ghost.warken) {
                    // kill ghost
                    ghost.hp--;
                    MyGameManager.getinstance().addScore(800);
//                    MyGameManager.getinstance().assetManager.get("sounds/ghost_die.ogg", Sound.class).play();
                } else // kill player if player is not invincible
                    if (!MyGameManager.getinstance().playerIsInvincible) {
                        player.hp--;
//                        if (GameManager.instance.playerIsAlive) {
//                            GameManager.instance.assetManager.get("sounds/pacman_die.ogg", Sound.class).play();
//                        }
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
