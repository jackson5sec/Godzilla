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
/*     */ @Beta
/*     */ public final class GraphBuilder<N>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private GraphBuilder(boolean directed) {
/*  56 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> directed() {
/*  61 */     return new GraphBuilder(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> undirected() {
/*  66 */     return new GraphBuilder(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> GraphBuilder<N> from(Graph<N> graph) {
/*  77 */     return (new GraphBuilder(graph.isDirected()))
/*  78 */       .allowsSelfLoops(graph.allowsSelfLoops())
/*  79 */       .nodeOrder(graph.nodeOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphBuilder<N> allowsSelfLoops(boolean allowsSelfLoops) {
/*  88 */     this.allowsSelfLoops = allowsSelfLoops;
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphBuilder<N> expectedNodeCount(int expectedNodeCount) {
/*  98 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> GraphBuilder<N1> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 104 */     GraphBuilder<N1> newBuilder = cast();
/* 105 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 106 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> MutableGraph<N1> build() {
/* 111 */     return new ConfigurableMutableGraph<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N> GraphBuilder<N1> cast() {
/* 116 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\GraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */