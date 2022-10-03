/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.AbstractLanguageSupport;
/*     */ import org.fife.rsta.ac.GoToMemberAction;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.ImportDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Package;
/*     */ import org.fife.rsta.ac.java.tree.JavaOutlineTree;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
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
/*     */ public class JavaLanguageSupport
/*     */   extends AbstractLanguageSupport
/*     */ {
/*     */   private Map<JavaParser, Info> parserToInfoMap;
/*     */   private JarManager jarManager;
/*     */   private static final String PROPERTY_LISTENER = "org.fife.rsta.ac.java.JavaLanguageSupport.Listener";
/*     */   
/*     */   public JavaLanguageSupport() {
/*  76 */     this.parserToInfoMap = new HashMap<>();
/*  77 */     this.jarManager = new JarManager();
/*  78 */     setAutoActivationEnabled(true);
/*  79 */     setParameterAssistanceEnabled(true);
/*  80 */     setShowDescWindow(true);
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
/*     */   public JavaCompletionProvider getCompletionProvider(RSyntaxTextArea textArea) {
/*  95 */     AutoCompletion ac = getAutoCompletionFor(textArea);
/*  96 */     return (JavaCompletionProvider)ac.getCompletionProvider();
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
/*     */   public JarManager getJarManager() {
/* 108 */     return this.jarManager;
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
/*     */   public JavaParser getParser(RSyntaxTextArea textArea) {
/* 122 */     Object parser = textArea.getClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser");
/* 123 */     if (parser instanceof JavaParser) {
/* 124 */       return (JavaParser)parser;
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(RSyntaxTextArea textArea) {
/* 136 */     JavaCompletionProvider p = new JavaCompletionProvider(this.jarManager);
/*     */     
/* 138 */     AutoCompletion ac = new JavaAutoCompletion(p, textArea);
/* 139 */     ac.setListCellRenderer(new JavaCellRenderer());
/* 140 */     ac.setAutoCompleteEnabled(isAutoCompleteEnabled());
/* 141 */     ac.setAutoActivationEnabled(isAutoActivationEnabled());
/* 142 */     ac.setAutoActivationDelay(getAutoActivationDelay());
/* 143 */     ac.setExternalURLHandler(new JavadocUrlHandler());
/* 144 */     ac.setParameterAssistanceEnabled(isParameterAssistanceEnabled());
/* 145 */     ac.setParamChoicesRenderer(new JavaParamListCellRenderer());
/* 146 */     ac.setShowDescWindow(getShowDescWindow());
/* 147 */     ac.install((JTextComponent)textArea);
/* 148 */     installImpl(textArea, ac);
/*     */     
/* 150 */     textArea.setToolTipSupplier((ToolTipSupplier)p);
/*     */     
/* 152 */     Listener listener = new Listener(textArea);
/* 153 */     textArea.putClientProperty("org.fife.rsta.ac.java.JavaLanguageSupport.Listener", listener);
/*     */     
/* 155 */     JavaParser parser = new JavaParser(textArea);
/* 156 */     textArea.putClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser", parser);
/* 157 */     textArea.addParser((Parser)parser);
/* 158 */     textArea.setToolTipSupplier((ToolTipSupplier)p);
/*     */     
/* 160 */     Info info = new Info(textArea, p, parser);
/* 161 */     this.parserToInfoMap.put(parser, info);
/*     */     
/* 163 */     installKeyboardShortcuts(textArea);
/*     */     
/* 165 */     textArea.setLinkGenerator(new JavaLinkGenerator(this));
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
/*     */   private void installKeyboardShortcuts(RSyntaxTextArea textArea) {
/* 177 */     InputMap im = textArea.getInputMap();
/* 178 */     ActionMap am = textArea.getActionMap();
/* 179 */     int c = textArea.getToolkit().getMenuShortcutKeyMask();
/* 180 */     int shift = 64;
/*     */     
/* 182 */     im.put(KeyStroke.getKeyStroke(79, c | shift), "GoToType");
/* 183 */     am.put("GoToType", (Action)new GoToMemberAction(JavaOutlineTree.class));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall(RSyntaxTextArea textArea) {
/* 194 */     uninstallImpl(textArea);
/*     */     
/* 196 */     JavaParser parser = getParser(textArea);
/* 197 */     Info info = this.parserToInfoMap.remove(parser);
/* 198 */     if (info != null) {
/* 199 */       parser.removePropertyChangeListener("CompilationUnit", info);
/*     */     }
/*     */     
/* 202 */     textArea.removeParser((Parser)parser);
/* 203 */     textArea.putClientProperty("org.fife.rsta.ac.LanguageSupport.LanguageParser", null);
/* 204 */     textArea.setToolTipSupplier(null);
/*     */     
/* 206 */     Object listener = textArea.getClientProperty("org.fife.rsta.ac.java.JavaLanguageSupport.Listener");
/* 207 */     if (listener instanceof Listener) {
/* 208 */       ((Listener)listener).uninstall();
/* 209 */       textArea.putClientProperty("org.fife.rsta.ac.java.JavaLanguageSupport.Listener", null);
/*     */     } 
/*     */     
/* 212 */     uninstallKeyboardShortcuts(textArea);
/* 213 */     textArea.setLinkGenerator(null);
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
/* 225 */     InputMap im = textArea.getInputMap();
/* 226 */     ActionMap am = textArea.getActionMap();
/* 227 */     int c = textArea.getToolkit().getMenuShortcutKeyMask();
/* 228 */     int shift = 64;
/*     */     
/* 230 */     im.remove(KeyStroke.getKeyStroke(79, c | shift));
/* 231 */     am.remove("GoToType");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ImportToAddInfo
/*     */   {
/*     */     public int offs;
/*     */ 
/*     */     
/*     */     public String text;
/*     */ 
/*     */ 
/*     */     
/*     */     public ImportToAddInfo(int offset, String text) {
/* 247 */       this.offs = offset;
/* 248 */       this.text = text;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Info
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public JavaCompletionProvider provider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Info(RSyntaxTextArea textArea, JavaCompletionProvider provider, JavaParser parser) {
/* 267 */       this.provider = provider;
/*     */       
/* 269 */       parser.addPropertyChangeListener("CompilationUnit", this);
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
/* 281 */       String name = e.getPropertyName();
/*     */       
/* 283 */       if ("CompilationUnit".equals(name)) {
/* 284 */         CompilationUnit cu = (CompilationUnit)e.getNewValue();
/*     */ 
/*     */         
/* 287 */         this.provider.setCompilationUnit(cu);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class JavaAutoCompletion
/*     */     extends AutoCompletion
/*     */   {
/*     */     private RSyntaxTextArea textArea;
/*     */ 
/*     */     
/*     */     private String replacementTextPrefix;
/*     */ 
/*     */ 
/*     */     
/*     */     public JavaAutoCompletion(JavaCompletionProvider provider, RSyntaxTextArea textArea) {
/* 306 */       super((CompletionProvider)provider);
/* 307 */       this.textArea = textArea;
/*     */     }
/*     */ 
/*     */     
/*     */     private String getCurrentLineText() {
/* 312 */       int caretPosition = this.textArea.getCaretPosition();
/* 313 */       Element root = this.textArea.getDocument().getDefaultRootElement();
/* 314 */       int line = root.getElementIndex(caretPosition);
/* 315 */       Element elem = root.getElement(line);
/* 316 */       int endOffset = elem.getEndOffset();
/* 317 */       int lineStart = elem.getStartOffset();
/*     */       
/* 319 */       String text = "";
/*     */       try {
/* 321 */         text = this.textArea.getText(lineStart, endOffset - lineStart).trim();
/* 322 */       } catch (BadLocationException e) {
/* 323 */         e.printStackTrace();
/*     */       } 
/*     */       
/* 326 */       return text;
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
/*     */     protected String getReplacementText(Completion c, Document doc, int start, int len) {
/* 341 */       String text = super.getReplacementText(c, doc, start, len);
/* 342 */       if (this.replacementTextPrefix != null) {
/* 343 */         text = this.replacementTextPrefix + text;
/* 344 */         this.replacementTextPrefix = null;
/*     */       } 
/* 346 */       return text;
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
/*     */     private JavaLanguageSupport.ImportToAddInfo getShouldAddImport(ClassCompletion cc) {
/* 365 */       String text = getCurrentLineText();
/*     */ 
/*     */       
/* 368 */       if (!text.startsWith("import ")) {
/*     */ 
/*     */         
/* 371 */         JavaCompletionProvider provider = (JavaCompletionProvider)getCompletionProvider();
/* 372 */         CompilationUnit cu = provider.getCompilationUnit();
/* 373 */         int offset = 0;
/* 374 */         boolean alreadyImported = false;
/*     */ 
/*     */         
/* 377 */         if (cu == null) {
/* 378 */           return null;
/*     */         }
/* 380 */         if ("java.lang".equals(cc.getPackageName()))
/*     */         {
/* 382 */           return null;
/*     */         }
/*     */         
/* 385 */         String className = cc.getClassName(false);
/* 386 */         String fqClassName = cc.getClassName(true);
/*     */ 
/*     */ 
/*     */         
/* 390 */         int lastClassNameDot = fqClassName.lastIndexOf('.');
/* 391 */         boolean ccInPackage = (lastClassNameDot > -1);
/* 392 */         Package pkg = cu.getPackage();
/* 393 */         if (ccInPackage && pkg != null) {
/* 394 */           String ccPkg = fqClassName.substring(0, lastClassNameDot);
/* 395 */           String pkgName = pkg.getName();
/* 396 */           if (ccPkg.equals(pkgName)) {
/* 397 */             return null;
/*     */           }
/*     */         }
/* 400 */         else if (!ccInPackage && pkg == null) {
/* 401 */           return null;
/*     */         } 
/*     */ 
/*     */         
/* 405 */         Iterator<ImportDeclaration> i = cu.getImportIterator();
/* 406 */         while (i.hasNext()) {
/*     */           
/* 408 */           ImportDeclaration id = i.next();
/* 409 */           offset = id.getNameEndOffset() + 1;
/*     */ 
/*     */           
/* 412 */           if (id.isStatic()) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 417 */           if (id.isWildcard()) {
/*     */             
/* 419 */             if (lastClassNameDot > -1) {
/* 420 */               String imported = id.getName();
/* 421 */               int j = imported.lastIndexOf('.');
/* 422 */               String importedPkg = imported.substring(0, j);
/* 423 */               String classPkg = fqClassName.substring(0, lastClassNameDot);
/* 424 */               if (importedPkg.equals(classPkg)) {
/* 425 */                 alreadyImported = true;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 434 */           String fullyImportedClassName = id.getName();
/* 435 */           int dot = fullyImportedClassName.lastIndexOf('.');
/*     */           
/* 437 */           String importedClassName = fullyImportedClassName.substring(dot + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 445 */           if (className.equals(importedClassName)) {
/* 446 */             offset = -1;
/* 447 */             if (fqClassName.equals(fullyImportedClassName)) {
/* 448 */               alreadyImported = true;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 459 */         if (!alreadyImported) {
/*     */           
/* 461 */           StringBuilder importToAdd = new StringBuilder();
/*     */ 
/*     */ 
/*     */           
/* 465 */           if (offset == 0 && 
/* 466 */             pkg != null) {
/* 467 */             offset = pkg.getNameEndOffset() + 1;
/*     */             
/* 469 */             importToAdd.append('\n');
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 475 */           if (offset > -1) {
/*     */             
/* 477 */             if (offset > 0) {
/* 478 */               importToAdd.append("\nimport ").append(fqClassName).append(';');
/*     */             } else {
/*     */               
/* 481 */               importToAdd.append("import ").append(fqClassName).append(";\n");
/*     */             } 
/*     */ 
/*     */             
/* 485 */             return new JavaLanguageSupport.ImportToAddInfo(offset, importToAdd.toString());
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 494 */           int dot = fqClassName.lastIndexOf('.');
/* 495 */           if (dot > -1) {
/* 496 */             String pkgName = fqClassName.substring(0, dot + 1);
/* 497 */             this.replacementTextPrefix = pkgName;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 505 */       return null;
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
/*     */     protected void insertCompletion(Completion c, boolean typedParamListStartChar) {
/* 518 */       JavaLanguageSupport.ImportToAddInfo importInfo = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 526 */       if (c instanceof ClassCompletion) {
/* 527 */         importInfo = getShouldAddImport((ClassCompletion)c);
/* 528 */         if (importInfo != null) {
/* 529 */           this.textArea.beginAtomicEdit();
/*     */         }
/*     */       } 
/*     */       
/*     */       try {
/* 534 */         super.insertCompletion(c, typedParamListStartChar);
/* 535 */         if (importInfo != null) {
/* 536 */           this.textArea.insert(importInfo.text, importInfo.offs);
/*     */         }
/*     */       } finally {
/*     */         
/* 540 */         this.textArea.endAtomicEdit();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected int refreshPopupWindow() {
/* 548 */       JavaParser parser = JavaLanguageSupport.this.getParser(this.textArea);
/* 549 */       RSyntaxDocument doc = (RSyntaxDocument)this.textArea.getDocument();
/* 550 */       String style = this.textArea.getSyntaxEditingStyle();
/* 551 */       parser.parse(doc, style);
/* 552 */       return super.refreshPopupWindow();
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
/*     */     public Listener(RSyntaxTextArea textArea) {
/* 568 */       this.textArea = textArea;
/* 569 */       textArea.addCaretListener(this);
/* 570 */       this.t = new Timer(650, this);
/* 571 */       this.t.setRepeats(false);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 577 */       JavaParser parser = JavaLanguageSupport.this.getParser(this.textArea);
/* 578 */       if (parser == null) {
/*     */         return;
/*     */       }
/* 581 */       CompilationUnit cu = parser.getCompilationUnit();
/*     */ 
/*     */ 
/*     */       
/* 585 */       if (cu != null) {
/* 586 */         int dot = this.textArea.getCaretPosition();
/* 587 */         Point p = cu.getEnclosingMethodRange(dot);
/* 588 */         if (p != null) {
/*     */           try {
/* 590 */             int startLine = this.textArea.getLineOfOffset(p.x);
/*     */             
/* 592 */             int endOffs = Math.min(p.y, this.textArea
/* 593 */                 .getDocument().getLength());
/* 594 */             int endLine = this.textArea.getLineOfOffset(endOffs);
/* 595 */             this.textArea.setActiveLineRange(startLine, endLine);
/* 596 */           } catch (BadLocationException ble) {
/* 597 */             ble.printStackTrace();
/*     */           } 
/*     */         } else {
/*     */           
/* 601 */           this.textArea.setActiveLineRange(-1, -1);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void caretUpdate(CaretEvent e) {
/* 609 */       this.t.restart();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void uninstall() {
/* 617 */       this.textArea.removeCaretListener(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */