/*    */ package com.kichik.pecoff4j.io;
/*    */ 
/*    */ import com.kichik.pecoff4j.resources.BitmapInfoHeader;
/*    */ import com.kichik.pecoff4j.resources.FixedFileInfo;
/*    */ import com.kichik.pecoff4j.resources.IconDirectory;
/*    */ import com.kichik.pecoff4j.resources.IconDirectoryEntry;
/*    */ import com.kichik.pecoff4j.resources.IconImage;
/*    */ import com.kichik.pecoff4j.resources.RGBQuad;
/*    */ import java.io.IOException;
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
/*    */ public class ResourceAssembler
/*    */ {
/*    */   public static void write(FixedFileInfo info, IDataWriter dw) throws IOException {
/* 24 */     dw.writeDoubleWord(info.getSignature());
/* 25 */     dw.writeDoubleWord(info.getStrucVersion());
/* 26 */     dw.writeDoubleWord(info.getFileVersionMS());
/* 27 */     dw.writeDoubleWord(info.getFileVersionLS());
/* 28 */     dw.writeDoubleWord(info.getProductVersionMS());
/* 29 */     dw.writeDoubleWord(info.getProductVersionLS());
/* 30 */     dw.writeDoubleWord(info.getFileFlagMask());
/* 31 */     dw.writeDoubleWord(info.getFileFlags());
/* 32 */     dw.writeDoubleWord(info.getFileOS());
/* 33 */     dw.writeDoubleWord(info.getFileType());
/* 34 */     dw.writeDoubleWord(info.getFileSubtype());
/* 35 */     dw.writeDoubleWord(info.getFileDateMS());
/* 36 */     dw.writeDoubleWord(info.getFileDateLS());
/*    */   }
/*    */   
/*    */   public static void write(RGBQuad rgb, IDataWriter dw) throws IOException {
/* 40 */     dw.writeByte(rgb.getBlue());
/* 41 */     dw.writeByte(rgb.getGreen());
/* 42 */     dw.writeByte(rgb.getRed());
/* 43 */     dw.writeByte(rgb.getReserved());
/*    */   }
/*    */   
/*    */   public static void write(IconImage ii, IDataWriter dw) throws IOException {
/* 47 */     if (ii.getHeader() != null) {
/* 48 */       write(ii.getHeader(), dw);
/* 49 */       RGBQuad[] colors = ii.getColors();
/* 50 */       if (colors != null) {
/* 51 */         for (int i = 0; i < colors.length; i++) {
/* 52 */           write(colors[i], dw);
/*    */         }
/*    */       }
/* 55 */       dw.writeBytes(ii.getXorMask());
/* 56 */       dw.writeBytes(ii.getAndMask());
/*    */     } else {
/* 58 */       dw.writeBytes(ii.getPNG());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void write(BitmapInfoHeader bih, IDataWriter dw) throws IOException {
/* 64 */     dw.writeDoubleWord(bih.getSize());
/* 65 */     dw.writeDoubleWord(bih.getWidth());
/* 66 */     dw.writeDoubleWord(bih.getHeight());
/* 67 */     dw.writeWord(bih.getPlanes());
/* 68 */     dw.writeWord(bih.getBitCount());
/* 69 */     dw.writeDoubleWord(bih.getCompression());
/* 70 */     dw.writeDoubleWord(bih.getSizeImage());
/* 71 */     dw.writeDoubleWord(bih.getXpelsPerMeter());
/* 72 */     dw.writeDoubleWord(bih.getYpelsPerMeter());
/* 73 */     dw.writeDoubleWord(bih.getClrUsed());
/* 74 */     dw.writeDoubleWord(bih.getClrImportant());
/*    */   }
/*    */ 
/*    */   
/*    */   public static void write(IconDirectoryEntry ide, IDataWriter dw) throws IOException {
/* 79 */     dw.writeByte(ide.getWidth());
/* 80 */     dw.writeByte(ide.getHeight());
/* 81 */     dw.writeByte(ide.getColorCount());
/* 82 */     dw.writeByte(ide.getReserved());
/* 83 */     dw.writeWord(ide.getPlanes());
/* 84 */     dw.writeWord(ide.getBitCount());
/* 85 */     dw.writeDoubleWord(ide.getBytesInRes());
/* 86 */     dw.writeDoubleWord(ide.getOffset());
/*    */   }
/*    */ 
/*    */   
/*    */   public static void write(IconDirectory id, IDataWriter dw) throws IOException {
/* 91 */     dw.writeWord(id.getReserved());
/* 92 */     dw.writeWord(id.getType());
/* 93 */     dw.writeWord(id.getCount());
/* 94 */     for (int i = 0; i < id.getCount(); i++)
/* 95 */       write(id.getEntry(i), dw); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\ResourceAssembler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */