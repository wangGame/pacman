package kw.pacman.game.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import javax.swing.text.html.parser.Entity;

import kw.pacman.game.components.PlayerComponent;
import kw.pacman.game.constant.Constant;

public enum PlayState implements State<PlayAgent> {
    MOVE_UP(){
        @Override
        public void update(PlayAgent entity) {
            updateExcute(entity,PlayerComponent.MOVE_UP);
        }
    },
    MOVE_DOWN(){
        @Override
        public void update(PlayAgent entity) {
            updateExcute(entity,PlayerComponent.MOVE_DOWN);
        }
    },
    MOVE_LEFT(){
        @Override
        public void update(PlayAgent entity) {
            updateExcute(entity,PlayerComponent.MOVE_LEFT);
        }
    },
    MOVE_RIGHT(){
        @Override
        public void update(PlayAgent entity) {
            updateExcute(entity,PlayerComponent.MOVE_RIGHT);
        }
    },
    DIE(){
        @Override
        public void update(PlayAgent entity) {
            entity.playerComponent.currentState = PlayerComponent.DIE;
            Constant.playerIsAlive = false;
            // re-spawn player
            if (entity.timer > 1.5f) {
//                GameManager.instance.decreasePlayerLives();
//                if (Constant.playerLives > 0) {
                    entity.playerComponent.getBody().setTransform(Constant.playHomePos, 0);
                    entity.playerComponent.hp = 1;
                    entity.stateMachine.changeState(MOVE_RIGHT);
                    Constant.playerIsAlive = true;
                    Constant.playerIsInvincible = true;
                    entity.playerComponent.invicibleTime = 0;
//                } else {
//                    Constant.makeGameOver();
//                }
            }
        }
    };

    @Override
    public void enter(PlayAgent entity) {

    }

    @Override
    public void exit(PlayAgent entity) {

    }

    @Override
    public boolean onMessage(PlayAgent entity, Telegram telegram) {
        return false;
    }

    public void updateExcute(PlayAgent entity,int state){
        entity.playerComponent.currentState = state;
        if (entity.playerComponent.hp <= 0){
            entity.stateMachine.changeState(DIE);
        }
    }
}
