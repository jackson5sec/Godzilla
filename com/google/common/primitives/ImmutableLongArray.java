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
/*     */ import java.util.function.LongConsumer;
/*     */ import java.util.stream.LongStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ImmutableLongArray
/*     */   implements Serializable
/*     */ {
/*  92 */   private static final ImmutableLongArray EMPTY = new ImmutableLongArray(new long[0]);
/*     */   private final long[] array;
/*     */   
/*     */   public static ImmutableLongArray of() {
/*  96 */     return EMPTY;
/*     */   }
/*     */   private final transient int start; private final int end;
/*     */   
/*     */   public static ImmutableLongArray of(long e0) {
/* 101 */     return new ImmutableLongArray(new long[] { e0 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1) {
/* 106 */     return new ImmutableLongArray(new long[] { e0, e1 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2) {
/* 111 */     return new ImmutableLongArray(new long[] { e0, e1, e2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2, long e3) {
/* 116 */     return new ImmutableLongArray(new long[] { e0, e1, e2, e3 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2, long e3, long e4) {
/* 121 */     return new ImmutableLongArray(new long[] { e0, e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray of(long e0, long e1, long e2, long e3, long e4, long e5) {
/* 126 */     return new ImmutableLongArray(new long[] { e0, e1, e2, e3, e4, e5 });
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
/*     */   public static ImmutableLongArray of(long first, long... rest) {
/* 139 */     Preconditions.checkArgument((rest.length <= 2147483646), "the total number of elements must fit in an int");
/*     */     
/* 141 */     long[] array = new long[rest.length + 1];
/* 142 */     array[0] = first;
/* 143 */     System.arraycopy(rest, 0, array, 1, rest.length);
/* 144 */     return new ImmutableLongArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(long[] values) {
/* 149 */     return (values.length == 0) ? EMPTY : new ImmutableLongArray(
/*     */         
/* 151 */         Arrays.copyOf(values, values.length));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(Collection<Long> values) {
/* 156 */     return values.isEmpty() ? EMPTY : new ImmutableLongArray(Longs.toArray((Collection)values));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(Iterable<Long> values) {
/* 167 */     if (values instanceof Collection) {
/* 168 */       return copyOf((Collection<Long>)values);
/*     */     }
/* 170 */     return builder().addAll(values).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableLongArray copyOf(LongStream stream) {
/* 176 */     long[] array = stream.toArray();
/* 177 */     return (array.length == 0) ? EMPTY : new ImmutableLongArray(array);
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
/* 191 */     Preconditions.checkArgument((initialCapacity >= 0), "Invalid initialCapacity: %s", initialCapacity);
/* 192 */     return new Builder(initialCapacity);
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
/* 204 */     return new Builder(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static final class Builder
/*     */   {
/*     */     private long[] array;
/*     */     
/* 214 */     private int count = 0;
/*     */     
/*     */     Builder(int initialCapacity) {
/* 217 */       this.array = new long[initialCapacity];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(long value) {
/* 225 */       ensureRoomFor(1);
/* 226 */       this.array[this.count] = value;
/* 227 */       this.count++;
/* 228 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(long[] values) {
/* 236 */       ensureRoomFor(values.length);
/* 237 */       System.arraycopy(values, 0, this.array, this.count, values.length);
/* 238 */       this.count += values.length;
/* 239 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Iterable<Long> values) {
/* 247 */       if (values instanceof Collection) {
/* 248 */         return addAll((Collection<Long>)values);
/*     */       }
/* 250 */       for (Long value : values) {
/* 251 */         add(value.longValue());
/*     */       }
/* 253 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Collection<Long> values) {
/* 261 */       ensureRoomFor(values.size());
/* 262 */       for (Long value : values) {
/* 263 */         this.array[this.count++] = value.longValue();
/*     */       }
/* 265 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(LongStream stream) {
/* 273 */       Spliterator.OfLong spliterator = stream.spliterator();
/* 274 */       long size = spliterator.getExactSizeIfKnown();
/* 275 */       if (size > 0L) {
/* 276 */         ensureRoomFor(Ints.saturatedCast(size));
/*     */       }
/* 278 */       spliterator.forEachRemaining(this::add);
/* 279 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(ImmutableLongArray values) {
/* 287 */       ensureRoomFor(values.length());
/* 288 */       System.arraycopy(values.array, values.start, this.array, this.count, values.length());
/* 289 */       this.count += values.length();
/* 290 */       return this;
/*     */     }
/*     */     
/*     */     private void ensureRoomFor(int numberToAdd) {
/* 294 */       int newCount = this.count + numberToAdd;
/* 295 */       if (newCount > this.array.length) {
/* 296 */         long[] newArray = new long[expandedCapacity(this.array.length, newCount)];
/* 297 */         System.arraycopy(this.array, 0, newArray, 0, this.count);
/* 298 */         this.array = newArray;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 304 */       if (minCapacity < 0) {
/* 305 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 308 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 309 */       if (newCapacity < minCapacity) {
/* 310 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 312 */       if (newCapacity < 0) {
/* 313 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 315 */       return newCapacity;
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
/*     */     public ImmutableLongArray build() {
/* 328 */       return (this.count == 0) ? ImmutableLongArray.EMPTY : new ImmutableLongArray(this.array, 0, this.count);
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
/*     */   private ImmutableLongArray(long[] array) {
/* 349 */     this(array, 0, array.length);
/*     */   }
/*     */   
/*     */   private ImmutableLongArray(long[] array, int start, int end) {
/* 353 */     this.array = array;
/* 354 */     this.start = start;
/* 355 */     this.end = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 360 */     return this.end - this.start;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 365 */     return (this.end == this.start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long get(int index) {
/* 375 */     Preconditions.checkElementIndex(index, length());
/* 376 */     return this.array[this.start + index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(long target) {
/* 384 */     for (int i = this.start; i < this.end; i++) {
/* 385 */       if (this.array[i] == target) {
/* 386 */         return i - this.start;
/*     */       }
/*     */     } 
/* 389 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(long target) {
/* 397 */     for (int i = this.end - 1; i >= this.start; i--) {
/* 398 */       if (this.array[i] == target) {
/* 399 */         return i - this.start;
/*     */       }
/*     */     } 
/* 402 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(long target) {
/* 410 */     return (indexOf(target) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(LongConsumer consumer) {
/* 415 */     Preconditions.checkNotNull(consumer);
/* 416 */     for (int i = this.start; i < this.end; i++) {
/* 417 */       consumer.accept(this.array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LongStream stream() {
/* 423 */     return Arrays.stream(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */   
/*     */   public long[] toArray() {
/* 428 */     return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableLongArray subArray(int startIndex, int endIndex) {
/* 439 */     Preconditions.checkPositionIndexes(startIndex, endIndex, length());
/* 440 */     return (startIndex == endIndex) ? EMPTY : new ImmutableLongArray(this.array, this.start + startIndex, this.start + endIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Spliterator.OfLong spliterator() {
/* 446 */     return Spliterators.spliterator(this.array, this.start, this.end, 1040);
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
/*     */   public List<Long> asList() {
/* 462 */     return new AsList(this);
/*     */   }
/*     */   
/*     */   static class AsList extends AbstractList<Long> implements RandomAccess, Serializable {
/*     */     private final ImmutableLongArray parent;
/*     */     
/*     */     private AsList(ImmutableLongArray parent) {
/* 469 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 476 */       return this.parent.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Long get(int index) {
/* 481 */       return Long.valueOf(this.parent.get(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 486 */       return (indexOf(target) >= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 491 */       return (target instanceof Long) ? this.parent.indexOf(((Long)target).longValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 496 */       return (target instanceof Long) ? this.parent.lastIndexOf(((Long)target).longValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Long> subList(int fromIndex, int toIndex) {
/* 501 */       return this.parent.subArray(fromIndex, toIndex).asList();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Spliterator<Long> spliterator() {
/* 507 */       return this.parent.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 512 */       if (object instanceof AsList) {
/* 513 */         AsList asList = (AsList)object;
/* 514 */         return this.parent.equals(asList.parent);
/*     */       } 
/*     */       
/* 517 */       if (!(object instanceof List)) {
/* 518 */         return false;
/*     */       }
/* 520 */       List<?> that = (List)object;
/* 521 */       if (size() != that.size()) {
/* 522 */         return false;
/*     */       }
/* 524 */       int i = this.parent.start;
/*     */       
/* 526 */       for (Object element : that) {
/* 527 */         if (!(element instanceof Long) || this.parent.array[i++] != ((Long)element).longValue()) {
/* 528 */           return false;
/*     */         }
/*     */       } 
/* 531 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 537 */       return this.parent.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 542 */       return this.parent.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 552 */     if (object == this) {
/* 553 */       return true;
/*     */     }
/* 555 */     if (!(object instanceof ImmutableLongArray)) {
/* 556 */       return false;
/*     */     }
/* 558 */     ImmutableLongArray that = (ImmutableLongArray)object;
/* 559 */     if (length() != that.length()) {
/* 560 */       return false;
/*     */     }
/* 562 */     for (int i = 0; i < length(); i++) {
/* 563 */       if (get(i) != that.get(i)) {
/* 564 */         return false;
/*     */       }
/*     */     } 
/* 567 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 573 */     int hash = 1;
/* 574 */     for (int i = this.start; i < this.end; i++) {
/* 575 */       hash *= 31;
/* 576 */       hash += Longs.hashCode(this.array[i]);
/*     */     } 
/* 578 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 587 */     if (isEmpty()) {
/* 588 */       return "[]";
/*     */     }
/* 590 */     StringBuilder builder = new StringBuilder(length() * 5);
/* 591 */     builder.append('[').append(this.array[this.start]);
/*     */     
/* 593 */     for (int i = this.start + 1; i < this.end; i++) {
/* 594 */       builder.append(", ").append(this.array[i]);
/*     */     }
/* 596 */     builder.append(']');
/* 597 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableLongArray trimmed() {
/* 607 */     return isPartialView() ? new ImmutableLongArray(toArray()) : this;
/*     */   }
/*     */   
/*     */   private boolean isPartialView() {
/* 611 */     return (this.start > 0 || this.end < this.array.length);
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 615 */     return trimmed();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 619 */     return isEmpty() ? EMPTY : this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\ImmutableLongArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */