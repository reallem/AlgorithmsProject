/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package falgorithmsproject;
import java.util.*;

// Edge class representing a graph edge
class Edge implements Comparable<Edge> {
    int src, dest, weight; // src= source vertex of the edge, dest= destination vertex of the edge.

    // Comparator function used for sorting edges
    public int compareTo(Edge compareEdge) {
        return this.weight - compareEdge.weight; // Compare edges based on their weight
    }
}

// Subset class used for union-find
class Subset {
    int parent, rank;// parent node in the union-find, rank used to keep the tree flat in the union-find.
// parent to keep track of the parent node, rank to manage the tree height
}

// Graph class representing the graph
class Graph {
    int V, E; // Number of vertices and edges
    Edge edge[]; // Collection of edges

    Graph(int v, int e) { // Constructor to initialize the graph
        V = v; // Set the number of vertices
        E = e; // Set the number of edges
        edge = new Edge[E]; // Initialize the edge array
        for (int i = 0; i < e; ++i) {
            edge[i] = new Edge(); // Create a new Edge object for each edge
        }
    }
// A utility function to find the subset of an element i
    int find(Subset subsets[], int i) {
        if (subsets[i].parent != i)// If the parent is not itself, then it's part of another subset
            subsets[i].parent = find(subsets, subsets[i].parent);// Path compression
        return subsets[i].parent;
    }
// A function that does union of two subsets x and y
    void union(Subset subsets[], int x, int y) { // x,y are element to union 
        int xroot = find(subsets, x); // xroot= root containing element x , Find root of subset x
        int yroot = find(subsets, y); // yroot= root containing element y ,Find root of subset y
// Attach smaller rank tree under root of high rank tree
        if (subsets[xroot].rank < subsets[yroot].rank) {
            subsets[xroot].parent = yroot;
        } else if (subsets[xroot].rank > subsets[yroot].rank) {
            subsets[yroot].parent = xroot;
        } else {
            subsets[yroot].parent = xroot;// If ranks are the same, make one root and increase its rank
            subsets[xroot].rank++;
        }
    }

    void KruskalMST() {
        Edge result[] = new Edge[V]; // array to store the resultant MST
        int e = 0; // index variable to result 
        int i = 0; // index variable for sorted edges 
        for (i = 0; i < V; ++i)
            result[i] = new Edge();
// Step 1: Sort all the edges in non-decreasing order of their weight
        Arrays.sort(edge);
// Allocate memory for creating V subsets
        Subset subsets[] = new Subset[V]; // array of subsets for union 
        for (i = 0; i < V; ++i)
            subsets[i] = new Subset();
 // Create V subsets with single elements
        for (int v = 0; v < V; ++v) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        i = 0;// Initialize index of sorted edges
        while (e < V - 1) {
            Edge next_edge = edge[i++];// Step 2: Pick the smallest edge. And increment the index for next iteration
            int x = find(subsets, next_edge.src);
            int y = find(subsets, next_edge.dest);
 // If including this edge doesn't cause cycle, include it in result and increment the index of result for next edge
            if (x != y) {
                result[e++] = next_edge;
                union(subsets, x, y);
            }
        }
// Print the constructed MST
        System.out.println("Following are the edges in the constructed MST");
        for (i = 0; i < e; ++i)
            System.out.println(result[i].src + " -- " + result[i].dest + " == " + result[i].weight);
    }
}

// Dijkstra class implementing the algorithm
class Dijkstra {
    static final int V = 50; // Number of vertices

    int minDistance(double dist[], Boolean sptSet[]) {  // disr[]= array of shortest dist from the src, sptSet[]= array to keep track of the vert include in SPT
        double min = Double.MAX_VALUE;  // min= minimum dist value  
        int min_index = -1; // index of vertex with min dist value
        for (int v = 0; v < V; v++)
            if (!sptSet[v] && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
        return min_index; // Return the index of the vertex with minimum distance value
    }
// Function that implements Dijkstra's single source shortest path algorithm
    double[] dijkstra(double graph[][], int src) {
        double dist[] = new double[V]; // The output array. dist[i] will hold the shortest distance from src to i
        Boolean sptSet[] = new Boolean[V]; // sptSet[i] will be true if vertex i is included in shortest path tree

// Initialize all distances as INFINITE and sptSet[] as false
        for (int i = 0; i < V; i++) {
            dist[i] = Double.MAX_VALUE;
            sptSet[i] = false;
        }
// Distance of source vertex from itself is always 0
        dist[src] = 0;
// Find shortest path for all vertices
        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, sptSet);// Pick the minimum distance vertex from the set of vertices not yet processed
            sptSet[u] = true; // Mark the picked vertex as processed
// Update dist value of the adjacent vertices of the picked vertex
            for (int v = 0; v < V; v++)
                if (!sptSet[v] && graph[u][v] != 0 && dist[u] != Double.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) // Update dist[v] only if is not in sptSet, there is an edge from u to v, and total weight of path from src to v through u is smaller than current value of dist[v]
                    dist[v] = dist[u] + graph[u][v];
        }

        return dist; // Return the array of distances
    }
}

public class AlgorithmsProject {
    static final int V = 50; // Number of vertices
    static final double R = 6371; // Radius of Earth in kilometers(used in haversine)

    public static void main(String[] args) {
        // Names of locations
        String[] names = {"جامعة الإمام","جامعة الملك سعود","جامعة الأميرة نوره","سفارة الهند","نادي الهلال السعودي","قصر المصمك","وزارة الدفاع","النخيل مول","سلام مول","منتزه السويدي","المتحف الوطني",
        "نادي الفروسية","انتركونتيننتال الرياض","استاد الملك فهد الدولي",
        "مركز الملك عبدالله المالي","البنك السعودي المركزي","مركز الرياض الدولي للمؤتمرات والمعارض","المدينة الرقمية",
        "ريتزكارتون","مطار الملك خالد الدولي","المكان مول","مكتبة الملك فهد الوطنية","ذا زون","فيا الرياض","جامعة اليمامة","نادي النصر السعودي","عرقه بلازا","ايكيا","جامع الأميرة هيا","مستشفى الحمادي",
        "مستشفى الملك فيصل التخصصي","مستشفى الإمام عبدالرحمن الفيصل","مستشفى الجافل","مدينة الملك سعود الطبية","المستشفى الجامعي لجامعة الإمام","مستشفى الملك عبدالله الجامعي","مستشفى واحة الصحة",
        "وزارة الداخلية","شرفات","البوليفارد","مستشفى دلة-النخيل","بيت1","كلية الملك فهد الامنية","مستشفى الدرعية","شركة ارامكو السعودية","محطة قطار سار","نادي ديراب للقولف","مستشفى اليرموك","درة الرياض","بيت2"
             
        };

        // 2D array for the Latitude and longitude of locations
        double[][] coordinates = {{24.81418582857288,46.69112540000001},{24.72506033079908,46.63839044782596},{24.847352437787496,46.72556200759584},{24.686267595131667,46.630620658506345},{24.60574278219949,46.62458312883537},
        {24.631224452609978,46.71336967132887},{24.66898508166789,46.71712828794501},{24.76801119336468,46.71491454232924},{24.55893834072133,46.637905728835364},{24.59734962620979,46.69498226142968},{24.647856949784508,46.71066620212786},
        {24.982817012473618,46.778549292293526},{24.666745248867535,46.692169728835374},{24.78889538189176,46.838992942329234},{24.765779874755115,46.64350029420437},{24.664124250301246,46.68704945767076},{24.75316823074471,46.72679375767075},
        {24.740422232212705,46.6354193},{24.66583214902209,46.63087752883537},{24.96518179104352,46.70265643185606},{24.79107318069485,46.61217017116463},{24.685691690140036,46.68651144232925},{24.73191242369224,46.64934230000001},
        {24.66715274135443,46.62640973939482},{24.86356060960647,46.593783371164626},{24.581519525946117,46.558985713493875},{24.693773943170104 ,46.607355713493874},{24.659387390277224,46.78735215626386},{24.555955050865986 ,46.714055942329246},
        {24.75697057743351,46.709901305496146},{24.671407848808276,46.676662594537405},{24.528034203244516,46.653067971164624},{24.57856857014801,46.604705342329254},{24.62956402602567,46.69196464771003},{24.812604880531275,46.70410718591167},{24.83616475589043,46.722149842329245},
        {24.752687036674036,46.72787230549625},{24.67054529724508,46.69575957116463},{24.803906373639716,46.673916228835374},{24.76954567543508,46.60453252340763},{24.747074769979893,46.651678334332246},{24.880969693737544 ,46.82859936905502},{24.78364370683669 ,46.85392407116463},
        {24.788081182339223 ,46.55862492883537},{24.52484900448246 ,46.87251652883538},{24.855530435029795 ,46.763057228835365},{24.434073807641575 ,46.49073097116462},{24.672022995408884 ,46.86775048650612},{25.006762384762684 ,46.49951845117305},{24.570534637682943 ,46.71346311449465}
        };

        // Specifying the hospital locations
        int[] hospitalLocations = {29,30,31,32,33,34,35,36,40,43,47}; 

        // Take ambulance's current location name as user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ambulance's current location (name): ");
        String ambulanceLocationName = scanner.nextLine(); //entering the ambulance current location.

        // Find the index of the ambulance's current location in the name array
        int ambulanceLocation = -1;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(ambulanceLocationName)) {
                ambulanceLocation = i;
                break;
            }
        }

        if (ambulanceLocation == -1) {
            System.out.println("Location not found.");
            return;
        }

        // Initialize distance graph
        double[][] distanceGraph = new double[V][V];

        // Calculate distances using the Haversine formula
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                double lat1 = Math.toRadians(coordinates[i][0]);
                double lon1 = Math.toRadians(coordinates[i][1]);
                double lat2 = Math.toRadians(coordinates[j][0]);
                double lon2 = Math.toRadians(coordinates[j][1]);
                distanceGraph[i][j] = haversine(lat1, lon1, lat2, lon2);
            }
        }

        // Execute Dijkstra's algorithm to find shortest paths from ambulance to hospitals
        Dijkstra dijkstra = new Dijkstra();
        double[] distance = dijkstra.dijkstra(distanceGraph, ambulanceLocation); //Array hold the shortest path.

        // Find the nearest hospital
        int nearestHospital = -1;
        double minDistance = Double.MAX_VALUE; // distance of the nearest hospital. 
        for (int hospitalLocation : hospitalLocations) {
            if (distance[hospitalLocation] < minDistance) {
                minDistance = distance[hospitalLocation];
                nearestHospital = hospitalLocation;
            }
        }

        // Calculate estimated travel time
        double speedWithoutTraffic = 120.0; // assuming that the ambulance speed without traffic is 120 km/h 
        double speedWithTraffic = 30.0; // assuming that the ambulance speed with traffic is 30 km/h
        double timeWithoutTraffic = (minDistance / speedWithoutTraffic)*60 ; // Convert hour to minutes
        double timeWithTraffic = (minDistance / speedWithTraffic)*60 ; // Convert hour to minutes

        System.out.println("Nearest hospital: " + names[nearestHospital]);
        System.out.println("Distance: " + minDistance + " km");
        
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("Do you want to consider traffic in the time? (yes/no) :");
        String ConsiderTraffic = scanner.nextLine();
        if(ConsiderTraffic.equalsIgnoreCase("yes")){
            System.out.println("Estimated time with traffic: "+ timeWithTraffic+" minutes");
        }
        if(ConsiderTraffic.equalsIgnoreCase("no")){
            System.out.println("Estimated time without traffic: "+ timeWithoutTraffic+ " minutes");
        }
    }

    // Haversine formula to calculate distance between two points on a sphere
    static double haversine(double lat1, double lon1, double lat2, double lon2) { // lat= latitude, lon= longitude. 
        double dLat = lat2 - lat1; // dLat= difference in latitude. 
        double dLon = lon2 - lon1; // dLon= difference in longitude. 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Return the calculated distance
    }
}