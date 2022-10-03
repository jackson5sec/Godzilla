/*     */ package org.fife.ui.rsyntaxtextarea.templates;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCodeTemplate
/*     */   implements CodeTemplate
/*     */ {
/*     */   private String id;
/*     */   
/*     */   public AbstractCodeTemplate() {}
/*     */   
/*     */   public AbstractCodeTemplate(String id) {
/*  40 */     setID(id);
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
/*     */   public Object clone() {
/*     */     try {
/*  54 */       return super.clone();
/*  55 */     } catch (CloneNotSupportedException e) {
/*  56 */       throw new InternalError("CodeTemplate implementation not Cloneable: " + 
/*     */           
/*  58 */           getClass().getName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(CodeTemplate o) {
/*  76 */     if (o == null) {
/*  77 */       return -1;
/*     */     }
/*  79 */     return getID().compareTo(o.getID());
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
/*     */   public boolean equals(Object obj) {
/*  91 */     if (obj instanceof CodeTemplate) {
/*  92 */       return (compareTo((CodeTemplate)obj) == 0);
/*     */     }
/*  94 */     return false;
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
/*     */   public String getID() {
/* 106 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     return this.id.hashCode();
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
/*     */   public void setID(String id) {
/* 129 */     if (id == null) {
/* 130 */       throw new IllegalArgumentException("id cannot be null");
/*     */     }
/* 132 */     this.id = id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\templates\AbstractCodeTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */