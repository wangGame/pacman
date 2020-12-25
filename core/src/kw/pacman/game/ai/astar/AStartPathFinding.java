package kw.pacman.game.ai.astar;

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

public class AStartPathFinding {
	 public final AStarMap map;
    /**
     * 一种{@code PathFinder}，可以根据该图提供的信息，找到从任意 Graph图中的一个节点到目标节点的
     * GraphPath路径。
     * 完全实现的路径查找器可以执行可中断和不可中断搜索。
     * 如果特定的路径查找器不能执行两种类型的搜索之一，
     * 则相应的方法应抛出UnsupportedOperationException
     * @param <N>节点类型
     */
	 private final PathFinder<Node> pathfinder;
	 /*
	 {@code Heuristic}生成从给定节点到目标的成本估算。
	 使用启发式函数，寻路算法可以选择最有可能导致最优路径的节点。
	 “最可能”的概念由启发式方法控制。如果试探法是准确的，则该算法将是有效的。
	 如果启发式算法很糟糕，那么它的性能甚至会比不使用任何启发式函数的其他算法
	 （例如* Dijkstra）更糟糕。 * *
	 @param <N>节点类型* *
	 @author davebaol
	  */
	 private final Heuristic<Node> heuristic;
	 private final GraphPath<Connection<Node>> connectionPath;

    public AStartPathFinding(AStarMap map) {
        this.map = map;
        this.pathfinder = new IndexedAStarPathFinder<Node>(createGraph(map));
        this.connectionPath = new DefaultGraphPath<Connection<Node>>();
        this.heuristic = new Heuristic<Node>() {
            @Override
            public float estimate (Node node, Node endNode) {
                // Manhattan distance
                return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
            }
	     };
    }

    public Node findNextNode(Vector2 source, Vector2 target) {
        int sourceX = MathUtils.floor(source.x);
        int sourceY = MathUtils.floor(source.y);
        int targetX = MathUtils.floor(target.x);
        int targetY = MathUtils.floor(target.y);

        if (map == null
               || sourceX < 0 || sourceX >= map.getWidth()
               || sourceY < 0 || sourceY >= map.getHeight()
               || targetX < 0 || targetX >= map.getWidth()
               || targetY < 0 || targetY >= map.getHeight()) {
           return null;
        }
       
        Node sourceNode = map.getNodeAt(sourceX, sourceY);
        Node targetNode = map.getNodeAt(targetX, targetY);
        connectionPath.clear();
        pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);

        return connectionPath.getCount() == 0 ? null : connectionPath.get(0).getToNode();
    }

    private static final int[][] NEIGHBORHOOD = new int[][] {
        new int[] {-1,  0},
        new int[] { 0, -1},
        new int[] { 0,  1},
        new int[] { 1,  0}
    };

    public static MyGraph createGraph (AStarMap map) {
        final int height = map.getHeight();
        final int width = map.getWidth();
        MyGraph graph = new MyGraph(map);
        //一个一个坐标进行扫描， 当前节点可以到达的记录下来
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Node node = map.getNodeAt(x, y);
                if (node.isWall) {
                    continue;
                } 
                // Add a connection for each valid neighbor
                for (int offset = 0; offset < NEIGHBORHOOD.length; offset++) {
                    int neighborX = node.x + NEIGHBORHOOD[offset][0];
                    int neighborY = node.y + NEIGHBORHOOD[offset][1];
                    if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
                        Node neighbor = map.getNodeAt(neighborX, neighborY);
                        if (!neighbor.isWall) {
                            // Add connection to walkable neighbor
                            node.getConnections().add(new DefaultConnection<Node>(node, neighbor));
                        }
                    }
                }
                node.getConnections().shuffle();
            }
        }
        return graph;
    }

    // get width,height and node nums
    private static class MyGraph implements IndexedGraph<Node> {
   		
        AStarMap map;

        public MyGraph (AStarMap map) {
            this.map = map;
        }

        @Override
        public int getIndex(Node node) {
            return node.getIndex();
        }

        // 点的位置   比如 (6,5)  (6,4) 这种数据    乘以它的速度   (entity.nextNode.y - MathUtils.floor(entity.getPosition().y)) * entity.speed
        @Override
        public Array<Connection<Node>> getConnections(Node fromNode) {
            return fromNode.getConnections();
        }

        @Override
        public int getNodeCount() {
            return map.getHeight() * map.getHeight();
        }
   	 
    }
}
