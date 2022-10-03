/*    */ package org.apache.log4j.chainsaw;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import org.apache.log4j.Logger;
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
/*    */ class ExitAction
/*    */   extends AbstractAction
/*    */ {
/* 33 */   private static final Logger LOG = Logger.getLogger(ExitAction.class);
/*    */   
/* 35 */   public static final ExitAction INSTANCE = new ExitAction();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void actionPerformed(ActionEvent aIgnore) {
/* 45 */     LOG.info("shutting down");
/* 46 */     System.exit(0);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\chainsaw\ExitAction.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */