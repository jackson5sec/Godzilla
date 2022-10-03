/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.FileVisitOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.EnumSet;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileSystemUtils
/*     */ {
/*     */   public static boolean deleteRecursively(@Nullable File root) {
/*  58 */     if (root == null) {
/*  59 */       return false;
/*     */     }
/*     */     
/*     */     try {
/*  63 */       return deleteRecursively(root.toPath());
/*     */     }
/*  65 */     catch (IOException ex) {
/*  66 */       return false;
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
/*     */   public static boolean deleteRecursively(@Nullable Path root) throws IOException {
/*  80 */     if (root == null) {
/*  81 */       return false;
/*     */     }
/*  83 */     if (!Files.exists(root, new java.nio.file.LinkOption[0])) {
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     Files.walkFileTree(root, new SimpleFileVisitor<Path>()
/*     */         {
/*     */           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/*  90 */             Files.delete(file);
/*  91 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */           
/*     */           public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/*  95 */             Files.delete(dir);
/*  96 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         });
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyRecursively(File src, File dest) throws IOException {
/* 110 */     Assert.notNull(src, "Source File must not be null");
/* 111 */     Assert.notNull(dest, "Destination File must not be null");
/* 112 */     copyRecursively(src.toPath(), dest.toPath());
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
/*     */   public static void copyRecursively(final Path src, final Path dest) throws IOException {
/* 124 */     Assert.notNull(src, "Source Path must not be null");
/* 125 */     Assert.notNull(dest, "Destination Path must not be null");
/* 126 */     BasicFileAttributes srcAttr = Files.readAttributes(src, BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
/*     */     
/* 128 */     if (srcAttr.isDirectory()) {
/* 129 */       Files.walkFileTree(src, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 2147483647, new SimpleFileVisitor<Path>()
/*     */           {
/*     */             public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/* 132 */               Files.createDirectories(dest.resolve(src.relativize(dir)), (FileAttribute<?>[])new FileAttribute[0]);
/* 133 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */             
/*     */             public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 137 */               Files.copy(file, dest.resolve(src.relativize(file)), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 138 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */           });
/*     */     }
/* 142 */     else if (srcAttr.isRegularFile()) {
/* 143 */       Files.copy(src, dest, new CopyOption[0]);
/*     */     } else {
/*     */       
/* 146 */       throw new IllegalArgumentException("Source File must denote a directory or file");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\FileSystemUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */