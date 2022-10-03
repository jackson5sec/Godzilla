/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConfigurableNetwork<N, E>
/*     */   extends AbstractNetwork<N, E>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsParallelEdges;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   private final ElementOrder<E> edgeOrder;
/*     */   protected final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
/*     */   protected final MapIteratorCache<E, N> edgeToReferenceNode;
/*     */   
/*     */   ConfigurableNetwork(NetworkBuilder<? super N, ? super E> builder) {
/*  66 */     this(builder, builder.nodeOrder
/*     */         
/*  68 */         .createMap(((Integer)builder.expectedNodeCount
/*  69 */           .or(Integer.valueOf(10))).intValue()), builder.edgeOrder
/*  70 */         .createMap(((Integer)builder.expectedEdgeCount.or(Integer.valueOf(20))).intValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ConfigurableNetwork(NetworkBuilder<? super N, ? super E> builder, Map<N, NetworkConnections<N, E>> nodeConnections, Map<E, N> edgeToReferenceNode) {
/*  81 */     this.isDirected = builder.directed;
/*  82 */     this.allowsParallelEdges = builder.allowsParallelEdges;
/*  83 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  84 */     this.nodeOrder = builder.nodeOrder.cast();
/*  85 */     this.edgeOrder = builder.edgeOrder.cast();
/*     */ 
/*     */     
/*  88 */     this.nodeConnections = (nodeConnections instanceof java.util.TreeMap) ? new MapRetrievalCache<>(nodeConnections) : new MapIteratorCache<>(nodeConnections);
/*     */ 
/*     */ 
/*     */     
/*  92 */     this.edgeToReferenceNode = new MapIteratorCache<>(edgeToReferenceNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> nodes() {
/*  97 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edges() {
/* 102 */     return this.edgeToReferenceNode.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/* 107 */     return this.isDirected;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsParallelEdges() {
/* 112 */     return this.allowsParallelEdges;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/* 117 */     return this.allowsSelfLoops;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/* 122 */     return this.nodeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<E> edgeOrder() {
/* 127 */     return this.edgeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> incidentEdges(N node) {
/* 132 */     return checkedConnections(node).incidentEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointPair<N> incidentNodes(E edge) {
/* 137 */     N nodeU = checkedReferenceNode(edge);
/* 138 */     N nodeV = ((NetworkConnections<N, E>)this.nodeConnections.get(nodeU)).adjacentNode(edge);
/* 139 */     return EndpointPair.of(this, nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/* 144 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 149 */     NetworkConnections<N, E> connectionsU = checkedConnections(nodeU);
/* 150 */     if (!this.allowsSelfLoops && nodeU == nodeV) {
/* 151 */       return (Set<E>)ImmutableSet.of();
/*     */     }
/* 153 */     Preconditions.checkArgument(containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
/* 154 */     return connectionsU.edgesConnecting(nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> inEdges(N node) {
/* 159 */     return checkedConnections(node).inEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> outEdges(N node) {
/* 164 */     return checkedConnections(node).outEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/* 169 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/* 174 */     return checkedConnections(node).successors();
/*     */   }
/*     */   
/*     */   protected final NetworkConnections<N, E> checkedConnections(N node) {
/* 178 */     NetworkConnections<N, E> connections = this.nodeConnections.get(node);
/* 179 */     if (connections == null) {
/* 180 */       Preconditions.checkNotNull(node);
/* 181 */       throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
/*     */     } 
/* 183 */     return connections;
/*     */   }
/*     */   
/*     */   protected final N checkedReferenceNode(E edge) {
/* 187 */     N referenceNode = this.edgeToReferenceNode.get(edge);
/* 188 */     if (referenceNode == null) {
/* 189 */       Preconditions.checkNotNull(edge);
/* 190 */       throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", new Object[] { edge }));
/*     */     } 
/* 192 */     return referenceNode;
/*     */   }
/*     */   
/*     */   protected final boolean containsNode(N node) {
/* 196 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */   
/*     */   protected final boolean containsEdge(E edge) {
/* 200 */     return this.edgeToReferenceNode.containsKey(edge);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ConfigurableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */