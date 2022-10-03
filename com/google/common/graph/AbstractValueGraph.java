/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public abstract class AbstractValueGraph<N, V>
/*     */   extends AbstractBaseGraph<N>
/*     */   implements ValueGraph<N, V>
/*     */ {
/*     */   public Graph<N> asGraph() {
/*  45 */     return new AbstractGraph<N>()
/*     */       {
/*     */         public Set<N> nodes() {
/*  48 */           return AbstractValueGraph.this.nodes();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<EndpointPair<N>> edges() {
/*  53 */           return AbstractValueGraph.this.edges();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDirected() {
/*  58 */           return AbstractValueGraph.this.isDirected();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean allowsSelfLoops() {
/*  63 */           return AbstractValueGraph.this.allowsSelfLoops();
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> nodeOrder() {
/*  68 */           return AbstractValueGraph.this.nodeOrder();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> adjacentNodes(N node) {
/*  73 */           return AbstractValueGraph.this.adjacentNodes(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> predecessors(N node) {
/*  78 */           return AbstractValueGraph.this.predecessors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> successors(N node) {
/*  83 */           return AbstractValueGraph.this.successors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int degree(N node) {
/*  88 */           return AbstractValueGraph.this.degree(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int inDegree(N node) {
/*  93 */           return AbstractValueGraph.this.inDegree(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int outDegree(N node) {
/*  98 */           return AbstractValueGraph.this.outDegree(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 105 */     return Optional.ofNullable(edgeValueOrDefault(nodeU, nodeV, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 110 */     return Optional.ofNullable(edgeValueOrDefault(endpoints, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 115 */     if (obj == this) {
/* 116 */       return true;
/*     */     }
/* 118 */     if (!(obj instanceof ValueGraph)) {
/* 119 */       return false;
/*     */     }
/* 121 */     ValueGraph<?, ?> other = (ValueGraph<?, ?>)obj;
/*     */     
/* 123 */     return (isDirected() == other.isDirected() && 
/* 124 */       nodes().equals(other.nodes()) && 
/* 125 */       edgeValueMap(this).equals(edgeValueMap(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 130 */     return edgeValueMap(this).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     return "isDirected: " + 
/* 137 */       isDirected() + ", allowsSelfLoops: " + 
/*     */       
/* 139 */       allowsSelfLoops() + ", nodes: " + 
/*     */       
/* 141 */       nodes() + ", edges: " + 
/*     */       
/* 143 */       edgeValueMap(this);
/*     */   }
/*     */   
/*     */   private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(final ValueGraph<N, V> graph) {
/* 147 */     Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>()
/*     */       {
/*     */         public V apply(EndpointPair<N> edge)
/*     */         {
/* 151 */           return (V)graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null);
/*     */         }
/*     */       };
/* 154 */     return Maps.asMap(graph.edges(), edgeToValueFn);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\AbstractValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */