/*    */ package org.springframework.expression.spel.standard;
/*    */ 
/*    */ import org.springframework.expression.Expression;
/*    */ import org.springframework.expression.ParseException;
/*    */ import org.springframework.expression.ParserContext;
/*    */ import org.springframework.expression.common.TemplateAwareExpressionParser;
/*    */ import org.springframework.expression.spel.SpelParserConfiguration;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpelExpressionParser
/*    */   extends TemplateAwareExpressionParser
/*    */ {
/*    */   private final SpelParserConfiguration configuration;
/*    */   
/*    */   public SpelExpressionParser() {
/* 42 */     this.configuration = new SpelParserConfiguration();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SpelExpressionParser(SpelParserConfiguration configuration) {
/* 50 */     Assert.notNull(configuration, "SpelParserConfiguration must not be null");
/* 51 */     this.configuration = configuration;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpelExpression parseRaw(String expressionString) throws ParseException {
/* 56 */     return doParseExpression(expressionString, (ParserContext)null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SpelExpression doParseExpression(String expressionString, @Nullable ParserContext context) throws ParseException {
/* 61 */     return (new InternalSpelExpressionParser(this.configuration)).doParseExpression(expressionString, context);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\standard\SpelExpressionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */