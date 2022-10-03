/*      */ package org.mozilla.javascript.tools.shell;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.mozilla.javascript.Context;
/*      */ import org.mozilla.javascript.ContextAction;
/*      */ import org.mozilla.javascript.ContextFactory;
/*      */ import org.mozilla.javascript.ErrorReporter;
/*      */ import org.mozilla.javascript.Function;
/*      */ import org.mozilla.javascript.NativeArray;
/*      */ import org.mozilla.javascript.RhinoException;
/*      */ import org.mozilla.javascript.Script;
/*      */ import org.mozilla.javascript.ScriptRuntime;
/*      */ import org.mozilla.javascript.Scriptable;
/*      */ import org.mozilla.javascript.ScriptableObject;
/*      */ import org.mozilla.javascript.Undefined;
/*      */ import org.mozilla.javascript.Wrapper;
/*      */ import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
/*      */ import org.mozilla.javascript.commonjs.module.Require;
/*      */ import org.mozilla.javascript.commonjs.module.RequireBuilder;
/*      */ import org.mozilla.javascript.serialize.ScriptableInputStream;
/*      */ import org.mozilla.javascript.serialize.ScriptableOutputStream;
/*      */ import org.mozilla.javascript.tools.ToolErrorReporter;
/*      */ 
/*      */ public class Global extends ImporterTopLevel {
/*      */   static final long serialVersionUID = 4029130780977538005L;
/*      */   NativeArray history;
/*   46 */   private String[] prompts = new String[] { "js> ", "  > " }; boolean attemptedJLineLoad; private ShellConsole console; private InputStream inStream; private PrintStream outStream; private PrintStream errStream;
/*      */   private boolean sealedStdLib = false;
/*      */   boolean initialized;
/*      */   private QuitAction quitAction;
/*      */   private HashMap<String, String> doctestCanonicalizations;
/*      */   
/*      */   public Global() {}
/*      */   
/*      */   public Global(Context cx) {
/*   55 */     init(cx);
/*      */   }
/*      */   
/*      */   public boolean isInitialized() {
/*   59 */     return this.initialized;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initQuitAction(QuitAction quitAction) {
/*   67 */     if (quitAction == null)
/*   68 */       throw new IllegalArgumentException("quitAction is null"); 
/*   69 */     if (this.quitAction != null) {
/*   70 */       throw new IllegalArgumentException("The method is once-call.");
/*      */     }
/*   72 */     this.quitAction = quitAction;
/*      */   }
/*      */ 
/*      */   
/*      */   public void init(ContextFactory factory) {
/*   77 */     factory.call(new ContextAction()
/*      */         {
/*      */           public Object run(Context cx) {
/*   80 */             Global.this.init(cx);
/*   81 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void init(Context cx) {
/*   90 */     initStandardObjects(cx, this.sealedStdLib);
/*   91 */     String[] names = { "defineClass", "deserialize", "doctest", "gc", "help", "load", "loadClass", "print", "quit", "readFile", "readUrl", "runCommand", "seal", "serialize", "spawn", "sync", "toint32", "version" };
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
/*  111 */     defineFunctionProperties(names, Global.class, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  116 */     Environment.defineClass((ScriptableObject)this);
/*  117 */     Environment environment = new Environment((ScriptableObject)this);
/*  118 */     defineProperty("environment", environment, 2);
/*      */ 
/*      */     
/*  121 */     this.history = (NativeArray)cx.newArray((Scriptable)this, 0);
/*  122 */     defineProperty("history", this.history, 2);
/*      */     
/*  124 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public Require installRequire(Context cx, List<String> modulePath, boolean sandboxed) {
/*  129 */     RequireBuilder rb = new RequireBuilder();
/*  130 */     rb.setSandboxed(sandboxed);
/*  131 */     List<URI> uris = new ArrayList<URI>();
/*  132 */     if (modulePath != null) {
/*  133 */       for (String path : modulePath) {
/*      */         try {
/*  135 */           URI uri = new URI(path);
/*  136 */           if (!uri.isAbsolute())
/*      */           {
/*  138 */             uri = (new File(path)).toURI().resolve("");
/*      */           }
/*  140 */           if (!uri.toString().endsWith("/"))
/*      */           {
/*      */             
/*  143 */             uri = new URI(uri + "/");
/*      */           }
/*  145 */           uris.add(uri);
/*  146 */         } catch (URISyntaxException usx) {
/*  147 */           throw new RuntimeException(usx);
/*      */         } 
/*      */       } 
/*      */     }
/*  151 */     rb.setModuleScriptProvider((ModuleScriptProvider)new SoftCachingModuleScriptProvider((ModuleSourceProvider)new UrlModuleSourceProvider(uris, null)));
/*      */ 
/*      */     
/*  154 */     Require require = rb.createRequire(cx, (Scriptable)this);
/*  155 */     require.install((Scriptable)this);
/*  156 */     return require;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void help(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  167 */     PrintStream out = getInstance(funObj).getOut();
/*  168 */     out.println(ToolErrorReporter.getMessage("msg.help"));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void gc(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  174 */     System.gc();
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
/*      */   public static Object print(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  190 */     PrintStream out = getInstance(funObj).getOut();
/*  191 */     for (int i = 0; i < args.length; i++) {
/*  192 */       if (i > 0) {
/*  193 */         out.print(" ");
/*      */       }
/*      */       
/*  196 */       String s = Context.toString(args[i]);
/*      */       
/*  198 */       out.print(s);
/*      */     } 
/*  200 */     out.println();
/*  201 */     return Context.getUndefinedValue();
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
/*      */   public static void quit(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  213 */     Global global = getInstance(funObj);
/*  214 */     if (global.quitAction != null) {
/*  215 */       int exitCode = (args.length == 0) ? 0 : ScriptRuntime.toInt32(args[0]);
/*      */       
/*  217 */       global.quitAction.quit(cx, exitCode);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double version(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  229 */     double result = cx.getLanguageVersion();
/*  230 */     if (args.length > 0) {
/*  231 */       double d = Context.toNumber(args[0]);
/*  232 */       cx.setLanguageVersion((int)d);
/*      */     } 
/*  234 */     return result;
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
/*      */   public static void load(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  246 */     for (Object arg : args) {
/*  247 */       String file = Context.toString(arg);
/*      */       try {
/*  249 */         Main.processFile(cx, thisObj, file);
/*  250 */       } catch (IOException ioex) {
/*  251 */         String msg = ToolErrorReporter.getMessage("msg.couldnt.read.source", file, ioex.getMessage());
/*      */         
/*  253 */         throw Context.reportRuntimeError(msg);
/*  254 */       } catch (VirtualMachineError ex) {
/*      */         
/*  256 */         ex.printStackTrace();
/*  257 */         String msg = ToolErrorReporter.getMessage("msg.uncaughtJSException", ex.toString());
/*      */         
/*  259 */         throw Context.reportRuntimeError(msg);
/*      */       } 
/*      */     } 
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
/*      */   public static void defineClass(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IllegalAccessException, InstantiationException, InvocationTargetException {
/*  283 */     Class<?> clazz = getClass(args);
/*  284 */     if (!Scriptable.class.isAssignableFrom(clazz)) {
/*  285 */       throw reportRuntimeError("msg.must.implement.Scriptable");
/*      */     }
/*  287 */     ScriptableObject.defineClass(thisObj, clazz);
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
/*      */   public static void loadClass(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IllegalAccessException, InstantiationException {
/*  308 */     Class<?> clazz = getClass(args);
/*  309 */     if (!Script.class.isAssignableFrom(clazz)) {
/*  310 */       throw reportRuntimeError("msg.must.implement.Script");
/*      */     }
/*  312 */     Script script = (Script)clazz.newInstance();
/*  313 */     script.exec(cx, thisObj);
/*      */   }
/*      */   
/*      */   private static Class<?> getClass(Object[] args) {
/*  317 */     if (args.length == 0) {
/*  318 */       throw reportRuntimeError("msg.expected.string.arg");
/*      */     }
/*  320 */     Object arg0 = args[0];
/*  321 */     if (arg0 instanceof Wrapper) {
/*  322 */       Object wrapped = ((Wrapper)arg0).unwrap();
/*  323 */       if (wrapped instanceof Class)
/*  324 */         return (Class)wrapped; 
/*      */     } 
/*  326 */     String className = Context.toString(args[0]);
/*      */     try {
/*  328 */       return Class.forName(className);
/*      */     }
/*  330 */     catch (ClassNotFoundException cnfe) {
/*  331 */       throw reportRuntimeError("msg.class.not.found", className);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void serialize(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
/*  339 */     if (args.length < 2) {
/*  340 */       throw Context.reportRuntimeError("Expected an object to serialize and a filename to write the serialization to");
/*      */     }
/*      */ 
/*      */     
/*  344 */     Object obj = args[0];
/*  345 */     String filename = Context.toString(args[1]);
/*  346 */     FileOutputStream fos = new FileOutputStream(filename);
/*  347 */     Scriptable scope = ScriptableObject.getTopLevelScope(thisObj);
/*  348 */     ScriptableOutputStream out = new ScriptableOutputStream(fos, scope);
/*  349 */     out.writeObject(obj);
/*  350 */     out.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object deserialize(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException, ClassNotFoundException {
/*  357 */     if (args.length < 1) {
/*  358 */       throw Context.reportRuntimeError("Expected a filename to read the serialization from");
/*      */     }
/*      */     
/*  361 */     String filename = Context.toString(args[0]);
/*  362 */     FileInputStream fis = new FileInputStream(filename);
/*  363 */     Scriptable scope = ScriptableObject.getTopLevelScope(thisObj);
/*  364 */     ScriptableInputStream scriptableInputStream = new ScriptableInputStream(fis, scope);
/*  365 */     Object deserialized = scriptableInputStream.readObject();
/*  366 */     scriptableInputStream.close();
/*  367 */     return Context.toObject(deserialized, scope);
/*      */   }
/*      */   
/*      */   public String[] getPrompts(Context cx) {
/*  371 */     if (ScriptableObject.hasProperty((Scriptable)this, "prompts")) {
/*  372 */       Object promptsJS = ScriptableObject.getProperty((Scriptable)this, "prompts");
/*      */       
/*  374 */       if (promptsJS instanceof Scriptable) {
/*  375 */         Scriptable s = (Scriptable)promptsJS;
/*  376 */         if (ScriptableObject.hasProperty(s, 0) && ScriptableObject.hasProperty(s, 1)) {
/*      */ 
/*      */           
/*  379 */           Object elem0 = ScriptableObject.getProperty(s, 0);
/*  380 */           if (elem0 instanceof Function) {
/*  381 */             elem0 = ((Function)elem0).call(cx, (Scriptable)this, s, new Object[0]);
/*      */           }
/*      */           
/*  384 */           this.prompts[0] = Context.toString(elem0);
/*  385 */           Object elem1 = ScriptableObject.getProperty(s, 1);
/*  386 */           if (elem1 instanceof Function) {
/*  387 */             elem1 = ((Function)elem1).call(cx, (Scriptable)this, s, new Object[0]);
/*      */           }
/*      */           
/*  390 */           this.prompts[1] = Context.toString(elem1);
/*      */         } 
/*      */       } 
/*      */     } 
/*  394 */     return this.prompts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object doctest(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  404 */     if (args.length == 0) {
/*  405 */       return Boolean.FALSE;
/*      */     }
/*  407 */     String session = Context.toString(args[0]);
/*  408 */     Global global = getInstance(funObj);
/*  409 */     return new Integer(global.runDoctest(cx, (Scriptable)global, session, (String)null, 0));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int runDoctest(Context cx, Scriptable scope, String session, String sourceName, int lineNumber) {
/*  415 */     this.doctestCanonicalizations = new HashMap<String, String>();
/*  416 */     String[] lines = session.split("\r\n?|\n");
/*  417 */     String prompt0 = this.prompts[0].trim();
/*  418 */     String prompt1 = this.prompts[1].trim();
/*  419 */     int testCount = 0;
/*  420 */     int i = 0;
/*  421 */     while (i < lines.length && !lines[i].trim().startsWith(prompt0)) {
/*  422 */       i++;
/*      */     }
/*  424 */     while (i < lines.length) {
/*  425 */       String inputString = lines[i].trim().substring(prompt0.length());
/*  426 */       inputString = inputString + "\n";
/*  427 */       i++;
/*  428 */       while (i < lines.length && lines[i].trim().startsWith(prompt1)) {
/*  429 */         inputString = inputString + lines[i].trim().substring(prompt1.length());
/*  430 */         inputString = inputString + "\n";
/*  431 */         i++;
/*      */       } 
/*  433 */       String expectedString = "";
/*  434 */       while (i < lines.length && !lines[i].trim().startsWith(prompt0)) {
/*      */ 
/*      */         
/*  437 */         expectedString = expectedString + lines[i] + "\n";
/*  438 */         i++;
/*      */       } 
/*  440 */       PrintStream savedOut = getOut();
/*  441 */       PrintStream savedErr = getErr();
/*  442 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/*  443 */       ByteArrayOutputStream err = new ByteArrayOutputStream();
/*  444 */       setOut(new PrintStream(out));
/*  445 */       setErr(new PrintStream(err));
/*  446 */       String resultString = "";
/*  447 */       ErrorReporter savedErrorReporter = cx.getErrorReporter();
/*  448 */       cx.setErrorReporter((ErrorReporter)new ToolErrorReporter(false, getErr()));
/*      */       try {
/*  450 */         testCount++;
/*  451 */         Object result = cx.evaluateString(scope, inputString, "doctest input", 1, null);
/*      */         
/*  453 */         if (result != Context.getUndefinedValue() && (!(result instanceof Function) || !inputString.trim().startsWith("function")))
/*      */         {
/*      */ 
/*      */           
/*  457 */           resultString = Context.toString(result);
/*      */         }
/*  459 */       } catch (RhinoException e) {
/*  460 */         ToolErrorReporter.reportException(cx.getErrorReporter(), e);
/*      */       } finally {
/*  462 */         setOut(savedOut);
/*  463 */         setErr(savedErr);
/*  464 */         cx.setErrorReporter(savedErrorReporter);
/*  465 */         resultString = resultString + err.toString() + out.toString();
/*      */       } 
/*  467 */       if (!doctestOutputMatches(expectedString, resultString)) {
/*  468 */         String message = "doctest failure running:\n" + inputString + "expected: " + expectedString + "actual: " + resultString + "\n";
/*      */ 
/*      */ 
/*      */         
/*  472 */         if (sourceName != null) {
/*  473 */           throw Context.reportRuntimeError(message, sourceName, lineNumber + i - 1, null, 0);
/*      */         }
/*      */         
/*  476 */         throw Context.reportRuntimeError(message);
/*      */       } 
/*      */     } 
/*  479 */     return testCount;
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
/*      */   private boolean doctestOutputMatches(String expected, String actual) {
/*  494 */     expected = expected.trim();
/*  495 */     actual = actual.trim().replace("\r\n", "\n");
/*  496 */     if (expected.equals(actual))
/*  497 */       return true; 
/*  498 */     for (Map.Entry<String, String> entry : this.doctestCanonicalizations.entrySet()) {
/*  499 */       expected = expected.replace(entry.getKey(), entry.getValue());
/*      */     }
/*  501 */     if (expected.equals(actual)) {
/*  502 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  509 */     Pattern p = Pattern.compile("@[0-9a-fA-F]+");
/*  510 */     Matcher expectedMatcher = p.matcher(expected);
/*  511 */     Matcher actualMatcher = p.matcher(actual);
/*      */     while (true) {
/*  513 */       if (!expectedMatcher.find())
/*  514 */         return false; 
/*  515 */       if (!actualMatcher.find())
/*  516 */         return false; 
/*  517 */       if (actualMatcher.start() != expectedMatcher.start())
/*  518 */         return false; 
/*  519 */       int start = expectedMatcher.start();
/*  520 */       if (!expected.substring(0, start).equals(actual.substring(0, start)))
/*  521 */         return false; 
/*  522 */       String expectedGroup = expectedMatcher.group();
/*  523 */       String actualGroup = actualMatcher.group();
/*  524 */       String mapping = this.doctestCanonicalizations.get(expectedGroup);
/*  525 */       if (mapping == null) {
/*  526 */         this.doctestCanonicalizations.put(expectedGroup, actualGroup);
/*  527 */         expected = expected.replace(expectedGroup, actualGroup);
/*  528 */       } else if (!actualGroup.equals(mapping)) {
/*  529 */         return false;
/*      */       } 
/*  531 */       if (expected.equals(actual)) {
/*  532 */         return true;
/*      */       }
/*      */     } 
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
/*      */   public static Object spawn(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*      */     Runner runner;
/*  551 */     Scriptable scope = funObj.getParentScope();
/*      */     
/*  553 */     if (args.length != 0 && args[0] instanceof Function) {
/*  554 */       Object[] newArgs = null;
/*  555 */       if (args.length > 1 && args[1] instanceof Scriptable) {
/*  556 */         newArgs = cx.getElements((Scriptable)args[1]);
/*      */       }
/*  558 */       if (newArgs == null) newArgs = ScriptRuntime.emptyArgs; 
/*  559 */       runner = new Runner(scope, (Function)args[0], newArgs);
/*  560 */     } else if (args.length != 0 && args[0] instanceof Script) {
/*  561 */       runner = new Runner(scope, (Script)args[0]);
/*      */     } else {
/*  563 */       throw reportRuntimeError("msg.spawn.args");
/*      */     } 
/*  565 */     runner.factory = cx.getFactory();
/*  566 */     Thread thread = new Thread(runner);
/*  567 */     thread.start();
/*  568 */     return thread;
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
/*      */   public static Object sync(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  595 */     if (args.length >= 1 && args.length <= 2 && args[0] instanceof Function) {
/*  596 */       Object syncObject = null;
/*  597 */       if (args.length == 2 && args[1] != Undefined.instance) {
/*  598 */         syncObject = args[1];
/*      */       }
/*  600 */       return new Synchronizer((Scriptable)args[0], syncObject);
/*      */     } 
/*      */     
/*  603 */     throw reportRuntimeError("msg.sync.args");
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
/*      */   public static Object runCommand(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
/*  647 */     int L = args.length;
/*  648 */     if (L == 0 || (L == 1 && args[0] instanceof Scriptable)) {
/*  649 */       throw reportRuntimeError("msg.runCommand.bad.args");
/*      */     }
/*  651 */     File wd = null;
/*  652 */     InputStream in = null;
/*  653 */     OutputStream out = null, err = null;
/*  654 */     ByteArrayOutputStream outBytes = null, errBytes = null;
/*  655 */     Object outObj = null, errObj = null;
/*  656 */     String[] environment = null;
/*  657 */     Scriptable params = null;
/*  658 */     Object[] addArgs = null;
/*  659 */     if (args[L - 1] instanceof Scriptable) {
/*  660 */       params = (Scriptable)args[L - 1];
/*  661 */       L--;
/*  662 */       Object envObj = ScriptableObject.getProperty(params, "env");
/*  663 */       if (envObj != Scriptable.NOT_FOUND) {
/*  664 */         if (envObj == null) {
/*  665 */           environment = new String[0];
/*      */         } else {
/*  667 */           if (!(envObj instanceof Scriptable)) {
/*  668 */             throw reportRuntimeError("msg.runCommand.bad.env");
/*      */           }
/*  670 */           Scriptable envHash = (Scriptable)envObj;
/*  671 */           Object[] ids = ScriptableObject.getPropertyIds(envHash);
/*  672 */           environment = new String[ids.length];
/*  673 */           for (int j = 0; j != ids.length; j++) {
/*  674 */             Object val; String key; Object keyObj = ids[j];
/*      */             
/*  676 */             if (keyObj instanceof String) {
/*  677 */               key = (String)keyObj;
/*  678 */               val = ScriptableObject.getProperty(envHash, key);
/*      */             } else {
/*  680 */               int ikey = ((Number)keyObj).intValue();
/*  681 */               key = Integer.toString(ikey);
/*  682 */               val = ScriptableObject.getProperty(envHash, ikey);
/*      */             } 
/*  684 */             if (val == ScriptableObject.NOT_FOUND) {
/*  685 */               val = Undefined.instance;
/*      */             }
/*  687 */             environment[j] = key + '=' + ScriptRuntime.toString(val);
/*      */           } 
/*      */         } 
/*      */       }
/*  691 */       Object wdObj = ScriptableObject.getProperty(params, "dir");
/*  692 */       if (wdObj != Scriptable.NOT_FOUND) {
/*  693 */         wd = new File(ScriptRuntime.toString(wdObj));
/*      */       }
/*      */       
/*  696 */       Object inObj = ScriptableObject.getProperty(params, "input");
/*  697 */       if (inObj != Scriptable.NOT_FOUND) {
/*  698 */         in = toInputStream(inObj);
/*      */       }
/*  700 */       outObj = ScriptableObject.getProperty(params, "output");
/*  701 */       if (outObj != Scriptable.NOT_FOUND) {
/*  702 */         out = toOutputStream(outObj);
/*  703 */         if (out == null) {
/*  704 */           outBytes = new ByteArrayOutputStream();
/*  705 */           out = outBytes;
/*      */         } 
/*      */       } 
/*  708 */       errObj = ScriptableObject.getProperty(params, "err");
/*  709 */       if (errObj != Scriptable.NOT_FOUND) {
/*  710 */         err = toOutputStream(errObj);
/*  711 */         if (err == null) {
/*  712 */           errBytes = new ByteArrayOutputStream();
/*  713 */           err = errBytes;
/*      */         } 
/*      */       } 
/*  716 */       Object addArgsObj = ScriptableObject.getProperty(params, "args");
/*  717 */       if (addArgsObj != Scriptable.NOT_FOUND) {
/*  718 */         Scriptable s = Context.toObject(addArgsObj, getTopLevelScope(thisObj));
/*      */         
/*  720 */         addArgs = cx.getElements(s);
/*      */       } 
/*      */     } 
/*  723 */     Global global = getInstance(funObj);
/*  724 */     if (out == null) {
/*  725 */       out = (global != null) ? global.getOut() : System.out;
/*      */     }
/*  727 */     if (err == null) {
/*  728 */       err = (global != null) ? global.getErr() : System.err;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  735 */     String[] cmd = new String[(addArgs == null) ? L : (L + addArgs.length)]; int i;
/*  736 */     for (i = 0; i != L; i++) {
/*  737 */       cmd[i] = ScriptRuntime.toString(args[i]);
/*      */     }
/*  739 */     if (addArgs != null) {
/*  740 */       for (i = 0; i != addArgs.length; i++) {
/*  741 */         cmd[L + i] = ScriptRuntime.toString(addArgs[i]);
/*      */       }
/*      */     }
/*      */     
/*  745 */     int exitCode = runProcess(cmd, environment, wd, in, out, err);
/*  746 */     if (outBytes != null) {
/*  747 */       String s = ScriptRuntime.toString(outObj) + outBytes.toString();
/*  748 */       ScriptableObject.putProperty(params, "output", s);
/*      */     } 
/*  750 */     if (errBytes != null) {
/*  751 */       String s = ScriptRuntime.toString(errObj) + errBytes.toString();
/*  752 */       ScriptableObject.putProperty(params, "err", s);
/*      */     } 
/*      */     
/*  755 */     return new Integer(exitCode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void seal(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*      */     int i;
/*  764 */     for (i = 0; i != args.length; i++) {
/*  765 */       Object arg = args[i];
/*  766 */       if (!(arg instanceof ScriptableObject) || arg == Undefined.instance) {
/*      */         
/*  768 */         if (!(arg instanceof Scriptable) || arg == Undefined.instance)
/*      */         {
/*  770 */           throw reportRuntimeError("msg.shell.seal.not.object");
/*      */         }
/*  772 */         throw reportRuntimeError("msg.shell.seal.not.scriptable");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  777 */     for (i = 0; i != args.length; i++) {
/*  778 */       Object arg = args[i];
/*  779 */       ((ScriptableObject)arg).sealObject();
/*      */     } 
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
/*      */   public static Object readFile(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
/*  800 */     if (args.length == 0) {
/*  801 */       throw reportRuntimeError("msg.shell.readFile.bad.args");
/*      */     }
/*  803 */     String path = ScriptRuntime.toString(args[0]);
/*  804 */     String charCoding = null;
/*  805 */     if (args.length >= 2) {
/*  806 */       charCoding = ScriptRuntime.toString(args[1]);
/*      */     }
/*      */     
/*  809 */     return readUrl(path, charCoding, true);
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
/*      */   public static Object readUrl(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
/*  830 */     if (args.length == 0) {
/*  831 */       throw reportRuntimeError("msg.shell.readUrl.bad.args");
/*      */     }
/*  833 */     String url = ScriptRuntime.toString(args[0]);
/*  834 */     String charCoding = null;
/*  835 */     if (args.length >= 2) {
/*  836 */       charCoding = ScriptRuntime.toString(args[1]);
/*      */     }
/*      */     
/*  839 */     return readUrl(url, charCoding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object toint32(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
/*  848 */     Object arg = (args.length != 0) ? args[0] : Undefined.instance;
/*  849 */     if (arg instanceof Integer)
/*  850 */       return arg; 
/*  851 */     return ScriptRuntime.wrapInt(ScriptRuntime.toInt32(arg));
/*      */   }
/*      */   
/*      */   private boolean loadJLine(Charset cs) {
/*  855 */     if (!this.attemptedJLineLoad) {
/*      */       
/*  857 */       this.attemptedJLineLoad = true;
/*  858 */       this.console = ShellConsole.getConsole((Scriptable)this, cs);
/*      */     } 
/*  860 */     return (this.console != null);
/*      */   }
/*      */   
/*      */   public ShellConsole getConsole(Charset cs) {
/*  864 */     if (!loadJLine(cs)) {
/*  865 */       this.console = ShellConsole.getConsole(getIn(), getErr(), cs);
/*      */     }
/*  867 */     return this.console;
/*      */   }
/*      */   
/*      */   public InputStream getIn() {
/*  871 */     if (this.inStream == null && !this.attemptedJLineLoad && 
/*  872 */       loadJLine(Charset.defaultCharset())) {
/*  873 */       this.inStream = this.console.getIn();
/*      */     }
/*      */     
/*  876 */     return (this.inStream == null) ? System.in : this.inStream;
/*      */   }
/*      */   
/*      */   public void setIn(InputStream in) {
/*  880 */     this.inStream = in;
/*      */   }
/*      */   
/*      */   public PrintStream getOut() {
/*  884 */     return (this.outStream == null) ? System.out : this.outStream;
/*      */   }
/*      */   
/*      */   public void setOut(PrintStream out) {
/*  888 */     this.outStream = out;
/*      */   }
/*      */   
/*      */   public PrintStream getErr() {
/*  892 */     return (this.errStream == null) ? System.err : this.errStream;
/*      */   }
/*      */   
/*      */   public void setErr(PrintStream err) {
/*  896 */     this.errStream = err;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSealedStdLib(boolean value) {
/*  901 */     this.sealedStdLib = value;
/*      */   }
/*      */ 
/*      */   
/*      */   private static Global getInstance(Function function) {
/*  906 */     Scriptable scope = function.getParentScope();
/*  907 */     if (!(scope instanceof Global)) {
/*  908 */       throw reportRuntimeError("msg.bad.shell.function.scope", String.valueOf(scope));
/*      */     }
/*  910 */     return (Global)scope;
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
/*      */   private static int runProcess(String[] cmd, String[] environment, File wd, InputStream in, OutputStream out, OutputStream err) throws IOException {
/*      */     Process p;
/*  928 */     if (environment == null) {
/*  929 */       p = Runtime.getRuntime().exec(cmd, (String[])null, wd);
/*      */     } else {
/*  931 */       p = Runtime.getRuntime().exec(cmd, environment, wd);
/*      */     } 
/*      */     
/*      */     try {
/*  935 */       PipeThread inThread = null;
/*  936 */       if (in != null) {
/*  937 */         inThread = new PipeThread(false, in, p.getOutputStream());
/*  938 */         inThread.start();
/*      */       } else {
/*  940 */         p.getOutputStream().close();
/*      */       } 
/*      */       
/*  943 */       PipeThread outThread = null;
/*  944 */       if (out != null) {
/*  945 */         outThread = new PipeThread(true, p.getInputStream(), out);
/*  946 */         outThread.start();
/*      */       } else {
/*  948 */         p.getInputStream().close();
/*      */       } 
/*      */       
/*  951 */       PipeThread errThread = null;
/*  952 */       if (err != null) {
/*  953 */         errThread = new PipeThread(true, p.getErrorStream(), err);
/*  954 */         errThread.start();
/*      */       } else {
/*  956 */         p.getErrorStream().close();
/*      */       } 
/*      */ 
/*      */       
/*      */       while (true) {
/*      */         try {
/*  962 */           p.waitFor();
/*  963 */           if (outThread != null) {
/*  964 */             outThread.join();
/*      */           }
/*  966 */           if (inThread != null) {
/*  967 */             inThread.join();
/*      */           }
/*  969 */           if (errThread != null) {
/*  970 */             errThread.join();
/*      */           }
/*      */           break;
/*  973 */         } catch (InterruptedException ignore) {}
/*      */       } 
/*      */ 
/*      */       
/*  977 */       return p.exitValue();
/*      */     } finally {
/*  979 */       p.destroy();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void pipe(boolean fromProcess, InputStream from, OutputStream to) throws IOException {
/*      */     try {
/*  987 */       int SIZE = 4096;
/*  988 */       byte[] buffer = new byte[4096];
/*      */       while (true) {
/*      */         int n;
/*  991 */         if (!fromProcess) {
/*  992 */           n = from.read(buffer, 0, 4096);
/*      */         } else {
/*      */           try {
/*  995 */             n = from.read(buffer, 0, 4096);
/*  996 */           } catch (IOException ex) {
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 1001 */         if (n < 0)
/* 1002 */           break;  if (fromProcess) {
/* 1003 */           to.write(buffer, 0, n);
/* 1004 */           to.flush(); continue;
/*      */         } 
/*      */         try {
/* 1007 */           to.write(buffer, 0, n);
/* 1008 */           to.flush();
/* 1009 */         } catch (IOException ex) {
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } finally {
/*      */       
/*      */       try {
/* 1017 */         if (fromProcess) {
/* 1018 */           from.close();
/*      */         } else {
/* 1020 */           to.close();
/*      */         } 
/* 1022 */       } catch (IOException ex) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static InputStream toInputStream(Object value) throws IOException {
/* 1032 */     InputStream is = null;
/* 1033 */     String s = null;
/* 1034 */     if (value instanceof Wrapper) {
/* 1035 */       Object unwrapped = ((Wrapper)value).unwrap();
/* 1036 */       if (unwrapped instanceof InputStream) {
/* 1037 */         is = (InputStream)unwrapped;
/* 1038 */       } else if (unwrapped instanceof byte[]) {
/* 1039 */         is = new ByteArrayInputStream((byte[])unwrapped);
/* 1040 */       } else if (unwrapped instanceof Reader) {
/* 1041 */         s = readReader((Reader)unwrapped);
/* 1042 */       } else if (unwrapped instanceof char[]) {
/* 1043 */         s = new String((char[])unwrapped);
/*      */       } 
/*      */     } 
/* 1046 */     if (is == null) {
/* 1047 */       if (s == null) s = ScriptRuntime.toString(value); 
/* 1048 */       is = new ByteArrayInputStream(s.getBytes());
/*      */     } 
/* 1050 */     return is;
/*      */   }
/*      */   
/*      */   private static OutputStream toOutputStream(Object value) {
/* 1054 */     OutputStream os = null;
/* 1055 */     if (value instanceof Wrapper) {
/* 1056 */       Object unwrapped = ((Wrapper)value).unwrap();
/* 1057 */       if (unwrapped instanceof OutputStream) {
/* 1058 */         os = (OutputStream)unwrapped;
/*      */       }
/*      */     } 
/* 1061 */     return os;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String readUrl(String filePath, String charCoding, boolean urlIsFile) throws IOException {
/* 1069 */     InputStream is = null; try {
/*      */       int chunkLength; Reader r;
/* 1071 */       if (!urlIsFile) {
/* 1072 */         URL urlObj = new URL(filePath);
/* 1073 */         URLConnection uc = urlObj.openConnection();
/* 1074 */         is = uc.getInputStream();
/* 1075 */         chunkLength = uc.getContentLength();
/* 1076 */         if (chunkLength <= 0)
/* 1077 */           chunkLength = 1024; 
/* 1078 */         if (charCoding == null) {
/* 1079 */           String type = uc.getContentType();
/* 1080 */           if (type != null) {
/* 1081 */             charCoding = getCharCodingFromType(type);
/*      */           }
/*      */         } 
/*      */       } else {
/* 1085 */         File f = new File(filePath);
/* 1086 */         if (!f.exists())
/* 1087 */           throw new FileNotFoundException("File not found: " + filePath); 
/* 1088 */         if (!f.canRead()) {
/* 1089 */           throw new IOException("Cannot read file: " + filePath);
/*      */         }
/* 1091 */         long length = f.length();
/* 1092 */         chunkLength = (int)length;
/* 1093 */         if (chunkLength != length) {
/* 1094 */           throw new IOException("Too big file size: " + length);
/*      */         }
/* 1096 */         if (chunkLength == 0) return "";
/*      */         
/* 1098 */         is = new FileInputStream(f);
/*      */       } 
/*      */ 
/*      */       
/* 1102 */       if (charCoding == null) {
/* 1103 */         r = new InputStreamReader(is);
/*      */       } else {
/* 1105 */         r = new InputStreamReader(is, charCoding);
/*      */       } 
/* 1107 */       return readReader(r, chunkLength);
/*      */     } finally {
/*      */       
/* 1110 */       if (is != null) {
/* 1111 */         is.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String getCharCodingFromType(String type) {
/* 1117 */     int i = type.indexOf(';');
/* 1118 */     if (i >= 0) {
/* 1119 */       int end = type.length();
/* 1120 */       i++;
/* 1121 */       while (i != end && type.charAt(i) <= ' ') {
/* 1122 */         i++;
/*      */       }
/* 1124 */       String charset = "charset";
/* 1125 */       if (charset.regionMatches(true, 0, type, i, charset.length())) {
/*      */         
/* 1127 */         i += charset.length();
/* 1128 */         while (i != end && type.charAt(i) <= ' ') {
/* 1129 */           i++;
/*      */         }
/* 1131 */         if (i != end && type.charAt(i) == '=') {
/* 1132 */           i++;
/* 1133 */           while (i != end && type.charAt(i) <= ' ') {
/* 1134 */             i++;
/*      */           }
/* 1136 */           if (i != end) {
/*      */ 
/*      */             
/* 1139 */             while (type.charAt(end - 1) <= ' ') {
/* 1140 */               end--;
/*      */             }
/* 1142 */             return type.substring(i, end);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1147 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String readReader(Reader reader) throws IOException {
/* 1153 */     return readReader(reader, 4096);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String readReader(Reader reader, int initialBufferSize) throws IOException {
/* 1159 */     char[] buffer = new char[initialBufferSize];
/* 1160 */     int offset = 0;
/*      */     while (true) {
/* 1162 */       int n = reader.read(buffer, offset, buffer.length - offset);
/* 1163 */       if (n < 0)
/* 1164 */         break;  offset += n;
/* 1165 */       if (offset == buffer.length) {
/* 1166 */         char[] tmp = new char[buffer.length * 2];
/* 1167 */         System.arraycopy(buffer, 0, tmp, 0, offset);
/* 1168 */         buffer = tmp;
/*      */       } 
/*      */     } 
/* 1171 */     return new String(buffer, 0, offset);
/*      */   }
/*      */   
/*      */   static RuntimeException reportRuntimeError(String msgId) {
/* 1175 */     String message = ToolErrorReporter.getMessage(msgId);
/* 1176 */     return (RuntimeException)Context.reportRuntimeError(message);
/*      */   }
/*      */ 
/*      */   
/*      */   static RuntimeException reportRuntimeError(String msgId, String msgArg) {
/* 1181 */     String message = ToolErrorReporter.getMessage(msgId, msgArg);
/* 1182 */     return (RuntimeException)Context.reportRuntimeError(message);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\Global.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */