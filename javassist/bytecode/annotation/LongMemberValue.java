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
/*     */ public class LongMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public LongMemberValue(int index, ConstPool cp) {
/*  41 */     super('J', cp);
/*  42 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongMemberValue(long j, ConstPool cp) {
/*  51 */     super('J', cp);
/*  52 */     setValue(j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongMemberValue(ConstPool cp) {
/*  59 */     super('J', cp);
/*  60 */     setValue(0L);
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
/*  65 */     return Long.valueOf(getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getType(ClassLoader cl) {
/*  70 */     return long.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValue() {
/*  77 */     return this.cp.getLongInfo(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(long newValue) {
/*  84 */     this.valueIndex = this.cp.addLongInfo(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     return Long.toString(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 100 */     writer.constValueIndex(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 108 */     visitor.visitLongMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\LongMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */