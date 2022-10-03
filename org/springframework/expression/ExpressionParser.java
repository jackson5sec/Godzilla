package org.springframework.expression;

public interface ExpressionParser {
  Expression parseExpression(String paramString) throws ParseException;
  
  Expression parseExpression(String paramString, ParserContext paramParserContext) throws ParseException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\ExpressionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */