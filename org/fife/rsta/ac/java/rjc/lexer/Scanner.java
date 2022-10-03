/*     */ package org.fife.rsta.ac.java.rjc.lexer;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Position;
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
/*     */ public class Scanner
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private SourceCodeScanner s;
/*     */   private Stack<Token> stack;
/*     */   private int typeArgLevel;
/*     */   private Document doc;
/*     */   private Token mostRecentToken;
/*     */   private Stack<Stack<Token>> resetPositions;
/*     */   private Stack<Token> currentResetTokenStack;
/*     */   private int currentResetStartOffset;
/*     */   
/*     */   public Scanner() {
/*  67 */     this((Reader)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scanner(List<Token> tokens) {
/*  77 */     this.stack = new Stack<>();
/*  78 */     for (int i = tokens.size() - 1; i >= 0; i--) {
/*  79 */       this.stack.push(tokens.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scanner(Reader r) {
/*  90 */     this.s = (r != null) ? new SourceCodeScanner(r) : null;
/*  91 */     this.s.setKeepLastDocComment(true);
/*  92 */     this.stack = new Stack<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushOntoStack(Token t) {
/* 103 */     if (t != null && !this.stack.isEmpty() && t.equals(this.stack.peek())) {
/* 104 */       System.err.println("ERROR: Token being duplicated: " + t);
/* 105 */       Thread.dumpStack();
/* 106 */       System.exit(5);
/*     */     }
/* 108 */     else if (t == null) {
/* 109 */       System.err.println("ERROR: null token pushed onto stack");
/* 110 */       Thread.dumpStack();
/* 111 */       System.exit(6);
/*     */     } 
/* 113 */     this.stack.push(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseTypeArgumentsLevel() {
/* 124 */     if (--this.typeArgLevel < 0) {
/* 125 */       throw new InternalError("typeArgLevel dipped below 0");
/*     */     }
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
/*     */   public Offset createOffset(int offs) {
/* 139 */     if (this.doc != null) {
/*     */       try {
/* 141 */         return new DocumentOffset(this.doc.createPosition(offs));
/* 142 */       } catch (BadLocationException ble) {
/* 143 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/* 146 */     return () -> offs;
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
/*     */   private void debugPrintToken(Token t) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 169 */     return this.s.getColumn();
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
/*     */   public String getLastDocComment() {
/* 181 */     return this.s.getLastDocComment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 192 */     return this.s.getLine();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Token getMostRecentToken() {
/* 202 */     return this.mostRecentToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 212 */     return this.s.getOffset();
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
/*     */   public void eatParenPairs() throws IOException {
/* 226 */     Token t = yylex();
/* 227 */     if (t == null || t.getType() != 8388609) {
/* 228 */       throw new InternalError("'(' expected, found: " + t);
/*     */     }
/*     */     
/* 231 */     int blockDepth = 0;
/* 232 */     int parenDepth = 1;
/*     */     
/* 234 */     while ((t = yylex()) != null) {
/* 235 */       int type = t.getType();
/* 236 */       switch (type) {
/*     */         case 8388611:
/* 238 */           blockDepth++;
/*     */         
/*     */         case 8388612:
/* 241 */           blockDepth = Math.max(blockDepth - 1, 0);
/*     */         
/*     */         case 8388609:
/* 244 */           if (blockDepth == 0) {
/* 245 */             parenDepth++;
/*     */           }
/*     */         
/*     */         case 8388610:
/* 249 */           if (blockDepth == 0 && --parenDepth == 0) {
/*     */             return;
/*     */           }
/*     */       } 
/*     */     } 
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
/*     */   public void eatThroughNext(int tokenType) throws IOException {
/*     */     Token t;
/* 268 */     while ((t = yylex()) != null && t.getType() != tokenType);
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
/*     */   public void eatThroughNextSkippingBlocks(int tokenType) throws IOException {
/* 283 */     int blockDepth = 0; Token t;
/* 284 */     while ((t = yylex()) != null) {
/* 285 */       int type = t.getType();
/* 286 */       if (type == 8388611) {
/* 287 */         blockDepth++; continue;
/*     */       } 
/* 289 */       if (type == 8388612) {
/* 290 */         blockDepth--; continue;
/*     */       } 
/* 292 */       if (type == tokenType && 
/* 293 */         blockDepth <= 0) {
/*     */         return;
/*     */       }
/*     */     } 
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
/*     */   public Token eatThroughNextSkippingBlocks(int tokenType1, int tokenType2) throws IOException {
/* 317 */     int blockDepth = 0; Token t;
/* 318 */     while ((t = yylex()) != null) {
/* 319 */       int type = t.getType();
/* 320 */       if (type == 8388611) {
/* 321 */         blockDepth++; continue;
/*     */       } 
/* 323 */       if (type == 8388612) {
/* 324 */         blockDepth--; continue;
/*     */       } 
/* 326 */       if ((type == tokenType1 || type == tokenType2) && 
/* 327 */         blockDepth <= 0) {
/* 328 */         return t;
/*     */       }
/*     */     } 
/*     */     
/* 332 */     return null;
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
/*     */   public Token eatThroughNextSkippingBlocksAndStuffInParens(int tokenType1, int tokenType2) throws IOException {
/* 353 */     int blockDepth = 0;
/* 354 */     int parenDepth = 0;
/*     */     Token t;
/* 356 */     while ((t = yylex()) != null) {
/* 357 */       int type = t.getType();
/* 358 */       switch (type) {
/*     */         case 8388611:
/* 360 */           blockDepth++;
/*     */           continue;
/*     */         case 8388612:
/* 363 */           blockDepth--;
/*     */           continue;
/*     */         case 8388609:
/* 366 */           parenDepth++;
/*     */           continue;
/*     */         case 8388610:
/* 369 */           parenDepth--;
/*     */           continue;
/*     */       } 
/* 372 */       if ((type == tokenType1 || type == tokenType2) && 
/* 373 */         blockDepth <= 0 && parenDepth <= 0) {
/* 374 */         return t;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 380 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void eatUntilNext(int type1, int type2) throws IOException {
/*     */     Token t;
/* 387 */     while ((t = yylex()) != null) {
/* 388 */       int type = t.getType();
/* 389 */       if (type == type1 || type == type2) {
/* 390 */         yyPushback(t);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void eatUntilNext(int type1, int type2, int type3) throws IOException {
/*     */     Token t;
/* 399 */     while ((t = yylex()) != null) {
/* 400 */       int type = t.getType();
/* 401 */       if (type == type1 || type == type2 || type == type3) {
/* 402 */         yyPushback(t);
/*     */         break;
/*     */       } 
/*     */     } 
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
/*     */   public int getTypeArgumentsLevel() {
/* 417 */     return this.typeArgLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseTypeArgumentsLevel() {
/* 428 */     this.typeArgLevel++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markResetPosition() {
/* 436 */     if (this.s != null) {
/* 437 */       if (this.resetPositions == null) {
/* 438 */         this.resetPositions = new Stack<>();
/*     */       }
/* 440 */       this.currentResetTokenStack = new Stack<>();
/* 441 */       this.resetPositions.push(this.currentResetTokenStack);
/* 442 */       this.currentResetStartOffset = this.s.getOffset();
/*     */     } 
/*     */   }
/*     */   public void resetToLastMarkedPosition() {
/* 446 */     if (this.s != null) {
/* 447 */       if (this.currentResetTokenStack == null) {
/* 448 */         throw new InternalError("No resetTokenStack!");
/*     */       }
/*     */       
/* 451 */       while (!this.stack.isEmpty()) {
/* 452 */         Token t = this.stack.peek();
/* 453 */         if (t.getOffset() >= this.currentResetStartOffset) {
/* 454 */           this.stack.pop();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 461 */       while (!this.currentResetTokenStack.isEmpty()) {
/* 462 */         Token t = this.currentResetTokenStack.pop();
/* 463 */         this.stack.push(t);
/*     */       } 
/* 465 */       this.resetPositions.pop();
/* 466 */       this.currentResetTokenStack = this.resetPositions.isEmpty() ? null : this.resetPositions.peek();
/* 467 */       this.currentResetStartOffset = -1;
/*     */     } 
/*     */   }
/*     */   public void clearResetPosition() {
/* 471 */     if (this.s != null) {
/* 472 */       if (this.currentResetTokenStack == null) {
/* 473 */         throw new InternalError("No resetTokenStack!");
/*     */       }
/* 475 */       this.resetPositions.pop();
/* 476 */       this.currentResetTokenStack = this.resetPositions.isEmpty() ? null : this.resetPositions.peek();
/* 477 */       this.currentResetStartOffset = -1;
/*     */     } 
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
/*     */   public void setDocument(Document doc) {
/* 491 */     this.doc = doc;
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
/*     */   public int skipBracketPairs() throws IOException {
/* 503 */     int count = 0;
/*     */     
/* 505 */     while (yyPeekCheckType() == 8388613 && 
/* 506 */       yyPeekCheckType(2) == 8388614) {
/* 507 */       yylex();
/* 508 */       yylex();
/* 509 */       count++;
/*     */     } 
/*     */     
/* 512 */     return count;
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
/*     */   public Token yylex() throws IOException {
/*     */     Token t;
/* 529 */     if (this.stack.isEmpty()) {
/* 530 */       t = (this.s != null) ? this.s.yylex() : null;
/*     */     } else {
/*     */       
/* 533 */       t = this.stack.pop();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 538 */     if (this.typeArgLevel > 0 && t != null && t.isOperator()) {
/* 539 */       String lexeme = t.getLexeme();
/* 540 */       if (lexeme.length() > 1) {
/* 541 */         char ch = lexeme.charAt(0);
/* 542 */         if (ch == '<') {
/* 543 */           Token rest = null;
/* 544 */           switch (t.getType()) {
/*     */             
/*     */             case 16777225:
/* 547 */               rest = new TokenImpl(33554433, "=", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */             
/*     */             case 16777240:
/* 551 */               rest = new TokenImpl(16777219, "<", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */             
/*     */             case 33554467:
/* 555 */               rest = new TokenImpl(16777225, "<=", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */           } 
/* 558 */           this.stack.push(rest);
/*     */           
/* 560 */           t = new TokenImpl(16777219, "<", t.getLine(), t.getColumn(), t.getOffset());
/*     */         }
/* 562 */         else if (ch == '>') {
/* 563 */           Token rest = null;
/* 564 */           switch (t.getType()) {
/*     */             
/*     */             case 16777226:
/* 567 */               rest = new TokenImpl(33554433, "=", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */             
/*     */             case 16777241:
/* 571 */               rest = new TokenImpl(16777218, ">", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */             
/*     */             case 16777242:
/* 575 */               rest = new TokenImpl(16777241, ">>", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */             
/*     */             case 33554468:
/* 579 */               rest = new TokenImpl(16777226, ">=", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */             
/*     */             case 33554469:
/* 583 */               rest = new TokenImpl(33554468, ">>=", t.getLine(), t.getColumn() + 1, t.getOffset() + 1);
/*     */               break;
/*     */           } 
/* 586 */           this.stack.push(rest);
/*     */           
/* 588 */           t = new TokenImpl(16777218, ">", t.getLine(), t.getColumn(), t.getOffset());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 593 */     debugPrintToken(t);
/* 594 */     if (this.currentResetTokenStack != null) {
/* 595 */       this.currentResetTokenStack.push(t);
/*     */     }
/* 597 */     if (t != null) {
/* 598 */       this.mostRecentToken = t;
/*     */     }
/* 600 */     return t;
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
/*     */   public Token yylexNonNull(String error) throws IOException {
/* 616 */     Token t = yylex();
/* 617 */     if (t == null) {
/* 618 */       throw new EOFException(error);
/*     */     }
/* 620 */     return t;
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
/*     */   public Token yylexNonNull(int type, String error) throws IOException {
/* 637 */     return yylexNonNull(type, -1, error);
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
/*     */   public Token yylexNonNull(int type1, int type2, String error) throws IOException {
/* 657 */     return yylexNonNull(type1, type2, -1, error);
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
/*     */   public Token yylexNonNull(int type1, int type2, int type3, String error) throws IOException {
/* 679 */     Token t = yylex();
/* 680 */     if (t == null) {
/* 681 */       throw new IOException(error);
/*     */     }
/* 683 */     if (t.getType() != type1 && (type2 == -1 || t.getType() != type2) && (type3 == -1 || t
/* 684 */       .getType() != type3)) {
/* 685 */       throw new IOException(error + ", found '" + t.getLexeme() + "'");
/*     */     }
/* 687 */     return t;
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
/*     */   public Token yyPeek() throws IOException {
/* 699 */     Token t = yylex();
/* 700 */     if (t != null) {
/* 701 */       pushOntoStack(t);
/*     */     }
/* 703 */     return t;
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
/*     */   public Token yyPeek(int depth) throws IOException {
/* 717 */     if (depth < 1) {
/* 718 */       throw new IllegalArgumentException("depth must be >= 1");
/*     */     }
/* 720 */     Stack<Token> read = new Stack<>();
/* 721 */     for (int i = 0; i < depth; i++) {
/* 722 */       Token token = yylex();
/* 723 */       if (token != null) {
/* 724 */         read.push(token);
/*     */       } else {
/*     */         
/* 727 */         while (!read.isEmpty()) {
/* 728 */           yyPushback(read.pop());
/*     */         }
/* 730 */         return null;
/*     */       } 
/*     */     } 
/* 733 */     Token t = read.peek();
/* 734 */     while (!read.isEmpty()) {
/* 735 */       yyPushback(read.pop());
/*     */     }
/* 737 */     return t;
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
/*     */   public int yyPeekCheckType() throws IOException {
/* 749 */     Token t = yyPeek();
/* 750 */     return (t != null) ? t.getType() : -1;
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
/*     */   public int yyPeekCheckType(int index) throws IOException {
/* 763 */     Token t = yyPeek(index);
/* 764 */     return (t != null) ? t.getType() : -1;
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
/*     */   public Token yyPeekNonNull(String error) throws IOException {
/* 776 */     Token t = yyPeek();
/* 777 */     if (t == null) {
/* 778 */       throw new IOException(error);
/*     */     }
/* 780 */     return t;
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
/*     */   public Token yyPeekNonNull(int type, String error) throws IOException {
/* 794 */     return yyPeekNonNull(type, -1, error);
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
/*     */   public Token yyPeekNonNull(int type1, int type2, String error) throws IOException {
/* 810 */     return yyPeekNonNull(type1, type2, -1, error);
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
/*     */   public Token yyPeekNonNull(int type1, int type2, int type3, String error) throws IOException {
/* 827 */     Token t = yyPeek();
/* 828 */     if (t == null) {
/* 829 */       throw new IOException(error);
/*     */     }
/* 831 */     if (t.getType() != type1 && (type2 == -1 || t.getType() != type2) && (type3 == -1 || t
/* 832 */       .getType() != type3)) {
/* 833 */       throw new IOException(error + ", found '" + t.getLexeme() + "'");
/*     */     }
/* 835 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void yyPushback(Token t) {
/* 845 */     if (t != null) {
/* 846 */       pushOntoStack(t);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DocumentOffset
/*     */     implements Offset
/*     */   {
/*     */     public Position pos;
/*     */     
/*     */     public DocumentOffset(Position pos) {
/* 856 */       this.pos = pos;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getOffset() {
/* 861 */       return this.pos.getOffset();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\lexer\Scanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */