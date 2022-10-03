/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
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
/*     */ public class Scope
/*     */   extends Jump
/*     */ {
/*     */   protected Map<String, Symbol> symbolTable;
/*     */   protected Scope parentScope;
/*     */   protected ScriptNode top;
/*     */   private List<Scope> childScopes;
/*     */   
/*     */   public Scope() {}
/*     */   
/*     */   public Scope(int pos) {
/*  39 */     this.position = pos;
/*     */   }
/*     */   
/*     */   public Scope(int pos, int len) {
/*  43 */     this(pos);
/*  44 */     this.length = len;
/*     */   }
/*     */   
/*     */   public Scope getParentScope() {
/*  48 */     return this.parentScope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentScope(Scope parentScope) {
/*  55 */     this.parentScope = parentScope;
/*  56 */     this.top = (parentScope == null) ? (ScriptNode)this : parentScope.top;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearParentScope() {
/*  63 */     this.parentScope = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Scope> getChildScopes() {
/*  71 */     return this.childScopes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChildScope(Scope child) {
/*  81 */     if (this.childScopes == null) {
/*  82 */       this.childScopes = new ArrayList<Scope>();
/*     */     }
/*  84 */     this.childScopes.add(child);
/*  85 */     child.setParentScope(this);
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
/*     */   public void replaceWith(Scope newScope) {
/*  98 */     if (this.childScopes != null) {
/*  99 */       for (Scope kid : this.childScopes) {
/* 100 */         newScope.addChildScope(kid);
/*     */       }
/* 102 */       this.childScopes.clear();
/* 103 */       this.childScopes = null;
/*     */     } 
/* 105 */     if (this.symbolTable != null && !this.symbolTable.isEmpty()) {
/* 106 */       joinScopes(this, newScope);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptNode getTop() {
/* 114 */     return this.top;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTop(ScriptNode top) {
/* 121 */     this.top = top;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scope splitScope(Scope scope) {
/* 131 */     Scope result = new Scope(scope.getType());
/* 132 */     result.symbolTable = scope.symbolTable;
/* 133 */     scope.symbolTable = null;
/* 134 */     result.parent = scope.parent;
/* 135 */     result.setParentScope(scope.getParentScope());
/* 136 */     result.setParentScope(result);
/* 137 */     scope.parent = result;
/* 138 */     result.top = scope.top;
/* 139 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void joinScopes(Scope source, Scope dest) {
/* 146 */     Map<String, Symbol> src = source.ensureSymbolTable();
/* 147 */     Map<String, Symbol> dst = dest.ensureSymbolTable();
/* 148 */     if (!Collections.disjoint(src.keySet(), dst.keySet())) {
/* 149 */       codeBug();
/*     */     }
/* 151 */     for (Map.Entry<String, Symbol> entry : src.entrySet()) {
/* 152 */       Symbol sym = entry.getValue();
/* 153 */       sym.setContainingTable(dest);
/* 154 */       dst.put(entry.getKey(), sym);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scope getDefiningScope(String name) {
/* 165 */     for (Scope s = this; s != null; s = s.parentScope) {
/* 166 */       Map<String, Symbol> symbolTable = s.getSymbolTable();
/* 167 */       if (symbolTable != null && symbolTable.containsKey(name)) {
/* 168 */         return s;
/*     */       }
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Symbol getSymbol(String name) {
/* 180 */     return (this.symbolTable == null) ? null : this.symbolTable.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putSymbol(Symbol symbol) {
/* 187 */     if (symbol.getName() == null)
/* 188 */       throw new IllegalArgumentException("null symbol name"); 
/* 189 */     ensureSymbolTable();
/* 190 */     this.symbolTable.put(symbol.getName(), symbol);
/* 191 */     symbol.setContainingTable(this);
/* 192 */     this.top.addSymbol(symbol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Symbol> getSymbolTable() {
/* 200 */     return this.symbolTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSymbolTable(Map<String, Symbol> table) {
/* 207 */     this.symbolTable = table;
/*     */   }
/*     */   
/*     */   private Map<String, Symbol> ensureSymbolTable() {
/* 211 */     if (this.symbolTable == null) {
/* 212 */       this.symbolTable = new LinkedHashMap<String, Symbol>(5);
/*     */     }
/* 214 */     return this.symbolTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AstNode> getStatements() {
/* 225 */     List<AstNode> stmts = new ArrayList<AstNode>();
/* 226 */     Node n = getFirstChild();
/* 227 */     while (n != null) {
/* 228 */       stmts.add((AstNode)n);
/* 229 */       n = n.getNext();
/*     */     } 
/* 231 */     return stmts;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 236 */     StringBuilder sb = new StringBuilder();
/* 237 */     sb.append(makeIndent(depth));
/* 238 */     sb.append("{\n");
/* 239 */     for (Node kid : this) {
/* 240 */       sb.append(((AstNode)kid).toSource(depth + 1));
/*     */     }
/* 242 */     sb.append(makeIndent(depth));
/* 243 */     sb.append("}\n");
/* 244 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 249 */     if (v.visit(this))
/* 250 */       for (Node kid : this)
/* 251 */         ((AstNode)kid).visit(v);  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Scope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */