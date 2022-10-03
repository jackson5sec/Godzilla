/*     */ package javassist;
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
/*     */ public final class CtPrimitiveType
/*     */   extends CtClass
/*     */ {
/*     */   private char descriptor;
/*     */   private String wrapperName;
/*     */   private String getMethodName;
/*     */   private String mDescriptor;
/*     */   private int returnOp;
/*     */   private int arrayType;
/*     */   private int dataSize;
/*     */   
/*     */   CtPrimitiveType(String name, char desc, String wrapper, String methodName, String mDesc, int opcode, int atype, int size) {
/*  35 */     super(name);
/*  36 */     this.descriptor = desc;
/*  37 */     this.wrapperName = wrapper;
/*  38 */     this.getMethodName = methodName;
/*  39 */     this.mDescriptor = mDesc;
/*  40 */     this.returnOp = opcode;
/*  41 */     this.arrayType = atype;
/*  42 */     this.dataSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimitive() {
/*  51 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getModifiers() {
/*  61 */     return 17;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getDescriptor() {
/*  68 */     return this.descriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWrapperName() {
/*  75 */     return this.wrapperName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGetMethodName() {
/*  83 */     return this.getMethodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGetMethodDescriptor() {
/*  91 */     return this.mDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReturnOp() {
/*  98 */     return this.returnOp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArrayType() {
/* 106 */     return this.arrayType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDataSize() {
/* 113 */     return this.dataSize;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtPrimitiveType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */