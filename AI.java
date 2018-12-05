import java.util.*;

class AI {
    int size;
    int maxsize;
    long time;

    AI(){
        time = 0;
        size = 0;
        maxsize = 0;
    }

    List<Node> bfs() {
        //uses queue and initialises with enough space to not need resizing
        Queue<Node> fringe = new ArrayDeque<>(12000000);
        initNode(fringe);
        long startTime = System.currentTimeMillis();
        while (fringe.size() != 0) {
            Node current = fringe.poll();
            size++;
            if (current.isWin()) {
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                return findPathBack(current);
            }
            List<Node> children = current.genChildren();
            for (Node n : children) heuristic(n);
            fringe.addAll(children);
            if (fringe.size() + size > maxsize) {
                maxsize = fringe.size() + size;
            }
        }
        return null;
    }

    List<Node> dfs(Integer depth){
        //uses stack to store nodes, calls different initNode to use ExpandedNode
        Stack<ExpandedNode> fringe = new Stack<>();
        initNode(fringe, true);
        long startTime = System.currentTimeMillis();
        while(fringe.size()!=0){
            ExpandedNode current = fringe.pop();
            size++;
            if(current.isWin()){
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                return findPathBack(current);
            }else {
                if (current.layer != depth) {
                    List<Node> children = current.genChildren();
                    if (depth == -1) Collections.shuffle(children);
                    for (Node n : children) {
                        fringe.push((ExpandedNode) n);
                    }
                    if (fringe.size() +1 > maxsize) {
                        maxsize = fringe.size() + 1;
                    }
                }
            }
            //methods to decide if any removal of branches is necessary
            //these methods use whiles and counters as large fringes can cause stackOverflow errors
            List<Object> t = markVisited(current,0);
            while(t.get(1) == null){
                t = markVisited((ExpandedNode) t.get(0), 0);
            }
            if((Boolean)t.get(1)){
                Node x = cutTopOfRemovalTree(fringe, current,0);
                while(x != null){
                    x = cutTopOfRemovalTree(fringe, x,0);
                }
            }
        }
        return null;
    }

    List<Node> itDeep() {
        time = 0;
        int size = 0;
        int maxsize = 0;
        int i = 0;
        long startTime = System.currentTimeMillis();
        while(i<30){
            //calls dfs on depth i, dfs cannot go past depth i
            List<Node> result = dfs(i);
            //stores and accumulates size + maxsize
            size += this.size;
            if(maxsize<this.maxsize){
                maxsize=this.maxsize;
            }
            if(result!=null) {
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                this.size = size;
                this.maxsize = maxsize;
                return result;
            }
            i++;
        }
        return null;
    }

    List<Node> aStar(){
        PriorityQueue<Node> fringe = new PriorityQueue<>();
        initNode(fringe);
        long startTime = System.currentTimeMillis();
        while(fringe.size()!=0){
            Node current = fringe.poll();
            size++;
            if(current.isWin()){
                long endTime = System.currentTimeMillis();
                time = endTime - startTime;
                return findPathBack(current);
            }else{
                List<Node> children = current.genChildren();
                for(Node n : children){
                    n.heuristic = n.layer + heuristic(n);
                }
                if(fringe.size()+size>maxsize){
                    maxsize = fringe.size()+size;
                }
                fringe.addAll(children);
            }
        }
        return null;
    }

    //generates a list of nodes tht is the path from start to goal
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

    //recursively finds node.parent until root of tree is found
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

    //checks if all the children of a node are visited, if so and node has parent then recurse
    private List<Object> markVisited(ExpandedNode current, int i){
        try {
            List<Object> rt = new ArrayList<>();
            rt.add(current);
            if (i == 5000) {
                rt.add(null);
                return rt;
            }
            if (current.allChildrenVisited()) {
                current.visited = true;
                if (current.parent != null) {
                    i++;
                    rt.add(true);
                    return markVisited((ExpandedNode) current.parent, i);
                }
            }
            rt.add(current.visited);
            return rt;
        }catch (java.lang.StackOverflowError e){
            System.out.println(i);
        }
        return null;
    }

    //cuts visited branch at the top of the tree where parent is unvisited
    private Node cutTopOfRemovalTree(Collection<ExpandedNode> fringe, Node child, int i){
        if(i==5000){
            return child;
        }
        if(child.visited){
            fringe.remove(child);
            if(child.parent!=null){
                if(child.parent.visited){
                    i++;
                    cutTopOfRemovalTree(fringe, child.parent, i);
                }else{
                    ((ExpandedNode)child.parent).children.remove(child);
                }
            }
        }
        return null;
    }

    //initialises a board and node and resets object variables
    private void initNode(Collection<Node> fringe){
        fringe.clear();
        Node n = new Node(null);
        n.newBoard();
        fringe.add(n);
        size = 0;
        maxsize = 0;
        time = 0;
    }

    private void initNode(Collection<ExpandedNode> fringe, boolean t){
        fringe.clear();
        ExpandedNode n = new ExpandedNode(null);
        n.newBoard();
        fringe.add(n);
        size = 0;
        maxsize = 0;
        time = 0;
    }

    //
    private int heuristic(Node n){
        byte[][] tiles = n.getTiles();
        byte[] manhatDis = new byte[]{manhattanDistance(tiles[1], new byte[]{1,1}),
                                      manhattanDistance(tiles[2], new byte[]{1,2}),
                                      manhattanDistance(tiles[3], new byte[]{1,3})};
        //index of highest manhattan distance
        int maxAt = 0;
        for (int i = 0; i < manhatDis.length; i++) {
            maxAt = manhatDis[i] > manhatDis[maxAt] ? i : maxAt;
        }

        int distancefromsoln = 0;
        //distance from solution
        for(int i = 0; i < 3; i++){
            distancefromsoln += manhatDis[i];
        }
        //distance from agent to furthest tile from its solution
        int agentDistance = (manhattanDistance(tiles[0],tiles[maxAt+1]))-1;
        int repeatingModif = 0;
        //if agent was to step back it would take another to get back to the same place (which was previous best)
        //ie add 1 to heuristic
        if(n.parent.move!=0) {
            byte parentMove = n.parent.move;
            switch (n.move) {
                case 8:
                    if (parentMove == 2) {
                        repeatingModif = 1;
                    }
                    break;
                case 2:
                    if (parentMove == 8) {
                        repeatingModif = 1;
                    }
                    break;
                case 4:
                    if (parentMove == 6) {
                        repeatingModif = 1;
                    }
                    break;
                case 6:
                    if (parentMove == 4) {
                        repeatingModif = 1;
                    }
                    break;
                default:
            }
        }
        return  agentDistance+distancefromsoln+repeatingModif;
    }

    //returns manhattan distance of two points
    private byte manhattanDistance(byte[] point, byte[] target){
        return (byte) (Math.abs(point[0]-target[0]) + Math.abs(point[1]-target[1]));
    }
}
