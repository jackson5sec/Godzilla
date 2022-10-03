/*    */ package org.mozilla.javascript;
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
/*    */ public class NativeJavaConstructor
/*    */   extends BaseFunction
/*    */ {
/*    */   static final long serialVersionUID = -8149253217482668463L;
/*    */   MemberBox ctor;
/*    */   
/*    */   public NativeJavaConstructor(MemberBox ctor) {
/* 31 */     this.ctor = ctor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 38 */     return NativeJavaClass.constructSpecific(cx, scope, args, this.ctor);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFunctionName() {
/* 44 */     String sig = JavaMembers.liveConnectSignature(this.ctor.argTypes);
/* 45 */     return "<init>".concat(sig);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return "[JavaConstructor " + this.ctor.getName() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeJavaConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */