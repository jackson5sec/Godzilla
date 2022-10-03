/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.mozilla.javascript.Node;
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
/*     */ public class FunctionNode
/*     */   extends ScriptNode
/*     */ {
/*     */   public static final int FUNCTION_STATEMENT = 1;
/*     */   public static final int FUNCTION_EXPRESSION = 2;
/*     */   public static final int FUNCTION_EXPRESSION_STATEMENT = 3;
/*     */   
/*     */   public enum Form
/*     */   {
/*  67 */     FUNCTION, GETTER, SETTER;
/*     */   }
/*  69 */   private static final List<AstNode> NO_PARAMS = Collections.unmodifiableList(new ArrayList<AstNode>());
/*     */   
/*     */   private Name functionName;
/*     */   
/*     */   private List<AstNode> params;
/*     */   private AstNode body;
/*     */   private boolean isExpressionClosure;
/*  76 */   private Form functionForm = Form.FUNCTION;
/*  77 */   private int lp = -1;
/*  78 */   private int rp = -1;
/*     */   
/*     */   private int functionType;
/*     */   
/*     */   private boolean needsActivation;
/*     */   
/*     */   private boolean isGenerator;
/*     */   
/*     */   private List<Node> generatorResumePoints;
/*     */   
/*     */   private Map<Node, int[]> liveLocals;
/*     */   
/*     */   private AstNode memberExprNode;
/*     */ 
/*     */   
/*     */   public FunctionNode() {}
/*     */   
/*     */   public FunctionNode(int pos) {
/*  96 */     super(pos);
/*     */   }
/*     */   
/*     */   public FunctionNode(int pos, Name name) {
/* 100 */     super(pos);
/* 101 */     setFunctionName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name getFunctionName() {
/* 109 */     return this.functionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFunctionName(Name name) {
/* 117 */     this.functionName = name;
/* 118 */     if (name != null) {
/* 119 */       name.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 127 */     return (this.functionName != null) ? this.functionName.getIdentifier() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AstNode> getParams() {
/* 136 */     return (this.params != null) ? this.params : NO_PARAMS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParams(List<AstNode> params) {
/* 145 */     if (params == null) {
/* 146 */       this.params = null;
/*     */     } else {
/* 148 */       if (this.params != null)
/* 149 */         this.params.clear(); 
/* 150 */       for (AstNode param : params) {
/* 151 */         addParam(param);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(AstNode param) {
/* 162 */     assertNotNull(param);
/* 163 */     if (this.params == null) {
/* 164 */       this.params = new ArrayList<AstNode>();
/*     */     }
/* 166 */     this.params.add(param);
/* 167 */     param.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParam(AstNode node) {
/* 176 */     return (this.params == null) ? false : this.params.contains(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getBody() {
/* 186 */     return this.body;
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
/*     */   public void setBody(AstNode body) {
/* 201 */     assertNotNull(body);
/* 202 */     this.body = body;
/* 203 */     if (Boolean.TRUE.equals(body.getProp(25))) {
/* 204 */       setIsExpressionClosure(true);
/*     */     }
/* 206 */     int absEnd = body.getPosition() + body.getLength();
/* 207 */     body.setParent(this);
/* 208 */     setLength(absEnd - this.position);
/* 209 */     setEncodedSourceBounds(this.position, absEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLp() {
/* 216 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLp(int lp) {
/* 223 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRp() {
/* 230 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRp(int rp) {
/* 237 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParens(int lp, int rp) {
/* 244 */     this.lp = lp;
/* 245 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpressionClosure() {
/* 252 */     return this.isExpressionClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsExpressionClosure(boolean isExpressionClosure) {
/* 259 */     this.isExpressionClosure = isExpressionClosure;
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
/*     */   public boolean requiresActivation() {
/* 273 */     return this.needsActivation;
/*     */   }
/*     */   
/*     */   public void setRequiresActivation() {
/* 277 */     this.needsActivation = true;
/*     */   }
/*     */   
/*     */   public boolean isGenerator() {
/* 281 */     return this.isGenerator;
/*     */   }
/*     */   
/*     */   public void setIsGenerator() {
/* 285 */     this.isGenerator = true;
/*     */   }
/*     */   
/*     */   public void addResumptionPoint(Node target) {
/* 289 */     if (this.generatorResumePoints == null)
/* 290 */       this.generatorResumePoints = new ArrayList<Node>(); 
/* 291 */     this.generatorResumePoints.add(target);
/*     */   }
/*     */   
/*     */   public List<Node> getResumptionPoints() {
/* 295 */     return this.generatorResumePoints;
/*     */   }
/*     */   
/*     */   public Map<Node, int[]> getLiveLocals() {
/* 299 */     return this.liveLocals;
/*     */   }
/*     */   
/*     */   public void addLiveLocals(Node node, int[] locals) {
/* 303 */     if (this.liveLocals == null)
/* 304 */       this.liveLocals = (Map)new HashMap<Node, int>(); 
/* 305 */     this.liveLocals.put(node, locals);
/*     */   }
/*     */ 
/*     */   
/*     */   public int addFunction(FunctionNode fnNode) {
/* 310 */     int result = super.addFunction(fnNode);
/* 311 */     if (getFunctionCount() > 0) {
/* 312 */       this.needsActivation = true;
/*     */     }
/* 314 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFunctionType() {
/* 321 */     return this.functionType;
/*     */   }
/*     */   
/*     */   public void setFunctionType(int type) {
/* 325 */     this.functionType = type;
/*     */   }
/*     */   
/*     */   public boolean isGetterOrSetter() {
/* 329 */     return (this.functionForm == Form.GETTER || this.functionForm == Form.SETTER);
/*     */   }
/*     */   
/*     */   public boolean isGetter() {
/* 333 */     return (this.functionForm == Form.GETTER);
/*     */   }
/*     */   
/*     */   public boolean isSetter() {
/* 337 */     return (this.functionForm == Form.SETTER);
/*     */   }
/*     */   
/*     */   public void setFunctionIsGetter() {
/* 341 */     this.functionForm = Form.GETTER;
/*     */   }
/*     */   
/*     */   public void setFunctionIsSetter() {
/* 345 */     this.functionForm = Form.SETTER;
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
/*     */   public void setMemberExprNode(AstNode node) {
/* 359 */     this.memberExprNode = node;
/* 360 */     if (node != null)
/* 361 */       node.setParent(this); 
/*     */   }
/*     */   
/*     */   public AstNode getMemberExprNode() {
/* 365 */     return this.memberExprNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 370 */     StringBuilder sb = new StringBuilder();
/* 371 */     if (!isGetterOrSetter()) {
/* 372 */       sb.append(makeIndent(depth));
/* 373 */       sb.append("function");
/*     */     } 
/* 375 */     if (this.functionName != null) {
/* 376 */       sb.append(" ");
/* 377 */       sb.append(this.functionName.toSource(0));
/*     */     } 
/* 379 */     if (this.params == null) {
/* 380 */       sb.append("() ");
/*     */     } else {
/* 382 */       sb.append("(");
/* 383 */       printList(this.params, sb);
/* 384 */       sb.append(") ");
/*     */     } 
/* 386 */     if (this.isExpressionClosure) {
/* 387 */       AstNode body = getBody();
/* 388 */       if (body.getLastChild() instanceof ReturnStatement) {
/*     */         
/* 390 */         body = ((ReturnStatement)body.getLastChild()).getReturnValue();
/* 391 */         sb.append(body.toSource(0));
/* 392 */         if (this.functionType == 1) {
/* 393 */           sb.append(";");
/*     */         }
/*     */       } else {
/*     */         
/* 397 */         sb.append(" ");
/* 398 */         sb.append(body.toSource(0));
/*     */       } 
/*     */     } else {
/* 401 */       sb.append(getBody().toSource(depth).trim());
/*     */     } 
/* 403 */     if (this.functionType == 1 || isGetterOrSetter()) {
/* 404 */       sb.append("\n");
/*     */     }
/* 406 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 416 */     if (v.visit(this)) {
/* 417 */       if (this.functionName != null) {
/* 418 */         this.functionName.visit(v);
/*     */       }
/* 420 */       for (AstNode param : getParams()) {
/* 421 */         param.visit(v);
/*     */       }
/* 423 */       getBody().visit(v);
/* 424 */       if (!this.isExpressionClosure && 
/* 425 */         this.memberExprNode != null)
/* 426 */         this.memberExprNode.visit(v); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\FunctionNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */