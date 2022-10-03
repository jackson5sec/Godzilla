/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ import org.mozilla.javascript.Node;
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
/*    */ public class Block
/*    */   extends AstNode
/*    */ {
/*    */   public Block() {}
/*    */   
/*    */   public Block(int pos) {
/* 30 */     super(pos);
/*    */   }
/*    */   
/*    */   public Block(int pos, int len) {
/* 34 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addStatement(AstNode statement) {
/* 41 */     addChild(statement);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 46 */     StringBuilder sb = new StringBuilder();
/* 47 */     sb.append(makeIndent(depth));
/* 48 */     sb.append("{\n");
/* 49 */     for (Node kid : this) {
/* 50 */       sb.append(((AstNode)kid).toSource(depth + 1));
/*    */     }
/* 52 */     sb.append(makeIndent(depth));
/* 53 */     sb.append("}\n");
/* 54 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 59 */     if (v.visit(this))
/* 60 */       for (Node kid : this)
/* 61 */         ((AstNode)kid).visit(v);  
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Block.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */