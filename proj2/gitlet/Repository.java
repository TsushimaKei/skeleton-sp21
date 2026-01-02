package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.charset.StandardCharsets;


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
            return;
        }
        // 提取出暂存区内容
        File index = Utils.join(GITLET_DIR, "index");
        StagingArea staging = Utils.readObject(index, StagingArea.class);
        byte[] contents = Utils.readContents(f);
        String  id = sha1(contents);



        // 把head反序列化成commit对象
        String heads = getHeadCommitId();
        File commitFile = Utils.join(GITLET_DIR, "objects", heads);
        Commit CommitHead = Utils.readObject(commitFile, Commit.class);


        String CommitHash = CommitHead.getBlobId(fileName);

        // 判断与提交记录是否重复
        if (id.equals(CommitHash)) {
            // 文件和 HEAD 一样：取消暂存（如果之前 add 过）
            staging.remove(fileName);
            // 如果你加了 unRemove，就顺便取消 removed 暂存
            // staging.unRemove(fileName);
        } else {
            staging.add(fileName, id);
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
            Utils.writeObject(index, staging);
            return;
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
        // 当暂存区为空
        if (staging.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        // 每次提交必须有一个非空的消息
        if (message == null || message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        // 准备一张新提交要用的文件映射表newMap
        String parentId = getHeadCommitId();
        File parentFile = Utils.join(GITLET_DIR, "objects", parentId);
        Commit parent = Utils.readObject(parentFile, Commit.class);


        HashMap<String, String> newMap = new HashMap<>(parent.getblobs());

        // 把暂存区 addArea 覆盖进 newMap
        HashMap<String, String> TAmap = staging.getAddArea();
        HashSet<String> TRmap = staging.getRemovedArea();

        for (String fileName : TAmap.keySet()){
            newMap.put(fileName, TAmap.get(fileName));
        }
        // 把暂存区 removedArea 从 newMap 删除
        for (String fileName : TRmap) {
            newMap.remove(fileName);
        }
        // 用 newMap 创建新 Commit 并存到 objects
        Commit newCommit =new Commit(message, parentId, new Date(), newMap);
        String newId = sha1(serialize(newCommit));
        File outFile = Utils.join(GITLET_DIR, "objects", newId);
        writeObject(outFile, newCommit);

        // 更新当前分支指针 + 清空暂存区
        File headfile = Utils.join(GITLET_DIR, "HEAD");
        String headRefPath = Utils.readContentsAsString(headfile).trim();
        File branchFile = Utils.join(GITLET_DIR, headRefPath);//获取路径
        Utils.writeContents(branchFile, newId);
        staging.clear();
        writeObject(index, staging);

    }

    public static void log() {
        // 如果没有gitlet目录会报错
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);


        String currId = getHeadCommitId();
        while (currId != null && !currId.isEmpty()) {
            File commitIFile = join(GITLET_DIR, "objects", currId);
            Commit c = readObject(commitIFile, Commit.class);
            System.out.println("===");
            System.out.println("commit " + currId);
            System.out.println("Date: " + sdf.format(c.getTimestamp()));
            System.out.println(c.getMessage());
            System.out.println();

            currId = c.getParent();
        }
    }
    public static void globalLog(){
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        List<String> ids = plainFilenamesIn(objectsDir());
        if (ids == null) {
            return;
        }
        Collections.sort(ids);
        for (String id : ids) {
            Commit c = tryReadCommit(id);
            if (c != null) {
                printCommit(id, c);
            }
        }

    }
    public static void find(String message) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        List<String> ids = plainFilenamesIn(objectsDir());
        if (ids == null) {
            System.out.println("Found no commit with that message.");
            return;
        }
        boolean found = false;
        for (String id : ids) {
            Commit c = tryReadCommit(id);
            if (c != null && c.getMessage().equals(message)) {
                System.out.println(id);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        // staging
        File index = join(GITLET_DIR, "index");
        StagingArea staging = readObject(index, StagingArea.class);

        // head commit
        String headId = getHeadCommitId();
        Commit head = readCommitStrict(headId);

        // 1) Branches
        System.out.println("=== Branches ===");
        List<String> branches = plainFilenamesIn(refsHeadsDir());
        if (branches != null) {
            Collections.sort(branches);
            String curr = currentBranchName();
            for (String b : branches) {
                if (b.equals(curr)) {
                    System.out.println("*" + b);
                } else {
                    System.out.println(b);
                }
            }
        }
        System.out.println();

        // 2) Staged Files
        System.out.println("=== Staged Files ===");
        List<String> staged = new ArrayList<>(staging.getAddArea().keySet());
        Collections.sort(staged);
        for (String f : staged) {
            System.out.println(f);
        }
        System.out.println();

        // 3) Removed Files
        System.out.println("=== Removed Files ===");
        List<String> removed = new ArrayList<>(staging.getRemovedArea());
        Collections.sort(removed);
        for (String f : removed) {
            System.out.println(f);
        }
        System.out.println();

        // 4) Modifications Not Staged For Commit
        System.out.println("=== Modifications Not Staged For Commit ===");
        List<String> mods = new ArrayList<>();

        HashMap<String, String> tracked = head.getblobs();
        HashMap<String, String> stagedAdd = staging.getAddArea();
        HashSet<String> stagedRm = staging.getRemovedArea();

        // 4a) tracked files
        for (String f : tracked.keySet()) {
            File wf = join(CWD, f);
            if (!wf.exists() && !stagedRm.contains(f)) {
                mods.add(f + " (deleted)");
            } else if (wf.exists() && !stagedAdd.containsKey(f)) {
                String wHash = sha1OfWorkingFile(f);
                String cHash = tracked.get(f);
                if (wHash != null && !wHash.equals(cHash)) {
                    mods.add(f + " (modified)");
                }
            }
        }

        // 4b) staged for addition
        for (String f : stagedAdd.keySet()) {
            File wf = join(CWD, f);
            if (!wf.exists()) {
                mods.add(f + " (deleted)");
            } else {
                String wHash = sha1OfWorkingFile(f);
                String sHash = stagedAdd.get(f);
                if (wHash != null && !wHash.equals(sHash)) {
                    mods.add(f + " (modified)");
                }
            }
        }

        Collections.sort(mods);
        for (String line : mods) {
            System.out.println(line);
        }
        System.out.println();

        // 5) Untracked Files
        System.out.println("=== Untracked Files ===");
        List<String> untracked = new ArrayList<>();
        for (String f : workingDirFiles()) {
            boolean isTracked = tracked.containsKey(f);
            boolean isStaged = stagedAdd.containsKey(f);
            if (!isTracked && !isStaged) {
                untracked.add(f);
            }
        }
        Collections.sort(untracked);
        for (String f : untracked) {
            System.out.println(f);
        }
        System.out.println();
    }
    public static void checkoutFileFromHead(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        String headId = getHeadCommitId();
        Commit head = readCommitStrict(headId);

        String blobId = head.getBlobId(fileName);
        if (blobId == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        writeBlobToWorkingDir(fileName, blobId);
    }

    public static void checkoutFileFromCommit(String commitIdOrPrefix, String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        String realId = resolveCommitId(commitIdOrPrefix);
        if (realId == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit c = readCommitStrict(realId);

        String blobId = c.getBlobId(fileName);
        if (blobId == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        writeBlobToWorkingDir(fileName, blobId);
    }

    public static void checkoutBranch(String branchName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        File branchFile = join(refsHeadsDir(), branchName);
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            return;
        }

        String curr = currentBranchName();
        if (branchName.equals(curr)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        // staging
        File index = join(GITLET_DIR, "index");
        StagingArea staging = readObject(index, StagingArea.class);

        // current head commit
        String headId = getHeadCommitId();
        Commit currentCommit = readCommitStrict(headId);

        // target commit
        String targetId = readContentsAsString(branchFile).trim();
        Commit targetCommit = readCommitStrict(targetId);

        // untracked protection
        if (wouldOverwriteUntracked(targetCommit, currentCommit, staging)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }

        // switch working directory to target snapshot
        checkoutCommitSnapshot(targetCommit, currentCommit);

        // update HEAD to point to that branch ref
        File head = join(GITLET_DIR, "HEAD");
        String newRefPath = join("refs", "heads", branchName).getPath();
        writeContents(head, newRefPath);

        // clear staging
        staging.clear();
        writeObject(index, staging);
    }
    public static void branch(String name) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        File out = join(refsHeadsDir(), name);
        if (out.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        String headId = getHeadCommitId();
        writeContents(out, headId);
    }

    public static void rmBranch(String name) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        File f = join(refsHeadsDir(), name);
        if (!f.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (name.equals(currentBranchName())) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        f.delete();
    }

    public static void reset(String commitIdOrPrefix) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        String realId = resolveCommitId(commitIdOrPrefix);
        if (realId == null) {
            System.out.println("No commit with that id exists.");
            return;
        }

        File index = join(GITLET_DIR, "index");
        StagingArea staging = readObject(index, StagingArea.class);

        String headId = getHeadCommitId();
        Commit currentCommit = readCommitStrict(headId);

        Commit targetCommit = readCommitStrict(realId);

        if (wouldOverwriteUntracked(targetCommit, currentCommit, staging)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }

        checkoutCommitSnapshot(targetCommit, currentCommit);

        // move current branch pointer
        File branchFile = headBranchFile();
        writeContents(branchFile, realId);

        // clear staging
        staging.clear();
        writeObject(index, staging);
    }

    public static void merge(String branchName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        // staging
        File index = join(GITLET_DIR, "index");
        StagingArea staging = readObject(index, StagingArea.class);

        // 1) 有未提交更改
        if (!staging.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }

        // 2) 分支是否存在
        File givenBranchFile = join(refsHeadsDir(), branchName);
        if (!givenBranchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // 3) 不能 merge 自己
        String currBranch = currentBranchName();
        if (branchName.equals(currBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        // 当前 / given 的 head commit
        String currId = getHeadCommitId();
        Commit currCommit = readCommitStrict(currId);

        String givenId = readContentsAsString(givenBranchFile).trim();
        Commit givenCommit = readCommitStrict(givenId);

        // 4) untracked 覆盖保护（按 spec）
        if (wouldOverwriteUntracked(givenCommit, currCommit, staging)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }

        // 5) 找 split point
        String splitId = findSplitPoint(currId, givenId);
        Commit splitCommit = readCommitStrict(splitId);

        // 6) 快速出口：given 是 curr 的祖先
        if (splitId.equals(givenId)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }

        // 7) 快速出口：curr 是 given 的祖先 => fast-forward
        if (splitId.equals(currId)) {
            // 这里不能 checkoutBranch(branchName)，否则会切换 HEAD 指向分支（错误）
            // 正确：把当前分支指针快进到 givenId，并更新工作区
            reset(givenId);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        // 三方 map
        HashMap<String, String> splitMap = splitCommit.getblobs();
        HashMap<String, String> currMap = currCommit.getblobs();
        HashMap<String, String> givenMap = givenCommit.getblobs();

        // 最终要写进 merge commit 的 map：从当前快照开始改
        HashMap<String, String> mergedMap = new HashMap<>(currMap);

        // files union
        HashSet<String> allFiles = new HashSet<>();
        allFiles.addAll(splitMap.keySet());
        allFiles.addAll(currMap.keySet());
        allFiles.addAll(givenMap.keySet());

        boolean conflict = false;

        for (String file : allFiles) {
            String S = splitMap.get(file);
            String C = currMap.get(file);
            String G = givenMap.get(file);

            boolean SCeq = Objects.equals(S, C);
            boolean SGeq = Objects.equals(S, G);
            boolean CGeq = Objects.equals(C, G);

            // 规则 1：S == C 且 S != G  => 用 given（或删除）
            if (SCeq && !SGeq) {
                if (G == null) {
                    // given 删除了，current 没改 => 删除
                    if (C != null) {
                        staging.removedAdded(file);
                        File wf = join(CWD, file);
                        if (wf.exists()) {
                            restrictedDelete(wf);
                        }
                        mergedMap.remove(file);
                    }
                } else {
                    // 用 given 版本
                    writeBlobToWorkingDir(file, G);
                    staging.add(file, G);
                    mergedMap.put(file, G);
                }
                continue;
            }

            // 规则 2：S == G 且 S != C  => 保持 current（不动）
            if (SGeq && !SCeq) {
                continue;
            }

            // 规则 3：C == G（两边最后一致）=> 不动
            if (CGeq) {
                continue;
            }

            // 其他情况：判断是否冲突 or 是否需要“新增 given 的新文件”
            // split 不存在该文件（新文件场景）
            if (S == null) {
                // 只有 given 新增
                if (C == null && G != null) {
                    writeBlobToWorkingDir(file, G);
                    staging.add(file, G);
                    mergedMap.put(file, G);
                    continue;
                }
                // C!=null 且 G==null => 只有 current 新增，不动
                // C!=null 且 G!=null 且 C!=G => 冲突（下面处理）
            }

            // split 存在该文件
            // current 删除、given 修改 => 冲突
            // current 修改、given 删除 => 冲突
            // current 和 given 都修改但不同 => 冲突

            // ====== 冲突处理 ======
            conflict = true;

            byte[] currBytes = (C == null) ? new byte[0] : readContents(join(objectsDir(), C));
            byte[] givenBytes = (G == null) ? new byte[0] : readContents(join(objectsDir(), G));

            String currText = new String(currBytes, StandardCharsets.UTF_8);
            String givenText = new String(givenBytes, StandardCharsets.UTF_8);

            String conflictText =
                    "<<<<<<< HEAD\n" +
                            currText +
                            "=======\n" +
                            givenText +
                            ">>>>>>>\n";

            byte[] outBytes = conflictText.getBytes(StandardCharsets.UTF_8);

            // 写入工作区
            writeContents(join(CWD, file), outBytes);

            // 存成新 blob 并暂存
            String newBlobId = sha1(outBytes);
            File blobFile = join(objectsDir(), newBlobId);
            writeContents(blobFile, outBytes);

            staging.add(file, newBlobId);
            mergedMap.put(file, newBlobId);
        }

        // 8) 创建 merge commit（两个 parent）
        String msg = "Merged " + branchName + " into " + currBranch + ".";
        Commit mergeCommit = new Commit(msg, currId, givenId, new Date(), mergedMap);

        String newId = sha1(serialize(mergeCommit));
        writeObject(join(objectsDir(), newId), mergeCommit);

        // 更新当前分支指针（注意：不是改 HEAD 文件，是改当前分支文件内容）
        File currBranchFile = headBranchFile();
        writeContents(currBranchFile, newId);

        // 清空暂存区
        staging.clear();
        writeObject(index, staging);

        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }










    /* ===================== Helpers ===================== */
    private static String findSplitPoint(String currId, String givenId) {
        Map<String, Integer> distCurr = ancestorDistances(currId);
        Map<String, Integer> distGiven = ancestorDistances(givenId);

        String best = null;
        int bestSum = Integer.MAX_VALUE;

        for (String id : distCurr.keySet()) {
            if (distGiven.containsKey(id)) {
                int sum = distCurr.get(id) + distGiven.get(id);
                if (sum < bestSum) {
                    bestSum = sum;
                    best = id;
                }
            }
        }
        // 理论上一定能找到（至少 initial commit）
        return best;
    }

    private static Map<String, Integer> ancestorDistances(String startId) {
        Map<String, Integer> dist = new HashMap<>();
        ArrayDeque<String> q = new ArrayDeque<>();

        dist.put(startId, 0);
        q.add(startId);

        while (!q.isEmpty()) {
            String id = q.remove();
            int d = dist.get(id);

            Commit c = readCommitStrict(id);

            // 注意：merge commit 有两个 parent
            String p1 = (c instanceof Commit) ? ((Commit) c).getParent1() : c.getParent();
            String p2;
            try {
                p2 = ((Commit) c).getParent2();
            } catch (Exception e) {
                p2 = null; // 兼容你还没升级 Commit 的情况（不推荐）
            }

            if (p1 != null && !dist.containsKey(p1)) {
                dist.put(p1, d + 1);
                q.add(p1);
            }
            if (p2 != null && !dist.containsKey(p2)) {
                dist.put(p2, d + 1);
                q.add(p2);
            }
        }
        return dist;
    }

    private static File objectsDir() {
        return join(GITLET_DIR, "objects");
    }

    private static File refsHeadsDir() {
        return join(GITLET_DIR, "refs", "heads");
    }

    private static String headRefPath() {
        File head = join(GITLET_DIR, "HEAD");
        if (!head.exists()) {
            return null;
        }
        return readContentsAsString(head).trim(); // e.g. "refs/heads/master"
    }

    private static File headBranchFile() {
        String ref = headRefPath();
        if (ref == null) {
            return null;
        }
        return join(GITLET_DIR, ref);
    }

    private static String currentBranchName() {
        String ref = headRefPath();
        if (ref == null) {
            return null;
        }
        int idx = ref.lastIndexOf('/');
        return (idx >= 0) ? ref.substring(idx + 1) : ref;
    }

    private static Commit readCommitStrict(String commitId) {
        File f = join(objectsDir(), commitId);
        return readObject(f, Commit.class);
    }

    /** 兼容 objects 混存：尝试把一个对象读成 Commit；不是 commit 就返回 null。 */
    private static Commit tryReadCommit(String maybeId) {
        try {
            File f = join(objectsDir(), maybeId);
            if (!f.exists()) {
                return null;
            }
            return readObject(f, Commit.class);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** 支持短 id：prefix 在 objects 里唯一匹配就扩展成完整 id。*/
    private static String resolveCommitId(String idOrPrefix) {
        if (idOrPrefix == null) {
            return null;
        }
        File f = join(objectsDir(), idOrPrefix);
        if (f.exists() && tryReadCommit(idOrPrefix) != null) {
            return idOrPrefix;
        }
        List<String> all = plainFilenamesIn(objectsDir());
        if (all == null) {
            return null;
        }
        List<String> matches = new ArrayList<>();
        for (String name : all) {
            if (name.startsWith(idOrPrefix) && tryReadCommit(name) != null) {
                matches.add(name);
            }
        }
        if (matches.isEmpty()) {
            return null;
        }
        Collections.sort(matches);
        return matches.get(0); // 61B 测试一般保证不会歧义
    }

    private static List<String> workingDirFiles() {
        List<String> names = plainFilenamesIn(CWD);
        if (names == null) {
            return new ArrayList<>();
        }
        Collections.sort(names);
        return names;
    }

    private static String sha1OfWorkingFile(String fileName) {
        File f = join(CWD, fileName);
        if (!f.exists()) {
            return null;
        }
        return sha1(readContents(f));
    }

    private static void writeBlobToWorkingDir(String fileName, String blobId) {
        File blobFile = join(objectsDir(), blobId);
        byte[] bytes = readContents(blobFile);
        writeContents(join(CWD, fileName), bytes);
    }

    /** untracked 覆盖保护：切换快照前必须检查 */
    private static boolean wouldOverwriteUntracked(Commit target, Commit current, StagingArea staging) {
        HashMap<String, String> currTracked = current.getblobs();
        HashMap<String, String> targetTracked = target.getblobs();
        HashMap<String, String> stagedAdd = staging.getAddArea();

        for (String f : workingDirFiles()) {
            boolean trackedNow = currTracked.containsKey(f);
            boolean stagedNow = stagedAdd.containsKey(f);
            boolean untracked = !trackedNow && !stagedNow;

            if (untracked && targetTracked.containsKey(f)) {
                return true;
            }
        }
        return false;
    }

    /** 把工作区变成 target 的快照：删除 current 有而 target 没有的；写入 target 有的。*/
    private static void checkoutCommitSnapshot(Commit target, Commit current) {
        HashMap<String, String> currTracked = current.getblobs();
        HashMap<String, String> targetTracked = target.getblobs();

        // 删除：current 跟踪但 target 不跟踪
        for (String f : currTracked.keySet()) {
            if (!targetTracked.containsKey(f)) {
                File wf = join(CWD, f);
                if (wf.exists()) {
                    restrictedDelete(wf);
                }
            }
        }

        // 写入：target 跟踪的全部写入
        for (String f : targetTracked.keySet()) {
            writeBlobToWorkingDir(f, targetTracked.get(f));
        }
    }

    /** 按 spec 打印一个 commit（log / global-log 用） */
    private static void printCommit(String id, Commit c) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        System.out.println("===");
        System.out.println("commit " + id);
        System.out.println("Date: " + sdf.format(c.getTimestamp()));
        System.out.println(c.getMessage());
        System.out.println();
    }

}
