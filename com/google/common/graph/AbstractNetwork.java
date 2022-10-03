/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public abstract class AbstractNetwork<N, E>
/*     */   implements Network<N, E>
/*     */ {
/*     */   public Graph<N> asGraph() {
/*  57 */     return new AbstractGraph<N>()
/*     */       {
/*     */         public Set<N> nodes() {
/*  60 */           return AbstractNetwork.this.nodes();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<EndpointPair<N>> edges() {
/*  65 */           if (AbstractNetwork.this.allowsParallelEdges()) {
/*  66 */             return super.edges();
/*     */           }
/*     */ 
/*     */           
/*  70 */           return new AbstractSet<EndpointPair<N>>()
/*     */             {
/*     */               public Iterator<EndpointPair<N>> iterator() {
/*  73 */                 return Iterators.transform(AbstractNetwork.this
/*  74 */                     .edges().iterator(), new Function<E, EndpointPair<N>>()
/*     */                     {
/*     */                       public EndpointPair<N> apply(E edge)
/*     */                       {
/*  78 */                         return AbstractNetwork.this.incidentNodes(edge);
/*     */                       }
/*     */                     });
/*     */               }
/*     */ 
/*     */               
/*     */               public int size() {
/*  85 */                 return AbstractNetwork.this.edges().size();
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public boolean contains(Object obj) {
/*  94 */                 if (!(obj instanceof EndpointPair)) {
/*  95 */                   return false;
/*     */                 }
/*  97 */                 EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  98 */                 return (AbstractNetwork.null.this.isOrderingCompatible(endpointPair) && AbstractNetwork.null.this
/*  99 */                   .nodes().contains(endpointPair.nodeU()) && AbstractNetwork.null.this
/* 100 */                   .successors((N)endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> nodeOrder() {
/* 107 */           return AbstractNetwork.this.nodeOrder();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDirected() {
/* 112 */           return AbstractNetwork.this.isDirected();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean allowsSelfLoops() {
/* 117 */           return AbstractNetwork.this.allowsSelfLoops();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> adjacentNodes(N node) {
/* 122 */           return AbstractNetwork.this.adjacentNodes(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> predecessors(N node) {
/* 127 */           return AbstractNetwork.this.predecessors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> successors(N node) {
/* 132 */           return AbstractNetwork.this.successors(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 141 */     if (isDirected()) {
/* 142 */       return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
/*     */     }
/* 144 */     return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 150 */     return isDirected() ? inEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 155 */     return isDirected() ? outEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> adjacentEdges(E edge) {
/* 160 */     EndpointPair<N> endpointPair = incidentNodes(edge);
/*     */     
/* 162 */     Sets.SetView setView = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
/* 163 */     return (Set<E>)Sets.difference((Set)setView, (Set)ImmutableSet.of(edge));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 168 */     Set<E> outEdgesU = outEdges(nodeU);
/* 169 */     Set<E> inEdgesV = inEdges(nodeV);
/* 170 */     return (outEdgesU.size() <= inEdgesV.size()) ? 
/* 171 */       Collections.<E>unmodifiableSet(Sets.filter(outEdgesU, connectedPredicate(nodeU, nodeV))) : 
/* 172 */       Collections.<E>unmodifiableSet(Sets.filter(inEdgesV, connectedPredicate(nodeV, nodeU)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 177 */     validateEndpoints(endpoints);
/* 178 */     return edgesConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */   
/*     */   private Predicate<E> connectedPredicate(final N nodePresent, final N nodeToCheck) {
/* 182 */     return new Predicate<E>()
/*     */       {
/*     */         public boolean apply(E edge) {
/* 185 */           return AbstractNetwork.this.incidentNodes(edge).adjacentNode(nodePresent).equals(nodeToCheck);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 192 */     return Optional.ofNullable(edgeConnectingOrNull(nodeU, nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 197 */     validateEndpoints(endpoints);
/* 198 */     return edgeConnecting(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */   
/*     */   public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 203 */     Set<E> edgesConnecting = edgesConnecting(nodeU, nodeV);
/* 204 */     switch (edgesConnecting.size()) {
/*     */       case 0:
/* 206 */         return null;
/*     */       case 1:
/* 208 */         return edgesConnecting.iterator().next();
/*     */     } 
/* 210 */     throw new IllegalArgumentException(String.format("Cannot call edgeConnecting() when parallel edges exist between %s and %s. Consider calling edgesConnecting() instead.", new Object[] { nodeU, nodeV }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 216 */     validateEndpoints(endpoints);
/* 217 */     return edgeConnectingOrNull(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 222 */     return !edgesConnecting(nodeU, nodeV).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 227 */     Preconditions.checkNotNull(endpoints);
/* 228 */     if (!isOrderingCompatible(endpoints)) {
/* 229 */       return false;
/*     */     }
/* 231 */     return !edgesConnecting(endpoints.nodeU(), endpoints.nodeV()).isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateEndpoints(EndpointPair<?> endpoints) {
/* 239 */     Preconditions.checkNotNull(endpoints);
/* 240 */     Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: unordered endpoints cannot be used with directed graphs");
/*     */   }
/*     */   
/*     */   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
/* 244 */     return (endpoints.isOrdered() || !isDirected());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 249 */     if (obj == this) {
/* 250 */       return true;
/*     */     }
/* 252 */     if (!(obj instanceof Network)) {
/* 253 */       return false;
/*     */     }
/* 255 */     Network<?, ?> other = (Network<?, ?>)obj;
/*     */     
/* 257 */     return (isDirected() == other.isDirected() && 
/* 258 */       nodes().equals(other.nodes()) && 
/* 259 */       edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 264 */     return edgeIncidentNodesMap(this).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 270 */     return "isDirected: " + 
/* 271 */       isDirected() + ", allowsParallelEdges: " + 
/*     */       
/* 273 */       allowsParallelEdges() + ", allowsSelfLoops: " + 
/*     */       
/* 275 */       allowsSelfLoops() + ", nodes: " + 
/*     */       
/* 277 */       nodes() + ", edges: " + 
/*     */       
/* 279 */       edgeIncidentNodesMap(this);
/*     */   }
/*     */   
/*     */   private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(final Network<N, E> network) {
/* 283 */     Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>()
/*     */       {
/*     */         public EndpointPair<N> apply(E edge)
/*     */         {
/* 287 */           return network.incidentNodes(edge);
/*     */         }
/*     */       };
/* 290 */     return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\AbstractNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */