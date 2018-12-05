import java.util.ArrayList;
import java.util.List;

class Main {

    public static void main(String[] args) {
        AI ai = new AI();
        List<Node> n = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    System.out.println("DFS");
                    n = ai.dfs(-1);
                    break;
                case 1:
                    System.out.println("BFS");
                    n = ai.bfs();
                    break;
                case 2:
                    System.out.println("ITDEEP");
                    n = ai.itDeep();
                    break;
                case 3:
                    System.out.println("A*");
                    n = ai.aStar();
                    break;
            }
            System.out.println("Soln size: " + (n.size() - 1));
            System.out.println("T Complexity: " + ai.size);
            System.out.println("S Complexity: " + ai.maxsize);
            System.out.println("Time: " + ai.time + "ms");
            String route = "";
            for (Node m : n) {
                route = route.concat(convertMove(m.move));
                m.printBoard();
            }
            System.out.println(route);
            System.out.println("###############");

        }
    }

    //converts the encoded moves of 2,4,6,8 into letters for easy reading
    private static String convertMove(int move) {
        String s = "";
        switch (move) {
            case 8:
                s = "U ";
                break;
            case 2:
                s = "D ";
                break;
            case 4:
                s = "L ";
                break;
            case 6:
                s = "R ";
                break;
            default:
        }
        return s;
    }
}
