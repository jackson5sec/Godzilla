/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Iterator;
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
/*     */ @Immutable(containerOf = {"N"})
/*     */ @Beta
/*     */ public abstract class EndpointPair<N>
/*     */   implements Iterable<N>
/*     */ {
/*     */   private final N nodeU;
/*     */   private final N nodeV;
/*     */   
/*     */   private EndpointPair(N nodeU, N nodeV) {
/*  47 */     this.nodeU = (N)Preconditions.checkNotNull(nodeU);
/*  48 */     this.nodeV = (N)Preconditions.checkNotNull(nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> EndpointPair<N> ordered(N source, N target) {
/*  53 */     return new Ordered<>(source, target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> EndpointPair<N> unordered(N nodeU, N nodeV) {
/*  59 */     return new Unordered<>(nodeV, nodeU);
/*     */   }
/*     */ 
/*     */   
/*     */   static <N> EndpointPair<N> of(Graph<?> graph, N nodeU, N nodeV) {
/*  64 */     return graph.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   static <N> EndpointPair<N> of(Network<?, ?> network, N nodeU, N nodeV) {
/*  69 */     return network.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N nodeU() {
/*  91 */     return this.nodeU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N nodeV() {
/*  99 */     return this.nodeV;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N adjacentNode(Object node) {
/* 108 */     if (node.equals(this.nodeU))
/* 109 */       return this.nodeV; 
/* 110 */     if (node.equals(this.nodeV)) {
/* 111 */       return this.nodeU;
/*     */     }
/* 113 */     throw new IllegalArgumentException("EndpointPair " + this + " does not contain node " + node);
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
/*     */   public final UnmodifiableIterator<N> iterator() {
/* 126 */     return Iterators.forArray(new Object[] { this.nodeU, this.nodeV });
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract N source();
/*     */ 
/*     */   
/*     */   public abstract N target();
/*     */ 
/*     */   
/*     */   public abstract boolean isOrdered();
/*     */ 
/*     */   
/*     */   public abstract boolean equals(Object paramObject);
/*     */   
/*     */   public abstract int hashCode();
/*     */   
/*     */   private static final class Ordered<N>
/*     */     extends EndpointPair<N>
/*     */   {
/*     */     private Ordered(N source, N target) {
/* 147 */       super(source, target);
/*     */     }
/*     */ 
/*     */     
/*     */     public N source() {
/* 152 */       return nodeU();
/*     */     }
/*     */ 
/*     */     
/*     */     public N target() {
/* 157 */       return nodeV();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOrdered() {
/* 162 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 167 */       if (obj == this) {
/* 168 */         return true;
/*     */       }
/* 170 */       if (!(obj instanceof EndpointPair)) {
/* 171 */         return false;
/*     */       }
/*     */       
/* 174 */       EndpointPair<?> other = (EndpointPair)obj;
/* 175 */       if (isOrdered() != other.isOrdered()) {
/* 176 */         return false;
/*     */       }
/*     */       
/* 179 */       return (source().equals(other.source()) && target().equals(other.target()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 184 */       return Objects.hashCode(new Object[] { source(), target() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 189 */       return "<" + source() + " -> " + target() + ">";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Unordered<N> extends EndpointPair<N> {
/*     */     private Unordered(N nodeU, N nodeV) {
/* 195 */       super(nodeU, nodeV);
/*     */     }
/*     */ 
/*     */     
/*     */     public N source() {
/* 200 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */ 
/*     */     
/*     */     public N target() {
/* 205 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOrdered() {
/* 210 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 215 */       if (obj == this) {
/* 216 */         return true;
/*     */       }
/* 218 */       if (!(obj instanceof EndpointPair)) {
/* 219 */         return false;
/*     */       }
/*     */       
/* 222 */       EndpointPair<?> other = (EndpointPair)obj;
/* 223 */       if (isOrdered() != other.isOrdered()) {
/* 224 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 231 */       if (nodeU().equals(other.nodeU()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 237 */         return nodeV().equals(other.nodeV());
/*     */       }
/* 239 */       return (nodeU().equals(other.nodeV()) && nodeV().equals(other.nodeU()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 244 */       return nodeU().hashCode() + nodeV().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 249 */       return "[" + nodeU() + ", " + nodeV() + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\EndpointPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */