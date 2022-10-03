/*     */ package org.fife.rsta.ac.js.tree;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.tree.MutableTreeNode;
/*     */ import org.fife.rsta.ac.js.IconFactory;
/*     */ import org.fife.rsta.ac.js.util.RhinoUtil;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.mozilla.javascript.ast.Assignment;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.ExpressionStatement;
/*     */ import org.mozilla.javascript.ast.FunctionCall;
/*     */ import org.mozilla.javascript.ast.FunctionNode;
/*     */ import org.mozilla.javascript.ast.Name;
/*     */ import org.mozilla.javascript.ast.NodeVisitor;
/*     */ import org.mozilla.javascript.ast.ObjectLiteral;
/*     */ import org.mozilla.javascript.ast.ObjectProperty;
/*     */ import org.mozilla.javascript.ast.PropertyGet;
/*     */ import org.mozilla.javascript.ast.VariableDeclaration;
/*     */ import org.mozilla.javascript.ast.VariableInitializer;
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
/*     */ class JavaScriptOutlineTreeGenerator
/*     */   implements NodeVisitor
/*     */ {
/*     */   private JavaScriptTreeNode root;
/*     */   private RSyntaxTextArea textArea;
/*     */   private JavaScriptTreeNode curScopeTreeNode;
/*  52 */   private Map<String, List<JavaScriptTreeNode>> prototypeAdditions = null;
/*     */ 
/*     */ 
/*     */   
/*     */   JavaScriptOutlineTreeGenerator(RSyntaxTextArea textArea, AstRoot ast) {
/*  57 */     this.textArea = textArea;
/*  58 */     this.root = new JavaScriptTreeNode((AstNode)null);
/*  59 */     if (ast != null) {
/*  60 */       ast.visit(this);
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
/*     */   private void addPrototypeAdditionsToRoot() {
/*  72 */     if (this.prototypeAdditions != null) {
/*     */       
/*  74 */       this.root.refresh();
/*     */       
/*  76 */       for (Map.Entry<String, List<JavaScriptTreeNode>> entry : this.prototypeAdditions.entrySet()) {
/*  77 */         String clazz = entry.getKey();
/*  78 */         for (int i = 0; i < this.root.getChildCount(); i++) {
/*  79 */           JavaScriptTreeNode childNode = (JavaScriptTreeNode)this.root.getChildAt(i);
/*  80 */           String text = childNode.getText(true);
/*  81 */           if (text != null && text.startsWith(clazz + "(")) {
/*  82 */             for (JavaScriptTreeNode memberNode : entry.getValue()) {
/*  83 */               childNode.add((MutableTreeNode)memberNode);
/*     */             }
/*  85 */             childNode.setIcon(IconFactory.getIcon("default_class"));
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */   private JavaScriptTreeNode createTreeNode(AstNode node) {
/* 120 */     JavaScriptTreeNode tn = new JavaScriptTreeNode(node);
/*     */     try {
/* 122 */       int offs = node.getAbsolutePosition();
/* 123 */       tn.setOffset(this.textArea.getDocument().createPosition(offs));
/* 124 */     } catch (BadLocationException ble) {
/* 125 */       ble.printStackTrace();
/*     */     } 
/* 127 */     return tn;
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
/*     */   private JavaScriptTreeNode createTreeNode(List<AstNode> nodes) {
/* 139 */     JavaScriptTreeNode tn = new JavaScriptTreeNode(nodes);
/*     */     try {
/* 141 */       int offs = ((AstNode)nodes.get(0)).getAbsolutePosition();
/* 142 */       tn.setOffset(this.textArea.getDocument().createPosition(offs));
/* 143 */     } catch (BadLocationException ble) {
/* 144 */       ble.printStackTrace();
/*     */     } 
/* 146 */     return tn;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<AstNode> getChainedPropertyGetNodes(PropertyGet pg) {
/* 151 */     List<AstNode> nodes = new ArrayList<>();
/* 152 */     getChainedPropertyGetNodesImpl(pg, nodes);
/* 153 */     return nodes;
/*     */   }
/*     */ 
/*     */   
/*     */   private void getChainedPropertyGetNodesImpl(PropertyGet pg, List<AstNode> nodes) {
/* 158 */     if (pg.getLeft() instanceof PropertyGet) {
/* 159 */       getChainedPropertyGetNodesImpl((PropertyGet)pg.getLeft(), nodes);
/*     */     } else {
/*     */       
/* 162 */       nodes.add(pg.getLeft());
/*     */     } 
/* 164 */     nodes.add(pg.getRight());
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptTreeNode getTreeRoot() {
/* 169 */     addPrototypeAdditionsToRoot();
/* 170 */     return this.root;
/*     */   }
/*     */   
/*     */   public boolean visit(AstNode node) {
/*     */     FunctionNode fn;
/*     */     VariableDeclaration varDec;
/*     */     ExpressionStatement exprStmt;
/* 177 */     if (node == null) {
/* 178 */       return false;
/*     */     }
/*     */     
/* 181 */     int nodeType = node.getType();
/* 182 */     switch (nodeType) {
/*     */       
/*     */       case 136:
/* 185 */         this.curScopeTreeNode = this.root;
/* 186 */         return true;
/*     */       
/*     */       case 109:
/* 189 */         fn = (FunctionNode)node;
/* 190 */         return visitFunction(fn);
/*     */       
/*     */       case 122:
/* 193 */         varDec = (VariableDeclaration)node;
/* 194 */         return visitVariableDeclaration(varDec);
/*     */       
/*     */       case 129:
/* 197 */         return true;
/*     */       
/*     */       case 134:
/* 200 */         exprStmt = (ExpressionStatement)node;
/* 201 */         return visitExpressionStatement(exprStmt);
/*     */     } 
/*     */ 
/*     */     
/* 205 */     return false;
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
/*     */   private boolean visitExpressionStatement(ExpressionStatement exprStmt) {
/* 217 */     AstNode expr = exprStmt.getExpression();
/*     */ 
/*     */     
/* 220 */     if (expr instanceof Assignment) {
/*     */       
/* 222 */       Assignment assignment = (Assignment)expr;
/* 223 */       AstNode left = assignment.getLeft();
/*     */ 
/*     */       
/* 226 */       if (left instanceof PropertyGet) {
/*     */         
/* 228 */         PropertyGet pg = (PropertyGet)left;
/*     */         
/* 230 */         List<AstNode> chainedPropertyGetNodes = getChainedPropertyGetNodes(pg);
/* 231 */         int count = chainedPropertyGetNodes.size();
/*     */ 
/*     */         
/* 234 */         if (count >= 3 && 
/* 235 */           RhinoUtil.isPrototypeNameNode(chainedPropertyGetNodes.get(count - 2))) {
/*     */           
/* 237 */           String clazz = RhinoUtil.getPrototypeClazz(chainedPropertyGetNodes, count - 2);
/* 238 */           AstNode propNode = chainedPropertyGetNodes.get(count - 1);
/* 239 */           String member = ((Name)propNode).getIdentifier();
/*     */           
/* 241 */           JavaScriptTreeNode tn = createTreeNode(propNode);
/* 242 */           AstNode propertyValue = assignment.getRight();
/* 243 */           visitPrototypeMember(tn, clazz, member, propertyValue);
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 248 */         else if (RhinoUtil.isPrototypeNameNode(chainedPropertyGetNodes.get(count - 1))) {
/*     */           
/* 250 */           JavaScriptTreeNode tn = createTreeNode(chainedPropertyGetNodes);
/* 251 */           tn.setIcon(IconFactory.getIcon("local_variable"));
/* 252 */           tn.setSortPriority(2);
/* 253 */           this.curScopeTreeNode.add((MutableTreeNode)tn);
/*     */           
/* 255 */           String clazz = RhinoUtil.getPrototypeClazz(chainedPropertyGetNodes, count - 1);
/* 256 */           AstNode rhs = assignment.getRight();
/*     */ 
/*     */           
/* 259 */           if (rhs instanceof ObjectLiteral) {
/* 260 */             tn.setText(clazz + "()");
/* 261 */             ObjectLiteral value = (ObjectLiteral)rhs;
/* 262 */             visitPrototypeMembers(value, clazz);
/*     */ 
/*     */           
/*     */           }
/* 266 */           else if (rhs instanceof FunctionCall) {
/*     */             
/* 268 */             FunctionCall rhsFunc = (FunctionCall)rhs;
/* 269 */             AstNode target = rhsFunc.getTarget();
/* 270 */             if (target instanceof PropertyGet)
/*     */             {
/* 272 */               pg = (PropertyGet)target;
/* 273 */               if (RhinoUtil.isSimplePropertyGet(pg, "Object", "create")) {
/* 274 */                 tn.setText(clazz + "()");
/* 275 */                 List<AstNode> args = rhsFunc.getArguments();
/*     */ 
/*     */                 
/* 278 */                 if (args.size() >= 2) {
/* 279 */                   AstNode arg2 = args.get(1);
/* 280 */                   if (arg2 instanceof ObjectLiteral) {
/* 281 */                     ObjectLiteral descriptorObjLit = (ObjectLiteral)arg2;
/* 282 */                     visitPropertyDescriptors(descriptorObjLit, clazz);
/*     */                   }
/*     */                 
/*     */                 } 
/*     */               } else {
/*     */                 
/* 288 */                 tn.setText(clazz + "(???)");
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 296 */             tn.setText(clazz + "(???)");
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 303 */           JavaScriptTreeNode tn = createTreeNode(chainedPropertyGetNodes);
/* 304 */           tn.setIcon(IconFactory.getIcon("default_class"));
/* 305 */           tn.setSortPriority(1);
/*     */ 
/*     */           
/* 308 */           String clazz = RhinoUtil.getPrototypeClazz(chainedPropertyGetNodes, count);
/* 309 */           AstNode rhs = assignment.getRight();
/*     */ 
/*     */           
/* 312 */           if (rhs instanceof ObjectLiteral) {
/*     */             
/* 314 */             this.curScopeTreeNode.add((MutableTreeNode)tn);
/* 315 */             tn.setText(clazz + "()");
/*     */             
/* 317 */             ObjectLiteral value = (ObjectLiteral)rhs;
/* 318 */             List<ObjectProperty> properties = value.getElements();
/* 319 */             for (ObjectProperty property : properties)
/*     */             {
/* 321 */               AstNode propertyKey = property.getLeft();
/* 322 */               tn = createTreeNode(propertyKey);
/*     */               
/* 324 */               String memberName = RhinoUtil.getPropertyName(propertyKey);
/* 325 */               AstNode propertyValue = property.getRight();
/* 326 */               visitPrototypeMember(tn, clazz, memberName, propertyValue);
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 334 */           else if (rhs instanceof FunctionCall) {
/*     */             
/* 336 */             FunctionCall rhsFunc = (FunctionCall)rhs;
/* 337 */             AstNode target = rhsFunc.getTarget();
/* 338 */             if (target instanceof PropertyGet) {
/*     */               
/* 340 */               pg = (PropertyGet)target;
/* 341 */               if (RhinoUtil.isSimplePropertyGet(pg, "Object", "create")) {
/* 342 */                 this.curScopeTreeNode.add((MutableTreeNode)tn);
/* 343 */                 tn.setText(clazz + "()");
/* 344 */                 List<AstNode> args = rhsFunc.getArguments();
/*     */ 
/*     */                 
/* 347 */                 if (args.size() >= 2) {
/* 348 */                   AstNode arg2 = args.get(1);
/* 349 */                   if (arg2 instanceof ObjectLiteral) {
/* 350 */                     ObjectLiteral descriptorObjLit = (ObjectLiteral)arg2;
/* 351 */                     visitPropertyDescriptors(descriptorObjLit, clazz);
/*     */                   }
/*     */                 
/*     */                 }
/*     */               
/* 356 */               } else if (RhinoUtil.isSimplePropertyGet(pg, "Object", "freeze")) {
/* 357 */                 this.curScopeTreeNode.add((MutableTreeNode)tn);
/* 358 */                 tn.setText(clazz + "()");
/* 359 */                 List<AstNode> args = rhsFunc.getArguments();
/* 360 */                 if (args.size() == 1) {
/* 361 */                   AstNode arg = args.get(0);
/* 362 */                   if (arg instanceof ObjectLiteral) {
/* 363 */                     tn.setText(clazz + "()");
/* 364 */                     ObjectLiteral value = (ObjectLiteral)arg;
/* 365 */                     visitPrototypeMembers(value, clazz);
/*     */                   }
/*     */                 
/*     */                 } 
/*     */               } 
/*     */             } else {
/*     */               
/* 372 */               tn.setText(clazz + "(???)");
/*     */             }
/*     */           
/*     */           }
/* 376 */           else if (rhs instanceof FunctionNode) {
/* 377 */             String text = clazz;
/* 378 */             this.curScopeTreeNode.add((MutableTreeNode)tn);
/* 379 */             tn.setText(text);
/*     */             
/* 381 */             this.curScopeTreeNode = tn;
/* 382 */             ((FunctionNode)rhs).getBody().visit(this);
/* 383 */             this.curScopeTreeNode = (JavaScriptTreeNode)this.curScopeTreeNode.getParent();
/*     */           }
/*     */           else {
/*     */             
/* 387 */             this.curScopeTreeNode.add((MutableTreeNode)tn);
/* 388 */             tn.setText(clazz + "(???)");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     return false;
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
/*     */   private void visitPropertyDescriptors(ObjectLiteral descriptorObjLit, String clazz) {
/* 416 */     List<ObjectProperty> descriptors = descriptorObjLit.getElements();
/* 417 */     for (ObjectProperty prop : descriptors) {
/*     */       
/* 419 */       AstNode propertyKey = prop.getLeft();
/* 420 */       AstNode propertyValue = prop.getRight();
/*     */ 
/*     */       
/* 423 */       if (propertyValue instanceof ObjectLiteral) {
/*     */         
/* 425 */         JavaScriptTreeNode tn = createTreeNode(propertyKey);
/*     */         
/* 427 */         String memberName = RhinoUtil.getPropertyName(propertyKey);
/* 428 */         visitPropertyDescriptor(tn, clazz, memberName, (ObjectLiteral)propertyValue);
/*     */       } 
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
/*     */   private void visitPropertyDescriptor(JavaScriptTreeNode tn, String clazz, String memberName, ObjectLiteral propDesc) {
/* 454 */     List<ObjectProperty> propDescProperties = propDesc.getElements();
/* 455 */     for (ObjectProperty propDescProperty : propDescProperties) {
/*     */       
/* 457 */       AstNode propertyKey = propDescProperty.getLeft();
/* 458 */       String propName = RhinoUtil.getPropertyName(propertyKey);
/* 459 */       if ("value".equals(propName)) {
/*     */         
/* 461 */         AstNode propertyValue = propDescProperty.getRight();
/* 462 */         boolean isFunction = propertyValue instanceof FunctionNode;
/* 463 */         String text = memberName;
/* 464 */         if (isFunction) {
/* 465 */           FunctionNode func = (FunctionNode)propertyValue;
/* 466 */           text = text + RhinoUtil.getFunctionArgsString(func);
/* 467 */           tn.setIcon(IconFactory.getIcon("methpub_obj"));
/* 468 */           tn.setSortPriority(1);
/*     */         } else {
/*     */           
/* 471 */           tn.setIcon(IconFactory.getIcon("field_public_obj"));
/* 472 */           tn.setSortPriority(2);
/*     */         } 
/*     */         
/* 475 */         tn.setText(text);
/* 476 */         if (this.prototypeAdditions == null) {
/* 477 */           this.prototypeAdditions = new HashMap<>();
/*     */         }
/* 479 */         List<JavaScriptTreeNode> list = this.prototypeAdditions.get(clazz);
/* 480 */         if (list == null) {
/* 481 */           list = new ArrayList<>();
/* 482 */           this.prototypeAdditions.put(clazz, list);
/*     */         } 
/*     */         
/* 485 */         list.add(tn);
/*     */         
/* 487 */         if (isFunction) {
/* 488 */           JavaScriptTreeNode prevScopeTreeNode = this.curScopeTreeNode;
/* 489 */           this.curScopeTreeNode = tn;
/* 490 */           FunctionNode func = (FunctionNode)propertyValue;
/* 491 */           func.getBody().visit(this);
/* 492 */           this.curScopeTreeNode = prevScopeTreeNode;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void visitPrototypeMembers(ObjectLiteral objLiteral, String clazz) {
/* 505 */     List<ObjectProperty> properties = objLiteral.getElements();
/* 506 */     for (ObjectProperty property : properties) {
/*     */       
/* 508 */       AstNode propertyKey = property.getLeft();
/* 509 */       JavaScriptTreeNode tn = createTreeNode(propertyKey);
/*     */       
/* 511 */       String memberName = RhinoUtil.getPropertyName(propertyKey);
/* 512 */       AstNode propertyValue = property.getRight();
/* 513 */       visitPrototypeMember(tn, clazz, memberName, propertyValue);
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
/*     */   private void visitPrototypeMember(JavaScriptTreeNode tn, String clazz, String memberName, AstNode memberValue) {
/* 532 */     boolean isFunction = memberValue instanceof FunctionNode;
/* 533 */     String text = memberName;
/* 534 */     if (isFunction) {
/* 535 */       FunctionNode func = (FunctionNode)memberValue;
/* 536 */       text = text + RhinoUtil.getFunctionArgsString(func);
/* 537 */       tn.setIcon(IconFactory.getIcon("methpub_obj"));
/* 538 */       tn.setSortPriority(1);
/*     */     } else {
/*     */       
/* 541 */       tn.setIcon(IconFactory.getIcon("field_public_obj"));
/* 542 */       tn.setSortPriority(2);
/*     */     } 
/*     */     
/* 545 */     tn.setText(text);
/* 546 */     if (this.prototypeAdditions == null) {
/* 547 */       this.prototypeAdditions = new HashMap<>();
/*     */     }
/* 549 */     List<JavaScriptTreeNode> list = this.prototypeAdditions.get(clazz);
/* 550 */     if (list == null) {
/* 551 */       list = new ArrayList<>();
/* 552 */       this.prototypeAdditions.put(clazz, list);
/*     */     } 
/*     */     
/* 555 */     list.add(tn);
/*     */     
/* 557 */     if (isFunction) {
/* 558 */       JavaScriptTreeNode prevScopeTreeNode = this.curScopeTreeNode;
/* 559 */       this.curScopeTreeNode = tn;
/* 560 */       FunctionNode func = (FunctionNode)memberValue;
/* 561 */       func.getBody().visit(this);
/* 562 */       this.curScopeTreeNode = prevScopeTreeNode;
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
/*     */   private boolean visitFunction(FunctionNode fn) {
/* 579 */     Name funcName = fn.getFunctionName();
/*     */ 
/*     */ 
/*     */     
/* 583 */     if (funcName != null) {
/*     */       
/* 585 */       String text = fn.getName() + RhinoUtil.getFunctionArgsString(fn);
/*     */       
/* 587 */       JavaScriptTreeNode tn = createTreeNode((AstNode)funcName);
/* 588 */       tn.setText(text);
/* 589 */       tn.setIcon(IconFactory.getIcon("default_function"));
/* 590 */       tn.setSortPriority(1);
/*     */       
/* 592 */       this.curScopeTreeNode.add((MutableTreeNode)tn);
/*     */       
/* 594 */       this.curScopeTreeNode = tn;
/* 595 */       fn.getBody().visit(this);
/* 596 */       this.curScopeTreeNode = (JavaScriptTreeNode)this.curScopeTreeNode.getParent();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 601 */     return false;
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
/*     */   private boolean visitVariableDeclaration(VariableDeclaration varDec) {
/* 616 */     List<VariableInitializer> vars = varDec.getVariables();
/* 617 */     for (VariableInitializer var : vars) {
/*     */       String varName;
/* 619 */       Name varNameNode = null;
/*     */       
/* 621 */       AstNode target = var.getTarget();
/* 622 */       switch (target.getType()) {
/*     */         case 39:
/* 624 */           varNameNode = (Name)target;
/*     */           
/* 626 */           varName = varNameNode.getIdentifier();
/*     */           break;
/*     */         default:
/* 629 */           System.out.println("... Unknown var target type: " + target.getClass());
/* 630 */           varName = "?";
/*     */           break;
/*     */       } 
/*     */       
/* 634 */       boolean isFunction = var.getInitializer() instanceof FunctionNode;
/* 635 */       JavaScriptTreeNode tn = createTreeNode((AstNode)varNameNode);
/* 636 */       if (isFunction) {
/*     */         
/* 638 */         FunctionNode func = (FunctionNode)var.getInitializer();
/* 639 */         tn.setText(varName + RhinoUtil.getFunctionArgsString(func));
/* 640 */         tn.setIcon(IconFactory.getIcon("default_class"));
/* 641 */         tn.setSortPriority(1);
/* 642 */         this.curScopeTreeNode.add((MutableTreeNode)tn);
/*     */         
/* 644 */         this.curScopeTreeNode = tn;
/* 645 */         func.getBody().visit(this);
/* 646 */         this.curScopeTreeNode = (JavaScriptTreeNode)this.curScopeTreeNode.getParent();
/*     */         
/*     */         continue;
/*     */       } 
/* 650 */       tn.setText(varName);
/* 651 */       tn.setIcon(IconFactory.getIcon("local_variable"));
/* 652 */       tn.setSortPriority(2);
/* 653 */       this.curScopeTreeNode.add((MutableTreeNode)tn);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 658 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\tree\JavaScriptOutlineTreeGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */