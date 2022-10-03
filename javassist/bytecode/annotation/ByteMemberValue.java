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
/*     */ public class ByteMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public ByteMemberValue(int index, ConstPool cp) {
/*  40 */     super('B', cp);
/*  41 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteMemberValue(byte b, ConstPool cp) {
/*  50 */     super('B', cp);
/*  51 */     setValue(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteMemberValue(ConstPool cp) {
/*  58 */     super('B', cp);
/*  59 */     setValue((byte)0);
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
/*  64 */     return Byte.valueOf(getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getType(ClassLoader cl) {
/*  69 */     return byte.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getValue() {
/*  76 */     return (byte)this.cp.getIntegerInfo(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(byte newValue) {
/*  83 */     this.valueIndex = this.cp.addIntegerInfo(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     return Byte.toString(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/*  99 */     writer.constValueIndex(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 107 */     visitor.visitByteMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\ByteMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */