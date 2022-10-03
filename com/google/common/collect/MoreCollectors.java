/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class MoreCollectors
/*     */ {
/*  44 */   private static final Collector<Object, ?, Optional<Object>> TO_OPTIONAL = Collector.of(ToOptionalState::new, ToOptionalState::add, ToOptionalState::combine, ToOptionalState::getOptional, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Collector<T, ?, Optional<T>> toOptional() {
/*  59 */     return (Collector)TO_OPTIONAL;
/*     */   }
/*     */   
/*  62 */   private static final Object NULL_PLACEHOLDER = new Object();
/*     */   
/*     */   static {
/*  65 */     ONLY_ELEMENT = Collector.of(ToOptionalState::new, (state, o) -> state.add((o == null) ? NULL_PLACEHOLDER : o), ToOptionalState::combine, state -> {
/*     */           Object result = state.getElement();
/*     */           return (result == NULL_PLACEHOLDER) ? null : result;
/*     */         }new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Collector<Object, ?, Object> ONLY_ELEMENT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Collector<T, ?, T> onlyElement() {
/*  82 */     return (Collector)ONLY_ELEMENT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ToOptionalState
/*     */   {
/*     */     static final int MAX_EXTRAS = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     Object element = null;
/*  97 */     List<Object> extras = null;
/*     */ 
/*     */ 
/*     */     
/*     */     IllegalArgumentException multiples(boolean overflow) {
/* 102 */       StringBuilder sb = (new StringBuilder()).append("expected one element but was: <").append(this.element);
/* 103 */       for (Object o : this.extras) {
/* 104 */         sb.append(", ").append(o);
/*     */       }
/* 106 */       if (overflow) {
/* 107 */         sb.append(", ...");
/*     */       }
/* 109 */       sb.append('>');
/* 110 */       throw new IllegalArgumentException(sb.toString());
/*     */     }
/*     */     
/*     */     void add(Object o) {
/* 114 */       Preconditions.checkNotNull(o);
/* 115 */       if (this.element == null) {
/* 116 */         this.element = o;
/* 117 */       } else if (this.extras == null) {
/* 118 */         this.extras = new ArrayList(4);
/* 119 */         this.extras.add(o);
/* 120 */       } else if (this.extras.size() < 4) {
/* 121 */         this.extras.add(o);
/*     */       } else {
/* 123 */         throw multiples(true);
/*     */       } 
/*     */     }
/*     */     
/*     */     ToOptionalState combine(ToOptionalState other) {
/* 128 */       if (this.element == null)
/* 129 */         return other; 
/* 130 */       if (other.element == null) {
/* 131 */         return this;
/*     */       }
/* 133 */       if (this.extras == null) {
/* 134 */         this.extras = new ArrayList();
/*     */       }
/* 136 */       this.extras.add(other.element);
/* 137 */       if (other.extras != null) {
/* 138 */         this.extras.addAll(other.extras);
/*     */       }
/* 140 */       if (this.extras.size() > 4) {
/* 141 */         this.extras.subList(4, this.extras.size()).clear();
/* 142 */         throw multiples(true);
/*     */       } 
/* 144 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     Optional<Object> getOptional() {
/* 149 */       if (this.extras == null) {
/* 150 */         return Optional.ofNullable(this.element);
/*     */       }
/* 152 */       throw multiples(false);
/*     */     }
/*     */ 
/*     */     
/*     */     Object getElement() {
/* 157 */       if (this.element == null)
/* 158 */         throw new NoSuchElementException(); 
/* 159 */       if (this.extras == null) {
/* 160 */         return this.element;
/*     */       }
/* 162 */       throw multiples(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\MoreCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */