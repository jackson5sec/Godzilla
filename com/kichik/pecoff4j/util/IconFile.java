/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import com.kichik.pecoff4j.io.DataReader;
/*    */ import com.kichik.pecoff4j.io.IDataReader;
/*    */ import com.kichik.pecoff4j.io.IDataWriter;
/*    */ import com.kichik.pecoff4j.io.ResourceAssembler;
/*    */ import com.kichik.pecoff4j.io.ResourceParser;
/*    */ import com.kichik.pecoff4j.resources.IconDirectory;
/*    */ import com.kichik.pecoff4j.resources.IconImage;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
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
/*    */ public class IconFile
/*    */ {
/*    */   private IconDirectory directory;
/*    */   private IconImage[] images;
/*    */   
/*    */   public static IconFile parse(String filename) throws IOException {
/* 29 */     return read((IDataReader)new DataReader(new FileInputStream(filename)));
/*    */   }
/*    */   
/*    */   public static IconFile parse(File file) throws IOException {
/* 33 */     return read((IDataReader)new DataReader(new FileInputStream(file)));
/*    */   }
/*    */   
/*    */   public static IconFile read(IDataReader dr) throws IOException {
/* 37 */     IconFile ic = new IconFile();
/* 38 */     ic.directory = ResourceParser.readIconDirectory(dr);
/* 39 */     ic.images = new IconImage[ic.directory.getCount()];
/* 40 */     for (int i = 0; i < ic.directory.getCount(); i++) {
/* 41 */       dr.jumpTo(ic.directory.getEntry(i).getOffset());
/* 42 */       ic.images[i] = ResourceParser.readIconImage(dr, ic.directory
/* 43 */           .getEntry(i).getBytesInRes());
/*    */     } 
/* 45 */     return ic;
/*    */   }
/*    */   
/*    */   public void write(IDataWriter dw) throws IOException {
/* 49 */     int offset = this.directory.sizeOf(); int i;
/* 50 */     for (i = 0; i < this.images.length; i++) {
/* 51 */       this.directory.getEntry(i).setOffset(offset);
/* 52 */       offset += this.images[i].sizeOf();
/*    */     } 
/* 54 */     ResourceAssembler.write(this.directory, dw);
/* 55 */     for (i = 0; i < this.images.length; i++) {
/* 56 */       ResourceAssembler.write(this.images[i], dw);
/*    */     }
/*    */   }
/*    */   
/*    */   public IconDirectory getDirectory() {
/* 61 */     return this.directory;
/*    */   }
/*    */   
/*    */   public void setDirectory(IconDirectory directory) {
/* 65 */     this.directory = directory;
/*    */   }
/*    */   
/*    */   public IconImage[] getImages() {
/* 69 */     return this.images;
/*    */   }
/*    */   
/*    */   public void setImages(IconImage[] images) {
/* 73 */     this.images = images;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\IconFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */