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
/*    */ public class Label
/*    */   extends Jump
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public Label() {}
/*    */   
/*    */   public Label(int pos) {
/* 28 */     this(pos, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Label(int pos, int len) {
/* 33 */     this.position = pos;
/* 34 */     this.length = len;
/*    */   }
/*    */   
/*    */   public Label(int pos, int len, String name) {
/* 38 */     this(pos, len);
/* 39 */     setName(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 55 */     name = (name == null) ? null : name.trim();
/* 56 */     if (name == null || "".equals(name))
/* 57 */       throw new IllegalArgumentException("invalid label name"); 
/* 58 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 63 */     StringBuilder sb = new StringBuilder();
/* 64 */     sb.append(makeIndent(depth));
/* 65 */     sb.append(this.name);
/* 66 */     sb.append(":\n");
/* 67 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 75 */     v.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Label.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */