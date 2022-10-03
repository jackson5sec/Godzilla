/*     */ package org.springframework.cglib.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class SorterTemplate
/*     */ {
/*     */   private static final int MERGESORT_THRESHOLD = 12;
/*     */   private static final int QUICKSORT_THRESHOLD = 7;
/*     */   
/*     */   protected abstract void swap(int paramInt1, int paramInt2);
/*     */   
/*     */   protected abstract int compare(int paramInt1, int paramInt2);
/*     */   
/*     */   protected void quickSort(int lo, int hi) {
/*  28 */     quickSortHelper(lo, hi);
/*  29 */     insertionSort(lo, hi);
/*     */   }
/*     */   
/*     */   private void quickSortHelper(int lo, int hi) {
/*     */     while (true) {
/*  34 */       int diff = hi - lo;
/*  35 */       if (diff <= 7) {
/*     */         break;
/*     */       }
/*  38 */       int i = (hi + lo) / 2;
/*  39 */       if (compare(lo, i) > 0) {
/*  40 */         swap(lo, i);
/*     */       }
/*  42 */       if (compare(lo, hi) > 0) {
/*  43 */         swap(lo, hi);
/*     */       }
/*  45 */       if (compare(i, hi) > 0) {
/*  46 */         swap(i, hi);
/*     */       }
/*  48 */       int j = hi - 1;
/*  49 */       swap(i, j);
/*  50 */       i = lo;
/*  51 */       int v = j;
/*     */       while (true) {
/*  53 */         if (compare(++i, v) < 0) {
/*     */           continue;
/*     */         }
/*  56 */         while (compare(--j, v) > 0);
/*     */ 
/*     */         
/*  59 */         if (j < i) {
/*     */           break;
/*     */         }
/*  62 */         swap(i, j);
/*     */       } 
/*  64 */       swap(i, hi - 1);
/*  65 */       if (j - lo <= hi - i + 1) {
/*  66 */         quickSortHelper(lo, j);
/*  67 */         lo = i + 1; continue;
/*     */       } 
/*  69 */       quickSortHelper(i + 1, hi);
/*  70 */       hi = j;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void insertionSort(int lo, int hi) {
/*  76 */     for (int i = lo + 1; i <= hi; i++) {
/*  77 */       for (int j = i; j > lo && 
/*  78 */         compare(j - 1, j) > 0; j--) {
/*  79 */         swap(j - 1, j);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void mergeSort(int lo, int hi) {
/*  88 */     int diff = hi - lo;
/*  89 */     if (diff <= 12) {
/*  90 */       insertionSort(lo, hi);
/*     */       return;
/*     */     } 
/*  93 */     int mid = lo + diff / 2;
/*  94 */     mergeSort(lo, mid);
/*  95 */     mergeSort(mid, hi);
/*  96 */     merge(lo, mid, hi, mid - lo, hi - mid);
/*     */   }
/*     */   private void merge(int lo, int pivot, int hi, int len1, int len2) {
/*     */     int first_cut, second_cut, len11, len22;
/* 100 */     if (len1 == 0 || len2 == 0) {
/*     */       return;
/*     */     }
/* 103 */     if (len1 + len2 == 2) {
/* 104 */       if (compare(pivot, lo) < 0) {
/* 105 */         swap(pivot, lo);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 111 */     if (len1 > len2) {
/* 112 */       len11 = len1 / 2;
/* 113 */       first_cut = lo + len11;
/* 114 */       second_cut = lower(pivot, hi, first_cut);
/* 115 */       len22 = second_cut - pivot;
/*     */     } else {
/* 117 */       len22 = len2 / 2;
/* 118 */       second_cut = pivot + len22;
/* 119 */       first_cut = upper(lo, pivot, second_cut);
/* 120 */       len11 = first_cut - lo;
/*     */     } 
/* 122 */     rotate(first_cut, pivot, second_cut);
/* 123 */     int new_mid = first_cut + len22;
/* 124 */     merge(lo, first_cut, new_mid, len11, len22);
/* 125 */     merge(new_mid, second_cut, hi, len1 - len11, len2 - len22);
/*     */   }
/*     */   
/*     */   private void rotate(int lo, int mid, int hi) {
/* 129 */     int lot = lo;
/* 130 */     int hit = mid - 1;
/* 131 */     while (lot < hit) {
/* 132 */       swap(lot++, hit--);
/*     */     }
/* 134 */     lot = mid; hit = hi - 1;
/* 135 */     while (lot < hit) {
/* 136 */       swap(lot++, hit--);
/*     */     }
/* 138 */     lot = lo; hit = hi - 1;
/* 139 */     while (lot < hit) {
/* 140 */       swap(lot++, hit--);
/*     */     }
/*     */   }
/*     */   
/*     */   private int lower(int lo, int hi, int val) {
/* 145 */     int len = hi - lo;
/* 146 */     while (len > 0) {
/* 147 */       int half = len / 2;
/* 148 */       int mid = lo + half;
/* 149 */       if (compare(mid, val) < 0) {
/* 150 */         lo = mid + 1;
/* 151 */         len = len - half - 1; continue;
/*     */       } 
/* 153 */       len = half;
/*     */     } 
/*     */     
/* 156 */     return lo;
/*     */   }
/*     */   
/*     */   private int upper(int lo, int hi, int val) {
/* 160 */     int len = hi - lo;
/* 161 */     while (len > 0) {
/* 162 */       int half = len / 2;
/* 163 */       int mid = lo + half;
/* 164 */       if (compare(val, mid) < 0) {
/* 165 */         len = half; continue;
/*     */       } 
/* 167 */       lo = mid + 1;
/* 168 */       len = len - half - 1;
/*     */     } 
/*     */     
/* 171 */     return lo;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cgli\\util\SorterTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */