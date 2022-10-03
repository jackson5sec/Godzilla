/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractLanguageSupport;
/*     */ import org.fife.rsta.ac.GoToMemberAction;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.rsta.ac.java.buildpath.ClasspathLibraryInfo;
/*     */ import org.fife.rsta.ac.java.buildpath.ClasspathSourceLocation;
/*     */ import org.fife.rsta.ac.java.buildpath.LibraryInfo;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.js.tree.JavaScriptOutlineTree;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.modes.JavaScriptTokenMaker;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.NodeVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptLanguageSupport
/*     */   extends AbstractLanguageSupport
/*     */ {
/*     */   private Map<JavaScriptParser, Info> parserToInfoMap;
/*     */   private JarManager jarManager;
/*     */   private boolean xmlAvailable;
/*     */   private boolean client;
/*     */   private boolean strictMode;
/*     */   private int languageVersion;
/*     */   private JsErrorParser errorParser;
/*     */   private JavaScriptParser parser;
/*     */   private JavaScriptCompletionProvider provider;
/*     */   private File defaultJshintrc;
/*     */   private static final String PROPERTY_LISTENER = "org.fife.rsta.ac.js.JavaScriptLanguageSupport.Listener";
/*     */   
/*     */   public JavaScriptLanguageSupport() {
/*  89 */     this.parserToInfoMap = new HashMap<>();
/*  90 */     this.jarManager = createJarManager();
/*  91 */     this.provider = createJavaScriptCompletionProvider();
/*  92 */     setErrorParser(JsErrorParser.RHINO);
/*     */ 
/*     */     
/*  95 */     setECMAVersion((String)null, this.jarManager);
/*  96 */     setDefaultCompletionCellRenderer((ListCellRenderer)new JavaScriptCellRenderer());
/*  97 */     setAutoActivationEnabled(true);
/*  98 */     setParameterAssistanceEnabled(true);
/*  99 */     setShowDescWindow(true);
/* 100 */     setLanguageVersion(-2147483648);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JarManager createJarManager() {
/* 110 */     JarManager jarManager = new JarManager();
/* 111 */     return jarManager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setECMAVersion(String version, JarManager jarManager) {
/*     */     try {
/* 118 */       List<String> classes = this.provider.getProvider().getTypesFactory().setTypeDeclarationVersion(version, isXmlAvailable(), isClient());
/* 119 */       this.provider.getProvider().setXMLSupported(isXmlAvailable());
/* 120 */       if (classes != null) {
/* 121 */         ClasspathLibraryInfo classpathLibraryInfo = new ClasspathLibraryInfo(classes, (SourceLocation)new ClasspathSourceLocation());
/*     */         
/* 123 */         jarManager.addClassFileSource((LibraryInfo)classpathLibraryInfo);
/*     */       } 
/* 125 */     } catch (IOException ioe) {
/* 126 */       ioe.printStackTrace();
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
/*     */   protected JavaScriptCompletionProvider createJavaScriptCompletionProvider() {
/* 139 */     return new JavaScriptCompletionProvider(this.jarManager, this);
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
/*     */   public File getDefaultJsHintRCFile() {
/* 154 */     return this.defaultJshintrc;
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
/*     */   public JsErrorParser getErrorParser() {
/* 167 */     return this.errorParser;
/*     */   }
/*     */ 
/*     */   
/*     */   public JarManager getJarManager() {
/* 172 */     return this.jarManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptParser getJavaScriptParser() {
/* 177 */     return this.parser;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getJsHintIndent() {
/* 182 */     int DEFAULT = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     return 4;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLanguageVersion() {
/* 247 */     return this.languageVersion;
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
/*     */   public JavaScriptParser getParser(RSyntaxTextArea textArea) {
/* 262 */     Object parser = textArea.getClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser");
/* 263 */     if (parser instanceof JavaScriptParser) {
/* 264 */       return (JavaScriptParser)parser;
/*     */     }
/* 266 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(RSyntaxTextArea textArea) {
/* 275 */     AutoCompletion ac = new JavaScriptAutoCompletion(this.provider, textArea);
/* 276 */     ac.setListCellRenderer(getDefaultCompletionCellRenderer());
/* 277 */     ac.setAutoCompleteEnabled(isAutoCompleteEnabled());
/* 278 */     ac.setAutoActivationEnabled(isAutoActivationEnabled());
/* 279 */     ac.setAutoActivationDelay(getAutoActivationDelay());
/* 280 */     ac.setParameterAssistanceEnabled(isParameterAssistanceEnabled());
/* 281 */     ac.setExternalURLHandler(new JavaScriptDocUrlhandler(this));
/* 282 */     ac.setShowDescWindow(getShowDescWindow());
/* 283 */     ac.install((JTextComponent)textArea);
/* 284 */     installImpl(textArea, ac);
/*     */     
/* 286 */     Listener listener = new Listener(textArea);
/* 287 */     textArea.putClientProperty("org.fife.rsta.ac.js.JavaScriptLanguageSupport.Listener", listener);
/*     */     
/* 289 */     this.parser = new JavaScriptParser(this, textArea);
/* 290 */     textArea.putClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser", this.parser);
/* 291 */     textArea.addParser((Parser)this.parser);
/*     */ 
/*     */     
/* 294 */     Info info = new Info(this.provider, this.parser);
/* 295 */     this.parserToInfoMap.put(this.parser, info);
/*     */     
/* 297 */     installKeyboardShortcuts(textArea);
/*     */ 
/*     */     
/* 300 */     JavaScriptTokenMaker.setE4xSupported(isXmlAvailable());
/*     */     
/* 302 */     textArea.setLinkGenerator(new JavaScriptLinkGenerator(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void installKeyboardShortcuts(RSyntaxTextArea textArea) {
/* 313 */     InputMap im = textArea.getInputMap();
/* 314 */     ActionMap am = textArea.getActionMap();
/* 315 */     int c = textArea.getToolkit().getMenuShortcutKeyMask();
/* 316 */     int shift = 64;
/*     */     
/* 318 */     im.put(KeyStroke.getKeyStroke(79, c | shift), "GoToType");
/* 319 */     am.put("GoToType", (Action)new GoToMemberAction(JavaScriptOutlineTree.class));
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
/*     */   public boolean isStrictMode() {
/* 333 */     return this.strictMode;
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
/*     */   public boolean isXmlAvailable() {
/* 346 */     return this.xmlAvailable;
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
/*     */   public boolean isClient() {
/* 359 */     return this.client;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void reparseDocument(int offset) {
/* 364 */     this.provider.reparseDocument(offset);
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
/*     */   public void setClient(boolean client) {
/* 377 */     this.client = client;
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
/*     */   public boolean setDefaultJsHintRCFile(File file) {
/* 395 */     if ((file == null && this.defaultJshintrc != null) || (file != null && this.defaultJshintrc == null) || (file != null && 
/* 396 */       !file.equals(this.defaultJshintrc))) {
/* 397 */       this.defaultJshintrc = file;
/* 398 */       return true;
/*     */     } 
/* 400 */     return false;
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
/*     */   public boolean setErrorParser(JsErrorParser errorParser) {
/* 414 */     if (errorParser == null) {
/* 415 */       throw new IllegalArgumentException("errorParser cannot be null");
/*     */     }
/* 417 */     if (errorParser != this.errorParser) {
/* 418 */       this.errorParser = errorParser;
/* 419 */       return true;
/*     */     } 
/* 421 */     return false;
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
/*     */   public void setLanguageVersion(int languageVersion) {
/* 437 */     if (languageVersion < 0) {
/* 438 */       languageVersion = -1;
/*     */     }
/* 440 */     this.languageVersion = languageVersion;
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
/*     */   public boolean setStrictMode(boolean strict) {
/* 454 */     if (strict != this.strictMode) {
/* 455 */       this.strictMode = strict;
/* 456 */       return true;
/*     */     } 
/* 458 */     return false;
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
/*     */   public boolean setXmlAvailable(boolean available) {
/* 472 */     if (available != this.xmlAvailable) {
/* 473 */       this.xmlAvailable = available;
/* 474 */       return true;
/*     */     } 
/* 476 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 483 */     uninstallImpl(textArea);
/*     */     
/* 485 */     JavaScriptParser parser = getParser(textArea);
/* 486 */     Info info = this.parserToInfoMap.remove(parser);
/* 487 */     if (info != null) {
/* 488 */       parser.removePropertyChangeListener("AST", info);
/*     */     }
/*     */     
/* 491 */     textArea.removeParser((Parser)parser);
/* 492 */     textArea.putClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser", null);
/* 493 */     textArea.setToolTipSupplier(null);
/*     */     
/* 495 */     Object listener = textArea.getClientProperty("org.fife.rsta.ac.js.JavaScriptLanguageSupport.Listener");
/* 496 */     if (listener instanceof Listener) {
/* 497 */       ((Listener)listener).uninstall();
/* 498 */       textArea.putClientProperty("org.fife.rsta.ac.js.JavaScriptLanguageSupport.Listener", null);
/*     */     } 
/*     */     
/* 501 */     uninstallKeyboardShortcuts(textArea);
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
/*     */   private void uninstallKeyboardShortcuts(RSyntaxTextArea textArea) {
/* 513 */     InputMap im = textArea.getInputMap();
/* 514 */     ActionMap am = textArea.getActionMap();
/* 515 */     int c = textArea.getToolkit().getMenuShortcutKeyMask();
/* 516 */     int shift = 64;
/*     */     
/* 518 */     im.remove(KeyStroke.getKeyStroke(79, c | shift));
/* 519 */     am.remove("GoToType");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Info
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public JavaScriptCompletionProvider provider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Info(JavaScriptCompletionProvider provider, JavaScriptParser parser) {
/* 536 */       this.provider = provider;
/*     */       
/* 538 */       parser.addPropertyChangeListener("AST", this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 550 */       String name = e.getPropertyName();
/*     */       
/* 552 */       if ("AST".equals(name)) {
/* 553 */         AstRoot root = (AstRoot)e.getNewValue();
/* 554 */         this.provider.setASTRoot(root);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class JavaScriptAutoCompletion
/*     */     extends AutoCompletion
/*     */   {
/*     */     private RSyntaxTextArea textArea;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JavaScriptAutoCompletion(JavaScriptCompletionProvider provider, RSyntaxTextArea textArea) {
/* 572 */       super((CompletionProvider)provider);
/* 573 */       this.textArea = textArea;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getReplacementText(Completion c, Document doc, int start, int len) {
/* 580 */       String replacement = super.getReplacementText(c, doc, start, len);
/* 581 */       if (c instanceof org.fife.rsta.ac.js.completion.JavaScriptShorthandCompletion) {
/*     */         
/*     */         try {
/*     */           
/* 585 */           int caret = this.textArea.getCaretPosition();
/* 586 */           String leadingWS = RSyntaxUtilities.getLeadingWhitespace(doc, caret);
/* 587 */           if (replacement.indexOf('\n') > -1) {
/* 588 */             replacement = replacement.replaceAll("\n", "\n" + leadingWS);
/*     */           
/*     */           }
/*     */         }
/* 592 */         catch (BadLocationException badLocationException) {}
/*     */       }
/* 594 */       return replacement;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int refreshPopupWindow() {
/* 600 */       JavaScriptParser parser = JavaScriptLanguageSupport.this.getParser(this.textArea);
/* 601 */       RSyntaxDocument doc = (RSyntaxDocument)this.textArea.getDocument();
/* 602 */       String style = this.textArea.getSyntaxEditingStyle();
/* 603 */       parser.parse(doc, style);
/* 604 */       return super.refreshPopupWindow();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     implements CaretListener, ActionListener
/*     */   {
/*     */     private RSyntaxTextArea textArea;
/*     */ 
/*     */     
/*     */     private Timer t;
/*     */ 
/*     */     
/*     */     private JavaScriptLanguageSupport.DeepestScopeVisitor visitor;
/*     */ 
/*     */     
/*     */     public Listener(RSyntaxTextArea textArea) {
/* 623 */       this.textArea = textArea;
/* 624 */       textArea.addCaretListener(this);
/* 625 */       this.t = new Timer(650, this);
/* 626 */       this.t.setRepeats(false);
/* 627 */       this.visitor = new JavaScriptLanguageSupport.DeepestScopeVisitor();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 633 */       JavaScriptParser parser = JavaScriptLanguageSupport.this.getParser(this.textArea);
/* 634 */       if (parser == null) {
/*     */         return;
/*     */       }
/* 637 */       AstRoot astRoot = parser.getAstRoot();
/*     */       
/* 639 */       if (astRoot != null) {
/* 640 */         int dot = this.textArea.getCaretPosition();
/* 641 */         this.visitor.reset(dot);
/* 642 */         astRoot.visit(this.visitor);
/* 643 */         AstNode scope = this.visitor.getDeepestScope();
/* 644 */         if (scope != null && scope != astRoot) {
/* 645 */           int start = scope.getAbsolutePosition();
/* 646 */           int end = Math.min(start + scope.getLength() - 1, this.textArea
/* 647 */               .getDocument().getLength());
/*     */           try {
/* 649 */             int startLine = this.textArea.getLineOfOffset(start);
/*     */             
/* 651 */             int endLine = (end < 0) ? this.textArea.getLineCount() : this.textArea.getLineOfOffset(end);
/* 652 */             this.textArea.setActiveLineRange(startLine, endLine);
/* 653 */           } catch (BadLocationException ble) {
/* 654 */             ble.printStackTrace();
/*     */           } 
/*     */         } else {
/*     */           
/* 658 */           this.textArea.setActiveLineRange(-1, -1);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void caretUpdate(CaretEvent e) {
/* 666 */       this.t.restart();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void uninstall() {
/* 706 */       this.textArea.removeCaretListener(this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DeepestScopeVisitor
/*     */     implements NodeVisitor {
/*     */     private int offs;
/*     */     private AstNode deepestScope;
/*     */     
/*     */     private DeepestScopeVisitor() {}
/*     */     
/*     */     private boolean containsOffs(AstNode node) {
/* 718 */       int start = node.getAbsolutePosition();
/* 719 */       return (start <= this.offs && start + node.getLength() > this.offs);
/*     */     }
/*     */     
/*     */     public AstNode getDeepestScope() {
/* 723 */       return this.deepestScope;
/*     */     }
/*     */     
/*     */     public void reset(int offs) {
/* 727 */       this.offs = offs;
/* 728 */       this.deepestScope = null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean visit(AstNode node) {
/* 734 */       switch (node.getType()) {
/*     */         case 109:
/* 736 */           if (containsOffs(node)) {
/* 737 */             this.deepestScope = node;
/* 738 */             return true;
/*     */           } 
/* 740 */           return false;
/*     */         default:
/* 742 */           return true;
/*     */ 
/*     */         
/*     */         case 129:
/*     */           break;
/*     */       } 
/*     */       
/* 749 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */