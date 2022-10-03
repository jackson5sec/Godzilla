/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.support.BooleanTypedValue;
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
/*     */ public class OperatorMatches
/*     */   extends Operator
/*     */ {
/*     */   private static final int PATTERN_ACCESS_THRESHOLD = 1000000;
/*  45 */   private final ConcurrentMap<String, Pattern> patternCache = new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*     */   public OperatorMatches(int startPos, int endPos, SpelNodeImpl... operands) {
/*  49 */     super("matches", startPos, endPos, operands);
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
/*     */   public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  63 */     SpelNodeImpl leftOp = getLeftOperand();
/*  64 */     SpelNodeImpl rightOp = getRightOperand();
/*  65 */     String left = leftOp.<String>getValue(state, String.class);
/*  66 */     Object right = getRightOperand().getValue(state);
/*     */     
/*  68 */     if (left == null) {
/*  69 */       throw new SpelEvaluationException(leftOp.getStartPosition(), SpelMessage.INVALID_FIRST_OPERAND_FOR_MATCHES_OPERATOR, new Object[] { null });
/*     */     }
/*     */     
/*  72 */     if (!(right instanceof String)) {
/*  73 */       throw new SpelEvaluationException(rightOp.getStartPosition(), SpelMessage.INVALID_SECOND_OPERAND_FOR_MATCHES_OPERATOR, new Object[] { right });
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  78 */       String rightString = (String)right;
/*  79 */       Pattern pattern = this.patternCache.get(rightString);
/*  80 */       if (pattern == null) {
/*  81 */         pattern = Pattern.compile(rightString);
/*  82 */         this.patternCache.putIfAbsent(rightString, pattern);
/*     */       } 
/*  84 */       Matcher matcher = pattern.matcher(new MatcherInput(left, new AccessCount()));
/*  85 */       return BooleanTypedValue.forValue(matcher.matches());
/*     */     }
/*  87 */     catch (PatternSyntaxException ex) {
/*  88 */       throw new SpelEvaluationException(rightOp
/*  89 */           .getStartPosition(), ex, SpelMessage.INVALID_PATTERN, new Object[] { right });
/*     */     }
/*  91 */     catch (IllegalStateException ex) {
/*  92 */       throw new SpelEvaluationException(rightOp
/*  93 */           .getStartPosition(), ex, SpelMessage.FLAWED_PATTERN, new Object[] { right });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class AccessCount {
/*     */     private int count;
/*     */     
/*     */     private AccessCount() {}
/*     */     
/*     */     public void check() throws IllegalStateException {
/* 103 */       if (this.count++ > 1000000) {
/* 104 */         throw new IllegalStateException("Pattern access threshold exceeded");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MatcherInput
/*     */     implements CharSequence
/*     */   {
/*     */     private final CharSequence value;
/*     */     private OperatorMatches.AccessCount access;
/*     */     
/*     */     public MatcherInput(CharSequence value, OperatorMatches.AccessCount access) {
/* 117 */       this.value = value;
/* 118 */       this.access = access;
/*     */     }
/*     */ 
/*     */     
/*     */     public char charAt(int index) {
/* 123 */       this.access.check();
/* 124 */       return this.value.charAt(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSequence subSequence(int start, int end) {
/* 129 */       return new MatcherInput(this.value.subSequence(start, end), this.access);
/*     */     }
/*     */ 
/*     */     
/*     */     public int length() {
/* 134 */       return this.value.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 139 */       return this.value.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\OperatorMatches.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */