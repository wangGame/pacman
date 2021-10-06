package kw.test.pacmen.ai.astar;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;
public class MyNode {
    public final int x;
    public final int y;
    public boolean isWall;
    private final int index;
    private final Array<Connection<MyNode>> connections;

    public MyNode(MyAStarMap myAStarMap,int x,int y){
        this.x = x;
        this.y = y;
        this.index = x*myAStarMap.getHeight()+y;
        this.isWall = false;
        this.connections = new Array<Connection<MyNode>>();
    }

    public int getIndex(){
        return index;
    }

    public Array<Connection<MyNode>> getConnections(){
        return connections;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
