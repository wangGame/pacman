package kw.pacman.game.constant;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

import kw.pacman.game.asset.Asset;

public class Constant {
    public static final short FANMIAN = 0;
    public static final short ZHENGMIAN = 1;
    public static final short SERVER = 0;
    public static final short CLIENT = 1;
    public static final short NOTHING_BIT = 0;
    public static final short WALL_BIT = 1;
    public static final short PLAYER_BIT = 1 << 1;
    public static final short PILL_BIT = 1 << 2;
    public static final short GHOST_BIT = 1 << 3;
    public static final short GATE_BIT = 1 << 4;
    public static final short LIGHT_BIT = 1 << 5;


    public static Game game;
    public static Batch batch;

    public static AssetManager assetManager;
    public static Viewport fillViewport;
    public static OrthographicCamera camera;
    public static final float PPM = 16f;
    public static World world;
    public static Box2DDebugRenderer box2DDebugRenderer;
    public static int pillNum = 0;

    public static boolean bigPill = false;
}
