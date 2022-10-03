/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
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
/*    */ abstract class AbstractUndirectedNetworkConnections<N, E>
/*    */   implements NetworkConnections<N, E>
/*    */ {
/*    */   protected final Map<E, N> incidentEdgeMap;
/*    */   
/*    */   protected AbstractUndirectedNetworkConnections(Map<E, N> incidentEdgeMap) {
/* 38 */     this.incidentEdgeMap = (Map<E, N>)Preconditions.checkNotNull(incidentEdgeMap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> predecessors() {
/* 43 */     return adjacentNodes();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> successors() {
/* 48 */     return adjacentNodes();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<E> incidentEdges() {
/* 53 */     return Collections.unmodifiableSet(this.incidentEdgeMap.keySet());
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<E> inEdges() {
/* 58 */     return incidentEdges();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<E> outEdges() {
/* 63 */     return incidentEdges();
/*    */   }
/*    */ 
/*    */   
/*    */   public N adjacentNode(E edge) {
/* 68 */     return (N)Preconditions.checkNotNull(this.incidentEdgeMap.get(edge));
/*    */   }
/*    */ 
/*    */   
/*    */   public N removeInEdge(E edge, boolean isSelfLoop) {
/* 73 */     if (!isSelfLoop) {
/* 74 */       return removeOutEdge(edge);
/*    */     }
/* 76 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public N removeOutEdge(E edge) {
/* 81 */     N previousNode = this.incidentEdgeMap.remove(edge);
/* 82 */     return (N)Preconditions.checkNotNull(previousNode);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 87 */     if (!isSelfLoop) {
/* 88 */       addOutEdge(edge, node);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void addOutEdge(E edge, N node) {
/* 94 */     N previousNode = this.incidentEdgeMap.put(edge, node);
/* 95 */     Preconditions.checkState((previousNode == null));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\AbstractUndirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */