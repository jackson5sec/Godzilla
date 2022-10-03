/*    */ package com.google.common.graph;
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
/*    */ final class ConfigurableMutableGraph<N>
/*    */   extends ForwardingGraph<N>
/*    */   implements MutableGraph<N>
/*    */ {
/*    */   private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;
/*    */   
/*    */   ConfigurableMutableGraph(AbstractGraphBuilder<? super N> builder) {
/* 36 */     this.backingValueGraph = new ConfigurableMutableValueGraph<>(builder);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BaseGraph<N> delegate() {
/* 41 */     return this.backingValueGraph;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addNode(N node) {
/* 46 */     return this.backingValueGraph.addNode(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean putEdge(N nodeU, N nodeV) {
/* 51 */     return (this.backingValueGraph.putEdgeValue(nodeU, nodeV, GraphConstants.Presence.EDGE_EXISTS) == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean putEdge(EndpointPair<N> endpoints) {
/* 56 */     validateEndpoints(endpoints);
/* 57 */     return putEdge(endpoints.nodeU(), endpoints.nodeV());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeNode(N node) {
/* 62 */     return this.backingValueGraph.removeNode(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeEdge(N nodeU, N nodeV) {
/* 67 */     return (this.backingValueGraph.removeEdge(nodeU, nodeV) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeEdge(EndpointPair<N> endpoints) {
/* 72 */     validateEndpoints(endpoints);
/* 73 */     return removeEdge(endpoints.nodeU(), endpoints.nodeV());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ConfigurableMutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */