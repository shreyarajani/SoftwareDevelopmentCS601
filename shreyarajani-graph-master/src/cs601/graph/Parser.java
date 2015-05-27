package cs601.graph;

import java.io.IOException;

public class Parser {
	Token token;
	Lexer in;
	protected StringBuffer buf = new StringBuffer();

	public Parser(Lexer in) {
		this.in = in;
		consume();
	}

	// prog : graph command*
	public String prog() {
		Graph g = graph();
		do {
			switch (token.type) {
				case Lexer.KW_PRINT :
					print(g);
					break;
				case Lexer.KW_NODES :
					nodes(g);
					break;
				case Lexer.KW_LEN :
					len(g);
					break;
				case Lexer.KW_REACH :
					reach(g);
					break;
				case Lexer.KW_ROOTS :
					roots(g);
					break;
				default :
					throw new IllegalArgumentException("unknown command: "+token);
			}
		} while (token.type!=Lexer.EOF );
		return buf.toString();
	}

	// print : 'print'
	public void print(Graph g) {
		match(Lexer.KW_PRINT);
		match(Lexer.NEWLINE);
		buf.append("Graph:");
		buf.append('\n');
		buf.append(g);
    }

	// nodes : 'nodes' start stop
	public void nodes(Graph g) {
		match(Lexer.KW_NODES);
		String start = token.text;
		match(Lexer.NODE);
		String stop = token.text;
		match(Lexer.NODE);
		match(Lexer.NEWLINE);
		buf.append("nodes "+start+" -> "+stop+" = "+g.getAllNodes(start,stop));
		buf.append('\n');
	}

	// len : 'len' start stop
	public void len(Graph g) {
		match(Lexer.KW_LEN);
		String start = token.text;
		match(Lexer.NODE);
		String stop = token.text;
		match(Lexer.NODE);
		match(Lexer.NEWLINE);
		buf.append("len "+start+" -> "+stop+" = "+g.getMinPathLength(start,stop));
		buf.append('\n');
	}

	// reach : 'reach' start
	public void reach(Graph g) {
		match(Lexer.KW_REACH);
		String start = token.text;
		match(Lexer.NODE);
		match(Lexer.NEWLINE);
		buf.append("reach "+start+" = "+g.getAllReachableNodes(start));
		buf.append('\n');
	}

	// roots : 'roots'
	public void roots(Graph g) {
		match(Lexer.KW_ROOTS);
		match(Lexer.NEWLINE);
		buf.append("roots = "+g.getRootNames());
		buf.append('\n');
	}

	// graph : ((edge|node) newline)*
	public Graph graph() {
		Graph<String,Node<String>> g = new UnweightedGraph<>();
		while ( token.type==Lexer.NODE ) {
			String name = token.text;
			Node<String> source = new NamedNode(name);
			source = g.addNode(source);
			consume(); // move beyond node name to -> or '\n'
			if ( token.type==Lexer.ARROW ) {
				consume(); // move beyond arrow to target
				String targetName = token.text;
				NamedNode target = new NamedNode(targetName);
				g.addNode(target);
				consume(); // move to newline
				source.addEdge(target);
			}
			match(Lexer.NEWLINE);
		}
		return g;
	}

	public void match(int type) {
		if ( token.type!=type ) {
			String expecting = "<EOF>";
			if ( type!=Lexer.EOF ) {
				expecting = Lexer.tokenNames[type];
			}
			String found = "<EOF>";
			if ( token.type!=Lexer.EOF ) {
				expecting = Lexer.tokenNames[token.type];
			}
			throw new IllegalArgumentException("expected "+expecting+
				" found "+found);
		}
		consume();
	}

	public void consume() {
		try {
			token = in.nextToken();
		}
		catch (IOException ioe) {
			System.err.println("io error reaching token");
		}
	}
}
