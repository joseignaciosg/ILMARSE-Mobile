package ilmarse.mobile.model.impl;

public class HtmlDesEncoder {

	public HtmlDesEncoder(){
		super();
	}
	
	
	public String encodeString(String s){
		if (s.contains("&#225;")){//a
			s = replace(s, "&#225;", "‡");
		}
		if (s.contains("&#233")){//e
			s = replace(s, "&#233","");

		}
		if (s.contains("&#237;")){//i
			s = replace(s, "&#237;","’");
		}
		if (s.contains("&#243;")){//o
			System.out.println("holas");
			s = replace(s, "&#243;","—");
		}
		if (s.contains("&#250;")){//u
			s = replace(s, "&#250;","œ");
		}
		if (s.contains("&#241;")){//u
			s = replace(s, "&#241;","–");
		}
		System.out.println(s);
		return s;
	}
	
	private  String replace(String str, String pattern, String replace) {
	    int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	        result.append(str.substring(s, e));
	        result.append(replace);
	        s = e+pattern.length();
	    }
	    result.append(str.substring(s));
	    return result.toString();
	}
}