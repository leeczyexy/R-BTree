package com.rbtree.demo;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.regexp.internal.REUtil;

import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.springframework.context.annotation.Bean;

import java.awt.Color;
import java.awt.TextArea;
import java.rmi.server.RMIClassLoader;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import javax.naming.ldap.Rdn;

import ch.qos.logback.core.joran.conditional.ElseAction;

/**
 * @PackageName com.rbtree.demo
 * @ProjectName demo
 * @ClassName RBree
 * @Auther Lee
 * @Date 2019/3/27
 * @Description todo
 */

/**
 * 实现比较接口的类
 * @param <T>
 */
public class RBTree<T extends Comparable<T>> {
    static boolean RED=true;
    static boolean BLACK=false;

    //根节点
    RBNode<T> mRoot;

    //节点信息
    public class RBNode<T extends Comparable<T>>{
        boolean color;
        T key;
        RBNode<T> rChild;
        RBNode<T> lChild;
        RBNode<T> parent;

        public RBNode(T key, boolean color,RBNode<T>rChild,RBNode<T> lChild,RBNode<T> parent){
            this.color=color;
            this.key=key;
            this.lChild=lChild;
            this.rChild=rChild;
        }

        //获取节点信息
    }
    public RBTree(){

    }
    //返回父节点
    public RBNode<T> parentOf(RBNode<T> thisNode){
        if(thisNode.parent!=null)
            return thisNode.parent;
        else return null;
    }
    //返回左子节点
    public RBNode<T> lChildOf(RBNode<T> thisNode){
        if(thisNode.lChild==null){
            return thisNode.lChild;
        }else{
            return null;
        }
    }
    //返回右子节点
    public RBNode<T> rChildOf(RBNode<T> thisNode){
        if(thisNode.rChild==null){
            return thisNode.rChild;
        }else{
            return null;
        }
    }

    //判断节点颜色
    public String rColor(RBNode<T> thisNode){
        if(thisNode.color==RED)
            return "R";
        else
            return "B";
    }
    //设为黑
    public void setBlack(RBNode<T> thisNode){
        thisNode.color=BLACK;
    }
    //设为红
    public void setRed(RBNode<T> thisNode){
        thisNode.color=RED;
    }
    //设置父节点
    public void setParent(RBNode<T> thisNode,RBNode<T> father){
        thisNode.parent=father;
    }

    //输出节点信息
    public void print(RBNode<T> thisNode){
        System.out.println(thisNode.key.toString());
    }

    //前序遍历
    public void viewByFont(RBNode<T> viewNode){
        if (viewNode==null) return;

        print(viewNode);
        if(viewNode.lChild!=null)
            viewByFont(viewNode.lChild);
        if(viewNode.rChild!=null)
            viewByFont(viewNode.rChild);
    }
    //中序遍历
    public void viewByMid(RBNode<T> viewNode){
        if (viewNode==null) return;

        if(viewNode.lChild!=null)
            viewByMid(viewNode.lChild);
        print(viewNode);
        if(viewNode.rChild!=null)
            viewByMid(viewNode.rChild);
    }
    //后序遍历
    public void viewByBeh(RBNode<T> viewNode){
        if (viewNode==null) return;
        if(viewNode.lChild!=null)
            viewByBeh(viewNode.lChild);
        if(viewNode.rChild!=null)
            viewByBeh(viewNode.rChild);
        print(viewNode);
    }
    //递归实现查找键值为key
    public RBNode<T> findX(RBNode<T> Root,T key){
        if(Root==null){
            return Root;
        }
        if(key.compareTo(Root.key)<0)
            return findX(Root.lChild,key);
        else if(key.compareTo(Root.key)>0)
            return findX(Root.rChild,key);
        else
            return Root;
    }
    //非递归实现查找
    public RBNode<T> findT(RBNode<T> Root,T key){
        if(Root==null)
            return null;
        while (Root!=null){
            if(key.compareTo(Root.key)<0){
                Root=Root.lChild;
            }else if (key.compareTo(Root.key)>0){
                Root=Root.rChild;
            }else{
                return null;
            }
        }
        return null;
    }
    //查找最小节点
    public RBNode<T> findMin(RBNode<T> Root){
        while (Root.lChild!=null){
            Root=Root.lChild;
        }
        return Root;
    }
    //查找最大节点
    public RBNode<T> findMax(RBNode<T> Root){
        while(Root.rChild!=null){
            Root=Root.rChild;
        }
        return Root;
    }

    /*
     * 对红黑树的节点(x)进行左旋转
     *
     * 左旋示意图(对节点x进行左旋)：
     *      px                              px
     *     /                               /
     *    x                               y
     *   /  \      --(左旋)-.           / \                #
     *  lx   y                          x  ry
     *     /   \                       /  \
     *    ly   ry                     lx  ly
     *
     *
     */
    public void leftSpin(RBNode<T> thisNode){
        if(thisNode.rChild==null){
            return;
        }
        RBNode<T> thisParent=thisNode.parent;
        RBNode<T> thisRChild=thisNode.rChild;
        RBNode<T> thisRLChild=null;
        if(thisRChild.lChild!=null){
            thisRLChild=thisRChild.lChild;
        }

        thisParent.lChild=thisRChild;
        thisRChild.lChild=thisNode;
        thisNode.rChild=thisRLChild;
    }
    /*
     * 对红黑树的节点(y)进行右旋转
     *
     * 右旋示意图(对节点y进行左旋)：
     *            py                               py
     *           /                                /
     *          y                                x
     *         /  \      --(右旋)-.            /  \                     #
     *        x   ry                           lx   y
     *       / \                                   / \                   #
     *      lx  rx                                rx  ry
     *
     */
    public void rightSpin(RBNode<T> thisNode){
        if(thisNode.rChild==null){
            return;
        }
        RBNode<T> thisParent=thisNode.parent;
        RBNode<T> thisLChild=thisNode.lChild;
        RBNode<T> thisLRChild=null;
        if(thisLChild.lChild!=null){
            thisLRChild=thisLChild.rChild;
        }

        thisParent.lChild=thisLChild;
        thisLChild.rChild=thisNode;
        thisNode.lChild=thisLRChild;
    }
    /**
     * 插入修正函数
     */
    public void insertFix(RBNode<T> node){
        RBNode<T> parent,grandParent;
        while((parent=parentOf(node))!=null&&parent.color==RED){

            grandParent=parentOf(node);
            if(parent==grandParent.lChild){
                RBNode<T> uncle=grandParent.rChild;
                //1--叔叔节点为红色
                if(uncle!=null&&uncle.color==RED){
                    uncle.color=BLACK;
                    parent.color=BLACK;
                    grandParent.color= RED;
                    node=grandParent;
                    continue;
                }

                //2--叔叔节点为黑色,当前节点为右节点
                if(uncle!=null&&uncle.color==BLACK){
                    RBNode<T> tmp;
                    leftSpin(parent);
                    tmp=parent;
                    parent=node;
                    node=tmp;
                }
                parent.color=BLACK;
                grandParent.color=RED;
                rightSpin(grandParent);
            }else {    //若“z的父节点”是“z的祖父节点的右孩子”
                // Case 1条件：叔叔节点是红色
                RBNode<T> uncle = grandParent.lChild;
                if ((uncle!=null) && uncle.color==RED) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(grandParent);
                    node = grandParent;
                    continue;
                }

                // Case 2条件：叔叔是黑色，且当前节点是左孩子
                if (parent.lChild == node) {
                    RBNode<T> tmp;
                    rightSpin(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是右孩子。
                setBlack(parent);
                setRed(grandParent);
                leftSpin(grandParent);
            }
            mRoot.color=BLACK;
        }

    }

    /**
     * 插入函数
     */
    public void insert(RBNode<T> node){
        RBNode<T> preNode=null;
        RBNode<T> thisNode=this.mRoot;

        while (thisNode!=null){
            preNode=thisNode;
            while(thisNode!=null){
                if(thisNode.key.compareTo(node.key)<0){
                    thisNode=thisNode.lChild;
                }else{
                    thisNode=thisNode.rChild;
                }
            }
            node.parent=preNode;
            if(preNode!=null){
                if(preNode.key.compareTo(node.key)<0){
                    preNode.lChild=node;
                }
                else {
                    preNode.rChild=node;
                }
                node.color=RED;
                //修正二叉树

            }
        }
    }
    /**
     * 红黑树删除修正函数
     */
    private void removeFixUp(RBNode<T> node, RBNode<T> parent){
        RBNode<T> other;

        while ((node==null || node.color== BLACK) && (node != this.mRoot)) {
            if (parent.lChild == node) {
                other = parent.rChild;
                if (other.color==RED) {
                    // Case 1: x的兄弟w是红色的
                    setBlack(other);
                    setRed(parent);
                    leftSpin(parent);
                    other = parent.rChild;
                }

                if ((other.lChild==null || other.lChild.color==BLACK) &&
                        (other.rChild==null ||other.rChild.color==BLACK)) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.rChild==null || other.rChild.color==BLACK) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        setBlack(other.lChild);
                        setRed(other);
                        rightSpin(other);
                        other = parent.rChild;
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    other.color= parent.color;
                    setBlack(parent);
                    setBlack(other.rChild);
                    leftSpin(parent);
                    node = this.mRoot;
                    break;
                }
            } else {

                other = parent.lChild;
                if (other.color==RED) {
                    // Case 1: x的兄弟w是红色的
                    setBlack(other);
                    setRed(parent);
                    rightSpin(parent);
                    other = parent.lChild;
                }

                if ((other.lChild==null || other.lChild.color==BLACK) &&
                        (other.rChild==null || other.rChild.color==BLACK)) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.lChild==null || other.lChild.color==BLACK) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        setBlack(other.rChild);
                        setRed(other);
                        leftSpin(other);
                        other = parent.lChild;
                    }

                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    other.color= parent.color;
                    setBlack(parent);
                    setBlack(other.lChild);
                    rightSpin(parent);
                    node = this.mRoot;
                    break;
                }
            }
        }

        if (node!=null)
            setBlack(node);
    }
    /**
     * 删除节点
     */
    public RBNode<T> remove(T key){
        RBNode<T> node;
        if((node=findX(mRoot,key))==null){
            return null;
        }
        RBNode<T> child, parent;
        boolean color;

        // 被删除节点的"左右孩子都不为空"的情况。
        if ( (node.lChild!=null) && (node.rChild!=null) ) {
            // 被删节点的后继节点。(称为"取代节点")
            // 用它来取代"被删节点"的位置，然后再将"被删节点"去掉。
            RBNode<T> replace = node;

            // 获取后继节点
            replace = replace.rChild;
            while (replace.lChild != null)
                replace = replace.lChild;

            // "node节点"不是根节点(只有根节点不存在父节点)
            if (parentOf(node)!=null) {
                if (parentOf(node).lChild == node)
                    parentOf(node).lChild = replace;
                else
                    parentOf(node).rChild = replace;
            } else {
                // "node节点"是根节点，更新根节点。
                this.mRoot = replace;
            }

            // child是"取代节点"的右孩子，也是需要"调整的节点"。
            // "取代节点"肯定不存在左孩子！因为它是一个后继节点。
            child = replace.rChild;
            parent = parentOf(replace);
            // 保存"取代节点"的颜色
            color = replace.color;

            // "被删除节点"是"它的后继节点的父节点"
            if (parent == node) {
                parent = replace;
            } else {
                // child不为空
                if (child!=null)
                    setParent(child, parent);
                parent.lChild = child;

                replace.rChild = node.rChild;
                setParent(node.rChild, replace);
            }

            replace.parent = node.parent;
            replace.color = node.color;
            replace.lChild = node.lChild;
            node.lChild.parent = replace;

            if (color == BLACK)
                removeFixUp(child, parent);


            return node;
        }

        if (node.lChild !=null) {
            child = node.lChild;
        } else {
            child = node.rChild;
        }

        parent = node.parent;
        // 保存"取代节点"的颜色
        color = node.color;

        if (child!=null)
            child.parent = parent;

        // "node节点"不是根节点
        if (parent!=null) {
            if (parent.lChild == node)
                parent.lChild = child;
            else
                parent.rChild = child;
        } else {
            this.mRoot = child;
        }

        if (color == BLACK)
            removeFixUp(child, parent);
        node = null;
        return node;
    }
    /**
     * 销毁红黑树
     */

    /**
     * 打印红黑树
     */
}
