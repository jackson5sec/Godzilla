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
/*    */ public class NewExpr
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected boolean newArray;
/*    */   protected int arrayType;
/*    */   
/*    */   public NewExpr(ASTList className, ASTList args) {
/* 32 */     super(className, new ASTList(args));
/* 33 */     this.newArray = false;
/* 34 */     this.arrayType = 307;
/*    */   }
/*    */   
/*    */   public NewExpr(int type, ASTList arraySize, ArrayInit init) {
/* 38 */     super(null, new ASTList(arraySize));
/* 39 */     this.newArray = true;
/* 40 */     this.arrayType = type;
/* 41 */     if (init != null) {
/* 42 */       append(this, init);
/*    */     }
/*    */   }
/*    */   
/*    */   public static NewExpr makeObjectArray(ASTList className, ASTList arraySize, ArrayInit init) {
/* 47 */     NewExpr e = new NewExpr(className, arraySize);
/* 48 */     e.newArray = true;
/* 49 */     if (init != null) {
/* 50 */       append(e, init);
/*    */     }
/* 52 */     return e;
/*    */   }
/*    */   public boolean isArray() {
/* 55 */     return this.newArray;
/*    */   }
/*    */   
/*    */   public int getArrayType() {
/* 59 */     return this.arrayType;
/*    */   } public ASTList getClassName() {
/* 61 */     return (ASTList)getLeft();
/*    */   } public ASTList getArguments() {
/* 63 */     return (ASTList)getRight().getLeft();
/*    */   } public ASTList getArraySize() {
/* 65 */     return getArguments();
/*    */   }
/*    */   public ArrayInit getInitializer() {
/* 68 */     ASTree t = getRight().getRight();
/* 69 */     if (t == null)
/* 70 */       return null; 
/* 71 */     return (ArrayInit)t.getLeft();
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 75 */     v.atNewExpr(this);
/*    */   }
/*    */   
/*    */   protected String getTag() {
/* 79 */     return this.newArray ? "new[]" : "new";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\NewExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */