package kw.pacman.game.screen;

import kw.pacman.game.asset.Asset;
import kw.pacman.game.constant.Constant;
import kw.pacman.game.screen.base.BaseScreen;

public class LoadingScreen extends BaseScreen {
    private Asset asset;
    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        asset = new Asset();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Constant.assetManager.update()){
            asset.getAtlas();
            enterScreen(new MainScreen());
        }
    }
}
