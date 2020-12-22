package kw.pacman.game.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import kw.pacman.game.constant.Constant;

public class Asset {
    private TextureAtlas actorAtlas;
    private AssetManager manager;
    public Asset(){
        manager = Constant.assetManager;
        manager.load("images/actors.pack", TextureAtlas.class);
    }

    public void getAtlas(){
        actorAtlas = manager.get("images/actors.pack", TextureAtlas.class);
    }
}
