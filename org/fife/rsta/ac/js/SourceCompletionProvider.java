/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Point;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.rsta.ac.ShorthandCompletionCache;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.js.ast.CodeBlock;
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptVariableDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*     */ import org.fife.rsta.ac.js.ast.VariableResolver;
/*     */ import org.fife.rsta.ac.js.ast.jsType.JavaScriptType;
/*     */ import org.fife.rsta.ac.js.ast.jsType.JavaScriptTypesFactory;
/*     */ import org.fife.rsta.ac.js.ast.parser.JavaScriptParser;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclarationFactory;
/*     */ import org.fife.rsta.ac.js.completion.JSCompletion;
/*     */ import org.fife.rsta.ac.js.completion.JSVariableCompletion;
/*     */ import org.fife.rsta.ac.js.engine.JavaScriptEngine;
/*     */ import org.fife.rsta.ac.js.engine.JavaScriptEngineFactory;
/*     */ import org.fife.rsta.ac.js.resolver.JavaScriptResolver;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceCompletionProvider
/*     */   extends DefaultCompletionProvider
/*     */ {
/*     */   private JavaScriptCompletionProvider parent;
/*     */   private JarManager jarManager;
/*     */   private int dot;
/*     */   private JavaScriptEngine engine;
/*     */   private JavaScriptTypesFactory javaScriptTypesFactory;
/*     */   private VariableResolver variableResolver;
/*     */   private PreProcessingScripts preProcessing;
/*     */   private ShorthandCompletionCache shorthandCache;
/*     */   private boolean xmlSupported;
/*     */   private String self;
/*     */   private TypeDeclarationOptions typeDeclarationOptions;
/*     */   private String lastCompletionsAtText;
/*     */   private List<Completion> lastParameterizedCompletionsAt;
/*     */   
/*     */   public SourceCompletionProvider(boolean xmlSupported) {
/*  82 */     this((String)null, xmlSupported);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceCompletionProvider(String javaScriptEngine, boolean xmlSupported) {
/* 121 */     this.lastCompletionsAtText = null;
/* 122 */     this.lastParameterizedCompletionsAt = null;
/*     */     this.variableResolver = new VariableResolver();
/*     */     this.xmlSupported = xmlSupported;
/*     */     setParameterizedCompletionParams('(', ", ", ')');
/*     */     setAutoActivationRules(false, ".");
/*     */     this.engine = JavaScriptEngineFactory.Instance().getEngineFromCache(javaScriptEngine);
/*     */     this.javaScriptTypesFactory = this.engine.getJavaScriptTypesFactory(this);
/*     */     setSelf("JSGlobal"); } public List<Completion> getCompletionsAt(JTextComponent tc, Point p) {
/* 130 */     int offset = tc.viewToModel(p);
/* 131 */     if (offset < 0 || offset >= tc.getDocument().getLength()) {
/* 132 */       this.lastCompletionsAtText = null;
/* 133 */       return this.lastParameterizedCompletionsAt = null;
/*     */     } 
/*     */     
/* 136 */     Segment s = new Segment();
/* 137 */     Document doc = tc.getDocument();
/* 138 */     Element root = doc.getDefaultRootElement();
/* 139 */     int line = root.getElementIndex(offset);
/* 140 */     Element elem = root.getElement(line);
/* 141 */     int start = elem.getStartOffset();
/* 142 */     int end = elem.getEndOffset() - 1;
/*     */ 
/*     */     
/*     */     try {
/* 146 */       doc.getText(start, end - start, s);
/*     */ 
/*     */       
/* 149 */       int startOffs = s.offset + offset - start - 1;
/* 150 */       while (startOffs >= s.offset && Character.isLetterOrDigit(s.array[startOffs])) {
/* 151 */         startOffs--;
/*     */       }
/*     */ 
/*     */       
/* 155 */       int endOffs = s.offset + offset - start;
/* 156 */       while (endOffs < s.offset + s.count && Character.isLetterOrDigit(s.array[endOffs])) {
/* 157 */         endOffs++;
/*     */       }
/*     */       
/* 160 */       int len = endOffs - startOffs - 1;
/* 161 */       if (len <= 0) {
/* 162 */         return this.lastParameterizedCompletionsAt = null;
/*     */       }
/* 164 */       String text = new String(s.array, startOffs + 1, len);
/*     */       
/* 166 */       if (text.equals(this.lastCompletionsAtText)) {
/* 167 */         return this.lastParameterizedCompletionsAt;
/*     */       }
/*     */ 
/*     */       
/* 171 */       AstRoot ast = this.parent.getASTRoot();
/* 172 */       Set<Completion> set = new HashSet<>();
/* 173 */       CodeBlock block = iterateAstRoot(ast, set, text, tc.getCaretPosition(), this.typeDeclarationOptions);
/* 174 */       recursivelyAddLocalVars(set, block, this.dot, (String)null, false, false);
/* 175 */       this.lastCompletionsAtText = text;
/* 176 */       return this.lastParameterizedCompletionsAt = new ArrayList<>(set);
/*     */     }
/* 178 */     catch (BadLocationException ble) {
/* 179 */       ble.printStackTrace();
/*     */ 
/*     */       
/* 182 */       this.lastCompletionsAtText = null;
/* 183 */       return this.lastParameterizedCompletionsAt = null;
/*     */     } 
/*     */   }
/*     */   private void addShorthandCompletions(Set<Completion> set) {
/*     */     if (this.shorthandCache != null)
/*     */       set.addAll(this.shorthandCache.getShorthandCompletions()); 
/*     */   }
/*     */   public void setShorthandCache(ShorthandCompletionCache shorthandCache) {
/*     */     this.shorthandCache = shorthandCache;
/*     */   }
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/* 194 */     comp.setCursor(Cursor.getPredefinedCursor(3));
/*     */ 
/*     */     
/*     */     try {
/* 198 */       this.variableResolver.resetLocalVariables();
/*     */       
/* 200 */       this.completions.clear();
/*     */       
/* 202 */       this.dot = comp.getCaretPosition();
/*     */       
/* 204 */       AstRoot astRoot = this.parent.getASTRoot();
/*     */       
/* 206 */       if (astRoot == null) {
/* 207 */         return this.completions;
/*     */       }
/*     */       
/* 210 */       Set<Completion> set = new TreeSet<>();
/*     */ 
/*     */ 
/*     */       
/* 214 */       String text = getAlreadyEnteredText(comp);
/*     */       
/* 216 */       if (supportsPreProcessingScripts()) {
/* 217 */         this.variableResolver.resetPreProcessingVariables(false);
/*     */       }
/*     */       
/* 220 */       if (text == null) {
/* 221 */         return this.completions;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 227 */       boolean noDotInText = (text.indexOf('.') == -1);
/*     */ 
/*     */       
/* 230 */       CodeBlock block = iterateAstRoot(astRoot, set, text, this.dot, this.typeDeclarationOptions);
/*     */       
/* 232 */       boolean isNew = false;
/* 233 */       if (noDotInText) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 238 */         if (text.length() > 0) {
/* 239 */           addShorthandCompletions(set);
/*     */         }
/*     */ 
/*     */         
/* 243 */         if (text.length() > 0) {
/*     */           
/* 245 */           JavaScriptHelper.ParseText pt = JavaScriptHelper.parseEnteredText(text);
/* 246 */           text = pt.text;
/* 247 */           isNew = pt.isNew;
/*     */           
/* 249 */           if (isNew) {
/* 250 */             return handleNewFilter(set, text);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 255 */           loadECMAClasses(set, "");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 261 */         parseTextAndResolve(set, "this." + text);
/* 262 */         recursivelyAddLocalVars(set, block, this.dot, (String)null, false, false);
/*     */       } else {
/*     */         
/* 265 */         parseTextAndResolve(set, text);
/*     */       } 
/*     */ 
/*     */       
/* 269 */       if (noDotInText && supportsPreProcessingScripts() && !isNew) {
/* 270 */         set.addAll(this.preProcessing.getCompletions());
/*     */       }
/*     */       
/* 273 */       return resolveCompletions(text, set);
/*     */     } finally {
/*     */       
/* 276 */       comp.setCursor(Cursor.getPredefinedCursor(2));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Completion> handleNewFilter(Set<Completion> set, String text) {
/* 283 */     set.clear();
/*     */     
/* 285 */     loadECMAClasses(set, text);
/* 286 */     return resolveCompletions(text, set);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Completion> resolveCompletions(String text, Set<Completion> set) {
/* 292 */     this.completions.addAll(set);
/*     */ 
/*     */     
/* 295 */     this.completions.sort((Comparator)this.comparator);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     text = text.substring(text.lastIndexOf('.') + 1);
/*     */     
/* 302 */     int start = Collections.binarySearch(this.completions, text, (Comparator<? super String>)this.comparator);
/* 303 */     if (start < 0) {
/* 304 */       start = -(start + 1);
/*     */     }
/*     */     else {
/*     */       
/* 308 */       while (start > 0 && this.comparator
/* 309 */         .compare(this.completions.get(start - 1), text) == 0) {
/* 310 */         start--;
/*     */       }
/*     */     } 
/*     */     
/* 314 */     int end = Collections.binarySearch(this.completions, text + '{', (Comparator<? super String>)this.comparator);
/*     */     
/* 316 */     end = -(end + 1);
/*     */     
/* 318 */     return this.completions.subList(start, end);
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
/*     */   private void loadECMAClasses(Set<Completion> set, String text) {
/* 330 */     List<JavaScriptType> list = this.engine.getJavaScriptTypesFactory(this).getECMAObjectTypes(this);
/*     */     
/* 332 */     for (JavaScriptType type : list) {
/*     */       
/* 334 */       if (text.length() == 0) {
/* 335 */         if (type.getClassTypeCompletion() != null)
/* 336 */           set.add(type.getClassTypeCompletion()); 
/*     */         continue;
/*     */       } 
/* 339 */       if (type.getType().getJSName().startsWith(text)) {
/* 340 */         for (JSCompletion jsc : type.getConstructorCompletions().values()) {
/* 341 */           set.add(jsc);
/*     */         }
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
/*     */   public String getSelf() {
/* 355 */     return this.self;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseTextAndResolve(Set<Completion> set, String text) {
/* 364 */     JavaScriptResolver compiler = this.engine.getJavaScriptResolver(this);
/*     */     try {
/* 366 */       JavaScriptType type = compiler.compileText(text);
/* 367 */       boolean resolved = populateCompletionsFromType(type, set);
/* 368 */       if (!resolved) {
/* 369 */         type = compiler.compileText("this." + text);
/* 370 */         populateCompletionsFromType(type, set);
/*     */       } 
/* 372 */     } catch (IOException io) {
/*     */       
/* 374 */       io.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean populateCompletionsFromType(JavaScriptType type, Set<Completion> set) {
/* 384 */     if (type != null) {
/* 385 */       this.javaScriptTypesFactory.populateCompletionsForType(type, set);
/* 386 */       return true;
/*     */     } 
/* 388 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlreadyEnteredText(JTextComponent comp) {
/* 393 */     String text = super.getAlreadyEnteredText(comp);
/* 394 */     if (text != null) {
/* 395 */       int charIndex = JavaScriptHelper.findIndexOfFirstOpeningBracket(text);
/* 396 */       text = text.substring(charIndex, text.length());
/* 397 */       int sqIndex = JavaScriptHelper.findIndexOfFirstOpeningSquareBracket(text);
/* 398 */       text = text.substring(sqIndex).trim();
/* 399 */       if (charIndex > 0 || sqIndex > 0) {
/* 400 */         text = JavaScriptHelper.trimFromLastParam(text);
/* 401 */         Logger.log("SourceCompletionProvider:getAlreadyEnteredText()::afterTrim " + text);
/*     */       } 
/*     */     } 
/*     */     
/* 405 */     return text;
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
/*     */   protected CodeBlock iterateAstRoot(AstRoot root, Set<Completion> set, String entered, int dot, TypeDeclarationOptions options) {
/* 423 */     JavaScriptParser parser = this.engine.getParser(this, dot, options);
/* 424 */     return parser.convertAstNodeToCodeBlock(root, set, entered);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration resolveTypeDeclation(String name) {
/* 435 */     return this.variableResolver.resolveType(name, this.dot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaScriptVariableDeclaration findDeclaration(String name) {
/* 446 */     return this.variableResolver.findDeclaration(name, this.dot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaScriptVariableDeclaration findNonLocalDeclaration(String name) {
/* 456 */     return this.variableResolver.findNonLocalDeclaration(name, this.dot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration resolveTypeFromFunctionNode(AstNode functionNode) {
/* 467 */     String functionText = functionNode.toSource();
/*     */ 
/*     */     
/* 470 */     return resolveTypeDeclation(functionText);
/*     */   }
/*     */ 
/*     */   
/*     */   void setParent(JavaScriptCompletionProvider parent) {
/* 475 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJavaScriptTypesFactory(JavaScriptTypesFactory factory) {
/* 480 */     this.javaScriptTypesFactory = factory;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptTypesFactory getJavaScriptTypesFactory() {
/* 485 */     return this.javaScriptTypesFactory;
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
/*     */   protected void recursivelyAddLocalVars(Set<Completion> completions, CodeBlock block, int dot, String text, boolean findMatch, boolean isPreprocessing) {
/* 501 */     if (!block.contains(dot)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     int i;
/* 506 */     for (i = 0; i < block.getVariableDeclarationCount(); ) {
/* 507 */       JavaScriptVariableDeclaration dec = block.getVariableDeclaration(i);
/* 508 */       int decOffs = dec.getOffset();
/* 509 */       if (dot <= decOffs) {
/*     */         
/* 511 */         if (!findMatch || dec.getName().equals(text)) {
/* 512 */           JSVariableCompletion completion = new JSVariableCompletion((CompletionProvider)this, dec, !isPreprocessing);
/*     */ 
/*     */ 
/*     */           
/* 516 */           if (completions.contains(completion)) {
/* 517 */             completions.remove(completion);
/*     */           }
/* 519 */           completions.add(completion);
/*     */         } 
/*     */ 
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/*     */     
/* 527 */     for (i = 0; i < block.getChildCodeBlockCount(); i++) {
/* 528 */       CodeBlock child = block.getChildCodeBlock(i);
/* 529 */       if (child.contains(dot)) {
/* 530 */         recursivelyAddLocalVars(completions, child, dot, text, findMatch, isPreprocessing);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidChar(char ch) {
/* 539 */     return (Character.isJavaIdentifierPart(ch) || ch == ',' || ch == '.' || ch == 
/* 540 */       getParameterListStart() || ch == getParameterListEnd() || ch == ' ' || ch == '"' || ch == '[' || ch == ']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJarManager(JarManager jarManager) {
/* 551 */     this.jarManager = jarManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public JarManager getJarManager() {
/* 556 */     return this.jarManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public VariableResolver getVariableResolver() {
/* 561 */     return this.variableResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptLanguageSupport getLanguageSupport() {
/* 566 */     return this.parent.getLanguageSupport();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPreProcessingScripts(PreProcessingScripts preProcessing) {
/* 571 */     this.preProcessing = preProcessing;
/*     */   }
/*     */ 
/*     */   
/*     */   public PreProcessingScripts getPreProcessingScripts() {
/* 576 */     return this.preProcessing;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean supportsPreProcessingScripts() {
/* 581 */     return (this.preProcessing != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptEngine getJavaScriptEngine() {
/* 586 */     return this.engine;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJavaScriptEngine(JavaScriptEngine engine) {
/* 591 */     this.engine = engine;
/*     */   }
/*     */   
/*     */   public SourceLocation getSourceLocForClass(String className) {
/* 595 */     return this.jarManager.getSourceLocForClass(className);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isXMLSupported() {
/* 601 */     return this.xmlSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXMLSupported(boolean xmlSupported) {
/* 606 */     this.xmlSupported = xmlSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelf(String self) {
/* 612 */     this.self = self;
/*     */   }
/*     */ 
/*     */   
/*     */   public void parseDocument(int dot) {
/* 617 */     AstRoot ast = this.parent.getASTRoot();
/* 618 */     Set<Completion> set = new HashSet<>();
/* 619 */     this.variableResolver.resetLocalVariables();
/* 620 */     iterateAstRoot(ast, set, "", dot, this.typeDeclarationOptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDeclarationFactory getTypesFactory() {
/* 625 */     return this.engine.getTypesFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeDeclationOptions(TypeDeclarationOptions typeDeclarationOptions) {
/* 633 */     this.typeDeclarationOptions = typeDeclarationOptions;
/*     */   }
/*     */ 
/*     */   
/*     */   public void debugCodeBlock(CodeBlock block, int tab) {
/* 638 */     System.out.println();
/* 639 */     tab++;
/* 640 */     if (block != null) {
/* 641 */       for (int j = 0; j < tab; j++) {
/* 642 */         System.out.print("\t");
/*     */       }
/* 644 */       System.out.print("Start: " + block.getStartOffset() + " end:" + block
/* 645 */           .getEndOffset());
/* 646 */       for (int ii = 0; ii < block.getVariableDeclarationCount(); ii++) {
/*     */         
/* 648 */         JavaScriptVariableDeclaration vd = block.getVariableDeclaration(ii);
/* 649 */         System.out.print(" " + vd.getName() + " ");
/*     */       } 
/* 651 */       for (int i = 0; i < block.getChildCodeBlockCount(); i++)
/* 652 */         debugCodeBlock(block.getChildCodeBlock(i), tab); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\SourceCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */