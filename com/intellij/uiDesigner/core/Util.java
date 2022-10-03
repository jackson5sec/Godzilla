/*     */ package com.intellij.uiDesigner.core;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public final class Util {
/*   8 */   private static final Dimension MAX_SIZE = new Dimension(2147483647, 2147483647);
/*     */   public static final int DEFAULT_INDENT = 10;
/*     */   
/*     */   public static Dimension getMinimumSize(Component component, GridConstraints constraints, boolean addIndent) {
/*     */     try {
/*  13 */       Dimension size = getSize(constraints.myMinimumSize, component.getMinimumSize());
/*  14 */       if (addIndent) {
/*  15 */         size.width += 10 * constraints.getIndent();
/*     */       }
/*  17 */       return size;
/*     */     }
/*  19 */     catch (NullPointerException npe) {
/*  20 */       return new Dimension(0, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dimension getMaximumSize(GridConstraints constraints, boolean addIndent) {
/*     */     try {
/*  29 */       Dimension size = getSize(constraints.myMaximumSize, MAX_SIZE);
/*  30 */       if (addIndent && size.width < MAX_SIZE.width) {
/*  31 */         size.width += 10 * constraints.getIndent();
/*     */       }
/*  33 */       return size;
/*     */     }
/*  35 */     catch (NullPointerException e) {
/*  36 */       return new Dimension(0, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Dimension getPreferredSize(Component component, GridConstraints constraints, boolean addIndent) {
/*     */     try {
/*  42 */       Dimension size = getSize(constraints.myPreferredSize, component.getPreferredSize());
/*  43 */       if (addIndent) {
/*  44 */         size.width += 10 * constraints.getIndent();
/*     */       }
/*  46 */       return size;
/*     */     }
/*  48 */     catch (NullPointerException e) {
/*  49 */       return new Dimension(0, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Dimension getSize(Dimension overridenSize, Dimension ownSize) {
/*  54 */     int overridenWidth = (overridenSize.width >= 0) ? overridenSize.width : ownSize.width;
/*  55 */     int overridenHeight = (overridenSize.height >= 0) ? overridenSize.height : ownSize.height;
/*  56 */     return new Dimension(overridenWidth, overridenHeight);
/*     */   }
/*     */   
/*     */   public static void adjustSize(Component component, GridConstraints constraints, Dimension size) {
/*  60 */     Dimension minimumSize = getMinimumSize(component, constraints, false);
/*  61 */     Dimension maximumSize = getMaximumSize(constraints, false);
/*     */     
/*  63 */     size.width = Math.max(size.width, minimumSize.width);
/*  64 */     size.height = Math.max(size.height, minimumSize.height);
/*     */     
/*  66 */     size.width = Math.min(size.width, maximumSize.width);
/*  67 */     size.height = Math.min(size.height, maximumSize.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int eliminate(int[] cellIndices, int[] spans, ArrayList<Integer> eliminated) {
/*  75 */     int size = cellIndices.length;
/*  76 */     if (size != spans.length) {
/*  77 */       throw new IllegalArgumentException("size mismatch: " + size + ", " + spans.length);
/*     */     }
/*  79 */     if (eliminated != null && eliminated.size() != 0) {
/*  80 */       throw new IllegalArgumentException("eliminated must be empty");
/*     */     }
/*     */     
/*  83 */     int cellCount = 0;
/*  84 */     for (int i = 0; i < size; i++) {
/*  85 */       cellCount = Math.max(cellCount, cellIndices[i] + spans[i]);
/*     */     }
/*     */     
/*  88 */     for (int cell = cellCount - 1; cell >= 0; cell--) {
/*     */ 
/*     */       
/*  91 */       boolean starts = false;
/*  92 */       boolean ends = false;
/*     */       int j;
/*  94 */       for (j = 0; j < size; j++) {
/*  95 */         if (cellIndices[j] == cell) {
/*  96 */           starts = true;
/*     */         }
/*  98 */         if (cellIndices[j] + spans[j] - 1 == cell) {
/*  99 */           ends = true;
/*     */         }
/*     */       } 
/*     */       
/* 103 */       if (!starts || !ends) {
/*     */ 
/*     */ 
/*     */         
/* 107 */         if (eliminated != null) {
/* 108 */           eliminated.add(new Integer(cell));
/*     */         }
/*     */ 
/*     */         
/* 112 */         for (j = 0; j < size; j++) {
/* 113 */           boolean decreaseSpan = (cellIndices[j] <= cell && cell < cellIndices[j] + spans[j]);
/* 114 */           boolean decreaseIndex = (cellIndices[j] > cell);
/*     */           
/* 116 */           if (decreaseSpan) {
/* 117 */             spans[j] = spans[j] - 1;
/*     */           }
/*     */           
/* 120 */           if (decreaseIndex) {
/* 121 */             cellIndices[j] = cellIndices[j] - 1;
/*     */           }
/*     */         } 
/*     */         
/* 125 */         cellCount--;
/*     */       } 
/*     */     } 
/* 128 */     return cellCount;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */