package org.springframework.core.type.filter;

import java.io.IOException;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

@FunctionalInterface
public interface TypeFilter {
  boolean match(MetadataReader paramMetadataReader, MetadataReaderFactory paramMetadataReaderFactory) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\filter\TypeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */