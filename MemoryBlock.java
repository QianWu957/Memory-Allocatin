
public class MemoryBlock{
	Integer size;
	Boolean available;
	Process process;
	Integer location;
	
	MemoryBlock next;
	
	public MemoryBlock() {
		size = 0;
		available = true;
		process = null;
		next = null;
		location = 0;
	}

	@Override
	public String toString() {
		return "MemoryBlock [size=" + size + ", available=" + available +  ", location="
				+ location + "]";
	}
}