/*     */ package com.google.common.graph;
/*     */ 
/*     */ import java.util.Optional;
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
/*     */ abstract class ForwardingNetwork<N, E>
/*     */   extends AbstractNetwork<N, E>
/*     */ {
/*     */   public Set<N> nodes() {
/*  35 */     return delegate().nodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edges() {
/*  40 */     return delegate().edges();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  45 */     return delegate().isDirected();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsParallelEdges() {
/*  50 */     return delegate().allowsParallelEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  55 */     return delegate().allowsSelfLoops();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/*  60 */     return delegate().nodeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<E> edgeOrder() {
/*  65 */     return delegate().edgeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/*  70 */     return delegate().adjacentNodes(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/*  75 */     return delegate().predecessors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/*  80 */     return delegate().successors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> incidentEdges(N node) {
/*  85 */     return delegate().incidentEdges(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> inEdges(N node) {
/*  90 */     return delegate().inEdges(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> outEdges(N node) {
/*  95 */     return delegate().outEdges(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointPair<N> incidentNodes(E edge) {
/* 100 */     return delegate().incidentNodes(edge);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> adjacentEdges(E edge) {
/* 105 */     return delegate().adjacentEdges(edge);
/*     */   }
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 110 */     return delegate().degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 115 */     return delegate().inDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 120 */     return delegate().outDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 125 */     return delegate().edgesConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 130 */     return delegate().edgesConnecting(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 135 */     return delegate().edgeConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 140 */     return delegate().edgeConnecting(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 145 */     return delegate().edgeConnectingOrNull(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 150 */     return delegate().edgeConnectingOrNull(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 155 */     return delegate().hasEdgeConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 160 */     return delegate().hasEdgeConnecting(endpoints);
/*     */   }
/*     */   
/*     */   protected abstract Network<N, E> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ForwardingNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */