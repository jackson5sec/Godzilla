/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public final class ClipboardHistory
/*     */ {
/*     */   private static ClipboardHistory instance;
/*  40 */   private List<String> history = new ArrayList<>();
/*  41 */   private int maxSize = 12;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_MAX_SIZE = 12;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(String str) {
/*  52 */     int size = this.history.size();
/*  53 */     if (size == 0) {
/*  54 */       this.history.add(str);
/*     */     } else {
/*     */       
/*  57 */       int index = this.history.indexOf(str);
/*  58 */       if (index != size - 1) {
/*  59 */         if (index > -1) {
/*  60 */           this.history.remove(index);
/*     */         }
/*  62 */         this.history.add(str);
/*     */       } 
/*  64 */       trim();
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
/*     */   public static ClipboardHistory get() {
/*  78 */     if (instance == null) {
/*  79 */       instance = new ClipboardHistory();
/*     */     }
/*  81 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getHistory() {
/*  91 */     List<String> copy = new ArrayList<>(this.history);
/*  92 */     Collections.reverse(copy);
/*  93 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxSize() {
/* 104 */     return this.maxSize;
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
/*     */   public void setMaxSize(int maxSize) {
/* 117 */     if (maxSize <= 0) {
/* 118 */       throw new IllegalArgumentException("Maximum size must be >= 0");
/*     */     }
/* 120 */     this.maxSize = maxSize;
/* 121 */     trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void trim() {
/* 130 */     while (this.history.size() > this.maxSize)
/* 131 */       this.history.remove(0); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\ClipboardHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */