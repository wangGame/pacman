package kw.test.pacmen.manger;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import kw.test.pacmen.ai.astar.MyAstartPathFinding;
import kw.test.pacmen.utils.Box2dLocation;

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

    public Vector2 playerLocation;
    private int highScore = 0;
    private int score;
    private static MyGameManager instance;
    //是否活着F
    public boolean playerIsAlive = true;
    //初始位置
    public Vector2 playerSpawnPos;
    //无敌
    public boolean playerIsInvincible;
    public boolean bigPillEaten;
    public MyAstartPathFinding pathfinder;
    public Vector2 ghostSpawnPos;
    public int totalPills = 3;
    public AssetManager assetManager;

    public MyGameManager(){
        assetManager = new AssetManager();
        assetManager.load("images/actors.pack", TextureAtlas.class);
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

    public void addScore(int score) {
        this.score += score;
        if (this.score > highScore) {
            highScore = this.score;
        }
    }

    @Override
    public void dispose() {
    }
}
