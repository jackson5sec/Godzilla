/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.HashMultiset;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ final class UndirectedMultiNetworkConnections<N, E>
/*     */   extends AbstractUndirectedNetworkConnections<N, E>
/*     */ {
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> adjacentNodesReference;
/*     */   
/*     */   private UndirectedMultiNetworkConnections(Map<E, N> incidentEdges) {
/*  46 */     super(incidentEdges);
/*     */   }
/*     */   
/*     */   static <N, E> UndirectedMultiNetworkConnections<N, E> of() {
/*  50 */     return new UndirectedMultiNetworkConnections<>(new HashMap<>(2, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   static <N, E> UndirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> incidentEdges) {
/*  55 */     return new UndirectedMultiNetworkConnections<>((Map<E, N>)ImmutableMap.copyOf(incidentEdges));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/*  62 */     return Collections.unmodifiableSet(adjacentNodesMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> adjacentNodesMultiset() {
/*     */     HashMultiset hashMultiset;
/*  66 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/*  67 */     if (adjacentNodes == null) {
/*  68 */       hashMultiset = HashMultiset.create(this.incidentEdgeMap.values());
/*  69 */       this.adjacentNodesReference = new SoftReference(hashMultiset);
/*     */     } 
/*  71 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(final N node) {
/*  76 */     return new MultiEdgesConnecting<E>(this.incidentEdgeMap, node)
/*     */       {
/*     */         public int size() {
/*  79 */           return UndirectedMultiNetworkConnections.this.adjacentNodesMultiset().count(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeInEdge(E edge, boolean isSelfLoop) {
/*  86 */     if (!isSelfLoop) {
/*  87 */       return removeOutEdge(edge);
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(E edge) {
/*  94 */     N node = super.removeOutEdge(edge);
/*  95 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/*  96 */     if (adjacentNodes != null) {
/*  97 */       Preconditions.checkState(adjacentNodes.remove(node));
/*     */     }
/*  99 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 104 */     if (!isSelfLoop) {
/* 105 */       addOutEdge(edge, node);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 111 */     super.addOutEdge(edge, node);
/* 112 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/* 113 */     if (adjacentNodes != null) {
/* 114 */       Preconditions.checkState(adjacentNodes.add(node));
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> T getReference(Reference<T> reference) {
/* 119 */     return (reference == null) ? null : reference.get();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\UndirectedMultiNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */