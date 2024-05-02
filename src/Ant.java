public class Ant {
    protected int pathSize;
    protected int[] path;
    protected boolean[] explored;

    public Ant(int size) {
        this.pathSize = size;
        this.path = new int[size];
        this.explored = new boolean[size];
    }

    protected void exploreCity(int currentIndex, int city) {
        path[currentIndex + 1] = city;
        explored[city] = true;
    }

    protected boolean hasExplored(int index) {
        return explored[index];
    }

    protected double calculatePathLength(double[][] graph) {
        double length = graph[path[pathSize - 1]][path[0]];
        for (int i = 0; i < pathSize - 1; i++)
            length += graph[path[i]][path[i + 1]];
        return length;
    }

    protected void clearExploredCities() {
        for (int i = 0; i < pathSize; i++)
            explored[i] = false;
    }
}
