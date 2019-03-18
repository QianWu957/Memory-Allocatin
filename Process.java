
public class Process{
	public Integer number;
	public Integer size;
	public Integer location;
	
	public static Integer count = 0;
	
	public Process() {
		count++;
		number = count;
	}

	@Override
	public String toString() {
		return "Process [number=" + number + ", size=" + size + ", location=" + location + "]";
	}
}