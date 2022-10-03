/*     */ package org.fife.rsta.ac.java.tree;
/*     */ 
/*     */ import javax.swing.Icon;
/*     */ import org.fife.rsta.ac.java.DecoratableIcon;
/*     */ import org.fife.rsta.ac.java.IconFactory;
/*     */ import org.fife.rsta.ac.java.rjc.ast.ASTNode;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CodeBlock;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*     */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*     */ import org.fife.ui.autocomplete.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MemberTreeNode
/*     */   extends JavaTreeNode
/*     */ {
/*     */   private String text;
/*     */   
/*     */   public MemberTreeNode(CodeBlock cb) {
/*  38 */     super((ASTNode)cb);
/*  39 */     this.text = "<html>" + cb.getName();
/*  40 */     IconFactory fact = IconFactory.get();
/*  41 */     Icon base = fact.getIcon("methodPrivateIcon");
/*  42 */     DecoratableIcon di = new DecoratableIcon(base);
/*  43 */     int priority = 3;
/*  44 */     if (cb.isStatic()) {
/*  45 */       di.addDecorationIcon(fact.getIcon("staticIcon"));
/*  46 */       priority -= 16;
/*     */     } 
/*  48 */     setIcon((Icon)di);
/*  49 */     setSortPriority(priority);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberTreeNode(Field field) {
/*  55 */     super((ASTNode)field);
/*     */     String icon;
/*  57 */     Modifiers mods = field.getModifiers();
/*     */ 
/*     */     
/*  60 */     if (mods == null) {
/*  61 */       icon = "fieldDefaultIcon";
/*     */     }
/*  63 */     else if (mods.isPrivate()) {
/*  64 */       icon = "fieldPrivateIcon";
/*     */     }
/*  66 */     else if (mods.isProtected()) {
/*  67 */       icon = "fieldProtectedIcon";
/*     */     }
/*  69 */     else if (mods.isPublic()) {
/*  70 */       icon = "fieldPublicIcon";
/*     */     } else {
/*     */       
/*  73 */       icon = "fieldDefaultIcon";
/*     */     } 
/*     */     
/*  76 */     StringBuilder sb = new StringBuilder();
/*  77 */     sb.append("<html>");
/*  78 */     sb.append(field.getName());
/*  79 */     sb.append(" : ");
/*  80 */     sb.append("<font color='#888888'>");
/*     */     
/*  82 */     appendType(field.getType(), sb);
/*  83 */     this.text = sb.toString();
/*  84 */     int priority = 1;
/*     */     
/*  86 */     IconFactory fact = IconFactory.get();
/*  87 */     Icon base = fact.getIcon(icon);
/*  88 */     DecoratableIcon di = new DecoratableIcon(base);
/*  89 */     di.setDeprecated(field.isDeprecated());
/*  90 */     if (mods != null) {
/*  91 */       if (mods.isStatic()) {
/*  92 */         di.addDecorationIcon(fact.getIcon("staticIcon"));
/*  93 */         priority -= 16;
/*     */       } 
/*  95 */       if (mods.isFinal()) {
/*  96 */         di.addDecorationIcon(fact.getIcon("finalIcon"));
/*     */       }
/*     */     } 
/*  99 */     setIcon((Icon)di);
/*     */     
/* 101 */     setSortPriority(priority);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberTreeNode(Method method) {
/* 108 */     super((ASTNode)method);
/*     */     
/*     */     String icon;
/* 111 */     int priority = 3;
/*     */     
/* 113 */     Modifiers mods = method.getModifiers();
/* 114 */     if (mods == null) {
/* 115 */       icon = "methodDefaultIcon";
/*     */     }
/* 117 */     else if (mods.isPrivate()) {
/* 118 */       icon = "methodPrivateIcon";
/*     */     }
/* 120 */     else if (mods.isProtected()) {
/* 121 */       icon = "methodProtectedIcon";
/*     */     }
/* 123 */     else if (mods.isPublic()) {
/* 124 */       icon = "methodPublicIcon";
/*     */     } else {
/*     */       
/* 127 */       icon = "methodDefaultIcon";
/*     */     } 
/* 129 */     StringBuilder sb = new StringBuilder();
/* 130 */     sb.append("<html>");
/* 131 */     sb.append(method.getName());
/* 132 */     sb.append('(');
/* 133 */     int paramCount = method.getParameterCount();
/* 134 */     for (int i = 0; i < paramCount; i++) {
/* 135 */       FormalParameter param = method.getParameter(i);
/* 136 */       appendType(param.getType(), sb);
/* 137 */       if (i < paramCount - 1) {
/* 138 */         sb.append(", ");
/*     */       }
/*     */     } 
/* 141 */     sb.append(')');
/* 142 */     if (method.getType() != null) {
/* 143 */       sb.append(" : ");
/* 144 */       sb.append("<font color='#888888'>");
/* 145 */       appendType(method.getType(), sb);
/*     */     } 
/*     */     
/* 148 */     this.text = sb.toString();
/*     */     
/* 150 */     IconFactory fact = IconFactory.get();
/* 151 */     Icon base = fact.getIcon(icon);
/* 152 */     DecoratableIcon di = new DecoratableIcon(base);
/* 153 */     di.setDeprecated(method.isDeprecated());
/* 154 */     if (mods != null) {
/* 155 */       if (mods.isAbstract()) {
/* 156 */         di.addDecorationIcon(fact.getIcon("abstractIcon"));
/*     */       }
/* 158 */       if (method.isConstructor()) {
/* 159 */         di.addDecorationIcon(fact.getIcon("constructorIcon"));
/* 160 */         priority = 2;
/*     */       } 
/* 162 */       if (mods.isStatic()) {
/* 163 */         di.addDecorationIcon(fact.getIcon("staticIcon"));
/* 164 */         priority -= 16;
/*     */       } 
/* 166 */       if (mods.isFinal()) {
/* 167 */         di.addDecorationIcon(fact.getIcon("finalIcon"));
/*     */       }
/*     */     } 
/* 170 */     setIcon((Icon)di);
/*     */     
/* 172 */     setSortPriority(priority);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void appendType(Type type, StringBuilder sb) {
/* 178 */     if (type != null) {
/* 179 */       String t = type.toString();
/* 180 */       t = t.replaceAll("<", "&lt;");
/* 181 */       t = t.replaceAll(">", "&gt;");
/* 182 */       sb.append(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText(boolean selected) {
/* 190 */     return selected ? Util.stripHtml(this.text)
/* 191 */       .replaceAll("&lt;", "<").replaceAll("&gt;", ">") : this.text;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\tree\MemberTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */