/*    */ package core.c2profile.exception;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ 
/*    */ public class UnsupportedOperationException extends java.lang.UnsupportedOperationException {
/*    */   public UnsupportedOperationException(String message) {
/*  7 */     super(EasyI18N.getI18nString(message));
/*    */   }
/*    */   public UnsupportedOperationException(String format, Object... args) {
/* 10 */     this(String.format(EasyI18N.getI18nString(format), args));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\exception\UnsupportedOperationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */