/*      */ package org.mozilla.javascript.tools.debugger;
/*      */ 
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.mozilla.javascript.Callable;
/*      */ import org.mozilla.javascript.Context;
/*      */ import org.mozilla.javascript.ContextAction;
/*      */ import org.mozilla.javascript.ContextFactory;
/*      */ import org.mozilla.javascript.ImporterTopLevel;
/*      */ import org.mozilla.javascript.Kit;
/*      */ import org.mozilla.javascript.ObjArray;
/*      */ import org.mozilla.javascript.ScriptRuntime;
/*      */ import org.mozilla.javascript.Scriptable;
/*      */ import org.mozilla.javascript.ScriptableObject;
/*      */ import org.mozilla.javascript.Undefined;
/*      */ import org.mozilla.javascript.debug.DebugFrame;
/*      */ import org.mozilla.javascript.debug.DebuggableObject;
/*      */ import org.mozilla.javascript.debug.DebuggableScript;
/*      */ import org.mozilla.javascript.debug.Debugger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Dim
/*      */ {
/*      */   public static final int STEP_OVER = 0;
/*      */   public static final int STEP_INTO = 1;
/*      */   public static final int STEP_OUT = 2;
/*      */   public static final int GO = 3;
/*      */   public static final int BREAK = 4;
/*      */   public static final int EXIT = 5;
/*      */   private static final int IPROXY_DEBUG = 0;
/*      */   private static final int IPROXY_LISTEN = 1;
/*      */   private static final int IPROXY_COMPILE_SCRIPT = 2;
/*      */   private static final int IPROXY_EVAL_SCRIPT = 3;
/*      */   private static final int IPROXY_STRING_IS_COMPILABLE = 4;
/*      */   private static final int IPROXY_OBJECT_TO_STRING = 5;
/*      */   private static final int IPROXY_OBJECT_PROPERTY = 6;
/*      */   private static final int IPROXY_OBJECT_IDS = 7;
/*      */   private GuiCallback callback;
/*      */   private boolean breakFlag;
/*      */   private ScopeProvider scopeProvider;
/*      */   private SourceProvider sourceProvider;
/*   62 */   private int frameIndex = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile ContextData interruptedContextData;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ContextFactory contextFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   78 */   private Object monitor = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   84 */   private Object eventThreadMonitor = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   private volatile int returnValue = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean insideInterruptLoop;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String evalRequest;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StackFrame evalFrame;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String evalResult;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean breakOnExceptions;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean breakOnEnter;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean breakOnReturn;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  131 */   private final Map<String, SourceInfo> urlToSourceInfo = Collections.synchronizedMap(new HashMap<String, SourceInfo>());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  137 */   private final Map<String, FunctionSource> functionNames = Collections.synchronizedMap(new HashMap<String, FunctionSource>());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   private final Map<DebuggableScript, FunctionSource> functionToSource = Collections.synchronizedMap(new HashMap<DebuggableScript, FunctionSource>());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private DimIProxy listener;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGuiCallback(GuiCallback callback) {
/*  155 */     this.callback = callback;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBreak() {
/*  162 */     this.breakFlag = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScopeProvider(ScopeProvider scopeProvider) {
/*  169 */     this.scopeProvider = scopeProvider;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourceProvider(SourceProvider sourceProvider) {
/*  176 */     this.sourceProvider = sourceProvider;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void contextSwitch(int frameIndex) {
/*  183 */     this.frameIndex = frameIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBreakOnExceptions(boolean breakOnExceptions) {
/*  190 */     this.breakOnExceptions = breakOnExceptions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBreakOnEnter(boolean breakOnEnter) {
/*  197 */     this.breakOnEnter = breakOnEnter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBreakOnReturn(boolean breakOnReturn) {
/*  204 */     this.breakOnReturn = breakOnReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void attachTo(ContextFactory factory) {
/*  211 */     detach();
/*  212 */     this.contextFactory = factory;
/*  213 */     this.listener = new DimIProxy(this, 1);
/*  214 */     factory.addListener(this.listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void detach() {
/*  221 */     if (this.listener != null) {
/*  222 */       this.contextFactory.removeListener(this.listener);
/*  223 */       this.contextFactory = null;
/*  224 */       this.listener = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dispose() {
/*  232 */     detach();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FunctionSource getFunctionSource(DebuggableScript fnOrScript) {
/*  239 */     FunctionSource fsource = functionSource(fnOrScript);
/*  240 */     if (fsource == null) {
/*  241 */       String url = getNormalizedUrl(fnOrScript);
/*  242 */       SourceInfo si = sourceInfo(url);
/*  243 */       if (si == null && 
/*  244 */         !fnOrScript.isGeneratedScript()) {
/*      */         
/*  246 */         String source = loadSource(url);
/*  247 */         if (source != null) {
/*  248 */           DebuggableScript top = fnOrScript;
/*      */           while (true) {
/*  250 */             DebuggableScript parent = top.getParent();
/*  251 */             if (parent == null) {
/*      */               break;
/*      */             }
/*  254 */             top = parent;
/*      */           } 
/*  256 */           registerTopScript(top, source);
/*  257 */           fsource = functionSource(fnOrScript);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  262 */     return fsource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String loadSource(String sourceUrl) {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_2
/*      */     //   2: aload_1
/*      */     //   3: bipush #35
/*      */     //   5: invokevirtual indexOf : (I)I
/*      */     //   8: istore_3
/*      */     //   9: iload_3
/*      */     //   10: iflt -> 20
/*      */     //   13: aload_1
/*      */     //   14: iconst_0
/*      */     //   15: iload_3
/*      */     //   16: invokevirtual substring : (II)Ljava/lang/String;
/*      */     //   19: astore_1
/*      */     //   20: aload_1
/*      */     //   21: bipush #58
/*      */     //   23: invokevirtual indexOf : (I)I
/*      */     //   26: ifge -> 220
/*      */     //   29: aload_1
/*      */     //   30: ldc '~/'
/*      */     //   32: invokevirtual startsWith : (Ljava/lang/String;)Z
/*      */     //   35: ifeq -> 99
/*      */     //   38: ldc 'user.home'
/*      */     //   40: invokestatic getSystemProperty : (Ljava/lang/String;)Ljava/lang/String;
/*      */     //   43: astore #5
/*      */     //   45: aload #5
/*      */     //   47: ifnull -> 99
/*      */     //   50: aload_1
/*      */     //   51: iconst_2
/*      */     //   52: invokevirtual substring : (I)Ljava/lang/String;
/*      */     //   55: astore #6
/*      */     //   57: new java/io/File
/*      */     //   60: dup
/*      */     //   61: new java/io/File
/*      */     //   64: dup
/*      */     //   65: aload #5
/*      */     //   67: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   70: aload #6
/*      */     //   72: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
/*      */     //   75: astore #7
/*      */     //   77: aload #7
/*      */     //   79: invokevirtual exists : ()Z
/*      */     //   82: ifeq -> 99
/*      */     //   85: new java/io/FileInputStream
/*      */     //   88: dup
/*      */     //   89: aload #7
/*      */     //   91: invokespecial <init> : (Ljava/io/File;)V
/*      */     //   94: astore #4
/*      */     //   96: goto -> 233
/*      */     //   99: new java/io/File
/*      */     //   102: dup
/*      */     //   103: aload_1
/*      */     //   104: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   107: astore #5
/*      */     //   109: aload #5
/*      */     //   111: invokevirtual exists : ()Z
/*      */     //   114: ifeq -> 131
/*      */     //   117: new java/io/FileInputStream
/*      */     //   120: dup
/*      */     //   121: aload #5
/*      */     //   123: invokespecial <init> : (Ljava/io/File;)V
/*      */     //   126: astore #4
/*      */     //   128: goto -> 233
/*      */     //   131: goto -> 136
/*      */     //   134: astore #5
/*      */     //   136: aload_1
/*      */     //   137: ldc '//'
/*      */     //   139: invokevirtual startsWith : (Ljava/lang/String;)Z
/*      */     //   142: ifeq -> 168
/*      */     //   145: new java/lang/StringBuilder
/*      */     //   148: dup
/*      */     //   149: invokespecial <init> : ()V
/*      */     //   152: ldc 'http:'
/*      */     //   154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   157: aload_1
/*      */     //   158: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   161: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   164: astore_1
/*      */     //   165: goto -> 220
/*      */     //   168: aload_1
/*      */     //   169: ldc '/'
/*      */     //   171: invokevirtual startsWith : (Ljava/lang/String;)Z
/*      */     //   174: ifeq -> 200
/*      */     //   177: new java/lang/StringBuilder
/*      */     //   180: dup
/*      */     //   181: invokespecial <init> : ()V
/*      */     //   184: ldc 'http://127.0.0.1'
/*      */     //   186: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   189: aload_1
/*      */     //   190: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   193: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   196: astore_1
/*      */     //   197: goto -> 220
/*      */     //   200: new java/lang/StringBuilder
/*      */     //   203: dup
/*      */     //   204: invokespecial <init> : ()V
/*      */     //   207: ldc 'http://'
/*      */     //   209: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   212: aload_1
/*      */     //   213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   216: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   219: astore_1
/*      */     //   220: new java/net/URL
/*      */     //   223: dup
/*      */     //   224: aload_1
/*      */     //   225: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   228: invokevirtual openStream : ()Ljava/io/InputStream;
/*      */     //   231: astore #4
/*      */     //   233: new java/io/InputStreamReader
/*      */     //   236: dup
/*      */     //   237: aload #4
/*      */     //   239: invokespecial <init> : (Ljava/io/InputStream;)V
/*      */     //   242: invokestatic readReader : (Ljava/io/Reader;)Ljava/lang/String;
/*      */     //   245: astore_2
/*      */     //   246: aload #4
/*      */     //   248: invokevirtual close : ()V
/*      */     //   251: goto -> 264
/*      */     //   254: astore #8
/*      */     //   256: aload #4
/*      */     //   258: invokevirtual close : ()V
/*      */     //   261: aload #8
/*      */     //   263: athrow
/*      */     //   264: goto -> 304
/*      */     //   267: astore #4
/*      */     //   269: getstatic java/lang/System.err : Ljava/io/PrintStream;
/*      */     //   272: new java/lang/StringBuilder
/*      */     //   275: dup
/*      */     //   276: invokespecial <init> : ()V
/*      */     //   279: ldc 'Failed to load source from '
/*      */     //   281: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   284: aload_1
/*      */     //   285: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   288: ldc ': '
/*      */     //   290: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   293: aload #4
/*      */     //   295: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   298: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   301: invokevirtual println : (Ljava/lang/String;)V
/*      */     //   304: aload_2
/*      */     //   305: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #269	-> 0
/*      */     //   #270	-> 2
/*      */     //   #271	-> 9
/*      */     //   #272	-> 13
/*      */     //   #278	-> 20
/*      */     //   #281	-> 29
/*      */     //   #282	-> 38
/*      */     //   #283	-> 45
/*      */     //   #284	-> 50
/*      */     //   #285	-> 57
/*      */     //   #286	-> 77
/*      */     //   #287	-> 85
/*      */     //   #288	-> 96
/*      */     //   #292	-> 99
/*      */     //   #293	-> 109
/*      */     //   #294	-> 117
/*      */     //   #295	-> 128
/*      */     //   #297	-> 131
/*      */     //   #299	-> 136
/*      */     //   #300	-> 145
/*      */     //   #301	-> 168
/*      */     //   #302	-> 177
/*      */     //   #304	-> 200
/*      */     //   #308	-> 220
/*      */     //   #312	-> 233
/*      */     //   #314	-> 246
/*      */     //   #315	-> 251
/*      */     //   #314	-> 254
/*      */     //   #319	-> 264
/*      */     //   #316	-> 267
/*      */     //   #317	-> 269
/*      */     //   #320	-> 304
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   96	3	4	is	Ljava/io/InputStream;
/*      */     //   57	42	6	pathFromHome	Ljava/lang/String;
/*      */     //   77	22	7	f	Ljava/io/File;
/*      */     //   45	54	5	home	Ljava/lang/String;
/*      */     //   128	3	4	is	Ljava/io/InputStream;
/*      */     //   109	22	5	f	Ljava/io/File;
/*      */     //   136	0	5	ex	Ljava/lang/SecurityException;
/*      */     //   233	31	4	is	Ljava/io/InputStream;
/*      */     //   269	35	4	ex	Ljava/io/IOException;
/*      */     //   0	306	0	this	Lorg/mozilla/javascript/tools/debugger/Dim;
/*      */     //   0	306	1	sourceUrl	Ljava/lang/String;
/*      */     //   2	304	2	source	Ljava/lang/String;
/*      */     //   9	297	3	hash	I
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   20	264	267	java/io/IOException
/*      */     //   29	96	134	java/lang/SecurityException
/*      */     //   99	128	134	java/lang/SecurityException
/*      */     //   233	246	254	finally
/*      */     //   254	256	254	finally
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void registerTopScript(DebuggableScript topScript, String source) {
/*  327 */     if (!topScript.isTopLevel()) {
/*  328 */       throw new IllegalArgumentException();
/*      */     }
/*  330 */     String url = getNormalizedUrl(topScript);
/*  331 */     DebuggableScript[] functions = getAllFunctions(topScript);
/*  332 */     if (this.sourceProvider != null) {
/*  333 */       String providedSource = this.sourceProvider.getSource(topScript);
/*  334 */       if (providedSource != null) {
/*  335 */         source = providedSource;
/*      */       }
/*      */     } 
/*      */     
/*  339 */     SourceInfo sourceInfo = new SourceInfo(source, functions, url);
/*      */     
/*  341 */     synchronized (this.urlToSourceInfo) {
/*  342 */       SourceInfo old = this.urlToSourceInfo.get(url);
/*  343 */       if (old != null) {
/*  344 */         sourceInfo.copyBreakpointsFrom(old);
/*      */       }
/*  346 */       this.urlToSourceInfo.put(url, sourceInfo);
/*  347 */       for (int i = 0; i != sourceInfo.functionSourcesTop(); i++) {
/*  348 */         FunctionSource fsource = sourceInfo.functionSource(i);
/*  349 */         String name = fsource.name();
/*  350 */         if (name.length() != 0) {
/*  351 */           this.functionNames.put(name, fsource);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  356 */     synchronized (this.functionToSource) {
/*  357 */       for (int i = 0; i != functions.length; i++) {
/*  358 */         FunctionSource fsource = sourceInfo.functionSource(i);
/*  359 */         this.functionToSource.put(functions[i], fsource);
/*      */       } 
/*      */     } 
/*      */     
/*  363 */     this.callback.updateSourceText(sourceInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FunctionSource functionSource(DebuggableScript fnOrScript) {
/*  370 */     return this.functionToSource.get(fnOrScript);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] functionNames() {
/*  377 */     synchronized (this.urlToSourceInfo) {
/*  378 */       return (String[])this.functionNames.keySet().toArray((Object[])new String[this.functionNames.size()]);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FunctionSource functionSourceByName(String functionName) {
/*  386 */     return this.functionNames.get(functionName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SourceInfo sourceInfo(String url) {
/*  393 */     return this.urlToSourceInfo.get(url);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getNormalizedUrl(DebuggableScript fnOrScript) {
/*  400 */     String url = fnOrScript.getSourceName();
/*  401 */     if (url == null) { url = "<stdin>";
/*      */        }
/*      */     
/*      */     else
/*      */     
/*      */     { 
/*  407 */       char evalSeparator = '#';
/*  408 */       StringBuilder sb = null;
/*  409 */       int urlLength = url.length();
/*  410 */       int cursor = 0;
/*      */       while (true) {
/*  412 */         int searchStart = url.indexOf(evalSeparator, cursor);
/*  413 */         if (searchStart < 0) {
/*      */           break;
/*      */         }
/*  416 */         String replace = null;
/*  417 */         int i = searchStart + 1;
/*  418 */         while (i != urlLength) {
/*  419 */           int c = url.charAt(i);
/*  420 */           if (48 > c || c > 57) {
/*      */             break;
/*      */           }
/*  423 */           i++;
/*      */         } 
/*  425 */         if (i != searchStart + 1)
/*      */         {
/*  427 */           if ("(eval)".regionMatches(0, url, i, 6)) {
/*  428 */             cursor = i + 6;
/*  429 */             replace = "(eval)";
/*      */           } 
/*      */         }
/*  432 */         if (replace == null) {
/*      */           break;
/*      */         }
/*  435 */         if (sb == null) {
/*  436 */           sb = new StringBuilder();
/*  437 */           sb.append(url.substring(0, searchStart));
/*      */         } 
/*  439 */         sb.append(replace);
/*      */       } 
/*  441 */       if (sb != null) {
/*  442 */         if (cursor != urlLength) {
/*  443 */           sb.append(url.substring(cursor));
/*      */         }
/*  445 */         url = sb.toString();
/*      */       }  }
/*      */     
/*  448 */     return url;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static DebuggableScript[] getAllFunctions(DebuggableScript function) {
/*  456 */     ObjArray functions = new ObjArray();
/*  457 */     collectFunctions_r(function, functions);
/*  458 */     DebuggableScript[] result = new DebuggableScript[functions.size()];
/*  459 */     functions.toArray((Object[])result);
/*  460 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void collectFunctions_r(DebuggableScript function, ObjArray array) {
/*  468 */     array.add(function);
/*  469 */     for (int i = 0; i != function.getFunctionCount(); i++) {
/*  470 */       collectFunctions_r(function.getFunction(i), array);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearAllBreakpoints() {
/*  478 */     for (SourceInfo si : this.urlToSourceInfo.values()) {
/*  479 */       si.removeAllBreakpoints();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleBreakpointHit(StackFrame frame, Context cx) {
/*  487 */     this.breakFlag = false;
/*  488 */     interrupted(cx, frame, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleExceptionThrown(Context cx, Throwable ex, StackFrame frame) {
/*  496 */     if (this.breakOnExceptions) {
/*  497 */       ContextData cd = frame.contextData();
/*  498 */       if (cd.lastProcessedException != ex) {
/*  499 */         interrupted(cx, frame, ex);
/*  500 */         cd.lastProcessedException = ex;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ContextData currentContextData() {
/*  509 */     return this.interruptedContextData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReturnValue(int returnValue) {
/*  516 */     synchronized (this.monitor) {
/*  517 */       this.returnValue = returnValue;
/*  518 */       this.monitor.notify();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void go() {
/*  526 */     synchronized (this.monitor) {
/*  527 */       this.returnValue = 3;
/*  528 */       this.monitor.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String eval(String expr) {
/*  536 */     String result = "undefined";
/*  537 */     if (expr == null) {
/*  538 */       return result;
/*      */     }
/*  540 */     ContextData contextData = currentContextData();
/*  541 */     if (contextData == null || this.frameIndex >= contextData.frameCount()) {
/*  542 */       return result;
/*      */     }
/*  544 */     StackFrame frame = contextData.getFrame(this.frameIndex);
/*  545 */     if (contextData.eventThreadFlag) {
/*  546 */       Context cx = Context.getCurrentContext();
/*  547 */       result = do_eval(cx, frame, expr);
/*      */     } else {
/*  549 */       synchronized (this.monitor) {
/*  550 */         if (this.insideInterruptLoop) {
/*  551 */           this.evalRequest = expr;
/*  552 */           this.evalFrame = frame;
/*  553 */           this.monitor.notify();
/*      */           do {
/*      */             try {
/*  556 */               this.monitor.wait();
/*  557 */             } catch (InterruptedException exc) {
/*  558 */               Thread.currentThread().interrupt();
/*      */               break;
/*      */             } 
/*  561 */           } while (this.evalRequest != null);
/*  562 */           result = this.evalResult;
/*      */         } 
/*      */       } 
/*      */     } 
/*  566 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void compileScript(String url, String text) {
/*  573 */     DimIProxy action = new DimIProxy(this, 2);
/*  574 */     action.url = url;
/*  575 */     action.text = text;
/*  576 */     action.withContext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void evalScript(String url, String text) {
/*  583 */     DimIProxy action = new DimIProxy(this, 3);
/*  584 */     action.url = url;
/*  585 */     action.text = text;
/*  586 */     action.withContext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String objectToString(Object object) {
/*  593 */     DimIProxy action = new DimIProxy(this, 5);
/*  594 */     action.object = object;
/*  595 */     action.withContext();
/*  596 */     return action.stringResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean stringIsCompilableUnit(String str) {
/*  603 */     DimIProxy action = new DimIProxy(this, 4);
/*  604 */     action.text = str;
/*  605 */     action.withContext();
/*  606 */     return action.booleanResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObjectProperty(Object object, Object id) {
/*  613 */     DimIProxy action = new DimIProxy(this, 6);
/*  614 */     action.object = object;
/*  615 */     action.id = id;
/*  616 */     action.withContext();
/*  617 */     return action.objectResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] getObjectIds(Object object) {
/*  624 */     DimIProxy action = new DimIProxy(this, 7);
/*  625 */     action.object = object;
/*  626 */     action.withContext();
/*  627 */     return action.objectArrayResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object getObjectPropertyImpl(Context cx, Object object, Object id) {
/*      */     Object result;
/*  635 */     Scriptable scriptable = (Scriptable)object;
/*      */     
/*  637 */     if (id instanceof String) {
/*  638 */       String name = (String)id;
/*  639 */       if (name.equals("this")) {
/*  640 */         result = scriptable;
/*  641 */       } else if (name.equals("__proto__")) {
/*  642 */         result = scriptable.getPrototype();
/*  643 */       } else if (name.equals("__parent__")) {
/*  644 */         result = scriptable.getParentScope();
/*      */       } else {
/*  646 */         result = ScriptableObject.getProperty(scriptable, name);
/*  647 */         if (result == ScriptableObject.NOT_FOUND) {
/*  648 */           result = Undefined.instance;
/*      */         }
/*      */       } 
/*      */     } else {
/*  652 */       int index = ((Integer)id).intValue();
/*  653 */       result = ScriptableObject.getProperty(scriptable, index);
/*  654 */       if (result == ScriptableObject.NOT_FOUND) {
/*  655 */         result = Undefined.instance;
/*      */       }
/*      */     } 
/*  658 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] getObjectIdsImpl(Context cx, Object object) {
/*      */     Object[] ids;
/*  665 */     if (!(object instanceof Scriptable) || object == Undefined.instance) {
/*  666 */       return Context.emptyArgs;
/*      */     }
/*      */ 
/*      */     
/*  670 */     Scriptable scriptable = (Scriptable)object;
/*  671 */     if (scriptable instanceof DebuggableObject) {
/*  672 */       ids = ((DebuggableObject)scriptable).getAllIds();
/*      */     } else {
/*  674 */       ids = scriptable.getIds();
/*      */     } 
/*      */     
/*  677 */     Scriptable proto = scriptable.getPrototype();
/*  678 */     Scriptable parent = scriptable.getParentScope();
/*  679 */     int extra = 0;
/*  680 */     if (proto != null) {
/*  681 */       extra++;
/*      */     }
/*  683 */     if (parent != null) {
/*  684 */       extra++;
/*      */     }
/*  686 */     if (extra != 0) {
/*  687 */       Object[] tmp = new Object[extra + ids.length];
/*  688 */       System.arraycopy(ids, 0, tmp, extra, ids.length);
/*  689 */       ids = tmp;
/*  690 */       extra = 0;
/*  691 */       if (proto != null) {
/*  692 */         ids[extra++] = "__proto__";
/*      */       }
/*  694 */       if (parent != null) {
/*  695 */         ids[extra++] = "__parent__";
/*      */       }
/*      */     } 
/*      */     
/*  699 */     return ids;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void interrupted(Context cx, StackFrame frame, Throwable scriptException) {
/*      */     // Byte code:
/*      */     //   0: aload_2
/*      */     //   1: invokevirtual contextData : ()Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   4: astore #4
/*      */     //   6: aload_0
/*      */     //   7: getfield callback : Lorg/mozilla/javascript/tools/debugger/GuiCallback;
/*      */     //   10: invokeinterface isGuiEventThread : ()Z
/*      */     //   15: istore #5
/*      */     //   17: aload #4
/*      */     //   19: iload #5
/*      */     //   21: invokestatic access$402 : (Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
/*      */     //   24: pop
/*      */     //   25: iconst_0
/*      */     //   26: istore #6
/*      */     //   28: aload_0
/*      */     //   29: getfield eventThreadMonitor : Ljava/lang/Object;
/*      */     //   32: dup
/*      */     //   33: astore #7
/*      */     //   35: monitorenter
/*      */     //   36: iload #5
/*      */     //   38: ifeq -> 57
/*      */     //   41: aload_0
/*      */     //   42: getfield interruptedContextData : Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   45: ifnull -> 80
/*      */     //   48: iconst_1
/*      */     //   49: istore #6
/*      */     //   51: aload #7
/*      */     //   53: monitorexit
/*      */     //   54: goto -> 100
/*      */     //   57: aload_0
/*      */     //   58: getfield interruptedContextData : Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   61: ifnull -> 80
/*      */     //   64: aload_0
/*      */     //   65: getfield eventThreadMonitor : Ljava/lang/Object;
/*      */     //   68: invokevirtual wait : ()V
/*      */     //   71: goto -> 57
/*      */     //   74: astore #8
/*      */     //   76: aload #7
/*      */     //   78: monitorexit
/*      */     //   79: return
/*      */     //   80: aload_0
/*      */     //   81: aload #4
/*      */     //   83: putfield interruptedContextData : Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   86: aload #7
/*      */     //   88: monitorexit
/*      */     //   89: goto -> 100
/*      */     //   92: astore #9
/*      */     //   94: aload #7
/*      */     //   96: monitorexit
/*      */     //   97: aload #9
/*      */     //   99: athrow
/*      */     //   100: iload #6
/*      */     //   102: ifeq -> 106
/*      */     //   105: return
/*      */     //   106: aload_0
/*      */     //   107: getfield interruptedContextData : Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   110: ifnonnull -> 117
/*      */     //   113: invokestatic codeBug : ()Ljava/lang/RuntimeException;
/*      */     //   116: pop
/*      */     //   117: aload #4
/*      */     //   119: invokevirtual frameCount : ()I
/*      */     //   122: istore #7
/*      */     //   124: aload_0
/*      */     //   125: iload #7
/*      */     //   127: iconst_1
/*      */     //   128: isub
/*      */     //   129: putfield frameIndex : I
/*      */     //   132: invokestatic currentThread : ()Ljava/lang/Thread;
/*      */     //   135: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   138: astore #8
/*      */     //   140: aload_3
/*      */     //   141: ifnonnull -> 150
/*      */     //   144: aconst_null
/*      */     //   145: astore #9
/*      */     //   147: goto -> 156
/*      */     //   150: aload_3
/*      */     //   151: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   154: astore #9
/*      */     //   156: iconst_m1
/*      */     //   157: istore #10
/*      */     //   159: iload #5
/*      */     //   161: ifne -> 358
/*      */     //   164: aload_0
/*      */     //   165: getfield monitor : Ljava/lang/Object;
/*      */     //   168: dup
/*      */     //   169: astore #11
/*      */     //   171: monitorenter
/*      */     //   172: aload_0
/*      */     //   173: getfield insideInterruptLoop : Z
/*      */     //   176: ifeq -> 183
/*      */     //   179: invokestatic codeBug : ()Ljava/lang/RuntimeException;
/*      */     //   182: pop
/*      */     //   183: aload_0
/*      */     //   184: iconst_1
/*      */     //   185: putfield insideInterruptLoop : Z
/*      */     //   188: aload_0
/*      */     //   189: aconst_null
/*      */     //   190: putfield evalRequest : Ljava/lang/String;
/*      */     //   193: aload_0
/*      */     //   194: iconst_m1
/*      */     //   195: putfield returnValue : I
/*      */     //   198: aload_0
/*      */     //   199: getfield callback : Lorg/mozilla/javascript/tools/debugger/GuiCallback;
/*      */     //   202: aload_2
/*      */     //   203: aload #8
/*      */     //   205: aload #9
/*      */     //   207: invokeinterface enterInterrupt : (Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   212: aload_0
/*      */     //   213: getfield monitor : Ljava/lang/Object;
/*      */     //   216: invokevirtual wait : ()V
/*      */     //   219: goto -> 233
/*      */     //   222: astore #12
/*      */     //   224: invokestatic currentThread : ()Ljava/lang/Thread;
/*      */     //   227: invokevirtual interrupt : ()V
/*      */     //   230: goto -> 323
/*      */     //   233: aload_0
/*      */     //   234: getfield evalRequest : Ljava/lang/String;
/*      */     //   237: ifnull -> 306
/*      */     //   240: aload_0
/*      */     //   241: aconst_null
/*      */     //   242: putfield evalResult : Ljava/lang/String;
/*      */     //   245: aload_0
/*      */     //   246: aload_1
/*      */     //   247: aload_0
/*      */     //   248: getfield evalFrame : Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;
/*      */     //   251: aload_0
/*      */     //   252: getfield evalRequest : Ljava/lang/String;
/*      */     //   255: invokestatic do_eval : (Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;Ljava/lang/String;)Ljava/lang/String;
/*      */     //   258: putfield evalResult : Ljava/lang/String;
/*      */     //   261: aload_0
/*      */     //   262: aconst_null
/*      */     //   263: putfield evalRequest : Ljava/lang/String;
/*      */     //   266: aload_0
/*      */     //   267: aconst_null
/*      */     //   268: putfield evalFrame : Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;
/*      */     //   271: aload_0
/*      */     //   272: getfield monitor : Ljava/lang/Object;
/*      */     //   275: invokevirtual notify : ()V
/*      */     //   278: goto -> 303
/*      */     //   281: astore #13
/*      */     //   283: aload_0
/*      */     //   284: aconst_null
/*      */     //   285: putfield evalRequest : Ljava/lang/String;
/*      */     //   288: aload_0
/*      */     //   289: aconst_null
/*      */     //   290: putfield evalFrame : Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;
/*      */     //   293: aload_0
/*      */     //   294: getfield monitor : Ljava/lang/Object;
/*      */     //   297: invokevirtual notify : ()V
/*      */     //   300: aload #13
/*      */     //   302: athrow
/*      */     //   303: goto -> 212
/*      */     //   306: aload_0
/*      */     //   307: getfield returnValue : I
/*      */     //   310: iconst_m1
/*      */     //   311: if_icmpeq -> 212
/*      */     //   314: aload_0
/*      */     //   315: getfield returnValue : I
/*      */     //   318: istore #10
/*      */     //   320: goto -> 323
/*      */     //   323: aload_0
/*      */     //   324: iconst_0
/*      */     //   325: putfield insideInterruptLoop : Z
/*      */     //   328: goto -> 341
/*      */     //   331: astore #14
/*      */     //   333: aload_0
/*      */     //   334: iconst_0
/*      */     //   335: putfield insideInterruptLoop : Z
/*      */     //   338: aload #14
/*      */     //   340: athrow
/*      */     //   341: aload #11
/*      */     //   343: monitorexit
/*      */     //   344: goto -> 355
/*      */     //   347: astore #15
/*      */     //   349: aload #11
/*      */     //   351: monitorexit
/*      */     //   352: aload #15
/*      */     //   354: athrow
/*      */     //   355: goto -> 408
/*      */     //   358: aload_0
/*      */     //   359: iconst_m1
/*      */     //   360: putfield returnValue : I
/*      */     //   363: aload_0
/*      */     //   364: getfield callback : Lorg/mozilla/javascript/tools/debugger/GuiCallback;
/*      */     //   367: aload_2
/*      */     //   368: aload #8
/*      */     //   370: aload #9
/*      */     //   372: invokeinterface enterInterrupt : (Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   377: aload_0
/*      */     //   378: getfield returnValue : I
/*      */     //   381: iconst_m1
/*      */     //   382: if_icmpne -> 402
/*      */     //   385: aload_0
/*      */     //   386: getfield callback : Lorg/mozilla/javascript/tools/debugger/GuiCallback;
/*      */     //   389: invokeinterface dispatchNextGuiEvent : ()V
/*      */     //   394: goto -> 377
/*      */     //   397: astore #11
/*      */     //   399: goto -> 377
/*      */     //   402: aload_0
/*      */     //   403: getfield returnValue : I
/*      */     //   406: istore #10
/*      */     //   408: iload #10
/*      */     //   410: tableswitch default -> 503, 0 -> 436, 1 -> 457, 2 -> 474
/*      */     //   436: aload #4
/*      */     //   438: iconst_1
/*      */     //   439: invokestatic access$1402 : (Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
/*      */     //   442: pop
/*      */     //   443: aload #4
/*      */     //   445: aload #4
/*      */     //   447: invokevirtual frameCount : ()I
/*      */     //   450: invokestatic access$1502 : (Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;I)I
/*      */     //   453: pop
/*      */     //   454: goto -> 503
/*      */     //   457: aload #4
/*      */     //   459: iconst_1
/*      */     //   460: invokestatic access$1402 : (Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
/*      */     //   463: pop
/*      */     //   464: aload #4
/*      */     //   466: iconst_m1
/*      */     //   467: invokestatic access$1502 : (Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;I)I
/*      */     //   470: pop
/*      */     //   471: goto -> 503
/*      */     //   474: aload #4
/*      */     //   476: invokevirtual frameCount : ()I
/*      */     //   479: iconst_1
/*      */     //   480: if_icmple -> 503
/*      */     //   483: aload #4
/*      */     //   485: iconst_1
/*      */     //   486: invokestatic access$1402 : (Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
/*      */     //   489: pop
/*      */     //   490: aload #4
/*      */     //   492: aload #4
/*      */     //   494: invokevirtual frameCount : ()I
/*      */     //   497: iconst_1
/*      */     //   498: isub
/*      */     //   499: invokestatic access$1502 : (Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;I)I
/*      */     //   502: pop
/*      */     //   503: aload_0
/*      */     //   504: getfield eventThreadMonitor : Ljava/lang/Object;
/*      */     //   507: dup
/*      */     //   508: astore #7
/*      */     //   510: monitorenter
/*      */     //   511: aload_0
/*      */     //   512: aconst_null
/*      */     //   513: putfield interruptedContextData : Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   516: aload_0
/*      */     //   517: getfield eventThreadMonitor : Ljava/lang/Object;
/*      */     //   520: invokevirtual notifyAll : ()V
/*      */     //   523: aload #7
/*      */     //   525: monitorexit
/*      */     //   526: goto -> 537
/*      */     //   529: astore #16
/*      */     //   531: aload #7
/*      */     //   533: monitorexit
/*      */     //   534: aload #16
/*      */     //   536: athrow
/*      */     //   537: goto -> 579
/*      */     //   540: astore #17
/*      */     //   542: aload_0
/*      */     //   543: getfield eventThreadMonitor : Ljava/lang/Object;
/*      */     //   546: dup
/*      */     //   547: astore #18
/*      */     //   549: monitorenter
/*      */     //   550: aload_0
/*      */     //   551: aconst_null
/*      */     //   552: putfield interruptedContextData : Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   555: aload_0
/*      */     //   556: getfield eventThreadMonitor : Ljava/lang/Object;
/*      */     //   559: invokevirtual notifyAll : ()V
/*      */     //   562: aload #18
/*      */     //   564: monitorexit
/*      */     //   565: goto -> 576
/*      */     //   568: astore #19
/*      */     //   570: aload #18
/*      */     //   572: monitorexit
/*      */     //   573: aload #19
/*      */     //   575: athrow
/*      */     //   576: aload #17
/*      */     //   578: athrow
/*      */     //   579: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #707	-> 0
/*      */     //   #708	-> 6
/*      */     //   #709	-> 17
/*      */     //   #711	-> 25
/*      */     //   #714	-> 28
/*      */     //   #715	-> 36
/*      */     //   #716	-> 41
/*      */     //   #717	-> 48
/*      */     //   #718	-> 51
/*      */     //   #721	-> 57
/*      */     //   #723	-> 64
/*      */     //   #726	-> 71
/*      */     //   #724	-> 74
/*      */     //   #725	-> 76
/*      */     //   #729	-> 80
/*      */     //   #730	-> 86
/*      */     //   #732	-> 100
/*      */     //   #747	-> 105
/*      */     //   #750	-> 106
/*      */     //   #754	-> 117
/*      */     //   #755	-> 124
/*      */     //   #757	-> 132
/*      */     //   #759	-> 140
/*      */     //   #760	-> 144
/*      */     //   #762	-> 150
/*      */     //   #765	-> 156
/*      */     //   #766	-> 159
/*      */     //   #767	-> 164
/*      */     //   #768	-> 172
/*      */     //   #769	-> 183
/*      */     //   #770	-> 188
/*      */     //   #771	-> 193
/*      */     //   #772	-> 198
/*      */     //   #777	-> 212
/*      */     //   #781	-> 219
/*      */     //   #778	-> 222
/*      */     //   #779	-> 224
/*      */     //   #780	-> 230
/*      */     //   #782	-> 233
/*      */     //   #783	-> 240
/*      */     //   #785	-> 245
/*      */     //   #788	-> 261
/*      */     //   #789	-> 266
/*      */     //   #790	-> 271
/*      */     //   #791	-> 278
/*      */     //   #788	-> 281
/*      */     //   #789	-> 288
/*      */     //   #790	-> 293
/*      */     //   #792	-> 303
/*      */     //   #794	-> 306
/*      */     //   #795	-> 314
/*      */     //   #796	-> 320
/*      */     //   #800	-> 323
/*      */     //   #801	-> 328
/*      */     //   #800	-> 331
/*      */     //   #802	-> 341
/*      */     //   #804	-> 358
/*      */     //   #805	-> 363
/*      */     //   #806	-> 377
/*      */     //   #808	-> 385
/*      */     //   #810	-> 394
/*      */     //   #809	-> 397
/*      */     //   #810	-> 399
/*      */     //   #812	-> 402
/*      */     //   #814	-> 408
/*      */     //   #816	-> 436
/*      */     //   #817	-> 443
/*      */     //   #818	-> 454
/*      */     //   #820	-> 457
/*      */     //   #821	-> 464
/*      */     //   #822	-> 471
/*      */     //   #824	-> 474
/*      */     //   #825	-> 483
/*      */     //   #826	-> 490
/*      */     //   #833	-> 503
/*      */     //   #834	-> 511
/*      */     //   #835	-> 516
/*      */     //   #836	-> 523
/*      */     //   #837	-> 537
/*      */     //   #833	-> 540
/*      */     //   #834	-> 550
/*      */     //   #835	-> 555
/*      */     //   #836	-> 562
/*      */     //   #839	-> 579
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   76	4	8	exc	Ljava/lang/InterruptedException;
/*      */     //   147	3	9	alertMessage	Ljava/lang/String;
/*      */     //   224	9	12	exc	Ljava/lang/InterruptedException;
/*      */     //   399	0	11	exc	Ljava/lang/InterruptedException;
/*      */     //   124	379	7	frameCount	I
/*      */     //   140	363	8	threadTitle	Ljava/lang/String;
/*      */     //   156	347	9	alertMessage	Ljava/lang/String;
/*      */     //   159	344	10	returnValue	I
/*      */     //   0	580	0	this	Lorg/mozilla/javascript/tools/debugger/Dim;
/*      */     //   0	580	1	cx	Lorg/mozilla/javascript/Context;
/*      */     //   0	580	2	frame	Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;
/*      */     //   0	580	3	scriptException	Ljava/lang/Throwable;
/*      */     //   6	574	4	contextData	Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
/*      */     //   17	563	5	eventThreadFlag	Z
/*      */     //   28	552	6	recursiveEventThreadCall	Z
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   36	54	92	finally
/*      */     //   57	79	92	finally
/*      */     //   64	71	74	java/lang/InterruptedException
/*      */     //   80	89	92	finally
/*      */     //   92	97	92	finally
/*      */     //   117	503	540	finally
/*      */     //   172	344	347	finally
/*      */     //   212	219	222	java/lang/InterruptedException
/*      */     //   212	323	331	finally
/*      */     //   245	261	281	finally
/*      */     //   281	283	281	finally
/*      */     //   331	333	331	finally
/*      */     //   347	352	347	finally
/*      */     //   385	394	397	java/lang/InterruptedException
/*      */     //   511	526	529	finally
/*      */     //   529	534	529	finally
/*      */     //   540	542	540	finally
/*      */     //   550	565	568	finally
/*      */     //   568	573	568	finally
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String do_eval(Context cx, StackFrame frame, String expr) {
/*      */     String str;
/*  846 */     Debugger saved_debugger = cx.getDebugger();
/*  847 */     Object saved_data = cx.getDebuggerContextData();
/*  848 */     int saved_level = cx.getOptimizationLevel();
/*      */     
/*  850 */     cx.setDebugger(null, null);
/*  851 */     cx.setOptimizationLevel(-1);
/*  852 */     cx.setGeneratingDebug(false);
/*      */     try {
/*  854 */       Callable script = (Callable)cx.compileString(expr, "", 0, null);
/*  855 */       Object result = script.call(cx, frame.scope, frame.thisObj, ScriptRuntime.emptyArgs);
/*      */       
/*  857 */       if (result == Undefined.instance) {
/*  858 */         str = "";
/*      */       } else {
/*  860 */         str = ScriptRuntime.toString(result);
/*      */       } 
/*  862 */     } catch (Exception exc) {
/*  863 */       str = exc.getMessage();
/*      */     } finally {
/*  865 */       cx.setGeneratingDebug(true);
/*  866 */       cx.setOptimizationLevel(saved_level);
/*  867 */       cx.setDebugger(saved_debugger, saved_data);
/*      */     } 
/*  869 */     if (str == null) {
/*  870 */       str = "null";
/*      */     }
/*  872 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DimIProxy
/*      */     implements ContextAction, ContextFactory.Listener, Debugger
/*      */   {
/*      */     private Dim dim;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int type;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String url;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String text;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object object;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object id;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean booleanResult;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String stringResult;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object objectResult;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object[] objectArrayResult;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private DimIProxy(Dim dim, int type) {
/*  937 */       this.dim = dim;
/*  938 */       this.type = type;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object run(Context cx) {
/*      */       Scriptable scope;
/*      */       ImporterTopLevel importerTopLevel;
/*  947 */       switch (this.type) {
/*      */         case 2:
/*  949 */           cx.compileString(this.text, this.url, 1, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  992 */           return null;case 3: scope = null; if (this.dim.scopeProvider != null) scope = this.dim.scopeProvider.getScope();  if (scope == null) importerTopLevel = new ImporterTopLevel(cx);  cx.evaluateString((Scriptable)importerTopLevel, this.text, this.url, 1, null); return null;case 4: this.booleanResult = cx.stringIsCompilableUnit(this.text); return null;case 5: if (this.object == Undefined.instance) { this.stringResult = "undefined"; } else if (this.object == null) { this.stringResult = "null"; } else if (this.object instanceof org.mozilla.javascript.NativeCall) { this.stringResult = "[object Call]"; } else { this.stringResult = Context.toString(this.object); }  return null;case 6: this.objectResult = this.dim.getObjectPropertyImpl(cx, this.object, this.id); return null;case 7: this.objectArrayResult = this.dim.getObjectIdsImpl(cx, this.object); return null;
/*      */       } 
/*      */       throw Kit.codeBug();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void withContext() {
/* 1000 */       this.dim.contextFactory.call(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void contextCreated(Context cx) {
/* 1009 */       if (this.type != 1) Kit.codeBug(); 
/* 1010 */       Dim.ContextData contextData = new Dim.ContextData();
/* 1011 */       Debugger debugger = new DimIProxy(this.dim, 0);
/* 1012 */       cx.setDebugger(debugger, contextData);
/* 1013 */       cx.setGeneratingDebug(true);
/* 1014 */       cx.setOptimizationLevel(-1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void contextReleased(Context cx) {
/* 1021 */       if (this.type != 1) Kit.codeBug();
/*      */     
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public DebugFrame getFrame(Context cx, DebuggableScript fnOrScript) {
/* 1030 */       if (this.type != 0) Kit.codeBug();
/*      */       
/* 1032 */       Dim.FunctionSource item = this.dim.getFunctionSource(fnOrScript);
/* 1033 */       if (item == null)
/*      */       {
/* 1035 */         return null;
/*      */       }
/* 1037 */       return new Dim.StackFrame(cx, this.dim, item);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void handleCompilationDone(Context cx, DebuggableScript fnOrScript, String source) {
/* 1046 */       if (this.type != 0) Kit.codeBug();
/*      */       
/* 1048 */       if (!fnOrScript.isTopLevel()) {
/*      */         return;
/*      */       }
/* 1051 */       this.dim.registerTopScript(fnOrScript, source);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ContextData
/*      */   {
/* 1063 */     private ObjArray frameStack = new ObjArray();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean breakNextLine;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1074 */     private int stopAtFrameDepth = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean eventThreadFlag;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Throwable lastProcessedException;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static ContextData get(Context cx) {
/* 1090 */       return (ContextData)cx.getDebuggerContextData();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int frameCount() {
/* 1097 */       return this.frameStack.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Dim.StackFrame getFrame(int frameNumber) {
/* 1104 */       int num = this.frameStack.size() - frameNumber - 1;
/* 1105 */       return (Dim.StackFrame)this.frameStack.get(num);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void pushFrame(Dim.StackFrame frame) {
/* 1112 */       this.frameStack.push(frame);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void popFrame() {
/* 1119 */       this.frameStack.pop();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class StackFrame
/*      */     implements DebugFrame
/*      */   {
/*      */     private Dim dim;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Dim.ContextData contextData;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Scriptable scope;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Scriptable thisObj;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Dim.FunctionSource fsource;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean[] breakpoints;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int lineNumber;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private StackFrame(Context cx, Dim dim, Dim.FunctionSource fsource) {
/* 1167 */       this.dim = dim;
/* 1168 */       this.contextData = Dim.ContextData.get(cx);
/* 1169 */       this.fsource = fsource;
/* 1170 */       this.breakpoints = (fsource.sourceInfo()).breakpoints;
/* 1171 */       this.lineNumber = fsource.firstLine();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onEnter(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 1179 */       this.contextData.pushFrame(this);
/* 1180 */       this.scope = scope;
/* 1181 */       this.thisObj = thisObj;
/* 1182 */       if (this.dim.breakOnEnter) {
/* 1183 */         this.dim.handleBreakpointHit(this, cx);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onLineChange(Context cx, int lineno) {
/* 1191 */       this.lineNumber = lineno;
/*      */       
/* 1193 */       if (!this.breakpoints[lineno] && !this.dim.breakFlag) {
/* 1194 */         boolean lineBreak = this.contextData.breakNextLine;
/* 1195 */         if (lineBreak && this.contextData.stopAtFrameDepth >= 0) {
/* 1196 */           lineBreak = (this.contextData.frameCount() <= this.contextData.stopAtFrameDepth);
/*      */         }
/*      */         
/* 1199 */         if (!lineBreak) {
/*      */           return;
/*      */         }
/* 1202 */         this.contextData.stopAtFrameDepth = -1;
/* 1203 */         this.contextData.breakNextLine = false;
/*      */       } 
/*      */       
/* 1206 */       this.dim.handleBreakpointHit(this, cx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onExceptionThrown(Context cx, Throwable exception) {
/* 1213 */       this.dim.handleExceptionThrown(cx, exception, this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onExit(Context cx, boolean byThrow, Object resultOrException) {
/* 1221 */       if (this.dim.breakOnReturn && !byThrow) {
/* 1222 */         this.dim.handleBreakpointHit(this, cx);
/*      */       }
/* 1224 */       this.contextData.popFrame();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onDebuggerStatement(Context cx) {
/* 1231 */       this.dim.handleBreakpointHit(this, cx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Dim.SourceInfo sourceInfo() {
/* 1238 */       return this.fsource.sourceInfo();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Dim.ContextData contextData() {
/* 1245 */       return this.contextData;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object scope() {
/* 1252 */       return this.scope;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object thisObj() {
/* 1259 */       return this.thisObj;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getUrl() {
/* 1266 */       return this.fsource.sourceInfo().url();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getLineNumber() {
/* 1273 */       return this.lineNumber;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getFunctionName() {
/* 1280 */       return this.fsource.name();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class FunctionSource
/*      */   {
/*      */     private Dim.SourceInfo sourceInfo;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int firstLine;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private FunctionSource(Dim.SourceInfo sourceInfo, int firstLine, String name) {
/* 1309 */       if (name == null) throw new IllegalArgumentException(); 
/* 1310 */       this.sourceInfo = sourceInfo;
/* 1311 */       this.firstLine = firstLine;
/* 1312 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Dim.SourceInfo sourceInfo() {
/* 1320 */       return this.sourceInfo;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int firstLine() {
/* 1327 */       return this.firstLine;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String name() {
/* 1334 */       return this.name;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SourceInfo
/*      */   {
/* 1346 */     private static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String source;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String url;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean[] breakableLines;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean[] breakpoints;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Dim.FunctionSource[] functionSources;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private SourceInfo(String source, DebuggableScript[] functions, String normilizedUrl) {
/* 1378 */       this.source = source;
/* 1379 */       this.url = normilizedUrl;
/*      */       
/* 1381 */       int N = functions.length;
/* 1382 */       int[][] lineArrays = new int[N][];
/* 1383 */       for (int i = 0; i != N; i++) {
/* 1384 */         lineArrays[i] = functions[i].getLineNumbers();
/*      */       }
/*      */       
/* 1387 */       int minAll = 0, maxAll = -1;
/* 1388 */       int[] firstLines = new int[N]; int j;
/* 1389 */       for (j = 0; j != N; j++) {
/* 1390 */         int[] lines = lineArrays[j];
/* 1391 */         if (lines == null || lines.length == 0) {
/* 1392 */           firstLines[j] = -1;
/*      */         } else {
/*      */           
/* 1395 */           int max = lines[0], min = max;
/* 1396 */           for (int k = 1; k != lines.length; k++) {
/* 1397 */             int line = lines[k];
/* 1398 */             if (line < min) {
/* 1399 */               min = line;
/* 1400 */             } else if (line > max) {
/* 1401 */               max = line;
/*      */             } 
/*      */           } 
/* 1404 */           firstLines[j] = min;
/* 1405 */           if (minAll > maxAll) {
/* 1406 */             minAll = min;
/* 1407 */             maxAll = max;
/*      */           } else {
/* 1409 */             if (min < minAll) {
/* 1410 */               minAll = min;
/*      */             }
/* 1412 */             if (max > maxAll) {
/* 1413 */               maxAll = max;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1419 */       if (minAll > maxAll) {
/*      */         
/* 1421 */         this.breakableLines = EMPTY_BOOLEAN_ARRAY;
/* 1422 */         this.breakpoints = EMPTY_BOOLEAN_ARRAY;
/*      */       } else {
/* 1424 */         if (minAll < 0)
/*      */         {
/* 1426 */           throw new IllegalStateException(String.valueOf(minAll));
/*      */         }
/* 1428 */         int linesTop = maxAll + 1;
/* 1429 */         this.breakableLines = new boolean[linesTop];
/* 1430 */         this.breakpoints = new boolean[linesTop];
/* 1431 */         for (int k = 0; k != N; k++) {
/* 1432 */           int[] lines = lineArrays[k];
/* 1433 */           if (lines != null && lines.length != 0) {
/* 1434 */             for (int m = 0; m != lines.length; m++) {
/* 1435 */               int line = lines[m];
/* 1436 */               this.breakableLines[line] = true;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/* 1441 */       this.functionSources = new Dim.FunctionSource[N];
/* 1442 */       for (j = 0; j != N; j++) {
/* 1443 */         String name = functions[j].getFunctionName();
/* 1444 */         if (name == null) {
/* 1445 */           name = "";
/*      */         }
/* 1447 */         this.functionSources[j] = new Dim.FunctionSource(this, firstLines[j], name);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String source() {
/* 1456 */       return this.source;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String url() {
/* 1463 */       return this.url;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int functionSourcesTop() {
/* 1470 */       return this.functionSources.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Dim.FunctionSource functionSource(int i) {
/* 1477 */       return this.functionSources[i];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void copyBreakpointsFrom(SourceInfo old) {
/* 1485 */       int end = old.breakpoints.length;
/* 1486 */       if (end > this.breakpoints.length) {
/* 1487 */         end = this.breakpoints.length;
/*      */       }
/* 1489 */       for (int line = 0; line != end; line++) {
/* 1490 */         if (old.breakpoints[line]) {
/* 1491 */           this.breakpoints[line] = true;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean breakableLine(int line) {
/* 1501 */       return (line < this.breakableLines.length && this.breakableLines[line]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean breakpoint(int line) {
/* 1509 */       if (!breakableLine(line)) {
/* 1510 */         throw new IllegalArgumentException(String.valueOf(line));
/*      */       }
/* 1512 */       return (line < this.breakpoints.length && this.breakpoints[line]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean breakpoint(int line, boolean value) {
/*      */       boolean changed;
/* 1519 */       if (!breakableLine(line)) {
/* 1520 */         throw new IllegalArgumentException(String.valueOf(line));
/*      */       }
/*      */       
/* 1523 */       synchronized (this.breakpoints) {
/* 1524 */         if (this.breakpoints[line] != value) {
/* 1525 */           this.breakpoints[line] = value;
/* 1526 */           changed = true;
/*      */         } else {
/* 1528 */           changed = false;
/*      */         } 
/*      */       } 
/* 1531 */       return changed;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void removeAllBreakpoints() {
/* 1538 */       synchronized (this.breakpoints) {
/* 1539 */         for (int line = 0; line != this.breakpoints.length; line++)
/* 1540 */           this.breakpoints[line] = false; 
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\debugger\Dim.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */