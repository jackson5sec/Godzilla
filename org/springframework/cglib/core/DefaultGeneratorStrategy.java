/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import org.springframework.asm.ClassWriter;
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
/*    */ public class DefaultGeneratorStrategy
/*    */   implements GeneratorStrategy
/*    */ {
/* 21 */   public static final DefaultGeneratorStrategy INSTANCE = new DefaultGeneratorStrategy();
/*    */   
/*    */   public byte[] generate(ClassGenerator cg) throws Exception {
/* 24 */     DebuggingClassWriter cw = getClassVisitor();
/* 25 */     transform(cg).generateClass(cw);
/* 26 */     return transform(cw.toByteArray());
/*    */   }
/*    */   
/*    */   protected DebuggingClassWriter getClassVisitor() throws Exception {
/* 30 */     return new DebuggingClassWriter(2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final ClassWriter getClassWriter() {
/* 36 */     throw new UnsupportedOperationException("You are calling getClassWriter, which no longer exists in this cglib version.");
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] transform(byte[] b) throws Exception {
/* 41 */     return b;
/*    */   }
/*    */   
/*    */   protected ClassGenerator transform(ClassGenerator cg) throws Exception {
/* 45 */     return cg;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\DefaultGeneratorStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */