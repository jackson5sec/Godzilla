/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.ClassEmitter;
/*    */ import org.springframework.cglib.core.EmitUtils;
/*    */ import org.springframework.cglib.core.TypeUtils;
/*    */ import org.springframework.cglib.transform.ClassEmitterTransformer;
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
/*    */ public class AddPropertyTransformer
/*    */   extends ClassEmitterTransformer
/*    */ {
/*    */   private final String[] names;
/*    */   private final Type[] types;
/*    */   
/*    */   public AddPropertyTransformer(Map props) {
/* 28 */     int size = props.size();
/* 29 */     this.names = (String[])props.keySet().toArray((Object[])new String[size]);
/* 30 */     this.types = new Type[size];
/* 31 */     for (int i = 0; i < size; i++) {
/* 32 */       this.types[i] = (Type)props.get(this.names[i]);
/*    */     }
/*    */   }
/*    */   
/*    */   public AddPropertyTransformer(String[] names, Type[] types) {
/* 37 */     this.names = names;
/* 38 */     this.types = types;
/*    */   }
/*    */   
/*    */   public void end_class() {
/* 42 */     if (!TypeUtils.isAbstract(getAccess())) {
/* 43 */       EmitUtils.add_properties((ClassEmitter)this, this.names, this.types);
/*    */     }
/* 45 */     super.end_class();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\AddPropertyTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */