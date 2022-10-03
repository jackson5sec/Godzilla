/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class XmlRef
/*     */   extends AstNode
/*     */ {
/*     */   protected Name namespace;
/*  37 */   protected int atPos = -1;
/*  38 */   protected int colonPos = -1;
/*     */ 
/*     */   
/*     */   public XmlRef() {}
/*     */   
/*     */   public XmlRef(int pos) {
/*  44 */     super(pos);
/*     */   }
/*     */   
/*     */   public XmlRef(int pos, int len) {
/*  48 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name getNamespace() {
/*  55 */     return this.namespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespace(Name namespace) {
/*  63 */     this.namespace = namespace;
/*  64 */     if (namespace != null) {
/*  65 */       namespace.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAttributeAccess() {
/*  72 */     return (this.atPos >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAtPos() {
/*  80 */     return this.atPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAtPos(int atPos) {
/*  87 */     this.atPos = atPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColonPos() {
/*  95 */     return this.colonPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColonPos(int colonPos) {
/* 102 */     this.colonPos = colonPos;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */