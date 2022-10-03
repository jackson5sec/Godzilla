/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class TopKSelector<T>
/*     */ {
/*     */   private final int k;
/*     */   private final Comparator<? super T> comparator;
/*     */   private final T[] buffer;
/*     */   private int bufferSize;
/*     */   private T threshold;
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
/*  64 */     return least(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
/*  74 */     return new TopKSelector<>(comparator, k);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
/*  85 */     return greatest(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
/*  95 */     return new TopKSelector<>(Ordering.<T>from(comparator).reverse(), k);
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
/*     */   private TopKSelector(Comparator<? super T> comparator, int k) {
/* 116 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator, "comparator");
/* 117 */     this.k = k;
/* 118 */     Preconditions.checkArgument((k >= 0), "k must be nonnegative, was %s", k);
/* 119 */     this.buffer = (T[])new Object[k * 2];
/* 120 */     this.bufferSize = 0;
/* 121 */     this.threshold = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offer(T elem) {
/* 129 */     if (this.k == 0)
/*     */       return; 
/* 131 */     if (this.bufferSize == 0) {
/* 132 */       this.buffer[0] = elem;
/* 133 */       this.threshold = elem;
/* 134 */       this.bufferSize = 1;
/* 135 */     } else if (this.bufferSize < this.k) {
/* 136 */       this.buffer[this.bufferSize++] = elem;
/* 137 */       if (this.comparator.compare(elem, this.threshold) > 0) {
/* 138 */         this.threshold = elem;
/*     */       }
/* 140 */     } else if (this.comparator.compare(elem, this.threshold) < 0) {
/*     */       
/* 142 */       this.buffer[this.bufferSize++] = elem;
/* 143 */       if (this.bufferSize == 2 * this.k) {
/* 144 */         trim();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void trim() {
/* 154 */     int left = 0;
/* 155 */     int right = 2 * this.k - 1;
/*     */     
/* 157 */     int minThresholdPosition = 0;
/*     */ 
/*     */ 
/*     */     
/* 161 */     int iterations = 0;
/* 162 */     int maxIterations = IntMath.log2(right - left, RoundingMode.CEILING) * 3;
/* 163 */     while (left < right) {
/* 164 */       int pivotIndex = left + right + 1 >>> 1;
/*     */       
/* 166 */       int pivotNewIndex = partition(left, right, pivotIndex);
/*     */       
/* 168 */       if (pivotNewIndex > this.k) {
/* 169 */         right = pivotNewIndex - 1;
/* 170 */       } else if (pivotNewIndex < this.k) {
/* 171 */         left = Math.max(pivotNewIndex, left + 1);
/* 172 */         minThresholdPosition = pivotNewIndex;
/*     */       } else {
/*     */         break;
/*     */       } 
/* 176 */       iterations++;
/* 177 */       if (iterations >= maxIterations) {
/*     */         
/* 179 */         Arrays.sort(this.buffer, left, right, this.comparator);
/*     */         break;
/*     */       } 
/*     */     } 
/* 183 */     this.bufferSize = this.k;
/*     */     
/* 185 */     this.threshold = this.buffer[minThresholdPosition];
/* 186 */     for (int i = minThresholdPosition + 1; i < this.k; i++) {
/* 187 */       if (this.comparator.compare(this.buffer[i], this.threshold) > 0) {
/* 188 */         this.threshold = this.buffer[i];
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
/*     */   private int partition(int left, int right, int pivotIndex) {
/* 200 */     T pivotValue = this.buffer[pivotIndex];
/* 201 */     this.buffer[pivotIndex] = this.buffer[right];
/*     */     
/* 203 */     int pivotNewIndex = left;
/* 204 */     for (int i = left; i < right; i++) {
/* 205 */       if (this.comparator.compare(this.buffer[i], pivotValue) < 0) {
/* 206 */         swap(pivotNewIndex, i);
/* 207 */         pivotNewIndex++;
/*     */       } 
/*     */     } 
/* 210 */     this.buffer[right] = this.buffer[pivotNewIndex];
/* 211 */     this.buffer[pivotNewIndex] = pivotValue;
/* 212 */     return pivotNewIndex;
/*     */   }
/*     */   
/*     */   private void swap(int i, int j) {
/* 216 */     T tmp = this.buffer[i];
/* 217 */     this.buffer[i] = this.buffer[j];
/* 218 */     this.buffer[j] = tmp;
/*     */   }
/*     */   
/*     */   TopKSelector<T> combine(TopKSelector<T> other) {
/* 222 */     for (int i = 0; i < other.bufferSize; i++) {
/* 223 */       offer(other.buffer[i]);
/*     */     }
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offerAll(Iterable<? extends T> elements) {
/* 236 */     offerAll(elements.iterator());
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
/*     */   public void offerAll(Iterator<? extends T> elements) {
/* 248 */     while (elements.hasNext()) {
/* 249 */       offer(elements.next());
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
/*     */   public List<T> topK() {
/* 262 */     Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
/* 263 */     if (this.bufferSize > this.k) {
/* 264 */       Arrays.fill((Object[])this.buffer, this.k, this.buffer.length, (Object)null);
/* 265 */       this.bufferSize = this.k;
/* 266 */       this.threshold = this.buffer[this.k - 1];
/*     */     } 
/*     */     
/* 269 */     return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TopKSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */