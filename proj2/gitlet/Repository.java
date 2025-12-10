package gitlet;

import javax.security.sasl.SaslServer;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** 持久化辅助函数 */

    /**
     * 获取当前 HEAD 指针指向的 Commit 的 SHA-1 ID
     */
    private static String getHeadCommitId() {
        File HEAD = Utils.join(GITLET_DIR, "HEAD");
        if (!HEAD.exists()) {
            return null;
        }
        // 1. 读取 HEAD 文件的内容
        String refPath = Utils.readContentsAsString(HEAD).trim();
        // 2. 根据路径找到对应的分支文件
        File branchHeadFile = Utils.join(GITLET_DIR, refPath);
        // 处理特殊情况
        if (!branchHeadFile.exists()) {
            // 这种情况通常不应该发生，除非手动删除了文件
            return null;
        }
        // 3. 读取分支文件中的 Commit ID
        return Utils.readContentsAsString(branchHeadFile).trim();// .trim()用于去除首位空白字符，如/n
    }

    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        // 初始化一个.gitlet目录
        GITLET_DIR.mkdirs();
        // 创建子目录 (objects, refs)
        File objects = Utils.join(GITLET_DIR, "objects");
        objects.mkdirs();
        File commits = Utils.join(objects, "commits");
        commits.mkdirs();
        File blobs = Utils.join(objects, "blobs");
        blobs.mkdirs();
        File refs = Utils.join(GITLET_DIR, "refs");
        refs.mkdirs();
        File heads = Utils.join(refs, "heads");
        heads.mkdirs();
        File master = Utils.join(heads, "master");
        // 创建HEAD指针，存储当前活动分支的路径
        File HEAD = Utils.join(GITLET_DIR, "HEAD");
        File refFile = Utils.join("refs", "heads", "master");
        String refPath = refFile.getPath();
        writeContents(HEAD, refPath);
        // 新建index索引，也就是我们的暂存区
        File index = Utils.join(GITLET_DIR, "index");
        StagingArea staging = new StagingArea();
        writeObject(index, staging);

        // 生成Initial Commit（初始提交）对象
        Date initialDate = new Date(0);// 传入 0 毫秒
        Commit InitialCommit = new Commit("initial commit",null,initialDate,new HashMap<>());
        // 创建 master 指针指向该 Commit
            // 计算InitialCommit的哈希值
        byte[] bytes = serialize(InitialCommit);
        String id = sha1(bytes);
            // 使用这个哈希值当作文件名写入objects
        File Commitfile = Utils.join(objects, id);
        writeObject(Commitfile, InitialCommit);
            // 把这个id写入master文件
        writeContents(master, id);
    }

    public static void add(String fileName) {
        // 如果没有gitlet目录会报错
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        // 检查文件是否存在
        File f = Utils.join(CWD, fileName);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        File index = Utils.join(GITLET_DIR, "index");
        StagingArea staging = Utils.readObject(index, StagingArea.class);
        byte[] contents = Utils.readContents(f);
        String  id = sha1(contents);

        // 在暂存区中的文件hash  id
        String stagingId = staging.getStagedHash(fileName);

        // 把head反序列化成commit对象
        String heads = getHeadCommitId();
        File commitFile = Utils.join(GITLET_DIR, "objects", heads);
        Commit CommitHead = Utils.readObject(commitFile, Commit.class);


        String CommitHash = CommitHead.getBlobId(fileName);
        // 判断与提交记录是否重复
        if (id.equals(CommitHash)) {
            staging.remove(fileName);
        } else {
            staging.add(fileName, id);
            // 持久化写入objects
            File blobFile = Utils.join(GITLET_DIR, "objects", id);
            Utils.writeContents(blobFile, contents);
        }
        // 持久化
        Utils.writeObject(index, staging);
    }

    public static void rm(String fileName) {
        // 如果没有gitlet目录会报错
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        // 将暂存区反序列化
        File index = Utils.join(GITLET_DIR, "index");
        StagingArea staging = Utils.readObject(index, StagingArea.class);

        // 把head反序列化成commit对象
        String heads = getHeadCommitId();
        File commitFile = Utils.join(GITLET_DIR, "objects", heads);
        Commit CommitHead = Utils.readObject(commitFile, Commit.class);

        // 如果文件既未暂存也未被 head 提交跟踪，打印错误消息
        String stagingId = staging.getStagedHash(fileName);// 找到暂存区中的对应文件的id
        String CommitHash = CommitHead.getBlobId(fileName);// 找到commit中对应文件的id
        if (stagingId == null && CommitHash == null) {
            System.out.println("No reason to remove the file.");
            return;
        }
        // 如果当前文件只存在于在暂存区，则将他从暂存区移除
        if (stagingId != null && CommitHash == null) {
            staging.remove(fileName);

        }
        // 定位文件目录下的文件
        File workfile = Utils.join(CWD, fileName);

        // 如果文件已被commit跟踪，则将他的放入“待删除暂存区”，并将目录文件删除
        if (CommitHash != null && workfile.exists()) {
            staging.removedAdded(fileName);
            Utils.restrictedDelete(workfile);
        } else if (CommitHash != null && !workfile.exists()) {
            staging.removedAdded(fileName);// 已被commit跟踪，但是文件已经不存在于文件目录
        }
        // 持久化
        Utils.writeObject(index, staging);
    }

    public static void commit(String message) {
        // 如果没有gitlet目录会报错
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        // 将暂存区反序列化
        File index = Utils.join(GITLET_DIR, "index");
        StagingArea staging = Utils.readObject(index, StagingArea.class);
        // 先检查暂存区有无内容
        if (staging == null) {
            System.out.println("No changes added to the commit.");
            return;
        }
        // 每次提交必须有一个非空的消息
        if (message == null) {
            System.out.println("Please enter a commit message.");
        }
        //
    }
    /* TODO: fill in the rest of this class. */
}
