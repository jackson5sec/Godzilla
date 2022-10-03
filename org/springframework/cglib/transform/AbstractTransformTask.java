/*     */ package org.springframework.cglib.transform;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.cglib.core.ClassNameReader;
/*     */ import org.springframework.cglib.core.DebuggingClassWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTransformTask
/*     */   extends AbstractProcessTask
/*     */ {
/*     */   private static final int ZIP_MAGIC = 1347093252;
/*     */   private static final int CLASS_MAGIC = -889275714;
/*     */   private boolean verbose;
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/*  39 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ClassTransformer getClassTransformer(String[] paramArrayOfString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Attribute[] attributes() {
/*  54 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processFile(File file) throws Exception {
/*  59 */     if (isClassFile(file)) {
/*     */       
/*  61 */       processClassFile(file);
/*     */     }
/*  63 */     else if (isJarFile(file)) {
/*     */       
/*  65 */       processJarFile(file);
/*     */     }
/*     */     else {
/*     */       
/*  69 */       log("ignoring " + file.toURI(), 1);
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
/*     */   private void processClassFile(File file) throws Exception, FileNotFoundException, IOException, MalformedURLException {
/*  84 */     ClassReader reader = getClassReader(file);
/*  85 */     String[] name = ClassNameReader.getClassInfo(reader);
/*  86 */     DebuggingClassWriter w = new DebuggingClassWriter(2);
/*     */     
/*  88 */     ClassTransformer t = getClassTransformer(name);
/*  89 */     if (t != null) {
/*     */       
/*  91 */       if (this.verbose) {
/*  92 */         log("processing " + file.toURI());
/*     */       }
/*  94 */       (new TransformingClassGenerator(new ClassReaderGenerator(
/*  95 */             getClassReader(file), attributes(), getFlags()), t))
/*  96 */         .generateClass((ClassVisitor)w);
/*  97 */       FileOutputStream fos = new FileOutputStream(file);
/*     */       try {
/*  99 */         fos.write(w.toByteArray());
/*     */       } finally {
/* 101 */         fos.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getFlags() {
/* 109 */     return 0;
/*     */   }
/*     */   
/*     */   private static ClassReader getClassReader(File file) throws Exception {
/* 113 */     InputStream in = new BufferedInputStream(new FileInputStream(file));
/*     */     try {
/* 115 */       ClassReader r = new ClassReader(in);
/* 116 */       return r;
/*     */     } finally {
/* 118 */       in.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isClassFile(File file) throws IOException {
/* 125 */     return checkMagic(file, -889275714L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processJarFile(File file) throws Exception {
/* 131 */     if (this.verbose) {
/* 132 */       log("processing " + file.toURI());
/*     */     }
/*     */     
/* 135 */     File tempFile = File.createTempFile(file.getName(), null, new File(file
/* 136 */           .getAbsoluteFile().getParent()));
/*     */     
/*     */     try {
/* 139 */       ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
/*     */       try {
/* 141 */         FileOutputStream fout = new FileOutputStream(tempFile);
/*     */         try {
/* 143 */           ZipOutputStream out = new ZipOutputStream(fout);
/*     */           
/*     */           ZipEntry entry;
/* 146 */           while ((entry = zip.getNextEntry()) != null) {
/*     */ 
/*     */             
/* 149 */             byte[] bytes = getBytes(zip);
/*     */             
/* 151 */             if (!entry.isDirectory()) {
/*     */               
/* 153 */               DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes));
/*     */ 
/*     */ 
/*     */               
/* 157 */               if (din.readInt() == -889275714) {
/*     */                 
/* 159 */                 bytes = process(bytes);
/*     */               
/*     */               }
/* 162 */               else if (this.verbose) {
/* 163 */                 log("ignoring " + entry.toString());
/*     */               } 
/*     */             } 
/*     */ 
/*     */             
/* 168 */             ZipEntry outEntry = new ZipEntry(entry.getName());
/* 169 */             outEntry.setMethod(entry.getMethod());
/* 170 */             outEntry.setComment(entry.getComment());
/* 171 */             outEntry.setSize(bytes.length);
/*     */ 
/*     */             
/* 174 */             if (outEntry.getMethod() == 0) {
/* 175 */               CRC32 crc = new CRC32();
/* 176 */               crc.update(bytes);
/* 177 */               outEntry.setCrc(crc.getValue());
/* 178 */               outEntry.setCompressedSize(bytes.length);
/*     */             } 
/* 180 */             out.putNextEntry(outEntry);
/* 181 */             out.write(bytes);
/* 182 */             out.closeEntry();
/* 183 */             zip.closeEntry();
/*     */           } 
/*     */           
/* 186 */           out.close();
/*     */         } finally {
/* 188 */           fout.close();
/*     */         } 
/*     */       } finally {
/* 191 */         zip.close();
/*     */       } 
/*     */ 
/*     */       
/* 195 */       if (file.delete()) {
/*     */         
/* 197 */         File newFile = new File(tempFile.getAbsolutePath());
/*     */         
/* 199 */         if (!newFile.renameTo(file)) {
/* 200 */           throw new IOException("can not rename " + tempFile + " to " + file);
/*     */         }
/*     */       } else {
/*     */         
/* 204 */         throw new IOException("can not delete " + file);
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 209 */       tempFile.delete();
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
/*     */   private byte[] process(byte[] bytes) throws Exception {
/* 223 */     ClassReader reader = new ClassReader(new ByteArrayInputStream(bytes));
/* 224 */     String[] name = ClassNameReader.getClassInfo(reader);
/* 225 */     DebuggingClassWriter w = new DebuggingClassWriter(2);
/*     */     
/* 227 */     ClassTransformer t = getClassTransformer(name);
/* 228 */     if (t != null) {
/* 229 */       if (this.verbose) {
/* 230 */         log("processing " + name[0]);
/*     */       }
/* 232 */       (new TransformingClassGenerator(new ClassReaderGenerator(new ClassReader(new ByteArrayInputStream(bytes)), 
/*     */             
/* 234 */             attributes(), getFlags()), t)).generateClass((ClassVisitor)w);
/* 235 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 236 */       out.write(w.toByteArray());
/* 237 */       return out.toByteArray();
/*     */     } 
/* 239 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] getBytes(ZipInputStream zip) throws IOException {
/* 249 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/* 250 */     InputStream in = new BufferedInputStream(zip);
/*     */     int b;
/* 252 */     while ((b = in.read()) != -1) {
/* 253 */       bout.write(b);
/*     */     }
/* 255 */     return bout.toByteArray();
/*     */   }
/*     */   
/*     */   private boolean checkMagic(File file, long magic) throws IOException {
/* 259 */     DataInputStream in = new DataInputStream(new FileInputStream(file));
/*     */     try {
/* 261 */       int m = in.readInt();
/* 262 */       return (magic == m);
/*     */     } finally {
/* 264 */       in.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isJarFile(File file) throws IOException {
/* 269 */     return checkMagic(file, 1347093252L);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\AbstractTransformTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */