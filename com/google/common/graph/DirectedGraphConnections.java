/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ final class DirectedGraphConnections<N, V>
/*     */   implements GraphConnections<N, V>
/*     */ {
/*     */   private static final class PredAndSucc
/*     */   {
/*     */     private final Object successorValue;
/*     */     
/*     */     PredAndSucc(Object successorValue) {
/*  54 */       this.successorValue = successorValue;
/*     */     }
/*     */   }
/*     */   
/*  58 */   private static final Object PRED = new Object();
/*     */ 
/*     */   
/*     */   private final Map<N, Object> adjacentNodeValues;
/*     */   
/*     */   private int predecessorCount;
/*     */   
/*     */   private int successorCount;
/*     */ 
/*     */   
/*     */   private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, int predecessorCount, int successorCount) {
/*  69 */     this.adjacentNodeValues = (Map<N, Object>)Preconditions.checkNotNull(adjacentNodeValues);
/*  70 */     this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
/*  71 */     this.successorCount = Graphs.checkNonNegative(successorCount);
/*  72 */     Preconditions.checkState((predecessorCount <= adjacentNodeValues
/*  73 */         .size() && successorCount <= adjacentNodeValues
/*  74 */         .size()));
/*     */   }
/*     */ 
/*     */   
/*     */   static <N, V> DirectedGraphConnections<N, V> of() {
/*  79 */     int initialCapacity = 4;
/*  80 */     return new DirectedGraphConnections<>(new HashMap<>(initialCapacity, 1.0F), 0, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <N, V> DirectedGraphConnections<N, V> ofImmutable(Set<N> predecessors, Map<N, V> successorValues) {
/*  86 */     Map<N, Object> adjacentNodeValues = new HashMap<>();
/*  87 */     adjacentNodeValues.putAll(successorValues);
/*  88 */     for (N predecessor : predecessors) {
/*  89 */       Object value = adjacentNodeValues.put(predecessor, PRED);
/*  90 */       if (value != null) {
/*  91 */         adjacentNodeValues.put(predecessor, new PredAndSucc(value));
/*     */       }
/*     */     } 
/*  94 */     return new DirectedGraphConnections<>(
/*  95 */         (Map<N, Object>)ImmutableMap.copyOf(adjacentNodeValues), predecessors.size(), successorValues.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/* 100 */     return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/* 105 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 108 */           final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
/* 109 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>()
/*     */             {
/*     */               protected N computeNext() {
/* 112 */                 while (entries.hasNext()) {
/* 113 */                   Map.Entry<N, Object> entry = entries.next();
/* 114 */                   if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
/* 115 */                     return entry.getKey();
/*     */                   }
/*     */                 } 
/* 118 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 125 */           return DirectedGraphConnections.this.predecessorCount;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/* 130 */           return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/* 137 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 140 */           final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
/* 141 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>()
/*     */             {
/*     */               protected N computeNext() {
/* 144 */                 while (entries.hasNext()) {
/* 145 */                   Map.Entry<N, Object> entry = entries.next();
/* 146 */                   if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
/* 147 */                     return entry.getKey();
/*     */                   }
/*     */                 } 
/* 150 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 157 */           return DirectedGraphConnections.this.successorCount;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object obj) {
/* 162 */           return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V value(N node) {
/* 170 */     Object value = this.adjacentNodeValues.get(node);
/* 171 */     if (value == PRED) {
/* 172 */       return null;
/*     */     }
/* 174 */     if (value instanceof PredAndSucc) {
/* 175 */       return (V)((PredAndSucc)value).successorValue;
/*     */     }
/* 177 */     return (V)value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePredecessor(N node) {
/* 183 */     Object previousValue = this.adjacentNodeValues.get(node);
/* 184 */     if (previousValue == PRED) {
/* 185 */       this.adjacentNodeValues.remove(node);
/* 186 */       Graphs.checkNonNegative(--this.predecessorCount);
/* 187 */     } else if (previousValue instanceof PredAndSucc) {
/* 188 */       this.adjacentNodeValues.put(node, ((PredAndSucc)previousValue).successorValue);
/* 189 */       Graphs.checkNonNegative(--this.predecessorCount);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V removeSuccessor(Object node) {
/* 196 */     Object previousValue = this.adjacentNodeValues.get(node);
/* 197 */     if (previousValue == null || previousValue == PRED)
/* 198 */       return null; 
/* 199 */     if (previousValue instanceof PredAndSucc) {
/* 200 */       this.adjacentNodeValues.put((N)node, PRED);
/* 201 */       Graphs.checkNonNegative(--this.successorCount);
/* 202 */       return (V)((PredAndSucc)previousValue).successorValue;
/*     */     } 
/* 204 */     this.adjacentNodeValues.remove(node);
/* 205 */     Graphs.checkNonNegative(--this.successorCount);
/* 206 */     return (V)previousValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPredecessor(N node, V unused) {
/* 212 */     Object previousValue = this.adjacentNodeValues.put(node, PRED);
/* 213 */     if (previousValue == null) {
/* 214 */       Graphs.checkPositive(++this.predecessorCount);
/* 215 */     } else if (previousValue instanceof PredAndSucc) {
/*     */       
/* 217 */       this.adjacentNodeValues.put(node, previousValue);
/* 218 */     } else if (previousValue != PRED) {
/*     */       
/* 220 */       this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
/* 221 */       Graphs.checkPositive(++this.predecessorCount);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V addSuccessor(N node, V value) {
/* 228 */     Object previousValue = this.adjacentNodeValues.put(node, value);
/* 229 */     if (previousValue == null) {
/* 230 */       Graphs.checkPositive(++this.successorCount);
/* 231 */       return null;
/* 232 */     }  if (previousValue instanceof PredAndSucc) {
/* 233 */       this.adjacentNodeValues.put(node, new PredAndSucc(value));
/* 234 */       return (V)((PredAndSucc)previousValue).successorValue;
/* 235 */     }  if (previousValue == PRED) {
/* 236 */       this.adjacentNodeValues.put(node, new PredAndSucc(value));
/* 237 */       Graphs.checkPositive(++this.successorCount);
/* 238 */       return null;
/*     */     } 
/* 240 */     return (V)previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isPredecessor(Object value) {
/* 245 */     return (value == PRED || value instanceof PredAndSucc);
/*     */   }
/*     */   
/*     */   private static boolean isSuccessor(Object value) {
/* 249 */     return (value != PRED && value != null);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\DirectedGraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */