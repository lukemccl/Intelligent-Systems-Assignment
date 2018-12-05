import java.util.*;

class ExpandedNode extends Node{

    //saves list of children for case of DFS
    List<Node> children;

    ExpandedNode(Node parent){
        super(parent);
        children = new ArrayList<>();
    }

    //duplicate of other genChildren, except saves children to object variable
    List<Node> genChildren(){
        for (byte i = 0; i < 4; i++) {
            ExpandedNode n = new ExpandedNode(this);
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
            if (makeMove(n, n.move)) {
                children.add(n);
            }
        }
        return children;
    }

    //checks if all children of this node are visited
    public boolean allChildrenVisited(){
        boolean visited = true;
        for (Node m : children) {
            if (!m.visited) {
                visited = false;
            }
        }
        return visited;
    }
}
