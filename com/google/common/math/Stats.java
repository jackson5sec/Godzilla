/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ public final class Stats
/*     */   implements Serializable
/*     */ {
/*     */   private final long count;
/*     */   private final double mean;
/*     */   private final double sumOfSquaresOfDeltas;
/*     */   private final double min;
/*     */   private final double max;
/*     */   static final int BYTES = 40;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Stats(long count, double mean, double sumOfSquaresOfDeltas, double min, double max) {
/*  86 */     this.count = count;
/*  87 */     this.mean = mean;
/*  88 */     this.sumOfSquaresOfDeltas = sumOfSquaresOfDeltas;
/*  89 */     this.min = min;
/*  90 */     this.max = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterable<? extends Number> values) {
/* 100 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 101 */     accumulator.addAll(values);
/* 102 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterator<? extends Number> values) {
/* 112 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 113 */     accumulator.addAll(values);
/* 114 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(double... values) {
/* 123 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 124 */     acummulator.addAll(values);
/* 125 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(int... values) {
/* 134 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 135 */     acummulator.addAll(values);
/* 136 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(long... values) {
/* 146 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 147 */     acummulator.addAll(values);
/* 148 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */   
/*     */   public long count() {
/* 153 */     return this.count;
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
/*     */ 
/*     */   
/*     */   public double mean() {
/* 178 */     Preconditions.checkState((this.count != 0L));
/* 179 */     return this.mean;
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
/*     */   public double sum() {
/* 195 */     return this.mean * this.count;
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
/*     */   public double populationVariance() {
/* 214 */     Preconditions.checkState((this.count > 0L));
/* 215 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 216 */       return Double.NaN;
/*     */     }
/* 218 */     if (this.count == 1L) {
/* 219 */       return 0.0D;
/*     */     }
/* 221 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / count();
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
/*     */   public double populationStandardDeviation() {
/* 241 */     return Math.sqrt(populationVariance());
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
/*     */   public double sampleVariance() {
/* 261 */     Preconditions.checkState((this.count > 1L));
/* 262 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 263 */       return Double.NaN;
/*     */     }
/* 265 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public double sampleStandardDeviation() {
/* 287 */     return Math.sqrt(sampleVariance());
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
/*     */   public double min() {
/* 304 */     Preconditions.checkState((this.count != 0L));
/* 305 */     return this.min;
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
/*     */   public double max() {
/* 322 */     Preconditions.checkState((this.count != 0L));
/* 323 */     return this.max;
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
/*     */   public boolean equals(Object obj) {
/* 343 */     if (obj == null) {
/* 344 */       return false;
/*     */     }
/* 346 */     if (getClass() != obj.getClass()) {
/* 347 */       return false;
/*     */     }
/* 349 */     Stats other = (Stats)obj;
/* 350 */     return (this.count == other.count && 
/* 351 */       Double.doubleToLongBits(this.mean) == Double.doubleToLongBits(other.mean) && 
/* 352 */       Double.doubleToLongBits(this.sumOfSquaresOfDeltas) == Double.doubleToLongBits(other.sumOfSquaresOfDeltas) && 
/* 353 */       Double.doubleToLongBits(this.min) == Double.doubleToLongBits(other.min) && 
/* 354 */       Double.doubleToLongBits(this.max) == Double.doubleToLongBits(other.max));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 365 */     return Objects.hashCode(new Object[] { Long.valueOf(this.count), Double.valueOf(this.mean), Double.valueOf(this.sumOfSquaresOfDeltas), Double.valueOf(this.min), Double.valueOf(this.max) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 370 */     if (count() > 0L) {
/* 371 */       return MoreObjects.toStringHelper(this)
/* 372 */         .add("count", this.count)
/* 373 */         .add("mean", this.mean)
/* 374 */         .add("populationStandardDeviation", populationStandardDeviation())
/* 375 */         .add("min", this.min)
/* 376 */         .add("max", this.max)
/* 377 */         .toString();
/*     */     }
/* 379 */     return MoreObjects.toStringHelper(this).add("count", this.count).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 384 */     return this.sumOfSquaresOfDeltas;
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
/*     */   public static double meanOf(Iterable<? extends Number> values) {
/* 398 */     return meanOf(values.iterator());
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
/*     */   public static double meanOf(Iterator<? extends Number> values) {
/* 412 */     Preconditions.checkArgument(values.hasNext());
/* 413 */     long count = 1L;
/* 414 */     double mean = ((Number)values.next()).doubleValue();
/* 415 */     while (values.hasNext()) {
/* 416 */       double value = ((Number)values.next()).doubleValue();
/* 417 */       count++;
/* 418 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 420 */         mean += (value - mean) / count; continue;
/*     */       } 
/* 422 */       mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */     } 
/*     */     
/* 425 */     return mean;
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
/*     */   public static double meanOf(double... values) {
/* 438 */     Preconditions.checkArgument((values.length > 0));
/* 439 */     double mean = values[0];
/* 440 */     for (int index = 1; index < values.length; index++) {
/* 441 */       double value = values[index];
/* 442 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 444 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 446 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 449 */     return mean;
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
/*     */   public static double meanOf(int... values) {
/* 462 */     Preconditions.checkArgument((values.length > 0));
/* 463 */     double mean = values[0];
/* 464 */     for (int index = 1; index < values.length; index++) {
/* 465 */       double value = values[index];
/* 466 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 468 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 470 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 473 */     return mean;
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
/*     */   public static double meanOf(long... values) {
/* 487 */     Preconditions.checkArgument((values.length > 0));
/* 488 */     double mean = values[0];
/* 489 */     for (int index = 1; index < values.length; index++) {
/* 490 */       double value = values[index];
/* 491 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 493 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 495 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 498 */     return mean;
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
/*     */   public byte[] toByteArray() {
/* 513 */     ByteBuffer buff = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
/* 514 */     writeTo(buff);
/* 515 */     return buff.array();
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
/*     */   void writeTo(ByteBuffer buffer) {
/* 529 */     Preconditions.checkNotNull(buffer);
/* 530 */     Preconditions.checkArgument(
/* 531 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 534 */         .remaining());
/* 535 */     buffer
/* 536 */       .putLong(this.count)
/* 537 */       .putDouble(this.mean)
/* 538 */       .putDouble(this.sumOfSquaresOfDeltas)
/* 539 */       .putDouble(this.min)
/* 540 */       .putDouble(this.max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats fromByteArray(byte[] byteArray) {
/* 551 */     Preconditions.checkNotNull(byteArray);
/* 552 */     Preconditions.checkArgument((byteArray.length == 40), "Expected Stats.BYTES = %s remaining , got %s", 40, byteArray.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 557 */     return readFrom(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN));
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
/*     */   static Stats readFrom(ByteBuffer buffer) {
/* 571 */     Preconditions.checkNotNull(buffer);
/* 572 */     Preconditions.checkArgument(
/* 573 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 576 */         .remaining());
/* 577 */     return new Stats(buffer
/* 578 */         .getLong(), buffer
/* 579 */         .getDouble(), buffer
/* 580 */         .getDouble(), buffer
/* 581 */         .getDouble(), buffer
/* 582 */         .getDouble());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\Stats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */