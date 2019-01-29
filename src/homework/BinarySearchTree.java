package homework;


import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.*;

/**
 * 这是我的二叉查找树
 *
 * @author heson_z
 * @version v1.0
 */
/*
    要实现的方法：
    void add(char c);
    void remove(char c);
    boolean contains(char c);
    char findMin();
    char findMax();
    void preOrder();
    void inOrder();
    void postOrder();
    TreeNode preBuild(String s1, String s2)
    TreeNOde postBuild(String s1, String s2)
    boolean isEmpty();
    void clear();
 */
public class BinarySearchTree {
    class EmptyTreeException extends RuntimeException {
        public EmptyTreeException(String message) {
            super(message);
        }
    }

    //有哪些成员变量
    TreeNode root;

    public BinarySearchTree() {
    }

    public void clear() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public char findMin() {
        // 先判空
        if (isEmpty()) {
            throw new EmptyTreeException("Empty tree!");
        }
        // 循环实现
        // 从根节点开始，找最左边节点
        /*TreeNode node = root;
        while(node.left != null) {
            node = node.left;
        }
        return node.element;*/
        // 递归实现
        TreeNode node = findMin(root);
        return node.element;
    }

    private TreeNode findMin(TreeNode node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node;
        }
        return findMin(node.left);
    }

    public char findMax() {
        if (isEmpty()) {
            throw new EmptyTreeException("Empty tree!");
        }
        // 循环实现
        /*TreeNode node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.element;*/
        // 递归实现
        TreeNode node = findMax(root);
        return node.element;
    }

    private TreeNode findMax(TreeNode node) {
        if (node.right == null) {
            return node;
        }
        return findMax(node.right);
    }

    /**
     * 添加元素
     *
     * @param c 要添加的元素
     */
    public void add(char c) {
        /*if (root == null) {
            root= new TreeNode(c);
        }*/
        // 用递归实现
        root = add(root, c);
    }

    private TreeNode add(TreeNode node, char c) {
        // 如果 node 为null,就新建一个TreeNode
        if (node == null) {
            return new TreeNode(c);
        }
        // 比较c 和 node.element的大小
        int compare = c - node.element;
        if (compare > 0) {
            node.right = add(node.right, c);
        }
        if (compare < 0) {
            node.left = add(node.left, c);
        }
        return node;
    }

    public void remove(char c) {
        // 在root这个树下删除元素c,将删除后的树赋值给root
        root = remove(root, c);
    }

    private TreeNode remove(TreeNode node, char c) {
        if (node == null) {
            throw new NoSuchElementException();
        }
        int compare = c - node.element;
        if (compare > 0) {
            node.right = remove(node.right, c);
        } else if (compare < 0) {
            node.left = remove(node.left, c);
        } else if (node.left == null && node.right == null) {
            // 删除叶子节点
            node = null;
        } else if (node.left != null && node.right != null) {
            // 有两个子节点的情况
            TreeNode minOfRight = findMin(node.right);
            node.element = minOfRight.element;
            node.right = remove(node.right, minOfRight.element);
        } else {
            node = (node.left != null ? node.left : node.right);
        }
        return node;
    }

    /**
     * 查找元素是否在树中存在
     *
     * @param c 查找的元素
     * @return 如果不存在则返回false, 否则返回true
     */
    public boolean contains(char c) {
        // 用循环做
       /* TreeNode node = root;
        while (node != null) {
            int compare = c - node.element;
            if (compare > 0) {
                node = node.right;
            } else if (compare < 0) {
                node = node.left;
            } else {
                return true;
            }
        }
        // 说明元素不存在
        return false;*/

        // 用递归
        return contains(root, c);
    }

    private boolean contains(TreeNode node, char c) {
        if (node == null) {
            return false;
        }
        int compare = c - node.element;
        if (compare > 0) {
            return contains(node.right, c);
        } else if (compare < 0) {
            return contains(node.left, c);
        } else {
            return true;
        }
    }

    /**
     * 先序遍历
     */
    public void preOrder() {
        /*preOrder(root);
        System.out.println();*/
        // 用Stack实现先序遍历
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            System.out.print(node.element + " ");
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        // 换行
        System.out.println();
    }

    private void preOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        // 先遍历根；
        System.out.print(node.element + " ");
        // 遍历左子树
        preOrder(node.left);
        // 遍历右子树
        preOrder(node.right);
    }

    /**
     * 中序遍历
     */
    public void inOrder() {
        inOrder(root);
        // 换行
        System.out.println();
    }

    private void inOrder(TreeNode node) {
        // 说明遍历完了
        if (node == null) {
            return;
        }
        // 先遍历左子树
        inOrder(node.left);
        // 遍历根
        System.out.print(node.element + " ");
        // 遍历右子树
        inOrder(node.right);
    }

    public void postOrder() {
        postOrder(root);
        System.out.println();
    }

    private void postOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        // 先遍历左子树
        postOrder(node.left);
        // 再遍历右子树
        postOrder(node.right);
        // 遍历根
        System.out.print(node.element + " ");
    }

    /**
     * 层级遍历
     */
    public void levelOrder() {
        // 创建一个队列
        Deque<TreeNode> queue = new ArrayDeque<>();
        // 将root入队
        queue.addLast(root);
        // 判断队列是否为空
        while (!queue.isEmpty()) {
            // 将节点出队列
            TreeNode node = queue.poll();
            System.out.print(node.element + " ");
            if (node.left != null) {
                queue.addLast(node.left);
            }
            if (node.right != null) {
                queue.addLast(node.right);
            }
        }
        // 换行
        System.out.println();
    }
    // build方法应该是一个静态方法

    /**
     * 根据先序和中序遍历去建树
     *
     * @param preOrder 先序
     * @param inOrder  后序
     * @return 构造的树
     */
    public static BinarySearchTree build(String preOrder, String inOrder) {
        BinarySearchTree tree = new BinarySearchTree();
        char[] pre = preOrder.toCharArray();
        char[] in = inOrder.toCharArray();

        tree.root = build(pre, in);
        // 创建根节点
        // 创建左子树
        // 创建右子树
        return tree;
    }

    private static TreeNode buid(String in, String post) {
        return build_(in, post, 0, in.length() - 1, 0, post.length() - 1);

    }

    private static TreeNode build_(String in, String post, int inStart, int inEnd, int postStart, int postEnd) {
        if (inStart > inEnd || postStart > postEnd)
            return null;
        char s = post.charAt(postEnd);
        int c = in.indexOf(s);

        TreeNode node = new TreeNode(s);
        node.left = build_(in, post, inStart, c - 1, postStart, postStart + c - inStart - 1);
        node.right = build_(in, post, c + 1, inEnd, postStart + c - inStart, postEnd - 1);
        return node;


    }

    public static int log(int x, int y) {
        return (int) (Math.log(y) / Math.log(x));
    }

    public static int j = 0;

    private static TreeNode build2(char[] s, int count) {
        if (count > log(2, s.length) + 1 || j > s.length - 1) {
            j--;
            return null;
        }
        TreeNode treeNode = new TreeNode(s[j]);
        j++;
        treeNode.left = build2(s,  count + 1);
        j++;
        treeNode.right = build2(s,  count + 1);
        return treeNode;
    }

    private static TreeNode build(char[] pre, char[] in) {
        // 进行判空
        if (pre == null || pre.length == 0) {
            return null;
        }
        // 创建根节点
        TreeNode node = new TreeNode(pre[0]);
        // 根据中序找到头节点的位置
        int index = 0;
        while (in[index] != pre[0]) {
            index++;
        }
        //将中序和先序分成左右部分
        char[] leftOfIn = Arrays.copyOfRange(in, 0, index);
        char[] rightOfIn = Arrays.copyOfRange(in, index + 1, in.length);
        char[] leftOfPre = Arrays.copyOfRange(pre, 1, leftOfIn.length + 1);
        char[] rightOfPre = Arrays.copyOfRange(pre, leftOfIn.length + 1, pre.length);
        // 创建左子树
        node.left = build(leftOfPre, leftOfIn);
        // 创建右子树
        node.right = build(rightOfPre, rightOfIn);
        return node;
    }

    private static class TreeNode {
        char element;
        TreeNode left;
        TreeNode right;

        public TreeNode(char element) {
            this.element = element;
        }

        public TreeNode(char element, TreeNode left, TreeNode right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        /*BinarySearchTree tree = new BinarySearchTree();
        tree.add('C');
        tree.add('K');
        tree.add('F');
        tree.add('G');
        tree.add('B');
        tree.add('D');*/
        /*System.out.println(tree.findMin());
        System.out.println(tree.findMax());
        tree.preOrder();
        tree.inOrder();
        tree.postOrder();
        tree.levelOrder();*/
        // 测试remove方法
        //tree.remove('A');
        // tree.remove('B');
        // tree.remove('K');
        /*tree.remove('C');
        tree.levelOrder();*/

        // 测试contains方法
        /*System.out.println(tree.contains('A'));
        System.out.println(tree.contains('G'));*/
        String postOrder = "BCA";
        String inOrder = "BAC";
        BinarySearchTree tree = new BinarySearchTree();
// 1题       tree.root = BinarySearchTree.buid(inOrder, postOrder);
        //2题
        tree.root = BinarySearchTree.build2("ABDECFG".toCharArray(), 1);
        tree.preOrder();
        tree.inOrder();
        tree.postOrder();
        tree.levelOrder();

        System.out.println(log(2, 31));

    }
}
