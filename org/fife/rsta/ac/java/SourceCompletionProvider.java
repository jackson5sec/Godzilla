/*      */ package org.fife.rsta.ac.java;
/*      */ 
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Point;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import org.fife.rsta.ac.ShorthandCompletionCache;
/*      */ import org.fife.rsta.ac.java.buildpath.LibraryInfo;
/*      */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*      */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*      */ import org.fife.rsta.ac.java.classreader.FieldInfo;
/*      */ import org.fife.rsta.ac.java.classreader.MemberInfo;
/*      */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*      */ import org.fife.rsta.ac.java.classreader.Util;
/*      */ import org.fife.rsta.ac.java.rjc.ast.CodeBlock;
/*      */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*      */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*      */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*      */ import org.fife.rsta.ac.java.rjc.ast.ImportDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.ast.LocalVariable;
/*      */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*      */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*      */ import org.fife.rsta.ac.java.rjc.ast.NormalClassDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*      */ import org.fife.rsta.ac.java.rjc.lang.TypeArgument;
/*      */ import org.fife.rsta.ac.java.rjc.lang.TypeParameter;
/*      */ import org.fife.ui.autocomplete.Completion;
/*      */ import org.fife.ui.autocomplete.CompletionProvider;
/*      */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*      */ import org.fife.ui.rsyntaxtextarea.Token;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class SourceCompletionProvider
/*      */   extends DefaultCompletionProvider
/*      */ {
/*      */   private JavaCompletionProvider javaProvider;
/*      */   private JarManager jarManager;
/*      */   private static final String JAVA_LANG_PACKAGE = "java.lang.*";
/*      */   private static final String THIS = "this";
/*      */   private ShorthandCompletionCache shorthandCache;
/*      */   
/*      */   public SourceCompletionProvider() {
/*   92 */     this((JarManager)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SourceCompletionProvider(JarManager jarManager) {
/*  102 */     if (jarManager == null) {
/*  103 */       jarManager = new JarManager();
/*      */     }
/*  105 */     this.jarManager = jarManager;
/*  106 */     setParameterizedCompletionParams('(', ", ", ')');
/*  107 */     setAutoActivationRules(false, ".");
/*  108 */     setParameterChoicesProvider(new SourceParamChoicesProvider());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCompletionsForStaticMembers(Set<Completion> set, CompilationUnit cu, ClassFile cf, String pkg) {
/*  117 */     int methodCount = cf.getMethodCount();
/*  118 */     for (int i = 0; i < methodCount; i++) {
/*  119 */       MethodInfo info = cf.getMethodInfo(i);
/*  120 */       if (isAccessible((MemberInfo)info, pkg) && info.isStatic()) {
/*  121 */         MethodCompletion mc = new MethodCompletion((CompletionProvider)this, info);
/*  122 */         set.add(mc);
/*      */       } 
/*      */     } 
/*      */     
/*  126 */     int fieldCount = cf.getFieldCount();
/*  127 */     for (int j = 0; j < fieldCount; j++) {
/*  128 */       FieldInfo info = cf.getFieldInfo(j);
/*  129 */       if (isAccessible((MemberInfo)info, pkg) && info.isStatic()) {
/*  130 */         FieldCompletion fc = new FieldCompletion((CompletionProvider)this, info);
/*  131 */         set.add(fc);
/*      */       } 
/*      */     } 
/*      */     
/*  135 */     ClassFile superClass = getClassFileFor(cu, cf.getSuperClassName(true));
/*  136 */     if (superClass != null) {
/*  137 */       addCompletionsForStaticMembers(set, cu, superClass, pkg);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCompletionsForExtendedClass(Set<Completion> set, CompilationUnit cu, ClassFile cf, String pkg, Map<String, String> typeParamMap) {
/*  164 */     cf.setTypeParamsToTypeArgs(typeParamMap);
/*      */ 
/*      */ 
/*      */     
/*  168 */     int methodCount = cf.getMethodCount();
/*  169 */     for (int i = 0; i < methodCount; i++) {
/*  170 */       MethodInfo info = cf.getMethodInfo(i);
/*      */       
/*  172 */       if (isAccessible((MemberInfo)info, pkg) && !info.isConstructor()) {
/*  173 */         MethodCompletion mc = new MethodCompletion((CompletionProvider)this, info);
/*  174 */         set.add(mc);
/*      */       } 
/*      */     } 
/*      */     
/*  178 */     int fieldCount = cf.getFieldCount();
/*  179 */     for (int j = 0; j < fieldCount; j++) {
/*  180 */       FieldInfo info = cf.getFieldInfo(j);
/*  181 */       if (isAccessible((MemberInfo)info, pkg)) {
/*  182 */         FieldCompletion fc = new FieldCompletion((CompletionProvider)this, info);
/*  183 */         set.add(fc);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  188 */     ClassFile superClass = getClassFileFor(cu, cf.getSuperClassName(true));
/*  189 */     if (superClass != null) {
/*  190 */       addCompletionsForExtendedClass(set, cu, superClass, pkg, typeParamMap);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  196 */     for (int k = 0; k < cf.getImplementedInterfaceCount(); k++) {
/*  197 */       String inter = cf.getImplementedInterfaceName(k, true);
/*  198 */       cf = getClassFileFor(cu, inter);
/*  199 */       addCompletionsForExtendedClass(set, cu, cf, pkg, typeParamMap);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCompletionsForLocalVarsMethods(CompilationUnit cu, LocalVariable var, Set<Completion> retVal) {
/*  216 */     Type type = var.getType();
/*  217 */     String pkg = cu.getPackageName();
/*      */     
/*  219 */     if (type.isArray()) {
/*  220 */       ClassFile cf = getClassFileFor(cu, "java.lang.Object");
/*  221 */       addCompletionsForExtendedClass(retVal, cu, cf, pkg, (Map<String, String>)null);
/*      */       
/*  223 */       FieldCompletion fc = FieldCompletion.createLengthCompletion((CompletionProvider)this, type);
/*  224 */       retVal.add(fc);
/*      */     
/*      */     }
/*  227 */     else if (!type.isBasicType()) {
/*  228 */       String typeStr = type.getName(true, false);
/*  229 */       ClassFile cf = getClassFileFor(cu, typeStr);
/*  230 */       if (cf != null) {
/*  231 */         Map<String, String> typeParamMap = createTypeParamMap(type, cf);
/*  232 */         addCompletionsForExtendedClass(retVal, cu, cf, pkg, typeParamMap);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addShorthandCompletions(Set<Completion> set) {
/*  245 */     if (this.shorthandCache != null) {
/*  246 */       set.addAll(this.shorthandCache.getShorthandCompletions());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShorthandCache(ShorthandCompletionCache shorthandCache) {
/*  256 */     this.shorthandCache = shorthandCache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassFile getClassFileFor(CompilationUnit cu, String className) {
/*  272 */     if (className == null) {
/*  273 */       return null;
/*      */     }
/*      */     
/*  276 */     ClassFile superClass = null;
/*      */ 
/*      */     
/*  279 */     if (!Util.isFullyQualified(className)) {
/*      */ 
/*      */       
/*  282 */       String pkg = cu.getPackageName();
/*  283 */       if (pkg != null) {
/*  284 */         String temp = pkg + "." + className;
/*  285 */         superClass = this.jarManager.getClassEntry(temp);
/*      */       } 
/*      */ 
/*      */       
/*  289 */       if (superClass == null) {
/*  290 */         Iterator<ImportDeclaration> i = cu.getImportIterator();
/*  291 */         while (i.hasNext()) {
/*  292 */           ImportDeclaration id = i.next();
/*  293 */           String imported = id.getName();
/*  294 */           if (imported.endsWith(".*")) {
/*  295 */             String temp = imported.substring(0, imported
/*  296 */                 .length() - 1) + className;
/*  297 */             superClass = this.jarManager.getClassEntry(temp);
/*  298 */             if (superClass != null)
/*      */               break; 
/*      */             continue;
/*      */           } 
/*  302 */           if (imported.endsWith("." + className)) {
/*  303 */             superClass = this.jarManager.getClassEntry(imported);
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  310 */       if (superClass == null) {
/*  311 */         String temp = "java.lang." + className;
/*  312 */         superClass = this.jarManager.getClassEntry(temp);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  318 */       superClass = this.jarManager.getClassEntry(className);
/*      */     } 
/*      */     
/*  321 */     return superClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addLocalVarCompletions(Set<Completion> set, Method method, int offs) {
/*  337 */     for (int i = 0; i < method.getParameterCount(); i++) {
/*  338 */       FormalParameter param = method.getParameter(i);
/*  339 */       set.add(new LocalVariableCompletion((CompletionProvider)this, (LocalVariable)param));
/*      */     } 
/*      */     
/*  342 */     CodeBlock body = method.getBody();
/*  343 */     if (body != null) {
/*  344 */       addLocalVarCompletions(set, body, offs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addLocalVarCompletions(Set<Completion> set, CodeBlock block, int offs) {
/*      */     int i;
/*  361 */     for (i = 0; i < block.getLocalVarCount(); ) {
/*  362 */       LocalVariable var = block.getLocalVar(i);
/*  363 */       if (var.getNameEndOffset() <= offs) {
/*  364 */         set.add(new LocalVariableCompletion((CompletionProvider)this, var));
/*      */ 
/*      */         
/*      */         i++;
/*      */       } 
/*      */     } 
/*      */     
/*  371 */     for (i = 0; i < block.getChildBlockCount(); i++) {
/*  372 */       CodeBlock child = block.getChildBlock(i);
/*  373 */       if (child.containsOffset(offs)) {
/*  374 */         addLocalVarCompletions(set, child, offs);
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/*  379 */       if (child.getNameStartOffset() > offs) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addJar(LibraryInfo info) throws IOException {
/*  400 */     this.jarManager.addClassFileSource(info);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkStringLiteralMember(JTextComponent comp, String alreadyEntered, CompilationUnit cu, Set<Completion> set) {
/*  419 */     boolean stringLiteralMember = false;
/*      */     
/*  421 */     int offs = comp.getCaretPosition() - alreadyEntered.length() - 1;
/*  422 */     if (offs > 1) {
/*  423 */       RSyntaxTextArea textArea = (RSyntaxTextArea)comp;
/*  424 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*      */       
/*      */       try {
/*  427 */         if (doc.charAt(offs) == '"' && doc.charAt(offs + 1) == '.') {
/*  428 */           int curLine = textArea.getLineOfOffset(offs);
/*  429 */           Token list = textArea.getTokenListForLine(curLine);
/*  430 */           Token prevToken = RSyntaxUtilities.getTokenAtOffset(list, offs);
/*  431 */           if (prevToken != null && prevToken
/*  432 */             .getType() == 13) {
/*  433 */             ClassFile cf = getClassFileFor(cu, "java.lang.String");
/*  434 */             addCompletionsForExtendedClass(set, cu, cf, cu
/*  435 */                 .getPackageName(), (Map<String, String>)null);
/*  436 */             stringLiteralMember = true;
/*      */           } else {
/*      */             
/*  439 */             System.out.println(prevToken);
/*      */           } 
/*      */         } 
/*  442 */       } catch (BadLocationException ble) {
/*  443 */         ble.printStackTrace();
/*      */       } 
/*      */     } 
/*      */     
/*  447 */     return stringLiteralMember;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearJars() {
/*  460 */     this.jarManager.clearClassFileSources();
/*      */ 
/*      */ 
/*      */     
/*  464 */     clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, String> createTypeParamMap(Type type, ClassFile cf) {
/*  480 */     Map<String, String> typeParamMap = null;
/*  481 */     List<TypeArgument> typeArgs = type.getTypeArguments(type.getIdentifierCount() - 1);
/*  482 */     if (typeArgs != null) {
/*  483 */       typeParamMap = new HashMap<>();
/*  484 */       List<String> paramTypes = cf.getParamTypes();
/*      */ 
/*      */       
/*  487 */       int min = Math.min((paramTypes == null) ? 0 : paramTypes.size(), typeArgs
/*  488 */           .size());
/*  489 */       for (int i = 0; i < min; i++) {
/*  490 */         TypeArgument typeArg = typeArgs.get(i);
/*  491 */         typeParamMap.put(paramTypes.get(i), typeArg.toString());
/*      */       } 
/*      */     } 
/*  494 */     return typeParamMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Completion> getCompletionsAt(JTextComponent tc, Point p) {
/*  503 */     getCompletionsImpl(tc);
/*  504 */     return super.getCompletionsAt(tc, p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/*  514 */     comp.setCursor(Cursor.getPredefinedCursor(3));
/*      */ 
/*      */     
/*      */     try {
/*  518 */       this.completions = new ArrayList();
/*      */       
/*  520 */       CompilationUnit cu = this.javaProvider.getCompilationUnit();
/*  521 */       if (cu == null) {
/*  522 */         return this.completions;
/*      */       }
/*      */       
/*  525 */       Set<Completion> set = new TreeSet<>();
/*      */ 
/*      */ 
/*      */       
/*  529 */       String text = getAlreadyEnteredText(comp);
/*      */ 
/*      */       
/*  532 */       boolean stringLiteralMember = checkStringLiteralMember(comp, text, cu, set);
/*      */ 
/*      */ 
/*      */       
/*  536 */       if (!stringLiteralMember) {
/*      */ 
/*      */ 
/*      */         
/*  540 */         if (text.indexOf('.') == -1) {
/*  541 */           addShorthandCompletions(set);
/*      */         }
/*      */         
/*  544 */         loadImportCompletions(set, text, cu);
/*      */ 
/*      */ 
/*      */         
/*  548 */         this.jarManager.addCompletions((CompletionProvider)this, text, set);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  554 */         loadCompletionsForCaretPosition(cu, comp, text, set);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  559 */       this.completions = new ArrayList<>(set);
/*  560 */       Collections.sort(this.completions);
/*      */ 
/*      */ 
/*      */       
/*  564 */       text = text.substring(text.lastIndexOf('.') + 1);
/*      */ 
/*      */       
/*  567 */       int start = Collections.binarySearch(this.completions, text, (Comparator<? super String>)this.comparator);
/*  568 */       if (start < 0) {
/*  569 */         start = -(start + 1);
/*      */       }
/*      */       else {
/*      */         
/*  573 */         while (start > 0 && this.comparator
/*  574 */           .compare(this.completions.get(start - 1), text) == 0) {
/*  575 */           start--;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  580 */       int end = Collections.binarySearch(this.completions, text + '{', (Comparator<? super String>)this.comparator);
/*  581 */       end = -(end + 1);
/*      */       
/*  583 */       return this.completions.subList(start, end);
/*      */     } finally {
/*      */       
/*  586 */       comp.setCursor(Cursor.getPredefinedCursor(2));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> getJars() {
/*  603 */     return this.jarManager.getClassFileSources();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SourceLocation getSourceLocForClass(String className) {
/*  609 */     return this.jarManager.getSourceLocForClass(className);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isAccessible(MemberInfo info, String pkg) {
/*  622 */     boolean accessible = false;
/*  623 */     int access = info.getAccessFlags();
/*      */     
/*  625 */     if (Util.isPublic(access) || 
/*  626 */       Util.isProtected(access)) {
/*  627 */       accessible = true;
/*      */     }
/*  629 */     else if (Util.isDefault(access)) {
/*  630 */       String pkg2 = info.getClassFile().getPackageName();
/*      */       
/*  632 */       accessible = ((pkg == null && pkg2 == null) || (pkg != null && pkg.equals(pkg2)));
/*      */     } 
/*      */     
/*  635 */     return accessible;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isValidChar(char ch) {
/*  645 */     return (Character.isJavaIdentifierPart(ch) || ch == '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadCompletionsForCaretPosition(CompilationUnit cu, JTextComponent comp, String alreadyEntered, Set<Completion> retVal) {
/*  673 */     int caret = comp.getCaretPosition();
/*      */ 
/*      */ 
/*      */     
/*  677 */     int lastDot = alreadyEntered.lastIndexOf('.');
/*  678 */     boolean qualified = (lastDot > -1);
/*  679 */     String prefix = qualified ? alreadyEntered.substring(0, lastDot) : null;
/*      */     
/*  681 */     Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/*  682 */     while (i.hasNext()) {
/*      */       
/*  684 */       TypeDeclaration td = i.next();
/*  685 */       int start = td.getBodyStartOffset();
/*  686 */       int end = td.getBodyEndOffset();
/*      */       
/*  688 */       if (caret > start && caret <= end) {
/*  689 */         loadCompletionsForCaretPosition(cu, comp, alreadyEntered, retVal, td, prefix, caret);
/*      */         
/*      */         continue;
/*      */       } 
/*  693 */       if (caret < start) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadCompletionsForCaretPosition(CompilationUnit cu, JTextComponent comp, String alreadyEntered, Set<Completion> retVal, TypeDeclaration td, String prefix, int caret) {
/*  731 */     for (int i = 0; i < td.getChildTypeCount(); i++) {
/*  732 */       TypeDeclaration childType = td.getChildType(i);
/*  733 */       loadCompletionsForCaretPosition(cu, comp, alreadyEntered, retVal, childType, prefix, caret);
/*      */     } 
/*      */ 
/*      */     
/*  737 */     Method currentMethod = null;
/*      */     
/*  739 */     Map<String, String> typeParamMap = new HashMap<>();
/*  740 */     if (td instanceof NormalClassDeclaration) {
/*  741 */       NormalClassDeclaration ncd = (NormalClassDeclaration)td;
/*  742 */       List<TypeParameter> typeParams = ncd.getTypeParameters();
/*  743 */       if (typeParams != null) {
/*  744 */         for (TypeParameter typeParam : typeParams) {
/*  745 */           String typeVar = typeParam.getName();
/*      */           
/*  747 */           typeParamMap.put(typeVar, typeVar);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  755 */     String pkg = cu.getPackageName();
/*  756 */     Iterator<Member> j = td.getMemberIterator();
/*  757 */     while (j.hasNext()) {
/*  758 */       Member m = j.next();
/*  759 */       if (m instanceof Method) {
/*  760 */         Method method = (Method)m;
/*  761 */         if (prefix == null || "this".equals(prefix)) {
/*  762 */           retVal.add(new MethodCompletion((CompletionProvider)this, method));
/*      */         }
/*  764 */         if (caret >= method.getBodyStartOffset() && caret < method.getBodyEndOffset()) {
/*  765 */           currentMethod = method;
/*      */ 
/*      */           
/*  768 */           if (prefix == null)
/*  769 */             addLocalVarCompletions(retVal, method, caret); 
/*      */         } 
/*      */         continue;
/*      */       } 
/*  773 */       if (m instanceof Field && (
/*  774 */         prefix == null || "this".equals(prefix))) {
/*  775 */         Field field = (Field)m;
/*  776 */         retVal.add(new FieldCompletion((CompletionProvider)this, field));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  783 */     if ((prefix == null || "this".equals(prefix)) && 
/*  784 */       td instanceof NormalClassDeclaration) {
/*  785 */       NormalClassDeclaration ncd = (NormalClassDeclaration)td;
/*  786 */       Type extended = ncd.getExtendedType();
/*  787 */       if (extended != null) {
/*  788 */         String superClassName = extended.toString();
/*  789 */         ClassFile cf = getClassFileFor(cu, superClassName);
/*  790 */         if (cf != null) {
/*  791 */           addCompletionsForExtendedClass(retVal, cu, cf, pkg, (Map<String, String>)null);
/*      */         } else {
/*      */           
/*  794 */           System.out.println("[DEBUG]: Couldn't find ClassFile for: " + superClassName);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  802 */     if (prefix != null && !"this".equals(prefix)) {
/*  803 */       loadCompletionsForCaretPositionQualified(cu, alreadyEntered, retVal, td, currentMethod, prefix, caret);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadCompletionsForCaretPositionQualified(CompilationUnit cu, String alreadyEntered, Set<Completion> retVal, TypeDeclaration td, Method currentMethod, String prefix, int offs) {
/*  832 */     int dot = prefix.indexOf('.');
/*  833 */     if (dot > -1) {
/*  834 */       System.out.println("[DEBUG]: Qualified non-this completions currently only go 1 level deep");
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  839 */     if (!prefix.matches("[A-Za-z_][A-Za-z0-9_\\$]*")) {
/*  840 */       System.out.println("[DEBUG]: Only identifier non-this completions are currently supported");
/*      */       
/*      */       return;
/*      */     } 
/*  844 */     String pkg = cu.getPackageName();
/*  845 */     boolean matched = false;
/*      */     
/*  847 */     for (Iterator<Member> j = td.getMemberIterator(); j.hasNext(); ) {
/*      */       
/*  849 */       Member m = j.next();
/*      */ 
/*      */       
/*  852 */       if (m instanceof Field) {
/*      */         
/*  854 */         Field field = (Field)m;
/*      */         
/*  856 */         if (field.getName().equals(prefix)) {
/*      */           
/*  858 */           Type type = field.getType();
/*  859 */           if (type.isArray()) {
/*  860 */             ClassFile cf = getClassFileFor(cu, "java.lang.Object");
/*  861 */             addCompletionsForExtendedClass(retVal, cu, cf, pkg, (Map<String, String>)null);
/*      */             
/*  863 */             FieldCompletion fc = FieldCompletion.createLengthCompletion((CompletionProvider)this, type);
/*  864 */             retVal.add(fc);
/*      */           }
/*  866 */           else if (!type.isBasicType()) {
/*  867 */             String typeStr = type.getName(true, false);
/*  868 */             ClassFile cf = getClassFileFor(cu, typeStr);
/*      */             
/*  870 */             if (cf != null) {
/*  871 */               Map<String, String> typeParamMap = createTypeParamMap(type, cf);
/*  872 */               addCompletionsForExtendedClass(retVal, cu, cf, pkg, typeParamMap);
/*      */ 
/*      */               
/*  875 */               for (int i = 0; i < cf.getImplementedInterfaceCount(); i++) {
/*  876 */                 String inter = cf.getImplementedInterfaceName(i, true);
/*  877 */                 cf = getClassFileFor(cu, inter);
/*  878 */                 System.out.println(cf);
/*      */               } 
/*      */             } 
/*      */           } 
/*  882 */           matched = true;
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  891 */     if (currentMethod != null) {
/*      */       
/*  893 */       boolean found = false;
/*      */ 
/*      */       
/*  896 */       for (int i = 0; i < currentMethod.getParameterCount(); i++) {
/*  897 */         FormalParameter param = currentMethod.getParameter(i);
/*  898 */         String name = param.getName();
/*      */         
/*  900 */         if (prefix.equals(name)) {
/*  901 */           addCompletionsForLocalVarsMethods(cu, (LocalVariable)param, retVal);
/*  902 */           found = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */       
/*  908 */       if (!found) {
/*  909 */         CodeBlock body = currentMethod.getBody();
/*  910 */         if (body != null) {
/*  911 */           loadCompletionsForCaretPositionQualifiedCodeBlock(cu, retVal, td, body, prefix, offs);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  916 */       matched |= found;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  922 */     if (!matched) {
/*  923 */       List<ImportDeclaration> imports = cu.getImports();
/*  924 */       List<ClassFile> matches = this.jarManager.getClassesWithUnqualifiedName(prefix, imports);
/*      */       
/*  926 */       if (matches != null) {
/*  927 */         for (int i = 0; i < matches.size(); i++) {
/*  928 */           ClassFile cf = matches.get(i);
/*  929 */           addCompletionsForStaticMembers(retVal, cu, cf, pkg);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadCompletionsForCaretPositionQualifiedCodeBlock(CompilationUnit cu, Set<Completion> retVal, TypeDeclaration td, CodeBlock block, String prefix, int offs) {
/*  941 */     boolean found = false;
/*      */     int i;
/*  943 */     for (i = 0; i < block.getLocalVarCount(); ) {
/*  944 */       LocalVariable var = block.getLocalVar(i);
/*  945 */       if (var.getNameEndOffset() <= offs) {
/*      */         
/*  947 */         if (prefix.equals(var.getName())) {
/*  948 */           addCompletionsForLocalVarsMethods(cu, var, retVal);
/*  949 */           found = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/*      */         i++;
/*      */       } 
/*      */     } 
/*      */     
/*  958 */     if (found) {
/*      */       return;
/*      */     }
/*      */     
/*  962 */     for (i = 0; i < block.getChildBlockCount(); i++) {
/*  963 */       CodeBlock child = block.getChildBlock(i);
/*  964 */       if (child.containsOffset(offs)) {
/*  965 */         loadCompletionsForCaretPositionQualifiedCodeBlock(cu, retVal, td, child, prefix, offs);
/*      */ 
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/*  971 */       if (child.getNameStartOffset() > offs) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadCompletionsForImport(Set<Completion> set, String importStr, String pkgName) {
/*  988 */     if (importStr.endsWith(".*")) {
/*  989 */       String pkg = importStr.substring(0, importStr.length() - 2);
/*  990 */       boolean inPkg = pkg.equals(pkgName);
/*  991 */       List<ClassFile> classes = this.jarManager.getClassesInPackage(pkg, inPkg);
/*  992 */       for (ClassFile cf : classes) {
/*  993 */         set.add(new ClassCompletion((CompletionProvider)this, cf));
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  998 */       ClassFile cf = this.jarManager.getClassEntry(importStr);
/*  999 */       if (cf != null) {
/* 1000 */         set.add(new ClassCompletion((CompletionProvider)this, cf));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadImportCompletions(Set<Completion> set, String text, CompilationUnit cu) {
/* 1017 */     if (text.indexOf('.') > -1) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1023 */     String pkgName = cu.getPackageName();
/* 1024 */     loadCompletionsForImport(set, "java.lang.*", pkgName);
/* 1025 */     for (Iterator<ImportDeclaration> i = cu.getImportIterator(); i.hasNext(); ) {
/* 1026 */       ImportDeclaration id = i.next();
/* 1027 */       String name = id.getName();
/* 1028 */       if (!"java.lang.*".equals(name)) {
/* 1029 */         loadCompletionsForImport(set, name, pkgName);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeJar(File jar) {
/* 1051 */     boolean removed = this.jarManager.removeClassFileSource(jar);
/*      */ 
/*      */ 
/*      */     
/* 1055 */     if (removed) {
/* 1056 */       clear();
/*      */     }
/* 1058 */     return removed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setJavaProvider(JavaCompletionProvider javaProvider) {
/* 1068 */     this.javaProvider = javaProvider;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\SourceCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */