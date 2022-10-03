/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Quantiles
/*     */ {
/*     */   public static ScaleAndIndex median() {
/* 135 */     return scale(2).index(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Scale quartiles() {
/* 140 */     return scale(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Scale percentiles() {
/* 145 */     return scale(100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scale scale(int scale) {
/* 155 */     return new Scale(scale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Scale
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Scale(int scale) {
/* 169 */       Preconditions.checkArgument((scale > 0), "Quantile scale must be positive");
/* 170 */       this.scale = scale;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndex index(int index) {
/* 179 */       return new Quantiles.ScaleAndIndex(this.scale, index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(int... indexes) {
/* 191 */       return new Quantiles.ScaleAndIndexes(this.scale, (int[])indexes.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(Collection<Integer> indexes) {
/* 203 */       return new Quantiles.ScaleAndIndexes(this.scale, Ints.toArray(indexes));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndex
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */     
/*     */     private final int index;
/*     */ 
/*     */ 
/*     */     
/*     */     private ScaleAndIndex(int scale, int index) {
/* 219 */       Quantiles.checkIndex(index, scale);
/* 220 */       this.scale = scale;
/* 221 */       this.index = index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(Collection<? extends Number> dataset) {
/* 233 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(double... dataset) {
/* 244 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(long... dataset) {
/* 256 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(int... dataset) {
/* 267 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double computeInPlace(double... dataset) {
/* 278 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 279 */       if (Quantiles.containsNaN(dataset)) {
/* 280 */         return Double.NaN;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 290 */       long numerator = this.index * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */       
/* 294 */       int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 295 */       int remainder = (int)(numerator - quotient * this.scale);
/* 296 */       Quantiles.selectInPlace(quotient, dataset, 0, dataset.length - 1);
/* 297 */       if (remainder == 0) {
/* 298 */         return dataset[quotient];
/*     */       }
/* 300 */       Quantiles.selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
/* 301 */       return Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndexes
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */     
/*     */     private final int[] indexes;
/*     */ 
/*     */ 
/*     */     
/*     */     private ScaleAndIndexes(int scale, int[] indexes) {
/* 318 */       for (int index : indexes) {
/* 319 */         Quantiles.checkIndex(index, scale);
/*     */       }
/* 321 */       this.scale = scale;
/* 322 */       this.indexes = indexes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(Collection<? extends Number> dataset) {
/* 335 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(double... dataset) {
/* 347 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(long... dataset) {
/* 360 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(int... dataset) {
/* 372 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> computeInPlace(double... dataset) {
/* 384 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 385 */       if (Quantiles.containsNaN(dataset)) {
/* 386 */         Map<Integer, Double> nanMap = new HashMap<>();
/* 387 */         for (int index : this.indexes) {
/* 388 */           nanMap.put(Integer.valueOf(index), Double.valueOf(Double.NaN));
/*     */         }
/* 390 */         return Collections.unmodifiableMap(nanMap);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 398 */       int[] quotients = new int[this.indexes.length];
/* 399 */       int[] remainders = new int[this.indexes.length];
/*     */       
/* 401 */       int[] requiredSelections = new int[this.indexes.length * 2];
/* 402 */       int requiredSelectionsCount = 0;
/* 403 */       for (int i = 0; i < this.indexes.length; i++) {
/*     */ 
/*     */         
/* 406 */         long numerator = this.indexes[i] * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 411 */         int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 412 */         int remainder = (int)(numerator - quotient * this.scale);
/* 413 */         quotients[i] = quotient;
/* 414 */         remainders[i] = remainder;
/* 415 */         requiredSelections[requiredSelectionsCount] = quotient;
/* 416 */         requiredSelectionsCount++;
/* 417 */         if (remainder != 0) {
/* 418 */           requiredSelections[requiredSelectionsCount] = quotient + 1;
/* 419 */           requiredSelectionsCount++;
/*     */         } 
/*     */       } 
/* 422 */       Arrays.sort(requiredSelections, 0, requiredSelectionsCount);
/* 423 */       Quantiles.selectAllInPlace(requiredSelections, 0, requiredSelectionsCount - 1, dataset, 0, dataset.length - 1);
/*     */       
/* 425 */       Map<Integer, Double> ret = new HashMap<>();
/* 426 */       for (int j = 0; j < this.indexes.length; j++) {
/* 427 */         int quotient = quotients[j];
/* 428 */         int remainder = remainders[j];
/* 429 */         if (remainder == 0) {
/* 430 */           ret.put(Integer.valueOf(this.indexes[j]), Double.valueOf(dataset[quotient]));
/*     */         } else {
/* 432 */           ret.put(
/* 433 */               Integer.valueOf(this.indexes[j]), Double.valueOf(Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale)));
/*     */         } 
/*     */       } 
/* 436 */       return Collections.unmodifiableMap(ret);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean containsNaN(double... dataset) {
/* 442 */     for (double value : dataset) {
/* 443 */       if (Double.isNaN(value)) {
/* 444 */         return true;
/*     */       }
/*     */     } 
/* 447 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double interpolate(double lower, double upper, double remainder, double scale) {
/* 456 */     if (lower == Double.NEGATIVE_INFINITY) {
/* 457 */       if (upper == Double.POSITIVE_INFINITY)
/*     */       {
/* 459 */         return Double.NaN;
/*     */       }
/*     */       
/* 462 */       return Double.NEGATIVE_INFINITY;
/*     */     } 
/* 464 */     if (upper == Double.POSITIVE_INFINITY)
/*     */     {
/* 466 */       return Double.POSITIVE_INFINITY;
/*     */     }
/* 468 */     return lower + (upper - lower) * remainder / scale;
/*     */   }
/*     */   
/*     */   private static void checkIndex(int index, int scale) {
/* 472 */     if (index < 0 || index > scale) {
/* 473 */       throw new IllegalArgumentException("Quantile indexes must be between 0 and the scale, which is " + scale);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static double[] longsToDoubles(long[] longs) {
/* 479 */     int len = longs.length;
/* 480 */     double[] doubles = new double[len];
/* 481 */     for (int i = 0; i < len; i++) {
/* 482 */       doubles[i] = longs[i];
/*     */     }
/* 484 */     return doubles;
/*     */   }
/*     */   
/*     */   private static double[] intsToDoubles(int[] ints) {
/* 488 */     int len = ints.length;
/* 489 */     double[] doubles = new double[len];
/* 490 */     for (int i = 0; i < len; i++) {
/* 491 */       doubles[i] = ints[i];
/*     */     }
/* 493 */     return doubles;
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
/*     */   private static void selectInPlace(int required, double[] array, int from, int to) {
/* 520 */     if (required == from) {
/* 521 */       int min = from;
/* 522 */       for (int index = from + 1; index <= to; index++) {
/* 523 */         if (array[min] > array[index]) {
/* 524 */           min = index;
/*     */         }
/*     */       } 
/* 527 */       if (min != from) {
/* 528 */         swap(array, min, from);
/*     */       }
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 535 */     while (to > from) {
/* 536 */       int partitionPoint = partition(array, from, to);
/* 537 */       if (partitionPoint >= required) {
/* 538 */         to = partitionPoint - 1;
/*     */       }
/* 540 */       if (partitionPoint <= required) {
/* 541 */         from = partitionPoint + 1;
/*     */       }
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
/*     */ 
/*     */   
/*     */   private static int partition(double[] array, int from, int to) {
/* 556 */     movePivotToStartOfSlice(array, from, to);
/* 557 */     double pivot = array[from];
/*     */ 
/*     */ 
/*     */     
/* 561 */     int partitionPoint = to;
/* 562 */     for (int i = to; i > from; i--) {
/* 563 */       if (array[i] > pivot) {
/* 564 */         swap(array, partitionPoint, i);
/* 565 */         partitionPoint--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 572 */     swap(array, from, partitionPoint);
/* 573 */     return partitionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void movePivotToStartOfSlice(double[] array, int from, int to) {
/* 583 */     int mid = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 588 */     boolean toLessThanMid = (array[to] < array[mid]);
/* 589 */     boolean midLessThanFrom = (array[mid] < array[from]);
/* 590 */     boolean toLessThanFrom = (array[to] < array[from]);
/* 591 */     if (toLessThanMid == midLessThanFrom) {
/*     */       
/* 593 */       swap(array, mid, from);
/* 594 */     } else if (toLessThanMid != toLessThanFrom) {
/*     */       
/* 596 */       swap(array, from, to);
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
/*     */   
/*     */   private static void selectAllInPlace(int[] allRequired, int requiredFrom, int requiredTo, double[] array, int from, int to) {
/* 609 */     int requiredChosen = chooseNextSelection(allRequired, requiredFrom, requiredTo, from, to);
/* 610 */     int required = allRequired[requiredChosen];
/*     */ 
/*     */     
/* 613 */     selectInPlace(required, array, from, to);
/*     */ 
/*     */     
/* 616 */     int requiredBelow = requiredChosen - 1;
/* 617 */     while (requiredBelow >= requiredFrom && allRequired[requiredBelow] == required) {
/* 618 */       requiredBelow--;
/*     */     }
/* 620 */     if (requiredBelow >= requiredFrom) {
/* 621 */       selectAllInPlace(allRequired, requiredFrom, requiredBelow, array, from, required - 1);
/*     */     }
/*     */ 
/*     */     
/* 625 */     int requiredAbove = requiredChosen + 1;
/* 626 */     while (requiredAbove <= requiredTo && allRequired[requiredAbove] == required) {
/* 627 */       requiredAbove++;
/*     */     }
/* 629 */     if (requiredAbove <= requiredTo) {
/* 630 */       selectAllInPlace(allRequired, requiredAbove, requiredTo, array, required + 1, to);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static int chooseNextSelection(int[] allRequired, int requiredFrom, int requiredTo, int from, int to) {
/* 645 */     if (requiredFrom == requiredTo) {
/* 646 */       return requiredFrom;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 651 */     int centerFloor = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 658 */     int low = requiredFrom;
/* 659 */     int high = requiredTo;
/* 660 */     while (high > low + 1) {
/* 661 */       int mid = low + high >>> 1;
/* 662 */       if (allRequired[mid] > centerFloor) {
/* 663 */         high = mid; continue;
/* 664 */       }  if (allRequired[mid] < centerFloor) {
/* 665 */         low = mid; continue;
/*     */       } 
/* 667 */       return mid;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 672 */     if (from + to - allRequired[low] - allRequired[high] > 0) {
/* 673 */       return high;
/*     */     }
/* 675 */     return low;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void swap(double[] array, int i, int j) {
/* 681 */     double temp = array[i];
/* 682 */     array[i] = array[j];
/* 683 */     array[j] = temp;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\math\Quantiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */