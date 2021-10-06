package kw.test.pacmen.components;

import com.badlogic.ashley.core.Component;

public class MyPillComponent implements Component {
    public boolean eaten;
    public boolean big;

    public MyPillComponent(boolean isBig){
        big = isBig;
        eaten = false;
    }
}
