/*     */ package com.kitfox.svg.app.ant;
/*     */ 
/*     */ import com.kitfox.svg.SVGCache;
/*     */ import com.kitfox.svg.app.beans.SVGIcon;
/*     */ import com.kitfox.svg.xml.ColorTable;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
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
/*     */ public class SVGToImageAntTask
/*     */   extends Task
/*     */ {
/* 103 */   private ArrayList<FileSet> filesets = new ArrayList<FileSet>();
/*     */   boolean verbose = false;
/*     */   File destDir;
/* 106 */   private String format = "png";
/* 107 */   Color backgroundColor = null;
/* 108 */   int width = -1;
/* 109 */   int height = -1;
/*     */   boolean antiAlias = true;
/* 111 */   String interpolation = "bicubic";
/*     */ 
/*     */ 
/*     */   
/*     */   boolean clipToViewBox = false;
/*     */ 
/*     */   
/*     */   boolean sizeToFit = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 123 */     return this.format;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFormat(String format) {
/* 128 */     this.format = format;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBackgroundColor(String bgColor) {
/* 133 */     this.backgroundColor = ColorTable.parseColor(bgColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeight(int height) {
/* 138 */     this.height = height;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWidth(int width) {
/* 143 */     this.width = width;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 148 */     this.antiAlias = antiAlias;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInterpolation(String interpolation) {
/* 153 */     this.interpolation = interpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSizeToFit(boolean sizeToFit) {
/* 158 */     this.sizeToFit = sizeToFit;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClipToViewBox(boolean clipToViewBox) {
/* 163 */     this.clipToViewBox = clipToViewBox;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 168 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDestDir(File destDir) {
/* 173 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 182 */     this.filesets.add(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 190 */     if (this.verbose) log("Building SVG images");
/*     */     
/* 192 */     for (FileSet fs : this.filesets) {
/* 193 */       DirectoryScanner directoryScanner = fs.getDirectoryScanner(getProject());
/* 194 */       String[] files = directoryScanner.getIncludedFiles();
/*     */ 
/*     */       
/*     */       try {
/* 198 */         File basedir = directoryScanner.getBasedir();
/*     */         
/* 200 */         if (this.verbose) log("Scaning " + basedir);
/*     */         
/* 202 */         for (int i = 0; i < files.length; i++)
/*     */         {
/*     */ 
/*     */           
/* 206 */           translate(basedir, files[i]);
/*     */         }
/*     */       }
/* 209 */       catch (Exception e) {
/*     */         
/* 211 */         throw new BuildException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void translate(File baseDir, String shortName) throws BuildException {
/* 218 */     File source = new File(baseDir, shortName);
/*     */     
/* 220 */     if (this.verbose) log("Reading file: " + source);
/*     */     
/* 222 */     Matcher matchName = Pattern.compile("(.*)\\.svg", 2).matcher(shortName);
/* 223 */     if (matchName.matches())
/*     */     {
/* 225 */       shortName = matchName.group(1);
/*     */     }
/* 227 */     shortName = shortName + "." + this.format;
/*     */     
/* 229 */     SVGIcon icon = new SVGIcon();
/* 230 */     icon.setSvgURI(source.toURI());
/* 231 */     icon.setAntiAlias(this.antiAlias);
/* 232 */     if (this.interpolation.equals("nearest neighbor")) {
/*     */       
/* 234 */       icon.setInterpolation(0);
/*     */     }
/* 236 */     else if (this.interpolation.equals("bilinear")) {
/*     */       
/* 238 */       icon.setInterpolation(1);
/*     */     }
/* 240 */     else if (this.interpolation.equals("bicubic")) {
/*     */       
/* 242 */       icon.setInterpolation(2);
/*     */     } 
/*     */     
/* 245 */     int iconWidth = (this.width > 0) ? this.width : icon.getIconWidth();
/* 246 */     int iconHeight = (this.height > 0) ? this.height : icon.getIconHeight();
/* 247 */     icon.setClipToViewbox(this.clipToViewBox);
/* 248 */     icon.setPreferredSize(new Dimension(iconWidth, iconHeight));
/* 249 */     icon.setScaleToFit(this.sizeToFit);
/* 250 */     BufferedImage image = new BufferedImage(iconWidth, iconHeight, 2);
/* 251 */     Graphics2D g = image.createGraphics();
/*     */     
/* 253 */     if (this.backgroundColor != null) {
/*     */       
/* 255 */       g.setColor(this.backgroundColor);
/* 256 */       g.fillRect(0, 0, iconWidth, iconHeight);
/*     */     } 
/*     */     
/* 259 */     g.setClip(0, 0, iconWidth, iconHeight);
/*     */     
/* 261 */     icon.paintIcon(null, g, 0, 0);
/* 262 */     g.dispose();
/*     */     
/* 264 */     File outFile = (this.destDir == null) ? new File(baseDir, shortName) : new File(this.destDir, shortName);
/* 265 */     if (this.verbose) log("Writing file: " + outFile);
/*     */ 
/*     */     
/*     */     try {
/* 269 */       ImageIO.write(image, this.format, outFile);
/*     */     }
/* 271 */     catch (IOException e) {
/*     */       
/* 273 */       log("Error writing image: " + e.getMessage());
/* 274 */       throw new BuildException(e);
/*     */     } 
/*     */ 
/*     */     
/* 278 */     SVGCache.getSVGUniverse().clear();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\ant\SVGToImageAntTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */