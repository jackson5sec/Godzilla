/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.Immutable;
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
/*     */ @Immutable(containerOf = {"N", "E"})
/*     */ @Beta
/*     */ public final class ImmutableNetwork<N, E>
/*     */   extends ConfigurableNetwork<N, E>
/*     */ {
/*     */   private ImmutableNetwork(Network<N, E> network) {
/*  50 */     super(
/*  51 */         NetworkBuilder.from(network), getNodeConnections(network), getEdgeToReferenceNode(network));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> network) {
/*  56 */     return (network instanceof ImmutableNetwork) ? (ImmutableNetwork<N, E>)network : new ImmutableNetwork<>(network);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network) {
/*  68 */     return (ImmutableNetwork<N, E>)Preconditions.checkNotNull(network);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableGraph<N> asGraph() {
/*  73 */     return new ImmutableGraph<>(super.asGraph());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> network) {
/*  80 */     ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
/*  81 */     for (N node : network.nodes()) {
/*  82 */       nodeConnections.put(node, connectionsOf(network, node));
/*     */     }
/*  84 */     return (Map<N, NetworkConnections<N, E>>)nodeConnections.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> network) {
/*  91 */     ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
/*  92 */     for (E edge : network.edges()) {
/*  93 */       edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
/*     */     }
/*  95 */     return (Map<E, N>)edgeToReferenceNode.build();
/*     */   }
/*     */   
/*     */   private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> network, N node) {
/*  99 */     if (network.isDirected()) {
/* 100 */       Map<E, N> inEdgeMap = Maps.asMap(network.inEdges(node), sourceNodeFn(network));
/* 101 */       Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), targetNodeFn(network));
/* 102 */       int selfLoopCount = network.edgesConnecting(node, node).size();
/* 103 */       return network.allowsParallelEdges() ? 
/* 104 */         DirectedMultiNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount) : 
/* 105 */         DirectedNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
/*     */     } 
/*     */     
/* 108 */     Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
/* 109 */     return network.allowsParallelEdges() ? 
/* 110 */       UndirectedMultiNetworkConnections.<N, E>ofImmutable(incidentEdgeMap) : 
/* 111 */       UndirectedNetworkConnections.<N, E>ofImmutable(incidentEdgeMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <N, E> Function<E, N> sourceNodeFn(final Network<N, E> network) {
/* 116 */     return new Function<E, N>()
/*     */       {
/*     */         public N apply(E edge) {
/* 119 */           return network.incidentNodes(edge).source();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> targetNodeFn(final Network<N, E> network) {
/* 125 */     return new Function<E, N>()
/*     */       {
/*     */         public N apply(E edge) {
/* 128 */           return network.incidentNodes(edge).target();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> adjacentNodeFn(final Network<N, E> network, final N node) {
/* 134 */     return new Function<E, N>()
/*     */       {
/*     */         public N apply(E edge) {
/* 137 */           return network.incidentNodes(edge).adjacentNode(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ImmutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */