/*      */ package org.springframework.expression.spel.standard;
/*      */ 
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.regex.Pattern;
/*      */ import org.springframework.expression.Expression;
/*      */ import org.springframework.expression.ParseException;
/*      */ import org.springframework.expression.ParserContext;
/*      */ import org.springframework.expression.common.TemplateAwareExpressionParser;
/*      */ import org.springframework.expression.spel.InternalParseException;
/*      */ import org.springframework.expression.spel.SpelMessage;
/*      */ import org.springframework.expression.spel.SpelParseException;
/*      */ import org.springframework.expression.spel.SpelParserConfiguration;
/*      */ import org.springframework.expression.spel.ast.Assign;
/*      */ import org.springframework.expression.spel.ast.BeanReference;
/*      */ import org.springframework.expression.spel.ast.BooleanLiteral;
/*      */ import org.springframework.expression.spel.ast.CompoundExpression;
/*      */ import org.springframework.expression.spel.ast.ConstructorReference;
/*      */ import org.springframework.expression.spel.ast.Elvis;
/*      */ import org.springframework.expression.spel.ast.FunctionReference;
/*      */ import org.springframework.expression.spel.ast.Identifier;
/*      */ import org.springframework.expression.spel.ast.Indexer;
/*      */ import org.springframework.expression.spel.ast.InlineList;
/*      */ import org.springframework.expression.spel.ast.InlineMap;
/*      */ import org.springframework.expression.spel.ast.Literal;
/*      */ import org.springframework.expression.spel.ast.MethodReference;
/*      */ import org.springframework.expression.spel.ast.NullLiteral;
/*      */ import org.springframework.expression.spel.ast.OpAnd;
/*      */ import org.springframework.expression.spel.ast.OpDec;
/*      */ import org.springframework.expression.spel.ast.OpDivide;
/*      */ import org.springframework.expression.spel.ast.OpEQ;
/*      */ import org.springframework.expression.spel.ast.OpGE;
/*      */ import org.springframework.expression.spel.ast.OpGT;
/*      */ import org.springframework.expression.spel.ast.OpInc;
/*      */ import org.springframework.expression.spel.ast.OpLE;
/*      */ import org.springframework.expression.spel.ast.OpLT;
/*      */ import org.springframework.expression.spel.ast.OpMinus;
/*      */ import org.springframework.expression.spel.ast.OpModulus;
/*      */ import org.springframework.expression.spel.ast.OpMultiply;
/*      */ import org.springframework.expression.spel.ast.OpNE;
/*      */ import org.springframework.expression.spel.ast.OpOr;
/*      */ import org.springframework.expression.spel.ast.OpPlus;
/*      */ import org.springframework.expression.spel.ast.OperatorBetween;
/*      */ import org.springframework.expression.spel.ast.OperatorInstanceof;
/*      */ import org.springframework.expression.spel.ast.OperatorMatches;
/*      */ import org.springframework.expression.spel.ast.OperatorNot;
/*      */ import org.springframework.expression.spel.ast.OperatorPower;
/*      */ import org.springframework.expression.spel.ast.Projection;
/*      */ import org.springframework.expression.spel.ast.PropertyOrFieldReference;
/*      */ import org.springframework.expression.spel.ast.QualifiedIdentifier;
/*      */ import org.springframework.expression.spel.ast.Selection;
/*      */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*      */ import org.springframework.expression.spel.ast.StringLiteral;
/*      */ import org.springframework.expression.spel.ast.Ternary;
/*      */ import org.springframework.expression.spel.ast.TypeReference;
/*      */ import org.springframework.expression.spel.ast.VariableReference;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.StringUtils;
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
/*      */ class InternalSpelExpressionParser
/*      */   extends TemplateAwareExpressionParser
/*      */ {
/*   90 */   private static final Pattern VALID_QUALIFIED_ID_PATTERN = Pattern.compile("[\\p{L}\\p{N}_$]+");
/*      */ 
/*      */   
/*      */   private final SpelParserConfiguration configuration;
/*      */ 
/*      */   
/*   96 */   private final Deque<SpelNodeImpl> constructedNodes = new ArrayDeque<>();
/*      */ 
/*      */   
/*   99 */   private String expressionString = "";
/*      */ 
/*      */   
/*  102 */   private List<Token> tokenStream = Collections.emptyList();
/*      */ 
/*      */ 
/*      */   
/*      */   private int tokenStreamLength;
/*      */ 
/*      */ 
/*      */   
/*      */   private int tokenStreamPointer;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InternalSpelExpressionParser(SpelParserConfiguration configuration) {
/*  116 */     this.configuration = configuration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SpelExpression doParseExpression(String expressionString, @Nullable ParserContext context) throws ParseException {
/*      */     try {
/*  125 */       this.expressionString = expressionString;
/*  126 */       Tokenizer tokenizer = new Tokenizer(expressionString);
/*  127 */       this.tokenStream = tokenizer.process();
/*  128 */       this.tokenStreamLength = this.tokenStream.size();
/*  129 */       this.tokenStreamPointer = 0;
/*  130 */       this.constructedNodes.clear();
/*  131 */       SpelNodeImpl ast = eatExpression();
/*  132 */       Assert.state((ast != null), "No node");
/*  133 */       Token t = peekToken();
/*  134 */       if (t != null) {
/*  135 */         throw new SpelParseException(t.startPos, SpelMessage.MORE_INPUT, new Object[] { toString(nextToken()) });
/*      */       }
/*  137 */       Assert.isTrue(this.constructedNodes.isEmpty(), "At least one node expected");
/*  138 */       return new SpelExpression(expressionString, ast, this.configuration);
/*      */     }
/*  140 */     catch (InternalParseException ex) {
/*  141 */       throw ex.getCause();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatExpression() {
/*      */     NullLiteral nullLiteral;
/*  153 */     SpelNodeImpl expr = eatLogicalOrExpression();
/*  154 */     Token t = peekToken();
/*  155 */     if (t != null) {
/*  156 */       if (t.kind == TokenKind.ASSIGN) {
/*  157 */         if (expr == null) {
/*  158 */           nullLiteral = new NullLiteral(t.startPos - 1, t.endPos - 1);
/*      */         }
/*  160 */         nextToken();
/*  161 */         SpelNodeImpl assignedValue = eatLogicalOrExpression();
/*  162 */         return (SpelNodeImpl)new Assign(t.startPos, t.endPos, new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, assignedValue });
/*      */       } 
/*  164 */       if (t.kind == TokenKind.ELVIS) {
/*  165 */         NullLiteral nullLiteral1; if (nullLiteral == null) {
/*  166 */           nullLiteral = new NullLiteral(t.startPos - 1, t.endPos - 2);
/*      */         }
/*  168 */         nextToken();
/*  169 */         SpelNodeImpl valueIfNull = eatExpression();
/*  170 */         if (valueIfNull == null) {
/*  171 */           nullLiteral1 = new NullLiteral(t.startPos + 1, t.endPos + 1);
/*      */         }
/*  173 */         return (SpelNodeImpl)new Elvis(t.startPos, t.endPos, new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, (SpelNodeImpl)nullLiteral1 });
/*      */       } 
/*  175 */       if (t.kind == TokenKind.QMARK) {
/*  176 */         if (nullLiteral == null) {
/*  177 */           nullLiteral = new NullLiteral(t.startPos - 1, t.endPos - 1);
/*      */         }
/*  179 */         nextToken();
/*  180 */         SpelNodeImpl ifTrueExprValue = eatExpression();
/*  181 */         eatToken(TokenKind.COLON);
/*  182 */         SpelNodeImpl ifFalseExprValue = eatExpression();
/*  183 */         return (SpelNodeImpl)new Ternary(t.startPos, t.endPos, new SpelNodeImpl[] { (SpelNodeImpl)nullLiteral, ifTrueExprValue, ifFalseExprValue });
/*      */       } 
/*      */     } 
/*  186 */     return (SpelNodeImpl)nullLiteral;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatLogicalOrExpression() {
/*      */     OpOr opOr;
/*  192 */     SpelNodeImpl expr = eatLogicalAndExpression();
/*  193 */     while (peekIdentifierToken("or") || peekToken(TokenKind.SYMBOLIC_OR)) {
/*  194 */       Token t = takeToken();
/*  195 */       SpelNodeImpl rhExpr = eatLogicalAndExpression();
/*  196 */       checkOperands(t, expr, rhExpr);
/*  197 */       opOr = new OpOr(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  199 */     return (SpelNodeImpl)opOr;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatLogicalAndExpression() {
/*      */     OpAnd opAnd;
/*  205 */     SpelNodeImpl expr = eatRelationalExpression();
/*  206 */     while (peekIdentifierToken("and") || peekToken(TokenKind.SYMBOLIC_AND)) {
/*  207 */       Token t = takeToken();
/*  208 */       SpelNodeImpl rhExpr = eatRelationalExpression();
/*  209 */       checkOperands(t, expr, rhExpr);
/*  210 */       opAnd = new OpAnd(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  212 */     return (SpelNodeImpl)opAnd;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatRelationalExpression() {
/*  218 */     SpelNodeImpl expr = eatSumExpression();
/*  219 */     Token relationalOperatorToken = maybeEatRelationalOperator();
/*  220 */     if (relationalOperatorToken != null) {
/*  221 */       Token t = takeToken();
/*  222 */       SpelNodeImpl rhExpr = eatSumExpression();
/*  223 */       checkOperands(t, expr, rhExpr);
/*  224 */       TokenKind tk = relationalOperatorToken.kind;
/*      */       
/*  226 */       if (relationalOperatorToken.isNumericRelationalOperator()) {
/*  227 */         if (tk == TokenKind.GT) {
/*  228 */           return (SpelNodeImpl)new OpGT(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  230 */         if (tk == TokenKind.LT) {
/*  231 */           return (SpelNodeImpl)new OpLT(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  233 */         if (tk == TokenKind.LE) {
/*  234 */           return (SpelNodeImpl)new OpLE(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  236 */         if (tk == TokenKind.GE) {
/*  237 */           return (SpelNodeImpl)new OpGE(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  239 */         if (tk == TokenKind.EQ) {
/*  240 */           return (SpelNodeImpl)new OpEQ(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */         }
/*  242 */         Assert.isTrue((tk == TokenKind.NE), "Not-equals token expected");
/*  243 */         return (SpelNodeImpl)new OpNE(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */       } 
/*      */       
/*  246 */       if (tk == TokenKind.INSTANCEOF) {
/*  247 */         return (SpelNodeImpl)new OperatorInstanceof(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */       }
/*      */       
/*  250 */       if (tk == TokenKind.MATCHES) {
/*  251 */         return (SpelNodeImpl)new OperatorMatches(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */       }
/*      */       
/*  254 */       Assert.isTrue((tk == TokenKind.BETWEEN), "Between token expected");
/*  255 */       return (SpelNodeImpl)new OperatorBetween(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  257 */     return expr;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatSumExpression() {
/*      */     OpMinus opMinus;
/*  263 */     SpelNodeImpl expr = eatProductExpression();
/*  264 */     while (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.INC)) {
/*  265 */       OpPlus opPlus; Token t = takeToken();
/*  266 */       SpelNodeImpl rhExpr = eatProductExpression();
/*  267 */       checkRightOperand(t, rhExpr);
/*  268 */       if (t.kind == TokenKind.PLUS) {
/*  269 */         opPlus = new OpPlus(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr }); continue;
/*      */       } 
/*  271 */       if (t.kind == TokenKind.MINUS) {
/*  272 */         opMinus = new OpMinus(t.startPos, t.endPos, new SpelNodeImpl[] { (SpelNodeImpl)opPlus, rhExpr });
/*      */       }
/*      */     } 
/*  275 */     return (SpelNodeImpl)opMinus;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatProductExpression() {
/*      */     OpModulus opModulus;
/*  281 */     SpelNodeImpl expr = eatPowerIncDecExpression();
/*  282 */     while (peekToken(TokenKind.STAR, TokenKind.DIV, TokenKind.MOD)) {
/*  283 */       OpMultiply opMultiply; OpDivide opDivide; Token t = takeToken();
/*  284 */       SpelNodeImpl rhExpr = eatPowerIncDecExpression();
/*  285 */       checkOperands(t, expr, rhExpr);
/*  286 */       if (t.kind == TokenKind.STAR) {
/*  287 */         opMultiply = new OpMultiply(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr }); continue;
/*      */       } 
/*  289 */       if (t.kind == TokenKind.DIV) {
/*  290 */         opDivide = new OpDivide(t.startPos, t.endPos, new SpelNodeImpl[] { (SpelNodeImpl)opMultiply, rhExpr });
/*      */         continue;
/*      */       } 
/*  293 */       Assert.isTrue((t.kind == TokenKind.MOD), "Mod token expected");
/*  294 */       opModulus = new OpModulus(t.startPos, t.endPos, new SpelNodeImpl[] { (SpelNodeImpl)opDivide, rhExpr });
/*      */     } 
/*      */     
/*  297 */     return (SpelNodeImpl)opModulus;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatPowerIncDecExpression() {
/*  303 */     SpelNodeImpl expr = eatUnaryExpression();
/*  304 */     if (peekToken(TokenKind.POWER)) {
/*  305 */       Token t = takeToken();
/*  306 */       SpelNodeImpl rhExpr = eatUnaryExpression();
/*  307 */       checkRightOperand(t, rhExpr);
/*  308 */       return (SpelNodeImpl)new OperatorPower(t.startPos, t.endPos, new SpelNodeImpl[] { expr, rhExpr });
/*      */     } 
/*  310 */     if (expr != null && peekToken(TokenKind.INC, TokenKind.DEC)) {
/*  311 */       Token t = takeToken();
/*  312 */       if (t.getKind() == TokenKind.INC) {
/*  313 */         return (SpelNodeImpl)new OpInc(t.startPos, t.endPos, true, new SpelNodeImpl[] { expr });
/*      */       }
/*  315 */       return (SpelNodeImpl)new OpDec(t.startPos, t.endPos, true, new SpelNodeImpl[] { expr });
/*      */     } 
/*  317 */     return expr;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatUnaryExpression() {
/*  323 */     if (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.NOT)) {
/*  324 */       Token t = takeToken();
/*  325 */       SpelNodeImpl expr = eatUnaryExpression();
/*  326 */       Assert.state((expr != null), "No node");
/*  327 */       if (t.kind == TokenKind.NOT) {
/*  328 */         return (SpelNodeImpl)new OperatorNot(t.startPos, t.endPos, expr);
/*      */       }
/*  330 */       if (t.kind == TokenKind.PLUS) {
/*  331 */         return (SpelNodeImpl)new OpPlus(t.startPos, t.endPos, new SpelNodeImpl[] { expr });
/*      */       }
/*  333 */       Assert.isTrue((t.kind == TokenKind.MINUS), "Minus token expected");
/*  334 */       return (SpelNodeImpl)new OpMinus(t.startPos, t.endPos, new SpelNodeImpl[] { expr });
/*      */     } 
/*  336 */     if (peekToken(TokenKind.INC, TokenKind.DEC)) {
/*  337 */       Token t = takeToken();
/*  338 */       SpelNodeImpl expr = eatUnaryExpression();
/*  339 */       if (t.getKind() == TokenKind.INC) {
/*  340 */         return (SpelNodeImpl)new OpInc(t.startPos, t.endPos, false, new SpelNodeImpl[] { expr });
/*      */       }
/*  342 */       return (SpelNodeImpl)new OpDec(t.startPos, t.endPos, false, new SpelNodeImpl[] { expr });
/*      */     } 
/*  344 */     return eatPrimaryExpression();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatPrimaryExpression() {
/*  350 */     SpelNodeImpl start = eatStartNode();
/*  351 */     List<SpelNodeImpl> nodes = null;
/*  352 */     SpelNodeImpl node = eatNode();
/*  353 */     while (node != null) {
/*  354 */       if (nodes == null) {
/*  355 */         nodes = new ArrayList<>(4);
/*  356 */         nodes.add(start);
/*      */       } 
/*  358 */       nodes.add(node);
/*  359 */       node = eatNode();
/*      */     } 
/*  361 */     if (start == null || nodes == null) {
/*  362 */       return start;
/*      */     }
/*  364 */     return (SpelNodeImpl)new CompoundExpression(start.getStartPosition(), ((SpelNodeImpl)nodes.get(nodes.size() - 1)).getEndPosition(), nodes
/*  365 */         .<SpelNodeImpl>toArray(new SpelNodeImpl[0]));
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatNode() {
/*  371 */     return peekToken(TokenKind.DOT, TokenKind.SAFE_NAVI) ? eatDottedNode() : eatNonDottedNode();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl eatNonDottedNode() {
/*  377 */     if (peekToken(TokenKind.LSQUARE) && 
/*  378 */       maybeEatIndexer()) {
/*  379 */       return pop();
/*      */     }
/*      */     
/*  382 */     return null;
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
/*      */   private SpelNodeImpl eatDottedNode() {
/*  395 */     Token t = takeToken();
/*  396 */     boolean nullSafeNavigation = (t.kind == TokenKind.SAFE_NAVI);
/*  397 */     if (maybeEatMethodOrProperty(nullSafeNavigation) || maybeEatFunctionOrVar() || 
/*  398 */       maybeEatProjection(nullSafeNavigation) || maybeEatSelection(nullSafeNavigation)) {
/*  399 */       return pop();
/*      */     }
/*  401 */     if (peekToken() == null)
/*      */     {
/*  403 */       throw internalException(t.startPos, SpelMessage.OOD, new Object[0]);
/*      */     }
/*      */     
/*  406 */     throw internalException(t.startPos, SpelMessage.UNEXPECTED_DATA_AFTER_DOT, new Object[] { toString(peekToken()) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatFunctionOrVar() {
/*  417 */     if (!peekToken(TokenKind.HASH)) {
/*  418 */       return false;
/*      */     }
/*  420 */     Token t = takeToken();
/*  421 */     Token functionOrVariableName = eatToken(TokenKind.IDENTIFIER);
/*  422 */     SpelNodeImpl[] args = maybeEatMethodArgs();
/*  423 */     if (args == null) {
/*  424 */       push((SpelNodeImpl)new VariableReference(functionOrVariableName.stringValue(), t.startPos, functionOrVariableName.endPos));
/*      */       
/*  426 */       return true;
/*      */     } 
/*      */     
/*  429 */     push((SpelNodeImpl)new FunctionReference(functionOrVariableName.stringValue(), t.startPos, functionOrVariableName.endPos, args));
/*      */     
/*  431 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SpelNodeImpl[] maybeEatMethodArgs() {
/*  437 */     if (!peekToken(TokenKind.LPAREN)) {
/*  438 */       return null;
/*      */     }
/*  440 */     List<SpelNodeImpl> args = new ArrayList<>();
/*  441 */     consumeArguments(args);
/*  442 */     eatToken(TokenKind.RPAREN);
/*  443 */     return args.<SpelNodeImpl>toArray(new SpelNodeImpl[0]);
/*      */   }
/*      */   
/*      */   private void eatConstructorArgs(List<SpelNodeImpl> accumulatedArguments) {
/*  447 */     if (!peekToken(TokenKind.LPAREN)) {
/*  448 */       throw new InternalParseException(new SpelParseException(this.expressionString, 
/*  449 */             positionOf(peekToken()), SpelMessage.MISSING_CONSTRUCTOR_ARGS, new Object[0]));
/*      */     }
/*  451 */     consumeArguments(accumulatedArguments);
/*  452 */     eatToken(TokenKind.RPAREN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void consumeArguments(List<SpelNodeImpl> accumulatedArguments) {
/*  459 */     Token next, t = peekToken();
/*  460 */     Assert.state((t != null), "Expected token");
/*  461 */     int pos = t.startPos;
/*      */     
/*      */     do {
/*  464 */       nextToken();
/*  465 */       t = peekToken();
/*  466 */       if (t == null) {
/*  467 */         throw internalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/*      */       }
/*  469 */       if (t.kind != TokenKind.RPAREN) {
/*  470 */         accumulatedArguments.add(eatExpression());
/*      */       }
/*  472 */       next = peekToken();
/*      */     }
/*  474 */     while (next != null && next.kind == TokenKind.COMMA);
/*      */     
/*  476 */     if (next == null) {
/*  477 */       throw internalException(pos, SpelMessage.RUN_OUT_OF_ARGUMENTS, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private int positionOf(@Nullable Token t) {
/*  482 */     if (t == null)
/*      */     {
/*      */       
/*  485 */       return this.expressionString.length();
/*      */     }
/*  487 */     return t.startPos;
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
/*      */   @Nullable
/*      */   private SpelNodeImpl eatStartNode() {
/*  503 */     if (maybeEatLiteral()) {
/*  504 */       return pop();
/*      */     }
/*  506 */     if (maybeEatParenExpression()) {
/*  507 */       return pop();
/*      */     }
/*  509 */     if (maybeEatTypeReference() || maybeEatNullReference() || maybeEatConstructorReference() || 
/*  510 */       maybeEatMethodOrProperty(false) || maybeEatFunctionOrVar()) {
/*  511 */       return pop();
/*      */     }
/*  513 */     if (maybeEatBeanReference()) {
/*  514 */       return pop();
/*      */     }
/*  516 */     if (maybeEatProjection(false) || maybeEatSelection(false) || maybeEatIndexer()) {
/*  517 */       return pop();
/*      */     }
/*  519 */     if (maybeEatInlineListOrMap()) {
/*  520 */       return pop();
/*      */     }
/*      */     
/*  523 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatBeanReference() {
/*  530 */     if (peekToken(TokenKind.BEAN_REF) || peekToken(TokenKind.FACTORY_BEAN_REF)) {
/*  531 */       BeanReference beanReference; Token beanRefToken = takeToken();
/*  532 */       Token beanNameToken = null;
/*  533 */       String beanName = null;
/*  534 */       if (peekToken(TokenKind.IDENTIFIER)) {
/*  535 */         beanNameToken = eatToken(TokenKind.IDENTIFIER);
/*  536 */         beanName = beanNameToken.stringValue();
/*      */       }
/*  538 */       else if (peekToken(TokenKind.LITERAL_STRING)) {
/*  539 */         beanNameToken = eatToken(TokenKind.LITERAL_STRING);
/*  540 */         beanName = beanNameToken.stringValue();
/*  541 */         beanName = beanName.substring(1, beanName.length() - 1);
/*      */       } else {
/*      */         
/*  544 */         throw internalException(beanRefToken.startPos, SpelMessage.INVALID_BEAN_REFERENCE, new Object[0]);
/*      */       } 
/*      */       
/*  547 */       if (beanRefToken.getKind() == TokenKind.FACTORY_BEAN_REF) {
/*  548 */         String beanNameString = String.valueOf(TokenKind.FACTORY_BEAN_REF.tokenChars) + beanName;
/*  549 */         beanReference = new BeanReference(beanRefToken.startPos, beanNameToken.endPos, beanNameString);
/*      */       } else {
/*      */         
/*  552 */         beanReference = new BeanReference(beanNameToken.startPos, beanNameToken.endPos, beanName);
/*      */       } 
/*  554 */       this.constructedNodes.push(beanReference);
/*  555 */       return true;
/*      */     } 
/*  557 */     return false;
/*      */   }
/*      */   
/*      */   private boolean maybeEatTypeReference() {
/*  561 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  562 */       Token typeName = peekToken();
/*  563 */       Assert.state((typeName != null), "Expected token");
/*  564 */       if (!"T".equals(typeName.stringValue())) {
/*  565 */         return false;
/*      */       }
/*      */       
/*  568 */       Token t = takeToken();
/*  569 */       if (peekToken(TokenKind.RSQUARE)) {
/*      */         
/*  571 */         push((SpelNodeImpl)new PropertyOrFieldReference(false, t.stringValue(), t.startPos, t.endPos));
/*  572 */         return true;
/*      */       } 
/*  574 */       eatToken(TokenKind.LPAREN);
/*  575 */       SpelNodeImpl node = eatPossiblyQualifiedId();
/*      */ 
/*      */       
/*  578 */       int dims = 0;
/*  579 */       while (peekToken(TokenKind.LSQUARE, true)) {
/*  580 */         eatToken(TokenKind.RSQUARE);
/*  581 */         dims++;
/*      */       } 
/*  583 */       eatToken(TokenKind.RPAREN);
/*  584 */       this.constructedNodes.push(new TypeReference(typeName.startPos, typeName.endPos, node, dims));
/*  585 */       return true;
/*      */     } 
/*  587 */     return false;
/*      */   }
/*      */   
/*      */   private boolean maybeEatNullReference() {
/*  591 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  592 */       Token nullToken = peekToken();
/*  593 */       Assert.state((nullToken != null), "Expected token");
/*  594 */       if (!"null".equalsIgnoreCase(nullToken.stringValue())) {
/*  595 */         return false;
/*      */       }
/*  597 */       nextToken();
/*  598 */       this.constructedNodes.push(new NullLiteral(nullToken.startPos, nullToken.endPos));
/*  599 */       return true;
/*      */     } 
/*  601 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatProjection(boolean nullSafeNavigation) {
/*  606 */     Token t = peekToken();
/*  607 */     if (!peekToken(TokenKind.PROJECT, true)) {
/*  608 */       return false;
/*      */     }
/*  610 */     Assert.state((t != null), "No token");
/*  611 */     SpelNodeImpl expr = eatExpression();
/*  612 */     Assert.state((expr != null), "No node");
/*  613 */     eatToken(TokenKind.RSQUARE);
/*  614 */     this.constructedNodes.push(new Projection(nullSafeNavigation, t.startPos, t.endPos, expr));
/*  615 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatInlineListOrMap() {
/*      */     InlineMap inlineMap;
/*  621 */     Token t = peekToken();
/*  622 */     if (!peekToken(TokenKind.LCURLY, true)) {
/*  623 */       return false;
/*      */     }
/*  625 */     Assert.state((t != null), "No token");
/*  626 */     SpelNodeImpl expr = null;
/*  627 */     Token closingCurly = peekToken();
/*  628 */     if (peekToken(TokenKind.RCURLY, true))
/*      */     
/*  630 */     { Assert.state((closingCurly != null), "No token");
/*  631 */       InlineList inlineList = new InlineList(t.startPos, closingCurly.endPos, new SpelNodeImpl[0]); }
/*      */     
/*  633 */     else if (peekToken(TokenKind.COLON, true))
/*  634 */     { closingCurly = eatToken(TokenKind.RCURLY);
/*      */       
/*  636 */       inlineMap = new InlineMap(t.startPos, closingCurly.endPos, new SpelNodeImpl[0]); }
/*      */     else
/*      */     
/*  639 */     { SpelNodeImpl firstExpression = eatExpression();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  644 */       if (peekToken(TokenKind.RCURLY))
/*  645 */       { List<SpelNodeImpl> elements = new ArrayList<>();
/*  646 */         elements.add(firstExpression);
/*  647 */         closingCurly = eatToken(TokenKind.RCURLY);
/*  648 */         InlineList inlineList = new InlineList(t.startPos, closingCurly.endPos, elements.<SpelNodeImpl>toArray(new SpelNodeImpl[0])); }
/*      */       else
/*  650 */       { if (peekToken(TokenKind.COMMA, true))
/*  651 */         { List<SpelNodeImpl> elements = new ArrayList<>();
/*  652 */           elements.add(firstExpression);
/*      */           while (true)
/*  654 */           { elements.add(eatExpression());
/*      */             
/*  656 */             if (!peekToken(TokenKind.COMMA, true))
/*  657 */             { closingCurly = eatToken(TokenKind.RCURLY);
/*  658 */               InlineList inlineList = new InlineList(t.startPos, closingCurly.endPos, elements.<SpelNodeImpl>toArray(new SpelNodeImpl[0]));
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
/*  677 */               this.constructedNodes.push(inlineList);
/*  678 */               return true; }  }  }  if (peekToken(TokenKind.COLON, true)) { List<SpelNodeImpl> elements = new ArrayList<>(); elements.add(firstExpression); elements.add(eatExpression()); while (peekToken(TokenKind.COMMA, true)) { elements.add(eatExpression()); eatToken(TokenKind.COLON); elements.add(eatExpression()); }  closingCurly = eatToken(TokenKind.RCURLY); inlineMap = new InlineMap(t.startPos, closingCurly.endPos, elements.<SpelNodeImpl>toArray(new SpelNodeImpl[0])); } else { throw internalException(t.startPos, SpelMessage.OOD, new Object[0]); }  }  }  this.constructedNodes.push(inlineMap); return true;
/*      */   }
/*      */   
/*      */   private boolean maybeEatIndexer() {
/*  682 */     Token t = peekToken();
/*  683 */     if (!peekToken(TokenKind.LSQUARE, true)) {
/*  684 */       return false;
/*      */     }
/*  686 */     Assert.state((t != null), "No token");
/*  687 */     SpelNodeImpl expr = eatExpression();
/*  688 */     Assert.state((expr != null), "No node");
/*  689 */     eatToken(TokenKind.RSQUARE);
/*  690 */     this.constructedNodes.push(new Indexer(t.startPos, t.endPos, expr));
/*  691 */     return true;
/*      */   }
/*      */   
/*      */   private boolean maybeEatSelection(boolean nullSafeNavigation) {
/*  695 */     Token t = peekToken();
/*  696 */     if (!peekSelectToken()) {
/*  697 */       return false;
/*      */     }
/*  699 */     Assert.state((t != null), "No token");
/*  700 */     nextToken();
/*  701 */     SpelNodeImpl expr = eatExpression();
/*  702 */     if (expr == null) {
/*  703 */       throw internalException(t.startPos, SpelMessage.MISSING_SELECTION_EXPRESSION, new Object[0]);
/*      */     }
/*  705 */     eatToken(TokenKind.RSQUARE);
/*  706 */     if (t.kind == TokenKind.SELECT_FIRST) {
/*  707 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 1, t.startPos, t.endPos, expr));
/*      */     }
/*  709 */     else if (t.kind == TokenKind.SELECT_LAST) {
/*  710 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 2, t.startPos, t.endPos, expr));
/*      */     } else {
/*      */       
/*  713 */       this.constructedNodes.push(new Selection(nullSafeNavigation, 0, t.startPos, t.endPos, expr));
/*      */     } 
/*  715 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SpelNodeImpl eatPossiblyQualifiedId() {
/*  723 */     Deque<SpelNodeImpl> qualifiedIdPieces = new ArrayDeque<>();
/*  724 */     Token node = peekToken();
/*  725 */     while (isValidQualifiedId(node)) {
/*  726 */       nextToken();
/*  727 */       if (node.kind != TokenKind.DOT) {
/*  728 */         qualifiedIdPieces.add(new Identifier(node.stringValue(), node.startPos, node.endPos));
/*      */       }
/*  730 */       node = peekToken();
/*      */     } 
/*  732 */     if (qualifiedIdPieces.isEmpty()) {
/*  733 */       if (node == null) {
/*  734 */         throw internalException(this.expressionString.length(), SpelMessage.OOD, new Object[0]);
/*      */       }
/*  736 */       throw internalException(node.startPos, SpelMessage.NOT_EXPECTED_TOKEN, new Object[] { "qualified ID", node
/*  737 */             .getKind().toString().toLowerCase() });
/*      */     } 
/*  739 */     return (SpelNodeImpl)new QualifiedIdentifier(((SpelNodeImpl)qualifiedIdPieces.getFirst()).getStartPosition(), ((SpelNodeImpl)qualifiedIdPieces
/*  740 */         .getLast()).getEndPosition(), (SpelNodeImpl[])qualifiedIdPieces.toArray((Object[])new SpelNodeImpl[0]));
/*      */   }
/*      */   
/*      */   private boolean isValidQualifiedId(@Nullable Token node) {
/*  744 */     if (node == null || node.kind == TokenKind.LITERAL_STRING) {
/*  745 */       return false;
/*      */     }
/*  747 */     if (node.kind == TokenKind.DOT || node.kind == TokenKind.IDENTIFIER) {
/*  748 */       return true;
/*      */     }
/*  750 */     String value = node.stringValue();
/*  751 */     return (StringUtils.hasLength(value) && VALID_QUALIFIED_ID_PATTERN.matcher(value).matches());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatMethodOrProperty(boolean nullSafeNavigation) {
/*  758 */     if (peekToken(TokenKind.IDENTIFIER)) {
/*  759 */       Token methodOrPropertyName = takeToken();
/*  760 */       SpelNodeImpl[] args = maybeEatMethodArgs();
/*  761 */       if (args == null) {
/*      */         
/*  763 */         push((SpelNodeImpl)new PropertyOrFieldReference(nullSafeNavigation, methodOrPropertyName.stringValue(), methodOrPropertyName.startPos, methodOrPropertyName.endPos));
/*      */         
/*  765 */         return true;
/*      */       } 
/*      */       
/*  768 */       push((SpelNodeImpl)new MethodReference(nullSafeNavigation, methodOrPropertyName.stringValue(), methodOrPropertyName.startPos, methodOrPropertyName.endPos, args));
/*      */ 
/*      */       
/*  771 */       return true;
/*      */     } 
/*  773 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEatConstructorReference() {
/*  779 */     if (peekIdentifierToken("new")) {
/*  780 */       Token newToken = takeToken();
/*      */       
/*  782 */       if (peekToken(TokenKind.RSQUARE)) {
/*      */         
/*  784 */         push((SpelNodeImpl)new PropertyOrFieldReference(false, newToken.stringValue(), newToken.startPos, newToken.endPos));
/*  785 */         return true;
/*      */       } 
/*  787 */       SpelNodeImpl possiblyQualifiedConstructorName = eatPossiblyQualifiedId();
/*  788 */       List<SpelNodeImpl> nodes = new ArrayList<>();
/*  789 */       nodes.add(possiblyQualifiedConstructorName);
/*  790 */       if (peekToken(TokenKind.LSQUARE)) {
/*      */         
/*  792 */         List<SpelNodeImpl> dimensions = new ArrayList<>();
/*  793 */         while (peekToken(TokenKind.LSQUARE, true)) {
/*  794 */           if (!peekToken(TokenKind.RSQUARE)) {
/*  795 */             dimensions.add(eatExpression());
/*      */           } else {
/*      */             
/*  798 */             dimensions.add(null);
/*      */           } 
/*  800 */           eatToken(TokenKind.RSQUARE);
/*      */         } 
/*  802 */         if (maybeEatInlineListOrMap()) {
/*  803 */           nodes.add(pop());
/*      */         }
/*  805 */         push((SpelNodeImpl)new ConstructorReference(newToken.startPos, newToken.endPos, dimensions
/*  806 */               .<SpelNodeImpl>toArray(new SpelNodeImpl[0]), nodes.<SpelNodeImpl>toArray(new SpelNodeImpl[0])));
/*      */       }
/*      */       else {
/*      */         
/*  810 */         eatConstructorArgs(nodes);
/*      */         
/*  812 */         push((SpelNodeImpl)new ConstructorReference(newToken.startPos, newToken.endPos, nodes.<SpelNodeImpl>toArray(new SpelNodeImpl[0])));
/*      */       } 
/*  814 */       return true;
/*      */     } 
/*  816 */     return false;
/*      */   }
/*      */   
/*      */   private void push(SpelNodeImpl newNode) {
/*  820 */     this.constructedNodes.push(newNode);
/*      */   }
/*      */   
/*      */   private SpelNodeImpl pop() {
/*  824 */     return this.constructedNodes.pop();
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
/*      */   private boolean maybeEatLiteral() {
/*  836 */     Token t = peekToken();
/*  837 */     if (t == null) {
/*  838 */       return false;
/*      */     }
/*  840 */     if (t.kind == TokenKind.LITERAL_INT) {
/*  841 */       push((SpelNodeImpl)Literal.getIntLiteral(t.stringValue(), t.startPos, t.endPos, 10));
/*      */     }
/*  843 */     else if (t.kind == TokenKind.LITERAL_LONG) {
/*  844 */       push((SpelNodeImpl)Literal.getLongLiteral(t.stringValue(), t.startPos, t.endPos, 10));
/*      */     }
/*  846 */     else if (t.kind == TokenKind.LITERAL_HEXINT) {
/*  847 */       push((SpelNodeImpl)Literal.getIntLiteral(t.stringValue(), t.startPos, t.endPos, 16));
/*      */     }
/*  849 */     else if (t.kind == TokenKind.LITERAL_HEXLONG) {
/*  850 */       push((SpelNodeImpl)Literal.getLongLiteral(t.stringValue(), t.startPos, t.endPos, 16));
/*      */     }
/*  852 */     else if (t.kind == TokenKind.LITERAL_REAL) {
/*  853 */       push((SpelNodeImpl)Literal.getRealLiteral(t.stringValue(), t.startPos, t.endPos, false));
/*      */     }
/*  855 */     else if (t.kind == TokenKind.LITERAL_REAL_FLOAT) {
/*  856 */       push((SpelNodeImpl)Literal.getRealLiteral(t.stringValue(), t.startPos, t.endPos, true));
/*      */     }
/*  858 */     else if (peekIdentifierToken("true")) {
/*  859 */       push((SpelNodeImpl)new BooleanLiteral(t.stringValue(), t.startPos, t.endPos, true));
/*      */     }
/*  861 */     else if (peekIdentifierToken("false")) {
/*  862 */       push((SpelNodeImpl)new BooleanLiteral(t.stringValue(), t.startPos, t.endPos, false));
/*      */     }
/*  864 */     else if (t.kind == TokenKind.LITERAL_STRING) {
/*  865 */       push((SpelNodeImpl)new StringLiteral(t.stringValue(), t.startPos, t.endPos, t.stringValue()));
/*      */     } else {
/*      */       
/*  868 */       return false;
/*      */     } 
/*  870 */     nextToken();
/*  871 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean maybeEatParenExpression() {
/*  876 */     if (peekToken(TokenKind.LPAREN)) {
/*  877 */       nextToken();
/*  878 */       SpelNodeImpl expr = eatExpression();
/*  879 */       Assert.state((expr != null), "No node");
/*  880 */       eatToken(TokenKind.RPAREN);
/*  881 */       push(expr);
/*  882 */       return true;
/*      */     } 
/*      */     
/*  885 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Token maybeEatRelationalOperator() {
/*  894 */     Token t = peekToken();
/*  895 */     if (t == null) {
/*  896 */       return null;
/*      */     }
/*  898 */     if (t.isNumericRelationalOperator()) {
/*  899 */       return t;
/*      */     }
/*  901 */     if (t.isIdentifier()) {
/*  902 */       String idString = t.stringValue();
/*  903 */       if (idString.equalsIgnoreCase("instanceof")) {
/*  904 */         return t.asInstanceOfToken();
/*      */       }
/*  906 */       if (idString.equalsIgnoreCase("matches")) {
/*  907 */         return t.asMatchesToken();
/*      */       }
/*  909 */       if (idString.equalsIgnoreCase("between")) {
/*  910 */         return t.asBetweenToken();
/*      */       }
/*      */     } 
/*  913 */     return null;
/*      */   }
/*      */   
/*      */   private Token eatToken(TokenKind expectedKind) {
/*  917 */     Token t = nextToken();
/*  918 */     if (t == null) {
/*  919 */       int pos = this.expressionString.length();
/*  920 */       throw internalException(pos, SpelMessage.OOD, new Object[0]);
/*      */     } 
/*  922 */     if (t.kind != expectedKind) {
/*  923 */       throw internalException(t.startPos, SpelMessage.NOT_EXPECTED_TOKEN, new Object[] { expectedKind
/*  924 */             .toString().toLowerCase(), t.getKind().toString().toLowerCase() });
/*      */     }
/*  926 */     return t;
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind desiredTokenKind) {
/*  930 */     return peekToken(desiredTokenKind, false);
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind desiredTokenKind, boolean consumeIfMatched) {
/*  934 */     Token t = peekToken();
/*  935 */     if (t == null) {
/*  936 */       return false;
/*      */     }
/*  938 */     if (t.kind == desiredTokenKind) {
/*  939 */       if (consumeIfMatched) {
/*  940 */         this.tokenStreamPointer++;
/*      */       }
/*  942 */       return true;
/*      */     } 
/*      */     
/*  945 */     if (desiredTokenKind == TokenKind.IDENTIFIER)
/*      */     {
/*      */ 
/*      */       
/*  949 */       if (t.kind.ordinal() >= TokenKind.DIV.ordinal() && t.kind.ordinal() <= TokenKind.NOT.ordinal() && t.data != null)
/*      */       {
/*      */         
/*  952 */         return true;
/*      */       }
/*      */     }
/*  955 */     return false;
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind possible1, TokenKind possible2) {
/*  959 */     Token t = peekToken();
/*  960 */     if (t == null) {
/*  961 */       return false;
/*      */     }
/*  963 */     return (t.kind == possible1 || t.kind == possible2);
/*      */   }
/*      */   
/*      */   private boolean peekToken(TokenKind possible1, TokenKind possible2, TokenKind possible3) {
/*  967 */     Token t = peekToken();
/*  968 */     if (t == null) {
/*  969 */       return false;
/*      */     }
/*  971 */     return (t.kind == possible1 || t.kind == possible2 || t.kind == possible3);
/*      */   }
/*      */   
/*      */   private boolean peekIdentifierToken(String identifierString) {
/*  975 */     Token t = peekToken();
/*  976 */     if (t == null) {
/*  977 */       return false;
/*      */     }
/*  979 */     return (t.kind == TokenKind.IDENTIFIER && identifierString.equalsIgnoreCase(t.stringValue()));
/*      */   }
/*      */   
/*      */   private boolean peekSelectToken() {
/*  983 */     Token t = peekToken();
/*  984 */     if (t == null) {
/*  985 */       return false;
/*      */     }
/*  987 */     return (t.kind == TokenKind.SELECT || t.kind == TokenKind.SELECT_FIRST || t.kind == TokenKind.SELECT_LAST);
/*      */   }
/*      */   
/*      */   private Token takeToken() {
/*  991 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/*  992 */       throw new IllegalStateException("No token");
/*      */     }
/*  994 */     return this.tokenStream.get(this.tokenStreamPointer++);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Token nextToken() {
/*  999 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/* 1000 */       return null;
/*      */     }
/* 1002 */     return this.tokenStream.get(this.tokenStreamPointer++);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Token peekToken() {
/* 1007 */     if (this.tokenStreamPointer >= this.tokenStreamLength) {
/* 1008 */       return null;
/*      */     }
/* 1010 */     return this.tokenStream.get(this.tokenStreamPointer);
/*      */   }
/*      */   
/*      */   public String toString(@Nullable Token t) {
/* 1014 */     if (t == null) {
/* 1015 */       return "";
/*      */     }
/* 1017 */     if (t.getKind().hasPayload()) {
/* 1018 */       return t.stringValue();
/*      */     }
/* 1020 */     return t.kind.toString().toLowerCase();
/*      */   }
/*      */   
/*      */   private void checkOperands(Token token, @Nullable SpelNodeImpl left, @Nullable SpelNodeImpl right) {
/* 1024 */     checkLeftOperand(token, left);
/* 1025 */     checkRightOperand(token, right);
/*      */   }
/*      */   
/*      */   private void checkLeftOperand(Token token, @Nullable SpelNodeImpl operandExpression) {
/* 1029 */     if (operandExpression == null) {
/* 1030 */       throw internalException(token.startPos, SpelMessage.LEFT_OPERAND_PROBLEM, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkRightOperand(Token token, @Nullable SpelNodeImpl operandExpression) {
/* 1035 */     if (operandExpression == null) {
/* 1036 */       throw internalException(token.startPos, SpelMessage.RIGHT_OPERAND_PROBLEM, new Object[0]);
/*      */     }
/*      */   }
/*      */   
/*      */   private InternalParseException internalException(int startPos, SpelMessage message, Object... inserts) {
/* 1041 */     return new InternalParseException(new SpelParseException(this.expressionString, startPos, message, inserts));
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\standard\InternalSpelExpressionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */