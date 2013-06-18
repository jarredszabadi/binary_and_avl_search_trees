
public class BSTDictionary  <E, K extends Sortable> implements Dictionary<E, K>
{
    //root = entry into the tree
    protected BSTNode<E,K> root;
    
    //Constructor initializes tree to null
    public BSTDictionary()
    {
        root=null;
    }
    
    /*
     * @param  key - the key to be searched for
     * @return element - the element held by the node referenced by key
     * This recursive function returns the element held by the key requested
     */
    public E search(K key){
        BSTNode<E,K> current = root;    //get entry point
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
        //if not found then return null
        return null;
    }
    /*
     * @param key- key to be delted
     * @return -the new tree
     * finds and deletes the node with the matching key
     */
    public void delete(K key){
        root = delete(key, root);
    }
    /*
     * @param key - the key of the new node to be inserted
     * @parame element - the element of the enw node to be inserted
     * @return - the enw tree
     * inserts a new node into the tree
     */
    public void insert(K key, E element){
        root = insert(key, element, root);
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
        return 1+depth(root);
    }
    
    /*
     * Internal Methods
     */
    /*
     * Depth
     * @param node - the root of the subtree currently being evaluated
     * return - the value of the longest tree banch
     */
    protected int depth(BSTNode node){
        BSTNode<E,K> leftTree = node.getLeft();//left subtree of node
        BSTNode<E,K> rightTree = node.getRight();//right subtree of node
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
     * Insert
     * @param key- the key of the new ndoe to be inserted
     * @parame element - the element of the new node to be inserted
     * @param node - the current node in our traversal
     * @return the new tree 
     * 
     * this is a recursive function that finds the appropriate spot ont he tree to add the new element based on its key value
     * 
     * Algorithm
     * if current node == null
     *      - insert new node in current nodes spot
     * if current node has the same key as the one to be inserted 
     *      -then overwrite(could do nothing as well since overwriting the one key with a second of the same value is the same)
     * otherwise
     *  traverse tree to find appropriate spot for insertion
     *  if key is lesser in value than the current node's key 
     *      -follow the left branch
     *  if key is greater in value than the curren node's key
     *      -follow the right branch
     */
    protected BSTNode<E,K> insert(K key, E element, BSTNode node){
        //if current node is unoccupied then insert new node
        if(node == null)
        {
            return new BSTNode(key, element, null, null);
        }
        //if current node has the same value as key to be inserted then overwrite
        if(key.compareTo(node.getKey())==0){
            node.setElement(element);
        }
        //otherwise traverse tree to find appropriate insertion locaion
        else{
            //if key is less than current node's key
            if(key.compareTo(node.getKey())<0)
            {
                //recursively follow the left branch
                node.setLeft(insert(key, element, node.getLeft()));
            }
            //if key is greater than curren node's key
            else{
                //recursively follow the right branch
                node.setRight(insert(key,element, node.getRight()));
            }
        }
        //return root
        return node;
    }
    
        

    /*
     * Delete
     * @param key - key to search for and delete
     * @param node - current node in tree traversal
     * @return new tree
     * 
     * Algorithm
     * if key is less than the current node's key
     *      -traverse the left branch of current node to find key
     * if key is greater than the curren node's key
     *      -traverse the right branch of the current node to find key
     * otherwise if we found key
     *      if node has only one branch
     *          -move that branch into the place of the deleted node(because all other nodes will still be less than their new parent)
     *      otherwise if it has 2 branches
     *          -locate the inorder successor
     *          -move successor into current node's place and delete successors old node
     * otherwise if key is not in tree
     *  -return null
     *      
     */    
    protected BSTNode<E,K> delete(K key, BSTNode node)
    {
        //key is not in tree
          if(node==null)
           {return null;}
           //key is less than current node's key so follow left branch
          if(key.compareTo(node.getKey())<0)
          {
              node.setLeft(delete(key, node.getLeft()));
          }
          //key is greater than current node's key so follow the right branch
          else if(key.compareTo(node.getKey())>0)
          {
              node.setRight(delete(key, node.getRight()));
          }
          //found key
          else{
              //only left branch contains nodes so move left branch up
              if(node.getRight()==null)
              {
                  return node.getLeft();
              }
              //only right branch contains nodes so move righ branch up
              if(node.getLeft()==null)
              {
                  return node.getRight();
              }
              //this code actually moves and reorganizes the nodes for deleteion
              BSTNode temproot = node; //make a copy of current node
              node = min(temproot.getRight()); //reset node to its inorder successor
              node.setRight(deleteMin(temproot.getRight()));//delete the inorder succesor node after its value has been copied into current node
              node.setLeft(temproot.getLeft()); //reset nodes left branch to it's original state
          }
          //return node
          return node;
          
          
    }
    /*
     * deleteMin
     * -called by delete
     * @param node- node to traverse
     * @return - original node
     * searches for the inorder succesor's node and deletes it
     * 
     */
    protected BSTNode <E,K>deleteMin(BSTNode node){
        //if there is no leftBranch then return the final node in the right branch
          if(node.getLeft()==null)
          {return node.getRight();}
          //otherwise keep following the left branches to get to the inorder successor
          node.setLeft(deleteMin(node.getLeft()));
          return node;
    }
    /*
     * min
     * -called by delete
     * @param node - subtree to traverse
     * returns the original node
     * finds the inorder succesor
     */
    protected BSTNode<E,K> min(BSTNode node){
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
     * toString
     * -called by toString
     * @param node - the current node to be displayed
     * recursive function to output all of a node's branches
     */
    protected String toString(BSTNode node){
        //if node is null output a '/'
         if (node == null)
         {
              return "/";
         }
         //otherwise output to the screen parentNode(leftchild,rightchild)
         return(""+(node.getKey()).toString())+ "(" + toString(node.getLeft()) + "," + toString(node.getRight())+ ")";
    }
         
    
 }
