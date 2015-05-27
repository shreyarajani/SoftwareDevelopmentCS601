package cs601.graph;
import java.util.ArrayList;
import java.util.List;

/** A node with a name and list of directed edges (edges do not have labels). */
public class NamedNode implements Node<String> {

    String name;
    List<String> edges = new ArrayList<>();

    public NamedNode(String name) {
        this.name = name;
    }

    @Override
    public String getEdge(int i) throws IndexOutOfBoundsException {
        if(!(i >=0 && i < edges.size())){
            throw new ArrayIndexOutOfBoundsException();
        }
        return edges.get(i);
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

    @Override
    public void addEdge(Node<String> target) {
        if(target == null){
            return;
        }
        String name = target.getName();
        if(!(edges.contains(name))){
            edges.add(name);
        }
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Iterable<String> edges() {
        return edges;
    }

    @Override
    public int compareTo(Node<String> o) {

        if(name.equals(o.getName())){
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public String toString(){
        return name;
    }
}
