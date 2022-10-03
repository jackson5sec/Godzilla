/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RComboBoxModel<E>
/*     */   extends DefaultComboBoxModel<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private int maxNumElements;
/*     */   
/*     */   public RComboBoxModel() {
/*  48 */     setMaxNumElements(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RComboBoxModel(E[] items) {
/*  59 */     super(items);
/*  60 */     setMaxNumElements(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RComboBoxModel(Vector<E> v) {
/*  71 */     super(v);
/*  72 */     setMaxNumElements(8);
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
/*     */   public void addElement(E anObject) {
/*  84 */     insertElementAt(anObject, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureValidItemCount() {
/*  92 */     while (getSize() > this.maxNumElements) {
/*  93 */       removeElementAt(getSize() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxNumElements() {
/* 104 */     return this.maxNumElements;
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
/*     */   public void insertElementAt(E anObject, int index) {
/* 119 */     int oldPos = getIndexOf(anObject);
/* 120 */     if (oldPos == index) {
/*     */       return;
/*     */     }
/* 123 */     if (oldPos > -1) {
/* 124 */       removeElement(anObject);
/*     */     }
/*     */     
/* 127 */     super.insertElementAt(anObject, index);
/* 128 */     ensureValidItemCount();
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
/*     */   public void setMaxNumElements(int numElements) {
/* 141 */     this.maxNumElements = (numElements <= 0) ? 4 : numElements;
/* 142 */     ensureValidItemCount();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\RComboBoxModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */