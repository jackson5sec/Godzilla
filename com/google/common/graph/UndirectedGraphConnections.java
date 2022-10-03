/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
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
/*    */ final class UndirectedGraphConnections<N, V>
/*    */   implements GraphConnections<N, V>
/*    */ {
/*    */   private final Map<N, V> adjacentNodeValues;
/*    */   
/*    */   private UndirectedGraphConnections(Map<N, V> adjacentNodeValues) {
/* 40 */     this.adjacentNodeValues = (Map<N, V>)Preconditions.checkNotNull(adjacentNodeValues);
/*    */   }
/*    */   
/*    */   static <N, V> UndirectedGraphConnections<N, V> of() {
/* 44 */     return new UndirectedGraphConnections<>(new HashMap<>(2, 1.0F));
/*    */   }
/*    */   
/*    */   static <N, V> UndirectedGraphConnections<N, V> ofImmutable(Map<N, V> adjacentNodeValues) {
/* 48 */     return new UndirectedGraphConnections<>((Map<N, V>)ImmutableMap.copyOf(adjacentNodeValues));
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> adjacentNodes() {
/* 53 */     return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> predecessors() {
/* 58 */     return adjacentNodes();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> successors() {
/* 63 */     return adjacentNodes();
/*    */   }
/*    */ 
/*    */   
/*    */   public V value(N node) {
/* 68 */     return this.adjacentNodeValues.get(node);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removePredecessor(N node) {
/* 74 */     V unused = removeSuccessor(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public V removeSuccessor(N node) {
/* 79 */     return this.adjacentNodeValues.remove(node);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addPredecessor(N node, V value) {
/* 85 */     V unused = addSuccessor(node, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public V addSuccessor(N node, V value) {
/* 90 */     return this.adjacentNodeValues.put(node, value);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\UndirectedGraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */