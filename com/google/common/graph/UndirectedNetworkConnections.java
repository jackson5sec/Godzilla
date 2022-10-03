/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.collect.BiMap;
/*    */ import com.google.common.collect.HashBiMap;
/*    */ import com.google.common.collect.ImmutableBiMap;
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
/*    */ final class UndirectedNetworkConnections<N, E>
/*    */   extends AbstractUndirectedNetworkConnections<N, E>
/*    */ {
/*    */   protected UndirectedNetworkConnections(Map<E, N> incidentEdgeMap) {
/* 38 */     super(incidentEdgeMap);
/*    */   }
/*    */   
/*    */   static <N, E> UndirectedNetworkConnections<N, E> of() {
/* 42 */     return new UndirectedNetworkConnections<>((Map<E, N>)HashBiMap.create(2));
/*    */   }
/*    */   
/*    */   static <N, E> UndirectedNetworkConnections<N, E> ofImmutable(Map<E, N> incidentEdges) {
/* 46 */     return new UndirectedNetworkConnections<>((Map<E, N>)ImmutableBiMap.copyOf(incidentEdges));
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> adjacentNodes() {
/* 51 */     return Collections.unmodifiableSet(((BiMap)this.incidentEdgeMap).values());
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<E> edgesConnecting(N node) {
/* 56 */     return new EdgesConnecting<>((Map<?, E>)((BiMap)this.incidentEdgeMap).inverse(), node);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\UndirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */