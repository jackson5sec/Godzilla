/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.FunctionNode;
/*     */ import org.mozilla.javascript.ast.Jump;
/*     */ import org.mozilla.javascript.ast.Scope;
/*     */ import org.mozilla.javascript.ast.ScriptNode;
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
/*     */ public class NodeTransformer
/*     */ {
/*     */   private ObjArray loops;
/*     */   private ObjArray loopEnds;
/*     */   private boolean hasFinally;
/*     */   
/*     */   public final void transform(ScriptNode tree) {
/*  34 */     transformCompilationUnit(tree);
/*  35 */     for (int i = 0; i != tree.getFunctionCount(); i++) {
/*  36 */       FunctionNode fn = tree.getFunctionNode(i);
/*  37 */       transform((ScriptNode)fn);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void transformCompilationUnit(ScriptNode tree) {
/*  43 */     this.loops = new ObjArray();
/*  44 */     this.loopEnds = new ObjArray();
/*     */ 
/*     */     
/*  47 */     this.hasFinally = false;
/*     */ 
/*     */     
/*  50 */     boolean createScopeObjects = (tree.getType() != 109 || ((FunctionNode)tree).requiresActivation());
/*     */     
/*  52 */     tree.flattenSymbolTable(!createScopeObjects);
/*     */ 
/*     */ 
/*     */     
/*  56 */     boolean inStrictMode = (tree instanceof AstRoot && ((AstRoot)tree).isInStrictMode());
/*     */     
/*  58 */     transformCompilationUnit_r(tree, (Node)tree, (Scope)tree, createScopeObjects, inStrictMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void transformCompilationUnit_r(ScriptNode tree, Node parent, Scope scope, boolean createScopeObjects, boolean inStrictMode) {
/*  68 */     Node node = null; while (true) {
/*     */       Node leave; Jump jump1; boolean isGenerator; Jump jump; Node node1, result; Scope defining; Node child, nameSource, finallytarget, unwindBlock; Jump jumpStatement; Node cursor; String name; int i;
/*     */       Scope scope1;
/*  71 */       Node previous = null;
/*  72 */       if (node == null) {
/*  73 */         node = parent.getFirstChild();
/*     */       } else {
/*  75 */         previous = node;
/*  76 */         node = node.getNext();
/*     */       } 
/*  78 */       if (node == null) {
/*     */         break;
/*     */       }
/*     */       
/*  82 */       int type = node.getType();
/*  83 */       if (createScopeObjects && (type == 129 || type == 132 || type == 157) && node instanceof Scope) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  88 */         Scope newScope = (Scope)node;
/*  89 */         if (newScope.getSymbolTable() != null) {
/*     */ 
/*     */           
/*  92 */           Node let = new Node((type == 157) ? 158 : 153);
/*     */           
/*  94 */           Node innerLet = new Node(153);
/*  95 */           let.addChildToBack(innerLet);
/*  96 */           for (String str : newScope.getSymbolTable().keySet()) {
/*  97 */             innerLet.addChildToBack(Node.newString(39, str));
/*     */           }
/*  99 */           newScope.setSymbolTable(null);
/* 100 */           Node oldNode = node;
/* 101 */           node = replaceCurrent(parent, previous, node, let);
/* 102 */           type = node.getType();
/* 103 */           let.addChildToBack(oldNode);
/*     */         } 
/*     */       } 
/*     */       
/* 107 */       switch (type) {
/*     */         
/*     */         case 114:
/*     */         case 130:
/*     */         case 132:
/* 112 */           this.loops.push(node);
/* 113 */           this.loopEnds.push(((Jump)node).target);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 123:
/* 118 */           this.loops.push(node);
/* 119 */           leave = node.getNext();
/* 120 */           if (leave.getType() != 3) {
/* 121 */             Kit.codeBug();
/*     */           }
/* 123 */           this.loopEnds.push(leave);
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 81:
/* 129 */           jump1 = (Jump)node;
/* 130 */           finallytarget = jump1.getFinally();
/* 131 */           if (finallytarget != null) {
/* 132 */             this.hasFinally = true;
/* 133 */             this.loops.push(node);
/* 134 */             this.loopEnds.push(finallytarget);
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case 3:
/*     */         case 131:
/* 141 */           if (!this.loopEnds.isEmpty() && this.loopEnds.peek() == node) {
/* 142 */             this.loopEnds.pop();
/* 143 */             this.loops.pop();
/*     */           } 
/*     */           break;
/*     */         
/*     */         case 72:
/* 148 */           ((FunctionNode)tree).addResumptionPoint(node);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 4:
/* 153 */           isGenerator = (tree.getType() == 109 && ((FunctionNode)tree).isGenerator());
/*     */           
/* 155 */           if (isGenerator) {
/* 156 */             node.putIntProp(20, 1);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 165 */           if (!this.hasFinally)
/*     */             break; 
/* 167 */           unwindBlock = null;
/* 168 */           for (i = this.loops.size() - 1; i >= 0; i--) {
/* 169 */             Node n = (Node)this.loops.get(i);
/* 170 */             int elemtype = n.getType();
/* 171 */             if (elemtype == 81 || elemtype == 123) {
/*     */               Node unwind;
/* 173 */               if (elemtype == 81) {
/* 174 */                 Jump jsrnode = new Jump(135);
/* 175 */                 Node jsrtarget = ((Jump)n).getFinally();
/* 176 */                 jsrnode.target = jsrtarget;
/* 177 */                 Jump jump2 = jsrnode;
/*     */               } else {
/* 179 */                 unwind = new Node(3);
/*     */               } 
/* 181 */               if (unwindBlock == null) {
/* 182 */                 unwindBlock = new Node(129, node.getLineno());
/*     */               }
/*     */               
/* 185 */               unwindBlock.addChildToBack(unwind);
/*     */             } 
/*     */           } 
/* 188 */           if (unwindBlock != null) {
/* 189 */             Node returnNode = node;
/* 190 */             Node returnExpr = returnNode.getFirstChild();
/* 191 */             node = replaceCurrent(parent, previous, node, unwindBlock);
/* 192 */             if (returnExpr == null || isGenerator) {
/* 193 */               unwindBlock.addChildToBack(returnNode); continue;
/*     */             } 
/* 195 */             Node store = new Node(134, returnExpr);
/* 196 */             unwindBlock.addChildToFront(store);
/* 197 */             returnNode = new Node(64);
/* 198 */             unwindBlock.addChildToBack(returnNode);
/*     */             
/* 200 */             transformCompilationUnit_r(tree, store, scope, createScopeObjects, inStrictMode);
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 120:
/*     */         case 121:
/* 213 */           jump = (Jump)node;
/* 214 */           jumpStatement = jump.getJumpStatement();
/* 215 */           if (jumpStatement == null) Kit.codeBug();
/*     */           
/* 217 */           i = this.loops.size(); while (true) {
/* 218 */             if (i == 0)
/*     */             {
/*     */ 
/*     */               
/* 222 */               throw Kit.codeBug();
/*     */             }
/* 224 */             i--;
/* 225 */             Node n = (Node)this.loops.get(i);
/* 226 */             if (n == jumpStatement) {
/*     */               break;
/*     */             }
/*     */             
/* 230 */             int elemtype = n.getType();
/* 231 */             if (elemtype == 123) {
/* 232 */               Node node2 = new Node(3);
/* 233 */               previous = addBeforeCurrent(parent, previous, node, node2); continue;
/*     */             } 
/* 235 */             if (elemtype == 81) {
/* 236 */               Jump tryNode = (Jump)n;
/* 237 */               Jump jsrFinally = new Jump(135);
/* 238 */               jsrFinally.target = tryNode.getFinally();
/* 239 */               previous = addBeforeCurrent(parent, previous, node, (Node)jsrFinally);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 244 */           if (type == 120) {
/* 245 */             jump.target = jumpStatement.target;
/*     */           } else {
/* 247 */             jump.target = jumpStatement.getContinue();
/*     */           } 
/* 249 */           jump.setType(5);
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 38:
/* 255 */           visitCall(node, tree);
/*     */           break;
/*     */         
/*     */         case 30:
/* 259 */           visitNew(node, tree);
/*     */           break;
/*     */         
/*     */         case 153:
/*     */         case 158:
/* 264 */           node1 = node.getFirstChild();
/* 265 */           if (node1.getType() == 153) {
/*     */ 
/*     */             
/* 268 */             boolean createWith = (tree.getType() != 109 || ((FunctionNode)tree).requiresActivation());
/*     */             
/* 270 */             node = visitLet(createWith, parent, previous, node);
/*     */             break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 122:
/*     */         case 154:
/* 280 */           result = new Node(129);
/* 281 */           for (cursor = node.getFirstChild(); cursor != null; ) {
/*     */ 
/*     */             
/* 284 */             Node n = cursor;
/* 285 */             cursor = cursor.getNext();
/* 286 */             if (n.getType() == 39) {
/* 287 */               if (!n.hasChildren())
/*     */                 continue; 
/* 289 */               Node init = n.getFirstChild();
/* 290 */               n.removeChild(init);
/* 291 */               n.setType(49);
/* 292 */               n = new Node((type == 154) ? 155 : 8, n, init);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/* 299 */             else if (n.getType() != 158) {
/* 300 */               throw Kit.codeBug();
/*     */             } 
/* 302 */             Node pop = new Node(133, n, node.getLineno());
/* 303 */             result.addChildToBack(pop);
/*     */           } 
/* 305 */           node = replaceCurrent(parent, previous, node, result);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 137:
/* 310 */           defining = scope.getDefiningScope(node.getString());
/* 311 */           if (defining != null) {
/* 312 */             node.setScope(defining);
/*     */           }
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 7:
/*     */         case 32:
/* 323 */           child = node.getFirstChild();
/* 324 */           if (type == 7) {
/* 325 */             while (child.getType() == 26) {
/* 326 */               child = child.getFirstChild();
/*     */             }
/* 328 */             if (child.getType() == 12 || child.getType() == 13) {
/*     */ 
/*     */               
/* 331 */               Node first = child.getFirstChild();
/* 332 */               Node last = child.getLastChild();
/* 333 */               if (first.getType() == 39 && first.getString().equals("undefined")) {
/*     */                 
/* 335 */                 child = last;
/* 336 */               } else if (last.getType() == 39 && last.getString().equals("undefined")) {
/*     */                 
/* 338 */                 child = first;
/*     */               } 
/*     */             } 
/* 341 */           }  if (child.getType() == 33) {
/* 342 */             child.setType(34);
/*     */           }
/*     */           break;
/*     */         
/*     */         case 8:
/* 347 */           if (inStrictMode) {
/* 348 */             node.setType(73);
/*     */           }
/*     */ 
/*     */ 
/*     */         
/*     */         case 31:
/*     */         case 39:
/*     */         case 155:
/* 356 */           if (createScopeObjects) {
/*     */             break;
/*     */           }
/*     */           
/* 360 */           if (type == 39) {
/* 361 */             nameSource = node;
/*     */           } else {
/* 363 */             nameSource = node.getFirstChild();
/* 364 */             if (nameSource.getType() != 49) {
/* 365 */               if (type == 31) {
/*     */                 break;
/*     */               }
/* 368 */               throw Kit.codeBug();
/*     */             } 
/*     */           } 
/* 371 */           if (nameSource.getScope() != null) {
/*     */             break;
/*     */           }
/* 374 */           name = nameSource.getString();
/* 375 */           scope1 = scope.getDefiningScope(name);
/* 376 */           if (scope1 != null) {
/* 377 */             nameSource.setScope(scope1);
/* 378 */             if (type == 39) {
/* 379 */               node.setType(55); break;
/* 380 */             }  if (type == 8 || type == 73) {
/*     */               
/* 382 */               node.setType(56);
/* 383 */               nameSource.setType(41); break;
/* 384 */             }  if (type == 155) {
/* 385 */               node.setType(156);
/* 386 */               nameSource.setType(41); break;
/* 387 */             }  if (type == 31) {
/*     */               
/* 389 */               Node n = new Node(44);
/* 390 */               node = replaceCurrent(parent, previous, node, n); break;
/*     */             } 
/* 392 */             throw Kit.codeBug();
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 399 */       transformCompilationUnit_r(tree, node, (node instanceof Scope) ? (Scope)node : scope, createScopeObjects, inStrictMode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitNew(Node node, ScriptNode tree) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitCall(Node node, ScriptNode tree) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node visitLet(boolean createWith, Node parent, Node previous, Node scopeNode) {
/* 414 */     Node result, vars = scopeNode.getFirstChild();
/* 415 */     Node body = vars.getNext();
/* 416 */     scopeNode.removeChild(vars);
/* 417 */     scopeNode.removeChild(body);
/* 418 */     boolean isExpression = (scopeNode.getType() == 158);
/*     */ 
/*     */     
/* 421 */     if (createWith) {
/* 422 */       result = new Node(isExpression ? 159 : 129);
/* 423 */       result = replaceCurrent(parent, previous, scopeNode, result);
/* 424 */       ArrayList<Object> list = new ArrayList();
/* 425 */       Node objectLiteral = new Node(66);
/* 426 */       for (Node v = vars.getFirstChild(); v != null; v = v.getNext()) {
/* 427 */         Node current = v;
/* 428 */         if (current.getType() == 158) {
/*     */           
/* 430 */           List<?> destructuringNames = (List)current.getProp(22);
/*     */           
/* 432 */           Node c = current.getFirstChild();
/* 433 */           if (c.getType() != 153) throw Kit.codeBug();
/*     */           
/* 435 */           if (isExpression) {
/* 436 */             body = new Node(89, c.getNext(), body);
/*     */           } else {
/* 438 */             body = new Node(129, new Node(133, c.getNext()), body);
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 444 */           if (destructuringNames != null) {
/* 445 */             list.addAll(destructuringNames);
/* 446 */             for (int i = 0; i < destructuringNames.size(); i++) {
/* 447 */               objectLiteral.addChildToBack(new Node(126, Node.newNumber(0.0D)));
/*     */             }
/*     */           } 
/*     */           
/* 451 */           current = c.getFirstChild();
/*     */         } 
/* 453 */         if (current.getType() != 39) throw Kit.codeBug(); 
/* 454 */         list.add(ScriptRuntime.getIndexObject(current.getString()));
/* 455 */         Node init = current.getFirstChild();
/* 456 */         if (init == null) {
/* 457 */           init = new Node(126, Node.newNumber(0.0D));
/*     */         }
/* 459 */         objectLiteral.addChildToBack(init);
/*     */       } 
/* 461 */       objectLiteral.putProp(12, list.toArray());
/* 462 */       Node newVars = new Node(2, objectLiteral);
/* 463 */       result.addChildToBack(newVars);
/* 464 */       result.addChildToBack(new Node(123, body));
/* 465 */       result.addChildToBack(new Node(3));
/*     */     } else {
/* 467 */       result = new Node(isExpression ? 89 : 129);
/* 468 */       result = replaceCurrent(parent, previous, scopeNode, result);
/* 469 */       Node newVars = new Node(89);
/* 470 */       for (Node v = vars.getFirstChild(); v != null; v = v.getNext()) {
/* 471 */         Node current = v;
/* 472 */         if (current.getType() == 158) {
/*     */           
/* 474 */           Node c = current.getFirstChild();
/* 475 */           if (c.getType() != 153) throw Kit.codeBug();
/*     */           
/* 477 */           if (isExpression) {
/* 478 */             body = new Node(89, c.getNext(), body);
/*     */           } else {
/* 480 */             body = new Node(129, new Node(133, c.getNext()), body);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 485 */           Scope.joinScopes((Scope)current, (Scope)scopeNode);
/*     */           
/* 487 */           current = c.getFirstChild();
/*     */         } 
/* 489 */         if (current.getType() != 39) throw Kit.codeBug(); 
/* 490 */         Node stringNode = Node.newString(current.getString());
/* 491 */         stringNode.setScope((Scope)scopeNode);
/* 492 */         Node init = current.getFirstChild();
/* 493 */         if (init == null) {
/* 494 */           init = new Node(126, Node.newNumber(0.0D));
/*     */         }
/* 496 */         newVars.addChildToBack(new Node(56, stringNode, init));
/*     */       } 
/* 498 */       if (isExpression) {
/* 499 */         result.addChildToBack(newVars);
/* 500 */         scopeNode.setType(89);
/* 501 */         result.addChildToBack(scopeNode);
/* 502 */         scopeNode.addChildToBack(body);
/* 503 */         if (body instanceof Scope) {
/* 504 */           Scope scopeParent = ((Scope)body).getParentScope();
/* 505 */           ((Scope)body).setParentScope((Scope)scopeNode);
/* 506 */           ((Scope)scopeNode).setParentScope(scopeParent);
/*     */         } 
/*     */       } else {
/* 509 */         result.addChildToBack(new Node(133, newVars));
/* 510 */         scopeNode.setType(129);
/* 511 */         result.addChildToBack(scopeNode);
/* 512 */         scopeNode.addChildrenToBack(body);
/* 513 */         if (body instanceof Scope) {
/* 514 */           Scope scopeParent = ((Scope)body).getParentScope();
/* 515 */           ((Scope)body).setParentScope((Scope)scopeNode);
/* 516 */           ((Scope)scopeNode).setParentScope(scopeParent);
/*     */         } 
/*     */       } 
/*     */     } 
/* 520 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Node addBeforeCurrent(Node parent, Node previous, Node current, Node toAdd) {
/* 526 */     if (previous == null) {
/* 527 */       if (current != parent.getFirstChild()) Kit.codeBug(); 
/* 528 */       parent.addChildToFront(toAdd);
/*     */     } else {
/* 530 */       if (current != previous.getNext()) Kit.codeBug(); 
/* 531 */       parent.addChildAfter(toAdd, previous);
/*     */     } 
/* 533 */     return toAdd;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Node replaceCurrent(Node parent, Node previous, Node current, Node replacement) {
/* 539 */     if (previous == null) {
/* 540 */       if (current != parent.getFirstChild()) Kit.codeBug(); 
/* 541 */       parent.replaceChild(current, replacement);
/* 542 */     } else if (previous.next == current) {
/*     */ 
/*     */       
/* 545 */       parent.replaceChildAfter(previous, replacement);
/*     */     } else {
/* 547 */       parent.replaceChild(current, replacement);
/*     */     } 
/* 549 */     return replacement;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NodeTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */