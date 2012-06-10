
public class PartialPath {

	private int [] m_digits = null;
	private int m_delay = 0;
	public PartialPath(int[] digits, int delay) {
		super();
		this.m_digits = digits;
		this.m_delay = delay;
	}
	public int[] getDigits() {
		return m_digits;
	}
	public void setDigits(int[] m_digits) {
		this.m_digits = m_digits;
	}
	public int getDelay() {
		return m_delay;
	}
	public void setDelay(int m_delay) {
		this.m_delay = m_delay;
	}
	
	
}