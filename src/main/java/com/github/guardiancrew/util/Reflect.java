package com.github.guardiancrew.util;

import com.github.guardiancrew.Guardian;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;

public class Reflect {

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T>[] getClasses(String pkg, Class<T> clazz) throws ClassNotFoundException, URISyntaxException, IOException {
        Guardian instance = Guardian.getInstance();
        List<Class<? extends T>> classes = new ArrayList<>();
        pkg = pkg.replace('.', '/');
        for (Enumeration<JarEntry> enumeration = instance.getJarFile().entries(); enumeration.hasMoreElements();) {
            JarEntry entry = enumeration.nextElement();
            if (entry.isDirectory() || !entry.getName().startsWith(pkg) || !entry.getName().endsWith(".class"))
                continue;
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - ".class".length());
            Class<?> c = Class.forName(className);
            if (clazz.isAssignableFrom(c))
                classes.add((Class<? extends T>) c);
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
