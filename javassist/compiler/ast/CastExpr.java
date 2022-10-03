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
/*    */ public class CastExpr
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected int castType;
/*    */   protected int arrayDim;
/*    */   
/*    */   public CastExpr(ASTList className, int dim, ASTree expr) {
/* 32 */     super(className, new ASTList(expr));
/* 33 */     this.castType = 307;
/* 34 */     this.arrayDim = dim;
/*    */   }
/*    */   
/*    */   public CastExpr(int type, int dim, ASTree expr) {
/* 38 */     super(null, new ASTList(expr));
/* 39 */     this.castType = type;
/* 40 */     this.arrayDim = dim;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getType() {
/* 45 */     return this.castType;
/*    */   } public int getArrayDim() {
/* 47 */     return this.arrayDim;
/*    */   } public ASTList getClassName() {
/* 49 */     return (ASTList)getLeft();
/*    */   } public ASTree getOprand() {
/* 51 */     return getRight().getLeft();
/*    */   } public void setOprand(ASTree t) {
/* 53 */     getRight().setLeft(t);
/*    */   }
/*    */   public String getTag() {
/* 56 */     return "cast:" + this.castType + ":" + this.arrayDim;
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 59 */     v.atCastExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\CastExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */