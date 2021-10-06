package kw.test.pacmen.ai.fsm;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;


import kw.test.pacmen.components.MyPlayerComponent;
import kw.test.pacmen.manger.MyGameManager;

public enum  MyPlayerState implements State<MyPlayerAgent> {
    MOVE_UP(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.MOVE_UP;
            if (entity.playerComponent.getBody().getLinearVelocity().y<SPEED_THRESHOLD){
                entity.stateStateMachine.changeState(IDLE_UP);
            }
            if (entity.playerComponent.hp <= 0){
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },MOVE_DOWN(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.MOVE_DOWN;
            if (entity.playerComponent.getBody().getLinearVelocity().y>-SPEED_THRESHOLD){
                entity.stateStateMachine.changeState(IDLE_DOWN);
            }
            if (entity.playerComponent.hp<0){
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },MOVE_LEFT(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.MOVE_LEFT;
            if (entity.playerComponent.getBody().getLinearVelocity().x > - SPEED_THRESHOLD){
                entity.stateStateMachine.changeState(IDLE_LEFT);
            }
            if (entity.playerComponent.hp<0){
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },MOVE_RIGHT(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.MOVE_RIGHT;
            if (entity.playerComponent.getBody().getLinearVelocity().x < SPEED_THRESHOLD){
                entity.stateStateMachine.changeState(IDLE_RIGHT);
            }
            if (entity.playerComponent.hp<0){
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },IDLE_UP(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.IDLE_UP;
            changeStateUponVelocity(entity);
            if (entity.playerComponent.hp <= 0) {
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },
    IDLE_DOWN(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.IDLE_DOWN;
            changeStateUponVelocity(entity);

            if (entity.playerComponent.hp <= 0) {
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },
    IDLE_LEFT(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.IDLE_LEFT;
            changeStateUponVelocity(entity);

            if (entity.playerComponent.hp <= 0) {
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },
    IDLE_RIGHT(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.IDLE_RIGHT;
            changeStateUponVelocity(entity);

            if (entity.playerComponent.hp <= 0) {
                entity.stateStateMachine.changeState(DIE);
            }
        }
    },
    DIE(){
        @Override
        public void update(MyPlayerAgent entity) {
            entity.playerComponent.currentState = MyPlayerComponent.DIE;
            MyGameManager.getinstance().playerIsAlive = false;

            if (entity.timer > 1.5F){
                MyGameManager.getinstance().decreasePlayerLives();
                if (MyGameManager.getinstance().playerLives > 0) {
                    entity.playerComponent.getBody().setTransform(MyGameManager.getinstance().playerSpawnPos, 0);
                    entity.playerComponent.hp = 1;
                    entity.stateStateMachine.changeState(IDLE_RIGHT);
                    MyGameManager.getinstance().playerIsAlive = true;
                    MyGameManager.getinstance().playerIsInvincible = true;
                }else {
                    MyGameManager.getinstance().makeGameOver();
                }
            }
        }
    };
    /**
     * This method will execute when the state is entered.
     *
     * @param entity the entity entering the state
     */
    @Override
    public void enter(MyPlayerAgent entity) {
        entity.timer = 0;
    }

    /**
     * This is the state's normal update function
     *
     * @param entity the entity lasting the state
     */
//    @Override
//    public void update(MyPlayerAgent entity) {
//
//    }

    /**
     * This method will execute when the state is exited.
     *
     * @param entity the entity exiting the state
     */
    @Override
    public void exit(MyPlayerAgent entity) {

    }

    private static final float SPEED_THRESHOLD = 0.5f;
    /**
     * This method executes if the {@code entity} receives a {@code telegram} from the message dispatcher while it is in this
     * state.
     *
     * @param entity   the entity that received the message
     * @param telegram the message sent to the entity
     * @return true if the message has been successfully handled; false otherwise.
     */
    @Override
    public boolean onMessage(MyPlayerAgent entity, Telegram telegram) {
        return false;
    }

    /**
     * 根据速度的坐标得到运动的方向
     * @param entity
     */
    private static void changeStateUponVelocity(MyPlayerAgent entity) {
        Vector2 velocity = entity.playerComponent.getBody().getLinearVelocity();
        //根据速度的大小
        if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
            if (velocity.x >= SPEED_THRESHOLD) {
                entity.stateStateMachine.changeState(MOVE_RIGHT);
            } else if (velocity.x <= -SPEED_THRESHOLD) {
                entity.stateStateMachine.changeState(MOVE_LEFT);
            }
        } else if (velocity.y >= SPEED_THRESHOLD) {
            entity.stateStateMachine.changeState(MOVE_UP);
        } else if (velocity.y <= -SPEED_THRESHOLD) {
            entity.stateStateMachine.changeState(MOVE_DOWN);
        }
    }
}
