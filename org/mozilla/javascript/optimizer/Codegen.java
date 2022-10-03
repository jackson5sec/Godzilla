/*      */ package org.mozilla.javascript.optimizer;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.mozilla.classfile.ClassFileWriter;
/*      */ import org.mozilla.javascript.CompilerEnvirons;
/*      */ import org.mozilla.javascript.Context;
/*      */ import org.mozilla.javascript.Evaluator;
/*      */ import org.mozilla.javascript.Function;
/*      */ import org.mozilla.javascript.GeneratedClassLoader;
/*      */ import org.mozilla.javascript.Kit;
/*      */ import org.mozilla.javascript.NativeFunction;
/*      */ import org.mozilla.javascript.ObjArray;
/*      */ import org.mozilla.javascript.ObjToIntMap;
/*      */ import org.mozilla.javascript.RhinoException;
/*      */ import org.mozilla.javascript.Script;
/*      */ import org.mozilla.javascript.ScriptRuntime;
/*      */ import org.mozilla.javascript.Scriptable;
/*      */ import org.mozilla.javascript.SecurityController;
/*      */ import org.mozilla.javascript.ast.FunctionNode;
/*      */ import org.mozilla.javascript.ast.Name;
/*      */ import org.mozilla.javascript.ast.ScriptNode;
/*      */ 
/*      */ public class Codegen implements Evaluator {
/*      */   static final String DEFAULT_MAIN_METHOD_CLASS = "org.mozilla.javascript.optimizer.OptRuntime";
/*      */   private static final String SUPER_CLASS_NAME = "org.mozilla.javascript.NativeFunction";
/*      */   static final String ID_FIELD_NAME = "_id";
/*      */   static final String REGEXP_INIT_METHOD_NAME = "_reInit";
/*      */   static final String REGEXP_INIT_METHOD_SIGNATURE = "(Lorg/mozilla/javascript/Context;)V";
/*      */   static final String FUNCTION_INIT_SIGNATURE = "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V";
/*      */   static final String FUNCTION_CONSTRUCTOR_SIGNATURE = "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V";
/*      */   
/*      */   public void captureStackInfo(RhinoException ex) {
/*   36 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public String getSourcePositionFromStack(Context cx, int[] linep) {
/*   40 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public String getPatchedStack(RhinoException ex, String nativeStackTrace) {
/*   44 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public List<String> getScriptStack(RhinoException ex) {
/*   48 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public void setEvalScriptFlag(Script script) {
/*   52 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction) {
/*      */     int serial;
/*   61 */     synchronized (globalLock) {
/*   62 */       serial = ++globalSerialClassCounter;
/*      */     } 
/*      */     
/*   65 */     String baseName = "c";
/*   66 */     if (tree.getSourceName().length() > 0) {
/*   67 */       baseName = tree.getSourceName().replaceAll("\\W", "_");
/*   68 */       if (!Character.isJavaIdentifierStart(baseName.charAt(0))) {
/*   69 */         baseName = "_" + baseName;
/*      */       }
/*      */     } 
/*      */     
/*   73 */     String mainClassName = "org.mozilla.javascript.gen." + baseName + "_" + serial;
/*      */     
/*   75 */     byte[] mainClassBytes = compileToClassFile(compilerEnv, mainClassName, tree, encodedSource, returnFunction);
/*      */ 
/*      */ 
/*      */     
/*   79 */     return new Object[] { mainClassName, mainClassBytes };
/*      */   }
/*      */ 
/*      */   
/*      */   public Script createScriptObject(Object bytecode, Object staticSecurityDomain) {
/*      */     Script script;
/*   85 */     Class<?> cl = defineClass(bytecode, staticSecurityDomain);
/*      */ 
/*      */     
/*      */     try {
/*   89 */       script = (Script)cl.newInstance();
/*   90 */     } catch (Exception ex) {
/*   91 */       throw new RuntimeException("Unable to instantiate compiled class:" + ex.toString());
/*      */     } 
/*      */     
/*   94 */     return script;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Function createFunctionObject(Context cx, Scriptable scope, Object bytecode, Object staticSecurityDomain) {
/*      */     NativeFunction f;
/*  101 */     Class<?> cl = defineClass(bytecode, staticSecurityDomain);
/*      */ 
/*      */     
/*      */     try {
/*  105 */       Constructor<?> ctor = cl.getConstructors()[0];
/*  106 */       Object[] initArgs = { scope, cx, Integer.valueOf(0) };
/*  107 */       f = (NativeFunction)ctor.newInstance(initArgs);
/*  108 */     } catch (Exception ex) {
/*  109 */       throw new RuntimeException("Unable to instantiate compiled class:" + ex.toString());
/*      */     } 
/*      */     
/*  112 */     return (Function)f;
/*      */   }
/*      */ 
/*      */   
/*      */   private Class<?> defineClass(Object bytecode, Object staticSecurityDomain) {
/*      */     Exception exception;
/*  118 */     Object[] nameBytesPair = (Object[])bytecode;
/*  119 */     String className = (String)nameBytesPair[0];
/*  120 */     byte[] classBytes = (byte[])nameBytesPair[1];
/*      */ 
/*      */ 
/*      */     
/*  124 */     ClassLoader rhinoLoader = getClass().getClassLoader();
/*      */     
/*  126 */     GeneratedClassLoader loader = SecurityController.createLoader(rhinoLoader, staticSecurityDomain);
/*      */ 
/*      */     
/*      */     try {
/*  130 */       Class<?> cl = loader.defineClass(className, classBytes);
/*  131 */       loader.linkClass(cl);
/*  132 */       return cl;
/*  133 */     } catch (SecurityException x) {
/*  134 */       exception = x;
/*  135 */     } catch (IllegalArgumentException x) {
/*  136 */       exception = x;
/*      */     } 
/*  138 */     throw new RuntimeException("Malformed optimizer package " + exception);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] compileToClassFile(CompilerEnvirons compilerEnv, String mainClassName, ScriptNode scriptOrFn, String encodedSource, boolean returnFunction) {
/*      */     FunctionNode functionNode;
/*  147 */     this.compilerEnv = compilerEnv;
/*      */     
/*  149 */     transform(scriptOrFn);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  155 */     if (returnFunction) {
/*  156 */       functionNode = scriptOrFn.getFunctionNode(0);
/*      */     }
/*      */     
/*  159 */     initScriptNodesData((ScriptNode)functionNode);
/*      */     
/*  161 */     this.mainClassName = mainClassName;
/*  162 */     this.mainClassSignature = ClassFileWriter.classNameToSignature(mainClassName);
/*      */ 
/*      */     
/*      */     try {
/*  166 */       return generateCode(encodedSource);
/*  167 */     } catch (org.mozilla.classfile.ClassFileWriter.ClassFileFormatException e) {
/*  168 */       throw reportClassFileFormatException(functionNode, e.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private RuntimeException reportClassFileFormatException(ScriptNode scriptOrFn, String message) {
/*  176 */     String msg = (scriptOrFn instanceof FunctionNode) ? ScriptRuntime.getMessage2("msg.while.compiling.fn", ((FunctionNode)scriptOrFn).getFunctionName(), message) : ScriptRuntime.getMessage1("msg.while.compiling.script", message);
/*      */ 
/*      */ 
/*      */     
/*  180 */     return (RuntimeException)Context.reportRuntimeError(msg, scriptOrFn.getSourceName(), scriptOrFn.getLineno(), null, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void transform(ScriptNode tree) {
/*  186 */     initOptFunctions_r(tree);
/*      */     
/*  188 */     int optLevel = this.compilerEnv.getOptimizationLevel();
/*      */     
/*  190 */     Map<String, OptFunctionNode> possibleDirectCalls = null;
/*  191 */     if (optLevel > 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  197 */       if (tree.getType() == 136) {
/*  198 */         int functionCount = tree.getFunctionCount();
/*  199 */         for (int i = 0; i != functionCount; i++) {
/*  200 */           OptFunctionNode ofn = OptFunctionNode.get(tree, i);
/*  201 */           if (ofn.fnode.getFunctionType() == 1) {
/*      */ 
/*      */             
/*  204 */             String name = ofn.fnode.getName();
/*  205 */             if (name.length() != 0) {
/*  206 */               if (possibleDirectCalls == null) {
/*  207 */                 possibleDirectCalls = new HashMap<String, OptFunctionNode>();
/*      */               }
/*  209 */               possibleDirectCalls.put(name, ofn);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  216 */     if (possibleDirectCalls != null) {
/*  217 */       this.directCallTargets = new ObjArray();
/*      */     }
/*      */     
/*  220 */     OptTransformer ot = new OptTransformer(possibleDirectCalls, this.directCallTargets);
/*      */     
/*  222 */     ot.transform(tree);
/*      */     
/*  224 */     if (optLevel > 0) {
/*  225 */       (new Optimizer()).optimize(tree);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void initOptFunctions_r(ScriptNode scriptOrFn) {
/*  231 */     for (int i = 0, N = scriptOrFn.getFunctionCount(); i != N; i++) {
/*  232 */       FunctionNode fn = scriptOrFn.getFunctionNode(i);
/*  233 */       new OptFunctionNode(fn);
/*  234 */       initOptFunctions_r((ScriptNode)fn);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void initScriptNodesData(ScriptNode scriptOrFn) {
/*  240 */     ObjArray x = new ObjArray();
/*  241 */     collectScriptNodes_r(scriptOrFn, x);
/*      */     
/*  243 */     int count = x.size();
/*  244 */     this.scriptOrFnNodes = new ScriptNode[count];
/*  245 */     x.toArray((Object[])this.scriptOrFnNodes);
/*      */     
/*  247 */     this.scriptOrFnIndexes = new ObjToIntMap(count);
/*  248 */     for (int i = 0; i != count; i++) {
/*  249 */       this.scriptOrFnIndexes.put(this.scriptOrFnNodes[i], i);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void collectScriptNodes_r(ScriptNode n, ObjArray x) {
/*  256 */     x.add(n);
/*  257 */     int nestedCount = n.getFunctionCount();
/*  258 */     for (int i = 0; i != nestedCount; i++) {
/*  259 */       collectScriptNodes_r((ScriptNode)n.getFunctionNode(i), x);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private byte[] generateCode(String encodedSource) {
/*  265 */     boolean hasScript = (this.scriptOrFnNodes[0].getType() == 136);
/*  266 */     boolean hasFunctions = (this.scriptOrFnNodes.length > 1 || !hasScript);
/*      */     
/*  268 */     String sourceFile = null;
/*  269 */     if (this.compilerEnv.isGenerateDebugInfo()) {
/*  270 */       sourceFile = this.scriptOrFnNodes[0].getSourceName();
/*      */     }
/*      */     
/*  273 */     ClassFileWriter cfw = new ClassFileWriter(this.mainClassName, "org.mozilla.javascript.NativeFunction", sourceFile);
/*      */ 
/*      */     
/*  276 */     cfw.addField("_id", "I", (short)2);
/*      */     
/*  278 */     if (hasFunctions) {
/*  279 */       generateFunctionConstructor(cfw);
/*      */     }
/*      */     
/*  282 */     if (hasScript) {
/*  283 */       cfw.addInterface("org/mozilla/javascript/Script");
/*  284 */       generateScriptCtor(cfw);
/*  285 */       generateMain(cfw);
/*  286 */       generateExecute(cfw);
/*      */     } 
/*      */     
/*  289 */     generateCallMethod(cfw);
/*  290 */     generateResumeGenerator(cfw);
/*      */     
/*  292 */     generateNativeFunctionOverrides(cfw, encodedSource);
/*      */     
/*  294 */     int count = this.scriptOrFnNodes.length;
/*  295 */     for (int i = 0; i != count; i++) {
/*  296 */       ScriptNode n = this.scriptOrFnNodes[i];
/*      */       
/*  298 */       BodyCodegen bodygen = new BodyCodegen();
/*  299 */       bodygen.cfw = cfw;
/*  300 */       bodygen.codegen = this;
/*  301 */       bodygen.compilerEnv = this.compilerEnv;
/*  302 */       bodygen.scriptOrFn = n;
/*  303 */       bodygen.scriptOrFnIndex = i;
/*      */       
/*      */       try {
/*  306 */         bodygen.generateBodyCode();
/*  307 */       } catch (org.mozilla.classfile.ClassFileWriter.ClassFileFormatException e) {
/*  308 */         throw reportClassFileFormatException(n, e.getMessage());
/*      */       } 
/*      */       
/*  311 */       if (n.getType() == 109) {
/*  312 */         OptFunctionNode ofn = OptFunctionNode.get(n);
/*  313 */         generateFunctionInit(cfw, ofn);
/*  314 */         if (ofn.isTargetOfDirectCall()) {
/*  315 */           emitDirectConstructor(cfw, ofn);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  320 */     emitRegExpInit(cfw);
/*  321 */     emitConstantDudeInitializers(cfw);
/*      */     
/*  323 */     return cfw.toByteArray();
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
/*      */   private void emitDirectConstructor(ClassFileWriter cfw, OptFunctionNode ofn) {
/*  340 */     cfw.startMethod(getDirectCtorName((ScriptNode)ofn.fnode), getBodyMethodSignature((ScriptNode)ofn.fnode), (short)10);
/*      */ 
/*      */ 
/*      */     
/*  344 */     int argCount = ofn.fnode.getParamCount();
/*  345 */     int firstLocal = 4 + argCount * 3 + 1;
/*      */     
/*  347 */     cfw.addALoad(0);
/*  348 */     cfw.addALoad(1);
/*  349 */     cfw.addALoad(2);
/*  350 */     cfw.addInvoke(182, "org/mozilla/javascript/BaseFunction", "createObject", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  356 */     cfw.addAStore(firstLocal);
/*      */     
/*  358 */     cfw.addALoad(0);
/*  359 */     cfw.addALoad(1);
/*  360 */     cfw.addALoad(2);
/*  361 */     cfw.addALoad(firstLocal);
/*  362 */     for (int i = 0; i < argCount; i++) {
/*  363 */       cfw.addALoad(4 + i * 3);
/*  364 */       cfw.addDLoad(5 + i * 3);
/*      */     } 
/*  366 */     cfw.addALoad(4 + argCount * 3);
/*  367 */     cfw.addInvoke(184, this.mainClassName, getBodyMethodName((ScriptNode)ofn.fnode), getBodyMethodSignature((ScriptNode)ofn.fnode));
/*      */ 
/*      */ 
/*      */     
/*  371 */     int exitLabel = cfw.acquireLabel();
/*  372 */     cfw.add(89);
/*  373 */     cfw.add(193, "org/mozilla/javascript/Scriptable");
/*  374 */     cfw.add(153, exitLabel);
/*      */     
/*  376 */     cfw.add(192, "org/mozilla/javascript/Scriptable");
/*  377 */     cfw.add(176);
/*  378 */     cfw.markLabel(exitLabel);
/*      */     
/*  380 */     cfw.addALoad(firstLocal);
/*  381 */     cfw.add(176);
/*      */     
/*  383 */     cfw.stopMethod((short)(firstLocal + 1));
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isGenerator(ScriptNode node) {
/*  388 */     return (node.getType() == 109 && ((FunctionNode)node).isGenerator());
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
/*      */   private void generateResumeGenerator(ClassFileWriter cfw) {
/*  406 */     boolean hasGenerators = false;
/*  407 */     for (int i = 0; i < this.scriptOrFnNodes.length; i++) {
/*  408 */       if (isGenerator(this.scriptOrFnNodes[i])) {
/*  409 */         hasGenerators = true;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  414 */     if (!hasGenerators) {
/*      */       return;
/*      */     }
/*  417 */     cfw.startMethod("resumeGenerator", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;ILjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  425 */     cfw.addALoad(0);
/*  426 */     cfw.addALoad(1);
/*  427 */     cfw.addALoad(2);
/*  428 */     cfw.addALoad(4);
/*  429 */     cfw.addALoad(5);
/*  430 */     cfw.addILoad(3);
/*      */     
/*  432 */     cfw.addLoadThis();
/*  433 */     cfw.add(180, cfw.getClassName(), "_id", "I");
/*      */     
/*  435 */     int startSwitch = cfw.addTableSwitch(0, this.scriptOrFnNodes.length - 1);
/*  436 */     cfw.markTableSwitchDefault(startSwitch);
/*  437 */     int endlabel = cfw.acquireLabel();
/*      */     
/*  439 */     for (int j = 0; j < this.scriptOrFnNodes.length; j++) {
/*  440 */       ScriptNode n = this.scriptOrFnNodes[j];
/*  441 */       cfw.markTableSwitchCase(startSwitch, j, 6);
/*  442 */       if (isGenerator(n)) {
/*  443 */         String type = "(" + this.mainClassSignature + "Lorg/mozilla/javascript/Context;" + "Lorg/mozilla/javascript/Scriptable;" + "Ljava/lang/Object;" + "Ljava/lang/Object;I)Ljava/lang/Object;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  449 */         cfw.addInvoke(184, this.mainClassName, getBodyMethodName(n) + "_gen", type);
/*      */ 
/*      */ 
/*      */         
/*  453 */         cfw.add(176);
/*      */       } else {
/*  455 */         cfw.add(167, endlabel);
/*      */       } 
/*      */     } 
/*      */     
/*  459 */     cfw.markLabel(endlabel);
/*  460 */     pushUndefined(cfw);
/*  461 */     cfw.add(176);
/*      */ 
/*      */ 
/*      */     
/*  465 */     cfw.stopMethod((short)6);
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateCallMethod(ClassFileWriter cfw) {
/*  470 */     cfw.startMethod("call", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  482 */     int nonTopCallLabel = cfw.acquireLabel();
/*  483 */     cfw.addALoad(1);
/*  484 */     cfw.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "hasTopCall", "(Lorg/mozilla/javascript/Context;)Z");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  489 */     cfw.add(154, nonTopCallLabel);
/*  490 */     cfw.addALoad(0);
/*  491 */     cfw.addALoad(1);
/*  492 */     cfw.addALoad(2);
/*  493 */     cfw.addALoad(3);
/*  494 */     cfw.addALoad(4);
/*  495 */     cfw.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "doTopCall", "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  504 */     cfw.add(176);
/*  505 */     cfw.markLabel(nonTopCallLabel);
/*      */ 
/*      */     
/*  508 */     cfw.addALoad(0);
/*  509 */     cfw.addALoad(1);
/*  510 */     cfw.addALoad(2);
/*  511 */     cfw.addALoad(3);
/*  512 */     cfw.addALoad(4);
/*      */     
/*  514 */     int end = this.scriptOrFnNodes.length;
/*  515 */     boolean generateSwitch = (2 <= end);
/*      */     
/*  517 */     int switchStart = 0;
/*  518 */     int switchStackTop = 0;
/*  519 */     if (generateSwitch) {
/*  520 */       cfw.addLoadThis();
/*  521 */       cfw.add(180, cfw.getClassName(), "_id", "I");
/*      */ 
/*      */       
/*  524 */       switchStart = cfw.addTableSwitch(1, end - 1);
/*      */     } 
/*      */     
/*  527 */     for (int i = 0; i != end; i++) {
/*  528 */       ScriptNode n = this.scriptOrFnNodes[i];
/*  529 */       if (generateSwitch) {
/*  530 */         if (i == 0) {
/*  531 */           cfw.markTableSwitchDefault(switchStart);
/*  532 */           switchStackTop = cfw.getStackTop();
/*      */         } else {
/*  534 */           cfw.markTableSwitchCase(switchStart, i - 1, switchStackTop);
/*      */         } 
/*      */       }
/*      */       
/*  538 */       if (n.getType() == 109) {
/*  539 */         OptFunctionNode ofn = OptFunctionNode.get(n);
/*  540 */         if (ofn.isTargetOfDirectCall()) {
/*  541 */           int pcount = ofn.fnode.getParamCount();
/*  542 */           if (pcount != 0)
/*      */           {
/*      */             
/*  545 */             for (int p = 0; p != pcount; p++) {
/*  546 */               cfw.add(190);
/*  547 */               cfw.addPush(p);
/*  548 */               int undefArg = cfw.acquireLabel();
/*  549 */               int beyond = cfw.acquireLabel();
/*  550 */               cfw.add(164, undefArg);
/*      */               
/*  552 */               cfw.addALoad(4);
/*  553 */               cfw.addPush(p);
/*  554 */               cfw.add(50);
/*  555 */               cfw.add(167, beyond);
/*  556 */               cfw.markLabel(undefArg);
/*  557 */               pushUndefined(cfw);
/*  558 */               cfw.markLabel(beyond);
/*      */               
/*  560 */               cfw.adjustStackTop(-1);
/*  561 */               cfw.addPush(0.0D);
/*      */               
/*  563 */               cfw.addALoad(4);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*  568 */       cfw.addInvoke(184, this.mainClassName, getBodyMethodName(n), getBodyMethodSignature(n));
/*      */ 
/*      */ 
/*      */       
/*  572 */       cfw.add(176);
/*      */     } 
/*  574 */     cfw.stopMethod((short)5);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateMain(ClassFileWriter cfw) {
/*  580 */     cfw.startMethod("main", "([Ljava/lang/String;)V", (short)9);
/*      */ 
/*      */ 
/*      */     
/*  584 */     cfw.add(187, cfw.getClassName());
/*  585 */     cfw.add(89);
/*  586 */     cfw.addInvoke(183, cfw.getClassName(), "<init>", "()V");
/*      */ 
/*      */     
/*  589 */     cfw.add(42);
/*      */     
/*  591 */     cfw.addInvoke(184, this.mainMethodClass, "main", "(Lorg/mozilla/javascript/Script;[Ljava/lang/String;)V");
/*      */ 
/*      */ 
/*      */     
/*  595 */     cfw.add(177);
/*      */     
/*  597 */     cfw.stopMethod((short)1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateExecute(ClassFileWriter cfw) {
/*  602 */     cfw.startMethod("exec", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;", (short)17);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  608 */     int CONTEXT_ARG = 1;
/*  609 */     int SCOPE_ARG = 2;
/*      */     
/*  611 */     cfw.addLoadThis();
/*  612 */     cfw.addALoad(1);
/*  613 */     cfw.addALoad(2);
/*  614 */     cfw.add(89);
/*  615 */     cfw.add(1);
/*  616 */     cfw.addInvoke(182, cfw.getClassName(), "call", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  625 */     cfw.add(176);
/*      */     
/*  627 */     cfw.stopMethod((short)3);
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateScriptCtor(ClassFileWriter cfw) {
/*  632 */     cfw.startMethod("<init>", "()V", (short)1);
/*      */     
/*  634 */     cfw.addLoadThis();
/*  635 */     cfw.addInvoke(183, "org.mozilla.javascript.NativeFunction", "<init>", "()V");
/*      */ 
/*      */     
/*  638 */     cfw.addLoadThis();
/*  639 */     cfw.addPush(0);
/*  640 */     cfw.add(181, cfw.getClassName(), "_id", "I");
/*      */     
/*  642 */     cfw.add(177);
/*      */     
/*  644 */     cfw.stopMethod((short)1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateFunctionConstructor(ClassFileWriter cfw) {
/*  649 */     int SCOPE_ARG = 1;
/*  650 */     int CONTEXT_ARG = 2;
/*  651 */     int ID_ARG = 3;
/*      */     
/*  653 */     cfw.startMethod("<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V", (short)1);
/*  654 */     cfw.addALoad(0);
/*  655 */     cfw.addInvoke(183, "org.mozilla.javascript.NativeFunction", "<init>", "()V");
/*      */ 
/*      */     
/*  658 */     cfw.addLoadThis();
/*  659 */     cfw.addILoad(3);
/*  660 */     cfw.add(181, cfw.getClassName(), "_id", "I");
/*      */     
/*  662 */     cfw.addLoadThis();
/*  663 */     cfw.addALoad(2);
/*  664 */     cfw.addALoad(1);
/*      */     
/*  666 */     int start = (this.scriptOrFnNodes[0].getType() == 136) ? 1 : 0;
/*  667 */     int end = this.scriptOrFnNodes.length;
/*  668 */     if (start == end) throw badTree(); 
/*  669 */     boolean generateSwitch = (2 <= end - start);
/*      */     
/*  671 */     int switchStart = 0;
/*  672 */     int switchStackTop = 0;
/*  673 */     if (generateSwitch) {
/*  674 */       cfw.addILoad(3);
/*      */ 
/*      */       
/*  677 */       switchStart = cfw.addTableSwitch(start + 1, end - 1);
/*      */     } 
/*      */     
/*  680 */     for (int i = start; i != end; i++) {
/*  681 */       if (generateSwitch) {
/*  682 */         if (i == start) {
/*  683 */           cfw.markTableSwitchDefault(switchStart);
/*  684 */           switchStackTop = cfw.getStackTop();
/*      */         } else {
/*  686 */           cfw.markTableSwitchCase(switchStart, i - 1 - start, switchStackTop);
/*      */         } 
/*      */       }
/*      */       
/*  690 */       OptFunctionNode ofn = OptFunctionNode.get(this.scriptOrFnNodes[i]);
/*  691 */       cfw.addInvoke(183, this.mainClassName, getFunctionInitMethodName(ofn), "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V");
/*      */ 
/*      */ 
/*      */       
/*  695 */       cfw.add(177);
/*      */     } 
/*      */ 
/*      */     
/*  699 */     cfw.stopMethod((short)4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateFunctionInit(ClassFileWriter cfw, OptFunctionNode ofn) {
/*  705 */     int CONTEXT_ARG = 1;
/*  706 */     int SCOPE_ARG = 2;
/*  707 */     cfw.startMethod(getFunctionInitMethodName(ofn), "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V", (short)18);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  712 */     cfw.addLoadThis();
/*  713 */     cfw.addALoad(1);
/*  714 */     cfw.addALoad(2);
/*  715 */     cfw.addInvoke(182, "org/mozilla/javascript/NativeFunction", "initScriptFunction", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  723 */     if (ofn.fnode.getRegexpCount() != 0) {
/*  724 */       cfw.addALoad(1);
/*  725 */       cfw.addInvoke(184, this.mainClassName, "_reInit", "(Lorg/mozilla/javascript/Context;)V");
/*      */     } 
/*      */ 
/*      */     
/*  729 */     cfw.add(177);
/*      */     
/*  731 */     cfw.stopMethod((short)3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateNativeFunctionOverrides(ClassFileWriter cfw, String encodedSource) {
/*  740 */     cfw.startMethod("getLanguageVersion", "()I", (short)1);
/*      */     
/*  742 */     cfw.addPush(this.compilerEnv.getLanguageVersion());
/*  743 */     cfw.add(172);
/*      */ 
/*      */     
/*  746 */     cfw.stopMethod((short)1);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  751 */     int Do_getFunctionName = 0;
/*  752 */     int Do_getParamCount = 1;
/*  753 */     int Do_getParamAndVarCount = 2;
/*  754 */     int Do_getParamOrVarName = 3;
/*  755 */     int Do_getEncodedSource = 4;
/*  756 */     int Do_getParamOrVarConst = 5;
/*  757 */     int SWITCH_COUNT = 6;
/*      */     
/*  759 */     for (int methodIndex = 0; methodIndex != 6; methodIndex++) {
/*  760 */       if (methodIndex != 4 || encodedSource != null) {
/*      */         short methodLocals;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  770 */         switch (methodIndex) {
/*      */           case 0:
/*  772 */             methodLocals = 1;
/*  773 */             cfw.startMethod("getFunctionName", "()Ljava/lang/String;", (short)1);
/*      */             break;
/*      */           
/*      */           case 1:
/*  777 */             methodLocals = 1;
/*  778 */             cfw.startMethod("getParamCount", "()I", (short)1);
/*      */             break;
/*      */           
/*      */           case 2:
/*  782 */             methodLocals = 1;
/*  783 */             cfw.startMethod("getParamAndVarCount", "()I", (short)1);
/*      */             break;
/*      */           
/*      */           case 3:
/*  787 */             methodLocals = 2;
/*  788 */             cfw.startMethod("getParamOrVarName", "(I)Ljava/lang/String;", (short)1);
/*      */             break;
/*      */           
/*      */           case 5:
/*  792 */             methodLocals = 3;
/*  793 */             cfw.startMethod("getParamOrVarConst", "(I)Z", (short)1);
/*      */             break;
/*      */           
/*      */           case 4:
/*  797 */             methodLocals = 1;
/*  798 */             cfw.startMethod("getEncodedSource", "()Ljava/lang/String;", (short)1);
/*      */             
/*  800 */             cfw.addPush(encodedSource);
/*      */             break;
/*      */           default:
/*  803 */             throw Kit.codeBug();
/*      */         } 
/*      */         
/*  806 */         int count = this.scriptOrFnNodes.length;
/*      */         
/*  808 */         int switchStart = 0;
/*  809 */         int switchStackTop = 0;
/*  810 */         if (count > 1) {
/*      */ 
/*      */           
/*  813 */           cfw.addLoadThis();
/*  814 */           cfw.add(180, cfw.getClassName(), "_id", "I");
/*      */ 
/*      */ 
/*      */           
/*  818 */           switchStart = cfw.addTableSwitch(1, count - 1);
/*      */         } 
/*      */         
/*  821 */         for (int i = 0; i != count; i++) {
/*  822 */           int paramAndVarCount, paramSwitchStart; boolean[] constness; int j, k, m; ScriptNode n = this.scriptOrFnNodes[i];
/*  823 */           if (i == 0) {
/*  824 */             if (count > 1) {
/*  825 */               cfw.markTableSwitchDefault(switchStart);
/*  826 */               switchStackTop = cfw.getStackTop();
/*      */             } 
/*      */           } else {
/*  829 */             cfw.markTableSwitchCase(switchStart, i - 1, switchStackTop);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  834 */           switch (methodIndex) {
/*      */             
/*      */             case 0:
/*  837 */               if (n.getType() == 136) {
/*  838 */                 cfw.addPush("");
/*      */               } else {
/*  840 */                 String name = ((FunctionNode)n).getName();
/*  841 */                 cfw.addPush(name);
/*      */               } 
/*  843 */               cfw.add(176);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 1:
/*  848 */               cfw.addPush(n.getParamCount());
/*  849 */               cfw.add(172);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 2:
/*  854 */               cfw.addPush(n.getParamAndVarCount());
/*  855 */               cfw.add(172);
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             case 3:
/*  861 */               paramAndVarCount = n.getParamAndVarCount();
/*  862 */               if (paramAndVarCount == 0) {
/*      */ 
/*      */ 
/*      */                 
/*  866 */                 cfw.add(1);
/*  867 */                 cfw.add(176); break;
/*  868 */               }  if (paramAndVarCount == 1) {
/*      */ 
/*      */                 
/*  871 */                 cfw.addPush(n.getParamOrVarName(0));
/*  872 */                 cfw.add(176);
/*      */                 break;
/*      */               } 
/*  875 */               cfw.addILoad(1);
/*      */ 
/*      */               
/*  878 */               paramSwitchStart = cfw.addTableSwitch(1, paramAndVarCount - 1);
/*      */               
/*  880 */               for (j = 0; j != paramAndVarCount; j++) {
/*  881 */                 if (cfw.getStackTop() != 0) Kit.codeBug(); 
/*  882 */                 String s = n.getParamOrVarName(j);
/*  883 */                 if (j == 0) {
/*  884 */                   cfw.markTableSwitchDefault(paramSwitchStart);
/*      */                 } else {
/*  886 */                   cfw.markTableSwitchCase(paramSwitchStart, j - 1, 0);
/*      */                 } 
/*      */                 
/*  889 */                 cfw.addPush(s);
/*  890 */                 cfw.add(176);
/*      */               } 
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 5:
/*  898 */               paramAndVarCount = n.getParamAndVarCount();
/*  899 */               constness = n.getParamAndVarConst();
/*  900 */               if (paramAndVarCount == 0) {
/*      */ 
/*      */ 
/*      */                 
/*  904 */                 cfw.add(3);
/*  905 */                 cfw.add(172); break;
/*  906 */               }  if (paramAndVarCount == 1) {
/*      */ 
/*      */                 
/*  909 */                 cfw.addPush(constness[0]);
/*  910 */                 cfw.add(172);
/*      */                 break;
/*      */               } 
/*  913 */               cfw.addILoad(1);
/*      */ 
/*      */               
/*  916 */               k = cfw.addTableSwitch(1, paramAndVarCount - 1);
/*      */               
/*  918 */               for (m = 0; m != paramAndVarCount; m++) {
/*  919 */                 if (cfw.getStackTop() != 0) Kit.codeBug(); 
/*  920 */                 if (m == 0) {
/*  921 */                   cfw.markTableSwitchDefault(k);
/*      */                 } else {
/*  923 */                   cfw.markTableSwitchCase(k, m - 1, 0);
/*      */                 } 
/*      */                 
/*  926 */                 cfw.addPush(constness[m]);
/*  927 */                 cfw.add(172);
/*      */               } 
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 4:
/*  935 */               cfw.addPush(n.getEncodedSourceStart());
/*  936 */               cfw.addPush(n.getEncodedSourceEnd());
/*  937 */               cfw.addInvoke(182, "java/lang/String", "substring", "(II)Ljava/lang/String;");
/*      */ 
/*      */ 
/*      */               
/*  941 */               cfw.add(176);
/*      */               break;
/*      */             
/*      */             default:
/*  945 */               throw Kit.codeBug();
/*      */           } 
/*      */         
/*      */         } 
/*  949 */         cfw.stopMethod(methodLocals);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void emitRegExpInit(ClassFileWriter cfw) {
/*  957 */     int totalRegCount = 0;
/*  958 */     for (int i = 0; i != this.scriptOrFnNodes.length; i++) {
/*  959 */       totalRegCount += this.scriptOrFnNodes[i].getRegexpCount();
/*      */     }
/*  961 */     if (totalRegCount == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  965 */     cfw.startMethod("_reInit", "(Lorg/mozilla/javascript/Context;)V", (short)10);
/*      */     
/*  967 */     cfw.addField("_reInitDone", "Z", (short)74);
/*      */     
/*  969 */     cfw.add(178, this.mainClassName, "_reInitDone", "Z");
/*  970 */     int doInit = cfw.acquireLabel();
/*  971 */     cfw.add(153, doInit);
/*  972 */     cfw.add(177);
/*  973 */     cfw.markLabel(doInit);
/*      */ 
/*      */     
/*  976 */     cfw.addALoad(0);
/*  977 */     cfw.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "checkRegExpProxy", "(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/RegExpProxy;");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  982 */     cfw.addAStore(1);
/*      */ 
/*      */ 
/*      */     
/*  986 */     for (int j = 0; j != this.scriptOrFnNodes.length; j++) {
/*  987 */       ScriptNode n = this.scriptOrFnNodes[j];
/*  988 */       int regCount = n.getRegexpCount();
/*  989 */       for (int k = 0; k != regCount; k++) {
/*  990 */         String reFieldName = getCompiledRegexpName(n, k);
/*  991 */         String reFieldType = "Ljava/lang/Object;";
/*  992 */         String reString = n.getRegexpString(k);
/*  993 */         String reFlags = n.getRegexpFlags(k);
/*  994 */         cfw.addField(reFieldName, reFieldType, (short)10);
/*      */         
/*  996 */         cfw.addALoad(1);
/*  997 */         cfw.addALoad(0);
/*  998 */         cfw.addPush(reString);
/*  999 */         if (reFlags == null) {
/* 1000 */           cfw.add(1);
/*      */         } else {
/* 1002 */           cfw.addPush(reFlags);
/*      */         } 
/* 1004 */         cfw.addInvoke(185, "org/mozilla/javascript/RegExpProxy", "compileRegExp", "(Lorg/mozilla/javascript/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1010 */         cfw.add(179, this.mainClassName, reFieldName, reFieldType);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1015 */     cfw.addPush(1);
/* 1016 */     cfw.add(179, this.mainClassName, "_reInitDone", "Z");
/* 1017 */     cfw.add(177);
/* 1018 */     cfw.stopMethod((short)2);
/*      */   }
/*      */ 
/*      */   
/*      */   private void emitConstantDudeInitializers(ClassFileWriter cfw) {
/* 1023 */     int N = this.itsConstantListSize;
/* 1024 */     if (N == 0) {
/*      */       return;
/*      */     }
/* 1027 */     cfw.startMethod("<clinit>", "()V", (short)24);
/*      */     
/* 1029 */     double[] array = this.itsConstantList;
/* 1030 */     for (int i = 0; i != N; i++) {
/* 1031 */       double num = array[i];
/* 1032 */       String constantName = "_k" + i;
/* 1033 */       String constantType = getStaticConstantWrapperType(num);
/* 1034 */       cfw.addField(constantName, constantType, (short)10);
/*      */       
/* 1036 */       int inum = (int)num;
/* 1037 */       if (inum == num) {
/* 1038 */         cfw.addPush(inum);
/* 1039 */         cfw.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
/*      */       } else {
/*      */         
/* 1042 */         cfw.addPush(num);
/* 1043 */         addDoubleWrap(cfw);
/*      */       } 
/* 1045 */       cfw.add(179, this.mainClassName, constantName, constantType);
/*      */     } 
/*      */ 
/*      */     
/* 1049 */     cfw.add(177);
/* 1050 */     cfw.stopMethod((short)0);
/*      */   }
/*      */ 
/*      */   
/*      */   void pushNumberAsObject(ClassFileWriter cfw, double num) {
/* 1055 */     if (num == 0.0D) {
/* 1056 */       if (1.0D / num > 0.0D) {
/*      */         
/* 1058 */         cfw.add(178, "org/mozilla/javascript/optimizer/OptRuntime", "zeroObj", "Ljava/lang/Double;");
/*      */       }
/*      */       else {
/*      */         
/* 1062 */         cfw.addPush(num);
/* 1063 */         addDoubleWrap(cfw);
/*      */       } 
/*      */     } else {
/* 1066 */       if (num == 1.0D) {
/* 1067 */         cfw.add(178, "org/mozilla/javascript/optimizer/OptRuntime", "oneObj", "Ljava/lang/Double;");
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1072 */       if (num == -1.0D) {
/* 1073 */         cfw.add(178, "org/mozilla/javascript/optimizer/OptRuntime", "minusOneObj", "Ljava/lang/Double;");
/*      */ 
/*      */       
/*      */       }
/* 1077 */       else if (num != num) {
/* 1078 */         cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "NaNobj", "Ljava/lang/Double;");
/*      */ 
/*      */       
/*      */       }
/* 1082 */       else if (this.itsConstantListSize >= 2000) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1087 */         cfw.addPush(num);
/* 1088 */         addDoubleWrap(cfw);
/*      */       } else {
/*      */         
/* 1091 */         int N = this.itsConstantListSize;
/* 1092 */         int index = 0;
/* 1093 */         if (N == 0) {
/* 1094 */           this.itsConstantList = new double[64];
/*      */         } else {
/* 1096 */           double[] array = this.itsConstantList;
/* 1097 */           while (index != N && array[index] != num) {
/* 1098 */             index++;
/*      */           }
/* 1100 */           if (N == array.length) {
/* 1101 */             array = new double[N * 2];
/* 1102 */             System.arraycopy(this.itsConstantList, 0, array, 0, N);
/* 1103 */             this.itsConstantList = array;
/*      */           } 
/*      */         } 
/* 1106 */         if (index == N) {
/* 1107 */           this.itsConstantList[N] = num;
/* 1108 */           this.itsConstantListSize = N + 1;
/*      */         } 
/* 1110 */         String constantName = "_k" + index;
/* 1111 */         String constantType = getStaticConstantWrapperType(num);
/* 1112 */         cfw.add(178, this.mainClassName, constantName, constantType);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addDoubleWrap(ClassFileWriter cfw) {
/* 1119 */     cfw.addInvoke(184, "org/mozilla/javascript/optimizer/OptRuntime", "wrapDouble", "(D)Ljava/lang/Double;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getStaticConstantWrapperType(double num) {
/* 1126 */     int inum = (int)num;
/* 1127 */     if (inum == num) {
/* 1128 */       return "Ljava/lang/Integer;";
/*      */     }
/* 1130 */     return "Ljava/lang/Double;";
/*      */   }
/*      */ 
/*      */   
/*      */   static void pushUndefined(ClassFileWriter cfw) {
/* 1135 */     cfw.add(178, "org/mozilla/javascript/Undefined", "instance", "Ljava/lang/Object;");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   int getIndex(ScriptNode n) {
/* 1141 */     return this.scriptOrFnIndexes.getExisting(n);
/*      */   }
/*      */ 
/*      */   
/*      */   String getDirectCtorName(ScriptNode n) {
/* 1146 */     return "_n" + getIndex(n);
/*      */   }
/*      */ 
/*      */   
/*      */   String getBodyMethodName(ScriptNode n) {
/* 1151 */     return "_c_" + cleanName(n) + "_" + getIndex(n);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String cleanName(ScriptNode n) {
/* 1159 */     String result = "";
/* 1160 */     if (n instanceof FunctionNode) {
/* 1161 */       Name name = ((FunctionNode)n).getFunctionName();
/* 1162 */       if (name == null) {
/* 1163 */         result = "anonymous";
/*      */       } else {
/* 1165 */         result = name.getIdentifier();
/*      */       } 
/*      */     } else {
/* 1168 */       result = "script";
/*      */     } 
/* 1170 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   String getBodyMethodSignature(ScriptNode n) {
/* 1175 */     StringBuilder sb = new StringBuilder();
/* 1176 */     sb.append('(');
/* 1177 */     sb.append(this.mainClassSignature);
/* 1178 */     sb.append("Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/* 1181 */     if (n.getType() == 109) {
/* 1182 */       OptFunctionNode ofn = OptFunctionNode.get(n);
/* 1183 */       if (ofn.isTargetOfDirectCall()) {
/* 1184 */         int pCount = ofn.fnode.getParamCount();
/* 1185 */         for (int i = 0; i != pCount; i++) {
/* 1186 */           sb.append("Ljava/lang/Object;D");
/*      */         }
/*      */       } 
/*      */     } 
/* 1190 */     sb.append("[Ljava/lang/Object;)Ljava/lang/Object;");
/* 1191 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   String getFunctionInitMethodName(OptFunctionNode ofn) {
/* 1196 */     return "_i" + getIndex((ScriptNode)ofn.fnode);
/*      */   }
/*      */ 
/*      */   
/*      */   String getCompiledRegexpName(ScriptNode n, int regexpIndex) {
/* 1201 */     return "_re" + getIndex(n) + "_" + regexpIndex;
/*      */   }
/*      */ 
/*      */   
/*      */   static RuntimeException badTree() {
/* 1206 */     throw new RuntimeException("Bad tree in codegen");
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMainMethodClass(String className) {
/* 1211 */     this.mainMethodClass = className;
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
/* 1235 */   private static final Object globalLock = new Object();
/*      */   
/*      */   private static int globalSerialClassCounter;
/*      */   
/*      */   private CompilerEnvirons compilerEnv;
/*      */   
/*      */   private ObjArray directCallTargets;
/*      */   ScriptNode[] scriptOrFnNodes;
/*      */   private ObjToIntMap scriptOrFnIndexes;
/* 1244 */   private String mainMethodClass = "org.mozilla.javascript.optimizer.OptRuntime";
/*      */   String mainClassName;
/*      */   String mainClassSignature;
/*      */   private double[] itsConstantList;
/*      */   private int itsConstantListSize;
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\optimizer\Codegen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */