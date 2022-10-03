/*    */ package com.jediterm.terminal.model;
/*    */ 
/*    */ import com.jediterm.terminal.util.Pair;
/*    */ import java.awt.Point;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TerminalSelection
/*    */ {
/*    */   private final Point myStart;
/*    */   private Point myEnd;
/*    */   
/*    */   public TerminalSelection(Point start) {
/* 17 */     this.myStart = start;
/*    */   }
/*    */   
/*    */   public TerminalSelection(Point start, Point end) {
/* 21 */     this.myStart = start;
/* 22 */     this.myEnd = end;
/*    */   }
/*    */   
/*    */   public Point getStart() {
/* 26 */     return this.myStart;
/*    */   }
/*    */   
/*    */   public Point getEnd() {
/* 30 */     return this.myEnd;
/*    */   }
/*    */   
/*    */   public void updateEnd(Point end) {
/* 34 */     this.myEnd = end;
/*    */   }
/*    */   
/*    */   public Pair<Point, Point> pointsForRun(int width) {
/* 38 */     Pair<Point, Point> p = SelectionUtil.sortPoints(new Point(this.myStart), new Point(this.myEnd));
/* 39 */     ((Point)p.second).x = Math.min(((Point)p.second).x + 1, width);
/* 40 */     return p;
/*    */   }
/*    */   
/*    */   public boolean contains(Point toTest) {
/* 44 */     return intersects(toTest.x, toTest.y, 1);
/*    */   }
/*    */   
/*    */   public void shiftY(int dy) {
/* 48 */     this.myStart.y += dy;
/* 49 */     this.myEnd.y += dy;
/*    */   }
/*    */   
/*    */   public boolean intersects(int x, int row, int length) {
/* 53 */     return (null != intersect(x, row, length));
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Pair<Integer, Integer> intersect(int x, int row, int length) {
/* 58 */     int newLength, newX = x;
/*    */ 
/*    */     
/* 61 */     Pair<Point, Point> p = SelectionUtil.sortPoints(new Point(this.myStart), new Point(this.myEnd));
/*    */     
/* 63 */     if (((Point)p.first).y == row) {
/* 64 */       newX = Math.max(x, ((Point)p.first).x);
/*    */     }
/*    */     
/* 67 */     if (((Point)p.second).y == row) {
/* 68 */       newLength = Math.min(((Point)p.second).x, x + length - 1) - newX + 1;
/*    */     } else {
/* 70 */       newLength = length - newX + x;
/*    */     } 
/*    */     
/* 73 */     if (newLength <= 0 || row < ((Point)p.first).y || row > ((Point)p.second).y) {
/* 74 */       return null;
/*    */     }
/* 76 */     return Pair.create(Integer.valueOf(newX), Integer.valueOf(newLength));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     return "[x=" + this.myStart.x + ",y=" + this.myStart.y + "] -> [x=" + this.myEnd.x + ",y=" + this.myEnd.y + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\TerminalSelection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */