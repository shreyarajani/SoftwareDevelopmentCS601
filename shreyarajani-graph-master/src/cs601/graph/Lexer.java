package cs601.graph;

import java.io.*;

public class Lexer {
	public static final int EOF = -1;
	public static final int NODE = 1;
	public static final int ARROW = 2;
	public static final int NEWLINE = 3;
	public static final int KW_PRINT = 4;
	public static final int KW_NODES = 5;
	public static final int KW_LEN = 6;
	public static final int KW_REACH = 7;
	public static final int KW_ROOTS = 8;

	public static String[] tokenNames = {
		null,
		"NODE",
		"ARROW",
		"NEWLINE",
		"PRINT",
		"NODES",
		"LEN",
		"REACH"
	};

	/** Current char of lookahead */
	int c;

	/** Where to pull char from */
	Reader in;

	public Lexer(Reader in) throws IOException {
		this.in = in;
		consume();
	}

	public Token nextToken() throws IOException {
		while ( c!=EOF ) {
			if ( c==' ' || c=='\t' ) {
				consume();
				continue;
			}
			if ( isNodeNameChar(c) )
			{
				StringBuilder buf = new StringBuilder();
				while ( isNodeNameChar(c) ) {
					buf.append((char)c);
					consume();
				}
				String name = buf.toString();
				if ( name.equals("print") ) {
					return new Token(KW_PRINT, "print");
				}
				if ( name.equals("nodes") ) {
					return new Token(KW_NODES, "nodes");
				}
				if ( name.equals("len") ) {
					return new Token(KW_LEN, "len");
				}
				if ( name.equals("reach") ) {
					return new Token(KW_REACH, "reach");
				}
				if ( name.equals("roots") ) {
					return new Token(KW_ROOTS, "roots");
				}
				return new Token(NODE, name);
			}
			else if ( c=='-' ) {
				consume();
				if ( c=='>' ) {
					consume();
					return new Token(ARROW);
				}
			}
			else if ( c=='\n' || c=='\r' ) {
				consume();
				return new Token(NEWLINE);
			}
			throw new IllegalArgumentException("invalid char: "+(char)c);
		}
		return new Token(EOF);
	}

	protected boolean isNodeNameChar(int c) {
		return (c>='0' && c<='9') ||
			   (c>='a' && c<='z') ||
			   (c>='A' && c<='Z') ||
			   c=='_';
	}

	public void consume() throws IOException {
		c = in.read();
	}
}
