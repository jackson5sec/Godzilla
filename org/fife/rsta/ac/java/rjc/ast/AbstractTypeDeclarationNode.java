/*     */ package org.fife.rsta.ac.java.rjc.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Offset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTypeDeclarationNode
/*     */   extends AbstractASTNode
/*     */   implements TypeDeclaration
/*     */ {
/*     */   private Package pkg;
/*     */   private Modifiers modifiers;
/*     */   private TypeDeclaration parentType;
/*     */   private List<TypeDeclaration> childTypes;
/*     */   private Offset bodyStartOffs;
/*     */   private Offset bodyEndOffs;
/*     */   private boolean deprecated;
/*     */   private String docComment;
/*     */   private List<Member> memberList;
/*     */   
/*     */   public AbstractTypeDeclarationNode(String name, Offset start) {
/*  38 */     super(name, start);
/*  39 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractTypeDeclarationNode(String name, Offset start, Offset end) {
/*  44 */     super(name, start, end);
/*  45 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMember(Member member) {
/*  50 */     member.setParentTypeDeclaration(this);
/*  51 */     this.memberList.add(member);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTypeDeclaration(TypeDeclaration type) {
/*  57 */     if (this.childTypes == null) {
/*  58 */       this.childTypes = new ArrayList<>(1);
/*     */     }
/*  60 */     type.setParentType(this);
/*  61 */     this.childTypes.add(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBodyContainsOffset(int offs) {
/*  67 */     return (offs >= getBodyStartOffset() && offs < getBodyEndOffset());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBodyEndOffset() {
/*  73 */     return (this.bodyEndOffs != null) ? this.bodyEndOffs.getOffset() : Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBodyStartOffset() {
/*  79 */     return (this.bodyStartOffs == null) ? 0 : this.bodyStartOffs.getOffset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration getChildType(int index) {
/*  85 */     return this.childTypes.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration getChildTypeAtOffset(int offs) {
/*  95 */     TypeDeclaration typeDec = null;
/*     */     
/*  97 */     for (int i = 0; i < getChildTypeCount(); i++) {
/*  98 */       TypeDeclaration td = getChildType(i);
/*  99 */       if (td.getBodyContainsOffset(offs)) {
/* 100 */         typeDec = td;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 105 */     return typeDec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChildTypeCount() {
/* 112 */     return (this.childTypes == null) ? 0 : this.childTypes.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDocComment() {
/* 118 */     return this.docComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Field> getFieldIterator() {
/* 127 */     List<Field> fields = new ArrayList<>();
/* 128 */     for (Iterator<Member> i = getMemberIterator(); i.hasNext(); ) {
/* 129 */       Member member = i.next();
/* 130 */       if (member instanceof Field) {
/* 131 */         fields.add((Field)member);
/*     */       }
/*     */     } 
/* 134 */     return fields.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember(int index) {
/* 140 */     return this.memberList.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMemberCount() {
/* 146 */     return this.memberList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Member> getMemberIterator() {
/* 155 */     return this.memberList.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Method> getMethodIterator() {
/* 164 */     List<Method> methods = new ArrayList<>();
/* 165 */     for (Iterator<Member> i = getMemberIterator(); i.hasNext(); ) {
/* 166 */       Member member = i.next();
/* 167 */       if (member instanceof Method) {
/* 168 */         methods.add((Method)member);
/*     */       }
/*     */     } 
/* 171 */     return methods.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Method> getMethodsByName(String name) {
/* 180 */     List<Method> methods = new ArrayList<>();
/* 181 */     for (Iterator<Member> i = getMemberIterator(); i.hasNext(); ) {
/* 182 */       Member member = i.next();
/* 183 */       if (member instanceof Method && name.equals(member.getName())) {
/* 184 */         methods.add((Method)member);
/*     */       }
/*     */     } 
/* 187 */     return methods;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Modifiers getModifiers() {
/* 193 */     return this.modifiers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName(boolean fullyQualified) {
/* 202 */     String name = getName();
/* 203 */     if (fullyQualified) {
/* 204 */       Package pkg = getPackage();
/* 205 */       if (pkg != null) {
/* 206 */         name = pkg.getName() + "." + name;
/*     */       }
/*     */     } 
/* 209 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Package getPackage() {
/* 218 */     return this.pkg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration getParentType() {
/* 227 */     return this.parentType;
/*     */   }
/*     */ 
/*     */   
/*     */   private void init() {
/* 232 */     this.memberList = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 238 */     return this.deprecated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 247 */     return (this.modifiers == null) ? false : this.modifiers.isStatic();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBodyEndOffset(Offset end) {
/* 252 */     this.bodyEndOffs = end;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBodyStartOffset(Offset start) {
/* 257 */     this.bodyStartOffs = start;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDeprecated(boolean deprecated) {
/* 262 */     this.deprecated = deprecated;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocComment(String comment) {
/* 268 */     this.docComment = comment;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setModifiers(Modifiers modifiers) {
/* 273 */     this.modifiers = modifiers;
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
/*     */   public void setPackage(Package pkg) {
/* 285 */     this.pkg = pkg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentType(TypeDeclaration parentType) {
/* 294 */     this.parentType = parentType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 300 */     StringBuilder sb = new StringBuilder();
/* 301 */     if (this.modifiers != null) {
/* 302 */       sb.append(this.modifiers.toString()).append(' ');
/*     */     }
/* 304 */     sb.append(getTypeString()).append(' ');
/* 305 */     sb.append(getName());
/* 306 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\AbstractTypeDeclarationNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */