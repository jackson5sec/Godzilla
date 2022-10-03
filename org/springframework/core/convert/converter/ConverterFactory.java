package org.springframework.core.convert.converter;

public interface ConverterFactory<S, R> {
  <T extends R> Converter<S, T> getConverter(Class<T> paramClass);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\converter\ConverterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */