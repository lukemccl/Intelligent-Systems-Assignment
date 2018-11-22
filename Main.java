import java.util.List;

class Main {

    public static void main (String[] args){

        // GameManual game = new GameManual(3);
        AI ai = new AI();
        final long startTime = System.currentTimeMillis();
        List<Node> n = ai.bfs();
        final long endTime = System.currentTimeMillis();
        System.out.println("Soln size: " + (n.size()-1));
        System.out.println("T Complexity: " + ai.size);
        System.out.println("Time: " + (endTime-startTime) +"ms");
        System.out.println();
        for(Node m: n) System.out.print(convertMove(m.move));
        System.out.println();
        //for(Node m: n) m.printBoard();
    }

    static String convertMove(int move){
        String s = "";
        switch (move){
            case 8:
                s="U ";
                break;
            case 2:
                s="D ";
                break;
            case 4:
                s="L ";
                break;
            case 6:
                s="R ";
                break;
            default:
        }
        return s;
    }
}
