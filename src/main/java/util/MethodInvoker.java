package util;

import java.lang.reflect.Method;

/**
 * 可視性を意識せずにメソッドを実行するためのユーティリティ・クラスです.
 * @author naga
 */
public class MethodInvoker {

    private Class clazz;
    private Object targetInstance;
    private String methodName;
    private Object[] argValues;
    private Class[] argTypes;

    /**
     * メソッド実行対象クラスのインスタンスと実行メソッド名を引数とするコンストラクタ.
     * @param instance メソッド実行対象クラスのインスタンス ｎｕｌｌの場合の挙動は保証しません
     * @param methodName 実行メソッド名 ｎｕｌｌの場合の挙動は保証しません
     */
    public MethodInvoker(Object instance, String methodName) {
        this.targetInstance = instance;
        this.clazz = instance.getClass();
        this.methodName = methodName;
    }

    /**
     * 実行対象メソッドが複数の引数を必要とする場合に使用します.
     * @param values 実行時の引数の実値 実行対象メソッドのシグニチャに定義されている順番で配列にセットしてください
     * @param types 実行時の引数の型 リフレクションにより対象メソッドを特定するために指定が必要です。
     *              引数の実値に対応した順番で配列にセットしてください。
     */
    public void args(Object[] values, Class[] types) {
        if (values == null || types == null
                || values.length != types.length) {
            throw new IllegalArgumentException();
        }

        this.argValues = values;
        this.argTypes = types;
    }

    /**
     * 実行対象メソッドが引数を必要とする場合に使用します.
     * @param value 実行時の引数の実値
     * @param type 実行時の引数の型 リフレクションにより対象メソッドを特定するために指定が必要です。
     */
    public void arg(Object value, Class type) {
        if (type == null) {
            throw new IllegalArgumentException();
        }

        this.argValues = new Object[] {value};
        this.argTypes = new Class[] { type };
    }

    /**
     * メソッドを実行します.
     * @return メソッドに戻り値がある場合、{@code Object}型で返却します.
     *         戻り値の型を指定したい場合、{@link #invokeAndReturn(Class)}を使用してください。
     * @throws RuntimeException 例外はすべて{@link RuntimeException}にラップして投げます
     */
    @SuppressWarnings("unchecked")
    public Object invoke() {
        try {
            Method method;
            if (hasArgs()) {
                method = clazz.getDeclaredMethod(methodName, argTypes);
                method.setAccessible(true);
                return method.invoke(targetInstance, argValues);
            } else {
                method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                return method.invoke(targetInstance);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * メソッド実行後、戻り値を指定した型にキャストし返却します.
     * @param type 戻り値の型
     * @param <E> 型パラメータ
     * @return 指定した型にキャストした戻り値
     * @throws RuntimeException 例外はすべて{@link RuntimeException}にラップして投げます
     */
    @SuppressWarnings("unchecked")
    public <E> E invokeAndReturn(Class<E> type) {
        return (E) this.invoke();
    }

    private boolean hasArgs() {
        return argValues != null;
    }
}
