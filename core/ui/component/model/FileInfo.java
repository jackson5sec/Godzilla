/*    */ package core.ui.component.model;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileInfo
/*    */ {
/* 10 */   private static final String[] ShowSize = new String[] { "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
/*    */ 
/*    */   
/*    */   private long size;
/*    */ 
/*    */   
/*    */   public FileInfo(String size) {
/* 17 */     this.size = functions.stringToLong(size, 0L).longValue();
/*    */   }
/*    */   
/*    */   public long getSize() {
/* 21 */     return this.size;
/*    */   }
/*    */   
/*    */   public void setSize(int size) {
/* 25 */     this.size = size;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     int em = -1;
/* 31 */     float tmp = (float)this.size;
/* 32 */     float lastTmp = 0.0F;
/* 33 */     if (this.size >= 1024L) {
/* 34 */       while ((tmp /= 1024.0F) >= 1.0F) {
/* 35 */         em++;
/* 36 */         lastTmp = tmp;
/*    */       } 
/* 38 */       return (new DecimalFormat(".00")).format(lastTmp) + ShowSize[em];
/*    */     } 
/* 40 */     return Long.toString(this.size);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\model\FileInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */