import java.util.Objects;
import java.util.Scanner;

class GameManual{

    public static Scanner scanner = new Scanner(System.in);
    private Tile[] tiles;
    private int input;
    private int[] dirUp = new int[]{0, -1};
    private int[] dirDown = new int[]{0, 1};
    private int[] dirLeft = new int[]{-1, 0};
    private int[] dirRight = new int[]{1, 0};

    GameManual(int difficulty) {
        tiles = new Tile[difficulty+1];
        tiles[0] = new Agent();
        tiles[0].setCoords(difficulty, difficulty);
        for (int i = 1; i < tiles.length; i++) {
            tiles[i] = new Block(getCharForNumber(i));
            tiles[i].setCoords(i-1,difficulty);
        }
        input = 5;
        run();
    }

    void run(){
        boolean end = false;
        int moves = 0;
        while(!end) {
            printBoard();
            if ((tiles[1].coords == new int[]{1,1}) && (tiles[2].coords == new int[]{1,2}) && (tiles[3].coords == new int[]{1,3})){
                end = true;
            }
            if (!end) {
                input = scanner.nextInt();
                ((Agent) tiles[0]).move(input);
                moves++;
            } else {
                System.out.println("Win with "+ moves+ " moves!");
            }
        }
    }

    private void moveTile(Tile tile, int[] direction) {
        tile.setCoords(tile.coords[0] += direction[0], tile.coords[1] += direction[1]);
    }

    private void printBoard(){
        for(int i = 0; i < tiles.length ; i++){
            for(int j = 0; j < tiles.length; j++){
                Tile toPrint = null;
                for (Tile tile : tiles) {
                    if (tile.coords[0] == j && tile.coords[1] == i) {
                        toPrint = tile;
                        break;
                    }
                }
                if(toPrint!=null){
                    System.out.print(toPrint.val + " ");
                }else{
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        System.out.println("~~~~~~~~");
    }
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    class Tile {

        String val = null;
        int[] coords = new int[2];

        void up() {
            moveTile(this, dirUp);
        }

        void down() {
            moveTile(this, dirDown);
        }

        void left() {
            moveTile(this, dirLeft);
        }

        void right() {
            moveTile(this, dirRight);
        }

        void setCoords(int x, int y) {
            coords[0] = x;
            coords[1] = y;
        }
    }

    private class Block extends Tile {
        Block(String val) {
            this.val = val;
        }
    }

    private class Agent extends Tile {
        Agent() { this.val = "#";}

        void move(Integer dir) {
            switch (dir) {
                case 8:
                    {
                        Tile t1 = null;
                        for (int i = 1; i < tiles.length; i++) {
                            if (Objects.equals(tiles[i].coords[0], coords[0]) && tiles[i].coords[1] == coords[1] - 1) {
                                t1 = tiles[i];
                                break;
                            }
                        }
                        if (t1 != null) {
                            t1.down();
                        }
                        up();
                    }
                    break;
                case 2:
                    {
                        Tile t2 = null;
                        for (int i = 1; i < tiles.length; i++) {
                            if (Objects.equals(tiles[i].coords[0], coords[0]) && tiles[i].coords[1] == coords[1] + 1) {
                                t2 = tiles[i];
                                break;
                            }
                        }
                        if (t2 != null) {
                            t2.up();
                        }
                        down();
                    }
                    break;
                case 4:
                    {
                        Tile t3 = null;
                        for (int i = 1; i < tiles.length; i++) {
                            if (tiles[i].coords[0] == coords[0] - 1 && Objects.equals(tiles[i].coords[1], coords[1])) {
                                t3 = tiles[i];
                                break;
                            }
                        }
                        if (t3 != null) {
                            t3.right();
                        }
                        left();
                    }
                    break;
                case 6:
                    {
                        Tile t4 = null;
                        for (int i = 1; i < tiles.length; i++) {
                            if (tiles[i].coords[0] == coords[0] + 1 && Objects.equals(tiles[i].coords[1], coords[1])) {
                                t4 = tiles[i];
                                break;
                            }
                        }
                        if (t4 != null) {
                            t4.left();
                        }
                        right();
                    }
                    break;
                case 55555:
                    newBoard();
                    break;
                default:
                    System.err.print("No Such Direction");
            }
        }
    }

    void newBoard(){
        tiles[0].coords = new int[]{3,3};
        tiles[1].coords = new int[]{0,3};
        tiles[2].coords = new int[]{1,3};
        tiles[3].coords = new int[]{2,3};
    }
}


