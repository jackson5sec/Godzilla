/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CtClass;
/*    */ import javassist.CtField;
/*    */ import javassist.Modifier;
/*    */ import javassist.bytecode.CodeAttribute;
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
/*    */ 
/*    */ 
/*    */ public final class TransformFieldAccess
/*    */   extends Transformer
/*    */ {
/*    */   private String newClassname;
/*    */   private String newFieldname;
/*    */   private String fieldname;
/*    */   private CtClass fieldClass;
/*    */   private boolean isPrivate;
/*    */   private int newIndex;
/*    */   private ConstPool constPool;
/*    */   
/*    */   public TransformFieldAccess(Transformer next, CtField field, String newClassname, String newFieldname) {
/* 39 */     super(next);
/* 40 */     this.fieldClass = field.getDeclaringClass();
/* 41 */     this.fieldname = field.getName();
/* 42 */     this.isPrivate = Modifier.isPrivate(field.getModifiers());
/* 43 */     this.newClassname = newClassname;
/* 44 */     this.newFieldname = newFieldname;
/* 45 */     this.constPool = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initialize(ConstPool cp, CodeAttribute attr) {
/* 50 */     if (this.constPool != cp) {
/* 51 */       this.newIndex = 0;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) {
/* 64 */     int c = iterator.byteAt(pos);
/* 65 */     if (c == 180 || c == 178 || c == 181 || c == 179) {
/*    */       
/* 67 */       int index = iterator.u16bitAt(pos + 1);
/*    */       
/* 69 */       String typedesc = TransformReadField.isField(clazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
/*    */       
/* 71 */       if (typedesc != null) {
/* 72 */         if (this.newIndex == 0) {
/* 73 */           int nt = cp.addNameAndTypeInfo(this.newFieldname, typedesc);
/*    */           
/* 75 */           this.newIndex = cp.addFieldrefInfo(cp
/* 76 */               .addClassInfo(this.newClassname), nt);
/* 77 */           this.constPool = cp;
/*    */         } 
/*    */         
/* 80 */         iterator.write16bit(this.newIndex, pos + 1);
/*    */       } 
/*    */     } 
/*    */     
/* 84 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformFieldAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */