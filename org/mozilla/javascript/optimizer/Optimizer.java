/*     */ package org.mozilla.javascript.optimizer;
/*     */ 
/*     */ import org.mozilla.javascript.Node;
/*     */ import org.mozilla.javascript.ObjArray;
/*     */ import org.mozilla.javascript.ast.ScriptNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Optimizer
/*     */ {
/*     */   static final int NoType = 0;
/*     */   static final int NumberType = 1;
/*     */   static final int AnyType = 3;
/*     */   private boolean inDirectCallFunction;
/*     */   OptFunctionNode theFunction;
/*     */   private boolean parameterUsedInNumberContext;
/*     */   
/*     */   void optimize(ScriptNode scriptOrFn) {
/*  24 */     int functionCount = scriptOrFn.getFunctionCount();
/*  25 */     for (int i = 0; i != functionCount; i++) {
/*  26 */       OptFunctionNode f = OptFunctionNode.get(scriptOrFn, i);
/*  27 */       optimizeFunction(f);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void optimizeFunction(OptFunctionNode theFunction) {
/*  33 */     if (theFunction.fnode.requiresActivation())
/*     */       return; 
/*  35 */     this.inDirectCallFunction = theFunction.isTargetOfDirectCall();
/*  36 */     this.theFunction = theFunction;
/*     */     
/*  38 */     ObjArray statementsArray = new ObjArray();
/*  39 */     buildStatementList_r((Node)theFunction.fnode, statementsArray);
/*  40 */     Node[] theStatementNodes = new Node[statementsArray.size()];
/*  41 */     statementsArray.toArray((Object[])theStatementNodes);
/*     */     
/*  43 */     Block.runFlowAnalyzes(theFunction, theStatementNodes);
/*     */     
/*  45 */     if (!theFunction.fnode.requiresActivation()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  53 */       this.parameterUsedInNumberContext = false;
/*  54 */       for (Node theStatementNode : theStatementNodes) {
/*  55 */         rewriteForNumberVariables(theStatementNode, 1);
/*     */       }
/*  57 */       theFunction.setParameterNumberContext(this.parameterUsedInNumberContext);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void markDCPNumberContext(Node n) {
/*  96 */     if (this.inDirectCallFunction && n.getType() == 55) {
/*  97 */       int varIndex = this.theFunction.getVarIndex(n);
/*  98 */       if (this.theFunction.isParameter(varIndex)) {
/*  99 */         this.parameterUsedInNumberContext = true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean convertParameter(Node n) {
/* 106 */     if (this.inDirectCallFunction && n.getType() == 55) {
/* 107 */       int varIndex = this.theFunction.getVarIndex(n);
/* 108 */       if (this.theFunction.isParameter(varIndex)) {
/* 109 */         n.removeProp(8);
/* 110 */         return true;
/*     */       } 
/*     */     } 
/* 113 */     return false; } private int rewriteForNumberVariables(Node n, int desired) { Node node2; int varIndex; Node node1, lChild, arrayBase, child; int type; Node rChild, arrayIndex;
/*     */     OptFunctionNode target;
/*     */     int rType, lType;
/*     */     Node rValue;
/*     */     int baseType, k, j, i, indexType, m, rValueType;
/* 118 */     switch (n.getType()) {
/*     */       case 133:
/* 120 */         node2 = n.getFirstChild();
/* 121 */         type = rewriteForNumberVariables(node2, 1);
/* 122 */         if (type == 1)
/* 123 */           n.putIntProp(8, 0); 
/* 124 */         return 0;
/*     */       
/*     */       case 40:
/* 127 */         n.putIntProp(8, 0);
/* 128 */         return 1;
/*     */ 
/*     */       
/*     */       case 55:
/* 132 */         varIndex = this.theFunction.getVarIndex(n);
/* 133 */         if (this.inDirectCallFunction && this.theFunction.isParameter(varIndex) && desired == 1) {
/*     */ 
/*     */ 
/*     */           
/* 137 */           n.putIntProp(8, 0);
/* 138 */           return 1;
/*     */         } 
/* 140 */         if (this.theFunction.isNumberVar(varIndex)) {
/* 141 */           n.putIntProp(8, 0);
/* 142 */           return 1;
/*     */         } 
/* 144 */         return 0;
/*     */ 
/*     */       
/*     */       case 106:
/*     */       case 107:
/* 149 */         node1 = n.getFirstChild();
/* 150 */         type = rewriteForNumberVariables(node1, 1);
/* 151 */         if (node1.getType() == 55) {
/* 152 */           if (type == 1 && !convertParameter(node1)) {
/*     */             
/* 154 */             n.putIntProp(8, 0);
/* 155 */             markDCPNumberContext(node1);
/* 156 */             return 1;
/*     */           } 
/* 158 */           return 0;
/*     */         } 
/* 160 */         if (node1.getType() == 36 || node1.getType() == 33)
/*     */         {
/* 162 */           return type;
/*     */         }
/* 164 */         return 0;
/*     */       
/*     */       case 56:
/*     */       case 156:
/* 168 */         lChild = n.getFirstChild();
/* 169 */         rChild = lChild.getNext();
/* 170 */         rType = rewriteForNumberVariables(rChild, 1);
/* 171 */         k = this.theFunction.getVarIndex(n);
/* 172 */         if (this.inDirectCallFunction && this.theFunction.isParameter(k)) {
/*     */ 
/*     */           
/* 175 */           if (rType == 1) {
/* 176 */             if (!convertParameter(rChild)) {
/* 177 */               n.putIntProp(8, 0);
/* 178 */               return 1;
/*     */             } 
/* 180 */             markDCPNumberContext(rChild);
/* 181 */             return 0;
/*     */           } 
/*     */           
/* 184 */           return rType;
/*     */         } 
/* 186 */         if (this.theFunction.isNumberVar(k)) {
/* 187 */           if (rType != 1) {
/* 188 */             n.removeChild(rChild);
/* 189 */             n.addChildToBack(new Node(150, rChild));
/*     */           } 
/*     */           
/* 192 */           n.putIntProp(8, 0);
/* 193 */           markDCPNumberContext(rChild);
/* 194 */           return 1;
/*     */         } 
/*     */         
/* 197 */         if (rType == 1 && 
/* 198 */           !convertParameter(rChild)) {
/* 199 */           n.removeChild(rChild);
/* 200 */           n.addChildToBack(new Node(149, rChild));
/*     */         } 
/*     */ 
/*     */         
/* 204 */         return 0;
/*     */ 
/*     */       
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/* 211 */         lChild = n.getFirstChild();
/* 212 */         rChild = lChild.getNext();
/* 213 */         lType = rewriteForNumberVariables(lChild, 1);
/* 214 */         j = rewriteForNumberVariables(rChild, 1);
/* 215 */         markDCPNumberContext(lChild);
/* 216 */         markDCPNumberContext(rChild);
/*     */         
/* 218 */         if (convertParameter(lChild)) {
/* 219 */           if (convertParameter(rChild))
/* 220 */             return 0; 
/* 221 */           if (j == 1) {
/* 222 */             n.putIntProp(8, 2);
/*     */           }
/*     */         }
/* 225 */         else if (convertParameter(rChild)) {
/* 226 */           if (lType == 1) {
/* 227 */             n.putIntProp(8, 1);
/*     */           
/*     */           }
/*     */         }
/* 231 */         else if (lType == 1) {
/* 232 */           if (j == 1) {
/* 233 */             n.putIntProp(8, 0);
/*     */           } else {
/*     */             
/* 236 */             n.putIntProp(8, 1);
/*     */           }
/*     */         
/*     */         }
/* 240 */         else if (j == 1) {
/* 241 */           n.putIntProp(8, 2);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 246 */         return 0;
/*     */ 
/*     */       
/*     */       case 21:
/* 250 */         lChild = n.getFirstChild();
/* 251 */         rChild = lChild.getNext();
/* 252 */         lType = rewriteForNumberVariables(lChild, 1);
/* 253 */         j = rewriteForNumberVariables(rChild, 1);
/*     */ 
/*     */         
/* 256 */         if (convertParameter(lChild)) {
/* 257 */           if (convertParameter(rChild)) {
/* 258 */             return 0;
/*     */           }
/*     */           
/* 261 */           if (j == 1) {
/* 262 */             n.putIntProp(8, 2);
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 267 */         else if (convertParameter(rChild)) {
/* 268 */           if (lType == 1) {
/* 269 */             n.putIntProp(8, 1);
/*     */           
/*     */           }
/*     */         }
/* 273 */         else if (lType == 1) {
/* 274 */           if (j == 1) {
/* 275 */             n.putIntProp(8, 0);
/* 276 */             return 1;
/*     */           } 
/*     */           
/* 279 */           n.putIntProp(8, 1);
/*     */ 
/*     */         
/*     */         }
/* 283 */         else if (j == 1) {
/* 284 */           n.putIntProp(8, 2);
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 290 */         return 0;
/*     */ 
/*     */       
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 18:
/*     */       case 19:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/* 302 */         lChild = n.getFirstChild();
/* 303 */         rChild = lChild.getNext();
/* 304 */         lType = rewriteForNumberVariables(lChild, 1);
/* 305 */         j = rewriteForNumberVariables(rChild, 1);
/* 306 */         markDCPNumberContext(lChild);
/* 307 */         markDCPNumberContext(rChild);
/* 308 */         if (lType == 1) {
/* 309 */           if (j == 1) {
/* 310 */             n.putIntProp(8, 0);
/* 311 */             return 1;
/*     */           } 
/*     */           
/* 314 */           if (!convertParameter(rChild)) {
/* 315 */             n.removeChild(rChild);
/* 316 */             n.addChildToBack(new Node(150, rChild));
/*     */             
/* 318 */             n.putIntProp(8, 0);
/*     */           } 
/* 320 */           return 1;
/*     */         } 
/*     */ 
/*     */         
/* 324 */         if (j == 1) {
/* 325 */           if (!convertParameter(lChild)) {
/* 326 */             n.removeChild(lChild);
/* 327 */             n.addChildToFront(new Node(150, lChild));
/*     */             
/* 329 */             n.putIntProp(8, 0);
/*     */           } 
/* 331 */           return 1;
/*     */         } 
/*     */         
/* 334 */         if (!convertParameter(lChild)) {
/* 335 */           n.removeChild(lChild);
/* 336 */           n.addChildToFront(new Node(150, lChild));
/*     */         } 
/*     */         
/* 339 */         if (!convertParameter(rChild)) {
/* 340 */           n.removeChild(rChild);
/* 341 */           n.addChildToBack(new Node(150, rChild));
/*     */         } 
/*     */         
/* 344 */         n.putIntProp(8, 0);
/* 345 */         return 1;
/*     */ 
/*     */ 
/*     */       
/*     */       case 37:
/*     */       case 140:
/* 351 */         arrayBase = n.getFirstChild();
/* 352 */         arrayIndex = arrayBase.getNext();
/* 353 */         rValue = arrayIndex.getNext();
/* 354 */         i = rewriteForNumberVariables(arrayBase, 1);
/* 355 */         if (i == 1 && 
/* 356 */           !convertParameter(arrayBase)) {
/* 357 */           n.removeChild(arrayBase);
/* 358 */           n.addChildToFront(new Node(149, arrayBase));
/*     */         } 
/*     */ 
/*     */         
/* 362 */         m = rewriteForNumberVariables(arrayIndex, 1);
/* 363 */         if (m == 1 && 
/* 364 */           !convertParameter(arrayIndex))
/*     */         {
/*     */ 
/*     */           
/* 368 */           n.putIntProp(8, 1);
/*     */         }
/*     */         
/* 371 */         rValueType = rewriteForNumberVariables(rValue, 1);
/* 372 */         if (rValueType == 1 && 
/* 373 */           !convertParameter(rValue)) {
/* 374 */           n.removeChild(rValue);
/* 375 */           n.addChildToBack(new Node(149, rValue));
/*     */         } 
/*     */ 
/*     */         
/* 379 */         return 0;
/*     */       
/*     */       case 36:
/* 382 */         arrayBase = n.getFirstChild();
/* 383 */         arrayIndex = arrayBase.getNext();
/* 384 */         baseType = rewriteForNumberVariables(arrayBase, 1);
/* 385 */         if (baseType == 1 && 
/* 386 */           !convertParameter(arrayBase)) {
/* 387 */           n.removeChild(arrayBase);
/* 388 */           n.addChildToFront(new Node(149, arrayBase));
/*     */         } 
/*     */ 
/*     */         
/* 392 */         indexType = rewriteForNumberVariables(arrayIndex, 1);
/* 393 */         if (indexType == 1 && 
/* 394 */           !convertParameter(arrayIndex))
/*     */         {
/*     */ 
/*     */           
/* 398 */           n.putIntProp(8, 2);
/*     */         }
/*     */         
/* 401 */         return 0;
/*     */ 
/*     */       
/*     */       case 38:
/* 405 */         child = n.getFirstChild();
/*     */         
/* 407 */         rewriteAsObjectChildren(child, child.getFirstChild());
/* 408 */         child = child.getNext();
/*     */         
/* 410 */         target = (OptFunctionNode)n.getProp(9);
/*     */         
/* 412 */         if (target != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 417 */           while (child != null) {
/* 418 */             int i1 = rewriteForNumberVariables(child, 1);
/* 419 */             if (i1 == 1) {
/* 420 */               markDCPNumberContext(child);
/*     */             }
/* 422 */             child = child.getNext();
/*     */           } 
/*     */         } else {
/* 425 */           rewriteAsObjectChildren(n, child);
/*     */         } 
/* 427 */         return 0;
/*     */     } 
/*     */     
/* 430 */     rewriteAsObjectChildren(n, n.getFirstChild());
/* 431 */     return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void rewriteAsObjectChildren(Node n, Node child) {
/* 439 */     while (child != null) {
/* 440 */       Node nextChild = child.getNext();
/* 441 */       int type = rewriteForNumberVariables(child, 0);
/* 442 */       if (type == 1 && 
/* 443 */         !convertParameter(child)) {
/* 444 */         n.removeChild(child);
/* 445 */         Node nuChild = new Node(149, child);
/* 446 */         if (nextChild == null) {
/* 447 */           n.addChildToBack(nuChild);
/*     */         } else {
/* 449 */           n.addChildBefore(nuChild, nextChild);
/*     */         } 
/*     */       } 
/* 452 */       child = nextChild;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void buildStatementList_r(Node node, ObjArray statements) {
/* 458 */     int type = node.getType();
/* 459 */     if (type == 129 || type == 141 || type == 132 || type == 109) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 464 */       Node child = node.getFirstChild();
/* 465 */       while (child != null) {
/* 466 */         buildStatementList_r(child, statements);
/* 467 */         child = child.getNext();
/*     */       } 
/*     */     } else {
/* 470 */       statements.add(node);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\optimizer\Optimizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */