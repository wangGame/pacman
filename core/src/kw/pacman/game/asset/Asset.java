package kw.pacman.game.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import kw.pacman.game.constant.Constant;

public class Asset {
    public static BitmapFont commonfont;
    public static BitmapFont animalfont;
    public static BitmapFont time;
    private AssetManager manager;
    public Asset(){
        manager = Constant.assetManager;
        BitmapFontLoader.BitmapFontParameter textureParameter = null;
        textureParameter = new BitmapFontLoader.BitmapFontParameter();
        textureParameter.genMipMaps = true;
        textureParameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        textureParameter.atlasName = "image/main.atlas";
        manager.load("fonts/gamefont.fnt", BitmapFont.class, textureParameter);
        manager.load("fonts/animal.fnt", BitmapFont.class, textureParameter);
        manager.load("fonts/time.fnt", BitmapFont.class, textureParameter);
    }

    public void getAtlas(){
        commonfont = manager.get("fonts/gamefont.fnt", BitmapFont.class);
        animalfont = manager.get("fonts/animal.fnt",BitmapFont.class);
        time = manager.get("fonts/time.fnt",BitmapFont.class);
    }
}
