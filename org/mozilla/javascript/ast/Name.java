/*     */ package org.mozilla.javascript.ast;
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
/*     */ public class Name
/*     */   extends AstNode
/*     */ {
/*     */   private String identifier;
/*     */   private Scope scope;
/*     */   
/*     */   public Name() {}
/*     */   
/*     */   public Name(int pos) {
/*  33 */     super(pos);
/*     */   }
/*     */   
/*     */   public Name(int pos, int len) {
/*  37 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Name(int pos, int len, String name) {
/*  47 */     super(pos, len);
/*  48 */     setIdentifier(name);
/*     */   }
/*     */   
/*     */   public Name(int pos, String name) {
/*  52 */     super(pos);
/*  53 */     setIdentifier(name);
/*  54 */     setLength(name.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIdentifier() {
/*  61 */     return this.identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdentifier(String identifier) {
/*  69 */     assertNotNull(identifier);
/*  70 */     this.identifier = identifier;
/*  71 */     setLength(identifier.length());
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
/*     */   public void setScope(Scope s) {
/*  86 */     this.scope = s;
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
/*     */   public Scope getScope() {
/*  98 */     return this.scope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scope getDefiningScope() {
/* 107 */     Scope enclosing = getEnclosingScope();
/* 108 */     String name = getIdentifier();
/* 109 */     return (enclosing == null) ? null : enclosing.getDefiningScope(name);
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
/*     */   public boolean isLocalName() {
/* 126 */     Scope scope = getDefiningScope();
/* 127 */     return (scope != null && scope.getParentScope() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 137 */     return (this.identifier == null) ? 0 : this.identifier.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 142 */     return makeIndent(depth) + ((this.identifier == null) ? "<null>" : this.identifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 150 */     v.visit(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Name.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */