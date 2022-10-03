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
/*     */ final class DirectedMultiNetworkConnections<N, E>
/*     */   extends AbstractDirectedNetworkConnections<N, E>
/*     */ {
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> predecessorsReference;
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> successorsReference;
/*     */   
/*     */   private DirectedMultiNetworkConnections(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
/*  46 */     super(inEdges, outEdges, selfLoopCount);
/*     */   }
/*     */   
/*     */   static <N, E> DirectedMultiNetworkConnections<N, E> of() {
/*  50 */     return new DirectedMultiNetworkConnections<>(new HashMap<>(2, 1.0F), new HashMap<>(2, 1.0F), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <N, E> DirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
/*  58 */     return new DirectedMultiNetworkConnections<>(
/*  59 */         (Map<E, N>)ImmutableMap.copyOf(inEdges), (Map<E, N>)ImmutableMap.copyOf(outEdges), selfLoopCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/*  66 */     return Collections.unmodifiableSet(predecessorsMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> predecessorsMultiset() {
/*     */     HashMultiset hashMultiset;
/*  70 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/*  71 */     if (predecessors == null) {
/*  72 */       hashMultiset = HashMultiset.create(this.inEdgeMap.values());
/*  73 */       this.predecessorsReference = new SoftReference(hashMultiset);
/*     */     } 
/*  75 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/*  82 */     return Collections.unmodifiableSet(successorsMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> successorsMultiset() {
/*     */     HashMultiset hashMultiset;
/*  86 */     Multiset<N> successors = getReference(this.successorsReference);
/*  87 */     if (successors == null) {
/*  88 */       hashMultiset = HashMultiset.create(this.outEdgeMap.values());
/*  89 */       this.successorsReference = new SoftReference(hashMultiset);
/*     */     } 
/*  91 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(final N node) {
/*  96 */     return new MultiEdgesConnecting<E>(this.outEdgeMap, node)
/*     */       {
/*     */         public int size() {
/*  99 */           return DirectedMultiNetworkConnections.this.successorsMultiset().count(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeInEdge(E edge, boolean isSelfLoop) {
/* 106 */     N node = super.removeInEdge(edge, isSelfLoop);
/* 107 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/* 108 */     if (predecessors != null) {
/* 109 */       Preconditions.checkState(predecessors.remove(node));
/*     */     }
/* 111 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(E edge) {
/* 116 */     N node = super.removeOutEdge(edge);
/* 117 */     Multiset<N> successors = getReference(this.successorsReference);
/* 118 */     if (successors != null) {
/* 119 */       Preconditions.checkState(successors.remove(node));
/*     */     }
/* 121 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 126 */     super.addInEdge(edge, node, isSelfLoop);
/* 127 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/* 128 */     if (predecessors != null) {
/* 129 */       Preconditions.checkState(predecessors.add(node));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 135 */     super.addOutEdge(edge, node);
/* 136 */     Multiset<N> successors = getReference(this.successorsReference);
/* 137 */     if (successors != null) {
/* 138 */       Preconditions.checkState(successors.add(node));
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> T getReference(Reference<T> reference) {
/* 143 */     return (reference == null) ? null : reference.get();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\DirectedMultiNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */