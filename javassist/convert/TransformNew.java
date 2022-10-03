/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtClass;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.StackMap;
/*     */ import javassist.bytecode.StackMapTable;
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
/*     */ public final class TransformNew
/*     */   extends Transformer
/*     */ {
/*     */   private int nested;
/*     */   private String classname;
/*     */   private String trapClass;
/*     */   private String trapMethod;
/*     */   
/*     */   public TransformNew(Transformer next, String classname, String trapClass, String trapMethod) {
/*  34 */     super(next);
/*  35 */     this.classname = classname;
/*  36 */     this.trapClass = trapClass;
/*  37 */     this.trapMethod = trapMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(ConstPool cp, CodeAttribute attr) {
/*  42 */     this.nested = 0;
/*     */   }
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
/*     */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws CannotCompileException {
/*  62 */     int c = iterator.byteAt(pos);
/*  63 */     if (c == 187) {
/*  64 */       int index = iterator.u16bitAt(pos + 1);
/*  65 */       if (cp.getClassInfo(index).equals(this.classname)) {
/*  66 */         if (iterator.byteAt(pos + 3) != 89) {
/*  67 */           throw new CannotCompileException("NEW followed by no DUP was found");
/*     */         }
/*     */         
/*  70 */         iterator.writeByte(0, pos);
/*  71 */         iterator.writeByte(0, pos + 1);
/*  72 */         iterator.writeByte(0, pos + 2);
/*  73 */         iterator.writeByte(0, pos + 3);
/*  74 */         this.nested++;
/*     */ 
/*     */         
/*  77 */         StackMapTable smt = (StackMapTable)iterator.get().getAttribute("StackMapTable");
/*  78 */         if (smt != null) {
/*  79 */           smt.removeNew(pos);
/*     */         }
/*     */         
/*  82 */         StackMap sm = (StackMap)iterator.get().getAttribute("StackMap");
/*  83 */         if (sm != null) {
/*  84 */           sm.removeNew(pos);
/*     */         }
/*     */       } 
/*  87 */     } else if (c == 183) {
/*  88 */       int index = iterator.u16bitAt(pos + 1);
/*  89 */       int typedesc = cp.isConstructor(this.classname, index);
/*  90 */       if (typedesc != 0 && this.nested > 0) {
/*  91 */         int methodref = computeMethodref(typedesc, cp);
/*  92 */         iterator.writeByte(184, pos);
/*  93 */         iterator.write16bit(methodref, pos + 1);
/*  94 */         this.nested--;
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     return pos;
/*     */   }
/*     */   
/*     */   private int computeMethodref(int typedesc, ConstPool cp) {
/* 102 */     int classIndex = cp.addClassInfo(this.trapClass);
/* 103 */     int mnameIndex = cp.addUtf8Info(this.trapMethod);
/* 104 */     typedesc = cp.addUtf8Info(
/* 105 */         Descriptor.changeReturnType(this.classname, cp
/* 106 */           .getUtf8Info(typedesc)));
/* 107 */     return cp.addMethodrefInfo(classIndex, cp
/* 108 */         .addNameAndTypeInfo(mnameIndex, typedesc));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformNew.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */