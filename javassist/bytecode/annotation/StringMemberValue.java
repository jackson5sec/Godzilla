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
/*     */ public class StringMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public StringMemberValue(int index, ConstPool cp) {
/*  41 */     super('s', cp);
/*  42 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMemberValue(String str, ConstPool cp) {
/*  51 */     super('s', cp);
/*  52 */     setValue(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMemberValue(ConstPool cp) {
/*  59 */     super('s', cp);
/*  60 */     setValue("");
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
/*  65 */     return getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getType(ClassLoader cl) {
/*  70 */     return String.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  77 */     return this.cp.getUtf8Info(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String newValue) {
/*  84 */     this.valueIndex = this.cp.addUtf8Info(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     return "\"" + getValue() + "\"";
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
/* 108 */     visitor.visitStringMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\StringMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */