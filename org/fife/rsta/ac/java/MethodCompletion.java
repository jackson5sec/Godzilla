/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.FunctionCompletion;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MethodCompletion
/*     */   extends FunctionCompletion
/*     */   implements MemberCompletion
/*     */ {
/*     */   private MemberCompletion.Data data;
/*     */   private String compareString;
/*     */   private static final int NON_CONSTRUCTOR_RELEVANCE = 2;
/*     */   
/*     */   public MethodCompletion(CompletionProvider provider, Method m) {
/*  76 */     super(provider, m.getName(), (m.getType() == null) ? "void" : m.getType().toString());
/*  77 */     setDefinedIn(m.getParentTypeDeclaration().getName());
/*  78 */     this.data = new MethodData(m);
/*  79 */     setRelevanceAppropriately();
/*     */     
/*  81 */     int count = m.getParameterCount();
/*  82 */     List<ParameterizedCompletion.Parameter> params = new ArrayList<>(count);
/*  83 */     for (int i = 0; i < count; i++) {
/*  84 */       FormalParameter param = m.getParameter(i);
/*  85 */       Type type = param.getType();
/*  86 */       String name = param.getName();
/*  87 */       params.add(new ParameterizedCompletion.Parameter(type, name));
/*     */     } 
/*  89 */     setParams(params);
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
/*     */   public MethodCompletion(CompletionProvider provider, MethodInfo info) {
/* 103 */     super(provider, info.getName(), info.getReturnTypeString(false));
/* 104 */     setDefinedIn(info.getClassFile().getClassName(false));
/* 105 */     this.data = new MethodInfoData(info, (SourceCompletionProvider)provider);
/* 106 */     setRelevanceAppropriately();
/*     */     
/* 108 */     String[] paramTypes = info.getParameterTypes();
/* 109 */     List<ParameterizedCompletion.Parameter> params = new ArrayList<>(paramTypes.length);
/* 110 */     for (int i = 0; i < paramTypes.length; i++) {
/* 111 */       String name = ((MethodInfoData)this.data).getParameterName(i);
/* 112 */       String type = paramTypes[i].substring(paramTypes[i].lastIndexOf('.') + 1);
/* 113 */       params.add(new ParameterizedCompletion.Parameter(type, name));
/*     */     } 
/* 115 */     setParams(params);
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
/*     */   public int compareTo(Completion c2) {
/* 129 */     int rc = -1;
/*     */     
/* 131 */     if (c2 == this) {
/* 132 */       rc = 0;
/*     */     
/*     */     }
/* 135 */     else if (c2 instanceof MethodCompletion) {
/* 136 */       rc = getCompareString().compareTo(((MethodCompletion)c2)
/* 137 */           .getCompareString());
/*     */     
/*     */     }
/* 140 */     else if (c2 != null) {
/* 141 */       rc = toString().compareToIgnoreCase(c2.toString());
/* 142 */       if (rc == 0) {
/* 143 */         String clazz1 = getClass().getName();
/* 144 */         clazz1 = clazz1.substring(clazz1.lastIndexOf('.'));
/* 145 */         String clazz2 = c2.getClass().getName();
/* 146 */         clazz2 = clazz2.substring(clazz2.lastIndexOf('.'));
/* 147 */         rc = clazz1.compareTo(clazz2);
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     return rc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 158 */     return (obj instanceof MethodCompletion && ((MethodCompletion)obj)
/*     */       
/* 160 */       .getCompareString().equals(getCompareString()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEntered(JTextComponent comp) {
/* 166 */     String temp = getProvider().getAlreadyEnteredText(comp);
/* 167 */     int lastDot = temp.lastIndexOf('.');
/* 168 */     if (lastDot > -1) {
/* 169 */       temp = temp.substring(lastDot + 1);
/*     */     }
/* 171 */     return temp;
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
/*     */   private String getCompareString() {
/* 191 */     if (this.compareString == null) {
/* 192 */       StringBuilder sb = new StringBuilder(getName());
/*     */       
/* 194 */       int paramCount = getParamCount();
/* 195 */       if (paramCount < 10) {
/* 196 */         sb.append('0');
/*     */       }
/* 198 */       sb.append(paramCount);
/* 199 */       for (int i = 0; i < paramCount; i++) {
/* 200 */         String type = getParam(i).getType();
/* 201 */         sb.append(type);
/* 202 */         if (i < paramCount - 1) {
/* 203 */           sb.append(',');
/*     */         }
/*     */       } 
/* 206 */       this.compareString = sb.toString();
/*     */     } 
/*     */     
/* 209 */     return this.compareString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/* 216 */     return this.data.getEnclosingClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/* 222 */     return IconFactory.get().getIcon(this.data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 228 */     return this.data.getSignature();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 235 */     String summary = this.data.getSummary();
/*     */ 
/*     */     
/* 238 */     if (summary != null && summary.startsWith("/**")) {
/* 239 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/* 242 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 248 */     return getCompareString().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 254 */     return this.data.isDeprecated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 263 */     rendererText(this, g, x, y, selected);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRelevanceAppropriately() {
/* 272 */     if (!this.data.isConstructor()) {
/* 273 */       setRelevance(2);
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
/*     */   public static void rendererText(MemberCompletion mc, Graphics g, int x, int y, boolean selected) {
/* 290 */     String shortType = mc.getType();
/* 291 */     int dot = shortType.lastIndexOf('.');
/* 292 */     if (dot > -1) {
/* 293 */       shortType = shortType.substring(dot + 1);
/*     */     }
/*     */ 
/*     */     
/* 297 */     String sig = mc.getSignature();
/* 298 */     FontMetrics fm = g.getFontMetrics();
/* 299 */     g.drawString(sig, x, y);
/* 300 */     int newX = x + fm.stringWidth(sig);
/* 301 */     if (mc.isDeprecated()) {
/* 302 */       int midY = y + fm.getDescent() - fm.getHeight() / 2;
/* 303 */       g.drawLine(x, midY, newX, midY);
/*     */     } 
/* 305 */     x = newX;
/*     */ 
/*     */     
/* 308 */     StringBuilder sb = (new StringBuilder(" : ")).append(shortType);
/* 309 */     sb.append(" - ");
/* 310 */     String s = sb.toString();
/* 311 */     g.drawString(s, x, y);
/* 312 */     x += fm.stringWidth(s);
/*     */ 
/*     */     
/* 315 */     Color origColor = g.getColor();
/* 316 */     if (!selected) {
/* 317 */       g.setColor(Color.GRAY);
/*     */     }
/* 319 */     g.drawString(mc.getEnclosingClassName(false), x, y);
/* 320 */     if (!selected) {
/* 321 */       g.setColor(origColor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 332 */     return getSignature();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\MethodCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */