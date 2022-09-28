import java.util.*;

public class PowerGrid
{
    static class EdgeCompare implements Comparable
    {
        Edge edge;

        EdgeCompare(Edge edge) {
            this.edge = edge;
        }

        @Override
        public int compareTo(Object o) {
            return ((Double) edge.getData()).compareTo((Double)((EdgeCompare) o).edge.getData());
        }

        @Override
        public String toString() {
            return this.edge.getFirstEndpoint().getName().toString()
                    + "     " +
                    this.edge.getSecondEndpoint().getName().toString()
                    + "     " +
                    this.edge.getData();
        }
    }
    public static void main(String[] args)
    {
        String s;
        while (true)
        {
            System.out.println ("Enter a txt file name: like (test1.txt, test2.txt)");
            s = KeyboardReader.readString();
            if (s == KeyboardReader.EOI_STRING) {
                System.out.println ("EOI");
                System.out.println ("EOI"); // swallowed!!?
                break;
            }
            else if (s == KeyboardReader.ERROR_STRING) {
                System.out.println ("ERROR");
                continue;
            }
            else
                System.out.println (s + " entered");
            break;
        }
        SimpleGraph G = new SimpleGraph();
        GraphInput.LoadSimpleGraph(G, s);

        Set<EdgeCompare> edges = kruskal(G);
        System.out.println(printHelper(edges));

    }
    private static String printHelper(Set<EdgeCompare> set)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Edges of the MST: ");
        sb.append("\n");
        int totalWeight = 0;
        for (EdgeCompare edgeCompare : set)
        {
            sb.append(edgeCompare.toString());
            sb.append("\n");
            totalWeight += (Double)edgeCompare.edge.getData();
        }
        sb.append("\n");
        sb.append("Total weight is: " + totalWeight);
        return sb.toString();
    }
    public static Set<EdgeCompare> kruskal(SimpleGraph graph)
    {
        Set<EdgeCompare> resultSet = new HashSet<>();
        PriorityQueue<EdgeCompare> heap = new PriorityQueue<>();
        Iterator iterator = graph.edges();
        while (iterator.hasNext())
        {
            Edge edge = (Edge) iterator.next();
            heap.add(new EdgeCompare(edge));
        }
        Iterator iterator1 = graph.vertices();
        int counter = 1;
        while (iterator1.hasNext())
        {
            ((Vertex)iterator1.next()).setData(counter);
            counter++;
        }
        int[] treeArray = new int[graph.numVertices() + 1];
        Arrays.fill(treeArray, -1);
        int totalEdges = 0;
        while (totalEdges < graph.numVertices() - 1 && !heap.isEmpty())
        {
            EdgeCompare min = heap.poll(); // shortest edge
            Vertex node1 = min.edge.getFirstEndpoint();
            Vertex node2 = min.edge.getSecondEndpoint();

            int node1Data = find(treeArray, (int) node1.getData());
            int node2Data = find(treeArray, (int) node2.getData());

            if (unionSet(treeArray, node1Data, node2Data))
            {
                totalEdges++;
                resultSet.add(min);
            }
        }
        return resultSet;
    }
    private static boolean unionSet(int[] trees, int node1, int node2)
    {
        if (node1 == node2) return false;
        if (trees[node1] >= trees[node2])
        {
            trees[node1] = node2;
        }
        else
        {
            trees[node2] = node1;
        }
        return true;
    }

    private static int find(int[] trees, int nodeData)
    {
        if (trees[nodeData] < 0)
        {
            return nodeData;
        }
        else
        {
            return find(trees, trees[nodeData]);
        }
    }
}
