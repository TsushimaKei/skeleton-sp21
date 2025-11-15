package flik;

/* 导入 JUnit 库 */
import org.junit.Test;
import static org.junit.Assert.*;

public class Fliktest {


    @Test
    public void testIsSameNumber() {

        /* * 我们来测试一下这个 lab 发现 bug 的核心
         * 我们将使用 assertTrue(String message, boolean condition)
         * 如果 "condition" 是 false，测试就会失败并显示 "message"。
         */

        int a = 128;
        int b = 128;

        // 测试 128 (已知的 bug 点)
        // 我们预期 a 和 b 是相等的，所以 isSameNumber 应该返回 true
        // 注意：HorribleSteve 的代码在这里失败了，所以这个测试也会失败，
        // 这就帮我们抓住了 Flik.java 里的 bug！
        assertTrue("Bug found: 128 == 128 should be true, but Flik.isSameNumber returns false.",
                Flik.isSameNumber(a, b));

        // 我们也应该测试一下没 bug 的情况
        int i = 0;
        int j = 0;
        assertTrue("Test failed: 0 == 0 should be true.",
                Flik.isSameNumber(i, j));

        i = 127;
        j = 127;
        assertTrue("Test failed: 127 == 127 should be true.",
                Flik.isSameNumber(i, j));
    }

    /**
     * (可选) 我们可以写一个更强大的测试，
     * 模仿 HorribleSteve.java，用一个循环来找到 bug。
     */
    @Test
    public void testIsSameNumberLoop() {
        int i = 0;
        int j = 0; // 用 j 来和 i 保持一致，模拟 HorribleSteve 的代码

        while (i < 500) {

            // 我们断言 Flik.isSameNumber(i, j) 必须总是 true
            // 我们还提供了一个非常有用的失败信息
            assertTrue("Bug found when i = " + i, Flik.isSameNumber(i, j));

            i++;
            j++; // 保持 i 和 j 相等
        }
    }
}