/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class CacheBuilderSpec
/*     */ {
/*  89 */   private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
/*     */ 
/*     */   
/*  92 */   private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
/*     */ 
/*     */ 
/*     */   
/*  96 */   private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder()
/*  97 */     .put("initialCapacity", new InitialCapacityParser())
/*  98 */     .put("maximumSize", new MaximumSizeParser())
/*  99 */     .put("maximumWeight", new MaximumWeightParser())
/* 100 */     .put("concurrencyLevel", new ConcurrencyLevelParser())
/* 101 */     .put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK))
/* 102 */     .put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT))
/* 103 */     .put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK))
/* 104 */     .put("recordStats", new RecordStatsParser())
/* 105 */     .put("expireAfterAccess", new AccessDurationParser())
/* 106 */     .put("expireAfterWrite", new WriteDurationParser())
/* 107 */     .put("refreshAfterWrite", new RefreshDurationParser())
/* 108 */     .put("refreshInterval", new RefreshDurationParser())
/* 109 */     .build();
/*     */   
/*     */   @VisibleForTesting
/*     */   Integer initialCapacity;
/*     */   
/*     */   @VisibleForTesting
/*     */   Long maximumSize;
/*     */   @VisibleForTesting
/*     */   Long maximumWeight;
/*     */   @VisibleForTesting
/*     */   Integer concurrencyLevel;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength keyStrength;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength valueStrength;
/*     */   @VisibleForTesting
/*     */   Boolean recordStats;
/*     */   
/*     */   private CacheBuilderSpec(String specification) {
/* 128 */     this.specification = specification;
/*     */   }
/*     */   @VisibleForTesting
/*     */   long writeExpirationDuration; @VisibleForTesting
/*     */   TimeUnit writeExpirationTimeUnit;
/*     */   @VisibleForTesting
/*     */   long accessExpirationDuration;
/*     */   
/*     */   public static CacheBuilderSpec parse(String cacheBuilderSpecification) {
/* 137 */     CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
/* 138 */     if (!cacheBuilderSpecification.isEmpty()) {
/* 139 */       for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
/* 140 */         ImmutableList<String> immutableList = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
/* 141 */         Preconditions.checkArgument(!immutableList.isEmpty(), "blank key-value pair");
/* 142 */         Preconditions.checkArgument(
/* 143 */             (immutableList.size() <= 2), "key-value pair %s with more than one equals sign", keyValuePair);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 148 */         String key = immutableList.get(0);
/* 149 */         ValueParser valueParser = (ValueParser)VALUE_PARSERS.get(key);
/* 150 */         Preconditions.checkArgument((valueParser != null), "unknown key %s", key);
/*     */         
/* 152 */         String value = (immutableList.size() == 1) ? null : immutableList.get(1);
/* 153 */         valueParser.parse(spec, key, value);
/*     */       } 
/*     */     }
/*     */     
/* 157 */     return spec;
/*     */   }
/*     */   @VisibleForTesting
/*     */   TimeUnit accessExpirationTimeUnit; @VisibleForTesting
/*     */   long refreshDuration;
/*     */   public static CacheBuilderSpec disableCaching() {
/* 163 */     return parse("maximumSize=0");
/*     */   }
/*     */   @VisibleForTesting
/*     */   TimeUnit refreshTimeUnit; private final String specification;
/*     */   CacheBuilder<Object, Object> toCacheBuilder() {
/* 168 */     CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
/* 169 */     if (this.initialCapacity != null) {
/* 170 */       builder.initialCapacity(this.initialCapacity.intValue());
/*     */     }
/* 172 */     if (this.maximumSize != null) {
/* 173 */       builder.maximumSize(this.maximumSize.longValue());
/*     */     }
/* 175 */     if (this.maximumWeight != null) {
/* 176 */       builder.maximumWeight(this.maximumWeight.longValue());
/*     */     }
/* 178 */     if (this.concurrencyLevel != null) {
/* 179 */       builder.concurrencyLevel(this.concurrencyLevel.intValue());
/*     */     }
/* 181 */     if (this.keyStrength != null) {
/* 182 */       switch (this.keyStrength) {
/*     */         case WEAK:
/* 184 */           builder.weakKeys();
/*     */           break;
/*     */         default:
/* 187 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 190 */     if (this.valueStrength != null) {
/* 191 */       switch (this.valueStrength) {
/*     */         case SOFT:
/* 193 */           builder.softValues();
/*     */           break;
/*     */         case WEAK:
/* 196 */           builder.weakValues();
/*     */           break;
/*     */         default:
/* 199 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 202 */     if (this.recordStats != null && this.recordStats.booleanValue()) {
/* 203 */       builder.recordStats();
/*     */     }
/* 205 */     if (this.writeExpirationTimeUnit != null) {
/* 206 */       builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
/*     */     }
/* 208 */     if (this.accessExpirationTimeUnit != null) {
/* 209 */       builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
/*     */     }
/* 211 */     if (this.refreshTimeUnit != null) {
/* 212 */       builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
/*     */     }
/*     */     
/* 215 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toParsableString() {
/* 224 */     return this.specification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 238 */     return Objects.hashCode(new Object[] { this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 246 */           durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 247 */           durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 248 */           durationInNanos(this.refreshDuration, this.refreshTimeUnit) });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 253 */     if (this == obj) {
/* 254 */       return true;
/*     */     }
/* 256 */     if (!(obj instanceof CacheBuilderSpec)) {
/* 257 */       return false;
/*     */     }
/* 259 */     CacheBuilderSpec that = (CacheBuilderSpec)obj;
/* 260 */     return (Objects.equal(this.initialCapacity, that.initialCapacity) && 
/* 261 */       Objects.equal(this.maximumSize, that.maximumSize) && 
/* 262 */       Objects.equal(this.maximumWeight, that.maximumWeight) && 
/* 263 */       Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && 
/* 264 */       Objects.equal(this.keyStrength, that.keyStrength) && 
/* 265 */       Objects.equal(this.valueStrength, that.valueStrength) && 
/* 266 */       Objects.equal(this.recordStats, that.recordStats) && 
/* 267 */       Objects.equal(
/* 268 */         durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 269 */         durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && 
/* 270 */       Objects.equal(
/* 271 */         durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 272 */         durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && 
/* 273 */       Objects.equal(
/* 274 */         durationInNanos(this.refreshDuration, this.refreshTimeUnit), 
/* 275 */         durationInNanos(that.refreshDuration, that.refreshTimeUnit)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Long durationInNanos(long duration, TimeUnit unit) {
/* 283 */     return (unit == null) ? null : Long.valueOf(unit.toNanos(duration));
/*     */   }
/*     */   
/*     */   static abstract class IntegerParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseInteger(CacheBuilderSpec param1CacheBuilderSpec, int param1Int);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 292 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key);
/*     */       try {
/* 294 */         parseInteger(spec, Integer.parseInt(value));
/* 295 */       } catch (NumberFormatException e) {
/* 296 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 297 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class LongParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseLong(CacheBuilderSpec param1CacheBuilderSpec, long param1Long);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 308 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key);
/*     */       try {
/* 310 */         parseLong(spec, Long.parseLong(value));
/* 311 */       } catch (NumberFormatException e) {
/* 312 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 313 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class InitialCapacityParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 322 */       Preconditions.checkArgument((spec.initialCapacity == null), "initial capacity was already set to ", spec.initialCapacity);
/*     */ 
/*     */ 
/*     */       
/* 326 */       spec.initialCapacity = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumSizeParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 334 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", spec.maximumSize);
/* 335 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", spec.maximumWeight);
/*     */       
/* 337 */       spec.maximumSize = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumWeightParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 345 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", spec.maximumWeight);
/*     */       
/* 347 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", spec.maximumSize);
/* 348 */       spec.maximumWeight = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConcurrencyLevelParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 356 */       Preconditions.checkArgument((spec.concurrencyLevel == null), "concurrency level was already set to ", spec.concurrencyLevel);
/*     */ 
/*     */ 
/*     */       
/* 360 */       spec.concurrencyLevel = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class KeyStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public KeyStrengthParser(LocalCache.Strength strength) {
/* 369 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 374 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 375 */       Preconditions.checkArgument((spec.keyStrength == null), "%s was already set to %s", key, spec.keyStrength);
/* 376 */       spec.keyStrength = this.strength;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ValueStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public ValueStrengthParser(LocalCache.Strength strength) {
/* 385 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 390 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 391 */       Preconditions.checkArgument((spec.valueStrength == null), "%s was already set to %s", key, spec.valueStrength);
/*     */ 
/*     */       
/* 394 */       spec.valueStrength = this.strength;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class RecordStatsParser
/*     */     implements ValueParser
/*     */   {
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 403 */       Preconditions.checkArgument((value == null), "recordStats does not take values");
/* 404 */       Preconditions.checkArgument((spec.recordStats == null), "recordStats already set");
/* 405 */       spec.recordStats = Boolean.valueOf(true);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class DurationParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseDuration(CacheBuilderSpec param1CacheBuilderSpec, long param1Long, TimeUnit param1TimeUnit);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 415 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key); try {
/*     */         TimeUnit timeUnit;
/* 417 */         char lastChar = value.charAt(value.length() - 1);
/*     */         
/* 419 */         switch (lastChar) {
/*     */           case 'd':
/* 421 */             timeUnit = TimeUnit.DAYS;
/*     */             break;
/*     */           case 'h':
/* 424 */             timeUnit = TimeUnit.HOURS;
/*     */             break;
/*     */           case 'm':
/* 427 */             timeUnit = TimeUnit.MINUTES;
/*     */             break;
/*     */           case 's':
/* 430 */             timeUnit = TimeUnit.SECONDS;
/*     */             break;
/*     */           default:
/* 433 */             throw new IllegalArgumentException(CacheBuilderSpec
/* 434 */                 .format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", new Object[] { key, value }));
/*     */         } 
/*     */ 
/*     */         
/* 438 */         long duration = Long.parseLong(value.substring(0, value.length() - 1));
/* 439 */         parseDuration(spec, duration, timeUnit);
/* 440 */       } catch (NumberFormatException e) {
/* 441 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 442 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AccessDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 451 */       Preconditions.checkArgument((spec.accessExpirationTimeUnit == null), "expireAfterAccess already set");
/* 452 */       spec.accessExpirationDuration = duration;
/* 453 */       spec.accessExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class WriteDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 461 */       Preconditions.checkArgument((spec.writeExpirationTimeUnit == null), "expireAfterWrite already set");
/* 462 */       spec.writeExpirationDuration = duration;
/* 463 */       spec.writeExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class RefreshDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 471 */       Preconditions.checkArgument((spec.refreshTimeUnit == null), "refreshAfterWrite already set");
/* 472 */       spec.refreshDuration = duration;
/* 473 */       spec.refreshTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String format(String format, Object... args) {
/* 478 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */   
/*     */   private static interface ValueParser {
/*     */     void parse(CacheBuilderSpec param1CacheBuilderSpec, String param1String1, String param1String2);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\CacheBuilderSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */