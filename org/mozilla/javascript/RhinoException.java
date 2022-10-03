/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RhinoException
/*     */   extends RuntimeException
/*     */ {
/*  24 */   private static final Pattern JAVA_STACK_PATTERN = Pattern.compile("_c_(.*)_\\d+");
/*     */   static final long serialVersionUID = 1883500631321581169L;
/*     */   
/*     */   RhinoException() {
/*  28 */     Evaluator e = Context.createInterpreter();
/*  29 */     if (e != null) {
/*  30 */       e.captureStackInfo(this);
/*     */     }
/*     */   }
/*     */   
/*     */   RhinoException(String details) {
/*  35 */     super(details);
/*  36 */     Evaluator e = Context.createInterpreter();
/*  37 */     if (e != null) {
/*  38 */       e.captureStackInfo(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getMessage() {
/*  44 */     String details = details();
/*  45 */     if (this.sourceName == null || this.lineNumber <= 0) {
/*  46 */       return details;
/*     */     }
/*  48 */     StringBuilder buf = new StringBuilder(details);
/*  49 */     buf.append(" (");
/*  50 */     if (this.sourceName != null) {
/*  51 */       buf.append(this.sourceName);
/*     */     }
/*  53 */     if (this.lineNumber > 0) {
/*  54 */       buf.append('#');
/*  55 */       buf.append(this.lineNumber);
/*     */     } 
/*  57 */     buf.append(')');
/*  58 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String details() {
/*  63 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String sourceName() {
/*  72 */     return this.sourceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void initSourceName(String sourceName) {
/*  85 */     if (sourceName == null) throw new IllegalArgumentException(); 
/*  86 */     if (this.sourceName != null) throw new IllegalStateException(); 
/*  87 */     this.sourceName = sourceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int lineNumber() {
/*  96 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void initLineNumber(int lineNumber) {
/* 109 */     if (lineNumber <= 0) throw new IllegalArgumentException(String.valueOf(lineNumber)); 
/* 110 */     if (this.lineNumber > 0) throw new IllegalStateException(); 
/* 111 */     this.lineNumber = lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int columnNumber() {
/* 119 */     return this.columnNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void initColumnNumber(int columnNumber) {
/* 132 */     if (columnNumber <= 0) throw new IllegalArgumentException(String.valueOf(columnNumber)); 
/* 133 */     if (this.columnNumber > 0) throw new IllegalStateException(); 
/* 134 */     this.columnNumber = columnNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String lineSource() {
/* 142 */     return this.lineSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void initLineSource(String lineSource) {
/* 155 */     if (lineSource == null) throw new IllegalArgumentException(); 
/* 156 */     if (this.lineSource != null) throw new IllegalStateException(); 
/* 157 */     this.lineSource = lineSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void recordErrorOrigin(String sourceName, int lineNumber, String lineSource, int columnNumber) {
/* 164 */     if (lineNumber == -1) {
/* 165 */       lineNumber = 0;
/*     */     }
/*     */     
/* 168 */     if (sourceName != null) {
/* 169 */       initSourceName(sourceName);
/*     */     }
/* 171 */     if (lineNumber != 0) {
/* 172 */       initLineNumber(lineNumber);
/*     */     }
/* 174 */     if (lineSource != null) {
/* 175 */       initLineSource(lineSource);
/*     */     }
/* 177 */     if (columnNumber != 0) {
/* 178 */       initColumnNumber(columnNumber);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String generateStackTrace() {
/* 185 */     CharArrayWriter writer = new CharArrayWriter();
/* 186 */     super.printStackTrace(new PrintWriter(writer));
/* 187 */     String origStackTrace = writer.toString();
/* 188 */     Evaluator e = Context.createInterpreter();
/* 189 */     if (e != null)
/* 190 */       return e.getPatchedStack(this, origStackTrace); 
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptStackTrace() {
/* 204 */     return getScriptStackTrace(-1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptStackTrace(int limit, String functionName) {
/* 223 */     ScriptStackElement[] stack = getScriptStack(limit, functionName);
/* 224 */     return formatStackTrace(stack, details());
/*     */   }
/*     */ 
/*     */   
/*     */   static String formatStackTrace(ScriptStackElement[] stack, String message) {
/* 229 */     StringBuilder buffer = new StringBuilder();
/* 230 */     String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
/*     */     
/* 232 */     if (stackStyle == StackStyle.V8 && !"null".equals(message)) {
/*     */       
/* 234 */       buffer.append(message);
/* 235 */       buffer.append(lineSeparator);
/*     */     } 
/*     */     
/* 238 */     for (ScriptStackElement elem : stack) {
/* 239 */       switch (stackStyle) {
/*     */         case MOZILLA:
/* 241 */           elem.renderMozillaStyle(buffer);
/*     */           break;
/*     */         case V8:
/* 244 */           elem.renderV8Style(buffer);
/*     */           break;
/*     */         case RHINO:
/* 247 */           elem.renderJavaStyle(buffer);
/*     */           break;
/*     */       } 
/* 250 */       buffer.append(lineSeparator);
/*     */     } 
/* 252 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getScriptStackTrace(FilenameFilter filter) {
/* 267 */     return getScriptStackTrace();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptStackElement[] getScriptStack() {
/* 280 */     return getScriptStack(-1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptStackElement[] getScriptStack(int limit, String hideFunction) {
/* 296 */     List<ScriptStackElement> list = new ArrayList<ScriptStackElement>();
/* 297 */     ScriptStackElement[][] interpreterStack = (ScriptStackElement[][])null;
/* 298 */     if (this.interpreterStackInfo != null) {
/* 299 */       Evaluator interpreter = Context.createInterpreter();
/* 300 */       if (interpreter instanceof Interpreter) {
/* 301 */         interpreterStack = ((Interpreter)interpreter).getScriptStackElements(this);
/*     */       }
/*     */     } 
/* 304 */     int interpreterStackIndex = 0;
/* 305 */     StackTraceElement[] stack = getStackTrace();
/* 306 */     int count = 0;
/* 307 */     boolean printStarted = (hideFunction == null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     for (StackTraceElement e : stack) {
/* 313 */       String fileName = e.getFileName();
/* 314 */       if (e.getMethodName().startsWith("_c_") && e.getLineNumber() > -1 && fileName != null && !fileName.endsWith(".java")) {
/*     */ 
/*     */ 
/*     */         
/* 318 */         String methodName = e.getMethodName();
/* 319 */         Matcher match = JAVA_STACK_PATTERN.matcher(methodName);
/*     */ 
/*     */         
/* 322 */         methodName = (!"_c_script_0".equals(methodName) && match.find()) ? match.group(1) : null;
/*     */ 
/*     */         
/* 325 */         if (!printStarted && hideFunction.equals(methodName)) {
/* 326 */           printStarted = true;
/* 327 */         } else if (printStarted && (limit < 0 || count < limit)) {
/* 328 */           list.add(new ScriptStackElement(fileName, methodName, e.getLineNumber()));
/* 329 */           count++;
/*     */         }
/*     */       
/* 332 */       } else if ("org.mozilla.javascript.Interpreter".equals(e.getClassName()) && "interpretLoop".equals(e.getMethodName()) && interpreterStack != null && interpreterStack.length > interpreterStackIndex) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 337 */         for (ScriptStackElement elem : interpreterStack[interpreterStackIndex++]) {
/* 338 */           if (!printStarted && hideFunction.equals(elem.functionName)) {
/* 339 */             printStarted = true;
/* 340 */           } else if (printStarted && (limit < 0 || count < limit)) {
/* 341 */             list.add(elem);
/* 342 */             count++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 347 */     return list.<ScriptStackElement>toArray(new ScriptStackElement[list.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter s) {
/* 354 */     if (this.interpreterStackInfo == null) {
/* 355 */       super.printStackTrace(s);
/*     */     } else {
/* 357 */       s.print(generateStackTrace());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream s) {
/* 364 */     if (this.interpreterStackInfo == null) {
/* 365 */       super.printStackTrace(s);
/*     */     } else {
/* 367 */       s.print(generateStackTrace());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean usesMozillaStackStyle() {
/* 382 */     return (stackStyle == StackStyle.MOZILLA);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void useMozillaStackStyle(boolean flag) {
/* 397 */     stackStyle = flag ? StackStyle.MOZILLA : StackStyle.RHINO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setStackStyle(StackStyle style) {
/* 408 */     stackStyle = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StackStyle getStackStyle() {
/* 416 */     return stackStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 422 */   private static StackStyle stackStyle = StackStyle.RHINO;
/*     */   
/*     */   private String sourceName;
/*     */   
/*     */   private int lineNumber;
/*     */   
/*     */   private String lineSource;
/*     */   private int columnNumber;
/*     */   Object interpreterStackInfo;
/*     */   int[] interpreterLineData;
/*     */   
/*     */   static {
/* 434 */     String style = System.getProperty("rhino.stack.style");
/* 435 */     if (style != null)
/* 436 */       if ("Rhino".equalsIgnoreCase(style)) {
/* 437 */         stackStyle = StackStyle.RHINO;
/* 438 */       } else if ("Mozilla".equalsIgnoreCase(style)) {
/* 439 */         stackStyle = StackStyle.MOZILLA;
/* 440 */       } else if ("V8".equalsIgnoreCase(style)) {
/* 441 */         stackStyle = StackStyle.V8;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\RhinoException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */