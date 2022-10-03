/*     */ package org.fife.rsta.ac.java.tree;
/*     */ 
/*     */ import javax.swing.Icon;
/*     */ import org.fife.rsta.ac.java.DecoratableIcon;
/*     */ import org.fife.rsta.ac.java.IconFactory;
/*     */ import org.fife.rsta.ac.java.rjc.ast.ASTNode;
/*     */ import org.fife.rsta.ac.java.rjc.ast.EnumDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.NormalClassDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.NormalInterfaceDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TypeDeclarationTreeNode
/*     */   extends JavaTreeNode
/*     */ {
/*     */   public TypeDeclarationTreeNode(TypeDeclaration typeDec) {
/*  35 */     super((ASTNode)typeDec);
/*     */     
/*  37 */     String iconName = null;
/*  38 */     int priority = 0;
/*     */     
/*  40 */     if (typeDec instanceof NormalClassDeclaration) {
/*  41 */       NormalClassDeclaration ncd = (NormalClassDeclaration)typeDec;
/*  42 */       if (ncd.getModifiers() != null) {
/*  43 */         if (ncd.getModifiers().isPublic()) {
/*  44 */           iconName = "classIcon";
/*     */         }
/*  46 */         else if (ncd.getModifiers().isProtected()) {
/*  47 */           iconName = "innerClassProtectedIcon";
/*     */         }
/*  49 */         else if (ncd.getModifiers().isPrivate()) {
/*  50 */           iconName = "innerClassPrivateIcon";
/*     */         } else {
/*     */           
/*  53 */           iconName = "innerClassDefaultIcon";
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  58 */         iconName = "defaultClassIcon";
/*     */       }
/*     */     
/*  61 */     } else if (typeDec instanceof NormalInterfaceDeclaration) {
/*  62 */       NormalInterfaceDeclaration nid = (NormalInterfaceDeclaration)typeDec;
/*  63 */       if (nid.getModifiers() != null && nid.getModifiers().isPublic()) {
/*  64 */         iconName = "interfaceIcon";
/*     */       } else {
/*     */         
/*  67 */         iconName = "defaultInterfaceIcon";
/*     */       }
/*     */     
/*  70 */     } else if (typeDec instanceof EnumDeclaration) {
/*  71 */       EnumDeclaration ed = (EnumDeclaration)typeDec;
/*  72 */       if (ed.getModifiers() != null) {
/*  73 */         if (ed.getModifiers().isPublic()) {
/*  74 */           iconName = "enumIcon";
/*     */         }
/*  76 */         else if (ed.getModifiers().isProtected()) {
/*  77 */           iconName = "enumProtectedIcon";
/*     */         }
/*  79 */         else if (ed.getModifiers().isPrivate()) {
/*  80 */           iconName = "enumPrivateIcon";
/*     */         } else {
/*     */           
/*  83 */           iconName = "enumDefaultIcon";
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  88 */         iconName = "enumDefaultIcon";
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     IconFactory fact = IconFactory.get();
/*  93 */     Icon mainIcon = fact.getIcon(iconName);
/*     */     
/*  95 */     if (mainIcon == null) {
/*  96 */       System.out.println("*** " + typeDec);
/*     */     } else {
/*     */       
/*  99 */       DecoratableIcon di = new DecoratableIcon(mainIcon);
/* 100 */       di.setDeprecated(typeDec.isDeprecated());
/* 101 */       Modifiers mods = typeDec.getModifiers();
/* 102 */       if (mods != null) {
/* 103 */         if (mods.isAbstract()) {
/* 104 */           di.addDecorationIcon(fact.getIcon("abstractIcon"));
/*     */         }
/* 106 */         else if (mods.isFinal()) {
/* 107 */           di.addDecorationIcon(fact.getIcon("finalIcon"));
/*     */         } 
/* 109 */         if (mods.isStatic()) {
/* 110 */           di.addDecorationIcon(fact.getIcon("staticIcon"));
/* 111 */           priority = -16;
/*     */         } 
/*     */       } 
/* 114 */       setIcon((Icon)di);
/*     */     } 
/*     */     
/* 117 */     setSortPriority(priority);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText(boolean selected) {
/* 124 */     TypeDeclaration typeDec = (TypeDeclaration)getUserObject();
/*     */     
/* 126 */     return (typeDec != null) ? typeDec.getName() : null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\tree\TypeDeclarationTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */