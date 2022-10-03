/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ abstract class AbstractDirectedNetworkConnections<N, E>
/*     */   implements NetworkConnections<N, E>
/*     */ {
/*     */   protected final Map<E, N> inEdgeMap;
/*     */   protected final Map<E, N> outEdgeMap;
/*     */   private int selfLoopCount;
/*     */   
/*     */   protected AbstractDirectedNetworkConnections(Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount) {
/*  53 */     this.inEdgeMap = (Map<E, N>)Preconditions.checkNotNull(inEdgeMap);
/*  54 */     this.outEdgeMap = (Map<E, N>)Preconditions.checkNotNull(outEdgeMap);
/*  55 */     this.selfLoopCount = Graphs.checkNonNegative(selfLoopCount);
/*  56 */     Preconditions.checkState((selfLoopCount <= inEdgeMap.size() && selfLoopCount <= outEdgeMap.size()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/*  61 */     return (Set<N>)Sets.union(predecessors(), successors());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> incidentEdges() {
/*  66 */     return new AbstractSet<E>()
/*     */       {
/*     */ 
/*     */         
/*     */         public UnmodifiableIterator<E> iterator()
/*     */         {
/*  72 */           Iterable<E> incidentEdges = (AbstractDirectedNetworkConnections.this.selfLoopCount == 0) ? Iterables.concat(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()) : (Iterable<E>)Sets.union(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet());
/*  73 */           return Iterators.unmodifiableIterator(incidentEdges.iterator());
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/*  78 */           return IntMath.saturatedAdd(AbstractDirectedNetworkConnections.this.inEdgeMap.size(), AbstractDirectedNetworkConnections.this.outEdgeMap.size() - AbstractDirectedNetworkConnections.this.selfLoopCount);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/*  83 */           return (AbstractDirectedNetworkConnections.this.inEdgeMap.containsKey(obj) || AbstractDirectedNetworkConnections.this.outEdgeMap.containsKey(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> inEdges() {
/*  90 */     return Collections.unmodifiableSet(this.inEdgeMap.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> outEdges() {
/*  95 */     return Collections.unmodifiableSet(this.outEdgeMap.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public N adjacentNode(E edge) {
/* 102 */     return (N)Preconditions.checkNotNull(this.outEdgeMap.get(edge));
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeInEdge(E edge, boolean isSelfLoop) {
/* 107 */     if (isSelfLoop) {
/* 108 */       Graphs.checkNonNegative(--this.selfLoopCount);
/*     */     }
/* 110 */     N previousNode = this.inEdgeMap.remove(edge);
/* 111 */     return (N)Preconditions.checkNotNull(previousNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(E edge) {
/* 116 */     N previousNode = this.outEdgeMap.remove(edge);
/* 117 */     return (N)Preconditions.checkNotNull(previousNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 122 */     if (isSelfLoop) {
/* 123 */       Graphs.checkPositive(++this.selfLoopCount);
/*     */     }
/* 125 */     N previousNode = this.inEdgeMap.put(edge, node);
/* 126 */     Preconditions.checkState((previousNode == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 131 */     N previousNode = this.outEdgeMap.put(edge, node);
/* 132 */     Preconditions.checkState((previousNode == null));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\AbstractDirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */