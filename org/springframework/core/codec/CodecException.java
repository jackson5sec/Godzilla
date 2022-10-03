/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class CodecException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   public CodecException(String msg) {
/* 38 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CodecException(String msg, @Nullable Throwable cause) {
/* 47 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\CodecException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */