/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public abstract class ReflectionHelper
/*     */ {
/*     */   @Nullable
/*     */   static ArgumentsMatchInfo compareArguments(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter) {
/*  59 */     Assert.isTrue((expectedArgTypes.size() == suppliedArgTypes.size()), "Expected argument types and supplied argument types should be arrays of same length");
/*     */ 
/*     */     
/*  62 */     ArgumentsMatchKind match = ArgumentsMatchKind.EXACT;
/*  63 */     for (int i = 0; i < expectedArgTypes.size() && match != null; i++) {
/*  64 */       TypeDescriptor suppliedArg = suppliedArgTypes.get(i);
/*  65 */       TypeDescriptor expectedArg = expectedArgTypes.get(i);
/*     */       
/*  67 */       if (suppliedArg == null) {
/*  68 */         if (expectedArg.isPrimitive()) {
/*  69 */           match = null;
/*     */         }
/*     */       }
/*  72 */       else if (!expectedArg.equals(suppliedArg)) {
/*  73 */         if (suppliedArg.isAssignableTo(expectedArg)) {
/*  74 */           if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
/*  75 */             match = ArgumentsMatchKind.CLOSE;
/*     */           }
/*     */         }
/*  78 */         else if (typeConverter.canConvert(suppliedArg, expectedArg)) {
/*  79 */           match = ArgumentsMatchKind.REQUIRES_CONVERSION;
/*     */         } else {
/*     */           
/*  82 */           match = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*  86 */     return (match != null) ? new ArgumentsMatchInfo(match) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTypeDifferenceWeight(List<TypeDescriptor> paramTypes, List<TypeDescriptor> argTypes) {
/*  93 */     int result = 0;
/*  94 */     for (int i = 0; i < paramTypes.size(); i++) {
/*  95 */       TypeDescriptor paramType = paramTypes.get(i);
/*  96 */       TypeDescriptor argType = (i < argTypes.size()) ? argTypes.get(i) : null;
/*  97 */       if (argType == null) {
/*  98 */         if (paramType.isPrimitive()) {
/*  99 */           return Integer.MAX_VALUE;
/*     */         }
/*     */       } else {
/*     */         
/* 103 */         Class<?> paramTypeClazz = paramType.getType();
/* 104 */         if (!ClassUtils.isAssignable(paramTypeClazz, argType.getType())) {
/* 105 */           return Integer.MAX_VALUE;
/*     */         }
/* 107 */         if (paramTypeClazz.isPrimitive()) {
/* 108 */           paramTypeClazz = Object.class;
/*     */         }
/* 110 */         Class<?> superClass = argType.getType().getSuperclass();
/* 111 */         while (superClass != null) {
/* 112 */           if (paramTypeClazz.equals(superClass)) {
/* 113 */             result += 2;
/* 114 */             superClass = null; continue;
/*     */           } 
/* 116 */           if (ClassUtils.isAssignable(paramTypeClazz, superClass)) {
/* 117 */             result += 2;
/* 118 */             superClass = superClass.getSuperclass();
/*     */             continue;
/*     */           } 
/* 121 */           superClass = null;
/*     */         } 
/*     */         
/* 124 */         if (paramTypeClazz.isInterface()) {
/* 125 */           result++;
/*     */         }
/*     */       } 
/*     */     } 
/* 129 */     return result;
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
/*     */   @Nullable
/*     */   static ArgumentsMatchInfo compareArgumentsVarargs(List<TypeDescriptor> expectedArgTypes, List<TypeDescriptor> suppliedArgTypes, TypeConverter typeConverter) {
/* 147 */     Assert.isTrue(!CollectionUtils.isEmpty(expectedArgTypes), "Expected arguments must at least include one array (the varargs parameter)");
/*     */     
/* 149 */     Assert.isTrue(((TypeDescriptor)expectedArgTypes.get(expectedArgTypes.size() - 1)).isArray(), "Final expected argument should be array type (the varargs parameter)");
/*     */ 
/*     */     
/* 152 */     ArgumentsMatchKind match = ArgumentsMatchKind.EXACT;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     int argCountUpToVarargs = expectedArgTypes.size() - 1;
/* 158 */     for (int i = 0; i < argCountUpToVarargs && match != null; i++) {
/* 159 */       TypeDescriptor suppliedArg = suppliedArgTypes.get(i);
/* 160 */       TypeDescriptor expectedArg = expectedArgTypes.get(i);
/* 161 */       if (suppliedArg == null) {
/* 162 */         if (expectedArg.isPrimitive()) {
/* 163 */           match = null;
/*     */         
/*     */         }
/*     */       }
/* 167 */       else if (!expectedArg.equals(suppliedArg)) {
/* 168 */         if (suppliedArg.isAssignableTo(expectedArg)) {
/* 169 */           if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
/* 170 */             match = ArgumentsMatchKind.CLOSE;
/*     */           }
/*     */         }
/* 173 */         else if (typeConverter.canConvert(suppliedArg, expectedArg)) {
/* 174 */           match = ArgumentsMatchKind.REQUIRES_CONVERSION;
/*     */         } else {
/*     */           
/* 177 */           match = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 184 */     if (match == null) {
/* 185 */       return null;
/*     */     }
/*     */     
/* 188 */     if (suppliedArgTypes.size() != expectedArgTypes.size() || 
/* 189 */       !((TypeDescriptor)expectedArgTypes.get(expectedArgTypes.size() - 1)).equals(suppliedArgTypes
/* 190 */         .get(suppliedArgTypes.size() - 1))) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 197 */       TypeDescriptor varargsDesc = expectedArgTypes.get(expectedArgTypes.size() - 1);
/* 198 */       TypeDescriptor elementDesc = varargsDesc.getElementTypeDescriptor();
/* 199 */       Assert.state((elementDesc != null), "No element type");
/* 200 */       Class<?> varargsParamType = elementDesc.getType();
/*     */ 
/*     */       
/* 203 */       for (int j = expectedArgTypes.size() - 1; j < suppliedArgTypes.size(); j++) {
/* 204 */         TypeDescriptor suppliedArg = suppliedArgTypes.get(j);
/* 205 */         if (suppliedArg == null) {
/* 206 */           if (varargsParamType.isPrimitive()) {
/* 207 */             match = null;
/*     */           
/*     */           }
/*     */         }
/* 211 */         else if (varargsParamType != suppliedArg.getType()) {
/* 212 */           if (ClassUtils.isAssignable(varargsParamType, suppliedArg.getType())) {
/* 213 */             if (match != ArgumentsMatchKind.REQUIRES_CONVERSION) {
/* 214 */               match = ArgumentsMatchKind.CLOSE;
/*     */             }
/*     */           }
/* 217 */           else if (typeConverter.canConvert(suppliedArg, TypeDescriptor.valueOf(varargsParamType))) {
/* 218 */             match = ArgumentsMatchKind.REQUIRES_CONVERSION;
/*     */           } else {
/*     */             
/* 221 */             match = null;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 228 */     return (match != null) ? new ArgumentsMatchInfo(match) : null;
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
/*     */   
/*     */   public static boolean convertAllArguments(TypeConverter converter, Object[] arguments, Method method) throws SpelEvaluationException {
/* 249 */     Integer varargsPosition = method.isVarArgs() ? Integer.valueOf(method.getParameterCount() - 1) : null;
/* 250 */     return convertArguments(converter, arguments, method, varargsPosition);
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
/*     */   static boolean convertArguments(TypeConverter converter, Object[] arguments, Executable executable, @Nullable Integer varargsPosition) throws EvaluationException {
/*     */     int i;
/* 267 */     boolean conversionOccurred = false;
/* 268 */     if (varargsPosition == null) {
/* 269 */       for (int j = 0; j < arguments.length; j++) {
/* 270 */         TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forExecutable(executable, j));
/* 271 */         Object argument = arguments[j];
/* 272 */         arguments[j] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 273 */         i = conversionOccurred | ((argument != arguments[j]) ? 1 : 0);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 278 */       for (int j = 0; j < varargsPosition.intValue(); j++) {
/* 279 */         TypeDescriptor targetType = new TypeDescriptor(MethodParameter.forExecutable(executable, j));
/* 280 */         Object argument = arguments[j];
/* 281 */         arguments[j] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 282 */         i |= (argument != arguments[j]) ? 1 : 0;
/*     */       } 
/* 284 */       MethodParameter methodParam = MethodParameter.forExecutable(executable, varargsPosition.intValue());
/* 285 */       if (varargsPosition.intValue() == arguments.length - 1) {
/*     */ 
/*     */         
/* 288 */         TypeDescriptor targetType = new TypeDescriptor(methodParam);
/* 289 */         Object argument = arguments[varargsPosition.intValue()];
/* 290 */         TypeDescriptor sourceType = TypeDescriptor.forObject(argument);
/* 291 */         arguments[varargsPosition.intValue()] = converter.convertValue(argument, sourceType, targetType);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 296 */         if (argument != arguments[varargsPosition.intValue()] && 
/* 297 */           !isFirstEntryInArray(argument, arguments[varargsPosition.intValue()])) {
/* 298 */           i = 1;
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 303 */         TypeDescriptor targetType = (new TypeDescriptor(methodParam)).getElementTypeDescriptor();
/* 304 */         Assert.state((targetType != null), "No element type");
/* 305 */         for (int k = varargsPosition.intValue(); k < arguments.length; k++) {
/* 306 */           Object argument = arguments[k];
/* 307 */           arguments[k] = converter.convertValue(argument, TypeDescriptor.forObject(argument), targetType);
/* 308 */           i |= (argument != arguments[k]) ? 1 : 0;
/*     */         } 
/*     */       } 
/*     */     } 
/* 312 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isFirstEntryInArray(Object value, @Nullable Object possibleArray) {
/* 322 */     if (possibleArray == null) {
/* 323 */       return false;
/*     */     }
/* 325 */     Class<?> type = possibleArray.getClass();
/* 326 */     if (!type.isArray() || Array.getLength(possibleArray) == 0 || 
/* 327 */       !ClassUtils.isAssignableValue(type.getComponentType(), value)) {
/* 328 */       return false;
/*     */     }
/* 330 */     Object arrayValue = Array.get(possibleArray, 0);
/* 331 */     return type.getComponentType().isPrimitive() ? arrayValue.equals(value) : ((arrayValue == value));
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
/*     */   public static Object[] setupArgumentsForVarargsInvocation(Class<?>[] requiredParameterTypes, Object... args) {
/* 345 */     int parameterCount = requiredParameterTypes.length;
/* 346 */     int argumentCount = args.length;
/*     */ 
/*     */     
/* 349 */     if (parameterCount != args.length || requiredParameterTypes[parameterCount - 1] != ((args[argumentCount - 1] != null) ? args[argumentCount - 1]
/*     */       
/* 351 */       .getClass() : null)) {
/*     */       
/* 353 */       int arraySize = 0;
/* 354 */       if (argumentCount >= parameterCount) {
/* 355 */         arraySize = argumentCount - parameterCount - 1;
/*     */       }
/*     */ 
/*     */       
/* 359 */       Object[] newArgs = new Object[parameterCount];
/* 360 */       System.arraycopy(args, 0, newArgs, 0, newArgs.length - 1);
/*     */ 
/*     */ 
/*     */       
/* 364 */       Class<?> componentType = requiredParameterTypes[parameterCount - 1].getComponentType();
/* 365 */       Object repackagedArgs = Array.newInstance(componentType, arraySize);
/* 366 */       for (int i = 0; i < arraySize; i++) {
/* 367 */         Array.set(repackagedArgs, i, args[parameterCount - 1 + i]);
/*     */       }
/* 369 */       newArgs[newArgs.length - 1] = repackagedArgs;
/* 370 */       return newArgs;
/*     */     } 
/* 372 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum ArgumentsMatchKind
/*     */   {
/* 382 */     EXACT,
/*     */ 
/*     */     
/* 385 */     CLOSE,
/*     */ 
/*     */     
/* 388 */     REQUIRES_CONVERSION;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ArgumentsMatchInfo
/*     */   {
/*     */     private final ReflectionHelper.ArgumentsMatchKind kind;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ArgumentsMatchInfo(ReflectionHelper.ArgumentsMatchKind kind) {
/* 404 */       this.kind = kind;
/*     */     }
/*     */     
/*     */     public boolean isExactMatch() {
/* 408 */       return (this.kind == ReflectionHelper.ArgumentsMatchKind.EXACT);
/*     */     }
/*     */     
/*     */     public boolean isCloseMatch() {
/* 412 */       return (this.kind == ReflectionHelper.ArgumentsMatchKind.CLOSE);
/*     */     }
/*     */     
/*     */     public boolean isMatchRequiringConversion() {
/* 416 */       return (this.kind == ReflectionHelper.ArgumentsMatchKind.REQUIRES_CONVERSION);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 421 */       return "ArgumentMatchInfo: " + this.kind;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\ReflectionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */