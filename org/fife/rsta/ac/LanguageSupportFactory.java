/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
/*     */ public class LanguageSupportFactory
/*     */   implements PropertyChangeListener
/*     */ {
/*  32 */   private static final LanguageSupportFactory INSTANCE = new LanguageSupportFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> styleToSupportClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, LanguageSupport> styleToSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LANGUAGE_SUPPORT_PROPERTY = "org.fife.rsta.ac.LanguageSupport";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LanguageSupportFactory() {
/*  59 */     createSupportMap();
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
/*     */   public void addLanguageSupport(String style, String lsClassName) {
/*  73 */     this.styleToSupportClass.put(style, lsClassName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createSupportMap() {
/*  82 */     this.styleToSupport = new HashMap<>();
/*  83 */     this.styleToSupportClass = new HashMap<>();
/*     */     
/*  85 */     String prefix = "org.fife.rsta.ac.";
/*     */     
/*  87 */     addLanguageSupport("text/c", prefix + "c.CLanguageSupport");
/*     */     
/*  89 */     addLanguageSupport("text/css", prefix + "css.CssLanguageSupport");
/*     */     
/*  91 */     addLanguageSupport("text/groovy", prefix + "groovy.GroovyLanguageSupport");
/*     */     
/*  93 */     addLanguageSupport("text/html", prefix + "html.HtmlLanguageSupport");
/*     */     
/*  95 */     addLanguageSupport("text/java", prefix + "java.JavaLanguageSupport");
/*     */     
/*  97 */     addLanguageSupport("text/javascript", prefix + "js.JavaScriptLanguageSupport");
/*     */     
/*  99 */     addLanguageSupport("text/jsp", prefix + "jsp.JspLanguageSupport");
/*     */     
/* 101 */     addLanguageSupport("text/less", prefix + "less.LessLanguageSupport");
/*     */     
/* 103 */     addLanguageSupport("text/perl", prefix + "perl.PerlLanguageSupport");
/*     */     
/* 105 */     addLanguageSupport("text/php", prefix + "php.PhpLanguageSupport");
/*     */     
/* 107 */     addLanguageSupport("text/typescript", prefix + "ts.TypeScriptLanguageSupport");
/*     */     
/* 109 */     addLanguageSupport("text/unix", prefix + "sh.ShellLanguageSupport");
/*     */     
/* 111 */     addLanguageSupport("text/xml", prefix + "xml.XmlLanguageSupport");
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
/*     */   public static LanguageSupportFactory get() {
/* 123 */     return INSTANCE;
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
/*     */   public LanguageSupport getSupportFor(String style) {
/* 137 */     LanguageSupport support = this.styleToSupport.get(style);
/*     */     
/* 139 */     if (support == null) {
/* 140 */       String supportClazz = this.styleToSupportClass.get(style);
/* 141 */       if (supportClazz != null) {
/*     */         try {
/* 143 */           Class<?> clazz = Class.forName(supportClazz);
/* 144 */           support = (LanguageSupport)clazz.newInstance();
/* 145 */         } catch (RuntimeException re) {
/* 146 */           throw re;
/* 147 */         } catch (Exception e) {
/* 148 */           e.printStackTrace();
/*     */         } 
/* 150 */         this.styleToSupport.put(style, support);
/*     */         
/* 152 */         this.styleToSupportClass.remove(style);
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     return support;
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
/*     */   private void installSupport(RSyntaxTextArea textArea) {
/* 168 */     String style = textArea.getSyntaxEditingStyle();
/* 169 */     LanguageSupport support = getSupportFor(style);
/* 170 */     if (support != null) {
/* 171 */       support.install(textArea);
/*     */     }
/* 173 */     textArea.putClientProperty("org.fife.rsta.ac.LanguageSupport", support);
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
/*     */   public void propertyChange(PropertyChangeEvent e) {
/* 186 */     RSyntaxTextArea source = (RSyntaxTextArea)e.getSource();
/* 187 */     String name = e.getPropertyName();
/* 188 */     if ("RSTA.syntaxStyle".equals(name)) {
/* 189 */       uninstallSupport(source);
/* 190 */       installSupport(source);
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
/*     */   public void register(RSyntaxTextArea textArea) {
/* 205 */     installSupport(textArea);
/* 206 */     textArea.addPropertyChangeListener("RSTA.syntaxStyle", this);
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
/*     */   private void uninstallSupport(RSyntaxTextArea textArea) {
/* 218 */     LanguageSupport support = (LanguageSupport)textArea.getClientProperty("org.fife.rsta.ac.LanguageSupport");
/*     */     
/* 220 */     if (support != null) {
/* 221 */       support.uninstall(textArea);
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
/*     */   public void unregister(RSyntaxTextArea textArea) {
/* 234 */     uninstallSupport(textArea);
/* 235 */     textArea.removePropertyChangeListener("RSTA.syntaxStyle", this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\LanguageSupportFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */