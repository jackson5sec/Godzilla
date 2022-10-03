/*     */ package org.mozilla.javascript.tools.shell;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ContextAction;
/*     */ import org.mozilla.javascript.ErrorReporter;
/*     */ import org.mozilla.javascript.GeneratedClassLoader;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.NativeArray;
/*     */ import org.mozilla.javascript.RhinoException;
/*     */ import org.mozilla.javascript.Script;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.SecurityController;
/*     */ import org.mozilla.javascript.commonjs.module.ModuleScope;
/*     */ import org.mozilla.javascript.commonjs.module.Require;
/*     */ import org.mozilla.javascript.tools.SourceReader;
/*     */ import org.mozilla.javascript.tools.ToolErrorReporter;
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
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*  55 */   public static ShellContextFactory shellContextFactory = new ShellContextFactory();
/*     */   
/*  57 */   public static Global global = new Global();
/*     */   protected static ToolErrorReporter errorReporter;
/*  59 */   protected static int exitCode = 0;
/*     */   private static final int EXITCODE_RUNTIME_ERROR = 3;
/*     */   private static final int EXITCODE_FILE_NOT_FOUND = 4;
/*     */   static boolean processStdin = true;
/*  63 */   static List<String> fileList = new ArrayList<String>();
/*     */   static List<String> modulePath;
/*     */   static String mainModule;
/*     */   static boolean sandboxed = false;
/*     */   static boolean useRequire = false;
/*     */   static Require require;
/*     */   private static SecurityProxy securityImpl;
/*  70 */   private static final ScriptCache scriptCache = new ScriptCache(32);
/*     */   
/*     */   static {
/*  73 */     global.initQuitAction(new IProxy(3));
/*     */   }
/*     */ 
/*     */   
/*     */   private static class IProxy
/*     */     implements ContextAction, QuitAction
/*     */   {
/*     */     private static final int PROCESS_FILES = 1;
/*     */     
/*     */     private static final int EVAL_INLINE_SCRIPT = 2;
/*     */     
/*     */     private static final int SYSTEM_EXIT = 3;
/*     */     
/*     */     private int type;
/*     */     String[] args;
/*     */     String scriptText;
/*     */     
/*     */     IProxy(int type) {
/*  91 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object run(Context cx) {
/*  96 */       if (Main.useRequire) {
/*  97 */         Main.require = Main.global.installRequire(cx, Main.modulePath, Main.sandboxed);
/*     */       }
/*  99 */       if (this.type == 1) {
/* 100 */         Main.processFiles(cx, this.args);
/* 101 */       } else if (this.type == 2) {
/* 102 */         Main.evalInlineScript(cx, this.scriptText);
/*     */       } else {
/* 104 */         throw Kit.codeBug();
/*     */       } 
/* 106 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void quit(Context cx, int exitCode) {
/* 111 */       if (this.type == 3) {
/* 112 */         System.exit(exitCode);
/*     */         return;
/*     */       } 
/* 115 */       throw Kit.codeBug();
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
/*     */   public static void main(String[] args) {
/*     */     try {
/* 129 */       if (Boolean.getBoolean("rhino.use_java_policy_security")) {
/* 130 */         initJavaPolicySecuritySupport();
/*     */       }
/* 132 */     } catch (SecurityException ex) {
/* 133 */       ex.printStackTrace(System.err);
/*     */     } 
/*     */     
/* 136 */     int result = exec(args);
/* 137 */     if (result != 0) {
/* 138 */       System.exit(result);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int exec(String[] origArgs) {
/* 147 */     errorReporter = new ToolErrorReporter(false, global.getErr());
/* 148 */     shellContextFactory.setErrorReporter((ErrorReporter)errorReporter);
/* 149 */     String[] args = processOptions(origArgs);
/* 150 */     if (exitCode > 0) {
/* 151 */       return exitCode;
/*     */     }
/* 153 */     if (processStdin) {
/* 154 */       fileList.add(null);
/*     */     }
/* 156 */     if (!global.initialized) {
/* 157 */       global.init(shellContextFactory);
/*     */     }
/* 159 */     IProxy iproxy = new IProxy(1);
/* 160 */     iproxy.args = args;
/* 161 */     shellContextFactory.call(iproxy);
/*     */     
/* 163 */     return exitCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void processFiles(Context cx, String[] args) {
/* 171 */     Object[] array = new Object[args.length];
/* 172 */     System.arraycopy(args, 0, array, 0, args.length);
/* 173 */     Scriptable argsObj = cx.newArray((Scriptable)global, array);
/* 174 */     global.defineProperty("arguments", argsObj, 2);
/*     */ 
/*     */     
/* 177 */     for (String file : fileList) {
/*     */       try {
/* 179 */         processSource(cx, file);
/* 180 */       } catch (IOException ioex) {
/* 181 */         Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.read.source", file, ioex.getMessage()));
/*     */         
/* 183 */         exitCode = 4;
/* 184 */       } catch (RhinoException rex) {
/* 185 */         ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
/*     */         
/* 187 */         exitCode = 3;
/* 188 */       } catch (VirtualMachineError ex) {
/*     */         
/* 190 */         ex.printStackTrace();
/* 191 */         String msg = ToolErrorReporter.getMessage("msg.uncaughtJSException", ex.toString());
/*     */         
/* 193 */         Context.reportError(msg);
/* 194 */         exitCode = 3;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void evalInlineScript(Context cx, String scriptText) {
/*     */     try {
/* 201 */       Script script = cx.compileString(scriptText, "<command>", 1, null);
/* 202 */       if (script != null) {
/* 203 */         script.exec(cx, getShellScope());
/*     */       }
/* 205 */     } catch (RhinoException rex) {
/* 206 */       ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
/*     */       
/* 208 */       exitCode = 3;
/* 209 */     } catch (VirtualMachineError ex) {
/*     */       
/* 211 */       ex.printStackTrace();
/* 212 */       String msg = ToolErrorReporter.getMessage("msg.uncaughtJSException", ex.toString());
/*     */       
/* 214 */       Context.reportError(msg);
/* 215 */       exitCode = 3;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Global getGlobal() {
/* 221 */     return global;
/*     */   }
/*     */   
/*     */   static Scriptable getShellScope() {
/* 225 */     return getScope(null);
/*     */   }
/*     */   
/*     */   static Scriptable getScope(String path) {
/* 229 */     if (useRequire) {
/*     */       URI uri;
/*     */ 
/*     */       
/* 233 */       if (path == null) {
/*     */         
/* 235 */         uri = (new File(System.getProperty("user.dir"))).toURI();
/*     */       
/*     */       }
/* 238 */       else if (SourceReader.toUrl(path) != null) {
/*     */         try {
/* 240 */           uri = new URI(path);
/* 241 */         } catch (URISyntaxException x) {
/*     */           
/* 243 */           uri = (new File(path)).toURI();
/*     */         } 
/*     */       } else {
/* 246 */         uri = (new File(path)).toURI();
/*     */       } 
/*     */       
/* 249 */       return (Scriptable)new ModuleScope((Scriptable)global, uri, null);
/*     */     } 
/* 251 */     return (Scriptable)global;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] processOptions(String[] args) {
/*     */     String usageError;
/* 261 */     for (int i = 0;; i++) {
/* 262 */       if (i == args.length) {
/* 263 */         return new String[0];
/*     */       }
/* 265 */       String arg = args[i];
/* 266 */       if (!arg.startsWith("-")) {
/* 267 */         processStdin = false;
/* 268 */         fileList.add(arg);
/* 269 */         mainModule = arg;
/* 270 */         String[] result = new String[args.length - i - 1];
/* 271 */         System.arraycopy(args, i + 1, result, 0, args.length - i - 1);
/* 272 */         return result;
/*     */       } 
/* 274 */       if (arg.equals("-version")) {
/* 275 */         int version; if (++i == args.length) {
/* 276 */           usageError = arg;
/*     */           
/*     */           break;
/*     */         } 
/*     */         try {
/* 281 */           version = Integer.parseInt(args[i]);
/* 282 */         } catch (NumberFormatException ex) {
/* 283 */           usageError = args[i];
/*     */           break;
/*     */         } 
/* 286 */         if (!Context.isValidLanguageVersion(version)) {
/* 287 */           usageError = args[i];
/*     */           break;
/*     */         } 
/* 290 */         shellContextFactory.setLanguageVersion(version);
/*     */       
/*     */       }
/* 293 */       else if (arg.equals("-opt") || arg.equals("-O")) {
/* 294 */         int opt; if (++i == args.length) {
/* 295 */           usageError = arg;
/*     */           
/*     */           break;
/*     */         } 
/*     */         try {
/* 300 */           opt = Integer.parseInt(args[i]);
/* 301 */         } catch (NumberFormatException ex) {
/* 302 */           usageError = args[i];
/*     */           break;
/*     */         } 
/* 305 */         if (opt == -2) {
/*     */           
/* 307 */           opt = -1;
/* 308 */         } else if (!Context.isValidOptimizationLevel(opt)) {
/* 309 */           usageError = args[i];
/*     */           break;
/*     */         } 
/* 312 */         shellContextFactory.setOptimizationLevel(opt);
/*     */       
/*     */       }
/* 315 */       else if (arg.equals("-encoding")) {
/* 316 */         if (++i == args.length) {
/* 317 */           usageError = arg;
/*     */           break;
/*     */         } 
/* 320 */         String enc = args[i];
/* 321 */         shellContextFactory.setCharacterEncoding(enc);
/*     */       
/*     */       }
/* 324 */       else if (arg.equals("-strict")) {
/* 325 */         shellContextFactory.setStrictMode(true);
/* 326 */         shellContextFactory.setAllowReservedKeywords(false);
/* 327 */         errorReporter.setIsReportingWarnings(true);
/*     */       
/*     */       }
/* 330 */       else if (arg.equals("-fatal-warnings")) {
/* 331 */         shellContextFactory.setWarningAsError(true);
/*     */       
/*     */       }
/* 334 */       else if (arg.equals("-e")) {
/* 335 */         processStdin = false;
/* 336 */         if (++i == args.length) {
/* 337 */           usageError = arg;
/*     */           break;
/*     */         } 
/* 340 */         if (!global.initialized) {
/* 341 */           global.init(shellContextFactory);
/*     */         }
/* 343 */         IProxy iproxy = new IProxy(2);
/* 344 */         iproxy.scriptText = args[i];
/* 345 */         shellContextFactory.call(iproxy);
/*     */       
/*     */       }
/* 348 */       else if (arg.equals("-require")) {
/* 349 */         useRequire = true;
/*     */       
/*     */       }
/* 352 */       else if (arg.equals("-sandbox")) {
/* 353 */         sandboxed = true;
/* 354 */         useRequire = true;
/*     */       
/*     */       }
/* 357 */       else if (arg.equals("-modules")) {
/* 358 */         if (++i == args.length) {
/* 359 */           usageError = arg;
/*     */           break;
/*     */         } 
/* 362 */         if (modulePath == null) {
/* 363 */           modulePath = new ArrayList<String>();
/*     */         }
/* 365 */         modulePath.add(args[i]);
/* 366 */         useRequire = true;
/*     */       
/*     */       }
/* 369 */       else if (arg.equals("-w")) {
/* 370 */         errorReporter.setIsReportingWarnings(true);
/*     */       
/*     */       }
/* 373 */       else if (arg.equals("-f")) {
/* 374 */         processStdin = false;
/* 375 */         if (++i == args.length) {
/* 376 */           usageError = arg;
/*     */           break;
/*     */         } 
/* 379 */         if (args[i].equals("-")) {
/* 380 */           fileList.add(null);
/*     */         } else {
/* 382 */           fileList.add(args[i]);
/* 383 */           mainModule = args[i];
/*     */         }
/*     */       
/*     */       }
/* 387 */       else if (arg.equals("-sealedlib")) {
/* 388 */         global.setSealedStdLib(true);
/*     */       
/*     */       }
/* 391 */       else if (arg.equals("-debug")) {
/* 392 */         shellContextFactory.setGeneratingDebug(true);
/*     */       } else {
/*     */         
/* 395 */         if (arg.equals("-?") || arg.equals("-help")) {
/*     */ 
/*     */           
/* 398 */           global.getOut().println(ToolErrorReporter.getMessage("msg.shell.usage", Main.class.getName()));
/*     */           
/* 400 */           exitCode = 1;
/* 401 */           return null;
/*     */         } 
/* 403 */         usageError = arg;
/*     */         break;
/*     */       } 
/*     */     } 
/* 407 */     global.getOut().println(ToolErrorReporter.getMessage("msg.shell.invalid", usageError));
/*     */     
/* 409 */     global.getOut().println(ToolErrorReporter.getMessage("msg.shell.usage", Main.class.getName()));
/*     */     
/* 411 */     exitCode = 1;
/* 412 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initJavaPolicySecuritySupport() {
/*     */     Throwable throwable;
/*     */     try {
/* 419 */       Class<?> cl = Class.forName("org.mozilla.javascript.tools.shell.JavaPolicySecurity");
/*     */       
/* 421 */       securityImpl = (SecurityProxy)cl.newInstance();
/* 422 */       SecurityController.initGlobal(securityImpl);
/*     */       return;
/* 424 */     } catch (ClassNotFoundException ex) {
/* 425 */       throwable = ex;
/* 426 */     } catch (IllegalAccessException ex) {
/* 427 */       throwable = ex;
/* 428 */     } catch (InstantiationException ex) {
/* 429 */       throwable = ex;
/* 430 */     } catch (LinkageError ex) {
/* 431 */       throwable = ex;
/*     */     } 
/* 433 */     throw Kit.initCause(new IllegalStateException("Can not load security support: " + throwable), throwable);
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
/*     */   public static void processSource(Context cx, String filename) throws IOException {
/* 449 */     if (filename == null || filename.equals("-")) {
/* 450 */       Charset cs; Scriptable scope = getShellScope();
/*     */       
/* 452 */       String charEnc = shellContextFactory.getCharacterEncoding();
/* 453 */       if (charEnc != null) {
/* 454 */         cs = Charset.forName(charEnc);
/*     */       } else {
/* 456 */         cs = Charset.defaultCharset();
/*     */       } 
/* 458 */       ShellConsole console = global.getConsole(cs);
/* 459 */       if (filename == null)
/*     */       {
/* 461 */         console.println(cx.getImplementationVersion());
/*     */       }
/*     */       
/* 464 */       int lineno = 1;
/* 465 */       boolean hitEOF = false;
/* 466 */       while (!hitEOF) {
/* 467 */         String[] prompts = global.getPrompts(cx);
/* 468 */         String prompt = null;
/* 469 */         if (filename == null)
/* 470 */           prompt = prompts[0]; 
/* 471 */         console.flush();
/* 472 */         String source = "";
/*     */         
/*     */         while (true) {
/*     */           String newline;
/*     */           
/*     */           try {
/* 478 */             newline = console.readLine(prompt);
/*     */           }
/* 480 */           catch (IOException ioe) {
/* 481 */             console.println(ioe.toString());
/*     */             break;
/*     */           } 
/* 484 */           if (newline == null) {
/* 485 */             hitEOF = true;
/*     */             break;
/*     */           } 
/* 488 */           source = source + newline + "\n";
/* 489 */           lineno++;
/* 490 */           if (cx.stringIsCompilableUnit(source))
/*     */             break; 
/* 492 */           prompt = prompts[1];
/*     */         } 
/*     */         try {
/* 495 */           Script script = cx.compileString(source, "<stdin>", lineno, null);
/* 496 */           if (script != null) {
/* 497 */             Object result = script.exec(cx, scope);
/*     */             
/* 499 */             if (result != Context.getUndefinedValue() && (!(result instanceof org.mozilla.javascript.Function) || !source.trim().startsWith("function"))) {
/*     */               
/*     */               try {
/*     */ 
/*     */                 
/* 504 */                 console.println(Context.toString(result));
/* 505 */               } catch (RhinoException rex) {
/* 506 */                 ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
/*     */               } 
/*     */             }
/*     */             
/* 510 */             NativeArray h = global.history;
/* 511 */             h.put((int)h.getLength(), (Scriptable)h, source);
/*     */           } 
/* 513 */         } catch (RhinoException rex) {
/* 514 */           ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
/*     */           
/* 516 */           exitCode = 3;
/* 517 */         } catch (VirtualMachineError ex) {
/*     */           
/* 519 */           ex.printStackTrace();
/* 520 */           String msg = ToolErrorReporter.getMessage("msg.uncaughtJSException", ex.toString());
/*     */           
/* 522 */           Context.reportError(msg);
/* 523 */           exitCode = 3;
/*     */         } 
/*     */       } 
/* 526 */       console.println();
/* 527 */       console.flush();
/* 528 */     } else if (useRequire && filename.equals(mainModule)) {
/* 529 */       require.requireMain(cx, filename);
/*     */     } else {
/* 531 */       processFile(cx, getScope(filename), filename);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void processFileNoThrow(Context cx, Scriptable scope, String filename) {
/*     */     try {
/* 537 */       processFile(cx, scope, filename);
/* 538 */     } catch (IOException ioex) {
/* 539 */       Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.read.source", filename, ioex.getMessage()));
/*     */       
/* 541 */       exitCode = 4;
/* 542 */     } catch (RhinoException rex) {
/* 543 */       ToolErrorReporter.reportException(cx.getErrorReporter(), rex);
/*     */       
/* 545 */       exitCode = 3;
/* 546 */     } catch (VirtualMachineError ex) {
/*     */       
/* 548 */       ex.printStackTrace();
/* 549 */       String msg = ToolErrorReporter.getMessage("msg.uncaughtJSException", ex.toString());
/*     */       
/* 551 */       Context.reportError(msg);
/* 552 */       exitCode = 3;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void processFile(Context cx, Scriptable scope, String filename) throws IOException {
/* 559 */     if (securityImpl == null) {
/* 560 */       processFileSecure(cx, scope, filename, null);
/*     */     } else {
/* 562 */       securityImpl.callProcessFileSecure(cx, scope, filename);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void processFileSecure(Context cx, Scriptable scope, String path, Object securityDomain) throws IOException {
/* 570 */     boolean isClass = path.endsWith(".class");
/* 571 */     Object source = readFileOrUrl(path, !isClass);
/*     */     
/* 573 */     byte[] digest = getDigest(source);
/* 574 */     String key = path + "_" + cx.getOptimizationLevel();
/* 575 */     ScriptReference ref = scriptCache.get(key, digest);
/* 576 */     Script script = (ref != null) ? ref.get() : null;
/*     */     
/* 578 */     if (script == null) {
/* 579 */       if (isClass) {
/* 580 */         script = loadCompiledScript(cx, path, (byte[])source, securityDomain);
/*     */       } else {
/* 582 */         String strSrc = (String)source;
/*     */ 
/*     */ 
/*     */         
/* 586 */         if (strSrc.length() > 0 && strSrc.charAt(0) == '#') {
/* 587 */           for (int i = 1; i != strSrc.length(); i++) {
/* 588 */             int c = strSrc.charAt(i);
/* 589 */             if (c == 10 || c == 13) {
/* 590 */               strSrc = strSrc.substring(i);
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/* 595 */         script = cx.compileString(strSrc, path, 1, securityDomain);
/*     */       } 
/* 597 */       scriptCache.put(key, digest, script);
/*     */     } 
/*     */     
/* 600 */     if (script != null) {
/* 601 */       script.exec(cx, scope);
/*     */     }
/*     */   }
/*     */   
/*     */   private static byte[] getDigest(Object source) {
/* 606 */     byte[] digest = null;
/*     */     
/* 608 */     if (source != null) {
/* 609 */       byte[] bytes; if (source instanceof String) {
/*     */         try {
/* 611 */           bytes = ((String)source).getBytes("UTF-8");
/* 612 */         } catch (UnsupportedEncodingException ue) {
/* 613 */           bytes = ((String)source).getBytes();
/*     */         } 
/*     */       } else {
/* 616 */         bytes = (byte[])source;
/*     */       } 
/*     */       try {
/* 619 */         MessageDigest md = MessageDigest.getInstance("MD5");
/* 620 */         digest = md.digest(bytes);
/* 621 */       } catch (NoSuchAlgorithmException nsa) {
/*     */         
/* 623 */         throw new RuntimeException(nsa);
/*     */       } 
/*     */     } 
/*     */     
/* 627 */     return digest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Script loadCompiledScript(Context cx, String path, byte[] data, Object securityDomain) throws FileNotFoundException {
/* 634 */     if (data == null) {
/* 635 */       throw new FileNotFoundException(path);
/*     */     }
/*     */ 
/*     */     
/* 639 */     int nameStart = path.lastIndexOf('/');
/* 640 */     if (nameStart < 0) {
/* 641 */       nameStart = 0;
/*     */     } else {
/* 643 */       nameStart++;
/*     */     } 
/* 645 */     int nameEnd = path.lastIndexOf('.');
/* 646 */     if (nameEnd < nameStart)
/*     */     {
/*     */       
/* 649 */       nameEnd = path.length();
/*     */     }
/* 651 */     String name = path.substring(nameStart, nameEnd);
/*     */     try {
/* 653 */       GeneratedClassLoader loader = SecurityController.createLoader(cx.getApplicationClassLoader(), securityDomain);
/* 654 */       Class<?> clazz = loader.defineClass(name, data);
/* 655 */       loader.linkClass(clazz);
/* 656 */       if (!Script.class.isAssignableFrom(clazz)) {
/* 657 */         throw Context.reportRuntimeError("msg.must.implement.Script");
/*     */       }
/* 659 */       return (Script)clazz.newInstance();
/* 660 */     } catch (IllegalAccessException iaex) {
/* 661 */       Context.reportError(iaex.toString());
/* 662 */       throw new RuntimeException(iaex);
/* 663 */     } catch (InstantiationException inex) {
/* 664 */       Context.reportError(inex.toString());
/* 665 */       throw new RuntimeException(inex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static InputStream getIn() {
/* 670 */     return getGlobal().getIn();
/*     */   }
/*     */   
/*     */   public static void setIn(InputStream in) {
/* 674 */     getGlobal().setIn(in);
/*     */   }
/*     */   
/*     */   public static PrintStream getOut() {
/* 678 */     return getGlobal().getOut();
/*     */   }
/*     */   
/*     */   public static void setOut(PrintStream out) {
/* 682 */     getGlobal().setOut(out);
/*     */   }
/*     */   
/*     */   public static PrintStream getErr() {
/* 686 */     return getGlobal().getErr();
/*     */   }
/*     */   
/*     */   public static void setErr(PrintStream err) {
/* 690 */     getGlobal().setErr(err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object readFileOrUrl(String path, boolean convertToString) throws IOException {
/* 701 */     return SourceReader.readFileOrUrl(path, convertToString, shellContextFactory.getCharacterEncoding());
/*     */   }
/*     */   
/*     */   static class ScriptReference
/*     */     extends SoftReference<Script>
/*     */   {
/*     */     String path;
/*     */     byte[] digest;
/*     */     
/*     */     ScriptReference(String path, byte[] digest, Script script, ReferenceQueue<Script> queue) {
/* 711 */       super(script, queue);
/* 712 */       this.path = path;
/* 713 */       this.digest = digest;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ScriptCache extends LinkedHashMap<String, ScriptReference> {
/*     */     ReferenceQueue<Script> queue;
/*     */     int capacity;
/*     */     
/*     */     ScriptCache(int capacity) {
/* 722 */       super(capacity + 1, 2.0F, true);
/* 723 */       this.capacity = capacity;
/* 724 */       this.queue = new ReferenceQueue<Script>();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean removeEldestEntry(Map.Entry<String, Main.ScriptReference> eldest) {
/* 729 */       return (size() > this.capacity);
/*     */     }
/*     */     
/*     */     Main.ScriptReference get(String path, byte[] digest) {
/*     */       Main.ScriptReference ref;
/* 734 */       while ((ref = (Main.ScriptReference)this.queue.poll()) != null) {
/* 735 */         remove(ref.path);
/*     */       }
/* 737 */       ref = get(path);
/* 738 */       if (ref != null && !Arrays.equals(digest, ref.digest)) {
/* 739 */         remove(ref.path);
/* 740 */         ref = null;
/*     */       } 
/* 742 */       return ref;
/*     */     }
/*     */     
/*     */     void put(String path, byte[] digest, Script script) {
/* 746 */       put(path, new Main.ScriptReference(path, digest, script, this.queue));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */