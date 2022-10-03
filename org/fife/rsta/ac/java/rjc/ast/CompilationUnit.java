/*     */ package org.fife.rsta.ac.java.rjc.ast;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Annotation;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Offset;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Token;
/*     */ import org.fife.rsta.ac.java.rjc.notices.ParserNotice;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompilationUnit
/*     */   extends AbstractASTNode
/*     */   implements TypeDeclarationContainer
/*     */ {
/*     */   private List<Annotation> annotations;
/*     */   private Package pkg;
/*     */   private List<ImportDeclaration> imports;
/*     */   private List<TypeDeclaration> typeDeclarations;
/*     */   private List<ParserNotice> notices;
/*  45 */   private static final Offset ZERO_OFFSET = new ZeroOffset();
/*     */ 
/*     */   
/*     */   public CompilationUnit(String name) {
/*  49 */     super(name, ZERO_OFFSET);
/*  50 */     this.imports = new ArrayList<>(3);
/*  51 */     this.typeDeclarations = new ArrayList<>(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addImportDeclaration(ImportDeclaration dec) {
/*  56 */     this.imports.add(dec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParserNotice(Token t, String msg) {
/*  67 */     addParserNotice(new ParserNotice(t, msg));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParserNotice(ParserNotice notice) {
/*  72 */     if (this.notices == null) {
/*  73 */       this.notices = new ArrayList<>();
/*  74 */       this.notices.add(notice);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTypeDeclaration(TypeDeclaration typeDec) {
/*  81 */     this.typeDeclarations.add(typeDec);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnnotationCount() {
/*  86 */     return this.annotations.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Annotation> getAnnotationIterator() {
/*  91 */     return this.annotations.iterator();
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
/*     */   public TypeDeclaration getDeepestTypeDeclarationAtOffset(int offs) {
/* 107 */     TypeDeclaration td = getTypeDeclarationAtOffset(offs);
/*     */     
/* 109 */     if (td != null) {
/* 110 */       TypeDeclaration next = td.getChildTypeAtOffset(offs);
/* 111 */       while (next != null) {
/* 112 */         td = next;
/* 113 */         next = td.getChildTypeAtOffset(offs);
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     return td;
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
/*     */   public Point getEnclosingMethodRange(int offs) {
/* 131 */     Point range = null;
/*     */     
/* 133 */     for (Iterator<TypeDeclaration> i = getTypeDeclarationIterator(); i.hasNext(); ) {
/*     */       
/* 135 */       TypeDeclaration td = i.next();
/* 136 */       int start = td.getBodyStartOffset();
/* 137 */       int end = td.getBodyEndOffset();
/*     */       
/* 139 */       if (offs >= start && offs <= end) {
/*     */         
/* 141 */         if (td instanceof NormalClassDeclaration) {
/* 142 */           NormalClassDeclaration ncd = (NormalClassDeclaration)td;
/* 143 */           for (Iterator<Member> j = ncd.getMemberIterator(); j.hasNext(); ) {
/* 144 */             Member m = j.next();
/* 145 */             if (m instanceof Method) {
/* 146 */               Method method = (Method)m;
/* 147 */               CodeBlock body = method.getBody();
/* 148 */               if (body != null) {
/* 149 */                 int start2 = method.getNameStartOffset();
/*     */                 
/* 151 */                 int end2 = body.getNameEndOffset();
/* 152 */                 if (offs >= start2 && offs <= end2) {
/* 153 */                   range = new Point(start2, end2);
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 161 */         if (range == null) {
/* 162 */           range = new Point(start, end);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 169 */     return range;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getImportCount() {
/* 175 */     return this.imports.size();
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
/*     */   public List<ImportDeclaration> getImports() {
/* 188 */     return new ArrayList<>(this.imports);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<ImportDeclaration> getImportIterator() {
/* 193 */     return this.imports.iterator();
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
/*     */   public Package getPackage() {
/* 205 */     return this.pkg;
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
/*     */   public String getPackageName() {
/* 217 */     return (this.pkg == null) ? null : this.pkg.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParserNotice getParserNotice(int index) {
/* 222 */     if (this.notices == null) {
/* 223 */       throw new IndexOutOfBoundsException("No parser notices available");
/*     */     }
/* 225 */     return this.notices.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParserNoticeCount() {
/* 230 */     return (this.notices == null) ? 0 : this.notices.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeDeclaration getTypeDeclaration(int index) {
/* 235 */     return this.typeDeclarations.get(index);
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
/*     */   public TypeDeclaration getTypeDeclarationAtOffset(int offs) {
/* 250 */     TypeDeclaration typeDec = null;
/*     */     
/* 252 */     for (TypeDeclaration td : this.typeDeclarations) {
/* 253 */       if (td.getBodyContainsOffset(offs)) {
/* 254 */         typeDec = td;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 259 */     return typeDec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTypeDeclarationCount() {
/* 265 */     return this.typeDeclarations.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<TypeDeclaration> getTypeDeclarationIterator() {
/* 270 */     return this.typeDeclarations.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPackage(Package pkg) {
/* 275 */     this.pkg = pkg;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ZeroOffset
/*     */     implements Offset
/*     */   {
/*     */     private ZeroOffset() {}
/*     */ 
/*     */     
/*     */     public int getOffset() {
/* 286 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\CompilationUnit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */