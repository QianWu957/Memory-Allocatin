import java.util.ArrayList;
import java.util.List;

public class Memory {

	MemoryBlock head;
	
	public Memory(Integer totalSize){
		head = new MemoryBlock();
		head.size = totalSize;
	}
	
	public List<Process> getProcessList() {
		List<MemoryBlock> blocks = getMemoryBlockList();

		List<Process> list = new ArrayList<Process>();
		for (MemoryBlock memoryBlock : blocks) {
			if(!memoryBlock.available){
				list.add(memoryBlock.process);
			}
		}
		return list;
	}
	
	public List<MemoryBlock> getMemoryBlockList() {
		List<MemoryBlock> blocks = new ArrayList<MemoryBlock>();
		MemoryBlock node = head;
		while(node != null){
			blocks.add(node);
			node = node.next;
		}
		return blocks;
	}
	
	public boolean allocate(Process process){
		MemoryBlock node = findEmptyBlock(process.size);
		if(node != null){
			process.location = node.location;
			if(node.size == process.size){
				node.available = false;
				node.process = process;
			}else {
				MemoryBlock nextNode = new MemoryBlock();
				nextNode.location = node.location + process.size;
				nextNode.size = node.size - process.size;
				nextNode.next = node.next;
				
				node.available = false;
				node.process = process;
				node.size = process.size;
				node.next = nextNode;
			}
			
			compact();
			return true;
		}else {
			return false;
		}
	}
	
	public boolean deallocate(Integer number){
		MemoryBlock node = head;
		while(node != null){
			if(node.available.equals(false) && node.process.number.equals(number)){
				node.available = true;
				node.process = null;
				
				compact();
				return true;
			}
			node = node.next;
		}
		
		compact();
		return false;
	}
	
	//find a node which is bigger than "size"
	public MemoryBlock findEmptyBlock(Integer size){
		MemoryBlock node = head;
		while(node != null){
			if(node.available && node.size>=size){
				return node;
			}
			node = node.next;
		}
		return null;
	}
	
	//combine the adjacent empty blocks
	public void compact(){
		MemoryBlock connected = findConnectedEmptyBlock();
		System.out.println(connected);
		while(connected != null){
			
			connected.size += connected.next.size;
			connected.next = connected.next.next;
			
			connected = findConnectedEmptyBlock();
		}
	}
	
	//return a node which is available and it's next node also is also available
	public MemoryBlock findConnectedEmptyBlock(){
		MemoryBlock node = head;
		while(node != null && node.next != null){
			if(node.available && node.next.available){
				return node;
			}
			node = node.next;
		}
		return null;
	}
}
