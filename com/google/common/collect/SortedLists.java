/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible
/*     */ @Beta
/*     */ final class SortedLists
/*     */ {
/*     */   enum KeyPresentBehavior
/*     */   {
/*  49 */     ANY_PRESENT
/*     */     {
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  53 */         return foundIndex;
/*     */       }
/*     */     },
/*     */     
/*  57 */     LAST_PRESENT
/*     */     {
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  63 */         int lower = foundIndex;
/*  64 */         int upper = list.size() - 1;
/*     */         
/*  66 */         while (lower < upper) {
/*  67 */           int middle = lower + upper + 1 >>> 1;
/*  68 */           int c = comparator.compare(list.get(middle), key);
/*  69 */           if (c > 0) {
/*  70 */             upper = middle - 1; continue;
/*     */           } 
/*  72 */           lower = middle;
/*     */         } 
/*     */         
/*  75 */         return lower;
/*     */       }
/*     */     },
/*     */     
/*  79 */     FIRST_PRESENT
/*     */     {
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  85 */         int lower = 0;
/*  86 */         int upper = foundIndex;
/*     */ 
/*     */         
/*  89 */         while (lower < upper) {
/*  90 */           int middle = lower + upper >>> 1;
/*  91 */           int c = comparator.compare(list.get(middle), key);
/*  92 */           if (c < 0) {
/*  93 */             lower = middle + 1; continue;
/*     */           } 
/*  95 */           upper = middle;
/*     */         } 
/*     */         
/*  98 */         return lower;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     FIRST_AFTER
/*     */     {
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 109 */         return LAST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) + 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     LAST_BEFORE
/*     */     {
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 120 */         return FIRST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) - 1;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <E> int resultIndex(Comparator<? super E> param1Comparator, E param1E, List<? extends E> param1List, int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum KeyAbsentBehavior
/*     */   {
/* 136 */     NEXT_LOWER
/*     */     {
/*     */       int resultIndex(int higherIndex) {
/* 139 */         return higherIndex - 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     NEXT_HIGHER
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 149 */         return higherIndex;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     INVERTED_INSERTION_INDEX
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 167 */         return higherIndex ^ 0xFFFFFFFF;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract int resultIndex(int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 186 */     Preconditions.checkNotNull(e);
/* 187 */     return binarySearch(list, e, Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 202 */     return binarySearch(list, keyFunction, key, 
/* 203 */         Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 220 */     return binarySearch(
/* 221 */         Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
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
/*     */   public static <E> int binarySearch(List<? extends E> list, E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 253 */     Preconditions.checkNotNull(comparator);
/* 254 */     Preconditions.checkNotNull(list);
/* 255 */     Preconditions.checkNotNull(presentBehavior);
/* 256 */     Preconditions.checkNotNull(absentBehavior);
/* 257 */     if (!(list instanceof java.util.RandomAccess)) {
/* 258 */       list = Lists.newArrayList(list);
/*     */     }
/*     */ 
/*     */     
/* 262 */     int lower = 0;
/* 263 */     int upper = list.size() - 1;
/*     */     
/* 265 */     while (lower <= upper) {
/* 266 */       int middle = lower + upper >>> 1;
/* 267 */       int c = comparator.compare(key, list.get(middle));
/* 268 */       if (c < 0) {
/* 269 */         upper = middle - 1; continue;
/* 270 */       }  if (c > 0) {
/* 271 */         lower = middle + 1; continue;
/*     */       } 
/* 273 */       return lower + presentBehavior
/* 274 */         .<E>resultIndex(comparator, key, list
/* 275 */           .subList(lower, upper + 1), middle - lower);
/*     */     } 
/*     */     
/* 278 */     return absentBehavior.resultIndex(lower);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SortedLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */