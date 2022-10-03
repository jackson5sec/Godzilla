/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.AbstractQueue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible
/*     */ public final class MinMaxPriorityQueue<E>
/*     */   extends AbstractQueue<E>
/*     */ {
/*     */   private final Heap minHeap;
/*     */   private final Heap maxHeap;
/*     */   @VisibleForTesting
/*     */   final int maximumSize;
/*     */   private Object[] queue;
/*     */   private int size;
/*     */   private int modCount;
/*     */   private static final int EVEN_POWERS_OF_TWO = 1431655765;
/*     */   private static final int ODD_POWERS_OF_TWO = -1431655766;
/*     */   private static final int DEFAULT_CAPACITY = 11;
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create() {
/* 109 */     return (new Builder(Ordering.natural())).create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> initialContents) {
/* 118 */     return (new Builder(Ordering.natural())).create(initialContents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> orderedBy(Comparator<B> comparator) {
/* 126 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> expectedSize(int expectedSize) {
/* 134 */     return (new Builder<>(Ordering.natural())).expectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder<Comparable> maximumSize(int maximumSize) {
/* 144 */     return (new Builder<>(Ordering.natural())).maximumSize(maximumSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class Builder<B>
/*     */   {
/*     */     private static final int UNSET_EXPECTED_SIZE = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Comparator<B> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     private int expectedSize = -1;
/* 167 */     private int maximumSize = Integer.MAX_VALUE;
/*     */     
/*     */     private Builder(Comparator<B> comparator) {
/* 170 */       this.comparator = (Comparator<B>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> expectedSize(int expectedSize) {
/* 179 */       Preconditions.checkArgument((expectedSize >= 0));
/* 180 */       this.expectedSize = expectedSize;
/* 181 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<B> maximumSize(int maximumSize) {
/* 192 */       Preconditions.checkArgument((maximumSize > 0));
/* 193 */       this.maximumSize = maximumSize;
/* 194 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create() {
/* 202 */       return create(Collections.emptySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> initialContents) {
/* 212 */       MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue<>(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, initialContents));
/* 213 */       for (T element : initialContents) {
/* 214 */         queue.offer(element);
/*     */       }
/* 216 */       return queue;
/*     */     }
/*     */ 
/*     */     
/*     */     private <T extends B> Ordering<T> ordering() {
/* 221 */       return Ordering.from(this.comparator);
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
/*     */   private MinMaxPriorityQueue(Builder<? super E> builder, int queueSize) {
/* 233 */     Ordering<E> ordering = builder.ordering();
/* 234 */     this.minHeap = new Heap(ordering);
/* 235 */     this.maxHeap = new Heap(ordering.reverse());
/* 236 */     this.minHeap.otherHeap = this.maxHeap;
/* 237 */     this.maxHeap.otherHeap = this.minHeap;
/*     */     
/* 239 */     this.maximumSize = builder.maximumSize;
/*     */     
/* 241 */     this.queue = new Object[queueSize];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 246 */     return this.size;
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E element) {
/* 259 */     offer(element);
/* 260 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> newElements) {
/* 266 */     boolean modified = false;
/* 267 */     for (E element : newElements) {
/* 268 */       offer(element);
/* 269 */       modified = true;
/*     */     } 
/* 271 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E element) {
/* 282 */     Preconditions.checkNotNull(element);
/* 283 */     this.modCount++;
/* 284 */     int insertIndex = this.size++;
/*     */     
/* 286 */     growIfNeeded();
/*     */ 
/*     */ 
/*     */     
/* 290 */     heapForIndex(insertIndex).bubbleUp(insertIndex, element);
/* 291 */     return (this.size <= this.maximumSize || pollLast() != element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E poll() {
/* 297 */     return isEmpty() ? null : removeAndGet(0);
/*     */   }
/*     */ 
/*     */   
/*     */   E elementData(int index) {
/* 302 */     return (E)this.queue[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 307 */     return isEmpty() ? null : elementData(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getMaxElementIndex() {
/* 312 */     switch (this.size) {
/*     */       case 1:
/* 314 */         return 0;
/*     */       case 2:
/* 316 */         return 1;
/*     */     } 
/*     */ 
/*     */     
/* 320 */     return (this.maxHeap.compareElements(1, 2) <= 0) ? 1 : 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst() {
/* 330 */     return poll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst() {
/* 340 */     return remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/* 348 */     return peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast() {
/* 357 */     return isEmpty() ? null : removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast() {
/* 367 */     if (isEmpty()) {
/* 368 */       throw new NoSuchElementException();
/*     */     }
/* 370 */     return removeAndGet(getMaxElementIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peekLast() {
/* 378 */     return isEmpty() ? null : elementData(getMaxElementIndex());
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
/*     */   @VisibleForTesting
/*     */   @CanIgnoreReturnValue
/*     */   MoveDesc<E> removeAt(int index) {
/* 397 */     Preconditions.checkPositionIndex(index, this.size);
/* 398 */     this.modCount++;
/* 399 */     this.size--;
/* 400 */     if (this.size == index) {
/* 401 */       this.queue[this.size] = null;
/* 402 */       return null;
/*     */     } 
/* 404 */     E actualLastElement = elementData(this.size);
/* 405 */     int lastElementAt = heapForIndex(this.size).swapWithConceptuallyLastElement(actualLastElement);
/* 406 */     if (lastElementAt == index) {
/*     */ 
/*     */ 
/*     */       
/* 410 */       this.queue[this.size] = null;
/* 411 */       return null;
/*     */     } 
/* 413 */     E toTrickle = elementData(this.size);
/* 414 */     this.queue[this.size] = null;
/* 415 */     MoveDesc<E> changes = fillHole(index, toTrickle);
/* 416 */     if (lastElementAt < index) {
/*     */       
/* 418 */       if (changes == null)
/*     */       {
/* 420 */         return new MoveDesc<>(actualLastElement, toTrickle);
/*     */       }
/*     */ 
/*     */       
/* 424 */       return new MoveDesc<>(actualLastElement, changes.replaced);
/*     */     } 
/*     */ 
/*     */     
/* 428 */     return changes;
/*     */   }
/*     */   
/*     */   private MoveDesc<E> fillHole(int index, E toTrickle) {
/* 432 */     Heap heap = heapForIndex(index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 440 */     int vacated = heap.fillHoleAt(index);
/*     */     
/* 442 */     int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
/* 443 */     if (bubbledTo == vacated)
/*     */     {
/*     */ 
/*     */       
/* 447 */       return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
/*     */     }
/* 449 */     return (bubbledTo < index) ? new MoveDesc<>(toTrickle, elementData(index)) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   static class MoveDesc<E>
/*     */   {
/*     */     final E toTrickle;
/*     */     final E replaced;
/*     */     
/*     */     MoveDesc(E toTrickle, E replaced) {
/* 459 */       this.toTrickle = toTrickle;
/* 460 */       this.replaced = replaced;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private E removeAndGet(int index) {
/* 466 */     E value = elementData(index);
/* 467 */     removeAt(index);
/* 468 */     return value;
/*     */   }
/*     */   
/*     */   private Heap heapForIndex(int i) {
/* 472 */     return isEvenLevel(i) ? this.minHeap : this.maxHeap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isEvenLevel(int index) {
/* 480 */     int oneBased = index + 1 ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/* 481 */     Preconditions.checkState((oneBased > 0), "negative index");
/* 482 */     return ((oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isIntact() {
/* 492 */     for (int i = 1; i < this.size; i++) {
/* 493 */       if (!heapForIndex(i).verifyIndex(i)) {
/* 494 */         return false;
/*     */       }
/*     */     } 
/* 497 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Heap
/*     */   {
/*     */     final Ordering<E> ordering;
/*     */     
/*     */     @Weak
/*     */     Heap otherHeap;
/*     */ 
/*     */     
/*     */     Heap(Ordering<E> ordering) {
/* 511 */       this.ordering = ordering;
/*     */     }
/*     */     
/*     */     int compareElements(int a, int b) {
/* 515 */       return this.ordering.compare(MinMaxPriorityQueue.this.elementData(a), MinMaxPriorityQueue.this.elementData(b));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MinMaxPriorityQueue.MoveDesc<E> tryCrossOverAndBubbleUp(int removeIndex, int vacated, E toTrickle) {
/*     */       E parent;
/* 523 */       int crossOver = crossOver(vacated, toTrickle);
/* 524 */       if (crossOver == vacated) {
/* 525 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 533 */       if (crossOver < removeIndex) {
/*     */ 
/*     */         
/* 536 */         parent = MinMaxPriorityQueue.this.elementData(removeIndex);
/*     */       } else {
/* 538 */         parent = MinMaxPriorityQueue.this.elementData(getParentIndex(removeIndex));
/*     */       } 
/*     */       
/* 541 */       if (this.otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex) {
/* 542 */         return new MinMaxPriorityQueue.MoveDesc<>(toTrickle, parent);
/*     */       }
/* 544 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     void bubbleUp(int index, E x) {
/*     */       Heap heap;
/* 550 */       int crossOver = crossOverUp(index, x);
/*     */ 
/*     */       
/* 553 */       if (crossOver == index) {
/* 554 */         heap = this;
/*     */       } else {
/* 556 */         index = crossOver;
/* 557 */         heap = this.otherHeap;
/*     */       } 
/* 559 */       heap.bubbleUpAlternatingLevels(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     int bubbleUpAlternatingLevels(int index, E x) {
/* 568 */       while (index > 2) {
/* 569 */         int grandParentIndex = getGrandparentIndex(index);
/* 570 */         E e = MinMaxPriorityQueue.this.elementData(grandParentIndex);
/* 571 */         if (this.ordering.compare(e, x) <= 0) {
/*     */           break;
/*     */         }
/* 574 */         MinMaxPriorityQueue.this.queue[index] = e;
/* 575 */         index = grandParentIndex;
/*     */       } 
/* 577 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 578 */       return index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int findMin(int index, int len) {
/* 586 */       if (index >= MinMaxPriorityQueue.this.size) {
/* 587 */         return -1;
/*     */       }
/* 589 */       Preconditions.checkState((index > 0));
/* 590 */       int limit = Math.min(index, MinMaxPriorityQueue.this.size - len) + len;
/* 591 */       int minIndex = index;
/* 592 */       for (int i = index + 1; i < limit; i++) {
/* 593 */         if (compareElements(i, minIndex) < 0) {
/* 594 */           minIndex = i;
/*     */         }
/*     */       } 
/* 597 */       return minIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     int findMinChild(int index) {
/* 602 */       return findMin(getLeftChildIndex(index), 2);
/*     */     }
/*     */ 
/*     */     
/*     */     int findMinGrandChild(int index) {
/* 607 */       int leftChildIndex = getLeftChildIndex(index);
/* 608 */       if (leftChildIndex < 0) {
/* 609 */         return -1;
/*     */       }
/* 611 */       return findMin(getLeftChildIndex(leftChildIndex), 4);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOverUp(int index, E x) {
/* 619 */       if (index == 0) {
/* 620 */         MinMaxPriorityQueue.this.queue[0] = x;
/* 621 */         return 0;
/*     */       } 
/* 623 */       int parentIndex = getParentIndex(index);
/* 624 */       E parentElement = MinMaxPriorityQueue.this.elementData(parentIndex);
/* 625 */       if (parentIndex != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 630 */         int grandparentIndex = getParentIndex(parentIndex);
/* 631 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 632 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/* 633 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 634 */           if (this.ordering.compare(uncleElement, parentElement) < 0) {
/* 635 */             parentIndex = uncleIndex;
/* 636 */             parentElement = uncleElement;
/*     */           } 
/*     */         } 
/*     */       } 
/* 640 */       if (this.ordering.compare(parentElement, x) < 0) {
/* 641 */         MinMaxPriorityQueue.this.queue[index] = parentElement;
/* 642 */         MinMaxPriorityQueue.this.queue[parentIndex] = x;
/* 643 */         return parentIndex;
/*     */       } 
/* 645 */       MinMaxPriorityQueue.this.queue[index] = x;
/* 646 */       return index;
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
/*     */     int swapWithConceptuallyLastElement(E actualLastElement) {
/* 659 */       int parentIndex = getParentIndex(MinMaxPriorityQueue.this.size);
/* 660 */       if (parentIndex != 0) {
/* 661 */         int grandparentIndex = getParentIndex(parentIndex);
/* 662 */         int uncleIndex = getRightChildIndex(grandparentIndex);
/* 663 */         if (uncleIndex != parentIndex && getLeftChildIndex(uncleIndex) >= MinMaxPriorityQueue.this.size) {
/* 664 */           E uncleElement = MinMaxPriorityQueue.this.elementData(uncleIndex);
/* 665 */           if (this.ordering.compare(uncleElement, actualLastElement) < 0) {
/* 666 */             MinMaxPriorityQueue.this.queue[uncleIndex] = actualLastElement;
/* 667 */             MinMaxPriorityQueue.this.queue[MinMaxPriorityQueue.this.size] = uncleElement;
/* 668 */             return uncleIndex;
/*     */           } 
/*     */         } 
/*     */       } 
/* 672 */       return MinMaxPriorityQueue.this.size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int crossOver(int index, E x) {
/* 682 */       int minChildIndex = findMinChild(index);
/*     */ 
/*     */       
/* 685 */       if (minChildIndex > 0 && this.ordering.compare(MinMaxPriorityQueue.this.elementData(minChildIndex), x) < 0) {
/* 686 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minChildIndex);
/* 687 */         MinMaxPriorityQueue.this.queue[minChildIndex] = x;
/* 688 */         return minChildIndex;
/*     */       } 
/* 690 */       return crossOverUp(index, x);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int fillHoleAt(int index) {
/*     */       int minGrandchildIndex;
/* 702 */       while ((minGrandchildIndex = findMinGrandChild(index)) > 0) {
/* 703 */         MinMaxPriorityQueue.this.queue[index] = MinMaxPriorityQueue.this.elementData(minGrandchildIndex);
/* 704 */         index = minGrandchildIndex;
/*     */       } 
/* 706 */       return index;
/*     */     }
/*     */     
/*     */     private boolean verifyIndex(int i) {
/* 710 */       if (getLeftChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getLeftChildIndex(i)) > 0) {
/* 711 */         return false;
/*     */       }
/* 713 */       if (getRightChildIndex(i) < MinMaxPriorityQueue.this.size && compareElements(i, getRightChildIndex(i)) > 0) {
/* 714 */         return false;
/*     */       }
/* 716 */       if (i > 0 && compareElements(i, getParentIndex(i)) > 0) {
/* 717 */         return false;
/*     */       }
/* 719 */       if (i > 2 && compareElements(getGrandparentIndex(i), i) > 0) {
/* 720 */         return false;
/*     */       }
/* 722 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int getLeftChildIndex(int i) {
/* 728 */       return i * 2 + 1;
/*     */     }
/*     */     
/*     */     private int getRightChildIndex(int i) {
/* 732 */       return i * 2 + 2;
/*     */     }
/*     */     
/*     */     private int getParentIndex(int i) {
/* 736 */       return (i - 1) / 2;
/*     */     }
/*     */     
/*     */     private int getGrandparentIndex(int i) {
/* 740 */       return getParentIndex(getParentIndex(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class QueueIterator
/*     */     implements Iterator<E>
/*     */   {
/* 750 */     private int cursor = -1;
/* 751 */     private int nextCursor = -1;
/* 752 */     private int expectedModCount = MinMaxPriorityQueue.this.modCount;
/*     */     
/*     */     private Queue<E> forgetMeNot;
/*     */     
/*     */     private List<E> skipMe;
/*     */     
/*     */     private E lastFromForgetMeNot;
/*     */     private boolean canRemove;
/*     */     
/*     */     public boolean hasNext() {
/* 762 */       checkModCount();
/* 763 */       nextNotInSkipMe(this.cursor + 1);
/* 764 */       return (this.nextCursor < MinMaxPriorityQueue.this.size() || (this.forgetMeNot != null && !this.forgetMeNot.isEmpty()));
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 769 */       checkModCount();
/* 770 */       nextNotInSkipMe(this.cursor + 1);
/* 771 */       if (this.nextCursor < MinMaxPriorityQueue.this.size()) {
/* 772 */         this.cursor = this.nextCursor;
/* 773 */         this.canRemove = true;
/* 774 */         return MinMaxPriorityQueue.this.elementData(this.cursor);
/* 775 */       }  if (this.forgetMeNot != null) {
/* 776 */         this.cursor = MinMaxPriorityQueue.this.size();
/* 777 */         this.lastFromForgetMeNot = this.forgetMeNot.poll();
/* 778 */         if (this.lastFromForgetMeNot != null) {
/* 779 */           this.canRemove = true;
/* 780 */           return this.lastFromForgetMeNot;
/*     */         } 
/*     */       } 
/* 783 */       throw new NoSuchElementException("iterator moved past last element in queue.");
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 788 */       CollectPreconditions.checkRemove(this.canRemove);
/* 789 */       checkModCount();
/* 790 */       this.canRemove = false;
/* 791 */       this.expectedModCount++;
/* 792 */       if (this.cursor < MinMaxPriorityQueue.this.size()) {
/* 793 */         MinMaxPriorityQueue.MoveDesc<E> moved = MinMaxPriorityQueue.this.removeAt(this.cursor);
/* 794 */         if (moved != null) {
/* 795 */           if (this.forgetMeNot == null) {
/* 796 */             this.forgetMeNot = new ArrayDeque<>();
/* 797 */             this.skipMe = new ArrayList<>(3);
/*     */           } 
/* 799 */           if (!foundAndRemovedExactReference(this.skipMe, moved.toTrickle)) {
/* 800 */             this.forgetMeNot.add(moved.toTrickle);
/*     */           }
/* 802 */           if (!foundAndRemovedExactReference(this.forgetMeNot, moved.replaced)) {
/* 803 */             this.skipMe.add(moved.replaced);
/*     */           }
/*     */         } 
/* 806 */         this.cursor--;
/* 807 */         this.nextCursor--;
/*     */       } else {
/* 809 */         Preconditions.checkState(removeExact(this.lastFromForgetMeNot));
/* 810 */         this.lastFromForgetMeNot = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean foundAndRemovedExactReference(Iterable<E> elements, E target) {
/* 816 */       for (Iterator<E> it = elements.iterator(); it.hasNext(); ) {
/* 817 */         E element = it.next();
/* 818 */         if (element == target) {
/* 819 */           it.remove();
/* 820 */           return true;
/*     */         } 
/*     */       } 
/* 823 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean removeExact(Object target) {
/* 828 */       for (int i = 0; i < MinMaxPriorityQueue.this.size; i++) {
/* 829 */         if (MinMaxPriorityQueue.this.queue[i] == target) {
/* 830 */           MinMaxPriorityQueue.this.removeAt(i);
/* 831 */           return true;
/*     */         } 
/*     */       } 
/* 834 */       return false;
/*     */     }
/*     */     
/*     */     private void checkModCount() {
/* 838 */       if (MinMaxPriorityQueue.this.modCount != this.expectedModCount) {
/* 839 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void nextNotInSkipMe(int c) {
/* 848 */       if (this.nextCursor < c) {
/* 849 */         if (this.skipMe != null) {
/* 850 */           while (c < MinMaxPriorityQueue.this.size() && foundAndRemovedExactReference(this.skipMe, MinMaxPriorityQueue.this.elementData(c))) {
/* 851 */             c++;
/*     */           }
/*     */         }
/* 854 */         this.nextCursor = c;
/*     */       } 
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
/*     */     private QueueIterator() {}
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
/*     */   public Iterator<E> iterator() {
/* 880 */     return new QueueIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 885 */     for (int i = 0; i < this.size; i++) {
/* 886 */       this.queue[i] = null;
/*     */     }
/* 888 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 893 */     Object[] copyTo = new Object[this.size];
/* 894 */     System.arraycopy(this.queue, 0, copyTo, 0, this.size);
/* 895 */     return copyTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 904 */     return this.minHeap.ordering;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   int capacity() {
/* 909 */     return this.queue.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable<?> initialContents) {
/* 920 */     int result = (configuredExpectedSize == -1) ? 11 : configuredExpectedSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 926 */     if (initialContents instanceof Collection) {
/* 927 */       int initialSize = ((Collection)initialContents).size();
/* 928 */       result = Math.max(result, initialSize);
/*     */     } 
/*     */ 
/*     */     
/* 932 */     return capAtMaximumSize(result, maximumSize);
/*     */   }
/*     */   
/*     */   private void growIfNeeded() {
/* 936 */     if (this.size > this.queue.length) {
/* 937 */       int newCapacity = calculateNewCapacity();
/* 938 */       Object[] newQueue = new Object[newCapacity];
/* 939 */       System.arraycopy(this.queue, 0, newQueue, 0, this.queue.length);
/* 940 */       this.queue = newQueue;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calculateNewCapacity() {
/* 946 */     int oldCapacity = this.queue.length;
/*     */     
/* 948 */     int newCapacity = (oldCapacity < 64) ? ((oldCapacity + 1) * 2) : IntMath.checkedMultiply(oldCapacity / 2, 3);
/* 949 */     return capAtMaximumSize(newCapacity, this.maximumSize);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int capAtMaximumSize(int queueSize, int maximumSize) {
/* 954 */     return Math.min(queueSize - 1, maximumSize) + 1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\MinMaxPriorityQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */