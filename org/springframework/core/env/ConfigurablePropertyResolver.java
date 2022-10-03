package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.lang.Nullable;

public interface ConfigurablePropertyResolver extends PropertyResolver {
  ConfigurableConversionService getConversionService();
  
  void setConversionService(ConfigurableConversionService paramConfigurableConversionService);
  
  void setPlaceholderPrefix(String paramString);
  
  void setPlaceholderSuffix(String paramString);
  
  void setValueSeparator(@Nullable String paramString);
  
  void setIgnoreUnresolvableNestedPlaceholders(boolean paramBoolean);
  
  void setRequiredProperties(String... paramVarArgs);
  
  void validateRequiredProperties() throws MissingRequiredPropertiesException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\ConfigurablePropertyResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */