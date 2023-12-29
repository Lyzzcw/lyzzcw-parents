package lyzzcw.work.component.sqi.factory;



import lyzzcw.work.component.sqi.annotation.SPI;
import lyzzcw.work.component.sqi.annotation.SPIClass;
import lyzzcw.work.component.sqi.loader.ExtensionLoader;

import java.util.Optional;

/**
 * SpiExtensionFactory
 */
@SPIClass
public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(final String key, final Class<T> clazz) {
        return Optional.ofNullable(clazz)
                .filter(Class::isInterface)
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                .map(ExtensionLoader::getDefaultSpiClassInstance)
                .orElse(null);
    }
}
