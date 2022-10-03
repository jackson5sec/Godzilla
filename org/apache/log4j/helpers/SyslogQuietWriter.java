/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.io.Writer;
/*    */ import org.apache.log4j.spi.ErrorHandler;
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
/*    */ public class SyslogQuietWriter
/*    */   extends QuietWriter
/*    */ {
/*    */   int syslogFacility;
/*    */   int level;
/*    */   
/*    */   public SyslogQuietWriter(Writer writer, int syslogFacility, ErrorHandler eh) {
/* 38 */     super(writer, eh);
/* 39 */     this.syslogFacility = syslogFacility;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLevel(int level) {
/* 44 */     this.level = level;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSyslogFacility(int syslogFacility) {
/* 49 */     this.syslogFacility = syslogFacility;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(String string) {
/* 54 */     super.write("<" + (this.syslogFacility | this.level) + ">" + string);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\helpers\SyslogQuietWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */