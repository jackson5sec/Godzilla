/*    */ package com.jgoodies.common.format;
/*    */ 
/*    */ import com.jgoodies.common.display.Displayable;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.Format;
/*    */ import java.text.ParsePosition;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DisplayableFormat
/*    */   extends Format
/*    */ {
/* 50 */   public static final DisplayableFormat INSTANCE = new DisplayableFormat();
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
/*    */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
/* 72 */     if (obj == null) {
/* 73 */       return toAppendTo;
/*    */     }
/* 75 */     if (!(obj instanceof Displayable)) {
/* 76 */       throw new ClassCastException("The object to format must implement the Displayable interface.");
/*    */     }
/* 78 */     toAppendTo.append(((Displayable)obj).getDisplayString());
/* 79 */     return toAppendTo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object parseObject(String source, ParsePosition pos) {
/* 90 */     throw new UnsupportedOperationException("The DisplayableFormat cannot parse.");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\format\DisplayableFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */