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
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ImmutableIntArray
/*     */   implements Serializable
/*     */ {
/*  92 */   private static final ImmutableIntArray EMPTY = new ImmutableIntArray(new int[0]);
/*     */   private final int[] array;
/*     */   
/*     */   public static ImmutableIntArray of() {
/*  96 */     return EMPTY;
/*     */   }
/*     */   private final transient int start; private final int end;
/*     */   
/*     */   public static ImmutableIntArray of(int e0) {
/* 101 */     return new ImmutableIntArray(new int[] { e0 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray of(int e0, int e1) {
/* 106 */     return new ImmutableIntArray(new int[] { e0, e1 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray of(int e0, int e1, int e2) {
/* 111 */     return new ImmutableIntArray(new int[] { e0, e1, e2 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray of(int e0, int e1, int e2, int e3) {
/* 116 */     return new ImmutableIntArray(new int[] { e0, e1, e2, e3 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray of(int e0, int e1, int e2, int e3, int e4) {
/* 121 */     return new ImmutableIntArray(new int[] { e0, e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray of(int e0, int e1, int e2, int e3, int e4, int e5) {
/* 126 */     return new ImmutableIntArray(new int[] { e0, e1, e2, e3, e4, e5 });
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
/*     */   public static ImmutableIntArray of(int first, int... rest) {
/* 139 */     Preconditions.checkArgument((rest.length <= 2147483646), "the total number of elements must fit in an int");
/*     */     
/* 141 */     int[] array = new int[rest.length + 1];
/* 142 */     array[0] = first;
/* 143 */     System.arraycopy(rest, 0, array, 1, rest.length);
/* 144 */     return new ImmutableIntArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray copyOf(int[] values) {
/* 149 */     return (values.length == 0) ? EMPTY : new ImmutableIntArray(Arrays.copyOf(values, values.length));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray copyOf(Collection<Integer> values) {
/* 154 */     return values.isEmpty() ? EMPTY : new ImmutableIntArray(Ints.toArray((Collection)values));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray copyOf(Iterable<Integer> values) {
/* 165 */     if (values instanceof Collection) {
/* 166 */       return copyOf((Collection<Integer>)values);
/*     */     }
/* 168 */     return builder().addAll(values).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableIntArray copyOf(IntStream stream) {
/* 174 */     int[] array = stream.toArray();
/* 175 */     return (array.length == 0) ? EMPTY : new ImmutableIntArray(array);
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
/* 189 */     Preconditions.checkArgument((initialCapacity >= 0), "Invalid initialCapacity: %s", initialCapacity);
/* 190 */     return new Builder(initialCapacity);
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
/* 202 */     return new Builder(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static final class Builder
/*     */   {
/*     */     private int[] array;
/*     */     
/* 212 */     private int count = 0;
/*     */     
/*     */     Builder(int initialCapacity) {
/* 215 */       this.array = new int[initialCapacity];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(int value) {
/* 223 */       ensureRoomFor(1);
/* 224 */       this.array[this.count] = value;
/* 225 */       this.count++;
/* 226 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(int[] values) {
/* 234 */       ensureRoomFor(values.length);
/* 235 */       System.arraycopy(values, 0, this.array, this.count, values.length);
/* 236 */       this.count += values.length;
/* 237 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Iterable<Integer> values) {
/* 245 */       if (values instanceof Collection) {
/* 246 */         return addAll((Collection<Integer>)values);
/*     */       }
/* 248 */       for (Integer value : values) {
/* 249 */         add(value.intValue());
/*     */       }
/* 251 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Collection<Integer> values) {
/* 259 */       ensureRoomFor(values.size());
/* 260 */       for (Integer value : values) {
/* 261 */         this.array[this.count++] = value.intValue();
/*     */       }
/* 263 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(IntStream stream) {
/* 271 */       Spliterator.OfInt spliterator = stream.spliterator();
/* 272 */       long size = spliterator.getExactSizeIfKnown();
/* 273 */       if (size > 0L) {
/* 274 */         ensureRoomFor(Ints.saturatedCast(size));
/*     */       }
/* 276 */       spliterator.forEachRemaining(this::add);
/* 277 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(ImmutableIntArray values) {
/* 285 */       ensureRoomFor(values.length());
/* 286 */       System.arraycopy(values.array, values.start, this.array, this.count, values.length());
/* 287 */       this.count += values.length();
/* 288 */       return this;
/*     */     }
/*     */     
/*     */     private void ensureRoomFor(int numberToAdd) {
/* 292 */       int newCount = this.count + numberToAdd;
/* 293 */       if (newCount > this.array.length) {
/* 294 */         int[] newArray = new int[expandedCapacity(this.array.length, newCount)];
/* 295 */         System.arraycopy(this.array, 0, newArray, 0, this.count);
/* 296 */         this.array = newArray;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 302 */       if (minCapacity < 0) {
/* 303 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 306 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 307 */       if (newCapacity < minCapacity) {
/* 308 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 310 */       if (newCapacity < 0) {
/* 311 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/* 313 */       return newCapacity;
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
/*     */     public ImmutableIntArray build() {
/* 326 */       return (this.count == 0) ? ImmutableIntArray.EMPTY : new ImmutableIntArray(this.array, 0, this.count);
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
/*     */   private ImmutableIntArray(int[] array) {
/* 347 */     this(array, 0, array.length);
/*     */   }
/*     */   
/*     */   private ImmutableIntArray(int[] array, int start, int end) {
/* 351 */     this.array = array;
/* 352 */     this.start = start;
/* 353 */     this.end = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 358 */     return this.end - this.start;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 363 */     return (this.end == this.start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(int index) {
/* 373 */     Preconditions.checkElementIndex(index, length());
/* 374 */     return this.array[this.start + index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(int target) {
/* 382 */     for (int i = this.start; i < this.end; i++) {
/* 383 */       if (this.array[i] == target) {
/* 384 */         return i - this.start;
/*     */       }
/*     */     } 
/* 387 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(int target) {
/* 395 */     for (int i = this.end - 1; i >= this.start; i--) {
/* 396 */       if (this.array[i] == target) {
/* 397 */         return i - this.start;
/*     */       }
/*     */     } 
/* 400 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(int target) {
/* 408 */     return (indexOf(target) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(IntConsumer consumer) {
/* 413 */     Preconditions.checkNotNull(consumer);
/* 414 */     for (int i = this.start; i < this.end; i++) {
/* 415 */       consumer.accept(this.array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IntStream stream() {
/* 421 */     return Arrays.stream(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] toArray() {
/* 426 */     return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableIntArray subArray(int startIndex, int endIndex) {
/* 437 */     Preconditions.checkPositionIndexes(startIndex, endIndex, length());
/* 438 */     return (startIndex == endIndex) ? EMPTY : new ImmutableIntArray(this.array, this.start + startIndex, this.start + endIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Spliterator.OfInt spliterator() {
/* 444 */     return Spliterators.spliterator(this.array, this.start, this.end, 1040);
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
/*     */   public List<Integer> asList() {
/* 460 */     return new AsList(this);
/*     */   }
/*     */   
/*     */   static class AsList extends AbstractList<Integer> implements RandomAccess, Serializable {
/*     */     private final ImmutableIntArray parent;
/*     */     
/*     */     private AsList(ImmutableIntArray parent) {
/* 467 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 474 */       return this.parent.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer get(int index) {
/* 479 */       return Integer.valueOf(this.parent.get(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 484 */       return (indexOf(target) >= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 489 */       return (target instanceof Integer) ? this.parent.indexOf(((Integer)target).intValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 494 */       return (target instanceof Integer) ? this.parent.lastIndexOf(((Integer)target).intValue()) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Integer> subList(int fromIndex, int toIndex) {
/* 499 */       return this.parent.subArray(fromIndex, toIndex).asList();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Spliterator<Integer> spliterator() {
/* 505 */       return this.parent.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 510 */       if (object instanceof AsList) {
/* 511 */         AsList asList = (AsList)object;
/* 512 */         return this.parent.equals(asList.parent);
/*     */       } 
/*     */       
/* 515 */       if (!(object instanceof List)) {
/* 516 */         return false;
/*     */       }
/* 518 */       List<?> that = (List)object;
/* 519 */       if (size() != that.size()) {
/* 520 */         return false;
/*     */       }
/* 522 */       int i = this.parent.start;
/*     */       
/* 524 */       for (Object element : that) {
/* 525 */         if (!(element instanceof Integer) || this.parent.array[i++] != ((Integer)element).intValue()) {
/* 526 */           return false;
/*     */         }
/*     */       } 
/* 529 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 535 */       return this.parent.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 540 */       return this.parent.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 550 */     if (object == this) {
/* 551 */       return true;
/*     */     }
/* 553 */     if (!(object instanceof ImmutableIntArray)) {
/* 554 */       return false;
/*     */     }
/* 556 */     ImmutableIntArray that = (ImmutableIntArray)object;
/* 557 */     if (length() != that.length()) {
/* 558 */       return false;
/*     */     }
/* 560 */     for (int i = 0; i < length(); i++) {
/* 561 */       if (get(i) != that.get(i)) {
/* 562 */         return false;
/*     */       }
/*     */     } 
/* 565 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 571 */     int hash = 1;
/* 572 */     for (int i = this.start; i < this.end; i++) {
/* 573 */       hash *= 31;
/* 574 */       hash += Ints.hashCode(this.array[i]);
/*     */     } 
/* 576 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 585 */     if (isEmpty()) {
/* 586 */       return "[]";
/*     */     }
/* 588 */     StringBuilder builder = new StringBuilder(length() * 5);
/* 589 */     builder.append('[').append(this.array[this.start]);
/*     */     
/* 591 */     for (int i = this.start + 1; i < this.end; i++) {
/* 592 */       builder.append(", ").append(this.array[i]);
/*     */     }
/* 594 */     builder.append(']');
/* 595 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableIntArray trimmed() {
/* 605 */     return isPartialView() ? new ImmutableIntArray(toArray()) : this;
/*     */   }
/*     */   
/*     */   private boolean isPartialView() {
/* 609 */     return (this.start > 0 || this.end < this.array.length);
/*     */   }
/*     */   
/*     */   Object writeReplace() {
/* 613 */     return trimmed();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 617 */     return isEmpty() ? EMPTY : this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\ImmutableIntArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */