import java.util.Arrays;

class GameState {


    Boolean isWin;
    Boolean isError;
    private byte[][] tiles;
    final static byte[] dirUp = new byte[]{0, -1};
    final static byte[] dirDown = new byte[]{0, 1};
    final static byte[] dirLeft = new byte[]{-1, 0};
    final static byte[] dirRight = new byte[]{1, 0};

    GameState(){
        tiles = new byte[4][2];
        isWin = false;
        isError = false;
    }

    GameState(byte[][] tiles, Boolean isWin, Boolean error){
        this.tiles = Arrays.copyOf(tiles, tiles.length);
        this.setTiles(tiles);
        this.isWin = isWin;
        this.isError = error;
    }

    void setTiles(byte[][] tiles){
        for(int i = 0; i<tiles.length;i++){
            this.tiles[i][0] = tiles[i][0];
            this.tiles[i][1] = tiles[i][1];
        }
    }

    byte[][] getTiles(){
        return tiles;
    }
}
