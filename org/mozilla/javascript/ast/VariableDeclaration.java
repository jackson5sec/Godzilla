/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.mozilla.javascript.Node;
/*     */ import org.mozilla.javascript.Token;
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
/*     */ public class VariableDeclaration
/*     */   extends AstNode
/*     */ {
/*  29 */   private List<VariableInitializer> variables = new ArrayList<VariableInitializer>();
/*     */ 
/*     */   
/*     */   private boolean isStatement;
/*     */ 
/*     */ 
/*     */   
/*     */   public VariableDeclaration() {}
/*     */ 
/*     */   
/*     */   public VariableDeclaration(int pos) {
/*  40 */     super(pos);
/*     */   }
/*     */   
/*     */   public VariableDeclaration(int pos, int len) {
/*  44 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VariableInitializer> getVariables() {
/*  51 */     return this.variables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariables(List<VariableInitializer> variables) {
/*  59 */     assertNotNull(variables);
/*  60 */     this.variables.clear();
/*  61 */     for (VariableInitializer vi : variables) {
/*  62 */       addVariable(vi);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVariable(VariableInitializer v) {
/*  72 */     assertNotNull(v);
/*  73 */     this.variables.add(v);
/*  74 */     v.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node setType(int type) {
/*  83 */     if (type != 122 && type != 154 && type != 153)
/*     */     {
/*     */       
/*  86 */       throw new IllegalArgumentException("invalid decl type: " + type); } 
/*  87 */     return super.setType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVar() {
/*  96 */     return (this.type == 122);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConst() {
/* 103 */     return (this.type == 154);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLet() {
/* 110 */     return (this.type == 153);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatement() {
/* 117 */     return this.isStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsStatement(boolean isStatement) {
/* 124 */     this.isStatement = isStatement;
/*     */   }
/*     */   
/*     */   private String declTypeName() {
/* 128 */     return Token.typeToName(this.type).toLowerCase();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 133 */     StringBuilder sb = new StringBuilder();
/* 134 */     sb.append(makeIndent(depth));
/* 135 */     sb.append(declTypeName());
/* 136 */     sb.append(" ");
/* 137 */     printList(this.variables, sb);
/* 138 */     if (isStatement()) {
/* 139 */       sb.append(";\n");
/*     */     }
/* 141 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 149 */     if (v.visit(this))
/* 150 */       for (AstNode var : this.variables)
/* 151 */         var.visit(v);  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\VariableDeclaration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */