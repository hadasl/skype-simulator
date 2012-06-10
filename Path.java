import java.util.ArrayList;
import java.util.List;



public class Path {
	private List<PartialPath> m_list = new ArrayList<PartialPath>();
	public void Add(int [] digits, int delay)
	{
		PartialPath p = new PartialPath(digits, delay);
		m_list.add(p);
	}
	public int size()
	{
		return m_list.size();
	}
	public PartialPath GetPart(int i)
	{
		return (PartialPath)m_list.get(i);
	}
}
