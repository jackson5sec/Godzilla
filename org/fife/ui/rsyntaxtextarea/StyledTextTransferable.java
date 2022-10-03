/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
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
/*     */ class StyledTextTransferable
/*     */   implements Transferable
/*     */ {
/*     */   private String html;
/*     */   private byte[] rtfBytes;
/*  45 */   private static final DataFlavor[] FLAVORS = new DataFlavor[] { DataFlavor.fragmentHtmlFlavor, new DataFlavor("text/rtf", "RTF"), DataFlavor.stringFlavor, DataFlavor.plainTextFlavor };
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
/*     */   StyledTextTransferable(String html, byte[] rtfBytes) {
/*  60 */     this.html = html;
/*  61 */     this.rtfBytes = rtfBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
/*  69 */     if (flavor.equals(FLAVORS[0])) {
/*  70 */       return this.html;
/*     */     }
/*     */     
/*  73 */     if (flavor.equals(FLAVORS[1])) {
/*  74 */       return new ByteArrayInputStream((this.rtfBytes == null) ? new byte[0] : this.rtfBytes);
/*     */     }
/*     */     
/*  77 */     if (flavor.equals(FLAVORS[2])) {
/*  78 */       return (this.rtfBytes == null) ? "" : RtfToText.getPlainText(this.rtfBytes);
/*     */     }
/*     */     
/*  81 */     if (flavor.equals(FLAVORS[3])) {
/*  82 */       String text = "";
/*  83 */       if (this.rtfBytes != null) {
/*  84 */         text = RtfToText.getPlainText(this.rtfBytes);
/*     */       }
/*  86 */       return new StringReader(text);
/*     */     } 
/*     */     
/*  89 */     throw new UnsupportedFlavorException(flavor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataFlavor[] getTransferDataFlavors() {
/*  95 */     return (DataFlavor[])FLAVORS.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDataFlavorSupported(DataFlavor flavor) {
/* 101 */     for (DataFlavor flavor1 : FLAVORS) {
/* 102 */       if (flavor.equals(flavor1)) {
/* 103 */         return true;
/*     */       }
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\StyledTextTransferable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */