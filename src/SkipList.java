import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Skip List implementation for Assignment 2 
 * @author jordan
 * @version February 17th 2019
 * @param <Item> - the class of items to be stored in the SkipList.
 */
public class SkipList<Item> implements List<Item>{
	/**The head node of the skip list**/
	private SkipNode<Item> head;
	/**The tail node of the skip list**/
	private SkipNode<Item> tail;
	/**The current size of the skip list - in items.**/
	private int size;
	/**The number of levels within the skip list**/
	private int levels;
	
	/**
	 * Constructor for creating a SkipList.
	 */
	public SkipList() {
		this.size = 0;
		this.levels = 1;
		head = new SkipNode<Item>(null);
		tail = new SkipNode<Item>(null);
		head.indexWidth = 1;
		head.right = tail;
		tail.left = head;
	}
	/**
	 * Adds the given item to the skip list.
	 * @param item - the item to be added to the skip list.
	 * @return true
	 */
	public boolean add(Item item) {
		SkipNode<Item> bottomTail = this.tail;
		SkipNode<Item> leftNode;
		SkipNode<Item> newNode = new SkipNode<Item>(item);
		newNode.indexWidth = 1;
		
		//Moving to the bottom of the list.
		size++;
		while(bottomTail.down != null) {
			++bottomTail.left.indexWidth;
			bottomTail = bottomTail.down;
		}
		
		//Updating node directional pointers
		leftNode = bottomTail.left;
		leftNode.right = newNode;
		newNode.left = leftNode;
		
		newNode.right = bottomTail;
		bottomTail.left = newNode;
		promoteNode(newNode);
		return true;
	}
	/**
	 * Used for promoting nodes strictly at the end of the list.
	 * @param promotionNode - the node to be possibly lifed through the skip list.
	 */
	private void promoteNode(SkipNode<Item> promotionNode) {

		//Getting the bottom tail.
		SkipNode<Item> currentTail = this.tail;
		while(currentTail.down != null) {
			currentTail = currentTail.down;
		}
		
		int currentLevel = 1;
		//Randomly promote nodes at the end.
		long random = Math.round(Math.random() * 0.8);
		while(random == 1) {
			random = Math.round(Math.random() * 0.8);
			++currentLevel;
			
			//If the next level up exists...
			if(currentLevel <= this.levels) {
				//Updating the tail up
				currentTail = currentTail.up;
				SkipNode<Item> leftNode = currentTail.left;
				
				//Creating the new level promotion node.
				promotionNode.up = new SkipNode<Item>(promotionNode.item);
				promotionNode.up.down = promotionNode;
				promotionNode = promotionNode.up;
				
				//Pointing the left node to the promotion node.
				promotionNode.left = leftNode;
				leftNode.right = promotionNode;
				--leftNode.indexWidth;
				
				//Pointing the current tail to the promotion node.
				promotionNode.right = currentTail;
				currentTail.left = promotionNode;
				promotionNode.indexWidth = 1;
			}
			//Otherwise, if the next level does not exist, we must create the next level.
			else {
				++this.levels;
				
				//Creating the head for the next level up.
				this.head.up = new SkipNode<Item>(null);
				this.head.up.down = this.head;
				this.head = this.head.up;
				
				//Creating the tail for the next level up.
				currentTail.up = new SkipNode<Item>(null);
				currentTail.up.down = currentTail;
				currentTail = currentTail.up;
				this.tail = currentTail;
				
				//Creating the promotion node for the next level up.
				promotionNode.up = new SkipNode<Item>(promotionNode.item);
				promotionNode.up.down = promotionNode;
				promotionNode = promotionNode.up;
				
				//Updating the head to point to the promotion node.
				this.head.right = promotionNode;
				promotionNode.left = this.head;
				this.head.indexWidth = this.size;
				
				//Updating the tail to point to the promotion node.
				currentTail.left = promotionNode;
				promotionNode.right = currentTail;
				promotionNode.indexWidth = 1;
			}
			
			
		}
	}
	/**
	 * Adds the given item to the skip list.
	 * Any items at the index or further (if they exist) are pushed back.
	 * @param index - the index at which to add the item.
	 * @param newItem - the item to be added to the list at the given index
	 * @throws IndexOutOfBoundsException if the index is outside the range [0,size] 
	 */
	public void add(int index, Item newItem) {
		if(index > size || index < 0)throw new IndexOutOfBoundsException("Index is ourside of range");
		else if(index == size) {
			this.add(newItem);
			return;
		}
		
		//Incrementing size.
		++size;
		
		//Getting to the indexed node.
		SkipNode<Item> currentNode = this.head;
		//Keeping track of the index distance to the node.
		int travelingIndex = index + 1;
		
		while(travelingIndex > 0) {
			//If the index distance is too large on this level...
			if(currentNode.indexWidth > travelingIndex) {
				//Updating the node index width, since we know the new node will be to the bottom right.
				++currentNode.indexWidth;
				currentNode = currentNode.down;
			}
			//Otherwise...
			else {
				travelingIndex -= currentNode.indexWidth;
				currentNode = currentNode.right;
			}
		}
		
		/*
		 * Creating a new node with the item currently housed at the index position
		 * to be pushed into index position + 1 
		 */
		SkipNode<Item> newNode = new SkipNode<Item> (currentNode.item);
		newNode.indexWidth = 1;
		
		//Replacing the node items on the way down
		while(currentNode.down != null) {
			//Updating the node index width, since we'll be placing the item to the bottom right
			++currentNode.indexWidth;
			currentNode.item = newItem;
			currentNode = currentNode.down;
		}
		currentNode.item = newItem;
		
		//Updating new node pointers
		SkipNode<Item> rightNode = currentNode.right;
		newNode.right = rightNode;
		rightNode.left = newNode;
		currentNode.right = newNode;
		newNode.left = currentNode;
		promoteNode(newNode,index+1);
	}
	/**
	 * Promotes the node within the skip list.
	 * @param promotionNode - the node to be promoted within the skip list.
	 * @param nodeIndex - the index of the promoted node.
	 */
	private void promoteNode(SkipNode<Item> promotionNode,int nodeIndex) {
		long random = Math.round(Math.random()*0.8);
		int currentLevel = 1;
		while(random == 1) {
			
			random = Math.round(Math.random()*0.8);
			++currentLevel;
			if(currentLevel > this.levels) {
				//Updating the number of levels
				++this.levels;
				
				//Building the new level head, updating head ptr.
				this.head.up = new SkipNode<Item>(null);
				this.head.up.down = this.head;
				this.head = this.head.up;
				
				//Building the new level tail, updating tail ptr.
				this.tail.up = new SkipNode<Item>(null);
				this.tail.up.down = this.tail;
				this.tail = this.tail.up;
				
				//Building the new level promotion node, updating promotion node ptr.
				promotionNode.up = new SkipNode<Item>(promotionNode.item);
				promotionNode.up.down = promotionNode;
				promotionNode = promotionNode.up;
				
				//Updating the promotion node to have the head to the left.
				promotionNode.left = this.head;
				this.head.right = promotionNode;
				this.head.indexWidth = nodeIndex+1;
				
				//Updating the promotion node to have the tail to the right.
				promotionNode.right = this.tail;
				this.tail.left = promotionNode;
				promotionNode.indexWidth = this.size - nodeIndex;
			}
			else {
				//Keeping track of the index distance travelled to the right.
				int distanceTravelled = 0;
				
				//Getting the node to the right of where the new node will be.
				SkipNode<Item> rightNode = promotionNode;
				while(rightNode.up == null) {
					distanceTravelled += rightNode.indexWidth;
					rightNode = rightNode.right;
				}
				rightNode = rightNode.up;
				//Getting the node to the left of where the new node will be. 
				SkipNode<Item> leftNode = rightNode.left;
				
				//Building the new level promotion node, updating the promotion node ptr.
				promotionNode.up = new SkipNode<Item>(promotionNode.item);
				promotionNode.up.down = promotionNode;
				promotionNode = promotionNode.up;
				
				//Getting the right node to point to the promotion node.
				promotionNode.right = rightNode;
				rightNode.left = promotionNode;
				promotionNode.indexWidth = distanceTravelled;
				
				//Getting the right node to point to the promotion node.
				promotionNode.left = leftNode;
				leftNode.right = promotionNode;
				leftNode.indexWidth -= distanceTravelled;
			}
			
		}
	}
	/**
	 * Retrieves the item contained at the given index within the SkipList.
	 * @param index - the index at which to find the item in the list.
	 * @return item - the item at the index within the list.
	 * @throws IndexOutOfBoundsException if the index is outside of the range, [0,list.size-1]
	 */
	public Item get(int index) {
		//Check that the index is valid
		if(index >= size || index < 0)throw new IndexOutOfBoundsException();
		//
		SkipNode<Item> currentNode = head;
		++index;
		while(index > 0) {
			if(currentNode.indexWidth > index) {
				currentNode = currentNode.down;
			}
			else {
				index -= currentNode.indexWidth;
				currentNode = currentNode.right;
			}
		}
		return currentNode.item;
	}
	/**
	 * Method for testing the structure of the skip list
	 * Prints each level from top to bottom of the skip list
	 * Items indicated with (), distances between nodes indicated with <>
	 */
	protected void printList() {
		System.out.println("-- SkipList Structure --");
		SkipNode<Item> currentHead = this.head;
		SkipNode<Item> currentNode = currentHead;
		int currentLevel = this.levels;
		while(currentLevel >= 1) {
			System.out.print(String.format("Level %d: ",currentLevel));
			while(currentNode != null) {
				System.out.print(currentNode);
				if(currentNode.right != null)System.out.print(String.format(" <-%d-> ",currentNode.indexWidth));
				currentNode = currentNode.right;
			}
			System.out.println();
			currentLevel--;
			currentHead = currentHead.down;
			currentNode = currentHead;
		}
		
	}
	/**This method generates a string representation of the contents of the SkipList.
	 * This follows the AbstractCollection API.
	 * The string begins with a '[' and ends with a ']' 
	 * with the elements of the list from beginning to end, separated by commas.
	 * @return the String representation of the SkipList
	 */
	public String toString() {
		String str = "[";
		//Getting to the bottom of the head.
		SkipNode currentNode = this.head;
		for(int i = 1;i < this.levels;i++) {
			currentNode = currentNode.down;
		}
		
		//Collecting all the elements in the string
		currentNode = currentNode.right;
		while(currentNode.item != null) {
			str += currentNode.item;
			currentNode = currentNode.right;
			if(currentNode.item != null)str += ", ";
		}
		str += "]";
		return str;
	}

	@Override
	public boolean addAll(Collection<? extends Item> arg0) {
		throw notNeeded();
	}


	@Override
	public boolean addAll(int arg0, Collection<? extends Item> arg1) {
		throw notNeeded();
	}


	@Override
	public void clear() {
		throw notNeeded();
	}


	@Override
	public boolean contains(Object arg0) {
		throw notNeeded();
	}


	@Override
	public boolean containsAll(Collection<?> arg0) {
		throw notNeeded();
	}


	


	@Override
	public int indexOf(Object arg0) {
		throw notNeeded();
	}


	@Override
	public boolean isEmpty() {
		return false;
	}


	@Override
	public Iterator<Item> iterator() {
		throw notNeeded();
	}


	@Override
	public int lastIndexOf(Object arg0) {
		throw notNeeded();
	}


	@Override
	public ListIterator<Item> listIterator() {
		throw notNeeded();
	}


	@Override
	public ListIterator<Item> listIterator(int arg0) {
		throw notNeeded();
	}


	@Override
	public boolean remove(Object arg0) {
		throw notNeeded();
	}


	@Override
	public Item remove(int arg0) {
		throw notNeeded();
	}


	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw notNeeded();
	}


	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw notNeeded();
	}


	@Override
	public Item set(int arg0, Item arg1) {
		throw notNeeded();
	}


	/**
	 * Returns the current size of the SkipList.
	 * @return the size of the SkipList.
	 */
	@Override
	public int size() {
		return this.size;
	}


	@Override
	public List<Item> subList(int arg0, int arg1) {
		throw notNeeded();
	}


	@Override
	public Object[] toArray() {
		throw notNeeded();
	}


	@Override
	public <T> T[] toArray(T[] arg0) {
		throw notNeeded();
	}
	
	/**
	 * Returns a runtime exception for methods that do not need to be implemented as part of the assignment.
	 * @return a not-required runtime exception.
	 */
	private static RuntimeException notNeeded(){
		return new UnsupportedOperationException("Method not required for assignment");
	}
	
	/**
	 * Constructor for the nodes of items in the skiplist
	 * @author jordan
	 * @param <Item> - the type of item being stored in the skiplist
	 * @version Monday February 11th 2019
	 */
	private static class SkipNode<Item>{
		
		/**The item contained within this node. **/
		public Item item;
		
		/**The index distance from this node to the right node. **/
		public int indexWidth;
		
		/**The node to the right of this node **/
		public SkipNode<Item> right; 
		
		/**The node to the left of this node **/
		public SkipNode<Item> left;
	
		/**The node directly under this node**/
		public SkipNode<Item> down;
		
		/**The node directly above this node**/
		public SkipNode<Item> up;
		
		/**
		 * Constructs the skip node with the given item.
		 * @param item - the item to be contained within the node.
		 */
		public SkipNode(Item item) {
			this.item = item;
		}
		public String toString() {
			return String.format("(%s)",item);
		}
	}
}
