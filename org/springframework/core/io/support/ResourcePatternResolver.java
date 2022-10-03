package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public interface ResourcePatternResolver extends ResourceLoader {
  public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
  
  Resource[] getResources(String paramString) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\ResourcePatternResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */