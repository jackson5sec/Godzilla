/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Graphs
/*     */ {
/*     */   public static <N> boolean hasCycle(Graph<N> graph) {
/*  60 */     int numEdges = graph.edges().size();
/*  61 */     if (numEdges == 0) {
/*  62 */       return false;
/*     */     }
/*  64 */     if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
/*  65 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  69 */     Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
/*  70 */     for (N node : graph.nodes()) {
/*  71 */       if (subgraphHasCycle(graph, visitedNodes, node, null)) {
/*  72 */         return true;
/*     */       }
/*     */     } 
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasCycle(Network<?, ?> network) {
/*  88 */     if (!network.isDirected() && network
/*  89 */       .allowsParallelEdges() && network
/*  90 */       .edges().size() > network.asGraph().edges().size()) {
/*  91 */       return true;
/*     */     }
/*  93 */     return hasCycle(network.asGraph());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N> boolean subgraphHasCycle(Graph<N> graph, Map<Object, NodeVisitState> visitedNodes, N node, N previousNode) {
/* 103 */     NodeVisitState state = visitedNodes.get(node);
/* 104 */     if (state == NodeVisitState.COMPLETE) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (state == NodeVisitState.PENDING) {
/* 108 */       return true;
/*     */     }
/*     */     
/* 111 */     visitedNodes.put(node, NodeVisitState.PENDING);
/* 112 */     for (N nextNode : graph.successors(node)) {
/* 113 */       if (canTraverseWithoutReusingEdge(graph, nextNode, previousNode) && 
/* 114 */         subgraphHasCycle(graph, visitedNodes, nextNode, node)) {
/* 115 */         return true;
/*     */       }
/*     */     } 
/* 118 */     visitedNodes.put(node, NodeVisitState.COMPLETE);
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, Object previousNode) {
/* 130 */     if (graph.isDirected() || !Objects.equal(previousNode, nextNode)) {
/* 131 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> Graph<N> transitiveClosure(Graph<N> graph) {
/* 149 */     MutableGraph<N> transitiveClosure = GraphBuilder.<N>from(graph).allowsSelfLoops(true).build();
/*     */ 
/*     */ 
/*     */     
/* 153 */     if (graph.isDirected()) {
/*     */       
/* 155 */       for (N node : graph.nodes()) {
/* 156 */         for (N reachableNode : reachableNodes(graph, node)) {
/* 157 */           transitiveClosure.putEdge(node, reachableNode);
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 163 */       Set<N> visitedNodes = new HashSet<>();
/* 164 */       for (N node : graph.nodes()) {
/* 165 */         if (!visitedNodes.contains(node)) {
/* 166 */           Set<N> reachableNodes = reachableNodes(graph, node);
/* 167 */           visitedNodes.addAll(reachableNodes);
/* 168 */           int pairwiseMatch = 1;
/* 169 */           for (N nodeU : reachableNodes) {
/* 170 */             for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++)) {
/* 171 */               transitiveClosure.putEdge(nodeU, nodeV);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     return transitiveClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> Set<N> reachableNodes(Graph<N> graph, N node) {
/* 193 */     Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 194 */     Set<N> visitedNodes = new LinkedHashSet<>();
/* 195 */     Queue<N> queuedNodes = new ArrayDeque<>();
/* 196 */     visitedNodes.add(node);
/* 197 */     queuedNodes.add(node);
/*     */     
/* 199 */     while (!queuedNodes.isEmpty()) {
/* 200 */       N currentNode = queuedNodes.remove();
/* 201 */       for (N successor : graph.successors(currentNode)) {
/* 202 */         if (visitedNodes.add(successor)) {
/* 203 */           queuedNodes.add(successor);
/*     */         }
/*     */       } 
/*     */     } 
/* 207 */     return Collections.unmodifiableSet(visitedNodes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> Graph<N> transpose(Graph<N> graph) {
/* 219 */     if (!graph.isDirected()) {
/* 220 */       return graph;
/*     */     }
/*     */     
/* 223 */     if (graph instanceof TransposedGraph) {
/* 224 */       return ((TransposedGraph)graph).graph;
/*     */     }
/*     */     
/* 227 */     return new TransposedGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph) {
/* 235 */     if (!graph.isDirected()) {
/* 236 */       return graph;
/*     */     }
/*     */     
/* 239 */     if (graph instanceof TransposedValueGraph) {
/* 240 */       return ((TransposedValueGraph)graph).graph;
/*     */     }
/*     */     
/* 243 */     return new TransposedValueGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> Network<N, E> transpose(Network<N, E> network) {
/* 251 */     if (!network.isDirected()) {
/* 252 */       return network;
/*     */     }
/*     */     
/* 255 */     if (network instanceof TransposedNetwork) {
/* 256 */       return ((TransposedNetwork)network).network;
/*     */     }
/*     */     
/* 259 */     return new TransposedNetwork<>(network);
/*     */   }
/*     */   
/*     */   static <N> EndpointPair<N> transpose(EndpointPair<N> endpoints) {
/* 263 */     if (endpoints.isOrdered()) {
/* 264 */       return EndpointPair.ordered(endpoints.target(), endpoints.source());
/*     */     }
/* 266 */     return endpoints;
/*     */   }
/*     */   
/*     */   private static class TransposedGraph<N>
/*     */     extends ForwardingGraph<N>
/*     */   {
/*     */     private final Graph<N> graph;
/*     */     
/*     */     TransposedGraph(Graph<N> graph) {
/* 275 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Graph<N> delegate() {
/* 280 */       return this.graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 285 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 290 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 295 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 300 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 305 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 310 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedValueGraph<N, V>
/*     */     extends ForwardingValueGraph<N, V>
/*     */   {
/*     */     private final ValueGraph<N, V> graph;
/*     */     
/*     */     TransposedValueGraph(ValueGraph<N, V> graph) {
/* 320 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ValueGraph<N, V> delegate() {
/* 325 */       return this.graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 330 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 335 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 340 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 345 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 350 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 355 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 360 */       return delegate().edgeValue(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 365 */       return delegate().edgeValue(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public V edgeValueOrDefault(N nodeU, N nodeV, V defaultValue) {
/* 370 */       return delegate().edgeValueOrDefault(nodeV, nodeU, defaultValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public V edgeValueOrDefault(EndpointPair<N> endpoints, V defaultValue) {
/* 375 */       return delegate().edgeValueOrDefault(Graphs.transpose(endpoints), defaultValue);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedNetwork<N, E> extends ForwardingNetwork<N, E> {
/*     */     private final Network<N, E> network;
/*     */     
/*     */     TransposedNetwork(Network<N, E> network) {
/* 383 */       this.network = network;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Network<N, E> delegate() {
/* 388 */       return this.network;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 393 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 398 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 403 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 408 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> inEdges(N node) {
/* 413 */       return delegate().outEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> outEdges(N node) {
/* 418 */       return delegate().inEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public EndpointPair<N> incidentNodes(E edge) {
/* 423 */       EndpointPair<N> endpointPair = delegate().incidentNodes(edge);
/* 424 */       return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 429 */       return delegate().edgesConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 434 */       return delegate().edgesConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 439 */       return delegate().edgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 444 */       return delegate().edgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 449 */       return delegate().edgeConnectingOrNull(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 454 */       return delegate().edgeConnectingOrNull(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 459 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 464 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes) {
/* 481 */     MutableGraph<N> subgraph = (nodes instanceof Collection) ? GraphBuilder.<N>from(graph).expectedNodeCount(((Collection)nodes).size()).build() : GraphBuilder.<N>from(graph).build();
/* 482 */     for (N node : nodes) {
/* 483 */       subgraph.addNode(node);
/*     */     }
/* 485 */     for (N node : subgraph.nodes()) {
/* 486 */       for (N successorNode : graph.successors(node)) {
/* 487 */         if (subgraph.nodes().contains(successorNode)) {
/* 488 */           subgraph.putEdge(node, successorNode);
/*     */         }
/*     */       } 
/*     */     } 
/* 492 */     return subgraph;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes) {
/* 508 */     MutableValueGraph<N, V> subgraph = (nodes instanceof Collection) ? ValueGraphBuilder.<N, V>from(graph).expectedNodeCount(((Collection)nodes).size()).build() : ValueGraphBuilder.<N, V>from(graph).build();
/* 509 */     for (N node : nodes) {
/* 510 */       subgraph.addNode(node);
/*     */     }
/* 512 */     for (N node : subgraph.nodes()) {
/* 513 */       for (N successorNode : graph.successors(node)) {
/* 514 */         if (subgraph.nodes().contains(successorNode)) {
/* 515 */           subgraph.putEdgeValue(node, successorNode, graph
/* 516 */               .edgeValueOrDefault(node, successorNode, null));
/*     */         }
/*     */       } 
/*     */     } 
/* 520 */     return subgraph;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes) {
/* 536 */     MutableNetwork<N, E> subgraph = (nodes instanceof Collection) ? NetworkBuilder.<N, E>from(network).expectedNodeCount(((Collection)nodes).size()).build() : NetworkBuilder.<N, E>from(network).build();
/* 537 */     for (N node : nodes) {
/* 538 */       subgraph.addNode(node);
/*     */     }
/* 540 */     for (N node : subgraph.nodes()) {
/* 541 */       for (E edge : network.outEdges(node)) {
/* 542 */         N successorNode = network.incidentNodes(edge).adjacentNode(node);
/* 543 */         if (subgraph.nodes().contains(successorNode)) {
/* 544 */           subgraph.addEdge(node, successorNode, edge);
/*     */         }
/*     */       } 
/*     */     } 
/* 548 */     return subgraph;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
/* 553 */     MutableGraph<N> copy = GraphBuilder.<N>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 554 */     for (N node : graph.nodes()) {
/* 555 */       copy.addNode(node);
/*     */     }
/* 557 */     for (EndpointPair<N> edge : graph.edges()) {
/* 558 */       copy.putEdge(edge.nodeU(), edge.nodeV());
/*     */     }
/* 560 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/* 566 */     MutableValueGraph<N, V> copy = ValueGraphBuilder.<N, V>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 567 */     for (N node : graph.nodes()) {
/* 568 */       copy.addNode(node);
/*     */     }
/* 570 */     for (EndpointPair<N> edge : graph.edges()) {
/* 571 */       copy.putEdgeValue(edge
/* 572 */           .nodeU(), edge.nodeV(), graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null));
/*     */     }
/* 574 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
/* 583 */     MutableNetwork<N, E> copy = NetworkBuilder.<N, E>from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
/* 584 */     for (N node : network.nodes()) {
/* 585 */       copy.addNode(node);
/*     */     }
/* 587 */     for (E edge : network.edges()) {
/* 588 */       EndpointPair<N> endpointPair = network.incidentNodes(edge);
/* 589 */       copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
/*     */     } 
/* 591 */     return copy;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNonNegative(int value) {
/* 596 */     Preconditions.checkArgument((value >= 0), "Not true that %s is non-negative.", value);
/* 597 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkNonNegative(long value) {
/* 602 */     Preconditions.checkArgument((value >= 0L), "Not true that %s is non-negative.", value);
/* 603 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkPositive(int value) {
/* 608 */     Preconditions.checkArgument((value > 0), "Not true that %s is positive.", value);
/* 609 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkPositive(long value) {
/* 614 */     Preconditions.checkArgument((value > 0L), "Not true that %s is positive.", value);
/* 615 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum NodeVisitState
/*     */   {
/* 624 */     PENDING,
/* 625 */     COMPLETE;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\Graphs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */