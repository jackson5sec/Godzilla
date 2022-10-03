/*      */ package org.mozilla.javascript.optimizer;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import org.mozilla.classfile.ClassFileWriter;
/*      */ import org.mozilla.javascript.CompilerEnvirons;
/*      */ import org.mozilla.javascript.Context;
/*      */ import org.mozilla.javascript.Kit;
/*      */ import org.mozilla.javascript.Node;
/*      */ import org.mozilla.javascript.Token;
/*      */ import org.mozilla.javascript.ast.FunctionNode;
/*      */ import org.mozilla.javascript.ast.Jump;
/*      */ import org.mozilla.javascript.ast.ScriptNode;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class BodyCodegen
/*      */ {
/*      */   private static final int JAVASCRIPT_EXCEPTION = 0;
/*      */   private static final int EVALUATOR_EXCEPTION = 1;
/*      */   private static final int ECMAERROR_EXCEPTION = 2;
/*      */   private static final int THROWABLE_EXCEPTION = 3;
/*      */   private static final int FINALLY_EXCEPTION = 4;
/*      */   private static final int EXCEPTION_MAX = 5;
/*      */   
/*      */   void generateBodyCode() {
/*      */     ScriptNode scriptNode;
/* 1258 */     this.isGenerator = Codegen.isGenerator(this.scriptOrFn);
/*      */ 
/*      */     
/* 1261 */     initBodyGeneration();
/*      */     
/* 1263 */     if (this.isGenerator) {
/*      */ 
/*      */ 
/*      */       
/* 1267 */       String type = "(" + this.codegen.mainClassSignature + "Lorg/mozilla/javascript/Context;" + "Lorg/mozilla/javascript/Scriptable;" + "Ljava/lang/Object;" + "Ljava/lang/Object;I)Ljava/lang/Object;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1273 */       this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn) + "_gen", type, (short)10);
/*      */     }
/*      */     else {
/*      */       
/* 1277 */       this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short)10);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1282 */     generatePrologue();
/*      */     
/* 1284 */     if (this.fnCurrent != null) {
/* 1285 */       Node treeTop = this.scriptOrFn.getLastChild();
/*      */     } else {
/* 1287 */       scriptNode = this.scriptOrFn;
/*      */     } 
/* 1289 */     generateStatement((Node)scriptNode);
/* 1290 */     generateEpilogue();
/*      */     
/* 1292 */     this.cfw.stopMethod((short)(this.localsMax + 1));
/*      */     
/* 1294 */     if (this.isGenerator)
/*      */     {
/*      */       
/* 1297 */       generateGenerator();
/*      */     }
/*      */     
/* 1300 */     if (this.literals != null)
/*      */     {
/* 1302 */       for (int i = 0; i < this.literals.size(); i++) {
/* 1303 */         Node node = this.literals.get(i);
/* 1304 */         int type = node.getType();
/* 1305 */         switch (type) {
/*      */           case 66:
/* 1307 */             generateObjectLiteralFactory(node, i + 1);
/*      */             break;
/*      */           case 65:
/* 1310 */             generateArrayLiteralFactory(node, i + 1);
/*      */             break;
/*      */           default:
/* 1313 */             Kit.codeBug(Token.typeToName(type));
/*      */             break;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateGenerator() {
/* 1324 */     this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short)10);
/*      */ 
/*      */ 
/*      */     
/* 1328 */     initBodyGeneration();
/* 1329 */     this.argsLocal = this.firstFreeLocal = (short)(this.firstFreeLocal + 1);
/* 1330 */     this.localsMax = this.firstFreeLocal;
/*      */ 
/*      */     
/* 1333 */     if (this.fnCurrent != null) {
/*      */ 
/*      */ 
/*      */       
/* 1337 */       this.cfw.addALoad(this.funObjLocal);
/* 1338 */       this.cfw.addInvoke(185, "org/mozilla/javascript/Scriptable", "getParentScope", "()Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */       
/* 1342 */       this.cfw.addAStore(this.variableObjectLocal);
/*      */     } 
/*      */ 
/*      */     
/* 1346 */     this.cfw.addALoad(this.funObjLocal);
/* 1347 */     this.cfw.addALoad(this.variableObjectLocal);
/* 1348 */     this.cfw.addALoad(this.argsLocal);
/* 1349 */     addScriptRuntimeInvoke("createFunctionActivation", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1354 */     this.cfw.addAStore(this.variableObjectLocal);
/*      */ 
/*      */     
/* 1357 */     this.cfw.add(187, this.codegen.mainClassName);
/*      */     
/* 1359 */     this.cfw.add(89);
/* 1360 */     this.cfw.addALoad(this.variableObjectLocal);
/* 1361 */     this.cfw.addALoad(this.contextLocal);
/* 1362 */     this.cfw.addPush(this.scriptOrFnIndex);
/* 1363 */     this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V");
/*      */ 
/*      */     
/* 1366 */     generateNestedFunctionInits();
/*      */ 
/*      */     
/* 1369 */     this.cfw.addALoad(this.variableObjectLocal);
/* 1370 */     this.cfw.addALoad(this.thisObjLocal);
/* 1371 */     this.cfw.addLoadConstant(this.maxLocals);
/* 1372 */     this.cfw.addLoadConstant(this.maxStack);
/* 1373 */     addOptRuntimeInvoke("createNativeGenerator", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;II)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1379 */     this.cfw.add(176);
/* 1380 */     this.cfw.stopMethod((short)(this.localsMax + 1));
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateNestedFunctionInits() {
/* 1385 */     int functionCount = this.scriptOrFn.getFunctionCount();
/* 1386 */     for (int i = 0; i != functionCount; i++) {
/* 1387 */       OptFunctionNode ofn = OptFunctionNode.get(this.scriptOrFn, i);
/* 1388 */       if (ofn.fnode.getFunctionType() == 1)
/*      */       {
/*      */         
/* 1391 */         visitFunction(ofn, 1);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void initBodyGeneration() {
/* 1398 */     this.varRegisters = null;
/* 1399 */     if (this.scriptOrFn.getType() == 109) {
/* 1400 */       this.fnCurrent = OptFunctionNode.get(this.scriptOrFn);
/* 1401 */       this.hasVarsInRegs = !this.fnCurrent.fnode.requiresActivation();
/* 1402 */       if (this.hasVarsInRegs) {
/* 1403 */         int n = this.fnCurrent.fnode.getParamAndVarCount();
/* 1404 */         if (n != 0) {
/* 1405 */           this.varRegisters = new short[n];
/*      */         }
/*      */       } 
/* 1408 */       this.inDirectCallFunction = this.fnCurrent.isTargetOfDirectCall();
/* 1409 */       if (this.inDirectCallFunction && !this.hasVarsInRegs) Codegen.badTree(); 
/*      */     } else {
/* 1411 */       this.fnCurrent = null;
/* 1412 */       this.hasVarsInRegs = false;
/* 1413 */       this.inDirectCallFunction = false;
/*      */     } 
/*      */     
/* 1416 */     this.locals = new int[1024];
/*      */     
/* 1418 */     this.funObjLocal = 0;
/* 1419 */     this.contextLocal = 1;
/* 1420 */     this.variableObjectLocal = 2;
/* 1421 */     this.thisObjLocal = 3;
/* 1422 */     this.localsMax = 4;
/* 1423 */     this.firstFreeLocal = 4;
/*      */     
/* 1425 */     this.popvLocal = -1;
/* 1426 */     this.argsLocal = -1;
/* 1427 */     this.itsZeroArgArray = -1;
/* 1428 */     this.itsOneArgArray = -1;
/* 1429 */     this.epilogueLabel = -1;
/* 1430 */     this.enterAreaStartLabel = -1;
/* 1431 */     this.generatorStateLocal = -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generatePrologue() {
/*      */     String debugVariableName;
/* 1439 */     if (this.inDirectCallFunction) {
/* 1440 */       int directParameterCount = this.scriptOrFn.getParamCount();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1445 */       if (this.firstFreeLocal != 4) Kit.codeBug();  int i;
/* 1446 */       for (i = 0; i != directParameterCount; i++) {
/* 1447 */         this.varRegisters[i] = this.firstFreeLocal;
/*      */         
/* 1449 */         this.firstFreeLocal = (short)(this.firstFreeLocal + 3);
/*      */       } 
/* 1451 */       if (!this.fnCurrent.getParameterNumberContext()) {
/*      */         
/* 1453 */         this.itsForcedObjectParameters = true;
/* 1454 */         for (i = 0; i != directParameterCount; i++) {
/* 1455 */           short reg = this.varRegisters[i];
/* 1456 */           this.cfw.addALoad(reg);
/* 1457 */           this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */           
/* 1461 */           int isObjectLabel = this.cfw.acquireLabel();
/* 1462 */           this.cfw.add(166, isObjectLabel);
/* 1463 */           this.cfw.addDLoad(reg + 1);
/* 1464 */           addDoubleWrap();
/* 1465 */           this.cfw.addAStore(reg);
/* 1466 */           this.cfw.markLabel(isObjectLabel);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1471 */     if (this.fnCurrent != null) {
/*      */       
/* 1473 */       this.cfw.addALoad(this.funObjLocal);
/* 1474 */       this.cfw.addInvoke(185, "org/mozilla/javascript/Scriptable", "getParentScope", "()Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */       
/* 1478 */       this.cfw.addAStore(this.variableObjectLocal);
/*      */     } 
/*      */ 
/*      */     
/* 1482 */     this.argsLocal = this.firstFreeLocal = (short)(this.firstFreeLocal + 1);
/* 1483 */     this.localsMax = this.firstFreeLocal;
/*      */ 
/*      */     
/* 1486 */     if (this.isGenerator) {
/*      */ 
/*      */       
/* 1489 */       this.operationLocal = this.firstFreeLocal = (short)(this.firstFreeLocal + 1);
/* 1490 */       this.localsMax = this.firstFreeLocal;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1496 */       this.cfw.addALoad(this.thisObjLocal);
/* 1497 */       this.generatorStateLocal = this.firstFreeLocal = (short)(this.firstFreeLocal + 1);
/* 1498 */       this.localsMax = this.firstFreeLocal;
/* 1499 */       this.cfw.add(192, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState");
/* 1500 */       this.cfw.add(89);
/* 1501 */       this.cfw.addAStore(this.generatorStateLocal);
/* 1502 */       this.cfw.add(180, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "thisObj", "Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */       
/* 1506 */       this.cfw.addAStore(this.thisObjLocal);
/*      */       
/* 1508 */       if (this.epilogueLabel == -1) {
/* 1509 */         this.epilogueLabel = this.cfw.acquireLabel();
/*      */       }
/*      */       
/* 1512 */       List<Node> targets = ((FunctionNode)this.scriptOrFn).getResumptionPoints();
/* 1513 */       if (targets != null) {
/*      */         
/* 1515 */         generateGetGeneratorResumptionPoint();
/*      */ 
/*      */         
/* 1518 */         this.generatorSwitch = this.cfw.addTableSwitch(0, targets.size() + 0);
/*      */         
/* 1520 */         generateCheckForThrowOrClose(-1, false, 0);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1526 */     if (this.fnCurrent == null && this.scriptOrFn.getRegexpCount() != 0) {
/* 1527 */       this.cfw.addALoad(this.contextLocal);
/* 1528 */       this.cfw.addInvoke(184, this.codegen.mainClassName, "_reInit", "(Lorg/mozilla/javascript/Context;)V");
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1533 */     if (this.compilerEnv.isGenerateObserverCount()) {
/* 1534 */       saveCurrentCodeOffset();
/*      */     }
/* 1536 */     if (this.hasVarsInRegs) {
/*      */       
/* 1538 */       int parmCount = this.scriptOrFn.getParamCount();
/* 1539 */       if (parmCount > 0 && !this.inDirectCallFunction) {
/*      */ 
/*      */         
/* 1542 */         this.cfw.addALoad(this.argsLocal);
/* 1543 */         this.cfw.add(190);
/* 1544 */         this.cfw.addPush(parmCount);
/* 1545 */         int label = this.cfw.acquireLabel();
/* 1546 */         this.cfw.add(162, label);
/* 1547 */         this.cfw.addALoad(this.argsLocal);
/* 1548 */         this.cfw.addPush(parmCount);
/* 1549 */         addScriptRuntimeInvoke("padArguments", "([Ljava/lang/Object;I)[Ljava/lang/Object;");
/*      */ 
/*      */         
/* 1552 */         this.cfw.addAStore(this.argsLocal);
/* 1553 */         this.cfw.markLabel(label);
/*      */       } 
/*      */       
/* 1556 */       int paramCount = this.fnCurrent.fnode.getParamCount();
/* 1557 */       int varCount = this.fnCurrent.fnode.getParamAndVarCount();
/* 1558 */       boolean[] constDeclarations = this.fnCurrent.fnode.getParamAndVarConst();
/*      */ 
/*      */ 
/*      */       
/* 1562 */       short firstUndefVar = -1;
/* 1563 */       for (int i = 0; i != varCount; i++) {
/* 1564 */         short reg = -1;
/* 1565 */         if (i < paramCount) {
/* 1566 */           if (!this.inDirectCallFunction) {
/* 1567 */             reg = getNewWordLocal();
/* 1568 */             this.cfw.addALoad(this.argsLocal);
/* 1569 */             this.cfw.addPush(i);
/* 1570 */             this.cfw.add(50);
/* 1571 */             this.cfw.addAStore(reg);
/*      */           } 
/* 1573 */         } else if (this.fnCurrent.isNumberVar(i)) {
/* 1574 */           reg = getNewWordPairLocal(constDeclarations[i]);
/* 1575 */           this.cfw.addPush(0.0D);
/* 1576 */           this.cfw.addDStore(reg);
/*      */         } else {
/* 1578 */           reg = getNewWordLocal(constDeclarations[i]);
/* 1579 */           if (firstUndefVar == -1) {
/* 1580 */             Codegen.pushUndefined(this.cfw);
/* 1581 */             firstUndefVar = reg;
/*      */           } else {
/* 1583 */             this.cfw.addALoad(firstUndefVar);
/*      */           } 
/* 1585 */           this.cfw.addAStore(reg);
/*      */         } 
/* 1587 */         if (reg >= 0) {
/* 1588 */           if (constDeclarations[i]) {
/* 1589 */             this.cfw.addPush(0);
/* 1590 */             this.cfw.addIStore(reg + (this.fnCurrent.isNumberVar(i) ? 2 : 1));
/*      */           } 
/* 1592 */           this.varRegisters[i] = reg;
/*      */         } 
/*      */ 
/*      */         
/* 1596 */         if (this.compilerEnv.isGenerateDebugInfo()) {
/* 1597 */           String name = this.fnCurrent.fnode.getParamOrVarName(i);
/* 1598 */           String type = this.fnCurrent.isNumberVar(i) ? "D" : "Ljava/lang/Object;";
/*      */           
/* 1600 */           int startPC = this.cfw.getCurrentCodeOffset();
/* 1601 */           if (reg < 0) {
/* 1602 */             reg = this.varRegisters[i];
/*      */           }
/* 1604 */           this.cfw.addVariableDescriptor(name, type, startPC, reg);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1615 */     if (this.isGenerator) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1620 */     if (this.fnCurrent != null) {
/* 1621 */       debugVariableName = "activation";
/* 1622 */       this.cfw.addALoad(this.funObjLocal);
/* 1623 */       this.cfw.addALoad(this.variableObjectLocal);
/* 1624 */       this.cfw.addALoad(this.argsLocal);
/* 1625 */       addScriptRuntimeInvoke("createFunctionActivation", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1630 */       this.cfw.addAStore(this.variableObjectLocal);
/* 1631 */       this.cfw.addALoad(this.contextLocal);
/* 1632 */       this.cfw.addALoad(this.variableObjectLocal);
/* 1633 */       addScriptRuntimeInvoke("enterActivationFunction", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V");
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1638 */       debugVariableName = "global";
/* 1639 */       this.cfw.addALoad(this.funObjLocal);
/* 1640 */       this.cfw.addALoad(this.thisObjLocal);
/* 1641 */       this.cfw.addALoad(this.contextLocal);
/* 1642 */       this.cfw.addALoad(this.variableObjectLocal);
/* 1643 */       this.cfw.addPush(0);
/* 1644 */       addScriptRuntimeInvoke("initScript", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Z)V");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1653 */     this.enterAreaStartLabel = this.cfw.acquireLabel();
/* 1654 */     this.epilogueLabel = this.cfw.acquireLabel();
/* 1655 */     this.cfw.markLabel(this.enterAreaStartLabel);
/*      */     
/* 1657 */     generateNestedFunctionInits();
/*      */ 
/*      */     
/* 1660 */     if (this.compilerEnv.isGenerateDebugInfo()) {
/* 1661 */       this.cfw.addVariableDescriptor(debugVariableName, "Lorg/mozilla/javascript/Scriptable;", this.cfw.getCurrentCodeOffset(), this.variableObjectLocal);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1666 */     if (this.fnCurrent == null) {
/*      */       
/* 1668 */       this.popvLocal = getNewWordLocal();
/* 1669 */       Codegen.pushUndefined(this.cfw);
/* 1670 */       this.cfw.addAStore(this.popvLocal);
/*      */       
/* 1672 */       int linenum = this.scriptOrFn.getEndLineno();
/* 1673 */       if (linenum != -1) {
/* 1674 */         this.cfw.addLineNumberEntry((short)linenum);
/*      */       }
/*      */     } else {
/* 1677 */       if (this.fnCurrent.itsContainsCalls0) {
/* 1678 */         this.itsZeroArgArray = getNewWordLocal();
/* 1679 */         this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
/*      */ 
/*      */         
/* 1682 */         this.cfw.addAStore(this.itsZeroArgArray);
/*      */       } 
/* 1684 */       if (this.fnCurrent.itsContainsCalls1) {
/* 1685 */         this.itsOneArgArray = getNewWordLocal();
/* 1686 */         this.cfw.addPush(1);
/* 1687 */         this.cfw.add(189, "java/lang/Object");
/* 1688 */         this.cfw.addAStore(this.itsOneArgArray);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateGetGeneratorResumptionPoint() {
/* 1695 */     this.cfw.addALoad(this.generatorStateLocal);
/* 1696 */     this.cfw.add(180, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateSetGeneratorResumptionPoint(int nextState) {
/* 1704 */     this.cfw.addALoad(this.generatorStateLocal);
/* 1705 */     this.cfw.addLoadConstant(nextState);
/* 1706 */     this.cfw.add(181, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateGetGeneratorStackState() {
/* 1714 */     this.cfw.addALoad(this.generatorStateLocal);
/* 1715 */     addOptRuntimeInvoke("getGeneratorStackState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateEpilogue() {
/* 1721 */     if (this.compilerEnv.isGenerateObserverCount())
/* 1722 */       addInstructionCount(); 
/* 1723 */     if (this.isGenerator) {
/*      */       
/* 1725 */       Map<Node, int[]> liveLocals = ((FunctionNode)this.scriptOrFn).getLiveLocals();
/* 1726 */       if (liveLocals != null) {
/* 1727 */         List<Node> nodes = ((FunctionNode)this.scriptOrFn).getResumptionPoints();
/* 1728 */         for (int i = 0; i < nodes.size(); i++) {
/* 1729 */           Node node = nodes.get(i);
/* 1730 */           int[] live = liveLocals.get(node);
/* 1731 */           if (live != null) {
/* 1732 */             this.cfw.markTableSwitchCase(this.generatorSwitch, getNextGeneratorState(node));
/*      */             
/* 1734 */             generateGetGeneratorLocalsState();
/* 1735 */             for (int j = 0; j < live.length; j++) {
/* 1736 */               this.cfw.add(89);
/* 1737 */               this.cfw.addLoadConstant(j);
/* 1738 */               this.cfw.add(50);
/* 1739 */               this.cfw.addAStore(live[j]);
/*      */             } 
/* 1741 */             this.cfw.add(87);
/* 1742 */             this.cfw.add(167, getTargetLabel(node));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1748 */       if (this.finallys != null) {
/* 1749 */         for (Node n : this.finallys.keySet()) {
/* 1750 */           if (n.getType() == 125) {
/* 1751 */             FinallyReturnPoint ret = this.finallys.get(n);
/*      */             
/* 1753 */             this.cfw.markLabel(ret.tableLabel, (short)1);
/*      */ 
/*      */             
/* 1756 */             int startSwitch = this.cfw.addTableSwitch(0, ret.jsrPoints.size() - 1);
/*      */             
/* 1758 */             int c = 0;
/* 1759 */             this.cfw.markTableSwitchDefault(startSwitch);
/* 1760 */             for (int i = 0; i < ret.jsrPoints.size(); i++) {
/*      */               
/* 1762 */               this.cfw.markTableSwitchCase(startSwitch, c);
/* 1763 */               this.cfw.add(167, ((Integer)ret.jsrPoints.get(i)).intValue());
/*      */               
/* 1765 */               c++;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1772 */     if (this.epilogueLabel != -1) {
/* 1773 */       this.cfw.markLabel(this.epilogueLabel);
/*      */     }
/*      */     
/* 1776 */     if (this.hasVarsInRegs) {
/* 1777 */       this.cfw.add(176); return;
/*      */     } 
/* 1779 */     if (this.isGenerator) {
/* 1780 */       if (((FunctionNode)this.scriptOrFn).getResumptionPoints() != null) {
/* 1781 */         this.cfw.markTableSwitchDefault(this.generatorSwitch);
/*      */       }
/*      */ 
/*      */       
/* 1785 */       generateSetGeneratorResumptionPoint(-1);
/*      */ 
/*      */       
/* 1788 */       this.cfw.addALoad(this.variableObjectLocal);
/* 1789 */       addOptRuntimeInvoke("throwStopIteration", "(Ljava/lang/Object;)V");
/*      */ 
/*      */       
/* 1792 */       Codegen.pushUndefined(this.cfw);
/* 1793 */       this.cfw.add(176);
/*      */     }
/* 1795 */     else if (this.fnCurrent == null) {
/* 1796 */       this.cfw.addALoad(this.popvLocal);
/* 1797 */       this.cfw.add(176);
/*      */     } else {
/* 1799 */       generateActivationExit();
/* 1800 */       this.cfw.add(176);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1805 */       int finallyHandler = this.cfw.acquireLabel();
/* 1806 */       this.cfw.markHandler(finallyHandler);
/* 1807 */       short exceptionObject = getNewWordLocal();
/* 1808 */       this.cfw.addAStore(exceptionObject);
/*      */ 
/*      */ 
/*      */       
/* 1812 */       generateActivationExit();
/*      */       
/* 1814 */       this.cfw.addALoad(exceptionObject);
/* 1815 */       releaseWordLocal(exceptionObject);
/*      */       
/* 1817 */       this.cfw.add(191);
/*      */ 
/*      */       
/* 1820 */       this.cfw.addExceptionHandler(this.enterAreaStartLabel, this.epilogueLabel, finallyHandler, null);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateGetGeneratorLocalsState() {
/* 1826 */     this.cfw.addALoad(this.generatorStateLocal);
/* 1827 */     addOptRuntimeInvoke("getGeneratorLocalsState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateActivationExit() {
/* 1833 */     if (this.fnCurrent == null || this.hasVarsInRegs) throw Kit.codeBug(); 
/* 1834 */     this.cfw.addALoad(this.contextLocal);
/* 1835 */     addScriptRuntimeInvoke("exitActivationFunction", "(Lorg/mozilla/javascript/Context;)V"); } private void generateStatement(Node node) {
/*      */     boolean prevLocal;
/*      */     int fnIndex, local, enumType, i;
/*      */     OptFunctionNode ofn;
/*      */     int scopeIndex, label, t;
/*      */     String name;
/* 1841 */     updateLineNumber(node);
/* 1842 */     int type = node.getType();
/* 1843 */     Node child = node.getFirstChild();
/* 1844 */     switch (type) {
/*      */       
/*      */       case 123:
/*      */       case 128:
/*      */       case 129:
/*      */       case 130:
/*      */       case 132:
/*      */       case 136:
/* 1852 */         if (this.compilerEnv.isGenerateObserverCount())
/*      */         {
/*      */           
/* 1855 */           addInstructionCount(1);
/*      */         }
/* 1857 */         while (child != null) {
/* 1858 */           generateStatement(child);
/* 1859 */           child = child.getNext();
/*      */         } 
/*      */ 
/*      */       
/*      */       case 141:
/* 1864 */         prevLocal = this.inLocalBlock;
/* 1865 */         this.inLocalBlock = true;
/* 1866 */         i = getNewWordLocal();
/* 1867 */         if (this.isGenerator) {
/* 1868 */           this.cfw.add(1);
/* 1869 */           this.cfw.addAStore(i);
/*      */         } 
/* 1871 */         node.putIntProp(2, i);
/* 1872 */         while (child != null) {
/* 1873 */           generateStatement(child);
/* 1874 */           child = child.getNext();
/*      */         } 
/* 1876 */         releaseWordLocal((short)i);
/* 1877 */         node.removeProp(2);
/* 1878 */         this.inLocalBlock = prevLocal;
/*      */ 
/*      */ 
/*      */       
/*      */       case 109:
/* 1883 */         fnIndex = node.getExistingIntProp(1);
/* 1884 */         ofn = OptFunctionNode.get(this.scriptOrFn, fnIndex);
/* 1885 */         t = ofn.fnode.getFunctionType();
/* 1886 */         if (t == 3) {
/* 1887 */           visitFunction(ofn, t);
/*      */         }
/* 1889 */         else if (t != 1) {
/* 1890 */           throw Codegen.badTree();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 81:
/* 1897 */         visitTryCatchFinally((Jump)node, child);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 57:
/* 1903 */         this.cfw.setStackTop((short)0);
/*      */         
/* 1905 */         local = getLocalBlockRegister(node);
/* 1906 */         scopeIndex = node.getExistingIntProp(14);
/*      */ 
/*      */         
/* 1909 */         name = child.getString();
/* 1910 */         child = child.getNext();
/* 1911 */         generateExpression(child, node);
/* 1912 */         if (scopeIndex == 0) {
/* 1913 */           this.cfw.add(1);
/*      */         } else {
/*      */           
/* 1916 */           this.cfw.addALoad(local);
/*      */         } 
/* 1918 */         this.cfw.addPush(name);
/* 1919 */         this.cfw.addALoad(this.contextLocal);
/* 1920 */         this.cfw.addALoad(this.variableObjectLocal);
/*      */         
/* 1922 */         addScriptRuntimeInvoke("newCatchScope", "(Ljava/lang/Throwable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1930 */         this.cfw.addAStore(local);
/*      */ 
/*      */ 
/*      */       
/*      */       case 50:
/* 1935 */         generateExpression(child, node);
/* 1936 */         if (this.compilerEnv.isGenerateObserverCount())
/* 1937 */           addInstructionCount(); 
/* 1938 */         generateThrowJavaScriptException();
/*      */ 
/*      */       
/*      */       case 51:
/* 1942 */         if (this.compilerEnv.isGenerateObserverCount())
/* 1943 */           addInstructionCount(); 
/* 1944 */         this.cfw.addALoad(getLocalBlockRegister(node));
/* 1945 */         this.cfw.add(191);
/*      */ 
/*      */       
/*      */       case 4:
/*      */       case 64:
/* 1950 */         if (!this.isGenerator) {
/* 1951 */           if (child != null) {
/* 1952 */             generateExpression(child, node);
/* 1953 */           } else if (type == 4) {
/* 1954 */             Codegen.pushUndefined(this.cfw);
/*      */           } else {
/* 1956 */             if (this.popvLocal < 0) throw Codegen.badTree(); 
/* 1957 */             this.cfw.addALoad(this.popvLocal);
/*      */           } 
/*      */         }
/* 1960 */         if (this.compilerEnv.isGenerateObserverCount())
/* 1961 */           addInstructionCount(); 
/* 1962 */         if (this.epilogueLabel == -1) {
/* 1963 */           if (!this.hasVarsInRegs) throw Codegen.badTree(); 
/* 1964 */           this.epilogueLabel = this.cfw.acquireLabel();
/*      */         } 
/* 1966 */         this.cfw.add(167, this.epilogueLabel);
/*      */ 
/*      */       
/*      */       case 114:
/* 1970 */         if (this.compilerEnv.isGenerateObserverCount())
/* 1971 */           addInstructionCount(); 
/* 1972 */         visitSwitch((Jump)node, child);
/*      */ 
/*      */       
/*      */       case 2:
/* 1976 */         generateExpression(child, node);
/* 1977 */         this.cfw.addALoad(this.contextLocal);
/* 1978 */         this.cfw.addALoad(this.variableObjectLocal);
/* 1979 */         addScriptRuntimeInvoke("enterWith", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1985 */         this.cfw.addAStore(this.variableObjectLocal);
/* 1986 */         incReferenceWordLocal(this.variableObjectLocal);
/*      */ 
/*      */       
/*      */       case 3:
/* 1990 */         this.cfw.addALoad(this.variableObjectLocal);
/* 1991 */         addScriptRuntimeInvoke("leaveWith", "(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */         
/* 1995 */         this.cfw.addAStore(this.variableObjectLocal);
/* 1996 */         decReferenceWordLocal(this.variableObjectLocal);
/*      */ 
/*      */       
/*      */       case 58:
/*      */       case 59:
/*      */       case 60:
/* 2002 */         generateExpression(child, node);
/* 2003 */         this.cfw.addALoad(this.contextLocal);
/* 2004 */         this.cfw.addALoad(this.variableObjectLocal);
/* 2005 */         enumType = (type == 58) ? 0 : ((type == 59) ? 1 : 2);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2010 */         this.cfw.addPush(enumType);
/* 2011 */         addScriptRuntimeInvoke("enumInit", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2017 */         this.cfw.addAStore(getLocalBlockRegister(node));
/*      */ 
/*      */       
/*      */       case 133:
/* 2021 */         if (child.getType() == 56) {
/*      */ 
/*      */           
/* 2024 */           visitSetVar(child, child.getFirstChild(), false);
/*      */         }
/* 2026 */         else if (child.getType() == 156) {
/*      */ 
/*      */           
/* 2029 */           visitSetConstVar(child, child.getFirstChild(), false);
/*      */         }
/* 2031 */         else if (child.getType() == 72) {
/* 2032 */           generateYieldPoint(child, false);
/*      */         } else {
/*      */           
/* 2035 */           generateExpression(child, node);
/* 2036 */           if (node.getIntProp(8, -1) != -1) {
/* 2037 */             this.cfw.add(88);
/*      */           } else {
/* 2039 */             this.cfw.add(87);
/*      */           } 
/*      */         } 
/*      */       
/*      */       case 134:
/* 2044 */         generateExpression(child, node);
/* 2045 */         if (this.popvLocal < 0) {
/* 2046 */           this.popvLocal = getNewWordLocal();
/*      */         }
/* 2048 */         this.cfw.addAStore(this.popvLocal);
/*      */ 
/*      */ 
/*      */       
/*      */       case 131:
/* 2053 */         if (this.compilerEnv.isGenerateObserverCount())
/* 2054 */           addInstructionCount(); 
/* 2055 */         label = getTargetLabel(node);
/* 2056 */         this.cfw.markLabel(label);
/* 2057 */         if (this.compilerEnv.isGenerateObserverCount()) {
/* 2058 */           saveCurrentCodeOffset();
/*      */         }
/*      */ 
/*      */       
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 135:
/* 2066 */         if (this.compilerEnv.isGenerateObserverCount())
/* 2067 */           addInstructionCount(); 
/* 2068 */         visitGoto((Jump)node, type, child);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 125:
/* 2078 */         if (this.isGenerator) {
/*      */ 
/*      */ 
/*      */           
/* 2082 */           if (this.compilerEnv.isGenerateObserverCount()) {
/* 2083 */             saveCurrentCodeOffset();
/*      */           }
/*      */           
/* 2086 */           this.cfw.setStackTop((short)1);
/*      */ 
/*      */           
/* 2089 */           int finallyRegister = getNewWordLocal();
/*      */           
/* 2091 */           int finallyStart = this.cfw.acquireLabel();
/* 2092 */           int finallyEnd = this.cfw.acquireLabel();
/* 2093 */           this.cfw.markLabel(finallyStart);
/*      */           
/* 2095 */           generateIntegerWrap();
/* 2096 */           this.cfw.addAStore(finallyRegister);
/*      */           
/* 2098 */           while (child != null) {
/* 2099 */             generateStatement(child);
/* 2100 */             child = child.getNext();
/*      */           } 
/*      */           
/* 2103 */           this.cfw.addALoad(finallyRegister);
/* 2104 */           this.cfw.add(192, "java/lang/Integer");
/* 2105 */           generateIntegerUnwrap();
/* 2106 */           FinallyReturnPoint ret = this.finallys.get(node);
/* 2107 */           ret.tableLabel = this.cfw.acquireLabel();
/* 2108 */           this.cfw.add(167, ret.tableLabel);
/*      */           
/* 2110 */           releaseWordLocal((short)finallyRegister);
/* 2111 */           this.cfw.markLabel(finallyEnd);
/*      */         } 
/*      */ 
/*      */       
/*      */       case 160:
/*      */         return;
/*      */     } 
/*      */     
/* 2119 */     throw Codegen.badTree();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateIntegerWrap() {
/* 2126 */     this.cfw.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateIntegerUnwrap() {
/* 2133 */     this.cfw.addInvoke(182, "java/lang/Integer", "intValue", "()I");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateThrowJavaScriptException() {
/* 2140 */     this.cfw.add(187, "org/mozilla/javascript/JavaScriptException");
/*      */     
/* 2142 */     this.cfw.add(90);
/* 2143 */     this.cfw.add(95);
/* 2144 */     this.cfw.addPush(this.scriptOrFn.getSourceName());
/* 2145 */     this.cfw.addPush(this.itsLineNumber);
/* 2146 */     this.cfw.addInvoke(183, "org/mozilla/javascript/JavaScriptException", "<init>", "(Ljava/lang/Object;Ljava/lang/String;I)V");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2151 */     this.cfw.add(191);
/*      */   }
/*      */ 
/*      */   
/*      */   private int getNextGeneratorState(Node node) {
/* 2156 */     int nodeIndex = ((FunctionNode)this.scriptOrFn).getResumptionPoints().indexOf(node);
/*      */     
/* 2158 */     return nodeIndex + 1; } private void generateExpression(Node node, Node parent) { int specialType; double num; int i; Node next; int local, trueTarget, falseTarget; Node ifThen; int prop, trueGOTO; boolean isName; int j; Node ifElse; int falseGOTO; String special; int memberTypeFlags; Node enterWith, initStmt; int beyond, elseTarget; String methodName;
/*      */     Node with, expr;
/*      */     short stack;
/*      */     String signature;
/*      */     Node leaveWith;
/* 2163 */     int afterHook, type = node.getType();
/* 2164 */     Node child = node.getFirstChild();
/* 2165 */     switch (type) {
/*      */       case 138:
/*      */         return;
/*      */       
/*      */       case 109:
/* 2170 */         if (this.fnCurrent != null || parent.getType() != 136) {
/* 2171 */           int fnIndex = node.getExistingIntProp(1);
/* 2172 */           OptFunctionNode ofn = OptFunctionNode.get(this.scriptOrFn, fnIndex);
/*      */           
/* 2174 */           int t = ofn.fnode.getFunctionType();
/* 2175 */           if (t != 2) {
/* 2176 */             throw Codegen.badTree();
/*      */           }
/* 2178 */           visitFunction(ofn, t);
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*      */       case 39:
/* 2184 */         this.cfw.addALoad(this.contextLocal);
/* 2185 */         this.cfw.addALoad(this.variableObjectLocal);
/* 2186 */         this.cfw.addPush(node.getString());
/* 2187 */         addScriptRuntimeInvoke("name", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 30:
/*      */       case 38:
/* 2199 */         specialType = node.getIntProp(10, 0);
/*      */         
/* 2201 */         if (specialType == 0) {
/*      */           
/* 2203 */           OptFunctionNode target = (OptFunctionNode)node.getProp(9);
/*      */ 
/*      */           
/* 2206 */           if (target != null) {
/* 2207 */             visitOptimizedCall(node, target, type, child);
/* 2208 */           } else if (type == 38) {
/* 2209 */             visitStandardCall(node, child);
/*      */           } else {
/* 2211 */             visitStandardNew(node, child);
/*      */           } 
/*      */         } else {
/* 2214 */           visitSpecialCall(node, type, specialType, child);
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*      */       case 70:
/* 2220 */         generateFunctionAndThisObj(child, node);
/*      */         
/* 2222 */         child = child.getNext();
/* 2223 */         generateCallArgArray(node, child, false);
/* 2224 */         this.cfw.addALoad(this.contextLocal);
/* 2225 */         addScriptRuntimeInvoke("callRef", "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Ref;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 40:
/* 2236 */         num = node.getDouble();
/* 2237 */         if (node.getIntProp(8, -1) != -1) {
/* 2238 */           this.cfw.addPush(num);
/*      */         } else {
/* 2240 */           this.codegen.pushNumberAsObject(this.cfw, num);
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*      */       case 41:
/* 2246 */         this.cfw.addPush(node.getString());
/*      */ 
/*      */       
/*      */       case 43:
/* 2250 */         this.cfw.addALoad(this.thisObjLocal);
/*      */ 
/*      */       
/*      */       case 63:
/* 2254 */         this.cfw.add(42);
/*      */ 
/*      */       
/*      */       case 42:
/* 2258 */         this.cfw.add(1);
/*      */ 
/*      */       
/*      */       case 45:
/* 2262 */         this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
/*      */ 
/*      */ 
/*      */       
/*      */       case 44:
/* 2267 */         this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 48:
/* 2274 */         this.cfw.addALoad(this.contextLocal);
/* 2275 */         this.cfw.addALoad(this.variableObjectLocal);
/* 2276 */         i = node.getExistingIntProp(4);
/* 2277 */         this.cfw.add(178, this.codegen.mainClassName, this.codegen.getCompiledRegexpName(this.scriptOrFn, i), "Ljava/lang/Object;");
/*      */ 
/*      */         
/* 2280 */         this.cfw.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "wrapRegExp", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 89:
/* 2291 */         next = child.getNext();
/* 2292 */         while (next != null) {
/* 2293 */           generateExpression(child, node);
/* 2294 */           this.cfw.add(87);
/* 2295 */           child = next;
/* 2296 */           next = next.getNext();
/*      */         } 
/* 2298 */         generateExpression(child, node);
/*      */ 
/*      */ 
/*      */       
/*      */       case 61:
/*      */       case 62:
/* 2304 */         local = getLocalBlockRegister(node);
/* 2305 */         this.cfw.addALoad(local);
/* 2306 */         if (type == 61) {
/* 2307 */           addScriptRuntimeInvoke("enumNext", "(Ljava/lang/Object;)Ljava/lang/Boolean;");
/*      */         } else {
/*      */           
/* 2310 */           this.cfw.addALoad(this.contextLocal);
/* 2311 */           addScriptRuntimeInvoke("enumId", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 65:
/* 2320 */         visitArrayLiteral(node, child, false);
/*      */ 
/*      */       
/*      */       case 66:
/* 2324 */         visitObjectLiteral(node, child, false);
/*      */ 
/*      */       
/*      */       case 26:
/* 2328 */         trueTarget = this.cfw.acquireLabel();
/* 2329 */         j = this.cfw.acquireLabel();
/* 2330 */         beyond = this.cfw.acquireLabel();
/* 2331 */         generateIfJump(child, node, trueTarget, j);
/*      */         
/* 2333 */         this.cfw.markLabel(trueTarget);
/* 2334 */         this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
/*      */         
/* 2336 */         this.cfw.add(167, beyond);
/* 2337 */         this.cfw.markLabel(j);
/* 2338 */         this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
/*      */         
/* 2340 */         this.cfw.markLabel(beyond);
/* 2341 */         this.cfw.adjustStackTop(-1);
/*      */ 
/*      */ 
/*      */       
/*      */       case 27:
/* 2346 */         generateExpression(child, node);
/* 2347 */         addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
/* 2348 */         this.cfw.addPush(-1);
/* 2349 */         this.cfw.add(130);
/* 2350 */         this.cfw.add(135);
/* 2351 */         addDoubleWrap();
/*      */ 
/*      */       
/*      */       case 126:
/* 2355 */         generateExpression(child, node);
/* 2356 */         this.cfw.add(87);
/* 2357 */         Codegen.pushUndefined(this.cfw);
/*      */ 
/*      */       
/*      */       case 32:
/* 2361 */         generateExpression(child, node);
/* 2362 */         addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 137:
/* 2368 */         visitTypeofname(node);
/*      */ 
/*      */       
/*      */       case 106:
/*      */       case 107:
/* 2373 */         visitIncDec(node);
/*      */ 
/*      */       
/*      */       case 104:
/*      */       case 105:
/* 2378 */         generateExpression(child, node);
/* 2379 */         this.cfw.add(89);
/* 2380 */         addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
/*      */         
/* 2382 */         falseTarget = this.cfw.acquireLabel();
/* 2383 */         if (type == 105) {
/* 2384 */           this.cfw.add(153, falseTarget);
/*      */         } else {
/* 2386 */           this.cfw.add(154, falseTarget);
/* 2387 */         }  this.cfw.add(87);
/* 2388 */         generateExpression(child.getNext(), node);
/* 2389 */         this.cfw.markLabel(falseTarget);
/*      */ 
/*      */ 
/*      */       
/*      */       case 102:
/* 2394 */         ifThen = child.getNext();
/* 2395 */         ifElse = ifThen.getNext();
/* 2396 */         generateExpression(child, node);
/* 2397 */         addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
/*      */         
/* 2399 */         elseTarget = this.cfw.acquireLabel();
/* 2400 */         this.cfw.add(153, elseTarget);
/* 2401 */         stack = this.cfw.getStackTop();
/* 2402 */         generateExpression(ifThen, node);
/* 2403 */         afterHook = this.cfw.acquireLabel();
/* 2404 */         this.cfw.add(167, afterHook);
/* 2405 */         this.cfw.markLabel(elseTarget, stack);
/* 2406 */         generateExpression(ifElse, node);
/* 2407 */         this.cfw.markLabel(afterHook);
/*      */ 
/*      */ 
/*      */       
/*      */       case 21:
/* 2412 */         generateExpression(child, node);
/* 2413 */         generateExpression(child.getNext(), node);
/* 2414 */         switch (node.getIntProp(8, -1)) {
/*      */           case 0:
/* 2416 */             this.cfw.add(99);
/*      */           
/*      */           case 1:
/* 2419 */             addOptRuntimeInvoke("add", "(DLjava/lang/Object;)Ljava/lang/Object;");
/*      */ 
/*      */           
/*      */           case 2:
/* 2423 */             addOptRuntimeInvoke("add", "(Ljava/lang/Object;D)Ljava/lang/Object;");
/*      */         } 
/*      */ 
/*      */         
/* 2427 */         if (child.getType() == 41) {
/* 2428 */           addScriptRuntimeInvoke("add", "(Ljava/lang/CharSequence;Ljava/lang/Object;)Ljava/lang/CharSequence;");
/*      */ 
/*      */         
/*      */         }
/* 2432 */         else if (child.getNext().getType() == 41) {
/* 2433 */           addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2438 */           this.cfw.addALoad(this.contextLocal);
/* 2439 */           addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 23:
/* 2450 */         visitArithmetic(node, 107, child, parent);
/*      */ 
/*      */       
/*      */       case 22:
/* 2454 */         visitArithmetic(node, 103, child, parent);
/*      */ 
/*      */       
/*      */       case 24:
/*      */       case 25:
/* 2459 */         visitArithmetic(node, (type == 24) ? 111 : 115, child, parent);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/* 2470 */         visitBitOp(node, type, child);
/*      */ 
/*      */       
/*      */       case 28:
/*      */       case 29:
/* 2475 */         generateExpression(child, node);
/* 2476 */         addObjectToDouble();
/* 2477 */         if (type == 29) {
/* 2478 */           this.cfw.add(119);
/*      */         }
/* 2480 */         addDoubleWrap();
/*      */ 
/*      */ 
/*      */       
/*      */       case 150:
/* 2485 */         generateExpression(child, node);
/* 2486 */         addObjectToDouble();
/*      */ 
/*      */ 
/*      */       
/*      */       case 149:
/* 2491 */         prop = -1;
/* 2492 */         if (child.getType() == 40) {
/* 2493 */           prop = child.getIntProp(8, -1);
/*      */         }
/* 2495 */         if (prop != -1) {
/* 2496 */           child.removeProp(8);
/* 2497 */           generateExpression(child, node);
/* 2498 */           child.putIntProp(8, prop);
/*      */         } else {
/* 2500 */           generateExpression(child, node);
/* 2501 */           addDoubleWrap();
/*      */         } 
/*      */ 
/*      */ 
/*      */       
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 52:
/*      */       case 53:
/* 2512 */         trueGOTO = this.cfw.acquireLabel();
/* 2513 */         falseGOTO = this.cfw.acquireLabel();
/* 2514 */         visitIfJumpRelOp(node, child, trueGOTO, falseGOTO);
/* 2515 */         addJumpedBooleanWrap(trueGOTO, falseGOTO);
/*      */ 
/*      */ 
/*      */       
/*      */       case 12:
/*      */       case 13:
/*      */       case 46:
/*      */       case 47:
/* 2523 */         trueGOTO = this.cfw.acquireLabel();
/* 2524 */         falseGOTO = this.cfw.acquireLabel();
/* 2525 */         visitIfJumpEqOp(node, child, trueGOTO, falseGOTO);
/* 2526 */         addJumpedBooleanWrap(trueGOTO, falseGOTO);
/*      */ 
/*      */ 
/*      */       
/*      */       case 33:
/*      */       case 34:
/* 2532 */         visitGetProp(node, child);
/*      */ 
/*      */       
/*      */       case 36:
/* 2536 */         generateExpression(child, node);
/* 2537 */         generateExpression(child.getNext(), node);
/* 2538 */         this.cfw.addALoad(this.contextLocal);
/* 2539 */         if (node.getIntProp(8, -1) != -1) {
/* 2540 */           addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 2547 */           this.cfw.addALoad(this.variableObjectLocal);
/* 2548 */           addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 67:
/* 2559 */         generateExpression(child, node);
/* 2560 */         this.cfw.addALoad(this.contextLocal);
/* 2561 */         addScriptRuntimeInvoke("refGet", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 55:
/* 2569 */         visitGetVar(node);
/*      */ 
/*      */       
/*      */       case 56:
/* 2573 */         visitSetVar(node, child, true);
/*      */ 
/*      */       
/*      */       case 8:
/* 2577 */         visitSetName(node, child);
/*      */ 
/*      */       
/*      */       case 73:
/* 2581 */         visitStrictSetName(node, child);
/*      */ 
/*      */       
/*      */       case 155:
/* 2585 */         visitSetConst(node, child);
/*      */ 
/*      */       
/*      */       case 156:
/* 2589 */         visitSetConstVar(node, child, true);
/*      */ 
/*      */       
/*      */       case 35:
/*      */       case 139:
/* 2594 */         visitSetProp(type, node, child);
/*      */ 
/*      */       
/*      */       case 37:
/*      */       case 140:
/* 2599 */         visitSetElem(type, node, child);
/*      */ 
/*      */ 
/*      */       
/*      */       case 68:
/*      */       case 142:
/* 2605 */         generateExpression(child, node);
/* 2606 */         child = child.getNext();
/* 2607 */         if (type == 142) {
/* 2608 */           this.cfw.add(89);
/* 2609 */           this.cfw.addALoad(this.contextLocal);
/* 2610 */           addScriptRuntimeInvoke("refGet", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2616 */         generateExpression(child, node);
/* 2617 */         this.cfw.addALoad(this.contextLocal);
/* 2618 */         this.cfw.addALoad(this.variableObjectLocal);
/* 2619 */         addScriptRuntimeInvoke("refSet", "(Lorg/mozilla/javascript/Ref;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 69:
/* 2630 */         generateExpression(child, node);
/* 2631 */         this.cfw.addALoad(this.contextLocal);
/* 2632 */         addScriptRuntimeInvoke("refDel", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 31:
/* 2639 */         isName = (child.getType() == 49);
/* 2640 */         generateExpression(child, node);
/* 2641 */         child = child.getNext();
/* 2642 */         generateExpression(child, node);
/* 2643 */         this.cfw.addALoad(this.contextLocal);
/* 2644 */         this.cfw.addPush(isName);
/* 2645 */         addScriptRuntimeInvoke("delete", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Z)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 49:
/* 2654 */         while (child != null) {
/* 2655 */           generateExpression(child, node);
/* 2656 */           child = child.getNext();
/*      */         } 
/*      */         
/* 2659 */         this.cfw.addALoad(this.contextLocal);
/* 2660 */         this.cfw.addALoad(this.variableObjectLocal);
/* 2661 */         this.cfw.addPush(node.getString());
/* 2662 */         addScriptRuntimeInvoke("bind", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 54:
/* 2672 */         this.cfw.addALoad(getLocalBlockRegister(node));
/*      */ 
/*      */ 
/*      */       
/*      */       case 71:
/* 2677 */         special = (String)node.getProp(17);
/* 2678 */         generateExpression(child, node);
/* 2679 */         this.cfw.addPush(special);
/* 2680 */         this.cfw.addALoad(this.contextLocal);
/* 2681 */         this.cfw.addALoad(this.variableObjectLocal);
/* 2682 */         addScriptRuntimeInvoke("specialRef", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Ref;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/* 2697 */         memberTypeFlags = node.getIntProp(16, 0);
/*      */ 
/*      */         
/*      */         do {
/* 2701 */           generateExpression(child, node);
/* 2702 */           child = child.getNext();
/* 2703 */         } while (child != null);
/* 2704 */         this.cfw.addALoad(this.contextLocal);
/*      */         
/* 2706 */         switch (type) {
/*      */           case 77:
/* 2708 */             methodName = "memberRef";
/* 2709 */             signature = "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;I)Lorg/mozilla/javascript/Ref;";
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 78:
/* 2716 */             methodName = "memberRef";
/* 2717 */             signature = "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;I)Lorg/mozilla/javascript/Ref;";
/*      */             break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 79:
/* 2725 */             methodName = "nameRef";
/* 2726 */             signature = "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Lorg/mozilla/javascript/Ref;";
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2731 */             this.cfw.addALoad(this.variableObjectLocal);
/*      */             break;
/*      */           case 80:
/* 2734 */             methodName = "nameRef";
/* 2735 */             signature = "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Lorg/mozilla/javascript/Ref;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2741 */             this.cfw.addALoad(this.variableObjectLocal);
/*      */             break;
/*      */           default:
/* 2744 */             throw Kit.codeBug();
/*      */         } 
/* 2746 */         this.cfw.addPush(memberTypeFlags);
/* 2747 */         addScriptRuntimeInvoke(methodName, signature);
/*      */ 
/*      */ 
/*      */       
/*      */       case 146:
/* 2752 */         visitDotQuery(node, child);
/*      */ 
/*      */       
/*      */       case 75:
/* 2756 */         generateExpression(child, node);
/* 2757 */         this.cfw.addALoad(this.contextLocal);
/* 2758 */         addScriptRuntimeInvoke("escapeAttributeValue", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 76:
/* 2765 */         generateExpression(child, node);
/* 2766 */         this.cfw.addALoad(this.contextLocal);
/* 2767 */         addScriptRuntimeInvoke("escapeTextValue", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 74:
/* 2774 */         generateExpression(child, node);
/* 2775 */         this.cfw.addALoad(this.contextLocal);
/* 2776 */         addScriptRuntimeInvoke("setDefaultNamespace", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 72:
/* 2783 */         generateYieldPoint(node, true);
/*      */ 
/*      */       
/*      */       case 159:
/* 2787 */         enterWith = child;
/* 2788 */         with = enterWith.getNext();
/* 2789 */         leaveWith = with.getNext();
/* 2790 */         generateStatement(enterWith);
/* 2791 */         generateExpression(with.getFirstChild(), with);
/* 2792 */         generateStatement(leaveWith);
/*      */ 
/*      */ 
/*      */       
/*      */       case 157:
/* 2797 */         initStmt = child;
/* 2798 */         expr = child.getNext();
/* 2799 */         generateStatement(initStmt);
/* 2800 */         generateExpression(expr, node);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2805 */     throw new RuntimeException("Unexpected node type " + type); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateYieldPoint(Node node, boolean exprContext) {
/* 2812 */     int top = this.cfw.getStackTop();
/* 2813 */     this.maxStack = (this.maxStack > top) ? this.maxStack : top;
/* 2814 */     if (this.cfw.getStackTop() != 0) {
/* 2815 */       generateGetGeneratorStackState();
/* 2816 */       for (int i = 0; i < top; i++) {
/* 2817 */         this.cfw.add(90);
/* 2818 */         this.cfw.add(95);
/* 2819 */         this.cfw.addLoadConstant(i);
/* 2820 */         this.cfw.add(95);
/* 2821 */         this.cfw.add(83);
/*      */       } 
/*      */       
/* 2824 */       this.cfw.add(87);
/*      */     } 
/*      */ 
/*      */     
/* 2828 */     Node child = node.getFirstChild();
/* 2829 */     if (child != null) {
/* 2830 */       generateExpression(child, node);
/*      */     } else {
/* 2832 */       Codegen.pushUndefined(this.cfw);
/*      */     } 
/*      */     
/* 2835 */     int nextState = getNextGeneratorState(node);
/* 2836 */     generateSetGeneratorResumptionPoint(nextState);
/*      */     
/* 2838 */     boolean hasLocals = generateSaveLocals(node);
/*      */     
/* 2840 */     this.cfw.add(176);
/*      */     
/* 2842 */     generateCheckForThrowOrClose(getTargetLabel(node), hasLocals, nextState);
/*      */ 
/*      */ 
/*      */     
/* 2846 */     if (top != 0) {
/* 2847 */       generateGetGeneratorStackState();
/* 2848 */       for (int i = 0; i < top; i++) {
/* 2849 */         this.cfw.add(89);
/* 2850 */         this.cfw.addLoadConstant(top - i - 1);
/* 2851 */         this.cfw.add(50);
/* 2852 */         this.cfw.add(95);
/*      */       } 
/* 2854 */       this.cfw.add(87);
/*      */     } 
/*      */ 
/*      */     
/* 2858 */     if (exprContext) {
/* 2859 */       this.cfw.addALoad(this.argsLocal);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateCheckForThrowOrClose(int label, boolean hasLocals, int nextState) {
/* 2866 */     int throwLabel = this.cfw.acquireLabel();
/* 2867 */     int closeLabel = this.cfw.acquireLabel();
/*      */ 
/*      */     
/* 2870 */     this.cfw.markLabel(throwLabel);
/* 2871 */     this.cfw.addALoad(this.argsLocal);
/* 2872 */     generateThrowJavaScriptException();
/*      */ 
/*      */     
/* 2875 */     this.cfw.markLabel(closeLabel);
/* 2876 */     this.cfw.addALoad(this.argsLocal);
/* 2877 */     this.cfw.add(192, "java/lang/Throwable");
/* 2878 */     this.cfw.add(191);
/*      */ 
/*      */ 
/*      */     
/* 2882 */     if (label != -1)
/* 2883 */       this.cfw.markLabel(label); 
/* 2884 */     if (!hasLocals)
/*      */     {
/* 2886 */       this.cfw.markTableSwitchCase(this.generatorSwitch, nextState);
/*      */     }
/*      */ 
/*      */     
/* 2890 */     this.cfw.addILoad(this.operationLocal);
/* 2891 */     this.cfw.addLoadConstant(2);
/* 2892 */     this.cfw.add(159, closeLabel);
/* 2893 */     this.cfw.addILoad(this.operationLocal);
/* 2894 */     this.cfw.addLoadConstant(1);
/* 2895 */     this.cfw.add(159, throwLabel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateIfJump(Node node, Node parent, int trueLabel, int falseLabel) {
/* 2903 */     int interLabel, type = node.getType();
/* 2904 */     Node child = node.getFirstChild();
/*      */     
/* 2906 */     switch (type) {
/*      */       case 26:
/* 2908 */         generateIfJump(child, node, falseLabel, trueLabel);
/*      */         return;
/*      */       
/*      */       case 104:
/*      */       case 105:
/* 2913 */         interLabel = this.cfw.acquireLabel();
/* 2914 */         if (type == 105) {
/* 2915 */           generateIfJump(child, node, interLabel, falseLabel);
/*      */         } else {
/*      */           
/* 2918 */           generateIfJump(child, node, trueLabel, interLabel);
/*      */         } 
/* 2920 */         this.cfw.markLabel(interLabel);
/* 2921 */         child = child.getNext();
/* 2922 */         generateIfJump(child, node, trueLabel, falseLabel);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 52:
/*      */       case 53:
/* 2932 */         visitIfJumpRelOp(node, child, trueLabel, falseLabel);
/*      */         return;
/*      */       
/*      */       case 12:
/*      */       case 13:
/*      */       case 46:
/*      */       case 47:
/* 2939 */         visitIfJumpEqOp(node, child, trueLabel, falseLabel);
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 2944 */     generateExpression(node, parent);
/* 2945 */     addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
/* 2946 */     this.cfw.add(154, trueLabel);
/* 2947 */     this.cfw.add(167, falseLabel);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitFunction(OptFunctionNode ofn, int functionType) {
/* 2953 */     int fnIndex = this.codegen.getIndex((ScriptNode)ofn.fnode);
/* 2954 */     this.cfw.add(187, this.codegen.mainClassName);
/*      */     
/* 2956 */     this.cfw.add(89);
/* 2957 */     this.cfw.addALoad(this.variableObjectLocal);
/* 2958 */     this.cfw.addALoad(this.contextLocal);
/* 2959 */     this.cfw.addPush(fnIndex);
/* 2960 */     this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V");
/*      */ 
/*      */     
/* 2963 */     if (functionType == 2) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 2968 */     this.cfw.addPush(functionType);
/* 2969 */     this.cfw.addALoad(this.variableObjectLocal);
/* 2970 */     this.cfw.addALoad(this.contextLocal);
/* 2971 */     addOptRuntimeInvoke("initFunction", "(Lorg/mozilla/javascript/NativeFunction;ILorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;)V");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getTargetLabel(Node target) {
/* 2981 */     int labelId = target.labelId();
/* 2982 */     if (labelId == -1) {
/* 2983 */       labelId = this.cfw.acquireLabel();
/* 2984 */       target.labelId(labelId);
/*      */     } 
/* 2986 */     return labelId;
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitGoto(Jump node, int type, Node child) {
/* 2991 */     Node target = node.target;
/* 2992 */     if (type == 6 || type == 7) {
/* 2993 */       if (child == null) throw Codegen.badTree(); 
/* 2994 */       int targetLabel = getTargetLabel(target);
/* 2995 */       int fallThruLabel = this.cfw.acquireLabel();
/* 2996 */       if (type == 6) {
/* 2997 */         generateIfJump(child, (Node)node, targetLabel, fallThruLabel);
/*      */       } else {
/* 2999 */         generateIfJump(child, (Node)node, fallThruLabel, targetLabel);
/* 3000 */       }  this.cfw.markLabel(fallThruLabel);
/*      */     }
/* 3002 */     else if (type == 135) {
/* 3003 */       if (this.isGenerator) {
/* 3004 */         addGotoWithReturn(target);
/*      */       } else {
/*      */         
/* 3007 */         inlineFinally(target);
/*      */       } 
/*      */     } else {
/* 3010 */       addGoto(target, 167);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addGotoWithReturn(Node target) {
/* 3016 */     FinallyReturnPoint ret = this.finallys.get(target);
/* 3017 */     this.cfw.addLoadConstant(ret.jsrPoints.size());
/* 3018 */     addGoto(target, 167);
/* 3019 */     int retLabel = this.cfw.acquireLabel();
/* 3020 */     this.cfw.markLabel(retLabel);
/* 3021 */     ret.jsrPoints.add(Integer.valueOf(retLabel));
/*      */   }
/*      */   
/*      */   private void generateArrayLiteralFactory(Node node, int count) {
/* 3025 */     String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + count;
/* 3026 */     initBodyGeneration();
/* 3027 */     this.argsLocal = this.firstFreeLocal = (short)(this.firstFreeLocal + 1);
/* 3028 */     this.localsMax = this.firstFreeLocal;
/* 3029 */     this.cfw.startMethod(methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;", (short)2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3035 */     visitArrayLiteral(node, node.getFirstChild(), true);
/* 3036 */     this.cfw.add(176);
/* 3037 */     this.cfw.stopMethod((short)(this.localsMax + 1));
/*      */   }
/*      */   
/*      */   private void generateObjectLiteralFactory(Node node, int count) {
/* 3041 */     String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + count;
/* 3042 */     initBodyGeneration();
/* 3043 */     this.argsLocal = this.firstFreeLocal = (short)(this.firstFreeLocal + 1);
/* 3044 */     this.localsMax = this.firstFreeLocal;
/* 3045 */     this.cfw.startMethod(methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;", (short)2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3051 */     visitObjectLiteral(node, node.getFirstChild(), true);
/* 3052 */     this.cfw.add(176);
/* 3053 */     this.cfw.stopMethod((short)(this.localsMax + 1));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitArrayLiteral(Node node, Node child, boolean topLevel) {
/* 3059 */     int count = 0;
/* 3060 */     for (Node cursor = child; cursor != null; cursor = cursor.getNext()) {
/* 3061 */       count++;
/*      */     }
/*      */ 
/*      */     
/* 3065 */     if (!topLevel && (count > 10 || this.cfw.getCurrentCodeOffset() > 30000) && !this.hasVarsInRegs && !this.isGenerator && !this.inLocalBlock) {
/*      */       
/* 3067 */       if (this.literals == null) {
/* 3068 */         this.literals = new LinkedList<Node>();
/*      */       }
/* 3070 */       this.literals.add(node);
/* 3071 */       String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + this.literals.size();
/* 3072 */       this.cfw.addALoad(this.funObjLocal);
/* 3073 */       this.cfw.addALoad(this.contextLocal);
/* 3074 */       this.cfw.addALoad(this.variableObjectLocal);
/* 3075 */       this.cfw.addALoad(this.thisObjLocal);
/* 3076 */       this.cfw.addALoad(this.argsLocal);
/* 3077 */       this.cfw.addInvoke(182, this.codegen.mainClassName, methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3087 */     if (this.isGenerator) {
/*      */       int i;
/*      */       
/* 3090 */       for (i = 0; i != count; i++) {
/* 3091 */         generateExpression(child, node);
/* 3092 */         child = child.getNext();
/*      */       } 
/* 3094 */       addNewObjectArray(count);
/* 3095 */       for (i = 0; i != count; i++) {
/* 3096 */         this.cfw.add(90);
/* 3097 */         this.cfw.add(95);
/* 3098 */         this.cfw.addPush(count - i - 1);
/* 3099 */         this.cfw.add(95);
/* 3100 */         this.cfw.add(83);
/*      */       } 
/*      */     } else {
/* 3103 */       addNewObjectArray(count);
/* 3104 */       for (int i = 0; i != count; i++) {
/* 3105 */         this.cfw.add(89);
/* 3106 */         this.cfw.addPush(i);
/* 3107 */         generateExpression(child, node);
/* 3108 */         this.cfw.add(83);
/* 3109 */         child = child.getNext();
/*      */       } 
/*      */     } 
/* 3112 */     int[] skipIndexes = (int[])node.getProp(11);
/* 3113 */     if (skipIndexes == null) {
/* 3114 */       this.cfw.add(1);
/* 3115 */       this.cfw.add(3);
/*      */     } else {
/* 3117 */       this.cfw.addPush(OptRuntime.encodeIntArray(skipIndexes));
/* 3118 */       this.cfw.addPush(skipIndexes.length);
/*      */     } 
/* 3120 */     this.cfw.addALoad(this.contextLocal);
/* 3121 */     this.cfw.addALoad(this.variableObjectLocal);
/* 3122 */     addOptRuntimeInvoke("newArrayLiteral", "([Ljava/lang/Object;Ljava/lang/String;ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addLoadPropertyIds(Object[] properties, int count) {
/* 3133 */     addNewObjectArray(count);
/* 3134 */     for (int i = 0; i != count; i++) {
/* 3135 */       this.cfw.add(89);
/* 3136 */       this.cfw.addPush(i);
/* 3137 */       Object id = properties[i];
/* 3138 */       if (id instanceof String) {
/* 3139 */         this.cfw.addPush((String)id);
/*      */       } else {
/* 3141 */         this.cfw.addPush(((Integer)id).intValue());
/* 3142 */         addScriptRuntimeInvoke("wrapInt", "(I)Ljava/lang/Integer;");
/*      */       } 
/* 3144 */       this.cfw.add(83);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addLoadPropertyValues(Node node, Node child, int count) {
/* 3150 */     if (this.isGenerator) {
/*      */       int i;
/* 3152 */       for (i = 0; i != count; i++) {
/* 3153 */         int childType = child.getType();
/* 3154 */         if (childType == 151 || childType == 152) {
/* 3155 */           generateExpression(child.getFirstChild(), node);
/*      */         } else {
/* 3157 */           generateExpression(child, node);
/*      */         } 
/* 3159 */         child = child.getNext();
/*      */       } 
/* 3161 */       addNewObjectArray(count);
/* 3162 */       for (i = 0; i != count; i++) {
/* 3163 */         this.cfw.add(90);
/* 3164 */         this.cfw.add(95);
/* 3165 */         this.cfw.addPush(count - i - 1);
/* 3166 */         this.cfw.add(95);
/* 3167 */         this.cfw.add(83);
/*      */       } 
/*      */     } else {
/* 3170 */       addNewObjectArray(count);
/* 3171 */       Node child2 = child;
/* 3172 */       for (int i = 0; i != count; i++) {
/* 3173 */         this.cfw.add(89);
/* 3174 */         this.cfw.addPush(i);
/* 3175 */         int childType = child2.getType();
/* 3176 */         if (childType == 151 || childType == 152) {
/* 3177 */           generateExpression(child2.getFirstChild(), node);
/*      */         } else {
/* 3179 */           generateExpression(child2, node);
/*      */         } 
/* 3181 */         this.cfw.add(83);
/* 3182 */         child2 = child2.getNext();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitObjectLiteral(Node node, Node child, boolean topLevel) {
/* 3189 */     Object[] properties = (Object[])node.getProp(12);
/* 3190 */     int count = properties.length;
/*      */ 
/*      */     
/* 3193 */     if (!topLevel && (count > 10 || this.cfw.getCurrentCodeOffset() > 30000) && !this.hasVarsInRegs && !this.isGenerator && !this.inLocalBlock) {
/*      */       
/* 3195 */       if (this.literals == null) {
/* 3196 */         this.literals = new LinkedList<Node>();
/*      */       }
/* 3198 */       this.literals.add(node);
/* 3199 */       String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + this.literals.size();
/* 3200 */       this.cfw.addALoad(this.funObjLocal);
/* 3201 */       this.cfw.addALoad(this.contextLocal);
/* 3202 */       this.cfw.addALoad(this.variableObjectLocal);
/* 3203 */       this.cfw.addALoad(this.thisObjLocal);
/* 3204 */       this.cfw.addALoad(this.argsLocal);
/* 3205 */       this.cfw.addInvoke(182, this.codegen.mainClassName, methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3214 */     if (this.isGenerator) {
/*      */ 
/*      */       
/* 3217 */       addLoadPropertyValues(node, child, count);
/* 3218 */       addLoadPropertyIds(properties, count);
/*      */       
/* 3220 */       this.cfw.add(95);
/*      */     } else {
/* 3222 */       addLoadPropertyIds(properties, count);
/* 3223 */       addLoadPropertyValues(node, child, count);
/*      */     } 
/*      */ 
/*      */     
/* 3227 */     boolean hasGetterSetters = false;
/* 3228 */     Node child2 = child; int i;
/* 3229 */     for (i = 0; i != count; i++) {
/* 3230 */       int childType = child2.getType();
/* 3231 */       if (childType == 151 || childType == 152) {
/* 3232 */         hasGetterSetters = true;
/*      */         break;
/*      */       } 
/* 3235 */       child2 = child2.getNext();
/*      */     } 
/*      */     
/* 3238 */     if (hasGetterSetters) {
/* 3239 */       this.cfw.addPush(count);
/* 3240 */       this.cfw.add(188, 10);
/* 3241 */       child2 = child;
/* 3242 */       for (i = 0; i != count; i++) {
/* 3243 */         this.cfw.add(89);
/* 3244 */         this.cfw.addPush(i);
/* 3245 */         int childType = child2.getType();
/* 3246 */         if (childType == 151) {
/* 3247 */           this.cfw.add(2);
/* 3248 */         } else if (childType == 152) {
/* 3249 */           this.cfw.add(4);
/*      */         } else {
/* 3251 */           this.cfw.add(3);
/*      */         } 
/* 3253 */         this.cfw.add(79);
/* 3254 */         child2 = child2.getNext();
/*      */       } 
/*      */     } else {
/* 3257 */       this.cfw.add(1);
/*      */     } 
/*      */     
/* 3260 */     this.cfw.addALoad(this.contextLocal);
/* 3261 */     this.cfw.addALoad(this.variableObjectLocal);
/* 3262 */     addScriptRuntimeInvoke("newObjectLiteral", "([Ljava/lang/Object;[Ljava/lang/Object;[ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitSpecialCall(Node node, int type, int specialType, Node child) {
/*      */     String methodName, callSignature;
/* 3274 */     this.cfw.addALoad(this.contextLocal);
/*      */     
/* 3276 */     if (type == 30) {
/* 3277 */       generateExpression(child, node);
/*      */     } else {
/*      */       
/* 3280 */       generateFunctionAndThisObj(child, node);
/*      */     } 
/*      */     
/* 3283 */     child = child.getNext();
/*      */     
/* 3285 */     generateCallArgArray(node, child, false);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3290 */     if (type == 30) {
/* 3291 */       methodName = "newObjectSpecial";
/* 3292 */       callSignature = "(Lorg/mozilla/javascript/Context;Ljava/lang/Object;[Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3299 */       this.cfw.addALoad(this.variableObjectLocal);
/* 3300 */       this.cfw.addALoad(this.thisObjLocal);
/* 3301 */       this.cfw.addPush(specialType);
/*      */     } else {
/* 3303 */       methodName = "callSpecial";
/* 3304 */       callSignature = "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;ILjava/lang/String;I)Ljava/lang/Object;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3313 */       this.cfw.addALoad(this.variableObjectLocal);
/* 3314 */       this.cfw.addALoad(this.thisObjLocal);
/* 3315 */       this.cfw.addPush(specialType);
/* 3316 */       String sourceName = this.scriptOrFn.getSourceName();
/* 3317 */       this.cfw.addPush((sourceName == null) ? "" : sourceName);
/* 3318 */       this.cfw.addPush(this.itsLineNumber);
/*      */     } 
/*      */     
/* 3321 */     addOptRuntimeInvoke(methodName, callSignature);
/*      */   }
/*      */   
/*      */   private void visitStandardCall(Node node, Node child) {
/*      */     String methodName, signature;
/* 3326 */     if (node.getType() != 38) throw Codegen.badTree();
/*      */     
/* 3328 */     Node firstArgChild = child.getNext();
/* 3329 */     int childType = child.getType();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3334 */     if (firstArgChild == null) {
/* 3335 */       if (childType == 39)
/*      */       {
/* 3337 */         String name = child.getString();
/* 3338 */         this.cfw.addPush(name);
/* 3339 */         methodName = "callName0";
/* 3340 */         signature = "(Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
/*      */ 
/*      */       
/*      */       }
/* 3344 */       else if (childType == 33)
/*      */       {
/* 3346 */         Node propTarget = child.getFirstChild();
/* 3347 */         generateExpression(propTarget, node);
/* 3348 */         Node id = propTarget.getNext();
/* 3349 */         String property = id.getString();
/* 3350 */         this.cfw.addPush(property);
/* 3351 */         methodName = "callProp0";
/* 3352 */         signature = "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
/*      */       
/*      */       }
/*      */       else
/*      */       {
/* 3357 */         if (childType == 34) {
/* 3358 */           throw Kit.codeBug();
/*      */         }
/* 3360 */         generateFunctionAndThisObj(child, node);
/* 3361 */         methodName = "call0";
/* 3362 */         signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 3369 */     else if (childType == 39) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3374 */       String name = child.getString();
/* 3375 */       generateCallArgArray(node, firstArgChild, false);
/* 3376 */       this.cfw.addPush(name);
/* 3377 */       methodName = "callName";
/* 3378 */       signature = "([Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 3384 */       int argCount = 0;
/* 3385 */       for (Node arg = firstArgChild; arg != null; arg = arg.getNext()) {
/* 3386 */         argCount++;
/*      */       }
/* 3388 */       generateFunctionAndThisObj(child, node);
/*      */       
/* 3390 */       if (argCount == 1) {
/* 3391 */         generateExpression(firstArgChild, node);
/* 3392 */         methodName = "call1";
/* 3393 */         signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 3399 */       else if (argCount == 2) {
/* 3400 */         generateExpression(firstArgChild, node);
/* 3401 */         generateExpression(firstArgChild.getNext(), node);
/* 3402 */         methodName = "call2";
/* 3403 */         signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/* 3411 */         generateCallArgArray(node, firstArgChild, false);
/* 3412 */         methodName = "callN";
/* 3413 */         signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3422 */     this.cfw.addALoad(this.contextLocal);
/* 3423 */     this.cfw.addALoad(this.variableObjectLocal);
/* 3424 */     addOptRuntimeInvoke(methodName, signature);
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitStandardNew(Node node, Node child) {
/* 3429 */     if (node.getType() != 30) throw Codegen.badTree();
/*      */     
/* 3431 */     Node firstArgChild = child.getNext();
/*      */     
/* 3433 */     generateExpression(child, node);
/*      */     
/* 3435 */     this.cfw.addALoad(this.contextLocal);
/* 3436 */     this.cfw.addALoad(this.variableObjectLocal);
/*      */     
/* 3438 */     generateCallArgArray(node, firstArgChild, false);
/* 3439 */     addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
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
/*      */   private void visitOptimizedCall(Node node, OptFunctionNode target, int type, Node child) {
/* 3451 */     Node firstArgChild = child.getNext();
/* 3452 */     String className = this.codegen.mainClassName;
/*      */     
/* 3454 */     short thisObjLocal = 0;
/* 3455 */     if (type == 30) {
/* 3456 */       generateExpression(child, node);
/*      */     } else {
/* 3458 */       generateFunctionAndThisObj(child, node);
/* 3459 */       thisObjLocal = getNewWordLocal();
/* 3460 */       this.cfw.addAStore(thisObjLocal);
/*      */     } 
/*      */ 
/*      */     
/* 3464 */     int beyond = this.cfw.acquireLabel();
/* 3465 */     int regularCall = this.cfw.acquireLabel();
/*      */     
/* 3467 */     this.cfw.add(89);
/* 3468 */     this.cfw.add(193, className);
/* 3469 */     this.cfw.add(153, regularCall);
/* 3470 */     this.cfw.add(192, className);
/* 3471 */     this.cfw.add(89);
/* 3472 */     this.cfw.add(180, className, "_id", "I");
/* 3473 */     this.cfw.addPush(this.codegen.getIndex((ScriptNode)target.fnode));
/* 3474 */     this.cfw.add(160, regularCall);
/*      */ 
/*      */     
/* 3477 */     this.cfw.addALoad(this.contextLocal);
/* 3478 */     this.cfw.addALoad(this.variableObjectLocal);
/*      */ 
/*      */     
/* 3481 */     if (type == 30) {
/* 3482 */       this.cfw.add(1);
/*      */     } else {
/* 3484 */       this.cfw.addALoad(thisObjLocal);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3494 */     Node argChild = firstArgChild;
/* 3495 */     while (argChild != null) {
/* 3496 */       int dcp_register = nodeIsDirectCallParameter(argChild);
/* 3497 */       if (dcp_register >= 0) {
/* 3498 */         this.cfw.addALoad(dcp_register);
/* 3499 */         this.cfw.addDLoad(dcp_register + 1);
/* 3500 */       } else if (argChild.getIntProp(8, -1) == 0) {
/*      */ 
/*      */         
/* 3503 */         this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */         
/* 3507 */         generateExpression(argChild, node);
/*      */       } else {
/* 3509 */         generateExpression(argChild, node);
/* 3510 */         this.cfw.addPush(0.0D);
/*      */       } 
/* 3512 */       argChild = argChild.getNext();
/*      */     } 
/*      */     
/* 3515 */     this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
/*      */ 
/*      */     
/* 3518 */     this.cfw.addInvoke(184, this.codegen.mainClassName, (type == 30) ? this.codegen.getDirectCtorName((ScriptNode)target.fnode) : this.codegen.getBodyMethodName((ScriptNode)target.fnode), this.codegen.getBodyMethodSignature((ScriptNode)target.fnode));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3525 */     this.cfw.add(167, beyond);
/*      */     
/* 3527 */     this.cfw.markLabel(regularCall);
/*      */     
/* 3529 */     this.cfw.addALoad(this.contextLocal);
/* 3530 */     this.cfw.addALoad(this.variableObjectLocal);
/*      */     
/* 3532 */     if (type != 30) {
/* 3533 */       this.cfw.addALoad(thisObjLocal);
/* 3534 */       releaseWordLocal(thisObjLocal);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3539 */     generateCallArgArray(node, firstArgChild, true);
/*      */     
/* 3541 */     if (type == 30) {
/* 3542 */       addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/* 3550 */       this.cfw.addInvoke(185, "org/mozilla/javascript/Callable", "call", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3560 */     this.cfw.markLabel(beyond);
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateCallArgArray(Node node, Node argChild, boolean directCall) {
/* 3565 */     int argCount = 0;
/* 3566 */     for (Node child = argChild; child != null; child = child.getNext()) {
/* 3567 */       argCount++;
/*      */     }
/*      */     
/* 3570 */     if (argCount == 1 && this.itsOneArgArray >= 0) {
/* 3571 */       this.cfw.addALoad(this.itsOneArgArray);
/*      */     } else {
/* 3573 */       addNewObjectArray(argCount);
/*      */     } 
/*      */     
/* 3576 */     for (int i = 0; i != argCount; i++) {
/*      */ 
/*      */ 
/*      */       
/* 3580 */       if (!this.isGenerator) {
/* 3581 */         this.cfw.add(89);
/* 3582 */         this.cfw.addPush(i);
/*      */       } 
/*      */       
/* 3585 */       if (!directCall) {
/* 3586 */         generateExpression(argChild, node);
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 3593 */         int dcp_register = nodeIsDirectCallParameter(argChild);
/* 3594 */         if (dcp_register >= 0) {
/* 3595 */           dcpLoadAsObject(dcp_register);
/*      */         } else {
/* 3597 */           generateExpression(argChild, node);
/* 3598 */           int childNumberFlag = argChild.getIntProp(8, -1);
/*      */           
/* 3600 */           if (childNumberFlag == 0) {
/* 3601 */             addDoubleWrap();
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3609 */       if (this.isGenerator) {
/* 3610 */         short tempLocal = getNewWordLocal();
/* 3611 */         this.cfw.addAStore(tempLocal);
/* 3612 */         this.cfw.add(192, "[Ljava/lang/Object;");
/* 3613 */         this.cfw.add(89);
/* 3614 */         this.cfw.addPush(i);
/* 3615 */         this.cfw.addALoad(tempLocal);
/* 3616 */         releaseWordLocal(tempLocal);
/*      */       } 
/*      */       
/* 3619 */       this.cfw.add(83);
/*      */       
/* 3621 */       argChild = argChild.getNext();
/*      */     } 
/*      */   }
/*      */   private void generateFunctionAndThisObj(Node node, Node parent) {
/*      */     Node target;
/*      */     String name;
/*      */     Node id;
/* 3628 */     int type = node.getType();
/* 3629 */     switch (node.getType()) {
/*      */       case 34:
/* 3631 */         throw Kit.codeBug();
/*      */       
/*      */       case 33:
/*      */       case 36:
/* 3635 */         target = node.getFirstChild();
/* 3636 */         generateExpression(target, node);
/* 3637 */         id = target.getNext();
/* 3638 */         if (type == 33) {
/* 3639 */           String property = id.getString();
/* 3640 */           this.cfw.addPush(property);
/* 3641 */           this.cfw.addALoad(this.contextLocal);
/* 3642 */           this.cfw.addALoad(this.variableObjectLocal);
/* 3643 */           addScriptRuntimeInvoke("getPropFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
/*      */ 
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */ 
/*      */         
/* 3651 */         generateExpression(id, node);
/* 3652 */         if (node.getIntProp(8, -1) != -1)
/* 3653 */           addDoubleWrap(); 
/* 3654 */         this.cfw.addALoad(this.contextLocal);
/* 3655 */         this.cfw.addALoad(this.variableObjectLocal);
/* 3656 */         addScriptRuntimeInvoke("getElemFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 39:
/* 3668 */         name = node.getString();
/* 3669 */         this.cfw.addPush(name);
/* 3670 */         this.cfw.addALoad(this.contextLocal);
/* 3671 */         this.cfw.addALoad(this.variableObjectLocal);
/* 3672 */         addScriptRuntimeInvoke("getNameFunctionAndThis", "(Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/* 3682 */         generateExpression(node, parent);
/* 3683 */         this.cfw.addALoad(this.contextLocal);
/* 3684 */         addScriptRuntimeInvoke("getValueFunctionAndThis", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Callable;");
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3692 */     this.cfw.addALoad(this.contextLocal);
/* 3693 */     addScriptRuntimeInvoke("lastStoredScriptable", "(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateLineNumber(Node node) {
/* 3701 */     this.itsLineNumber = node.getLineno();
/* 3702 */     if (this.itsLineNumber == -1)
/*      */       return; 
/* 3704 */     this.cfw.addLineNumberEntry((short)this.itsLineNumber);
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
/*      */   private void visitTryCatchFinally(Jump node, Node child) {
/* 3721 */     short savedVariableObject = getNewWordLocal();
/* 3722 */     this.cfw.addALoad(this.variableObjectLocal);
/* 3723 */     this.cfw.addAStore(savedVariableObject);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3730 */     int startLabel = this.cfw.acquireLabel();
/* 3731 */     this.cfw.markLabel(startLabel, (short)0);
/*      */     
/* 3733 */     Node catchTarget = node.target;
/* 3734 */     Node finallyTarget = node.getFinally();
/* 3735 */     int[] handlerLabels = new int[5];
/*      */     
/* 3737 */     this.exceptionManager.pushExceptionInfo(node);
/* 3738 */     if (catchTarget != null) {
/* 3739 */       handlerLabels[0] = this.cfw.acquireLabel();
/* 3740 */       handlerLabels[1] = this.cfw.acquireLabel();
/* 3741 */       handlerLabels[2] = this.cfw.acquireLabel();
/* 3742 */       Context cx = Context.getCurrentContext();
/* 3743 */       if (cx != null && cx.hasFeature(13))
/*      */       {
/* 3745 */         handlerLabels[3] = this.cfw.acquireLabel();
/*      */       }
/*      */     } 
/* 3748 */     if (finallyTarget != null) {
/* 3749 */       handlerLabels[4] = this.cfw.acquireLabel();
/*      */     }
/* 3751 */     this.exceptionManager.setHandlers(handlerLabels, startLabel);
/*      */ 
/*      */     
/* 3754 */     if (this.isGenerator && finallyTarget != null) {
/* 3755 */       FinallyReturnPoint ret = new FinallyReturnPoint();
/* 3756 */       if (this.finallys == null) {
/* 3757 */         this.finallys = new HashMap<Node, FinallyReturnPoint>();
/*      */       }
/*      */       
/* 3760 */       this.finallys.put(finallyTarget, ret);
/*      */       
/* 3762 */       this.finallys.put(finallyTarget.getNext(), ret);
/*      */     } 
/*      */     
/* 3765 */     while (child != null) {
/* 3766 */       if (child == catchTarget) {
/* 3767 */         int catchLabel = getTargetLabel(catchTarget);
/* 3768 */         this.exceptionManager.removeHandler(0, catchLabel);
/*      */         
/* 3770 */         this.exceptionManager.removeHandler(1, catchLabel);
/*      */         
/* 3772 */         this.exceptionManager.removeHandler(2, catchLabel);
/*      */         
/* 3774 */         this.exceptionManager.removeHandler(3, catchLabel);
/*      */       } 
/*      */       
/* 3777 */       generateStatement(child);
/* 3778 */       child = child.getNext();
/*      */     } 
/*      */ 
/*      */     
/* 3782 */     int realEnd = this.cfw.acquireLabel();
/* 3783 */     this.cfw.add(167, realEnd);
/*      */     
/* 3785 */     int exceptionLocal = getLocalBlockRegister((Node)node);
/*      */ 
/*      */     
/* 3788 */     if (catchTarget != null) {
/*      */       
/* 3790 */       int catchLabel = catchTarget.labelId();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3797 */       generateCatchBlock(0, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[0]);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3804 */       generateCatchBlock(1, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[1]);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3812 */       generateCatchBlock(2, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[2]);
/*      */ 
/*      */ 
/*      */       
/* 3816 */       Context cx = Context.getCurrentContext();
/* 3817 */       if (cx != null && cx.hasFeature(13))
/*      */       {
/*      */         
/* 3820 */         generateCatchBlock(3, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[3]);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3828 */     if (finallyTarget != null) {
/* 3829 */       int finallyHandler = this.cfw.acquireLabel();
/* 3830 */       int finallyEnd = this.cfw.acquireLabel();
/* 3831 */       this.cfw.markHandler(finallyHandler);
/* 3832 */       if (!this.isGenerator) {
/* 3833 */         this.cfw.markLabel(handlerLabels[4]);
/*      */       }
/* 3835 */       this.cfw.addAStore(exceptionLocal);
/*      */ 
/*      */       
/* 3838 */       this.cfw.addALoad(savedVariableObject);
/* 3839 */       this.cfw.addAStore(this.variableObjectLocal);
/*      */ 
/*      */       
/* 3842 */       int finallyLabel = finallyTarget.labelId();
/* 3843 */       if (this.isGenerator) {
/* 3844 */         addGotoWithReturn(finallyTarget);
/*      */       } else {
/* 3846 */         inlineFinally(finallyTarget, handlerLabels[4], finallyEnd);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3851 */       this.cfw.addALoad(exceptionLocal);
/* 3852 */       if (this.isGenerator)
/* 3853 */         this.cfw.add(192, "java/lang/Throwable"); 
/* 3854 */       this.cfw.add(191);
/*      */       
/* 3856 */       this.cfw.markLabel(finallyEnd);
/*      */       
/* 3858 */       if (this.isGenerator) {
/* 3859 */         this.cfw.addExceptionHandler(startLabel, finallyLabel, finallyHandler, null);
/*      */       }
/*      */     } 
/*      */     
/* 3863 */     releaseWordLocal(savedVariableObject);
/* 3864 */     this.cfw.markLabel(realEnd);
/*      */     
/* 3866 */     if (!this.isGenerator) {
/* 3867 */       this.exceptionManager.popExceptionInfo();
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
/*      */   private void generateCatchBlock(int exceptionType, short savedVariableObject, int catchLabel, int exceptionLocal, int handler) {
/* 3887 */     if (handler == 0) {
/* 3888 */       handler = this.cfw.acquireLabel();
/*      */     }
/* 3890 */     this.cfw.markHandler(handler);
/*      */ 
/*      */     
/* 3893 */     this.cfw.addAStore(exceptionLocal);
/*      */ 
/*      */     
/* 3896 */     this.cfw.addALoad(savedVariableObject);
/* 3897 */     this.cfw.addAStore(this.variableObjectLocal);
/*      */     
/* 3899 */     String exceptionName = exceptionTypeToName(exceptionType);
/*      */     
/* 3901 */     this.cfw.add(167, catchLabel);
/*      */   }
/*      */ 
/*      */   
/*      */   private String exceptionTypeToName(int exceptionType) {
/* 3906 */     if (exceptionType == 0)
/* 3907 */       return "org/mozilla/javascript/JavaScriptException"; 
/* 3908 */     if (exceptionType == 1)
/* 3909 */       return "org/mozilla/javascript/EvaluatorException"; 
/* 3910 */     if (exceptionType == 2)
/* 3911 */       return "org/mozilla/javascript/EcmaError"; 
/* 3912 */     if (exceptionType == 3)
/* 3913 */       return "java/lang/Throwable"; 
/* 3914 */     if (exceptionType == 4) {
/* 3915 */       return null;
/*      */     }
/* 3917 */     throw Kit.codeBug();
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
/*      */   private class ExceptionManager
/*      */   {
/* 3948 */     private LinkedList<ExceptionInfo> exceptionInfo = new LinkedList<ExceptionInfo>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void pushExceptionInfo(Jump node) {
/* 3959 */       Node fBlock = BodyCodegen.this.getFinallyAtTarget(node.getFinally());
/* 3960 */       ExceptionInfo ei = new ExceptionInfo(node, fBlock);
/* 3961 */       this.exceptionInfo.add(ei);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addHandler(int exceptionType, int handlerLabel, int startLabel) {
/* 3975 */       ExceptionInfo top = getTop();
/* 3976 */       top.handlerLabels[exceptionType] = handlerLabel;
/* 3977 */       top.exceptionStarts[exceptionType] = startLabel;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setHandlers(int[] handlerLabels, int startLabel) {
/* 3992 */       ExceptionInfo top = getTop();
/* 3993 */       for (int i = 0; i < handlerLabels.length; i++) {
/* 3994 */         if (handlerLabels[i] != 0) {
/* 3995 */           addHandler(i, handlerLabels[i], startLabel);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int removeHandler(int exceptionType, int endLabel) {
/* 4012 */       ExceptionInfo top = getTop();
/* 4013 */       if (top.handlerLabels[exceptionType] != 0) {
/* 4014 */         int handlerLabel = top.handlerLabels[exceptionType];
/* 4015 */         endCatch(top, exceptionType, endLabel);
/* 4016 */         top.handlerLabels[exceptionType] = 0;
/* 4017 */         return handlerLabel;
/*      */       } 
/* 4019 */       return 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void popExceptionInfo() {
/* 4027 */       this.exceptionInfo.removeLast();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void markInlineFinallyStart(Node finallyBlock, int finallyStart) {
/* 4054 */       ListIterator<ExceptionInfo> iter = this.exceptionInfo.listIterator(this.exceptionInfo.size());
/*      */       
/* 4056 */       while (iter.hasPrevious()) {
/* 4057 */         ExceptionInfo ei = iter.previous();
/* 4058 */         for (int i = 0; i < 5; i++) {
/* 4059 */           if (ei.handlerLabels[i] != 0 && ei.currentFinally == null) {
/* 4060 */             endCatch(ei, i, finallyStart);
/* 4061 */             ei.exceptionStarts[i] = 0;
/* 4062 */             ei.currentFinally = finallyBlock;
/*      */           } 
/*      */         } 
/* 4065 */         if (ei.finallyBlock == finallyBlock) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void markInlineFinallyEnd(Node finallyBlock, int finallyEnd) {
/* 4083 */       ListIterator<ExceptionInfo> iter = this.exceptionInfo.listIterator(this.exceptionInfo.size());
/*      */       
/* 4085 */       while (iter.hasPrevious()) {
/* 4086 */         ExceptionInfo ei = iter.previous();
/* 4087 */         for (int i = 0; i < 5; i++) {
/* 4088 */           if (ei.handlerLabels[i] != 0 && ei.currentFinally == finallyBlock) {
/*      */             
/* 4090 */             ei.exceptionStarts[i] = finallyEnd;
/* 4091 */             ei.currentFinally = null;
/*      */           } 
/*      */         } 
/* 4094 */         if (ei.finallyBlock == finallyBlock) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void endCatch(ExceptionInfo ei, int exceptionType, int catchEnd) {
/* 4109 */       if (ei.exceptionStarts[exceptionType] == 0) {
/* 4110 */         throw new IllegalStateException("bad exception start");
/*      */       }
/*      */       
/* 4113 */       int currentStart = ei.exceptionStarts[exceptionType];
/* 4114 */       int currentStartPC = BodyCodegen.this.cfw.getLabelPC(currentStart);
/* 4115 */       int catchEndPC = BodyCodegen.this.cfw.getLabelPC(catchEnd);
/* 4116 */       if (currentStartPC != catchEndPC) {
/* 4117 */         BodyCodegen.this.cfw.addExceptionHandler(ei.exceptionStarts[exceptionType], catchEnd, ei.handlerLabels[exceptionType], BodyCodegen.this.exceptionTypeToName(exceptionType));
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ExceptionInfo getTop() {
/* 4126 */       return this.exceptionInfo.getLast();
/*      */     }
/*      */     private class ExceptionInfo { Jump node; Node finallyBlock; int[] handlerLabels;
/*      */       int[] exceptionStarts;
/*      */       Node currentFinally;
/*      */       
/*      */       ExceptionInfo(Jump node, Node finallyBlock) {
/* 4133 */         this.node = node;
/* 4134 */         this.finallyBlock = finallyBlock;
/* 4135 */         this.handlerLabels = new int[5];
/* 4136 */         this.exceptionStarts = new int[5];
/* 4137 */         this.currentFinally = null;
/*      */       } }
/*      */   
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
/* 4153 */   private ExceptionManager exceptionManager = new ExceptionManager(); static final int GENERATOR_TERMINATE = -1; static final int GENERATOR_START = 0; static final int GENERATOR_YIELD_START = 1; ClassFileWriter cfw; Codegen codegen; CompilerEnvirons compilerEnv; ScriptNode scriptOrFn; public int scriptOrFnIndex; private int savedCodeOffset;
/*      */   private OptFunctionNode fnCurrent;
/*      */   private static final int MAX_LOCALS = 1024;
/*      */   private int[] locals;
/*      */   private short firstFreeLocal;
/*      */   private short localsMax;
/*      */   private int itsLineNumber;
/*      */   private boolean hasVarsInRegs;
/*      */   private short[] varRegisters;
/*      */   private boolean inDirectCallFunction;
/*      */   private boolean itsForcedObjectParameters;
/*      */   private int enterAreaStartLabel;
/*      */   private int epilogueLabel;
/*      */   private boolean inLocalBlock;
/*      */   private short variableObjectLocal;
/*      */   private short popvLocal;
/*      */   private short contextLocal;
/*      */   private short argsLocal;
/*      */   private short operationLocal;
/*      */   private short thisObjLocal;
/*      */   private short funObjLocal;
/*      */   private short itsZeroArgArray;
/*      */   private short itsOneArgArray;
/*      */   private short generatorStateLocal;
/*      */   private boolean isGenerator;
/*      */   private int generatorSwitch;
/*      */   
/*      */   private void inlineFinally(Node finallyTarget, int finallyStart, int finallyEnd) {
/* 4181 */     Node fBlock = getFinallyAtTarget(finallyTarget);
/* 4182 */     fBlock.resetTargets();
/* 4183 */     Node child = fBlock.getFirstChild();
/* 4184 */     this.exceptionManager.markInlineFinallyStart(fBlock, finallyStart);
/* 4185 */     while (child != null) {
/* 4186 */       generateStatement(child);
/* 4187 */       child = child.getNext();
/*      */     } 
/* 4189 */     this.exceptionManager.markInlineFinallyEnd(fBlock, finallyEnd);
/*      */   }
/*      */   
/*      */   private void inlineFinally(Node finallyTarget) {
/* 4193 */     int finallyStart = this.cfw.acquireLabel();
/* 4194 */     int finallyEnd = this.cfw.acquireLabel();
/* 4195 */     this.cfw.markLabel(finallyStart);
/* 4196 */     inlineFinally(finallyTarget, finallyStart, finallyEnd);
/* 4197 */     this.cfw.markLabel(finallyEnd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node getFinallyAtTarget(Node node) {
/* 4207 */     if (node == null)
/* 4208 */       return null; 
/* 4209 */     if (node.getType() == 125)
/* 4210 */       return node; 
/* 4211 */     if (node != null && node.getType() == 131) {
/* 4212 */       Node fBlock = node.getNext();
/* 4213 */       if (fBlock != null && fBlock.getType() == 125) {
/* 4214 */         return fBlock;
/*      */       }
/*      */     } 
/* 4217 */     throw Kit.codeBug("bad finally target");
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean generateSaveLocals(Node node) {
/* 4222 */     int count = 0;
/* 4223 */     for (int i = 0; i < this.firstFreeLocal; i++) {
/* 4224 */       if (this.locals[i] != 0) {
/* 4225 */         count++;
/*      */       }
/*      */     } 
/* 4228 */     if (count == 0) {
/* 4229 */       ((FunctionNode)this.scriptOrFn).addLiveLocals(node, null);
/* 4230 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 4234 */     this.maxLocals = (this.maxLocals > count) ? this.maxLocals : count;
/*      */ 
/*      */     
/* 4237 */     int[] ls = new int[count];
/* 4238 */     int s = 0; int j;
/* 4239 */     for (j = 0; j < this.firstFreeLocal; j++) {
/* 4240 */       if (this.locals[j] != 0) {
/* 4241 */         ls[s] = j;
/* 4242 */         s++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 4247 */     ((FunctionNode)this.scriptOrFn).addLiveLocals(node, ls);
/*      */ 
/*      */     
/* 4250 */     generateGetGeneratorLocalsState();
/* 4251 */     for (j = 0; j < count; j++) {
/* 4252 */       this.cfw.add(89);
/* 4253 */       this.cfw.addLoadConstant(j);
/* 4254 */       this.cfw.addALoad(ls[j]);
/* 4255 */       this.cfw.add(83);
/*      */     } 
/*      */     
/* 4258 */     this.cfw.add(87);
/*      */     
/* 4260 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitSwitch(Jump switchNode, Node child) {
/* 4268 */     generateExpression(child, (Node)switchNode);
/*      */     
/* 4270 */     short selector = getNewWordLocal();
/* 4271 */     this.cfw.addAStore(selector);
/*      */     
/* 4273 */     Jump caseNode = (Jump)child.getNext();
/* 4274 */     for (; caseNode != null; 
/* 4275 */       caseNode = (Jump)caseNode.getNext()) {
/*      */       
/* 4277 */       if (caseNode.getType() != 115)
/* 4278 */         throw Codegen.badTree(); 
/* 4279 */       Node test = caseNode.getFirstChild();
/* 4280 */       generateExpression(test, (Node)caseNode);
/* 4281 */       this.cfw.addALoad(selector);
/* 4282 */       addScriptRuntimeInvoke("shallowEq", "(Ljava/lang/Object;Ljava/lang/Object;)Z");
/*      */ 
/*      */ 
/*      */       
/* 4286 */       addGoto(caseNode.target, 154);
/*      */     } 
/* 4288 */     releaseWordLocal(selector);
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitTypeofname(Node node) {
/* 4293 */     if (this.hasVarsInRegs) {
/* 4294 */       int varIndex = this.fnCurrent.fnode.getIndexForNameNode(node);
/* 4295 */       if (varIndex >= 0) {
/* 4296 */         if (this.fnCurrent.isNumberVar(varIndex)) {
/* 4297 */           this.cfw.addPush("number");
/* 4298 */         } else if (varIsDirectCallParameter(varIndex)) {
/* 4299 */           int dcp_register = this.varRegisters[varIndex];
/* 4300 */           this.cfw.addALoad(dcp_register);
/* 4301 */           this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */           
/* 4303 */           int isNumberLabel = this.cfw.acquireLabel();
/* 4304 */           this.cfw.add(165, isNumberLabel);
/* 4305 */           short stack = this.cfw.getStackTop();
/* 4306 */           this.cfw.addALoad(dcp_register);
/* 4307 */           addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
/*      */ 
/*      */           
/* 4310 */           int beyond = this.cfw.acquireLabel();
/* 4311 */           this.cfw.add(167, beyond);
/* 4312 */           this.cfw.markLabel(isNumberLabel, stack);
/* 4313 */           this.cfw.addPush("number");
/* 4314 */           this.cfw.markLabel(beyond);
/*      */         } else {
/* 4316 */           this.cfw.addALoad(this.varRegisters[varIndex]);
/* 4317 */           addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/* 4324 */     this.cfw.addALoad(this.variableObjectLocal);
/* 4325 */     this.cfw.addPush(node.getString());
/* 4326 */     addScriptRuntimeInvoke("typeofName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/String;");
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
/*      */   private void saveCurrentCodeOffset() {
/* 4338 */     this.savedCodeOffset = this.cfw.getCurrentCodeOffset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addInstructionCount() {
/* 4348 */     int count = this.cfw.getCurrentCodeOffset() - this.savedCodeOffset;
/*      */ 
/*      */ 
/*      */     
/* 4352 */     addInstructionCount(Math.max(count, 1));
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
/*      */   private void addInstructionCount(int count) {
/* 4364 */     this.cfw.addALoad(this.contextLocal);
/* 4365 */     this.cfw.addPush(count);
/* 4366 */     addScriptRuntimeInvoke("addInstructionCount", "(Lorg/mozilla/javascript/Context;I)V");
/*      */   } private void visitIncDec(Node node) {
/*      */     boolean post;
/*      */     int varIndex;
/*      */     short reg;
/*      */     boolean[] constDeclarations;
/*      */     Node getPropChild, elemChild, refChild;
/* 4373 */     int incrDecrMask = node.getExistingIntProp(13);
/* 4374 */     Node child = node.getFirstChild();
/* 4375 */     switch (child.getType()) {
/*      */       case 55:
/* 4377 */         if (!this.hasVarsInRegs) Kit.codeBug(); 
/* 4378 */         post = ((incrDecrMask & 0x2) != 0);
/* 4379 */         varIndex = this.fnCurrent.getVarIndex(child);
/* 4380 */         reg = this.varRegisters[varIndex];
/* 4381 */         constDeclarations = this.fnCurrent.fnode.getParamAndVarConst();
/* 4382 */         if (constDeclarations[varIndex]) {
/* 4383 */           if (node.getIntProp(8, -1) != -1) {
/* 4384 */             int offset = varIsDirectCallParameter(varIndex) ? 1 : 0;
/* 4385 */             this.cfw.addDLoad(reg + offset);
/* 4386 */             if (!post) {
/* 4387 */               this.cfw.addPush(1.0D);
/* 4388 */               if ((incrDecrMask & 0x1) == 0) {
/* 4389 */                 this.cfw.add(99);
/*      */               } else {
/* 4391 */                 this.cfw.add(103);
/*      */               } 
/*      */             } 
/*      */           } else {
/* 4395 */             if (varIsDirectCallParameter(varIndex)) {
/* 4396 */               dcpLoadAsObject(reg);
/*      */             } else {
/* 4398 */               this.cfw.addALoad(reg);
/*      */             } 
/* 4400 */             if (post) {
/* 4401 */               this.cfw.add(89);
/* 4402 */               addObjectToDouble();
/* 4403 */               this.cfw.add(88);
/*      */             } else {
/* 4405 */               addObjectToDouble();
/* 4406 */               this.cfw.addPush(1.0D);
/* 4407 */               if ((incrDecrMask & 0x1) == 0) {
/* 4408 */                 this.cfw.add(99);
/*      */               } else {
/* 4410 */                 this.cfw.add(103);
/*      */               } 
/* 4412 */               addDoubleWrap();
/*      */             }
/*      */           
/*      */           }
/*      */         
/* 4417 */         } else if (node.getIntProp(8, -1) != -1) {
/* 4418 */           int offset = varIsDirectCallParameter(varIndex) ? 1 : 0;
/* 4419 */           this.cfw.addDLoad(reg + offset);
/* 4420 */           if (post) {
/* 4421 */             this.cfw.add(92);
/*      */           }
/* 4423 */           this.cfw.addPush(1.0D);
/* 4424 */           if ((incrDecrMask & 0x1) == 0) {
/* 4425 */             this.cfw.add(99);
/*      */           } else {
/* 4427 */             this.cfw.add(103);
/*      */           } 
/* 4429 */           if (!post) {
/* 4430 */             this.cfw.add(92);
/*      */           }
/* 4432 */           this.cfw.addDStore(reg + offset);
/*      */         } else {
/* 4434 */           if (varIsDirectCallParameter(varIndex)) {
/* 4435 */             dcpLoadAsObject(reg);
/*      */           } else {
/* 4437 */             this.cfw.addALoad(reg);
/*      */           } 
/* 4439 */           if (post) {
/* 4440 */             this.cfw.add(89);
/*      */           }
/* 4442 */           addObjectToDouble();
/* 4443 */           this.cfw.addPush(1.0D);
/* 4444 */           if ((incrDecrMask & 0x1) == 0) {
/* 4445 */             this.cfw.add(99);
/*      */           } else {
/* 4447 */             this.cfw.add(103);
/*      */           } 
/* 4449 */           addDoubleWrap();
/* 4450 */           if (!post) {
/* 4451 */             this.cfw.add(89);
/*      */           }
/* 4453 */           this.cfw.addAStore(reg);
/*      */         } 
/*      */         return;
/*      */       case 39:
/* 4457 */         this.cfw.addALoad(this.variableObjectLocal);
/* 4458 */         this.cfw.addPush(child.getString());
/* 4459 */         this.cfw.addALoad(this.contextLocal);
/* 4460 */         this.cfw.addPush(incrDecrMask);
/* 4461 */         addScriptRuntimeInvoke("nameIncrDecr", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;I)Ljava/lang/Object;");
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 34:
/* 4468 */         throw Kit.codeBug();
/*      */       case 33:
/* 4470 */         getPropChild = child.getFirstChild();
/* 4471 */         generateExpression(getPropChild, node);
/* 4472 */         generateExpression(getPropChild.getNext(), node);
/* 4473 */         this.cfw.addALoad(this.contextLocal);
/* 4474 */         this.cfw.addALoad(this.variableObjectLocal);
/* 4475 */         this.cfw.addPush(incrDecrMask);
/* 4476 */         addScriptRuntimeInvoke("propIncrDecr", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 36:
/* 4485 */         elemChild = child.getFirstChild();
/* 4486 */         generateExpression(elemChild, node);
/* 4487 */         generateExpression(elemChild.getNext(), node);
/* 4488 */         this.cfw.addALoad(this.contextLocal);
/* 4489 */         this.cfw.addALoad(this.variableObjectLocal);
/* 4490 */         this.cfw.addPush(incrDecrMask);
/* 4491 */         if (elemChild.getNext().getIntProp(8, -1) != -1) {
/* 4492 */           addOptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */           
/* 4500 */           addScriptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 67:
/* 4511 */         refChild = child.getFirstChild();
/* 4512 */         generateExpression(refChild, node);
/* 4513 */         this.cfw.addALoad(this.contextLocal);
/* 4514 */         this.cfw.addALoad(this.variableObjectLocal);
/* 4515 */         this.cfw.addPush(incrDecrMask);
/* 4516 */         addScriptRuntimeInvoke("refIncrDecr", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
/*      */         return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4525 */     Codegen.badTree();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isArithmeticNode(Node node) {
/* 4531 */     int type = node.getType();
/* 4532 */     return (type == 22 || type == 25 || type == 24 || type == 23);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitArithmetic(Node node, int opCode, Node child, Node parent) {
/* 4541 */     int childNumberFlag = node.getIntProp(8, -1);
/* 4542 */     if (childNumberFlag != -1) {
/* 4543 */       generateExpression(child, node);
/* 4544 */       generateExpression(child.getNext(), node);
/* 4545 */       this.cfw.add(opCode);
/*      */     } else {
/*      */       
/* 4548 */       boolean childOfArithmetic = isArithmeticNode(parent);
/* 4549 */       generateExpression(child, node);
/* 4550 */       if (!isArithmeticNode(child))
/* 4551 */         addObjectToDouble(); 
/* 4552 */       generateExpression(child.getNext(), node);
/* 4553 */       if (!isArithmeticNode(child.getNext()))
/* 4554 */         addObjectToDouble(); 
/* 4555 */       this.cfw.add(opCode);
/* 4556 */       if (!childOfArithmetic) {
/* 4557 */         addDoubleWrap();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitBitOp(Node node, int type, Node child) {
/* 4564 */     int childNumberFlag = node.getIntProp(8, -1);
/* 4565 */     generateExpression(child, node);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4570 */     if (type == 20) {
/* 4571 */       addScriptRuntimeInvoke("toUint32", "(Ljava/lang/Object;)J");
/* 4572 */       generateExpression(child.getNext(), node);
/* 4573 */       addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
/*      */ 
/*      */       
/* 4576 */       this.cfw.addPush(31);
/* 4577 */       this.cfw.add(126);
/* 4578 */       this.cfw.add(125);
/* 4579 */       this.cfw.add(138);
/* 4580 */       addDoubleWrap();
/*      */       return;
/*      */     } 
/* 4583 */     if (childNumberFlag == -1) {
/* 4584 */       addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
/* 4585 */       generateExpression(child.getNext(), node);
/* 4586 */       addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
/*      */     } else {
/*      */       
/* 4589 */       addScriptRuntimeInvoke("toInt32", "(D)I");
/* 4590 */       generateExpression(child.getNext(), node);
/* 4591 */       addScriptRuntimeInvoke("toInt32", "(D)I");
/*      */     } 
/* 4593 */     switch (type) {
/*      */       case 9:
/* 4595 */         this.cfw.add(128);
/*      */         break;
/*      */       case 10:
/* 4598 */         this.cfw.add(130);
/*      */         break;
/*      */       case 11:
/* 4601 */         this.cfw.add(126);
/*      */         break;
/*      */       case 19:
/* 4604 */         this.cfw.add(122);
/*      */         break;
/*      */       case 18:
/* 4607 */         this.cfw.add(120);
/*      */         break;
/*      */       default:
/* 4610 */         throw Codegen.badTree();
/*      */     } 
/* 4612 */     this.cfw.add(135);
/* 4613 */     if (childNumberFlag == -1) {
/* 4614 */       addDoubleWrap();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private int nodeIsDirectCallParameter(Node node) {
/* 4620 */     if (node.getType() == 55 && this.inDirectCallFunction && !this.itsForcedObjectParameters) {
/*      */ 
/*      */       
/* 4623 */       int varIndex = this.fnCurrent.getVarIndex(node);
/* 4624 */       if (this.fnCurrent.isParameter(varIndex)) {
/* 4625 */         return this.varRegisters[varIndex];
/*      */       }
/*      */     } 
/* 4628 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean varIsDirectCallParameter(int varIndex) {
/* 4633 */     return (this.fnCurrent.isParameter(varIndex) && this.inDirectCallFunction && !this.itsForcedObjectParameters);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void genSimpleCompare(int type, int trueGOTO, int falseGOTO) {
/* 4639 */     if (trueGOTO == -1) throw Codegen.badTree(); 
/* 4640 */     switch (type) {
/*      */       case 15:
/* 4642 */         this.cfw.add(152);
/* 4643 */         this.cfw.add(158, trueGOTO);
/*      */         break;
/*      */       case 17:
/* 4646 */         this.cfw.add(151);
/* 4647 */         this.cfw.add(156, trueGOTO);
/*      */         break;
/*      */       case 14:
/* 4650 */         this.cfw.add(152);
/* 4651 */         this.cfw.add(155, trueGOTO);
/*      */         break;
/*      */       case 16:
/* 4654 */         this.cfw.add(151);
/* 4655 */         this.cfw.add(157, trueGOTO);
/*      */         break;
/*      */       default:
/* 4658 */         throw Codegen.badTree();
/*      */     } 
/*      */     
/* 4661 */     if (falseGOTO != -1) {
/* 4662 */       this.cfw.add(167, falseGOTO);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitIfJumpRelOp(Node node, Node child, int trueGOTO, int falseGOTO) {
/* 4668 */     if (trueGOTO == -1 || falseGOTO == -1) throw Codegen.badTree(); 
/* 4669 */     int type = node.getType();
/* 4670 */     Node rChild = child.getNext();
/* 4671 */     if (type == 53 || type == 52) {
/* 4672 */       generateExpression(child, node);
/* 4673 */       generateExpression(rChild, node);
/* 4674 */       this.cfw.addALoad(this.contextLocal);
/* 4675 */       addScriptRuntimeInvoke((type == 53) ? "instanceOf" : "in", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Z");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4681 */       this.cfw.add(154, trueGOTO);
/* 4682 */       this.cfw.add(167, falseGOTO);
/*      */       return;
/*      */     } 
/* 4685 */     int childNumberFlag = node.getIntProp(8, -1);
/* 4686 */     int left_dcp_register = nodeIsDirectCallParameter(child);
/* 4687 */     int right_dcp_register = nodeIsDirectCallParameter(rChild);
/* 4688 */     if (childNumberFlag != -1) {
/*      */ 
/*      */ 
/*      */       
/* 4692 */       if (childNumberFlag != 2) {
/*      */         
/* 4694 */         generateExpression(child, node);
/* 4695 */       } else if (left_dcp_register != -1) {
/* 4696 */         dcpLoadAsNumber(left_dcp_register);
/*      */       } else {
/* 4698 */         generateExpression(child, node);
/* 4699 */         addObjectToDouble();
/*      */       } 
/*      */       
/* 4702 */       if (childNumberFlag != 1) {
/*      */         
/* 4704 */         generateExpression(rChild, node);
/* 4705 */       } else if (right_dcp_register != -1) {
/* 4706 */         dcpLoadAsNumber(right_dcp_register);
/*      */       } else {
/* 4708 */         generateExpression(rChild, node);
/* 4709 */         addObjectToDouble();
/*      */       } 
/*      */       
/* 4712 */       genSimpleCompare(type, trueGOTO, falseGOTO);
/*      */     } else {
/*      */       
/* 4715 */       if (left_dcp_register != -1 && right_dcp_register != -1) {
/*      */ 
/*      */         
/* 4718 */         short stack = this.cfw.getStackTop();
/* 4719 */         int leftIsNotNumber = this.cfw.acquireLabel();
/* 4720 */         this.cfw.addALoad(left_dcp_register);
/* 4721 */         this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */         
/* 4725 */         this.cfw.add(166, leftIsNotNumber);
/* 4726 */         this.cfw.addDLoad(left_dcp_register + 1);
/* 4727 */         dcpLoadAsNumber(right_dcp_register);
/* 4728 */         genSimpleCompare(type, trueGOTO, falseGOTO);
/* 4729 */         if (stack != this.cfw.getStackTop()) throw Codegen.badTree();
/*      */         
/* 4731 */         this.cfw.markLabel(leftIsNotNumber);
/* 4732 */         int rightIsNotNumber = this.cfw.acquireLabel();
/* 4733 */         this.cfw.addALoad(right_dcp_register);
/* 4734 */         this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */         
/* 4738 */         this.cfw.add(166, rightIsNotNumber);
/* 4739 */         this.cfw.addALoad(left_dcp_register);
/* 4740 */         addObjectToDouble();
/* 4741 */         this.cfw.addDLoad(right_dcp_register + 1);
/* 4742 */         genSimpleCompare(type, trueGOTO, falseGOTO);
/* 4743 */         if (stack != this.cfw.getStackTop()) throw Codegen.badTree();
/*      */         
/* 4745 */         this.cfw.markLabel(rightIsNotNumber);
/*      */         
/* 4747 */         this.cfw.addALoad(left_dcp_register);
/* 4748 */         this.cfw.addALoad(right_dcp_register);
/*      */       } else {
/*      */         
/* 4751 */         generateExpression(child, node);
/* 4752 */         generateExpression(rChild, node);
/*      */       } 
/*      */       
/* 4755 */       if (type == 17 || type == 16) {
/* 4756 */         this.cfw.add(95);
/*      */       }
/* 4758 */       String routine = (type == 14 || type == 16) ? "cmp_LT" : "cmp_LE";
/*      */       
/* 4760 */       addScriptRuntimeInvoke(routine, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
/*      */ 
/*      */ 
/*      */       
/* 4764 */       this.cfw.add(154, trueGOTO);
/* 4765 */       this.cfw.add(167, falseGOTO);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitIfJumpEqOp(Node node, Node child, int trueGOTO, int falseGOTO) {
/* 4772 */     if (trueGOTO == -1 || falseGOTO == -1) throw Codegen.badTree();
/*      */     
/* 4774 */     short stackInitial = this.cfw.getStackTop();
/* 4775 */     int type = node.getType();
/* 4776 */     Node rChild = child.getNext();
/*      */ 
/*      */     
/* 4779 */     if (child.getType() == 42 || rChild.getType() == 42) {
/*      */       
/* 4781 */       if (child.getType() == 42) {
/* 4782 */         child = rChild;
/*      */       }
/* 4784 */       generateExpression(child, node);
/* 4785 */       if (type == 46 || type == 47) {
/* 4786 */         int testCode = (type == 46) ? 198 : 199;
/*      */         
/* 4788 */         this.cfw.add(testCode, trueGOTO);
/*      */       } else {
/* 4790 */         if (type != 12) {
/*      */           
/* 4792 */           if (type != 13) throw Codegen.badTree(); 
/* 4793 */           int tmp = trueGOTO;
/* 4794 */           trueGOTO = falseGOTO;
/* 4795 */           falseGOTO = tmp;
/*      */         } 
/* 4797 */         this.cfw.add(89);
/* 4798 */         int undefCheckLabel = this.cfw.acquireLabel();
/* 4799 */         this.cfw.add(199, undefCheckLabel);
/* 4800 */         short stack = this.cfw.getStackTop();
/* 4801 */         this.cfw.add(87);
/* 4802 */         this.cfw.add(167, trueGOTO);
/* 4803 */         this.cfw.markLabel(undefCheckLabel, stack);
/* 4804 */         Codegen.pushUndefined(this.cfw);
/* 4805 */         this.cfw.add(165, trueGOTO);
/*      */       } 
/* 4807 */       this.cfw.add(167, falseGOTO);
/*      */     } else {
/* 4809 */       String name; int testCode, child_dcp_register = nodeIsDirectCallParameter(child);
/* 4810 */       if (child_dcp_register != -1 && rChild.getType() == 149) {
/*      */ 
/*      */         
/* 4813 */         Node convertChild = rChild.getFirstChild();
/* 4814 */         if (convertChild.getType() == 40) {
/* 4815 */           this.cfw.addALoad(child_dcp_register);
/* 4816 */           this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */           
/* 4820 */           int notNumbersLabel = this.cfw.acquireLabel();
/* 4821 */           this.cfw.add(166, notNumbersLabel);
/* 4822 */           this.cfw.addDLoad(child_dcp_register + 1);
/* 4823 */           this.cfw.addPush(convertChild.getDouble());
/* 4824 */           this.cfw.add(151);
/* 4825 */           if (type == 12) {
/* 4826 */             this.cfw.add(153, trueGOTO);
/*      */           } else {
/* 4828 */             this.cfw.add(154, trueGOTO);
/* 4829 */           }  this.cfw.add(167, falseGOTO);
/* 4830 */           this.cfw.markLabel(notNumbersLabel);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 4835 */       generateExpression(child, node);
/* 4836 */       generateExpression(rChild, node);
/*      */ 
/*      */ 
/*      */       
/* 4840 */       switch (type) {
/*      */         case 12:
/* 4842 */           name = "eq";
/* 4843 */           testCode = 154;
/*      */           break;
/*      */         case 13:
/* 4846 */           name = "eq";
/* 4847 */           testCode = 153;
/*      */           break;
/*      */         case 46:
/* 4850 */           name = "shallowEq";
/* 4851 */           testCode = 154;
/*      */           break;
/*      */         case 47:
/* 4854 */           name = "shallowEq";
/* 4855 */           testCode = 153;
/*      */           break;
/*      */         default:
/* 4858 */           throw Codegen.badTree();
/*      */       } 
/* 4860 */       addScriptRuntimeInvoke(name, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
/*      */ 
/*      */ 
/*      */       
/* 4864 */       this.cfw.add(testCode, trueGOTO);
/* 4865 */       this.cfw.add(167, falseGOTO);
/*      */     } 
/* 4867 */     if (stackInitial != this.cfw.getStackTop()) throw Codegen.badTree();
/*      */   
/*      */   }
/*      */   
/*      */   private void visitSetName(Node node, Node child) {
/* 4872 */     String name = node.getFirstChild().getString();
/* 4873 */     while (child != null) {
/* 4874 */       generateExpression(child, node);
/* 4875 */       child = child.getNext();
/*      */     } 
/* 4877 */     this.cfw.addALoad(this.contextLocal);
/* 4878 */     this.cfw.addALoad(this.variableObjectLocal);
/* 4879 */     this.cfw.addPush(name);
/* 4880 */     addScriptRuntimeInvoke("setName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
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
/*      */   private void visitStrictSetName(Node node, Node child) {
/* 4892 */     String name = node.getFirstChild().getString();
/* 4893 */     while (child != null) {
/* 4894 */       generateExpression(child, node);
/* 4895 */       child = child.getNext();
/*      */     } 
/* 4897 */     this.cfw.addALoad(this.contextLocal);
/* 4898 */     this.cfw.addALoad(this.variableObjectLocal);
/* 4899 */     this.cfw.addPush(name);
/* 4900 */     addScriptRuntimeInvoke("strictSetName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
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
/*      */   private void visitSetConst(Node node, Node child) {
/* 4912 */     String name = node.getFirstChild().getString();
/* 4913 */     while (child != null) {
/* 4914 */       generateExpression(child, node);
/* 4915 */       child = child.getNext();
/*      */     } 
/* 4917 */     this.cfw.addALoad(this.contextLocal);
/* 4918 */     this.cfw.addPush(name);
/* 4919 */     addScriptRuntimeInvoke("setConst", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Ljava/lang/String;)Ljava/lang/Object;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitGetVar(Node node) {
/* 4930 */     if (!this.hasVarsInRegs) Kit.codeBug(); 
/* 4931 */     int varIndex = this.fnCurrent.getVarIndex(node);
/* 4932 */     short reg = this.varRegisters[varIndex];
/* 4933 */     if (varIsDirectCallParameter(varIndex)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4938 */       if (node.getIntProp(8, -1) != -1) {
/* 4939 */         dcpLoadAsNumber(reg);
/*      */       } else {
/* 4941 */         dcpLoadAsObject(reg);
/*      */       } 
/* 4943 */     } else if (this.fnCurrent.isNumberVar(varIndex)) {
/* 4944 */       this.cfw.addDLoad(reg);
/*      */     } else {
/* 4946 */       this.cfw.addALoad(reg);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitSetVar(Node node, Node child, boolean needValue) {
/* 4952 */     if (!this.hasVarsInRegs) Kit.codeBug(); 
/* 4953 */     int varIndex = this.fnCurrent.getVarIndex(node);
/* 4954 */     generateExpression(child.getNext(), node);
/* 4955 */     boolean isNumber = (node.getIntProp(8, -1) != -1);
/* 4956 */     short reg = this.varRegisters[varIndex];
/* 4957 */     boolean[] constDeclarations = this.fnCurrent.fnode.getParamAndVarConst();
/* 4958 */     if (constDeclarations[varIndex]) {
/* 4959 */       if (!needValue) {
/* 4960 */         if (isNumber) {
/* 4961 */           this.cfw.add(88);
/*      */         } else {
/* 4963 */           this.cfw.add(87);
/*      */         } 
/*      */       }
/* 4966 */     } else if (varIsDirectCallParameter(varIndex)) {
/* 4967 */       if (isNumber) {
/* 4968 */         if (needValue) this.cfw.add(92); 
/* 4969 */         this.cfw.addALoad(reg);
/* 4970 */         this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */         
/* 4974 */         int isNumberLabel = this.cfw.acquireLabel();
/* 4975 */         int beyond = this.cfw.acquireLabel();
/* 4976 */         this.cfw.add(165, isNumberLabel);
/* 4977 */         short stack = this.cfw.getStackTop();
/* 4978 */         addDoubleWrap();
/* 4979 */         this.cfw.addAStore(reg);
/* 4980 */         this.cfw.add(167, beyond);
/* 4981 */         this.cfw.markLabel(isNumberLabel, stack);
/* 4982 */         this.cfw.addDStore(reg + 1);
/* 4983 */         this.cfw.markLabel(beyond);
/*      */       } else {
/*      */         
/* 4986 */         if (needValue) this.cfw.add(89); 
/* 4987 */         this.cfw.addAStore(reg);
/*      */       } 
/*      */     } else {
/* 4990 */       boolean isNumberVar = this.fnCurrent.isNumberVar(varIndex);
/* 4991 */       if (isNumber) {
/* 4992 */         if (isNumberVar) {
/* 4993 */           this.cfw.addDStore(reg);
/* 4994 */           if (needValue) this.cfw.addDLoad(reg); 
/*      */         } else {
/* 4996 */           if (needValue) this.cfw.add(92);
/*      */ 
/*      */           
/* 4999 */           addDoubleWrap();
/* 5000 */           this.cfw.addAStore(reg);
/*      */         } 
/*      */       } else {
/* 5003 */         if (isNumberVar) Kit.codeBug(); 
/* 5004 */         this.cfw.addAStore(reg);
/* 5005 */         if (needValue) this.cfw.addALoad(reg);
/*      */       
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void visitSetConstVar(Node node, Node child, boolean needValue) {
/* 5012 */     if (!this.hasVarsInRegs) Kit.codeBug(); 
/* 5013 */     int varIndex = this.fnCurrent.getVarIndex(node);
/* 5014 */     generateExpression(child.getNext(), node);
/* 5015 */     boolean isNumber = (node.getIntProp(8, -1) != -1);
/* 5016 */     short reg = this.varRegisters[varIndex];
/* 5017 */     int beyond = this.cfw.acquireLabel();
/* 5018 */     int noAssign = this.cfw.acquireLabel();
/* 5019 */     if (isNumber) {
/* 5020 */       this.cfw.addILoad(reg + 2);
/* 5021 */       this.cfw.add(154, noAssign);
/* 5022 */       short stack = this.cfw.getStackTop();
/* 5023 */       this.cfw.addPush(1);
/* 5024 */       this.cfw.addIStore(reg + 2);
/* 5025 */       this.cfw.addDStore(reg);
/* 5026 */       if (needValue) {
/* 5027 */         this.cfw.addDLoad(reg);
/* 5028 */         this.cfw.markLabel(noAssign, stack);
/*      */       } else {
/* 5030 */         this.cfw.add(167, beyond);
/* 5031 */         this.cfw.markLabel(noAssign, stack);
/* 5032 */         this.cfw.add(88);
/*      */       } 
/*      */     } else {
/*      */       
/* 5036 */       this.cfw.addILoad(reg + 1);
/* 5037 */       this.cfw.add(154, noAssign);
/* 5038 */       short stack = this.cfw.getStackTop();
/* 5039 */       this.cfw.addPush(1);
/* 5040 */       this.cfw.addIStore(reg + 1);
/* 5041 */       this.cfw.addAStore(reg);
/* 5042 */       if (needValue) {
/* 5043 */         this.cfw.addALoad(reg);
/* 5044 */         this.cfw.markLabel(noAssign, stack);
/*      */       } else {
/* 5046 */         this.cfw.add(167, beyond);
/* 5047 */         this.cfw.markLabel(noAssign, stack);
/* 5048 */         this.cfw.add(87);
/*      */       } 
/*      */     } 
/* 5051 */     this.cfw.markLabel(beyond);
/*      */   }
/*      */ 
/*      */   
/*      */   private void visitGetProp(Node node, Node child) {
/* 5056 */     generateExpression(child, node);
/* 5057 */     Node nameChild = child.getNext();
/* 5058 */     generateExpression(nameChild, node);
/* 5059 */     if (node.getType() == 34) {
/* 5060 */       this.cfw.addALoad(this.contextLocal);
/* 5061 */       this.cfw.addALoad(this.variableObjectLocal);
/* 5062 */       addScriptRuntimeInvoke("getObjectPropNoWarn", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5075 */     int childType = child.getType();
/* 5076 */     if (childType == 43 && nameChild.getType() == 41) {
/* 5077 */       this.cfw.addALoad(this.contextLocal);
/* 5078 */       addScriptRuntimeInvoke("getObjectProp", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 5085 */       this.cfw.addALoad(this.contextLocal);
/* 5086 */       this.cfw.addALoad(this.variableObjectLocal);
/* 5087 */       addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
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
/*      */   private void visitSetProp(int type, Node node, Node child) {
/* 5099 */     Node objectChild = child;
/* 5100 */     generateExpression(child, node);
/* 5101 */     child = child.getNext();
/* 5102 */     if (type == 139) {
/* 5103 */       this.cfw.add(89);
/*      */     }
/* 5105 */     Node nameChild = child;
/* 5106 */     generateExpression(child, node);
/* 5107 */     child = child.getNext();
/* 5108 */     if (type == 139) {
/*      */       
/* 5110 */       this.cfw.add(90);
/*      */ 
/*      */       
/* 5113 */       if (objectChild.getType() == 43 && nameChild.getType() == 41) {
/*      */ 
/*      */         
/* 5116 */         this.cfw.addALoad(this.contextLocal);
/* 5117 */         addScriptRuntimeInvoke("getObjectProp", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 5124 */         this.cfw.addALoad(this.contextLocal);
/* 5125 */         this.cfw.addALoad(this.variableObjectLocal);
/* 5126 */         addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5135 */     generateExpression(child, node);
/* 5136 */     this.cfw.addALoad(this.contextLocal);
/* 5137 */     this.cfw.addALoad(this.variableObjectLocal);
/* 5138 */     addScriptRuntimeInvoke("setObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
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
/*      */   private void visitSetElem(int type, Node node, Node child) {
/* 5150 */     generateExpression(child, node);
/* 5151 */     child = child.getNext();
/* 5152 */     if (type == 140) {
/* 5153 */       this.cfw.add(89);
/*      */     }
/* 5155 */     generateExpression(child, node);
/* 5156 */     child = child.getNext();
/* 5157 */     boolean indexIsNumber = (node.getIntProp(8, -1) != -1);
/* 5158 */     if (type == 140) {
/* 5159 */       if (indexIsNumber) {
/*      */ 
/*      */         
/* 5162 */         this.cfw.add(93);
/* 5163 */         this.cfw.addALoad(this.contextLocal);
/* 5164 */         this.cfw.addALoad(this.variableObjectLocal);
/* 5165 */         addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/* 5174 */         this.cfw.add(90);
/* 5175 */         this.cfw.addALoad(this.contextLocal);
/* 5176 */         this.cfw.addALoad(this.variableObjectLocal);
/* 5177 */         addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5186 */     generateExpression(child, node);
/* 5187 */     this.cfw.addALoad(this.contextLocal);
/* 5188 */     this.cfw.addALoad(this.variableObjectLocal);
/* 5189 */     if (indexIsNumber) {
/* 5190 */       addScriptRuntimeInvoke("setObjectIndex", "(Ljava/lang/Object;DLjava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/* 5199 */       addScriptRuntimeInvoke("setObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
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
/*      */   private void visitDotQuery(Node node, Node child) {
/* 5212 */     updateLineNumber(node);
/* 5213 */     generateExpression(child, node);
/* 5214 */     this.cfw.addALoad(this.variableObjectLocal);
/* 5215 */     addScriptRuntimeInvoke("enterDotQuery", "(Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */ 
/*      */     
/* 5219 */     this.cfw.addAStore(this.variableObjectLocal);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5224 */     this.cfw.add(1);
/* 5225 */     int queryLoopStart = this.cfw.acquireLabel();
/* 5226 */     this.cfw.markLabel(queryLoopStart);
/* 5227 */     this.cfw.add(87);
/*      */     
/* 5229 */     generateExpression(child.getNext(), node);
/* 5230 */     addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
/* 5231 */     this.cfw.addALoad(this.variableObjectLocal);
/* 5232 */     addScriptRuntimeInvoke("updateDotQuery", "(ZLorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
/*      */ 
/*      */ 
/*      */     
/* 5236 */     this.cfw.add(89);
/* 5237 */     this.cfw.add(198, queryLoopStart);
/*      */     
/* 5239 */     this.cfw.addALoad(this.variableObjectLocal);
/* 5240 */     addScriptRuntimeInvoke("leaveDotQuery", "(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
/*      */ 
/*      */     
/* 5243 */     this.cfw.addAStore(this.variableObjectLocal);
/*      */   }
/*      */ 
/*      */   
/*      */   private int getLocalBlockRegister(Node node) {
/* 5248 */     Node localBlock = (Node)node.getProp(3);
/* 5249 */     int localSlot = localBlock.getExistingIntProp(2);
/* 5250 */     return localSlot;
/*      */   }
/*      */ 
/*      */   
/*      */   private void dcpLoadAsNumber(int dcp_register) {
/* 5255 */     this.cfw.addALoad(dcp_register);
/* 5256 */     this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */     
/* 5260 */     int isNumberLabel = this.cfw.acquireLabel();
/* 5261 */     this.cfw.add(165, isNumberLabel);
/* 5262 */     short stack = this.cfw.getStackTop();
/* 5263 */     this.cfw.addALoad(dcp_register);
/* 5264 */     addObjectToDouble();
/* 5265 */     int beyond = this.cfw.acquireLabel();
/* 5266 */     this.cfw.add(167, beyond);
/* 5267 */     this.cfw.markLabel(isNumberLabel, stack);
/* 5268 */     this.cfw.addDLoad(dcp_register + 1);
/* 5269 */     this.cfw.markLabel(beyond);
/*      */   }
/*      */ 
/*      */   
/*      */   private void dcpLoadAsObject(int dcp_register) {
/* 5274 */     this.cfw.addALoad(dcp_register);
/* 5275 */     this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
/*      */ 
/*      */ 
/*      */     
/* 5279 */     int isNumberLabel = this.cfw.acquireLabel();
/* 5280 */     this.cfw.add(165, isNumberLabel);
/* 5281 */     short stack = this.cfw.getStackTop();
/* 5282 */     this.cfw.addALoad(dcp_register);
/* 5283 */     int beyond = this.cfw.acquireLabel();
/* 5284 */     this.cfw.add(167, beyond);
/* 5285 */     this.cfw.markLabel(isNumberLabel, stack);
/* 5286 */     this.cfw.addDLoad(dcp_register + 1);
/* 5287 */     addDoubleWrap();
/* 5288 */     this.cfw.markLabel(beyond);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addGoto(Node target, int jumpcode) {
/* 5293 */     int targetLabel = getTargetLabel(target);
/* 5294 */     this.cfw.add(jumpcode, targetLabel);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addObjectToDouble() {
/* 5299 */     addScriptRuntimeInvoke("toNumber", "(Ljava/lang/Object;)D");
/*      */   }
/*      */ 
/*      */   
/*      */   private void addNewObjectArray(int size) {
/* 5304 */     if (size == 0) {
/* 5305 */       if (this.itsZeroArgArray >= 0) {
/* 5306 */         this.cfw.addALoad(this.itsZeroArgArray);
/*      */       } else {
/* 5308 */         this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 5313 */       this.cfw.addPush(size);
/* 5314 */       this.cfw.add(189, "java/lang/Object");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addScriptRuntimeInvoke(String methodName, String methodSignature) {
/* 5321 */     this.cfw.addInvoke(184, "org.mozilla.javascript.ScriptRuntime", methodName, methodSignature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addOptRuntimeInvoke(String methodName, String methodSignature) {
/* 5330 */     this.cfw.addInvoke(184, "org/mozilla/javascript/optimizer/OptRuntime", methodName, methodSignature);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addJumpedBooleanWrap(int trueLabel, int falseLabel) {
/* 5338 */     this.cfw.markLabel(falseLabel);
/* 5339 */     int skip = this.cfw.acquireLabel();
/* 5340 */     this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
/*      */     
/* 5342 */     this.cfw.add(167, skip);
/* 5343 */     this.cfw.markLabel(trueLabel);
/* 5344 */     this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
/*      */     
/* 5346 */     this.cfw.markLabel(skip);
/* 5347 */     this.cfw.adjustStackTop(-1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addDoubleWrap() {
/* 5352 */     addOptRuntimeInvoke("wrapDouble", "(D)Ljava/lang/Double;");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private short getNewWordPairLocal(boolean isConst) {
/* 5363 */     return getNewWordIntern(isConst ? 3 : 2);
/*      */   }
/*      */ 
/*      */   
/*      */   private short getNewWordLocal(boolean isConst) {
/* 5368 */     return getNewWordIntern(isConst ? 2 : 1);
/*      */   }
/*      */ 
/*      */   
/*      */   private short getNewWordLocal() {
/* 5373 */     return getNewWordIntern(1);
/*      */   }
/*      */ 
/*      */   
/*      */   private short getNewWordIntern(int count) {
/* 5378 */     assert count >= 1 && count <= 3;
/*      */     
/* 5380 */     int[] locals = this.locals;
/* 5381 */     int result = -1;
/* 5382 */     if (count > 1) {
/*      */       int i;
/* 5384 */       label41: for (i = this.firstFreeLocal; i + count <= 1024; ) {
/* 5385 */         for (int j = 0; j < count; j++) {
/* 5386 */           if (locals[i + j] != 0) {
/* 5387 */             i += j + 1;
/*      */             continue label41;
/*      */           } 
/*      */         } 
/* 5391 */         result = i;
/*      */       } 
/*      */     } else {
/*      */       
/* 5395 */       result = this.firstFreeLocal;
/*      */     } 
/*      */     
/* 5398 */     if (result != -1) {
/* 5399 */       locals[result] = 1;
/* 5400 */       if (count > 1)
/* 5401 */         locals[result + 1] = 1; 
/* 5402 */       if (count > 2) {
/* 5403 */         locals[result + 2] = 1;
/*      */       }
/* 5405 */       if (result == this.firstFreeLocal) {
/* 5406 */         for (int i = result + count; i < 1024; i++) {
/* 5407 */           if (locals[i] == 0) {
/* 5408 */             this.firstFreeLocal = (short)i;
/* 5409 */             if (this.localsMax < this.firstFreeLocal)
/* 5410 */               this.localsMax = this.firstFreeLocal; 
/* 5411 */             return (short)result;
/*      */           } 
/*      */         } 
/*      */       } else {
/* 5415 */         return (short)result;
/*      */       } 
/*      */     } 
/*      */     
/* 5419 */     throw Context.reportRuntimeError("Program too complex (out of locals)");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void incReferenceWordLocal(short local) {
/* 5425 */     this.locals[local] = this.locals[local] + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void decReferenceWordLocal(short local) {
/* 5431 */     this.locals[local] = this.locals[local] - 1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void releaseWordLocal(short local) {
/* 5436 */     if (local < this.firstFreeLocal)
/* 5437 */       this.firstFreeLocal = local; 
/* 5438 */     this.locals[local] = 0;
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
/* 5485 */   private int maxLocals = 0;
/* 5486 */   private int maxStack = 0;
/*      */   private Map<Node, FinallyReturnPoint> finallys;
/*      */   private List<Node> literals;
/*      */   
/*      */   static class FinallyReturnPoint
/*      */   {
/* 5492 */     public List<Integer> jsrPoints = new ArrayList<Integer>();
/* 5493 */     public int tableLabel = 0;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\optimizer\BodyCodegen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */