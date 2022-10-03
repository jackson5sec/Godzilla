/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class MapMaker
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*     */   static final int UNSET_INT = -1;
/*     */   boolean useCustomMap;
/*  98 */   int initialCapacity = -1;
/*  99 */   int concurrencyLevel = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MapMakerInternalMap.Strength keyStrength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MapMakerInternalMap.Strength valueStrength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Equivalence<Object> keyEquivalence;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   MapMaker keyEquivalence(Equivalence<Object> equivalence) {
/* 122 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", this.keyEquivalence);
/* 123 */     this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/* 124 */     this.useCustomMap = true;
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getKeyEquivalence() {
/* 129 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
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
/*     */   @CanIgnoreReturnValue
/*     */   public MapMaker initialCapacity(int initialCapacity) {
/* 144 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", this.initialCapacity);
/*     */ 
/*     */ 
/*     */     
/* 148 */     Preconditions.checkArgument((initialCapacity >= 0));
/* 149 */     this.initialCapacity = initialCapacity;
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   int getInitialCapacity() {
/* 154 */     return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public MapMaker concurrencyLevel(int concurrencyLevel) {
/* 178 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", this.concurrencyLevel);
/*     */ 
/*     */ 
/*     */     
/* 182 */     Preconditions.checkArgument((concurrencyLevel > 0));
/* 183 */     this.concurrencyLevel = concurrencyLevel;
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   int getConcurrencyLevel() {
/* 188 */     return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public MapMaker weakKeys() {
/* 205 */     return setKeyStrength(MapMakerInternalMap.Strength.WEAK);
/*     */   }
/*     */   
/*     */   MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
/* 209 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", this.keyStrength);
/* 210 */     this.keyStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength);
/* 211 */     if (strength != MapMakerInternalMap.Strength.STRONG)
/*     */     {
/* 213 */       this.useCustomMap = true;
/*     */     }
/* 215 */     return this;
/*     */   }
/*     */   
/*     */   MapMakerInternalMap.Strength getKeyStrength() {
/* 219 */     return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public MapMaker weakValues() {
/* 241 */     return setValueStrength(MapMakerInternalMap.Strength.WEAK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum Dummy
/*     */   {
/* 251 */     VALUE;
/*     */   }
/*     */   
/*     */   MapMaker setValueStrength(MapMakerInternalMap.Strength strength) {
/* 255 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", this.valueStrength);
/* 256 */     this.valueStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength);
/* 257 */     if (strength != MapMakerInternalMap.Strength.STRONG)
/*     */     {
/* 259 */       this.useCustomMap = true;
/*     */     }
/* 261 */     return this;
/*     */   }
/*     */   
/*     */   MapMakerInternalMap.Strength getValueStrength() {
/* 265 */     return (MapMakerInternalMap.Strength)MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
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
/*     */   public <K, V> ConcurrentMap<K, V> makeMap() {
/* 280 */     if (!this.useCustomMap) {
/* 281 */       return new ConcurrentHashMap<>(getInitialCapacity(), 0.75F, getConcurrencyLevel());
/*     */     }
/* 283 */     return MapMakerInternalMap.create(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 292 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/* 293 */     if (this.initialCapacity != -1) {
/* 294 */       s.add("initialCapacity", this.initialCapacity);
/*     */     }
/* 296 */     if (this.concurrencyLevel != -1) {
/* 297 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*     */     }
/* 299 */     if (this.keyStrength != null) {
/* 300 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*     */     }
/* 302 */     if (this.valueStrength != null) {
/* 303 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*     */     }
/* 305 */     if (this.keyEquivalence != null) {
/* 306 */       s.addValue("keyEquivalence");
/*     */     }
/* 308 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\MapMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */