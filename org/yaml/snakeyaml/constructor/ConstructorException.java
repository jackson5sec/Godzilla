/*    */ package org.yaml.snakeyaml.constructor;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
/*    */ import org.yaml.snakeyaml.error.MarkedYAMLException;
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
/*    */ public class ConstructorException
/*    */   extends MarkedYAMLException
/*    */ {
/*    */   private static final long serialVersionUID = -8816339931365239910L;
/*    */   
/*    */   protected ConstructorException(String context, Mark contextMark, String problem, Mark problemMark, Throwable cause) {
/* 26 */     super(context, contextMark, problem, problemMark, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ConstructorException(String context, Mark contextMark, String problem, Mark problemMark) {
/* 31 */     this(context, contextMark, problem, problemMark, null);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\constructor\ConstructorException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */