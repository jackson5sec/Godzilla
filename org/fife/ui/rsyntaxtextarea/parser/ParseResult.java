package org.fife.ui.rsyntaxtextarea.parser;

import java.util.List;

public interface ParseResult {
  Exception getError();
  
  int getFirstLineParsed();
  
  int getLastLineParsed();
  
  List<ParserNotice> getNotices();
  
  Parser getParser();
  
  long getParseTime();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\ParseResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */