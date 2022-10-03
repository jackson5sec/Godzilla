/*     */ package util;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Window;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.ToolTipManager;
/*     */ import org.fife.rsta.ac.LanguageSupportFactory;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ 
/*     */ public class UiFunction {
/*     */   public static String setSyntaxEditingStyle(RSyntaxTextArea textArea, String fileName) {
/*  17 */     String style = null;
/*  18 */     fileName = fileName.toLowerCase();
/*  19 */     if (fileName.endsWith(".as")) {
/*  20 */       style = "text/actionscript";
/*  21 */     } else if (fileName.endsWith(".asm")) {
/*  22 */       style = "text/asm";
/*  23 */     } else if (fileName.endsWith(".c")) {
/*  24 */       style = "text/c";
/*  25 */     } else if (fileName.endsWith(".clj")) {
/*  26 */       style = "text/clojure";
/*  27 */     } else if (fileName.endsWith(".cpp") || fileName.endsWith("cc")) {
/*  28 */       style = "text/cpp";
/*  29 */     } else if (fileName.endsWith(".cs") || fileName.endsWith(".aspx") || fileName.endsWith(".ashx") || fileName.endsWith(".asmx")) {
/*  30 */       style = "text/cs";
/*  31 */     } else if (fileName.endsWith(".css")) {
/*  32 */       style = "text/css";
/*  33 */     } else if (fileName.endsWith(".d")) {
/*  34 */       style = "text/d";
/*  35 */     } else if (fileName.equals("dockfile")) {
/*  36 */       style = "text/dockerfile";
/*  37 */     } else if (fileName.endsWith(".dart")) {
/*  38 */       style = "text/dart";
/*  39 */     } else if ((fileName.endsWith(".dpr") | fileName.endsWith(".dfm") | fileName.endsWith(".pas")) != 0) {
/*  40 */       style = "text/delphi";
/*  41 */     } else if (fileName.endsWith(".dtd")) {
/*  42 */       style = "text/dtd";
/*  43 */     } else if ((fileName.endsWith(".f") | fileName.endsWith(".f90")) != 0) {
/*  44 */       style = "text/fortran";
/*  45 */     } else if (fileName.endsWith(".groovy")) {
/*  46 */       style = "text/groovy";
/*  47 */     } else if (fileName.equals("hosts")) {
/*  48 */       style = "text/hosts";
/*  49 */     } else if (fileName.equals(".htaccess")) {
/*  50 */       style = "text/htaccess";
/*  51 */     } else if ((fileName.endsWith(".htm") | fileName.endsWith(".html")) != 0) {
/*  52 */       style = "text/html";
/*  53 */     } else if (fileName.endsWith(".ini")) {
/*  54 */       style = "text/ini";
/*  55 */     } else if ((fileName.endsWith(".java") | fileName.endsWith(".class")) != 0) {
/*  56 */       style = "text/java";
/*  57 */     } else if (fileName.endsWith(".js")) {
/*  58 */       style = "text/javascript";
/*  59 */     } else if (fileName.endsWith(".json")) {
/*  60 */       style = "text/json";
/*  61 */     } else if (fileName.equals(".jshintrc")) {
/*  62 */       style = "text/jshintrc";
/*  63 */     } else if (fileName.endsWith(".jsp") || fileName.endsWith(".jspx")) {
/*  64 */       style = "text/jsp";
/*  65 */     } else if (fileName.endsWith(".tex")) {
/*  66 */       style = "text/latex";
/*  67 */     } else if (fileName.endsWith(".less")) {
/*  68 */       style = "text/less";
/*  69 */     } else if (fileName.endsWith(".lsp")) {
/*  70 */       style = "text/lisp";
/*  71 */     } else if (fileName.endsWith(".lua")) {
/*  72 */       style = "text/lua";
/*  73 */     } else if (fileName.equals("makefile")) {
/*  74 */       style = "text/makefile";
/*  75 */     } else if (fileName.endsWith(".mxml")) {
/*  76 */       style = "text/mxml";
/*  77 */     } else if (fileName.endsWith(".nsi")) {
/*  78 */       style = "text/nsis";
/*  79 */     } else if ((fileName.endsWith(".pl") | fileName.endsWith(".perl")) != 0) {
/*  80 */       style = "text/perl";
/*  81 */     } else if (fileName.endsWith(".php") || fileName.endsWith(".phtml") || fileName.endsWith(".php4") || fileName.endsWith(".php3") || fileName.endsWith(".php5")) {
/*  82 */       style = "text/php";
/*  83 */     } else if (fileName.endsWith(".properties")) {
/*  84 */       style = "text/properties";
/*  85 */     } else if ((fileName.endsWith(".py") | fileName.endsWith(".pyc")) != 0) {
/*  86 */       style = "text/python";
/*  87 */     } else if ((fileName.endsWith(".rb") | fileName.endsWith(".rwb")) != 0) {
/*  88 */       style = "text/ruby";
/*  89 */     } else if (fileName.endsWith(".sas")) {
/*  90 */       style = "text/sas";
/*  91 */     } else if (fileName.endsWith(".scala")) {
/*  92 */       style = "text/scala";
/*  93 */     } else if (fileName.endsWith(".sql")) {
/*  94 */       style = "text/sql";
/*  95 */     } else if (fileName.endsWith(".tcl")) {
/*  96 */       style = "text/tcl";
/*  97 */     } else if ((fileName.endsWith(".ts") | fileName.endsWith(".tsx")) != 0) {
/*  98 */       style = "text/typescript";
/*  99 */     } else if (fileName.endsWith(".sh")) {
/* 100 */       style = "text/unix";
/* 101 */     } else if (fileName.endsWith(".vb")) {
/* 102 */       style = "text/vb";
/* 103 */     } else if (fileName.endsWith(".bat")) {
/* 104 */       style = "text/bat";
/* 105 */     } else if (fileName.endsWith(".xml")) {
/* 106 */       style = "text/xml";
/* 107 */     } else if (fileName.endsWith(".yaml")) {
/* 108 */       style = "text/yaml";
/* 109 */     } else if (fileName.endsWith(".go")) {
/* 110 */       style = "text/golang";
/* 111 */     } else if (fileName.endsWith(".asp")) {
/* 112 */       style = "text/javascript";
/*     */     } 
/*     */     
/* 115 */     if (style == null) {
/* 116 */       style = "text/plain";
/*     */     } else {
/* 118 */       LanguageSupportFactory.get().register(textArea);
/* 119 */       textArea.setCaretPosition(0);
/* 120 */       textArea.requestFocusInWindow();
/* 121 */       textArea.setMarkOccurrences(true);
/* 122 */       textArea.setCodeFoldingEnabled(true);
/* 123 */       textArea.setTabsEmulated(true);
/* 124 */       textArea.setTabSize(3);
/* 125 */       textArea.setUseFocusableTips(false);
/*     */ 
/*     */ 
/*     */       
/* 129 */       ToolTipManager.sharedInstance().registerComponent((JComponent)textArea);
/*     */     } 
/* 131 */     textArea.setSyntaxEditingStyle(style);
/* 132 */     textArea.registerReplaceDialog();
/* 133 */     textArea.registerGoToDialog();
/*     */     
/* 135 */     return style;
/*     */   }
/*     */   public static Frame getParentFrame(Container container) {
/* 138 */     while ((container = container.getParent()) != null) {
/* 139 */       if (Frame.class.isAssignableFrom(container.getClass())) {
/* 140 */         return (Frame)container;
/*     */       }
/*     */     } 
/* 143 */     return null;
/*     */   }
/*     */   public static Dialog getParentDialog(Container container) {
/* 146 */     while ((container = container.getParent()) != null) {
/* 147 */       if (Dialog.class.isAssignableFrom(container.getClass())) {
/* 148 */         return (Dialog)container;
/*     */       }
/*     */     } 
/* 151 */     return null;
/*     */   }
/*     */   public static Window getParentWindow(Container container) {
/* 154 */     while ((container = container.getParent()) != null) {
/* 155 */       if (Window.class.isAssignableFrom(container.getClass())) {
/* 156 */         return (Window)container;
/*     */       }
/*     */     } 
/* 159 */     return null;
/*     */   }
/*     */   public static String getFontType(Font font) {
/* 162 */     if (font.isBold())
/* 163 */       return "Bold".toUpperCase(); 
/* 164 */     if (font.isItalic())
/* 165 */       return "Italic".toUpperCase(); 
/* 166 */     if (font.isPlain()) {
/* 167 */       return "Plain".toUpperCase();
/*     */     }
/* 169 */     return "Plain";
/*     */   }
/*     */   
/*     */   public static int getFontType(String fontType) {
/* 173 */     switch (fontType.toUpperCase()) {
/*     */       case "BOLD":
/* 175 */         return 1;
/*     */       case "ITALIC":
/* 177 */         return 2;
/*     */       case "PLAIN":
/* 179 */         return 0;
/*     */     } 
/* 181 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] getAllFontName() {
/* 186 */     ArrayList<String> arrayList = new ArrayList<>();
/* 187 */     GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 188 */     Font[] fonts = e.getAllFonts();
/* 189 */     for (Font font : fonts) {
/* 190 */       arrayList.add(font.getFontName());
/*     */     }
/* 192 */     return arrayList.<String>toArray(new String[0]);
/*     */   }
/*     */   
/*     */   public static String[] getAllFontType() {
/* 196 */     ArrayList<String> arrayList = new ArrayList<>();
/* 197 */     arrayList.add("BOLD");
/* 198 */     arrayList.add("ITALIC");
/* 199 */     arrayList.add("PLAIN");
/* 200 */     return arrayList.<String>toArray(new String[0]);
/*     */   }
/*     */   
/*     */   public static String[] getAllFontSize() {
/* 204 */     ArrayList<String> arrayList = new ArrayList<>();
/* 205 */     for (int i = 8; i < 48; i++) {
/* 206 */       arrayList.add(Integer.toString(i));
/*     */     }
/* 208 */     return arrayList.<String>toArray(new String[0]);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\UiFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */