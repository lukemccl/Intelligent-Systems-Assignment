import java.util.*;

class Node implements Comparable<Node>{


    Node parent;
    List<Node> children;
    byte layer;
    boolean visited;
    int heuristic;

    byte move;
    boolean isWin;
    private boolean isError;
    private byte[][] tiles;
    private final static byte[] dirUp = new byte[]{0, -1};
    private final static byte[] dirDown = new byte[]{0, 1};
    private final static byte[] dirLeft = new byte[]{-1, 0};
    private final static byte[] dirRight = new byte[]{1, 0};

    Node(Node parent){
        children = new ArrayList<>();
        this.parent = parent;
        if(parent==null){
            layer = 0;
        }else{
            layer = (byte) (parent.layer+1);
        }
        move = 0;
        isWin = isError = false;
        newBoard();
        visited = false;
        heuristic = 0;
    }

    private void setTiles(byte[][] tiles){
        for(int i = 0; i<tiles.length;i++){
            this.tiles[i][0] = tiles[i][0];
            this.tiles[i][1] = tiles[i][1];
        }
    }

    void newBoard(){
        tiles = new byte[4][2];
        tiles[0] = new byte[]{3,3};
        tiles[1] = new byte[]{0,3};
        tiles[2] = new byte[]{1,3};
        tiles[3] = new byte[]{2,3};
    }

    List<Node> genChildren(){
        for (byte i = 0; i < 4; i++) {
            Node n = new Node(this);
            n.setTiles(tiles);
            //String move = "";
            switch (i) {
                //up
                case 0:
                    //move = "U ";
                    n.move = 8;
                    n = makeMove(n, 8);
                    break;
                //down
                case 1:
                    //move = "D ";
                    n.move = 2;
                    n = makeMove(n, 2);
                    break;
                //left
                case 2:
                    //move = "L ";
                    n.move = 4;
                    n = makeMove(n, 4);
                    break;
                //right
                case 3:
                    //move = "R ";
                    n.move = 6;
                    n = makeMove(n, 6);
                    break;
            }
            if (!n.isError) {
                children.add(n);
            }
        }
        return children;
    }

    byte[][] getTiles(){
        return tiles;
    }


    static private Node makeMove(Node trial, Integer dir) {
        byte[][] tiles = trial.getTiles();
        switch (dir) {
            case 8:
                if (tiles[0][1] == 0) {
                    trial.isError = true;
                }else {
                    for (int i = 1; i < tiles.length; i++) {
                        if (Objects.equals(tiles[i][0], tiles[0][0]) && tiles[i][1] == tiles[0][1] - 1) {
                            moveTile(tiles[i], dirDown);
                            break;
                        }
                    }
                    moveTile(tiles[0], dirUp);
                }
                break;
            case 2:
                if (tiles[0][1] == 3) {
                    trial.isError = true;
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
                    trial.isError = true;
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
                    trial.isError = true;
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
        if((tiles[1][0]==1 & tiles[1][1]==1) && (tiles[2][0]==1 & tiles[2][1]==2) && (tiles[3][0]==1 & tiles[3][1]==3)) {
            trial.isWin = true;
        }
        return trial;
    }

    static private void moveTile(byte[] tile, byte[] direction) {
        tile[0] += direction[0];
        tile[1] += direction[1];
    }

    void printBoard(){
        for(int i = 0; i < tiles.length ; i++){
            for(int j = 0; j < tiles.length; j++){
                String s = null;
                for (int k = 0; k < tiles.length; k++) {
                    byte[] tile = tiles[k];
                    if (tile[0] == j && tile[1] == i) {
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
                if(s!=null){
                    System.out.print(s + " ");
                }else{
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        System.out.println("~~~~~~~~");
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(heuristic, o.heuristic);
       /* if(this.heuristic>o.heuristic){
            return -1;
        }
        if(this.heuristic<o.heuristic){
            return 1;
        }
        return 0;*/
    }
}
