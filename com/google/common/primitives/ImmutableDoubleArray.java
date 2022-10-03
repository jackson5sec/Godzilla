/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.CheckReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.DoubleConsumer;
/*     */ import java.util.stream.DoubleStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible
/*     */ public final class ImmutableDoubleArray
/*     */   implements Serializable
/*     */ {
/*  92 */   private static final ImmutableDoubleArray EMPTY = new ImmutableDoubleArray(new double[0]);
/*     */   private final double[] array;
/*     */   
/*     */   public static ImmutableDoubleArray of() {
/*  96 */     return EMPTY;
/*     */   }
/*     */   private final transient int start; private final int end;
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0) {
/* 101 */     return new ImmutableDoubleArray(new double[] { e0 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1) {
/* 106 */     return new ImmutableDoubleArray(new double[] { e0, e1 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2) {
/* 111 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3) {
/* 116 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3, double e4) {
/* 121 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray of(double e0, double e1, double e2, double e3, double e4, double e5) {
/* 127 */     return new ImmutableDoubleArray(new double[] { e0, e1, e2, e3, e4, e5 });
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
/*     */   public static ImmutableDoubleArray of(double first, double... rest) {
/* 140 */     Preconditions.checkArgument((rest.length <= 2147483646), "the total number of elements must fit in an int");
/*     */     
/* 142 */     double[] array = new double[rest.length + 1];
/* 143 */     array[0] = first;
/* 144 */     System.arraycopy(rest, 0, array, 1, rest.length);
/* 145 */     return new ImmutableDoubleArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(double[] values) {
/* 150 */     return (values.length == 0) ? EMPTY : new ImmutableDoubleArray(
/*     */         
/* 152 */         Arrays.copyOf(values, values.length));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(Collection<Double> values) {
/* 157 */     return values.isEmpty() ? EMPTY : new ImmutableDoubleArray(Doubles.toArray((Collection)values));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(Iterable<Double> values) {
/* 168 */     if (values instanceof Collection) {
/* 169 */       return copyOf((Collection<Double>)values);
/*     */     }
/* 171 */     return builder().addAll(values).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableDoubleArray copyOf(DoubleStream stream) {
/* 177 */     double[] array = stream.toArray();
/* 178 */     return (array.length == 0) ? EMPTY : new ImmutableDoubleArray(array);
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
/*     */   public static Builder builder(int initialCapacity) {
/* 192 */     Preconditions.checkArgument((initialCapacity >= 0), "Invalid initialCapacity: %s", initialCapacity);
/* 193 */     return new Builder(initialCapacity);
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
/*     */   public static Builder builder() {
/* 205 */     return new Builder(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static final class Builder
/*     */   {
/*     */     private double[] array;
/*     */     
/* 215 */     private int count = 0;
/*     */     
/*     */     Builder(int initialCapacity) {
/* 218 */       this.array = new double[initialCapacity];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(double value) {
/* 226 */       ensureRoomFor(1);
/* 227 */       this.array[this.count] = value;
/* 228 */       this.count++;
/* 229 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(double[] values) {
/* 237 */       ensureRoomFor(values.length);
/* 238 */       System.arraycopy(values, 0, this.array, this.count, values.length);
/* 239 */       this.count += values.length;
/* 240 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Iterable<Double> values) {
/* 248 */       if (values instanceof Collection) {
/* 249 */         return addAll((Collection<Double>)values);
/*     */       }
/* 251 */       for (Double value : values) {
/* 252 */         add(value.doubleValue());
/*     */       }
/* 254 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Collection<Double> values) {
/* 262 */       ensureRoomFor(values.size());
/* 263 */       for (Double value : values) {
/* 264 */         this.array[this.count++] = value.doubleValue();
/*     */       }
/* 266 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(DoubleStream stream) {
/* 274 */       Spliterator.OfDouble spliterator = stream.spliterator();
/* 275 */       long size = spliterator.getExactSizeIfKnown();
/* 276 */       if (size > 0L) {
/* 277 */         ensureRoomFor(Ints.saturatedCast(size));
/*     */       }
/* 279 */       spliterator.forEachRemaining(this::add);
/* 280 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(ImmutableDoubleArray values) {
/* 288 */       ensureRoomFor(values.length());
/* 289 */       System.arraycopy(values.array, values.start, this.array, this.count, values.length());
/* 290 */       this.count += values.length();
/* 291 */       return this;
/*     */     }
/*     */     
/*     */     private void ensureRoomFor(int numberToAdd) {
/* 295 */       int newCount = this.count + numberToAdd;
/* 296 */       if (newCount > this.array.length) {
/* 297 */         double[] newArray = new double[expandedCapacity(this.array.length, newCount)];
/* 298 */         System.arraycopy(this.array, 0, newArray, 0, this.count);
/* 299 */         this.array = newArray;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 305 */       if (minCapacity < 0) {
/* 306 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 309 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 310 */       if (newCapacity < minCapacity) {
/* 311 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 313 */       if (newCapacity < 0) {
/* 314 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 316 */       return newCapacity;
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
/*     */     @CheckReturnValue
/*     */     public ImmutableDoubleArray build() {
/* 329 */       return (this.count == 0) ? ImmutableDoubleArray.EMPTY : new ImmutableDoubleArray(this.array, 0, this.count);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableDoubleArray(double[] array) {
/* 350 */     this(array, 0, array.length);
/*     */   }
/*     */   
/*     */   private ImmutableDoubleArray(double[] array, int start, int end) {
/* 354 */     this.array = array;
/* 355 */     this.start = start;
/* 356 */     this.end = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 361 */     return this.end - this.start;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 366 */     return (this.end == this.start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double get(int index) {
/* 376 */     Preconditions.checkElementIndex(index, length());
/* 377 */     return this.array[this.start + index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(double target) {
/* 386 */     for (int i = this.start; i < this.end; i++) {
/* 387 */       if (areEqual(this.array[i], target)) {
/* 388 */         return i - this.start;
/*     */       }
/*     */     } 
/* 391 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(double target) {
/* 400 */     for (int i = this.end - 1; i >= this.start; i--) {
/* 401 */       if (areEqual(this.array[i], target)) {
/* 402 */         return i - this.start;
/*     */       }
/*     */     } 
/* 405 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(double target) {
/* 413 */     return (indexOf(target) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(DoubleConsumer consumer) {
/* 418 */     Preconditions.checkNotNull(consumer);
/* 419 */     for (int i = this.start; i < this.end; i++) {
/* 420 */       consumer.accept(this.array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleStream stream() {
/* 426 */     return Arrays.stream(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */   
/*     */   public double[] toArray() {
/* 431 */     return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableDoubleArray subArray(int startIndex, int endIndex) {
/* 442 */     Preconditions.checkPositionIndexes(startIndex, endIndex, length());
/* 443 */     return (startIndex == endIndex) ? EMPTY : new ImmutableDoubleArray(this.array, this.start + startIndex, this.start + endIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Spliterator.OfDouble spliterator() {
/* 449 */     return Spliterators.spliterator(this.array, this.start, this.end, 1040);
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
/*     */   public List<Double> asList() {
/* 465 */     return new AsList(this);
/*     */   }
/*     */   
/*     */   static class AsList extends AbstractList<Double> implements RandomAccess, Serializable {
/*     */     private final ImmutableDoubleArray parent;
/*     */     
/*     */     private AsList(ImmutableDoubleArray parent) {
/* 472 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 479 */       return this.parent.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Double get(int index) {
/* 484 */       return Double.valueOf(this.parent.get(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 489 */       return (indexOf(target) >= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 494 */       return (target instanceof Double) ? this.parent.indexOf(((Double)target).doubleValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 499 */       return (target instanceof Double) ? this.parent.lastIndexOf(((Double)target).doubleValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Double> subList(int fromIndex, int toIndex) {
/* 504 */       return this.parent.subArray(fromIndex, toIndex).asList();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Spliterator<Double> spliterator() {
/* 510 */       return this.parent.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 515 */       if (object instanceof AsList) {
/* 516 */         AsList asList = (AsList)object;
/* 517 */         return this.parent.equals(asList.parent);
/*     */       } 
/*     */       
/* 520 */       if (!(object instanceof List)) {
/* 521 */         return false;
/*     */       }
/* 523 */       List<?> that = (List)object;
/* 524 */       if (size() != that.size()) {
/* 525 */         return false;
/*     */       }
/* 527 */       int i = this.parent.start;
/*     */       
/* 529 */       for (Object element : that) {
/* 530 */         if (!(element instanceof Double) || !ImmutableDoubleArray.areEqual(this.parent.array[i++], ((Double)element).doubleValue())) {
/* 531 */           return false;
/*     */         }
/*     */       } 
/* 534 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 540 */       return this.parent.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 545 */       return this.parent.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 555 */     if (object == this) {
/* 556 */       return true;
/*     */     }
/* 558 */     if (!(object instanceof ImmutableDoubleArray)) {
/* 559 */       return false;
/*     */     }
/* 561 */     ImmutableDoubleArray that = (ImmutableDoubleArray)object;
/* 562 */     if (length() != that.length()) {
/* 563 */       return false;
/*     */     }
/* 565 */     for (int i = 0; i < length(); i++) {
/* 566 */       if (!areEqual(get(i), that.get(i))) {
/* 567 */         return false;
/*     */       }
/*     */     } 
/* 570 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean areEqual(double a, double b) {
/* 575 */     return (Double.doubleToLongBits(a) == Double.doubleToLongBits(b));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 581 */     int hash = 1;
/* 582 */     for (int i = this.start; i < this.end; i++) {
/* 583 */       hash *= 31;
/* 584 */       hash += Doubles.hashCode(this.array[i]);
/*     */     } 
/* 586 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 595 */     if (isEmpty()) {
/* 596 */       return "[]";
/*     */     }
/* 598 */     StringBuilder builder = new StringBuilder(length() * 5);
/* 599 */     builder.append('[').append(this.array[this.start]);
/*     */     
/* 601 */     for (int i = this.start + 1; i < this.end; i++) {
/* 602 */       builder.append(", ").append(this.array[i]);
/*     */     }
/* 604 */     builder.append(']');
/* 605 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableDoubleArray trimmed() {
/* 615 */     return isPartialView() ? new ImmutableDoubleArray(toArray()) : this;
/*     */   }
/*     */   
/*     */   private boolean isPartialView() {
/* 619 */     return (this.start > 0 || this.end < this.array.length);
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 623 */     return trimmed();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 627 */     return isEmpty() ? EMPTY : this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\ImmutableDoubleArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */