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
/*    */ public class Stmnt
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected int operatorId;
/*    */   
/*    */   public Stmnt(int op, ASTree _head, ASTList _tail) {
/* 31 */     super(_head, _tail);
/* 32 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   public Stmnt(int op, ASTree _head) {
/* 36 */     super(_head);
/* 37 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   public Stmnt(int op) {
/* 41 */     this(op, null);
/*    */   }
/*    */   
/*    */   public static Stmnt make(int op, ASTree oprand1, ASTree oprand2) {
/* 45 */     return new Stmnt(op, oprand1, new ASTList(oprand2));
/*    */   }
/*    */   
/*    */   public static Stmnt make(int op, ASTree op1, ASTree op2, ASTree op3) {
/* 49 */     return new Stmnt(op, op1, new ASTList(op2, new ASTList(op3)));
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 53 */     v.atStmnt(this);
/*    */   } public int getOperator() {
/* 55 */     return this.operatorId;
/*    */   }
/*    */   
/*    */   protected String getTag() {
/* 59 */     if (this.operatorId < 128)
/* 60 */       return "stmnt:" + (char)this.operatorId; 
/* 61 */     return "stmnt:" + this.operatorId;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Stmnt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */