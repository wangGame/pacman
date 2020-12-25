package kw.pacman.game.components;

import com.badlogic.ashley.core.Component;

public class PillComponent implements Component {
    //是不是大的
    public boolean isBig;
    //是不是死了
    public boolean eaten;

    public PillComponent(boolean isBig) {
        this.isBig = isBig;
        this.eaten = false;
    }
}
