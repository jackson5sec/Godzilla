/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import javax.swing.text.BadLocationException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RDocumentCharSequence
/*     */   implements CharSequence
/*     */ {
/*     */   private RDocument doc;
/*     */   private int start;
/*     */   private int end;
/*     */   
/*     */   RDocumentCharSequence(RDocument doc, int start) {
/*  38 */     this(doc, start, doc.getLength());
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
/*     */   RDocumentCharSequence(RDocument doc, int start, int end) {
/*  50 */     this.doc = doc;
/*  51 */     this.start = start;
/*  52 */     this.end = end;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/*  58 */     if (index < 0 || index >= length()) {
/*  59 */       throw new IndexOutOfBoundsException("Index " + index + " is not in range [0-" + 
/*  60 */           length() + ")");
/*     */     }
/*     */     try {
/*  63 */       return this.doc.charAt(this.start + index);
/*  64 */     } catch (BadLocationException ble) {
/*  65 */       throw new IndexOutOfBoundsException(ble.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  72 */     return this.end - this.start;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/*  78 */     if (start < 0) {
/*  79 */       throw new IndexOutOfBoundsException("start must be >= 0 (" + start + ")");
/*     */     }
/*     */     
/*  82 */     if (end < 0) {
/*  83 */       throw new IndexOutOfBoundsException("end must be >= 0 (" + end + ")");
/*     */     }
/*     */     
/*  86 */     if (end > length()) {
/*  87 */       throw new IndexOutOfBoundsException("end must be <= " + 
/*  88 */           length() + " (" + end + ")");
/*     */     }
/*  90 */     if (start > end) {
/*  91 */       throw new IndexOutOfBoundsException("start (" + start + ") cannot be > end (" + end + ")");
/*     */     }
/*     */     
/*  94 */     int newStart = this.start + start;
/*  95 */     int newEnd = this.start + end;
/*  96 */     return new RDocumentCharSequence(this.doc, newStart, newEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     try {
/* 103 */       return this.doc.getText(this.start, length());
/* 104 */     } catch (BadLocationException ble) {
/* 105 */       ble.printStackTrace();
/* 106 */       return "";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RDocumentCharSequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */