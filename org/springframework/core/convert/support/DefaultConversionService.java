/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
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
/*     */ public class DefaultConversionService
/*     */   extends GenericConversionService
/*     */ {
/*     */   @Nullable
/*     */   private static volatile DefaultConversionService sharedInstance;
/*     */   
/*     */   public DefaultConversionService() {
/*  52 */     addDefaultConverters(this);
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
/*     */   public static ConversionService getSharedInstance() {
/*  68 */     DefaultConversionService cs = sharedInstance;
/*  69 */     if (cs == null) {
/*  70 */       synchronized (DefaultConversionService.class) {
/*  71 */         cs = sharedInstance;
/*  72 */         if (cs == null) {
/*  73 */           cs = new DefaultConversionService();
/*  74 */           sharedInstance = cs;
/*     */         } 
/*     */       } 
/*     */     }
/*  78 */     return cs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDefaultConverters(ConverterRegistry converterRegistry) {
/*  88 */     addScalarConverters(converterRegistry);
/*  89 */     addCollectionConverters(converterRegistry);
/*     */     
/*  91 */     converterRegistry.addConverter((GenericConverter)new ByteBufferConverter((ConversionService)converterRegistry));
/*  92 */     converterRegistry.addConverter(new StringToTimeZoneConverter());
/*  93 */     converterRegistry.addConverter(new ZoneIdToTimeZoneConverter());
/*  94 */     converterRegistry.addConverter(new ZonedDateTimeToCalendarConverter());
/*     */     
/*  96 */     converterRegistry.addConverter((GenericConverter)new ObjectToObjectConverter());
/*  97 */     converterRegistry.addConverter((GenericConverter)new IdToEntityConverter((ConversionService)converterRegistry));
/*  98 */     converterRegistry.addConverter((GenericConverter)new FallbackObjectToStringConverter());
/*  99 */     converterRegistry.addConverter((GenericConverter)new ObjectToOptionalConverter((ConversionService)converterRegistry));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addCollectionConverters(ConverterRegistry converterRegistry) {
/* 110 */     ConversionService conversionService = (ConversionService)converterRegistry;
/*     */     
/* 112 */     converterRegistry.addConverter((GenericConverter)new ArrayToCollectionConverter(conversionService));
/* 113 */     converterRegistry.addConverter((GenericConverter)new CollectionToArrayConverter(conversionService));
/*     */     
/* 115 */     converterRegistry.addConverter((GenericConverter)new ArrayToArrayConverter(conversionService));
/* 116 */     converterRegistry.addConverter((GenericConverter)new CollectionToCollectionConverter(conversionService));
/* 117 */     converterRegistry.addConverter((GenericConverter)new MapToMapConverter(conversionService));
/*     */     
/* 119 */     converterRegistry.addConverter((GenericConverter)new ArrayToStringConverter(conversionService));
/* 120 */     converterRegistry.addConverter((GenericConverter)new StringToArrayConverter(conversionService));
/*     */     
/* 122 */     converterRegistry.addConverter((GenericConverter)new ArrayToObjectConverter(conversionService));
/* 123 */     converterRegistry.addConverter((GenericConverter)new ObjectToArrayConverter(conversionService));
/*     */     
/* 125 */     converterRegistry.addConverter((GenericConverter)new CollectionToStringConverter(conversionService));
/* 126 */     converterRegistry.addConverter((GenericConverter)new StringToCollectionConverter(conversionService));
/*     */     
/* 128 */     converterRegistry.addConverter((GenericConverter)new CollectionToObjectConverter(conversionService));
/* 129 */     converterRegistry.addConverter((GenericConverter)new ObjectToCollectionConverter(conversionService));
/*     */     
/* 131 */     converterRegistry.addConverter((GenericConverter)new StreamConverter(conversionService));
/*     */   }
/*     */   
/*     */   private static void addScalarConverters(ConverterRegistry converterRegistry) {
/* 135 */     converterRegistry.addConverterFactory(new NumberToNumberConverterFactory());
/*     */     
/* 137 */     converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
/* 138 */     converterRegistry.addConverter(Number.class, String.class, new ObjectToStringConverter());
/*     */     
/* 140 */     converterRegistry.addConverter(new StringToCharacterConverter());
/* 141 */     converterRegistry.addConverter(Character.class, String.class, new ObjectToStringConverter());
/*     */     
/* 143 */     converterRegistry.addConverter(new NumberToCharacterConverter());
/* 144 */     converterRegistry.addConverterFactory(new CharacterToNumberFactory());
/*     */     
/* 146 */     converterRegistry.addConverter(new StringToBooleanConverter());
/* 147 */     converterRegistry.addConverter(Boolean.class, String.class, new ObjectToStringConverter());
/*     */     
/* 149 */     converterRegistry.addConverterFactory(new StringToEnumConverterFactory());
/* 150 */     converterRegistry.addConverter(new EnumToStringConverter((ConversionService)converterRegistry));
/*     */     
/* 152 */     converterRegistry.addConverterFactory(new IntegerToEnumConverterFactory());
/* 153 */     converterRegistry.addConverter(new EnumToIntegerConverter((ConversionService)converterRegistry));
/*     */     
/* 155 */     converterRegistry.addConverter(new StringToLocaleConverter());
/* 156 */     converterRegistry.addConverter(Locale.class, String.class, new ObjectToStringConverter());
/*     */     
/* 158 */     converterRegistry.addConverter(new StringToCharsetConverter());
/* 159 */     converterRegistry.addConverter(Charset.class, String.class, new ObjectToStringConverter());
/*     */     
/* 161 */     converterRegistry.addConverter(new StringToCurrencyConverter());
/* 162 */     converterRegistry.addConverter(Currency.class, String.class, new ObjectToStringConverter());
/*     */     
/* 164 */     converterRegistry.addConverter(new StringToPropertiesConverter());
/* 165 */     converterRegistry.addConverter(new PropertiesToStringConverter());
/*     */     
/* 167 */     converterRegistry.addConverter(new StringToUUIDConverter());
/* 168 */     converterRegistry.addConverter(UUID.class, String.class, new ObjectToStringConverter());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\DefaultConversionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */