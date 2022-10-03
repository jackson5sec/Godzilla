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
/*     */ public class CharMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public CharMemberValue(int index, ConstPool cp) {
/*  41 */     super('C', cp);
/*  42 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharMemberValue(char c, ConstPool cp) {
/*  51 */     super('C', cp);
/*  52 */     setValue(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharMemberValue(ConstPool cp) {
/*  59 */     super('C', cp);
/*  60 */     setValue(false);
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
/*  65 */     return Character.valueOf(getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getType(ClassLoader cl) {
/*  70 */     return char.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getValue() {
/*  77 */     return (char)this.cp.getIntegerInfo(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(char newValue) {
/*  84 */     this.valueIndex = this.cp.addIntegerInfo(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     return Character.toString(getValue());
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
/* 108 */     visitor.visitCharMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\CharMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */