package kw.test.pacmen.manger;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import kw.test.pacmen.ai.astar.MyAstartPathFinding;

public class MyGameManager implements Disposable {
//    short

    public static final float PPM = 16f;
    public static final short NOTHING_BIT = 0;
    public static final short WALL_BIT = 1;
    public static final short PLAYER_BIT = 1 << 1;
    public static final short PILL_BIT = 1 << 2;
    public static final short GHOST_BIT = 1 << 3;
    public static final short GATE_BIT = 1 << 4;
    public static final short LIGHT_BIT = 1 << 5;

    public static int currentdir = 0;


    private static MyGameManager instance;

    //是否活着F
    public boolean playerIsAlive;
    //有几条命
    public int playerLives;
    //初始位置
    public Vector2 playerSpawnPos;
    //无敌
    public boolean playerIsInvincible;
    public boolean bigPillEaten;
    public Location<Vector2> playerLocation;
    public MyAstartPathFinding pathfinder;
    public Vector2 ghostSpawnPos;

    public int totalPills = 3;
    //游戏结束
    private boolean gameOver = false;
    public AssetManager assetManager;

    public MyGameManager(){
        assetManager = new AssetManager();
        assetManager.load("images/actors.pack", TextureAtlas.class);
//        assetManager.load("sounds/pill.ogg", Sound.class);
//        assetManager.load("sounds/big_pill.ogg", Sound.class);
//        assetManager.load("sounds/ghost_die.ogg", Sound.class);
//        assetManager.load("sounds/pacman_die.ogg", Sound.class);
//        assetManager.load("sounds/clear.ogg", Sound.class);

        assetManager.finishLoading();

        playerSpawnPos = new Vector2();
        ghostSpawnPos = new Vector2();
    }

    public static MyGameManager getinstance(){
        if (instance == null) {
            instance = new MyGameManager();
        }
        return instance;
    }

    public void decreasePlayerLives() {
        playerLives--;
    }
    public void makeGameOver() {
        gameOver = true;
    }

    private int highScore = 0;
    private int score;
    public void addScore(int score) {
        this.score += score;
        if (this.score > highScore) {
            highScore = this.score;
        }
    }

    @Override
    public void dispose() {
//        assetManager.dispose();
    }
}
