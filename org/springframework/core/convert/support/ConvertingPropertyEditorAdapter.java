/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConvertingPropertyEditorAdapter
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   private final TypeDescriptor targetDescriptor;
/*    */   private final boolean canConvertToString;
/*    */   
/*    */   public ConvertingPropertyEditorAdapter(ConversionService conversionService, TypeDescriptor targetDescriptor) {
/* 50 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 51 */     Assert.notNull(targetDescriptor, "TypeDescriptor must not be null");
/* 52 */     this.conversionService = conversionService;
/* 53 */     this.targetDescriptor = targetDescriptor;
/* 54 */     this.canConvertToString = conversionService.canConvert(this.targetDescriptor, TypeDescriptor.valueOf(String.class));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(@Nullable String text) throws IllegalArgumentException {
/* 60 */     setValue(this.conversionService.convert(text, TypeDescriptor.valueOf(String.class), this.targetDescriptor));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getAsText() {
/* 66 */     if (this.canConvertToString) {
/* 67 */       return (String)this.conversionService.convert(getValue(), this.targetDescriptor, TypeDescriptor.valueOf(String.class));
/*    */     }
/*    */     
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ConvertingPropertyEditorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */