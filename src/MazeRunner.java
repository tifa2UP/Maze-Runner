
public class MazeRunner {

    public static void main(String[] arg){
        int PATH = 1;
        int PATHDFS = 2;
        int PATHBFS = 3;
        int SIZE = 4;

        Maze maze = new Maze(SIZE);
        maze.fill();
        System.out.println("Maze 1 for DFS");
        maze.displayMaze(PATH);
        maze.dfs();
        maze.printPath(maze.graph[0][0], maze.graph[SIZE-1][SIZE-1]);
        System.out.println("Single Path :");
        maze.displayMaze(PATH);
        System.out.println("\nDFS: ");
        maze.displayMaze(PATHDFS);

        Maze maze1 = new Maze(SIZE);
        maze1.fill();
        System.out.println("\n\nMaze 2 for BFS");
        maze1.displayMaze(PATH);
        maze1.bfs();
        maze1.printPath(maze1.graph[0][0], maze1.graph[SIZE-1][SIZE-1]);
        System.out.println("Single Path: ");

        maze1.displayMaze(PATH);
        System.out.println("BFS: ");
        maze1.displayMaze(PATHBFS);
    }
}
