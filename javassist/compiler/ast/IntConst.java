/*     */ package javassist.compiler.ast;
/*     */ 
/*     */ import javassist.compiler.CompileError;
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
/*     */ public class IntConst
/*     */   extends ASTree
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected long value;
/*     */   protected int type;
/*     */   
/*     */   public IntConst(long v, int tokenId) {
/*  31 */     this.value = v; this.type = tokenId;
/*     */   } public long get() {
/*  33 */     return this.value;
/*     */   } public void set(long v) {
/*  35 */     this.value = v;
/*     */   }
/*     */   
/*     */   public int getType() {
/*  39 */     return this.type;
/*     */   }
/*     */   public String toString() {
/*  42 */     return Long.toString(this.value);
/*     */   }
/*     */   
/*     */   public void accept(Visitor v) throws CompileError {
/*  46 */     v.atIntConst(this);
/*     */   }
/*     */   
/*     */   public ASTree compute(int op, ASTree right) {
/*  50 */     if (right instanceof IntConst)
/*  51 */       return compute0(op, (IntConst)right); 
/*  52 */     if (right instanceof DoubleConst) {
/*  53 */       return compute0(op, (DoubleConst)right);
/*     */     }
/*  55 */     return null;
/*     */   } private IntConst compute0(int op, IntConst right) {
/*     */     int newType;
/*     */     long newValue;
/*  59 */     int type1 = this.type;
/*  60 */     int type2 = right.type;
/*     */     
/*  62 */     if (type1 == 403 || type2 == 403) {
/*  63 */       newType = 403;
/*  64 */     } else if (type1 == 401 && type2 == 401) {
/*     */       
/*  66 */       newType = 401;
/*     */     } else {
/*  68 */       newType = 402;
/*     */     } 
/*  70 */     long value1 = this.value;
/*  71 */     long value2 = right.value;
/*     */     
/*  73 */     switch (op) {
/*     */       case 43:
/*  75 */         newValue = value1 + value2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 114 */         return new IntConst(newValue, newType);case 45: newValue = value1 - value2; return new IntConst(newValue, newType);case 42: newValue = value1 * value2; return new IntConst(newValue, newType);case 47: newValue = value1 / value2; return new IntConst(newValue, newType);case 37: newValue = value1 % value2; return new IntConst(newValue, newType);case 124: newValue = value1 | value2; return new IntConst(newValue, newType);case 94: newValue = value1 ^ value2; return new IntConst(newValue, newType);case 38: newValue = value1 & value2; return new IntConst(newValue, newType);case 364: newValue = this.value << (int)value2; newType = type1; return new IntConst(newValue, newType);case 366: newValue = this.value >> (int)value2; newType = type1; return new IntConst(newValue, newType);case 370: newValue = this.value >>> (int)value2; newType = type1; return new IntConst(newValue, newType);
/*     */     } 
/*     */     return null;
/*     */   } private DoubleConst compute0(int op, DoubleConst right) {
/* 118 */     double newValue, value1 = this.value;
/* 119 */     double value2 = right.value;
/*     */     
/* 121 */     switch (op) {
/*     */       case 43:
/* 123 */         newValue = value1 + value2;
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
/* 141 */         return new DoubleConst(newValue, right.type);case 45: newValue = value1 - value2; return new DoubleConst(newValue, right.type);case 42: newValue = value1 * value2; return new DoubleConst(newValue, right.type);case 47: newValue = value1 / value2; return new DoubleConst(newValue, right.type);case 37: newValue = value1 % value2; return new DoubleConst(newValue, right.type);
/*     */     } 
/*     */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\IntConst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */