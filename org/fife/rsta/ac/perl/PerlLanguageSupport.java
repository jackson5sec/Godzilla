/*     */ package org.fife.rsta.ac.perl;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractLanguageSupport;
/*     */ import org.fife.rsta.ac.IOUtil;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.CompletionCellRenderer;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rtextarea.ToolTipSupplier;
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
/*     */ public class PerlLanguageSupport
/*     */   extends AbstractLanguageSupport
/*     */ {
/*     */   private PerlCompletionProvider provider;
/*     */   private PerlParser parser;
/*     */   private static File perlInstallLoc;
/*     */   private static File DEFAULT_PERL_INSTALL_LOC;
/*     */   private static boolean useParensWithFunctions;
/*     */   private static boolean useSystemPerldoc;
/*     */   
/*     */   static {
/*  70 */     String path = IOUtil.getEnvSafely("PATH");
/*     */     
/*  72 */     if (path != null) {
/*     */       
/*  74 */       String perlLoc = "perl";
/*  75 */       if (File.separatorChar == '\\') {
/*  76 */         perlLoc = perlLoc + ".exe";
/*     */       }
/*     */       
/*  79 */       String[] dirs = path.split(File.pathSeparator);
/*  80 */       for (int i = 0; i < dirs.length; i++) {
/*  81 */         File temp = new File(dirs[i], perlLoc);
/*     */         
/*  83 */         if (temp.isFile()) {
/*  84 */           DEFAULT_PERL_INSTALL_LOC = (new File(dirs[i])).getParentFile();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  89 */       perlInstallLoc = DEFAULT_PERL_INSTALL_LOC;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PerlLanguageSupport() {
/* 100 */     setParameterAssistanceEnabled(true);
/* 101 */     setShowDescWindow(true);
/* 102 */     setAutoCompleteEnabled(true);
/* 103 */     setAutoActivationEnabled(true);
/* 104 */     setAutoActivationDelay(800);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/* 113 */     CompletionCellRenderer ccr = new CompletionCellRenderer();
/* 114 */     ccr.setShowTypes(false);
/* 115 */     return (ListCellRenderer<Object>)ccr;
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
/*     */   public static File getDefaultPerlInstallLocation() {
/* 127 */     return DEFAULT_PERL_INSTALL_LOC;
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
/*     */   public static File getPerlInstallLocation() {
/* 139 */     return perlInstallLoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PerlParser getParser() {
/* 149 */     if (this.parser == null) {
/* 150 */       this.parser = new PerlParser();
/*     */     }
/* 152 */     return this.parser;
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
/*     */   public PerlParser getParser(RSyntaxTextArea textArea) {
/* 166 */     Object parser = textArea.getClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser");
/* 167 */     if (parser instanceof PerlParser) {
/* 168 */       return (PerlParser)parser;
/*     */     }
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PerlCompletionProvider getProvider() {
/* 180 */     if (this.provider == null) {
/* 181 */       this.provider = new PerlCompletionProvider();
/*     */     }
/* 183 */     return this.provider;
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
/*     */   public String getPerl5LibOverride() {
/* 195 */     return getParser().getPerl5LibOverride();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseParensWithFunctions() {
/* 206 */     return useParensWithFunctions;
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
/*     */   public static boolean getUseSystemPerldoc() {
/* 224 */     return useSystemPerldoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getWarningsEnabled() {
/* 235 */     return getParser().getWarningsEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(RSyntaxTextArea textArea) {
/* 245 */     PerlCompletionProvider provider = getProvider();
/* 246 */     AutoCompletion ac = createAutoCompletion((CompletionProvider)provider);
/* 247 */     ac.install((JTextComponent)textArea);
/* 248 */     installImpl(textArea, ac);
/*     */     
/* 250 */     textArea.setToolTipSupplier((ToolTipSupplier)provider);
/*     */     
/* 252 */     PerlParser parser = getParser();
/* 253 */     textArea.addParser((Parser)parser);
/* 254 */     textArea.putClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser", parser);
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
/*     */   public boolean isParsingEnabled() {
/* 271 */     return getParser().isEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTaintModeEnabled() {
/* 282 */     return getParser().isTaintModeEnabled();
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
/*     */   public void setParsingEnabled(boolean enabled) {
/* 294 */     getParser().setEnabled(enabled);
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
/*     */   public void setPerl5LibOverride(String override) {
/* 307 */     getParser().setPerl5LibOverride(override);
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
/*     */   public static void setPerlInstallLocation(File loc) {
/* 319 */     perlInstallLoc = loc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaintModeEnabled(boolean enabled) {
/* 330 */     getParser().setTaintModeEnabled(enabled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWarningsEnabled(boolean enabled) {
/* 341 */     getParser().setWarningsEnabled(enabled);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseParensWithFunctions(boolean use) {
/* 352 */     if (use != useParensWithFunctions) {
/* 353 */       useParensWithFunctions = use;
/* 354 */       if (this.provider != null) {
/* 355 */         this.provider.setUseParensWithFunctions(use);
/*     */       }
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
/*     */   
/*     */   public static void setUseSystemPerldoc(boolean use) {
/* 375 */     useSystemPerldoc = use;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 385 */     uninstallImpl(textArea);
/*     */     
/* 387 */     PerlParser parser = getParser(textArea);
/* 388 */     if (parser != null)
/* 389 */       textArea.removeParser((Parser)parser); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\perl\PerlLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */