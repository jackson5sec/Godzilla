package com.google.common.graph;

import java.util.Set;

interface BaseGraph<N> extends SuccessorsFunction<N>, PredecessorsFunction<N> {
  Set<N> nodes();
  
  Set<EndpointPair<N>> edges();
  
  boolean isDirected();
  
  boolean allowsSelfLoops();
  
  ElementOrder<N> nodeOrder();
  
  Set<N> adjacentNodes(N paramN);
  
  Set<N> predecessors(N paramN);
  
  Set<N> successors(N paramN);
  
  Set<EndpointPair<N>> incidentEdges(N paramN);
  
  int degree(N paramN);
  
  int inDegree(N paramN);
  
  int outDegree(N paramN);
  
  boolean hasEdgeConnecting(N paramN1, N paramN2);
  
  boolean hasEdgeConnecting(EndpointPair<N> paramEndpointPair);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\BaseGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */