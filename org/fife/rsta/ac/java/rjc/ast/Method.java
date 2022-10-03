/*     */ package org.fife.rsta.ac.java.rjc.ast;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Offset;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Scanner;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Token;
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
/*     */ public class Method
/*     */   extends AbstractMember
/*     */ {
/*     */   private Modifiers modifiers;
/*     */   private Type type;
/*     */   private List<FormalParameter> parameters;
/*     */   private List<String> thrownTypeNames;
/*     */   private CodeBlock body;
/*     */   private boolean deprecated;
/*     */   private String docComment;
/*     */   
/*     */   public Method(Scanner s, Modifiers modifiers, Type type, Token nameToken, List<FormalParameter> params, List<String> thrownTypeNames) {
/*  36 */     super(nameToken.getLexeme(), s
/*  37 */         .createOffset(nameToken.getOffset()), s
/*  38 */         .createOffset(nameToken.getOffset() + nameToken.getLength()));
/*  39 */     if (modifiers == null) {
/*  40 */       modifiers = new Modifiers();
/*     */     }
/*  42 */     this.modifiers = modifiers;
/*  43 */     this.type = type;
/*  44 */     this.parameters = params;
/*  45 */     this.thrownTypeNames = thrownTypeNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public CodeBlock getBody() {
/*  50 */     return this.body;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBodyContainsOffset(int offs) {
/*  55 */     return (offs >= getBodyStartOffset() && offs < getBodyEndOffset());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBodyEndOffset() {
/*  60 */     return (this.body == null) ? Integer.MAX_VALUE : this.body.getNameEndOffset();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBodyStartOffset() {
/*  65 */     return getNameStartOffset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDocComment() {
/*  71 */     return this.docComment;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Modifiers getModifiers() {
/*  77 */     return this.modifiers;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNameAndParameters() {
/*  82 */     StringBuilder sb = new StringBuilder(getName());
/*  83 */     sb.append('(');
/*  84 */     int count = getParameterCount();
/*  85 */     for (int i = 0; i < count; i++) {
/*     */       
/*  87 */       FormalParameter fp = getParameter(i);
/*  88 */       sb.append(fp.getType().getName(false));
/*  89 */       sb.append(' ');
/*  90 */       sb.append(fp.getName());
/*  91 */       if (i < count - 1) {
/*  92 */         sb.append(", ");
/*     */       }
/*     */     } 
/*  95 */     sb.append(')');
/*  96 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public FormalParameter getParameter(int index) {
/* 101 */     return this.parameters.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterCount() {
/* 106 */     return this.parameters.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<FormalParameter> getParameterIterator() {
/* 111 */     return this.parameters.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThrownTypeNameCount() {
/* 116 */     return (this.thrownTypeNames == null) ? 0 : this.thrownTypeNames.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 122 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 127 */     return (this.type == null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 133 */     return this.deprecated;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBody(CodeBlock body) {
/* 138 */     this.body = body;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDeprecated(boolean deprecated) {
/* 143 */     this.deprecated = deprecated;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDocComment(String comment) {
/* 148 */     this.docComment = comment;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\Method.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */