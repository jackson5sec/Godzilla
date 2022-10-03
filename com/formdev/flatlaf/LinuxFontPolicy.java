/*     */ package com.formdev.flatlaf;
/*     */ 
/*     */ import com.formdev.flatlaf.util.StringUtils;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Font;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
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
/*     */ class LinuxFontPolicy
/*     */ {
/*     */   static Font getFont() {
/*  42 */     return SystemInfo.isKDE ? getKDEFont() : getGnomeFont();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Font getGnomeFont() {
/*  51 */     Object fontName = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Gtk/FontName");
/*  52 */     if (!(fontName instanceof String)) {
/*  53 */       fontName = "sans 10";
/*     */     }
/*  55 */     String family = "";
/*  56 */     int style = 0;
/*  57 */     int size = 10;
/*     */     
/*  59 */     StringTokenizer st = new StringTokenizer((String)fontName);
/*  60 */     while (st.hasMoreTokens()) {
/*  61 */       String word = st.nextToken();
/*     */       
/*  63 */       if (word.equalsIgnoreCase("italic")) {
/*  64 */         style |= 0x2; continue;
/*  65 */       }  if (word.equalsIgnoreCase("bold")) {
/*  66 */         style |= 0x1; continue;
/*  67 */       }  if (Character.isDigit(word.charAt(0))) {
/*     */         try {
/*  69 */           size = Integer.parseInt(word);
/*  70 */         } catch (NumberFormatException numberFormatException) {}
/*     */         
/*     */         continue;
/*     */       } 
/*  74 */       family = family.isEmpty() ? word : (family + ' ' + word);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (family.startsWith("Ubuntu") && !SystemInfo.isJetBrainsJVM && 
/*     */       
/*  81 */       !FlatSystemProperties.getBoolean("flatlaf.useUbuntuFont", false)) {
/*  82 */       family = "Liberation Sans";
/*     */     }
/*     */     
/*  85 */     double dsize = size * getGnomeFontScale();
/*  86 */     size = (int)(dsize + 0.5D);
/*  87 */     if (size < 1) {
/*  88 */       size = 1;
/*     */     }
/*     */     
/*  91 */     String logicalFamily = mapFcName(family.toLowerCase());
/*  92 */     if (logicalFamily != null) {
/*  93 */       family = logicalFamily;
/*     */     }
/*  95 */     return createFont(family, style, size, dsize);
/*     */   }
/*     */   
/*     */   private static Font createFont(String family, int style, int size, double dsize) {
/*  99 */     Font font = FlatLaf.createCompositeFont(family, style, size);
/*     */ 
/*     */     
/* 102 */     font = font.deriveFont(style, (float)dsize);
/*     */     
/* 104 */     return font;
/*     */   }
/*     */ 
/*     */   
/*     */   private static double getGnomeFontScale() {
/* 109 */     if (isSystemScaling()) {
/* 110 */       return 1.3333333333333333D;
/*     */     }
/*     */ 
/*     */     
/* 114 */     Object value = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Xft/DPI");
/* 115 */     if (value instanceof Integer) {
/* 116 */       int dpi = ((Integer)value).intValue() / 1024;
/* 117 */       if (dpi == -1)
/* 118 */         dpi = 96; 
/* 119 */       if (dpi < 50)
/* 120 */         dpi = 50; 
/* 121 */       return dpi / 72.0D;
/*     */     } 
/* 123 */     return GraphicsEnvironment.getLocalGraphicsEnvironment()
/* 124 */       .getDefaultScreenDevice().getDefaultConfiguration()
/* 125 */       .getNormalizingTransform().getScaleY();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String mapFcName(String name) {
/* 133 */     switch (name) { case "sans":
/* 134 */         return "sansserif";
/* 135 */       case "sans-serif": return "sansserif";
/* 136 */       case "serif": return "serif";
/* 137 */       case "monospace": return "monospaced"; }
/*     */     
/* 139 */     return null;
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
/*     */   private static Font getKDEFont() {
/* 155 */     List<String> kdeglobals = readConfig("kdeglobals");
/* 156 */     List<String> kcmfonts = readConfig("kcmfonts");
/*     */     
/* 158 */     String generalFont = getConfigEntry(kdeglobals, "General", "font");
/* 159 */     String forceFontDPI = getConfigEntry(kcmfonts, "General", "forceFontDPI");
/*     */     
/* 161 */     String family = "sansserif";
/* 162 */     int style = 0;
/* 163 */     int size = 10;
/*     */     
/* 165 */     if (generalFont != null) {
/* 166 */       List<String> strs = StringUtils.split(generalFont, ',');
/*     */       try {
/* 168 */         family = strs.get(0);
/* 169 */         size = Integer.parseInt(strs.get(1));
/* 170 */         if ("75".equals(strs.get(4)))
/* 171 */           style |= 0x1; 
/* 172 */         if ("1".equals(strs.get(5)))
/* 173 */           style |= 0x2; 
/* 174 */       } catch (RuntimeException ex) {
/* 175 */         FlatLaf.LOG.log(Level.CONFIG, "FlatLaf: Failed to parse 'font=" + generalFont + "'.", ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 180 */     int dpi = 96;
/* 181 */     if (forceFontDPI != null && !isSystemScaling()) {
/*     */       try {
/* 183 */         dpi = Integer.parseInt(forceFontDPI);
/* 184 */         if (dpi <= 0)
/* 185 */           dpi = 96; 
/* 186 */         if (dpi < 50)
/* 187 */           dpi = 50; 
/* 188 */       } catch (NumberFormatException ex) {
/* 189 */         FlatLaf.LOG.log(Level.CONFIG, "FlatLaf: Failed to parse 'forceFontDPI=" + forceFontDPI + "'.", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 194 */     double fontScale = dpi / 72.0D;
/* 195 */     double dsize = size * fontScale;
/* 196 */     size = (int)(dsize + 0.5D);
/* 197 */     if (size < 1) {
/* 198 */       size = 1;
/*     */     }
/* 200 */     return createFont(family, style, size, dsize);
/*     */   }
/*     */   
/*     */   private static List<String> readConfig(String filename) {
/* 204 */     File userHome = new File(System.getProperty("user.home"));
/*     */ 
/*     */     
/* 207 */     String[] configDirs = { ".config", ".kde4/share/config", ".kde/share/config" };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     File file = null;
/* 213 */     for (String configDir : configDirs) {
/* 214 */       file = new File(userHome, configDir + "/" + filename);
/* 215 */       if (file.isFile())
/*     */         break; 
/*     */     } 
/* 218 */     if (!file.isFile()) {
/* 219 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 222 */     ArrayList<String> lines = new ArrayList<>(200);
/* 223 */     try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
/* 224 */       String line = null;
/* 225 */       while ((line = reader.readLine()) != null)
/* 226 */         lines.add(line); 
/* 227 */     } catch (IOException ex) {
/* 228 */       FlatLaf.LOG.log(Level.CONFIG, "FlatLaf: Failed to read '" + filename + "'.", ex);
/*     */     } 
/* 230 */     return lines;
/*     */   }
/*     */   
/*     */   private static String getConfigEntry(List<String> config, String group, String key) {
/* 234 */     int groupLength = group.length();
/* 235 */     int keyLength = key.length();
/* 236 */     boolean inGroup = false;
/* 237 */     for (String line : config) {
/* 238 */       if (!inGroup) {
/* 239 */         if (line.length() >= groupLength + 2 && line
/* 240 */           .charAt(0) == '[' && line
/* 241 */           .charAt(groupLength + 1) == ']' && line
/* 242 */           .indexOf(group) == 1)
/*     */         {
/* 244 */           inGroup = true; } 
/*     */         continue;
/*     */       } 
/* 247 */       if (line.startsWith("[")) {
/* 248 */         return null;
/*     */       }
/* 250 */       if (line.length() >= keyLength + 2 && line
/* 251 */         .charAt(keyLength) == '=' && line
/* 252 */         .startsWith(key))
/*     */       {
/* 254 */         return line.substring(keyLength + 1);
/*     */       }
/*     */     } 
/*     */     
/* 258 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSystemScaling() {
/* 268 */     GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/* 269 */     return (UIScale.getSystemScaleFactor(gc) > 1.0D);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\LinuxFontPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */