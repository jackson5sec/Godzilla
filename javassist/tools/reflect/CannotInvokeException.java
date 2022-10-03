/*    */ package javassist.tools.reflect;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
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
/*    */ public class CannotInvokeException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 33 */   private Throwable err = null;
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getReason() {
/* 38 */     return this.err;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(String reason) {
/* 44 */     super(reason);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(InvocationTargetException e) {
/* 51 */     super("by " + e.getTargetException().toString());
/* 52 */     this.err = e.getTargetException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(IllegalAccessException e) {
/* 59 */     super("by " + e.toString());
/* 60 */     this.err = e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(ClassNotFoundException e) {
/* 67 */     super("by " + e.toString());
/* 68 */     this.err = e;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\reflect\CannotInvokeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */