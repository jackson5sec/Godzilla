/*    */ package com.kitfox.svg.pathcmd;
/*    */ 
/*    */ import java.awt.geom.AffineTransform;
/*    */ import java.awt.geom.GeneralPath;
/*    */ import java.awt.geom.PathIterator;
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
/*    */ public class PathUtil
/*    */ {
/*    */   public static String buildPathString(GeneralPath path) {
/* 61 */     float[] coords = new float[6];
/*    */     
/* 63 */     StringBuffer sb = new StringBuffer();
/*    */     
/* 65 */     for (PathIterator pathIt = path.getPathIterator(new AffineTransform()); !pathIt.isDone(); pathIt.next()) {
/*    */       
/* 67 */       int segId = pathIt.currentSegment(coords);
/*    */       
/* 69 */       switch (segId) {
/*    */ 
/*    */         
/*    */         case 4:
/* 73 */           sb.append(" Z");
/*    */           break;
/*    */ 
/*    */         
/*    */         case 3:
/* 78 */           sb.append(" C " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + " " + coords[4] + " " + coords[5]);
/*    */           break;
/*    */ 
/*    */         
/*    */         case 1:
/* 83 */           sb.append(" L " + coords[0] + " " + coords[1]);
/*    */           break;
/*    */ 
/*    */         
/*    */         case 0:
/* 88 */           sb.append(" M " + coords[0] + " " + coords[1]);
/*    */           break;
/*    */ 
/*    */         
/*    */         case 2:
/* 93 */           sb.append(" Q " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3]);
/*    */           break;
/*    */       } 
/*    */ 
/*    */     
/*    */     } 
/* 99 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\PathUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */