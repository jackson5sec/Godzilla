/*    */ package com.jediterm.terminal.ui;
/*    */ 
/*    */ final class Cell {
/*    */   private final int myLine;
/*    */   private final int myColumn;
/*    */   
/*    */   public Cell(int line, int column) {
/*  8 */     this.myLine = line;
/*  9 */     this.myColumn = column;
/*    */   }
/*    */   
/*    */   public int getLine() {
/* 13 */     return this.myLine;
/*    */   }
/*    */   
/*    */   public int getColumn() {
/* 17 */     return this.myColumn;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\Cell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */