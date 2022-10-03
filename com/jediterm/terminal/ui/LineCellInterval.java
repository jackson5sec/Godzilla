/*    */ package com.jediterm.terminal.ui;
/*    */ 
/*    */ final class LineCellInterval {
/*    */   private final int myLine;
/*    */   private final int myStartColumn;
/*    */   private final int myEndColumn;
/*    */   
/*    */   public LineCellInterval(int line, int startColumn, int endColumn) {
/*  9 */     this.myLine = line;
/* 10 */     this.myStartColumn = startColumn;
/* 11 */     this.myEndColumn = endColumn;
/*    */   }
/*    */   
/*    */   public int getLine() {
/* 15 */     return this.myLine;
/*    */   }
/*    */   
/*    */   public int getStartColumn() {
/* 19 */     return this.myStartColumn;
/*    */   }
/*    */   
/*    */   public int getEndColumn() {
/* 23 */     return this.myEndColumn;
/*    */   }
/*    */   
/*    */   public int getCellCount() {
/* 27 */     return this.myEndColumn - this.myStartColumn + 1;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\LineCellInterval.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */