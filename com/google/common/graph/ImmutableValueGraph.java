/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.errorprone.annotations.Immutable;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable(containerOf = {"N", "V"})
/*    */ @Beta
/*    */ public final class ImmutableValueGraph<N, V>
/*    */   extends ConfigurableValueGraph<N, V>
/*    */ {
/*    */   private ImmutableValueGraph(ValueGraph<N, V> graph) {
/* 47 */     super(ValueGraphBuilder.from(graph), (Map<N, GraphConnections<N, V>>)getNodeConnections(graph), graph.edges().size());
/*    */   }
/*    */ 
/*    */   
/*    */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/* 52 */     return (graph instanceof ImmutableValueGraph) ? (ImmutableValueGraph<N, V>)graph : new ImmutableValueGraph<>(graph);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
/* 64 */     return (ImmutableValueGraph<N, V>)Preconditions.checkNotNull(graph);
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableGraph<N> asGraph() {
/* 69 */     return new ImmutableGraph<>(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
/* 77 */     ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
/* 78 */     for (N node : graph.nodes()) {
/* 79 */       nodeConnections.put(node, connectionsOf(graph, node));
/*    */     }
/* 81 */     return nodeConnections.build();
/*    */   }
/*    */ 
/*    */   
/*    */   private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
/* 86 */     Function<N, V> successorNodeToValueFn = new Function<N, V>()
/*    */       {
/*    */         public V apply(N successorNode)
/*    */         {
/* 90 */           return graph.edgeValueOrDefault(node, successorNode, null);
/*    */         }
/*    */       };
/* 93 */     return graph.isDirected() ? 
/* 94 */       DirectedGraphConnections.<N, V>ofImmutable(graph
/* 95 */         .predecessors(node), Maps.asMap(graph.successors(node), successorNodeToValueFn)) : 
/* 96 */       UndirectedGraphConnections.<N, V>ofImmutable(
/* 97 */         Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ImmutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */