/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class DiscreteDomain<C extends Comparable>
/*     */ {
/*     */   final boolean supportsFastOffset;
/*     */   
/*     */   public static DiscreteDomain<Integer> integers() {
/*  54 */     return IntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
/*  58 */     private static final IntegerDomain INSTANCE = new IntegerDomain();
/*     */     
/*     */     IntegerDomain() {
/*  61 */       super(true);
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Integer next(Integer value) {
/*  66 */       int i = value.intValue();
/*  67 */       return (i == Integer.MAX_VALUE) ? null : Integer.valueOf(i + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer previous(Integer value) {
/*  72 */       int i = value.intValue();
/*  73 */       return (i == Integer.MIN_VALUE) ? null : Integer.valueOf(i - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     Integer offset(Integer origin, long distance) {
/*  78 */       CollectPreconditions.checkNonnegative(distance, "distance");
/*  79 */       return Integer.valueOf(Ints.checkedCast(origin.longValue() + distance));
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(Integer start, Integer end) {
/*  84 */       return end.intValue() - start.intValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer minValue() {
/*  89 */       return Integer.valueOf(-2147483648);
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer maxValue() {
/*  94 */       return Integer.valueOf(2147483647);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/*  98 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 103 */       return "DiscreteDomain.integers()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiscreteDomain<Long> longs() {
/* 115 */     return LongDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
/* 119 */     private static final LongDomain INSTANCE = new LongDomain(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongDomain() {
/* 122 */       super(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public Long next(Long value) {
/* 127 */       long l = value.longValue();
/* 128 */       return (l == Long.MAX_VALUE) ? null : Long.valueOf(l + 1L);
/*     */     }
/*     */ 
/*     */     
/*     */     public Long previous(Long value) {
/* 133 */       long l = value.longValue();
/* 134 */       return (l == Long.MIN_VALUE) ? null : Long.valueOf(l - 1L);
/*     */     }
/*     */ 
/*     */     
/*     */     Long offset(Long origin, long distance) {
/* 139 */       CollectPreconditions.checkNonnegative(distance, "distance");
/* 140 */       long result = origin.longValue() + distance;
/* 141 */       if (result < 0L) {
/* 142 */         Preconditions.checkArgument((origin.longValue() < 0L), "overflow");
/*     */       }
/* 144 */       return Long.valueOf(result);
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(Long start, Long end) {
/* 149 */       long result = end.longValue() - start.longValue();
/* 150 */       if (end.longValue() > start.longValue() && result < 0L) {
/* 151 */         return Long.MAX_VALUE;
/*     */       }
/* 153 */       if (end.longValue() < start.longValue() && result > 0L) {
/* 154 */         return Long.MIN_VALUE;
/*     */       }
/* 156 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long minValue() {
/* 161 */       return Long.valueOf(Long.MIN_VALUE);
/*     */     }
/*     */ 
/*     */     
/*     */     public Long maxValue() {
/* 166 */       return Long.valueOf(Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 170 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 175 */       return "DiscreteDomain.longs()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiscreteDomain<BigInteger> bigIntegers() {
/* 187 */     return BigIntegerDomain.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BigIntegerDomain
/*     */     extends DiscreteDomain<BigInteger> implements Serializable {
/* 192 */     private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
/*     */     
/*     */     BigIntegerDomain() {
/* 195 */       super(true);
/*     */     }
/*     */     
/* 198 */     private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
/* 199 */     private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public BigInteger next(BigInteger value) {
/* 203 */       return value.add(BigInteger.ONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public BigInteger previous(BigInteger value) {
/* 208 */       return value.subtract(BigInteger.ONE);
/*     */     }
/*     */ 
/*     */     
/*     */     BigInteger offset(BigInteger origin, long distance) {
/* 213 */       CollectPreconditions.checkNonnegative(distance, "distance");
/* 214 */       return origin.add(BigInteger.valueOf(distance));
/*     */     }
/*     */ 
/*     */     
/*     */     public long distance(BigInteger start, BigInteger end) {
/* 219 */       return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 223 */       return INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 228 */       return "DiscreteDomain.bigIntegers()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DiscreteDomain() {
/* 238 */     this(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private DiscreteDomain(boolean supportsFastOffset) {
/* 243 */     this.supportsFastOffset = supportsFastOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   C offset(C origin, long distance) {
/* 251 */     CollectPreconditions.checkNonnegative(distance, "distance"); long i;
/* 252 */     for (i = 0L; i < distance; i++) {
/* 253 */       origin = next(origin);
/*     */     }
/* 255 */     return origin;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public C minValue() {
/* 304 */     throw new NoSuchElementException();
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
/*     */   public C maxValue() {
/* 320 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   public abstract C next(C paramC);
/*     */   
/*     */   public abstract C previous(C paramC);
/*     */   
/*     */   public abstract long distance(C paramC1, C paramC2);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\DiscreteDomain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */