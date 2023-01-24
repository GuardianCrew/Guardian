package com.github.guardiancrew.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class Reflect {

    // FIXME: 1/24/2023 borken
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T>[] getClasses(String pkg, Class<T> clazz) throws ClassNotFoundException, URISyntaxException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class<? extends T>> classes = new ArrayList<>();
        String fileFormattedPkg = pkg.replace('.', '\\');
        for (Enumeration<URL> enumeration = classLoader.getResources(fileFormattedPkg); enumeration.hasMoreElements();) {
            URL url = enumeration.nextElement();
            File pkgAsFile = new File(url.toURI());
            File[] files = pkgAsFile.listFiles();
            if (files == null) return (Class<? extends T>[]) new Class<?>[0];
            for (File file : files) {
                String fString = file.toString();
                if (fString.endsWith(".class") && !(fString.endsWith(clazz.getSimpleName()))) {
                    String className = fString.substring(fString.lastIndexOf('\\')+1);
                    Class<?> claz = Class.forName(pkg + "." + className.replaceAll(".class", ""));
                    if (clazz.isAssignableFrom(claz)) {
                        classes.add((Class<? extends T>) claz);
                    }
                }
            }
        }
        return (Class<? extends T>[]) classes.toArray(new Class<?>[0]);
    }

    public static boolean classExists(String classPath) {
        try {
            Class.forName(classPath);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean methodExists(Class<?> c, String name, Class<?>... parameterTypes) {
        try {
            c.getDeclaredMethod(name, parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}
