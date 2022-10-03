/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ class ImmutableMapEntry<K, V>
/*     */   extends ImmutableEntry<K, V>
/*     */ {
/*     */   static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int size) {
/*  43 */     return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[size];
/*     */   }
/*     */   
/*     */   ImmutableMapEntry(K key, V value) {
/*  47 */     super(key, value);
/*  48 */     CollectPreconditions.checkEntryNotNull(key, value);
/*     */   }
/*     */   
/*     */   ImmutableMapEntry(ImmutableMapEntry<K, V> contents) {
/*  52 */     super(contents.getKey(), contents.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableMapEntry<K, V> getNextInKeyBucket() {
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableMapEntry<K, V> getNextInValueBucket() {
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isReusable() {
/*  71 */     return true;
/*     */   }
/*     */   
/*     */   static class NonTerminalImmutableMapEntry<K, V> extends ImmutableMapEntry<K, V> {
/*     */     private final transient ImmutableMapEntry<K, V> nextInKeyBucket;
/*     */     
/*     */     NonTerminalImmutableMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket) {
/*  78 */       super(key, value);
/*  79 */       this.nextInKeyBucket = nextInKeyBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     final ImmutableMapEntry<K, V> getNextInKeyBucket() {
/*  84 */       return this.nextInKeyBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isReusable() {
/*  89 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class NonTerminalImmutableBiMapEntry<K, V>
/*     */     extends NonTerminalImmutableMapEntry<K, V>
/*     */   {
/*     */     private final transient ImmutableMapEntry<K, V> nextInValueBucket;
/*     */ 
/*     */     
/*     */     NonTerminalImmutableBiMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket, ImmutableMapEntry<K, V> nextInValueBucket) {
/* 102 */       super(key, value, nextInKeyBucket);
/* 103 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableMapEntry<K, V> getNextInValueBucket() {
/* 109 */       return this.nextInValueBucket;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableMapEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */