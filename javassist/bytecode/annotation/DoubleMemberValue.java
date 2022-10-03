/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import javassist.ClassPool;
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
/*     */ public class DoubleMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public DoubleMemberValue(int index, ConstPool cp) {
/*  42 */     super('D', cp);
/*  43 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleMemberValue(double d, ConstPool cp) {
/*  52 */     super('D', cp);
/*  53 */     setValue(d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleMemberValue(ConstPool cp) {
/*  60 */     super('D', cp);
/*  61 */     setValue(0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
/*  66 */     return Double.valueOf(getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getType(ClassLoader cl) {
/*  71 */     return double.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue() {
/*  78 */     return this.cp.getDoubleInfo(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(double newValue) {
/*  85 */     this.valueIndex = this.cp.addDoubleInfo(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     return Double.toString(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 101 */     writer.constValueIndex(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 109 */     visitor.visitDoubleMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\DoubleMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */