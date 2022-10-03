/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @Beta
/*     */ public final class NetworkBuilder<N, E>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   boolean allowsParallelEdges = false;
/*  60 */   ElementOrder<? super E> edgeOrder = ElementOrder.insertion();
/*  61 */   Optional<Integer> expectedEdgeCount = Optional.absent();
/*     */ 
/*     */   
/*     */   private NetworkBuilder(boolean directed) {
/*  65 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> directed() {
/*  70 */     return new NetworkBuilder<>(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> undirected() {
/*  75 */     return new NetworkBuilder<>(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> NetworkBuilder<N, E> from(Network<N, E> network) {
/*  87 */     return (new NetworkBuilder<>(network.isDirected()))
/*  88 */       .allowsParallelEdges(network.allowsParallelEdges())
/*  89 */       .allowsSelfLoops(network.allowsSelfLoops())
/*  90 */       .nodeOrder(network.nodeOrder())
/*  91 */       .edgeOrder(network.edgeOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> allowsParallelEdges(boolean allowsParallelEdges) {
/*  99 */     this.allowsParallelEdges = allowsParallelEdges;
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> allowsSelfLoops(boolean allowsSelfLoops) {
/* 109 */     this.allowsSelfLoops = allowsSelfLoops;
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> expectedNodeCount(int expectedNodeCount) {
/* 119 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> expectedEdgeCount(int expectedEdgeCount) {
/* 129 */     this.expectedEdgeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedEdgeCount)));
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> NetworkBuilder<N1, E> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 135 */     NetworkBuilder<N1, E> newBuilder = cast();
/* 136 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 137 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <E1 extends E> NetworkBuilder<N, E1> edgeOrder(ElementOrder<E1> edgeOrder) {
/* 142 */     NetworkBuilder<N, E1> newBuilder = cast();
/* 143 */     newBuilder.edgeOrder = (ElementOrder<? super E>)Preconditions.checkNotNull(edgeOrder);
/* 144 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N, E1 extends E> MutableNetwork<N1, E1> build() {
/* 149 */     return new ConfigurableMutableNetwork<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N, E1 extends E> NetworkBuilder<N1, E1> cast() {
/* 154 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\NetworkBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */