package uk.co.codelity.event.sourcing.core.utils.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ReflectionUtility {
    static Logger logger = LoggerFactory.getLogger(ReflectionUtility.class);

    private ReflectionUtility(){
    }

    public static Set<Method> getMethodsWithAnnotation(String packageName, Class<? extends Annotation> annotation) throws Exception {
        Set<Class<?>> classes = getClasses(packageName);
        return classes.stream()
                .flatMap(c -> Arrays.stream(c.getMethods()))
                .filter(method -> nonNull(method.getAnnotation(annotation)))
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> getClassesWithAnnotation(String packageName, Class<? extends Annotation> annotation) throws Exception {
        Set<Class<?>> classes = getClasses(packageName);
        return classes.stream().filter(c -> nonNull(c.getAnnotation(annotation)))
                .collect(Collectors.toSet());
    }
    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        Enumeration<URL> urls = getResources(packageName);
        Set<Class<?>> result = new HashSet<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            ResourceLookup resourceLookup = ResourceLookupFactory.create(url, packageName);
            if (nonNull(resourceLookup)) {
                Set<Class<?>> list = resourceLookup.getClasses();
                result.addAll(list);
            }
        }
        return result;
    }

    private static Enumeration<URL> getResources(String packageName) throws IOException {
        return ClassLoader.getSystemClassLoader()
                .getResources(packageName.replaceAll("[.]", "/"));
    }
}
