/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlPropRef
/*    */   extends XmlRef
/*    */ {
/*    */   private Name propName;
/*    */   
/*    */   public XmlPropRef() {}
/*    */   
/*    */   public XmlPropRef(int pos) {
/* 40 */     super(pos);
/*    */   }
/*    */   
/*    */   public XmlPropRef(int pos, int len) {
/* 44 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Name getPropName() {
/* 51 */     return this.propName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPropName(Name propName) {
/* 59 */     assertNotNull(propName);
/* 60 */     this.propName = propName;
/* 61 */     propName.setParent(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 66 */     StringBuilder sb = new StringBuilder();
/* 67 */     sb.append(makeIndent(depth));
/* 68 */     if (isAttributeAccess()) {
/* 69 */       sb.append("@");
/*    */     }
/* 71 */     if (this.namespace != null) {
/* 72 */       sb.append(this.namespace.toSource(0));
/* 73 */       sb.append("::");
/*    */     } 
/* 75 */     sb.append(this.propName.toSource(0));
/* 76 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 84 */     if (v.visit(this)) {
/* 85 */       if (this.namespace != null) {
/* 86 */         this.namespace.visit(v);
/*    */       }
/* 88 */       this.propName.visit(v);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlPropRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */