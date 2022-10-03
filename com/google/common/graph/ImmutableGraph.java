/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Functions;
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
/*    */ 
/*    */ @Immutable(containerOf = {"N"})
/*    */ @Beta
/*    */ public class ImmutableGraph<N>
/*    */   extends ForwardingGraph<N>
/*    */ {
/*    */   private final BaseGraph<N> backingGraph;
/*    */   
/*    */   ImmutableGraph(BaseGraph<N> backingGraph) {
/* 51 */     this.backingGraph = backingGraph;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
/* 56 */     return (graph instanceof ImmutableGraph) ? (ImmutableGraph<N>)graph : new ImmutableGraph<>(new ConfigurableValueGraph<>(
/*    */ 
/*    */ 
/*    */           
/* 60 */           GraphBuilder.from(graph), (Map<N, GraphConnections<N, ?>>)getNodeConnections(graph), graph.edges().size()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph) {
/* 70 */     return (ImmutableGraph<N>)Preconditions.checkNotNull(graph);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> graph) {
/* 78 */     ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
/* 79 */     for (N node : graph.nodes()) {
/* 80 */       nodeConnections.put(node, connectionsOf(graph, node));
/*    */     }
/* 82 */     return nodeConnections.build();
/*    */   }
/*    */   
/*    */   private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> graph, N node) {
/* 86 */     Function<Object, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
/* 87 */     return graph.isDirected() ? 
/* 88 */       DirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(graph
/* 89 */         .predecessors(node), Maps.asMap(graph.successors(node), edgeValueFn)) : 
/* 90 */       UndirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(
/* 91 */         Maps.asMap(graph.adjacentNodes(node), edgeValueFn));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BaseGraph<N> delegate() {
/* 96 */     return this.backingGraph;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ImmutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */