/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import com.kichik.pecoff4j.PE;
/*    */ import com.kichik.pecoff4j.ResourceDirectory;
/*    */ import com.kichik.pecoff4j.ResourceEntry;
/*    */ import com.kichik.pecoff4j.io.DataReader;
/*    */ import com.kichik.pecoff4j.io.DataWriter;
/*    */ import com.kichik.pecoff4j.io.IDataReader;
/*    */ import com.kichik.pecoff4j.io.IDataWriter;
/*    */ import com.kichik.pecoff4j.io.PEParser;
/*    */ import com.kichik.pecoff4j.io.ResourceParser;
/*    */ import com.kichik.pecoff4j.resources.GroupIconDirectory;
/*    */ import com.kichik.pecoff4j.resources.GroupIconDirectoryEntry;
/*    */ import com.kichik.pecoff4j.resources.IconDirectory;
/*    */ import com.kichik.pecoff4j.resources.IconDirectoryEntry;
/*    */ import com.kichik.pecoff4j.resources.IconImage;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
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
/*    */ public class IconExtractor
/*    */ {
/*    */   public static void extract(File pecoff, File outputDir) throws IOException {
/* 32 */     PE pe = PEParser.parse(pecoff);
/* 33 */     ResourceDirectory rd = pe.getImageData().getResourceTable();
/* 34 */     if (rd == null)
/*    */       return; 
/* 36 */     ResourceEntry[] entries = ResourceHelper.findResources(rd, 14);
/*    */     
/* 38 */     for (int i = 0; i < entries.length; i++) {
/* 39 */       GroupIconDirectory gid = GroupIconDirectory.read(entries[i]
/* 40 */           .getData());
/* 41 */       IconFile icf = new IconFile();
/* 42 */       IconDirectory icd = new IconDirectory();
/* 43 */       icd.setType(1);
/* 44 */       icd.setReserved(0);
/* 45 */       icf.setDirectory(icd);
/* 46 */       IconImage[] images = new IconImage[gid.getCount()];
/* 47 */       icf.setImages(images);
/*    */       
/* 49 */       for (int j = 0; j < gid.getCount(); j++) {
/* 50 */         GroupIconDirectoryEntry gide = gid.getEntry(j);
/* 51 */         IconDirectoryEntry ide = new IconDirectoryEntry();
/* 52 */         ide.copyFrom(gide);
/* 53 */         icd.add(ide);
/* 54 */         ResourceEntry[] icos = ResourceHelper.findResources(rd, 3, gide
/* 55 */             .getId());
/* 56 */         if (icos == null || icos.length != 1) {
/* 57 */           throw new IOException("Unexpected icons in resource file");
/*    */         }
/* 59 */         byte[] d = icos[0].getData();
/* 60 */         ide.setBytesInRes(d.length);
/*    */         
/* 62 */         if (gide.getWidth() == 0 && gide.getHeight() == 0) {
/* 63 */           IconImage ii = ResourceParser.readPNG(d);
/* 64 */           images[j] = ii;
/*    */         } else {
/* 66 */           IconImage ii = ResourceParser.readIconImage((IDataReader)new DataReader(d), gide
/* 67 */               .getBytesInRes());
/* 68 */           images[j] = ii;
/*    */         } 
/*    */       } 
/*    */       
/* 72 */       File outFile = new File(outputDir, pecoff.getName() + "-icon" + i + ".ico");
/*    */       
/* 74 */       DataWriter dw = new DataWriter(new FileOutputStream(outFile));
/* 75 */       icf.write((IDataWriter)dw);
/* 76 */       dw.close();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\IconExtractor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */