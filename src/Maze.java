
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Maze{
    static final int WHITE = 1;
    static final int GREY = 2;
    static final int BLACK = 3;

    static final int PATH = 1;
    static final int PATHDFS = 2;
    static final int PATHBFS = 3;

    Vertex[][] graph;
    Node[] adjList;
    final int SIZE;
    Stack<Vertex> cellStack;
    int totalCells;
    Vertex currentCell;
    int visitedCells;
    int[][] adjMatrix;


    public Maze(int size)
    {
        this.SIZE = size;
        totalCells = SIZE * SIZE;
        adjMatrix = new int[SIZE][SIZE] ;
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                adjMatrix[i][j] = 0;
            }
        }// fill adjMatrix with 0s
        adjList = new Node[totalCells];
        for(int i = 0; i < totalCells; i++){
            adjList[i] = null;
        }
        cellStack = new Stack<Vertex>();
        graph = new Vertex[SIZE][SIZE];
    }

    public void displayMaze(int path){
        System.out.println("");
        for(int i = 0; i < SIZE; i++){
            for(int j =0; j < SIZE; j++)
            {
                if(graph[j][i].hasNorthWall){
                    if(graph[j][i] == graph[0][0])
                        System.out.print("+   ");
                    else
                        System.out.print("+---");
                }
                else{// open wall
                    System.out.print("+   ");
                }
            }// end inner loop
            System.out.println("+");
            for(int j =0; j < SIZE; j++)
            {
                // PATH = display path with #s
                if(path == PATH)
                {
                    if(graph[j][i].hasWestWall){
                        if(graph[j][i].isInPath)
                            System.out.print("| " + "#"+" ");
                        else
                            System.out.print("|   ");
                    }
                    else{
                        if(graph[j][i].isInPath)
                            System.out.print("  " + "#" +" ");
                        else
                            System.out.print("    ");
                    }
                }//end if PATH
                // PATHDFS means path as visited by dfs
                else if(path == PATHBFS)
                {
                    if(graph[j][i].hasWestWall)
                    {
                        //graph[j][i].distance <= graph[SIZE-1][SIZE-1].distance
                        if(graph[j][i].visitBfsOrder <= graph[SIZE-1][SIZE-1].visitBfsOrder)
                        {
                            System.out.print("| " + String.format("%2s", graph[j][i].visitBfsOrder));
                        }
                        else
                            System.out.print("|   ");
                    }
                    else{
                        if(graph[j][i].visitBfsOrder <= graph[SIZE-1][SIZE-1].visitBfsOrder)
                        {
                            System.out.print("  " +  String.format("%2s", graph[j][i].visitBfsOrder));
                        }
                        else
                            System.out.print("    ");
                    }
                }// else PATHBFS
                else{
                    if(graph[j][i].hasWestWall){
                        if(graph[j][i].dtime <= graph[SIZE-1][SIZE-1].dtime)
                        {
                            System.out.print("| " + String.format("%2s", graph[j][i].visitedOrder));
                        }
                        else
                            System.out.print("|   ");
                    }
                    else{
                        if(graph[j][i].dtime <= graph[SIZE-1][SIZE-1].dtime)
                        {
                            System.out.print("  " +  String.format("%2s", graph[j][i].visitedOrder));
                        }
                        else
                            System.out.print("    ");
                    }
                }// else PATHDFS

            }// end inner loop
            System.out.println("|");
        }
        for(int j =0; j < SIZE; j++){
            if(j == SIZE-1)
                System.out.print("+   ");
            else
                System.out.print("+---");

        }// end inner loop
        System.out.println("+");

    }


    public void mazeGenerator()
    {
        currentCell = graph[0][0];
        currentCell.isVisited = true;
        visitedCells = 1;

        while(visitedCells < totalCells)
        {
            if(validNeighbors(currentCell)!=0)
            {
                Vertex vtx = null;

                Random generator = new Random();
                int random = generator.nextInt(validNeighbors(currentCell));
                vtx = currentCell.neighbors.get(random);

                removeWall(currentCell, vtx);

                Node newNode = new Node(vtx);
                Node currentNode = adjList[currentCell.label-1];
                if (currentNode == null) {
                    currentNode = newNode;
                    adjList[currentCell.label-1] = currentNode;
                }
                else {
                    while(currentNode.next != null){
                        currentNode = currentNode.next;
                    }
                    currentNode.next = newNode;
                }

                Node newNode2 = new Node(currentCell);
                currentNode = adjList[vtx.label-1];
                if (currentNode == null) {
                    currentNode = newNode2;
                    adjList[vtx.label-1] = newNode2;
                }
                else {
                    while(currentNode.next != null){
                        currentNode = currentNode.next;
                    }
                    currentNode.next = newNode2;
                }
                cellStack.push(currentCell);
                currentCell = vtx;
                currentCell.isVisited = true;
                visitedCells++;
            }else{
                if(!cellStack.isEmpty()){

                    Vertex v = cellStack.pop();
                    currentCell = v;
                }
            }

        }
    }
    int visitBfsOrd;
    public void bfs()
    {
        Vertex s = this.graph[0][0];
        s.color = GREY;
        s.distance = 0;
        s.parent = null;

        Queue<Vertex> queue = new LinkedList<Vertex>();
        queue.add(s);

        while(!queue.isEmpty())
        {
            Vertex u = queue.remove();
            Node v = adjList[u.label - 1];
            while(v != null)
            {
                if(v.vxt.color == WHITE)
                {
                    v.vxt.color = GREY;
                    visitBfsOrd = visitBfsOrd + 1;
                    v.vxt.visitBfsOrder = visitBfsOrd;
                    v.vxt.distance = u.distance + 1;
                    v.vxt.parent = u;
                    queue.add(v.vxt);
                    //System.out.println("U node=" + u.label);
                    //System.out.println("V node=" + v.vxt.label);
                }
                v = v.next;
            }
        }
    }// BSF

    int time;
    int visitOrd;
    public void dfs()
    {
        time = 0;
        Vertex currentNode;// = graph[graph[0].label-1];
        System.out.println("");
        for(int i = 0; i < totalCells; i++){

            currentNode = graph[i/SIZE][i%SIZE];
            if(currentNode.color == WHITE)
                dfsVisit(currentNode);
        }//end for loop
        System.out.println("");
    }//end dfs

    public void dfsVisit(Vertex u){
        time = time + 1;
        u.visitedOrder = visitOrd;
        visitOrd = visitOrd + 1;
        u.dtime = time;
        u.color = GREY;
        Node v = adjList[u.label - 1];
        while(v != null){
            if(v.vxt.color == WHITE){
                v.vxt.parent = u;
                dfsVisit( v.vxt);
            }
            v = v.next;
        }
        u.color = BLACK;
        time = time + 1;
        u.ftime = time;
    }

    public void printPath( Vertex startVtx, Vertex endVtx){

        if (startVtx.label == endVtx.label)
            ;//System.out.print(" " + startVtx.label);
        else if (endVtx.parent == null)
            System.out.print(" No path exists");
        else
            printPath( startVtx, endVtx.parent);
        endVtx.isInPath = true;
        //System.out.print(endVtx.label);
    }

    public void printAdjList(Node[] graph){
        Node currentNode = null;
        for(int i = 0; i < totalCells; i++)
        {
            System.out.print("\nRow : ["+(i+1)+"]");
            currentNode = graph[i];
            while(currentNode != null){
                System.out.print("->"+currentNode.vxt.label);
                currentNode = currentNode.next;
            }
        }
    }

    public int validNeighbors(Vertex v)
    {
        Iterator<Vertex> it = v.neighbors.iterator();
        while (it.hasNext())
        {
            Vertex vtx = it.next();
            if(vtx.isVisited)
            {
                it.remove();
            }
        }
        int neighbors =0;
        //This loop filters out neighbors that have already been visited
        for(int x = 0; x < v.neighbors.size(); x++)
        {
            if(!v.neighbors.get(x).isVisited)
            {
                neighbors++;
            }
        }
        return neighbors;
    }// end validNeighbors

    /**
     * This method removes the walls between two given cells.
     * @param current vertex
     * @param next vertex
     */

    public void removeWall(Vertex current, Vertex next)
    {
        //System.out.println("Currently removing walls between " + current.label + " and " + next.label);
        if(current.label + SIZE == next.label)
        {
            //remove south wall
            current.hasSouthWall = false;
            next.hasNorthWall = false;
        }
        else if(current.label + 1 ==next.label)
        {
            //remove east wall
            current.hasEastWall = false;
            next.hasWestWall = false;
        }
        else if(current.label -1 == next.label)
        {
            //remove the west wall
            current.hasWestWall = false;
            next.hasEastWall = false;
        }
        else if(current.label - SIZE == next.label)
        {
            //remove the northern wall
            current.hasNorthWall = false;
            next.hasSouthWall = false;
        }

        current.neighbors.remove(next);
        next.neighbors.remove(current);
    }//end removeWall

    /**
     * assign attributes to vertices
     */
    public void fill()
    {
        int vertexNumber = 1;

        //This loop creates a new vertex
        for(int i=0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                Vertex v = new Vertex(j,i);
                graph[j][i] = v;
            }
        }

        //adds values to vertex
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                graph[j][i].label = vertexNumber;
                graph[j][i].parent = null;
                vertexNumber++;
            }
        }

        //This loop assigns the neighbors
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                assignNeighbors(graph[j][i]);
            }
        }
        mazeGenerator();
    }

    /**
     * This method assigns neighbors to a specified cell.
     * Uses simple constraints such as determining the (x,y) coordinates of the aforementioned cell
     * @param currentCell
     */
    public void assignNeighbors(Vertex v)
    {
        //cell north of current cell
        if(v.y != 0)
        {
            v.neighbors.add(graph[v.x][v.y-1]);
        }

        //cell south of the current cell
        if(v.y != (SIZE-1))
        {
            v.neighbors.add(graph[v.x][v.y+1]);
        }

        //cell left of the current cell
        if(v.x != 0)
        {
            v.neighbors.add(graph[v.x -1][v.y]);
        }

        //right of the current
        if(v.x != SIZE-1)
        {
            v.neighbors.add(graph[v.x + 1][v.y]);
        }
    }

    class Node{
        Node next = null;
        Vertex vxt;
        public Node(){}
        public Node(Vertex x){
            vxt = x;
        }
    }// node inner class

    class Vertex
    {
        int visitBfsOrder;
        int distance;
        int label;
        int x;
        int y;
        int visitNum = 0;
        int color = WHITE;
        boolean isVisited = false;
        boolean isInPath = false;
        int dtime;
        int ftime;
        Vertex parent;
        int visitedOrder = 0;

        boolean hasNorthWall = true;
        boolean hasSouthWall = true;
        boolean hasEastWall = true;
        boolean hasWestWall = true;
        boolean hasAllWalls = true;
        ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
        public Vertex(int x, int y){
            this.x = x;
            this.y = y;
        }
    }// node inner class

}
