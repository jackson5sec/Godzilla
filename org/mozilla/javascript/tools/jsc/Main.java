/*     */ package org.mozilla.javascript.tools.jsc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.mozilla.javascript.CompilerEnvirons;
/*     */ import org.mozilla.javascript.ErrorReporter;
/*     */ import org.mozilla.javascript.optimizer.ClassCompiler;
/*     */ import org.mozilla.javascript.tools.SourceReader;
/*     */ import org.mozilla.javascript.tools.ToolErrorReporter;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   private boolean printHelp;
/*     */   private ToolErrorReporter reporter;
/*     */   private CompilerEnvirons compilerEnv;
/*     */   private ClassCompiler compiler;
/*     */   private String targetName;
/*     */   private String targetPackage;
/*     */   private String destinationDir;
/*     */   private String characterEncoding;
/*     */   
/*     */   public static void main(String[] args) {
/*  28 */     Main main = new Main();
/*  29 */     args = main.processOptions(args);
/*  30 */     if (args == null) {
/*  31 */       if (main.printHelp) {
/*  32 */         System.out.println(ToolErrorReporter.getMessage("msg.jsc.usage", Main.class.getName()));
/*     */         
/*  34 */         System.exit(0);
/*     */       } 
/*  36 */       System.exit(1);
/*     */     } 
/*  38 */     if (!main.reporter.hasReportedError()) {
/*  39 */       main.processSource(args);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Main() {
/*  45 */     this.reporter = new ToolErrorReporter(true);
/*  46 */     this.compilerEnv = new CompilerEnvirons();
/*  47 */     this.compilerEnv.setErrorReporter((ErrorReporter)this.reporter);
/*  48 */     this.compiler = new ClassCompiler(this.compilerEnv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] processOptions(String[] args) {
/*  57 */     this.targetPackage = "";
/*  58 */     this.compilerEnv.setGenerateDebugInfo(false);
/*  59 */     for (int i = 0; i < args.length; i++) {
/*  60 */       String arg = args[i];
/*  61 */       if (!arg.startsWith("-")) {
/*  62 */         int tail = args.length - i;
/*  63 */         if (this.targetName != null && tail > 1) {
/*  64 */           addError("msg.multiple.js.to.file", this.targetName);
/*  65 */           return null;
/*     */         } 
/*  67 */         String[] result = new String[tail];
/*  68 */         for (int j = 0; j != tail; j++) {
/*  69 */           result[j] = args[i + j];
/*     */         }
/*  71 */         return result;
/*     */       } 
/*  73 */       if (arg.equals("-help") || arg.equals("-h") || arg.equals("--help")) {
/*     */ 
/*     */         
/*  76 */         this.printHelp = true;
/*  77 */         return null;
/*     */       } 
/*     */ 
/*     */       
/*  81 */       try { if (arg.equals("-version") && ++i < args.length)
/*  82 */         { int version = Integer.parseInt(args[i]);
/*  83 */           this.compilerEnv.setLanguageVersion(version);
/*     */            }
/*     */         
/*  86 */         else if ((arg.equals("-opt") || arg.equals("-O")) && ++i < args.length)
/*     */         
/*     */         { 
/*  89 */           int optLevel = Integer.parseInt(args[i]);
/*  90 */           this.compilerEnv.setOptimizationLevel(optLevel);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */            }
/*     */         
/*  98 */         else if (arg.equals("-nosource"))
/*  99 */         { this.compilerEnv.setGeneratingSource(false);
/*     */            }
/*     */         
/* 102 */         else if (arg.equals("-debug") || arg.equals("-g"))
/* 103 */         { this.compilerEnv.setGenerateDebugInfo(true);
/*     */            }
/*     */         
/* 106 */         else if (arg.equals("-main-method-class") && ++i < args.length)
/* 107 */         { this.compiler.setMainMethodClass(args[i]);
/*     */            }
/*     */         
/* 110 */         else if (arg.equals("-encoding") && ++i < args.length)
/* 111 */         { this.characterEncoding = args[i];
/*     */            }
/*     */         
/* 114 */         else if (arg.equals("-o") && ++i < args.length)
/* 115 */         { String name = args[i];
/* 116 */           int end = name.length();
/* 117 */           if (end == 0 || !Character.isJavaIdentifierStart(name.charAt(0))) {
/*     */ 
/*     */             
/* 120 */             addError("msg.invalid.classfile.name", name);
/*     */           } else {
/*     */             String str;
/* 123 */             for (int j = 1; j < end; j++) {
/* 124 */               char c = name.charAt(j);
/* 125 */               if (!Character.isJavaIdentifierPart(c)) {
/* 126 */                 if (c == '.')
/*     */                 {
/* 128 */                   if (j == end - 6 && name.endsWith(".class")) {
/* 129 */                     str = name.substring(0, j);
/*     */                     break;
/*     */                   } 
/*     */                 }
/* 133 */                 addError("msg.invalid.classfile.name", str);
/*     */                 break;
/*     */               } 
/*     */             } 
/* 137 */             this.targetName = str;
/*     */           }  }
/*     */         else
/* 140 */         { if (arg.equals("-observe-instruction-count")) {
/* 141 */             this.compilerEnv.setGenerateObserverCount(true);
/*     */           }
/* 143 */           if (arg.equals("-package") && ++i < args.length)
/* 144 */           { String pkg = args[i];
/* 145 */             int end = pkg.length();
/* 146 */             for (int j = 0; j != end; ) {
/* 147 */               char c = pkg.charAt(j);
/* 148 */               if (Character.isJavaIdentifierStart(c)) {
/* 149 */                 for (; ++j != end; j++) {
/* 150 */                   c = pkg.charAt(j);
/* 151 */                   if (!Character.isJavaIdentifierPart(c)) {
/*     */                     break;
/*     */                   }
/*     */                 } 
/* 155 */                 if (j == end) {
/*     */                   break;
/*     */                 }
/* 158 */                 if (c == '.' && j != end - 1) {
/*     */                   j++; continue;
/*     */                 } 
/*     */               } 
/* 162 */               addError("msg.package.name", this.targetPackage);
/* 163 */               return null;
/*     */             } 
/* 165 */             this.targetPackage = pkg;
/*     */              }
/*     */           
/* 168 */           else if (arg.equals("-extends") && ++i < args.length)
/* 169 */           { Class<?> superClass; String targetExtends = args[i];
/*     */             
/*     */             try {
/* 172 */               superClass = Class.forName(targetExtends);
/* 173 */             } catch (ClassNotFoundException e) {
/* 174 */               throw new Error(e.toString());
/*     */             } 
/* 176 */             this.compiler.setTargetExtends(superClass);
/*     */              }
/*     */           
/* 179 */           else if (arg.equals("-implements") && ++i < args.length)
/*     */           
/* 181 */           { String targetImplements = args[i];
/* 182 */             StringTokenizer st = new StringTokenizer(targetImplements, ",");
/*     */             
/* 184 */             List<Class<?>> list = new ArrayList<Class<?>>();
/* 185 */             while (st.hasMoreTokens()) {
/* 186 */               String className = st.nextToken();
/*     */               try {
/* 188 */                 list.add(Class.forName(className));
/* 189 */               } catch (ClassNotFoundException e) {
/* 190 */                 throw new Error(e.toString());
/*     */               } 
/*     */             } 
/* 193 */             Class<?>[] implementsClasses = (Class[])list.<Class<?>[]>toArray((Class<?>[][])new Class[list.size()]);
/* 194 */             this.compiler.setTargetImplements(implementsClasses);
/*     */              }
/*     */           
/* 197 */           else if (arg.equals("-d") && ++i < args.length)
/* 198 */           { this.destinationDir = args[i]; }
/*     */           else
/*     */           
/* 201 */           { badUsage(arg);
/* 202 */             return null; }  }  }
/*     */       catch (NumberFormatException numberFormatException) { badUsage(args[i]); return null; }
/*     */     
/* 205 */     }  p(ToolErrorReporter.getMessage("msg.no.file"));
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void badUsage(String s) {
/* 212 */     System.err.println(ToolErrorReporter.getMessage("msg.jsc.bad.usage", Main.class.getName(), s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processSource(String[] filenames) {
/* 222 */     for (int i = 0; i != filenames.length; i++) {
/* 223 */       String filename = filenames[i];
/* 224 */       if (!filename.endsWith(".js")) {
/* 225 */         addError("msg.extension.not.js", filename);
/*     */         return;
/*     */       } 
/* 228 */       File f = new File(filename);
/* 229 */       String source = readSource(f);
/* 230 */       if (source == null)
/*     */         return; 
/* 232 */       String mainClassName = this.targetName;
/* 233 */       if (mainClassName == null) {
/* 234 */         String name = f.getName();
/* 235 */         String nojs = name.substring(0, name.length() - 3);
/* 236 */         mainClassName = getClassName(nojs);
/*     */       } 
/* 238 */       if (this.targetPackage.length() != 0) {
/* 239 */         mainClassName = this.targetPackage + "." + mainClassName;
/*     */       }
/*     */       
/* 242 */       Object[] compiled = this.compiler.compileToClassFiles(source, filename, 1, mainClassName);
/*     */ 
/*     */       
/* 245 */       if (compiled == null || compiled.length == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 249 */       File targetTopDir = null;
/* 250 */       if (this.destinationDir != null) {
/* 251 */         targetTopDir = new File(this.destinationDir);
/*     */       } else {
/* 253 */         String parent = f.getParent();
/* 254 */         if (parent != null) {
/* 255 */           targetTopDir = new File(parent);
/*     */         }
/*     */       } 
/* 258 */       for (int j = 0; j != compiled.length; j += 2) {
/* 259 */         String className = (String)compiled[j];
/* 260 */         byte[] bytes = (byte[])compiled[j + 1];
/* 261 */         File outfile = getOutputFile(targetTopDir, className);
/*     */         try {
/* 263 */           FileOutputStream os = new FileOutputStream(outfile);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 269 */         catch (IOException ioe) {
/* 270 */           addFormatedError(ioe.toString());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String readSource(File f) {
/* 278 */     String absPath = f.getAbsolutePath();
/* 279 */     if (!f.isFile()) {
/* 280 */       addError("msg.jsfile.not.found", absPath);
/* 281 */       return null;
/*     */     } 
/*     */     try {
/* 284 */       return (String)SourceReader.readFileOrUrl(absPath, true, this.characterEncoding);
/*     */     }
/* 286 */     catch (FileNotFoundException ex) {
/* 287 */       addError("msg.couldnt.open", absPath);
/* 288 */     } catch (IOException ioe) {
/* 289 */       addFormatedError(ioe.toString());
/*     */     } 
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private File getOutputFile(File parentDir, String className) {
/* 296 */     String path = className.replace('.', File.separatorChar);
/* 297 */     path = path.concat(".class");
/* 298 */     File f = new File(parentDir, path);
/* 299 */     String dirPath = f.getParent();
/* 300 */     if (dirPath != null) {
/* 301 */       File dir = new File(dirPath);
/* 302 */       if (!dir.exists()) {
/* 303 */         dir.mkdirs();
/*     */       }
/*     */     } 
/* 306 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getClassName(String name) {
/* 316 */     char[] s = new char[name.length() + 1];
/*     */     
/* 318 */     int j = 0;
/*     */     
/* 320 */     if (!Character.isJavaIdentifierStart(name.charAt(0))) {
/* 321 */       s[j++] = '_';
/*     */     }
/* 323 */     for (int i = 0; i < name.length(); i++, j++) {
/* 324 */       char c = name.charAt(i);
/* 325 */       if (Character.isJavaIdentifierPart(c)) {
/* 326 */         s[j] = c;
/*     */       } else {
/* 328 */         s[j] = '_';
/*     */       } 
/*     */     } 
/* 331 */     return (new String(s)).trim();
/*     */   }
/*     */   
/*     */   private static void p(String s) {
/* 335 */     System.out.println(s);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addError(String messageId, String arg) {
/*     */     String msg;
/* 341 */     if (arg == null) {
/* 342 */       msg = ToolErrorReporter.getMessage(messageId);
/*     */     } else {
/* 344 */       msg = ToolErrorReporter.getMessage(messageId, arg);
/*     */     } 
/* 346 */     addFormatedError(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addFormatedError(String message) {
/* 351 */     this.reporter.error(message, null, -1, null, -1);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\jsc\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */