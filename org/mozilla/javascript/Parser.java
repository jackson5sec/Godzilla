/*      */ package org.mozilla.javascript;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.mozilla.javascript.ast.ArrayComprehension;
/*      */ import org.mozilla.javascript.ast.ArrayComprehensionLoop;
/*      */ import org.mozilla.javascript.ast.ArrayLiteral;
/*      */ import org.mozilla.javascript.ast.Assignment;
/*      */ import org.mozilla.javascript.ast.AstNode;
/*      */ import org.mozilla.javascript.ast.AstRoot;
/*      */ import org.mozilla.javascript.ast.Block;
/*      */ import org.mozilla.javascript.ast.BreakStatement;
/*      */ import org.mozilla.javascript.ast.CatchClause;
/*      */ import org.mozilla.javascript.ast.Comment;
/*      */ import org.mozilla.javascript.ast.ConditionalExpression;
/*      */ import org.mozilla.javascript.ast.ContinueStatement;
/*      */ import org.mozilla.javascript.ast.DoLoop;
/*      */ import org.mozilla.javascript.ast.ElementGet;
/*      */ import org.mozilla.javascript.ast.EmptyExpression;
/*      */ import org.mozilla.javascript.ast.EmptyStatement;
/*      */ import org.mozilla.javascript.ast.ExpressionStatement;
/*      */ import org.mozilla.javascript.ast.ForInLoop;
/*      */ import org.mozilla.javascript.ast.ForLoop;
/*      */ import org.mozilla.javascript.ast.FunctionCall;
/*      */ import org.mozilla.javascript.ast.FunctionNode;
/*      */ import org.mozilla.javascript.ast.GeneratorExpression;
/*      */ import org.mozilla.javascript.ast.GeneratorExpressionLoop;
/*      */ import org.mozilla.javascript.ast.IfStatement;
/*      */ import org.mozilla.javascript.ast.InfixExpression;
/*      */ import org.mozilla.javascript.ast.Jump;
/*      */ import org.mozilla.javascript.ast.KeywordLiteral;
/*      */ import org.mozilla.javascript.ast.Label;
/*      */ import org.mozilla.javascript.ast.LabeledStatement;
/*      */ import org.mozilla.javascript.ast.LetNode;
/*      */ import org.mozilla.javascript.ast.Loop;
/*      */ import org.mozilla.javascript.ast.Name;
/*      */ import org.mozilla.javascript.ast.NewExpression;
/*      */ import org.mozilla.javascript.ast.ObjectLiteral;
/*      */ import org.mozilla.javascript.ast.ObjectProperty;
/*      */ import org.mozilla.javascript.ast.ParenthesizedExpression;
/*      */ import org.mozilla.javascript.ast.PropertyGet;
/*      */ import org.mozilla.javascript.ast.ReturnStatement;
/*      */ import org.mozilla.javascript.ast.Scope;
/*      */ import org.mozilla.javascript.ast.StringLiteral;
/*      */ import org.mozilla.javascript.ast.SwitchCase;
/*      */ import org.mozilla.javascript.ast.SwitchStatement;
/*      */ import org.mozilla.javascript.ast.Symbol;
/*      */ import org.mozilla.javascript.ast.ThrowStatement;
/*      */ import org.mozilla.javascript.ast.TryStatement;
/*      */ import org.mozilla.javascript.ast.UnaryExpression;
/*      */ import org.mozilla.javascript.ast.VariableDeclaration;
/*      */ import org.mozilla.javascript.ast.VariableInitializer;
/*      */ import org.mozilla.javascript.ast.WhileLoop;
/*      */ import org.mozilla.javascript.ast.WithStatement;
/*      */ import org.mozilla.javascript.ast.XmlDotQuery;
/*      */ import org.mozilla.javascript.ast.XmlElemRef;
/*      */ import org.mozilla.javascript.ast.XmlLiteral;
/*      */ import org.mozilla.javascript.ast.XmlPropRef;
/*      */ 
/*      */ public class Parser {
/*      */   public static final int ARGC_LIMIT = 65536;
/*      */   static final int CLEAR_TI_MASK = 65535;
/*      */   static final int TI_AFTER_EOL = 65536;
/*   66 */   private int currentFlaggedToken = 0;
/*      */   
/*      */   static final int TI_CHECK_LABEL = 131072;
/*      */   CompilerEnvirons compilerEnv;
/*      */   private ErrorReporter errorReporter;
/*      */   private IdeErrorReporter errorCollector;
/*      */   private String sourceURI;
/*      */   private char[] sourceChars;
/*      */   boolean calledByCompileFunction;
/*      */   private boolean parseFinished;
/*      */   private TokenStream ts;
/*      */   private int currentToken;
/*      */   private int syntaxErrorCount;
/*      */   private List<Comment> scannedComments;
/*      */   private Comment currentJsDocComment;
/*      */   protected int nestingOfFunction;
/*      */   private LabeledStatement currentLabel;
/*      */   private boolean inDestructuringAssignment;
/*      */   protected boolean inUseStrictDirective;
/*      */   ScriptNode currentScriptOrFn;
/*      */   Scope currentScope;
/*      */   private int endFlags;
/*      */   private boolean inForInit;
/*      */   private Map<String, LabeledStatement> labelSet;
/*      */   private List<Loop> loopSet;
/*      */   private List<Jump> loopAndSwitchSet;
/*      */   private int prevNameTokenStart;
/*   93 */   private String prevNameTokenString = ""; private int prevNameTokenLineno; private static final int PROP_ENTRY = 1;
/*      */   private static final int GET_ENTRY = 2;
/*      */   private static final int SET_ENTRY = 4;
/*      */   
/*      */   private static class ParserException extends RuntimeException {
/*      */     static final long serialVersionUID = 5882582646773765630L;
/*      */     
/*      */     private ParserException() {} }
/*      */   
/*      */   public Parser() {
/*  103 */     this(new CompilerEnvirons());
/*      */   }
/*      */   
/*      */   public Parser(CompilerEnvirons compilerEnv) {
/*  107 */     this(compilerEnv, compilerEnv.getErrorReporter());
/*      */   }
/*      */   
/*      */   public Parser(CompilerEnvirons compilerEnv, ErrorReporter errorReporter) {
/*  111 */     this.compilerEnv = compilerEnv;
/*  112 */     this.errorReporter = errorReporter;
/*  113 */     if (errorReporter instanceof IdeErrorReporter) {
/*  114 */       this.errorCollector = (IdeErrorReporter)errorReporter;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void addStrictWarning(String messageId, String messageArg) {
/*  120 */     int beg = -1, end = -1;
/*  121 */     if (this.ts != null) {
/*  122 */       beg = this.ts.tokenBeg;
/*  123 */       end = this.ts.tokenEnd - this.ts.tokenBeg;
/*      */     } 
/*  125 */     addStrictWarning(messageId, messageArg, beg, end);
/*      */   }
/*      */ 
/*      */   
/*      */   void addStrictWarning(String messageId, String messageArg, int position, int length) {
/*  130 */     if (this.compilerEnv.isStrictMode())
/*  131 */       addWarning(messageId, messageArg, position, length); 
/*      */   }
/*      */   
/*      */   void addWarning(String messageId, String messageArg) {
/*  135 */     int beg = -1, end = -1;
/*  136 */     if (this.ts != null) {
/*  137 */       beg = this.ts.tokenBeg;
/*  138 */       end = this.ts.tokenEnd - this.ts.tokenBeg;
/*      */     } 
/*  140 */     addWarning(messageId, messageArg, beg, end);
/*      */   }
/*      */   
/*      */   void addWarning(String messageId, int position, int length) {
/*  144 */     addWarning(messageId, null, position, length);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void addWarning(String messageId, String messageArg, int position, int length) {
/*  150 */     String message = lookupMessage(messageId, messageArg);
/*  151 */     if (this.compilerEnv.reportWarningAsError()) {
/*  152 */       addError(messageId, messageArg, position, length);
/*  153 */     } else if (this.errorCollector != null) {
/*  154 */       this.errorCollector.warning(message, this.sourceURI, position, length);
/*      */     } else {
/*  156 */       this.errorReporter.warning(message, this.sourceURI, this.ts.getLineno(), this.ts.getLine(), this.ts.getOffset());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   void addError(String messageId) {
/*  162 */     addError(messageId, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */   }
/*      */   
/*      */   void addError(String messageId, int position, int length) {
/*  166 */     addError(messageId, null, position, length);
/*      */   }
/*      */   
/*      */   void addError(String messageId, String messageArg) {
/*  170 */     addError(messageId, messageArg, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void addError(String messageId, String messageArg, int position, int length) {
/*  176 */     this.syntaxErrorCount++;
/*  177 */     String message = lookupMessage(messageId, messageArg);
/*  178 */     if (this.errorCollector != null) {
/*  179 */       this.errorCollector.error(message, this.sourceURI, position, length);
/*      */     } else {
/*  181 */       int lineno = 1, offset = 1;
/*  182 */       String line = "";
/*  183 */       if (this.ts != null) {
/*  184 */         lineno = this.ts.getLineno();
/*  185 */         line = this.ts.getLine();
/*  186 */         offset = this.ts.getOffset();
/*      */       } 
/*  188 */       this.errorReporter.error(message, this.sourceURI, lineno, line, offset);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addStrictWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
/*  195 */     if (this.compilerEnv.isStrictMode()) {
/*  196 */       addWarning(messageId, messageArg, position, length, line, lineSource, lineOffset);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addWarning(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
/*  203 */     String message = lookupMessage(messageId, messageArg);
/*  204 */     if (this.compilerEnv.reportWarningAsError()) {
/*  205 */       addError(messageId, messageArg, position, length, line, lineSource, lineOffset);
/*  206 */     } else if (this.errorCollector != null) {
/*  207 */       this.errorCollector.warning(message, this.sourceURI, position, length);
/*      */     } else {
/*  209 */       this.errorReporter.warning(message, this.sourceURI, line, lineSource, lineOffset);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addError(String messageId, String messageArg, int position, int length, int line, String lineSource, int lineOffset) {
/*  216 */     this.syntaxErrorCount++;
/*  217 */     String message = lookupMessage(messageId, messageArg);
/*  218 */     if (this.errorCollector != null) {
/*  219 */       this.errorCollector.error(message, this.sourceURI, position, length);
/*      */     } else {
/*  221 */       this.errorReporter.error(message, this.sourceURI, line, lineSource, lineOffset);
/*      */     } 
/*      */   }
/*      */   
/*      */   String lookupMessage(String messageId) {
/*  226 */     return lookupMessage(messageId, null);
/*      */   }
/*      */   
/*      */   String lookupMessage(String messageId, String messageArg) {
/*  230 */     return (messageArg == null) ? ScriptRuntime.getMessage0(messageId) : ScriptRuntime.getMessage1(messageId, messageArg);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void reportError(String messageId) {
/*  236 */     reportError(messageId, null);
/*      */   }
/*      */   
/*      */   void reportError(String messageId, String messageArg) {
/*  240 */     if (this.ts == null) {
/*  241 */       reportError(messageId, messageArg, 1, 1);
/*      */     } else {
/*  243 */       reportError(messageId, messageArg, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void reportError(String messageId, int position, int length) {
/*  250 */     reportError(messageId, null, position, length);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void reportError(String messageId, String messageArg, int position, int length) {
/*  256 */     addError(messageId, position, length);
/*      */     
/*  258 */     if (!this.compilerEnv.recoverFromErrors()) {
/*  259 */       throw new ParserException();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getNodeEnd(AstNode n) {
/*  267 */     return n.getPosition() + n.getLength();
/*      */   }
/*      */   
/*      */   private void recordComment(int lineno, String comment) {
/*  271 */     if (this.scannedComments == null) {
/*  272 */       this.scannedComments = new ArrayList<Comment>();
/*      */     }
/*  274 */     Comment commentNode = new Comment(this.ts.tokenBeg, this.ts.getTokenLength(), this.ts.commentType, comment);
/*      */ 
/*      */ 
/*      */     
/*  278 */     if (this.ts.commentType == Token.CommentType.JSDOC && this.compilerEnv.isRecordingLocalJsDocComments())
/*      */     {
/*  280 */       this.currentJsDocComment = commentNode;
/*      */     }
/*  282 */     commentNode.setLineno(lineno);
/*  283 */     this.scannedComments.add(commentNode);
/*      */   }
/*      */   
/*      */   private Comment getAndResetJsDoc() {
/*  287 */     Comment saved = this.currentJsDocComment;
/*  288 */     this.currentJsDocComment = null;
/*  289 */     return saved;
/*      */   }
/*      */ 
/*      */   
/*      */   private int getNumberOfEols(String comment) {
/*  294 */     int lines = 0;
/*  295 */     for (int i = comment.length() - 1; i >= 0; i--) {
/*  296 */       if (comment.charAt(i) == '\n') {
/*  297 */         lines++;
/*      */       }
/*      */     } 
/*  300 */     return lines;
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
/*      */   private int peekToken() throws IOException {
/*  324 */     if (this.currentFlaggedToken != 0) {
/*  325 */       return this.currentToken;
/*      */     }
/*      */     
/*  328 */     int lineno = this.ts.getLineno();
/*  329 */     int tt = this.ts.getToken();
/*  330 */     boolean sawEOL = false;
/*      */ 
/*      */     
/*  333 */     while (tt == 1 || tt == 161) {
/*  334 */       if (tt == 1) {
/*  335 */         lineno++;
/*  336 */         sawEOL = true;
/*      */       }
/*  338 */       else if (this.compilerEnv.isRecordingComments()) {
/*  339 */         String comment = this.ts.getAndResetCurrentComment();
/*  340 */         recordComment(lineno, comment);
/*      */ 
/*      */         
/*  343 */         lineno += getNumberOfEols(comment);
/*      */       } 
/*      */       
/*  346 */       tt = this.ts.getToken();
/*      */     } 
/*      */     
/*  349 */     this.currentToken = tt;
/*  350 */     this.currentFlaggedToken = tt | (sawEOL ? 65536 : 0);
/*  351 */     return this.currentToken;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int peekFlaggedToken() throws IOException {
/*  357 */     peekToken();
/*  358 */     return this.currentFlaggedToken;
/*      */   }
/*      */   
/*      */   private void consumeToken() {
/*  362 */     this.currentFlaggedToken = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int nextToken() throws IOException {
/*  368 */     int tt = peekToken();
/*  369 */     consumeToken();
/*  370 */     return tt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int nextFlaggedToken() throws IOException {
/*  376 */     peekToken();
/*  377 */     int ttFlagged = this.currentFlaggedToken;
/*  378 */     consumeToken();
/*  379 */     return ttFlagged;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean matchToken(int toMatch) throws IOException {
/*  385 */     if (peekToken() != toMatch) {
/*  386 */       return false;
/*      */     }
/*  388 */     consumeToken();
/*  389 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int peekTokenOrEOL() throws IOException {
/*  400 */     int tt = peekToken();
/*      */     
/*  402 */     if ((this.currentFlaggedToken & 0x10000) != 0) {
/*  403 */       tt = 1;
/*      */     }
/*  405 */     return tt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean mustMatchToken(int toMatch, String messageId) throws IOException {
/*  411 */     return mustMatchToken(toMatch, messageId, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean mustMatchToken(int toMatch, String msgId, int pos, int len) throws IOException {
/*  418 */     if (matchToken(toMatch)) {
/*  419 */       return true;
/*      */     }
/*  421 */     reportError(msgId, pos, len);
/*  422 */     return false;
/*      */   }
/*      */   
/*      */   private void mustHaveXML() {
/*  426 */     if (!this.compilerEnv.isXmlAvailable()) {
/*  427 */       reportError("msg.XML.not.available");
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean eof() {
/*  432 */     return this.ts.eof();
/*      */   }
/*      */   
/*      */   boolean insideFunction() {
/*  436 */     return (this.nestingOfFunction != 0);
/*      */   }
/*      */   
/*      */   void pushScope(Scope scope) {
/*  440 */     Scope parent = scope.getParentScope();
/*      */ 
/*      */     
/*  443 */     if (parent != null) {
/*  444 */       if (parent != this.currentScope)
/*  445 */         codeBug(); 
/*      */     } else {
/*  447 */       this.currentScope.addChildScope(scope);
/*      */     } 
/*  449 */     this.currentScope = scope;
/*      */   }
/*      */   
/*      */   void popScope() {
/*  453 */     this.currentScope = this.currentScope.getParentScope();
/*      */   }
/*      */   
/*      */   private void enterLoop(Loop loop) {
/*  457 */     if (this.loopSet == null)
/*  458 */       this.loopSet = new ArrayList<Loop>(); 
/*  459 */     this.loopSet.add(loop);
/*  460 */     if (this.loopAndSwitchSet == null)
/*  461 */       this.loopAndSwitchSet = new ArrayList<Jump>(); 
/*  462 */     this.loopAndSwitchSet.add(loop);
/*  463 */     pushScope((Scope)loop);
/*  464 */     if (this.currentLabel != null) {
/*  465 */       this.currentLabel.setStatement((AstNode)loop);
/*  466 */       this.currentLabel.getFirstLabel().setLoop((Jump)loop);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  471 */       loop.setRelative(-this.currentLabel.getPosition());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void exitLoop() {
/*  476 */     Loop loop = this.loopSet.remove(this.loopSet.size() - 1);
/*  477 */     this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
/*  478 */     if (loop.getParent() != null) {
/*  479 */       loop.setRelative(loop.getParent().getPosition());
/*      */     }
/*  481 */     popScope();
/*      */   }
/*      */   
/*      */   private void enterSwitch(SwitchStatement node) {
/*  485 */     if (this.loopAndSwitchSet == null)
/*  486 */       this.loopAndSwitchSet = new ArrayList<Jump>(); 
/*  487 */     this.loopAndSwitchSet.add(node);
/*      */   }
/*      */   
/*      */   private void exitSwitch() {
/*  491 */     this.loopAndSwitchSet.remove(this.loopAndSwitchSet.size() - 1);
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
/*      */   public AstRoot parse(String sourceString, String sourceURI, int lineno) {
/*  504 */     if (this.parseFinished) throw new IllegalStateException("parser reused"); 
/*  505 */     this.sourceURI = sourceURI;
/*  506 */     if (this.compilerEnv.isIdeMode()) {
/*  507 */       this.sourceChars = sourceString.toCharArray();
/*      */     }
/*  509 */     this.ts = new TokenStream(this, null, sourceString, lineno);
/*      */     try {
/*  511 */       return parse();
/*  512 */     } catch (IOException iox) {
/*      */       
/*  514 */       throw new IllegalStateException();
/*      */     } finally {
/*  516 */       this.parseFinished = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AstRoot parse(Reader sourceReader, String sourceURI, int lineno) throws IOException {
/*  528 */     if (this.parseFinished) throw new IllegalStateException("parser reused"); 
/*  529 */     if (this.compilerEnv.isIdeMode()) {
/*  530 */       return parse(readFully(sourceReader), sourceURI, lineno);
/*      */     }
/*      */     try {
/*  533 */       this.sourceURI = sourceURI;
/*  534 */       this.ts = new TokenStream(this, sourceReader, null, lineno);
/*  535 */       return parse();
/*      */     } finally {
/*  537 */       this.parseFinished = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private AstRoot parse() throws IOException {
/*  543 */     int pos = 0;
/*  544 */     AstRoot root = new AstRoot(pos);
/*  545 */     this.currentScope = (Scope)(this.currentScriptOrFn = (ScriptNode)root);
/*      */     
/*  547 */     int baseLineno = this.ts.lineno;
/*  548 */     int end = pos;
/*      */     
/*  550 */     boolean inDirectivePrologue = true;
/*  551 */     boolean savedStrictMode = this.inUseStrictDirective;
/*      */     
/*  553 */     this.inUseStrictDirective = false;
/*      */     try {
/*      */       while (true) {
/*      */         AstNode astNode;
/*  557 */         int tt = peekToken();
/*  558 */         if (tt <= 0) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/*  563 */         if (tt == 109) {
/*  564 */           consumeToken();
/*      */           try {
/*  566 */             FunctionNode functionNode = function(this.calledByCompileFunction ? 2 : 1);
/*      */           
/*      */           }
/*  569 */           catch (ParserException e) {
/*      */             break;
/*      */           } 
/*      */         } else {
/*  573 */           astNode = statement();
/*  574 */           if (inDirectivePrologue) {
/*  575 */             String directive = getDirective(astNode);
/*  576 */             if (directive == null) {
/*  577 */               inDirectivePrologue = false;
/*  578 */             } else if (directive.equals("use strict")) {
/*  579 */               this.inUseStrictDirective = true;
/*  580 */               root.setInStrictMode(true);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  585 */         end = getNodeEnd(astNode);
/*  586 */         root.addChildToBack((Node)astNode);
/*  587 */         astNode.setParent((AstNode)root);
/*      */       } 
/*  589 */     } catch (StackOverflowError ex) {
/*  590 */       String msg = lookupMessage("msg.too.deep.parser.recursion");
/*  591 */       if (!this.compilerEnv.isIdeMode()) {
/*  592 */         throw Context.reportRuntimeError(msg, this.sourceURI, this.ts.lineno, null, 0);
/*      */       }
/*      */     } finally {
/*  595 */       this.inUseStrictDirective = savedStrictMode;
/*      */     } 
/*      */     
/*  598 */     if (this.syntaxErrorCount != 0) {
/*  599 */       String msg = String.valueOf(this.syntaxErrorCount);
/*  600 */       msg = lookupMessage("msg.got.syntax.errors", msg);
/*  601 */       if (!this.compilerEnv.isIdeMode()) {
/*  602 */         throw this.errorReporter.runtimeError(msg, this.sourceURI, baseLineno, null, 0);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  607 */     if (this.scannedComments != null) {
/*      */ 
/*      */       
/*  610 */       int last = this.scannedComments.size() - 1;
/*  611 */       end = Math.max(end, getNodeEnd((AstNode)this.scannedComments.get(last)));
/*  612 */       for (Comment c : this.scannedComments) {
/*  613 */         root.addComment(c);
/*      */       }
/*      */     } 
/*      */     
/*  617 */     root.setLength(end - pos);
/*  618 */     root.setSourceName(this.sourceURI);
/*  619 */     root.setBaseLineno(baseLineno);
/*  620 */     root.setEndLineno(this.ts.lineno);
/*  621 */     return root;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode parseFunctionBody() throws IOException {
/*  627 */     boolean isExpressionClosure = false;
/*  628 */     if (!matchToken(85)) {
/*  629 */       if (this.compilerEnv.getLanguageVersion() < 180) {
/*  630 */         reportError("msg.no.brace.body");
/*      */       } else {
/*  632 */         isExpressionClosure = true;
/*      */       } 
/*      */     }
/*  635 */     this.nestingOfFunction++;
/*  636 */     int pos = this.ts.tokenBeg;
/*  637 */     Block pn = new Block(pos);
/*      */     
/*  639 */     boolean inDirectivePrologue = true;
/*  640 */     boolean savedStrictMode = this.inUseStrictDirective;
/*      */ 
/*      */     
/*  643 */     pn.setLineno(this.ts.lineno);
/*      */     try {
/*  645 */       if (isExpressionClosure) {
/*  646 */         ReturnStatement n = new ReturnStatement(this.ts.lineno);
/*  647 */         n.setReturnValue(assignExpr());
/*      */         
/*  649 */         n.putProp(25, Boolean.TRUE);
/*  650 */         pn.putProp(25, Boolean.TRUE);
/*  651 */         pn.addStatement((AstNode)n);
/*      */       } else {
/*      */         while (true) {
/*      */           FunctionNode functionNode; AstNode astNode;
/*  655 */           int tt = peekToken();
/*  656 */           switch (tt) {
/*      */             case -1:
/*      */             case 0:
/*      */             case 86:
/*      */               break;
/*      */             
/*      */             case 109:
/*  663 */               consumeToken();
/*  664 */               functionNode = function(1);
/*      */               break;
/*      */             default:
/*  667 */               astNode = statement();
/*  668 */               if (inDirectivePrologue) {
/*  669 */                 String directive = getDirective(astNode);
/*  670 */                 if (directive == null) {
/*  671 */                   inDirectivePrologue = false; break;
/*  672 */                 }  if (directive.equals("use strict")) {
/*  673 */                   this.inUseStrictDirective = true;
/*      */                 }
/*      */               } 
/*      */               break;
/*      */           } 
/*  678 */           pn.addStatement(astNode);
/*      */         } 
/*      */       } 
/*  681 */     } catch (ParserException e) {
/*      */     
/*      */     } finally {
/*  684 */       this.nestingOfFunction--;
/*  685 */       this.inUseStrictDirective = savedStrictMode;
/*      */     } 
/*      */     
/*  688 */     int end = this.ts.tokenEnd;
/*  689 */     getAndResetJsDoc();
/*  690 */     if (!isExpressionClosure && mustMatchToken(86, "msg.no.brace.after.body"))
/*  691 */       end = this.ts.tokenEnd; 
/*  692 */     pn.setLength(end - pos);
/*  693 */     return (AstNode)pn;
/*      */   }
/*      */   
/*      */   private String getDirective(AstNode n) {
/*  697 */     if (n instanceof ExpressionStatement) {
/*  698 */       AstNode e = ((ExpressionStatement)n).getExpression();
/*  699 */       if (e instanceof StringLiteral) {
/*  700 */         return ((StringLiteral)e).getValue();
/*      */       }
/*      */     } 
/*  703 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseFunctionParams(FunctionNode fnNode) throws IOException {
/*  709 */     if (matchToken(88)) {
/*  710 */       fnNode.setRp(this.ts.tokenBeg - fnNode.getPosition());
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  715 */     Map<String, Node> destructuring = null;
/*  716 */     Set<String> paramNames = new HashSet<String>();
/*      */     do {
/*  718 */       int tt = peekToken();
/*  719 */       if (tt == 83 || tt == 85) {
/*  720 */         AstNode expr = destructuringPrimaryExpr();
/*  721 */         markDestructuring(expr);
/*  722 */         fnNode.addParam(expr);
/*      */ 
/*      */ 
/*      */         
/*  726 */         if (destructuring == null) {
/*  727 */           destructuring = new HashMap<String, Node>();
/*      */         }
/*  729 */         String pname = this.currentScriptOrFn.getNextTempName();
/*  730 */         defineSymbol(87, pname, false);
/*  731 */         destructuring.put(pname, expr);
/*      */       }
/*  733 */       else if (mustMatchToken(39, "msg.no.parm")) {
/*  734 */         fnNode.addParam((AstNode)createNameNode());
/*  735 */         String paramName = this.ts.getString();
/*  736 */         defineSymbol(87, paramName);
/*  737 */         if (this.inUseStrictDirective) {
/*  738 */           if ("eval".equals(paramName) || "arguments".equals(paramName))
/*      */           {
/*      */             
/*  741 */             reportError("msg.bad.id.strict", paramName);
/*      */           }
/*  743 */           if (paramNames.contains(paramName))
/*  744 */             addError("msg.dup.param.strict", paramName); 
/*  745 */           paramNames.add(paramName);
/*      */         } 
/*      */       } else {
/*  748 */         fnNode.addParam((AstNode)makeErrorNode());
/*      */       }
/*      */     
/*  751 */     } while (matchToken(89));
/*      */     
/*  753 */     if (destructuring != null) {
/*  754 */       Node destructuringNode = new Node(89);
/*      */       
/*  756 */       for (Map.Entry<String, Node> param : destructuring.entrySet()) {
/*  757 */         Node assign = createDestructuringAssignment(122, param.getValue(), createName(param.getKey()));
/*      */         
/*  759 */         destructuringNode.addChildToBack(assign);
/*      */       } 
/*      */       
/*  762 */       fnNode.putProp(23, destructuringNode);
/*      */     } 
/*      */     
/*  765 */     if (mustMatchToken(88, "msg.no.paren.after.parms")) {
/*  766 */       fnNode.setRp(this.ts.tokenBeg - fnNode.getPosition());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private FunctionNode function(int type) throws IOException {
/*  773 */     int syntheticType = type;
/*  774 */     int baseLineno = this.ts.lineno;
/*  775 */     int functionSourceStart = this.ts.tokenBeg;
/*  776 */     Name name = null;
/*  777 */     AstNode memberExprNode = null;
/*      */     
/*  779 */     if (matchToken(39)) {
/*  780 */       name = createNameNode(true, 39);
/*  781 */       if (this.inUseStrictDirective) {
/*  782 */         String id = name.getIdentifier();
/*  783 */         if ("eval".equals(id) || "arguments".equals(id)) {
/*  784 */           reportError("msg.bad.id.strict", id);
/*      */         }
/*      */       } 
/*  787 */       if (!matchToken(87)) {
/*  788 */         if (this.compilerEnv.isAllowMemberExprAsFunctionName()) {
/*  789 */           Name name1 = name;
/*  790 */           name = null;
/*  791 */           memberExprNode = memberExprTail(false, (AstNode)name1);
/*      */         } 
/*  793 */         mustMatchToken(87, "msg.no.paren.parms");
/*      */       } 
/*  795 */     } else if (!matchToken(87)) {
/*      */ 
/*      */       
/*  798 */       if (this.compilerEnv.isAllowMemberExprAsFunctionName())
/*      */       {
/*      */ 
/*      */         
/*  802 */         memberExprNode = memberExpr(false);
/*      */       }
/*  804 */       mustMatchToken(87, "msg.no.paren.parms");
/*      */     } 
/*  806 */     int lpPos = (this.currentToken == 87) ? this.ts.tokenBeg : -1;
/*      */     
/*  808 */     if (memberExprNode != null) {
/*  809 */       syntheticType = 2;
/*      */     }
/*      */     
/*  812 */     if (syntheticType != 2 && name != null && name.length() > 0)
/*      */     {
/*      */       
/*  815 */       defineSymbol(109, name.getIdentifier());
/*      */     }
/*      */     
/*  818 */     FunctionNode fnNode = new FunctionNode(functionSourceStart, name);
/*  819 */     fnNode.setFunctionType(type);
/*  820 */     if (lpPos != -1) {
/*  821 */       fnNode.setLp(lpPos - functionSourceStart);
/*      */     }
/*  823 */     fnNode.setJsDocNode(getAndResetJsDoc());
/*      */     
/*  825 */     PerFunctionVariables savedVars = new PerFunctionVariables(fnNode);
/*      */     try {
/*  827 */       parseFunctionParams(fnNode);
/*  828 */       fnNode.setBody(parseFunctionBody());
/*  829 */       fnNode.setEncodedSourceBounds(functionSourceStart, this.ts.tokenEnd);
/*  830 */       fnNode.setLength(this.ts.tokenEnd - functionSourceStart);
/*      */       
/*  832 */       if (this.compilerEnv.isStrictMode() && !fnNode.getBody().hasConsistentReturnUsage()) {
/*      */         
/*  834 */         String msg = (name != null && name.length() > 0) ? "msg.no.return.value" : "msg.anon.no.return.value";
/*      */ 
/*      */         
/*  837 */         addStrictWarning(msg, (name == null) ? "" : name.getIdentifier());
/*      */       } 
/*      */     } finally {
/*  840 */       savedVars.restore();
/*      */     } 
/*      */     
/*  843 */     if (memberExprNode != null) {
/*      */       
/*  845 */       Kit.codeBug();
/*  846 */       fnNode.setMemberExprNode(memberExprNode);
/*      */     } 
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
/*  858 */     fnNode.setSourceName(this.sourceURI);
/*  859 */     fnNode.setBaseLineno(baseLineno);
/*  860 */     fnNode.setEndLineno(this.ts.lineno);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  866 */     if (this.compilerEnv.isIdeMode()) {
/*  867 */       fnNode.setParentScope(this.currentScope);
/*      */     }
/*  869 */     return fnNode;
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
/*      */   private AstNode statements(AstNode parent) throws IOException {
/*  881 */     if (this.currentToken != 85 && !this.compilerEnv.isIdeMode())
/*  882 */       codeBug(); 
/*  883 */     int pos = this.ts.tokenBeg;
/*  884 */     AstNode block = (parent != null) ? parent : (AstNode)new Block(pos);
/*  885 */     block.setLineno(this.ts.lineno);
/*      */     
/*      */     int tt;
/*  888 */     while ((tt = peekToken()) > 0 && tt != 86) {
/*  889 */       block.addChild(statement());
/*      */     }
/*  891 */     block.setLength(this.ts.tokenBeg - pos);
/*  892 */     return block;
/*      */   }
/*      */   
/*      */   private AstNode statements() throws IOException {
/*  896 */     return statements(null);
/*      */   }
/*      */   private static class ConditionData { AstNode condition;
/*      */     private ConditionData() {}
/*      */     
/*  901 */     int lp = -1;
/*  902 */     int rp = -1; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ConditionData condition() throws IOException {
/*  909 */     ConditionData data = new ConditionData();
/*      */     
/*  911 */     if (mustMatchToken(87, "msg.no.paren.cond")) {
/*  912 */       data.lp = this.ts.tokenBeg;
/*      */     }
/*  914 */     data.condition = expr();
/*      */     
/*  916 */     if (mustMatchToken(88, "msg.no.paren.after.cond")) {
/*  917 */       data.rp = this.ts.tokenBeg;
/*      */     }
/*      */ 
/*      */     
/*  921 */     if (data.condition instanceof Assignment) {
/*  922 */       addStrictWarning("msg.equal.as.assign", "", data.condition.getPosition(), data.condition.getLength());
/*      */     }
/*      */ 
/*      */     
/*  926 */     return data;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode statement() throws IOException
/*      */   {
/*  932 */     int pos = this.ts.tokenBeg;
/*      */     try {
/*  934 */       AstNode pn = statementHelper();
/*  935 */       if (pn != null) {
/*  936 */         if (this.compilerEnv.isStrictMode() && !pn.hasSideEffects()) {
/*  937 */           int beg = pn.getPosition();
/*  938 */           beg = Math.max(beg, lineBeginningFor(beg));
/*  939 */           addStrictWarning((pn instanceof EmptyStatement) ? "msg.extra.trailing.semi" : "msg.no.side.effects", "", beg, nodeEnd(pn) - beg);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  944 */         return pn;
/*      */       } 
/*  946 */     } catch (ParserException e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  952 */       int tt = peekTokenOrEOL();
/*  953 */       consumeToken();
/*  954 */       switch (tt) {
/*      */         case -1:
/*      */         case 0:
/*      */         case 1:
/*      */         case 82:
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/*  965 */     return (AstNode)new EmptyStatement(pos, this.ts.tokenBeg - pos); } private AstNode statementHelper() throws IOException { ThrowStatement throwStatement; BreakStatement breakStatement;
/*      */     ContinueStatement continueStatement;
/*      */     VariableDeclaration variableDeclaration;
/*      */     AstNode astNode2;
/*      */     KeywordLiteral keywordLiteral;
/*      */     EmptyStatement emptyStatement;
/*      */     AstNode astNode1;
/*  972 */     if (this.currentLabel != null && this.currentLabel.getStatement() != null) {
/*  973 */       this.currentLabel = null;
/*      */     }
/*  975 */     AstNode pn = null;
/*  976 */     int tt = peekToken(), pos = this.ts.tokenBeg;
/*      */     
/*  978 */     switch (tt)
/*      */     { case 112:
/*  980 */         return (AstNode)ifStatement();
/*      */       
/*      */       case 114:
/*  983 */         return (AstNode)switchStatement();
/*      */       
/*      */       case 117:
/*  986 */         return (AstNode)whileLoop();
/*      */       
/*      */       case 118:
/*  989 */         return (AstNode)doLoop();
/*      */       
/*      */       case 119:
/*  992 */         return (AstNode)forLoop();
/*      */       
/*      */       case 81:
/*  995 */         return (AstNode)tryStatement();
/*      */       
/*      */       case 50:
/*  998 */         throwStatement = throwStatement();
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
/* 1077 */         autoInsertSemicolon((AstNode)throwStatement);
/* 1078 */         return (AstNode)throwStatement;case 120: breakStatement = breakStatement(); autoInsertSemicolon((AstNode)breakStatement); return (AstNode)breakStatement;case 121: continueStatement = continueStatement(); autoInsertSemicolon((AstNode)continueStatement); return (AstNode)continueStatement;case 123: if (this.inUseStrictDirective) reportError("msg.no.with.strict");  return (AstNode)withStatement();case 122: case 154: consumeToken(); lineno = this.ts.lineno; variableDeclaration = variables(this.currentToken, this.ts.tokenBeg, true); variableDeclaration.setLineno(lineno); autoInsertSemicolon((AstNode)variableDeclaration); return (AstNode)variableDeclaration;case 153: astNode2 = letStatement(); if (!(astNode2 instanceof VariableDeclaration) || peekToken() != 82) return astNode2;  autoInsertSemicolon(astNode2); return astNode2;case 4: case 72: astNode2 = returnOrYield(tt, false); autoInsertSemicolon(astNode2); return astNode2;case 160: consumeToken(); keywordLiteral = new KeywordLiteral(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg, tt); keywordLiteral.setLineno(this.ts.lineno); autoInsertSemicolon((AstNode)keywordLiteral); return (AstNode)keywordLiteral;case 85: return block();case -1: consumeToken(); return (AstNode)makeErrorNode();case 82: consumeToken(); pos = this.ts.tokenBeg; emptyStatement = new EmptyStatement(pos, this.ts.tokenEnd - pos); emptyStatement.setLineno(this.ts.lineno); return (AstNode)emptyStatement;case 109: consumeToken(); return (AstNode)function(3);case 116: astNode1 = defaultXmlNamespace(); autoInsertSemicolon(astNode1); return astNode1;case 39: astNode1 = nameOrLabel(); if (!(astNode1 instanceof ExpressionStatement)) return astNode1;  autoInsertSemicolon(astNode1); return astNode1; }  int lineno = this.ts.lineno; ExpressionStatement expressionStatement = new ExpressionStatement(expr(), !insideFunction()); expressionStatement.setLineno(lineno); autoInsertSemicolon((AstNode)expressionStatement); return (AstNode)expressionStatement; }
/*      */ 
/*      */   
/*      */   private void autoInsertSemicolon(AstNode pn) throws IOException {
/* 1082 */     int ttFlagged = peekFlaggedToken();
/* 1083 */     int pos = pn.getPosition();
/* 1084 */     switch (ttFlagged & 0xFFFF) {
/*      */       
/*      */       case 82:
/* 1087 */         consumeToken();
/*      */         
/* 1089 */         pn.setLength(this.ts.tokenEnd - pos);
/*      */         return;
/*      */       
/*      */       case -1:
/*      */       case 0:
/*      */       case 86:
/* 1095 */         warnMissingSemi(pos, nodeEnd(pn));
/*      */         return;
/*      */     } 
/* 1098 */     if ((ttFlagged & 0x10000) == 0) {
/*      */       
/* 1100 */       reportError("msg.no.semi.stmt");
/*      */     } else {
/* 1102 */       warnMissingSemi(pos, nodeEnd(pn));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IfStatement ifStatement() throws IOException {
/* 1111 */     if (this.currentToken != 112) codeBug(); 
/* 1112 */     consumeToken();
/* 1113 */     int pos = this.ts.tokenBeg, lineno = this.ts.lineno, elsePos = -1;
/* 1114 */     ConditionData data = condition();
/* 1115 */     AstNode ifTrue = statement(), ifFalse = null;
/* 1116 */     if (matchToken(113)) {
/* 1117 */       elsePos = this.ts.tokenBeg - pos;
/* 1118 */       ifFalse = statement();
/*      */     } 
/* 1120 */     int end = getNodeEnd((ifFalse != null) ? ifFalse : ifTrue);
/* 1121 */     IfStatement pn = new IfStatement(pos, end - pos);
/* 1122 */     pn.setCondition(data.condition);
/* 1123 */     pn.setParens(data.lp - pos, data.rp - pos);
/* 1124 */     pn.setThenPart(ifTrue);
/* 1125 */     pn.setElsePart(ifFalse);
/* 1126 */     pn.setElsePosition(elsePos);
/* 1127 */     pn.setLineno(lineno);
/* 1128 */     return pn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private SwitchStatement switchStatement() throws IOException {
/* 1134 */     if (this.currentToken != 114) codeBug(); 
/* 1135 */     consumeToken();
/* 1136 */     int pos = this.ts.tokenBeg;
/*      */     
/* 1138 */     SwitchStatement pn = new SwitchStatement(pos);
/* 1139 */     if (mustMatchToken(87, "msg.no.paren.switch"))
/* 1140 */       pn.setLp(this.ts.tokenBeg - pos); 
/* 1141 */     pn.setLineno(this.ts.lineno);
/*      */     
/* 1143 */     AstNode discriminant = expr();
/* 1144 */     pn.setExpression(discriminant);
/* 1145 */     enterSwitch(pn);
/*      */     
/*      */     try {
/* 1148 */       if (mustMatchToken(88, "msg.no.paren.after.switch")) {
/* 1149 */         pn.setRp(this.ts.tokenBeg - pos);
/*      */       }
/* 1151 */       mustMatchToken(85, "msg.no.brace.switch");
/*      */       
/* 1153 */       boolean hasDefault = false;
/*      */       
/*      */       while (true) {
/* 1156 */         int tt = nextToken();
/* 1157 */         int casePos = this.ts.tokenBeg;
/* 1158 */         int caseLineno = this.ts.lineno;
/* 1159 */         AstNode caseExpression = null;
/* 1160 */         switch (tt) {
/*      */           case 86:
/* 1162 */             pn.setLength(this.ts.tokenEnd - pos);
/*      */             break;
/*      */           
/*      */           case 115:
/* 1166 */             caseExpression = expr();
/* 1167 */             mustMatchToken(103, "msg.no.colon.case");
/*      */             break;
/*      */           
/*      */           case 116:
/* 1171 */             if (hasDefault) {
/* 1172 */               reportError("msg.double.switch.default");
/*      */             }
/* 1174 */             hasDefault = true;
/* 1175 */             caseExpression = null;
/* 1176 */             mustMatchToken(103, "msg.no.colon.case");
/*      */             break;
/*      */           
/*      */           default:
/* 1180 */             reportError("msg.bad.switch");
/*      */             break;
/*      */         } 
/*      */         
/* 1184 */         SwitchCase caseNode = new SwitchCase(casePos);
/* 1185 */         caseNode.setExpression(caseExpression);
/* 1186 */         caseNode.setLength(this.ts.tokenEnd - pos);
/* 1187 */         caseNode.setLineno(caseLineno);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1192 */         while ((tt = peekToken()) != 86 && tt != 115 && tt != 116 && tt != 0)
/*      */         {
/* 1194 */           caseNode.addStatement(statement());
/*      */         }
/* 1196 */         pn.addCase(caseNode);
/*      */       } 
/*      */     } finally {
/* 1199 */       exitSwitch();
/*      */     } 
/* 1201 */     return pn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private WhileLoop whileLoop() throws IOException {
/* 1207 */     if (this.currentToken != 117) codeBug(); 
/* 1208 */     consumeToken();
/* 1209 */     int pos = this.ts.tokenBeg;
/* 1210 */     WhileLoop pn = new WhileLoop(pos);
/* 1211 */     pn.setLineno(this.ts.lineno);
/* 1212 */     enterLoop((Loop)pn);
/*      */     try {
/* 1214 */       ConditionData data = condition();
/* 1215 */       pn.setCondition(data.condition);
/* 1216 */       pn.setParens(data.lp - pos, data.rp - pos);
/* 1217 */       AstNode body = statement();
/* 1218 */       pn.setLength(getNodeEnd(body) - pos);
/* 1219 */       pn.setBody(body);
/*      */     } finally {
/* 1221 */       exitLoop();
/*      */     } 
/* 1223 */     return pn;
/*      */   }
/*      */ 
/*      */   
/*      */   private DoLoop doLoop() throws IOException {
/*      */     int i;
/* 1229 */     if (this.currentToken != 118) codeBug(); 
/* 1230 */     consumeToken();
/* 1231 */     int pos = this.ts.tokenBeg;
/* 1232 */     DoLoop pn = new DoLoop(pos);
/* 1233 */     pn.setLineno(this.ts.lineno);
/* 1234 */     enterLoop((Loop)pn);
/*      */     try {
/* 1236 */       AstNode body = statement();
/* 1237 */       mustMatchToken(117, "msg.no.while.do");
/* 1238 */       pn.setWhilePosition(this.ts.tokenBeg - pos);
/* 1239 */       ConditionData data = condition();
/* 1240 */       pn.setCondition(data.condition);
/* 1241 */       pn.setParens(data.lp - pos, data.rp - pos);
/* 1242 */       i = getNodeEnd(body);
/* 1243 */       pn.setBody(body);
/*      */     } finally {
/* 1245 */       exitLoop();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1250 */     if (matchToken(82)) {
/* 1251 */       i = this.ts.tokenEnd;
/*      */     }
/* 1253 */     pn.setLength(i - pos);
/* 1254 */     return pn;
/*      */   }
/*      */ 
/*      */   
/*      */   private Loop forLoop() throws IOException {
/*      */     ForLoop forLoop;
/* 1260 */     if (this.currentToken != 119) codeBug(); 
/* 1261 */     consumeToken();
/* 1262 */     int forPos = this.ts.tokenBeg, lineno = this.ts.lineno;
/* 1263 */     boolean isForEach = false, isForIn = false;
/* 1264 */     int eachPos = -1, inPos = -1, lp = -1, rp = -1;
/* 1265 */     AstNode init = null;
/* 1266 */     AstNode cond = null;
/* 1267 */     AstNode incr = null;
/* 1268 */     Loop pn = null;
/*      */     
/* 1270 */     Scope tempScope = new Scope();
/* 1271 */     pushScope(tempScope);
/*      */     
/*      */     try {
/* 1274 */       if (matchToken(39)) {
/* 1275 */         if ("each".equals(this.ts.getString())) {
/* 1276 */           isForEach = true;
/* 1277 */           eachPos = this.ts.tokenBeg - forPos;
/*      */         } else {
/* 1279 */           reportError("msg.no.paren.for");
/*      */         } 
/*      */       }
/*      */       
/* 1283 */       if (mustMatchToken(87, "msg.no.paren.for"))
/* 1284 */         lp = this.ts.tokenBeg - forPos; 
/* 1285 */       int tt = peekToken();
/*      */       
/* 1287 */       init = forLoopInit(tt);
/*      */       
/* 1289 */       if (matchToken(52)) {
/* 1290 */         isForIn = true;
/* 1291 */         inPos = this.ts.tokenBeg - forPos;
/* 1292 */         cond = expr();
/*      */       } else {
/* 1294 */         mustMatchToken(82, "msg.no.semi.for");
/* 1295 */         if (peekToken() == 82) {
/*      */           
/* 1297 */           EmptyExpression emptyExpression = new EmptyExpression(this.ts.tokenBeg, 1);
/* 1298 */           emptyExpression.setLineno(this.ts.lineno);
/*      */         } else {
/* 1300 */           cond = expr();
/*      */         } 
/*      */         
/* 1303 */         mustMatchToken(82, "msg.no.semi.for.cond");
/* 1304 */         int tmpPos = this.ts.tokenEnd;
/* 1305 */         if (peekToken() == 88) {
/* 1306 */           EmptyExpression emptyExpression = new EmptyExpression(tmpPos, 1);
/* 1307 */           emptyExpression.setLineno(this.ts.lineno);
/*      */         } else {
/* 1309 */           incr = expr();
/*      */         } 
/*      */       } 
/*      */       
/* 1313 */       if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
/* 1314 */         rp = this.ts.tokenBeg - forPos;
/*      */       }
/* 1316 */       if (isForIn) {
/* 1317 */         ForInLoop fis = new ForInLoop(forPos);
/* 1318 */         if (init instanceof VariableDeclaration)
/*      */         {
/* 1320 */           if (((VariableDeclaration)init).getVariables().size() > 1) {
/* 1321 */             reportError("msg.mult.index");
/*      */           }
/*      */         }
/* 1324 */         fis.setIterator(init);
/* 1325 */         fis.setIteratedObject(cond);
/* 1326 */         fis.setInPosition(inPos);
/* 1327 */         fis.setIsForEach(isForEach);
/* 1328 */         fis.setEachPosition(eachPos);
/* 1329 */         ForInLoop forInLoop1 = fis;
/*      */       } else {
/* 1331 */         ForLoop fl = new ForLoop(forPos);
/* 1332 */         fl.setInitializer(init);
/* 1333 */         fl.setCondition(cond);
/* 1334 */         fl.setIncrement(incr);
/* 1335 */         forLoop = fl;
/*      */       } 
/*      */ 
/*      */       
/* 1339 */       this.currentScope.replaceWith((Scope)forLoop);
/* 1340 */       popScope();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1345 */       enterLoop((Loop)forLoop);
/*      */       try {
/* 1347 */         AstNode body = statement();
/* 1348 */         forLoop.setLength(getNodeEnd(body) - forPos);
/* 1349 */         forLoop.setBody(body);
/*      */       } finally {
/* 1351 */         exitLoop();
/*      */       } 
/*      */     } finally {
/*      */       
/* 1355 */       if (this.currentScope == tempScope) {
/* 1356 */         popScope();
/*      */       }
/*      */     } 
/* 1359 */     forLoop.setParens(lp, rp);
/* 1360 */     forLoop.setLineno(lineno);
/* 1361 */     return (Loop)forLoop;
/*      */   }
/*      */   
/*      */   private AstNode forLoopInit(int tt) throws IOException {
/*      */     try {
/* 1366 */       this.inForInit = true;
/* 1367 */       AstNode init = null;
/* 1368 */       if (tt == 82) {
/* 1369 */         EmptyExpression emptyExpression = new EmptyExpression(this.ts.tokenBeg, 1);
/* 1370 */         emptyExpression.setLineno(this.ts.lineno);
/* 1371 */       } else if (tt == 122 || tt == 153) {
/* 1372 */         consumeToken();
/* 1373 */         VariableDeclaration variableDeclaration = variables(tt, this.ts.tokenBeg, false);
/*      */       } else {
/* 1375 */         init = expr();
/* 1376 */         markDestructuring(init);
/*      */       } 
/* 1378 */       return init;
/*      */     } finally {
/* 1380 */       this.inForInit = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private TryStatement tryStatement() throws IOException {
/* 1387 */     if (this.currentToken != 81) codeBug(); 
/* 1388 */     consumeToken();
/*      */ 
/*      */     
/* 1391 */     Comment jsdocNode = getAndResetJsDoc();
/*      */     
/* 1393 */     int tryPos = this.ts.tokenBeg, lineno = this.ts.lineno, finallyPos = -1;
/* 1394 */     if (peekToken() != 85) {
/* 1395 */       reportError("msg.no.brace.try");
/*      */     }
/* 1397 */     AstNode tryBlock = statement();
/* 1398 */     int tryEnd = getNodeEnd(tryBlock);
/*      */     
/* 1400 */     List<CatchClause> clauses = null;
/*      */     
/* 1402 */     boolean sawDefaultCatch = false;
/* 1403 */     int peek = peekToken();
/* 1404 */     if (peek == 124) {
/* 1405 */       while (matchToken(124)) {
/* 1406 */         int catchLineNum = this.ts.lineno;
/* 1407 */         if (sawDefaultCatch) {
/* 1408 */           reportError("msg.catch.unreachable");
/*      */         }
/* 1410 */         int catchPos = this.ts.tokenBeg, lp = -1, rp = -1, guardPos = -1;
/* 1411 */         if (mustMatchToken(87, "msg.no.paren.catch")) {
/* 1412 */           lp = this.ts.tokenBeg;
/*      */         }
/* 1414 */         mustMatchToken(39, "msg.bad.catchcond");
/* 1415 */         Name varName = createNameNode();
/* 1416 */         String varNameString = varName.getIdentifier();
/* 1417 */         if (this.inUseStrictDirective && (
/* 1418 */           "eval".equals(varNameString) || "arguments".equals(varNameString)))
/*      */         {
/*      */           
/* 1421 */           reportError("msg.bad.id.strict", varNameString);
/*      */         }
/*      */ 
/*      */         
/* 1425 */         AstNode catchCond = null;
/* 1426 */         if (matchToken(112)) {
/* 1427 */           guardPos = this.ts.tokenBeg;
/* 1428 */           catchCond = expr();
/*      */         } else {
/* 1430 */           sawDefaultCatch = true;
/*      */         } 
/*      */         
/* 1433 */         if (mustMatchToken(88, "msg.bad.catchcond"))
/* 1434 */           rp = this.ts.tokenBeg; 
/* 1435 */         mustMatchToken(85, "msg.no.brace.catchblock");
/*      */         
/* 1437 */         Block catchBlock = (Block)statements();
/* 1438 */         tryEnd = getNodeEnd((AstNode)catchBlock);
/* 1439 */         CatchClause catchNode = new CatchClause(catchPos);
/* 1440 */         catchNode.setVarName(varName);
/* 1441 */         catchNode.setCatchCondition(catchCond);
/* 1442 */         catchNode.setBody(catchBlock);
/* 1443 */         if (guardPos != -1) {
/* 1444 */           catchNode.setIfPosition(guardPos - catchPos);
/*      */         }
/* 1446 */         catchNode.setParens(lp, rp);
/* 1447 */         catchNode.setLineno(catchLineNum);
/*      */         
/* 1449 */         if (mustMatchToken(86, "msg.no.brace.after.body"))
/* 1450 */           tryEnd = this.ts.tokenEnd; 
/* 1451 */         catchNode.setLength(tryEnd - catchPos);
/* 1452 */         if (clauses == null)
/* 1453 */           clauses = new ArrayList<CatchClause>(); 
/* 1454 */         clauses.add(catchNode);
/*      */       } 
/* 1456 */     } else if (peek != 125) {
/* 1457 */       mustMatchToken(125, "msg.try.no.catchfinally");
/*      */     } 
/*      */     
/* 1460 */     AstNode finallyBlock = null;
/* 1461 */     if (matchToken(125)) {
/* 1462 */       finallyPos = this.ts.tokenBeg;
/* 1463 */       finallyBlock = statement();
/* 1464 */       tryEnd = getNodeEnd(finallyBlock);
/*      */     } 
/*      */     
/* 1467 */     TryStatement pn = new TryStatement(tryPos, tryEnd - tryPos);
/* 1468 */     pn.setTryBlock(tryBlock);
/* 1469 */     pn.setCatchClauses(clauses);
/* 1470 */     pn.setFinallyBlock(finallyBlock);
/* 1471 */     if (finallyPos != -1) {
/* 1472 */       pn.setFinallyPosition(finallyPos - tryPos);
/*      */     }
/* 1474 */     pn.setLineno(lineno);
/*      */     
/* 1476 */     if (jsdocNode != null) {
/* 1477 */       pn.setJsDocNode(jsdocNode);
/*      */     }
/*      */     
/* 1480 */     return pn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ThrowStatement throwStatement() throws IOException {
/* 1486 */     if (this.currentToken != 50) codeBug(); 
/* 1487 */     consumeToken();
/* 1488 */     int pos = this.ts.tokenBeg, lineno = this.ts.lineno;
/* 1489 */     if (peekTokenOrEOL() == 1)
/*      */     {
/*      */       
/* 1492 */       reportError("msg.bad.throw.eol");
/*      */     }
/* 1494 */     AstNode expr = expr();
/* 1495 */     ThrowStatement pn = new ThrowStatement(pos, getNodeEnd(expr), expr);
/* 1496 */     pn.setLineno(lineno);
/* 1497 */     return pn;
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
/*      */   private LabeledStatement matchJumpLabelName() throws IOException {
/* 1509 */     LabeledStatement label = null;
/*      */     
/* 1511 */     if (peekTokenOrEOL() == 39) {
/* 1512 */       consumeToken();
/* 1513 */       if (this.labelSet != null) {
/* 1514 */         label = this.labelSet.get(this.ts.getString());
/*      */       }
/* 1516 */       if (label == null) {
/* 1517 */         reportError("msg.undef.label");
/*      */       }
/*      */     } 
/*      */     
/* 1521 */     return label;
/*      */   }
/*      */ 
/*      */   
/*      */   private BreakStatement breakStatement() throws IOException {
/*      */     Jump jump;
/* 1527 */     if (this.currentToken != 120) codeBug(); 
/* 1528 */     consumeToken();
/* 1529 */     int lineno = this.ts.lineno, pos = this.ts.tokenBeg, end = this.ts.tokenEnd;
/* 1530 */     Name breakLabel = null;
/* 1531 */     if (peekTokenOrEOL() == 39) {
/* 1532 */       breakLabel = createNameNode();
/* 1533 */       end = getNodeEnd((AstNode)breakLabel);
/*      */     } 
/*      */ 
/*      */     
/* 1537 */     LabeledStatement labels = matchJumpLabelName();
/*      */     
/* 1539 */     Label label = (labels == null) ? null : labels.getFirstLabel();
/*      */     
/* 1541 */     if (label == null && breakLabel == null) {
/* 1542 */       if (this.loopAndSwitchSet == null || this.loopAndSwitchSet.size() == 0) {
/* 1543 */         if (breakLabel == null) {
/* 1544 */           reportError("msg.bad.break", pos, end - pos);
/*      */         }
/*      */       } else {
/* 1547 */         jump = this.loopAndSwitchSet.get(this.loopAndSwitchSet.size() - 1);
/*      */       } 
/*      */     }
/*      */     
/* 1551 */     BreakStatement pn = new BreakStatement(pos, end - pos);
/* 1552 */     pn.setBreakLabel(breakLabel);
/*      */     
/* 1554 */     if (jump != null)
/* 1555 */       pn.setBreakTarget(jump); 
/* 1556 */     pn.setLineno(lineno);
/* 1557 */     return pn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ContinueStatement continueStatement() throws IOException {
/* 1563 */     if (this.currentToken != 121) codeBug(); 
/* 1564 */     consumeToken();
/* 1565 */     int lineno = this.ts.lineno, pos = this.ts.tokenBeg, end = this.ts.tokenEnd;
/* 1566 */     Name label = null;
/* 1567 */     if (peekTokenOrEOL() == 39) {
/* 1568 */       label = createNameNode();
/* 1569 */       end = getNodeEnd((AstNode)label);
/*      */     } 
/*      */ 
/*      */     
/* 1573 */     LabeledStatement labels = matchJumpLabelName();
/* 1574 */     Loop target = null;
/* 1575 */     if (labels == null && label == null) {
/* 1576 */       if (this.loopSet == null || this.loopSet.size() == 0) {
/* 1577 */         reportError("msg.continue.outside");
/*      */       } else {
/* 1579 */         target = this.loopSet.get(this.loopSet.size() - 1);
/*      */       } 
/*      */     } else {
/* 1582 */       if (labels == null || !(labels.getStatement() instanceof Loop)) {
/* 1583 */         reportError("msg.continue.nonloop", pos, end - pos);
/*      */       }
/* 1585 */       target = (labels == null) ? null : (Loop)labels.getStatement();
/*      */     } 
/*      */     
/* 1588 */     ContinueStatement pn = new ContinueStatement(pos, end - pos);
/* 1589 */     if (target != null)
/* 1590 */       pn.setTarget(target); 
/* 1591 */     pn.setLabel(label);
/* 1592 */     pn.setLineno(lineno);
/* 1593 */     return pn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private WithStatement withStatement() throws IOException {
/* 1599 */     if (this.currentToken != 123) codeBug(); 
/* 1600 */     consumeToken();
/*      */     
/* 1602 */     Comment withComment = getAndResetJsDoc();
/*      */     
/* 1604 */     int lineno = this.ts.lineno, pos = this.ts.tokenBeg, lp = -1, rp = -1;
/* 1605 */     if (mustMatchToken(87, "msg.no.paren.with")) {
/* 1606 */       lp = this.ts.tokenBeg;
/*      */     }
/* 1608 */     AstNode obj = expr();
/*      */     
/* 1610 */     if (mustMatchToken(88, "msg.no.paren.after.with")) {
/* 1611 */       rp = this.ts.tokenBeg;
/*      */     }
/* 1613 */     AstNode body = statement();
/*      */     
/* 1615 */     WithStatement pn = new WithStatement(pos, getNodeEnd(body) - pos);
/* 1616 */     pn.setJsDocNode(withComment);
/* 1617 */     pn.setExpression(obj);
/* 1618 */     pn.setStatement(body);
/* 1619 */     pn.setParens(lp, rp);
/* 1620 */     pn.setLineno(lineno);
/* 1621 */     return pn;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode letStatement() throws IOException {
/*      */     VariableDeclaration variableDeclaration;
/* 1627 */     if (this.currentToken != 153) codeBug(); 
/* 1628 */     consumeToken();
/* 1629 */     int lineno = this.ts.lineno, pos = this.ts.tokenBeg;
/*      */     
/* 1631 */     if (peekToken() == 87) {
/* 1632 */       AstNode pn = let(true, pos);
/*      */     } else {
/* 1634 */       variableDeclaration = variables(153, pos, true);
/*      */     } 
/* 1636 */     variableDeclaration.setLineno(lineno);
/* 1637 */     return (AstNode)variableDeclaration;
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
/*      */   private static final boolean nowAllSet(int before, int after, int mask) {
/* 1649 */     return ((before & mask) != mask && (after & mask) == mask);
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode returnOrYield(int tt, boolean exprContext) throws IOException {
/*      */     ExpressionStatement expressionStatement;
/* 1655 */     if (!insideFunction()) {
/* 1656 */       reportError((tt == 4) ? "msg.bad.return" : "msg.bad.yield");
/*      */     }
/*      */     
/* 1659 */     consumeToken();
/* 1660 */     int lineno = this.ts.lineno, pos = this.ts.tokenBeg, end = this.ts.tokenEnd;
/*      */     
/* 1662 */     AstNode e = null;
/*      */     
/* 1664 */     switch (peekTokenOrEOL()) { case -1: case 0: case 1: case 72: case 82: case 84:
/*      */       case 86:
/*      */       case 88:
/*      */         break;
/*      */       default:
/* 1669 */         e = expr();
/* 1670 */         end = getNodeEnd(e);
/*      */         break; }
/*      */     
/* 1673 */     int before = this.endFlags;
/*      */ 
/*      */     
/* 1676 */     if (tt == 4) {
/* 1677 */       this.endFlags |= (e == null) ? 2 : 4;
/* 1678 */       ReturnStatement returnStatement = new ReturnStatement(pos, end - pos, e);
/*      */ 
/*      */       
/* 1681 */       if (nowAllSet(before, this.endFlags, 6))
/*      */       {
/* 1683 */         addStrictWarning("msg.return.inconsistent", "", pos, end - pos); } 
/*      */     } else {
/* 1685 */       if (!insideFunction())
/* 1686 */         reportError("msg.bad.yield"); 
/* 1687 */       this.endFlags |= 0x8;
/* 1688 */       Yield yield = new Yield(pos, end - pos, e);
/* 1689 */       setRequiresActivation();
/* 1690 */       setIsGenerator();
/* 1691 */       if (!exprContext) {
/* 1692 */         expressionStatement = new ExpressionStatement((AstNode)yield);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1697 */     if (insideFunction() && nowAllSet(before, this.endFlags, 12)) {
/*      */ 
/*      */       
/* 1700 */       Name name = ((FunctionNode)this.currentScriptOrFn).getFunctionName();
/* 1701 */       if (name == null || name.length() == 0) {
/* 1702 */         addError("msg.anon.generator.returns", "");
/*      */       } else {
/* 1704 */         addError("msg.generator.returns", name.getIdentifier());
/*      */       } 
/*      */     } 
/* 1707 */     expressionStatement.setLineno(lineno);
/* 1708 */     return (AstNode)expressionStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode block() throws IOException {
/* 1714 */     if (this.currentToken != 85) codeBug(); 
/* 1715 */     consumeToken();
/* 1716 */     int pos = this.ts.tokenBeg;
/* 1717 */     Scope block = new Scope(pos);
/* 1718 */     block.setLineno(this.ts.lineno);
/* 1719 */     pushScope(block);
/*      */     try {
/* 1721 */       statements((AstNode)block);
/* 1722 */       mustMatchToken(86, "msg.no.brace.block");
/* 1723 */       block.setLength(this.ts.tokenEnd - pos);
/* 1724 */       return (AstNode)block;
/*      */     } finally {
/* 1726 */       popScope();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode defaultXmlNamespace() throws IOException {
/* 1733 */     if (this.currentToken != 116) codeBug(); 
/* 1734 */     consumeToken();
/* 1735 */     mustHaveXML();
/* 1736 */     setRequiresActivation();
/* 1737 */     int lineno = this.ts.lineno, pos = this.ts.tokenBeg;
/*      */     
/* 1739 */     if (!matchToken(39) || !"xml".equals(this.ts.getString())) {
/* 1740 */       reportError("msg.bad.namespace");
/*      */     }
/* 1742 */     if (!matchToken(39) || !"namespace".equals(this.ts.getString())) {
/* 1743 */       reportError("msg.bad.namespace");
/*      */     }
/* 1745 */     if (!matchToken(90)) {
/* 1746 */       reportError("msg.bad.namespace");
/*      */     }
/*      */     
/* 1749 */     AstNode e = expr();
/* 1750 */     UnaryExpression dxmln = new UnaryExpression(pos, getNodeEnd(e) - pos);
/* 1751 */     dxmln.setOperator(74);
/* 1752 */     dxmln.setOperand(e);
/* 1753 */     dxmln.setLineno(lineno);
/*      */     
/* 1755 */     ExpressionStatement es = new ExpressionStatement((AstNode)dxmln, true);
/* 1756 */     return (AstNode)es;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void recordLabel(Label label, LabeledStatement bundle) throws IOException {
/* 1763 */     if (peekToken() != 103) codeBug(); 
/* 1764 */     consumeToken();
/* 1765 */     String name = label.getName();
/* 1766 */     if (this.labelSet == null) {
/* 1767 */       this.labelSet = new HashMap<String, LabeledStatement>();
/*      */     } else {
/* 1769 */       LabeledStatement ls = this.labelSet.get(name);
/* 1770 */       if (ls != null) {
/* 1771 */         if (this.compilerEnv.isIdeMode()) {
/* 1772 */           Label dup = ls.getLabelByName(name);
/* 1773 */           reportError("msg.dup.label", dup.getAbsolutePosition(), dup.getLength());
/*      */         } 
/*      */         
/* 1776 */         reportError("msg.dup.label", label.getPosition(), label.getLength());
/*      */       } 
/*      */     } 
/*      */     
/* 1780 */     bundle.addLabel(label);
/* 1781 */     this.labelSet.put(name, bundle);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode nameOrLabel() throws IOException {
/*      */     ExpressionStatement expressionStatement;
/*      */     AstNode astNode1;
/* 1793 */     if (this.currentToken != 39) throw codeBug(); 
/* 1794 */     int pos = this.ts.tokenBeg;
/*      */ 
/*      */     
/* 1797 */     this.currentFlaggedToken |= 0x20000;
/* 1798 */     AstNode expr = expr();
/*      */     
/* 1800 */     if (expr.getType() != 130) {
/* 1801 */       ExpressionStatement expressionStatement1 = new ExpressionStatement(expr, !insideFunction());
/* 1802 */       ((AstNode)expressionStatement1).lineno = expr.lineno;
/* 1803 */       return (AstNode)expressionStatement1;
/*      */     } 
/*      */     
/* 1806 */     LabeledStatement bundle = new LabeledStatement(pos);
/* 1807 */     recordLabel((Label)expr, bundle);
/* 1808 */     bundle.setLineno(this.ts.lineno);
/*      */     
/* 1810 */     AstNode stmt = null;
/* 1811 */     while (peekToken() == 39) {
/* 1812 */       this.currentFlaggedToken |= 0x20000;
/* 1813 */       expr = expr();
/* 1814 */       if (expr.getType() != 130) {
/* 1815 */         expressionStatement = new ExpressionStatement(expr, !insideFunction());
/* 1816 */         autoInsertSemicolon((AstNode)expressionStatement);
/*      */         break;
/*      */       } 
/* 1819 */       recordLabel((Label)expr, bundle);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1824 */       this.currentLabel = bundle;
/* 1825 */       if (expressionStatement == null) {
/* 1826 */         astNode1 = statementHelper();
/*      */       }
/*      */     } finally {
/* 1829 */       this.currentLabel = null;
/*      */       
/* 1831 */       for (Label lb : bundle.getLabels()) {
/* 1832 */         this.labelSet.remove(lb.getName());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1838 */     bundle.setLength((astNode1.getParent() == null) ? (getNodeEnd(astNode1) - pos) : getNodeEnd(astNode1));
/*      */ 
/*      */     
/* 1841 */     bundle.setStatement(astNode1);
/* 1842 */     return (AstNode)bundle;
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
/*      */   private VariableDeclaration variables(int declType, int pos, boolean isStatement) throws IOException {
/*      */     int end;
/* 1859 */     VariableDeclaration pn = new VariableDeclaration(pos);
/* 1860 */     pn.setType(declType);
/* 1861 */     pn.setLineno(this.ts.lineno);
/* 1862 */     Comment varjsdocNode = getAndResetJsDoc();
/* 1863 */     if (varjsdocNode != null) {
/* 1864 */       pn.setJsDocNode(varjsdocNode);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     do {
/* 1870 */       AstNode destructuring = null;
/* 1871 */       Name name = null;
/* 1872 */       int tt = peekToken(), kidPos = this.ts.tokenBeg;
/* 1873 */       end = this.ts.tokenEnd;
/*      */       
/* 1875 */       if (tt == 83 || tt == 85) {
/*      */         
/* 1877 */         destructuring = destructuringPrimaryExpr();
/* 1878 */         end = getNodeEnd(destructuring);
/* 1879 */         if (!(destructuring instanceof DestructuringForm))
/* 1880 */           reportError("msg.bad.assign.left", kidPos, end - kidPos); 
/* 1881 */         markDestructuring(destructuring);
/*      */       } else {
/*      */         
/* 1884 */         mustMatchToken(39, "msg.bad.var");
/* 1885 */         name = createNameNode();
/* 1886 */         name.setLineno(this.ts.getLineno());
/* 1887 */         if (this.inUseStrictDirective) {
/* 1888 */           String id = this.ts.getString();
/* 1889 */           if ("eval".equals(id) || "arguments".equals(this.ts.getString()))
/*      */           {
/* 1891 */             reportError("msg.bad.id.strict", id);
/*      */           }
/*      */         } 
/* 1894 */         defineSymbol(declType, this.ts.getString(), this.inForInit);
/*      */       } 
/*      */       
/* 1897 */       int lineno = this.ts.lineno;
/*      */       
/* 1899 */       Comment jsdocNode = getAndResetJsDoc();
/*      */       
/* 1901 */       AstNode init = null;
/* 1902 */       if (matchToken(90)) {
/* 1903 */         init = assignExpr();
/* 1904 */         end = getNodeEnd(init);
/*      */       } 
/*      */       
/* 1907 */       VariableInitializer vi = new VariableInitializer(kidPos, end - kidPos);
/* 1908 */       if (destructuring != null) {
/* 1909 */         if (init == null && !this.inForInit) {
/* 1910 */           reportError("msg.destruct.assign.no.init");
/*      */         }
/* 1912 */         vi.setTarget(destructuring);
/*      */       } else {
/* 1914 */         vi.setTarget((AstNode)name);
/*      */       } 
/* 1916 */       vi.setInitializer(init);
/* 1917 */       vi.setType(declType);
/* 1918 */       vi.setJsDocNode(jsdocNode);
/* 1919 */       vi.setLineno(lineno);
/* 1920 */       pn.addVariable(vi);
/*      */     }
/* 1922 */     while (matchToken(89));
/*      */ 
/*      */     
/* 1925 */     pn.setLength(end - pos);
/* 1926 */     pn.setIsStatement(isStatement);
/* 1927 */     return pn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode let(boolean isStatement, int pos) throws IOException {
/* 1934 */     LetNode pn = new LetNode(pos);
/* 1935 */     pn.setLineno(this.ts.lineno);
/* 1936 */     if (mustMatchToken(87, "msg.no.paren.after.let"))
/* 1937 */       pn.setLp(this.ts.tokenBeg - pos); 
/* 1938 */     pushScope((Scope)pn);
/*      */     try {
/* 1940 */       VariableDeclaration vars = variables(153, this.ts.tokenBeg, isStatement);
/* 1941 */       pn.setVariables(vars);
/* 1942 */       if (mustMatchToken(88, "msg.no.paren.let")) {
/* 1943 */         pn.setRp(this.ts.tokenBeg - pos);
/*      */       }
/* 1945 */       if (isStatement && peekToken() == 85) {
/*      */         
/* 1947 */         consumeToken();
/* 1948 */         int beg = this.ts.tokenBeg;
/* 1949 */         AstNode stmt = statements();
/* 1950 */         mustMatchToken(86, "msg.no.curly.let");
/* 1951 */         stmt.setLength(this.ts.tokenEnd - beg);
/* 1952 */         pn.setLength(this.ts.tokenEnd - pos);
/* 1953 */         pn.setBody(stmt);
/* 1954 */         pn.setType(153);
/*      */       } else {
/*      */         
/* 1957 */         AstNode expr = expr();
/* 1958 */         pn.setLength(getNodeEnd(expr) - pos);
/* 1959 */         pn.setBody(expr);
/* 1960 */         if (isStatement) {
/*      */           
/* 1962 */           ExpressionStatement es = new ExpressionStatement((AstNode)pn, !insideFunction());
/*      */           
/* 1964 */           es.setLineno(pn.getLineno());
/* 1965 */           return (AstNode)es;
/*      */         } 
/*      */       } 
/*      */     } finally {
/* 1969 */       popScope();
/*      */     } 
/* 1971 */     return (AstNode)pn;
/*      */   }
/*      */   
/*      */   void defineSymbol(int declType, String name) {
/* 1975 */     defineSymbol(declType, name, false);
/*      */   }
/*      */   
/*      */   void defineSymbol(int declType, String name, boolean ignoreNotInBlock) {
/* 1979 */     if (name == null) {
/* 1980 */       if (this.compilerEnv.isIdeMode()) {
/*      */         return;
/*      */       }
/* 1983 */       codeBug();
/*      */     } 
/*      */     
/* 1986 */     Scope definingScope = this.currentScope.getDefiningScope(name);
/* 1987 */     Symbol symbol = (definingScope != null) ? definingScope.getSymbol(name) : null;
/*      */ 
/*      */     
/* 1990 */     int symDeclType = (symbol != null) ? symbol.getDeclType() : -1;
/* 1991 */     if (symbol != null && (symDeclType == 154 || declType == 154 || (definingScope == this.currentScope && symDeclType == 153))) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1996 */       addError((symDeclType == 154) ? "msg.const.redecl" : ((symDeclType == 153) ? "msg.let.redecl" : ((symDeclType == 122) ? "msg.var.redecl" : ((symDeclType == 109) ? "msg.fn.redecl" : "msg.parm.redecl"))), name);
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 2003 */     switch (declType) {
/*      */       case 153:
/* 2005 */         if (!ignoreNotInBlock && (this.currentScope.getType() == 112 || this.currentScope instanceof Loop)) {
/*      */ 
/*      */           
/* 2008 */           addError("msg.let.decl.not.in.block");
/*      */           return;
/*      */         } 
/* 2011 */         this.currentScope.putSymbol(new Symbol(declType, name));
/*      */         return;
/*      */       
/*      */       case 109:
/*      */       case 122:
/*      */       case 154:
/* 2017 */         if (symbol != null) {
/* 2018 */           if (symDeclType == 122) {
/* 2019 */             addStrictWarning("msg.var.redecl", name);
/* 2020 */           } else if (symDeclType == 87) {
/* 2021 */             addStrictWarning("msg.var.hides.arg", name);
/*      */           } 
/*      */         } else {
/* 2024 */           this.currentScriptOrFn.putSymbol(new Symbol(declType, name));
/*      */         } 
/*      */         return;
/*      */       
/*      */       case 87:
/* 2029 */         if (symbol != null)
/*      */         {
/*      */           
/* 2032 */           addWarning("msg.dup.parms", name);
/*      */         }
/* 2034 */         this.currentScriptOrFn.putSymbol(new Symbol(declType, name));
/*      */         return;
/*      */     } 
/*      */     
/* 2038 */     throw codeBug();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode expr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2045 */     AstNode pn = assignExpr();
/* 2046 */     int pos = pn.getPosition();
/* 2047 */     while (matchToken(89)) {
/* 2048 */       int opPos = this.ts.tokenBeg;
/* 2049 */       if (this.compilerEnv.isStrictMode() && !pn.hasSideEffects()) {
/* 2050 */         addStrictWarning("msg.no.side.effects", "", pos, nodeEnd(pn) - pos);
/*      */       }
/* 2052 */       if (peekToken() == 72)
/* 2053 */         reportError("msg.yield.parenthesized"); 
/* 2054 */       infixExpression = new InfixExpression(89, pn, assignExpr(), opPos);
/*      */     } 
/* 2056 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode assignExpr() throws IOException {
/*      */     Assignment assignment;
/* 2062 */     int tt = peekToken();
/* 2063 */     if (tt == 72) {
/* 2064 */       return returnOrYield(tt, true);
/*      */     }
/* 2066 */     AstNode pn = condExpr();
/* 2067 */     tt = peekToken();
/* 2068 */     if (90 <= tt && tt <= 101) {
/* 2069 */       consumeToken();
/*      */ 
/*      */       
/* 2072 */       Comment jsdocNode = getAndResetJsDoc();
/*      */       
/* 2074 */       markDestructuring(pn);
/* 2075 */       int opPos = this.ts.tokenBeg;
/*      */       
/* 2077 */       assignment = new Assignment(tt, pn, assignExpr(), opPos);
/*      */       
/* 2079 */       if (jsdocNode != null) {
/* 2080 */         assignment.setJsDocNode(jsdocNode);
/*      */       }
/* 2082 */     } else if (tt == 82) {
/*      */ 
/*      */       
/* 2085 */       if (this.currentJsDocComment != null) {
/* 2086 */         assignment.setJsDocNode(getAndResetJsDoc());
/*      */       }
/*      */     } 
/* 2089 */     return (AstNode)assignment;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode condExpr() throws IOException {
/*      */     ConditionalExpression conditionalExpression;
/* 2095 */     AstNode pn = orExpr();
/* 2096 */     if (matchToken(102)) {
/* 2097 */       AstNode ifTrue; int line = this.ts.lineno;
/* 2098 */       int qmarkPos = this.ts.tokenBeg, colonPos = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2104 */       boolean wasInForInit = this.inForInit;
/* 2105 */       this.inForInit = false;
/*      */       
/*      */       try {
/* 2108 */         ifTrue = assignExpr();
/*      */       } finally {
/* 2110 */         this.inForInit = wasInForInit;
/*      */       } 
/* 2112 */       if (mustMatchToken(103, "msg.no.colon.cond"))
/* 2113 */         colonPos = this.ts.tokenBeg; 
/* 2114 */       AstNode ifFalse = assignExpr();
/* 2115 */       int beg = pn.getPosition(), len = getNodeEnd(ifFalse) - beg;
/* 2116 */       ConditionalExpression ce = new ConditionalExpression(beg, len);
/* 2117 */       ce.setLineno(line);
/* 2118 */       ce.setTestExpression(pn);
/* 2119 */       ce.setTrueExpression(ifTrue);
/* 2120 */       ce.setFalseExpression(ifFalse);
/* 2121 */       ce.setQuestionMarkPosition(qmarkPos - beg);
/* 2122 */       ce.setColonPosition(colonPos - beg);
/* 2123 */       conditionalExpression = ce;
/*      */     } 
/* 2125 */     return (AstNode)conditionalExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode orExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2131 */     AstNode pn = andExpr();
/* 2132 */     if (matchToken(104)) {
/* 2133 */       int opPos = this.ts.tokenBeg;
/* 2134 */       infixExpression = new InfixExpression(104, pn, orExpr(), opPos);
/*      */     } 
/* 2136 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode andExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2142 */     AstNode pn = bitOrExpr();
/* 2143 */     if (matchToken(105)) {
/* 2144 */       int opPos = this.ts.tokenBeg;
/* 2145 */       infixExpression = new InfixExpression(105, pn, andExpr(), opPos);
/*      */     } 
/* 2147 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode bitOrExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2153 */     AstNode pn = bitXorExpr();
/* 2154 */     while (matchToken(9)) {
/* 2155 */       int opPos = this.ts.tokenBeg;
/* 2156 */       infixExpression = new InfixExpression(9, pn, bitXorExpr(), opPos);
/*      */     } 
/* 2158 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode bitXorExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2164 */     AstNode pn = bitAndExpr();
/* 2165 */     while (matchToken(10)) {
/* 2166 */       int opPos = this.ts.tokenBeg;
/* 2167 */       infixExpression = new InfixExpression(10, pn, bitAndExpr(), opPos);
/*      */     } 
/* 2169 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode bitAndExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2175 */     AstNode pn = eqExpr();
/* 2176 */     while (matchToken(11)) {
/* 2177 */       int opPos = this.ts.tokenBeg;
/* 2178 */       infixExpression = new InfixExpression(11, pn, eqExpr(), opPos);
/*      */     } 
/* 2180 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode eqExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2186 */     AstNode pn = relExpr();
/*      */     while (true) {
/* 2188 */       int parseToken, tt = peekToken(), opPos = this.ts.tokenBeg;
/* 2189 */       switch (tt) {
/*      */         case 12:
/*      */         case 13:
/*      */         case 46:
/*      */         case 47:
/* 2194 */           consumeToken();
/* 2195 */           parseToken = tt;
/* 2196 */           if (this.compilerEnv.getLanguageVersion() == 120)
/*      */           {
/* 2198 */             if (tt == 12) {
/* 2199 */               parseToken = 46;
/* 2200 */             } else if (tt == 13) {
/* 2201 */               parseToken = 47;
/*      */             }  } 
/* 2203 */           infixExpression = new InfixExpression(parseToken, pn, relExpr(), opPos);
/*      */           continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2208 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode relExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2214 */     AstNode pn = shiftExpr();
/*      */     while (true) {
/* 2216 */       int tt = peekToken(), opPos = this.ts.tokenBeg;
/* 2217 */       switch (tt) {
/*      */         case 52:
/* 2219 */           if (this.inForInit) {
/*      */             break;
/*      */           }
/*      */         case 14:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 53:
/* 2227 */           consumeToken();
/* 2228 */           infixExpression = new InfixExpression(tt, pn, shiftExpr(), opPos);
/*      */           continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2233 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode shiftExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2239 */     AstNode pn = addExpr();
/*      */     while (true) {
/* 2241 */       int tt = peekToken(), opPos = this.ts.tokenBeg;
/* 2242 */       switch (tt) {
/*      */         case 18:
/*      */         case 19:
/*      */         case 20:
/* 2246 */           consumeToken();
/* 2247 */           infixExpression = new InfixExpression(tt, pn, addExpr(), opPos);
/*      */           continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2252 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode addExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2258 */     AstNode pn = mulExpr();
/*      */     while (true) {
/* 2260 */       int tt = peekToken(), opPos = this.ts.tokenBeg;
/* 2261 */       if (tt == 21 || tt == 22) {
/* 2262 */         consumeToken();
/* 2263 */         infixExpression = new InfixExpression(tt, pn, mulExpr(), opPos);
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2268 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */   
/*      */   private AstNode mulExpr() throws IOException {
/*      */     InfixExpression infixExpression;
/* 2274 */     AstNode pn = unaryExpr();
/*      */     while (true) {
/* 2276 */       int tt = peekToken(), opPos = this.ts.tokenBeg;
/* 2277 */       switch (tt) {
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/* 2281 */           consumeToken();
/* 2282 */           infixExpression = new InfixExpression(tt, pn, unaryExpr(), opPos);
/*      */           continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 2287 */     return (AstNode)infixExpression;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode unaryExpr() throws IOException {
/*      */     UnaryExpression unaryExpression1, expr;
/* 2294 */     int tt = peekToken();
/* 2295 */     int line = this.ts.lineno;
/*      */     
/* 2297 */     switch (tt) {
/*      */       case 26:
/*      */       case 27:
/*      */       case 32:
/*      */       case 126:
/* 2302 */         consumeToken();
/* 2303 */         unaryExpression1 = new UnaryExpression(tt, this.ts.tokenBeg, unaryExpr());
/* 2304 */         unaryExpression1.setLineno(line);
/* 2305 */         return (AstNode)unaryExpression1;
/*      */       
/*      */       case 21:
/* 2308 */         consumeToken();
/*      */         
/* 2310 */         unaryExpression1 = new UnaryExpression(28, this.ts.tokenBeg, unaryExpr());
/* 2311 */         unaryExpression1.setLineno(line);
/* 2312 */         return (AstNode)unaryExpression1;
/*      */       
/*      */       case 22:
/* 2315 */         consumeToken();
/*      */         
/* 2317 */         unaryExpression1 = new UnaryExpression(29, this.ts.tokenBeg, unaryExpr());
/* 2318 */         unaryExpression1.setLineno(line);
/* 2319 */         return (AstNode)unaryExpression1;
/*      */       
/*      */       case 106:
/*      */       case 107:
/* 2323 */         consumeToken();
/* 2324 */         expr = new UnaryExpression(tt, this.ts.tokenBeg, memberExpr(true));
/*      */         
/* 2326 */         expr.setLineno(line);
/* 2327 */         checkBadIncDec(expr);
/* 2328 */         return (AstNode)expr;
/*      */       
/*      */       case 31:
/* 2331 */         consumeToken();
/* 2332 */         unaryExpression1 = new UnaryExpression(tt, this.ts.tokenBeg, unaryExpr());
/* 2333 */         unaryExpression1.setLineno(line);
/* 2334 */         return (AstNode)unaryExpression1;
/*      */       
/*      */       case -1:
/* 2337 */         consumeToken();
/* 2338 */         return (AstNode)makeErrorNode();
/*      */ 
/*      */       
/*      */       case 14:
/* 2342 */         if (this.compilerEnv.isXmlAvailable()) {
/* 2343 */           consumeToken();
/* 2344 */           return memberExprTail(true, xmlInitializer());
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/* 2349 */     AstNode pn = memberExpr(true);
/*      */     
/* 2351 */     tt = peekTokenOrEOL();
/* 2352 */     if (tt != 106 && tt != 107) {
/* 2353 */       return pn;
/*      */     }
/* 2355 */     consumeToken();
/* 2356 */     UnaryExpression uexpr = new UnaryExpression(tt, this.ts.tokenBeg, pn, true);
/*      */     
/* 2358 */     uexpr.setLineno(line);
/* 2359 */     checkBadIncDec(uexpr);
/* 2360 */     return (AstNode)uexpr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode xmlInitializer() throws IOException {
/* 2367 */     if (this.currentToken != 14) codeBug(); 
/* 2368 */     int pos = this.ts.tokenBeg, tt = this.ts.getFirstXMLToken();
/* 2369 */     if (tt != 145 && tt != 148) {
/* 2370 */       reportError("msg.syntax");
/* 2371 */       return (AstNode)makeErrorNode();
/*      */     } 
/*      */     
/* 2374 */     XmlLiteral pn = new XmlLiteral(pos);
/* 2375 */     pn.setLineno(this.ts.lineno);
/*      */     
/* 2377 */     for (;; tt = this.ts.getNextXMLToken()) {
/* 2378 */       int beg; AstNode expr; XmlExpression xexpr; switch (tt) {
/*      */         case 145:
/* 2380 */           pn.addFragment((XmlFragment)new XmlString(this.ts.tokenBeg, this.ts.getString()));
/* 2381 */           mustMatchToken(85, "msg.syntax");
/* 2382 */           beg = this.ts.tokenBeg;
/* 2383 */           expr = (peekToken() == 86) ? (AstNode)new EmptyExpression(beg, this.ts.tokenEnd - beg) : expr();
/*      */ 
/*      */           
/* 2386 */           mustMatchToken(86, "msg.syntax");
/* 2387 */           xexpr = new XmlExpression(beg, expr);
/* 2388 */           xexpr.setIsXmlAttribute(this.ts.isXMLAttribute());
/* 2389 */           xexpr.setLength(this.ts.tokenEnd - beg);
/* 2390 */           pn.addFragment((XmlFragment)xexpr);
/*      */           break;
/*      */         
/*      */         case 148:
/* 2394 */           pn.addFragment((XmlFragment)new XmlString(this.ts.tokenBeg, this.ts.getString()));
/* 2395 */           return (AstNode)pn;
/*      */         
/*      */         default:
/* 2398 */           reportError("msg.syntax");
/* 2399 */           return (AstNode)makeErrorNode();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<AstNode> argumentList() throws IOException {
/* 2407 */     if (matchToken(88)) {
/* 2408 */       return null;
/*      */     }
/* 2410 */     List<AstNode> result = new ArrayList<AstNode>();
/* 2411 */     boolean wasInForInit = this.inForInit;
/* 2412 */     this.inForInit = false;
/*      */     try {
/*      */       do {
/* 2415 */         if (peekToken() == 72) {
/* 2416 */           reportError("msg.yield.parenthesized");
/*      */         }
/* 2418 */         AstNode en = assignExpr();
/* 2419 */         if (peekToken() == 119) {
/*      */           try {
/* 2421 */             result.add(generatorExpression(en, 0, true));
/*      */           }
/* 2423 */           catch (IOException ex) {}
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 2428 */           result.add(en);
/*      */         } 
/* 2430 */       } while (matchToken(89));
/*      */     } finally {
/* 2432 */       this.inForInit = wasInForInit;
/*      */     } 
/*      */     
/* 2435 */     mustMatchToken(88, "msg.no.paren.arg");
/* 2436 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode memberExpr(boolean allowCallSyntax) throws IOException {
/*      */     NewExpression newExpression;
/* 2447 */     int tt = peekToken(), lineno = this.ts.lineno;
/*      */ 
/*      */     
/* 2450 */     if (tt != 30) {
/* 2451 */       AstNode pn = primaryExpr();
/*      */     } else {
/* 2453 */       consumeToken();
/* 2454 */       int pos = this.ts.tokenBeg;
/* 2455 */       NewExpression nx = new NewExpression(pos);
/*      */       
/* 2457 */       AstNode target = memberExpr(false);
/* 2458 */       int end = getNodeEnd(target);
/* 2459 */       nx.setTarget(target);
/*      */       
/* 2461 */       int lp = -1;
/* 2462 */       if (matchToken(87)) {
/* 2463 */         lp = this.ts.tokenBeg;
/* 2464 */         List<AstNode> args = argumentList();
/* 2465 */         if (args != null && args.size() > 65536)
/* 2466 */           reportError("msg.too.many.constructor.args"); 
/* 2467 */         int rp = this.ts.tokenBeg;
/* 2468 */         end = this.ts.tokenEnd;
/* 2469 */         if (args != null)
/* 2470 */           nx.setArguments(args); 
/* 2471 */         nx.setParens(lp - pos, rp - pos);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2478 */       if (matchToken(85)) {
/* 2479 */         ObjectLiteral initializer = objectLiteral();
/* 2480 */         end = getNodeEnd((AstNode)initializer);
/* 2481 */         nx.setInitializer(initializer);
/*      */       } 
/* 2483 */       nx.setLength(end - pos);
/* 2484 */       newExpression = nx;
/*      */     } 
/* 2486 */     newExpression.setLineno(lineno);
/* 2487 */     AstNode tail = memberExprTail(allowCallSyntax, (AstNode)newExpression);
/* 2488 */     return tail;
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
/*      */   private AstNode memberExprTail(boolean allowCallSyntax, AstNode pn) throws IOException {
/*      */     FunctionCall functionCall;
/* 2502 */     if (pn == null) codeBug(); 
/* 2503 */     int pos = pn.getPosition(); while (true) {
/*      */       XmlDotQuery xmlDotQuery1; ElementGet elementGet1; int lineno, opPos, rp; AstNode filter; int end; XmlDotQuery q; int lb, rb; AstNode expr; ElementGet g;
/*      */       FunctionCall f;
/*      */       List<AstNode> args;
/* 2507 */       int tt = peekToken();
/* 2508 */       switch (tt) {
/*      */         case 108:
/*      */         case 143:
/* 2511 */           lineno = this.ts.lineno;
/* 2512 */           pn = propertyAccess(tt, pn);
/* 2513 */           pn.setLineno(lineno);
/*      */           continue;
/*      */         
/*      */         case 146:
/* 2517 */           consumeToken();
/* 2518 */           opPos = this.ts.tokenBeg; rp = -1;
/* 2519 */           lineno = this.ts.lineno;
/* 2520 */           mustHaveXML();
/* 2521 */           setRequiresActivation();
/* 2522 */           filter = expr();
/* 2523 */           end = getNodeEnd(filter);
/* 2524 */           if (mustMatchToken(88, "msg.no.paren")) {
/* 2525 */             rp = this.ts.tokenBeg;
/* 2526 */             end = this.ts.tokenEnd;
/*      */           } 
/* 2528 */           q = new XmlDotQuery(pos, end - pos);
/* 2529 */           q.setLeft(pn);
/* 2530 */           q.setRight(filter);
/* 2531 */           q.setOperatorPosition(opPos);
/* 2532 */           q.setRp(rp - pos);
/* 2533 */           q.setLineno(lineno);
/* 2534 */           xmlDotQuery1 = q;
/*      */           continue;
/*      */         
/*      */         case 83:
/* 2538 */           consumeToken();
/* 2539 */           lb = this.ts.tokenBeg; rb = -1;
/* 2540 */           lineno = this.ts.lineno;
/* 2541 */           expr = expr();
/* 2542 */           end = getNodeEnd(expr);
/* 2543 */           if (mustMatchToken(84, "msg.no.bracket.index")) {
/* 2544 */             rb = this.ts.tokenBeg;
/* 2545 */             end = this.ts.tokenEnd;
/*      */           } 
/* 2547 */           g = new ElementGet(pos, end - pos);
/* 2548 */           g.setTarget((AstNode)xmlDotQuery1);
/* 2549 */           g.setElement(expr);
/* 2550 */           g.setParens(lb, rb);
/* 2551 */           g.setLineno(lineno);
/* 2552 */           elementGet1 = g;
/*      */           continue;
/*      */         
/*      */         case 87:
/* 2556 */           if (!allowCallSyntax) {
/*      */             break;
/*      */           }
/* 2559 */           lineno = this.ts.lineno;
/* 2560 */           consumeToken();
/* 2561 */           checkCallRequiresActivation((AstNode)elementGet1);
/* 2562 */           f = new FunctionCall(pos);
/* 2563 */           f.setTarget((AstNode)elementGet1);
/*      */ 
/*      */           
/* 2566 */           f.setLineno(lineno);
/* 2567 */           f.setLp(this.ts.tokenBeg - pos);
/* 2568 */           args = argumentList();
/* 2569 */           if (args != null && args.size() > 65536)
/* 2570 */             reportError("msg.too.many.function.args"); 
/* 2571 */           f.setArguments(args);
/* 2572 */           f.setRp(this.ts.tokenBeg - pos);
/* 2573 */           f.setLength(this.ts.tokenEnd - pos);
/* 2574 */           functionCall = f;
/*      */           continue;
/*      */       } 
/*      */ 
/*      */       
/*      */       break;
/*      */     } 
/* 2581 */     return (AstNode)functionCall;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode propertyAccess(int tt, AstNode pn) throws IOException {
/* 2592 */     if (pn == null) codeBug(); 
/* 2593 */     int memberTypeFlags = 0, lineno = this.ts.lineno, dotPos = this.ts.tokenBeg;
/* 2594 */     consumeToken();
/*      */     
/* 2596 */     if (tt == 143) {
/* 2597 */       mustHaveXML();
/* 2598 */       memberTypeFlags = 4;
/*      */     } 
/*      */     
/* 2601 */     if (!this.compilerEnv.isXmlAvailable()) {
/* 2602 */       int maybeName = nextToken();
/* 2603 */       if (maybeName != 39 && (!this.compilerEnv.isReservedKeywordAsIdentifier() || !TokenStream.isKeyword(this.ts.getString())))
/*      */       {
/*      */         
/* 2606 */         reportError("msg.no.name.after.dot");
/*      */       }
/*      */       
/* 2609 */       Name name = createNameNode(true, 33);
/* 2610 */       PropertyGet pg = new PropertyGet(pn, name, dotPos);
/* 2611 */       pg.setLineno(lineno);
/* 2612 */       return (AstNode)pg;
/*      */     } 
/*      */     
/* 2615 */     AstNode ref = null;
/*      */     
/* 2617 */     int token = nextToken();
/* 2618 */     switch (token) {
/*      */       
/*      */       case 50:
/* 2621 */         saveNameTokenData(this.ts.tokenBeg, "throw", this.ts.lineno);
/* 2622 */         ref = propertyName(-1, "throw", memberTypeFlags);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 39:
/* 2627 */         ref = propertyName(-1, this.ts.getString(), memberTypeFlags);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 23:
/* 2632 */         saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
/* 2633 */         ref = propertyName(-1, "*", memberTypeFlags);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 147:
/* 2639 */         ref = attributeAccess();
/*      */         break;
/*      */       
/*      */       default:
/* 2643 */         if (this.compilerEnv.isReservedKeywordAsIdentifier()) {
/*      */           
/* 2645 */           String name = Token.keywordToName(token);
/* 2646 */           if (name != null) {
/* 2647 */             saveNameTokenData(this.ts.tokenBeg, name, this.ts.lineno);
/* 2648 */             ref = propertyName(-1, name, memberTypeFlags);
/*      */             break;
/*      */           } 
/*      */         } 
/* 2652 */         reportError("msg.no.name.after.dot");
/* 2653 */         return (AstNode)makeErrorNode();
/*      */     } 
/*      */     
/* 2656 */     boolean xml = ref instanceof org.mozilla.javascript.ast.XmlRef;
/* 2657 */     InfixExpression result = xml ? (InfixExpression)new XmlMemberGet() : (InfixExpression)new PropertyGet();
/* 2658 */     if (xml && tt == 108)
/* 2659 */       result.setType(108); 
/* 2660 */     int pos = pn.getPosition();
/* 2661 */     result.setPosition(pos);
/* 2662 */     result.setLength(getNodeEnd(ref) - pos);
/* 2663 */     result.setOperatorPosition(dotPos - pos);
/* 2664 */     result.setLineno(pn.getLineno());
/* 2665 */     result.setLeft(pn);
/* 2666 */     result.setRight(ref);
/* 2667 */     return (AstNode)result;
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
/*      */   private AstNode attributeAccess() throws IOException {
/* 2680 */     int tt = nextToken(), atPos = this.ts.tokenBeg;
/*      */     
/* 2682 */     switch (tt) {
/*      */       
/*      */       case 39:
/* 2685 */         return propertyName(atPos, this.ts.getString(), 0);
/*      */ 
/*      */       
/*      */       case 23:
/* 2689 */         saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
/* 2690 */         return propertyName(atPos, "*", 0);
/*      */ 
/*      */       
/*      */       case 83:
/* 2694 */         return (AstNode)xmlElemRef(atPos, null, -1);
/*      */     } 
/*      */     
/* 2697 */     reportError("msg.no.name.after.xmlAttr");
/* 2698 */     return (AstNode)makeErrorNode();
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
/*      */   private AstNode propertyName(int atPos, String s, int memberTypeFlags) throws IOException {
/* 2720 */     int pos = (atPos != -1) ? atPos : this.ts.tokenBeg, lineno = this.ts.lineno;
/* 2721 */     int colonPos = -1;
/* 2722 */     Name name = createNameNode(true, this.currentToken);
/* 2723 */     Name ns = null;
/*      */     
/* 2725 */     if (matchToken(144)) {
/* 2726 */       ns = name;
/* 2727 */       colonPos = this.ts.tokenBeg;
/*      */       
/* 2729 */       switch (nextToken()) {
/*      */         
/*      */         case 39:
/* 2732 */           name = createNameNode();
/*      */           break;
/*      */ 
/*      */         
/*      */         case 23:
/* 2737 */           saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
/* 2738 */           name = createNameNode(false, -1);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 83:
/* 2743 */           return (AstNode)xmlElemRef(atPos, ns, colonPos);
/*      */         
/*      */         default:
/* 2746 */           reportError("msg.no.name.after.coloncolon");
/* 2747 */           return (AstNode)makeErrorNode();
/*      */       } 
/*      */     
/*      */     } 
/* 2751 */     if (ns == null && memberTypeFlags == 0 && atPos == -1) {
/* 2752 */       return (AstNode)name;
/*      */     }
/*      */     
/* 2755 */     XmlPropRef ref = new XmlPropRef(pos, getNodeEnd((AstNode)name) - pos);
/* 2756 */     ref.setAtPos(atPos);
/* 2757 */     ref.setNamespace(ns);
/* 2758 */     ref.setColonPos(colonPos);
/* 2759 */     ref.setPropName(name);
/* 2760 */     ref.setLineno(lineno);
/* 2761 */     return (AstNode)ref;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private XmlElemRef xmlElemRef(int atPos, Name namespace, int colonPos) throws IOException {
/* 2771 */     int lb = this.ts.tokenBeg, rb = -1, pos = (atPos != -1) ? atPos : lb;
/* 2772 */     AstNode expr = expr();
/* 2773 */     int end = getNodeEnd(expr);
/* 2774 */     if (mustMatchToken(84, "msg.no.bracket.index")) {
/* 2775 */       rb = this.ts.tokenBeg;
/* 2776 */       end = this.ts.tokenEnd;
/*      */     } 
/* 2778 */     XmlElemRef ref = new XmlElemRef(pos, end - pos);
/* 2779 */     ref.setNamespace(namespace);
/* 2780 */     ref.setColonPos(colonPos);
/* 2781 */     ref.setAtPos(atPos);
/* 2782 */     ref.setExpression(expr);
/* 2783 */     ref.setBrackets(lb, rb);
/* 2784 */     return ref;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode destructuringPrimaryExpr() throws IOException, ParserException {
/*      */     try {
/* 2791 */       this.inDestructuringAssignment = true;
/* 2792 */       return primaryExpr();
/*      */     } finally {
/* 2794 */       this.inDestructuringAssignment = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private AstNode primaryExpr() throws IOException { String s;
/*      */     int pos, end;
/*      */     RegExpLiteral re;
/* 2801 */     int ttFlagged = nextFlaggedToken();
/* 2802 */     int tt = ttFlagged & 0xFFFF;
/*      */     
/* 2804 */     switch (tt) {
/*      */       case 109:
/* 2806 */         return (AstNode)function(2);
/*      */       
/*      */       case 83:
/* 2809 */         return arrayLiteral();
/*      */       
/*      */       case 85:
/* 2812 */         return (AstNode)objectLiteral();
/*      */       
/*      */       case 153:
/* 2815 */         return let(false, this.ts.tokenBeg);
/*      */       
/*      */       case 87:
/* 2818 */         return parenExpr();
/*      */       
/*      */       case 147:
/* 2821 */         mustHaveXML();
/* 2822 */         return attributeAccess();
/*      */       
/*      */       case 39:
/* 2825 */         return name(ttFlagged, tt);
/*      */       
/*      */       case 40:
/* 2828 */         s = this.ts.getString();
/* 2829 */         if (this.inUseStrictDirective && this.ts.isNumberOctal()) {
/* 2830 */           reportError("msg.no.octal.strict");
/*      */         }
/* 2832 */         if (this.ts.isNumberOctal()) {
/* 2833 */           s = "0" + s;
/*      */         }
/* 2835 */         if (this.ts.isNumberHex()) {
/* 2836 */           s = "0x" + s;
/*      */         }
/* 2838 */         return (AstNode)new NumberLiteral(this.ts.tokenBeg, s, this.ts.getNumber());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 41:
/* 2844 */         return (AstNode)createStringLiteral();
/*      */ 
/*      */       
/*      */       case 24:
/*      */       case 100:
/* 2849 */         this.ts.readRegExp(tt);
/* 2850 */         pos = this.ts.tokenBeg; end = this.ts.tokenEnd;
/* 2851 */         re = new RegExpLiteral(pos, end - pos);
/* 2852 */         re.setValue(this.ts.getString());
/* 2853 */         re.setFlags(this.ts.readAndClearRegExpFlags());
/* 2854 */         return (AstNode)re;
/*      */       
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/* 2860 */         pos = this.ts.tokenBeg; end = this.ts.tokenEnd;
/* 2861 */         return (AstNode)new KeywordLiteral(pos, end - pos, tt);
/*      */       
/*      */       case 127:
/* 2864 */         reportError("msg.reserved.id");
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
/*      */       case -1:
/* 2880 */         return (AstNode)makeErrorNode();
/*      */       case 0:
/*      */         reportError("msg.unexpected.eof");
/*      */     } 
/* 2884 */     reportError("msg.syntax"); } private AstNode parenExpr() throws IOException { boolean wasInForInit = this.inForInit;
/* 2885 */     this.inForInit = false;
/*      */     try {
/* 2887 */       Comment jsdocNode = getAndResetJsDoc();
/* 2888 */       int lineno = this.ts.lineno;
/* 2889 */       int begin = this.ts.tokenBeg;
/* 2890 */       AstNode e = expr();
/* 2891 */       if (peekToken() == 119) {
/* 2892 */         return generatorExpression(e, begin);
/*      */       }
/* 2894 */       ParenthesizedExpression pn = new ParenthesizedExpression(e);
/* 2895 */       if (jsdocNode == null) {
/* 2896 */         jsdocNode = getAndResetJsDoc();
/*      */       }
/* 2898 */       if (jsdocNode != null) {
/* 2899 */         pn.setJsDocNode(jsdocNode);
/*      */       }
/* 2901 */       mustMatchToken(88, "msg.no.paren");
/* 2902 */       pn.setLength(this.ts.tokenEnd - pn.getPosition());
/* 2903 */       pn.setLineno(lineno);
/* 2904 */       return (AstNode)pn;
/*      */     } finally {
/* 2906 */       this.inForInit = wasInForInit;
/*      */     }  }
/*      */ 
/*      */   
/*      */   private AstNode name(int ttFlagged, int tt) throws IOException {
/* 2911 */     String nameString = this.ts.getString();
/* 2912 */     int namePos = this.ts.tokenBeg, nameLineno = this.ts.lineno;
/* 2913 */     if (0 != (ttFlagged & 0x20000) && peekToken() == 103) {
/*      */ 
/*      */       
/* 2916 */       Label label = new Label(namePos, this.ts.tokenEnd - namePos);
/* 2917 */       label.setName(nameString);
/* 2918 */       label.setLineno(this.ts.lineno);
/* 2919 */       return (AstNode)label;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2924 */     saveNameTokenData(namePos, nameString, nameLineno);
/*      */     
/* 2926 */     if (this.compilerEnv.isXmlAvailable()) {
/* 2927 */       return propertyName(-1, nameString, 0);
/*      */     }
/* 2929 */     return (AstNode)createNameNode(true, 39);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode arrayLiteral() throws IOException {
/* 2939 */     if (this.currentToken != 83) codeBug(); 
/* 2940 */     int pos = this.ts.tokenBeg, end = this.ts.tokenEnd;
/* 2941 */     List<AstNode> elements = new ArrayList<AstNode>();
/* 2942 */     ArrayLiteral pn = new ArrayLiteral(pos);
/* 2943 */     boolean after_lb_or_comma = true;
/* 2944 */     int afterComma = -1;
/* 2945 */     int skipCount = 0;
/*      */     while (true) {
/* 2947 */       int tt = peekToken();
/* 2948 */       if (tt == 89) {
/* 2949 */         consumeToken();
/* 2950 */         afterComma = this.ts.tokenEnd;
/* 2951 */         if (!after_lb_or_comma) {
/* 2952 */           after_lb_or_comma = true; continue;
/*      */         } 
/* 2954 */         elements.add(new EmptyExpression(this.ts.tokenBeg, 1));
/* 2955 */         skipCount++; continue;
/*      */       } 
/* 2957 */       if (tt == 84) {
/* 2958 */         consumeToken();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2964 */         end = this.ts.tokenEnd;
/* 2965 */         pn.setDestructuringLength(elements.size() + (after_lb_or_comma ? 1 : 0));
/*      */         
/* 2967 */         pn.setSkipCount(skipCount);
/* 2968 */         if (afterComma != -1)
/* 2969 */           warnTrailingComma(pos, elements, afterComma);  break;
/*      */       } 
/* 2971 */       if (tt == 119 && !after_lb_or_comma && elements.size() == 1)
/*      */       {
/* 2973 */         return arrayComprehension(elements.get(0), pos); } 
/* 2974 */       if (tt == 0) {
/* 2975 */         reportError("msg.no.bracket.arg");
/*      */         break;
/*      */       } 
/* 2978 */       if (!after_lb_or_comma) {
/* 2979 */         reportError("msg.no.bracket.arg");
/*      */       }
/* 2981 */       elements.add(assignExpr());
/* 2982 */       after_lb_or_comma = false;
/* 2983 */       afterComma = -1;
/*      */     } 
/*      */     
/* 2986 */     for (AstNode e : elements) {
/* 2987 */       pn.addElement(e);
/*      */     }
/* 2989 */     pn.setLength(end - pos);
/* 2990 */     return (AstNode)pn;
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
/*      */   private AstNode arrayComprehension(AstNode result, int pos) throws IOException {
/* 3002 */     List<ArrayComprehensionLoop> loops = new ArrayList<ArrayComprehensionLoop>();
/*      */     
/* 3004 */     while (peekToken() == 119) {
/* 3005 */       loops.add(arrayComprehensionLoop());
/*      */     }
/* 3007 */     int ifPos = -1;
/* 3008 */     ConditionData data = null;
/* 3009 */     if (peekToken() == 112) {
/* 3010 */       consumeToken();
/* 3011 */       ifPos = this.ts.tokenBeg - pos;
/* 3012 */       data = condition();
/*      */     } 
/* 3014 */     mustMatchToken(84, "msg.no.bracket.arg");
/* 3015 */     ArrayComprehension pn = new ArrayComprehension(pos, this.ts.tokenEnd - pos);
/* 3016 */     pn.setResult(result);
/* 3017 */     pn.setLoops(loops);
/* 3018 */     if (data != null) {
/* 3019 */       pn.setIfPosition(ifPos);
/* 3020 */       pn.setFilter(data.condition);
/* 3021 */       pn.setFilterLp(data.lp - pos);
/* 3022 */       pn.setFilterRp(data.rp - pos);
/*      */     } 
/* 3024 */     return (AstNode)pn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayComprehensionLoop arrayComprehensionLoop() throws IOException {
/* 3030 */     if (nextToken() != 119) codeBug(); 
/* 3031 */     int pos = this.ts.tokenBeg;
/* 3032 */     int eachPos = -1, lp = -1, rp = -1, inPos = -1;
/* 3033 */     ArrayComprehensionLoop pn = new ArrayComprehensionLoop(pos);
/*      */     
/* 3035 */     pushScope((Scope)pn); try {
/*      */       Name name;
/* 3037 */       if (matchToken(39)) {
/* 3038 */         if (this.ts.getString().equals("each")) {
/* 3039 */           eachPos = this.ts.tokenBeg - pos;
/*      */         } else {
/* 3041 */           reportError("msg.no.paren.for");
/*      */         } 
/*      */       }
/* 3044 */       if (mustMatchToken(87, "msg.no.paren.for")) {
/* 3045 */         lp = this.ts.tokenBeg - pos;
/*      */       }
/*      */       
/* 3048 */       AstNode iter = null;
/* 3049 */       switch (peekToken()) {
/*      */         
/*      */         case 83:
/*      */         case 85:
/* 3053 */           iter = destructuringPrimaryExpr();
/* 3054 */           markDestructuring(iter);
/*      */           break;
/*      */         case 39:
/* 3057 */           consumeToken();
/* 3058 */           name = createNameNode();
/*      */           break;
/*      */         default:
/* 3061 */           reportError("msg.bad.var");
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 3066 */       if (name.getType() == 39) {
/* 3067 */         defineSymbol(153, this.ts.getString(), true);
/*      */       }
/*      */       
/* 3070 */       if (mustMatchToken(52, "msg.in.after.for.name"))
/* 3071 */         inPos = this.ts.tokenBeg - pos; 
/* 3072 */       AstNode obj = expr();
/* 3073 */       if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
/* 3074 */         rp = this.ts.tokenBeg - pos;
/*      */       }
/* 3076 */       pn.setLength(this.ts.tokenEnd - pos);
/* 3077 */       pn.setIterator((AstNode)name);
/* 3078 */       pn.setIteratedObject(obj);
/* 3079 */       pn.setInPosition(inPos);
/* 3080 */       pn.setEachPosition(eachPos);
/* 3081 */       pn.setIsForEach((eachPos != -1));
/* 3082 */       pn.setParens(lp, rp);
/* 3083 */       return pn;
/*      */     } finally {
/* 3085 */       popScope();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode generatorExpression(AstNode result, int pos) throws IOException {
/* 3092 */     return generatorExpression(result, pos, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AstNode generatorExpression(AstNode result, int pos, boolean inFunctionParams) throws IOException {
/* 3099 */     List<GeneratorExpressionLoop> loops = new ArrayList<GeneratorExpressionLoop>();
/*      */     
/* 3101 */     while (peekToken() == 119) {
/* 3102 */       loops.add(generatorExpressionLoop());
/*      */     }
/* 3104 */     int ifPos = -1;
/* 3105 */     ConditionData data = null;
/* 3106 */     if (peekToken() == 112) {
/* 3107 */       consumeToken();
/* 3108 */       ifPos = this.ts.tokenBeg - pos;
/* 3109 */       data = condition();
/*      */     } 
/* 3111 */     if (!inFunctionParams) {
/* 3112 */       mustMatchToken(88, "msg.no.paren.let");
/*      */     }
/* 3114 */     GeneratorExpression pn = new GeneratorExpression(pos, this.ts.tokenEnd - pos);
/* 3115 */     pn.setResult(result);
/* 3116 */     pn.setLoops(loops);
/* 3117 */     if (data != null) {
/* 3118 */       pn.setIfPosition(ifPos);
/* 3119 */       pn.setFilter(data.condition);
/* 3120 */       pn.setFilterLp(data.lp - pos);
/* 3121 */       pn.setFilterRp(data.rp - pos);
/*      */     } 
/* 3123 */     return (AstNode)pn;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private GeneratorExpressionLoop generatorExpressionLoop() throws IOException {
/* 3129 */     if (nextToken() != 119) codeBug(); 
/* 3130 */     int pos = this.ts.tokenBeg;
/* 3131 */     int lp = -1, rp = -1, inPos = -1;
/* 3132 */     GeneratorExpressionLoop pn = new GeneratorExpressionLoop(pos);
/*      */     
/* 3134 */     pushScope((Scope)pn); try {
/*      */       Name name;
/* 3136 */       if (mustMatchToken(87, "msg.no.paren.for")) {
/* 3137 */         lp = this.ts.tokenBeg - pos;
/*      */       }
/*      */       
/* 3140 */       AstNode iter = null;
/* 3141 */       switch (peekToken()) {
/*      */         
/*      */         case 83:
/*      */         case 85:
/* 3145 */           iter = destructuringPrimaryExpr();
/* 3146 */           markDestructuring(iter);
/*      */           break;
/*      */         case 39:
/* 3149 */           consumeToken();
/* 3150 */           name = createNameNode();
/*      */           break;
/*      */         default:
/* 3153 */           reportError("msg.bad.var");
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 3158 */       if (name.getType() == 39) {
/* 3159 */         defineSymbol(153, this.ts.getString(), true);
/*      */       }
/*      */       
/* 3162 */       if (mustMatchToken(52, "msg.in.after.for.name"))
/* 3163 */         inPos = this.ts.tokenBeg - pos; 
/* 3164 */       AstNode obj = expr();
/* 3165 */       if (mustMatchToken(88, "msg.no.paren.for.ctrl")) {
/* 3166 */         rp = this.ts.tokenBeg - pos;
/*      */       }
/* 3168 */       pn.setLength(this.ts.tokenEnd - pos);
/* 3169 */       pn.setIterator((AstNode)name);
/* 3170 */       pn.setIteratedObject(obj);
/* 3171 */       pn.setInPosition(inPos);
/* 3172 */       pn.setParens(lp, rp);
/* 3173 */       return pn;
/*      */     } finally {
/* 3175 */       popScope();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ObjectLiteral objectLiteral() throws IOException
/*      */   {
/* 3186 */     int pos = this.ts.tokenBeg, lineno = this.ts.lineno;
/* 3187 */     int afterComma = -1;
/* 3188 */     List<ObjectProperty> elems = new ArrayList<ObjectProperty>();
/* 3189 */     Set<String> getterNames = null;
/* 3190 */     Set<String> setterNames = null;
/* 3191 */     if (this.inUseStrictDirective) {
/* 3192 */       getterNames = new HashSet<String>();
/* 3193 */       setterNames = new HashSet<String>();
/*      */     } 
/* 3195 */     Comment objJsdocNode = getAndResetJsDoc(); while (true) {
/*      */       Name name; int ppos, peeked;
/*      */       boolean maybeGetterOrSetter;
/*      */       AstNode pname;
/* 3199 */       String propertyName = null;
/* 3200 */       int entryKind = 1;
/* 3201 */       int tt = peekToken();
/* 3202 */       Comment jsdocNode = getAndResetJsDoc();
/* 3203 */       switch (tt) {
/*      */         case 39:
/* 3205 */           name = createNameNode();
/* 3206 */           propertyName = this.ts.getString();
/* 3207 */           ppos = this.ts.tokenBeg;
/* 3208 */           consumeToken();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3218 */           peeked = peekToken();
/* 3219 */           maybeGetterOrSetter = ("get".equals(propertyName) || "set".equals(propertyName));
/*      */ 
/*      */           
/* 3222 */           if (maybeGetterOrSetter && peeked != 89 && peeked != 103 && peeked != 86) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3227 */             boolean isGet = "get".equals(propertyName);
/* 3228 */             entryKind = isGet ? 2 : 4;
/* 3229 */             AstNode astNode = objliteralProperty();
/* 3230 */             if (astNode == null) {
/* 3231 */               propertyName = null; break;
/*      */             } 
/* 3233 */             propertyName = this.ts.getString();
/* 3234 */             ObjectProperty objectProp = getterSetterProperty(ppos, astNode, isGet);
/*      */             
/* 3236 */             astNode.setJsDocNode(jsdocNode);
/* 3237 */             elems.add(objectProp);
/*      */             break;
/*      */           } 
/* 3240 */           name.setJsDocNode(jsdocNode);
/* 3241 */           elems.add(plainProperty((AstNode)name, tt));
/*      */           break;
/*      */ 
/*      */         
/*      */         case 86:
/* 3246 */           if (afterComma != -1) {
/* 3247 */             warnTrailingComma(pos, elems, afterComma);
/*      */           }
/*      */           break;
/*      */         default:
/* 3251 */           pname = objliteralProperty();
/* 3252 */           if (pname == null) {
/* 3253 */             propertyName = null; break;
/*      */           } 
/* 3255 */           propertyName = this.ts.getString();
/* 3256 */           pname.setJsDocNode(jsdocNode);
/* 3257 */           elems.add(plainProperty(pname, tt));
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 3262 */       if (this.inUseStrictDirective && propertyName != null) {
/* 3263 */         switch (entryKind) {
/*      */           case 1:
/* 3265 */             if (getterNames.contains(propertyName) || setterNames.contains(propertyName))
/*      */             {
/* 3267 */               addError("msg.dup.obj.lit.prop.strict", propertyName);
/*      */             }
/* 3269 */             getterNames.add(propertyName);
/* 3270 */             setterNames.add(propertyName);
/*      */             break;
/*      */           case 2:
/* 3273 */             if (getterNames.contains(propertyName)) {
/* 3274 */               addError("msg.dup.obj.lit.prop.strict", propertyName);
/*      */             }
/* 3276 */             getterNames.add(propertyName);
/*      */             break;
/*      */           case 4:
/* 3279 */             if (setterNames.contains(propertyName)) {
/* 3280 */               addError("msg.dup.obj.lit.prop.strict", propertyName);
/*      */             }
/* 3282 */             setterNames.add(propertyName);
/*      */             break;
/*      */         } 
/*      */ 
/*      */       
/*      */       }
/* 3288 */       getAndResetJsDoc();
/*      */       
/* 3290 */       if (matchToken(89)) {
/* 3291 */         afterComma = this.ts.tokenEnd;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 3297 */     mustMatchToken(86, "msg.no.brace.prop");
/* 3298 */     ObjectLiteral pn = new ObjectLiteral(pos, this.ts.tokenEnd - pos);
/* 3299 */     if (objJsdocNode != null) {
/* 3300 */       pn.setJsDocNode(objJsdocNode);
/*      */     }
/* 3302 */     pn.setElements(elems);
/* 3303 */     pn.setLineno(lineno);
/* 3304 */     return pn; } private AstNode objliteralProperty() throws IOException {
/*      */     Name name2;
/*      */     StringLiteral stringLiteral;
/*      */     NumberLiteral numberLiteral;
/*      */     Name name1;
/* 3309 */     int tt = peekToken();
/* 3310 */     switch (tt)
/*      */     { case 39:
/* 3312 */         name2 = createNameNode();
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
/* 3335 */         consumeToken();
/* 3336 */         return (AstNode)name2;case 41: stringLiteral = createStringLiteral(); consumeToken(); return (AstNode)stringLiteral;case 40: numberLiteral = new NumberLiteral(this.ts.tokenBeg, this.ts.getString(), this.ts.getNumber()); consumeToken(); return (AstNode)numberLiteral; }  if (this.compilerEnv.isReservedKeywordAsIdentifier() && TokenStream.isKeyword(this.ts.getString())) { name1 = createNameNode(); } else { reportError("msg.bad.prop"); return null; }  consumeToken(); return (AstNode)name1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ObjectProperty plainProperty(AstNode property, int ptt) throws IOException {
/* 3344 */     int tt = peekToken();
/* 3345 */     if ((tt == 89 || tt == 86) && ptt == 39 && this.compilerEnv.getLanguageVersion() >= 180) {
/*      */       
/* 3347 */       if (!this.inDestructuringAssignment) {
/* 3348 */         reportError("msg.bad.object.init");
/*      */       }
/* 3350 */       Name name = new Name(property.getPosition(), property.getString());
/* 3351 */       ObjectProperty objectProperty = new ObjectProperty();
/* 3352 */       objectProperty.putProp(26, Boolean.TRUE);
/* 3353 */       objectProperty.setLeftAndRight(property, (AstNode)name);
/* 3354 */       return objectProperty;
/*      */     } 
/* 3356 */     mustMatchToken(103, "msg.no.colon.prop");
/* 3357 */     ObjectProperty pn = new ObjectProperty();
/* 3358 */     pn.setOperatorPosition(this.ts.tokenBeg);
/* 3359 */     pn.setLeftAndRight(property, assignExpr());
/* 3360 */     return pn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ObjectProperty getterSetterProperty(int pos, AstNode propName, boolean isGetter) throws IOException {
/* 3367 */     FunctionNode fn = function(2);
/*      */     
/* 3369 */     Name name = fn.getFunctionName();
/* 3370 */     if (name != null && name.length() != 0) {
/* 3371 */       reportError("msg.bad.prop");
/*      */     }
/* 3373 */     ObjectProperty pn = new ObjectProperty(pos);
/* 3374 */     if (isGetter) {
/* 3375 */       pn.setIsGetter();
/* 3376 */       fn.setFunctionIsGetter();
/*      */     } else {
/* 3378 */       pn.setIsSetter();
/* 3379 */       fn.setFunctionIsSetter();
/*      */     } 
/* 3381 */     int end = getNodeEnd((AstNode)fn);
/* 3382 */     pn.setLeft(propName);
/* 3383 */     pn.setRight((AstNode)fn);
/* 3384 */     pn.setLength(end - pos);
/* 3385 */     return pn;
/*      */   }
/*      */   
/*      */   private Name createNameNode() {
/* 3389 */     return createNameNode(false, 39);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Name createNameNode(boolean checkActivation, int token) {
/* 3400 */     int beg = this.ts.tokenBeg;
/* 3401 */     String s = this.ts.getString();
/* 3402 */     int lineno = this.ts.lineno;
/* 3403 */     if (!"".equals(this.prevNameTokenString)) {
/* 3404 */       beg = this.prevNameTokenStart;
/* 3405 */       s = this.prevNameTokenString;
/* 3406 */       lineno = this.prevNameTokenLineno;
/* 3407 */       this.prevNameTokenStart = 0;
/* 3408 */       this.prevNameTokenString = "";
/* 3409 */       this.prevNameTokenLineno = 0;
/*      */     } 
/* 3411 */     if (s == null) {
/* 3412 */       if (this.compilerEnv.isIdeMode()) {
/* 3413 */         s = "";
/*      */       } else {
/* 3415 */         codeBug();
/*      */       } 
/*      */     }
/* 3418 */     Name name = new Name(beg, s);
/* 3419 */     name.setLineno(lineno);
/* 3420 */     if (checkActivation) {
/* 3421 */       checkActivationName(s, token);
/*      */     }
/* 3423 */     return name;
/*      */   }
/*      */   
/*      */   private StringLiteral createStringLiteral() {
/* 3427 */     int pos = this.ts.tokenBeg, end = this.ts.tokenEnd;
/* 3428 */     StringLiteral s = new StringLiteral(pos, end - pos);
/* 3429 */     s.setLineno(this.ts.lineno);
/* 3430 */     s.setValue(this.ts.getString());
/* 3431 */     s.setQuoteCharacter(this.ts.getQuoteChar());
/* 3432 */     return s;
/*      */   }
/*      */   
/*      */   protected void checkActivationName(String name, int token) {
/* 3436 */     if (!insideFunction()) {
/*      */       return;
/*      */     }
/* 3439 */     boolean activation = false;
/* 3440 */     if ("arguments".equals(name) || (this.compilerEnv.getActivationNames() != null && this.compilerEnv.getActivationNames().contains(name))) {
/*      */ 
/*      */ 
/*      */       
/* 3444 */       activation = true;
/* 3445 */     } else if ("length".equals(name) && 
/* 3446 */       token == 33 && this.compilerEnv.getLanguageVersion() == 120) {
/*      */ 
/*      */ 
/*      */       
/* 3450 */       activation = true;
/*      */     } 
/*      */     
/* 3453 */     if (activation) {
/* 3454 */       setRequiresActivation();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setRequiresActivation() {
/* 3459 */     if (insideFunction()) {
/* 3460 */       ((FunctionNode)this.currentScriptOrFn).setRequiresActivation();
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkCallRequiresActivation(AstNode pn) {
/* 3465 */     if ((pn.getType() == 39 && "eval".equals(((Name)pn).getIdentifier())) || (pn.getType() == 33 && "eval".equals(((PropertyGet)pn).getProperty().getIdentifier())))
/*      */     {
/*      */ 
/*      */       
/* 3469 */       setRequiresActivation(); } 
/*      */   }
/*      */   
/*      */   protected void setIsGenerator() {
/* 3473 */     if (insideFunction()) {
/* 3474 */       ((FunctionNode)this.currentScriptOrFn).setIsGenerator();
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkBadIncDec(UnaryExpression expr) {
/* 3479 */     AstNode op = removeParens(expr.getOperand());
/* 3480 */     int tt = op.getType();
/* 3481 */     if (tt != 39 && tt != 33 && tt != 36 && tt != 67 && tt != 38)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 3486 */       reportError((expr.getType() == 106) ? "msg.bad.incr" : "msg.bad.decr");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private ErrorNode makeErrorNode() {
/* 3492 */     ErrorNode pn = new ErrorNode(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
/* 3493 */     pn.setLineno(this.ts.lineno);
/* 3494 */     return pn;
/*      */   }
/*      */ 
/*      */   
/*      */   private int nodeEnd(AstNode node) {
/* 3499 */     return node.getPosition() + node.getLength();
/*      */   }
/*      */   
/*      */   private void saveNameTokenData(int pos, String name, int lineno) {
/* 3503 */     this.prevNameTokenStart = pos;
/* 3504 */     this.prevNameTokenString = name;
/* 3505 */     this.prevNameTokenLineno = lineno;
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
/*      */   private int lineBeginningFor(int pos) {
/* 3522 */     if (this.sourceChars == null) {
/* 3523 */       return -1;
/*      */     }
/* 3525 */     if (pos <= 0) {
/* 3526 */       return 0;
/*      */     }
/* 3528 */     char[] buf = this.sourceChars;
/* 3529 */     if (pos >= buf.length) {
/* 3530 */       pos = buf.length - 1;
/*      */     }
/* 3532 */     while (--pos >= 0) {
/* 3533 */       char c = buf[pos];
/* 3534 */       if (ScriptRuntime.isJSLineTerminator(c)) {
/* 3535 */         return pos + 1;
/*      */       }
/*      */     } 
/* 3538 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void warnMissingSemi(int pos, int end) {
/* 3545 */     if (this.compilerEnv.isStrictMode()) {
/* 3546 */       int[] linep = new int[2];
/* 3547 */       String line = this.ts.getLine(end, linep);
/*      */ 
/*      */ 
/*      */       
/* 3551 */       int beg = this.compilerEnv.isIdeMode() ? Math.max(pos, end - linep[1]) : pos;
/*      */ 
/*      */       
/* 3554 */       if (line != null) {
/* 3555 */         addStrictWarning("msg.missing.semi", "", beg, end - beg, linep[0], line, linep[1]);
/*      */       }
/*      */       else {
/*      */         
/* 3559 */         addStrictWarning("msg.missing.semi", "", beg, end - beg);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void warnTrailingComma(int pos, List<?> elems, int commaPos) {
/* 3565 */     if (this.compilerEnv.getWarnTrailingComma()) {
/*      */       
/* 3567 */       if (!elems.isEmpty()) {
/* 3568 */         pos = ((AstNode)elems.get(0)).getPosition();
/*      */       }
/* 3570 */       pos = Math.max(pos, lineBeginningFor(commaPos));
/* 3571 */       addWarning("msg.extra.trailing.comma", pos, commaPos - pos);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private String readFully(Reader reader) throws IOException {
/* 3577 */     BufferedReader in = new BufferedReader(reader);
/*      */     try {
/* 3579 */       char[] cbuf = new char[1024];
/* 3580 */       StringBuilder sb = new StringBuilder(1024);
/*      */       int bytes_read;
/* 3582 */       while ((bytes_read = in.read(cbuf, 0, 1024)) != -1) {
/* 3583 */         sb.append(cbuf, 0, bytes_read);
/*      */       }
/* 3585 */       return sb.toString();
/*      */     } finally {
/* 3587 */       in.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected class PerFunctionVariables
/*      */   {
/*      */     private ScriptNode savedCurrentScriptOrFn;
/*      */     private Scope savedCurrentScope;
/*      */     private int savedEndFlags;
/*      */     private boolean savedInForInit;
/*      */     private Map<String, LabeledStatement> savedLabelSet;
/*      */     private List<Loop> savedLoopSet;
/*      */     private List<Jump> savedLoopAndSwitchSet;
/*      */     
/*      */     PerFunctionVariables(FunctionNode fnNode) {
/* 3603 */       this.savedCurrentScriptOrFn = Parser.this.currentScriptOrFn;
/* 3604 */       Parser.this.currentScriptOrFn = (ScriptNode)fnNode;
/*      */       
/* 3606 */       this.savedCurrentScope = Parser.this.currentScope;
/* 3607 */       Parser.this.currentScope = (Scope)fnNode;
/*      */       
/* 3609 */       this.savedLabelSet = Parser.this.labelSet;
/* 3610 */       Parser.this.labelSet = null;
/*      */       
/* 3612 */       this.savedLoopSet = Parser.this.loopSet;
/* 3613 */       Parser.this.loopSet = null;
/*      */       
/* 3615 */       this.savedLoopAndSwitchSet = Parser.this.loopAndSwitchSet;
/* 3616 */       Parser.this.loopAndSwitchSet = null;
/*      */       
/* 3618 */       this.savedEndFlags = Parser.this.endFlags;
/* 3619 */       Parser.this.endFlags = 0;
/*      */       
/* 3621 */       this.savedInForInit = Parser.this.inForInit;
/* 3622 */       Parser.this.inForInit = false;
/*      */     }
/*      */     
/*      */     void restore() {
/* 3626 */       Parser.this.currentScriptOrFn = this.savedCurrentScriptOrFn;
/* 3627 */       Parser.this.currentScope = this.savedCurrentScope;
/* 3628 */       Parser.this.labelSet = this.savedLabelSet;
/* 3629 */       Parser.this.loopSet = this.savedLoopSet;
/* 3630 */       Parser.this.loopAndSwitchSet = this.savedLoopAndSwitchSet;
/* 3631 */       Parser.this.endFlags = this.savedEndFlags;
/* 3632 */       Parser.this.inForInit = this.savedInForInit;
/*      */     }
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
/*      */   Node createDestructuringAssignment(int type, Node left, Node right) {
/* 3650 */     String tempName = this.currentScriptOrFn.getNextTempName();
/* 3651 */     Node result = destructuringAssignmentHelper(type, left, right, tempName);
/*      */     
/* 3653 */     Node comma = result.getLastChild();
/* 3654 */     comma.addChildToBack(createName(tempName));
/* 3655 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Node destructuringAssignmentHelper(int variableType, Node left, Node right, String tempName) {
/* 3661 */     Scope result = createScopeNode(158, left.getLineno());
/* 3662 */     result.addChildToFront(new Node(153, createName(39, tempName, right)));
/*      */     
/*      */     try {
/* 3665 */       pushScope(result);
/* 3666 */       defineSymbol(153, tempName, true);
/*      */     } finally {
/* 3668 */       popScope();
/*      */     } 
/* 3670 */     Node comma = new Node(89);
/* 3671 */     result.addChildToBack(comma);
/* 3672 */     List<String> destructuringNames = new ArrayList<String>();
/* 3673 */     boolean empty = true;
/* 3674 */     switch (left.getType()) {
/*      */       case 65:
/* 3676 */         empty = destructuringArray((ArrayLiteral)left, variableType, tempName, comma, destructuringNames);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 66:
/* 3681 */         empty = destructuringObject((ObjectLiteral)left, variableType, tempName, comma, destructuringNames);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 33:
/*      */       case 36:
/* 3687 */         switch (variableType) {
/*      */           case 122:
/*      */           case 153:
/*      */           case 154:
/* 3691 */             reportError("msg.bad.assign.left"); break;
/*      */         } 
/* 3693 */         comma.addChildToBack(simpleAssignment(left, createName(tempName)));
/*      */         break;
/*      */       default:
/* 3696 */         reportError("msg.bad.assign.left"); break;
/*      */     } 
/* 3698 */     if (empty)
/*      */     {
/* 3700 */       comma.addChildToBack(createNumber(0.0D));
/*      */     }
/* 3702 */     result.putProp(22, destructuringNames);
/* 3703 */     return (Node)result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean destructuringArray(ArrayLiteral array, int variableType, String tempName, Node parent, List<String> destructuringNames) {
/* 3712 */     boolean empty = true;
/* 3713 */     int setOp = (variableType == 154) ? 155 : 8;
/*      */     
/* 3715 */     int index = 0;
/* 3716 */     for (AstNode n : array.getElements()) {
/* 3717 */       if (n.getType() == 128) {
/* 3718 */         index++;
/*      */         continue;
/*      */       } 
/* 3721 */       Node rightElem = new Node(36, createName(tempName), createNumber(index));
/*      */ 
/*      */       
/* 3724 */       if (n.getType() == 39) {
/* 3725 */         String name = n.getString();
/* 3726 */         parent.addChildToBack(new Node(setOp, createName(49, name, null), rightElem));
/*      */ 
/*      */ 
/*      */         
/* 3730 */         if (variableType != -1) {
/* 3731 */           defineSymbol(variableType, name, true);
/* 3732 */           destructuringNames.add(name);
/*      */         } 
/*      */       } else {
/* 3735 */         parent.addChildToBack(destructuringAssignmentHelper(variableType, (Node)n, rightElem, this.currentScriptOrFn.getNextTempName()));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3741 */       index++;
/* 3742 */       empty = false;
/*      */     } 
/* 3744 */     return empty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean destructuringObject(ObjectLiteral node, int variableType, String tempName, Node parent, List<String> destructuringNames) {
/* 3753 */     boolean empty = true;
/* 3754 */     int setOp = (variableType == 154) ? 155 : 8;
/*      */ 
/*      */     
/* 3757 */     for (ObjectProperty prop : node.getElements()) {
/* 3758 */       int lineno = 0;
/*      */ 
/*      */ 
/*      */       
/* 3762 */       if (this.ts != null) {
/* 3763 */         lineno = this.ts.lineno;
/*      */       }
/* 3765 */       AstNode id = prop.getLeft();
/* 3766 */       Node rightElem = null;
/* 3767 */       if (id instanceof Name) {
/* 3768 */         Node s = Node.newString(((Name)id).getIdentifier());
/* 3769 */         rightElem = new Node(33, createName(tempName), s);
/* 3770 */       } else if (id instanceof StringLiteral) {
/* 3771 */         Node s = Node.newString(((StringLiteral)id).getValue());
/* 3772 */         rightElem = new Node(33, createName(tempName), s);
/* 3773 */       } else if (id instanceof NumberLiteral) {
/* 3774 */         Node s = createNumber((int)((NumberLiteral)id).getNumber());
/* 3775 */         rightElem = new Node(36, createName(tempName), s);
/*      */       } else {
/* 3777 */         throw codeBug();
/*      */       } 
/* 3779 */       rightElem.setLineno(lineno);
/* 3780 */       AstNode value = prop.getRight();
/* 3781 */       if (value.getType() == 39) {
/* 3782 */         String name = ((Name)value).getIdentifier();
/* 3783 */         parent.addChildToBack(new Node(setOp, createName(49, name, null), rightElem));
/*      */ 
/*      */ 
/*      */         
/* 3787 */         if (variableType != -1) {
/* 3788 */           defineSymbol(variableType, name, true);
/* 3789 */           destructuringNames.add(name);
/*      */         } 
/*      */       } else {
/* 3792 */         parent.addChildToBack(destructuringAssignmentHelper(variableType, (Node)value, rightElem, this.currentScriptOrFn.getNextTempName()));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 3797 */       empty = false;
/*      */     } 
/* 3799 */     return empty;
/*      */   }
/*      */   
/*      */   protected Node createName(String name) {
/* 3803 */     checkActivationName(name, 39);
/* 3804 */     return Node.newString(39, name);
/*      */   }
/*      */   
/*      */   protected Node createName(int type, String name, Node child) {
/* 3808 */     Node result = createName(name);
/* 3809 */     result.setType(type);
/* 3810 */     if (child != null)
/* 3811 */       result.addChildToBack(child); 
/* 3812 */     return result;
/*      */   }
/*      */   
/*      */   protected Node createNumber(double number) {
/* 3816 */     return Node.newNumber(number);
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
/*      */   protected Scope createScopeNode(int token, int lineno) {
/* 3828 */     Scope scope = new Scope();
/* 3829 */     scope.setType(token);
/* 3830 */     scope.setLineno(lineno);
/* 3831 */     return scope;
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
/*      */   protected Node simpleAssignment(Node left, Node right) {
/*      */     Node obj, ref, id;
/* 3857 */     int type, nodeType = left.getType();
/* 3858 */     switch (nodeType) {
/*      */       case 39:
/* 3860 */         if (this.inUseStrictDirective && "eval".equals(((Name)left).getIdentifier()))
/*      */         {
/*      */           
/* 3863 */           reportError("msg.bad.id.strict", ((Name)left).getIdentifier());
/*      */         }
/*      */         
/* 3866 */         left.setType(49);
/* 3867 */         return new Node(8, left, right);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 33:
/*      */       case 36:
/* 3876 */         if (left instanceof PropertyGet) {
/* 3877 */           AstNode astNode = ((PropertyGet)left).getTarget();
/* 3878 */           Name name = ((PropertyGet)left).getProperty();
/* 3879 */         } else if (left instanceof ElementGet) {
/* 3880 */           AstNode astNode1 = ((ElementGet)left).getTarget();
/* 3881 */           AstNode astNode2 = ((ElementGet)left).getElement();
/*      */         } else {
/*      */           
/* 3884 */           obj = left.getFirstChild();
/* 3885 */           id = left.getLastChild();
/*      */         } 
/*      */         
/* 3888 */         if (nodeType == 33) {
/* 3889 */           type = 35;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3895 */           id.setType(41);
/*      */         } else {
/* 3897 */           type = 37;
/*      */         } 
/* 3899 */         return new Node(type, obj, id, right);
/*      */       
/*      */       case 67:
/* 3902 */         ref = left.getFirstChild();
/* 3903 */         checkMutableReference(ref);
/* 3904 */         return new Node(68, ref, right);
/*      */     } 
/*      */ 
/*      */     
/* 3908 */     throw codeBug();
/*      */   }
/*      */   
/*      */   protected void checkMutableReference(Node n) {
/* 3912 */     int memberTypeFlags = n.getIntProp(16, 0);
/* 3913 */     if ((memberTypeFlags & 0x4) != 0) {
/* 3914 */       reportError("msg.bad.assign.left");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected AstNode removeParens(AstNode node) {
/* 3920 */     while (node instanceof ParenthesizedExpression) {
/* 3921 */       node = ((ParenthesizedExpression)node).getExpression();
/*      */     }
/* 3923 */     return node;
/*      */   }
/*      */   
/*      */   void markDestructuring(AstNode node) {
/* 3927 */     if (node instanceof DestructuringForm) {
/* 3928 */       ((DestructuringForm)node).setIsDestructuring(true);
/* 3929 */     } else if (node instanceof ParenthesizedExpression) {
/* 3930 */       markDestructuring(((ParenthesizedExpression)node).getExpression());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private RuntimeException codeBug() throws RuntimeException {
/* 3938 */     throw Kit.codeBug("ts.cursor=" + this.ts.cursor + ", ts.tokenBeg=" + this.ts.tokenBeg + ", currentToken=" + this.currentToken);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */