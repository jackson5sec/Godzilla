/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import org.fife.rsta.ac.LanguageSupportFactory;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.FieldInfo;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.DescWindowCallback;
/*     */ import org.fife.ui.autocomplete.ExternalURLHandler;
/*     */ import org.fife.ui.autocomplete.Util;
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
/*     */ public class JavadocUrlHandler
/*     */   implements ExternalURLHandler
/*     */ {
/*     */   private String doBackups(String pkg, int backupCount) {
/*  52 */     int lastDot = pkg.length();
/*  53 */     while (lastDot > -1 && backupCount > 0) {
/*  54 */       lastDot = pkg.lastIndexOf('.', lastDot);
/*  55 */       backupCount--;
/*     */     } 
/*  57 */     return (lastDot > -1) ? pkg.substring(0, lastDot) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JavaLanguageSupport getJavaLanguageSupport() {
/*  67 */     return (JavaLanguageSupport)LanguageSupportFactory.get()
/*  68 */       .getSupportFor("text/java");
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
/*  79 */     int pound = url.indexOf('#');
/*  80 */     return (pound > -1) ? url.substring(pound + 1) : null;
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
/*     */   private static final String[] getArgs(String methodSignature) {
/*  92 */     String[] args = null;
/*     */     
/*  94 */     int lparen = methodSignature.indexOf('(');
/*  95 */     if (lparen > -1) {
/*  96 */       int rparen = methodSignature.indexOf(')', lparen);
/*  97 */       if (rparen > -1 && rparen > lparen + 1) {
/*  98 */         String temp = methodSignature.substring(lparen, rparen);
/*  99 */         args = temp.split("\\s*,\\s*");
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     if (args == null) {
/* 104 */       args = new String[0];
/*     */     }
/* 106 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getClass(Completion c, String desc) {
/* 117 */     String clazz = null;
/*     */     
/* 119 */     if (c instanceof ClassCompletion) {
/* 120 */       clazz = ((ClassCompletion)c).getClassName(true);
/*     */     }
/* 122 */     else if (c instanceof MemberCompletion) {
/* 123 */       MemberCompletion mc = (MemberCompletion)c;
/* 124 */       clazz = mc.getEnclosingClassName(true);
/*     */     } else {
/*     */       
/* 127 */       System.err.println("Can't determine class from completion type: " + c
/* 128 */           .getClass() + " (" + c.toString() + ") - href: " + desc);
/*     */     } 
/*     */     
/* 131 */     return clazz;
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
/*     */   private String getPackage(Completion c, String desc) {
/* 146 */     String pkg = null;
/*     */     
/* 148 */     if (c instanceof ClassCompletion) {
/* 149 */       pkg = ((ClassCompletion)c).getPackageName();
/*     */     }
/* 151 */     else if (c instanceof MemberCompletion) {
/* 152 */       String definedIn = ((MemberCompletion)c).getEnclosingClassName(true);
/* 153 */       if (definedIn != null) {
/* 154 */         int lastDot = definedIn.lastIndexOf('.');
/* 155 */         if (lastDot > -1) {
/* 156 */           pkg = definedIn.substring(0, lastDot);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 161 */       System.err.println("Can't determine package from completion type: " + c
/* 162 */           .getClass() + " (" + c.toString() + ") - href: " + desc);
/*     */     } 
/*     */     
/* 165 */     return pkg;
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
/*     */   private boolean isRelativeUrl(String text) {
/* 178 */     String[] EXTS = { ".html", ".htm" };
/* 179 */     for (int i = 0; i < EXTS.length; i++) {
/* 180 */       if (text.endsWith(EXTS[i]) || text.contains(EXTS[i] + "#") || text
/* 181 */         .contains(EXTS[i] + "?")) {
/* 182 */         return true;
/*     */       }
/*     */     } 
/* 185 */     return false;
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
/* 198 */     URL url = e.getURL();
/* 199 */     if (url != null) {
/*     */       
/*     */       try {
/* 202 */         Util.browse(new URI(url.toString()));
/* 203 */       } catch (URISyntaxException ioe) {
/* 204 */         UIManager.getLookAndFeel().provideErrorFeedback(null);
/* 205 */         ioe.printStackTrace();
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 213 */     String desc = e.getDescription();
/*     */     
/* 215 */     if (desc != null)
/*     */     {
/* 217 */       if (isRelativeUrl(desc)) {
/* 218 */         int ext = desc.indexOf(".htm");
/* 219 */         if (ext > -1)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 224 */           String anchor = getAnchor(desc);
/* 225 */           String clazz = desc.substring(0, ext);
/* 226 */           int backups = 0;
/* 227 */           while (clazz.startsWith("../")) {
/* 228 */             backups++;
/* 229 */             clazz = clazz.substring(3);
/*     */           } 
/* 231 */           clazz = clazz.replace('/', '.');
/*     */           
/* 233 */           String pkg = getPackage(c, desc);
/* 234 */           if (pkg != null) {
/* 235 */             clazz = doBackups(pkg, backups) + "." + clazz;
/* 236 */             JavaLanguageSupport jls = getJavaLanguageSupport();
/* 237 */             ClassFile cf = jls.getJarManager().getClassEntry(clazz);
/* 238 */             if (cf != null) {
/* 239 */               ClassCompletion cc = new ClassCompletion(c.getProvider(), cf);
/* 240 */               callback.showSummaryFor(cc, anchor);
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
/* 251 */         JavaLanguageSupport jls = getJavaLanguageSupport();
/*     */         
/* 253 */         String clazz = desc;
/* 254 */         String member = null;
/* 255 */         int pound = desc.indexOf('#');
/* 256 */         if (pound > -1) {
/* 257 */           member = clazz.substring(pound + 1);
/* 258 */           clazz = clazz.substring(0, pound);
/*     */         } 
/*     */ 
/*     */         
/* 262 */         if (member == null) {
/* 263 */           boolean guessedPackage = false;
/* 264 */           if (clazz.indexOf('.') == -1) {
/* 265 */             String pkg = getPackage(c, desc);
/* 266 */             if (pkg != null) {
/* 267 */               clazz = pkg + "." + clazz;
/*     */             }
/* 269 */             guessedPackage = true;
/*     */           } 
/* 271 */           ClassFile cf = jls.getJarManager().getClassEntry(clazz);
/* 272 */           if (cf == null && guessedPackage) {
/*     */             
/* 274 */             int lastDot = clazz.lastIndexOf('.');
/* 275 */             clazz = "java.lang." + clazz.substring(lastDot + 1);
/* 276 */             cf = jls.getJarManager().getClassEntry(clazz);
/*     */           } 
/* 278 */           if (cf != null) {
/* 279 */             ClassCompletion cc = new ClassCompletion(c.getProvider(), cf);
/* 280 */             callback.showSummaryFor(cc, null);
/*     */           } else {
/*     */             
/* 283 */             UIManager.getLookAndFeel().provideErrorFeedback(null);
/* 284 */             System.err.println("Unknown class: " + clazz);
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 292 */           boolean guessedPackage = false;
/*     */           
/* 294 */           if (pound == 0) {
/*     */             
/* 296 */             clazz = getClass(c, desc);
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 301 */           else if (clazz.indexOf('.') == -1) {
/* 302 */             String pkg = getPackage(c, desc);
/* 303 */             if (pkg != null) {
/* 304 */               clazz = pkg + "." + clazz;
/*     */             }
/* 306 */             guessedPackage = true;
/*     */           } 
/*     */ 
/*     */           
/* 310 */           ClassFile cf = jls.getJarManager().getClassEntry(clazz);
/* 311 */           if (cf == null && guessedPackage) {
/*     */             
/* 313 */             int lastDot = clazz.lastIndexOf('.');
/* 314 */             clazz = "java.lang." + clazz.substring(lastDot + 1);
/* 315 */             cf = jls.getJarManager().getClassEntry(clazz);
/*     */           } 
/* 317 */           if (cf != null) {
/*     */             
/* 319 */             Completion memberCompletion = null;
/*     */             
/* 321 */             int lparen = member.indexOf('(');
/* 322 */             if (lparen == -1) {
/* 323 */               FieldInfo fi = cf.getFieldInfoByName(member);
/* 324 */               if (fi != null) {
/* 325 */                 memberCompletion = new FieldCompletion(c.getProvider(), fi);
/*     */               } else {
/*     */                 
/* 328 */                 List<MethodInfo> miList = cf.getMethodInfoByName(member, -1);
/* 329 */                 if (miList != null && miList.size() > 0) {
/* 330 */                   MethodInfo mi = miList.get(0);
/* 331 */                   memberCompletion = new MethodCompletion(c.getProvider(), mi);
/*     */                 }
/*     */               
/*     */               } 
/*     */             } else {
/*     */               
/* 337 */               String[] args = getArgs(member);
/* 338 */               String methodName = member.substring(0, lparen);
/* 339 */               List<MethodInfo> miList = cf.getMethodInfoByName(methodName, args.length);
/* 340 */               if (miList != null && miList.size() > 0) {
/* 341 */                 if (miList.size() > 1) {
/*     */                   
/* 343 */                   System.err.println("Multiple overload support not yet implemented");
/*     */                 } else {
/*     */                   
/* 346 */                   MethodInfo mi = miList.get(0);
/* 347 */                   memberCompletion = new MethodCompletion(c.getProvider(), mi);
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/* 352 */             if (memberCompletion != null) {
/* 353 */               callback.showSummaryFor(memberCompletion, null);
/*     */             }
/*     */           }
/*     */           else {
/*     */             
/* 358 */             UIManager.getLookAndFeel().provideErrorFeedback(null);
/* 359 */             System.err.println("Unknown class: " + clazz + " (href: " + desc + ")");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavadocUrlHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */