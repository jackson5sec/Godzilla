/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.security.AccessControlException;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
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
/*     */ public class IconGroup
/*     */ {
/*     */   private String path;
/*     */   private boolean separateLargeIcons;
/*     */   private String largeIconSubDir;
/*     */   private String extension;
/*     */   private String name;
/*     */   private String jarFile;
/*     */   private static final String DEFAULT_EXTENSION = "gif";
/*     */   
/*     */   public IconGroup(String name, String path) {
/*  71 */     this(name, path, null);
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
/*     */   public IconGroup(String name, String path, String largeIconSubDir) {
/*  84 */     this(name, path, largeIconSubDir, "gif");
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
/*     */ 
/*     */   
/*     */   public IconGroup(String name, String path, String largeIconSubDir, String extension) {
/* 100 */     this(name, path, largeIconSubDir, extension, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IconGroup(String name, String path, String largeIconSubDir, String extension, String jar) {
/* 122 */     this.name = name;
/* 123 */     this.path = path;
/* 124 */     if (path != null && path.length() > 0 && !path.endsWith("/")) {
/* 125 */       this.path += "/";
/*     */     }
/* 127 */     this.separateLargeIcons = (largeIconSubDir != null);
/* 128 */     this.largeIconSubDir = largeIconSubDir;
/* 129 */     this.extension = (extension != null) ? extension : "gif";
/* 130 */     this.jarFile = jar;
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
/*     */   public boolean equals(Object o2) {
/* 143 */     if (o2 instanceof IconGroup) {
/* 144 */       IconGroup ig2 = (IconGroup)o2;
/* 145 */       if (ig2.getName().equals(getName()) && this.separateLargeIcons == ig2
/* 146 */         .hasSeparateLargeIcons()) {
/* 147 */         if (this.separateLargeIcons && 
/* 148 */           !this.largeIconSubDir.equals(ig2.largeIconSubDir)) {
/* 149 */           return false;
/*     */         }
/*     */         
/* 152 */         return this.path.equals(ig2.path);
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     return false;
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
/*     */   public Icon getFileTypeIcon(String rstaSyntax) {
/* 168 */     int slash = rstaSyntax.indexOf('/');
/*     */     
/* 170 */     if (slash > -1) {
/* 171 */       String fileType = rstaSyntax.substring(slash + 1).toLowerCase();
/* 172 */       String path = "fileTypes/" + fileType + '.' + this.extension;
/* 173 */       Icon icon = getIconImpl(path);
/* 174 */       if (icon == null) {
/* 175 */         icon = getIconImpl("fileTypes/default." + this.extension);
/*     */       }
/* 177 */       return icon;
/*     */     } 
/*     */     
/* 180 */     return null;
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
/*     */   
/*     */   public Icon getIcon(String name) {
/* 195 */     Icon icon = getIconImpl(this.path + name + "." + this.extension);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     if (icon != null && (icon.getIconWidth() < 1 || icon.getIconHeight() < 1)) {
/* 202 */       icon = null;
/*     */     }
/* 204 */     return icon;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected Icon getIconImpl(String iconFullPath) {
/*     */     try {
/* 222 */       if (this.jarFile == null) {
/*     */ 
/*     */ 
/*     */         
/* 226 */         URL uRL = getClass().getClassLoader().getResource(iconFullPath);
/* 227 */         if (uRL != null) {
/* 228 */           return new ImageIcon(uRL);
/*     */         }
/*     */         
/* 231 */         BufferedImage image = ImageIO.read(new File(iconFullPath));
/* 232 */         return (image != null) ? new ImageIcon(image) : null;
/*     */       } 
/*     */       
/* 235 */       URL url = new URL("jar:file:///" + this.jarFile + "!/" + iconFullPath);
/*     */ 
/*     */       
/* 238 */       Icon icon = new ImageIcon(url);
/*     */       
/* 240 */       return (icon.getIconWidth() == -1) ? null : icon;
/*     */     }
/* 242 */     catch (AccessControlException|java.io.IOException ace) {
/* 243 */       return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getLargeIcon(String name) {
/* 261 */     return getIconImpl(this.path + this.largeIconSubDir + "/" + name + "." + this.extension);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 272 */     return this.name;
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
/*     */   public boolean hasSeparateLargeIcons() {
/* 284 */     return this.separateLargeIcons;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 290 */     return getName().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\IconGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */