/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.LanguageSupport;
/*     */ import org.fife.rsta.ac.LanguageSupportFactory;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CodeBlock;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*     */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*     */ import org.fife.rsta.ac.java.rjc.ast.LocalVariable;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.ast.NormalClassDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Package;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*     */ import org.fife.ui.autocomplete.BasicCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.EmptyIcon;
/*     */ import org.fife.ui.autocomplete.ParameterChoicesProvider;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SourceParamChoicesProvider
/*     */   implements ParameterChoicesProvider
/*     */ {
/*     */   private CompletionProvider provider;
/*     */   
/*     */   private void addPublicAndProtectedFieldsAndGetters(Type type, JarManager jm, Package pkg, List<Completion> list) {}
/*     */   
/*     */   public List<Completion> getLocalVarsFieldsAndGetters(NormalClassDeclaration ncd, String type, int offs) {
/*  92 */     List<Completion> members = new ArrayList<>();
/*     */     
/*  94 */     if (!ncd.getBodyContainsOffset(offs)) {
/*  95 */       return members;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 100 */     Method method = ncd.getMethodContainingOffset(offs);
/* 101 */     if (method != null) {
/*     */ 
/*     */       
/* 104 */       Iterator<FormalParameter> iterator = method.getParameterIterator();
/* 105 */       while (iterator.hasNext()) {
/* 106 */         FormalParameter param = iterator.next();
/* 107 */         Type paramType = param.getType();
/* 108 */         if (isTypeCompatible(paramType, type))
/*     */         {
/* 110 */           members.add(new LocalVariableCompletion(this.provider, (LocalVariable)param));
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 115 */       CodeBlock body = method.getBody();
/* 116 */       if (body != null) {
/* 117 */         CodeBlock block = body.getDeepestCodeBlockContaining(offs);
/* 118 */         List<LocalVariable> vars = block.getLocalVarsBefore(offs);
/* 119 */         for (LocalVariable var : vars) {
/* 120 */           Type varType = var.getType();
/* 121 */           if (isTypeCompatible(varType, type))
/*     */           {
/* 123 */             members.add(new LocalVariableCompletion(this.provider, var));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     for (Iterator<Member> i = ncd.getMemberIterator(); i.hasNext(); ) {
/*     */       
/* 134 */       Member member = i.next();
/*     */       
/* 136 */       if (member instanceof Field) {
/* 137 */         Type fieldType = member.getType();
/* 138 */         if (isTypeCompatible(fieldType, type))
/*     */         {
/* 140 */           members.add(new FieldCompletion(this.provider, (Field)member));
/*     */         }
/*     */         continue;
/*     */       } 
/* 144 */       method = (Method)member;
/* 145 */       if (isSimpleGetter(method) && 
/* 146 */         isTypeCompatible(method.getType(), type))
/*     */       {
/* 148 */         members.add(new MethodCompletion(this.provider, method));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     return members;
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
/*     */   public List<Completion> getParameterChoices(JTextComponent tc, ParameterizedCompletion.Parameter param) {
/* 168 */     LanguageSupportFactory lsf = LanguageSupportFactory.get();
/* 169 */     LanguageSupport support = lsf.getSupportFor("text/java");
/*     */     
/* 171 */     JavaLanguageSupport jls = (JavaLanguageSupport)support;
/* 172 */     JarManager jm = jls.getJarManager();
/*     */ 
/*     */ 
/*     */     
/* 176 */     RSyntaxTextArea textArea = (RSyntaxTextArea)tc;
/* 177 */     JavaParser parser = jls.getParser(textArea);
/* 178 */     if (parser == null) {
/* 179 */       return null;
/*     */     }
/* 181 */     CompilationUnit cu = parser.getCompilationUnit();
/* 182 */     if (cu == null) {
/* 183 */       return null;
/*     */     }
/* 185 */     int dot = tc.getCaretPosition();
/* 186 */     TypeDeclaration typeDec = cu.getDeepestTypeDeclarationAtOffset(dot);
/* 187 */     if (typeDec == null) {
/* 188 */       return null;
/*     */     }
/*     */     
/* 191 */     List<Completion> list = null;
/* 192 */     Package pkg = typeDec.getPackage();
/* 193 */     this.provider = (CompletionProvider)jls.getCompletionProvider(textArea);
/*     */ 
/*     */     
/* 196 */     if (typeDec instanceof NormalClassDeclaration) {
/*     */ 
/*     */       
/* 199 */       NormalClassDeclaration ncd = (NormalClassDeclaration)typeDec;
/* 200 */       list = getLocalVarsFieldsAndGetters(ncd, param.getType(), dot);
/*     */ 
/*     */ 
/*     */       
/* 204 */       Type extended = ncd.getExtendedType();
/* 205 */       if (extended != null) {
/* 206 */         addPublicAndProtectedFieldsAndGetters(extended, jm, pkg, list);
/*     */       }
/*     */ 
/*     */       
/* 210 */       for (Iterator<Type> i = ncd.getImplementedIterator(); i.hasNext(); ) {
/* 211 */         Type implemented = i.next();
/* 212 */         addPublicAndProtectedFieldsAndGetters(implemented, jm, pkg, list);
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 219 */     else if (typeDec instanceof org.fife.rsta.ac.java.rjc.ast.NormalInterfaceDeclaration) {
/*     */     
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     if (!typeDec.isStatic());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     Object typeObj = param.getTypeObject();
/*     */     
/* 237 */     if (typeObj instanceof Type) {
/* 238 */       Type type = (Type)typeObj;
/* 239 */       if (type.isBasicType()) {
/* 240 */         if (isPrimitiveNumericType(type)) {
/* 241 */           list.add(new SimpleCompletion(this.provider, "0"));
/*     */         } else {
/*     */           
/* 244 */           list.add(new SimpleCompletion(this.provider, "false"));
/* 245 */           list.add(new SimpleCompletion(this.provider, "true"));
/*     */         } 
/*     */       } else {
/*     */         
/* 249 */         list.add(new SimpleCompletion(this.provider, "null"));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 254 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPrimitiveNumericType(Type type) {
/* 260 */     String str = type.getName(true);
/* 261 */     return ("byte".equals(str) || "float".equals(str) || "double"
/* 262 */       .equals(str) || "int".equals(str) || "short"
/* 263 */       .equals(str) || "long".equals(str));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSimpleGetter(Method method) {
/* 274 */     return (method.getParameterCount() == 0 && method
/* 275 */       .getName().startsWith("get"));
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
/*     */   private boolean isTypeCompatible(Type type, String typeName) {
/* 290 */     String typeName2 = type.getName(false);
/*     */ 
/*     */ 
/*     */     
/* 294 */     int lt = typeName2.indexOf('<');
/* 295 */     if (lt > -1) {
/* 296 */       String arrayDepth = null;
/* 297 */       int brackets = typeName2.indexOf('[', lt);
/* 298 */       if (brackets > -1) {
/* 299 */         arrayDepth = typeName2.substring(brackets);
/*     */       }
/* 301 */       typeName2 = typeName2.substring(lt);
/* 302 */       if (arrayDepth != null) {
/* 303 */         typeName2 = typeName2 + arrayDepth;
/*     */       }
/*     */     } 
/*     */     
/* 307 */     return typeName2.equalsIgnoreCase(typeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SimpleCompletion
/*     */     extends BasicCompletion
/*     */     implements JavaSourceCompletion
/*     */   {
/* 320 */     private Icon ICON = (Icon)new EmptyIcon(16);
/*     */     
/*     */     public SimpleCompletion(CompletionProvider provider, String text) {
/* 323 */       super(provider, text);
/* 324 */       setRelevance(-1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Icon getIcon() {
/* 329 */       return this.ICON;
/*     */     }
/*     */     
/*     */     public void rendererText(Graphics g, int x, int y, boolean selected) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\SourceParamChoicesProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */