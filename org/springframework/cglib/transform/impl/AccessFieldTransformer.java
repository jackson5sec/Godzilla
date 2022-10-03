/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.CodeEmitter;
/*    */ import org.springframework.cglib.core.Constants;
/*    */ import org.springframework.cglib.core.Signature;
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
/*    */ 
/*    */ 
/*    */ public class AccessFieldTransformer
/*    */   extends ClassEmitterTransformer
/*    */ {
/*    */   private Callback callback;
/*    */   
/*    */   public AccessFieldTransformer(Callback callback) {
/* 29 */     this.callback = callback;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void declare_field(int access, String name, Type type, Object value) {
/* 37 */     super.declare_field(access, name, type, value);
/*    */     
/* 39 */     String property = TypeUtils.upperFirst(this.callback.getPropertyName(getClassType(), name));
/* 40 */     if (property != null) {
/*    */       
/* 42 */       CodeEmitter e = begin_method(1, new Signature("get" + property, type, Constants.TYPES_EMPTY), null);
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 47 */       e.load_this();
/* 48 */       e.getfield(name);
/* 49 */       e.return_value();
/* 50 */       e.end_method();
/*    */       
/* 52 */       e = begin_method(1, new Signature("set" + property, Type.VOID_TYPE, new Type[] { type }), null);
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 57 */       e.load_this();
/* 58 */       e.load_arg(0);
/* 59 */       e.putfield(name);
/* 60 */       e.return_value();
/* 61 */       e.end_method();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static interface Callback {
/*    */     String getPropertyName(Type param1Type, String param1String);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\AccessFieldTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */