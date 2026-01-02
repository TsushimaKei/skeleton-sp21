package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Commit implements Serializable {

    /** 提交信息 */
    private String message;

    /** 第一父提交（普通提交的 parent） */
    private String parent1;

    /** 第二父提交（merge commit 才有；普通提交为 null） */
    private String parent2;

    /** 时间戳 */
    private Date timestamp;

    /** 跟踪文件：文件名 -> blobId */
    private HashMap<String, String> blobs;

    /** 普通 commit 构造器 */
    public Commit(String message, String parent1, Date timestamp, HashMap<String, String> blobs) {
        this(message, parent1, null, timestamp, blobs);
    }

    /** merge commit 构造器 */
    public Commit(String message, String parent1, String parent2, Date timestamp, HashMap<String, String> blobs) {
        this.message = message;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.timestamp = timestamp;
        // 防止外部引用污染
        this.blobs = new HashMap<>(blobs);
    }

    public String getBlobId(String filename) {
        return blobs.get(filename);
    }

    /** 返回拷贝，防止外部乱改历史 */
    public HashMap<String, String> getblobs() {
        return new HashMap<>(blobs);
    }

    public String getMessage() {
        return message;
    }

    /** 兼容你现有 Repository.log() 的写法：沿 parent1 走 */
    public String getParent() {
        return parent1;
    }

    public String getParent1() {
        return parent1;
    }

    public String getParent2() {
        return parent2;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isMergeCommit() {
        return parent2 != null;
    }
}
