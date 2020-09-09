package Core;

import Exceptions.CanNotCastResultException;

import java.lang.reflect.*;
import java.sql.*;

public class Tools {
    public static Object getResult(ResultSet res, int num, Type type) {
        try {
            if (type == String.class) {
                return (Object) res.getString(num);
            } else if (type == Date.class) {
                return (Object) res.getDate(num);
            } else if (type == int.class) {
                return (Object) res.getInt(num);
            } else if (type == double.class) {
                return (Object) res.getDouble(num);
            } else if (type == float.class) {
                return (Object) res.getFloat(num);
            } else if (type == Blob.class) {
                return (Object) res.getBlob(num);
            } else if (type == Clob.class) {
                return (Object) res.getClob(num);
            } else if (type == byte.class) {
                return (Object) res.getByte(num);
            } else {
                throw new CanNotCastResultException();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Object copy(Object object) {
        Class<?> classType = object.getClass();
        Constructor<?> constructor = null;
        try {
            constructor = classType.getConstructor(new Class<?>[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object objectCopy = null;
        try {
            objectCopy = constructor.newInstance(new Object[]{});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Field fields[] = classType.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String firstLetter = name.substring(0, 1).toUpperCase();
            String getMethodName = "get" + firstLetter + name.substring(1);
            String setMethodName = "set" + firstLetter + name.substring(1);
            Method getMethod = null;
            Method setMethod = null;
            try {
                getMethod = classType.getMethod(getMethodName, new Class<?>[]{});
                setMethod = classType.getMethod(setMethodName, new Class<?>[]{field.getType()});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Object value = null;
            try {
                value = getMethod.invoke(object, new Object[]{});
                setMethod.invoke(objectCopy, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return objectCopy;
    }
}
