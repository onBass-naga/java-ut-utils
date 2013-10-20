package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by naga on 13/10/19.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        MethodInvokerTest.正常系テスト.class,
        MethodInvokerTest.異常系テスト.class
})
public class MethodInvokerTest {

    public static class 正常系テスト {

        @Test public void 引数なしメソッドの実行() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting");
            String actual = sut.invokeAndReturn(String.class);
            assertThat(actual, is("Hi !!"));
        }

        @Test public void 引数なしstaticメソッドの実行() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting2");
            String actual = sut.invokeAndReturn(String.class);
            assertThat(actual, is("Hello."));
        }

        @Test public void 引数ありメソッドの実行() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting");
            sut.arg("Tom", String.class);
            String actual = sut.invokeAndReturn(String.class);
            assertThat(actual, is("Hi, Tom !!"));
        }

        @Test public void 複数引数ありメソッドの実行() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting");
            sut.args(new Object[] {"Tom", 3}, new Class[] {String.class, int.class});
            String actual = sut.invokeAndReturn(String.class);
            assertThat(actual, is("Hi, Tom3 !!"));
        }

        @Test public void メソッドが戻り値を持たない場合はnullが返却される() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "doNothing");
            String actual = sut.invokeAndReturn(String.class);
            assertThat(actual, is(nullValue()));
        }
    }

    public static class 異常系テスト {

        @Test(expected= Exception.class)
        public void テスト対象メソッドが存在しない場合は例外を投げる() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "non Exists Methood");
            sut.invoke();
        }

        @Test(expected= Exception.class)
        public void 引数のタイプがnullの場合は例外を投げる() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting");
            sut.arg("someone", null);
            sut.invoke();
        }

        @Test(expected= Exception.class)
        public void 複数引数の値配列がnullの場合は例外を投げる() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting");
            sut.args(null, new Class[]{String.class});
            sut.invoke();
        }

        @Test(expected= Exception.class)
        public void 複数引数のタイプ配列がnullの場合は例外を投げる() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting");
            sut.args(new Object[] {"someone"}, null);
            sut.invoke();
        }

        @Test(expected= Exception.class)
        public void 複数引数の値配列とタイプ配列の要素数が異なる場合は例外を投げる() {
            MethodInvoker sut = new MethodInvoker(new Hoge(), "greeting");
            sut.args(new Object[] {"someone"}, new Class[] {String.class, int.class});
            sut.invoke();
        }
    }
}
