/*    */ package com.google.common.graph;
/*    */ 
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
/*    */ abstract class ForwardingGraph<N>
/*    */   extends AbstractGraph<N>
/*    */ {
/*    */   public Set<N> nodes() {
/* 33 */     return delegate().nodes();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected long edgeCount() {
/* 42 */     return delegate().edges().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDirected() {
/* 47 */     return delegate().isDirected();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean allowsSelfLoops() {
/* 52 */     return delegate().allowsSelfLoops();
/*    */   }
/*    */ 
/*    */   
/*    */   public ElementOrder<N> nodeOrder() {
/* 57 */     return delegate().nodeOrder();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> adjacentNodes(N node) {
/* 62 */     return delegate().adjacentNodes(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> predecessors(N node) {
/* 67 */     return delegate().predecessors(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> successors(N node) {
/* 72 */     return delegate().successors(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public int degree(N node) {
/* 77 */     return delegate().degree(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public int inDegree(N node) {
/* 82 */     return delegate().inDegree(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public int outDegree(N node) {
/* 87 */     return delegate().outDegree(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 92 */     return delegate().hasEdgeConnecting(nodeU, nodeV);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 97 */     return delegate().hasEdgeConnecting(endpoints);
/*    */   }
/*    */   
/*    */   protected abstract BaseGraph<N> delegate();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ForwardingGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */