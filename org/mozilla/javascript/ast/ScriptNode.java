/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class ScriptNode
/*     */   extends Scope
/*     */ {
/*  22 */   private int encodedSourceStart = -1;
/*  23 */   private int encodedSourceEnd = -1;
/*     */   private String sourceName;
/*     */   private String encodedSource;
/*  26 */   private int endLineno = -1;
/*     */   
/*     */   private List<FunctionNode> functions;
/*     */   private List<RegExpLiteral> regexps;
/*  30 */   private List<FunctionNode> EMPTY_LIST = Collections.emptyList();
/*     */   
/*  32 */   private List<Symbol> symbols = new ArrayList<Symbol>(4);
/*  33 */   private int paramCount = 0;
/*     */   
/*     */   private String[] variableNames;
/*     */   private boolean[] isConsts;
/*     */   private Object compilerData;
/*  38 */   private int tempNumber = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptNode() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptNode(int pos) {
/*  50 */     super(pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSourceName() {
/*  58 */     return this.sourceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceName(String sourceName) {
/*  66 */     this.sourceName = sourceName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEncodedSourceStart() {
/*  74 */     return this.encodedSourceStart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncodedSourceStart(int start) {
/*  82 */     this.encodedSourceStart = start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEncodedSourceEnd() {
/*  90 */     return this.encodedSourceEnd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncodedSourceEnd(int end) {
/*  98 */     this.encodedSourceEnd = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncodedSourceBounds(int start, int end) {
/* 106 */     this.encodedSourceStart = start;
/* 107 */     this.encodedSourceEnd = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncodedSource(String encodedSource) {
/* 115 */     this.encodedSource = encodedSource;
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
/*     */   public String getEncodedSource() {
/* 134 */     return this.encodedSource;
/*     */   }
/*     */   
/*     */   public int getBaseLineno() {
/* 138 */     return this.lineno;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaseLineno(int lineno) {
/* 147 */     if (lineno < 0 || this.lineno >= 0) codeBug(); 
/* 148 */     this.lineno = lineno;
/*     */   }
/*     */   
/*     */   public int getEndLineno() {
/* 152 */     return this.endLineno;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEndLineno(int lineno) {
/* 157 */     if (lineno < 0 || this.endLineno >= 0) codeBug(); 
/* 158 */     this.endLineno = lineno;
/*     */   }
/*     */   
/*     */   public int getFunctionCount() {
/* 162 */     return (this.functions == null) ? 0 : this.functions.size();
/*     */   }
/*     */   
/*     */   public FunctionNode getFunctionNode(int i) {
/* 166 */     return this.functions.get(i);
/*     */   }
/*     */   
/*     */   public List<FunctionNode> getFunctions() {
/* 170 */     return (this.functions == null) ? this.EMPTY_LIST : this.functions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int addFunction(FunctionNode fnNode) {
/* 179 */     if (fnNode == null) codeBug(); 
/* 180 */     if (this.functions == null)
/* 181 */       this.functions = new ArrayList<FunctionNode>(); 
/* 182 */     this.functions.add(fnNode);
/* 183 */     return this.functions.size() - 1;
/*     */   }
/*     */   
/*     */   public int getRegexpCount() {
/* 187 */     return (this.regexps == null) ? 0 : this.regexps.size();
/*     */   }
/*     */   
/*     */   public String getRegexpString(int index) {
/* 191 */     return ((RegExpLiteral)this.regexps.get(index)).getValue();
/*     */   }
/*     */   
/*     */   public String getRegexpFlags(int index) {
/* 195 */     return ((RegExpLiteral)this.regexps.get(index)).getFlags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRegExp(RegExpLiteral re) {
/* 202 */     if (re == null) codeBug(); 
/* 203 */     if (this.regexps == null)
/* 204 */       this.regexps = new ArrayList<RegExpLiteral>(); 
/* 205 */     this.regexps.add(re);
/* 206 */     re.putIntProp(4, this.regexps.size() - 1);
/*     */   }
/*     */   
/*     */   public int getIndexForNameNode(Node nameNode) {
/* 210 */     if (this.variableNames == null) codeBug(); 
/* 211 */     Scope node = nameNode.getScope();
/* 212 */     Symbol symbol = (node == null) ? null : node.getSymbol(((Name)nameNode).getIdentifier());
/*     */ 
/*     */     
/* 215 */     return (symbol == null) ? -1 : symbol.getIndex();
/*     */   }
/*     */   
/*     */   public String getParamOrVarName(int index) {
/* 219 */     if (this.variableNames == null) codeBug(); 
/* 220 */     return this.variableNames[index];
/*     */   }
/*     */   
/*     */   public int getParamCount() {
/* 224 */     return this.paramCount;
/*     */   }
/*     */   
/*     */   public int getParamAndVarCount() {
/* 228 */     if (this.variableNames == null) codeBug(); 
/* 229 */     return this.symbols.size();
/*     */   }
/*     */   
/*     */   public String[] getParamAndVarNames() {
/* 233 */     if (this.variableNames == null) codeBug(); 
/* 234 */     return this.variableNames;
/*     */   }
/*     */   
/*     */   public boolean[] getParamAndVarConst() {
/* 238 */     if (this.variableNames == null) codeBug(); 
/* 239 */     return this.isConsts;
/*     */   }
/*     */   
/*     */   void addSymbol(Symbol symbol) {
/* 243 */     if (this.variableNames != null) codeBug(); 
/* 244 */     if (symbol.getDeclType() == 87) {
/* 245 */       this.paramCount++;
/*     */     }
/* 247 */     this.symbols.add(symbol);
/*     */   }
/*     */   
/*     */   public List<Symbol> getSymbols() {
/* 251 */     return this.symbols;
/*     */   }
/*     */   
/*     */   public void setSymbols(List<Symbol> symbols) {
/* 255 */     this.symbols = symbols;
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
/*     */   public void flattenSymbolTable(boolean flattenAllTables) {
/* 267 */     if (!flattenAllTables) {
/* 268 */       List<Symbol> newSymbols = new ArrayList<Symbol>();
/* 269 */       if (this.symbolTable != null)
/*     */       {
/*     */ 
/*     */         
/* 273 */         for (int j = 0; j < this.symbols.size(); j++) {
/* 274 */           Symbol symbol = this.symbols.get(j);
/* 275 */           if (symbol.getContainingTable() == this) {
/* 276 */             newSymbols.add(symbol);
/*     */           }
/*     */         } 
/*     */       }
/* 280 */       this.symbols = newSymbols;
/*     */     } 
/* 282 */     this.variableNames = new String[this.symbols.size()];
/* 283 */     this.isConsts = new boolean[this.symbols.size()];
/* 284 */     for (int i = 0; i < this.symbols.size(); i++) {
/* 285 */       Symbol symbol = this.symbols.get(i);
/* 286 */       this.variableNames[i] = symbol.getName();
/* 287 */       this.isConsts[i] = (symbol.getDeclType() == 154);
/* 288 */       symbol.setIndex(i);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getCompilerData() {
/* 293 */     return this.compilerData;
/*     */   }
/*     */   
/*     */   public void setCompilerData(Object data) {
/* 297 */     assertNotNull(data);
/*     */     
/* 299 */     if (this.compilerData != null)
/* 300 */       throw new IllegalStateException(); 
/* 301 */     this.compilerData = data;
/*     */   }
/*     */   
/*     */   public String getNextTempName() {
/* 305 */     return "$" + this.tempNumber++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 310 */     if (v.visit(this))
/* 311 */       for (Node kid : this)
/* 312 */         ((AstNode)kid).visit(v);  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ScriptNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */