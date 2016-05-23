package com.univocity.api;

import java.util.*;

/**
 * This is the entry point to the uniVocity data integration engine. It connects the resources in this API to their actual implementations in univocity's jars.
 *
 * <p>In some circumstances, you might need to configure the class loader before being able to obtain instances of {@link CommonFactoryProvider} from univocity.jar.
 *    If that is the case, use the {@link #setClassLoader(ClassLoader)} method before calling the {@link #provider()} method.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 *
 */
public final class Builder {

	private static ServiceLoader<CommonFactoryProvider> factoryProviderLoader = ServiceLoader.load(CommonFactoryProvider.class);

	private static CommonFactoryProvider loadProvider() {
		Exception error = null;
		try {
			for (CommonFactoryProvider provider : factoryProviderLoader) {
				return provider;
			}
		} catch(Exception e){
			error = e;
		}
		throw new IllegalStateException("Unable to load provider. You might need to use a different classloader in order to load it from uniVocity's jar file", error);
	}

	private static CommonFactoryProvider provider;

	/**
	 * Defines the class loader to be used to load uniVocity implementation classes (from univocity.jar)
	 *
	 * @param classLoader The class loader to be used to load provider classes, or <tt>null</tt> if the system class loader is to be used.
	 */
	public static final synchronized void setClassLoader(ClassLoader classLoader) {
		factoryProviderLoader = ServiceLoader.load(CommonFactoryProvider.class, classLoader);
	}

	private static final synchronized CommonFactoryProvider provider() {
		if (provider == null) {
			provider = loadProvider();
		}

		return provider;
	}

	public static final <T> T build(Class<T> builderType, Object ... args){
		return provider().build(builderType, args);
	}
}