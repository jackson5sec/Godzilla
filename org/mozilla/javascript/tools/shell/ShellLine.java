/*    */ package org.mozilla.javascript.tools.shell;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import org.mozilla.javascript.Scriptable;
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
/*    */ @Deprecated
/*    */ public class ShellLine
/*    */ {
/*    */   @Deprecated
/*    */   public static InputStream getStream(Scriptable scope) {
/* 24 */     ShellConsole console = ShellConsole.getConsole(scope, Charset.defaultCharset());
/*    */     
/* 26 */     return (console != null) ? console.getIn() : null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\ShellLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */