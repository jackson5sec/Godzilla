/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
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
/*    */ public class DoubleConst
/*    */   extends ASTree
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected double value;
/*    */   protected int type;
/*    */   
/*    */   public DoubleConst(double v, int tokenId) {
/* 31 */     this.value = v; this.type = tokenId;
/*    */   } public double get() {
/* 33 */     return this.value;
/*    */   } public void set(double v) {
/* 35 */     this.value = v;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 39 */     return this.type;
/*    */   }
/*    */   public String toString() {
/* 42 */     return Double.toString(this.value);
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 46 */     v.atDoubleConst(this);
/*    */   }
/*    */   
/*    */   public ASTree compute(int op, ASTree right) {
/* 50 */     if (right instanceof IntConst)
/* 51 */       return compute0(op, (IntConst)right); 
/* 52 */     if (right instanceof DoubleConst) {
/* 53 */       return compute0(op, (DoubleConst)right);
/*    */     }
/* 55 */     return null;
/*    */   }
/*    */   
/*    */   private DoubleConst compute0(int op, DoubleConst right) {
/*    */     int newType;
/* 60 */     if (this.type == 405 || right.type == 405) {
/*    */       
/* 62 */       newType = 405;
/*    */     } else {
/* 64 */       newType = 404;
/*    */     } 
/* 66 */     return compute(op, this.value, right.value, newType);
/*    */   }
/*    */   
/*    */   private DoubleConst compute0(int op, IntConst right) {
/* 70 */     return compute(op, this.value, right.value, this.type);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static DoubleConst compute(int op, double value1, double value2, int newType) {
/*    */     double newValue;
/* 77 */     switch (op) {
/*    */       case 43:
/* 79 */         newValue = value1 + value2;
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
/* 97 */         return new DoubleConst(newValue, newType);case 45: newValue = value1 - value2; return new DoubleConst(newValue, newType);case 42: newValue = value1 * value2; return new DoubleConst(newValue, newType);case 47: newValue = value1 / value2; return new DoubleConst(newValue, newType);case 37: newValue = value1 % value2; return new DoubleConst(newValue, newType);
/*    */     } 
/*    */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\DoubleConst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */