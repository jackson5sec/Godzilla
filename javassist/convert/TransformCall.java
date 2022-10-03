/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ public class TransformCall
/*     */   extends Transformer
/*     */ {
/*     */   protected String classname;
/*     */   protected String methodname;
/*     */   protected String methodDescriptor;
/*     */   protected String newClassname;
/*     */   protected String newMethodname;
/*     */   protected boolean newMethodIsPrivate;
/*     */   protected int newIndex;
/*     */   protected ConstPool constPool;
/*     */   
/*     */   public TransformCall(Transformer next, CtMethod origMethod, CtMethod substMethod) {
/*  41 */     this(next, origMethod.getName(), substMethod);
/*  42 */     this.classname = origMethod.getDeclaringClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TransformCall(Transformer next, String oldMethodName, CtMethod substMethod) {
/*  48 */     super(next);
/*  49 */     this.methodname = oldMethodName;
/*  50 */     this.methodDescriptor = substMethod.getMethodInfo2().getDescriptor();
/*  51 */     this.classname = this.newClassname = substMethod.getDeclaringClass().getName();
/*  52 */     this.newMethodname = substMethod.getName();
/*  53 */     this.constPool = null;
/*  54 */     this.newMethodIsPrivate = Modifier.isPrivate(substMethod.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(ConstPool cp, CodeAttribute attr) {
/*  59 */     if (this.constPool != cp) {
/*  60 */       this.newIndex = 0;
/*     */     }
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
/*     */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/*  74 */     int c = iterator.byteAt(pos);
/*  75 */     if (c == 185 || c == 183 || c == 184 || c == 182) {
/*     */       
/*  77 */       int index = iterator.u16bitAt(pos + 1);
/*  78 */       String cname = cp.eqMember(this.methodname, this.methodDescriptor, index);
/*  79 */       if (cname != null && matchClass(cname, clazz.getClassPool())) {
/*  80 */         int ntinfo = cp.getMemberNameAndType(index);
/*  81 */         pos = match(c, pos, iterator, cp
/*  82 */             .getNameAndTypeDescriptor(ntinfo), cp);
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     return pos;
/*     */   }
/*     */   
/*     */   private boolean matchClass(String name, ClassPool pool) {
/*  90 */     if (this.classname.equals(name)) {
/*  91 */       return true;
/*     */     }
/*     */     try {
/*  94 */       CtClass clazz = pool.get(name);
/*  95 */       CtClass declClazz = pool.get(this.classname);
/*  96 */       if (clazz.subtypeOf(declClazz)) {
/*     */         try {
/*  98 */           CtMethod m = clazz.getMethod(this.methodname, this.methodDescriptor);
/*  99 */           return m.getDeclaringClass().getName().equals(this.classname);
/*     */         }
/* 101 */         catch (NotFoundException e) {
/*     */           
/* 103 */           return true;
/*     */         } 
/*     */       }
/* 106 */     } catch (NotFoundException e) {
/* 107 */       return false;
/*     */     } 
/*     */     
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) throws BadBytecode {
/* 116 */     if (this.newIndex == 0) {
/* 117 */       int nt = cp.addNameAndTypeInfo(cp.addUtf8Info(this.newMethodname), typedesc);
/*     */       
/* 119 */       int ci = cp.addClassInfo(this.newClassname);
/* 120 */       if (c == 185) {
/* 121 */         this.newIndex = cp.addInterfaceMethodrefInfo(ci, nt);
/*     */       } else {
/* 123 */         if (this.newMethodIsPrivate && c == 182) {
/* 124 */           iterator.writeByte(183, pos);
/*     */         }
/* 126 */         this.newIndex = cp.addMethodrefInfo(ci, nt);
/*     */       } 
/*     */       
/* 129 */       this.constPool = cp;
/*     */     } 
/*     */     
/* 132 */     iterator.write16bit(this.newIndex, pos + 1);
/* 133 */     return pos;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformCall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */