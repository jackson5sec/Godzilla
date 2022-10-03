/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @Beta
/*     */ public final class ElementOrder<T>
/*     */ {
/*     */   private final Type type;
/*     */   private final Comparator<T> comparator;
/*     */   
/*     */   public enum Type
/*     */   {
/*  65 */     UNORDERED,
/*  66 */     INSERTION,
/*  67 */     SORTED;
/*     */   }
/*     */   
/*     */   private ElementOrder(Type type, Comparator<T> comparator) {
/*  71 */     this.type = (Type)Preconditions.checkNotNull(type);
/*  72 */     this.comparator = comparator;
/*  73 */     Preconditions.checkState((((type == Type.SORTED) ? true : false) == ((comparator != null) ? true : false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> unordered() {
/*  78 */     return new ElementOrder<>(Type.UNORDERED, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> insertion() {
/*  83 */     return new ElementOrder<>(Type.INSERTION, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
/*  90 */     return new ElementOrder<>(Type.SORTED, (Comparator<S>)Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
/*  98 */     return new ElementOrder<>(Type.SORTED, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public Type type() {
/* 103 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<T> comparator() {
/* 112 */     if (this.comparator != null) {
/* 113 */       return this.comparator;
/*     */     }
/* 115 */     throw new UnsupportedOperationException("This ordering does not define a comparator.");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 120 */     if (obj == this) {
/* 121 */       return true;
/*     */     }
/* 123 */     if (!(obj instanceof ElementOrder)) {
/* 124 */       return false;
/*     */     }
/*     */     
/* 127 */     ElementOrder<?> other = (ElementOrder)obj;
/* 128 */     return (this.type == other.type && Objects.equal(this.comparator, other.comparator));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 133 */     return Objects.hashCode(new Object[] { this.type, this.comparator });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
/* 139 */     if (this.comparator != null) {
/* 140 */       helper.add("comparator", this.comparator);
/*     */     }
/* 142 */     return helper.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   <K extends T, V> Map<K, V> createMap(int expectedSize) {
/* 147 */     switch (this.type) {
/*     */       case UNORDERED:
/* 149 */         return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */       case INSERTION:
/* 151 */         return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */       case SORTED:
/* 153 */         return Maps.newTreeMap(comparator());
/*     */     } 
/* 155 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <T1 extends T> ElementOrder<T1> cast() {
/* 161 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\ElementOrder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */