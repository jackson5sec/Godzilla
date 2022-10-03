/*      */ package com.google.common.cache;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.common.base.Suppliers;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.errorprone.annotations.CheckReturnValue;
/*      */ import java.time.Duration;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class CacheBuilder<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*      */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*      */   private static final int DEFAULT_REFRESH_NANOS = 0;
/*      */   
/*  168 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*      */       {
/*      */         public void recordHits(int count) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordMisses(int count) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordLoadSuccess(long loadTime) {}
/*      */ 
/*      */ 
/*      */         
/*      */         public void recordLoadException(long loadTime) {}
/*      */ 
/*      */         
/*      */         public void recordEviction() {}
/*      */ 
/*      */         
/*      */         public CacheStats snapshot() {
/*  189 */           return CacheBuilder.EMPTY_STATS;
/*      */         }
/*      */       });
/*  192 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*      */   
/*  194 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>()
/*      */     {
/*      */       public AbstractCache.StatsCounter get()
/*      */       {
/*  198 */         return new AbstractCache.SimpleStatsCounter();
/*      */       }
/*      */     };
/*      */   
/*      */   enum NullListener implements RemovalListener<Object, Object> {
/*  203 */     INSTANCE;
/*      */     
/*      */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*      */   }
/*      */   
/*      */   enum OneWeigher
/*      */     implements Weigher<Object, Object> {
/*  210 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public int weigh(Object key, Object value) {
/*  214 */       return 1;
/*      */     }
/*      */   }
/*      */   
/*  218 */   static final Ticker NULL_TICKER = new Ticker()
/*      */     {
/*      */       public long read()
/*      */       {
/*  222 */         return 0L;
/*      */       }
/*      */     };
/*      */   
/*  226 */   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
/*      */   
/*      */   static final int UNSET_INT = -1;
/*      */   
/*      */   boolean strictParsing = true;
/*      */   
/*  232 */   int initialCapacity = -1;
/*  233 */   int concurrencyLevel = -1;
/*  234 */   long maximumSize = -1L;
/*  235 */   long maximumWeight = -1L;
/*      */   
/*      */   Weigher<? super K, ? super V> weigher;
/*      */   
/*      */   LocalCache.Strength keyStrength;
/*      */   LocalCache.Strength valueStrength;
/*  241 */   long expireAfterWriteNanos = -1L;
/*      */ 
/*      */   
/*  244 */   long expireAfterAccessNanos = -1L;
/*      */ 
/*      */   
/*  247 */   long refreshNanos = -1L;
/*      */   
/*      */   Equivalence<Object> keyEquivalence;
/*      */   
/*      */   Equivalence<Object> valueEquivalence;
/*      */   
/*      */   RemovalListener<? super K, ? super V> removalListener;
/*      */   
/*      */   Ticker ticker;
/*  256 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CacheBuilder<Object, Object> newBuilder() {
/*  268 */     return new CacheBuilder<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
/*  278 */     return spec.toCacheBuilder().lenientParsing();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static CacheBuilder<Object, Object> from(String spec) {
/*  290 */     return from(CacheBuilderSpec.parse(spec));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   CacheBuilder<K, V> lenientParsing() {
/*  300 */     this.strictParsing = false;
/*  301 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
/*  314 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", this.keyEquivalence);
/*  315 */     this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/*  316 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getKeyEquivalence() {
/*  320 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
/*  334 */     Preconditions.checkState((this.valueEquivalence == null), "value equivalence was already set to %s", this.valueEquivalence);
/*      */     
/*  336 */     this.valueEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/*  337 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getValueEquivalence() {
/*  341 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
/*  356 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", this.initialCapacity);
/*      */ 
/*      */ 
/*      */     
/*  360 */     Preconditions.checkArgument((initialCapacity >= 0));
/*  361 */     this.initialCapacity = initialCapacity;
/*  362 */     return this;
/*      */   }
/*      */   
/*      */   int getInitialCapacity() {
/*  366 */     return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
/*  401 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", this.concurrencyLevel);
/*      */ 
/*      */ 
/*      */     
/*  405 */     Preconditions.checkArgument((concurrencyLevel > 0));
/*  406 */     this.concurrencyLevel = concurrencyLevel;
/*  407 */     return this;
/*      */   }
/*      */   
/*      */   int getConcurrencyLevel() {
/*  411 */     return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CacheBuilder<K, V> maximumSize(long maximumSize) {
/*  436 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  438 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*      */ 
/*      */ 
/*      */     
/*  442 */     Preconditions.checkState((this.weigher == null), "maximum size can not be combined with weigher");
/*  443 */     Preconditions.checkArgument((maximumSize >= 0L), "maximum size must not be negative");
/*  444 */     this.maximumSize = maximumSize;
/*  445 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> maximumWeight(long maximumWeight) {
/*  477 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*      */ 
/*      */ 
/*      */     
/*  481 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  483 */     this.maximumWeight = maximumWeight;
/*  484 */     Preconditions.checkArgument((maximumWeight >= 0L), "maximum weight must not be negative");
/*  485 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
/*  520 */     Preconditions.checkState((this.weigher == null));
/*  521 */     if (this.strictParsing) {
/*  522 */       Preconditions.checkState((this.maximumSize == -1L), "weigher can not be combined with maximum size", this.maximumSize);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  530 */     CacheBuilder<K1, V1> me = this;
/*  531 */     me.weigher = (Weigher<? super K, ? super V>)Preconditions.checkNotNull(weigher);
/*  532 */     return me;
/*      */   }
/*      */   
/*      */   long getMaximumWeight() {
/*  536 */     if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
/*  537 */       return 0L;
/*      */     }
/*  539 */     return (this.weigher == null) ? this.maximumSize : this.maximumWeight;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
/*  545 */     return (Weigher<K1, V1>)MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> weakKeys() {
/*  566 */     return setKeyStrength(LocalCache.Strength.WEAK);
/*      */   }
/*      */   
/*      */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/*  570 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", this.keyStrength);
/*  571 */     this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/*  572 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getKeyStrength() {
/*  576 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> weakValues() {
/*  598 */     return setValueStrength(LocalCache.Strength.WEAK);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> softValues() {
/*  623 */     return setValueStrength(LocalCache.Strength.SOFT);
/*      */   }
/*      */   
/*      */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/*  627 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", this.valueStrength);
/*  628 */     this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/*  629 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getValueStrength() {
/*  633 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> expireAfterWrite(Duration duration) {
/*  660 */     return expireAfterWrite(duration.toNanos(), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
/*  687 */     Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
/*      */ 
/*      */ 
/*      */     
/*  691 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/*  692 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/*  693 */     return this;
/*      */   }
/*      */   
/*      */   long getExpireAfterWriteNanos() {
/*  697 */     return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> expireAfterAccess(Duration duration) {
/*  727 */     return expireAfterAccess(duration.toNanos(), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
/*  757 */     Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
/*      */ 
/*      */ 
/*      */     
/*  761 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/*  762 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/*  763 */     return this;
/*      */   }
/*      */   
/*      */   long getExpireAfterAccessNanos() {
/*  767 */     return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> refreshAfterWrite(Duration duration) {
/*  802 */     return refreshAfterWrite(duration.toNanos(), TimeUnit.NANOSECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
/*  837 */     Preconditions.checkNotNull(unit);
/*  838 */     Preconditions.checkState((this.refreshNanos == -1L), "refresh was already set to %s ns", this.refreshNanos);
/*  839 */     Preconditions.checkArgument((duration > 0L), "duration must be positive: %s %s", duration, unit);
/*  840 */     this.refreshNanos = unit.toNanos(duration);
/*  841 */     return this;
/*      */   }
/*      */   
/*      */   long getRefreshNanos() {
/*  845 */     return (this.refreshNanos == -1L) ? 0L : this.refreshNanos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CacheBuilder<K, V> ticker(Ticker ticker) {
/*  859 */     Preconditions.checkState((this.ticker == null));
/*  860 */     this.ticker = (Ticker)Preconditions.checkNotNull(ticker);
/*  861 */     return this;
/*      */   }
/*      */   
/*      */   Ticker getTicker(boolean recordsTime) {
/*  865 */     if (this.ticker != null) {
/*  866 */       return this.ticker;
/*      */     }
/*  868 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckReturnValue
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
/*  895 */     Preconditions.checkState((this.removalListener == null));
/*      */ 
/*      */ 
/*      */     
/*  899 */     CacheBuilder<K1, V1> me = this;
/*  900 */     me.removalListener = (RemovalListener<? super K, ? super V>)Preconditions.checkNotNull(listener);
/*  901 */     return me;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
/*  907 */     return 
/*  908 */       (RemovalListener<K1, V1>)MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CacheBuilder<K, V> recordStats() {
/*  921 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/*  922 */     return this;
/*      */   }
/*      */   
/*      */   boolean isRecordingStats() {
/*  926 */     return (this.statsCounterSupplier == CACHE_STATS_COUNTER);
/*      */   }
/*      */   
/*      */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
/*  930 */     return this.statsCounterSupplier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
/*  947 */     checkWeightWithWeigher();
/*  948 */     return new LocalCache.LocalLoadingCache<>(this, loader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
/*  964 */     checkWeightWithWeigher();
/*  965 */     checkNonLoadingCache();
/*  966 */     return new LocalCache.LocalManualCache<>(this);
/*      */   }
/*      */   
/*      */   private void checkNonLoadingCache() {
/*  970 */     Preconditions.checkState((this.refreshNanos == -1L), "refreshAfterWrite requires a LoadingCache");
/*      */   }
/*      */   
/*      */   private void checkWeightWithWeigher() {
/*  974 */     if (this.weigher == null) {
/*  975 */       Preconditions.checkState((this.maximumWeight == -1L), "maximumWeight requires weigher");
/*      */     }
/*  977 */     else if (this.strictParsing) {
/*  978 */       Preconditions.checkState((this.maximumWeight != -1L), "weigher requires maximumWeight");
/*      */     }
/*  980 */     else if (this.maximumWeight == -1L) {
/*  981 */       logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  993 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/*  994 */     if (this.initialCapacity != -1) {
/*  995 */       s.add("initialCapacity", this.initialCapacity);
/*      */     }
/*  997 */     if (this.concurrencyLevel != -1) {
/*  998 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*      */     }
/* 1000 */     if (this.maximumSize != -1L) {
/* 1001 */       s.add("maximumSize", this.maximumSize);
/*      */     }
/* 1003 */     if (this.maximumWeight != -1L) {
/* 1004 */       s.add("maximumWeight", this.maximumWeight);
/*      */     }
/* 1006 */     if (this.expireAfterWriteNanos != -1L) {
/* 1007 */       s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
/*      */     }
/* 1009 */     if (this.expireAfterAccessNanos != -1L) {
/* 1010 */       s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
/*      */     }
/* 1012 */     if (this.keyStrength != null) {
/* 1013 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*      */     }
/* 1015 */     if (this.valueStrength != null) {
/* 1016 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*      */     }
/* 1018 */     if (this.keyEquivalence != null) {
/* 1019 */       s.addValue("keyEquivalence");
/*      */     }
/* 1021 */     if (this.valueEquivalence != null) {
/* 1022 */       s.addValue("valueEquivalence");
/*      */     }
/* 1024 */     if (this.removalListener != null) {
/* 1025 */       s.addValue("removalListener");
/*      */     }
/* 1027 */     return s.toString();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\CacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */