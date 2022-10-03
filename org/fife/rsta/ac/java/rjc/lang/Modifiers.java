/*     */ package org.fife.rsta.ac.java.rjc.lang;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Modifiers
/*     */ {
/*  30 */   public static final Integer ABSTRACT = Integer.valueOf(1024);
/*  31 */   public static final Integer FINAL = Integer.valueOf(16);
/*  32 */   public static final Integer INTERFACE = Integer.valueOf(512);
/*  33 */   public static final Integer NATIVE = Integer.valueOf(256);
/*  34 */   public static final Integer PRIVATE = Integer.valueOf(2);
/*  35 */   public static final Integer PROTECTED = Integer.valueOf(4);
/*  36 */   public static final Integer PUBLIC = Integer.valueOf(1);
/*  37 */   public static final Integer STATIC = Integer.valueOf(8);
/*  38 */   public static final Integer STRICTFP = Integer.valueOf(2048);
/*  39 */   public static final Integer SYNCHRONIZED = Integer.valueOf(32);
/*  40 */   public static final Integer TRANSIENT = Integer.valueOf(128);
/*  41 */   public static final Integer VOLATILE = Integer.valueOf(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static final Map<Integer, String> MODIFIER_TEXT = new HashMap<Integer, String>()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private List<Integer> modifiers = new ArrayList<>(1);
/*  71 */   private List<Annotation> annotations = new ArrayList<>(0);
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotation(Annotation annotation) {
/*  76 */     this.annotations.add(annotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addModifier(int tokenType) {
/*     */     Integer key;
/*  84 */     switch (tokenType) {
/*     */       case 65537:
/*  86 */         key = ABSTRACT;
/*     */         break;
/*     */       case 65554:
/*  89 */         key = FINAL;
/*     */         break;
/*     */       case 65564:
/*  92 */         key = INTERFACE;
/*     */         break;
/*     */       case 65566:
/*  95 */         key = NATIVE;
/*     */         break;
/*     */       case 65569:
/*  98 */         key = PRIVATE;
/*     */         break;
/*     */       case 65570:
/* 101 */         key = PROTECTED;
/*     */         break;
/*     */       case 65571:
/* 104 */         key = PUBLIC;
/*     */         break;
/*     */       case 65574:
/* 107 */         key = STATIC;
/*     */         break;
/*     */       case 65575:
/* 110 */         key = STRICTFP;
/*     */         break;
/*     */       case 65578:
/* 113 */         key = SYNCHRONIZED;
/*     */         break;
/*     */       case 65582:
/* 116 */         key = TRANSIENT;
/*     */         break;
/*     */       case 65585:
/* 119 */         key = VOLATILE;
/*     */         break;
/*     */       default:
/* 122 */         throw new IllegalArgumentException("Invalid tokenType: " + tokenType);
/*     */     } 
/*     */ 
/*     */     
/* 126 */     int pos = Collections.binarySearch((List)this.modifiers, key);
/* 127 */     if (pos < 0) {
/*     */       
/* 129 */       int insertionPoint = -(pos + 1);
/* 130 */       this.modifiers.add(insertionPoint, key);
/*     */     } 
/*     */     
/* 133 */     return (pos < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean containsModifier(Integer modifierKey) {
/* 139 */     return (Collections.binarySearch((List)this.modifiers, modifierKey) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 144 */     return containsModifier(ABSTRACT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 149 */     return containsModifier(FINAL);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrivate() {
/* 154 */     return containsModifier(PRIVATE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isProtected() {
/* 159 */     return containsModifier(PROTECTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublic() {
/* 164 */     return containsModifier(PUBLIC);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 169 */     return containsModifier(STATIC);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 175 */     StringBuilder sb = new StringBuilder(); int i;
/* 176 */     for (i = 0; i < this.annotations.size(); i++) {
/* 177 */       sb.append(((Annotation)this.annotations.get(i)).toString());
/* 178 */       if (i < this.annotations.size() - 1 || this.modifiers.size() > 0) {
/* 179 */         sb.append(' ');
/*     */       }
/*     */     } 
/* 182 */     for (i = 0; i < this.modifiers.size(); i++) {
/* 183 */       Integer modifier = this.modifiers.get(i);
/* 184 */       sb.append(MODIFIER_TEXT.get(modifier));
/* 185 */       if (i < this.modifiers.size() - 1) {
/* 186 */         sb.append(' ');
/*     */       }
/*     */     } 
/* 189 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\lang\Modifiers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */