public enum Heuristic {

	// ones from class
	NUM_OUT_OF_PLACE, //A* Number of tiles out of place
	DIST_FROM_GOAL_MANHATTAN,  //A* Total distances of tiles from the goal
	CUSTOM_LINEAR_CONFLICT, //  for "A* with a heuristic of your design/research"
	NONE // DFS
}
