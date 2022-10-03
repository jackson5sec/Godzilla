/*    */ package net.miginfocom.layout;
/*    */ 
/*    */ import java.io.Externalizable;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInput;
/*    */ import java.io.ObjectOutput;
/*    */ import java.io.ObjectStreamException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ResizeConstraint
/*    */   implements Externalizable
/*    */ {
/* 43 */   static final Float WEIGHT_100 = Float.valueOf(100.0F);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   Float grow = null;
/*    */ 
/*    */ 
/*    */   
/* 55 */   int growPrio = 100;
/*    */   
/* 57 */   Float shrink = WEIGHT_100;
/*    */   
/* 59 */   int shrinkPrio = 100;
/*    */ 
/*    */ 
/*    */   
/*    */   public ResizeConstraint() {}
/*    */ 
/*    */   
/*    */   ResizeConstraint(int shrinkPrio, Float shrinkWeight, int growPrio, Float growWeight) {
/* 67 */     this.shrinkPrio = shrinkPrio;
/* 68 */     this.shrink = shrinkWeight;
/* 69 */     this.growPrio = growPrio;
/* 70 */     this.grow = growWeight;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object readResolve() throws ObjectStreamException {
/* 79 */     return LayoutUtil.getSerializedObject(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 85 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeExternal(ObjectOutput out) throws IOException {
/* 91 */     if (getClass() == ResizeConstraint.class)
/* 92 */       LayoutUtil.writeAsXML(out, this); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\ResizeConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */