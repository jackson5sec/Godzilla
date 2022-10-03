/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Repeatable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RepeatableContainers
/*     */ {
/*     */   @Nullable
/*     */   private final RepeatableContainers parent;
/*     */   
/*     */   private RepeatableContainers(@Nullable RepeatableContainers parent) {
/*  51 */     this.parent = parent;
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
/*     */   public RepeatableContainers and(Class<? extends Annotation> container, Class<? extends Annotation> repeatable) {
/*  65 */     return new ExplicitRepeatableContainer(this, repeatable, container);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   Annotation[] findRepeatedAnnotations(Annotation annotation) {
/*  70 */     if (this.parent == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     return this.parent.findRepeatedAnnotations(annotation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  79 */     if (other == this) {
/*  80 */       return true;
/*     */     }
/*  82 */     if (other == null || getClass() != other.getClass()) {
/*  83 */       return false;
/*     */     }
/*  85 */     return ObjectUtils.nullSafeEquals(this.parent, ((RepeatableContainers)other).parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  90 */     return ObjectUtils.nullSafeHashCode(this.parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RepeatableContainers standardRepeatables() {
/* 100 */     return StandardRepeatableContainers.INSTANCE;
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
/*     */   public static RepeatableContainers of(Class<? extends Annotation> repeatable, @Nullable Class<? extends Annotation> container) {
/* 117 */     return new ExplicitRepeatableContainer(null, repeatable, container);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RepeatableContainers none() {
/* 126 */     return NoRepeatableContainers.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StandardRepeatableContainers
/*     */     extends RepeatableContainers
/*     */   {
/* 136 */     private static final Map<Class<? extends Annotation>, Object> cache = (Map<Class<? extends Annotation>, Object>)new ConcurrentReferenceHashMap();
/*     */     
/* 138 */     private static final Object NONE = new Object();
/*     */     
/* 140 */     private static StandardRepeatableContainers INSTANCE = new StandardRepeatableContainers();
/*     */     
/*     */     StandardRepeatableContainers() {
/* 143 */       super(null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     Annotation[] findRepeatedAnnotations(Annotation annotation) {
/* 149 */       Method method = getRepeatedAnnotationsMethod(annotation.annotationType());
/* 150 */       if (method != null) {
/* 151 */         return (Annotation[])ReflectionUtils.invokeMethod(method, annotation);
/*     */       }
/* 153 */       return super.findRepeatedAnnotations(annotation);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static Method getRepeatedAnnotationsMethod(Class<? extends Annotation> annotationType) {
/* 158 */       Object result = cache.computeIfAbsent(annotationType, StandardRepeatableContainers::computeRepeatedAnnotationsMethod);
/*     */       
/* 160 */       return (result != NONE) ? (Method)result : null;
/*     */     }
/*     */     
/*     */     private static Object computeRepeatedAnnotationsMethod(Class<? extends Annotation> annotationType) {
/* 164 */       AttributeMethods methods = AttributeMethods.forAnnotationType(annotationType);
/* 165 */       if (methods.hasOnlyValueAttribute()) {
/* 166 */         Method method = methods.get(0);
/* 167 */         Class<?> returnType = method.getReturnType();
/* 168 */         if (returnType.isArray()) {
/* 169 */           Class<?> componentType = returnType.getComponentType();
/* 170 */           if (Annotation.class.isAssignableFrom(componentType) && componentType
/* 171 */             .isAnnotationPresent((Class)Repeatable.class)) {
/* 172 */             return method;
/*     */           }
/*     */         } 
/*     */       } 
/* 176 */       return NONE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ExplicitRepeatableContainer
/*     */     extends RepeatableContainers
/*     */   {
/*     */     private final Class<? extends Annotation> repeatable;
/*     */ 
/*     */     
/*     */     private final Class<? extends Annotation> container;
/*     */ 
/*     */     
/*     */     private final Method valueMethod;
/*     */ 
/*     */     
/*     */     ExplicitRepeatableContainer(@Nullable RepeatableContainers parent, Class<? extends Annotation> repeatable, @Nullable Class<? extends Annotation> container) {
/* 195 */       super(parent);
/* 196 */       Assert.notNull(repeatable, "Repeatable must not be null");
/* 197 */       if (container == null) {
/* 198 */         container = deduceContainer(repeatable);
/*     */       }
/* 200 */       Method valueMethod = AttributeMethods.forAnnotationType(container).get("value");
/*     */       try {
/* 202 */         if (valueMethod == null) {
/* 203 */           throw new NoSuchMethodException("No value method found");
/*     */         }
/* 205 */         Class<?> returnType = valueMethod.getReturnType();
/* 206 */         if (!returnType.isArray() || returnType.getComponentType() != repeatable) {
/* 207 */           throw new AnnotationConfigurationException("Container type [" + container
/* 208 */               .getName() + "] must declare a 'value' attribute for an array of type [" + repeatable
/*     */               
/* 210 */               .getName() + "]");
/*     */         }
/*     */       }
/* 213 */       catch (AnnotationConfigurationException ex) {
/* 214 */         throw ex;
/*     */       }
/* 216 */       catch (Throwable ex) {
/* 217 */         throw new AnnotationConfigurationException("Invalid declaration of container type [" + container
/* 218 */             .getName() + "] for repeatable annotation [" + repeatable
/* 219 */             .getName() + "]", ex);
/*     */       } 
/*     */       
/* 222 */       this.repeatable = repeatable;
/* 223 */       this.container = container;
/* 224 */       this.valueMethod = valueMethod;
/*     */     }
/*     */     
/*     */     private Class<? extends Annotation> deduceContainer(Class<? extends Annotation> repeatable) {
/* 228 */       Repeatable annotation = repeatable.<Repeatable>getAnnotation(Repeatable.class);
/* 229 */       Assert.notNull(annotation, () -> "Annotation type must be a repeatable annotation: failed to resolve container type for " + repeatable.getName());
/*     */       
/* 231 */       return annotation.value();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     Annotation[] findRepeatedAnnotations(Annotation annotation) {
/* 237 */       if (this.container.isAssignableFrom(annotation.annotationType())) {
/* 238 */         return (Annotation[])ReflectionUtils.invokeMethod(this.valueMethod, annotation);
/*     */       }
/* 240 */       return super.findRepeatedAnnotations(annotation);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 245 */       if (!super.equals(other)) {
/* 246 */         return false;
/*     */       }
/* 248 */       ExplicitRepeatableContainer otherErc = (ExplicitRepeatableContainer)other;
/* 249 */       return (this.container.equals(otherErc.container) && this.repeatable.equals(otherErc.repeatable));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 254 */       int hashCode = super.hashCode();
/* 255 */       hashCode = 31 * hashCode + this.container.hashCode();
/* 256 */       hashCode = 31 * hashCode + this.repeatable.hashCode();
/* 257 */       return hashCode;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NoRepeatableContainers
/*     */     extends RepeatableContainers
/*     */   {
/* 267 */     private static NoRepeatableContainers INSTANCE = new NoRepeatableContainers();
/*     */     
/*     */     NoRepeatableContainers() {
/* 270 */       super(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\RepeatableContainers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */