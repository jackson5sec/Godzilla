/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.ClassPool;
/*    */ import javassist.CtClass;
/*    */ import javassist.CtField;
/*    */ import javassist.Modifier;
/*    */ import javassist.NotFoundException;
/*    */ import javassist.bytecode.BadBytecode;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.ConstPool;
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
/*    */ public class TransformReadField
/*    */   extends Transformer
/*    */ {
/*    */   protected String fieldname;
/*    */   protected CtClass fieldClass;
/*    */   protected boolean isPrivate;
/*    */   protected String methodClassname;
/*    */   protected String methodName;
/*    */   
/*    */   public TransformReadField(Transformer next, CtField field, String methodClassname, String methodName) {
/* 37 */     super(next);
/* 38 */     this.fieldClass = field.getDeclaringClass();
/* 39 */     this.fieldname = field.getName();
/* 40 */     this.methodClassname = methodClassname;
/* 41 */     this.methodName = methodName;
/* 42 */     this.isPrivate = Modifier.isPrivate(field.getModifiers());
/*    */   }
/*    */ 
/*    */   
/*    */   static String isField(ClassPool pool, ConstPool cp, CtClass fclass, String fname, boolean is_private, int index) {
/* 47 */     if (!cp.getFieldrefName(index).equals(fname)) {
/* 48 */       return null;
/*    */     }
/*    */     try {
/* 51 */       CtClass c = pool.get(cp.getFieldrefClassName(index));
/* 52 */       if (c == fclass || (!is_private && isFieldInSuper(c, fclass, fname))) {
/* 53 */         return cp.getFieldrefType(index);
/*    */       }
/* 55 */     } catch (NotFoundException notFoundException) {}
/* 56 */     return null;
/*    */   }
/*    */   
/*    */   static boolean isFieldInSuper(CtClass clazz, CtClass fclass, String fname) {
/* 60 */     if (!clazz.subclassOf(fclass)) {
/* 61 */       return false;
/*    */     }
/*    */     try {
/* 64 */       CtField f = clazz.getField(fname);
/* 65 */       return (f.getDeclaringClass() == fclass);
/*    */     }
/* 67 */     catch (NotFoundException notFoundException) {
/* 68 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/* 75 */     int c = iterator.byteAt(pos);
/* 76 */     if (c == 180 || c == 178) {
/* 77 */       int index = iterator.u16bitAt(pos + 1);
/* 78 */       String typedesc = isField(tclazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
/*    */       
/* 80 */       if (typedesc != null) {
/* 81 */         if (c == 178) {
/* 82 */           iterator.move(pos);
/* 83 */           pos = iterator.insertGap(1);
/* 84 */           iterator.writeByte(1, pos);
/* 85 */           pos = iterator.next();
/*    */         } 
/*    */         
/* 88 */         String type = "(Ljava/lang/Object;)" + typedesc;
/* 89 */         int mi = cp.addClassInfo(this.methodClassname);
/* 90 */         int methodref = cp.addMethodrefInfo(mi, this.methodName, type);
/* 91 */         iterator.writeByte(184, pos);
/* 92 */         iterator.write16bit(methodref, pos + 1);
/* 93 */         return pos;
/*    */       } 
/*    */     } 
/*    */     
/* 97 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformReadField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */