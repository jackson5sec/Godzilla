/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class TypeUtils
/*     */ {
/*     */   public static boolean isAssignable(Type lhsType, Type rhsType) {
/*  45 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  46 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*     */ 
/*     */     
/*  49 */     if (lhsType.equals(rhsType) || Object.class == lhsType) {
/*  50 */       return true;
/*     */     }
/*     */     
/*  53 */     if (lhsType instanceof Class) {
/*  54 */       Class<?> lhsClass = (Class)lhsType;
/*     */ 
/*     */       
/*  57 */       if (rhsType instanceof Class) {
/*  58 */         return ClassUtils.isAssignable(lhsClass, (Class)rhsType);
/*     */       }
/*     */       
/*  61 */       if (rhsType instanceof ParameterizedType) {
/*  62 */         Type rhsRaw = ((ParameterizedType)rhsType).getRawType();
/*     */ 
/*     */         
/*  65 */         if (rhsRaw instanceof Class) {
/*  66 */           return ClassUtils.isAssignable(lhsClass, (Class)rhsRaw);
/*     */         }
/*     */       }
/*  69 */       else if (lhsClass.isArray() && rhsType instanceof GenericArrayType) {
/*  70 */         Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
/*     */         
/*  72 */         return isAssignable(lhsClass.getComponentType(), rhsComponent);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  77 */     if (lhsType instanceof ParameterizedType) {
/*  78 */       if (rhsType instanceof Class) {
/*  79 */         Type lhsRaw = ((ParameterizedType)lhsType).getRawType();
/*     */         
/*  81 */         if (lhsRaw instanceof Class) {
/*  82 */           return ClassUtils.isAssignable((Class)lhsRaw, (Class)rhsType);
/*     */         }
/*     */       }
/*  85 */       else if (rhsType instanceof ParameterizedType) {
/*  86 */         return isAssignable((ParameterizedType)lhsType, (ParameterizedType)rhsType);
/*     */       } 
/*     */     }
/*     */     
/*  90 */     if (lhsType instanceof GenericArrayType) {
/*  91 */       Type lhsComponent = ((GenericArrayType)lhsType).getGenericComponentType();
/*     */       
/*  93 */       if (rhsType instanceof Class) {
/*  94 */         Class<?> rhsClass = (Class)rhsType;
/*     */         
/*  96 */         if (rhsClass.isArray()) {
/*  97 */           return isAssignable(lhsComponent, rhsClass.getComponentType());
/*     */         }
/*     */       }
/* 100 */       else if (rhsType instanceof GenericArrayType) {
/* 101 */         Type rhsComponent = ((GenericArrayType)rhsType).getGenericComponentType();
/*     */         
/* 103 */         return isAssignable(lhsComponent, rhsComponent);
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     if (lhsType instanceof WildcardType) {
/* 108 */       return isAssignable((WildcardType)lhsType, rhsType);
/*     */     }
/*     */     
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isAssignable(ParameterizedType lhsType, ParameterizedType rhsType) {
/* 115 */     if (lhsType.equals(rhsType)) {
/* 116 */       return true;
/*     */     }
/*     */     
/* 119 */     Type[] lhsTypeArguments = lhsType.getActualTypeArguments();
/* 120 */     Type[] rhsTypeArguments = rhsType.getActualTypeArguments();
/*     */     
/* 122 */     if (lhsTypeArguments.length != rhsTypeArguments.length) {
/* 123 */       return false;
/*     */     }
/*     */     
/* 126 */     for (int size = lhsTypeArguments.length, i = 0; i < size; i++) {
/* 127 */       Type lhsArg = lhsTypeArguments[i];
/* 128 */       Type rhsArg = rhsTypeArguments[i];
/*     */       
/* 130 */       if (!lhsArg.equals(rhsArg) && (!(lhsArg instanceof WildcardType) || 
/* 131 */         !isAssignable((WildcardType)lhsArg, rhsArg))) {
/* 132 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isAssignable(WildcardType lhsType, Type rhsType) {
/* 140 */     Type[] lUpperBounds = lhsType.getUpperBounds();
/*     */ 
/*     */     
/* 143 */     if (lUpperBounds.length == 0) {
/* 144 */       lUpperBounds = new Type[] { Object.class };
/*     */     }
/*     */     
/* 147 */     Type[] lLowerBounds = lhsType.getLowerBounds();
/*     */ 
/*     */     
/* 150 */     if (lLowerBounds.length == 0) {
/* 151 */       lLowerBounds = new Type[] { null };
/*     */     }
/*     */     
/* 154 */     if (rhsType instanceof WildcardType) {
/*     */ 
/*     */ 
/*     */       
/* 158 */       WildcardType rhsWcType = (WildcardType)rhsType;
/* 159 */       Type[] rUpperBounds = rhsWcType.getUpperBounds();
/*     */       
/* 161 */       if (rUpperBounds.length == 0) {
/* 162 */         rUpperBounds = new Type[] { Object.class };
/*     */       }
/*     */       
/* 165 */       Type[] rLowerBounds = rhsWcType.getLowerBounds();
/*     */       
/* 167 */       if (rLowerBounds.length == 0) {
/* 168 */         rLowerBounds = new Type[] { null };
/*     */       }
/*     */       
/* 171 */       for (Type lBound : lUpperBounds) {
/* 172 */         for (Type rBound : rUpperBounds) {
/* 173 */           if (!isAssignableBound(lBound, rBound)) {
/* 174 */             return false;
/*     */           }
/*     */         } 
/*     */         
/* 178 */         for (Type rBound : rLowerBounds) {
/* 179 */           if (!isAssignableBound(lBound, rBound)) {
/* 180 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 185 */       for (Type lBound : lLowerBounds) {
/* 186 */         for (Type rBound : rUpperBounds) {
/* 187 */           if (!isAssignableBound(rBound, lBound)) {
/* 188 */             return false;
/*     */           }
/*     */         } 
/*     */         
/* 192 */         for (Type rBound : rLowerBounds) {
/* 193 */           if (!isAssignableBound(rBound, lBound)) {
/* 194 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 200 */       for (Type lBound : lUpperBounds) {
/* 201 */         if (!isAssignableBound(lBound, rhsType)) {
/* 202 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 206 */       for (Type lBound : lLowerBounds) {
/* 207 */         if (!isAssignableBound(rhsType, lBound)) {
/* 208 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isAssignableBound(@Nullable Type lhsType, @Nullable Type rhsType) {
/* 217 */     if (rhsType == null) {
/* 218 */       return true;
/*     */     }
/* 220 */     if (lhsType == null) {
/* 221 */       return false;
/*     */     }
/* 223 */     return isAssignable(lhsType, rhsType);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\TypeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */