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
/*     */ public final class ValueGraphBuilder<N, V>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private ValueGraphBuilder(boolean directed) {
/*  60 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> directed() {
/*  65 */     return new ValueGraphBuilder<>(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> undirected() {
/*  70 */     return new ValueGraphBuilder<>(false);
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
/*     */   public static <N, V> ValueGraphBuilder<N, V> from(ValueGraph<N, V> graph) {
/*  82 */     return (new ValueGraphBuilder<>(graph.isDirected()))
/*  83 */       .allowsSelfLoops(graph.allowsSelfLoops())
/*  84 */       .nodeOrder(graph.nodeOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueGraphBuilder<N, V> allowsSelfLoops(boolean allowsSelfLoops) {
/*  93 */     this.allowsSelfLoops = allowsSelfLoops;
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueGraphBuilder<N, V> expectedNodeCount(int expectedNodeCount) {
/* 103 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> ValueGraphBuilder<N1, V> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 109 */     ValueGraphBuilder<N1, V> newBuilder = cast();
/* 110 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 111 */     return newBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <N1 extends N, V1 extends V> MutableValueGraph<N1, V1> build() {
/* 119 */     return new ConfigurableMutableValueGraph<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N, V1 extends V> ValueGraphBuilder<N1, V1> cast() {
/* 124 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ValueGraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */