import java.util.*;

class AI {
    int size;
    List<Node> fringe;

    public AI(){
        fringe = new ArrayList<>();
        Node n = new Node(null);
        n.newBoard();
        fringe.add(n);
        size = 0;
    }

    List<Node> bfs(){
        ArrayDeque<Node> fringe = new ArrayDeque<>(6500000);
        Node n = new Node(null);
        n.newBoard();
        fringe.add(n);
        while(fringe.size()!=0){
            Node current = fringe.pollFirst();
            if (current.isWin){
                return findPathBack(current);
            }
            List<Node> children = current.genChildren();
            size += children.size();
            fringe.addAll(children);
        }
        return null;
    }

    List<Node> dfs(Integer depth){
        while(fringe.size()!=0){
            Node current = fringe.get(0);
            if(current.isWin){
                return findPathBack(current);
            }else{
                if(current.layer!=depth || depth==-1) {
                    List<Node> childrenOfCurrent = current.genChildren();
                    size += childrenOfCurrent.size();
                    Collections.shuffle(childrenOfCurrent);
                    fringe.addAll(0, childrenOfCurrent);
                }
            }
            if(markVisited(current)){
                cutTopOfRemovalTree(current);
            }
        }
        return null;
    }

    List<Node> itDeep() {
        int i = 0;
        while(true){
            Node n = new Node(null);
            n.newBoard();
            fringe.add(n);
            List<Node> result = dfs(i);
            if(result!=null) {
                System.out.println(size);
                return result;
            }
            i++;
        }
    }

    List<Node> aStar(){
        while(fringe.size()!=0){
            Node current = fringe.get(0);
            if(current.isWin){
                System.out.println(size);
                return findPathBack(current);
            }else{
                List<Node> childrenOfCurrent = current.genChildren();
                for(Node n : childrenOfCurrent){
                    n.heuristic = heuristic(n);
                }
                size += childrenOfCurrent.size();
                fringe.addAll(childrenOfCurrent);
                Collections.sort(fringe);
            }
            fringe.remove(current);
        }
        return null;
    }


    protected List<Node> findPathBack(Node n){
        List<Node> result = new ArrayList<>();
        Node x = pathBack(result, n);
        while(x != null){
            List<Node> result2 = new ArrayList<>();
            x = pathBack(result2, x);
            result.addAll(result2);
        }
        Collections.reverse(result);
        return result;
    }

    protected Node pathBack(List<Node> result, Node n){
        if(result.size()==5000){
            return n;
        }
        result.add(n);
        if(n.parent!=null) {
            return pathBack(result, n.parent);
        }
        return null;
    }

    boolean markVisited(Node current){
        if (allChildrenVisited(current)){
            current.visited = true;
            if(current.parent!=null){
                markVisited(current.parent);
            }
            return true;
        }
        return false;
    }

    boolean allChildrenVisited(Node n){
        boolean visited = true;
        for (Node m : n.children) {
            if (!m.visited) {
                visited = false;
            }
        }
        return visited;
    }

    void cutTopOfRemovalTree(Node child){
        if(child.visited){
            fringe.remove(child);
        }
        if(child.parent!=null){
            if(child.parent.visited){
                cutTopOfRemovalTree(child.parent);
            }else{
                child.parent.children.remove(child);
            }
        }
    }

    int heuristic(Node n){
        byte[][] tiles = n.getTiles();
        byte[] manhatDis = new byte[]{manhattanDistance(tiles[1], new byte[]{1,1}),
                manhattanDistance(tiles[2], new byte[]{1,2}),
                manhattanDistance(tiles[3], new byte[]{1,3})};
        int distancefromsoln = 0;
        int inPlaceModif = 0;
        for(int i = 1; i < 4; i++){
            distancefromsoln += i -1 + manhatDis[i];
            if(manhatDis[i]==0){
                inPlaceModif+=-2;
            }
        }
        /*for(int i : manhatDis){
            distancefromsoln += i;
            if (i==0){
                inPlaceModif+=-2;
            }
        }*/
        ArrayList<Integer> mds = new ArrayList<>();
        for (int i = 1; i < 4; i++){
            mds.add((int) manhattanDistance(tiles[0], tiles[i]));
        }
        int agentDistance = Collections.max(mds);
        int repeatingModif = 0;
       /* if(n.parent.move.length()>0) {
            char parentMove = n.parent.move.charAt(n.parent.move.length() - 2);
            switch (n.move.charAt(n.move.length() - 2)) {
                case 'U':
                    if (parentMove == 'D') {
                        repeatingModif = 100;
                    }
                    break;
                case 'D':
                    if (parentMove == 'U') {
                        repeatingModif = 100;
                    }
                    break;
                case 'L':
                    if (parentMove == 'R') {
                        repeatingModif = 100;
                    }
                    break;
                case 'R':
                    if (parentMove == 'L') {
                        repeatingModif = 100;
                    }
                    break;
                default:

            }
        }*/
        return agentDistance + 2*distancefromsoln + inPlaceModif + repeatingModif;
    }

    byte manhattanDistance(byte[] point, byte[] target){
        return (byte) (Math.abs(point[0]-target[0]) + Math.abs(point[1]-target[1]));
    }

}
