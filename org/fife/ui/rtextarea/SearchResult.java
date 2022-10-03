/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SearchResult
/*     */   implements Comparable<SearchResult>
/*     */ {
/*     */   private DocumentRange matchRange;
/*     */   private int count;
/*     */   private int markedCount;
/*     */   private boolean wrapped;
/*     */   
/*     */   public SearchResult() {
/*  57 */     this(null, 0, 0);
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
/*     */   public SearchResult(DocumentRange range, int count, int markedCount) {
/*  75 */     this.matchRange = range;
/*  76 */     this.count = count;
/*  77 */     this.markedCount = markedCount;
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
/*     */   public int compareTo(SearchResult other) {
/*  90 */     if (other == null) {
/*  91 */       return 1;
/*     */     }
/*  93 */     if (other == this) {
/*  94 */       return 0;
/*     */     }
/*  96 */     int diff = this.count - other.count;
/*  97 */     if (diff != 0) {
/*  98 */       return diff;
/*     */     }
/* 100 */     diff = this.markedCount - other.markedCount;
/* 101 */     if (diff != 0) {
/* 102 */       return diff;
/*     */     }
/* 104 */     if (this.matchRange == null) {
/* 105 */       return (other.matchRange == null) ? 0 : -1;
/*     */     }
/* 107 */     return this.matchRange.compareTo(other.matchRange);
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
/* 120 */     if (other == this) {
/* 121 */       return true;
/*     */     }
/* 123 */     if (other instanceof SearchResult) {
/* 124 */       return (compareTo((SearchResult)other) == 0);
/*     */     }
/* 126 */     return false;
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
/*     */   public int getCount() {
/* 140 */     return this.count;
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
/*     */   public int getMarkedCount() {
/* 152 */     return this.markedCount;
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
/*     */   public DocumentRange getMatchRange() {
/* 168 */     return this.matchRange;
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
/* 180 */     int hash = this.count + this.markedCount;
/* 181 */     if (this.matchRange != null) {
/* 182 */       hash += this.matchRange.hashCode();
/*     */     }
/* 184 */     return hash;
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
/*     */   public void setCount(int count) {
/* 198 */     this.count = count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMarkedCount(int markedCount) {
/* 209 */     this.markedCount = markedCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMatchRange(DocumentRange range) {
/* 220 */     this.matchRange = range;
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
/*     */   public void setWrapped(boolean wrapped) {
/* 233 */     this.wrapped = wrapped;
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
/*     */   public boolean isWrapped() {
/* 245 */     return this.wrapped;
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
/* 256 */     return "[SearchResult: count=" + 
/* 257 */       getCount() + ", markedCount=" + 
/* 258 */       getMarkedCount() + ", matchRange=" + 
/* 259 */       getMatchRange() + "]";
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
/*     */   public boolean wasFound() {
/* 272 */     return (getCount() > 0);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\SearchResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */