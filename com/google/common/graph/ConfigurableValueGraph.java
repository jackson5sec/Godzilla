/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ class ConfigurableValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
/*     */   protected long edgeCount;
/*     */   
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  56 */     this(builder, builder.nodeOrder
/*     */         
/*  58 */         .createMap(((Integer)builder.expectedNodeCount
/*  59 */           .or(Integer.valueOf(10))).intValue()), 0L);
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
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
/*  71 */     this.isDirected = builder.directed;
/*  72 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  73 */     this.nodeOrder = builder.nodeOrder.cast();
/*     */     
/*  75 */     this.nodeConnections = (nodeConnections instanceof java.util.TreeMap) ? new MapRetrievalCache<>(nodeConnections) : new MapIteratorCache<>(nodeConnections);
/*     */ 
/*     */ 
/*     */     
/*  79 */     this.edgeCount = Graphs.checkNonNegative(edgeCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> nodes() {
/*  84 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  89 */     return this.isDirected;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  94 */     return this.allowsSelfLoops;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/*  99 */     return this.nodeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/* 104 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/* 109 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/* 114 */     return checkedConnections(node).successors();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 119 */     return hasEdgeConnecting_internal((N)Preconditions.checkNotNull(nodeU), (N)Preconditions.checkNotNull(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 124 */     Preconditions.checkNotNull(endpoints);
/* 125 */     return (isOrderingCompatible(endpoints) && 
/* 126 */       hasEdgeConnecting_internal(endpoints.nodeU(), endpoints.nodeV()));
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(N nodeU, N nodeV, V defaultValue) {
/* 131 */     return edgeValueOrDefault_internal((N)Preconditions.checkNotNull(nodeU), (N)Preconditions.checkNotNull(nodeV), defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(EndpointPair<N> endpoints, V defaultValue) {
/* 136 */     validateEndpoints(endpoints);
/* 137 */     return edgeValueOrDefault_internal(endpoints.nodeU(), endpoints.nodeV(), defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long edgeCount() {
/* 142 */     return this.edgeCount;
/*     */   }
/*     */   
/*     */   protected final GraphConnections<N, V> checkedConnections(N node) {
/* 146 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 147 */     if (connections == null) {
/* 148 */       Preconditions.checkNotNull(node);
/* 149 */       throw new IllegalArgumentException("Node " + node + " is not an element of this graph.");
/*     */     } 
/* 151 */     return connections;
/*     */   }
/*     */   
/*     */   protected final boolean containsNode(N node) {
/* 155 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */   
/*     */   protected final boolean hasEdgeConnecting_internal(N nodeU, N nodeV) {
/* 159 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 160 */     return (connectionsU != null && connectionsU.successors().contains(nodeV));
/*     */   }
/*     */   
/*     */   protected final V edgeValueOrDefault_internal(N nodeU, N nodeV, V defaultValue) {
/* 164 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 165 */     V value = (connectionsU == null) ? null : connectionsU.value(nodeV);
/* 166 */     return (value == null) ? defaultValue : value;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ConfigurableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */