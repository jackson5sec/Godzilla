/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import org.springframework.expression.TypedValue;
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
/*    */ public final class BooleanTypedValue
/*    */   extends TypedValue
/*    */ {
/* 32 */   public static final BooleanTypedValue TRUE = new BooleanTypedValue(true);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final BooleanTypedValue FALSE = new BooleanTypedValue(false);
/*    */ 
/*    */   
/*    */   private BooleanTypedValue(boolean b) {
/* 41 */     super(Boolean.valueOf(b));
/*    */   }
/*    */ 
/*    */   
/*    */   public static BooleanTypedValue forValue(boolean b) {
/* 46 */     return b ? TRUE : FALSE;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\BooleanTypedValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */