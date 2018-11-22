import java.util.*;

class AI {
    int size;
    int maxsize;
    long time;
    private List<Node> fringe;

    AI(){
        fringe = new ArrayList<>();
        time = 0;
        size = 1;
        maxsize = 1;
    }

    List<Node> bfs(){
        try {
            ArrayDeque<Node> fringe = new ArrayDeque<>(12000000);
            initNode(fringe);
            long startTime = System.currentTimeMillis();
            while (fringe.size() != 0) {
                Node current = fringe.pollFirst();
                if (current.isWin()) {
                    long endTime = System.currentTimeMillis();
                    time = endTime - startTime;
                    return findPathBack(current);
                }
                List<Node> children = current.genChildren();
                fringe.addAll(children);
                if(fringe.size()>maxsize){
                    maxsize = fringe.size();
                }
                size = fringe.size();
                //if (size % 15000 == 0) System.out.println(current.layer + "\t" + size);
            }
            return null;
        }catch (java.lang.OutOfMemoryError e){
            System.out.println("Halted on: " +size);
        }
        return null;
    }

    List<Node> dfs(Integer depth){
        Stack<Node> fringe = new Stack<>();
        initNode(fringe);
        long startTime = System.currentTimeMillis();
        while(fringe.size()!=0){
            Node current = fringe.pop();
            if(current.isWin()){
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                System.out.println("found");
                return findPathBack(current);
            }else {
                if (current.layer != depth || depth == -1) {
                    List<Node> children = current.genChildren();
                    size += children.size();
                    if (fringe.size() + 1 > maxsize) {
                        maxsize = fringe.size() + 1;
                    }
                    if (depth == -1) Collections.shuffle(children);
                    for (Node n : children) {
                        fringe.push(n);
                    }
                }
            }
            if(markVisited(current)){
                Node x = cutTopOfRemovalTree(current,0);
                while(x != null){
                    x = cutTopOfRemovalTree(x,0);
                }
            }
        }
        return null;
    }

    List<Node> itDeep() {
        initNode(fringe);
        int i = 0;
        long startTime = System.currentTimeMillis();
        while(true){
            List<Node> result = dfs(i);
            if(result!=null) {
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                return result;
            }
            i++;
        }
    }

    List<Node> aStar(){
        initNode(fringe);
        long startTime = System.currentTimeMillis();
        while(fringe.size()!=0){
            Node current = fringe.get(0);
            if(current.isWin()){
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                return findPathBack(current);
            }else{
                List<Node> childrenOfCurrent = current.genChildren();
                for(Node n : childrenOfCurrent){
                    n.heuristic = n.layer + heuristic(n);
                }
                size += childrenOfCurrent.size();
                if(fringe.size()>maxsize){
                    maxsize = fringe.size();
                }
                fringe.addAll(childrenOfCurrent);
                Collections.sort(fringe);
            }
            fringe.remove(current);
        }
        return null;
    }


    private List<Node> findPathBack(Node n){
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

    private Node pathBack(List<Node> result, Node n){
        if(result.size()==5000){
            return n;
        }
        result.add(n);
        if(n.parent!=null) {
            return pathBack(result, n.parent);
        }
        return null;
    }

    private boolean markVisited(Node current){
        if (allChildrenVisited(current)){
            current.visited = true;
            if(current.parent!=null){
                markVisited(current.parent);
            }
            return true;
        }
        return false;
    }

    private boolean allChildrenVisited(Node n){
        boolean visited = true;
        for (Node m : n.children) {
            if (!m.visited) {
                visited = false;
            }
        }
        return visited;
    }

    private Node cutTopOfRemovalTree(Node child, int i){
        if(i==5000){
            return child;
        }
        if(child.visited){
            fringe.remove(child);
        }
        if(child.parent!=null){
            if(child.parent.visited){
                i++;
                cutTopOfRemovalTree(child.parent, i);
            }else{
                child.parent.children.remove(child);
            }
        }
        return null;
    }

    private void initNode(Collection<Node> fringe){
        fringe.clear();
        size = 0;
        time = 0;
        maxsize = 0;
        Node n = new Node(null);
        n.newBoard();
        fringe.add(n);
    }

    private int heuristic(Node n){
        byte[][] tiles = n.getTiles();
        byte[] manhatDis = new byte[]{manhattanDistance(tiles[1], new byte[]{1,1}),
                                      manhattanDistance(tiles[2], new byte[]{1,2}),
                                      manhattanDistance(tiles[3], new byte[]{1,3})};
        int distancefromsoln = 0;
        for(int i = 0; i < 3; i++){
            distancefromsoln += i-1 + 2*manhatDis[i];
        }
        ArrayList<Integer> mds = new ArrayList<>();
        for (int i = 1; i < 4; i++){
            mds.add((int) manhattanDistance(tiles[0], tiles[i]));
        }
        int agentDistance = Collections.max(mds);
        int repeatingModif = 0;
        if(n.parent.move!=0) {
            byte parentMove = n.parent.move;
            switch (n.move) {
                case 8:
                    if (parentMove == 2) {
                        repeatingModif = 100;
                    }
                    break;
                case 2:
                    if (parentMove == 8) {
                        repeatingModif = 100;
                    }
                    break;
                case 4:
                    if (parentMove == 6) {
                        repeatingModif = 100;
                    }
                    break;
                case 6:
                    if (parentMove == 4) {
                        repeatingModif = 100;
                    }
                    break;
                default:
            }
        }
        return agentDistance + distancefromsoln + repeatingModif;
    }

    private byte manhattanDistance(byte[] point, byte[] target){
        return (byte) (Math.abs(point[0]-target[0]) + Math.abs(point[1]-target[1]));
    }
}
