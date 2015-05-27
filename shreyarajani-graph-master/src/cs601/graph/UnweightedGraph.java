package cs601.graph;

import java.util.*;

public class UnweightedGraph<ID extends Comparable, N extends Node<ID>>
        implements Graph<ID, N> {
    LinkedHashMap<ID, N> map = new LinkedHashMap(); //ID = name , N = object

    public N addNode(N node) {
        if(node != null) {
            if (!(map.containsKey(node.getName()))) {
                map.put(node.getName(), node);
            }
            return map.get(node.getName());
        }
        return node;
    }

    @Override
    public List<N> getAllNodes(ID start, ID stop) {
        List allNodes = new ArrayList<>();
        if (start == null || stop == null) {
            return Collections.emptyList();
        } else {
            List<Stack<ID>> allPaths = giveDFS(start, stop);
            Set finalAllNodes  = new LinkedHashSet();
            for (Stack<ID> path: allPaths){
                for (ID node: path){
                    finalAllNodes.add(node);
                }
            }

            return new ArrayList<>(finalAllNodes);
        }
    }

    private List<Stack<ID>> giveDFS(ID start, ID stop) {
        List<Stack<ID>> allPaths = new ArrayList<>();
        Stack<ID> current = new Stack();
        getEdgesFromNodes(start, stop, allPaths ,current);
        return allPaths;
    }

    private void getEdgesFromNodes(ID start, ID stop, List<Stack<ID>> allPaths, Stack current) {

        current.push(start);
        if(start.equals(stop)){
            Stack vaildStack = (Stack) current.clone();
            allPaths.add(vaildStack);
            current.pop();
            return;
        }
        for(ID edge: map.get(start).edges()){
            getEdgesFromNodes(edge, stop, allPaths, current);
        }
        current.pop();
    }

    @Override
    public int getMinPathLength(ID start, ID stop) {
        if(start == null || stop == null){
            return -1;
        }
        List<Stack<ID>> allPaths = giveDFS(start, stop);
        int minCount = Integer.MAX_VALUE;
        for (Stack<ID> path : allPaths){
            if(minCount > path.size()){
                minCount = path.size();
            }
        }
        return (minCount == Integer.MAX_VALUE)? -1:minCount-1;
    }

    @Override
    public List<N> getAllReachableNodes(ID start) {
        Set<Integer> resultSet = new HashSet<>();
        if (start == null) {
            return Collections.emptyList();
        } else {
            getEdgesFromNodes(start, resultSet);
        }
        List list= new ArrayList<>();
        list.addAll(resultSet);
        return list;
    }

    private void getEdgesFromNodes(ID start, Set resultSet) {

        resultSet.add(start);

        for(ID edge: map.get(start).edges()){
            getEdgesFromNodes(edge, resultSet);
        }
    }

    @Override
    public List<ID> getRootNames() {
        Set<ID> nodes = map.keySet();
        Set<ID> edgesList = new HashSet<>();
        for(ID node : nodes){
            edgesList.addAll((Collection<? extends ID>) map.get(node).edges());
        }
        for(ID node: edgesList){
            nodes.remove(node);
        }
        return (nodes == null)? new ArrayList<ID>() : new ArrayList<ID>(nodes);
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for(ID key: map.keySet()){
            N node = map.get(key);
            for(ID edge : node.edges()){
                ID name = node.getName();
                sb.append(name + " -> " + edge + "\n");
            }
        }
        return sb.toString();
    }
}