/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.FieldInfo;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.js.completion.JSClassCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSFieldCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSFunctionCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.DescWindowCallback;
/*     */ import org.fife.ui.autocomplete.ExternalURLHandler;
/*     */ import org.fife.ui.autocomplete.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptDocUrlhandler
/*     */   implements ExternalURLHandler
/*     */ {
/*     */   private JavaScriptLanguageSupport languageSupport;
/*     */   
/*     */   public JavaScriptDocUrlhandler(JavaScriptLanguageSupport languageSupport) {
/*  30 */     this.languageSupport = languageSupport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getClass(Completion c, String desc) {
/*  38 */     String clazz = null;
/*     */     
/*  40 */     if (c instanceof JSClassCompletion) {
/*  41 */       clazz = ((JSClassCompletion)c).getClassName(true);
/*     */     }
/*  43 */     else if (c instanceof JSCompletion) {
/*  44 */       JSCompletion jsc = (JSCompletion)c;
/*  45 */       clazz = jsc.getEnclosingClassName(true);
/*     */     } else {
/*     */       
/*  48 */       Logger.logError("Can't determine class from completion type: " + c
/*  49 */           .getClass() + " (" + c.toString() + ") - href: " + desc);
/*     */     } 
/*     */     
/*  52 */     return clazz;
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
/*     */   private String getPackage(Completion c, String desc) {
/*  66 */     String pkg = null;
/*     */     
/*  68 */     if (c instanceof JSClassCompletion) {
/*  69 */       pkg = ((JSClassCompletion)c).getPackageName();
/*     */     }
/*  71 */     if (c instanceof JSCompletion) {
/*  72 */       String definedIn = ((JSCompletion)c).getEnclosingClassName(true);
/*  73 */       if (definedIn != null) {
/*  74 */         int lastDot = definedIn.lastIndexOf('.');
/*  75 */         if (lastDot > -1) {
/*  76 */           pkg = definedIn.substring(0, lastDot);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/*  81 */       Logger.logError("Can't determine package from completion type: " + c
/*  82 */           .getClass() + " (" + c.toString() + ") - href: " + desc);
/*     */     } 
/*     */     
/*  85 */     return pkg;
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
/*     */   private boolean isRelativeUrl(String text) {
/*  97 */     String[] EXTS = { ".html", ".htm" };
/*  98 */     for (int i = 0; i < EXTS.length; i++) {
/*  99 */       if (text.endsWith(EXTS[i]) || text.contains(EXTS[i] + "#") || text
/* 100 */         .contains(EXTS[i] + "?")) {
/* 101 */         return true;
/*     */       }
/*     */     } 
/* 104 */     return false;
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
/*     */   private String doBackups(String pkg, int backupCount) {
/* 116 */     int lastDot = pkg.length();
/* 117 */     while (lastDot > -1 && backupCount > 0) {
/* 118 */       lastDot = pkg.lastIndexOf('.', lastDot);
/* 119 */       backupCount--;
/*     */     } 
/* 121 */     return (lastDot > -1) ? pkg.substring(0, lastDot) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JavaScriptLanguageSupport getJavaScriptLanguageSupport() {
/* 131 */     return this.languageSupport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String getAnchor(String url) {
/* 142 */     int pound = url.indexOf('#');
/* 143 */     return (pound > -1) ? url.substring(pound + 1) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String[] getArgs(String methodSignature) {
/* 154 */     String[] args = null;
/*     */     
/* 156 */     int lparen = methodSignature.indexOf('(');
/* 157 */     if (lparen > -1) {
/* 158 */       int rparen = methodSignature.indexOf(')', lparen);
/* 159 */       if (rparen > -1 && rparen > lparen + 1) {
/* 160 */         String temp = methodSignature.substring(lparen, rparen);
/* 161 */         args = temp.split("\\s*,\\s*");
/*     */       } 
/*     */     } 
/*     */     
/* 165 */     if (args == null) {
/* 166 */       args = new String[0];
/*     */     }
/* 168 */     return args;
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
/*     */   public void urlClicked(HyperlinkEvent e, Completion c, DescWindowCallback callback) {
/* 181 */     URL url = e.getURL();
/* 182 */     if (url != null) {
/*     */       
/*     */       try {
/* 185 */         Util.browse(new URI(url.toString()));
/* 186 */       } catch (URISyntaxException ioe) {
/* 187 */         UIManager.getLookAndFeel().provideErrorFeedback(null);
/* 188 */         ioe.printStackTrace();
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 196 */     String desc = e.getDescription();
/* 197 */     Logger.log(desc);
/* 198 */     if (desc != null)
/*     */     {
/* 200 */       if (isRelativeUrl(desc)) {
/* 201 */         int ext = desc.indexOf(".htm");
/* 202 */         if (ext > -1)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 207 */           String anchor = getAnchor(desc);
/* 208 */           String clazz = desc.substring(0, ext);
/* 209 */           int backups = 0;
/* 210 */           while (clazz.startsWith("../")) {
/* 211 */             backups++;
/* 212 */             clazz = clazz.substring(3);
/*     */           } 
/* 214 */           clazz = clazz.replace('/', '.');
/*     */           
/* 216 */           String pkg = getPackage(c, desc);
/* 217 */           if (pkg != null) {
/* 218 */             clazz = doBackups(pkg, backups) + "." + clazz;
/* 219 */             JavaScriptLanguageSupport jls = getJavaScriptLanguageSupport();
/* 220 */             ClassFile cf = jls.getJarManager().getClassEntry(clazz);
/* 221 */             if (cf != null) {
/* 222 */               JSClassCompletion cc = new JSClassCompletion(c.getProvider(), cf, true);
/* 223 */               callback.showSummaryFor((Completion)cc, anchor);
/*     */             
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 234 */         JavaScriptLanguageSupport jls = getJavaScriptLanguageSupport();
/*     */         
/* 236 */         String clazz = desc;
/* 237 */         String member = null;
/* 238 */         int pound = desc.indexOf('#');
/* 239 */         if (pound > -1) {
/* 240 */           member = clazz.substring(pound + 1);
/* 241 */           clazz = clazz.substring(0, pound);
/*     */         } 
/*     */ 
/*     */         
/* 245 */         if (member == null) {
/* 246 */           boolean guessedPackage = false;
/* 247 */           if (clazz.indexOf('.') == -1) {
/* 248 */             String pkg = getPackage(c, desc);
/* 249 */             if (pkg != null) {
/* 250 */               clazz = pkg + "." + clazz;
/*     */             }
/* 252 */             guessedPackage = true;
/*     */           } 
/* 254 */           ClassFile cf = jls.getJarManager().getClassEntry(clazz);
/* 255 */           if (cf == null && guessedPackage) {
/*     */             
/* 257 */             int lastDot = clazz.lastIndexOf('.');
/* 258 */             clazz = "java.lang." + clazz.substring(lastDot + 1);
/* 259 */             cf = jls.getJarManager().getClassEntry(clazz);
/*     */           } 
/* 261 */           if (cf != null) {
/* 262 */             JSClassCompletion cc = new JSClassCompletion(c.getProvider(), cf, true);
/* 263 */             callback.showSummaryFor((Completion)cc, null);
/*     */           } else {
/*     */             
/* 266 */             UIManager.getLookAndFeel().provideErrorFeedback(null);
/* 267 */             Logger.log("Unknown class: " + clazz);
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 275 */           boolean guessedPackage = false;
/*     */           
/* 277 */           if (pound == 0) {
/*     */             
/* 279 */             clazz = getClass(c, desc);
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 284 */           else if (clazz.indexOf('.') == -1) {
/* 285 */             String pkg = getPackage(c, desc);
/* 286 */             if (pkg != null) {
/* 287 */               clazz = pkg + "." + clazz;
/*     */             }
/* 289 */             guessedPackage = true;
/*     */           } 
/*     */ 
/*     */           
/* 293 */           ClassFile cf = (clazz != null) ? jls.getJarManager().getClassEntry(clazz) : null;
/* 294 */           if (cf == null && guessedPackage) {
/*     */             
/* 296 */             int lastDot = clazz.lastIndexOf('.');
/* 297 */             clazz = "java.lang." + clazz.substring(lastDot + 1);
/* 298 */             cf = jls.getJarManager().getClassEntry(clazz);
/*     */           } 
/*     */           
/* 301 */           if (cf != null) {
/*     */             JSFunctionCompletion jSFunctionCompletion;
/* 303 */             Completion memberCompletion = null;
/* 304 */             int lparen = member.indexOf('(');
/* 305 */             if (lparen == -1) {
/* 306 */               FieldInfo fi = cf.getFieldInfoByName(member);
/* 307 */               if (fi != null) {
/* 308 */                 JSFieldCompletion jSFieldCompletion = new JSFieldCompletion(c.getProvider(), fi);
/*     */               } else {
/*     */                 
/* 311 */                 List<MethodInfo> miList = cf.getMethodInfoByName(member, -1);
/* 312 */                 if (miList != null && miList.size() > 0) {
/* 313 */                   MethodInfo mi = miList.get(0);
/* 314 */                   jSFunctionCompletion = new JSFunctionCompletion(c.getProvider(), mi);
/*     */                 }
/*     */               
/*     */               } 
/*     */             } else {
/*     */               
/* 320 */               String[] args = getArgs(member);
/* 321 */               String methodName = member.substring(0, lparen);
/* 322 */               List<MethodInfo> miList = cf.getMethodInfoByName(methodName, args.length);
/* 323 */               if (miList != null && miList.size() > 0) {
/* 324 */                 if (miList.size() > 1) {
/*     */                   
/* 326 */                   Logger.log("Multiple overload support not yet implemented");
/*     */                 } else {
/*     */                   
/* 329 */                   MethodInfo mi = miList.get(0);
/* 330 */                   jSFunctionCompletion = new JSFunctionCompletion(c.getProvider(), mi);
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/* 335 */             if (jSFunctionCompletion != null) {
/* 336 */               callback.showSummaryFor((Completion)jSFunctionCompletion, null);
/*     */             }
/*     */           }
/*     */           else {
/*     */             
/* 341 */             UIManager.getLookAndFeel().provideErrorFeedback(null);
/* 342 */             Logger.logError("Unknown class: " + clazz + " (href: " + desc + ")");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptDocUrlhandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */