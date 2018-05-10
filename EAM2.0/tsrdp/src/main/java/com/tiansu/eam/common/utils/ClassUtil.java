package com.tiansu.eam.common.utils;

/**
 * @author wangjl
 * @description
 * @create 2017-09-05 16:59
 **/

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找指定路径下面实现指定接口的全部类
 *
 * @author wangjl
 *
 */
public class ClassUtil {

    /**
     * 获取同一路径下所有子类或接口实现类
     *
     * @param cls
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<Class<T>> getAllAssignedClass(Class<T> cls,boolean containsAbstract) throws IOException,
            ClassNotFoundException {
        List<Class<T>> classes = new ArrayList<Class<T>>();
        for (Class<T> c : getClasses(cls)) {
            if(Modifier.isAbstract(c.getModifiers()) && !containsAbstract){
                continue;
            }
            if (cls.isAssignableFrom(c) && !cls.equals(c) ) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 取得当前类路径下的所有类
     *
     * @param cls
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<Class<T>> getClasses(Class<T> cls) throws IOException,
            ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    /**
     * 迭代查找类
     *
     * @param dir
     * @param pk
     * @return
     * @throws ClassNotFoundException
     */
    private static <T> List<Class<T>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<T>> classes = new ArrayList<Class<T>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add((Class<T>) Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }

}