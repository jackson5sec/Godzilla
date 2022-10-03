/*     */ package org.mozilla.javascript.tools;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ErrorReporter;
/*     */ import org.mozilla.javascript.EvaluatorException;
/*     */ import org.mozilla.javascript.RhinoException;
/*     */ import org.mozilla.javascript.SecurityUtilities;
/*     */ import org.mozilla.javascript.WrappedException;
/*     */ 
/*     */ public class ToolErrorReporter
/*     */   implements ErrorReporter {
/*     */   private static final String messagePrefix = "js: ";
/*     */   private boolean hasReportedErrorFlag;
/*     */   private boolean reportWarnings;
/*     */   private PrintStream err;
/*     */   
/*     */   public ToolErrorReporter(boolean reportWarnings) {
/*  23 */     this(reportWarnings, System.err);
/*     */   }
/*     */   
/*     */   public ToolErrorReporter(boolean reportWarnings, PrintStream err) {
/*  27 */     this.reportWarnings = reportWarnings;
/*  28 */     this.err = err;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMessage(String messageId) {
/*  37 */     return getMessage(messageId, (Object[])null);
/*     */   }
/*     */   
/*     */   public static String getMessage(String messageId, String argument) {
/*  41 */     Object[] args = { argument };
/*  42 */     return getMessage(messageId, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getMessage(String messageId, Object arg1, Object arg2) {
/*  47 */     Object[] args = { arg1, arg2 };
/*  48 */     return getMessage(messageId, args);
/*     */   }
/*     */   public static String getMessage(String messageId, Object[] args) {
/*     */     String formatString;
/*  52 */     Context cx = Context.getCurrentContext();
/*  53 */     Locale locale = (cx == null) ? Locale.getDefault() : cx.getLocale();
/*     */ 
/*     */     
/*  56 */     ResourceBundle rb = ResourceBundle.getBundle("org.mozilla.javascript.tools.resources.Messages", locale);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  61 */       formatString = rb.getString(messageId);
/*  62 */     } catch (MissingResourceException mre) {
/*  63 */       throw new RuntimeException("no message resource found for message property " + messageId);
/*     */     } 
/*     */ 
/*     */     
/*  67 */     if (args == null) {
/*  68 */       return formatString;
/*     */     }
/*  70 */     MessageFormat formatter = new MessageFormat(formatString);
/*  71 */     return formatter.format(args);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getExceptionMessage(RhinoException ex) {
/*     */     String msg;
/*  78 */     if (ex instanceof org.mozilla.javascript.JavaScriptException) {
/*  79 */       msg = getMessage("msg.uncaughtJSException", ex.details());
/*  80 */     } else if (ex instanceof org.mozilla.javascript.EcmaError) {
/*  81 */       msg = getMessage("msg.uncaughtEcmaError", ex.details());
/*  82 */     } else if (ex instanceof EvaluatorException) {
/*  83 */       msg = ex.details();
/*     */     } else {
/*  85 */       msg = ex.toString();
/*     */     } 
/*  87 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
/*  93 */     if (!this.reportWarnings)
/*     */       return; 
/*  95 */     reportErrorMessage(message, sourceName, line, lineSource, lineOffset, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
/* 102 */     this.hasReportedErrorFlag = true;
/* 103 */     reportErrorMessage(message, sourceName, line, lineSource, lineOffset, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
/* 111 */     return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasReportedError() {
/* 116 */     return this.hasReportedErrorFlag;
/*     */   }
/*     */   
/*     */   public boolean isReportingWarnings() {
/* 120 */     return this.reportWarnings;
/*     */   }
/*     */   
/*     */   public void setIsReportingWarnings(boolean reportWarnings) {
/* 124 */     this.reportWarnings = reportWarnings;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reportException(ErrorReporter er, RhinoException ex) {
/* 129 */     if (er instanceof ToolErrorReporter) {
/* 130 */       ((ToolErrorReporter)er).reportException(ex);
/*     */     } else {
/* 132 */       String msg = getExceptionMessage(ex);
/* 133 */       er.error(msg, ex.sourceName(), ex.lineNumber(), ex.lineSource(), ex.columnNumber());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reportException(RhinoException ex) {
/* 140 */     if (ex instanceof WrappedException) {
/* 141 */       WrappedException we = (WrappedException)ex;
/* 142 */       we.printStackTrace(this.err);
/*     */     } else {
/* 144 */       String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
/*     */       
/* 146 */       String msg = getExceptionMessage(ex) + lineSeparator + ex.getScriptStackTrace();
/*     */       
/* 148 */       reportErrorMessage(msg, ex.sourceName(), ex.lineNumber(), ex.lineSource(), ex.columnNumber(), false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportErrorMessage(String message, String sourceName, int line, String lineSource, int lineOffset, boolean justWarning) {
/* 157 */     if (line > 0) {
/* 158 */       String lineStr = String.valueOf(line);
/* 159 */       if (sourceName != null) {
/* 160 */         Object[] args = { sourceName, lineStr, message };
/* 161 */         message = getMessage("msg.format3", args);
/*     */       } else {
/* 163 */         Object[] args = { lineStr, message };
/* 164 */         message = getMessage("msg.format2", args);
/*     */       } 
/*     */     } else {
/* 167 */       Object[] args = { message };
/* 168 */       message = getMessage("msg.format1", args);
/*     */     } 
/* 170 */     if (justWarning) {
/* 171 */       message = getMessage("msg.warning", message);
/*     */     }
/* 173 */     this.err.println("js: " + message);
/* 174 */     if (null != lineSource) {
/* 175 */       this.err.println("js: " + lineSource);
/* 176 */       this.err.println("js: " + buildIndicator(lineOffset));
/*     */     } 
/*     */   }
/*     */   
/*     */   private String buildIndicator(int offset) {
/* 181 */     StringBuilder sb = new StringBuilder();
/* 182 */     for (int i = 0; i < offset - 1; i++)
/* 183 */       sb.append("."); 
/* 184 */     sb.append("^");
/* 185 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\ToolErrorReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */