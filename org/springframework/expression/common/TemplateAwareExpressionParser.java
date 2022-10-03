/*     */ package org.springframework.expression.common;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.ExpressionParser;
/*     */ import org.springframework.expression.ParseException;
/*     */ import org.springframework.expression.ParserContext;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class TemplateAwareExpressionParser
/*     */   implements ExpressionParser
/*     */ {
/*     */   public Expression parseExpression(String expressionString) throws ParseException {
/*  43 */     return parseExpression(expressionString, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression parseExpression(String expressionString, @Nullable ParserContext context) throws ParseException {
/*  48 */     if (context != null && context.isTemplate()) {
/*  49 */       return parseTemplate(expressionString, context);
/*     */     }
/*     */     
/*  52 */     return doParseExpression(expressionString, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression parseTemplate(String expressionString, ParserContext context) throws ParseException {
/*  58 */     if (expressionString.isEmpty()) {
/*  59 */       return new LiteralExpression("");
/*     */     }
/*     */     
/*  62 */     Expression[] expressions = parseExpressions(expressionString, context);
/*  63 */     if (expressions.length == 1) {
/*  64 */       return expressions[0];
/*     */     }
/*     */     
/*  67 */     return new CompositeStringExpression(expressionString, expressions);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression[] parseExpressions(String expressionString, ParserContext context) throws ParseException {
/*  90 */     List<Expression> expressions = new ArrayList<>();
/*  91 */     String prefix = context.getExpressionPrefix();
/*  92 */     String suffix = context.getExpressionSuffix();
/*  93 */     int startIdx = 0;
/*     */     
/*  95 */     while (startIdx < expressionString.length()) {
/*  96 */       int prefixIndex = expressionString.indexOf(prefix, startIdx);
/*  97 */       if (prefixIndex >= startIdx) {
/*     */         
/*  99 */         if (prefixIndex > startIdx) {
/* 100 */           expressions.add(new LiteralExpression(expressionString.substring(startIdx, prefixIndex)));
/*     */         }
/* 102 */         int afterPrefixIndex = prefixIndex + prefix.length();
/* 103 */         int suffixIndex = skipToCorrectEndSuffix(suffix, expressionString, afterPrefixIndex);
/* 104 */         if (suffixIndex == -1) {
/* 105 */           throw new ParseException(expressionString, prefixIndex, "No ending suffix '" + suffix + "' for expression starting at character " + prefixIndex + ": " + expressionString
/*     */               
/* 107 */               .substring(prefixIndex));
/*     */         }
/* 109 */         if (suffixIndex == afterPrefixIndex) {
/* 110 */           throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + prefix + suffix + "' at character " + prefixIndex);
/*     */         }
/*     */ 
/*     */         
/* 114 */         String expr = expressionString.substring(prefixIndex + prefix.length(), suffixIndex);
/* 115 */         expr = expr.trim();
/* 116 */         if (expr.isEmpty()) {
/* 117 */           throw new ParseException(expressionString, prefixIndex, "No expression defined within delimiter '" + prefix + suffix + "' at character " + prefixIndex);
/*     */         }
/*     */ 
/*     */         
/* 121 */         expressions.add(doParseExpression(expr, context));
/* 122 */         startIdx = suffixIndex + suffix.length();
/*     */         
/*     */         continue;
/*     */       } 
/* 126 */       expressions.add(new LiteralExpression(expressionString.substring(startIdx)));
/* 127 */       startIdx = expressionString.length();
/*     */     } 
/*     */ 
/*     */     
/* 131 */     return expressions.<Expression>toArray(new Expression[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSuffixHere(String expressionString, int pos, String suffix) {
/* 142 */     int suffixPosition = 0;
/* 143 */     for (int i = 0; i < suffix.length() && pos < expressionString.length(); i++) {
/* 144 */       if (expressionString.charAt(pos++) != suffix.charAt(suffixPosition++)) {
/* 145 */         return false;
/*     */       }
/*     */     } 
/* 148 */     if (suffixPosition != suffix.length())
/*     */     {
/* 150 */       return false;
/*     */     }
/* 152 */     return true;
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
/*     */   private int skipToCorrectEndSuffix(String suffix, String expressionString, int afterPrefixIndex) throws ParseException {
/* 170 */     int pos = afterPrefixIndex;
/* 171 */     int maxlen = expressionString.length();
/* 172 */     int nextSuffix = expressionString.indexOf(suffix, afterPrefixIndex);
/* 173 */     if (nextSuffix == -1) {
/* 174 */       return -1;
/*     */     }
/* 176 */     Deque<Bracket> stack = new ArrayDeque<>();
/* 177 */     while (pos < maxlen && (
/* 178 */       !isSuffixHere(expressionString, pos, suffix) || !stack.isEmpty())) {
/*     */       Bracket p;
/*     */       int endLiteral;
/* 181 */       char ch = expressionString.charAt(pos);
/* 182 */       switch (ch) {
/*     */         case '(':
/*     */         case '[':
/*     */         case '{':
/* 186 */           stack.push(new Bracket(ch, pos));
/*     */           break;
/*     */         case ')':
/*     */         case ']':
/*     */         case '}':
/* 191 */           if (stack.isEmpty()) {
/* 192 */             throw new ParseException(expressionString, pos, "Found closing '" + ch + "' at position " + pos + " without an opening '" + 
/*     */                 
/* 194 */                 Bracket.theOpenBracketFor(ch) + "'");
/*     */           }
/* 196 */           p = stack.pop();
/* 197 */           if (!p.compatibleWithCloseBracket(ch)) {
/* 198 */             throw new ParseException(expressionString, pos, "Found closing '" + ch + "' at position " + pos + " but most recent opening is '" + p.bracket + "' at position " + p.pos);
/*     */           }
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case '"':
/*     */         case '\'':
/* 206 */           endLiteral = expressionString.indexOf(ch, pos + 1);
/* 207 */           if (endLiteral == -1) {
/* 208 */             throw new ParseException(expressionString, pos, "Found non terminating string literal starting at position " + pos);
/*     */           }
/*     */           
/* 211 */           pos = endLiteral;
/*     */           break;
/*     */       } 
/* 214 */       pos++;
/*     */     } 
/* 216 */     if (!stack.isEmpty()) {
/* 217 */       Bracket p = stack.pop();
/* 218 */       throw new ParseException(expressionString, p.pos, "Missing closing '" + 
/* 219 */           Bracket.theCloseBracketFor(p.bracket) + "' for '" + p.bracket + "' at position " + p.pos);
/*     */     } 
/* 221 */     if (!isSuffixHere(expressionString, pos, suffix)) {
/* 222 */       return -1;
/*     */     }
/* 224 */     return pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Expression doParseExpression(String paramString, @Nullable ParserContext paramParserContext) throws ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Bracket
/*     */   {
/*     */     char bracket;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int pos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Bracket(char bracket, int pos) {
/* 252 */       this.bracket = bracket;
/* 253 */       this.pos = pos;
/*     */     }
/*     */     
/*     */     boolean compatibleWithCloseBracket(char closeBracket) {
/* 257 */       if (this.bracket == '{') {
/* 258 */         return (closeBracket == '}');
/*     */       }
/* 260 */       if (this.bracket == '[') {
/* 261 */         return (closeBracket == ']');
/*     */       }
/* 263 */       return (closeBracket == ')');
/*     */     }
/*     */     
/*     */     static char theOpenBracketFor(char closeBracket) {
/* 267 */       if (closeBracket == '}') {
/* 268 */         return '{';
/*     */       }
/* 270 */       if (closeBracket == ']') {
/* 271 */         return '[';
/*     */       }
/* 273 */       return '(';
/*     */     }
/*     */     
/*     */     static char theCloseBracketFor(char openBracket) {
/* 277 */       if (openBracket == '{') {
/* 278 */         return '}';
/*     */       }
/* 280 */       if (openBracket == '[') {
/* 281 */         return ']';
/*     */       }
/* 283 */       return ')';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\common\TemplateAwareExpressionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */