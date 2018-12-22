package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		//checks to see if any of the parameteres is null
		if (g == null) {
			return null;
		}
		if (p1 == null) {
			return null;
		}
		if (p2 == null) {
			return null;
		}
		
		//This will hold the answer of the shortest chain from p1 to p2
		ArrayList<String> shortestPath = new ArrayList<String>();
		
		//This will create the array to see which vertices we have visited
		boolean[] visited = new boolean[g.members.length];
		
		//This will create a queue that is for use of BFS
		Queue<Person> queue = new Queue<Person>();
		
		//This will help us traverse back
		Person[] visitedAlready = new Person[g.members.length];
		
		//comment dont work? //This will set the location of p1 in visited array to true since we visited it
		int index = g.map.get(p1);
		
		//this will add p1 to the queue
		queue.enqueue(g.members[index]);
		
		//This will add p1 to the ArrayList shortestPath 
		//WE DID NOT NEED THIS
		//shortestPath.add(g.members[index].name);
		
		//set the current index in visited to true because we start off by visiting p1
		visited[index] = true;
		
		//loop through the queue, this is BFS
		while (queue.isEmpty() == false) {
			
			//we have to check each neighbor of pivot
			Person pivot = queue.dequeue();
			
			//set that we visited it. with pivotIndex "we are gonna get this guys first variable"
			int pivotIndex = g.map.get(pivot.name);
			visited[pivotIndex] = true;
			
			//This is the neighbor of pivot
			Friend neighbor = pivot.first;
			
			if (neighbor == null) {
				return null;
			}
			
			while (neighbor != null) {
				
				if (visited[neighbor.fnum] == false) {
					visited[neighbor.fnum] = true;
					visitedAlready[neighbor.fnum] = pivot; 
					queue.enqueue(g.members[neighbor.fnum]);
					
					//check if p2 matches the neighbor
					if (g.members[neighbor.fnum].name.equals(p2)) {
						pivot = g.members[neighbor.fnum];
						
						while (pivot.name.equals(p1) == false) {
							shortestPath.add(0, pivot.name);
							pivot = visitedAlready[g.map.get(pivot.name)];
						}
						shortestPath.add(0, p1);
						return shortestPath;
					}
				}
				neighbor = neighbor.next;
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		//check for bad input
		if (g == null) {
			return null;
		}
		if (school == null) {
			return null;
		}
		
		//This will keep track of the listOfCliques
		ArrayList<ArrayList<String>> listOfCliques= new ArrayList<ArrayList<String>>();
		
		//This will create the array to see which vertices we have visited
		boolean[] visited = new boolean[g.members.length];
		
		//This will initiate the helper method
		return BFS(g, g.members[0], listOfCliques, visited, school);
		
	}
	
	//This is the helper method
	private static ArrayList<ArrayList<String>> BFS(Graph g, Person start, ArrayList<ArrayList<String>> listOfCliques, boolean[] visited, String school){
		
		//This will hold the results of the cliques
		ArrayList<String> cliquesResults = new ArrayList<String>();
		
		//This will create a queue that is for use of BFS
		Queue<Person> queue = new Queue<Person>();
		
		//This will add start to the queue
		queue.enqueue(start);
		
		//set the current index in visited to true because we start off by visiting from the start
		visited[g.map.get(start.name)] = true;
		
		Person pivot = new Person();
		Friend neighbor;
		
		if (start.school == null || start.school.equals(school) == false) {
			
			queue.dequeue();
			
			for (int j = 0; j < visited.length; j++) {
				if (visited[j] == false) {
					return BFS(g, g.members[j], listOfCliques, visited, school);
				}
			}
		}
		
		while (queue.isEmpty() == false) {
			
			//We have to check each neighbor of pivot
			pivot = queue.dequeue();
			
			//This is the neighbor of pivot
			neighbor = pivot.first;
			cliquesResults.add(pivot.name);
			
			//checking all the parents neighbor
			while (neighbor != null) {
				
				//checking only the neighbor that hasn't been checked already
				if (visited[neighbor.fnum] == false) {
					
					//if the neighbor goes to school he gets checked in future
					if (g.members[neighbor.fnum].school == null) {
						
					}
					else {
						if (g.members[neighbor.fnum].school.equals(school)) {
							queue.enqueue(g.members[neighbor.fnum]);
						}
					}
					visited[neighbor.fnum] = true;
				}
				neighbor = neighbor.next;
			}
		}
		
		//This will take out the empty list at the end
		if (listOfCliques.isEmpty() == false && cliquesResults.isEmpty()) {
			
		} 
		else {
			listOfCliques.add(cliquesResults);
		}
		
		for (int i = 0; i < visited.length; i++) {
			if (visited[i] == false) {
				return BFS(g, g.members[i], listOfCliques, visited, school);
			}
		}
		
		//This will return answer
		return listOfCliques;
	}

	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		//check for bad input
		if (g == null) {
			return null;
		}
		
		//This will keep the connectors all in the ArrayList
		ArrayList<String> connectors = new ArrayList<String>();
		
		//This will create the array to see which vertices we have visited
		boolean[] visited = new boolean[g.members.length];
		
		//This is used to get the predecessors
		ArrayList<String> predecessor = new ArrayList<String>();
		
		//This holds the numbers of the DFS
		int[] numbersOfDFS= new int[g.members.length];
		
		//This holds the numbers before
		int[] before = new int[g.members.length];
		
		
		for (int i = 0; i < g.members.length; i++){
			if (visited[i] == false) {
				//This will recursively call the recursive method
				connectors = DFS(connectors, g, g.members[i], visited, new int[] {0,0}, numbersOfDFS, before, predecessor, true);
			}
		}
		
		//This will return the answer
		return connectors;
	}
	
	private static ArrayList<String> DFS(ArrayList<String> connectors, Graph g, Person start, boolean[] visited, int[] count, int[] numbersOfDFS, int[] back, ArrayList<String> backward, boolean started){
		
		//This sets the index visited to true
		visited[g.map.get(start.name)] = true;
		
		//This will store the neighbor of the start vertex
		Friend neighbor = start.first;
		
		numbersOfDFS[g.map.get(start.name)] = count[0];
		back[g.map.get(start.name)] = count[1];
		
		//This will loop through the neighbor
		while (neighbor != null) {
			
			if (visited[neighbor.fnum] == false) {
				
				count[0]++;
				count[1]++;
				
				connectors = DFS(connectors, g, g.members[neighbor.fnum], visited, count, numbersOfDFS, back, backward, false);
				
				//This means that there can be an answer that is inserted here just have to ensure it is 
				if (numbersOfDFS[g.map.get(start.name)] <= back[neighbor.fnum]) {
					
					if ((connectors.contains(start.name) == false && backward.contains(start.name)) || (connectors.contains(start.name) == false && started == false)) {
						connectors.add(start.name);
					}
				}
				else {
					//This will store start.name number into an int
					int first = back[g.map.get(start.name)];
					
					//This will store neighbor.fnum number into an int
					int second = back[neighbor.fnum];
					
					if (first < second) {
						back[g.map.get(start.name)] = first;
					}
					else {
						back[g.map.get(start.name)] = second;
					} 
				}		
			backward.add(start.name);
			}
			else {
				//This will store start.name number into an int
				int third = back[g.map.get(start.name)];
				
				//This will store neighbor.fnum number into an int
				int fourth = numbersOfDFS[neighbor.fnum];
				
				if (third < fourth) {
					back[g.map.get(start.name)] = third;
				}
				else {
					back[g.map.get(start.name)] = fourth;
				}
			}
			neighbor = neighbor.next;
		}
		//This will return the answer
		return connectors;
	}
}

