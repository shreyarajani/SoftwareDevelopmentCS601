package cs601.graph;

public class Token {
	public int type;
	public String text;
	public Token(int type, String text) {
		this.type = type;
		this.text = text;
	}

	public Token(int type) {
		this.type = type;
	}

	public String toString() {
		if ( text==null ) {
			if ( type==Lexer.EOF ) {
				return "EOF";
			}
			return Lexer.tokenNames[type];
		}
		return text+"<"+Lexer.tokenNames[type]+">";
	}
}
