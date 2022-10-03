/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CtClass;
/*    */ import javassist.CtField;
/*    */ import javassist.bytecode.BadBytecode;
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
/*    */ public final class TransformWriteField
/*    */   extends TransformReadField
/*    */ {
/*    */   public TransformWriteField(Transformer next, CtField field, String methodClassname, String methodName) {
/* 30 */     super(next, field, methodClassname, methodName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/* 37 */     int c = iterator.byteAt(pos);
/* 38 */     if (c == 181 || c == 179) {
/* 39 */       int index = iterator.u16bitAt(pos + 1);
/* 40 */       String typedesc = isField(tclazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
/*    */       
/* 42 */       if (typedesc != null) {
/* 43 */         if (c == 179) {
/* 44 */           CodeAttribute ca = iterator.get();
/* 45 */           iterator.move(pos);
/* 46 */           char c0 = typedesc.charAt(0);
/* 47 */           if (c0 == 'J' || c0 == 'D') {
/*    */             
/* 49 */             pos = iterator.insertGap(3);
/* 50 */             iterator.writeByte(1, pos);
/* 51 */             iterator.writeByte(91, pos + 1);
/* 52 */             iterator.writeByte(87, pos + 2);
/* 53 */             ca.setMaxStack(ca.getMaxStack() + 2);
/*    */           }
/*    */           else {
/*    */             
/* 57 */             pos = iterator.insertGap(2);
/* 58 */             iterator.writeByte(1, pos);
/* 59 */             iterator.writeByte(95, pos + 1);
/* 60 */             ca.setMaxStack(ca.getMaxStack() + 1);
/*    */           } 
/*    */           
/* 63 */           pos = iterator.next();
/*    */         } 
/*    */         
/* 66 */         int mi = cp.addClassInfo(this.methodClassname);
/* 67 */         String type = "(Ljava/lang/Object;" + typedesc + ")V";
/* 68 */         int methodref = cp.addMethodrefInfo(mi, this.methodName, type);
/* 69 */         iterator.writeByte(184, pos);
/* 70 */         iterator.write16bit(methodref, pos + 1);
/*    */       } 
/*    */     } 
/*    */     
/* 74 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformWriteField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */