/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ final class ConfigurableMutableValueGraph<N, V>
/*     */   extends ConfigurableValueGraph<N, V>
/*     */   implements MutableValueGraph<N, V>
/*     */ {
/*     */   ConfigurableMutableValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  46 */     super(builder);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addNode(N node) {
/*  52 */     Preconditions.checkNotNull(node, "node");
/*     */     
/*  54 */     if (containsNode(node)) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     addNodeInternal(node);
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private GraphConnections<N, V> addNodeInternal(N node) {
/*  69 */     GraphConnections<N, V> connections = newConnections();
/*  70 */     Preconditions.checkState((this.nodeConnections.put(node, connections) == null));
/*  71 */     return connections;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V putEdgeValue(N nodeU, N nodeV, V value) {
/*  77 */     Preconditions.checkNotNull(nodeU, "nodeU");
/*  78 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*  79 */     Preconditions.checkNotNull(value, "value");
/*     */     
/*  81 */     if (!allowsSelfLoops()) {
/*  82 */       Preconditions.checkArgument(!nodeU.equals(nodeV), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
/*     */     }
/*     */     
/*  85 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/*  86 */     if (connectionsU == null) {
/*  87 */       connectionsU = addNodeInternal(nodeU);
/*     */     }
/*  89 */     V previousValue = connectionsU.addSuccessor(nodeV, value);
/*  90 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/*  91 */     if (connectionsV == null) {
/*  92 */       connectionsV = addNodeInternal(nodeV);
/*     */     }
/*  94 */     connectionsV.addPredecessor(nodeU, value);
/*  95 */     if (previousValue == null) {
/*  96 */       Graphs.checkPositive(++this.edgeCount);
/*     */     }
/*  98 */     return previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V putEdgeValue(EndpointPair<N> endpoints, V value) {
/* 104 */     validateEndpoints(endpoints);
/* 105 */     return putEdgeValue(endpoints.nodeU(), endpoints.nodeV(), value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeNode(N node) {
/* 111 */     Preconditions.checkNotNull(node, "node");
/*     */     
/* 113 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 114 */     if (connections == null) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     if (allowsSelfLoops())
/*     */     {
/* 120 */       if (connections.removeSuccessor(node) != null) {
/* 121 */         connections.removePredecessor(node);
/* 122 */         this.edgeCount--;
/*     */       } 
/*     */     }
/*     */     
/* 126 */     for (N successor : connections.successors()) {
/* 127 */       ((GraphConnections)this.nodeConnections.getWithoutCaching(successor)).removePredecessor(node);
/* 128 */       this.edgeCount--;
/*     */     } 
/* 130 */     if (isDirected()) {
/* 131 */       for (N predecessor : connections.predecessors()) {
/* 132 */         Preconditions.checkState((((GraphConnections)this.nodeConnections.getWithoutCaching(predecessor)).removeSuccessor(node) != null));
/* 133 */         this.edgeCount--;
/*     */       } 
/*     */     }
/* 136 */     this.nodeConnections.remove(node);
/* 137 */     Graphs.checkNonNegative(this.edgeCount);
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V removeEdge(N nodeU, N nodeV) {
/* 144 */     Preconditions.checkNotNull(nodeU, "nodeU");
/* 145 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*     */     
/* 147 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 148 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/* 149 */     if (connectionsU == null || connectionsV == null) {
/* 150 */       return null;
/*     */     }
/*     */     
/* 153 */     V previousValue = connectionsU.removeSuccessor(nodeV);
/* 154 */     if (previousValue != null) {
/* 155 */       connectionsV.removePredecessor(nodeU);
/* 156 */       Graphs.checkNonNegative(--this.edgeCount);
/*     */     } 
/* 158 */     return previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V removeEdge(EndpointPair<N> endpoints) {
/* 164 */     validateEndpoints(endpoints);
/* 165 */     return removeEdge(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */   
/*     */   private GraphConnections<N, V> newConnections() {
/* 169 */     return isDirected() ? 
/* 170 */       DirectedGraphConnections.<N, V>of() : 
/* 171 */       UndirectedGraphConnections.<N, V>of();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ConfigurableMutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */