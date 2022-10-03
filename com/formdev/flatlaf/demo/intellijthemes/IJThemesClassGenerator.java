/*     */ package com.formdev.flatlaf.demo.intellijthemes;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
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
/*     */ public class IJThemesClassGenerator
/*     */ {
/*     */   private static final String CLASS_HEADER = "/*\n * Copyright 2020 FormDev Software GmbH\n *\n * Licensed under the Apache License, Version 2.0 (the \"License\");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n *     https://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an \"AS IS\" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n */\n\npackage com.formdev.flatlaf.intellijthemes${subPackage};\n\n//\n// DO NOT MODIFY\n// Generated with com.formdev.flatlaf.demo.intellijthemes.IJThemesClassGenerator\n//\n\n";
/*     */   private static final String CLASS_TEMPLATE = "import com.formdev.flatlaf.IntelliJTheme;\n\n/**\n * @author Karl Tauber\n */\npublic class ${themeClass}\n\textends IntelliJTheme.ThemeLaf\n{\n\tpublic static final String NAME = \"${themeName}\";\n\n\tpublic static boolean install() {\n\t\ttry {\n\t\t\treturn install( new ${themeClass}() );\n\t\t} catch( RuntimeException ex ) {\n\t\t\treturn false;\n\t\t}\n\t}\n\n\tpublic static void installLafInfo() {\n\t\tinstallLafInfo( NAME, ${themeClass}.class );\n\t}\n\n\tpublic ${themeClass}() {\n\t\tsuper( Utils.loadTheme( \"${themeFile}\" ) );\n\t}\n\n\t@Override\n\tpublic String getName() {\n\t\treturn NAME;\n\t}\n}\n";
/*     */   private static final String ALL_THEMES_TEMPLATE = "import javax.swing.UIManager.LookAndFeelInfo;\n\n/**\n * @author Karl Tauber\n */\npublic class FlatAllIJThemes\n{\n\tpublic static final FlatIJLookAndFeelInfo[] INFOS = {\n${allInfos}\n\t};\n\n\t//---- class FlatIJLookAndFeelInfo ----------------------------------------\n\n\tpublic static class FlatIJLookAndFeelInfo\n\t\textends LookAndFeelInfo\n\t{\n\t\tprivate final boolean dark;\n\n\t\tpublic FlatIJLookAndFeelInfo( String name, String className, boolean dark ) {\n\t\t\tsuper( name, className );\n\t\t\tthis.dark = dark;\n\t\t}\n\n\t\tpublic boolean isDark() {\n\t\t\treturn dark;\n\t\t}\n\t}\n}\n";
/*     */   private static final String THEME_TEMPLATE = "\t\tnew FlatIJLookAndFeelInfo( \"${themeName}\", \"com.formdev.flatlaf.intellijthemes${subPackage}.${themeClass}\", ${dark} ),";
/*     */   
/*     */   public static void main(String[] args) {
/*  34 */     IJThemesManager themesManager = new IJThemesManager();
/*  35 */     themesManager.loadBundledThemes();
/*     */     
/*  37 */     String toPath = "../flatlaf-intellij-themes/src/main/java/com/formdev/flatlaf/intellijthemes/themes/..";
/*     */     
/*  39 */     StringBuilder allInfos = new StringBuilder();
/*  40 */     StringBuilder markdownTable = new StringBuilder();
/*  41 */     markdownTable.append("Name | Class\n");
/*  42 */     markdownTable.append("-----|------\n");
/*     */     
/*  44 */     for (IJThemeInfo ti : themesManager.bundledThemes) {
/*  45 */       if (ti.sourceCodeUrl == null || ti.sourceCodePath == null) {
/*     */         continue;
/*     */       }
/*  48 */       generateClass(ti, toPath, allInfos, markdownTable);
/*     */     } 
/*     */     
/*  51 */     Path out = (new File(toPath, "FlatAllIJThemes.java")).toPath();
/*     */ 
/*     */     
/*  54 */     String allThemes = "/*\n * Copyright 2020 FormDev Software GmbH\n *\n * Licensed under the Apache License, Version 2.0 (the \"License\");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n *     https://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an \"AS IS\" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n */\n\npackage com.formdev.flatlaf.intellijthemes${subPackage};\n\n//\n// DO NOT MODIFY\n// Generated with com.formdev.flatlaf.demo.intellijthemes.IJThemesClassGenerator\n//\n\nimport javax.swing.UIManager.LookAndFeelInfo;\n\n/**\n * @author Karl Tauber\n */\npublic class FlatAllIJThemes\n{\n\tpublic static final FlatIJLookAndFeelInfo[] INFOS = {\n${allInfos}\n\t};\n\n\t//---- class FlatIJLookAndFeelInfo ----------------------------------------\n\n\tpublic static class FlatIJLookAndFeelInfo\n\t\textends LookAndFeelInfo\n\t{\n\t\tprivate final boolean dark;\n\n\t\tpublic FlatIJLookAndFeelInfo( String name, String className, boolean dark ) {\n\t\t\tsuper( name, className );\n\t\t\tthis.dark = dark;\n\t\t}\n\n\t\tpublic boolean isDark() {\n\t\t\treturn dark;\n\t\t}\n\t}\n}\n".replace("${subPackage}", "").replace("${allInfos}", allInfos);
/*  55 */     writeFile(out, allThemes);
/*     */     
/*  57 */     System.out.println(markdownTable);
/*     */   }
/*     */   
/*     */   private static void generateClass(IJThemeInfo ti, String toPath, StringBuilder allInfos, StringBuilder markdownTable) {
/*  61 */     String resourceName = ti.resourceName;
/*  62 */     String resourcePath = null;
/*  63 */     int resSep = resourceName.indexOf('/');
/*  64 */     if (resSep >= 0) {
/*  65 */       resourcePath = resourceName.substring(0, resSep);
/*  66 */       resourceName = resourceName.substring(resSep + 1);
/*     */     } 
/*     */     
/*  69 */     String name = ti.name;
/*  70 */     int nameSep = name.indexOf('/');
/*  71 */     if (nameSep >= 0) {
/*  72 */       name = name.substring(nameSep + 1).trim();
/*     */     }
/*  74 */     String themeName = name;
/*  75 */     if ("material-theme-ui-lite".equals(resourcePath)) {
/*  76 */       themeName = themeName + " (Material)";
/*     */     }
/*  78 */     StringBuilder buf = new StringBuilder();
/*  79 */     for (String n : name.split(" ")) {
/*  80 */       if (n.length() != 0 && !n.equals("-"))
/*     */       {
/*     */         
/*  83 */         if (Character.isUpperCase(n.charAt(0))) {
/*  84 */           buf.append(n);
/*     */         } else {
/*  86 */           buf.append(Character.toUpperCase(n.charAt(0))).append(n.substring(1));
/*     */         }  } 
/*     */     } 
/*  89 */     String subPackage = (resourcePath != null) ? ('.' + resourcePath.replace("-", "")) : "";
/*  90 */     String themeClass = "Flat" + buf + "IJTheme";
/*  91 */     String themeFile = resourceName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     String classBody = "/*\n * Copyright 2020 FormDev Software GmbH\n *\n * Licensed under the Apache License, Version 2.0 (the \"License\");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n *     https://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an \"AS IS\" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n */\n\npackage com.formdev.flatlaf.intellijthemes${subPackage};\n\n//\n// DO NOT MODIFY\n// Generated with com.formdev.flatlaf.demo.intellijthemes.IJThemesClassGenerator\n//\n\nimport com.formdev.flatlaf.IntelliJTheme;\n\n/**\n * @author Karl Tauber\n */\npublic class ${themeClass}\n\textends IntelliJTheme.ThemeLaf\n{\n\tpublic static final String NAME = \"${themeName}\";\n\n\tpublic static boolean install() {\n\t\ttry {\n\t\t\treturn install( new ${themeClass}() );\n\t\t} catch( RuntimeException ex ) {\n\t\t\treturn false;\n\t\t}\n\t}\n\n\tpublic static void installLafInfo() {\n\t\tinstallLafInfo( NAME, ${themeClass}.class );\n\t}\n\n\tpublic ${themeClass}() {\n\t\tsuper( Utils.loadTheme( \"${themeFile}\" ) );\n\t}\n\n\t@Override\n\tpublic String getName() {\n\t\treturn NAME;\n\t}\n}\n".replace("${subPackage}", subPackage).replace("${themeClass}", themeClass).replace("${themeFile}", themeFile).replace("${themeName}", themeName);
/*     */     
/*  99 */     File toDir = new File(toPath);
/* 100 */     if (resourcePath != null) {
/* 101 */       toDir = new File(toDir, resourcePath.replace("-", ""));
/*     */     }
/* 103 */     Path out = (new File(toDir, themeClass + ".java")).toPath();
/* 104 */     writeFile(out, classBody);
/*     */     
/* 106 */     if (allInfos.length() > 0)
/* 107 */       allInfos.append('\n'); 
/* 108 */     allInfos.append("\t\tnew FlatIJLookAndFeelInfo( \"${themeName}\", \"com.formdev.flatlaf.intellijthemes${subPackage}.${themeClass}\", ${dark} ),"
/* 109 */         .replace("${subPackage}", subPackage)
/* 110 */         .replace("${themeClass}", themeClass)
/* 111 */         .replace("${themeName}", themeName)
/* 112 */         .replace("${dark}", Boolean.toString(ti.dark)));
/*     */     
/* 114 */     markdownTable.append(String.format("[%s](%s) | `com.formdev.flatlaf.intellijthemes%s.%s`\n", new Object[] { themeName, ti.sourceCodeUrl, subPackage, themeClass }));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeFile(Path out, String content) {
/*     */     try {
/* 120 */       Files.write(out, content.getBytes(StandardCharsets.ISO_8859_1), new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING });
/*     */     }
/* 122 */     catch (IOException ex) {
/* 123 */       ex.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\intellijthemes\IJThemesClassGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */