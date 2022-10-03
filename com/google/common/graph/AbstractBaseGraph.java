/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.AbstractSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractBaseGraph<N>
/*     */   implements BaseGraph<N>
/*     */ {
/*     */   protected long edgeCount() {
/*  52 */     long degreeSum = 0L;
/*  53 */     for (N node : nodes()) {
/*  54 */       degreeSum += degree(node);
/*     */     }
/*     */     
/*  57 */     Preconditions.checkState(((degreeSum & 0x1L) == 0L));
/*  58 */     return degreeSum >>> 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> edges() {
/*  67 */     return new AbstractSet<EndpointPair<N>>()
/*     */       {
/*     */         public UnmodifiableIterator<EndpointPair<N>> iterator() {
/*  70 */           return (UnmodifiableIterator)EndpointPairIterator.of(AbstractBaseGraph.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/*  75 */           return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(Object o) {
/*  80 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/*  89 */           if (!(obj instanceof EndpointPair)) {
/*  90 */             return false;
/*     */           }
/*  92 */           EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  93 */           return (AbstractBaseGraph.this.isOrderingCompatible(endpointPair) && AbstractBaseGraph.this
/*  94 */             .nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this
/*  95 */             .successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> incidentEdges(N node) {
/* 102 */     Preconditions.checkNotNull(node);
/* 103 */     Preconditions.checkArgument(nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 104 */     return IncidentEdgeSet.of(this, node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 109 */     if (isDirected()) {
/* 110 */       return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
/*     */     }
/* 112 */     Set<N> neighbors = adjacentNodes(node);
/* 113 */     int selfLoopCount = (allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
/* 114 */     return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 120 */     return isDirected() ? predecessors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 125 */     return isDirected() ? successors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 130 */     Preconditions.checkNotNull(nodeU);
/* 131 */     Preconditions.checkNotNull(nodeV);
/* 132 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 137 */     Preconditions.checkNotNull(endpoints);
/* 138 */     if (!isOrderingCompatible(endpoints)) {
/* 139 */       return false;
/*     */     }
/* 141 */     N nodeU = endpoints.nodeU();
/* 142 */     N nodeV = endpoints.nodeV();
/* 143 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateEndpoints(EndpointPair<?> endpoints) {
/* 151 */     Preconditions.checkNotNull(endpoints);
/* 152 */     Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
/*     */   }
/*     */   
/*     */   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
/* 156 */     return (endpoints.isOrdered() || !isDirected());
/*     */   }
/*     */   
/*     */   private static abstract class IncidentEdgeSet<N> extends AbstractSet<EndpointPair<N>> {
/*     */     protected final N node;
/*     */     protected final BaseGraph<N> graph;
/*     */     
/*     */     public static <N> IncidentEdgeSet<N> of(BaseGraph<N> graph, N node) {
/* 164 */       return graph.isDirected() ? new Directed<>(graph, node) : new Undirected<>(graph, node);
/*     */     }
/*     */     
/*     */     private IncidentEdgeSet(BaseGraph<N> graph, N node) {
/* 168 */       this.graph = graph;
/* 169 */       this.node = node;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 174 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     private static final class Directed<N>
/*     */       extends IncidentEdgeSet<N> {
/*     */       private Directed(BaseGraph<N> graph, N node) {
/* 180 */         super(graph, node);
/*     */       }
/*     */ 
/*     */       
/*     */       public UnmodifiableIterator<EndpointPair<N>> iterator() {
/* 185 */         return Iterators.unmodifiableIterator(
/* 186 */             Iterators.concat(
/* 187 */               Iterators.transform(this.graph
/* 188 */                 .predecessors(this.node).iterator(), new Function<N, EndpointPair<N>>()
/*     */                 {
/*     */                   public EndpointPair<N> apply(N predecessor)
/*     */                   {
/* 192 */                     return EndpointPair.ordered(predecessor, AbstractBaseGraph.IncidentEdgeSet.Directed.this.node);
/*     */                   }
/* 195 */                 }), Iterators.transform(
/*     */                 
/* 197 */                 (Iterator)Sets.difference(this.graph.successors(this.node), (Set)ImmutableSet.of(this.node)).iterator(), new Function<N, EndpointPair<N>>()
/*     */                 {
/*     */                   public EndpointPair<N> apply(N successor)
/*     */                   {
/* 201 */                     return EndpointPair.ordered(AbstractBaseGraph.IncidentEdgeSet.Directed.this.node, successor);
/*     */                   }
/*     */                 })));
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 208 */         return this.graph.inDegree(this.node) + this.graph
/* 209 */           .outDegree(this.node) - (
/* 210 */           this.graph.successors(this.node).contains(this.node) ? 1 : 0);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 215 */         if (!(obj instanceof EndpointPair)) {
/* 216 */           return false;
/*     */         }
/*     */         
/* 219 */         EndpointPair<?> endpointPair = (EndpointPair)obj;
/* 220 */         if (!endpointPair.isOrdered()) {
/* 221 */           return false;
/*     */         }
/*     */         
/* 224 */         Object source = endpointPair.source();
/* 225 */         Object target = endpointPair.target();
/* 226 */         return ((this.node.equals(source) && this.graph.successors(this.node).contains(target)) || (this.node
/* 227 */           .equals(target) && this.graph.predecessors(this.node).contains(source)));
/*     */       }
/*     */     }
/*     */     
/*     */     private static final class Undirected<N> extends IncidentEdgeSet<N> {
/*     */       private Undirected(BaseGraph<N> graph, N node) {
/* 233 */         super(graph, node);
/*     */       }
/*     */ 
/*     */       
/*     */       public UnmodifiableIterator<EndpointPair<N>> iterator() {
/* 238 */         return Iterators.unmodifiableIterator(
/* 239 */             Iterators.transform(this.graph
/* 240 */               .adjacentNodes(this.node).iterator(), new Function<N, EndpointPair<N>>()
/*     */               {
/*     */                 public EndpointPair<N> apply(N adjacentNode)
/*     */                 {
/* 244 */                   return EndpointPair.unordered(AbstractBaseGraph.IncidentEdgeSet.Undirected.this.node, adjacentNode);
/*     */                 }
/*     */               }));
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 251 */         return this.graph.adjacentNodes(this.node).size();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 256 */         if (!(obj instanceof EndpointPair)) {
/* 257 */           return false;
/*     */         }
/*     */         
/* 260 */         EndpointPair<?> endpointPair = (EndpointPair)obj;
/* 261 */         if (endpointPair.isOrdered()) {
/* 262 */           return false;
/*     */         }
/* 264 */         Set<N> adjacent = this.graph.adjacentNodes(this.node);
/* 265 */         Object nodeU = endpointPair.nodeU();
/* 266 */         Object nodeV = endpointPair.nodeV();
/*     */         
/* 268 */         return ((this.node.equals(nodeV) && adjacent.contains(nodeU)) || (this.node
/* 269 */           .equals(nodeU) && adjacent.contains(nodeV)));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\AbstractBaseGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */