
public class AVLDictionary<E, K extends Sortable> implements Dictionary<E,K>
{
    //root = entry into the AVL tree
    protected AVLNode<E,K> root;
    
    //initializes dictionary to null
    public AVLDictionary()
    {
        root = null;
    }
    /*
     * @param  key - the key to be searched for
     * @return element - the element held by the node referenced by key
     * This recursive function returns the element held by the key requested
     */
    public E search(K key){
        AVLNode<E,K> current = root; //get entry point
        while(current!=null)
        {
            //if key found then return element
            if(key.compareTo(root.getKey())==0)
            {
                return root.getElement();
            }
            //otherwise traverse tree
            //if key is less than the current node's key then follow its left branch
            else if(key.compareTo(root.getKey())<0)
            {
                current = current.getLeft();
            }
            //otherwise if the key is greater than the curren node's key then follow its right branch
            else
            {
                current=current.getRight();
            }
            
        }
        //if key is not found then return null
        return null;
    }

    /*
     * @param key - the key of the new node to be inserted
     * @parame element - the element of the enw node to be inserted
     * @return - the enw tree
     * inserts a new node into the tree
     */
    public void insert(K key, E element){
        root=insert(key, element, root);
        //root.setBalance(balanceFactor(root));
    }

     /*
     * @param key- key to be delted
     * @return -the new tree
     * finds and deletes the node with the matching key
     */
    public void delete(K key){
        root=delete(key, root);
    }

    /*
     * This function prints the tree in the following manner
     * ParentNode(LeftChild,RightChild)
     * if leftChild has two children then the above format continues
     * ParentNode(LeftChild(LeftChild2,RightChild2),RightChild)
     *              H
     *             /\
     *            A  Z
     *           /\
     *             D
     * H(A(/,D),Z)
     */
    public void printTree(){
        String s = toString(root);
        System.out.println(s);
    }

   /*
     * returns the max branch depth of the tree
     */
    public int depth(){
        return depth(root);
    }
    //internal methods
    /*insert
     * 
     * @param key - the key of the node to be inserted
     * @param element - the element of the node to be inserted
     * @param node - the current node being traversed
     * @return - the root of the new tree
     * 
     * Algorithm
     * 1)insert normally like a binary search tree
     * 2)balance tree
     *  
     * 
     */
    
    protected AVLNode<E,K> insert(K key, E element, AVLNode node){

        //if current node = null then insert node here
        if(node == null)
        {
            return new AVLNode(key, element, null, null, AVLNode.EVEN);

        }//if key already exists overwire element
        if(key.compareTo(node.getKey())==0){
            node.setElement(element);
        }
        //otherwise traverse tree to find insertion location
        else{
            //if key i sless than current nodes's key follow left branch
            if(key.compareTo(node.getKey())<0)
            {
                //recursive call will insert key as binary search tree would
                node.setLeft(insert(key, element, node.getLeft()));    
                //set the balancefactor of the current node
                node.setBalance(balanceFactor(node));
                //if tree is left debalanced
                if(node.getBalance()==AVLNode.MORE_LEFT)
                {
                    //if key is less than leftchild of current node's ke
                    if(key.compareTo(node.getLeft().getKey())<0)
                    {
                        //rotate left
                        node=rotateLeft(node);
                    }
                    //otherwise if it's greater then
                    else
                    {
                        //rotate doublerotate left
                        node=doubleRotateLeft(node);
                    }
                }
                
            }
            //otherwise if key is greater than curren't node's key follow the right branch
            else{
                //insert node as a binary search tree node
                node.setRight(insert(key,element, node.getRight()));
                //get balance factor of current node
                node.setBalance(balanceFactor(node));
                //if tree is right debalanced
                if(node.getBalance()==AVLNode.MORE_RIGHT)
                {
                    //if key is greater than the rightchild fo current node's key then
                    if(key.compareTo(node.getRight().getKey())>0)
                    {
                        //rotate right
                        node = rotateRight(node);
                    }
                    //otherwise if it's less
                    else
                    {
                        //double rotate right
                        node = doubleRotateRight(node);
                    }
                }
            }
        }
        //set the root's balance factor
        node.setBalance(balanceFactor(node));
        //return root
        return node;
    }
    /*
     * Delete
     * @param key - key to search for
     * @param node - current node for traversal
     * 
     * 
     * 1)delete node
     * 2)rebalance
     */
    protected AVLNode<E,K> delete(K key, AVLNode node){
        //if key is not in tree then return null
        if(node==null)
        {
            return null;
        }
        //if key is less than current node's key follow the left branch to find key
        if(key.compareTo(node.getKey())<0)
        {   
            //recursive call to find and delete node with key
            node.setLeft(delete(key,node.getLeft()));
            //reset balance of the original node
            node.setBalance(balanceFactor(node));
            //if tree is debalanced
            if(node.getBalance()==AVLNode.MORE_RIGHT)
            {
                //if the righchild of current node is debablanced then rotate once
                if(node.getRight().getBalance()==AVLNode.MORE_RIGHT)
                {
                    node = doubleRotateRight(node);
                    
                }
                //otherwise rotateTwice
                else
                {
                    node = rotateRight(node);
                }
            }
            //otherwise just reset balance of tree
            else
            {
                node.setBalance(balanceFactor(node));
            }
        }
        //if key is greater than curren't nodes key follow right branch to find key
        else if(key.compareTo(node.getKey())>0)
        {
            //recursively find key and delete it
            node.setRight(delete(key, node.getRight()));
            //reset balance factor of tree
            node.setBalance(balanceFactor(node));
            //if current node is left debalanced
            if(node.getBalance()==AVLNode.MORE_LEFT)
            {
                //if leftchild is debalanced
                if(node.getLeft().getBalance()==AVLNode.MORE_LEFT)
                {
                    //rotate twice
                    node = doubleRotateLeft(node);
                    
                }
                else
                {
                    //rotate once
                    node = rotateLeft(node);
                }
            }
            //otherwise just set balance of tree
            else
            {
                node.setBalance(balanceFactor(node));
            }
        }
        //if we found key follow rules for binary search tree
        else{
                  if(node.getRight()==null)
                  {
                      return node.getLeft();
                  }
                  if(node.getLeft()==null)
                  {
                      return node.getRight();
                  }
                  AVLNode temproot = node;
                  node = min(temproot.getRight());
                  node.setRight(deleteMin(temproot.getRight()));
                  node.setLeft(temproot.getLeft());
        }
              return node;
    }
    
   
   //internal methods
   /*
    * balanceFacotor
    * @param node - to find balance factor for
    * 
    * determines the height of node's left and rigt subtrees and then determines the balance based on AVLNode's balance fields
    */
    protected int balanceFactor(AVLNode node){
        //determine the balance factor
        int d = subTreeDepth(node.getLeft())-subTreeDepth(node.getRight());
        //set into balance field
        //if rightside has 2 or more height than left
        if(d<-1){
            return AVLNode.MORE_RIGHT;
        }
        //if leftside has 2 or more height than right
        else if(d>1){
            return AVLNode.MORE_LEFT;
        }
        //if the children are within 1 height of eachother
        else {
            return AVLNode.EVEN;
        }
    }
    /*
     * subTreeDepth
     * determines the max height of a tree
     */
    protected int subTreeDepth(AVLNode node){
        //if node ==null then don't increment height
              if(node==null){
                  return 0;
              }
              //otherwise return the greatest height of the left and right subtree plus 1 to take into account the root
              else{
                  return Math.max(subTreeDepth(node.getLeft()), subTreeDepth(node.getRight()))+1;
              }
    }
    /*
     * rotateLeft
     * @param node - base node to rotate
     * return new node that is repositioned in the base's orginal position
     * 
     */
    protected AVLNode<E,K> rotateLeft(AVLNode node){
            AVLNode<E,K> left = node.getLeft();//set left to node's leftchild
            node.setLeft(left.getRight());  //set node's letchild to its rightchild
            left.setRight(node);        //set left's rightchild to node with its new leftchild
            node.setBalance(balanceFactor(node));   //set balance factor for node
            left.setBalance(balanceFactor(left));   //set balance factor for left
            return left;    //return left
    }
    /*
     * rotate the base node twice
     */
    protected AVLNode<E,K> doubleRotateLeft(AVLNode node)
    {
            node.setLeft(rotateRight(node.getLeft())); //set node's leftchild to its left child after it has been rotated right
            return rotateLeft(node);    //rotate node left
    }
    /*
     * rotate the base node right
     */
    protected AVLNode<E,K> rotateRight(AVLNode node){
        AVLNode<E,K> right = node.getRight();   //right = node's rightchild
        node.setRight(right.getLeft()); //set node's rightchild to its left child
        right.setLeft(node);    // set right's leftchild to node with its new rightchild
        node.setBalance(balanceFactor(node));   //set balance factor for node
        right.setBalance(balanceFactor(right)); //set balance factor for right
        return right;   //return right
    }
    protected AVLNode<E,K> doubleRotateRight(AVLNode node){
        node.setRight(rotateLeft(node.getRight()));
        return rotateRight(node);
    }
    /*
     * toString
     * -called by toString
     * @param node - the current node to be displayed
     * recursive function to output all of a node's branches
     */
    protected String toString(AVLNode node){
            if (node == null)
            {
                return "/";
            }
           return(""+(node.getKey()).toString())+ "(" + toString(node.getLeft()) + "," + toString(node.getRight())+ ")";
    }
     /*
     * Depth
     * @param node - the root of the subtree currently being evaluated
     * return - the value of the longest tree banch
     */
     protected int depth(AVLNode node){
        AVLNode<E,K> leftTree = node.getLeft();//left subtree of node
        AVLNode<E,K> rightTree = node.getRight();//right subtree of node
         //if subtree is a leaf then there are no more branches to follow, return 0
        if(leftTree ==null && rightTree == null)
        {
            return 0;
        }
         //if only the left subtree is 0 then follow the right branch and increment depth by 1
        else if(leftTree == null)
        {
            return 1+depth(rightTree);
        }
        //if only the right subtree is 0 then follow the left branch and increment by 1
        else if(rightTree==null)
        {
            return 1+depth(leftTree);
        }
        //otherwise if both subtrees contain nodes then return the larger depth of the left and right subtree depth. An extra 1 is included to represent the root
        else
            return 1+Math.max(depth(leftTree), depth(rightTree));
    }
     /*
     * min
     * -called by delete
     * @param node - subtree to traverse
     * returns the original node
     * finds the inorder succesor
     */
    protected AVLNode<E,K> min(AVLNode node){
         //if there is no left branch then we have reached the inorder successor
              if(node.getLeft()==null)
              {
                  return node;
              }
                 //otherwise keep going down the left branches to find inorder successot
                else{
                    return min(node.getLeft());
                }
    }
        /*
     * deleteMin
     * -called by delete
     * @param node- node to traverse
     * @return - original node
     * searches for the inorder succesor's node and deletes it
     * 
     */
    protected AVLNode <E,K>deleteMin(AVLNode node)
    {
        //if there is no leftBranch then return the final node in the right branch
              if(node.getLeft()==null)
              {return node.getRight();}
              //otherwise keep following the left branches to get to the inorder successor
              node.setLeft(deleteMin(node.getLeft()));
              return node;
    }
    

   
}
