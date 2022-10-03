/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
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
/*     */ abstract class EndpointPairIterator<N>
/*     */   extends AbstractIterator<EndpointPair<N>>
/*     */ {
/*     */   private final BaseGraph<N> graph;
/*     */   private final Iterator<N> nodeIterator;
/*  36 */   protected N node = null;
/*  37 */   protected Iterator<N> successorIterator = (Iterator<N>)ImmutableSet.of().iterator();
/*     */   
/*     */   static <N> EndpointPairIterator<N> of(BaseGraph<N> graph) {
/*  40 */     return graph.isDirected() ? new Directed<>(graph) : new Undirected<>(graph);
/*     */   }
/*     */   
/*     */   private EndpointPairIterator(BaseGraph<N> graph) {
/*  44 */     this.graph = graph;
/*  45 */     this.nodeIterator = graph.nodes().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean advance() {
/*  53 */     Preconditions.checkState(!this.successorIterator.hasNext());
/*  54 */     if (!this.nodeIterator.hasNext()) {
/*  55 */       return false;
/*     */     }
/*  57 */     this.node = this.nodeIterator.next();
/*  58 */     this.successorIterator = this.graph.successors(this.node).iterator();
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Directed<N>
/*     */     extends EndpointPairIterator<N>
/*     */   {
/*     */     private Directed(BaseGraph<N> graph) {
/*  68 */       super(graph);
/*     */     }
/*     */ 
/*     */     
/*     */     protected EndpointPair<N> computeNext() {
/*     */       while (true) {
/*  74 */         if (this.successorIterator.hasNext()) {
/*  75 */           return EndpointPair.ordered(this.node, this.successorIterator.next());
/*     */         }
/*  77 */         if (!advance()) {
/*  78 */           return (EndpointPair<N>)endOfData();
/*     */         }
/*     */       } 
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Undirected<N>
/*     */     extends EndpointPairIterator<N>
/*     */   {
/*     */     private Set<N> visitedNodes;
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
/*     */     private Undirected(BaseGraph<N> graph) {
/* 114 */       super(graph);
/* 115 */       this.visitedNodes = Sets.newHashSetWithExpectedSize(graph.nodes().size());
/*     */     }
/*     */ 
/*     */     
/*     */     protected EndpointPair<N> computeNext() {
/*     */       while (true) {
/* 121 */         while (this.successorIterator.hasNext()) {
/* 122 */           N otherNode = this.successorIterator.next();
/* 123 */           if (!this.visitedNodes.contains(otherNode)) {
/* 124 */             return EndpointPair.unordered(this.node, otherNode);
/*     */           }
/*     */         } 
/*     */         
/* 128 */         this.visitedNodes.add(this.node);
/* 129 */         if (!advance()) {
/* 130 */           this.visitedNodes = null;
/* 131 */           return (EndpointPair<N>)endOfData();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\EndpointPairIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */