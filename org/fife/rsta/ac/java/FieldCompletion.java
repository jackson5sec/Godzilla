/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Icon;
/*     */ import org.fife.rsta.ac.java.classreader.FieldInfo;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FieldCompletion
/*     */   extends AbstractJavaSourceCompletion
/*     */   implements MemberCompletion
/*     */ {
/*     */   private MemberCompletion.Data data;
/*     */   private static final int RELEVANCE = 3;
/*     */   
/*     */   public FieldCompletion(CompletionProvider provider, Field field) {
/*  52 */     super(provider, field.getName());
/*  53 */     this.data = new FieldData(field);
/*  54 */     setRelevance(3);
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldCompletion(CompletionProvider provider, FieldInfo info) {
/*  59 */     super(provider, info.getName());
/*  60 */     this.data = new FieldInfoData(info, (SourceCompletionProvider)provider);
/*  61 */     setRelevance(3);
/*     */   }
/*     */ 
/*     */   
/*     */   private FieldCompletion(CompletionProvider provider, String text) {
/*  66 */     super(provider, text);
/*  67 */     setRelevance(3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  73 */     return (obj instanceof FieldCompletion && ((FieldCompletion)obj)
/*  74 */       .getSignature().equals(getSignature()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static FieldCompletion createLengthCompletion(CompletionProvider provider, final Type type) {
/*  80 */     FieldCompletion fc = new FieldCompletion(provider, type.toString());
/*  81 */     fc.data = new MemberCompletion.Data()
/*     */       {
/*     */         public String getEnclosingClassName(boolean fullyQualified)
/*     */         {
/*  85 */           return type.getName(fullyQualified);
/*     */         }
/*     */ 
/*     */         
/*     */         public String getIcon() {
/*  90 */           return "fieldPublicIcon";
/*     */         }
/*     */ 
/*     */         
/*     */         public String getSignature() {
/*  95 */           return "length";
/*     */         }
/*     */ 
/*     */         
/*     */         public String getSummary() {
/* 100 */           return null;
/*     */         }
/*     */ 
/*     */         
/*     */         public String getType() {
/* 105 */           return "int";
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isConstructor() {
/* 110 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDeprecated() {
/* 115 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isAbstract() {
/* 120 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isFinal() {
/* 125 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isStatic() {
/* 130 */           return false;
/*     */         }
/*     */       };
/*     */     
/* 134 */     return fc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/* 140 */     return this.data.getEnclosingClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/* 146 */     return IconFactory.get().getIcon(this.data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 152 */     return this.data.getSignature();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 159 */     String summary = this.data.getSummary();
/*     */ 
/*     */     
/* 162 */     if (summary != null && summary.startsWith("/**")) {
/* 163 */       summary = Util.docCommentToHtml(summary);
/*     */     }
/*     */     
/* 166 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 173 */     return this.data.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     return getSignature().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 185 */     return this.data.isDeprecated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 191 */     MethodCompletion.rendererText(this, g, x, y, selected);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\FieldCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */