package bloomfilter;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitSetParser {

	private final String string;
	private int size = 1024;

	public BitSetParser(final String string) {
		this.string = string;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public BitSet parse() {
		BitSet bs = new BitSet(size);
		List<String> tokens = new ArrayList<String>();
		String str = "";
		for (int i = 0; i < string.length(); i++)
		{
			char ch = string.charAt(i);
			if (ch != '{' && ch != ',' && ch != '}' && ch != ' ')
				str += ch;
			else if (ch == ',')
			{
				tokens.add(str);
				str = "";
			}
		}
		
		int i;
		if (tokens.size() > 0) {
			for (String s : tokens) {
				s = s.trim();
				if (!s.isEmpty()) {
					i = Integer.parseInt(s);
					bs.set(i);
				} else {
					break;
				}
			}
		}
		return bs;
	}
}