/*
 *  MIT License
 *
 *  Copyright (c) 2020 Michael Pogrebinsky - Java Reflection - Master Class
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tyoma17.fields_and_arrays.modification_and_arrays_Creation.configloader;

import com.tyoma17.fields_and_arrays.modification_and_arrays_Creation.data.GameConfig;
import com.tyoma17.fields_and_arrays.modification_and_arrays_Creation.data.UserInterfaceConfig;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static final Path GAME_CONFIG_PATH = getGameConfigPath("/game-properties.cfg");
    private static final Path UI_CONFIG_PATH = getGameConfigPath("/user-interface.cfg");

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        GameConfig gameConfig = createConfigObject(GameConfig.class, GAME_CONFIG_PATH);
        System.out.println(gameConfig);

        UserInterfaceConfig userInterfaceConfig = createConfigObject(UserInterfaceConfig.class, UI_CONFIG_PATH);
        System.out.println(userInterfaceConfig);
    }

    public static <T> T createConfigObject(Class<T> clazz, Path filePath) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Scanner scanner = new Scanner(filePath);

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T configInstance = (T) constructor.newInstance();

        while (scanner.hasNextLine()) {

            String configLine = scanner.nextLine();
            String[] nameValuePair = configLine.split("=");

            if (nameValuePair.length != 2) {
                continue;
            }

            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];

            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.err.println(String.format("Property name: %s is unsupported", propertyName));
                continue;
            }

            field.setAccessible(true);

            Object parsedValue;

            if (field.getType().isArray()) {
                parsedValue = parseArray(field.getType().getComponentType(), propertyValue);
            } else {
                parsedValue = parseValue(field.getType(), propertyValue);
            }

            field.set(configInstance, parsedValue);
        }

        return configInstance;
    }

    private static Object parseArray(Class<?> arrayElementType, String value) {
        
        String[] elementValues = value.split(",");
        Object arrayObject = Array.newInstance(arrayElementType, elementValues.length);
        
        for (int i = 0; i < elementValues.length; i++) {
            Array.set(arrayObject, i, parseValue(arrayElementType, elementValues[i]));
        }

        return arrayObject;
    }

    private static Object parseValue(Class<?> type, String value) {

        if (type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(String.class)) {
            return value;
        }

        throw new RuntimeException(String.format("Type: %s is not supported", type.getTypeName()));
    }

    private static Path getGameConfigPath(String pathString) {
        Path path;
        try {
            path = Paths.get(Main.class.getResource(pathString).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return path;
    }
}
