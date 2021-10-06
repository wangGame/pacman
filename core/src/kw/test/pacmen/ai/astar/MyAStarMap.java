package kw.test.pacmen.ai.astar;

public class MyAStarMap {
    private MyNode[][] map;
    private final int width;
    private final int height;
    public MyAStarMap(int width,int height){
        this.width = width;
        this.height = height;

        map = new MyNode[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = new MyNode(this,x,y);
            }
        }
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MyNode getNodeAt(int x, int y) {
        return map[y][x];
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                stringBuilder.append(map[y][x].isWall ? "#" : " ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
