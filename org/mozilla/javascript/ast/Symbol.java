/*     */ package org.mozilla.javascript.ast;
/*     */ 
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
/*     */ public class Symbol
/*     */ {
/*     */   private int declType;
/*  20 */   private int index = -1;
/*     */ 
/*     */   
/*     */   private String name;
/*     */   
/*     */   private Node node;
/*     */   
/*     */   private Scope containingTable;
/*     */ 
/*     */   
/*     */   public Symbol() {}
/*     */ 
/*     */   
/*     */   public Symbol(int declType, String name) {
/*  34 */     setName(name);
/*  35 */     setDeclType(declType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDeclType() {
/*  42 */     return this.declType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeclType(int declType) {
/*  49 */     if (declType != 109 && declType != 87 && declType != 122 && declType != 153 && declType != 154)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  54 */       throw new IllegalArgumentException("Invalid declType: " + declType); } 
/*  55 */     this.declType = declType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  62 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  69 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/*  76 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/*  83 */     return this.index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndex(int index) {
/*  90 */     this.index = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNode(Node node) {
/*  97 */     this.node = node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scope getContainingTable() {
/* 104 */     return this.containingTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContainingTable(Scope containingTable) {
/* 111 */     this.containingTable = containingTable;
/*     */   }
/*     */   
/*     */   public String getDeclTypeName() {
/* 115 */     return Token.typeToName(this.declType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     StringBuilder result = new StringBuilder();
/* 121 */     result.append("Symbol (");
/* 122 */     result.append(getDeclTypeName());
/* 123 */     result.append(") name=");
/* 124 */     result.append(this.name);
/* 125 */     if (this.node != null) {
/* 126 */       result.append(" line=");
/* 127 */       result.append(this.node.getLineno());
/*     */     } 
/* 129 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Symbol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */