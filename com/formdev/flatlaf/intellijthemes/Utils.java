/*    */ package com.formdev.flatlaf.intellijthemes;
/*    */ 
/*    */ import com.formdev.flatlaf.FlatLaf;
/*    */ import com.formdev.flatlaf.IntelliJTheme;
/*    */ import java.io.IOException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ class Utils
/*    */ {
/* 30 */   static final Logger LOG = Logger.getLogger(FlatLaf.class.getName());
/*    */   
/*    */   static IntelliJTheme loadTheme(String name) {
/*    */     try {
/* 34 */       return new IntelliJTheme(Utils.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/" + name));
/*    */     }
/* 36 */     catch (IOException ex) {
/* 37 */       String msg = "FlatLaf: Failed to load IntelliJ theme '" + name + "'";
/* 38 */       LOG.log(Level.SEVERE, msg, ex);
/* 39 */       throw new RuntimeException(msg, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\intellijthemes\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */