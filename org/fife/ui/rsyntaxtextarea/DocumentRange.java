/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DocumentRange
/*     */   implements Comparable<DocumentRange>
/*     */ {
/*     */   private int startOffs;
/*     */   private int endOffs;
/*     */   
/*     */   public DocumentRange(int startOffs, int endOffs) {
/*  33 */     set(startOffs, endOffs);
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
/*     */   public int compareTo(DocumentRange other) {
/*  45 */     if (other == null) {
/*  46 */       return 1;
/*     */     }
/*  48 */     int diff = this.startOffs - other.startOffs;
/*  49 */     if (diff != 0) {
/*  50 */       return diff;
/*     */     }
/*  52 */     return this.endOffs - other.endOffs;
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
/*     */   public boolean equals(Object other) {
/*  65 */     if (other == this) {
/*  66 */       return true;
/*     */     }
/*  68 */     if (other instanceof DocumentRange) {
/*  69 */       return (compareTo((DocumentRange)other) == 0);
/*     */     }
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndOffset() {
/*  82 */     return this.endOffs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartOffset() {
/*  93 */     return this.startOffs;
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
/*     */   public int hashCode() {
/* 105 */     return this.startOffs + this.endOffs;
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
/*     */   public boolean isZeroLength() {
/* 118 */     return (this.startOffs == this.endOffs);
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
/*     */   public void set(int start, int end) {
/* 131 */     if (start < 0 || end < 0) {
/* 132 */       throw new IllegalArgumentException("start and end must be >= 0 (" + start + "-" + end + ")");
/*     */     }
/*     */     
/* 135 */     if (end < start) {
/* 136 */       throw new IllegalArgumentException("'end' cannot be less than 'start' (" + start + "-" + end + ")");
/*     */     }
/*     */ 
/*     */     
/* 140 */     this.startOffs = start;
/* 141 */     this.endOffs = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     return "[DocumentRange: " + this.startOffs + "-" + this.endOffs + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocumentRange translate(int amount) {
/* 163 */     this.startOffs += amount;
/* 164 */     this.endOffs += amount;
/* 165 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\DocumentRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */