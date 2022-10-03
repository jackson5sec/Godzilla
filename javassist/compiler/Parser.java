/*      */ package javassist.compiler;
/*      */ 
/*      */ import javassist.compiler.ast.ASTList;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.ArrayInit;
/*      */ import javassist.compiler.ast.AssignExpr;
/*      */ import javassist.compiler.ast.BinExpr;
/*      */ import javassist.compiler.ast.CallExpr;
/*      */ import javassist.compiler.ast.CastExpr;
/*      */ import javassist.compiler.ast.CondExpr;
/*      */ import javassist.compiler.ast.Declarator;
/*      */ import javassist.compiler.ast.DoubleConst;
/*      */ import javassist.compiler.ast.Expr;
/*      */ import javassist.compiler.ast.FieldDecl;
/*      */ import javassist.compiler.ast.InstanceOfExpr;
/*      */ import javassist.compiler.ast.IntConst;
/*      */ import javassist.compiler.ast.Keyword;
/*      */ import javassist.compiler.ast.Member;
/*      */ import javassist.compiler.ast.MethodDecl;
/*      */ import javassist.compiler.ast.NewExpr;
/*      */ import javassist.compiler.ast.Pair;
/*      */ import javassist.compiler.ast.Stmnt;
/*      */ import javassist.compiler.ast.StringL;
/*      */ import javassist.compiler.ast.Symbol;
/*      */ import javassist.compiler.ast.Variable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Parser
/*      */   implements TokenId
/*      */ {
/*      */   private Lex lex;
/*      */   
/*      */   public Parser(Lex lex) {
/*   47 */     this.lex = lex;
/*      */   }
/*      */   public boolean hasMore() {
/*   50 */     return (this.lex.lookAhead() >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASTList parseMember(SymbolTable tbl) throws CompileError {
/*   56 */     ASTList mem = parseMember1(tbl);
/*   57 */     if (mem instanceof MethodDecl)
/*   58 */       return (ASTList)parseMethod2(tbl, (MethodDecl)mem); 
/*   59 */     return mem;
/*      */   }
/*      */   
/*      */   public ASTList parseMember1(SymbolTable tbl) throws CompileError {
/*      */     Declarator d;
/*      */     String name;
/*   65 */     ASTList mods = parseMemberMods();
/*      */     
/*   67 */     boolean isConstructor = false;
/*   68 */     if (this.lex.lookAhead() == 400 && this.lex.lookAhead(1) == 40) {
/*   69 */       d = new Declarator(344, 0);
/*   70 */       isConstructor = true;
/*      */     } else {
/*      */       
/*   73 */       d = parseFormalType(tbl);
/*      */     } 
/*   75 */     if (this.lex.get() != 400) {
/*   76 */       throw new SyntaxError(this.lex);
/*      */     }
/*      */     
/*   79 */     if (isConstructor) {
/*   80 */       name = "<init>";
/*      */     } else {
/*   82 */       name = this.lex.getString();
/*      */     } 
/*   84 */     d.setVariable(new Symbol(name));
/*   85 */     if (isConstructor || this.lex.lookAhead() == 40)
/*   86 */       return (ASTList)parseMethod1(tbl, isConstructor, mods, d); 
/*   87 */     return (ASTList)parseField(tbl, mods, d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FieldDecl parseField(SymbolTable tbl, ASTList mods, Declarator d) throws CompileError {
/*   98 */     ASTree expr = null;
/*   99 */     if (this.lex.lookAhead() == 61) {
/*  100 */       this.lex.get();
/*  101 */       expr = parseExpression(tbl);
/*      */     } 
/*      */     
/*  104 */     int c = this.lex.get();
/*  105 */     if (c == 59)
/*  106 */       return new FieldDecl((ASTree)mods, new ASTList((ASTree)d, new ASTList(expr))); 
/*  107 */     if (c == 44) {
/*  108 */       throw new CompileError("only one field can be declared in one declaration", this.lex);
/*      */     }
/*      */     
/*  111 */     throw new SyntaxError(this.lex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private MethodDecl parseMethod1(SymbolTable tbl, boolean isConstructor, ASTList mods, Declarator d) throws CompileError {
/*  128 */     if (this.lex.get() != 40) {
/*  129 */       throw new SyntaxError(this.lex);
/*      */     }
/*  131 */     ASTList parms = null;
/*  132 */     if (this.lex.lookAhead() != 41)
/*      */       while (true) {
/*  134 */         parms = ASTList.append(parms, (ASTree)parseFormalParam(tbl));
/*  135 */         int t = this.lex.lookAhead();
/*  136 */         if (t == 44) {
/*  137 */           this.lex.get(); continue;
/*  138 */         }  if (t == 41) {
/*      */           break;
/*      */         }
/*      */       }  
/*  142 */     this.lex.get();
/*  143 */     d.addArrayDim(parseArrayDimension());
/*  144 */     if (isConstructor && d.getArrayDim() > 0) {
/*  145 */       throw new SyntaxError(this.lex);
/*      */     }
/*  147 */     ASTList throwsList = null;
/*  148 */     if (this.lex.lookAhead() == 341) {
/*  149 */       this.lex.get();
/*      */       while (true) {
/*  151 */         throwsList = ASTList.append(throwsList, (ASTree)parseClassType(tbl));
/*  152 */         if (this.lex.lookAhead() == 44) {
/*  153 */           this.lex.get();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     } 
/*  159 */     return new MethodDecl((ASTree)mods, new ASTList((ASTree)d, 
/*  160 */           ASTList.make((ASTree)parms, (ASTree)throwsList, null)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MethodDecl parseMethod2(SymbolTable tbl, MethodDecl md) throws CompileError {
/*  168 */     Stmnt body = null;
/*  169 */     if (this.lex.lookAhead() == 59) {
/*  170 */       this.lex.get();
/*      */     } else {
/*  172 */       body = parseBlock(tbl);
/*  173 */       if (body == null) {
/*  174 */         body = new Stmnt(66);
/*      */       }
/*      */     } 
/*  177 */     md.sublist(4).setHead((ASTree)body);
/*  178 */     return md;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseMemberMods() {
/*  188 */     ASTList list = null;
/*      */     while (true) {
/*  190 */       int t = this.lex.lookAhead();
/*  191 */       if (t == 300 || t == 315 || t == 332 || t == 331 || t == 330 || t == 338 || t == 335 || t == 345 || t == 342 || t == 347) {
/*      */ 
/*      */         
/*  194 */         list = new ASTList((ASTree)new Keyword(this.lex.get()), list);
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  199 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Declarator parseFormalType(SymbolTable tbl) throws CompileError {
/*  205 */     int t = this.lex.lookAhead();
/*  206 */     if (isBuiltinType(t) || t == 344) {
/*  207 */       this.lex.get();
/*  208 */       int i = parseArrayDimension();
/*  209 */       return new Declarator(t, i);
/*      */     } 
/*  211 */     ASTList name = parseClassType(tbl);
/*  212 */     int dim = parseArrayDimension();
/*  213 */     return new Declarator(name, dim);
/*      */   }
/*      */   
/*      */   private static boolean isBuiltinType(int t) {
/*  217 */     return (t == 301 || t == 303 || t == 306 || t == 334 || t == 324 || t == 326 || t == 317 || t == 312);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Declarator parseFormalParam(SymbolTable tbl) throws CompileError {
/*  226 */     Declarator d = parseFormalType(tbl);
/*  227 */     if (this.lex.get() != 400) {
/*  228 */       throw new SyntaxError(this.lex);
/*      */     }
/*  230 */     String name = this.lex.getString();
/*  231 */     d.setVariable(new Symbol(name));
/*  232 */     d.addArrayDim(parseArrayDimension());
/*  233 */     tbl.append(name, d);
/*  234 */     return d;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Stmnt parseStatement(SymbolTable tbl) throws CompileError {
/*  259 */     int t = this.lex.lookAhead();
/*  260 */     if (t == 123)
/*  261 */       return parseBlock(tbl); 
/*  262 */     if (t == 59) {
/*  263 */       this.lex.get();
/*  264 */       return new Stmnt(66);
/*      */     } 
/*  266 */     if (t == 400 && this.lex.lookAhead(1) == 58) {
/*  267 */       this.lex.get();
/*  268 */       String label = this.lex.getString();
/*  269 */       this.lex.get();
/*  270 */       return Stmnt.make(76, (ASTree)new Symbol(label), (ASTree)parseStatement(tbl));
/*      */     } 
/*  272 */     if (t == 320)
/*  273 */       return parseIf(tbl); 
/*  274 */     if (t == 346)
/*  275 */       return parseWhile(tbl); 
/*  276 */     if (t == 311)
/*  277 */       return parseDo(tbl); 
/*  278 */     if (t == 318)
/*  279 */       return parseFor(tbl); 
/*  280 */     if (t == 343)
/*  281 */       return parseTry(tbl); 
/*  282 */     if (t == 337)
/*  283 */       return parseSwitch(tbl); 
/*  284 */     if (t == 338)
/*  285 */       return parseSynchronized(tbl); 
/*  286 */     if (t == 333)
/*  287 */       return parseReturn(tbl); 
/*  288 */     if (t == 340)
/*  289 */       return parseThrow(tbl); 
/*  290 */     if (t == 302)
/*  291 */       return parseBreak(tbl); 
/*  292 */     if (t == 309) {
/*  293 */       return parseContinue(tbl);
/*      */     }
/*  295 */     return parseDeclarationOrExpression(tbl, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseBlock(SymbolTable tbl) throws CompileError {
/*  301 */     if (this.lex.get() != 123) {
/*  302 */       throw new SyntaxError(this.lex);
/*      */     }
/*  304 */     Stmnt body = null;
/*  305 */     SymbolTable tbl2 = new SymbolTable(tbl);
/*  306 */     while (this.lex.lookAhead() != 125) {
/*  307 */       Stmnt s = parseStatement(tbl2);
/*  308 */       if (s != null) {
/*  309 */         body = (Stmnt)ASTList.concat((ASTList)body, (ASTList)new Stmnt(66, (ASTree)s));
/*      */       }
/*      */     } 
/*  312 */     this.lex.get();
/*  313 */     if (body == null)
/*  314 */       return new Stmnt(66); 
/*  315 */     return body;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseIf(SymbolTable tbl) throws CompileError {
/*      */     Stmnt elsep;
/*  322 */     int t = this.lex.get();
/*  323 */     ASTree expr = parseParExpression(tbl);
/*  324 */     Stmnt thenp = parseStatement(tbl);
/*      */     
/*  326 */     if (this.lex.lookAhead() == 313) {
/*  327 */       this.lex.get();
/*  328 */       elsep = parseStatement(tbl);
/*      */     } else {
/*      */       
/*  331 */       elsep = null;
/*      */     } 
/*  333 */     return new Stmnt(t, expr, new ASTList((ASTree)thenp, new ASTList((ASTree)elsep)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseWhile(SymbolTable tbl) throws CompileError {
/*  341 */     int t = this.lex.get();
/*  342 */     ASTree expr = parseParExpression(tbl);
/*  343 */     Stmnt body = parseStatement(tbl);
/*  344 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseDo(SymbolTable tbl) throws CompileError {
/*  350 */     int t = this.lex.get();
/*  351 */     Stmnt body = parseStatement(tbl);
/*  352 */     if (this.lex.get() != 346 || this.lex.get() != 40) {
/*  353 */       throw new SyntaxError(this.lex);
/*      */     }
/*  355 */     ASTree expr = parseExpression(tbl);
/*  356 */     if (this.lex.get() != 41 || this.lex.get() != 59) {
/*  357 */       throw new SyntaxError(this.lex);
/*      */     }
/*  359 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseFor(SymbolTable tbl) throws CompileError {
/*      */     Stmnt expr1, expr3;
/*      */     ASTree expr2;
/*  368 */     int t = this.lex.get();
/*      */     
/*  370 */     SymbolTable tbl2 = new SymbolTable(tbl);
/*      */     
/*  372 */     if (this.lex.get() != 40) {
/*  373 */       throw new SyntaxError(this.lex);
/*      */     }
/*  375 */     if (this.lex.lookAhead() == 59) {
/*  376 */       this.lex.get();
/*  377 */       expr1 = null;
/*      */     } else {
/*      */       
/*  380 */       expr1 = parseDeclarationOrExpression(tbl2, true);
/*      */     } 
/*  382 */     if (this.lex.lookAhead() == 59) {
/*  383 */       expr2 = null;
/*      */     } else {
/*  385 */       expr2 = parseExpression(tbl2);
/*      */     } 
/*  387 */     if (this.lex.get() != 59) {
/*  388 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  390 */     if (this.lex.lookAhead() == 41) {
/*  391 */       expr3 = null;
/*      */     } else {
/*  393 */       expr3 = parseExprList(tbl2);
/*      */     } 
/*  395 */     if (this.lex.get() != 41) {
/*  396 */       throw new CompileError(") is missing", this.lex);
/*      */     }
/*  398 */     Stmnt body = parseStatement(tbl2);
/*  399 */     return new Stmnt(t, (ASTree)expr1, new ASTList(expr2, new ASTList((ASTree)expr3, (ASTList)body)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseSwitch(SymbolTable tbl) throws CompileError {
/*  411 */     int t = this.lex.get();
/*  412 */     ASTree expr = parseParExpression(tbl);
/*  413 */     Stmnt body = parseSwitchBlock(tbl);
/*  414 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */   
/*      */   private Stmnt parseSwitchBlock(SymbolTable tbl) throws CompileError {
/*  418 */     if (this.lex.get() != 123) {
/*  419 */       throw new SyntaxError(this.lex);
/*      */     }
/*  421 */     SymbolTable tbl2 = new SymbolTable(tbl);
/*  422 */     Stmnt s = parseStmntOrCase(tbl2);
/*  423 */     if (s == null) {
/*  424 */       throw new CompileError("empty switch block", this.lex);
/*      */     }
/*  426 */     int op = s.getOperator();
/*  427 */     if (op != 304 && op != 310) {
/*  428 */       throw new CompileError("no case or default in a switch block", this.lex);
/*      */     }
/*      */     
/*  431 */     Stmnt body = new Stmnt(66, (ASTree)s);
/*  432 */     while (this.lex.lookAhead() != 125) {
/*  433 */       Stmnt s2 = parseStmntOrCase(tbl2);
/*  434 */       if (s2 != null) {
/*  435 */         int op2 = s2.getOperator();
/*  436 */         if (op2 == 304 || op2 == 310) {
/*  437 */           body = (Stmnt)ASTList.concat((ASTList)body, (ASTList)new Stmnt(66, (ASTree)s2));
/*  438 */           s = s2;
/*      */           continue;
/*      */         } 
/*  441 */         s = (Stmnt)ASTList.concat((ASTList)s, (ASTList)new Stmnt(66, (ASTree)s2));
/*      */       } 
/*      */     } 
/*      */     
/*  445 */     this.lex.get();
/*  446 */     return body;
/*      */   }
/*      */   private Stmnt parseStmntOrCase(SymbolTable tbl) throws CompileError {
/*      */     Stmnt s;
/*  450 */     int t = this.lex.lookAhead();
/*  451 */     if (t != 304 && t != 310) {
/*  452 */       return parseStatement(tbl);
/*      */     }
/*  454 */     this.lex.get();
/*      */     
/*  456 */     if (t == 304) {
/*  457 */       s = new Stmnt(t, parseExpression(tbl));
/*      */     } else {
/*  459 */       s = new Stmnt(310);
/*      */     } 
/*  461 */     if (this.lex.get() != 58) {
/*  462 */       throw new CompileError(": is missing", this.lex);
/*      */     }
/*  464 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseSynchronized(SymbolTable tbl) throws CompileError {
/*  471 */     int t = this.lex.get();
/*  472 */     if (this.lex.get() != 40) {
/*  473 */       throw new SyntaxError(this.lex);
/*      */     }
/*  475 */     ASTree expr = parseExpression(tbl);
/*  476 */     if (this.lex.get() != 41) {
/*  477 */       throw new SyntaxError(this.lex);
/*      */     }
/*  479 */     Stmnt body = parseBlock(tbl);
/*  480 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseTry(SymbolTable tbl) throws CompileError {
/*  489 */     this.lex.get();
/*  490 */     Stmnt block = parseBlock(tbl);
/*  491 */     ASTList catchList = null;
/*  492 */     while (this.lex.lookAhead() == 305) {
/*  493 */       this.lex.get();
/*  494 */       if (this.lex.get() != 40) {
/*  495 */         throw new SyntaxError(this.lex);
/*      */       }
/*  497 */       SymbolTable tbl2 = new SymbolTable(tbl);
/*  498 */       Declarator d = parseFormalParam(tbl2);
/*  499 */       if (d.getArrayDim() > 0 || d.getType() != 307) {
/*  500 */         throw new SyntaxError(this.lex);
/*      */       }
/*  502 */       if (this.lex.get() != 41) {
/*  503 */         throw new SyntaxError(this.lex);
/*      */       }
/*  505 */       Stmnt b = parseBlock(tbl2);
/*  506 */       catchList = ASTList.append(catchList, (ASTree)new Pair((ASTree)d, (ASTree)b));
/*      */     } 
/*      */     
/*  509 */     Stmnt finallyBlock = null;
/*  510 */     if (this.lex.lookAhead() == 316) {
/*  511 */       this.lex.get();
/*  512 */       finallyBlock = parseBlock(tbl);
/*      */     } 
/*      */     
/*  515 */     return Stmnt.make(343, (ASTree)block, (ASTree)catchList, (ASTree)finallyBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseReturn(SymbolTable tbl) throws CompileError {
/*  521 */     int t = this.lex.get();
/*  522 */     Stmnt s = new Stmnt(t);
/*  523 */     if (this.lex.lookAhead() != 59) {
/*  524 */       s.setLeft(parseExpression(tbl));
/*      */     }
/*  526 */     if (this.lex.get() != 59) {
/*  527 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  529 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseThrow(SymbolTable tbl) throws CompileError {
/*  535 */     int t = this.lex.get();
/*  536 */     ASTree expr = parseExpression(tbl);
/*  537 */     if (this.lex.get() != 59) {
/*  538 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  540 */     return new Stmnt(t, expr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseBreak(SymbolTable tbl) throws CompileError {
/*  548 */     return parseContinue(tbl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseContinue(SymbolTable tbl) throws CompileError {
/*  556 */     int t = this.lex.get();
/*  557 */     Stmnt s = new Stmnt(t);
/*  558 */     int t2 = this.lex.get();
/*  559 */     if (t2 == 400) {
/*  560 */       s.setLeft((ASTree)new Symbol(this.lex.getString()));
/*  561 */       t2 = this.lex.get();
/*      */     } 
/*      */     
/*  564 */     if (t2 != 59) {
/*  565 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  567 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseDeclarationOrExpression(SymbolTable tbl, boolean exprList) throws CompileError {
/*      */     Stmnt expr;
/*  583 */     int t = this.lex.lookAhead();
/*  584 */     while (t == 315) {
/*  585 */       this.lex.get();
/*  586 */       t = this.lex.lookAhead();
/*      */     } 
/*      */     
/*  589 */     if (isBuiltinType(t)) {
/*  590 */       t = this.lex.get();
/*  591 */       int dim = parseArrayDimension();
/*  592 */       return parseDeclarators(tbl, new Declarator(t, dim));
/*      */     } 
/*  594 */     if (t == 400) {
/*  595 */       int i = nextIsClassType(0);
/*  596 */       if (i >= 0 && 
/*  597 */         this.lex.lookAhead(i) == 400) {
/*  598 */         ASTList name = parseClassType(tbl);
/*  599 */         int dim = parseArrayDimension();
/*  600 */         return parseDeclarators(tbl, new Declarator(name, dim));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  605 */     if (exprList) {
/*  606 */       expr = parseExprList(tbl);
/*      */     } else {
/*  608 */       expr = new Stmnt(69, parseExpression(tbl));
/*      */     } 
/*  610 */     if (this.lex.get() != 59) {
/*  611 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  613 */     return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseExprList(SymbolTable tbl) throws CompileError {
/*  619 */     Stmnt expr = null;
/*      */     while (true) {
/*  621 */       Stmnt e = new Stmnt(69, parseExpression(tbl));
/*  622 */       expr = (Stmnt)ASTList.concat((ASTList)expr, (ASTList)new Stmnt(66, (ASTree)e));
/*  623 */       if (this.lex.lookAhead() == 44) {
/*  624 */         this.lex.get(); continue;
/*      */       }  break;
/*  626 */     }  return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseDeclarators(SymbolTable tbl, Declarator d) throws CompileError {
/*  635 */     Stmnt decl = null;
/*      */     while (true) {
/*  637 */       decl = (Stmnt)ASTList.concat((ASTList)decl, (ASTList)new Stmnt(68, (ASTree)
/*  638 */             parseDeclarator(tbl, d)));
/*  639 */       int t = this.lex.get();
/*  640 */       if (t == 59)
/*  641 */         return decl; 
/*  642 */       if (t != 44) {
/*  643 */         throw new CompileError("; is missing", this.lex);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Declarator parseDeclarator(SymbolTable tbl, Declarator d) throws CompileError {
/*  652 */     if (this.lex.get() != 400 || d.getType() == 344) {
/*  653 */       throw new SyntaxError(this.lex);
/*      */     }
/*  655 */     String name = this.lex.getString();
/*  656 */     Symbol symbol = new Symbol(name);
/*  657 */     int dim = parseArrayDimension();
/*  658 */     ASTree init = null;
/*  659 */     if (this.lex.lookAhead() == 61) {
/*  660 */       this.lex.get();
/*  661 */       init = parseInitializer(tbl);
/*      */     } 
/*      */     
/*  664 */     Declarator decl = d.make(symbol, dim, init);
/*  665 */     tbl.append(name, decl);
/*  666 */     return decl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseInitializer(SymbolTable tbl) throws CompileError {
/*  672 */     if (this.lex.lookAhead() == 123)
/*  673 */       return (ASTree)parseArrayInitializer(tbl); 
/*  674 */     return parseExpression(tbl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayInit parseArrayInitializer(SymbolTable tbl) throws CompileError {
/*  683 */     this.lex.get();
/*  684 */     if (this.lex.lookAhead() == 125) {
/*  685 */       this.lex.get();
/*  686 */       return new ArrayInit(null);
/*      */     } 
/*  688 */     ASTree expr = parseExpression(tbl);
/*  689 */     ArrayInit init = new ArrayInit(expr);
/*  690 */     while (this.lex.lookAhead() == 44) {
/*  691 */       this.lex.get();
/*  692 */       expr = parseExpression(tbl);
/*  693 */       ASTList.append((ASTList)init, expr);
/*      */     } 
/*      */     
/*  696 */     if (this.lex.get() != 125) {
/*  697 */       throw new SyntaxError(this.lex);
/*      */     }
/*  699 */     return init;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseParExpression(SymbolTable tbl) throws CompileError {
/*  705 */     if (this.lex.get() != 40) {
/*  706 */       throw new SyntaxError(this.lex);
/*      */     }
/*  708 */     ASTree expr = parseExpression(tbl);
/*  709 */     if (this.lex.get() != 41) {
/*  710 */       throw new SyntaxError(this.lex);
/*      */     }
/*  712 */     return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ASTree parseExpression(SymbolTable tbl) throws CompileError {
/*  719 */     ASTree left = parseConditionalExpr(tbl);
/*  720 */     if (!isAssignOp(this.lex.lookAhead())) {
/*  721 */       return left;
/*      */     }
/*  723 */     int t = this.lex.get();
/*  724 */     ASTree right = parseExpression(tbl);
/*  725 */     return (ASTree)AssignExpr.makeAssign(t, left, right);
/*      */   }
/*      */   
/*      */   private static boolean isAssignOp(int t) {
/*  729 */     return (t == 61 || t == 351 || t == 352 || t == 353 || t == 354 || t == 355 || t == 356 || t == 360 || t == 361 || t == 365 || t == 367 || t == 371);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseConditionalExpr(SymbolTable tbl) throws CompileError {
/*  739 */     ASTree cond = parseBinaryExpr(tbl);
/*  740 */     if (this.lex.lookAhead() == 63) {
/*  741 */       this.lex.get();
/*  742 */       ASTree thenExpr = parseExpression(tbl);
/*  743 */       if (this.lex.get() != 58) {
/*  744 */         throw new CompileError(": is missing", this.lex);
/*      */       }
/*  746 */       ASTree elseExpr = parseExpression(tbl);
/*  747 */       return (ASTree)new CondExpr(cond, thenExpr, elseExpr);
/*      */     } 
/*  749 */     return cond;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseBinaryExpr(SymbolTable tbl) throws CompileError {
/*  794 */     ASTree expr = parseUnaryExpr(tbl);
/*      */     while (true) {
/*  796 */       int t = this.lex.lookAhead();
/*  797 */       int p = getOpPrecedence(t);
/*  798 */       if (p == 0)
/*  799 */         return expr; 
/*  800 */       expr = binaryExpr2(tbl, expr, p);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseInstanceOf(SymbolTable tbl, ASTree expr) throws CompileError {
/*  807 */     int t = this.lex.lookAhead();
/*  808 */     if (isBuiltinType(t)) {
/*  809 */       this.lex.get();
/*  810 */       int i = parseArrayDimension();
/*  811 */       return (ASTree)new InstanceOfExpr(t, i, expr);
/*      */     } 
/*  813 */     ASTList name = parseClassType(tbl);
/*  814 */     int dim = parseArrayDimension();
/*  815 */     return (ASTree)new InstanceOfExpr(name, dim, expr);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree binaryExpr2(SymbolTable tbl, ASTree expr, int prec) throws CompileError {
/*  821 */     int t = this.lex.get();
/*  822 */     if (t == 323) {
/*  823 */       return parseInstanceOf(tbl, expr);
/*      */     }
/*  825 */     ASTree expr2 = parseUnaryExpr(tbl);
/*      */     while (true) {
/*  827 */       int t2 = this.lex.lookAhead();
/*  828 */       int p2 = getOpPrecedence(t2);
/*  829 */       if (p2 != 0 && prec > p2) {
/*  830 */         expr2 = binaryExpr2(tbl, expr2, p2); continue;
/*      */       }  break;
/*  832 */     }  return (ASTree)BinExpr.makeBin(t, expr, expr2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  837 */   private static final int[] binaryOpPrecedence = new int[] { 0, 0, 0, 0, 1, 6, 0, 0, 0, 1, 2, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getOpPrecedence(int c) {
/*  844 */     if (33 <= c && c <= 63)
/*  845 */       return binaryOpPrecedence[c - 33]; 
/*  846 */     if (c == 94)
/*  847 */       return 7; 
/*  848 */     if (c == 124)
/*  849 */       return 8; 
/*  850 */     if (c == 369)
/*  851 */       return 9; 
/*  852 */     if (c == 368)
/*  853 */       return 10; 
/*  854 */     if (c == 358 || c == 350)
/*  855 */       return 5; 
/*  856 */     if (c == 357 || c == 359 || c == 323)
/*  857 */       return 4; 
/*  858 */     if (c == 364 || c == 366 || c == 370) {
/*  859 */       return 3;
/*      */     }
/*  861 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseUnaryExpr(SymbolTable tbl) throws CompileError {
/*      */     int t;
/*  875 */     switch (this.lex.lookAhead()) {
/*      */       case 33:
/*      */       case 43:
/*      */       case 45:
/*      */       case 126:
/*      */       case 362:
/*      */       case 363:
/*  882 */         t = this.lex.get();
/*  883 */         if (t == 45) {
/*  884 */           int t2 = this.lex.lookAhead();
/*  885 */           switch (t2) {
/*      */             case 401:
/*      */             case 402:
/*      */             case 403:
/*  889 */               this.lex.get();
/*  890 */               return (ASTree)new IntConst(-this.lex.getLong(), t2);
/*      */             case 404:
/*      */             case 405:
/*  893 */               this.lex.get();
/*  894 */               return (ASTree)new DoubleConst(-this.lex.getDouble(), t2);
/*      */           } 
/*      */ 
/*      */ 
/*      */         
/*      */         } 
/*  900 */         return (ASTree)Expr.make(t, parseUnaryExpr(tbl));
/*      */       case 40:
/*  902 */         return parseCast(tbl);
/*      */     } 
/*  904 */     return parsePostfix(tbl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseCast(SymbolTable tbl) throws CompileError {
/*  917 */     int t = this.lex.lookAhead(1);
/*  918 */     if (isBuiltinType(t) && nextIsBuiltinCast()) {
/*  919 */       this.lex.get();
/*  920 */       this.lex.get();
/*  921 */       int dim = parseArrayDimension();
/*  922 */       if (this.lex.get() != 41) {
/*  923 */         throw new CompileError(") is missing", this.lex);
/*      */       }
/*  925 */       return (ASTree)new CastExpr(t, dim, parseUnaryExpr(tbl));
/*      */     } 
/*  927 */     if (t == 400 && nextIsClassCast()) {
/*  928 */       this.lex.get();
/*  929 */       ASTList name = parseClassType(tbl);
/*  930 */       int dim = parseArrayDimension();
/*  931 */       if (this.lex.get() != 41) {
/*  932 */         throw new CompileError(") is missing", this.lex);
/*      */       }
/*  934 */       return (ASTree)new CastExpr(name, dim, parseUnaryExpr(tbl));
/*      */     } 
/*      */     
/*  937 */     return parsePostfix(tbl);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean nextIsBuiltinCast() {
/*  943 */     int i = 2; int t;
/*  944 */     while ((t = this.lex.lookAhead(i++)) == 91) {
/*  945 */       if (this.lex.lookAhead(i++) != 93)
/*  946 */         return false; 
/*      */     } 
/*  948 */     return (this.lex.lookAhead(i - 1) == 41);
/*      */   }
/*      */   
/*      */   private boolean nextIsClassCast() {
/*  952 */     int i = nextIsClassType(1);
/*  953 */     if (i < 0) {
/*  954 */       return false;
/*      */     }
/*  956 */     int t = this.lex.lookAhead(i);
/*  957 */     if (t != 41) {
/*  958 */       return false;
/*      */     }
/*  960 */     t = this.lex.lookAhead(i + 1);
/*  961 */     return (t == 40 || t == 412 || t == 406 || t == 400 || t == 339 || t == 336 || t == 328 || t == 410 || t == 411 || t == 403 || t == 402 || t == 401 || t == 405 || t == 404);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int nextIsClassType(int i) {
/*  971 */     while (this.lex.lookAhead(++i) == 46) {
/*  972 */       if (this.lex.lookAhead(++i) != 400)
/*  973 */         return -1; 
/*      */     }  int t;
/*  975 */     while ((t = this.lex.lookAhead(i++)) == 91) {
/*  976 */       if (this.lex.lookAhead(i++) != 93)
/*  977 */         return -1; 
/*      */     } 
/*  979 */     return i - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int parseArrayDimension() throws CompileError {
/*  985 */     int arrayDim = 0;
/*  986 */     while (this.lex.lookAhead() == 91) {
/*  987 */       arrayDim++;
/*  988 */       this.lex.get();
/*  989 */       if (this.lex.get() != 93) {
/*  990 */         throw new CompileError("] is missing", this.lex);
/*      */       }
/*      */     } 
/*  993 */     return arrayDim;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseClassType(SymbolTable tbl) throws CompileError {
/*  999 */     ASTList list = null;
/*      */     while (true) {
/* 1001 */       if (this.lex.get() != 400) {
/* 1002 */         throw new SyntaxError(this.lex);
/*      */       }
/* 1004 */       list = ASTList.append(list, (ASTree)new Symbol(this.lex.getString()));
/* 1005 */       if (this.lex.lookAhead() == 46) {
/* 1006 */         this.lex.get();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1011 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parsePostfix(SymbolTable tbl) throws CompileError {
/*      */     Expr expr1;
/* 1032 */     int token = this.lex.lookAhead();
/* 1033 */     switch (token) {
/*      */       case 401:
/*      */       case 402:
/*      */       case 403:
/* 1037 */         this.lex.get();
/* 1038 */         return (ASTree)new IntConst(this.lex.getLong(), token);
/*      */       case 404:
/*      */       case 405:
/* 1041 */         this.lex.get();
/* 1042 */         return (ASTree)new DoubleConst(this.lex.getDouble(), token);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1049 */     ASTree expr = parsePrimaryExpr(tbl); while (true) {
/*      */       String str; ASTree index; Expr expr2; ASTree aSTree1;
/*      */       int t;
/* 1052 */       switch (this.lex.lookAhead()) {
/*      */         case 40:
/* 1054 */           expr = parseMethodCall(tbl, expr);
/*      */           continue;
/*      */         case 91:
/* 1057 */           if (this.lex.lookAhead(1) == 93) {
/* 1058 */             int dim = parseArrayDimension();
/* 1059 */             if (this.lex.get() != 46 || this.lex.get() != 307) {
/* 1060 */               throw new SyntaxError(this.lex);
/*      */             }
/* 1062 */             expr = parseDotClass(expr, dim);
/*      */             continue;
/*      */           } 
/* 1065 */           index = parseArrayIndex(tbl);
/* 1066 */           if (index == null) {
/* 1067 */             throw new SyntaxError(this.lex);
/*      */           }
/* 1069 */           expr2 = Expr.make(65, expr, index);
/*      */           continue;
/*      */         
/*      */         case 362:
/*      */         case 363:
/* 1074 */           t = this.lex.get();
/* 1075 */           expr2 = Expr.make(t, null, (ASTree)expr2);
/*      */           continue;
/*      */         case 46:
/* 1078 */           this.lex.get();
/* 1079 */           t = this.lex.get();
/* 1080 */           if (t == 307) {
/* 1081 */             aSTree1 = parseDotClass((ASTree)expr2, 0); continue;
/* 1082 */           }  if (t == 336) {
/* 1083 */             expr1 = Expr.make(46, (ASTree)new Symbol(toClassName(aSTree1)), (ASTree)new Keyword(t)); continue;
/* 1084 */           }  if (t == 400) {
/* 1085 */             String str1 = this.lex.getString();
/* 1086 */             expr1 = Expr.make(46, (ASTree)expr1, (ASTree)new Member(str1));
/*      */             continue;
/*      */           } 
/* 1089 */           throw new CompileError("missing member name", this.lex);
/*      */         
/*      */         case 35:
/* 1092 */           this.lex.get();
/* 1093 */           t = this.lex.get();
/* 1094 */           if (t != 400) {
/* 1095 */             throw new CompileError("missing static member name", this.lex);
/*      */           }
/* 1097 */           str = this.lex.getString();
/* 1098 */           expr1 = Expr.make(35, (ASTree)new Symbol(toClassName((ASTree)expr1)), (ASTree)new Member(str)); continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1102 */     return (ASTree)expr1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseDotClass(ASTree className, int dim) throws CompileError {
/* 1114 */     String cname = toClassName(className);
/* 1115 */     if (dim > 0) {
/* 1116 */       StringBuffer sbuf = new StringBuffer();
/* 1117 */       while (dim-- > 0) {
/* 1118 */         sbuf.append('[');
/*      */       }
/* 1120 */       sbuf.append('L').append(cname.replace('.', '/')).append(';');
/* 1121 */       cname = sbuf.toString();
/*      */     } 
/*      */     
/* 1124 */     return (ASTree)Expr.make(46, (ASTree)new Symbol(cname), (ASTree)new Member("class"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseDotClass(int builtinType, int dim) throws CompileError {
/*      */     String cname;
/* 1134 */     if (dim > 0) {
/* 1135 */       String str = CodeGen.toJvmTypeName(builtinType, dim);
/* 1136 */       return (ASTree)Expr.make(46, (ASTree)new Symbol(str), (ASTree)new Member("class"));
/*      */     } 
/*      */     
/* 1139 */     switch (builtinType) {
/*      */       case 301:
/* 1141 */         cname = "java.lang.Boolean";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1172 */         return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 303: cname = "java.lang.Byte"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 306: cname = "java.lang.Character"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 334: cname = "java.lang.Short"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 324: cname = "java.lang.Integer"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 326: cname = "java.lang.Long"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 317: cname = "java.lang.Float"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 312: cname = "java.lang.Double"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 344: cname = "java.lang.Void"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));
/*      */     } 
/*      */     throw new CompileError("invalid builtin type: " + builtinType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseMethodCall(SymbolTable tbl, ASTree expr) throws CompileError {
/* 1183 */     if (expr instanceof Keyword) {
/* 1184 */       int token = ((Keyword)expr).get();
/* 1185 */       if (token != 339 && token != 336) {
/* 1186 */         throw new SyntaxError(this.lex);
/*      */       }
/* 1188 */     } else if (!(expr instanceof Symbol)) {
/*      */       
/* 1190 */       if (expr instanceof Expr) {
/* 1191 */         int op = ((Expr)expr).getOperator();
/* 1192 */         if (op != 46 && op != 35)
/* 1193 */           throw new SyntaxError(this.lex); 
/*      */       } 
/*      */     } 
/* 1196 */     return (ASTree)CallExpr.makeCall(expr, (ASTree)parseArgumentList(tbl));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String toClassName(ASTree name) throws CompileError {
/* 1202 */     StringBuffer sbuf = new StringBuffer();
/* 1203 */     toClassName(name, sbuf);
/* 1204 */     return sbuf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void toClassName(ASTree name, StringBuffer sbuf) throws CompileError {
/* 1210 */     if (name instanceof Symbol) {
/* 1211 */       sbuf.append(((Symbol)name).get());
/*      */       return;
/*      */     } 
/* 1214 */     if (name instanceof Expr) {
/* 1215 */       Expr expr = (Expr)name;
/* 1216 */       if (expr.getOperator() == 46) {
/* 1217 */         toClassName(expr.oprand1(), sbuf);
/* 1218 */         sbuf.append('.');
/* 1219 */         toClassName(expr.oprand2(), sbuf);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1224 */     throw new CompileError("bad static member access", this.lex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parsePrimaryExpr(SymbolTable tbl) throws CompileError {
/*      */     String name;
/*      */     Declarator decl;
/*      */     ASTree expr;
/*      */     int t;
/* 1243 */     switch (t = this.lex.get()) {
/*      */       case 336:
/*      */       case 339:
/*      */       case 410:
/*      */       case 411:
/*      */       case 412:
/* 1249 */         return (ASTree)new Keyword(t);
/*      */       case 400:
/* 1251 */         name = this.lex.getString();
/* 1252 */         decl = tbl.lookup(name);
/* 1253 */         if (decl == null)
/* 1254 */           return (ASTree)new Member(name); 
/* 1255 */         return (ASTree)new Variable(name, decl);
/*      */       case 406:
/* 1257 */         return (ASTree)new StringL(this.lex.getString());
/*      */       case 328:
/* 1259 */         return (ASTree)parseNew(tbl);
/*      */       case 40:
/* 1261 */         expr = parseExpression(tbl);
/* 1262 */         if (this.lex.get() == 41)
/* 1263 */           return expr; 
/* 1264 */         throw new CompileError(") is missing", this.lex);
/*      */     } 
/* 1266 */     if (isBuiltinType(t) || t == 344) {
/* 1267 */       int dim = parseArrayDimension();
/* 1268 */       if (this.lex.get() == 46 && this.lex.get() == 307) {
/* 1269 */         return parseDotClass(t, dim);
/*      */       }
/*      */     } 
/* 1272 */     throw new SyntaxError(this.lex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private NewExpr parseNew(SymbolTable tbl) throws CompileError {
/* 1281 */     ArrayInit init = null;
/* 1282 */     int t = this.lex.lookAhead();
/* 1283 */     if (isBuiltinType(t)) {
/* 1284 */       this.lex.get();
/* 1285 */       ASTList size = parseArraySize(tbl);
/* 1286 */       if (this.lex.lookAhead() == 123) {
/* 1287 */         init = parseArrayInitializer(tbl);
/*      */       }
/* 1289 */       return new NewExpr(t, size, init);
/*      */     } 
/* 1291 */     if (t == 400) {
/* 1292 */       ASTList name = parseClassType(tbl);
/* 1293 */       t = this.lex.lookAhead();
/* 1294 */       if (t == 40) {
/* 1295 */         ASTList args = parseArgumentList(tbl);
/* 1296 */         return new NewExpr(name, args);
/*      */       } 
/* 1298 */       if (t == 91) {
/* 1299 */         ASTList size = parseArraySize(tbl);
/* 1300 */         if (this.lex.lookAhead() == 123) {
/* 1301 */           init = parseArrayInitializer(tbl);
/*      */         }
/* 1303 */         return NewExpr.makeObjectArray(name, size, init);
/*      */       } 
/*      */     } 
/*      */     
/* 1307 */     throw new SyntaxError(this.lex);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseArraySize(SymbolTable tbl) throws CompileError {
/* 1313 */     ASTList list = null;
/* 1314 */     while (this.lex.lookAhead() == 91) {
/* 1315 */       list = ASTList.append(list, parseArrayIndex(tbl));
/*      */     }
/* 1317 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseArrayIndex(SymbolTable tbl) throws CompileError {
/* 1323 */     this.lex.get();
/* 1324 */     if (this.lex.lookAhead() == 93) {
/* 1325 */       this.lex.get();
/* 1326 */       return null;
/*      */     } 
/* 1328 */     ASTree index = parseExpression(tbl);
/* 1329 */     if (this.lex.get() != 93) {
/* 1330 */       throw new CompileError("] is missing", this.lex);
/*      */     }
/* 1332 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseArgumentList(SymbolTable tbl) throws CompileError {
/* 1338 */     if (this.lex.get() != 40) {
/* 1339 */       throw new CompileError("( is missing", this.lex);
/*      */     }
/* 1341 */     ASTList list = null;
/* 1342 */     if (this.lex.lookAhead() != 41)
/*      */       while (true) {
/* 1344 */         list = ASTList.append(list, parseExpression(tbl));
/* 1345 */         if (this.lex.lookAhead() == 44) {
/* 1346 */           this.lex.get();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       }  
/* 1351 */     if (this.lex.get() != 41) {
/* 1352 */       throw new CompileError(") is missing", this.lex);
/*      */     }
/* 1354 */     return list;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */