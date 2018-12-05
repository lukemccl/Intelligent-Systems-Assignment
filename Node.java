import java.util.*;

class Node implements Comparable<Node>{

    //variables needed for node functions
    Node parent;
    int layer;
    boolean visited;
    int heuristic;

    //variables needed for state functions
    byte move;
    protected byte[][] tiles;
    protected static byte[][] immutables;
    private final static byte[] dirUp = new byte[]{0, -1};
    private final static byte[] dirDown = new byte[]{0, 1};
    private final static byte[] dirLeft = new byte[]{-1, 0};
    private final static byte[] dirRight = new byte[]{1, 0};

    Node(Node parent){
        this.parent = parent;
        newBoard();
        if(parent==null){
            layer = 0;
        }else{
            layer = parent.layer+1;
            setTiles(parent.tiles);
        }
        move = 0;
        visited = false;
        heuristic = 0;
    }

    //method to set the immutable tiles if any, set number in variable and manually add it with coords
    protected void setImmutables(){
        int numImmutables = 1;
        immutables = new byte[0][2];
        //immutables[0] = new byte[]{2,2};
        //immutables[0] = new byte[]{x,y};
    }

    protected void setTiles(byte[][] tiles){
        for(int i = 0; i<tiles.length;i++){
            this.tiles[i][0] = tiles[i][0];
            this.tiles[i][1] = tiles[i][1];
        }
    }

    protected byte[][] getTiles(){
        return tiles;
    }

    //creates a new board - ensures array object isnt copied from one node to another
    //is how to set the starting coords of all tiles on the board at the start of a game
    protected void newBoard(){
        tiles = new byte[4][2];
        tiles[0] = new byte[]{3,3};
        tiles[1] = new byte[]{0,3};
        tiles[2] = new byte[]{1,3};
        tiles[3] = new byte[]{2,3};
        setImmutables();
    }

    //uses switch statement to generate a node with each node  U, L, R, D
    List<Node> genChildren(){
        ArrayList<Node> children = new ArrayList<>();
        for (byte i = 0; i < 4; i++) {
            Node n = new Node(this);
            switch (i) {
                //up
                case 0:
                    n.move = 8;
                    break;
                //left
                case 1:
                    n.move = 4;
                    break;
                //left
                case 2:
                    n.move = 6;
                    break;
                //right
                case 3:
                    n.move = 2;
                    break;
            }
            //if the move is a valid move it is added to the list to be returned, otherwise it is dropped
            if (makeMove(n, n.move)) {
                children.add(n);
            }
        }
        return children;
    }

    protected static boolean makeMove(Node trial, byte dir) {
        byte[][] tiles = trial.getTiles();
        switch (dir) {
            //for every direction
            case 8:
                //checks if agent is against that edge
                if (tiles[0][1] == 0) {
                    return false;
                }else {
                    //or if a block needs to be swapped
                    for (int i = 1; i < tiles.length; i++) {
                        if (Objects.equals(tiles[i][0], tiles[0][0]) && tiles[i][1] == tiles[0][1] - 1) {
                            moveTile(tiles[i], dirDown);
                            break;
                        }
                    }
                    //then moves agent block as required
                    moveTile(tiles[0], dirUp);
                }
                break;
            case 2:
                if (tiles[0][1] == 3) {
                    return false;
                }else {
                    for (int i = 1; i < tiles.length; i++) {
                        if (Objects.equals(tiles[i][0], tiles[0][0]) && tiles[i][1] == tiles[0][1] + 1) {
                            moveTile(tiles[i], dirUp);
                            break;
                        }
                    }
                    moveTile(tiles[0], dirDown);
                }
                break;
            case 4:
                if (tiles[0][0] == 0) {
                    return false;
                }else {
                    for (int i = 1; i < tiles.length; i++) {
                        if (tiles[i][0] == tiles[0][0] - 1 && Objects.equals(tiles[i][1], tiles[0][1])) {
                            moveTile(tiles[i], dirRight);
                            break;
                        }
                    }
                    moveTile(tiles[0], dirLeft);
                }
                break;
            case 6:
                if (tiles[0][0] == 3) {
                    return false;
                }else {
                    for (int i = 1; i < tiles.length; i++) {
                        if (tiles[i][0] == tiles[0][0] + 1 && Objects.equals(tiles[i][1], tiles[0][1])) {
                            moveTile(tiles[i], dirLeft);
                            break;
                        }
                    }
                    moveTile(tiles[0], dirRight);
                }
                break;
            case 5:
                break;
            default:
                System.err.print("No Such Direction");
        }
        //checks if any tile is in the same space as an immutable tile, if so then move is deemed false
        for (byte[] immutable : immutables) {
            for (byte[] tile : tiles) {
                if (immutable[0] == tile[0] && immutable[1] == tile[1]) {
                    return false;
                }
            }
        }
        //returns true if move is  valid
        return true;
    }

    //returns if the board configuration is a winning configuration
    boolean isWin(){
        return (tiles[1][0]==1 & tiles[1][1]==1) && (tiles[2][0]==1 & tiles[2][1]==2) && (tiles[3][0]==1 & tiles[3][1]==3);
    }

    static private void moveTile(byte[] tile, byte[] direction) {
        tile[0] += direction[0];
        tile[1] += direction[1];
    }

    //prints the board of node
    void printBoard(){
        for(int i = 0; i < tiles.length ; i++){
            for(int j = 0; j < tiles.length; j++){
                String s = null;
                for (int k = 0; k < tiles.length; k++) {
                    if (tiles[k][0] == j && tiles[k][1] == i) {
                        switch(k){
                            case 0:
                                s = "#";
                                break;
                            case 1:
                                s = "A";
                                break;
                            case 2:
                                s = "B";
                                break;
                            case 3:
                                s = "C";
                                break;
                        }
                        break;
                    }
                }
                for (byte[] immutable : immutables) {
                    if (immutable[0] == j && immutable[1] == i) {
                        s = "N";
                        break;
                    }
                }
                if(s==null){
                    s = "*";
                }
                System.out.print(s+" ");
            }
            System.out.println();
        }
        System.out.println("~~~~~~~~");
    }

    //used for priorityqueue to sort
    @Override
    public int compareTo(Node o) {
        return Integer.compare(heuristic, o.heuristic);
    }
}
