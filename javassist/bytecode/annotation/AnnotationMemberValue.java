/*    */ package javassist.bytecode.annotation;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import javassist.ClassPool;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationMemberValue
/*    */   extends MemberValue
/*    */ {
/*    */   Annotation value;
/*    */   
/*    */   public AnnotationMemberValue(ConstPool cp) {
/* 37 */     this(null, cp);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationMemberValue(Annotation a, ConstPool cp) {
/* 45 */     super('@', cp);
/* 46 */     this.value = a;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
/* 53 */     return AnnotationImpl.make(cl, getType(cl), cp, this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   Class<?> getType(ClassLoader cl) throws ClassNotFoundException {
/* 58 */     if (this.value == null)
/* 59 */       throw new ClassNotFoundException("no type specified"); 
/* 60 */     return loadClass(cl, this.value.getTypeName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Annotation getValue() {
/* 67 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(Annotation newValue) {
/* 74 */     this.value = newValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     return this.value.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(AnnotationsWriter writer) throws IOException {
/* 90 */     writer.annotationValue();
/* 91 */     this.value.write(writer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept(MemberValueVisitor visitor) {
/* 99 */     visitor.visitAnnotationMemberValue(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\AnnotationMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */