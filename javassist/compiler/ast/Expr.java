/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
/*    */ import javassist.compiler.TokenId;
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
/*    */ public class Expr
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected int operatorId;
/*    */   
/*    */   Expr(int op, ASTree _head, ASTList _tail) {
/* 37 */     super(_head, _tail);
/* 38 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   Expr(int op, ASTree _head) {
/* 42 */     super(_head);
/* 43 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   public static Expr make(int op, ASTree oprand1, ASTree oprand2) {
/* 47 */     return new Expr(op, oprand1, new ASTList(oprand2));
/*    */   }
/*    */   
/*    */   public static Expr make(int op, ASTree oprand1) {
/* 51 */     return new Expr(op, oprand1);
/*    */   }
/*    */   public int getOperator() {
/* 54 */     return this.operatorId;
/*    */   } public void setOperator(int op) {
/* 56 */     this.operatorId = op;
/*    */   } public ASTree oprand1() {
/* 58 */     return getLeft();
/*    */   }
/*    */   public void setOprand1(ASTree expr) {
/* 61 */     setLeft(expr);
/*    */   }
/*    */   public ASTree oprand2() {
/* 64 */     return getRight().getLeft();
/*    */   }
/*    */   public void setOprand2(ASTree expr) {
/* 67 */     getRight().setLeft(expr);
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 71 */     v.atExpr(this);
/*    */   }
/*    */   public String getName() {
/* 74 */     int id = this.operatorId;
/* 75 */     if (id < 128)
/* 76 */       return String.valueOf((char)id); 
/* 77 */     if (350 <= id && id <= 371)
/* 78 */       return opNames[id - 350]; 
/* 79 */     if (id == 323) {
/* 80 */       return "instanceof";
/*    */     }
/* 82 */     return String.valueOf(id);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTag() {
/* 87 */     return "op:" + getName();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Expr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */