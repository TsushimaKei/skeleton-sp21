package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
// 还需要导入什么包来用 Map 和 Set？

public class StagingArea implements Serializable {

    /** 用来存 [文件名 -> 哈希值] 的映射， add暂存区*/
    private HashMap<String, String> addArea = new HashMap<>();

    /** 用来存文件名的集合， removed暂存区 */
    private HashSet<String> removedArea = new HashSet<>();

    // 查找文件名
    public String getStagedHash(String fileName) {
        return addArea.get(fileName); // 如果找到返回哈希值，找不到返回 null
    }
    // 添加至暂存区
    public void add(String filename, String hash) {
        addArea.put(filename, hash);
        removedArea.remove(filename);
    }
    // 从暂存区删除，仅从暂存区中取消
    public void remove(String filename) {
        addArea.remove(filename);
    }
    // 从暂存区删除，如果此文件已被commit跟踪，则将其放入removedArea
    public void removedAdded(String filename) {
        removedArea.add(filename);
        addArea.remove(filename);
    }
}