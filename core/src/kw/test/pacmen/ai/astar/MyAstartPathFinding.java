package kw.test.pacmen.ai.astar;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MyAstartPathFinding {
    public final MyAStarMap myAStarMap;
    private final PathFinder<MyNode> pathFinder;
    private final Heuristic<MyNode> heuristic;
    private final GraphPath<Connection<MyNode>> connectionPath;

    public MyAstartPathFinding(MyAStarMap myAStarMap){
        this.myAStarMap = myAStarMap;
        this.pathFinder = new IndexedAStarPathFinder<MyNode>(createGraph(myAStarMap));
        this.connectionPath = new DefaultGraphPath<>();
        this.heuristic = new Heuristic<MyNode>() {
            @Override
            public float estimate(MyNode node, MyNode endNode) {
                return Math.abs(endNode.x - node.x)+Math.abs(endNode.y - node.y);
            }
        };
    }

    public MyNode findNextNode(Vector2 source,Vector2 target){
        int sourceX = MathUtils.floor(source.x);
        int sourceY = MathUtils.floor(source.y);
        int targetX = MathUtils.floor(target.x);
        int targetY = MathUtils.floor(target.y);

        if (myAStarMap == null
                || sourceX < 0 || sourceX >= myAStarMap.getWidth()
                || sourceY < 0 || sourceY >= myAStarMap.getHeight()
                || targetX < 0 || targetX >= myAStarMap.getWidth()
                || targetY < 0 || targetY >= myAStarMap.getHeight()) {
            return null;
        }

        MyNode sourceNode = myAStarMap.getNodeAt(sourceX, sourceY);
        MyNode targetNode = myAStarMap.getNodeAt(targetX, targetY);
        connectionPath.clear();
        pathFinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);
        return connectionPath.getCount() == 0?null : connectionPath.get(0).getToNode();

    }

    public static MyGrap createGraph(MyAStarMap myAStarMap){
        final int height = myAStarMap.getHeight();
        final int width = myAStarMap.getWidth();
        MyGrap grap = new MyGrap(myAStarMap);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                MyNode myNode = myAStarMap.getNodeAt(x,y);
                if (myNode.isWall){
                    continue;
                }
                for (int offset = 0; offset < NEIGHBORHOOD.length; offset++) {
                    int neighborX = myNode.x + NEIGHBORHOOD[offset][0];
                    int neighborY = myNode.y + NEIGHBORHOOD[offset][1];
                    if (neighborX >= 0 && neighborX < width &&
                    neighborY>=0&&neighborY<height){
                        MyNode neighbor = myAStarMap.getNodeAt(neighborX,neighborY);
                        if (!neighbor.isWall){
                            myNode.getConnections().add(new DefaultConnection<>(myNode,neighbor));
                        }
                    }
                }
                myNode.getConnections().shuffle();
            }
        }
        return grap;
    }

    private static final int[][] NEIGHBORHOOD = new int[][] {
            new int[] {-1,  0},
            new int[] { 0, -1},
            new int[] { 0,  1},
            new int[] { 1,  0}
    };

    public static class MyGrap implements IndexedGraph<MyNode>{
        MyAStarMap myAStarMap;

        public MyGrap(MyAStarMap myAStarMap){
            this.myAStarMap = myAStarMap;
        }

        @Override
        public int getIndex(MyNode node) {
            return node.getIndex();
        }

        @Override
        public int getNodeCount() {
            return myAStarMap.getWidth() * myAStarMap.getHeight();
        }

        @Override
        public Array<Connection<MyNode>> getConnections(MyNode fromNode) {
            return fromNode.getConnections();
        }
    }
}
