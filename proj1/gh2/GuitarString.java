package gh2;

// TODO: uncomment the following import once you're ready to start this portion
 import deque.ArrayDeque;
 import deque.Deque;
// TODO: maybe more imports

//Note: 在你完成双端队列（Deque）的实现之前，这个文件无法编译。
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday.
     * 常量。请勿修改。如果你好奇的话，关键字final意味着这些值在运行时不能被更改。我们将在周五的课上讨论这个以及其他主题。 */
    private static final int SR = 44100;      // Sampling Rate采样率
    private static final double DECAY = .996; // energy decay factor能量衰减因子

    /* Buffer for storing sound data. */
    // TODO: uncomment the following line once you're ready to start this portion
    //          准备好开始这部分内容后，取消对以下行的注释
    private Deque<Double> buffer = new ArrayDeque<>();

    /* Create a guitar string of the given frequency.
        创建一根具有给定频率的吉他弦 */
    public GuitarString(double frequency) {
        // TODO: Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this division operation into an int. For
        //       better accuracy, use the Math.round() function before casting.
        //       Your should initially fill your buffer array with zeros.
        // TODO: 创建一个容量为SR除以频率的缓冲区。你需要将这个除法运算的结果转换为整数。
        //       为了获得更高的精度，在转换之前使用Math.round()函数。你应该首先用零填充缓冲区数组。

        double N = (SR / frequency);
        N = Math.round(N);
        for (int i = 0; i < N; i++) {
            buffer.addLast(0.0);
        }

    }


    /* Pluck the guitar string by replacing the buffer with white noise.
    *   用白噪声替换缓冲区来拨动吉他弦。 */
    public void pluck() {
        // TODO: Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //      将缓冲区中的所有内容出队，并替换为-0.5到0.5之间的随机数。你可以使用以下方式获取这样的数字：
        //      double r = Math.random() - 0.5;
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        //       确保你的随机数互不相同。这并不意味着你需要检查这些数字是否互不相同。
        //       它的意思是，对于每个数组索引，你都应该重复调用Math.random() - 0.5来生成新的随机数。

        for (int i = 0; i < buffer.size(); i++) {
            buffer.removeFirst();
            double randomvalue = Math.random() - 0.5;
            buffer.addLast(randomvalue);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     * 通过执行一次卡尔普斯-斯特朗算法的迭代，将模拟推进一个时间步长。
     */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        //       将前端样本出队，并将一个新样本入队，该新样本是这两个样本的平均值乘以衰减系数。
        //       **不要调用StdAudio.play()。**

        double frontSample = buffer.removeFirst();
        double nextSample = buffer.get(0);
        double newSample = (frontSample + nextSample) * 0.5 * DECAY;
        buffer.addLast(newSample);
    }

    /* Return the double at the front of the buffer.
        返回缓冲区前端的双精度浮点数。 */
    public double sample() {
        // TODO: Return the correct thing.返回正确的东西。
        return buffer.get(0);
    }
}
