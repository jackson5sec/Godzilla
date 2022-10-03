/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
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
/*     */ public class TransformBefore
/*     */   extends TransformCall
/*     */ {
/*     */   protected CtClass[] parameterTypes;
/*     */   protected int locals;
/*     */   protected int maxLocals;
/*     */   protected byte[] saveCode;
/*     */   protected byte[] loadCode;
/*     */   
/*     */   public TransformBefore(Transformer next, CtMethod origMethod, CtMethod beforeMethod) throws NotFoundException {
/*  39 */     super(next, origMethod, beforeMethod);
/*     */ 
/*     */     
/*  42 */     this.methodDescriptor = origMethod.getMethodInfo2().getDescriptor();
/*     */     
/*  44 */     this.parameterTypes = origMethod.getParameterTypes();
/*  45 */     this.locals = 0;
/*  46 */     this.maxLocals = 0;
/*  47 */     this.saveCode = this.loadCode = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(ConstPool cp, CodeAttribute attr) {
/*  52 */     super.initialize(cp, attr);
/*  53 */     this.locals = 0;
/*  54 */     this.maxLocals = attr.getMaxLocals();
/*  55 */     this.saveCode = this.loadCode = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) throws BadBytecode {
/*  62 */     if (this.newIndex == 0) {
/*  63 */       String desc = Descriptor.ofParameters(this.parameterTypes) + 'V';
/*  64 */       desc = Descriptor.insertParameter(this.classname, desc);
/*  65 */       int nt = cp.addNameAndTypeInfo(this.newMethodname, desc);
/*  66 */       int ci = cp.addClassInfo(this.newClassname);
/*  67 */       this.newIndex = cp.addMethodrefInfo(ci, nt);
/*  68 */       this.constPool = cp;
/*     */     } 
/*     */     
/*  71 */     if (this.saveCode == null) {
/*  72 */       makeCode(this.parameterTypes, cp);
/*     */     }
/*  74 */     return match2(pos, iterator);
/*     */   }
/*     */   
/*     */   protected int match2(int pos, CodeIterator iterator) throws BadBytecode {
/*  78 */     iterator.move(pos);
/*  79 */     iterator.insert(this.saveCode);
/*  80 */     iterator.insert(this.loadCode);
/*  81 */     int p = iterator.insertGap(3);
/*  82 */     iterator.writeByte(184, p);
/*  83 */     iterator.write16bit(this.newIndex, p + 1);
/*  84 */     iterator.insert(this.loadCode);
/*  85 */     return iterator.next();
/*     */   }
/*     */   
/*     */   public int extraLocals() {
/*  89 */     return this.locals;
/*     */   }
/*     */   protected void makeCode(CtClass[] paramTypes, ConstPool cp) {
/*  92 */     Bytecode save = new Bytecode(cp, 0, 0);
/*  93 */     Bytecode load = new Bytecode(cp, 0, 0);
/*     */     
/*  95 */     int var = this.maxLocals;
/*  96 */     int len = (paramTypes == null) ? 0 : paramTypes.length;
/*  97 */     load.addAload(var);
/*  98 */     makeCode2(save, load, 0, len, paramTypes, var + 1);
/*  99 */     save.addAstore(var);
/*     */     
/* 101 */     this.saveCode = save.get();
/* 102 */     this.loadCode = load.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeCode2(Bytecode save, Bytecode load, int i, int n, CtClass[] paramTypes, int var) {
/* 108 */     if (i < n) {
/* 109 */       int size = load.addLoad(var, paramTypes[i]);
/* 110 */       makeCode2(save, load, i + 1, n, paramTypes, var + size);
/* 111 */       save.addStore(var, paramTypes[i]);
/*     */     } else {
/*     */       
/* 114 */       this.locals = var - this.maxLocals;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformBefore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */