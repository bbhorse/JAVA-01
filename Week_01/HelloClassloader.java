package org.java.lessons;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HelloClassloader extends ClassLoader {

    private static final int INT_255 = 255;
    private static final String HELLO_CLASS_NAME = "Hello";
    private static final String HELLO_METHOD_NAME = "hello";

    public static void main(String[] args) {
        try {
            final Object helloObj = new HelloClassloader().findClass(HELLO_CLASS_NAME).newInstance();
            final Method helloMethod = helloObj.getClass().getMethod(HELLO_METHOD_NAME);
            helloMethod.invoke(helloObj);
        } catch (InstantiationException ignore) {
            ignore.printStackTrace();
        } catch (IllegalAccessException ignore) {
            ignore.printStackTrace();
        } catch (ClassNotFoundException ignore) {
            ignore.printStackTrace();
        } catch (NoSuchMethodException ignore) {
            ignore.printStackTrace();
        } catch (InvocationTargetException ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = read255DivideBytes(Thread.currentThread().getContextClassLoader().getResourceAsStream("Hello.xlass"));
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] read255DivideBytes(InputStream input) {
        try {
            int oneByte = -2;
            int size = 0;
            byte[] bytes = new byte[1024];
            while ((oneByte = input.read()) >= 0) {
                bytes[size] = ((byte) (INT_255 - oneByte));
                if (size == bytes.length - 1) {
                    bytes = Arrays.copyOf(bytes, bytes.length * 2);
                }
                size++;
            }
            return Arrays.copyOf(bytes, size);
        } catch (IOException ioe) {
            //Do log exception
            throw new RuntimeException(ioe);
        }
    }
}
